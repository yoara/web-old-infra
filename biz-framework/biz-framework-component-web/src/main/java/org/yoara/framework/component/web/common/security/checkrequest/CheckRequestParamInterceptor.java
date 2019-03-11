package org.yoara.framework.component.web.common.security.checkrequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.yoara.framework.component.web.bean.JsonCommonCodeEnum;
import org.yoara.framework.component.web.common.security.checkrequest.annotation.CheckRequestParamAnnotation;
import org.yoara.framework.component.web.common.security.checkrequest.exception.CheckIllegalException;
import org.yoara.framework.component.web.common.security.checkrequest.policy.*;
import org.yoara.framework.component.web.interceptor.ZBaseInterceptorAdapter;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求输入校验拦截器
 * @author yoara
 */
public class CheckRequestParamInterceptor extends ZBaseInterceptorAdapter {
	//暂时先预留着，用于预先设置策略
	private HashMap<String,BaseCheckPolicy> policyMap;
	//替换处理方式是将替换的数据存在attribute中，增加一个通用前缀
	private boolean replaceInAttribute = true;
	public CheckRequestParamInterceptor(){
		policyMap = new HashMap<String,BaseCheckPolicy>();
	}

	public void setReplaceInAttribute(boolean replaceInAttribute) {
		this.replaceInAttribute = replaceInAttribute;
	}

	public final static String PREFIX = "REPLACE_OBJECT_";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
							 Object handler) throws Exception {
		//仅校验方法级别的Handler
		if(!(handler instanceof HandlerMethod)){
			return super.preHandle(request, response, handler);
		}
		AnnotationProperty ap = initAnnotation((HandlerMethod) handler);
		if(ap.getPolicy()==null){
			return super.preHandle(request, response, handler);
		}
		//设置输入
		String[] paramKeys = initParams(request, ap);
		if(paramKeys==null||paramKeys.length==0){
			return super.preHandle(request, response, handler);
		}
		//设置校验策略
		List<BaseCheckPolicy> policyList = initPolicys(ap);
		for(String paramKey:paramKeys){
			String paramValue = getString(paramKey,"");
			CheckResult result = doCheck(paramValue,policyList);
			//判断不符合过滤条件
			if(!result.isValid()){
				switch (ap.getDealType()){
					case REPLACE:
						doDealReplace(request, paramKey, result);
						break;
					case JSON:
						ResponseBody body = new ResponseBody();
						body.setCode(JsonCommonCodeEnum.E0006);
						printJsonMsg(response, body);
						return false;
					case ALERT:
						doDealAlert(response,JsonCommonCodeEnum.E0006.getMessage());
						return false;
					case EXCEPTION:
						throw new CheckIllegalException("Illegal Input Parameter");
				}
			}
		}
		return super.preHandle(request, response, handler);
	}

	/** 替换数据 **/
	private void doDealReplace(HttpServletRequest request, String paramKey, CheckResult result) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
		if(replaceInAttribute){
			request.setAttribute(
					PREFIX + paramKey, result.getReplaceObject());
		}else{
			try{
				ServletRequest checkRequest =
						((HttpServletRequestWrapper)request).getRequest();
				Class clazz = checkRequest.getClass();
				String clazzName = clazz.getName().toLowerCase();
				Field field = null;
				//针对不同的应用服务器获取不同的参数项
				if(clazzName.contains("jetty")){
					//jetty
					//org.mortbay.jetty.Request
					field = clazz.getDeclaredField("_parameters");
					field.setAccessible(true);
					Map requestMap = (Map)field.get(checkRequest);
					requestMap.put(paramKey, result.getReplaceObject());
					BeanUtils.setProperty(checkRequest, "_parameters", requestMap);
				}else if(clazzName.contains("caucho")){
					//resin
					//com.caucho.server.http.HttpServletRequestImpl
					field = clazz.getDeclaredField("_filledForm");
					field.setAccessible(true);
					Map requestMap = (Map)field.get(checkRequest);
					requestMap.put(paramKey, result.getReplaceObject());
					BeanUtils.setProperty(checkRequest, "_filledForm", requestMap);
				}else if(clazzName.contains("catalina")){
					//tomcat
					//org.apache.catalina.connector.RequestFacade
					//org.apache.catalina.connector.Request
					//org.apache.coyote.Request coyoteRequest
					//tomcat封装方式，需读取一次才会加载
					checkRequest.getParameterMap();
					clazz = Class.forName("org.apache.catalina.connector.RequestFacade");
					field = clazz.getDeclaredField("request");
					field.setAccessible(true);
					HttpServletRequest squest = (HttpServletRequest)field.get(checkRequest);
					clazz = Class.forName("org.apache.catalina.connector.Request");
					field = clazz.getDeclaredField("parameterMap");
					field.setAccessible(true);
					Map requestMap = (Map)field.get(squest);
					BeanUtils.setProperty(requestMap, "locked", false);
					requestMap.put(paramKey, result.getReplaceObject());
				}
			}catch(Exception e){
				request.setAttribute(
						PREFIX + paramKey, result.getReplaceObject());
			}
		}
	}

	/**设置注解值**/
	private AnnotationProperty initAnnotation(HandlerMethod handler) {
//		AnnotationProperty ap = new AnnotationProperty();
//		CheckRequestParamAnnotation annotation
//				= getAnnotation(handler,CheckRequestParamAnnotation.class);
//		ap.setAnnotationProperty(annotation);
//		return ap;
		AnnotationProperty ap = new AnnotationProperty();

		ap.setCheckParams(new String[]{});
		ap.setDealType(CheckRequestParamAnnotation.DealType.REPLACE);
		ap.setPolicy(new CheckRequestParamAnnotation.Policy[]{
				CheckRequestParamAnnotation.Policy.TAG,
				CheckRequestParamAnnotation.Policy.SQL});
		return ap;
	}

	/**判断过滤**/
	private CheckResult doCheck(String paramValue, List<BaseCheckPolicy> policyList) {
		CheckResult result = new CheckResult();
		result.setCheckObject(paramValue);
		if(!StringUtils.isEmpty(paramValue)){
			for(BaseCheckPolicy policy:policyList){
				if(policy instanceof SensitiveCheckPolicy){
					//result.setHandler(wordIndexService);
				}
				policy.doCheck(result);
				//已经检测到，则break返回
				if(!result.isValid()){
					break;
				}
			}
		}
		return result;
	}

	/**设置输入**/
	private String[] initParams(HttpServletRequest request,
								AnnotationProperty ap) {
		String[] params = ap.getCheckParams();
		if(params.length==0){
			//设置全部输入
			params = (String[])request.getParameterMap().keySet().toArray(new String[]{});
		}
		return params;
	}
	/**设置校验策略**/
	private List<BaseCheckPolicy> initPolicys(AnnotationProperty ap)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		CheckRequestParamAnnotation.Policy[] policys = ap.getPolicy();
		List<BaseCheckPolicy> policyList = new ArrayList<BaseCheckPolicy>();
		for(CheckRequestParamAnnotation.Policy policy:policys){
			switch (policy){
				case TAG:
					policyList.add(new TagCheckPolicy());
					break;
				case SQL:
					policyList.add(new SQLCheckPolicy());
					break;
				case SENSITIVE:
					policyList.add(new SensitiveCheckPolicy());
					break;
				case CUSTOM:
					String[] customPolicys = ap.getCustomPolicys();
					if(customPolicys!=null){
						for(String custom:customPolicys){
							policyList.add(
									(BaseCheckPolicy) Class.forName(custom).newInstance());
						}
					}
			}
		}
		return policyList;
	}

	/**注解属性对象**/
	class AnnotationProperty{
		/**默认不校验任何策略**/
		private CheckRequestParamAnnotation.Policy[] policy;
		/**自定义策略**/
		private String[] customPolicys;
		/**配置需要校验的输入参数，参数若为空数组表示全部匹配*/
		private String[] checkParams;
		/**返回策略**/
		private CheckRequestParamAnnotation.DealType dealType;

		public CheckRequestParamAnnotation.DealType getDealType() {
			return dealType;
		}

		public String[] getCustomPolicys() {
			return customPolicys;
		}

		public CheckRequestParamAnnotation.Policy[] getPolicy() {
			return policy;
		}

		public String[] getCheckParams() {
			return checkParams;
		}

		public void setPolicy(CheckRequestParamAnnotation.Policy[] policy) {
			this.policy = policy;
		}

		public void setCustomPolicys(String[] customPolicys) {
			this.customPolicys = customPolicys;
		}

		public void setCheckParams(String[] checkParams) {
			this.checkParams = checkParams;
		}

		public void setDealType(CheckRequestParamAnnotation.DealType dealType) {
			this.dealType = dealType;
		}

		public void setAnnotationProperty(CheckRequestParamAnnotation annotation){
			if(annotation==null){
				return;
			}
			if(annotation.policy()!=null){
				policy = annotation.policy();
			}
			if(annotation.customPolicys()!=null){
				customPolicys = annotation.customPolicys();
			}
			if(annotation.checkParams()!=null){
				checkParams = annotation.checkParams();
			}
			if(annotation.dealType()!=null){
				dealType = annotation.dealType();
			}
		}
	}
}
package org.yoara.framework.component.web.common.security.csrftoken;

import org.apache.commons.lang.StringUtils;
import org.yoara.framework.component.web.bean.JsonCommonCodeEnum;
import org.yoara.framework.component.web.cache.BridgeCache;
import org.yoara.framework.component.web.cache.CacheConstants;
import org.yoara.framework.component.web.common.security.csrftoken.annotation.CheckCSRFTokenAnnotation;
import org.yoara.framework.component.web.common.security.csrftoken.annotation.InitCSRFTokenAnnotation;
import org.yoara.framework.component.web.common.security.csrftoken.exception.CSRFTokenException;
import org.yoara.framework.component.web.interceptor.ZBaseInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 用于校验CSRF跨站请求伪造的token<br>
 * 标记了注解{@link CheckCSRFTokenAnnotation}  的展示层方法，在执行方法前会判断token<br>
 * @author yoara
 */
public class CSRFTokenInterceptor extends ZBaseInterceptorAdapter {
	//token的key值
	public final static String CSRF_TOKEN_INTE = "CSRF_TOKEN_INTE";
	/**token最大存活时间，单位为秒**/
	public final static int CSRF_TOKEN_EXPIRE = 60*2;

	@Resource
	private BridgeCache bridgeCache;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
							 Object handler) throws Exception {
		checkInitToken(request, handler);

		return checkCSRFToken(request, response, handler);
	}
	/** 检查是否需要初始化CSRF token **/
	private void checkInitToken(HttpServletRequest request, Object handler) {
		InitCSRFTokenAnnotation initAnnotation = getAnnotation(handler,InitCSRFTokenAnnotation.class);
		if(initAnnotation!=null){
			int expire = initAnnotation.expire();
			if(expire>CSRF_TOKEN_EXPIRE){
				expire = CSRF_TOKEN_EXPIRE;
			}
			String token = UUID.randomUUID().toString();
			//以token为key，表示该key存在即可
			bridgeCache.put(CacheConstants.CACHE_CSRF_TOKEN,token,true,expire);
			//将token放入返回值中
			request.setAttribute(CSRF_TOKEN_INTE,token);
		}
	}
	/** 检查是否需要校验CSRF token **/
	private boolean checkCSRFToken(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		CheckCSRFTokenAnnotation annotation = getAnnotation(handler,CheckCSRFTokenAnnotation.class);
		if(annotation!=null){
			//存在注解标记，必须进行token校验
			String token = getString(CSRF_TOKEN_INTE);
			if(StringUtils.isEmpty(token)){		//请求未带token
				return refused(response,annotation);
			}
			long ttl = bridgeCache.ttl(CacheConstants.CACHE_CSRF_TOKEN,token);
			if(ttl>0){	//token有效允许处理
				//是否需要使token失效
				if(annotation.makeTokenInvalid()){
					bridgeCache.remove(CacheConstants.CACHE_CSRF_TOKEN,token);
				}
				//执行接下来的逻辑
				return super.preHandle(request, response, handler);
			}else{	//token已失效
				return refused(response,annotation);
			}
		}
		return super.preHandle(request, response, handler);
	}
	/**校验不通过行为**/
	private boolean refused(HttpServletResponse response,CheckCSRFTokenAnnotation annotation) throws Exception {
		switch (annotation.dealType()){
			case JSON:
				ResponseBody body = new ResponseBody();
				body.setCode(JsonCommonCodeEnum.E0009);
				printJsonMsg(response, body);
				return false;
			case ALERT:
				doDealAlert(response,JsonCommonCodeEnum.E0009.getMessage());
				return false;
			default:
				throw new CSRFTokenException("CSRFToken invalid");
		}
	}
}
package org.yoara.framework.component.web.common.security.encrypt;

import org.yoara.framework.component.web.bean.JsonCommonCodeEnum;
import org.yoara.framework.component.web.common.security.encrypt.annotation.CheckRSAEncryptAnnotation;
import org.yoara.framework.component.web.common.security.encrypt.annotation.InitRSAEncryptAnnotation;
import org.yoara.framework.component.web.common.security.encrypt.exception.RSAEncryptException;
import org.yoara.framework.component.web.common.security.encrypt.helper.RSAEncryptHelper;
import org.yoara.framework.component.web.interceptor.ZBaseInterceptorAdapter;
import org.yoara.framework.core.util.encrypt.RSAUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;

/**
 * 用于校验RSA 公钥<br>
 * 标记了注解{@link CheckRSAEncryptAnnotation}  的展示层方法，在执行方法前会判断加密信息<br>
 * @author yoara
 */
public class RSAEncryptInterceptor extends ZBaseInterceptorAdapter {
	/**token最大存活时间，单位为秒**/
	public final static int ENCRYPT_TOKEN_EXPIRE = 60*30;
	@Resource
	protected RSAEncryptHelper rsaEncryptHelper;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
							 Object handler) throws Exception {
		checkInitRSAEncrypt(request, handler);

		return checkRSAEncrypt(request, response, handler);
	}
	/** 检查是否需要初始化RSA 公钥 **/
	private void checkInitRSAEncrypt(HttpServletRequest request, Object handler) {
		InitRSAEncryptAnnotation initAnnotation = getAnnotation(handler,InitRSAEncryptAnnotation.class);
		if(initAnnotation!=null){
			KeyPair keyPair = RSAUtil.makeKeyPair();
			// 公钥
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

			String key = rsaEncryptHelper.putRSAPriveteKey(keyPair,initAnnotation.expire());

			request.setAttribute(RSAEncryptHelper.RSA_PARAM_KEY,key);
			request.setAttribute(RSAEncryptHelper.RSA_PUBLIC_MODULES,publicKey.getModulus().toString(16));
			request.setAttribute(RSAEncryptHelper.RSA_PUBLIC_EXPONENT,publicKey.getPublicExponent().toString(16));
		}
	}
	/** 检查是否需要校验RSA 公钥 **/
	private boolean checkRSAEncrypt(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		CheckRSAEncryptAnnotation annotation = getAnnotation(handler,CheckRSAEncryptAnnotation.class);
		if(annotation!=null){
			String sign = getString(RSAEncryptHelper.RSA_PARAM_ENCRYPTSTR);
			boolean onlyMD5 = getBoolean(RSAEncryptHelper.RSA_PARAM_ONLYMD5,false);
			String key = getString(RSAEncryptHelper.RSA_PARAM_KEY);
			if(!EncryptCheckResult.MATCHED.equals(
					rsaEncryptHelper.rsaCheck(sign,rsaEncryptHelper.makeCheckMap(request),key,onlyMD5))){
				refused(response,annotation);
			}
		}
		return super.preHandle(request, response, handler);
	}
	/**校验不通过行为**/
	private boolean refused(HttpServletResponse response,CheckRSAEncryptAnnotation annotation) throws Exception {
		switch (annotation.dealType()){
			case JSON:
				ResponseBody body = new ResponseBody();
				body.setCode(JsonCommonCodeEnum.E0010);
				printJsonMsg(response, body);
				return false;
			case ALERT:
				doDealAlert(response,JsonCommonCodeEnum.E0010.getMessage());
				return false;
			default:
				throw new RSAEncryptException("RSA encrypt invalid");
		}
	}
}
package org.yoara.framework.component.web.common.security.encrypt.annotation;


import org.yoara.framework.component.web.common.security.encrypt.RSAEncryptInterceptor;

import java.lang.annotation.*;

/**
 * 初始化RSA 公钥枚举，标注了该枚举的类或方法，在拦截器中，方法执行完毕后将生成RSA 公钥信息并存储至response中
 * {@link RSAEncryptHelper#RSA_PARAM_KEY}
 * {@link RSAEncryptHelper#RSA_PUBLIC_EXPONENT}
 * {@link RSAEncryptHelper#RSA_PUBLIC_MODULES}
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InitRSAEncryptAnnotation {
    /**公钥过期时间，单位为秒，默认{@link RSAEncryptInterceptor#ENCRYPT_TOKEN_EXPIRE}秒。**/
    int expire() default RSAEncryptInterceptor.ENCRYPT_TOKEN_EXPIRE;

}

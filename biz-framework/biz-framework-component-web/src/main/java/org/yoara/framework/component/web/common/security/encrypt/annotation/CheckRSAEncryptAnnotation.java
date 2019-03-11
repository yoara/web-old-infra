package org.yoara.framework.component.web.common.security.encrypt.annotation;

import java.lang.annotation.*;

/**
 * 标注校验RSA 公钥，标注了该枚举的类或方法，在拦截器中，方法执行时将校验RSA 公钥
 *
 * <p>处理方式</p>
 * <ui>
 *     <li>·返回错误提示 {@link CheckRSAEncryptAnnotation.DealType#ALERT}</li>
 *     <li>·返回json信息 {@link CheckRSAEncryptAnnotation.DealType#JSON}</li>
 *     <li>·抛出异常 {@link CheckRSAEncryptAnnotation.DealType#EXCEPTION}</li>
 * </ui>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckRSAEncryptAnnotation {
    /** 处理方式 **/
    DealType dealType() default DealType.JSON;

    enum DealType {ALERT,JSON,EXCEPTION}
}

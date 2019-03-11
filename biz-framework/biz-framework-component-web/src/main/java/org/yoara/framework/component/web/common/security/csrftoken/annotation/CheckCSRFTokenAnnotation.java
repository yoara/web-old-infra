package org.yoara.framework.component.web.common.security.csrftoken.annotation;

import java.lang.annotation.*;

/**
 * 标注校验token，标注了该枚举的类或方法，在拦截器中，方法执行时将校验token
 *
 * <p>处理方式</p>
 * <ui>
 *     <li>·返回错误提示 {@link CheckCSRFTokenAnnotation.DealType#ALERT}</li>
 *     <li>·返回json信息 {@link CheckCSRFTokenAnnotation.DealType#JSON}</li>
 *     <li>·抛出异常 {@link CheckCSRFTokenAnnotation.DealType#EXCEPTION}</li>
 * </ui>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckCSRFTokenAnnotation {
    /** 处理方式 **/
    DealType dealType() default DealType.JSON;
    /**token使用后是否直接失效**/
    boolean makeTokenInvalid() default true;

    enum DealType {ALERT,JSON,EXCEPTION}
}

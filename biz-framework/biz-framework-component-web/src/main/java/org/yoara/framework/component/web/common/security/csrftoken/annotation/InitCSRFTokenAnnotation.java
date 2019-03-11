package org.yoara.framework.component.web.common.security.csrftoken.annotation;


import org.yoara.framework.component.web.common.security.csrftoken.CSRFTokenInterceptor;

import java.lang.annotation.*;

/**
 * 初始化token枚举，标注了该枚举的类或方法，在拦截器中，方法执行完毕后将生成一次性的token并存储至response中
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InitCSRFTokenAnnotation{
    /**token过期时间，单位为秒，默认{@link CSRFTokenInterceptor#CSRF_TOKEN_EXPIRE}秒**/
    int expire() default CSRFTokenInterceptor.CSRF_TOKEN_EXPIRE;

}

package com.company.seed.module.manager.model.annotation;

import com.company.seed.module.manager.model.enums.AuthorityAnnotationEnums;

import java.lang.annotation.*;

/**
 * Created by yoara on 2016/4/25.
 * 权限设定注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthorityAnnotation {
    AuthorityAnnotationEnums value();
}

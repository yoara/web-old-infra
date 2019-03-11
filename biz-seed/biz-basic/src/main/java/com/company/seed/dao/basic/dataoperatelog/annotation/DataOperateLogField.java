/**
 * 
 */
package com.company.seed.dao.basic.dataoperatelog.annotation;

import java.lang.annotation.*;

/**
 * 操作字段记录注解
 * Created by yoara on 2017/01/06.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataOperateLogField {
	String desc() default "";
	String field() default "";
}

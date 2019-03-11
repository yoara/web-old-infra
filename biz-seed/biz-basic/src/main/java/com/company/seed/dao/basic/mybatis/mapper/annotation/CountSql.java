package com.company.seed.dao.basic.mybatis.mapper.annotation;

import java.lang.annotation.*;

/**
 * 用于自动映射分页组件，可自行定义查询页总量
 * Created by yoara on 2017/01/06.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CountSql {
	/**
	 * 查询页总量的方法
     */
	String value() default "";
}

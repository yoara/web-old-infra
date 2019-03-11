package com.company.seed.dao.basic.dataoperatelog.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
/**
 * 操作实体注解
 * Created by yoara on 2017/01/06.
 */
public @interface DataOperateLogEntity {
	/**
	 * 日志存储的表
	 */
	String table() default "";

	/**
	 * 数据更改时查询历史对象方法名称
	 * <p>（方法有以String为参数的获取对象的记录存在于DAO类中）</p>
	 */
	String method();

	/**
	 * 获取存储ID 
	 */
	String id() default "id";

	/**
	 * 获取存储ID类型
	 */
	Class idType() default String.class;

	/**
	 *  描述
	 */
	String desc() default "";
}

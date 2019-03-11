package com.company.seed.dao.basic.dataoperatelog.annotation;

import com.company.seed.dao.basic.dataoperatelog.enums.DataLoggerTypeEnum;
import com.company.seed.dao.basic.dataoperatelog.enums.DataOperateTypeEnum;
import com.company.seed.model.Entity;

import java.lang.annotation.*;

/**
 * 操作日志方法注解
 * Created by yoara on 2017/01/06.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataOperateLogMethod {
	/**
	 * 方法类型
	 * @see {@link DataOperateTypeEnum}
     */
	DataOperateTypeEnum value();

	/**
	 * 待操作参数在参数列表中的下标位置
     */
	int index() default 0;

	/**
	 * 待操作数据的类型
	 */
	Class operateEntity() default Entity.class;

	/**
	 * 忽略null值
	 */
	boolean ignoreNull() default true;

	/**
	 * 是否异步记录
	 */
	boolean async() default true;
	/**
	 * 日志类型
	 */
	DataLoggerTypeEnum loggerType() default DataLoggerTypeEnum.NORMAL;

}

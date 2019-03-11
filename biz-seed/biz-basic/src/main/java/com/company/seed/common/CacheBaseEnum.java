package com.company.seed.common;

/**
 * 缓存枚举定义
 * Created by yoara on 2015/12/21.
 */
public enum CacheBaseEnum {
	USERINFO,
	/** 基础数据缓存 **/
	BASIC_DATA,

	/** web用的一些注解 **/
	FRAMEWORK_WEB,

	/** 分布式锁 **/
	REDIS_LOCK,
	/** 缓存注解相关 **/
	REDIS_CACHED
	;

}

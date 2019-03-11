package com.company.seed.common;

/**
 * 缓存类型,
 * 自己添加模块的缓存类型{@link CacheTypeEnum}
 * Created by yoara on 2015/12/21.
 */
public enum CacheTypeEnum {
	USERINFO(CacheBaseEnum.USERINFO),

	/** 分布式锁 **/
	REDIS_LOCK(CacheBaseEnum.REDIS_LOCK),
	/** 缓存注解相关 **/
	REDIS_CACHED_USERDEMO(CacheBaseEnum.REDIS_CACHED),


	/** framework-web 用 **/
	SESSION(CacheBaseEnum.FRAMEWORK_WEB),
	CACHE_CSRF_TOKEN(CacheBaseEnum.FRAMEWORK_WEB),
	CACHE_ENCRYPT_RSA(CacheBaseEnum.FRAMEWORK_WEB),
	CACHE_ENCRYPT_AES(CacheBaseEnum.FRAMEWORK_WEB),
	/** framework-web 用END **/

	;
	private CacheBaseEnum cacheBaseType;
	
	CacheTypeEnum(CacheBaseEnum cacheBaseType) {
		this.cacheBaseType = cacheBaseType;
	}

	public CacheBaseEnum getCacheBaseType() {
		return cacheBaseType;
	}
}

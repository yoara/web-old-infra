package com.company.seed.basic.web.cache;

import com.company.seed.cache.RedisCache;
import com.company.seed.common.CacheTypeEnum;
import org.springframework.stereotype.Service;
import org.yoara.framework.component.web.cache.BridgeCache;

import javax.annotation.Resource;

/**
 * 此类的目的是用于framework-web的分布式缓存桥接
 * Created by yoara on 2017/3/30.
 */
@Service("bridgeCache")
public class BridgeCacheImpl implements BridgeCache {
    @Resource
    private RedisCache redisCache;

    @Override
    public void put(String type, String key, Object value, int expire) {
        redisCache.put(CacheTypeEnum.valueOf(type),key,value,expire);
    }

    @Override
    public void remove(String type, String key) {
        redisCache.remove(CacheTypeEnum.valueOf(type),key);
    }

    @Override
    public Object get(String type, String key) {
        return redisCache.get(CacheTypeEnum.valueOf(type),key);
    }

    @Override
    public long ttl(String type, String key) {
        return redisCache.ttl(CacheTypeEnum.valueOf(type),key);
    }

    @Override
    public void expire(String type, String key, int expiryTime) {
        redisCache.expire(CacheTypeEnum.valueOf(type),key,expiryTime);
    }
}

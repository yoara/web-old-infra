package com.company.seed.cache;

import com.company.seed.common.CacheTypeEnum;

import java.util.List;
import java.util.Map;

/**
 * Cache接口
 * Created by yoara on 2016/3/3.
 */
public interface RedisCache {
	/**
	 * Get an item from the cache, nontransactionally
	 * 
	 * @param key
	 * @return the cached object or <tt>null</tt>
	 */
	public Object get(CacheTypeEnum cacheTypeEnum, String key);
	public List<Object> getAll(CacheTypeEnum cacheTypeEnum);
	public Object getHash(CacheTypeEnum cacheTypeEnum, String key, String hashKey);
	public Map<Object, Object> getHashAll(CacheTypeEnum cacheTypeEnum, String key);
	public List<Object> multiGet(CacheTypeEnum cacheTypeEnum, List<String> keys);
	/**
	 * Add an item to the cache, nontransactionally, with failfast semantics
	 * 
	 * @param key
	 * @param value
	 */
	public void put(CacheTypeEnum cacheTypeEnum, String key, Object value);

	public void put(CacheTypeEnum cacheTypeEnum, String key, Object value,
					int second);

	/**
	 * Add an item to the cache
	 * 
	 * @param key
	 * @param value
	 */
	public void update(CacheTypeEnum cacheTypeEnum, String key, Object value);

	/**
	 * Remove an item from the cache
	 */
	public void remove(CacheTypeEnum cacheTypeEnum, String key);
	
	/**
	 * Remove all items has same cache
	 */
	public void removeAll(CacheTypeEnum cacheTypeEnum);
	public void removeAll(CacheTypeEnum cacheTypeEnum,String key);

	/**
	 * Clear the cache
	 */
	public void clear();

	public void expire(CacheTypeEnum cacheTypeEnum, String id, int timeout);

	public long getExpire(CacheTypeEnum cacheTypeEnum, String id);

	public void putToday(CacheTypeEnum cacheTypeEnum, String key, Object value);
	public void putHashToday(CacheTypeEnum cacheTypeEnum,
							 String key, String hashKey, Object value);
	public void putHash(CacheTypeEnum cacheTypeEnum,
						String key, String hashKey, Object value);
	public void putHash(CacheTypeEnum cacheTypeEnum,
						String key, String hashKey, Object value, int second);
	/**
	 * 判断指定的键是否存在
	 * @authod yoara 2015-2-10
	 */
	public boolean persist(CacheTypeEnum cacheTypeEnum, String key);
	/**
	 * 判断指定的键剩余时间
	 * @authod yoara 2015-2-15
	 */
	public long ttl(CacheTypeEnum cacheTypeEnum, String key);
	/**
	 * 自增方法
	 * @authod yoara 2015-2-10
	 */
	public long incr(CacheTypeEnum cacheTypeEnum, String key);
	public long incr(CacheTypeEnum cacheTypeEnum, String key, int second);
	/**
	 * 自增方法
	 * @authod yoara 2015-2-10
	 */
	public long incr(CacheTypeEnum cacheTypeEnum, String key, long incrValue);
	public long incr(CacheTypeEnum cacheTypeEnum, String key, long incrValue, int second);
	/**
	 * 由于自增的key在通过spring-redis-data插入到redis中后序列化的方式发生变化，
	 * 必须用这种方式获得值，而不能通过{@link RedisCache#get(CacheTypeEnum, String)}获得
	 * @authod yoara 2015-2-10
	 */
	public long getIncr(CacheTypeEnum cacheTypeEnum, String key);
	/**
	 * 获得当天剩下的秒数
	 * @authod yoara 2015-2-15
	 **/
	public int getLeftSeconds();
	/**
	 * 重置并返回旧值
	 */
	public Object getAndSet(CacheTypeEnum cacheTypeEnum,
							String key, Object value);
}
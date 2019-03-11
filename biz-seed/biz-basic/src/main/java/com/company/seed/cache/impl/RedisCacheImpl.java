package com.company.seed.cache.impl;

import com.company.seed.cache.RedisCache;
import com.company.seed.common.CacheTypeEnum;
import com.company.seed.common.ContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis缓存实现类
 * Created by yoara on 2016/3/3.
 */
public class RedisCacheImpl implements RedisCache {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private Map<String,RedisTemplate<String, Object>> redis;

	protected RedisTemplate<String, Object> getRedisTemplate(Type type) {
		return redis.get(type.toString());
	}
	@Override
	public Object get(CacheTypeEnum cacheTypeEnum,String key) {
		key = wrapKey(cacheTypeEnum,key);
		try {
			return getRedisTemplate(cacheTypeEnum).opsForValue().get(key);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}

	@Override
	public List<Object> getAll(CacheTypeEnum cacheTypeEnum) {
		Set<String> keySet = null;
		try {
			keySet = getRedisTemplate(cacheTypeEnum).keys("*"+ wrapKey(cacheTypeEnum,"") +"*");
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		if (keySet != null) {
			Iterator<String> it = keySet.iterator();
			List<Object> resultList = new ArrayList<>();
			while ( it.hasNext() ) {
				String key = it.next();
				try {
					resultList.add(getRedisTemplate(cacheTypeEnum).opsForValue().get(key));
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
			}
			return resultList;
		}
		return null;
	}

	@Override
	public List<Object> multiGet(CacheTypeEnum cacheTypeEnum,List<String> keys) {
		if(keys==null){
			return null;
		}
		List<String> wrapKeys = new ArrayList<String>(keys.size());
		for(String key:keys){
			wrapKeys.add(wrapKey(cacheTypeEnum,key));
		}
		try {
			return getRedisTemplate(cacheTypeEnum).opsForValue().multiGet(wrapKeys);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Object getHash(CacheTypeEnum cacheTypeEnum,String key, String hashKey) {
		key = wrapKey(cacheTypeEnum,key);
		try {
			return getRedisTemplate(cacheTypeEnum).opsForHash().get(key, hashKey);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	@Override
	public Map<Object, Object> getHashAll(CacheTypeEnum cacheTypeEnum,String key) {
		key = wrapKey(cacheTypeEnum,key);
		try {
			return getRedisTemplate(cacheTypeEnum).opsForHash().entries(key);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	@Override
	public void put(CacheTypeEnum cacheTypeEnum,String key, Object value) {
		putToday(cacheTypeEnum, key, value);
	}

	@Override
	public void putToday(CacheTypeEnum cacheTypeEnum,String key, Object value) {
		try {
			put(cacheTypeEnum, key, value, getLeftSeconds());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void putHashToday(CacheTypeEnum cacheTypeEnum, String key,
							 String hashKey, Object value) {
		putHash(cacheTypeEnum,key,hashKey,value,getLeftSeconds());
	}
	@Override
	public void putHash(CacheTypeEnum cacheTypeEnum, String key,
						String hashKey, Object value) {
		putHashToday(cacheTypeEnum, key, hashKey, value);
	}
	@Override
	public void putHash(CacheTypeEnum cacheTypeEnum, String key,
						String hashKey, Object value,int second) {
		String key_here = wrapKey(cacheTypeEnum, key);
		try {
			getRedisTemplate(cacheTypeEnum, Type.SAVE).opsForHash().put(key_here, hashKey, value);
			expire(cacheTypeEnum, key, second);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	/**获得当天剩下的秒数**/
	public int getLeftSeconds(){
		Calendar now = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		return Long.valueOf(
				(calendar.getTime().getTime() - now.getTime().getTime()) / 1000)
				.intValue();
	}

	@Override
	public void update(CacheTypeEnum cacheTypeEnum,String key, Object value) {
		key = wrapKey(cacheTypeEnum,key);
		try {
			getRedisTemplate(cacheTypeEnum, Type.SAVE).opsForValue().set(key, value);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void put(CacheTypeEnum cacheTypeEnum,String key, Object value, int second) {
		key = wrapKey(cacheTypeEnum,key);
		try {
			getRedisTemplate(cacheTypeEnum, Type.SAVE).opsForValue().set(key, value, second, TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void remove(CacheTypeEnum cacheTypeEnum,String key) {
		key = wrapKey(cacheTypeEnum,key);
		try {
			getRedisTemplate(cacheTypeEnum, Type.SAVE).delete(key);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void removeAll(CacheTypeEnum cacheTypeEnum) {
		removeAll(cacheTypeEnum,"");
	}

	@Override
	public void removeAll(CacheTypeEnum cacheTypeEnum, String key) {
		String cacheKey = wrapKey(cacheTypeEnum,key);
		Set<String> keySet = null;
		try {
			keySet = getRedisTemplate(cacheTypeEnum, Type.QUERY).keys("*"+ cacheKey +"*");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		if (keySet != null) {
			Iterator<String> it = keySet.iterator();
			while ( it.hasNext() ) {
				String hereKey = it.next();
				try {
					getRedisTemplate(cacheTypeEnum, Type.SAVE).delete(hereKey);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		}
	}

	@Override
	public void clear() {
		return;
	}

	/**
	 * 返回redis模板对象
	 *
	 * @return
	 */
	private RedisTemplate<String, Object> getRedisTemplate(CacheTypeEnum cacheTypeEnum) {
		return getRedisTemplate(cacheTypeEnum, Type.QUERY);
	}

	/**
	 * 返回redis模板对象
	 * @return
	 */
	private RedisTemplate<String, Object> getRedisTemplate(CacheTypeEnum cacheTypeEnum,Type type) {
		//留待扩展
		return getRedisTemplate(type);
	}

	/**
	 * 缓存类型
	 *
	 */
	public enum Type {
		QUERY, SAVE
	}

	private String wrapKey(CacheTypeEnum cacheTypeEnum, String key) {
		if (cacheTypeEnum == CacheTypeEnum.SESSION) {
			return key;
		}
		StringBuilder keyBuilder = new StringBuilder();
		keyBuilder.append(ContextHolder.getDataSource().name())
				.append(":").append(cacheTypeEnum)
				.append(":").append(key);
		return keyBuilder.toString();
	}

	@Override
	public void expire(CacheTypeEnum cacheTypeEnum,String key, int timeout) {
		key = wrapKey(cacheTypeEnum,key);
		try {
			getRedisTemplate(cacheTypeEnum, Type.SAVE).expire(key, timeout, TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public long getExpire(CacheTypeEnum cacheTypeEnum,String key) {
		key = wrapKey(cacheTypeEnum,key);
		try {
			return getRedisTemplate(cacheTypeEnum).getExpire(key);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return 0L;
	}

	@Override
	public long incr(CacheTypeEnum cacheTypeEnum, String key) {
		String key_here = wrapKey(cacheTypeEnum,key);
		try {
			long count = getRedisTemplate(Type.SAVE).opsForValue().increment(key_here, new Long(1));
			return count;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return 0L;
	}
	@Override
	public long incr(CacheTypeEnum cacheTypeEnum, String key,int second) {
		String key_here = wrapKey(cacheTypeEnum,key);
		try {
			long count = getRedisTemplate(Type.SAVE).opsForValue().increment(key_here, new Long(1));
			expire(cacheTypeEnum, key,second);
			return count;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return 0L;
	}
	@Override
	public long incr(CacheTypeEnum cacheTypeEnum, String key,long incrValue) {
		String key_here = wrapKey(cacheTypeEnum,key);
		try {
			long count = getRedisTemplate(Type.SAVE).opsForValue().increment(key_here, incrValue);
			return count;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return 0L;
	}
	@Override
	public long incr(CacheTypeEnum cacheTypeEnum, String key,long incrValue,int second) {
		String key_here = wrapKey(cacheTypeEnum,key);
		try {
			long count = getRedisTemplate(Type.SAVE).opsForValue().increment(key_here, incrValue);
			expire(cacheTypeEnum, key, second);
			return count;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return 0L;
	}
	@Override
	public long getIncr(CacheTypeEnum cacheTypeEnum, String key) {
		key = wrapKey(cacheTypeEnum,key);
		try {
			RedisAtomicLong entityIdCounter = new RedisAtomicLong(key,
					getRedisTemplate(Type.SAVE).getConnectionFactory());
			return entityIdCounter.longValue();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return 0L;
	}

	@Override
	public boolean persist(CacheTypeEnum cacheTypeEnum, String key) {
		key = wrapKey(cacheTypeEnum,key);
		try {
			return getRedisTemplate(Type.QUERY).persist(key);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	@Override
	public long ttl(CacheTypeEnum cacheTypeEnum, String key) {
		key = wrapKey(cacheTypeEnum,key);
		try {
			return getRedisTemplate(Type.QUERY).getExpire(key);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return 0L;
	}

	public void setRedis(Map<String, RedisTemplate<String, Object>> redis) {
		this.redis = redis;
	}

	public Map<String,RedisTemplate<String, Object>> getRedis() {
		return redis;
	}

	@Override
	public Object getAndSet(CacheTypeEnum cacheTypeEnum, String key,
							Object value) {
		key = wrapKey(cacheTypeEnum,key);
		try {
			return getRedisTemplate(Type.SAVE).opsForValue().getAndSet(key, value);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}
}

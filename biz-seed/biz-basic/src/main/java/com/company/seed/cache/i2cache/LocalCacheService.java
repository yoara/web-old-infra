package com.company.seed.cache.i2cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * 本地二级缓存
 * Created by yoara on 2016/3/3.
 */
public class LocalCacheService {
	private final static Logger logger = LoggerFactory.getLogger(LocalCacheService.class);

	private static LocalCacheService instance = null;
	private static Object lock = new Object();
	// 本地缓存桶
	private final ConcurrentHashMap<String, HashMap<String,Object>>
			localCache = new ConcurrentHashMap<>();
	//用来保持最近使用的元素的Queue，头部将保存最不常用的数据
	private final ConcurrentLinkedQueue<String>
			queue = new ConcurrentLinkedQueue<>();

	private final static String CACHETIME = "CACHETIME";	//本地缓存时间
	private final static String CACHEVALUE = "CACHEVALUE";	//本地缓存值
	private static final int INTERVAL_EXPIRY_TIME = 30000;	//(ms)清理线程执行时间
	private static final double MEMORYRADIO = 0.1;			//内存占比控制
	private static final int CLEARCOUNT = 10;				//清除条目数量

	/**
	 * 1.清理线程将清除过期数据
	 * 2.清理线程在内存不足时将清理掉LRU（是Least Recently Used 近期最少使用）数据
	 */
	private Thread executer = new Thread() {
		@Override
		public void run() {
			while(true){
				try{
					long nowTime = System.currentTimeMillis();
					Set<Entry<String, HashMap<String, Object>>> setLocal
							= localCache.entrySet();
					//1.清除过期缓存
					List<String> expireKeys = checkOutTime(nowTime, setLocal);
					//2. TODO 当内存余量不足时清除LRU数据
					Set<String> lruKeys = checkLru();
					logger.info("LocalCacheService清理过期缓存数据：",expireKeys);
					logger.info("LocalCacheService清理LRU缓存数据：",lruKeys);
					//3.删除指定的key
					expireKeys.addAll(lruKeys);
					expireKeys.forEach(localCache::remove);
				}catch (Exception e){
					logger.error(e.getMessage(),e);
				}

				try {
					Thread.sleep(INTERVAL_EXPIRY_TIME);
				} catch (InterruptedException e) {
					logger.error(e.getMessage(),e);
				}
			}
		}

		private List<String> checkOutTime(long nowTime,
										 Set<Entry<String, HashMap<String, Object>>> setLocal) {
			return setLocal.stream()
					.filter(p->nowTime>(Long)(p.getValue().get(CACHETIME)))
					.map(p->p.getKey())
					.distinct().collect(Collectors.toList());
		}

		private Set<String> checkLru() {
			Set<String> lruKeys = new HashSet<>();
			long maxMemory = Runtime.getRuntime().maxMemory();
			long totalMemory = Runtime.getRuntime().totalMemory();
			double memoryRadio = (maxMemory-totalMemory)/1.0*maxMemory;
			if(memoryRadio < MEMORYRADIO){
				for(int i=0;i<queue.size()/CLEARCOUNT;i++){
					lruKeys.add(queue.poll());
				}
			}
			return lruKeys;
		}
	};

	private LocalCacheService() {}

	public static LocalCacheService getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new LocalCacheService();
					instance.executer.start();
				}
			}
		}
		return instance;
	}

	/**
	 * 从本地缓存中获取缓存数据
	 * @param cacheKey 缓存数据的KEY
	 * @return Object对象，请自行转换类型
	 * @authod yoara 2015-3-20
	 */
	public Object get(String cacheKey) {
		HashMap<String,Object> cacheMap = localCache.get(cacheKey);
		if ( cacheMap != null ) {
			long currentTime = System.currentTimeMillis();
			if(currentTime < (Long)cacheMap.get(CACHETIME)){
				//将数据移至队列尾部
				queue.remove(cacheKey);
				queue.offer(cacheKey);
				return cacheMap.get(CACHEVALUE);
			}
		}
		return null;
	}

	/**
	 * 将数据缓存至本地缓存
	 * @param cacheKey 缓存数据的KEY
	 * @param cacheValue 缓存数据
	 * @param intervalTime 缓存有效期，Long类型
	 * @authod yoara 2015-3-20
	 */
	public Object put(String cacheKey,Object cacheValue,long intervalTime){
		//缓存数据
		HashMap<String,Object> cacheMap = new HashMap<String,Object>();
		long nowTime = System.currentTimeMillis();
		long expireTime = nowTime + intervalTime;
		cacheMap.put(CACHETIME,expireTime);
		cacheMap.put(CACHEVALUE, cacheValue);
		//将数据移至队列尾部
		queue.remove(cacheKey);
		queue.offer(cacheKey);
		return localCache.put(cacheKey, cacheMap);
	}

	/**
	 * 延长有效期时间
	 * @param cacheKey 缓存数据的KEY
	 * @param intervalTime 缓存有效期Long类型
	 * @authod yoara 2015-3-31
	 */
	public void expire(String cacheKey,long intervalTime){
		long nowTime = System.currentTimeMillis();
		long expireTime = nowTime + intervalTime;
		HashMap<String,Object> cacheMap = localCache.get(cacheKey);
		if(cacheMap==null){
			return;
		}
		cacheMap.put(CACHETIME,expireTime);
	}

	/**
	 * 移除本地缓存数据
	 * @param cacheKey 缓存数据的KEY
	 * @authod yoara 2015-3-20
	 */
	public Object remove(String cacheKey){
		queue.remove(cacheKey);
		return localCache.remove(cacheKey);
	}
}

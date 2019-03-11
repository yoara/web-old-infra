package com.company.seed.threads;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 有限线程池
 * Created by yoara on 2017/1/3.
 */
public class ThreadFixedPool {
	private static Logger logger = LoggerFactory.getLogger(ThreadFixedPool.class);
	private static ConcurrentHashMap<Key,ExecutorService> poolMap = new ConcurrentHashMap();

	private static ExecutorService getPool(Key poolKey) {
		ExecutorService pool = poolMap.get(poolKey);
		if(pool==null || pool.isShutdown() || pool.isTerminated()){
			pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
			poolMap.put(poolKey,pool);
		}
		return pool;
	}

	/**
	 * 提交一个 Callable 任务用于执行，并返回一个表示该任务的 Future。
	 * @param poolKey 线程池key
	 * @param task 要提交的任务
	 * @return 表示任务等待完成的 Future
	 */
	public static <V> Future<V> call(Key poolKey, Callable<V> task) {
		return getPool(poolKey).submit(task);
	}

	/**
	 * 在未来某个时间执行给定的命令。该命令可能在新的线程、已入池的线程或者正调用的线程中执行，这由 Executor 实现决定。
	 * @param poolKey 线程池key
	 * @param task 可运行的任务
	 */
	public static void execute(Key poolKey, Runnable task) {
		getPool(poolKey).execute(task);
	}

	/**
	 * 提交一个 Runnable 任务用于执行，并返回一个表示该任务的 Future。该 Future 的 get 方法在成功 完成时将会返回 null。
	 * @param poolKey 线程池key
	 * @param task 要提交的任务
	 * @return 表示任务等待完成的 Future
	 */
	public static Future<?> submit(Key poolKey, Runnable task) {
		return getPool(poolKey).submit(task);
	}

	/**
	 * 关闭线程池(等待所有任务执行完成)
	 * @param poolKey 线程池key
	 */
	public static void shutdown(Key poolKey) {
		ExecutorService pool = getPool(poolKey);
		if (pool != null) {
			pool.shutdown();
			logger.info("ThreadCachePool shutdowning!");
		}
	}

	/**
	 * 即刻关闭线程池
	 * @param poolKey 线程池key
	 */
	public static void shutdownNow(Key poolKey) {
		ExecutorService pool = getPool(poolKey);
		if (pool != null) {
			pool.shutdownNow();
			logger.info("ThreadCachePool shutdown!");
		}
	}

	/**
	 * 获取线程池信息
	 */
	public static String poolInfo(Key poolKey) {
		ExecutorService pool = getPool(poolKey);
		if (pool != null) {
			return pool.toString();
		}
		return "";
	}

	public enum Key {
		DATA_OPERATE_AOP_START,
		DATA_OPERATE_AOP_END,
	}
}

package com.company.seed.threads;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 无限线程池
 * Created by yoara on 2017/1/3.
 */
public class ThreadCachePool {
	private static Logger logger = LoggerFactory.getLogger(ThreadCachePool.class);

	private static ExecutorService threadCachePool = Executors.newCachedThreadPool();
	
	private static ExecutorService getPool() {
		if(threadCachePool.isShutdown()||threadCachePool.isTerminated()){
			threadCachePool = Executors.newCachedThreadPool();
		}
		return threadCachePool;
	}

	/**
	 * 提交一个 Callable 任务用于执行，并返回一个表示该任务的 Future。
	 * 
	 * @param task 要提交的任务
	 * @return 表示任务等待完成的 Future
	 */
	public static <V> Future<V> call(Callable<V> task) {
		return getPool().submit(task);
	}

	/**
	 * 在未来某个时间执行给定的命令。该命令可能在新的线程、已入池的线程或者正调用的线程中执行，这由 Executor 实现决定。
	 * 
	 * @param task 可运行的任务
	 */
	public static void execute(Runnable task) {
		getPool().execute(task);
	}

	/**
	 * 提交一个 Runnable 任务用于执行，并返回一个表示该任务的 Future。该 Future 的 get 方法在成功 完成时将会返回 null。
	 * 
	 * @param task 要提交的任务
	 * @return 表示任务等待完成的 Future
	 */
	public static Future<?> submit(Runnable task) {
		return getPool().submit(task);
	}

	/**
	 * 关闭线程池(等待所有任务执行完成)
	 */
	public static void shutdown() {
		threadCachePool.shutdown();
		logger.info("ThreadCachePool shutdowning!");
	}

	/**
	 * 即刻关闭线程池
	 */
	public static void shutdownNow() {
		threadCachePool.shutdownNow();
		logger.info("ThreadCachePool shutdown!");
	}

	/**
	 * 获取线程池信息
	 */
	public static String poolInfo() {
		return threadCachePool.toString();
	}
}

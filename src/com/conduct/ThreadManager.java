package com.conduct;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.structure.IndexList;

public class ThreadManager {

	private static final int CORE_POOL_SIZE = 10;
	private static final int MAXIMUN_POOL_SIZE = 10;
	private static ThreadPoolExecutor fetchPool;

	static {
		fetchPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUN_POOL_SIZE,
				0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}

	/**
	 * 批量载入获取列表线程
	 */
	public void batchFetchWork(int type, int size, int max) {
		IndexList list = new IndexList();
		list.preloadList(type, type, max);
		for (int i = 1; i <= list.getPageCount(); i++) {
			fetchPool.execute(new FetchTask(list, i));
		}
	}

	final class FetchTask implements Runnable {
		private IndexList handle;
		private int pageNum;

		FetchTask(IndexList list, int num) {
			handle = list;
			pageNum = num;
		}

		public void run() {
			IndexList.extractList(handle, pageNum);
		}
	}
}

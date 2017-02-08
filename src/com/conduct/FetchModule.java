package com.conduct;

public class FetchModule {

	public static void fetch(int optType, int pageSize, int max) {
		ThreadManager tm = new ThreadManager();
		tm.batchFetchWork(optType, pageSize, max);
	}

}

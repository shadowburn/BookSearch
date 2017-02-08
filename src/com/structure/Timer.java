package com.structure;

import java.util.HashMap;

public class Timer {
	private static final HashMap<Integer, Long> time = new HashMap<Integer, Long>();
	
	public static void start(int id) {
		time.put(id, System.currentTimeMillis());
	}
	
	public static long durition(int id) {
		if(time.containsKey(id)) {
			long ms = System.currentTimeMillis() - time.remove(id);
			System.out.println("time used: " + ms + " ms.");
			return ms;
		}
		return 0;
	}
}

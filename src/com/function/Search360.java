package com.function;

import java.util.Hashtable;
import java.util.Random;

import com.structure.Result;
import com.structure.SearchEngine;

public class Search360 implements SearchEngine {
	private String engineUrl;
	private Random rand;
	
	public Search360() {
		this.engineUrl = "http://www.so.com/s?ie=utf-8&src=360sou_home&q=";
		this.rand = new Random();
	}
	
	@Override
	public Hashtable<Integer, Result> getSearchResult(String keyWords,
			String fileType, int itemCount) {
		// TODO Auto-generated method stub
		return null;
	}

}

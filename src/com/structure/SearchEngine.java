package com.structure;

import java.util.Hashtable;

public interface SearchEngine {

	public abstract Hashtable<Integer, Result> getSearchResult(String keyWords,
			String fileType, int itemCount);

}

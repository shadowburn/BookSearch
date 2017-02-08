package com.structure;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;

import com.conduct.MainEntry;
import com.function.CommonMethod;
import com.function.HttpResponder;

public class IndexList {

	private int typeId;
	private int resultCount;
	private int maxNumber;
	private int pageSize;
	private int pageCount;
	private boolean isLoaded;
	private Hashtable<String, Fiction> fictionSet;

	public IndexList() {
		maxNumber = 0;
		resultCount = 0;
		fictionSet = new Hashtable<String, Fiction>();
		isLoaded = false;
	}

	public int getTypeId() {
		return typeId;
	}

	public int getPageCount() {
		return pageCount;
	}

	public boolean isIdExist(String bid) {
		return fictionSet.contains(bid);
	}

	public int getFictionCount() {
		return fictionSet.size();
	}

	public Fiction getFiction(String key) {
		return fictionSet.get(key);
	}

	public Iterator<String> getIterator() {
		return fictionSet.keySet().iterator();
	}

	public boolean preloadList(int type, int size, int max) {
		fictionSet.clear();
		typeId = type;
		pageSize = size;
		maxNumber = max;
		HttpResponder hr = null;
		String src = null;
		try {
			hr = CommonMethod.request.sendGet(getSearchReqUrl(type, 1, size));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (hr == null) {
			return false;
		} else {
			src = hr.getContent();
		}
		if (0 != Integer.parseInt(CommonMethod
				.getValue(src, Constant.tagRet, 0))) {
			return false;
		}
		resultCount = Integer.parseInt(CommonMethod.getValue(src,
				Constant.tagCount, 0));
		if (resultCount <= 0) {
			return false;
		}
		if (resultCount > maxNumber) {
			resultCount = maxNumber;
		}
		pageSize = size;
		pageCount = resultCount / pageSize + 1;
		isLoaded = true;
		return true;
	}

	private String getSearchReqUrl(int type, int pageNum, int pageSize) {
		String url = Constant.url_Search;
		switch (type) {
		case 0:
			url = url
					+ "cat1=10000&cat2=-1&cat3=-1&sex=0&words=-1&finish=-1&feetype=0&sortkey=2&page=";
			break;
		case 1:
			url = url
					+ "cat1=20000&cat2=-1&cat3=-1&sex=1&words=-1&finish=-1&feetype=0&sortkey=3&page=";
			break;
		case 2:
			url = url
					+ "cat1=30000&cat2=-1&cat3=-1&sex=2&words=-1&finish=-1&feetype=0&sortkey=1&page=";
			break;
		default:
			break;
		}
		url += pageNum;
		url += "&pagesize=";
		url += pageSize;
		url += "&dm=0&ver=2";
		return url;
	}

	public static boolean extractList(IndexList list, int page) {
		if (list == null || !list.isLoaded) {
			return false;
		}
		if (page > list.pageCount) {
			return false;
		}
		HttpResponder hr = null;
		String src = null;
		String url = null;
		try {
			url = list.getSearchReqUrl(list.typeId, page, list.pageSize);
			hr = CommonMethod.request.sendGet(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (hr == null) {
			return false;
		} else {
			src = hr.getContent();
		}
		String[] block = CommonMethod.getBlocks(src, Constant.blockStart,
				Constant.blockEnd, Constant.blockSplit);
		if (block == null) {
			return false;
		}
		for (int j = 0; j < block.length; j++) {
			Fiction f = new Fiction(block[j]);
			if (list.isIdExist(f.getBid())) {
				System.out.println(f.getBid());
			} else {
				list.fictionSet.put(f.getBid(), f);
				Fiction.getChapterContent(MainEntry.dataPath, f);
			}
		}
		return true;
	}
}

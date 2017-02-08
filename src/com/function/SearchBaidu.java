package com.function;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Random;

import com.structure.Constant;
import com.structure.Result;
import com.structure.SearchEngine;

public class SearchBaidu implements SearchEngine {
	private String engineUrl;
	private Random rand;

	public SearchBaidu() {
		this.engineUrl = "http://www.baidu.com/s?tn=sitehao123&lm=-1&wd=";
		this.rand = new Random();
	}

	// 对HTML进行析取，析取出num个URL和摘要
	public Hashtable<Integer, Result> getSearchResult(String keyWords,
			String fileType, int itemCount) {
		Hashtable<Integer, Result> contentTable = new Hashtable<Integer, Result>();
		int ptr = 0, end = 0, n = 0;
		String str = Constant.tagStartList;

		String page = null;
		HttpResponder hr = null;
		if (fileType == null) {
			fileType = "";
		} else {
			fileType = "+filetype%3A" + fileType;
		}
		try {
			String url = engineUrl + keyWords + fileType + "&rsv_bp=0&inputT="
					+ rand.nextInt(100) + "&rn=" + itemCount;
			hr = CommonMethod.request.sendGet(url);
			if (hr != null) {
				page = hr.getContent();
			}

			ptr = page.indexOf(str, ptr);

			do {
				n++;
				ptr = page.indexOf(Constant.tagResult, ptr);
				end = page.indexOf(Constant.endTable, ptr);
				if (ptr == -1 || end == -1) {
					break;
				}
				str = page.substring(ptr, end);
				String herf = CommonMethod.getValue(str, Constant.tagHref, 0);
				String abst = CommonMethod.getValue(str, Constant.tagAbst, 0);
				if (herf != null && abst != null) {
					contentTable.put(n, new Result(herf, abst));
				}
				ptr = end;
			} while (ptr != -1 && end != -1);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return contentTable;
	}

}

package com.function;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Hashtable;

import com.structure.Constant;
import com.structure.Result;
import com.structure.SearchEngine;

public class Matcher {
	private String fileAbstract;
	private String fileResult;
	private SearchEngine engine;
	private int itemCount;

	public Matcher(String pathName, String eng, int count) {
		fileAbstract = pathName + Constant.filenameAbst;
		fileResult = pathName + Constant.filenameResult;
		if (eng.toLowerCase().equals("360")) {
			engine = new SearchBaidu();
		} else if (eng.toLowerCase().equals("sogou")) {
			engine = new SearchBaidu();
		} else {
			engine = new SearchBaidu();
		}
		itemCount = count;
	}

	public void matchResult(String fileType) {
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileAbstract), "UTF-8"));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileResult), "UTF-8"));
			while ((line = reader.readLine()) != null) {
				if (line.length() == 0) {
					continue;
				}
				String[] tmp = line.split("/");
				if (tmp.length != 2 || tmp[0].length() == 0
						|| tmp[1].length() == 0) {
					continue;
				}
				String bid = tmp[0].trim();
				String sAbst = tmp[1].trim();
				String[] urls = null;
				if (!sAbst.equals("#")) {
					System.out.println(bid + " done.");
					if ((urls = parseResult(sAbst, fileType)) != null
							&& urls.length > 0) {
						writer.write(bid);
						writer.write(" :: ");
						for (int i = 0; i < urls.length; i++) {
							writer.write(urls[i]);
							writer.write(" ; ");
						}
						writer.newLine();
						writer.flush();
					}
				}
			}
			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String[] parseResult(String keyWords, String fileType) {
		ArrayList<String> list = new ArrayList<String>();
		Hashtable<Integer, Result> resultSet = engine.getSearchResult(keyWords,
				fileType, itemCount);
		for (int i = 1; i <= itemCount; i++) {
			if (resultSet.containsKey(i)) {
				Result resItem = resultSet.get(i);
				if (resItem.matchKeyWords(keyWords)) {
					list.add(resItem.getRedirectUrl());
				}
			}
		}
		String[] ret = new String[list.size()];
		return list.toArray(ret);
	}
}

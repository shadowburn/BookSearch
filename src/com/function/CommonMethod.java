package com.function;

import java.util.regex.Pattern;

import com.structure.Constant;

public class CommonMethod {

	public static final HttpRequester request = new HttpRequester();

	public static String getValue(String src, String key, Integer offset) {
		if (null == src || null == key) {
			return null;
		}
		int ptr = src.indexOf(key, offset) + key.length();
		int end = 0;
		if (-1 == ptr) {
			return null;
		}
		if (key.equals(Constant.tagAbst)) {
			end = src.indexOf(Constant.endDiv, ptr);
		} else if ('\"' == src.charAt(ptr)) {
			ptr++;
			end = src.indexOf(Constant.splitString, ptr);
		} else {
			end = src.indexOf(Constant.splitInteger, ptr);
		}
		if (-1 == end || end <= ptr) {
			return null;
		}

		return src.substring(ptr, end);
	}

	public static String[] getBlocks(String src, String markStart,
			String markEnd, String splitter) {
		if (null == markStart || null == markEnd || null == splitter) {
			return null;
		}
		int ptr = src.indexOf(markStart) + markStart.length();
		int end = src.indexOf(markEnd);
		if (-1 == ptr || -1 == end || end <= ptr) {
			return null;
		}
		return src.substring(ptr, end).split(splitter);
	}

	public static boolean isChinese(String str) {
		if (str == null) {
			return false;
		}
		int len = str.length();
		Pattern p = Pattern.compile("[\\u4E00-\\u9FBF]" + "{" + len + "}");
		return p.matcher(str.trim()).find();
	}
	
}

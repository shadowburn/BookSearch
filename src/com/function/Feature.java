package com.function;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.structure.Constant;

public class Feature {
	private ArrayList<File> fileList;;
	private int words;

	public Feature(int words) {
		this.fileList = new ArrayList<File>();
		this.words = words;
	}

	public int getWords() {
		return words;
	}

	public void setWords(int words) {
		this.words = words;
	}
	
	public boolean addFile(String fileName) {
		File file = new File(fileName);
		if (!file.exists() || !file.isFile()) {
			return false;
		}
		fileList.add(file);
		return true;
	}

	public boolean addFolder(String pathName) {
		File root = new File(pathName);
		if (!root.exists() || !root.isDirectory()) {
			return false;
		}
		FileFilter filter = new FileFilter() {
			public boolean accept(File file) {
				if (file.getName().endsWith(".txt")) {
					return true;
				}
				return false;
			}
		};
		File[] files = root.listFiles(filter);
		for (File file : files) {
			if (file.isFile()) {
				fileList.add(file);
			}
		}
		return true;
	}

	public void getFeatures() {
		Iterator<File> it = fileList.iterator();
		while (it.hasNext()) {
			getAbstractInfo(it.next(), this.words);
		}
	}

	public void getAbstractInfo(File file, int words) {
		String path = file.getParent();
		String name = file.getName();
		String tmp[] = name.split("\\.");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tmp.length - 1; i++) {
			sb.append(tmp[i]);
		}
		name = sb.toString();
		if (path.charAt(path.length() - 1) != '/') {
			path += "/";
		}
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(path + Constant.filenameAbst, true), "UTF-8"));
			String sAbst = getKeyWords(file.getAbsolutePath(), words);
			if (sAbst == null) {
				sAbst = "#";
				System.out.println(name + " Error Abstract!");
			}
			bw.write(name);
			bw.write("/");
			bw.write(sAbst);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public void getDetailInfo(File file, int words) {
//		
//	}

	private String getKeyWords(String filePath, int num) {
		String str = null;
		char[] buffer = new char[num];
		Random rand = new Random();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(filePath), "UTF-8"));
			skipLines(reader, 5);
			do {
				reader.skip(rand.nextInt(num));
				if (reader.read(buffer) == -1) {
					str = null;
					break;
				}
				str = String.valueOf(buffer);
			} while (!CommonMethod.isChinese(str));
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (str != null) {
			return str.trim();
		}
		return null;
	}

//	private String[] getKeyWords(String filePath, int words, int num) {
//		return null;
//	}

	private boolean skipLines(BufferedReader reader, int lines) {
		if (lines <= 0) {
			return false;
		}
		try {
			for (int i = 0; i < lines; i++) {
				if (reader.readLine() == null) {
					return false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}

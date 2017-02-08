package com.structure;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeSet;

import com.function.CommonMethod;
import com.function.HttpResponder;

public class ICP implements Comparable<ICP> {
	private static final String mainUrl = "http://www.alexa.cn/index.php?url=";
	private static final String tagComName = "主办单位名称";
	private static final String tagICP = "网站备案/许可证号";
	private static final String tagICPStart = "target=\"_blank\">";

	private String originUrlStr;
	private String hostName;

	private String comName;
	private String icpInfo;

	public ICP(String urlString) {
		originUrlStr = urlString;
		hostName = null;
		icpInfo = null;
		comName = null;
	}

	private String getDomain(String str) {
		try {
			URL url = new URL(str);
			hostName = url.getHost();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hostName;
	}

	public void search() {
		String url = mainUrl + getDomain(originUrlStr);
		String content = null;
		try {
			HttpResponder hr = CommonMethod.request.sendGet(url);
			content = hr.getContent();
			if (content != null) {
				int ptr = 0, end = 0;
				ptr = content.indexOf(tagComName, ptr);
				ptr = content.indexOf(Constant.tagFont, ptr)
						+ Constant.tagFont.length();
				end = content.indexOf(Constant.endFont, ptr);
				if (ptr == -1 || end == -1) {
					throw new IOException("Unsolved company name!");
				}
				comName = content.substring(ptr, end).trim();

				ptr = end;
				ptr = content.indexOf(tagICP, ptr);
				ptr = content.indexOf(tagICPStart, ptr) + tagICPStart.length();
				end = content.indexOf(Constant.endA, ptr);
				if (ptr == -1 || end == -1) {
					throw new IOException("Unsolved ICP infomation!");
				}
				icpInfo = content.substring(ptr, end).trim();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getHostName() {
		return hostName;
	}

	public String getIcpInfo() {
		return icpInfo;
	}

	public String getComName() {
		return comName;
	}

	@Override
	public boolean equals(Object icp) {
		return this.hostName.equals(((ICP) icp).hostName);
	}

	public static void batchICP(String pathName) {
		Hashtable<String, ICP[]> table = new Hashtable<String, ICP[]>();
		TreeSet<ICP> set = new TreeSet<ICP>();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(pathName + Constant.filenameResult),
					"UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null && line.length() > 0) {
				String[] str = line.split("::");
				if (str.length == 2) {
					String bid = str[0].trim();
					String[] urls = str[1].split(";");
					for (int i = 0; i < urls.length; i++) {
						if (urls[i].trim().length() > 0) {
							ICP icp = new ICP(urls[i].trim());
							icp.getDomain(icp.originUrlStr);
							icp.search();
							set.add(icp);
						}
					}
					ICP[] ret = new ICP[set.size()];
					table.put(bid, set.toArray(ret));
				}
				set.clear();
			}
			reader.close();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(pathName + Constant.filenameICP),
					"UTF-8"));
			Iterator<String> it = table.keySet().iterator();
			while (it.hasNext()) {
				String bid = it.next();
				ICP[] data = table.get(bid);
				writer.write(bid);
				writer.write(":");
				writer.newLine();
				for (int i = 0; i < data.length; i++) {
					writer.write(data[i].getHostName());
					writer.write(", ");
					writer.write(data[i].getComName());
					writer.write(", ");
					writer.write(data[i].getIcpInfo());
					writer.newLine();
				}
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int compareTo(ICP o) {
		return this.hostName.compareTo(((ICP) o).hostName);
	}
}

package com.structure;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Hashtable;

import com.function.CommonMethod;
import com.function.HttpResponder;

public class Fiction {

	private int id;
	private String author;
	private String typeClass;
	private String bookName;
	private int finished;
	private int totalWords;
	private String intro;
	private String bid;

	public Fiction(String info) {
		int ptr = 0;
		int end = 0;

		ptr = info.indexOf(Constant.tagFinished, end)
				+ Constant.tagFinished.length() + 1;
		end = info.indexOf(Constant.splitString, ptr);
		finished = Integer.parseInt(info.substring(ptr, end));

		ptr = info.indexOf(Constant.tagIntro, end) + Constant.tagIntro.length()
				+ 1;
		end = info.indexOf(Constant.splitString, ptr);
		intro = formatContent(info.substring(ptr, end));

		ptr = info.indexOf(Constant.tagBookId, end)
				+ Constant.tagBookId.length() + 1;
		end = info.indexOf(Constant.splitString, ptr);
		id = Integer.parseInt(info.substring(ptr, end));

		ptr = info.indexOf(Constant.tagAuthor, end)
				+ Constant.tagAuthor.length() + 1;
		end = info.indexOf(Constant.splitString, ptr);
		author = info.substring(ptr, end);

		ptr = info.indexOf(Constant.tagClass, end) + Constant.tagClass.length()
				+ 1;
		end = info.indexOf(Constant.splitString, ptr);
		typeClass = info.substring(ptr, end);

		ptr = info.indexOf(Constant.tagTitle, end) + Constant.tagTitle.length()
				+ 1;
		end = info.indexOf(Constant.splitString, ptr);
		bookName = info.substring(ptr, end);

		ptr = info.indexOf(Constant.tagTotalWords, end)
				+ Constant.tagTotalWords.length() + 1;
		end = info.indexOf(Constant.splitString, ptr);
		totalWords = Integer.parseInt(info.substring(ptr, end));

		ptr = info.indexOf(Constant.tagBid, end) + Constant.tagBid.length() + 1;
		end = info.indexOf(Constant.splitString, ptr);
		bid = info.substring(ptr, end);

		if (!bid.equals(String.valueOf(id))) {
			System.out.println("bid2id error!");
		}
	}

	public int getId() {
		return id;
	}

	public String getAuthor() {
		return author;
	}

	public String getTypeClass() {
		return typeClass;
	}

	public String getTitle() {
		return bookName;
	}

	public int getFinished() {
		return finished;
	}

	public int getTotalWords() {
		return totalWords;
	}

	public String getIntro() {
		return intro;
	}

	public String getBid() {
		return bid;
	}

	public static boolean getChapterContent(String path, Fiction fic) {
		if (path == null || fic == null) {
			System.out.println("Fiction " + fic.bid + " is not exist!");
			return false;
		}

		if ('/' != path.charAt(path.length() - 1)) {
			path = path + "/";
		}
		int i = 1;
		int flag = 0;
		HttpResponder hr = null;
		String url = null;
		String src = null;

		try {
			Hashtable<Integer, String> chapter = new Hashtable<Integer, String>();
			Hashtable<Integer, String> content = new Hashtable<Integer, String>();
			do {
				url = Constant.url_Read
						+ "uin=0&g_tk=5381&callback=_Callback&bid="
						+ fic.getBid() + "&cid=" + i
						+ "&ispreget=false&c_f=&g_f=";
				hr = CommonMethod.request.sendGet(url);
				if (null != hr) {
					src = hr.getContent();
				} else {
					System.out.println("Chapter Error!");
					return false;
				}
				flag = Integer.parseInt(CommonMethod.getValue(src,
						Constant.tagRet, 0));
				if (0 == flag) {
					chapter.put(i, CommonMethod.getValue(src,
							Constant.tagTitle, 0));
					content.put(i, fic.formatContent(CommonMethod.getValue(src,
							Constant.tagContent, 0)));
				} else {
					break;
				}
				i++;
			} while (0 == flag);
			int n = i;
			i = 1;
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(path + fic.bid + ".txt"), "UTF-8"));
			bw.write(fic.bookName);
			bw.newLine();
			bw.write(fic.author);
			bw.newLine();
			bw.write(fic.intro);
			bw.newLine();
			bw.newLine();
			while (i < n) {
				bw.write(chapter.get(i));
				bw.newLine();
				bw.write(content.get(i));
				bw.newLine();
				bw.newLine();
				i++;
				bw.flush();
			}
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			System.out.println(url);
			System.out.println(src);
		} catch (Exception e) {
			System.out.println(url);
			System.out.println(src);
		}

		return true;
	}

	private String formatContent(String str) {
		str = str.replaceAll("\\<p>|<\\\\/p>", "");
		str = str.replaceAll("\\\\r\\\\n", "\r\n");
		str = str.replaceAll("\\\\r", "\r\n");
		str = str.replaceAll("\\\\t", "\t");
		str = str.replaceAll("\\\\uff0c", "，");
		str = str.replaceAll("\\\\uff01", "！");
		str = str.replaceAll("\\\\uff08", "(");
		str = str.replaceAll("\\\\uff09", ")");
		return str;
	}
}

package com.conduct;

public class MainEntry {
	public static String dataPath = "D:/Extract/";

	/**
	 * Program main entry
	 * 
	 * @param args
	 *            working path, operating, optional parameters
	 */
	public static void main(String[] args) {
		if (args[0].charAt(args[0].length() - 1) != '/') {
			dataPath = args[0] + "/";
		} else {
			dataPath = args[0];
		}
		if (args[1].toLowerCase().equals("fetch")) {
			int type = Integer.parseInt(args[2]);
			int size = Integer.parseInt(args[3]);
			int max = Integer.parseInt(args[4]);
			FetchModule.fetch(type, size, max);
		} else if (args[1].toLowerCase().equals("search")) {
			if (args.length < 3) {
				SearchModule.search(dataPath, null);
			} else {
				SearchModule.search(dataPath, args[2]);
			}
		} else if (args[1].toLowerCase().equals("icp")) {
			SearchModule.getIcp(dataPath);
		} else {
			System.out.println("Error input at param 2.");
		}
	}
}

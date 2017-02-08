package com.conduct;

import com.function.Feature;
import com.function.Matcher;
import com.structure.ICP;
import com.structure.Timer;

public class SearchModule {

	public static void search(String pathName, String fileTpye) {
		Timer.start(1);
		Feature f = new Feature(13);
		f.addFolder(pathName);
		f.getFeatures();
		Timer.durition(1);

		Timer.start(2);
		Matcher m = new Matcher(pathName, "default", 30);
		m.matchResult(fileTpye);
		Timer.durition(2);
	}
	
	public static void getIcp(String pathName) {
		Timer.start(3);
		ICP.batchICP(pathName);
		Timer.durition(3);
	}

}

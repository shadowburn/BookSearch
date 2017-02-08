package com.structure;

import java.io.IOException;
import java.net.SocketException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.AllClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;

public class Result {

	private String url_origin;
	private String url_redirect;
	private String content;
	private boolean match;

	public Result(String url, String abst) {
		url_origin = url;
		url_redirect = null;
		content = abst;
		match = false;
	}

	public String getOriginalUrl() {
		return url_origin;
	}

	public String getRedirectUrl() {
		if (url_redirect == null) {
			HttpClient client = new DefaultHttpClient();
			// 使用Get方式请求
			HttpGet httpget = new HttpGet(url_origin);
			HttpParams params = client.getParams();
			params.setParameter(AllClientPNames.HANDLE_REDIRECTS, false);

			String newUrl = null;
			// 执行请求
			try {
				HttpResponse response = client.execute(httpget);
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 301 || statusCode == 302) {
					Header[] hs = response.getHeaders("Location");
					for (Header h : hs) {
						newUrl = h.getValue();
					}
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (SocketException e){
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			httpget.releaseConnection();
			client.getConnectionManager().shutdown();
			url_redirect = newUrl;
		}
		return url_redirect;
	}

	public String getContent() {
		return content;
	}

	public boolean isMatched() {
		return match;
	}

	public boolean matchKeyWords(String keyWords) {
		if (-1 == content.indexOf(keyWords)) {
			match = false;
		} else {
			match = true;
		}
		return match;
	}
}

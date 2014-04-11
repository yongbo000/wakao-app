package me.wakao.app.util;


import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HttpUtil {
	public static final String CHARSET = "UTF-8";
	private final static int TIMEOUT_CONNECTION = 5000;
	private final static int TIMEOUT_SOCKET = 20000;
	
	public final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.94 Safari/537.36";
	
	public static HttpClient getHttpClient() {
		HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT_CONNECTION);
        HttpConnectionParams.setSoTimeout(params, TIMEOUT_SOCKET);
        
        HttpClient httpClient = new DefaultHttpClient(params);
        
		return httpClient;
	}	
	public static HttpGet getHttpGet(Map<String, String> request_headers) {
		HttpGet httpGet = new HttpGet();
		
		httpGet.setHeader("User-Agent", USER_AGENT);
		for(String k : request_headers.keySet()){
			httpGet.setHeader(k, request_headers.get(k));
		}
		
		return httpGet;
	}
	public static HttpPost getHttpPost(Map<String, String> headers) {
		HttpPost httpPost = new HttpPost();
		
		httpPost.setHeader("User-Agent", USER_AGENT);
		for(String k : headers.keySet()){
			httpPost.setHeader(k, headers.get(k));
		}
		
		return httpPost;
	}
	
	public static boolean isNetworkConnected(Context mContext) {
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = cm.getActiveNetworkInfo();
		return network != null && network.isConnectedOrConnecting();
	}
}

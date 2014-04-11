package me.wakao.app.robot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.regex.Pattern;

import me.wakao.app.util.HttpUtil;
import me.wakao.app.widget.PullToRefreshListView;
import me.wakao.app.widget.PullToRefreshListView.OnRefreshListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class MyRobot implements OnScrollListener, OnRefreshListener {
	//错误码
	public final static int IS_REFRESH_COMPLETE = 0;
	public final static int IS_GETMORE_COMPLETE = 1;
	public final static int IS_REFRESH_START = 2;
	public final static int IS_GETMORE_START = 3;
	public final static int IS_NETWORK_LOADING = 4;
	public final static int IS_NETWORK_ERROR = -1;
	public final static int IS_REFRESH_ERROR = -2;
	public final static int IS_GETMORE_ERROR = -3;
	public final static int IS_GET_COMMENT_COMPLETE = 5;
	public final static int IS_ADD_COMMENT_COMPLETE = 6;
	public final static int LOAD_DATA_ALL = 7;
	public final static int CLEAR_DATA = 8;

	public final static String HOST = "http://blog.wakao.me";
	public final static String PORT = "";
	public final static String TAG = "TAG";
	public final static int MAX_FAILCOUNT = 3; // 最大失败次数，超过即不再重新抓取

	protected PullToRefreshListView listview;
	protected Handler handler;

	protected LinearLayout footerLayout;
	protected TextView textMore;
	protected ProgressBar loadingBar;

	protected boolean IS_LOADING = false;
	
	protected String cacheDir;
	
	protected Context mContext;
	
	public void setCacheDir(String cacheDir){
		this.cacheDir = cacheDir;
	}
	
	public void setContext(Context mContext) {
		this.mContext = mContext;
	}

	public String fecthUrl(String url) {
		if(!HttpUtil.isNetworkConnected(mContext)){
			return null;
		}
		Log.e("TAG", "正在获取数据:" + url);
		String lineText;
		StringBuilder sb = new StringBuilder();
		InputStreamReader isr = null;
		int failCount = 0;
		do {
			try {
				HttpGet get = new HttpGet(url);
				HttpClient httpClient = new DefaultHttpClient();
				HttpResponse response = httpClient.execute(get);
				int status = response.getStatusLine().getStatusCode();
				HttpEntity entity = response.getEntity();
				if (status != HttpStatus.SC_OK || entity == null) {
					return null;
				}
				isr = new InputStreamReader(entity.getContent());
				BufferedReader bufReader = new BufferedReader(isr);
				while ((lineText = bufReader.readLine()) != null) {
					sb.append(lineText);
				}
				break;
			} catch (Exception e) {
				failCount++;
				e.printStackTrace();
				Log.e(TAG, "对于链接:" + url + " 第" + failCount
						+ "次抓取失敗，正在尝试重新抓取...");
			} finally {
				try {
					if (isr != null) {
						isr.close();
					}
				} catch (IOException e) {

				}
			}
		} while (failCount < MAX_FAILCOUNT);
		return sb.toString();
	}

	public String postDataToServer(List<NameValuePair> nvs, String url, String cookie) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(url);
		httpost.setHeader("Cookie", cookie);
		StringBuilder sb = new StringBuilder();
		// 设置表单提交编码为UTF-8
		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvs, HTTP.UTF_8));
			HttpResponse response = httpclient.execute(httpost);
			HttpEntity entity = response.getEntity();
			InputStreamReader isr = new InputStreamReader(entity.getContent());
			BufferedReader bufReader = new BufferedReader(isr);
			String lineText = null;
			while ((lineText = bufReader.readLine()) != null) {
				sb.append(lineText);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return sb.toString();
	}
	
	public static HttpGet getHttpGet(String url){
		HttpGet get = new HttpGet(url);
		get.setHeader("User-Agent", HttpUtil.USER_AGENT);
		if(Pattern.matches("^http://essay.*", url)){
			get.setHeader("Host", "essay.oss.aliyuncs.com");
			get.setHeader("Referer", "http://chuansongme.com");
		}else if(Pattern.matches("http://pic\\.qiushibaike\\.com", url)){
			get.setHeader("Host", "pic.qiushibaike.com");
		}
		return get;
	}
	/**
	 * 下载网络图片到本地
	 * 
	 * @param imgUrl
	 * */
	public static Bitmap downNetImage(String url) {
		Log.e("TAG", "开始下载图片:" + url);
		Bitmap bitmap = null;
		HttpClient client = new DefaultHttpClient();
		HttpGet get = getHttpGet(url);
		int failCount = 1;
		do {
			try {
				HttpResponse response = client.execute(get);
				HttpEntity entity = response.getEntity();
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK
						&& entity != null) {
					bitmap = BitmapFactory.decodeStream(entity.getContent());
				}
				break;
			} catch (Exception e) {
				failCount++;
				System.err.println("对于图片" + url + "第" + failCount
						+ "次下载失败,正在尝试重新下载...");
			}
		} while (failCount < MAX_FAILCOUNT);
		
		return bitmap;
	}

	public void onNetWorkComplete(int actionCode) {
		IS_LOADING = false;
		handler.sendEmptyMessage(actionCode);
	}
	
	public void onNetWorkComplete(String resMsg, int actionCode) {
		IS_LOADING = false;
		Message msg = new Message();
		msg.what = actionCode;
		msg.obj = resMsg;
		handler.sendMessage(msg);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		listview.onScroll(view, firstVisibleItem, visibleItemCount,
				totalItemCount);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		listview.onScrollStateChanged(view, scrollState);
		// 当不滚动时
		if (scrollState == SCROLL_STATE_IDLE) {
			// 判断滚动到底部
			if (!IS_LOADING
					&& view.getLastVisiblePosition() == (view.getCount() - 1)) {
				Log.e("TAG", "loading。。。");
				IS_LOADING = true;
				getMore();
			}
		}
	}

	@Override
	public void onRefresh() {
		if(IS_LOADING) { return; }
		IS_LOADING = true;
		refresh();
	}

	public void getMore() {}
	public void refresh() {}
	/**
	 * 写入缓存
	 * */
	public void writeToCache(String cacheKey, Object cacheValue){
		int cnt = 0;
		do {
			try {
				File destDir = new File(cacheDir);
				if (!destDir.exists()) {
					destDir.mkdirs();
				}
	            ObjectOutputStream os = new ObjectOutputStream(  
	                    new FileOutputStream(cacheDir + cacheKey));  
	            os.writeObject(cacheValue);  
	            os.close();
	            break;
	        } catch (Exception e) { 
	        	cnt++;
	            System.err.println("写入缓存失败,正在尝试第" + cnt + "次重新写入");
	        } 
		} while(cnt < MAX_FAILCOUNT);
		
	}
	/**
	 * 读取缓存
	 * */
	public Object readFromCache(String cacheKey){
		File file = new File(cacheDir + cacheKey);
		if(!file.exists()) {
			return null;
		}
		
		Object cacheObj = null;
		int cnt = 0;
		do {
			try {
				ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));  
				cacheObj = is.readObject();
	            is.close();
	            break;
	        } catch (Exception e) { 
	        	e.printStackTrace();
	        	cnt++;
	            System.err.println("读取缓存失败,正在尝试第" + cnt + "次重新读取");
	        } 
		} while(cnt < MAX_FAILCOUNT);
		return cacheObj;
	}
	
}

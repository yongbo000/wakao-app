package me.wakao.app.robot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import me.wakao.app.bean.ArticleObj;
import me.wakao.app.bean.FunnyObj;
import me.wakao.app.widget.PullToRefreshListView;

public class ArticleRobot extends MyRobot {
//	public final static String DATA_API_URL = "http://api2.wakao.me/articles/%1$s/%2$d";
	public final static String DATA_API_URL = "http://apitest.wakao.me/articles/%1$s/%2$d";
	public final static String CONTENT_DATA_API_URL = "http://apitest.wakao.me/article-content/%1$d";

	private int page = 1;

	private List<ArticleObj> dataArray;
	
	private String channel;

	public ArticleRobot() {}
	public ArticleRobot(PullToRefreshListView listview, Handler handler,
			List<ArticleObj> dataArray) {
		this.listview = listview;
		this.handler = handler;
		this.dataArray = dataArray;
	}
	public void setChannel(String channel){
		this.channel = channel;
	}
	public List<ArticleObj> getArticleData() {
		return dataArray;
	}

	private List<ArticleObj> parseJSON2Obj(String json) {
		List<ArticleObj> objs = new ArrayList<ArticleObj>();
		ArticleObj obj = null;
		JsonElement element = new JsonParser().parse(json);
		JsonArray array = element.getAsJsonObject().get("data")
				.getAsJsonArray();
		// 遍历数组
		Iterator<JsonElement> it = array.iterator();
		Gson gson = new Gson();
		while (it.hasNext()) {
			JsonElement e = it.next();
			// JsonElement转换为JavaBean对象
			obj = gson.fromJson(e, ArticleObj.class);
			objs.add(obj);
		}
		return objs;
	}

	@Override
	public void refresh() {
		handler.sendEmptyMessage(IS_NETWORK_LOADING);
		page = 1;
		new Thread() {
			@Override
			public void run() {
				String json = fecthUrl(String.format(DATA_API_URL, channel, page));
				if(json == null || json.length() == 0) { 
					onNetWorkComplete(MyRobot.IS_REFRESH_ERROR);
					return; 
				}
				List<ArticleObj> fns;
				try {
					fns = parseJSON2Obj(json);
				} catch (Exception e) {
					onNetWorkComplete(MyRobot.IS_REFRESH_ERROR);
					return;
				}
				dataArray.clear();
				dataArray.addAll(fns);
				onNetWorkComplete(MyRobot.IS_REFRESH_COMPLETE);
				writeToCache(channel, dataArray);
			}
		}.start();
	}

	public void getMore() {
		handler.sendEmptyMessage(IS_NETWORK_LOADING);
		new Thread() {
			@Override
			public void run() {
				String json = fecthUrl(String.format(DATA_API_URL, channel, ++page));
				if(json == null || json.length() == 0) { 
					onNetWorkComplete(MyRobot.IS_GETMORE_ERROR);
					return; 
				}
				Log.e("TAG", json);
				try {
					dataArray.addAll(parseJSON2Obj(json));
				} catch (Exception e) {
					onNetWorkComplete(MyRobot.IS_GETMORE_ERROR);
					return;
				}
				onNetWorkComplete(MyRobot.IS_GETMORE_COMPLETE);
			}
		}.start();
	}
	public void initData(List<ArticleObj> objs){
		dataArray.clear();
		dataArray.addAll(objs);
		onNetWorkComplete(MyRobot.IS_REFRESH_COMPLETE);
	}
}

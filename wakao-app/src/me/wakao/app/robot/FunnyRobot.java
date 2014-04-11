package me.wakao.app.robot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import me.wakao.app.bean.FunnyObj;
import me.wakao.app.widget.PullToRefreshListView;

public class FunnyRobot extends MyRobot {
//	public final static String DATA_API_URL_NEW = "http://api2.wakao.me/funnys/%1$d";
//	public final static String DATA_API_URL_HOT = "http://api2.wakao.me/funnys/hot/get";
	
	public final static String DATA_API_URL_NEW = "http://apitest.wakao.me/funnys/%1$d";
	public final static String DATA_API_URL_HOT = "http://apitest.wakao.me/funnys/hot/get";
	
	public final static int MODE_NEW = 0;
	public final static int MODE_HOT = 1;
	
	

	protected List<FunnyObj> dataArray;
	
	private int page = 1;
	private int mode;
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	public FunnyRobot(PullToRefreshListView listview,
			Handler handler, List<FunnyObj> dataArray) {
		this.listview = listview;
		this.handler = handler;
		this.dataArray = dataArray;
		this.mode = MODE_NEW;
	}
	public List<FunnyObj> getFunnyData() {
		return dataArray;
	}
	
	private List<FunnyObj> parseJSON2Obj(String json) {
		List<FunnyObj> objs = new ArrayList<FunnyObj>();
		FunnyObj obj = null;
		JsonElement element = new JsonParser().parse(json);
		JsonArray array = element.getAsJsonObject().get("data")
				.getAsJsonArray();
		// 遍历数组
		Iterator<JsonElement> it = array.iterator();
		Gson gson = new Gson();
		while (it.hasNext()) {
			JsonElement e = it.next();
			// JsonElement转换为JavaBean对象
			obj = gson.fromJson(e, FunnyObj.class);
			objs.add(obj);
		}
		return objs;
	}

	@Override
	public void refresh() {
		Log.i("TAG", "refresh.....");
		handler.sendEmptyMessage(IS_NETWORK_LOADING);
		page = 1;
		new Thread() {
			@Override
			public void run() {
				String api = mode == MODE_NEW ? DATA_API_URL_NEW : mode == MODE_HOT ? DATA_API_URL_HOT : DATA_API_URL_NEW;
				String json = fecthUrl(String.format(api, page));
				if(json == null || json.length() == 0) { 
					onNetWorkComplete(MyRobot.IS_REFRESH_ERROR);
					return; 
				}
				List<FunnyObj> fns;
				try {
					fns = parseJSON2Obj(json);
				} catch (Exception e) {
					onNetWorkComplete(MyRobot.IS_REFRESH_ERROR);
					return;
				}
				dataArray.clear();
				dataArray.addAll(fns);
				onNetWorkComplete(MyRobot.IS_REFRESH_COMPLETE);
				writeToCache("funny", dataArray);
			}
		}.start();
	}
	public void getMore() {
		if(mode == MODE_HOT) {
			onNetWorkComplete(MyRobot.LOAD_DATA_ALL);
			return;
		}
		handler.sendEmptyMessage(IS_NETWORK_LOADING);
		new Thread() {
			@Override
			public void run() {
				String api = mode == MODE_NEW ? DATA_API_URL_NEW : mode == MODE_HOT ? DATA_API_URL_HOT : DATA_API_URL_NEW;
				String json = fecthUrl(String.format(api, ++page));
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
	public void clear() {
		dataArray.clear();
		handler.sendEmptyMessage(CLEAR_DATA);
	}
	public void clickRefresh() {
		listview.clickRefresh();
	}
	public void initData(List<FunnyObj> objs){
		dataArray.clear();
		dataArray.addAll(objs);
		onNetWorkComplete(MyRobot.IS_REFRESH_COMPLETE);
	}
}

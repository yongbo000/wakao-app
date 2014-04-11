package me.wakao.app.robot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import android.os.Handler;
import android.util.Log;

import me.wakao.app.bean.RingObj;
import me.wakao.app.widget.PullToRefreshListView;

public class RingRobot extends MyRobot {

	public static final int MODE_SHUNXU = 1;
	public static final int MODE_TUIJIAN = 2;

	public final static String DATA_API_URL = "http://iapp.wakao.me/songapi/json/list?page=%1$d&mode=%2$d";

	private int page = 1;
	private int mode;

//	private List<RingObj> shunxuData;
//	private List<RingObj> hotData;
//	private List<RingObj> tuijianData;

	private List<RingObj> dataArray;

	public RingRobot(PullToRefreshListView listview, Handler handler,
			List<RingObj> dataArray) {
		this.mode = MODE_SHUNXU;
		this.listview = listview;
		this.handler = handler;
		this.dataArray = dataArray;
	}

	public List<RingObj> getRingData() {
		return dataArray;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public void setPage(int page) {
		this.page = page;
	}

	private List<RingObj> parseJSON2Obj(String json) {
		List<RingObj> objs = new ArrayList<RingObj>();
		RingObj obj = null;
		JsonElement element = new JsonParser().parse(json);
		JsonArray array = element.getAsJsonArray();
		// 遍历数组
		Iterator<JsonElement> it = array.iterator();
		Gson gson = new Gson();
		while (it.hasNext()) {
			JsonElement e = it.next();
			// JsonElement转换为JavaBean对象
			obj = gson.fromJson(e, RingObj.class);
			obj.setDownUrl(obj.getDownUrl().substring(0, obj.getDownUrl().indexOf("?")));
			objs.add(obj);
		}
		return objs;
	}

	public void onGetMore() {
		new Thread() {
			@Override
			public void run() {
				String json = fecthUrl(String
						.format(DATA_API_URL, ++page, mode));
				Log.e("TAG", json);
				dataArray.addAll(parseJSON2Obj(json));
				onNetWorkComplete(MyRobot.IS_GETMORE_COMPLETE);
			}
		}.start();
	}

	@Override
	public void onRefresh() {
		page = 1;
		new Thread() {
			@Override
			public void run() {
				String json = fecthUrl(String.format(DATA_API_URL, page, mode));
				Log.e("TAG", json);
				dataArray.clear();
				dataArray.addAll(parseJSON2Obj(json));
				onNetWorkComplete(MyRobot.IS_REFRESH_COMPLETE);
			}
		}.start();
	}
}

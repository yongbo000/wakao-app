package me.wakao.app.robot;

import java.util.ArrayList;
import java.util.List;

import me.wakao.app.bean.CommentObj;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Handler;

public class CommentRobot extends MyRobot {

//	private final static String ADD_DATA_API_URL = "http://api2.wakao.me/comment/funny/add";
//	private final static String GET_DATA_API_URL = "http://api2.wakao.me/comment/funny/get/%1$d";
	private final static String ADD_DATA_API_URL = "http://apitest.wakao.me/comment/funny/add";
	private final static String GET_DATA_API_URL = "http://apitest.wakao.me/comment/funny/get/%1$d";

	private String cookie;

	public CommentRobot(Handler handler) {
		this.handler = handler;
	}
	
	public String getComment(int id) {
		String res = fecthUrl(String.format(GET_DATA_API_URL, id));
		if(res == null){
			onNetWorkComplete(MyRobot.IS_NETWORK_ERROR);
		} else {
			onNetWorkComplete(res, MyRobot.IS_GET_COMMENT_COMPLETE);
		}
		return null;
	}

	public void addComment(final CommentObj obj) {
		if(IS_LOADING) return;
		IS_LOADING = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<NameValuePair> nvs = new ArrayList<NameValuePair>();
				nvs.add(new BasicNameValuePair("funny_id", obj.getFunnyId()));
				nvs.add(new BasicNameValuePair("content", obj.getContent()));
				nvs.add(new BasicNameValuePair("avatar", obj.getAvatar()));
				nvs.add(new BasicNameValuePair("user_id", obj.getUserId()));
				nvs.add(new BasicNameValuePair("user_name", obj.getUserName()));
				cookie = "uid=" + obj.getUserId() + ";";
				
				String res = postDataToServer(nvs, ADD_DATA_API_URL, cookie);
				onNetWorkComplete(res, MyRobot.IS_ADD_COMMENT_COMPLETE);
			}
		}).start();
	}
}

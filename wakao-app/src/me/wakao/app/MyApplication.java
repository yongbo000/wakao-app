package me.wakao.app;


import java.io.File;

import com.umeng.analytics.MobclickAgent;

import me.wakao.app.bean.UserObj;
import me.wakao.app.common.PropertyManger;
import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
	
	public static UserObj user;

	private PropertyManger propertyManger;
	@Override
	public void onCreate() {
		super.onCreate();
		propertyManger = new PropertyManger(this);
		propertyManger.setProsPath(PropertyManger.PATH_USERINFO);
		user = getUserInfo();
		
		MobclickAgent.openActivityDurationTrack(false);
	}
	
	/**
	 * 获取登录信息
	 * @return
	 */
	public UserObj getUserInfo() {
		if(user != null){
			return user;
		}
		UserObj userObj = null;
		if(!propertyManger.getProperties().isEmpty()){
			userObj = new UserObj();
			userObj.setWeiboName(propertyManger.getProperty("user.name"));
			userObj.setWeiboId(propertyManger.getProperty("user.media_uid"));
			userObj.setFaceUrl(propertyManger.getProperty("user.tinyurl"));
			Log.e("TAG", userObj.getWeiboId()+";"+userObj.getWeiboName()+";"+userObj.getFaceUrl());
		}
		return userObj;
	}
	public void logoutAccount(){
		user = null;
		propertyManger.removeProperty("user.name", "user.media_uid", "user.tinyurl");
	}
	/**
	 * 清除app缓存
	 */
	public void clearAppCache()
	{
		deleteDatabase("webview.db");  
		deleteDatabase("webview.db-shm");  
		deleteDatabase("webview.db-wal");  
		deleteDatabase("webviewCache.db");  
		deleteDatabase("webviewCache.db-shm");  
		deleteDatabase("webviewCache.db-wal");  
		//清除数据缓存
		clearCacheFolder(getFilesDir(),System.currentTimeMillis());
		clearCacheFolder(getCacheDir(),System.currentTimeMillis());
		//2.2版本才有将应用缓存转移到sd卡的功能
//		if(isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)){
//			clearCacheFolder(MethodsCompat.getExternalCacheDir(this),System.currentTimeMillis());
//		}
	}	
	
	/**
	 * 清除缓存目录
	 * @param dir 目录
	 * @param numDays 当前系统时间
	 * @return
	 */
	private int clearCacheFolder(File dir, long curTime) {          
	    int deletedFiles = 0;         
	    if (dir!= null && dir.isDirectory()) {             
	        try {                
	            for (File child:dir.listFiles()) {    
	                if (child.isDirectory()) {              
	                    deletedFiles += clearCacheFolder(child, curTime);          
	                }  
	                if (child.lastModified() < curTime) {     
	                    if (child.delete()) {                   
	                        deletedFiles++;           
	                    }    
	                }    
	            }             
	        } catch(Exception e) {       
	            e.printStackTrace();    
	        }     
	    }       
	    return deletedFiles;     
	}
}

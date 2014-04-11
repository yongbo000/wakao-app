package me.wakao.app.common;

import java.io.File;

import me.wakao.app.MyApplication;
import me.wakao.app.ui.myfragment.MyDialogFragment;
import me.wakao.app.util.FileUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View.OnClickListener;


public class AppTool {
	
	public final static String BASE_IMG_URL = "http://bcs.duapp.com/wakao01/";
	
	public static String getCacheSize(Context mContext){
		// 计算缓存大小
		long fileSize = 0;
		String cacheSize = "0KB";
		File filesDir = mContext.getFilesDir();
		File cacheDir = mContext.getCacheDir();

		fileSize += FileUtils.getDirSize(filesDir);
		fileSize += FileUtils.getDirSize(cacheDir);
		// 2.2版本才有将应用缓存转移到sd卡的功能
//		if (AppContext.isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
//			File externalCacheDir = MethodsCompat.getExternalCacheDir(mContext);
//			fileSize += FileUtils.getDirSize(externalCacheDir);
//		}
		if (fileSize > 0)
			cacheSize = FileUtils.formatFileSize(fileSize);
		return cacheSize;
	}
	/**
	 * 清除app缓存
	 * 
	 * @param activity
	 */
	public static void clearAppCache(Activity mContext,final Handler handler) {
		final MyApplication ac = (MyApplication) mContext.getApplication();
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					ac.clearAppCache();
					msg.what = 1;
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}
	
	public static DialogFragment showDialog(FragmentActivity mContext, OnClickListener okbtnClickListener,String tipMsg) {
		FragmentTransaction ft = mContext.getSupportFragmentManager().beginTransaction();
	    Fragment prev = mContext.getSupportFragmentManager().findFragmentByTag("dialog");
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null);
	    DialogFragment newFragment = new MyDialogFragment(okbtnClickListener, tipMsg);
	    newFragment.setShowsDialog(true);
	    newFragment.show(ft, "dialog");
	    return newFragment;
	}
}

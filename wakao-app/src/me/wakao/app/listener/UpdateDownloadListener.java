package me.wakao.app.listener;

import android.content.Context;

import com.umeng.update.UmengDownloadListener;

public class UpdateDownloadListener implements UmengDownloadListener {
	private Context mContext;
	
	public UpdateDownloadListener(Context mContext){
		this.mContext = mContext;
	}
	
	@Override
    public void OnDownloadStart() {
    }

    @Override
    public void OnDownloadUpdate(int progress) {
    }

    @Override
    public void OnDownloadEnd(int result, String file) {
    }
}

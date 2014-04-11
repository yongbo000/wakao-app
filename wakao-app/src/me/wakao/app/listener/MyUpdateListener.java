package me.wakao.app.listener;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

public class MyUpdateListener implements UmengUpdateListener {
	

	private Context mContext;
	public MyUpdateListener(Context mContext){
		this.mContext = mContext;
	}
	@Override
	public void onUpdateReturned(int updateStatus,
			UpdateResponse updateInfo) {
		switch (updateStatus) {
		case 0: // has update
			Log.i("--->", "callback result");
			UmengUpdateAgent.showUpdateDialog(mContext, updateInfo);
			break;
		case 1: // has no update
			Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_SHORT)
					.show();
			break;
		case 2: // none wifi
			Toast.makeText(mContext, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT)
					.show();
			break;
		case 3: // time out
			Toast.makeText(mContext, "超时", Toast.LENGTH_SHORT)
					.show();
			break;
		case 4: // is updating
			/*Toast.makeText(mContext, "正在下载更新...", Toast.LENGTH_SHORT)
					.show();*/
			break;
		}
	}
	
}

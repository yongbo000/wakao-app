package me.wakao.app.ui;

import me.wakao.app.MyApplication;
import me.wakao.app.R;
import me.wakao.app.bean.UserObj;
import me.wakao.app.common.AppTool;
import me.wakao.app.common.BaiduSocialShareConfig;
import me.wakao.app.common.PropertyManger;
import me.wakao.app.listener.MyUpdateListener;
import me.wakao.app.listener.UpdateDownloadListener;

import com.baidu.social.core.BaiduSocialException;
import com.baidu.social.core.BaiduSocialListener;
import com.baidu.social.core.Utility;
import com.baidu.sociallogin.BaiduSocialLogin;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateStatus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ActivitySetting extends FragmentActivity {

	private final Handler handler = new Handler(Looper.getMainLooper());
	private BaiduSocialLogin socialLogin;
	private final static String appKey = BaiduSocialShareConfig.mbApiKey;

	private TextView ac;
	private TextView ac_desc;
	private TextView logout;
	private TextView cacheSize;
	
	private TextView checkUpdate;

	private UserObj user;

	private PropertyManger propertyManger;
	
	private Context mContext;
	
	private FeedbackAgent agent;

	private Handler clearCacheHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				Toast.makeText(ActivitySetting.this, "缓存清除成功",
						Toast.LENGTH_SHORT).show();
				cacheSize.setText("0KB");
			} else {
				Toast.makeText(ActivitySetting.this, "缓存清除失败",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		mContext = this;
		
		propertyManger = new PropertyManger(this);
		propertyManger.setProsPath(PropertyManger.PATH_USERINFO);

		initView();
		// 实例化baidu社会化登录，传入appkey
		socialLogin = BaiduSocialLogin.getInstance(this, appKey);
		// 设置支持新浪微博单点登录的appid
		socialLogin.supportWeiBoSso(BaiduSocialShareConfig.SINA_SSO_APP_KEY);
	}

	private void initView() {
		TextView tView = (TextView) findViewById(R.id.top_bar_title);
		ImageButton iButton = (ImageButton) findViewById(R.id.m_toggle);
		checkUpdate = (TextView) findViewById(R.id.checkUpdate);
		agent = new FeedbackAgent(this);
		agent.sync();

		ac_desc = (TextView) findViewById(R.id.account_settings_desc);
		ac = (TextView) findViewById(R.id.account_name);
		cacheSize = (TextView) findViewById(R.id.cache_size);
		logout = (TextView) findViewById(R.id.logout);

		tView.setText("设置");
		iButton.setVisibility(View.GONE);
		cacheSize.setText(AppTool.getCacheSize(this));
		
		checkUpdate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// 如果想程序启动时自动检查是否需要更新， 把下面两行代码加在Activity 的onCreate()函数里。
				com.umeng.common.Log.LOG = true;
				
				UmengUpdateAgent.setUpdateOnlyWifi(false); // 目前我们默认在Wi-Fi接入情况下才进行自动提醒。如需要在其他网络环境下进行更新自动提醒，则请添加该行代码
				UmengUpdateAgent.setUpdateAutoPopup(false);
				UmengUpdateAgent.setUpdateListener(new MyUpdateListener(mContext));
				UmengUpdateAgent.setDownloadListener(new UpdateDownloadListener(mContext));
				UmengUpdateAgent.forceUpdate(mContext);
			}
		});
	}

	private void initAccount() {
		user = ((MyApplication) getApplication()).getUserInfo();
		if (user == null) {
			ac_desc.setText("未登录");
			logout.setVisibility(View.GONE);
		} else {
			ac.setText(user.getWeiboName());
			ac_desc.setText("已登录");
			logout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	
	public void toLogin(View view) {
		if (user == null) {
			if (socialLogin.isAccessTokenValid(Utility.SHARE_TYPE_SINA_WEIBO)) {
				socialLogin.getUserInfoWithShareType(ActivitySetting.this,
						Utility.SHARE_TYPE_SINA_WEIBO, new UserInfoListener());
			} else {
				socialLogin.authorize(ActivitySetting.this,
						Utility.SHARE_TYPE_SINA_WEIBO, new UserInfoListener());
			}
		}
	}

	public void exitAccount() {
		((MyApplication) getApplication()).logoutAccount();
		user = null;
		socialLogin.cleanAllAccessToken();
		// 更新UI
		ac_desc.setText("未登录");
		ac.setText("我的帐号");
		logout.setVisibility(View.GONE);
	}

	private DialogFragment dialog;

	public void toLogout(View view) {
		dialog = AppTool.showDialog(ActivitySetting.this,
				new LogoutButtonClickListener(), "您确定要退出帐号吗？");
	}

	public void clearCache(View view) {
		AppTool.clearAppCache(this, clearCacheHandler);
	}
	public void toFeedback(View view) {
		agent.startFeedbackActivity();
	}
	public void toAboutUs(View view) {
		Intent intent = new Intent(mContext, ActivityAbout.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);
	}

	class LogoutButtonClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			exitAccount();
			dialog.dismiss();
		}
	}

	class UserInfoListener implements BaiduSocialListener {

		@Override
		public void onAuthComplete(Bundle values) {
			// TODO Auto-generated method stubis
		}

		@Override
		public void onApiComplete(String responses) {
			// TODO Auto-generated method stub
			Log.e("TAG", responses);
			JsonElement ele = new JsonParser().parse(responses);
			JsonObject obj = ele.getAsJsonObject();

			user = new UserObj();
			user.setWeiboId(obj.get("media_uid").getAsString());
			user.setFaceUrl(obj.get("tinyurl").getAsString());
			user.setWeiboName(obj.get("username").getAsString());

			if (user.getWeiboName() == null
					|| user.getWeiboName().length() == 0
					|| user.getWeiboId() == null
					|| user.getWeiboId().length() == 0
					|| user.getFaceUrl() == null
					|| user.getFaceUrl().length() == 0) {
				Toast.makeText(ActivitySetting.this, "授权失败,请重试",
						Toast.LENGTH_SHORT).show();
				user = null;
				return;
			}

			propertyManger.setProperty("user.name", user.getWeiboName());
			propertyManger.setProperty("user.media_uid", user.getWeiboId());
			propertyManger.setProperty("user.tinyurl", user.getFaceUrl());

			// 更新UI
			handler.post(new Runnable() {
				@Override
				public void run() {
					ac.setText(user.getWeiboName());
					ac_desc.setText("已登录");
					logout.setVisibility(View.VISIBLE);
					Toast.makeText(ActivitySetting.this, "登录成功",
							Toast.LENGTH_SHORT).show();
				}
			});

		}

		@Override
		public void onError(BaiduSocialException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(ActivitySetting.this, "授权失败,请重试",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		initAccount();
		MobclickAgent.onPageStart("Settings");
		MobclickAgent.onResume(this);
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("Settings");
		MobclickAgent.onPause(this);
	}
}

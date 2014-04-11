package me.wakao.app.ui;

import com.umeng.analytics.MobclickAgent;

import me.wakao.app.R;
import me.wakao.app.common.AppTool;
import me.wakao.app.util.HttpUtil;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

public class ActivityImage extends Activity {
	
	private WebView webView;
	
	
	private ImageButton toggle_btn;
	private ImageButton back_btn;
	private TextView title;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image);
		
		initView();
	}
	private void initView(){
		title = (TextView)findViewById(R.id.top_bar_title);
		toggle_btn = (ImageButton)findViewById(R.id.m_toggle);
		back_btn = (ImageButton)findViewById(R.id.go_back);
		webView = (WebView) findViewById(R.id.wv_image);
		title.setText("妹子图");
		back_btn.setVisibility(View.VISIBLE);
		toggle_btn.setVisibility(View.GONE);
		back_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		webView.getSettings().setJavaScriptEnabled(true);
		if(!HttpUtil.isNetworkConnected(this)){
			webView.loadUrl("file:///android_asset/error/index.html");
			return;
		}
		webView.loadUrl("http://apitest.wakao.me/test.html");
	}
	
	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("Meizitu");
		MobclickAgent.onResume(this);
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("Meizitu");
		MobclickAgent.onPause(this);
	}

	
}

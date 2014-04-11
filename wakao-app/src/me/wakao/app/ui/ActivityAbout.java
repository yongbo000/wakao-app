package me.wakao.app.ui;

import me.wakao.app.R;

import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityAbout extends Activity {

	private LinearLayout commentbar;
	private WebView detail;
	private TextView title;
	
	private ImageButton toggle_btn;
	private ImageButton back_btn;
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_detail);
		initView();
	}
	@SuppressLint("SetJavaScriptEnabled")
	private void initView(){
		detail = (WebView) findViewById(R.id.wv_article_detail);
		commentbar = (LinearLayout)findViewById(R.id.comment_bar);
		title = (TextView)findViewById(R.id.top_bar_title);
		toggle_btn = (ImageButton)findViewById(R.id.m_toggle);
		back_btn = (ImageButton)findViewById(R.id.go_back);
		
		
		detail.getSettings().setJavaScriptEnabled(true);
		detail.loadUrl("file:///android_asset/about.html");
		title.setText("关于我们");
		back_btn.setVisibility(View.VISIBLE);
		toggle_btn.setVisibility(View.GONE);
		commentbar.setVisibility(View.GONE);
		
		back_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("AboutActivity");
		MobclickAgent.onResume(this);
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("AboutActivity");
		MobclickAgent.onPause(this);
	}
}

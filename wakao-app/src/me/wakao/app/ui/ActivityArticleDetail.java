package me.wakao.app.ui;
import com.umeng.analytics.MobclickAgent;

import me.wakao.app.R;
import me.wakao.app.robot.ArticleRobot;
import me.wakao.app.robot.MyRobot;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityArticleDetail extends Activity {

	private LinearLayout commentbar;
	private WebView detail;
	private MyRobot robot;
	private int id;
	private String from;
	private TextView title;
	
	private ImageButton toggle_btn;
	private ImageButton back_btn;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				String data = (String) msg.obj;
				detail.loadUrl("javascript:setData('"+data+"');");
			} else {
				Log.e("TAG", "network error");
			}
		}
	};
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_detail);

		Intent intent = getIntent();
		id = intent.getIntExtra("id", 0);
		from = intent.getStringExtra("from");
		robot = new MyRobot();
		robot.setContext(this);
		
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
		detail.addJavascriptInterface(new JSObject(), "Tool");
		detail.loadUrl("file:///android_asset/detail_article.html");
		title.setText(from);
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
	public class JSObject {
		public void initPage() {
			new Thread(new Runnable() {
				@Override
				public void run() {
					String res = robot.fecthUrl(String.format(ArticleRobot.CONTENT_DATA_API_URL, id));
					if(res == null){
						handler.sendEmptyMessage(-1);
					} else {
						Message msg = new Message();
						msg.what = 1;
						msg.obj = res;
						handler.sendMessage(msg);
					}
				}
			}).start();
		}
	}
	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("ArticleDetail");
		MobclickAgent.onResume(this);
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ArticleDetail");
		MobclickAgent.onPause(this);
	}

}

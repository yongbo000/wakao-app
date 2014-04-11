package me.wakao.app.ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import me.wakao.app.MyApplication;
import me.wakao.app.R;
import me.wakao.app.bean.CommentObj;
import me.wakao.app.bean.FunnyObj;
import me.wakao.app.bean.UserObj;
import me.wakao.app.common.AppTool;
import me.wakao.app.robot.CommentRobot;
import me.wakao.app.robot.MyRobot;
import me.wakao.app.util.FileUtils;
import me.wakao.app.util.ImageUtils;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class ActivityFunnyDetail extends FragmentActivity {

	private LinearLayout topbar;
	private ImageView editImg;
	private ViewSwitcher mCommentBarViewSwitcher;
	private EditText editText;
	private Button pubComment;

	private WebView detail;
	private CommentRobot cRobot;
	private FunnyObj obj;

	private UserObj user;

	private InputMethodManager imm;

	private Handler mainHandler;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String data = null;
			if (msg.what == MyRobot.IS_GET_COMMENT_COMPLETE) {
				data = (String) msg.obj;
				detail.loadUrl("javascript:setComments(" + data + ");");
			} else if (msg.what == MyRobot.IS_ADD_COMMENT_COMPLETE) {
				pubComment.setClickable(true);
				pubComment.setText("评论");
				editText.setText("");
				imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

				data = (String) msg.obj;
				detail.loadUrl("javascript:addComment(" + data + ");");
			} else if (msg.what == MyRobot.IS_NETWORK_ERROR) {
				Log.e("TAG", "network error");
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_detail);

		Intent intent = getIntent();
		obj = (FunnyObj) intent.getSerializableExtra("funny");

		cRobot = new CommentRobot(handler);
		cRobot.setContext(this);

		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		mainHandler = new Handler(getMainLooper());
		initView();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		detail = (WebView) findViewById(R.id.wv_article_detail);
		topbar = (LinearLayout) findViewById(R.id.top_bar);
		mCommentBarViewSwitcher = (ViewSwitcher) findViewById(R.id.comment_bar_viewswitcher);
		editText = (EditText) findViewById(R.id.comment_editer);
		editImg = (ImageView) findViewById(R.id.edit_img);
		pubComment = (Button) findViewById(R.id.pubcomment);

		topbar.setVisibility(View.GONE);
		detail.setWebViewClient(new WebViewClient());
		detail.getSettings().setJavaScriptEnabled(true);
		detail.addJavascriptInterface(new JSObject(), "Tool");
		detail.loadUrl("file:///android_asset/detail_funny.html");
		// detail.getSettings().setBlockNetworkImage(true);
		// detail.setWebViewClient(new WebViewClient());
		

		pubComment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String commText = editText.getText().toString().trim();
				if (user == null) {
					AppTool.showDialog(ActivityFunnyDetail.this,
							new OKButtonClickListener(), "小伙伴，登录后才能评论呢！");
					return;
				}
				if (commText.length() == 0) {
					Toast.makeText(ActivityFunnyDetail.this, "小伙伴，你就说点什么吧",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (commText.length() > 0) {
					CommentObj comObj = new CommentObj();
					comObj.setUserId(user.getWeiboId());
					comObj.setFunnyId(Integer.toString(obj.getId()));
					comObj.setContent(commText);
					comObj.setUserName(user.getWeiboName());
					comObj.setAvatar(user.getFaceUrl());
					pubComment.setClickable(false);
					pubComment.setText("...");
					cRobot.addComment(comObj);
				}
			}
		});

		editImg.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mCommentBarViewSwitcher.showNext();
				editText.requestFocus();
				editText.requestFocusFromTouch();
			}
		});
		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					imm.showSoftInput(v, 0);
				} else {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
		});
		editText.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (mCommentBarViewSwitcher.getDisplayedChild() == 1) {
						mCommentBarViewSwitcher.setDisplayedChild(0);
						editText.clearFocus();
					}
					return true;
				}
				return false;
			}
		});
	}

	class OKButtonClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ActivityFunnyDetail.this,
					ActivitySetting.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
		}
	}

	public void showInWebView(final Bitmap bitmap) {
		mainHandler.post(new Runnable() {
			@Override
			public void run() {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				byte[] bytes = baos.toByteArray();
				String image64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
				detail.loadUrl("javascript:setPic('data:image/jpeg;base64,"
						+ image64.trim() + "')");
			}
		});
	}

	public void toDown() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Bitmap bitmap = MyRobot.downNetImage(obj.getPic());
				if (bitmap == null) {
					Log.e("TAG", "null bitmap");
					return;
				}
				showInWebView(bitmap);
				try {
					ImageUtils.saveImage(ActivityFunnyDetail.this,
							FileUtils.getFileName(obj.getPic()), bitmap);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	public class JSObject {
		public void initPage() {
			mainHandler.post(new Runnable() {
				@Override
				public void run() {
					Gson gson = new Gson();
					String json = gson.toJson(obj);
					detail.loadUrl("javascript:setFunny(" + json + ");");
				}
			});
		}

		public void loadPic() {
			// 是否存在配图
			if (obj.getPic().length() > 0) {
				Bitmap bitmap = ImageUtils.getBitmap(ActivityFunnyDetail.this,
						FileUtils.getFileName(obj.getPic()));
				if (bitmap == null) {
					Log.e("TAG", "downing");
					toDown();
				} else {
					Log.e("TAG", "exist");
					showInWebView(bitmap);
				}
			}
		}

		public void initComment() {
			new Thread(new Runnable() {
				@Override
				public void run() {
					cRobot.getComment(obj.getId());
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
		user = ((MyApplication) getApplication()).getUserInfo();
		MobclickAgent.onPageStart("FunnyDetail");
		MobclickAgent.onResume(this);
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("FunnyDetail");
		MobclickAgent.onPause(this);
	}
}

package me.wakao.app.ui;

import me.wakao.app.R;
import me.wakao.app.bean.CategoryItem;
import me.wakao.app.common.AppTool;
import me.wakao.app.ui.myfragment.MyFragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity2 extends SlidingFragmentActivity implements
		OnItemClickListener {

	private ImageButton toggleMenu;
	private int cur_selected;

	private MyFragmentManager myfm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frame_content);
		setBehindContentView(R.layout.frame_menu);

		configSlidingMenu();
		initUI();

		UmengUpdateAgent.update(this);
		Log.e("TAG", "MainActivity Create");
	}

	private void initUI() {
		toggleMenu = (ImageButton) findViewById(R.id.m_toggle);
		toggleMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toggle();
			}
		});
		myfm = new MyFragmentManager(this);
		fragmentChange(R.id.toHome);
	}

	private void configSlidingMenu() {
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidth(20);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffset(80);
		sm.setFadeDegree(0.35f);

		// 设置slding menu的几种手势模式
		// TOUCHMODE_FULLSCREEN 全屏模式，在content页面中，滑动，可以打开sliding menu
		// TOUCHMODE_MARGIN 边缘模式，在content页面中，如果想打开slding ,你需要在屏幕边缘滑动才可以打开slding
		// menu
		// TOUCHMODE_NONE 自然是不能通过手势打开啦
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
	}

	public void OnChannelSelected(View view) {
		if(view.getId() == R.id.toExit){
			AppTool.showDialog(MainActivity2.this,
					new ExitButtonClickListener(), "您确定要退出吗？");
			return;
		}
		if(view.getId() == R.id.toGirl){
			myfm.doSlide(view.getId());
			return;
		}
		
		if (view.getId() == cur_selected) {
			toggle();
			return;
		}
		fragmentChange(view.getId());
		toggle();
	}

	public void toSetting(View view) {
		Intent intent = new Intent(this, ActivitySetting.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	/**
	 * 监听返回--是否退出程序
	 */
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		SlidingMenu sm = getSlidingMenu();
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& !sm.isMenuShowing()) {
			showMenu();
//			if(cur_selected == R.id.toHome){
//				AppTool.showDialog(MainActivity2.this,
//						new ExitButtonClickListener(), "您确定要退出吗？");
//			} else {
//				
//			}
		} else if (keyCode == KeyEvent.KEYCODE_BACK && sm.isMenuShowing()) {
			showContent();
		}
		return true;
	}

	class ExitButtonClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			System.exit(0);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("Home");
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("Home");
		MobclickAgent.onPause(this);
	}
	
	public void fragmentChange(int fragmentId){
		myfm.doSlide(fragmentId);
		cur_selected = fragmentId;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ListView lv = (ListView) parent;
		CategoryItem item = (CategoryItem) lv.getItemAtPosition(position);
		switch (item.getIcon()) {
		case R.drawable.xiao:
			fragmentChange(R.id.toFunny);
			break;
		case R.drawable.zhang:
			fragmentChange(R.id.toArticle);
			break;
		case R.drawable.qiu:
			fragmentChange(R.id.toArticle_NBA);
			break;
		case R.drawable.girl:
			fragmentChange(R.id.toGirl);
			break;
		default:
			break;
		}
	}
}
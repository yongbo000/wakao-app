package me.wakao.app.ui.myfragment;

import java.util.ArrayList;
import java.util.List;

import me.wakao.app.R;
import me.wakao.app.ui.ActivityImage;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.TextView;

public class MyFragmentManager {
	private Fragment funnyFragment;
	private Fragment articleFragment;
	private Fragment NBAFragment;
	private Fragment homeFragment;
	
	
	private List<Fragment> fmList = new ArrayList<Fragment>();
	
	private FragmentManager fragmentManager;
	
	private FragmentActivity mContext;
	private TextView barTitle;
	public MyFragmentManager(FragmentActivity mContext){
		this.mContext = mContext;
		this.barTitle = (TextView)(mContext.findViewById(R.id.top_bar_title));
		this.fragmentManager = mContext.getSupportFragmentManager();
	}
	public void hideFragment(FragmentTransaction fragmentTransaction, Fragment cur_fragment) {
		for (Fragment fm : fmList) {
			if(fm != cur_fragment) {
				fragmentTransaction.hide(fm);
			}
		}
	}
	public void doSlide(int rid){
		switch (rid) {
		case R.id.toHome:
			toHome();
			break;
		case R.id.toFunny:
			toFunny();
			break;
		case R.id.toArticle:
			toArticle();
			break;
		case R.id.toArticle_NBA:
			toNBA();
			break;
		case R.id.toGirl:
			toGirl();
			break;
		default:
			break;
		}
	}
	public void toHome() {
		barTitle.setText("首页");
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		homeFragment = (HomeFragment) fragmentManager
				.findFragmentByTag("homeFragment");
		hideFragment(fragmentTransaction, homeFragment);
		if (homeFragment == null) {
			homeFragment = new HomeFragment();
			fmList.add(homeFragment);
			Log.e("TAG", "new homeFragment");
			
			fragmentTransaction.add(R.id.listview_area, homeFragment,
					"homeFragment").commit();
			return;
		}
		fragmentTransaction.show(homeFragment).commit();
	}
	public void toFunny() {
		barTitle.setText("笑务处");
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		funnyFragment = (FunnyFragment) fragmentManager
				.findFragmentByTag("funnyFragment");
		hideFragment(fragmentTransaction, funnyFragment);
		if (funnyFragment == null) {
			funnyFragment = new FunnyFragment();
			fmList.add(funnyFragment);
			Log.e("TAG", "new FunnyFragment");
			
			fragmentTransaction.add(R.id.listview_area, funnyFragment,
					"funnyFragment").commit();
			return;
		}
		fragmentTransaction.show(funnyFragment).commit();
	}
	public void toArticle() {
		barTitle.setText("涨姿势");
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		articleFragment = (ArticleFragment) fragmentManager
				.findFragmentByTag("articleFragment");
		hideFragment(fragmentTransaction, articleFragment);
		if (articleFragment == null) {
			articleFragment = new ArticleFragment("Recommend");
			fmList.add(articleFragment);
			Log.e("TAG", "new ArticleFragment");
			
			fragmentTransaction.add(R.id.listview_area, articleFragment,
					"articleFragment").commit();
			return;
		}
		fragmentTransaction.show(articleFragment).commit();
	}
	public void toNBA() {
		barTitle.setText("热血篮球");
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		NBAFragment = (ArticleFragment) fragmentManager
				.findFragmentByTag("NBAFragment");
		hideFragment(fragmentTransaction, NBAFragment);
		if (NBAFragment == null) {
			NBAFragment = new ArticleFragment("NBA");
			fmList.add(NBAFragment);
			Log.e("TAG", "new NBAFragment");
			
			fragmentTransaction.add(R.id.listview_area, NBAFragment,
					"NBAFragment").commit();
			return;
		}
		fragmentTransaction.show(NBAFragment).commit();
	}
	public void toGirl() {
		Intent intent = new Intent(mContext, ActivityImage.class);
		mContext.startActivity(intent);
		mContext.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}
}

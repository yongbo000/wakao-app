package me.wakao.app.ui.myfragment;

import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.UnderlinePageIndicator;

import me.wakao.app.R;
import me.wakao.app.adapter.HomePageAdapter;
import me.wakao.app.adapter.ListviewHomeCategoryAdapter;
import me.wakao.app.bean.ArticleObj;
import me.wakao.app.bean.CategoryItem;
import me.wakao.app.common.HandlerFactory;
import me.wakao.app.robot.HomeRobot;
import me.wakao.app.ui.MainActivity2;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

public class HomeFragment extends Fragment {

	private ViewPager mPager;
	private PageIndicator mIndicator;

	public HomeRobot robot;
	
	private static List<CategoryItem> categorys = new ArrayList<CategoryItem>();
	static {
		categorys.add(new CategoryItem(R.drawable.girl, "妹子图库，各种小清新、萝莉、萌妹子等你发现"));
		categorys.add(new CategoryItem(R.drawable.xiao, "数据来自糗事百科、我们都爱冷笑话、头条网等等"));
		categorys.add(new CategoryItem(R.drawable.zhang, "鬼脚七、南方周末、多看阅读、骑行西藏、我们爱历史...等微信公众号文章"));
		categorys.add(new CategoryItem(R.drawable.qiu, "篮球专题，虎扑数据正在增加中..."));
	}

	public HomeFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		List<ArticleObj> dataArray = new ArrayList<ArticleObj>();
		
		LinearLayout homeLayout = (LinearLayout) inflater.inflate(
				R.layout.home_page, null);
		ListView navs = (ListView)homeLayout.findViewById(R.id.navs);
		ListviewHomeCategoryAdapter navsAdapter = new ListviewHomeCategoryAdapter(getActivity(), categorys, R.layout.listitem_home_category);
		navs.setOnItemClickListener((MainActivity2)getActivity());
		navs.setAdapter(navsAdapter);
		
		
		// 图片轮播
		HomePageAdapter mAdapter = new HomePageAdapter(getActivity()
				.getSupportFragmentManager(), dataArray);
		
		Handler handler = HandlerFactory.createOfferHandler(getActivity(), mAdapter);
		robot = new HomeRobot(handler, dataArray);
		robot.setContext(getActivity());
		robot.setChannel("offer");
		robot.setCacheDir(getActivity().getCacheDir().getAbsolutePath());
		
		
		List<ArticleObj> objs = (List<ArticleObj>) robot.readFromCache("offer");
		if(objs != null && objs.size() > 0) {
			dataArray.clear();
			dataArray.addAll(objs);
		} else {
			Log.e("TAG", "没有数据");
		}
		
		
		mPager = (ViewPager) homeLayout.findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mIndicator = (UnderlinePageIndicator) homeLayout
				.findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
		
		robot.onRefresh();
		Log.e("TAG", "HomeFragment create");
		return homeLayout;
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("HomeFragment");
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("HomeFragment");
	}
}

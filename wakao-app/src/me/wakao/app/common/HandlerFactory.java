package me.wakao.app.common;

import java.util.Date;


import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import me.wakao.app.R;
import me.wakao.app.robot.MyRobot;
import me.wakao.app.widget.PullToRefreshListView;

public class HandlerFactory {
	public static String pull_to_refresh_update = "最近更新：";

	public static Handler createListviewHandler(
			final PullToRefreshListView listview,
			final LinearLayout footerLayout, final BaseAdapter adapter) {
		return new Handler() {
			
			TextView tv = (TextView) footerLayout
					.findViewById(R.id.listview_foot_more);
			ProgressBar bar = (ProgressBar) footerLayout
					.findViewById(R.id.listview_foot_progress);

			@Override
			public void handleMessage(Message msg) {
				Log.i("TAG", "dealing...code:"+ msg.what);
				switch (msg.what) {
				case MyRobot.IS_NETWORK_LOADING:
					bar.setVisibility(View.VISIBLE);
					tv.setText("正在加载...");
					break;
				case MyRobot.IS_REFRESH_COMPLETE:
					adapter.notifyDataSetChanged();
					listview.setSelection(0);
					listview.onRefreshComplete(pull_to_refresh_update
							+ new Date().toLocaleString());
					bar.setVisibility(View.GONE);
					tv.setText("上拉松开加载更多");
					break;
				case MyRobot.IS_GETMORE_COMPLETE:
					adapter.notifyDataSetChanged();
					bar.setVisibility(View.GONE);
					tv.setText("上拉松开加载更多");
					break;
				case MyRobot.IS_NETWORK_ERROR:
					Toast.makeText(listview.getContext(), "网络连接失败",
							Toast.LENGTH_SHORT).show();
					break;
				case MyRobot.IS_REFRESH_ERROR:
					Toast.makeText(listview.getContext(), "数据刷新失败",
							Toast.LENGTH_SHORT).show();
					listview.setSelection(0);
					listview.onRefreshComplete(pull_to_refresh_update
							+ new Date().toLocaleString());
					break;
				case MyRobot.IS_GETMORE_ERROR:
					Toast.makeText(listview.getContext(), "网络连接失败",
							Toast.LENGTH_SHORT).show();
					bar.setVisibility(View.GONE);
					tv.setText("点击刷新");
					break;
				case MyRobot.LOAD_DATA_ALL:
					bar.setVisibility(View.GONE);
					tv.setText("没有更多数据了");
					break;
				case MyRobot.CLEAR_DATA:
					adapter.notifyDataSetChanged();
					break;
				default:
					Log.i("TAG", msg.what+ "no deal...");
					break;
				}
			}

		};
	}
	public static Handler createOfferHandler(final Activity context, final FragmentPagerAdapter adapter) {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MyRobot.IS_NETWORK_LOADING:
					break;
				case MyRobot.IS_REFRESH_COMPLETE:
					adapter.notifyDataSetChanged();
					break;
				case MyRobot.IS_NETWORK_ERROR:
					Toast.makeText(context, "网络连接失败",
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}

		};
	}
}

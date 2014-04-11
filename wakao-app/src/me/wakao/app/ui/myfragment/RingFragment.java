package me.wakao.app.ui.myfragment;

import java.util.ArrayList;
import java.util.List;

import me.wakao.app.R;

import me.wakao.app.adapter.ListviewRingAdapter;
import me.wakao.app.bean.RingObj;
import me.wakao.app.common.HandlerFactory;
import me.wakao.app.robot.RingRobot;
import me.wakao.app.widget.PullToRefreshListView;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * 
 * @author <a href="mailto:kris@krislq.com">Kris.lee</a>
 * @since Mar 12, 2013
 * @version 1.0.0
 */
public class RingFragment extends Fragment {

	public RingRobot robot;

	public RingFragment() {
	}

	public RingRobot getRobot() {
		return robot;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// inflater the layout
		List<RingObj> dataArray = new ArrayList<RingObj>();

		PullToRefreshListView listview = (PullToRefreshListView) inflater
				.inflate(R.layout.common_listview, null);

		LinearLayout footerLayout = (LinearLayout)inflater.inflate(R.layout.listview_footer, null);
		listview.addFooterView(footerLayout);

		ListviewRingAdapter adapter = new ListviewRingAdapter(getActivity(),
				dataArray, R.layout.listitem_ring);

		Handler handler = HandlerFactory.createListviewHandler(listview,
				footerLayout, adapter);

		robot = new RingRobot(listview, handler, dataArray);
		robot.setContext(getActivity());

		listview.setAdapter(adapter);
		listview.setOnScrollListener(robot);
		listview.setOnRefreshListener(robot);
		Log.e("TAG", "RingFragment create");
		return listview;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (robot.getRingData().size() == 0) {
			robot.onRefresh();
		}
	}

}

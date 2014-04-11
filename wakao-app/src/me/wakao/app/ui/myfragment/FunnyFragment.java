package me.wakao.app.ui.myfragment;

import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;

import me.wakao.app.R;

import me.wakao.app.adapter.ListviewFunnyAdapter;
import me.wakao.app.bean.FunnyObj;
import me.wakao.app.common.HandlerFactory;
import me.wakao.app.robot.FunnyRobot;
import me.wakao.app.ui.MainActivity2;
import me.wakao.app.widget.PullToRefreshListView;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class FunnyFragment extends Fragment implements OnClickListener {

	public FunnyRobot robot;
	
	public FunnyFragment() {}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflater the layout
        List<FunnyObj> funnyData = new ArrayList<FunnyObj>();
        LinearLayout wrapLayout = (LinearLayout)inflater.inflate(R.layout.funnyfragment, null);
        RelativeLayout footerbar = (RelativeLayout)wrapLayout.findViewById(R.id.footer_bar);
        initFooterBar(footerbar);
        PullToRefreshListView listview = (PullToRefreshListView)wrapLayout.findViewById(R.id.funnylistview);
        
        LinearLayout footerLayout = (LinearLayout)inflater.inflate(R.layout.listview_footer, null);
        footerLayout.setOnClickListener(this);
        listview.addFooterView(footerLayout);
        
        ListviewFunnyAdapter adapter = new ListviewFunnyAdapter(getActivity(), funnyData, R.layout.listitem_funny);
        
		Handler handler = HandlerFactory.createListviewHandler(listview, footerLayout, adapter);
		
		robot = new FunnyRobot(listview, handler, funnyData);
		robot.setContext(getActivity());
		robot.setCacheDir(getActivity().getCacheDir().getAbsolutePath());
		
		listview.setAdapter(adapter);
		listview.setOnScrollListener(robot);
		listview.setOnRefreshListener(robot);
		Log.e("TAG", "FunnyFragment create");
		
        return wrapLayout;
    }
    private void initFooterBar(RelativeLayout footerbar){
    	TextView new_btn = (TextView)footerbar.findViewById(R.id.new_btn);
    	TextView hot_btn = (TextView)footerbar.findViewById(R.id.hot_btn);
    	ImageButton home_btn = (ImageButton)footerbar.findViewById(R.id.home_btn);
    	new_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				robot.setMode(FunnyRobot.MODE_NEW);
				//robot.onRefresh();
				robot.clickRefresh();
			}
		});
    	hot_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				robot.setMode(FunnyRobot.MODE_HOT);
				//robot.onRefresh();
				robot.clickRefresh();
			}
		});
    	home_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity2)getActivity()).fragmentChange(R.id.toHome);
			}
		});
    }
    
    @Override
	public void onResume() {
		super.onResume();
		List<FunnyObj> objs = (List<FunnyObj>)robot.readFromCache("funny");
    	if(robot.getFunnyData().size() == 0) {
    		if(objs != null) {
    			robot.initData(objs);
    		} else {
    			robot.onRefresh();
    		}
    	}
    	MobclickAgent.onPageStart("FunnyFragment");
	}
    
    @Override
	public void onClick(View v) {
    	robot.onRefresh();
	}
    @Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("FunnyFragment");
	}
}

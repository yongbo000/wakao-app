package me.wakao.app.adapter;

import java.util.List;


import me.wakao.app.R;
import me.wakao.app.bean.RingObj;
import me.wakao.app.robot.MediaPlayerRobot;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ListviewRingAdapter extends BaseAdapter {
	private Context mContext;
	private List<RingObj> objs;
	private int itemViewResource;
	private LayoutInflater listContainer;


	private MediaPlayerRobot mRobot;


	private int sel_position = -1;
	
	static class ListItemView { // 自定义控件集合
		public TextView duration;
		public TextView name;
		public TextView artist;
		public TextView loadingText;
		public ToggleButton toggle_play_pause;
	}

	public ListviewRingAdapter(Context mContext, List<RingObj> objs,
			int resource) {
		this.mContext = mContext;
		this.objs = objs;
		this.itemViewResource = resource;
		this.listContainer = LayoutInflater.from(mContext);
		this.mRobot = new MediaPlayerRobot(mContext);
	}

	@Override
	public int getCount() {
		return objs.size();
	}

	@Override
	public Object getItem(int position) {
		return objs.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// 自定义视图
		ListItemView itemView = null;
		RingObj obj = objs.get(position);
		if (convertView == null) {
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);

			itemView = new ListItemView();
			itemView.name = (TextView) convertView.findViewById(R.id.name);
			itemView.artist = (TextView) convertView.findViewById(R.id.artist);
			itemView.duration = (TextView) convertView
					.findViewById(R.id.duration);
			itemView.loadingText = (TextView) convertView
					.findViewById(R.id.loading_txt);
			itemView.toggle_play_pause = (ToggleButton) convertView
					.findViewById(R.id.toggle_play_pause);

			// 设置控件集到convertView
			convertView.setTag(itemView);
		} else {
			itemView = (ListItemView) convertView.getTag();
		}

		itemView.name.setText(obj.getName());
		itemView.artist.setText(obj.getArtist());
		itemView.duration.setText(obj.getDuration() + "秒");
		itemView.toggle_play_pause.setId(position);
		
		if (position == sel_position) {
			itemView.toggle_play_pause.setChecked(true);
		} else {
			itemView.toggle_play_pause.setChecked(false);
			itemView.toggle_play_pause.setText("");
			itemView.toggle_play_pause.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ToggleButton tbButton = (ToggleButton) v;
					Handler handler = new MyHandler(tbButton);
					int id = tbButton.getId();
					Log.e("TAG", "id:"+id);
					if (id == sel_position) {
						if (tbButton.isChecked()) {
							mRobot.tiggerMediaPlay();
						} else {
							mRobot.tiggerMediaPause();
						}
						return;
					}
					if (tbButton.isChecked()) {
						if (sel_position != -1) {
							ToggleButton t = (ToggleButton) ((Activity) mContext)
									.findViewById(sel_position);
							if (t != null) {
								t.setChecked(false);
							}
						}
						mRobot.reset(handler).setRing(objs.get(id)).tiggerMediaPlay();
					} else {
						mRobot.tiggerMediaPause();
					}
					sel_position = v.getId();
				}
			});

		}
		return convertView;
	}
	
	static class MyHandler extends Handler {
		private ToggleButton tb;
		private int id;
		public MyHandler(ToggleButton tb){
			this.tb = tb;
			this.id = tb.getId();
		}
		
		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			if(what == MediaPlayerRobot.MEDIA_LOADING){
				Log.e("TAG", "MEDIA_LOADING");
				tb.setBackgroundResource(0);
				tb.setText("0.0%");
				tb.setClickable(false);
			} else if(what == MediaPlayerRobot.MEDIA_START) {
				Log.e("TAG", "MEDIA_START");
				tb.setText("");
				tb.setBackgroundResource(R.drawable.toggle_music);
				tb.setClickable(true);
				tb.setChecked(true);
			} else if(what == MediaPlayerRobot.MEDIA_END){
				Log.e("TAG", "MEDIA_END");
				tb.setChecked(false);
			} else if(what == MediaPlayerRobot.MEDIA_LOADING_PROCESS_UPDATE){
				Log.e("TAG", "MEDIA_LOADING_PROCESS_UPDATE");
				String txt = (String)msg.obj;
				Log.e("TAG", txt);
				tb.setText(txt);
			} else if(what == MediaPlayerRobot.MEDIA_RESET){
				tb.setText("");
				tb.setBackgroundResource(R.drawable.toggle_music);
				tb.setClickable(true);
			}
		}
	}
}

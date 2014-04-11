package me.wakao.app.adapter;

import java.util.List;


import me.wakao.app.R;
import me.wakao.app.bean.FunnyObj;
import me.wakao.app.ui.ActivityFunnyDetail;
import me.wakao.app.util.BitmapManager;
import me.wakao.app.util.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListviewFunnyAdapter extends BaseAdapter {
	private Context mContext;
	private List<FunnyObj> objs;
	private int itemViewResource;
	private LayoutInflater listContainer;
	private BitmapManager bmpManager;

	static class ListItemView { // 自定义控件集合
		public TextView content;
		public TextView comment_count;
		public TextView createtime;
		public TextView from;
		public ImageView pic;
		public LinearLayout item;
	}
	public class OnFunnyClick implements OnClickListener {
		private FunnyObj obj;
		public OnFunnyClick(FunnyObj obj){
			this.obj = obj;
		}
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext, ActivityFunnyDetail.class);
			//intent.putExtra("id", obj.getId());
			intent.putExtra("funny", obj);
			mContext.startActivity(intent);
			((Activity)mContext).overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
		}
	}
	public ListviewFunnyAdapter(Context mContext, List<FunnyObj> objs,
			int resource) {
		this.mContext = mContext;
		this.objs = objs;
		this.itemViewResource = resource;
		this.listContainer = LayoutInflater.from(mContext);
		this.bmpManager = new BitmapManager();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// 自定义视图
		ListItemView itemView = null;

		if (convertView == null) {
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);

			itemView = new ListItemView();
			itemView.item = (LinearLayout) convertView
					.findViewById(R.id.content_layout);
			itemView.content = (TextView) convertView
					.findViewById(R.id.content);
			itemView.comment_count = (TextView) convertView
					.findViewById(R.id.num_comments);
			itemView.createtime = (TextView) convertView
					.findViewById(R.id.display_time);
			itemView.from = (TextView) convertView
					.findViewById(R.id.from);
			itemView.pic = (ImageView) convertView
					.findViewById(R.id.pic);
			// 设置控件集到convertView
			convertView.setTag(itemView);
		} else {
			itemView = (ListItemView) convertView.getTag();
		}

		FunnyObj obj = objs.get(position);
		
		itemView.item.setOnClickListener(new OnFunnyClick(obj));
		itemView.content.setText(obj.getContent());
		itemView.comment_count.setText(Integer.toString(obj.getCommentCount()));
		itemView.createtime.setText(obj.getCreatetime());
		itemView.from.setText(obj.getFrom());
		if(obj.getPic() != null && !StringUtils.isEmpty(obj.getPic())){
			String pic = obj.getPic();
			String s_pic = pic.replace("medium", "small");
//			Log.e("TAG", "s_pic:"+s_pic);
			itemView.pic.setVisibility(View.VISIBLE);
			itemView.pic.setTag(s_pic);
			bmpManager.loadBitmap(s_pic, itemView.pic, BitmapFactory
					.decodeResource(mContext.getResources(),
							R.drawable.image_loading));
		} else {
			itemView.pic.setVisibility(View.GONE);
		}
		return convertView;
	}
}

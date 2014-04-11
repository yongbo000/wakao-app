package me.wakao.app.adapter;

import java.util.List;

import me.wakao.app.R;
import me.wakao.app.bean.CategoryItem;
import me.wakao.app.util.BitmapManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListviewHomeCategoryAdapter extends BaseAdapter {
	private Context mContext;
	private List<CategoryItem> objs;
	private int itemViewResource;
	private LayoutInflater listContainer;
	private BitmapManager bmpManager;

	static class ListItemView { // 自定义控件集合
//		public TextView content;
//		public LinearLayout item;
//		public TextView createtime;
//		public TextView from;
//		public TextView title;
		public TextView desc;
		public ImageView icon;
	}
	public ListviewHomeCategoryAdapter(Context mContext, List<CategoryItem> objs,
			int resource) {
		this.mContext = mContext;
		this.objs = objs;
		this.itemViewResource = resource;
		this.listContainer = LayoutInflater.from(mContext);
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 自定义视图
		ListItemView itemView = null;

		if (convertView == null) {
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);

			itemView = new ListItemView();
//			itemView.item = (LinearLayout) convertView
//					.findViewById(R.id.content_layout);
//			itemView.title = (TextView) convertView
//					.findViewById(R.id.channel_name);
			itemView.desc = (TextView) convertView
					.findViewById(R.id.desc);
//			itemView.createtime = (TextView) convertView
//					.findViewById(R.id.time);
//			itemView.from = (TextView) convertView
//					.findViewById(R.id.from);
			itemView.icon = (ImageView) convertView
					.findViewById(R.id.cat_icon);
			// 设置控件集到convertView
			convertView.setTag(itemView);
		} else {
			itemView = (ListItemView) convertView.getTag();
		}
		CategoryItem category = objs.get(position);
		itemView.desc.setText(category.getDesc());
		itemView.icon.setBackgroundResource(category.getIcon());
		return convertView;
	}
	
}

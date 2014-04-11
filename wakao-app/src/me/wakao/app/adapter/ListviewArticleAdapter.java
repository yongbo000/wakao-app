package me.wakao.app.adapter;

import java.util.List;

import me.wakao.app.R;
import me.wakao.app.bean.ArticleObj;
import me.wakao.app.common.AppTool;
import me.wakao.app.listener.OnArticleItemClickListener;
import me.wakao.app.util.BitmapManager;
import me.wakao.app.util.StringUtils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListviewArticleAdapter extends BaseAdapter {
	private Context mContext;
	private List<ArticleObj> objs;
	private int itemViewResource;
	private LayoutInflater listContainer;
	private BitmapManager bmpManager;

	static class ListItemView { // 自定义控件集合
		public TextView content;
		//public TextView comment_count;
		public LinearLayout item;
		public TextView createtime;
		public TextView from;
		public TextView title;
		public TextView intro;
		public ImageView pic;
	}
	public ListviewArticleAdapter(Context mContext, List<ArticleObj> objs,
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
			itemView.title = (TextView) convertView
					.findViewById(R.id.title);
			itemView.intro = (TextView) convertView
					.findViewById(R.id.intro);
//			itemView.comment_count = (TextView) convertView
//					.findViewById(R.id.num_comments);
			itemView.createtime = (TextView) convertView
					.findViewById(R.id.time);
			itemView.from = (TextView) convertView
					.findViewById(R.id.from);
			itemView.pic = (ImageView) convertView
					.findViewById(R.id.pic);
			// 设置控件集到convertView
			convertView.setTag(itemView);
		} else {
			itemView = (ListItemView) convertView.getTag();
		}

		ArticleObj obj = objs.get(position);
		
		itemView.item.setOnClickListener(new OnArticleItemClickListener(obj, mContext));
		itemView.title.setText(obj.getTitle());
		itemView.intro.setText(obj.getIntro());
		itemView.createtime.setText(obj.getCreatetime());
		itemView.from.setText(obj.getFrom());
		//itemView.comment_count.setText(Integer.toString(obj.getCommentCount()));
		if(obj.getPic() != null && !StringUtils.isEmpty(obj.getPic())){
			String imgurl = AppTool.BASE_IMG_URL + obj.getPic();
			itemView.pic.setVisibility(View.VISIBLE);
			itemView.pic.setTag(imgurl);
			bmpManager.loadBitmap(imgurl, itemView.pic, BitmapFactory
					.decodeResource(mContext.getResources(),
							R.drawable.image_loading));
		} else {
			itemView.pic.setVisibility(View.GONE);
		}
		return convertView;
	}
}

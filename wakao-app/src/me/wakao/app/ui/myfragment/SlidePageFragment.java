package me.wakao.app.ui.myfragment;

import me.wakao.app.R;
import me.wakao.app.bean.ArticleObj;
import me.wakao.app.common.AppTool;
import me.wakao.app.listener.OnArticleItemClickListener;
import me.wakao.app.util.BitmapManager;
import me.wakao.app.util.StringUtils;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public final class SlidePageFragment extends Fragment {
	
	private ArticleObj obj;
	private BitmapManager bmpManager;
	
	static class ListItemView { // 自定义控件集合
		public TextView content;
		public RelativeLayout item;
		public TextView createtime;
		public TextView from;
		public TextView title;
		public ImageView pic;
	}
	
	public SlidePageFragment(ArticleObj obj){
		this.obj = obj;
		this.bmpManager = new BitmapManager();
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	RelativeLayout page = (RelativeLayout)inflater.inflate(R.layout.listitem_page, null);
    	
    	ListItemView itemView = new ListItemView();
		itemView.item = (RelativeLayout) page
				.findViewById(R.id.content_layout);
		itemView.title = (TextView) page
				.findViewById(R.id.title);
//		itemView.createtime = (TextView) page
//				.findViewById(R.id.time);
//		itemView.from = (TextView) page
//				.findViewById(R.id.from);
		itemView.pic = (ImageView) page
				.findViewById(R.id.pic);
    	
    	
    	itemView.item.setOnClickListener(new OnArticleItemClickListener(obj, getActivity()));
//		itemView.createtime.setText(obj.getCreatetime());
//		itemView.from.setText(obj.getFrom());
		itemView.title.setText(obj.getTitle());
		if(obj.getPic() != null && !StringUtils.isEmpty(obj.getPic())){
			String imgurl = AppTool.BASE_IMG_URL + obj.getPic();
			itemView.pic.setVisibility(View.VISIBLE);
			itemView.pic.setTag(imgurl);
			bmpManager.loadBitmap(imgurl, itemView.pic, BitmapFactory
					.decodeResource(getActivity().getResources(),
							R.drawable.image_loading));
		} else {
			itemView.pic.setVisibility(View.GONE);
		}
        return page;
    }
}

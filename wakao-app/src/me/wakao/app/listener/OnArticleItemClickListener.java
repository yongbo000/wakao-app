package me.wakao.app.listener;

import me.wakao.app.R;
import me.wakao.app.bean.ArticleObj;
import me.wakao.app.ui.ActivityArticleDetail;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class OnArticleItemClickListener implements OnClickListener {

	private ArticleObj obj;
	private Context mContext;
	public OnArticleItemClickListener(ArticleObj obj,Context mContext){
		this.obj = obj;
		this.mContext = mContext;
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(mContext, ActivityArticleDetail.class);
		intent.putExtra("id", obj.getId());
		intent.putExtra("from", obj.getFrom());
		mContext.startActivity(intent);
		((Activity)mContext).overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);
	}

}

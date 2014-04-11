package me.wakao.app.adapter;


import java.util.List;

import me.wakao.app.bean.ArticleObj;
import me.wakao.app.ui.myfragment.SlidePageFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HomePageAdapter extends FragmentPagerAdapter {

    private List<ArticleObj> objs;

    public HomePageAdapter(FragmentManager fm, List<ArticleObj> objs) {
        super(fm);
        this.objs = objs;
    }
    
    @Override
    public Fragment getItem(int position) {
        return new SlidePageFragment(objs.get(position));
    }

    @Override
    public int getCount() {
        return objs.size();
    }
}

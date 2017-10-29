package com.zeyuan.kyq.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.zeyuan.kyq.app.BasePage;

import java.util.List;

/**
 * Created by Administrator on 2015/12/31.
 */
public class MainPagerAdapter extends PagerAdapter {
    List<BasePage> mViews;
    int size;

    public MainPagerAdapter(List<BasePage> views) {
        mViews = views;
        size = mViews.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = mViews.get(position % size).getRootView();
        ((ViewPager) container).removeView(view);
        object = null;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        View v = mViews.get(position % size).getRootView();
//            try {
        ((ViewPager) container).addView(v);
//            } catch (Exception e) {
//                 TODO: handle exception
//            }
        return v;
    }

    @Override
    public int getCount() {
//        return 40;
            return mViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

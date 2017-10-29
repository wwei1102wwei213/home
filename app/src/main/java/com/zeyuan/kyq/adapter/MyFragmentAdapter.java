package com.zeyuan.kyq.adapter;

/**
 * Created by Administrator on 2016/3/4.
 * <p>
 * Fragment适配器
 *
 * @user wwei
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MyFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;
    private String[] titles;

    public MyFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public MyFragmentAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragments, String[] titles) {
        super(fragmentManager);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragments.get(arg0);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void update(ArrayList<Fragment> fragments) {
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null && titles.length > position) {
            return titles[position];
        }
        return "";
    }
}

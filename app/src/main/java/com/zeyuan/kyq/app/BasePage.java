package com.zeyuan.kyq.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;


/**
 * Created by Administrator on 2015/10/23.
 * 这个是page里面抽出的共性
 *
 * 其实就是一个view
 */
public abstract class BasePage {
    protected Context context;
    protected View rootView;
    public BasePage(Context context) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        rootView =getView (inflater);
        initView(rootView);
    }

    public abstract View getView (LayoutInflater inflater);

    public abstract void initView (View rootView);


    public View getRootView() {
        return  rootView;
    }

    public View findViewById(int id) {
        return rootView.findViewById(id);
    }

    public abstract void initData();

}

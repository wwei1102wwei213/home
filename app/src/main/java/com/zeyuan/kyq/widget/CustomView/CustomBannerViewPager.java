package com.zeyuan.kyq.widget.CustomView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/1/12.
 *
 *
 *
 * @author wwei
 */
public class CustomBannerViewPager extends ViewPager {

    public CustomBannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomBannerViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(ev);
    }

    private ViewGroup parent;

    public void setParent(ViewGroup parent) {
        this.parent = parent;
    }

}

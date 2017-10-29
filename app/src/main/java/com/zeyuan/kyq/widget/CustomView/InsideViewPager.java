package com.zeyuan.kyq.widget.CustomView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 此ViewPager解决与父容器ScrollView冲突的问题,无法完美解决.有卡顿
 * 此自定义组件和下拉刷新scrollview配合暂时小完美，有待改善
 * @author bavariama
 *
 */

public class InsideViewPager extends ViewPager {
    float curX = 0f;
    float downX = 0f;
    float curY = 0f;
    float downY = 0f;
    boolean touch = false;
    OnSingleTouchListener onSingleTouchListener;

    public InsideViewPager(Context context) {
        // TODO Auto-generated constructor stub
        super(context);
    }

    public InsideViewPager(Context context, AttributeSet attrs) {
        // TODO Auto-generated constructor stub
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            getParent().requestDisallowInterceptTouchEvent(true);
            if (touch){
                onSingleTouch(false);
            }

        }else {
            getParent().requestDisallowInterceptTouchEvent(false);
            if (!touch){
                onSingleTouch(true);
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_UP) {
            getParent().requestDisallowInterceptTouchEvent(true);
            if (touch){
                onSingleTouch(false);
            }
        }
        return super.onTouchEvent(ev);
    }

    public void onSingleTouch(boolean flag) {
        touch = flag;
        if (onSingleTouchListener != null) {
            onSingleTouchListener.onSingleTouch(flag);
        }
    }

    public interface OnSingleTouchListener {
        public void onSingleTouch(boolean flag);
    }

    public void setOnSingleTouchListner(
            OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }

}

package com.zeyuan.kyq.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import com.andview.refreshview.callback.HomePageCallBack;

/**
 * Created by Administrator on 2017/4/10.
 *
 * @author wwei
 */
public class HomeLinearLayout extends LinearLayout{

    //自定义事件回调
    private HomePageCallBack callback;

    public void setCallback(HomePageCallBack callback) {
        this.callback = callback;
    }

    public HomeLinearLayout(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public HomeLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public HomeLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }
    private int mTouchSlop;
    private int mLastY = -1; // save event y
    private int mLastX = -1; // save event x
    private boolean mMoveForHorizontal = false;
    private int mInitialMotionY;

    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        final int action = ev.getAction();
        int deltaY = 0;
        int deltaX = 0;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = (int) ev.getRawY();
                mLastX = (int) ev.getRawX();
                mInitialMotionY = mLastY;
                break;
            case MotionEvent.ACTION_MOVE:
                int currentY = (int) ev.getRawY();
                int currentX = (int) ev.getRawX();
                deltaY = currentY - mLastY;
                deltaX = currentX - mLastX;
                mLastY = currentY;
                mLastX = currentX;
                if (!mMoveForHorizontal && Math.abs(deltaX) > mTouchSlop
                        && Math.abs(deltaX) > Math.abs(deltaY)) {
                    mMoveForHorizontal = true;
                }
                if (mMoveForHorizontal) {
                    return super.dispatchTouchEvent(ev);
                }
                if (((deltaY>0&&callback.isChangeAble(false))||(deltaY<0&&callback.isChangeAble(true)))){
                    callback.setViewChange(deltaY*9/5);
                    *//*if (deltaY<0){
                        callback.setViewChange(deltaY*8/5);
                    }else {
                        callback.setViewChange(deltaY*8/5);
                    }*//*
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mLastY = -1; // reset
                mLastX = -1;
                mInitialMotionY = 0;
                mMoveForHorizontal = false;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }*/
}

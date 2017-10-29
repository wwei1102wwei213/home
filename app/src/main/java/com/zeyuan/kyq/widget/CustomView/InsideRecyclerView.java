package com.zeyuan.kyq.widget.CustomView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/11/9.
 *
 *
 * @author wwei
 */
public class InsideRecyclerView extends RecyclerView {

    private boolean touch = false;

    private OnSingleTouchListener onSingleTouchListener;

    public InsideRecyclerView(Context context) {
        super(context);
    }

    public InsideRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InsideRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
            onSingleTouchListener.onRySingleTouch(flag);
        }
    }

    public interface OnSingleTouchListener {
        public void onRySingleTouch(boolean flag);
    }

    public void setOnSingleTouchListener(
            OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }

}

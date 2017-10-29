package com.zeyuan.kyq.widget.PullToRefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2016/10/14.
 *
 * 为实现菜单悬浮，以及添加上拉加载监听回调
 *
 * @author wwei
 */
public class PullableCallUpScrollView extends ScrollView implements Pullable{

    private boolean pullDownFlag = true;

    private OnScrollCustomListener onScrollCustomListener;

    public void setOnScrollCustomListener(OnScrollCustomListener onScrollCustomListener) {
        this.onScrollCustomListener = onScrollCustomListener;
    }

    public void setPullDownFlag(boolean pullDownFlag) {
        this.pullDownFlag = pullDownFlag;
    }

    public PullableCallUpScrollView(Context context) {
        super(context);
    }

    public PullableCallUpScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableCallUpScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean canPullDown() {
        if (pullDownFlag&&getScrollY() == 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean canPullUp() {
        if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
        {
            if (onScrollCustomListener!=null){
                onScrollCustomListener.onScrollBottom(true);
            }
            return false;
        }

        else
        return false;

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(onScrollCustomListener != null){
            onScrollCustomListener.onScrollCustom(t);
        }
    }

    public interface OnScrollCustomListener{
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         * @param scrollY
         *
         */
        void onScrollCustom(int scrollY);
        void onScrollBottom(boolean y);
    }
}

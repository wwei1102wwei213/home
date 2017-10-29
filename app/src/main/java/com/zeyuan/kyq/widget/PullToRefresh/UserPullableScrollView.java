package com.zeyuan.kyq.widget.PullToRefresh;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;


/**
 * Created by Administrator on 2016/10/31.
 *
 *
 *
 * @author wwei
 */
public class UserPullableScrollView extends ScrollView implements Pullable
{

    private OnScrollListener onScrollListener;
    private boolean downLoading = false;

    public void setDownLoading(boolean downLoading){
        this.downLoading = downLoading;
    }

    public UserPullableScrollView(Context context)
    {
        super(context);
    }

    public UserPullableScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public UserPullableScrollView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    private boolean pullDownFlag = true;

    public void setPullDownFlag(boolean pullDownFlag) {
        this.pullDownFlag = pullDownFlag;
    }

    public boolean isPullDownFlag() {
        return pullDownFlag;
    }

    @Override
    public boolean canPullDown()
    {
        /*if (pullDownFlag&&getScrollY() == 0)
            return true;
        else*/
            return false;
    }

    @Override
    public boolean canPullUp()
    {
        if (getScrollY() == (getChildAt(0).getHeight() - getMeasuredHeight()))
            return true;
        else
            return false;
    }

    /**
     * 设置滚动接口
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(onScrollListener != null){
            onScrollListener.onScroll(t);
            onScrollListener.onScrollChange(l, t, oldl, oldt);
        }
    }

   /* @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);

        if (null != onScrollListener&&clampedY&&(getScrollY() ==
                (getChildAt(0).getHeight() - getMeasuredHeight()))&&scrollY > 0){
            if (!downLoading){
                downLoading = true;
                this.onScrollListener.onScrollBottom(true);
            }

        }
    }*/

    /**
     *
     * 滚动的回调接口
     *
     * @author xiaanming
     *
     */
    public interface OnScrollListener{
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         * @param scrollY
         *
         */
        void onScroll(int scrollY);
        void onScrollChange(int x, int y, int oldx, int oldy);
        void onScrollBottom(boolean y);
    }



}


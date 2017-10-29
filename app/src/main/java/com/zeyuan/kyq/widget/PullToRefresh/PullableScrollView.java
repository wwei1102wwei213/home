package com.zeyuan.kyq.widget.PullToRefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class PullableScrollView extends ScrollView implements Pullable
{

	private OnScrollListener onScrollListener;

	public PullableScrollView(Context context)
	{
		super(context);
	}

	public PullableScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullableScrollView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	private boolean pullDownFlag = true;

	private boolean pullUpFlag = false;

	public void setPullUpFlag(boolean pullUpFlag) {
		this.pullUpFlag = pullUpFlag;
	}



	public void setPullDownFlag(boolean pullDownFlag) {
		this.pullDownFlag = pullDownFlag;
	}

	public boolean isPullDownFlag() {
		return pullDownFlag;
	}

	@Override
	public boolean canPullDown()
	{
		if (pullDownFlag&&getScrollY() == 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean canPullUp()
	{
		if (pullUpFlag&&getScrollY() == (getChildAt(0).getHeight() - getMeasuredHeight()))
			return true;
		return false;
	}

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
		}
	}
}

package com.zeyuan.kyq.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.andview.refreshview.XScrollView;


/**
 * @blog
 * 
 * @author
 *
 */
public class MyScrollView extends XScrollView {
	private OnScrollListener onScrollListener;

	public MyScrollView(Context context) {
		super(context);
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

//	public MyScrollView(Context context) {
//		this(context, null);
//	}
	
//	public MyScrollView(Context context, AttributeSet attrs) {
//		this(context, attrs, 0);
//	}

//	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//	}

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

	public interface OnScrollListener{
		public void onScroll(int scrollY);
	}
	
	

}

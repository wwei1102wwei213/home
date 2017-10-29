package com.zeyuan.kyq.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.zeyuan.kyq.R;

/***
 *  自定义控件
 *  用于登录页面图片轮播的标示，与ViewPager实现联动效果
 *  @user wwei
 */
public class DrawCircleView extends View{
	
	private int r;
	private int hx,sx;
	private int count ;
	private int hollow;
	private int solid;
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public DrawCircleView(Context context) {
		super(context);
	}
	
	public DrawCircleView(Context context, AttributeSet attr) {
		super(context, attr);
	}
	
	public void setDrawCricle(int count,int r,int hollow,int solid) {
		this.r = r;
		this.hollow = hollow;
		this.solid = solid;
		this.count = count;
		if(this.r == 0) this.r = 10;
		if(this.hollow == 0) this.hollow = R.color.gray_green_selector;
		if(this.solid == 0) this.solid = R.color.light_green;
		if(this.count== 0) this.count = 3;
	}	
	@Override
	protected void onDraw(Canvas canvas) {
		paint.setColor(this.hollow);
		for (int i = 0; i < count; i++) {
			canvas.drawCircle(hx+r*i*4, getHeight()/2, r, paint);
		}
		paint.setColor(this.solid);
		canvas.drawCircle(sx, getHeight()/2, r, paint);
	}
	
	public void redraw(int position){
		sx = hx+position*4*r;
		invalidate();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		hx = w/2-r*4*((count-1)/2);
		sx = hx;
	}


}

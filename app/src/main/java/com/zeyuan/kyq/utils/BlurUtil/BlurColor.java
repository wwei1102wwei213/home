package com.zeyuan.kyq.utils.BlurUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.utils.ExceptionUtils;

public class BlurColor {

	public static final int BLUR_CUSTOM = 99;
	private Context context;
	private ImageView image;
	private TextView text;
	private View v1;
	private View v2;
	private int tag = 0;
	private Bitmap temp;
	private float f1;
	private float f2;
	
	public BlurColor(Context context,ImageView image,TextView text){
		this.context = context;
		this.image = image;
		this.text = text;		
	}

	public BlurColor(Context context,ImageView image,TextView text,int tag){
		this.context = context;
		this.image = image;
		this.text = text;
		this.tag = tag;
	}

	public BlurColor(Context context,View v1,View v2,int flag){
		this.context = context;
		this.v1 = v1;
		this.v2 = v2;
	}

	public BlurColor(Context context,View v1,View v2,float f1,float f2,int flag){
		this.context = context;
		this.v1 = v1;
		this.v2 = v2;
		this.f1 = f1;
		this.f2 = f2;
		this.tag = flag;
	}

	public void applyBlur() {
		image.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				image.getViewTreeObserver().removeOnPreDrawListener(this);
				image.buildDrawingCache();
				Bitmap bmp = image.getDrawingCache();
//				temp = bmp;
				if (text.getMeasuredHeight()>0)
				blur2(bmp, text);
				return true;
			}
		});
	}



	public void applyBlurView() {
		v1.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				v1.getViewTreeObserver().removeOnPreDrawListener(this);
				v1.buildDrawingCache();
				Bitmap bmp = v1.getDrawingCache();
				if (v2.getMeasuredHeight()>0)
				blur2(bmp, v2);
				return true;
			}
		});
	}

	public void setTopBar() {

	}

	private void blur2(Bitmap bkg, View view) {
//		long startMs = System.currentTimeMillis();
		try {
			float radius = 4;
			float scaleFactor = 12;
			if(tag!=0){
				if(tag == BLUR_CUSTOM){
					radius = f1;
					scaleFactor =f2;
				}else if (tag == 1){
					radius = 6;
					scaleFactor = 15;
				}else {
					radius = 13;
					scaleFactor = 50;
				}
			}
			Bitmap overlay = Bitmap.createBitmap((int)(view.getMeasuredWidth()/scaleFactor),
					(int)(view.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(overlay);
			canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
			canvas.scale(1 / scaleFactor, 1 / scaleFactor);
			Paint paint = new Paint();
			paint.setFlags(Paint.FILTER_BITMAP_FLAG);
			canvas.drawBitmap(bkg, 0, 0, paint);
			overlay = ImageUtil.fastblur(context, overlay, (int) radius);
			if (Build.VERSION.SDK_INT>=16&&context!=null){
				view.setBackground(new BitmapDrawable(context.getResources(), overlay));
			}
		}catch (Exception e){
			ExceptionUtils.ExceptionToUM(e,context,"blur2");
		}

	}

	public void initBlur() {
		image.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				image.getViewTreeObserver().removeOnPreDrawListener(this);
				image.buildDrawingCache();
				temp = image.getDrawingCache();
				return true;
			}
		});
	}

	public Bitmap getBitmap() {
//		long startMs = System.currentTimeMillis();
		float radius = 4;
		float scaleFactor = 12;
		if(tag!=0){
			if(tag == BLUR_CUSTOM){
				radius = f1;
				scaleFactor =f2;
			}else {
				radius = 13;
				scaleFactor = 50;
			}
		}
		Bitmap overlay = Bitmap.createBitmap((int)(text.getMeasuredWidth()/scaleFactor),
				(int)(text.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(overlay);
		canvas.translate(-text.getLeft()/scaleFactor, -text.getTop()/scaleFactor);
		canvas.scale(1 / scaleFactor, 1 / scaleFactor);
		Paint paint = new Paint();
		paint.setFlags(Paint.FILTER_BITMAP_FLAG);
		canvas.drawBitmap(temp, 0, 0, paint);
		overlay = ImageUtil.fastblur(context, overlay, (int) radius);
		if(temp!=null){
			temp.recycle();
		}
//		view.setBackground(new BitmapDrawable(context.getResources(), overlay));
		return overlay;
	}

}

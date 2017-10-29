package com.zeyuan.kyq.utils.BlurUtil;

/**
 * Created by Administrator on 2016/7/25.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

public class BlurColorView {
    private Context context;
    private View image;
    private View text;

    public BlurColorView(Context context,View image,View text){
        this.context = context;
        this.image = image;
        this.text = text;
    }


    public void applyBlur() {
        try {
            image.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    try {
                        image.getViewTreeObserver().removeOnPreDrawListener(this);
                        image.buildDrawingCache();
                        Bitmap bmp = image.getDrawingCache();
                        blur2(bmp, text);
                    }catch (Exception e){

                    }
                    return true;
                }
            });
        }catch (Exception e){

        }

    }

    public void setTopBar() {

    }

    private void blur2(Bitmap bkg, View view) {
//		long startMs = System.currentTimeMillis();
        float radius = 5;
        float scaleFactor = 20;
        Bitmap overlay = Bitmap.createBitmap((int)(view.getMeasuredWidth()/scaleFactor), (int)(view.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);
        overlay = ImageUtil.fastblur(context, overlay, (int) radius);
        if (Build.VERSION.SDK_INT>=16)
        view.setBackground(new BitmapDrawable(context.getResources(), overlay));
//		PToast.show("cost " + (System.currentTimeMillis() - startMs) + "ms");
    }

}


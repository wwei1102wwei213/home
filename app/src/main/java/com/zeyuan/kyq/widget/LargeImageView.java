package com.zeyuan.kyq.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.zeyuan.kyq.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/5/17.
 */
public class LargeImageView extends ImageView{

    public static final String TAG = LargeImageView.class.getName();

    private BitmapRegionDecoder mDecoder;
    private final Rect mRect = new Rect();
    Bitmap[] bs ;
    int sw, sh;
    private Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);

    private float minScale; //默认是设备宽度/460

    Context cxt;
    int screenW;
    public LargeImageView(Context context) {
        this(context, null);
    }

    public LargeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LargeImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        cxt = context;
        setImageResource(R.color.white);
        if(context instanceof Activity){
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            screenW = wm.getDefaultDisplay().getWidth();
            minScale = screenW/(float)460;
            sw = screenW;
            sh = 1000;
        }
    }

    public void setMinScale1(float s){
        this.minScale = s;
    }

    /**
     * 每次绘制图片的长度高度
     * */
    public void setWH(int reqW,int reqH){
        this.sw = reqW;
        this.sh = reqH;
    }

    public void setImageBitmap1(final Bitmap bm) {
        //Toast.makeText(this.getContext(), "large bitmap"+bm.getHeight(), 0).show();
        startAnimation(AnimationUtils.loadAnimation(cxt, R.anim.headline_alpha_in));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int w = bm.getWidth();
                    int h = bm.getHeight();
                    int c = h/sh;
                    int count = h%sh==0?c:c+1;
                    Log.d(TAG, "sh=" + sh + ",h=" + h + ",count=" + count + ",Build.VERSION.SDK_INT=" + Build.VERSION.SDK_INT);
                    if(Build.VERSION.SDK_INT>1000){//因兼容低版本问题，暂不使用此切图方式
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);//arg1为传进来的原始bitmap
                        baos.toByteArray();
                        InputStream is = new ByteArrayInputStream(baos.toByteArray());
                        mDecoder = BitmapRegionDecoder.newInstance(is, true);
                        bs = new Bitmap[count];
                        for(int i=0;i<count;i++){
                            mRect.set(0, sh*i, w, (i+1)*sh);
                            bs[i] = mDecoder.decodeRegion(mRect, null);
                        }
                        baos.close();
                        is.close();
                    }else{
                        bs = new Bitmap[count];
                        for(int i=0;i<count;i++){
                            int height = i==(count-1)? h-i*sh:sh;
                            bs[i] =Bitmap.createBitmap(bm, 0, sh*i, w, height);
                            if(bs[i] == null){
                                throw new IllegalArgumentException("bitmap is null,pos at " + i);
                            }
                        }
                    }
                    if(bm!=null) bm.recycle();
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
            int top = 0;
            for(int i=0;bs!=null && i<bs.length;i++){
                //DeLog.d(TAG,"i =" +i +",bs.length="+bs.length+",top="+top+",scale="+minScale);
                canvas.save();
                if(minScale != 0){
                    canvas.scale(minScale, minScale);
                }
                if(bs[i] == null){
                    return;
                }
                canvas.drawBitmap(bs[i], 0,top, mPaint);
                canvas.restore();
                top+=bs[i].getHeight();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    Handler handler = new Handler(){

        @Override
        public void handleMessage(android.os.Message msg) {
            Log.d(TAG,"get notify...");
            postInvalidate();
        }
    };
}

package com.zeyuan.kyq.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.zeyuan.kyq.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhouyou on 2016/9/27.
 * Class desc:
 * <p>
 * This is a circle on the proliferation of Android custom view.
 * GitHub：https://github.com/Airsaid/DiffuseView
 */
public class DiffuseView extends View {

    /**
     * 扩散圆圈颜色
     */
    private int mColor = getResources().getColor(R.color.card_background);
    /**
     * 圆圈中心颜色
     */
    private int mCoreColor = getResources().getColor(R.color.cancer_other_sold);
    /**
     * 圆圈中心图片
     */
    private Bitmap mBitmap;
    /**
     * 中心圆半径
     */
    private float mCoreRadius = 150;
    /**
     * 扩散圆宽度
     */
    private int mDiffuseWidth = 3;
    /**
     * 最大宽度
     */
    private Integer mMaxWidth = 255;
    /**
     * 是否正在扩散中
     */
    private boolean mIsDiffuse = false;
    // 透明度集合
    private List<Integer> mAlphas = new ArrayList<>();
    // 扩散圆半径集合
    private List<Integer> mWidths = new ArrayList<>();
    private Paint mPaint;
    Handler handler = new Handler();

    public DiffuseView(Context context) {
        this(context, null);
    }

    public DiffuseView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DiffuseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DiffuseView, defStyleAttr, 0);
        mColor = a.getColor(R.styleable.DiffuseView_diffuse_color, mColor);
        mCoreColor = a.getColor(R.styleable.DiffuseView_diffuse_coreColor, mCoreColor);
        mCoreRadius = a.getFloat(R.styleable.DiffuseView_diffuse_coreRadius, mCoreRadius);
        mDiffuseWidth = a.getInt(R.styleable.DiffuseView_diffuse_width, mDiffuseWidth);
        mMaxWidth = a.getInt(R.styleable.DiffuseView_diffuse_maxWidth, mMaxWidth);
        int imageId = a.getResourceId(R.styleable.DiffuseView_diffuse_coreImage, -1);
        if (imageId != -1) mBitmap = BitmapFactory.decodeResource(getResources(), imageId);
        a.recycle();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mAlphas.add(255);
        mWidths.add(0);
    }

    @Override
    public void invalidate() {
        if (hasWindowFocus()) {
            super.invalidate();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    int cSize = 1;//圆数量

    @Override
    public void onDraw(Canvas canvas) {
        // 绘制扩散圆
        mPaint.setColor(mColor);
        for (int i = cSize; i > 0; i--) {
            // 设置透明度
            Integer alpha = 255 - i * 40;
            mPaint.setAlpha(alpha);

            // 绘制扩散圆
            Integer width = i * 40;
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mCoreRadius + width, mPaint);
        }
        if (cSize <= 5) {
            cSize++;
        } else {
            cSize = 1;
        }

        // 绘制中心圆及图片
        mPaint.setAlpha(255);
        mPaint.setColor(mCoreColor);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mCoreRadius, mPaint);


        if (mIsDiffuse) {//如果没有触发停止，重绘界面
            handler.postDelayed(dRun, 200);
        }
    }

    Runnable dRun = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    /**
     * 开始扩散
     */
    public void start() {
        mIsDiffuse = true;
        invalidate();
    }

    /**
     * 停止扩散
     */
    public void stop() {
        mIsDiffuse = false;
    }

    /**
     * 是否扩散中
     */
    public boolean isDiffuse() {
        return mIsDiffuse;
    }

    /**
     * 设置扩散圆颜色
     */
    public void setColor(int colorId) {
        mColor = colorId;
    }

    /**
     * 设置中心圆颜色
     */
    public void setCoreColor(int colorId) {
        mCoreColor = colorId;
    }

    /**
     * 设置中心圆图片
     */
    public void setCoreImage(int imageId) {
        mBitmap = BitmapFactory.decodeResource(getResources(), imageId);
    }

    /**
     * 设置中心圆半径
     */
    public void setCoreRadius(int radius) {
        mCoreRadius = radius;
    }

    /**
     * 设置扩散圆宽度(值越小宽度越大)
     */
    public void setDiffuseWidth(int width) {
        mDiffuseWidth = width;
    }

    /**
     * 设置最大宽度
     */
    public void setMaxWidth(int maxWidth) {
        mMaxWidth = maxWidth;
    }
}

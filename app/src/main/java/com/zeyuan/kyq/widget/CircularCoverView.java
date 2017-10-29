package com.zeyuan.kyq.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.DensityUtil;

/**
 * 遮蔽罩
 */
public class CircularCoverView extends View {

    private int radians = 30;

    private int coverColor = 0xffeaeaea;    //color of cover.
    private Paint mPaint;

    public CircularCoverView(Context context) {
        this(context, null, 0);
    }

    public CircularCoverView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularCoverView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircularCoverView);
        radians = typedArray.getDimensionPixelSize(R.styleable.CircularCoverView_radius, radians);
        coverColor = typedArray.getColor(R.styleable.CircularCoverView_cover_color, coverColor);
        typedArray.recycle();
        initPaint();
    }

    // 2.初始化画笔
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.transparent));//设置画笔颜色(全透明)
        mPaint.setStyle(Paint.Style.FILL);  //设置画笔模式为填充
        mPaint.setStrokeWidth(10f);         //设置画笔宽度为10px
        mPaint.setAntiAlias(true);          //开启抗锯齿
        mPaint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));//清除绘制区域的底色
    }

    /**
     * 设置圆角半径
     */
    public void setDPRadians(int radians) {
        this.radians = DensityUtil.dip2px(getContext().getApplicationContext(), radians);
    }


    /**
     * set color of cover.
     *
     * @param coverColor cover's color
     */
    public void setCoverColor(@ColorInt int coverColor) {
        this.coverColor = coverColor;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    int x1, y1, x2, y2;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制底色
        //canvas.drawARGB(80, 0, 0, 0);
        canvas.drawColor(coverColor);
        RectF rectF = new RectF(x1, y1, x2, y2);
        canvas.drawRoundRect(rectF, radians, radians, mPaint);
    }

    /**
     * 绘制透明矩形
     *
     * @param xy     矩形的起始xy轴坐标 px值
     * @param width  矩形宽度 px值
     * @param height 矩形高度 px值
     */
    public void drawTransparentRect(int[] xy, int width, int height) {
        x1 = xy[0];
        y1 = xy[1];
        x2 = x1 + width;
        y2 = y1 + height;
        invalidate();
    }

}
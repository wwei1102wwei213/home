package com.zeyuan.kyq.widget.ForAnimUI;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.View;

import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.UiUtils;


public class DynamicWave extends View {

    private int WAVE_PAINT_COLOR = 0x35ffffff;

    private static final float STRETCH_FACTOR_A = 20;
    private static final int OFFSET_Y = 0;

    private static final int TRANSLATE_X_SPEED_ONE = 7;

    private static final int TRANSLATE_X_SPEED_TWO = 5;
    private float mCycleFactorW;

    private int mTotalWidth, mTotalHeight;
    private float[] mYPositions;
    private float[] mResetOneYPositions;
    private float[] mResetTwoYPositions;
    private int mXOffsetSpeedOne;
    private int mXOffsetSpeedTwo;
    private int mXOneOffset;
    private int mXTwoOffset;

    private boolean drawAble = true;

    private Paint mWavePaint;
    private DrawFilter mDrawFilter;
    private int restartHeight = 45;

    public void setRestartHeight(int restartHeight) {
        this.restartHeight = restartHeight;
    }

    public void setWAVE_PAINT_COLOR(int WAVE_PAINT_COLOR) {
        this.WAVE_PAINT_COLOR = WAVE_PAINT_COLOR;
    }

    public void setDrawAble(boolean drawAble) {
        this.drawAble = drawAble;
    }

    public void startWave(){
        try {
            drawAble = true;
            postInvalidateDelayed(100);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"startWave");
        }
    }

    public void cancelWave(){
        drawAble = false;
    }

    public DynamicWave(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {

            mXOffsetSpeedOne = UiUtils.dipToPx(context, TRANSLATE_X_SPEED_ONE);
            mXOffsetSpeedTwo = UiUtils.dipToPx(context, TRANSLATE_X_SPEED_TWO);


            mWavePaint = new Paint();

            mWavePaint.setAntiAlias(true);

            mWavePaint.setStyle(Paint.Style.FILL);

            mWavePaint.setColor(WAVE_PAINT_COLOR);
            mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"DynamicWave");
        }
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (drawAble){


        try {
            canvas.setDrawFilter(mDrawFilter);
            resetPositonY();

            for (int i = 0; i < mTotalWidth; i++) {
                canvas.drawLine(i, mTotalHeight - mResetOneYPositions[i] - restartHeight, i,
                        mTotalHeight,
                        mWavePaint);
                canvas.drawLine(i, mTotalHeight - mResetTwoYPositions[i] - restartHeight, i,
                        mTotalHeight,
                        mWavePaint);
            }
            mXOneOffset += mXOffsetSpeedOne;
            mXTwoOffset += mXOffsetSpeedTwo;
            if (mXOneOffset >= mTotalWidth) {
                mXOneOffset = 0;
            }
            if (mXTwoOffset > mTotalWidth) {
                mXTwoOffset = 0;
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"onDraw");
        }
        try {
            if(drawAble){
                postInvalidateDelayed(100);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"postInvalidate");
        }
        }
    }

    private void resetPositonY() {
        try {
            int yOneInterval = mYPositions.length - mXOneOffset;

            System.arraycopy(mYPositions, mXOneOffset, mResetOneYPositions, 0, yOneInterval);
            System.arraycopy(mYPositions, 0, mResetOneYPositions, yOneInterval, mXOneOffset);

            int yTwoInterval = mYPositions.length - mXTwoOffset;
            System.arraycopy(mYPositions, mXTwoOffset, mResetTwoYPositions, 0,
                    yTwoInterval);
            System.arraycopy(mYPositions, 0, mResetTwoYPositions, yTwoInterval, mXTwoOffset);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"resetPositonY");
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        try {
            mTotalWidth = w;
            mTotalHeight = h;

            mYPositions = new float[mTotalWidth];

            mResetOneYPositions = new float[mTotalWidth];

            mResetTwoYPositions = new float[mTotalWidth];


            mCycleFactorW = (float) (2 * Math.PI / mTotalWidth);


            for (int i = 0; i < mTotalWidth; i++) {
                mYPositions[i] = (float) (STRETCH_FACTOR_A * Math.sin(mCycleFactorW * i) + OFFSET_Y);
            }
        }catch (Exception e){

        }
    }

}
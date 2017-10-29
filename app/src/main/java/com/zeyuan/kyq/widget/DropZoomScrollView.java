package com.zeyuan.kyq.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ScrollView;

import com.zeyuan.kyq.utils.BlurUtil.BlurColor;

/***
 *
 *
 *
 */
public class DropZoomScrollView extends ScrollView implements View.OnTouchListener {

    // 记录首次按下位置
    private float mFirstPosition = 0;
    // 是否正在放大
    private Boolean mScaling = false;

    private View dropZoomView;
    private View tvBlurView;
    private BlurColor bcv;
    private int dropZoomViewWidth;
    private int dropZoomViewHeight;

    private long mSpaceTime = 0;

    public DropZoomScrollView(Context context) {
        super(context);
    }

    public DropZoomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DropZoomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private AlphaAnimation mAlphaOut;
    private AlphaAnimation mAlphaIn;
    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);
        if (getChildAt(0) != null) {
            ViewGroup vg = (ViewGroup) getChildAt(0);
            if (vg.getChildAt(0) != null) {
                dropZoomView = vg.getChildAt(0);
                setOnTouchListener(this);
            }
            if (vg.getChildAt(1) != null) {
                tvBlurView = vg.getChildAt(1);
            }
//            bcv = new BlurColor(getContext(),dropZoomView,tvBlurView,0);
            /*mAlphaOut = new AlphaAnimation(1.0f,0.1f);
            mAlphaOut.setDuration(200);
            mAlphaOut.setFillAfter(true);
            mAlphaIn = new AlphaAnimation(0.1f,1.0f);
            mAlphaIn.setDuration(200);
            mAlphaIn.setFillAfter(true);*/
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (dropZoomViewWidth <= 0 || dropZoomViewHeight <= 0) {
            dropZoomViewWidth = dropZoomView.getMeasuredWidth();
            dropZoomViewHeight = dropZoomView.getMeasuredHeight();
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                //手指离开后恢复图片
                try {
                    mScaling = false;
                    tvBlurView.setVisibility(VISIBLE);
                    replyImage();
                }catch (Exception e){

                }

                break;
            case MotionEvent.ACTION_MOVE:
                try {
                    if(tvBlurView.getVisibility()!=INVISIBLE) tvBlurView.setVisibility(INVISIBLE);
                }catch (Exception e){

                }
                if (!mScaling) {
                    if (getScrollY() == 0) {
                        mFirstPosition = event.getY();// 滚动到顶部时记录位置，否则正常返回
                    } else {
                        break;
                    }
                }
                int distance = (int) ((event.getY() - mFirstPosition) * 0.6); // 滚动距离乘以一个系数
                if (distance < 0) { // 当前位置比记录位置要小，正常返回
                    break;
                }

                // 处理放大
                mScaling = true;
                setZoom(1 + distance);

                return true; // 返回true表示已经完成触摸事件，不再处理
        }
        return false;
    }

    // 回弹动画 (使用了属性动画)
    public void replyImage() {
        final float distance = dropZoomView.getMeasuredWidth() - dropZoomViewWidth;

        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration((long) (distance * 0.7));

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                setZoom(distance - ((distance) * cVal));
            }
        });
        anim.start();

    }

    //缩放
    public void setZoom(float s) {
        if (dropZoomViewHeight <= 0 || dropZoomViewWidth <= 0) {
            return;
        }
        ViewGroup.LayoutParams lp = dropZoomView.getLayoutParams();
        lp.width = (int) (dropZoomViewWidth + s);
        lp.height = (int) (dropZoomViewHeight * ((dropZoomViewWidth + s) / dropZoomViewWidth));
        try {
            dropZoomView.setLayoutParams(lp);
            tvBlurView.setLayoutParams(lp);
        }catch (Exception e){

        }
    }

    private OnScrollListener onScrollListener;

    public interface OnScrollListener{
        public void onScroll(int x, int y, int oldx, int oldy);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(onScrollListener!=null){
            onScrollListener.onScroll(l,t,oldl,oldt);
        }
    }

    public void setOnScrollListener(OnScrollListener onScrollListener){
        this.onScrollListener=onScrollListener;
    }
}

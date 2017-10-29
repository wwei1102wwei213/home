package com.zeyuan.kyq.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2017/8/24.
 * 子控件可滑动的滑动控件
 */

public class ZyScrollView extends ScrollView {
    public ZyScrollView(Context context) {
        super(context);
    }

    public ZyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //完全不阻断滑动事件往下传递
        return false;
    }
}

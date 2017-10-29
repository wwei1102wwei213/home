package com.zeyuan.kyq.widget.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * Created by Administrator on 2017/1/10.
 *
 * @author wwei
 */
public class CustomTabLayout extends HorizontalScrollView{

    private static final int ANIMATION_DURATION = 300;

    public CustomTabLayout(Context context) {
        super(context);
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface OnTabSelectedListener {

        public void onTabSelected(View tab);

        public void onTabUnselected(View tab);

        public void onTabReselected(View tab);
    }
}

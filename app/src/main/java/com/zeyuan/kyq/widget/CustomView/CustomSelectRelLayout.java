package com.zeyuan.kyq.widget.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2016/9/2.
 */
public class CustomSelectRelLayout extends RelativeLayout{


    public CustomSelectRelLayout(Context context) {
        super(context);
    }

    public CustomSelectRelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSelectRelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(isSelected()){
            getChildAt(0).setSelected(true);
        }else {
            getChildAt(0).setSelected(false);
        }
    }

}

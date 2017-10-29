package com.zeyuan.kyq.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.zeyuan.kyq.R;

/**
 * Created by Administrator on 2016/8/5.
 *
 *
 * @author wwei
 */
public class CircleFollowCheckBox extends CheckBox{

    public CircleFollowCheckBox(Context context) {
        super(context);
    }

    public CircleFollowCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CircleFollowCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if(checked){
            setText(R.string.no_follow);
        }else {
            setText(R.string.is_follow);
        }
    }
}

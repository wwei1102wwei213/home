package com.zeyuan.kyq.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.LogUtil;

/**
 * Created by Administrator on 2016/1/10.
 */
public class DoubleTvLayout extends LinearLayout {
    private static final String TAG = "DoubleTvLayout";
    private View mLayout;

    public DoubleTvLayout(Context context) {
        super(context);
        initView(context,null);
    }

    public DoubleTvLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);

    }


    private String left_txt;
    private String right_txt;

    public DoubleTvLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);

    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.double_tv_layout);
        try {
            left_txt = a.getString(R.styleable.double_tv_layout_left_txt);
            right_txt = a.getString(R.styleable.double_tv_layout_right_txt);
            LogUtil.i(TAG, "left txt is :" + left_txt);
            LogUtil.i(TAG, "right txt is :" + right_txt);

        } finally {
            a.recycle();
        }
    }


    private TextView leftTv;
    private TextView rightTv;

    private void initView(Context context, AttributeSet attrs) {
        init(context, attrs);
        mLayout = LayoutInflater.from(context).inflate(R.layout.double_tv_layout, null);
        mLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        addView(mLayout);
        leftTv = (TextView) mLayout.findViewById(R.id.tv_left);
        rightTv = (TextView) mLayout.findViewById(R.id.tv_right);
        if (!TextUtils.isEmpty(left_txt)) {
            leftTv.setText(left_txt);
        }
        if (!TextUtils.isEmpty(right_txt)) {
            rightTv.setText(right_txt);
        }
    }
    public void setLeftTxt(String left_txt) {
        if (!TextUtils.isEmpty(left_txt)) {
            leftTv.setText(left_txt);
        }
    }

    public void setRightTxt(String left_txt) {
        if (!TextUtils.isEmpty(left_txt)) {
            rightTv.setText(left_txt);
        }
    }
}

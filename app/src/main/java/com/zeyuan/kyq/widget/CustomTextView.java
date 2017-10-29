package com.zeyuan.kyq.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/5/25.
 *
 * 自定义控件：可以展开和收缩的TextView
 *
 * @author wwei
 */
public class CustomTextView extends TextView implements View.OnClickListener{

    private static boolean open = false;
    private final static int collapsedLineCount = 3;

    public CustomTextView(Context context) {
        super(context);
        setMinLines(collapsedLineCount);
        setOnClickListener(this);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMinLines(collapsedLineCount);
        setOnClickListener(this);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setMinLines(collapsedLineCount);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(open){
            setMinLines(collapsedLineCount);
            open = false;
        }else {
            setMinLines(Integer.MAX_VALUE);
            open = false;
        }
    }
}

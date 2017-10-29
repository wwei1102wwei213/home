package com.zeyuan.kyq.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zeyuan.kyq.R;

/**
 * Created by Administrator on 2016/3/11.
 */
public class ScrollViewHeader extends RelativeLayout {

    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;
    private final int ROTATE_ANIM_DURATION = 180;
    private int topMargin = 0;
    private int state = STATE_NORMAL;


    public ScrollViewHeader(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        if(!isInEditMode())
            initView(context);
    }

    public ScrollViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        if(!isInEditMode())
            initView(context);
    }

    public ScrollViewHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        if(!isInEditMode())
            initView(context);
    }

    /**
     * 初始化相关的view
     */
    public void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_scrollview_header, this, true);
    }

    /**
     * 设置scrollviewHeader的状态
     * @param state
     */
    public void setState(int state) {
        if(this.state == state) {
            return ;
        }
        switch (state) {
            case STATE_NORMAL://下拉刷新

                break;
            case STATE_READY://松开刷新

                break;
            case STATE_REFRESHING://正在加载

                break;
            default:
                break;
        }
        this.state = state;
    }

    /**
     * 更新header的margin
     * @param margin
     */
    public void updateMargin(int margin) {
        //这里用Linearlayout的原因是Headerview的父控件是scrollcontainer是一个linearlayout
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.getLayoutParams();
        params.topMargin = margin;
        topMargin = margin;
        setLayoutParams(params);
    }

    /**
     * 获取header的margin
     * @return
     */
    public int getTopMargin() {
        return topMargin;
    }
}

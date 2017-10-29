package com.zeyuan.kyq.widget.CustomView;


import android.content.Context;
import android.util.AttributeSet;
import android.view.*;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/7/19.
 *
 *
 *
 * @author wwei
 */
public class MoveImageView extends ImageView {
    public MoveImageView(Context context) {
        super(context);

    }
    public MoveImageView(Context context,AttributeSet attrs) {
        super(context,attrs);

    }
    public MoveImageView(Context context,AttributeSet attrs,int defStyle) {
        super(context,attrs,defStyle);

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //return super.onTouchEvent(event);
        int eventaction = event.getAction();
        switch(eventaction){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            {
                int x = (int)event.getX();
                int y = (int)event.getY();
                if(x>0&&y>0){
                    this.layout(x,y,x+this.getWidth(),y+this.getHeight());
                }
                break;
            }
        }
        this.invalidate();
        return true;
    }
}

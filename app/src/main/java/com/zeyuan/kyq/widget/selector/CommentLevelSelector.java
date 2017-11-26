package com.zeyuan.kyq.widget.selector;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zeyuan.kyq.R;

/**
 * Created by Administrator on 2017-11-26.
 */

public class CommentLevelSelector extends LinearLayout{

    private int type;//1 big 2 small
    private int back;//回调标识
    private int level;//已选等级
    private LevelSelectorListener callback;

    public CommentLevelSelector(Context context) {
        super(context);
    }

    public CommentLevelSelector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentLevelSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getLevel() {
        return level;
    }

    private ImageView[] imgs;
    private View[] views;
    public void init(int type, int back, int level, LevelSelectorListener callback){
        this.type = type;
        this.back = back;
        this.level = level;
        this.callback = callback;

        View v;
        if (type==1){
            v = LayoutInflater.from(getContext()).inflate(R.layout.item_choose_comment_level, this, false);
        } else {
            v = LayoutInflater.from(getContext()).inflate(R.layout.item_choose_comment_level_star, this, false);
        }
        imgs = new ImageView[5];
        views = new View[5];
        imgs[0] = (ImageView) v.findViewById(R.id.iv1);
        imgs[1] = (ImageView) v.findViewById(R.id.iv2);
        imgs[2] = (ImageView) v.findViewById(R.id.iv3);
        imgs[3] = (ImageView) v.findViewById(R.id.iv4);
        imgs[4] = (ImageView) v.findViewById(R.id.iv5);
        views[0] = v.findViewById(R.id.v1);
        views[1] = v.findViewById(R.id.v2);
        views[2] = v.findViewById(R.id.v3);
        views[3] = v.findViewById(R.id.v4);
        views[4] = v.findViewById(R.id.v5);
        for (int i=0;i<5;i++){
            final int index = i;
            views[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelectChanged(index);
                }
            });
        }
        addView(v);
    }

    private void setSelectChanged(int index){
        for (int i=0;i<5;i++){
            if (i<=index){
                imgs[i].setSelected(true);
            } else {
                imgs[i].setSelected(false);
            }
        }
        level = index+1;
        if (callback!=null){
            callback.onSelectorCallback(type, back, level);
        }
    }

    public interface LevelSelectorListener{
        void onSelectorCallback(int type, int back, int level);
    }

}

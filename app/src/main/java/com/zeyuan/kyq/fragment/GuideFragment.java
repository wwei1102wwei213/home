package com.zeyuan.kyq.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zeyuan.kyq.R;

/**
 * 使用该类fragment需要先给position赋值
 *
 * Created by Administrator on 2016/3/4.
 *
 * @author   wwei
 */

public class GuideFragment extends Fragment {

    private int position;

    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ImageView view = (ImageView)inflater.inflate(R.layout.item_fragment_viewpager_guiide,null);
        switch (position){
            case 1:
                view.setImageResource(R.mipmap.guide1);
                break;
            case 2:
                view.setImageResource(R.mipmap.guide2);
                break;
            case 3:
                view.setImageResource(R.mipmap.guide3);
                break;
            case 4:
                view.setImageResource(R.mipmap.guide4);
                break;
        }
        return view;
    }
}

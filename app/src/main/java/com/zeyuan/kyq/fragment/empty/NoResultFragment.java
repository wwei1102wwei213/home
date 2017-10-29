package com.zeyuan.kyq.fragment.empty;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseFragment;
import com.zeyuan.kyq.view.ReleaseForumActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoResultFragment extends BaseFragment {
    public interface OnClickCircleLister{
        void onClickCircle();
    }

    private OnClickCircleLister onClickCircleLister;

    public void setOnClickCircleLister(OnClickCircleLister onClickCircleLister) {
        this.onClickCircleLister = onClickCircleLister;
    }

    public NoResultFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_no_result, container, false);
        initView();
        return rootView;
    }

    private ImageView img;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;

    private void initView() {
        img = (ImageView) findViewById(R.id.img);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        if (resId != -1) {
            img.setImageResource(resId);
        }

        if (!TextUtils.isEmpty(str_tv1)) {
            this.tv1.setText(str_tv1);
        }
        if (!TextUtils.isEmpty(str_tv2)) {
            tv2.setVisibility(View.VISIBLE);
            this.tv2.setText(str_tv2);
        }

        if (!TextUtils.isEmpty(str_tv3)) {
            tv3.setVisibility(View.VISIBLE);
            tv3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("去发帖".equals(str_tv3)) {
                        startActivity(new Intent(context, ReleaseForumActivity.class));
                    }else {
                        if (onClickCircleLister != null) {
                            onClickCircleLister.onClickCircle();
                        }
                    }
                }
            });
            this.tv3.setText(str_tv3);
        }
    }

    private int resId = -1;
    private String str_tv1;
    private String str_tv2;
    private String str_tv3;

    public void setStr_tv1(String str_tv1) {
        this.str_tv1 = str_tv1;
    }

    public void setStr_tv2(String str_tv2) {
        this.str_tv2 = str_tv2;
    }

    public void setStr_tv3(String str_tv3) {
        this.str_tv3 = str_tv3;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

}

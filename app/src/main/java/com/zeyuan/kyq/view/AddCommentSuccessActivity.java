package com.zeyuan.kyq.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;

/**
 * Created by Administrator on 2017-11-12.
 */

public class AddCommentSuccessActivity extends BaseActivity{

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_success);
        type = getIntent().getIntExtra("add_success_type",0);
        initView();
    }

    private void initView(){
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String title = type==0?"新增成功":"评价医生";
        ((TextView) findViewById(R.id.tv_title)).setText(title);
        ImageView iv = (ImageView) findViewById(R.id.iv);
        iv.setImageResource(type==0?R.mipmap.comment_success_2:R.mipmap.comment_success_1);
        String hint = type==0?"新增后需要通过审核才会显示在列表":"患者的好评就是对我们最大的鼓励";
        ((TextView) findViewById(R.id.tv_hint)).setText(hint);
        findViewById(R.id.tv_success_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toShare();
            }
        });
    }

    private void toShare(){
        finish();
    }
}

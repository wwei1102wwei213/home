package com.zeyuan.kyq.view;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.fragment.main.CircleFriendFragment;

public class FocusFriendActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent();
        setContentView(R.layout.activity_focus_friend);
        initStatusBar();
        initView();
    }

    private void initView() {
        //设置返回键
        findViewById(R.id.ll_back).setOnClickListener(this);
        TextView tv_other_title = (TextView) findViewById(R.id.tv_other_title);
        tv_other_title.setText("圈友列表");
        setType(tv_other_title);
        CircleFriendFragment friendFragment = new CircleFriendFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_fragment, friendFragment);
        transaction.show(friendFragment);
        transaction.commitAllowingStateLoss();
    }

    private void setType(TextView title) {
        String name = getIntent().getStringExtra("tv_title");
        if (!TextUtils.isEmpty(name)) {
            title.setText(name);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;

        }
    }
}

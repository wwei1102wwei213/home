package com.zeyuan.kyq.view;

import android.os.Bundle;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;

/**
 * Created by Administrator on 2015/9/28.
 */
public class RegisterActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initTitlebar("注册");
    }
}

package com.zeyuan.kyq.view;

import android.os.Bundle;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;

/**
 * Created by Administrator on 2016/5/27.
 *
 * 查症状页面
 *
 * @author wwei
 */
public class SearchSymptomActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_drug);
        initTitlebar("查症状");
    }
}

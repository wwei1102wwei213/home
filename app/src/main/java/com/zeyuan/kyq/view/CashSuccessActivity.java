package com.zeyuan.kyq.view;

import android.os.Bundle;
import android.view.View;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;

/**
 * Created by Administrator on 2016/9/18.
 *
 * 提现成功页面
 *
 * @author wwei
 */
public class CashSuccessActivity extends BaseActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_success);

        findViewById(R.id.btn_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}

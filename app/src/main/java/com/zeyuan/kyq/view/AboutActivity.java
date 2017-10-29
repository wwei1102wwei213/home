package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.UserinfoData;

/**
 * 关于我们
 */
public class AboutActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initTitlebar(getString(R.string.about));
        TextView versionName = (TextView) findViewById(R.id.version_name);
        TextView tv_login_info = (TextView) findViewById(R.id.tv_login_info);
        String name = DataUtils.getVersionName(this);
        versionName.setText(getString(R.string.version_name) + name);
        String type = UserinfoData.getLoginType(this);
        String loginInfo = "ID: " + UserinfoData.getInfoID(this);
        if ("1".equals(type)) {
            loginInfo += " (微信账号登录)";
        } else {
            loginInfo += " (QQ账号登录)";
        }
        tv_login_info.setText(loginInfo);
        findViewById(R.id.work).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(AboutActivity.this, WorkActivity.class));
                return false;
            }
        });
    }

}

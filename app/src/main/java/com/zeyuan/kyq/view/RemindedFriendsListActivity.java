package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.RemindUserEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.utils.ExceptionUtils;

import java.util.ArrayList;

import static com.zeyuan.kyq.utils.Const.InfoCenterID;

public class RemindedFriendsListActivity extends BaseActivity {
    private LinearLayout ll_friends_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminded_friends_list);
        initView();
    }

    private void initView() {
        initTitlebar("@到的圈友");
        ll_friends_list = (LinearLayout) findViewById(R.id.ll_friends_list);
        ArrayList<RemindUserEntity> remindUserEntities = getIntent().getParcelableArrayListExtra("data");
        setData(remindUserEntities);
    }

    private void setData(ArrayList<RemindUserEntity> remindUserInfo) {
        if (remindUserInfo != null && remindUserInfo.size() > 0) {
            ll_friends_list.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(this);
            for (final RemindUserEntity rue : remindUserInfo
                    ) {
                try {
                    View item = inflater.inflate(R.layout.item_user_select, ll_friends_list, false);
                    ImageView civ_heard = (ImageView) item.findViewById(R.id.civ_heard);
                    TextView tv_name = (TextView) item.findViewById(R.id.tv_name);
                    item.findViewById(R.id.cb_select).setVisibility(View.GONE);
                    ll_friends_list.addView(item);
                    Glide.with(this).load(rue.getOss_request_url()).error(R.mipmap.loading_fail).into(civ_heard);
                    tv_name.setText(rue.getInfoName());
                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(rue.getInfoId()))
                                startActivity(new Intent(RemindedFriendsListActivity.this, InfoCenterActivity.class).putExtra(InfoCenterID, rue.getInfoId()));
                        }
                    });
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "setData");
                }
            }

        }

    }
}

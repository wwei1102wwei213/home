package com.zeyuan.kyq.view;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.zeyuan.kyq.Entity.MsgSettingsEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.UMSettingsBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/19.
 * 消息设置
 */

public class MessageSettingsActivity extends BaseActivity
        implements HttpResponseInterface, CompoundButton.OnCheckedChangeListener {
    private Switch switch_msg_remind;
    private Switch switch_msg_comment;
    private Switch switch_msg_fans;
    private int type;//0 .读取     1.保存
    private MsgSettingsEntity msgSettingsEntity = new MsgSettingsEntity();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_settings);
        initWhiteTitle("消息设置");
        initView();
    }

    private void initView() {
        switch_msg_remind = (Switch) findViewById(R.id.switch_msg_remind);
        switch_msg_comment = (Switch) findViewById(R.id.switch_msg_comment);
        switch_msg_fans = (Switch) findViewById(R.id.switch_msg_fans);

        switch_msg_fans.setEnabled(false);
        switch_msg_comment.setEnabled(false);
        switch_msg_remind.setEnabled(false);
        type = 0;
        //获取开关信息
        Factory.postPhp(MessageSettingsActivity.this, Const.PApi_UM_Switch);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_msg_remind:
                if (isChecked) {

                    msgSettingsEntity.setCall(0);
                } else {
                    msgSettingsEntity.setCall(1);

                }

                break;
            case R.id.switch_msg_comment:
                if (isChecked) {

                    msgSettingsEntity.setComment(0);
                } else {
                    msgSettingsEntity.setComment(1);

                }

                break;
            case R.id.switch_msg_fans:
                if (isChecked) {

                    msgSettingsEntity.setFollower(0);
                } else {
                    msgSettingsEntity.setFollower(1);

                }

                break;

        }
        switch_msg_fans.setEnabled(false);
        switch_msg_comment.setEnabled(false);
        switch_msg_remind.setEnabled(false);
        type = 1;
        Factory.postPhp(MessageSettingsActivity.this, Const.PApi_UM_Switch);
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> params = new HashMap<>();
        if (tag == Const.PApi_UM_Switch) {
            params.put("infoid", UserinfoData.getInfoID(getApplicationContext()));
            params.put("type", type + "");
            if (type == 1) {
                params.put("call", msgSettingsEntity.getCall() + "");
                params.put("follower", msgSettingsEntity.getFollower() + "");
                params.put("comment", msgSettingsEntity.getComment() + "");
            }

        }

        return params;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PApi_UM_Switch) {
            switch_msg_fans.setEnabled(true);
            switch_msg_comment.setEnabled(true);
            switch_msg_remind.setEnabled(true);
            UMSettingsBean baseBean = (UMSettingsBean) response;
            if (baseBean.getiResult().equals(Const.RESULT)) {
                //  showToast("操作成功！");
                setSwitchStatus(baseBean.getData());
            }
            if (type == 0) {
                type = 1;
            }

        }


    }

    @Override
    public void showLoading(int flag) {

    }

    @Override
    public void hideLoading(int flag) {

    }

    @Override
    public void showError(int flag) {

    }

    private void setSwitchStatus(MsgSettingsEntity msgSettingsEntity) {
        if (msgSettingsEntity == null)
            return;
        //取消监听，避免重复触发
        switch_msg_remind.setOnCheckedChangeListener(null);
        switch_msg_comment.setOnCheckedChangeListener(null);
        switch_msg_fans.setOnCheckedChangeListener(null);
        this.msgSettingsEntity = msgSettingsEntity;
        if (msgSettingsEntity.getCall() == 0) {
            switch_msg_remind.setChecked(true);
        } else {
            switch_msg_remind.setChecked(false);
        }
        if (msgSettingsEntity.getComment() == 0) {
            switch_msg_comment.setChecked(true);
        } else {
            switch_msg_comment.setChecked(false);
        }
        if (msgSettingsEntity.getFollower() == 0) {
            switch_msg_fans.setChecked(true);
        } else {
            switch_msg_fans.setChecked(false);
        }
        switch_msg_remind.setOnCheckedChangeListener(this);
        switch_msg_comment.setOnCheckedChangeListener(this);
        switch_msg_fans.setOnCheckedChangeListener(this);
    }
}

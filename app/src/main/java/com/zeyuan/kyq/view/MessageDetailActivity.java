package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.umeng.message.UTrack;
import com.umeng.message.entity.UMessage;
import com.zeyuan.kyq.Entity.PushNewEntity;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.db.DBHelper;
import com.zeyuan.kyq.service.ZYNotificationClickHandler;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.LogCustom;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/7/26.
 *
 * 自定义推送处理页面
 *
 * @author wwei
 */
public class MessageDetailActivity extends BaseActivity {

    private static final String GO_APP = "go_app";
    private static final String GO_URL = "go_url";
    private static final String GO_ACTIVITY = "go_activity";
    private static final String GO_CUSTOM = "go_custom";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ZYNotificationClickHandler handler = new ZYNotificationClickHandler();

        try {

            Intent intent = getIntent();
            String message = intent.getStringExtra("MessageAll");
            LogCustom.i(Const.TAG.ZY_UMENG, "message:" + message);
            UMessage msg = new UMessage(new JSONObject(message));
            //完全自定义消息的点击统计
            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
            long tills = intent.getLongExtra("MessageTime",0);
            PushNewEntity entity = new PushNewEntity();
            entity.setRead(1);
            entity.setTime(tills);
            entity.setMsg(message);
            DBHelper.getInstance().updataPushTable(ZYApplication.application,entity);
            String after = msg.after_open;
            if(!TextUtils.isEmpty(after)){
                if(GO_APP.equals(after)){
                    handler.launchApp(this,msg);
                }else if(GO_URL.equals(after)){
                    handler.openUrl(this,msg);
                }else if(GO_ACTIVITY.equals(after)){
                    handler.openActivity(this,msg);
                }else if(GO_CUSTOM.equals(after)){
                    handler.dealWithCustomAction(this,msg);
                }
            }else {
                if(msg.url != null && !TextUtils.isEmpty(msg.url.trim())) {
                    handler.openUrl(this, msg);
                } else if(msg.activity != null && !TextUtils.isEmpty(msg.activity.trim())) {
                    handler.openActivity(this, msg);
                } else if(msg.custom != null && !TextUtils.isEmpty(msg.custom.trim())) {
                    handler.dealWithCustomAction(this, msg);
                } else {
                    handler.launchApp(this, msg);
                }
            }
            finish();
        }catch (Exception e){
            Log.i("ZYS","自定义消息处理出错");
        }
    }


}

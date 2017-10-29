package com.zeyuan.kyq.service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/8/22.
 *
 *
 * @author wwei
 */
public class SMSBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        // 判断短信是否发送成功
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                Toast.makeText(context, "短信发送成功", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(context, "发送失败", Toast.LENGTH_LONG).show();
                break;
        }

    }
}

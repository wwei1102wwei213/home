package com.zeyuan.kyq.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.LogCustom;

/**
 * Created by Administrator on 2016/9/22.
 *
 *
 * @author wwei
 */
public class BootReceiver extends BroadcastReceiver {

    private Context mContext;

    public BootReceiver(){

    }

    public BootReceiver(Context context){
        mContext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        LogCustom.i("ZYO","INTENT:onReceiver");

        try {
            Intent mIntent = new Intent(context,ZYKaqService.class);
            mIntent.setAction(Const.SERVICE_START);
            mIntent.setPackage(context.getPackageName());
            context.startService(mIntent);
        }catch (Exception e){

        }

    }

}

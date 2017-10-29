package com.zeyuan.kyq.service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.utils.NetworkUtils;
import com.zeyuan.kyq.view.SplashActivity;

/**
 * Created by Administrator on 2016/5/9.
 *
 * 网络状态监听广播
 *
 * @wwei
 */
public class NetBroadcastReceiver extends BroadcastReceiver{

    public static NetworkChangeListener mListener;
    public static String mAction = "android.net.conn.CONNECTIVITY_CHANGE";


    private long spaceTime = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent==null){
            return;
        }

        ZYApplication.mNetWorkState = NetworkUtils.getNetworkState(context);
        if (ZYApplication.mNetWorkState!=0){
//            ZYApplication.mNetDelayFlag = false;
            if(ZYApplication.isFirst){
                mListener.OnNetChanged();
                ZYApplication.isFirst = false;
                Log.i("ZYS", "isFirst：" + ZYApplication.isFirst);
            }
        }else {
            if(mListener instanceof SplashActivity){
                mListener.OnNetChangedNone();
            }else{
//                ZYApplication.mNetDelayFlag = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(ZYApplication.mNetWorkState==0){
                            mListener.OnNetChangedNone();
                        }
                    }
                },5000);
            }

                /*if ((System.currentTimeMillis() - spaceTime) > 5000) {

                    spaceTime =  System.currentTimeMillis();
                }*/

        }
    }

    public interface NetworkChangeListener{
        void OnNetChangedNone();
        void OnNetChanged();
    }

}

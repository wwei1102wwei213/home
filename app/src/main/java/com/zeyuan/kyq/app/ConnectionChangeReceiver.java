package com.zeyuan.kyq.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2016/1/28.
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (activeNetInfo != null) {
            Toast.makeText(context, "Active Network Type : " +
                    activeNetInfo.getTypeName(), Toast.LENGTH_SHORT).show();
        }
        if (mobNetInfo != null) {
            Toast.makeText(context, "Mobile Network Type : " +
                    mobNetInfo.getTypeName(), Toast.LENGTH_SHORT).show();
        }
        if (activeNetInfo == null && mobNetInfo == null) {
            LogUtil.i("mainActivity","网络监听");
            if (android.os.Build.VERSION.SDK_INT > 10) {
                //3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
                context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            } else {
                context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }
        }*/
    }
}

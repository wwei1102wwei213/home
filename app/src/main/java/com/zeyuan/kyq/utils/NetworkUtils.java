package com.zeyuan.kyq.utils;


import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

import com.zeyuan.kyq.fragment.dialog.ZYDialog;

/**
 * Created by Administrator on 2016/5/9.
 *
 *  网络状态检测
 *
 */
public class NetworkUtils {

    public static boolean checkNetworkState(final Context context)
    {
        try {
            //判断有没有网
            ConnectivityManager manager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo=manager.getActiveNetworkInfo();
            if (activeNetworkInfo==null)
            {
                //没网显示一个dialog
                ZYDialog.Builder dialog=new ZYDialog.Builder(context);
                dialog.setTitle("网络状态");
                dialog.setMessage("亲，您现在没网络！");
                //打开网络
                dialog.setPositiveButton("打开网络", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //回调 callback
                        try {
                            //判断android的版本
                            int version = android.os.Build.VERSION.SDK_INT;
                            if (version > 10) {
                                Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                                context.startActivity(intent);
                                dialog.cancel();
                            }
                        } catch (Exception e) {
                            ExceptionUtils.ExceptionToUM(e, context, "checkNetworkState");
                        }
                    }
                });

                dialog.setNegativeButton("取消", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.cancel();
//                        System.exit(0);
                    }
                });
                dialog.create().show();
                return false;
            }

        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, context, "checkNetworkState");
        }
        return true;
    }

    public static void openNetworkPrompt(final Context context){
        ZYDialog.Builder dialog=new ZYDialog.Builder(context);
        dialog.setTitle("网络状态");
        dialog.setMessage("亲，现在没有网络");
        //打开网络
        dialog.setPositiveButton("打开网络", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //回调 callback
                try {
                    //判断android的版本
                    int version = android.os.Build.VERSION.SDK_INT;
                    if (version > 10) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                        context.startActivity(intent);
                        dialog.cancel();
                    }
                } catch (Exception e) {
                    ExceptionUtils.ExceptionToUM(e, context, "checkNetworkState");
                }
            }
        });

        dialog.setNegativeButton("取消", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
//                        System.exit(0);
            }
        });
        dialog.create().show();
    }

    public static void openNetworkHint(final Context context){
        ZYDialog.Builder dialog=new ZYDialog.Builder(context);
        dialog.setTitle("网络状态");
        dialog.setMessage("亲，现在没有网络");
        dialog.setNegativeButton("知道了", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.create().show();
    }

    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;

    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // Wifi
        State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (state == NetworkInfo.State.CONNECTED || state == State.CONNECTING) {
            return NETWORN_WIFI;
        }

        // 3G
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NETWORN_MOBILE;
        }
        return NETWORN_NONE;
    }


}

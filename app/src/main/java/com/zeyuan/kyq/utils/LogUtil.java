package com.zeyuan.kyq.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Administrator on 2015/8/24.
 * 打印管理类
 */
public final class LogUtil {
    private static final int FLAG = -1;


    public static void i(String TAG, String mes) {
        if (FLAG > 0)
            Log.i(TAG, mes);
    }

    public static void f(String mes,int num){
        if (FLAG>0) {
            if (TextUtils.isEmpty(mes) || mes.length() < 20) return;

            int temp = mes.length() / num;
            Log.i("ZYL", "分段数据" + mes.length());

            for (int i = 0, j = temp; i < mes.length() && j < mes.length(); i += temp, j += temp) {
                Log.i("ZYL", "分段数据" + i / temp + ":" + mes.substring(i, j));
            }

            Log.i("ZYL", "分段数据:" + mes.substring(mes.length() - 500, mes.length()));
        }
    }

    public static void i(String TAG, boolean mes) {
        if (FLAG > 0)
            Log.i(TAG, mes + "");
    }

    public static void i(String TAG, int mes) {
        if (FLAG > 0)
            Log.i(TAG, mes + "");
    }

    public static void i(String TAG, float mes) {
        if (FLAG > 0)
            Log.i(TAG, mes + "");
    }

    public static void e(String TAG, String mes) {
        if (FLAG > 0)
            Log.e(TAG, mes);
    }

    public static void v(String TAG, String mes) {
        if (FLAG > 0)
            Log.i(TAG, mes);
    }

    public static void d(String TAG, String mes) {
        if (FLAG > 0)
            Log.d(TAG, mes);
    }
}

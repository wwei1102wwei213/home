package com.zeyuan.kyq.utils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 *
 * 异常信息处理工具类
 *
 * Created by Administrator on 2016/3/4.
 *
 * @author wwei
 *
 */
public class ExceptionUtils{

    private static final String TAG = "EXCO";

    public static void ExceptionSend(Exception e){
        LogCustom.e(TAG,e.toString());
    }

    public static void ExceptionSend(Exception e,String where){
        try {
            LogCustom.e(TAG,"异常位置：" + where + "\n" + "异常信息：" + e.toString());
            StackTraceElement[] stack = e.getStackTrace();
            StringBuilder builder = new StringBuilder();
            for(int i = 0;i<stack.length;i++){
                builder.append(stack[i].toString()+"\n");
            }
            LogCustom.e(TAG, "异常信息详情("+where+")：" + builder.toString());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void ExceptionToUM(Exception e,Context context,String where){
        try {
            MobclickAgent.reportError(context, e.fillInStackTrace());
            LogCustom.e(TAG, "异常位置：" + where + "\n" + "异常信息：" + e.toString());

            StackTraceElement[] stack = e.getStackTrace();
            StringBuilder builder = new StringBuilder();
            for(int i = 0;i<stack.length;i++){
                builder.append(stack[i].toString()+"\n");
            }
            LogCustom.e(TAG, "异常位置：" + where + "\n" + "异常信息详情：" + builder.toString());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}

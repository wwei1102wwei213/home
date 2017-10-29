package com.zeyuan.kyq.utils;

import android.content.Context;
import android.view.WindowManager;

import static com.ta.TAApplication.getApplication;

/**
 * Created by Administrator on 2017/5/31.
 * 像素转换工具
 */

public class DensityUtil {
    public DensityUtil() {
    }

    public static int dip2px(Context var0, float var1) {
        float var2 = var0.getResources().getDisplayMetrics().density;
        return (int) (var1 * var2 + 0.5F);
    }

    public static int px2dip(Context var0, float var1) {
        float var2 = var0.getResources().getDisplayMetrics().density;
        return (int) (var1 / var2 + 0.5F);
    }

    public static int sp2px(Context var0, float var1) {
        float var2 = var0.getResources().getDisplayMetrics().scaledDensity;
        return (int) (var1 * var2 + 0.5F);
    }

    public static int px2sp(Context var0, float var1) {
        float var2 = var0.getResources().getDisplayMetrics().scaledDensity;
        return (int) (var1 / var2 + 0.5F);
    }

    //获取状态栏高度，返回px值
    public static int getStatusBarHeight(Context context) {
        try {
            int statusBarHeight1;
            //获取status_bar_height资源的ID
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                statusBarHeight1 = context.getResources().getDimensionPixelSize(resourceId);
            } else {
                statusBarHeight1 = DensityUtil.dip2px(getApplication(), 25);
                LogCustom.d("zyv", "resourceId=" + resourceId);
            }
            LogCustom.d("zyv", "statusbarheight=" + statusBarHeight1);
            return statusBarHeight1;

        } catch (Exception e) {
            LogCustom.d("zyv", "statusbarheight获取错误");
            return DensityUtil.dip2px(getApplication(), 25);

        }

    }

    //获取屏幕宽度 dp值
    public static int getWindowDPWidth(Context context) {
        WindowManager winManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return px2dip(context,winManager.getDefaultDisplay().getWidth());
    }
    //获取屏幕高度 dp值
    public static int getWindowDPHeight(Context context) {
        WindowManager winManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return px2dip(context,winManager.getDefaultDisplay().getHeight());
    }
}


package com.zeyuan.kyq.utils;

import android.content.Context;
import android.content.res.Resources;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

/**
 * 设备工具类
 * Created by cxw on 2016/1/29.
 */
public class DeviceUtils {
    /**
     * 检查是否有虚拟键盘
     * @param activity
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context activity) {
        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = ViewConfiguration.get(activity).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        if (!hasMenuKey && !hasBackKey) {
            // 做任何你需要做的,这个设备有一个导航栏
            return true;
        }
        return false;
    }

    /**
     * 获取导航栏高度
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        try {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height",
                    "dimen", "android");
            //获取NavigationBar的高度
            int height = resources.getDimensionPixelSize(resourceId);
            return height;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }
}

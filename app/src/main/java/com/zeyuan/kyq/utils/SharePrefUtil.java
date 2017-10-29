package com.zeyuan.kyq.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zeyuan.kyq.application.ZYApplication;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * SharePreferences操作工具类
 */
public final class SharePrefUtil {

    private final static String SP_NAME = "config";
    private final static String SP_NAME_FORUM_DATA = "forum_data";//编辑帖子的缓存数据
    private final static String SP_NAME_TIME_STAMP = "time_stamp";//圈子点击的时间戳
    private final static String SP_NAME_CLICK_EVENT = "click_event";//点击事件数据
    private final static String SP_NAME_MY_CIRCLE = "my_circle";//我的圈子数据
    private static SharedPreferences sp;

    /**
     * 保存布尔值
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveBoolean(Context context, String key, boolean value) {
        try {
            if (sp == null)
                sp = context.getSharedPreferences(SP_NAME, 0);
            sp.edit().putBoolean(key, value).commit();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "偏好设置出错");
        }
    }


    public static void cleanData(Context context) {
        try {
            if (sp == null) {
                sp = context.getSharedPreferences(SP_NAME, 0);
            }
            sp.edit().clear().commit();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "偏好设置出错");
        }
    }

    /**
     * 保存字符串
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveString(Context context, String key, String value) {
        try {
            if (sp == null)
                sp = context.getSharedPreferences(SP_NAME, 0);
            sp.edit().putString(key, value).commit();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "偏好设置出错");
        }
    }

    public static void clear(Context context) {
        try {
            if (sp == null)
                sp = context.getSharedPreferences(SP_NAME, 0);
            sp.edit().clear().commit();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "偏好设置出错");
        }

    }


    /**
     * 保存long型
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveLong(Context context, String key, long value) {
        try {
            if (sp == null)
                sp = context.getSharedPreferences(SP_NAME, 0);
            sp.edit().putLong(key, value).commit();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "偏好设置出错");
        }

    }

    /**
     * 保存int型
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveInt(Context context, String key, int value) {
        try {
            if (sp == null)
                sp = context.getSharedPreferences(SP_NAME, 0);
            sp.edit().putInt(key, value).commit();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "偏好设置出错");
        }

    }

    /**
     * 保存float型
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveFloat(Context context, String key, float value) {

        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        sp.edit().putFloat(key, value).commit();
    }

    /***
     *
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveStringSet(Context context, String key, Set<String> value) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        sp.edit().putStringSet(key, value).commit();
    }

    public static Set<String> getStringSet(Context context, String key) {
        if (sp == null) sp = context.getSharedPreferences(SP_NAME, 0);
        return sp.getStringSet(key, new LinkedHashSet<String>());
    }

    /**
     * 获取字符值
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(Context context, String key, String defValue) {
        try {
            if (sp == null)
                sp = context.getSharedPreferences(SP_NAME, 0);
            return sp.getString(key, defValue);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "偏好设置出错");
        }
        return defValue;
    }

    /**
     * 获取int值
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static int getInt(Context context, String key, int defValue) {
        try {
            if (sp == null)
                sp = context.getSharedPreferences(SP_NAME, 0);
            return sp.getInt(key, defValue);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "偏好设置出错");
        }
        return defValue;
    }

    /**
     * 获取long值
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static long getLong(Context context, String key, long defValue) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        return sp.getLong(key, defValue);
    }

    /**
     * 获取float值
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static float getFloat(Context context, String key, float defValue) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        return sp.getFloat(key, defValue);
    }

    /**
     * 获取布尔值
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getBoolean(Context context, String key,
                                     boolean defValue) {
        try {
            if (sp == null)
                sp = context.getSharedPreferences(SP_NAME, 0);
            return sp.getBoolean(key, defValue);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "偏好设置出错");
        }
        return defValue;
    }

    //是否存在缓存的帖子数据
    public static boolean isHadForumData(Context context) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(SP_NAME_FORUM_DATA, 0);
        return sp.getBoolean("isHadForumData", false);
    }

    //保存数据
    public static void saveForumData(Context context, String title, String content) {
        SharedPreferences.Editor spe = context.getApplicationContext().getSharedPreferences(SP_NAME_FORUM_DATA, 0).edit();
        spe.putString("title", title);
        spe.putString("content", content);
        spe.putBoolean("isHadForumData", true);
        spe.apply();
    }

    //清理数据
    public static void clearForumData(Context context) {
        SharedPreferences.Editor spe = context.getApplicationContext().getSharedPreferences(SP_NAME_FORUM_DATA, 0).edit();
        spe.putString("title", "");
        spe.putString("content", "");
        spe.putBoolean("isHadForumData", false);
        spe.apply();
    }

    //读取数据
    public static String[] getForumData(Context context) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(SP_NAME_FORUM_DATA, 0);
        String[] ForumData = new String[2];
        ForumData[0] = sp.getString("title", "");
        ForumData[1] = sp.getString("content", "");
        return ForumData;
    }

    //获取圈子点击的时间戳
    public static String[] getLastTimeStamp(Context context) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(SP_NAME_TIME_STAMP, 0);
        String[] ts = new String[3];
        ts[0] = sp.getString("t2", "0");
        ts[1] = sp.getString("t3", "0");
        ts[2] = sp.getString("t4", "0");
        return ts;
    }

    //存储圈子点击的时间戳
    public static void saveTimeStamp(Context context, String[] ts) {
        try {
            SharedPreferences.Editor spe = context.getApplicationContext().getSharedPreferences(SP_NAME_TIME_STAMP, 0).edit();
            spe.putString("t2", ts[0]);
            spe.putString("t3", ts[1]);
            spe.putString("t4", ts[2]);
            spe.apply();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "saveTimeStamp");
        }
    }

    //保存点击数据
    public static void saveClickEventData(Context context, String eventData) {
        SharedPreferences.Editor spe = context.getApplicationContext().getSharedPreferences(SP_NAME_CLICK_EVENT, 0).edit();
        spe.putString("eventData", eventData);
        spe.apply();
    }

    //读取保存的点击数据
    public static String readClickEventData(Context context) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(SP_NAME_CLICK_EVENT, 0);
        return sp.getString("eventData", "");
    }

    //清理点击数据
    public static void clearClickEventData(Context context) {
        SharedPreferences.Editor spe = context.getApplicationContext().getSharedPreferences(SP_NAME_CLICK_EVENT, 0).edit();
        spe.putString("eventData", "");
        spe.apply();
    }

    //保存我的圈子的数据
    public static void saveMyCircleData(String myCircleString) {
        if (ZYApplication.application != null) {
            SharedPreferences.Editor spe = ZYApplication.application.getSharedPreferences(SP_NAME_MY_CIRCLE, 0).edit();
            spe.putString("myCircle", myCircleString);
            spe.apply();
        }
    }

    //读取我的圈子数据
    public static String readMyCircleData() {
        if (ZYApplication.application != null) {
            SharedPreferences sp = ZYApplication.application.getSharedPreferences(SP_NAME_MY_CIRCLE, 0);
            return sp.getString("myCircle", "");
        }
        return "";
    }

    //删除我的圈子数据
    public static void clearMyCircleData() {
        if (ZYApplication.application != null) {
            SharedPreferences.Editor spe = ZYApplication.application.getSharedPreferences(SP_NAME_MY_CIRCLE, 0).edit();
            spe.putString("myCircle", "");
            spe.apply();
        }
    }
}

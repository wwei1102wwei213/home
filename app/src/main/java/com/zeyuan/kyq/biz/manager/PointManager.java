package com.zeyuan.kyq.biz.manager;

import android.content.Context;
import android.text.TextUtils;

import com.zeyuan.kyq.bean.ShortcutBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.SharePrefUtil;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/7.
 * 小红点管理类
 */

public class PointManager {
    private static PointManager instance;
    private String t2, t3, t4;//记录上次点击的时间戳
    private String point = "";

    public String getPoint() {
        if (!TextUtils.isEmpty(point))
            return point;
        else
            return "";
    }

    //设置点击好友后的时间戳
    public void setT2(Context context) {
        this.t2 = String.valueOf(System.currentTimeMillis() / 1000);
        saveTimeStamp(context, t2, t3, t4);
    }

    //设置点击项目后的时间戳
    public void setT3(Context context) {
        this.t3 = String.valueOf(System.currentTimeMillis() / 1000);
        saveTimeStamp(context, t2, t3, t4);
    }

    //设置点击地域圈后的时间戳
    public void setT4(Context context) {
        this.t4 = String.valueOf(System.currentTimeMillis() / 1000);
        saveTimeStamp(context, t2, t3, t4);
    }

    private void saveTimeStamp(Context context, String t2, String t3, String t4) {
        String[] tss = new String[3];
        tss[0] = t2;
        tss[1] = t3;
        tss[2] = t4;
        SharePrefUtil.saveTimeStamp(context, tss);
    }

    public static synchronized PointManager getInstance() {

        if (instance == null) {
            instance = new PointManager();
        }

        return instance;
    }

    /*刷新小红点数据*/
    public void refreshPointData(final Context context) {
        String[] ts = SharePrefUtil.getLastTimeStamp(context);
        t2 = ts[0];
        t3 = ts[1];
        t4 = ts[2];
        Factory.postPhp(new HttpResponseInterface() {
            @Override
            public Map getParamInfo(int tag) {
                Map<String, String> map = new HashMap<>();
                map.put("InfoID", UserinfoData.getInfoID(context));
                map.put("pointTypeStr", "1@0," + "2@" + t2 + ",3@" + t3 + ",4@" + t4);
                return map;
            }

            @Override
            public byte[] getPostParams(int flag) {
                return new byte[0];
            }

            @Override
            public void toActivity(Object response, int flag) {
                ShortcutBean bean = (ShortcutBean) response;
                if (Const.RESULT.equals(bean.getiResult())) {
                    if (onPointNumChangedListenerList != null && onPointNumChangedListenerList.size() > 0 && bean.getPointType() != null) {
                        point = Arrays.toString(bean.getPointType()).replace("[", "").replace(",", "");
                        for (OnPointNumChangedListener opl : onPointNumChangedListenerList
                                ) {
                            opl.onPointNumChanged(point);
                        }

                    }
                }
            }

            @Override
            public void showLoading(int flag) {

            }

            @Override
            public void hideLoading(int flag) {

            }

            @Override
            public void showError(int flag) {

            }
        }, Const.PApi_getCircleShortcut);
    }

    private List<OnPointNumChangedListener> onPointNumChangedListenerList;

    public void addOnPointNumChangedListener(OnPointNumChangedListener onPointNumChangedListener) {
        if (onPointNumChangedListenerList == null)
            onPointNumChangedListenerList = new ArrayList<>();
        this.onPointNumChangedListenerList.add(onPointNumChangedListener);
    }

    //清除监听器，避免内存泄漏
    public void clearOnPointNumChangedListener() {
        onPointNumChangedListenerList.clear();
    }

    public interface OnPointNumChangedListener {
        void onPointNumChanged(String pointString);
    }
}

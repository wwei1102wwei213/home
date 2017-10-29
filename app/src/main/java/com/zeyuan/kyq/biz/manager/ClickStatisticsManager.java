package com.zeyuan.kyq.biz.manager;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.zeyuan.kyq.Entity.ClickEventEntity;
import com.zeyuan.kyq.bean.ClickStatisticsBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.SharePrefUtil;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/8.
 * 点击统计工具
 */

public class ClickStatisticsManager {
    private static ClickStatisticsManager instance;
    private List<ClickEventEntity> data;

    private ClickStatisticsManager() {
        data = new ArrayList<>();
    }

    public static synchronized ClickStatisticsManager getInstance() {
        if (instance == null)
            instance = new ClickStatisticsManager();
        return instance;
    }

    //添加点击事件
    public void addClickEvent(String type, String tag) {
        ClickEventEntity cee = new ClickEventEntity();
        cee.setClicktime(String.valueOf(System.currentTimeMillis() / 1000));
        cee.setLooktype(type);
        cee.setLooktag(tag);
        data.add(cee);
    }

    //上传点击数据
    public void uploadClickData(Context context) {
        final Context context1 = context.getApplicationContext();
        final String ds = SharePrefUtil.readClickEventData(context1);
        if (TextUtils.isEmpty(ds))
            return;
        LogCustom.d("uploadClickData", ds);
        Factory.postPhp(new HttpResponseInterface() {
            @Override
            public Map getParamInfo(int tag) {
                Map<String, String> map = new HashMap<>();
                map.put("data", ds);
                map.put("InfoID", UserinfoData.getInfoID(context1.getApplicationContext()));
                return map;
            }

            @Override
            public byte[] getPostParams(int flag) {
                return new byte[0];
            }

            @Override
            public void toActivity(Object response, int flag) {
                ClickStatisticsBean statisticsBean = (ClickStatisticsBean) response;
                if (statisticsBean.getiResult().equals(Const.RESULT)) {
                    clearEventData(context1);
                    data.clear();
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
        }, Const.PApi_LookCount);
    }

    //保存事件，下次启动时上传
    public void saveEventData(Context context) {
        if (data != null && data.size() > 0) {
            Gson gson = new Gson();
            String ds = gson.toJson(data);
            LogCustom.d("saveEventData", ds);
            SharePrefUtil.saveClickEventData(context, ds);
        }

    }


    //清除数据
    private void clearEventData(Context context) {
        SharePrefUtil.clearClickEventData(context);
    }
}

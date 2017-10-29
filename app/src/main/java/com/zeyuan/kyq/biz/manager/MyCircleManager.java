package com.zeyuan.kyq.biz.manager;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.MyCircleBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.SharePrefUtil;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/22.
 * 我的圈子数据管理器
 */

public class MyCircleManager {
    private static MyCircleManager instance;

    //判断是否有圈子数据
    public boolean hadCircles() {
        return ZYApplication.DefaultCircles != null && ZYApplication.DefaultCircles.size() > 0;
    }

    private MyCircleManager() {
    }

    public static MyCircleManager getInstance() {
        if (instance == null) {
            instance = new MyCircleManager();
        }
        return instance;
    }

    //初始化获取我的圈子数据
    public void init(final Context context) {
        if (!TextUtils.isEmpty(SharePrefUtil.readMyCircleData())) {
            LogCustom.d("zyd", "EGetMycircle保存的圈子数据为：" + SharePrefUtil.readMyCircleData());
            try {
                Gson gson = new Gson();
                MyCircleBean bean = gson.fromJson(SharePrefUtil.readMyCircleData(), MyCircleBean.class);
                if (Const.RESULT.equals(bean.getIResult())) {
                    List<String> lists = bean.getFollowCircleNum();
                    ZYApplication.DefaultCircles = bean.getDefaultCircle();
                    ZYApplication.threadCircle = bean.getThreadCircle();
                    ZYApplication.RemindNum = bean.getRemindNum();
                    bindFollowCircle(lists, context);
                    LogCustom.d("zyd", "EGetMycircle圈子初始化完成！");
                }
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "CircleFindFragment");
            }

        }
        //更新数据
        Factory.post(new HttpResponseInterface() {
            @Override
            public Map getParamInfo(int tag) {
                return null;
            }

            @Override
            public byte[] getPostParams(int flag) {
                String[] args = null;
                if (flag == Const.EGetMycircle) {
                    String temp;
                    String StepID = UserinfoData.getStepID(context);
                    String CancerID = UserinfoData.getCancerID(context);
                    String CureConfID = MapDataUtils.getAllCureconfID(StepID);
                    String ProvinceID;
                    String CityID = UserinfoData.getCityID(context);
                    temp = Contants.InfoID + ",,," + UserinfoData.getInfoID(context);
                    if (Const.NO_STEP.equals(UserinfoData.getIsHaveStep(context))) {
                        temp += ",,," + Const.ISHAVESTEP + ",,," + "0";
                    } else {
                        if (!TextUtils.isEmpty(StepID)) {
                            temp += ",,," + Contants.StepID + ",,," + StepID;
                        }
                        if (!TextUtils.isEmpty(CureConfID)) {
                            temp += ",,," + Contants.CureConfID + ",,," + CureConfID;
                        } else {
                            temp += ",,," + Contants.CureConfID + ",,," + "0";
                        }
                        temp += ",,," + Const.ISHAVESTEP + ",,," + "1";
                    }
                    if (!TextUtils.isEmpty(CancerID)) {
                        temp += ",,," + Contants.CancerID + ",,," + CancerID;
                    }
                    if ("0".equals(CityID)) {
                        ProvinceID = "0";
                        temp += ",,," + Contants.CityID + ",,," + CityID;
                        temp += ",,," + Contants.ProvinceID + ",,," + ProvinceID;
                    } else {
                        ProvinceID = CityID.substring(0, 2) + "0000";
                        temp += ",,," + Contants.CityID + ",,," + CityID;
                        temp += ",,," + Contants.ProvinceID + ",,," + ProvinceID;
                    }
                    UserinfoData.saveProvinceID(context, ProvinceID);
                    temp += ",,,page,,," + "0";
                    temp += ",,,pagesize,,," + "2";
                    //args = new String[]{};
                    args = temp.split(",,,");
                }
                return HttpSecretUtils.getParamString(args);
            }

            @Override
            public void toActivity(Object response, int flag) {
                try {
                    MyCircleBean bean = (MyCircleBean) response;
                    if (Const.RESULT.equals(bean.getIResult())) {
                        //保存圈子
                        Gson gson = new Gson();
                        SharePrefUtil.saveMyCircleData(gson.toJson(bean));

                        List<String> lists = bean.getFollowCircleNum();
                        ZYApplication.DefaultCircles = bean.getDefaultCircle();
                        ZYApplication.threadCircle = bean.getThreadCircle();
                        ZYApplication.RemindNum = bean.getRemindNum();
                        bindFollowCircle(lists, context);
                        LogCustom.d("zyh", "EGetMycircle圈子数据更新完成！");
                    }
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "CircleFindFragment");
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
        }, Const.EGetMycircle);


    }

    private void bindFollowCircle(List<String> lists, Context context) {
        if (lists != null && lists.size() >= 0) {
            if (lists.size() > 0) {//存圈子
                List<String> list1 = new ArrayList<>();
                for (String entity : lists) {
                    list1.add(entity);
                }
                UserinfoData.saveFocusCirlce(context, list1);
            }
        }
    }

    //清除 我的圈子 数据
    public void clearCircle() {
        SharePrefUtil.clearMyCircleData();
        ZYApplication.DefaultCircles = null;
        ZYApplication.threadCircle = null;
        ZYApplication.RemindNum = 0;
        LogCustom.d("zyh", "圈子数据清理完成！");
    }
}

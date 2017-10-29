package com.zeyuan.kyq.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;

import com.zeyuan.kyq.Entity.PatientDataEntity;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.PatientDetailBean;
import com.zeyuan.kyq.utils.Secret.MovingSecretUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 偏好设置帮助类
 * <p>
 * Created by Administrator on 2016/1/6.
 * <p>
 * 个人用户信息 内存中没有就去硬盘中拿。
 */
public class UserinfoData {
    private static String StepID;
    public static String performid;//小医生的症状
    public static String planId;//小医生的症状
    private static String discoverTime;
    private static String sInfoID;
    private static String InfoName;
    private static String CancerID;
    private static String CityID;
    private static String ProvinceID;
    private static String PeriodID;
    private static String AvatarUrl;
    private static String CureConfID;
    private static String IsHaveStep;
    private static String Current;
    //    private static String ReplyNum;
    private static boolean RedPointShow;
    private static boolean IsTNM;
    public static String TNMText;
    public static String WD;//余额

    //文章已读ID数组
    private static SparseBooleanArray RecordArticleArray;
    //发帖草稿类容
    private static String releaseContent;
    //发帖草稿标题
    private static String repleaseTitle;
    //文章评论草稿
    private static SparseArray<String> DtaftArticleArray;
    //帖子评论草稿
    private static SparseArray<String> DtaftForumArray;

    public static SparseArray<String> getDtaftArticleArray() {
        if (DtaftArticleArray == null) DtaftArticleArray = new SparseArray<>();

        return DtaftArticleArray;
    }

    public static SparseArray<String> getDtaftForumArray() {
        if (DtaftForumArray == null) DtaftForumArray = new SparseArray<>();

        return DtaftForumArray;
    }

    public static String getReleaseContent() {
        return releaseContent;
    }

    public static void setReleaseContent(String data) {
        releaseContent = data;
    }

    public static String getRepleaseTitle() {
        return repleaseTitle;
    }

    public static void setRepleaseTitle(String repleaseTitle) {
        UserinfoData.repleaseTitle = repleaseTitle;
    }

    public static SparseBooleanArray getRecordArticleArray() {
        if (RecordArticleArray == null) RecordArticleArray = new SparseBooleanArray();
        return RecordArticleArray;
    }

    public static void setRecordArticleArray(SparseBooleanArray recordArticleArray) {
        RecordArticleArray = recordArticleArray;
    }


    public static String getStepID() {
        return StepID;
    }

    /**
     * 得到stepid 内存中没有就去硬盘中拿。
     *
     * @param context
     * @return
     */
    public static String getStepID(Context context) {
        if (TextUtils.isEmpty(StepID)) {
            String stepID = SharePrefUtil.getString(context, Contants.StepID, null);
            if (TextUtils.isEmpty(stepID)) {
                return null;
            } else {
                StepID = stepID;
            }
        }
        return StepID;
    }

    /**
     * 得到IsHaveStep 内存中没有就去硬盘中拿。
     *
     * @param context
     * @return
     */
    public static String getIsHaveStep(Context context) {
        if (TextUtils.isEmpty(IsHaveStep)) {
            String temp = SharePrefUtil.getString(context, Const.ISHAVESTEP, "0");
            if (TextUtils.isEmpty(temp)) {
                return "0";
            } else {
                IsHaveStep = temp;
            }
        }
        return IsHaveStep;
    }

    public static void saveIsHaveStep(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Const.ISHAVESTEP, data);
        IsHaveStep = data;
    }

    /***
     * getReplyNum 读取回复数量
     * saveReplyNum 存储回复数量
     * */

    public static int getReplyNum(Context context) {
        return SharePrefUtil.getInt(context, Contants.ReplyNum, 0);
    }

    public static void saveReplyNum(Context context, int data) {
        SharePrefUtil.saveInt(context, Contants.ReplyNum, data);
    }

    /***
     * getReplyNum 读取新回复状态
     * saveReplyNum 存储新回复状态
     * */
    public static boolean getRedPointShow(Context context) {
        return SharePrefUtil.getBoolean(context, Contants.RedPointShow, false);
    }

    public static void saveRedPointShow(Context context, boolean data) {
        SharePrefUtil.saveBoolean(context, Contants.RedPointShow, data);
    }


    public static void saveStepID(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Contants.StepID, data);
        StepID = data;
    }

    public static void saveDiscoverTime(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Contants.DiscoverTime, data);
        discoverTime = data;
    }

    //获取确诊时间
    public static String getDiscoverTime(Context context) {
        if (TextUtils.isEmpty(discoverTime)) {
            String stepID = SharePrefUtil.getString(context, Contants.DiscoverTime, null);
            if (TextUtils.isEmpty(stepID)) {
                return null;
            } else {
                discoverTime = stepID;
            }
        }
        return discoverTime;
    }

//    public static String getInfoID() {
//        return sInfoID;
//    }

    /**
     * 得到infoid
     *
     * @param context
     * @return
     */
    public static String getInfoID(Context context) {
        if (TextUtils.isEmpty(sInfoID)) {
            String inforid = SharePrefUtil.getString(context, Contants.SAVE_INFO_ID, null);
            if (TextUtils.isEmpty(inforid)) {
                sInfoID = null;
            } else {
                sInfoID = inforid;
            }
        }
        return sInfoID;
    }

    public static void saveInfoID(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Contants.SAVE_INFO_ID, data);
        sInfoID = data;
    }


    public static void saveInfoname(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Contants.InfoName, data);
        InfoName = data;
    }


    public static String getInfoname(Context context) {
        if (TextUtils.isEmpty(InfoName)) {
            String infoname = SharePrefUtil.getString(context, Contants.InfoName, null);
            if (TextUtils.isEmpty(infoname)) {
                return "";
            } else {
                InfoName = infoname;
            }
        }
        return InfoName;
    }

    /**
     * 这个是qq登录还是微信登录
     *
     * @param context
     * @param data
     */
    public static void saveType(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Contants.type, data);
    }

    public static String getType(Context context) {
        String cancerID = SharePrefUtil.getString(context, Contants.type, null);
        if (TextUtils.isEmpty(cancerID)) {
            return null;
        }
        return cancerID;
    }

//    private String logintype;

    /***
     * 保存登录标志
     * 1QQ 2WX
     *
     * @param context
     * @param data
     */
    public static void saveLoginType(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        ZYApplication.setLoginType(data);
        SharePrefUtil.saveString(context, "logintypeww", data);
    }

    public static String getLoginType(Context context) {
        return SharePrefUtil.getString(context, "logintypeww", null);
    }

    /***
     * 保存UnionID
     *
     * @param context
     * @param data
     */
    public static void saveUnionID(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, "unionidww", data);
    }

    public static String getUnionID(Context context) {

        return SharePrefUtil.getString(context, "unionidww", null);
    }

    /***
     * 保存OpenID
     *
     * @param context
     * @param data
     */
    public static void saveOpenID(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, "openidww", data);
    }

    public static String getOpenID(Context context) {
        return SharePrefUtil.getString(context, "openidww", null);
    }

    /*
    * 获取手机号码
    * */
    public static String getPhoneNum(Context context) {
        return SharePrefUtil.getString(context, "phoneNum", "");

    }

    /*
    * 保存手机号码
    * */
    public static void savePhoneNum(Context context, String PhoneNum) {
        SharePrefUtil.saveString(context, "phoneNum", PhoneNum);
    }

    //保存当前状态
    public static void savaCurrent(Context context, String current) {
        SharePrefUtil.saveString(context, "current", current);
    }

    //获取当前状态
    public static String getCurrent(Context context) {
        if (TextUtils.isEmpty(Current)) {
            String current = SharePrefUtil.getString(context, "current", null);
            if (TextUtils.isEmpty(current)) {
                return null;
            } else {
                Current = current;
            }
        }

        return Current;
    }

    /**
     * 获得CancerID
     */
    public static String getCancerID(Context context) {
        if (TextUtils.isEmpty(CancerID)) {
            String cancerID = SharePrefUtil.getString(context, Contants.CancerID, null);
            if (TextUtils.isEmpty(cancerID)) {
                return null;
            } else {
                CancerID = cancerID;
            }
        }

        return CancerID;
    }

    public static void saveCancerID(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Contants.CancerID, data);
        CancerID = data;
    }


    public static String getCityID(Context context) {
        if (TextUtils.isEmpty(CityID)) {
            String cityID = SharePrefUtil.getString(context, Contants.CityID, null);
            if (TextUtils.isEmpty(cityID)) {
                return "0";
            } else {
                CityID = cityID;
            }
        }
        return CityID;
    }

    public static void saveCityID(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Contants.CityID, data);
        CityID = data;
    }


    public static String getProvinceID(Context context) {
        if (TextUtils.isEmpty(ProvinceID)) {
            String provinceID = SharePrefUtil.getString(context, Contants.ProvinceID, null);
            if (TextUtils.isEmpty(provinceID)) {
                return "0";
            } else {
                ProvinceID = provinceID;
            }
        }
        return ProvinceID;
    }

    public static void saveProvinceID(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Contants.ProvinceID, data);
        ProvinceID = data;
    }


    public static String getPeriodID(Context context) {
        if (TextUtils.isEmpty(PeriodID)) {
            String provinceID = SharePrefUtil.getString(context, Contants.PeriodID, null);
            if (TextUtils.isEmpty(provinceID)) {
                return "0";
            } else {
                PeriodID = provinceID;
            }
        }
        return PeriodID;
    }

    public static void savePeriodID(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Contants.PeriodID, data);
        PeriodID = data;
    }

    public static void saveAvatarUrl(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Contants.Avatar, data);
        AvatarUrl = data;
    }


    public static String getAvatarUrl(Context context) {
        if (TextUtils.isEmpty(AvatarUrl)) {
            String avatarUrl = SharePrefUtil.getString(context, Contants.Avatar, "");
            if (TextUtils.isEmpty(avatarUrl)) {
                return "";
            } else {
                AvatarUrl = avatarUrl;
            }
        }
        return AvatarUrl;
    }

    /**
     * 保存关注的圈子
     *
     * @param context
     * @param list
     */
    public static void saveFocusCirlce(Context context, List<String> list) {

        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            if (sb.length() == 0) {
                sb.append(str);
            } else {
                sb.append("," + str);
            }
        }
        SharePrefUtil.saveString(context, "follow_circles", sb.toString());
    }

    /**
     * 得到关注的圈子
     *
     * @param context
     * @return
     */
    public static List<String> getFocusCircle(Context context) {

        List<String> list = new ArrayList<>();
        String temp = SharePrefUtil.getString(context, "follow_circles", null);
        if (TextUtils.isEmpty(temp)) {
            return null;
        }
        String[] strings = temp.split(",");
        for (String str : strings) {
            list.add(str);
        }
        return list;
    }

    public static void removeFocusCircle(Context context, String circleId) {
        List<String> focusCircle = getFocusCircle(context);
        if (!focusCircle.contains(circleId) || focusCircle.size() == 0) {
            return;
        }
//        int index = focusCircle.indexOf(circleId);
        focusCircle.remove(circleId);
        saveFocusCirlce(context, focusCircle);
    }


    public static void addFocusCircle(Context context, String circleId) {
        List<String> focusCircle = getFocusCircle(context);
        focusCircle.add(circleId);
        saveFocusCirlce(context, focusCircle);
    }


    public static void saveRemindTime(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Contants.RemindTime, data);
    }

    public static String getRemindTime(Context context) {
        String avatarUrl = SharePrefUtil.getString(context, Contants.RemindTime, "");
        if (TextUtils.isEmpty(avatarUrl)) {
            return "";
        }
        return avatarUrl;
    }

    private static final long day_mills = 24 * 60 * 60 * 1000;//一天的毫秒数

    public static boolean compareTime(Context context) {
        String remindTime = getRemindTime(context);
        if (TextUtils.isEmpty(remindTime)) {
            return true;
        }
        long saveTime = Long.valueOf(remindTime);
        if (System.currentTimeMillis() - saveTime >= day_mills) {
            return true;
        }
        return false;
    }

    public static boolean compareHomeTime(Context context) {
        String remindTime = getHomeRemindTime(context);
        if (TextUtils.isEmpty(remindTime)) {
            return true;
        }
        long saveTime = Long.valueOf(remindTime);
        if (System.currentTimeMillis() - saveTime >= day_mills) {
            return true;
        }
        return false;
    }

    public static void saveHomeRemindTime(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Const.HomeRecordTime, data);
    }

    public static String getHomeRemindTime(Context context) {
        String avatarUrl = SharePrefUtil.getString(context, Const.HomeRecordTime, "");
        if (TextUtils.isEmpty(avatarUrl)) {
            return "";
        }
        return avatarUrl;
    }


    public static void saveUserData(Context context, PatientDetailBean patientDetailBean) {
        UserinfoData.saveInfoname(context, patientDetailBean.getInfoName());
        UserinfoData.saveIsHaveStep(context, patientDetailBean.getIsHaveStep());
        UserinfoData.saveStepID(context, patientDetailBean.getStepID());
        UserinfoData.saveCancerID(context, patientDetailBean.getCancerID());
        UserinfoData.saveDiscoverTime(context, patientDetailBean.getDiscoverTime());
        UserinfoData.savePeriodID(context, patientDetailBean.getPeriodID());
        String headImgUrl = patientDetailBean.getHeadimgurl();
        if (TextUtils.isEmpty(headImgUrl) || "badiu".equals(headImgUrl)) {
            headImgUrl = "";
        }
        UserinfoData.saveAvatarUrl(context, headImgUrl);
        UserinfoData.saveInfoname(context, patientDetailBean.getInfoName());
        String cityID = patientDetailBean.getCity();
        String provinceID;
        UserinfoData.saveCityID(context, cityID);
        if (TextUtils.isEmpty(cityID) || "0".equals(cityID)) {
            provinceID = "";
        } else {
            provinceID = cityID.substring(0, 2) + "0000";
        }
        UserinfoData.saveProvinceID(context, provinceID);
        UserinfoData.setWD(patientDetailBean.getK(), patientDetailBean.getWD());
    }

    public static void saveUserDataChange(Context context, PatientDetailBean patientDetailBean) {
        UserinfoData.saveInfoname(context, patientDetailBean.getInfoName());
        UserinfoData.saveIsHaveStep(context, patientDetailBean.getIsHaveStep());
        UserinfoData.saveStepID(context, patientDetailBean.getStepID());
        UserinfoData.saveCancerID(context, patientDetailBean.getCancerID());
        UserinfoData.saveDiscoverTime(context, patientDetailBean.getDiscoverTime());
        UserinfoData.savePeriodID(context, patientDetailBean.getPeriodID());
        String headImgUrl = patientDetailBean.getHeadimgurl();
        UserinfoData.saveAvatarUrl(context, headImgUrl);
        String cityID = patientDetailBean.getCity();
        UserinfoData.saveCityID(context, cityID);
    }

    public static void saveUserDataChange(Context context, PatientDataEntity mEntity) {
        UserinfoData.saveCancerID(context, mEntity.getCancer() + "");
        UserinfoData.saveDiscoverTime(context, mEntity.getDiscoverTime() + "");
        UserinfoData.savePeriodID(context, mEntity.getPeriodID() + "");
    }

    public static void saveUserDataChange(Context context, String name, String url, String id) {
        UserinfoData.saveInfoname(context, name);
        UserinfoData.saveAvatarUrl(context, url);
        UserinfoData.saveCityID(context, id);
    }

    //保存是不是本人
    public static void saveIsMyself(Context context, String data) {
        SharePrefUtil.saveString(context, "isMyself", data);
    }

    //读取是不是本人
    public static String getIsMyself(Context context) {
        return SharePrefUtil.getString(context, "isMyself", "1");
    }

    //我的钱包余额
    public static String getWD() {
        return TextUtils.isEmpty(WD) ? "" : WD;
    }

    private static void setWD(int[] key, String wd) {
        if (key != null && key.length == 4) {
            MovingSecretUtils.TEA.setKey(key);
            if (TextUtils.isEmpty(wd)) {
                WD = "0.00";
            } else {
                if ("0,".equals(wd)) {
                    WD = "0.00";
                } else {
                    if (wd.contains(",")) {
                        String[] args = wd.split(",");
                        String temp = args[1];
                        LogCustom.i("ZYS", "temp:" + temp);
                        if (TextUtils.isEmpty(temp)) {
                            WD = "0.00";
                        } else {
                            temp = MovingSecretUtils.TEA.decryptByTea(temp);
                            WD = temp;
                        }

                    } else {
                        WD = "0.00";

                    }
                }
            }
        }
    }

    public static void clearMermory(Context context) {
        StepID = null;
        planId = null;
        performid = null;
        sInfoID = null;
        discoverTime = null;
        InfoName = null;
        CancerID = null;
        CityID = null;
        ProvinceID = null;
        PeriodID = null;
        AvatarUrl = null;
        IsHaveStep = null;
        Current = null;
        SharePrefUtil.cleanData(context);
    }


    public static void saveFirstGuide(Context context) {
        SharePrefUtil.saveBoolean(context, "first_guide", true);
    }

    public static boolean getFirstGuide(Context context) {
        return SharePrefUtil.getBoolean(context, "first_guide", false);
    }


    /**
     * 保存TNM分期标志
     */
    public static void saveTNM(Context context, boolean flag) {
        SharePrefUtil.saveBoolean(context, "digitistnm", flag);
    }

    /**
     * 获取TNM分期标志
     */
    public static boolean getTNM(Context context) {
        return SharePrefUtil.getBoolean(context, "digitistnm", false);
    }

    /**
     * 保存用户所选择的TNMID数据
     */
    public static void saveTNMIDText(Context context, String data) {
        SharePrefUtil.saveString(context, "tnmidtext", data);
    }

    /**
     * 获取用户所选择的TNMID数据
     */
    public static String getTNMIDText(Context context) {
        return SharePrefUtil.getString(context, "tnmidtext", "");
    }

    /**
     * 保存用户所选择的TNM数据
     */
    public static void saveTNMText(Context context, String data) {
        SharePrefUtil.saveString(context, "tnmtext", data);
    }

    /**
     * 获取用户所选择的TNM数据
     */
    public static String getTNMText(Context context) {
        return SharePrefUtil.getString(context, "tnmtext", "");
    }


    public static String getCureConfID(Context context) {
        if (TextUtils.isEmpty(CureConfID)) {
            String cureConfID = SharePrefUtil.getString(context, Contants.CureConfID, null);
            if (TextUtils.isEmpty(cureConfID)) {
                return "";
            } else {
                CureConfID = cureConfID;
            }
        }

        return CureConfID;
    }

    public static void saveCureConfID(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Contants.CureConfID, data);
        CureConfID = data;
    }

    public static boolean getIsHaveSconf(Context context) {
        return SharePrefUtil.getBoolean(context, Contants.IsHaveSconf, false);
    }

    public static void saveISHaveSconf(Context context, boolean data) {
        SharePrefUtil.saveBoolean(context, Contants.IsHaveSconf, data);
    }

    /***
     *
     * 加密解密key的保存与获取
     *
     * @param context
     * @return
     */
    public static String getKeyStr(Context context) {
        return SharePrefUtil.getString(context, Contants.SaveKeyString, null);
    }

    public static void saveKeyStr(Context context, String data) {
        SharePrefUtil.saveString(context, Contants.SaveKeyString, data);
    }

    /***
     * 获取主配置数据
     * 可以在以下两个方法中进行加解密操作
     *
     * @param context
     * @return
     */
    public static String getSyncConfData(Context context) {
        String syncConfData = SharePrefUtil.getString(context, Contants.SyncConfData, null);
        if (TextUtils.isEmpty(syncConfData)) {
            return null;
        }
        Log.i("Dec", "解密操作");
        return DecryptUtils.decodeBase64(syncConfData);
    }

    /***
     *
     * getMainPage数据缓存
     *
     * @param context
     * @param data
     */
    public static void saveGetMainPage(Context context, String data) {
        if (!TextUtils.isEmpty(data)) {
            SharePrefUtil.saveString(context, Const.E_GET_MAIN_PAGE, data);
        }
    }

    public static String getGetMainPage(Context context) {
        return SharePrefUtil.getString(context, Const.E_GET_MAIN_PAGE, null);
    }

    public static void saveDiagnoseTime(Context context, String data) {
        if (!TextUtils.isEmpty(data)) {
            SharePrefUtil.saveString(context, "diagnosed_time", data);
        }
    }

    public static String getDiagnoseTime(Context context) {
        return SharePrefUtil.getString(context, "diagnosed_time", 0 + "");
    }


    /**
     * 症状搜索历史记录
     */
    public static void saveRecordSymptom(Context context, String data) {
        SharePrefUtil.saveString(context, "recordsymptom", data);
    }

    /**
     * 症状搜索历史记录
     */
    public static String getRecordSymptom(Context context) {
        return SharePrefUtil.getString(context, "recordsymptom", "");
    }

    /**
     * 圈子搜索历史记录
     */
    public static void saveRecordCircle(Context context, String data) {
        SharePrefUtil.saveString(context, "recordcircle", data);
    }

    /**
     * 圈子搜索历史记录
     */
    public static String getRecordCircle(Context context) {
        return SharePrefUtil.getString(context, "recordcircle", "");
    }

    /**
     * 搜索历史记录
     */
    public static void saveSearchRecord(Context context, String key, String data) {
        SharePrefUtil.saveString(context, key, data);
    }

    public static String getSearchRecord(Context context, String key) {
        return SharePrefUtil.getString(context, key, "");
    }

    /**
     * 保存是否创建用户的标志
     */
    public static void saveIsHaveCreateInfo(Context context, String data) {
        SharePrefUtil.saveString(context, "IsHaveCreateInfo", data);
    }

    public static String getIsHaveCreateInfo(Context context) {
        return SharePrefUtil.getString(context, "IsHaveCreateInfo", "0");
    }

    /**
     * 保存是否创建用户的标志
     */
    public static void saveIsHaveCreateInfo21(Context context, String data) {
        SharePrefUtil.saveString(context, "IsHaveCreateInfo21", data);
    }

    public static String getIsHaveCreateInfo21(Context context) {
        return SharePrefUtil.getString(context, "IsHaveCreateInfo21", "0");
    }

    /**
     * 保存临时图像地址
     */
    public static void saveTempHeadImgUrl(Context context, String data) {
        SharePrefUtil.saveString(context, "TempHeadImageUrl", data);
    }

    public static String getTempHeadImgUrl(Context context) {
        return SharePrefUtil.getString(context, "TempHeadImageUrl", "");
    }

    /**
     * 保存用户信息
     */
    public static String getUserInfoMsg(Context context, String id) {
        if (TextUtils.isEmpty(id)) return "";
        return SharePrefUtil.getString(context, id + "UserInfo", "0");
    }

    public static void saveUserInfoMsg(Context context, String id, String data) {
        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(data)) {
            SharePrefUtil.saveString(context, id + "UserInfo", data);
        }
    }

    /**
     * 保存推送信息
     */
    public static int getUserPuskMsg(Context context) {
        return SharePrefUtil.getInt(context, "PushMsg", 0);
    }

    public static void saveUserPushMsg(Context context, int data) {
        if (data > 0 || data == 0) {
            SharePrefUtil.saveInt(context, "PushMsg", data);
        }
    }

    /**
     * 保存推送信息
     */
    public static int getUserOldPuskMsg(Context context) {
        return SharePrefUtil.getInt(context, "OldPushMsg", 0);
    }

    public static void saveUserOldPushMsg(Context context, int data) {
        if (data > 0 || data == 0) {
            SharePrefUtil.saveInt(context, "OldPushMsg", data);
        }
    }

    /**
     * 2.2版本初次进入标识
     */
    public static int getFlagForVer22(Context context) {
        return SharePrefUtil.getInt(context, "flagForVer22", 0);
    }

    public static void setFlagForVer22(Context context, int data) {
        SharePrefUtil.saveInt(context, "flagForVer22", data);
    }

}

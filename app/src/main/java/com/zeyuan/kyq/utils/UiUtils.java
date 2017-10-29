package com.zeyuan.kyq.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import com.meelive.ingkee.sdk.plugin.InKeSdkPluginAPI;
import com.zeyuan.kyq.Entity.BindMobileEntity;
import com.zeyuan.kyq.Entity.HomePageEntity;
import com.zeyuan.kyq.Entity.LiveBaseBean;
import com.zeyuan.kyq.Entity.LiveItemEntity;
import com.zeyuan.kyq.Entity.MainBannerEntity;
import com.zeyuan.kyq.Entity.PatientDataEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.FragmentCallBack;
import com.zeyuan.kyq.fragment.ChooseCancerFragment;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.view.AllMenuActivity;
import com.zeyuan.kyq.view.ArticleDetailActivity;
import com.zeyuan.kyq.view.ArticleTypeActivity;
import com.zeyuan.kyq.view.CareListActivity;
import com.zeyuan.kyq.view.FocusFriendActivity;
import com.zeyuan.kyq.view.ForumDetailActivity;
import com.zeyuan.kyq.view.FourmListActivity;
import com.zeyuan.kyq.view.HeadLineActivity;
import com.zeyuan.kyq.view.HomeSymptomActivity;
import com.zeyuan.kyq.view.InfoCenterActivity;
import com.zeyuan.kyq.view.MainActivity;
import com.zeyuan.kyq.view.MatchArticleActivity;
import com.zeyuan.kyq.view.MedicalRecordActivity;
import com.zeyuan.kyq.view.MoreCircleNewActivity;
import com.zeyuan.kyq.view.MyFosCircleActivity;
import com.zeyuan.kyq.view.NewCircleActivity;
import com.zeyuan.kyq.view.PerfectDataActivity;
import com.zeyuan.kyq.view.RecordClassifyActivity;
import com.zeyuan.kyq.view.ReleaseForumActivity;
import com.zeyuan.kyq.view.SearchComplicationActivity;
import com.zeyuan.kyq.view.SearchDrugActivity;
import com.zeyuan.kyq.view.SearchQuotaActivity;
import com.zeyuan.kyq.view.SelectCancerActivity;
import com.zeyuan.kyq.view.ServiceCenterActivity;
import com.zeyuan.kyq.view.ShowDiscuzActivity;
import com.zeyuan.kyq.view.SimilarActivity;
import com.zeyuan.kyq.view.YouzanActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zeyuan.kyq.utils.Const.InfoCenterID;

public class UiUtils {

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    static public int getScreenWidthPixels(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 判断是否在主线程
     *
     * @return
     */
    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 换算像素值
     *
     * @param context
     * @param dip
     * @return
     */
    static public int dipToPx(Context context, int dip) {
        return (int) (dip * getScreenDensity(context) + 0.5f);
    }

    static public float getScreenDensity(Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                    .getMetrics(dm);
            return dm.density;
        } catch (Exception e) {
            return DisplayMetrics.DENSITY_DEFAULT;
        }
    }

    /**
     * 获取症状图片
     *
     * @param id     症状id
     * @param select 是否选中
     * @return
     */
    public static int getSymptomImage(String id, boolean select) {

        if (TextUtils.isEmpty(id)) {
            if (select) {
                return R.mipmap.rv_body_11_select;
            } else {
                return R.mipmap.rv_body_11_no_select;
            }
        }
        int t = Integer.valueOf(id);
        int res;
        if (select) {
            switch (t) {
                case 0:
                    res = R.mipmap.rv_body_all_select;
                    break;
                case 11:
                    res = R.mipmap.rv_body_11_select;
                    break;
                case 12:
                    res = R.mipmap.rv_body_12_select;
                    break;
                case 13:
                    res = R.mipmap.rv_body_13_select;
                    break;
                case 15:
                    res = R.mipmap.rv_body_15_select;
                    break;
                case 17:
                    res = R.mipmap.rv_body_17_select;
                    break;
                case 18:
                    res = R.mipmap.rv_body_18_select;
                    break;
                case 19:
                    res = R.mipmap.rv_body_19_select;
                    break;
                case 22:
                    res = R.mipmap.rv_body_22_select;
                    break;
                case 23:
                    res = R.mipmap.rv_body_23_select;
                    break;
                case 24:
                    res = R.mipmap.rv_body_24_select;
                    break;
                case 25:
                    res = R.mipmap.rv_body_25_select;
                    break;
                case 26:
                    res = R.mipmap.rv_body_26_select;
                    break;
                case 27:
                    res = R.mipmap.rv_body_27_select;
                    break;
                case 28:
                    res = R.mipmap.rv_body_28_select;
                    break;
                case 29:
                    res = R.mipmap.rv_body_29_select;
                    break;
                case 30:
                    res = R.mipmap.rv_body_30_select;
                    break;
                case 33:
                    res = R.mipmap.rv_body_33_select;
                    break;
                case 34:
                    res = R.mipmap.rv_body_34_select;
                    break;
                case 35:
                    res = R.mipmap.rv_body_35_select;
                    break;
                case 36:
                    res = R.mipmap.rv_body_36_select;
                    break;
                case 37:
                    res = R.mipmap.rv_body_37_select;
                    break;
                case 38:
                    res = R.mipmap.rv_body_38_select;
                    break;
                case 39:
                    res = R.mipmap.rv_body_39_select;
                    break;
                case 44:
                    res = R.mipmap.rv_body_44_select;
                    break;
                case 45:
                    res = R.mipmap.rv_body_45_select;
                    break;
                default:
                    res = R.mipmap.rv_body_all_select;
                    break;
            }
        } else {
            switch (t) {
                case 0:
                    res = R.mipmap.rv_body_all_no_select;
                    break;
                case 11:
                    res = R.mipmap.rv_body_11_no_select;
                    break;
                case 12:
                    res = R.mipmap.rv_body_12_no_select;
                    break;
                case 13:
                    res = R.mipmap.rv_body_13_no_select;
                    break;
                case 15:
                    res = R.mipmap.rv_body_15_no_select;
                    break;
                case 17:
                    res = R.mipmap.rv_body_17_no_select;
                    break;
                case 18:
                    res = R.mipmap.rv_body_18_no_select;
                    break;
                case 19:
                    res = R.mipmap.rv_body_19_no_select;
                    break;
                case 22:
                    res = R.mipmap.rv_body_22_no_select;
                    break;
                case 23:
                    res = R.mipmap.rv_body_23_no_select;
                    break;
                case 24:
                    res = R.mipmap.rv_body_24_no_select;
                    break;
                case 25:
                    res = R.mipmap.rv_body_25_no_select;
                    break;
                case 26:
                    res = R.mipmap.rv_body_26_no_select;
                    break;
                case 27:
                    res = R.mipmap.rv_body_27_no_select;
                    break;
                case 28:
                    res = R.mipmap.rv_body_28_no_select;
                    break;
                case 29:
                    res = R.mipmap.rv_body_29_no_select;
                    break;
                case 30:
                    res = R.mipmap.rv_body_30_no_select;
                    break;
                case 33:
                    res = R.mipmap.rv_body_33_no_select;
                    break;
                case 34:
                    res = R.mipmap.rv_body_34_no_select;
                    break;
                case 35:
                    res = R.mipmap.rv_body_35_no_select;
                    break;
                case 36:
                    res = R.mipmap.rv_body_36_no_select;
                    break;
                case 37:
                    res = R.mipmap.rv_body_37_no_select;
                    break;
                case 38:
                    res = R.mipmap.rv_body_38_no_select;
                    break;
                case 39:
                    res = R.mipmap.rv_body_39_no_select;
                    break;
                case 44:
                    res = R.mipmap.rv_body_44_no_select;
                    break;
                case 45:
                    res = R.mipmap.rv_body_45_no_select;
                    break;
                default:
                    res = R.mipmap.rv_body_all_no_select;
                    break;
            }
        }
        return res;
    }

    /**
     * 获取症状分类数据
     *
     * @param id
     * @return
     */
    public static List<String> getSymptomClassifyData(String id) {
        List<String> list = new ArrayList<>();
        list.add("0");
        if (TextUtils.isEmpty(id)) return list;
        int t = Integer.valueOf(id);
        switch (t) {
            case 999:
                list.add("18");
                list.add("22");
                break;
            case 998:
                list.add("23");
                list.add("24");
                list.add("44");
                list.add("45");
                break;
            case 997:
                list.add("11");
                list.add("26");
                list.add("27");
                list.add("28");
                list.add("29");
                break;
            case 996:
                list.add("30");
                break;
            case 995:
                list.add("19");
                list.add("12");
                list.add("13");
                list.add("33");
                break;
            case 994:
                list.add("38");
                list.add("34");
                list.add("35");
                list.add("36");
                list.add("37");
                list.add("25");
                break;
            case 993:
                list.add("17");
                list.add("39");
                break;
        }
        return list;
    }

    /**
     * 获取圈子图标
     *
     * @param id
     * @return
     */
    public static int getCancerImage(String id) {

        if (TextUtils.isEmpty(id)) return R.mipmap.sceon_city;

        int t = Integer.valueOf(id);
        int res;

        if (t > 999 && t < 6999) {
            switch (t) {
                case 1001:
                    res = R.mipmap.circle1001;
                    break;
                case 1002:
                    res = R.mipmap.circle1002;
                    break;
                case 1003:
                    res = R.mipmap.circle1003;
                    break;
                case 1004:
                    res = R.mipmap.circle1004;
                    break;
                case 1005:
                    res = R.mipmap.circle1005;
                    break;
                case 1006:
                    res = R.mipmap.circle1006;
                    break;
                case 1007:
                    res = R.mipmap.circle1007;
                    break;
                case 1008:
                    res = R.mipmap.circle1008;
                    break;
                case 1009:
                    res = R.mipmap.circle1009;
                    break;
                case 1010:
                    res = R.mipmap.circle1010;
                    break;
                case 1011:
                    res = R.mipmap.circle1011;
                    break;
                case 1012:
                    res = R.mipmap.circle1012;
                    break;
                case 1013:
                    res = R.mipmap.circle1013;
                    break;
                case 1014:
                    res = R.mipmap.circle1014;
                    break;
                case 1015:
                    res = R.mipmap.circle1015;
                    break;
                case 1016:
                    res = R.mipmap.circle1016;
                    break;
                case 1017:
                    res = R.mipmap.circle1017;
                    break;
                case 1018:
                    res = R.mipmap.circle1018;
                    break;
                case 1019:
                    res = R.mipmap.circle1019;
                    break;
                case 1020:
                    res = R.mipmap.circle1020;
                    break;
                case 1021:
                    res = R.mipmap.circle1021;
                    break;
                case 1022:
                    res = R.mipmap.circle1022;
                    break;
                case 1023:
                    res = R.mipmap.circle1023;
                    break;
                case 1024:
                    res = R.mipmap.circle1024;
                    break;
                case 1025:
                    res = R.mipmap.circle1025;
                    break;
                case 1026:
                    res = R.mipmap.circle1026;
                    break;
                case 1027:
                    res = R.mipmap.circle1027;
                    break;
                case 1028:
                    res = R.mipmap.circle1028;
                    break;
                case 1029:
                    res = R.mipmap.circle1029;
                    break;
                case 1030:
                    res = R.mipmap.circle1030;
                    break;
                case 1031:
                    res = R.mipmap.circle1031;
                    break;
                case 1032:
                    res = R.mipmap.city_def;
                    break;
                case 1033:
                    res = R.mipmap.circle1033;
                    break;
                case 1034:
                    res = R.mipmap.circle1034;
                    break;
                case 1035:
                    res = R.mipmap.circle1035;
                    break;
                case 1036:
                    res = R.mipmap.circle1036;
                    break;
                default:
                    res = R.mipmap.city_def;
            }
            return res;
        } else if (t < 1000) {
            String temp = getCancerParentID(t + "");
            int f = Integer.valueOf(temp);
            switch (f) {
                case 29://肺癌
                    res = R.mipmap.circle_29;
                    break;
                case 33://肝癌
                    res = R.mipmap.circle_33;
                    break;
                case 34://胃癌
                    res = R.mipmap.circle_34;
                    break;
                case 36://肠癌
                    res = R.mipmap.circle_36;
                    break;
                case 37://乳腺
                    res = R.mipmap.circle_37;
                    break;
                case 48://食管
                    res = R.mipmap.circle_48;
                    break;
                case 51:
                    res = R.mipmap.circle_51;
                    break;
                case 55:
                    res = R.mipmap.circle_55;
                    break;
                case 72:
                    res = R.mipmap.circle_72;
                    break;
                case 86:
                    res = R.mipmap.circle_86;
                    break;
                case 94:
                    res = R.mipmap.circle_94;
                    break;
                case 96:
                    res = R.mipmap.circle_96;
                    break;
                case 97:
                    res = R.mipmap.circle_97;
                    break;
                case 148:
                    res = R.mipmap.circle_148;
                    break;
                default:
                    res = R.mipmap.circle_97;
            }
            return res;
        } else {
            switch (t) {
                case 7001:
                    res = R.mipmap.circle_7001;
                    break;
                case 7003:
                    res = R.mipmap.linchuang_circle_img;
                    break;
                case 7007:
                    res = R.mipmap.circle_7007;
                    break;
                case 7006:
                    res = R.mipmap.circle_7006;
                    break;
                case 7014:
                    res = R.mipmap.circle_7014;
                    break;
                case 9999:
                    res = R.mipmap.circle_9999;
                    break;
                default:
                    res = R.mipmap.circle_9999;
            }
            return res;
        }
    }

    /**
     * 获取父类节点癌种id
     *
     * @param id
     * @return
     */
    public static String getCancerParentID(String id) {
        Map<String, String> map = (Map<String, String>) Factory.getData(Const.N_DataCancerParent);
        String temp = map.get(id);
        if (TextUtils.isEmpty(temp)) {
            return "0";
        } else {
            if ("0".equals(temp)) {
                return id;
            } else {
                String temp2 = map.get(temp);
                if (TextUtils.isEmpty(temp2)) {
                    return "0";
                } else {
                    if ("0".equals(temp2)) {
                        return temp;
                    } else {
                        return temp2;
                    }
                }
            }
        }
    }

    /**
     * 点击关注后，结果显示封装
     *
     * @param context
     * @param follow
     * @param IResult
     * @param CircleID
     */
    public static void setFollowUI(Context context, String follow, String IResult, String CircleID) {
        if (Contants.OK_DATA.equals(IResult)) {
            if ("1".equals(follow)) {
                UserinfoData.addFocusCircle(context, CircleID);
                Toast.makeText(context, "关注成功", Toast.LENGTH_SHORT).show();
            } else {
                UserinfoData.removeFocusCircle(context, CircleID);
                Toast.makeText(context, "取消关注成功", Toast.LENGTH_SHORT).show();
            }
        } else {
            if ("-2".equals(IResult)) {
                if ("1".equals(follow)) {
                    Toast.makeText(context, "暂时无法关注该圈子", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "暂时无法取消关注该圈子", Toast.LENGTH_SHORT).show();
                }
            } else {
                if ("1".equals(follow)) {
                    Toast.makeText(context, "关注失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "取消关注失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Banner列表的排序
     *
     * @param list
     * @return
     */
    public static List<MainBannerEntity> sortBannerList(List<MainBannerEntity> list) {
        if (list != null && list.size() > 1) {
            try {
                Collections.sort(list, new Comparator<MainBannerEntity>() {
                    @Override
                    public int compare(MainBannerEntity m1, MainBannerEntity m2) {
                        return Integer.valueOf(m2.getPowerNum()) - Integer.valueOf(m1.getPowerNum());
                    }
                });
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "banner排序出错");
            }
        }
        return list;
    }

    /**
     * Banner列表的排序
     *
     * @param list
     * @return
     */
    public static List<MainBannerEntity> sortEventBannerList(List<MainBannerEntity> list) {
        if (list != null && list.size() > 1) {
            try {
                Collections.sort(list, new Comparator<MainBannerEntity>() {
                    @Override
                    public int compare(MainBannerEntity m1, MainBannerEntity m2) {
                        return Integer.valueOf(m2.getPowerNum()) - Integer.valueOf(m1.getPowerNum());
                    }
                });
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "banner排序出错");
            }
            try {
                Collections.sort(list, new Comparator<MainBannerEntity>() {
                    @Override
                    public int compare(MainBannerEntity m1, MainBannerEntity m2) {
                        if (m1.getPowerNum().equals(m2.getPowerNum())) {
                            return Integer.valueOf(m2.getStarttime()) - Integer.valueOf(m1.getStarttime());
                        } else {
                            return 0;
                        }
                    }
                });
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "banner排序出错");
            }
        }
        return list;
    }

    /**
     * 绑定错误码
     */
    public static String[] getErrorMsg(BindMobileEntity entity, String type, Context context) {
        String[] args = new String[2];
        String result = entity.getiResult();
        if (TextUtils.isEmpty(result)) return args;

        switch (result) {
            case "-1":
                args[0] = "验证码错误";
                break;
            case "-2":
                args[0] = "请获取验证码";
                break;
            case "-3":
                args[0] = "验证码已失效,请重新获取验证码";
                args[1] = "1";
                break;
            case "-4":
                args[0] = "未知错误，错误码1004";
                break;
            case "-5":
                args[0] = "该手机号码已绑定账号";
                args[1] = "1";
                break;
            case "-6":
                args[0] = "未知错误，错误码1006";
                break;
            case "-7":
                args[0] = "未知错误，错误码1007";
                break;
            case "-8":
                args[0] = "未知错误，错误码1008";
                break;
            default:
                args[0] = "未知错误，错误码9999";
        }

        return args;
    }

    //banner转跳
    public static void toBannerJump(Context context, MainBannerEntity entity) {
        if (entity == null || TextUtils.isEmpty(entity.getTagtype()) || TextUtils.isEmpty(entity.getTagurl()))
            return;
        int type = Integer.valueOf(entity.getTagtype());
        String tempUrl = entity.getTagurl();
        switch (type) {
            case 1:
                if (!TextUtils.isEmpty(entity.getId())) {
                    int id = Integer.valueOf(entity.getId());
                    switch (id) {
                        case 4:
                        case 5:
                        case 6:
                            break;
                        default:
                            if (tempUrl.contains("?")) {
                                tempUrl += "&kaq=" + getRandomMath()
                                        + UserinfoData.getInfoID(context) + "&lt=2&Type=2";
                            } else {
                                tempUrl += "?kaq=" + getRandomMath()
                                        + UserinfoData.getInfoID(context) + "&lt=2&Type=2";
                            }
                            Factory.onEvent(context, Const.EVENT_MainTopBanner,
                                    Const.EVENTFLAG, "{type:" + type + ";id:" + entity.getId() + ";url:" + tempUrl + "}");
                            context.startActivity(new Intent(context, ShowDiscuzActivity.class)
                                    .putExtra(Const.SHOW_HTML_MAIN_TOP, tempUrl));
                            break;
                    }
                }
                break;
            case 2:
                if (!TextUtils.isEmpty(entity.getTagurl())) {
                    Factory.onEvent(context, Const.EVENT_MainTopBanner,
                            Const.EVENTFLAG, "{type:" + type + ";id:" + entity.getId() + "}");
                    context.startActivity(new Intent(context, ArticleDetailActivity.class).
                            putExtra(Const.INTENT_ARTICLE_ID, entity.getTagurl()));
                }
                break;
            case 3:
                if (!TextUtils.isEmpty(entity.getTagurl())) {
                    Factory.onEvent(context, Const.EVENT_MainTopBanner,
                            Const.EVENTFLAG, "{type:" + type + ";id:" + entity.getId() + ";FORUM_ID:" + entity.getTagurl() + "}");
                    context.startActivity(new Intent(context, ForumDetailActivity.class)
                            .putExtra(Const.FORUM_ID, entity.getTagurl()));
                }
                break;
            case 4:
                Intent intent = new Intent(context, HeadLineActivity.class);
                if (!TextUtils.isEmpty(entity.getInfotext()) && !TextUtils.isEmpty(entity.getTagurl())) {
                    Factory.onEvent(context, Const.EVENT_MainTopBanner,
                            Const.EVENTFLAG, "{type:" + type + ";id:" + entity.getId() + ";url:" + entity.getTagurl()
                                    + ";infotext:" + entity.getInfotext() + "}");
                    intent.putExtra(Const.HEAD_LIST_TAG_URL, entity.getTagurl())
                            .putExtra(Const.HEAD_LIST_INFO_TEXT, entity.getInfotext());
                }
                context.startActivity(intent);
                break;
            case 5:
                Factory.onEvent(context, Const.EVENT_MainTopBanner,
                        Const.EVENTFLAG, "{type:" + type + ";id:" + entity.getId() + ";CircleID:" + entity.getTagurl() + "}");
                context.startActivity(new Intent(context, NewCircleActivity.class)
                        .putExtra(Contants.CircleID, entity.getTagurl()));
                break;
        }
    }


    public static void toMenuJump(Context context, HomePageEntity entity, FragmentCallBack callback,
                                  boolean isHome, FragmentActivity activity) {
        String type = entity.getSkiptype();
        if (TextUtils.isEmpty(type)) return;
        int t = Integer.valueOf(type);
        String a = entity.getSign_a();
        switch (t) {
            case 1://普通链接
                if (!TextUtils.isEmpty(a)) {
                    //加上登录信息
                    if (a.contains("?")) {
                        a += "&kaq=" + getRandomMath()
                                + UserinfoData.getInfoID(context) + "&lt=2&Type=2";
                    } else {
                        a += "?kaq=" + getRandomMath()
                                + UserinfoData.getInfoID(context) + "&lt=2&Type=2";
                    }
                    context.startActivity(new Intent(context, ShowDiscuzActivity.class)
                            .putExtra(Const.SHOW_HTML_MAIN_TOP, a));
                }
                break;
            case 2://文章
                if (!TextUtils.isEmpty(a)) {
                    context.startActivity(new Intent(context, ArticleDetailActivity.class)
                            .putExtra(Const.INTENT_ARTICLE_ID, a));
                }
                break;
            case 3://帖子
                if (!TextUtils.isEmpty(a)) {
                    context.startActivity(new Intent(context, ForumDetailActivity.class)
                            .putExtra(Const.FORUM_ID, a));
                }
                break;
            case 11://文章列表
                if (!TextUtils.isEmpty(a)) {
                    context.startActivity(new Intent(context, ArticleTypeActivity.class)
                            .putExtra(Const.INTENT_ARTICLE_TYPE_ENTITY, entity));
                }
                break;
            case 5://圈子
                if (!TextUtils.isEmpty(a)) {
                    context.startActivity(new Intent(context, NewCircleActivity.class)
                            .putExtra(Contants.CircleID, a));
                }
                break;
            case 10://APP内页
                if (!TextUtils.isEmpty(a)) {
                    int jump = Integer.valueOf(a);
                    switch (jump) {
                        case 0://服务中心
                            context.startActivity(new Intent(context, ServiceCenterActivity.class));
                            break;
                        case 1://精准知识
                            context.startActivity(new Intent(context, MatchArticleActivity.class));
                            break;
                        case 2://发布帖子
                            context.startActivity(new Intent(context, ReleaseForumActivity.class));
                            break;
                        case 3://圈子主页
                            try {
                                if (isHome) {
                                    ((MainActivity) activity).toBottomPage(2);
                                } else {
                                    ((AllMenuActivity) activity).toBottomPage(2);
                                }
                            } catch (Exception e) {
                                ExceptionUtils.ExceptionSend(e, "转跳时类型转换失败 3");
                            }
                            break;
                        case 4://圈圈助手
                            try {
                                if (isHome) {
                                    ((MainActivity) activity).toBottomPage(4);
                                } else {
                                    ((AllMenuActivity) activity).toBottomPage(4);
                                }
                            } catch (Exception e) {
                                ExceptionUtils.ExceptionSend(e, "转跳时类型转换失败 4");
                            }
                            break;
                        case 9://查并发症
                            context.startActivity(new Intent(context, SearchComplicationActivity.class)
                                    .putExtra(Const.SEARCH_POLICY_TYPE, Const.TYPE_COMPLICATION));
                            break;
                        case 10://查副作用
                            context.startActivity(new Intent(context, SearchComplicationActivity.class)
                                    .putExtra(Const.SEARCH_POLICY_TYPE, Const.TYPE_EFFECT));
                            break;
                        case 11://差指标
                            context.startActivity(new Intent(context, SearchQuotaActivity.class));
                            break;
                        case 5:
                        case 6:
                        case 12://病历、智能问诊、、、
                            toIfJump(jump, context, callback, activity);
                            break;
                        case 7://查症状
                            context.startActivity(new Intent(context, HomeSymptomActivity.class));
                            break;
                        case 8:
                            context.startActivity(new Intent(context, SearchDrugActivity.class));
                            break;
                        case 13://直接跳转到映客直播
                            toInkeLive(entity.getSign_b(), context);
                            break;
                        case 14://用户自己的个人中心
                            context.startActivity(new Intent(context, InfoCenterActivity.class)
                                    .putExtra(InfoCenterID, UserinfoData.getInfoID(context)));
                            break;
                        case 15://粉丝列表
                            context.startActivity(new Intent(context, CareListActivity.class)
                                    .putExtra(InfoCenterID, UserinfoData.getInfoID(context))
                                    .putExtra(Const.CareListType, 1));
                            break;
                        case 16://我关注的粉丝
                            context.startActivity(new Intent(context, CareListActivity.class)
                                    .putExtra(Const.InfoCenterID, UserinfoData.getInfoID(context))
                                    .putExtra(Const.CareListType, 2));
                            break;
                        case 17://我关注的圈子
                            context.startActivity(new Intent(context, MyFosCircleActivity.class));
                            break;
                        case 18://全部圈子
                            context.startActivity(new Intent(context, MoreCircleNewActivity.class));
                            break;
                        case 19://相似患者
                            context.startActivity(new Intent(context, SimilarActivity.class));
                            break;
                        case 20://用户列表
                            context.startActivity(new Intent(context, FocusFriendActivity.class)
                                    .putExtra(Const.TypeID, entity.getSign_b()).putExtra("title", entity.getName()));
                            break;
                        case 21://帖子列表
                            context.startActivity(new Intent(context, FourmListActivity.class)
                                    .putExtra(Const.TypeID, entity.getSign_b()).putExtra("title", entity.getName()));
                            break;
                        case 25://有赞页面
                            context.startActivity(new Intent(context, YouzanActivity.class).putExtra("uzUrl", entity.getSign_b()));
                            break;
                        default:
                            Toast.makeText(context.getApplicationContext(), "当前版本暂不支持该功能\n" +
                                    "请更新到抗癌圈的最新版本~", Toast.LENGTH_SHORT).show();
                            break;

                    }
                }
                break;
            default:
                Toast.makeText(context.getApplicationContext(), "当前版本暂不支持该功能\n" +
                        "请更新到抗癌圈的最新版本~", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private static void toIfJump(final int jump, final Context context, final FragmentCallBack callback
            , final FragmentActivity activity) {
        String isHas = UserinfoData.getIsHaveStep(context);
        int has = Integer.valueOf(isHas);
        if (has == 0) {
            ZYDialog.Builder dialog = new ZYDialog.Builder(context);
            dialog.setTitle("缺少治疗信息");
            dialog.setMessage("如需使用该功能，请完善最新治疗方案。若不知道具体信息，可通过主治医生或陪床家人获取。");
            dialog.setPositiveButton("去完善", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        startIndividuationTreatment(activity);
                        // createInfoStepFragment(activity, callback);
                        dialog.dismiss();
                    } catch (Exception e) {
                        ExceptionUtils.ExceptionSend(e, "checkNetworkState");
                    }
                }
            });

            dialog.setNegativeButton("暂不使用", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.create().show();
        } else {
            switch (jump) {
                case 5://病历
                    context.startActivity(new Intent(context, MedicalRecordActivity.class));
                    break;
                case 6://智能问诊
                    context.startActivity(new Intent(context, PerfectDataActivity.class));
                    break;

                case 12://记录病情
                    context.startActivity(new Intent(context, RecordClassifyActivity.class));
                    break;
            }
        }

    }

    //映客直播确认状态接口
    private static void toInkeLive(final String vID, final Context context) {
        Factory.postPhp(new HttpResponseInterface() {
            @Override
            public Map getParamInfo(int tag) {
                Map<String, String> map = new HashMap<>();
                if (tag == Const.PApi_inkevideo_liveidid) {
                    map.put(Contants.InfoID, UserinfoData.getInfoID(context));
                    map.put("vid", vID + "");
                }
                return map;
            }

            @Override
            public byte[] getPostParams(int flag) {
                return new byte[0];
            }

            @Override
            public void toActivity(Object response, int flag) {
                if (flag == Const.PApi_inkevideo_liveidid) {
                    LiveBaseBean bean = (LiveBaseBean) response;
                    if (Const.RESULT.equals(bean.IResult())) {
                        LiveItemEntity entity = bean.getData();
                        if (!TextUtils.isEmpty(entity.getType())) {
                            int type = Integer.valueOf(entity.getType());

                            switch (type) {
                                case 0:
                                    if (!TextUtils.isEmpty(entity.getLiveid())) {
                                        InKeSdkPluginAPI.start(context, ZYApplication.YK_UserInfo, false, entity.getLiveid());
                                    }
//                                    else {
//                                        //showToast("直播id为空");
//                                    }
                                    break;
                                case 1:
                                    if (!TextUtils.isEmpty(entity.getSign())) {
                                        context.startActivity(new Intent(context, ShowDiscuzActivity.class)
                                                .putExtra(Const.SHOW_HTML_MAIN_TOP, entity.getSign()));
                                    }
                                    break;
                                case 2:
                                    if (!TextUtils.isEmpty(entity.getSign())) {
                                        context.startActivity(new Intent(context, ArticleDetailActivity.class)
                                                .putExtra(Const.INTENT_ARTICLE_ID, entity.getSign()));
                                    }
                                    break;
                                case 3:
                                    if (!TextUtils.isEmpty(entity.getSign())) {
                                        context.startActivity(new Intent(context, ForumDetailActivity.class)
                                                .putExtra(Const.FORUM_ID, entity.getSign())
                                                .putExtra(Const.AUTHORID, entity.getAnchorid()));
                                    }
                                    break;


                            }
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
        }, Const.PApi_inkevideo_liveidid);

    }

    //选择癌种
    public static void showChooseCancerTypeFragment(FragmentActivity activity, FragmentCallBack callback) {
        ChooseCancerFragment fragment = ChooseCancerFragment.getInstance(callback);
        fragment.show(activity.getSupportFragmentManager(), ChooseCancerFragment.TAG);

    }

    //开启个性化治疗
    public static void startIndividuationTreatment(Activity activity) {
        activity.startActivityForResult(new Intent(activity, SelectCancerActivity.class), 9);

    }
//    private static void createInfoStepFragment(FragmentActivity activity, FragmentCallBack callback) {
//        InfoStepFragment stepFragment = InfoStepFragment.getInstance(callback, 0);
//        stepFragment.show(activity.getSupportFragmentManager(), InfoStepFragment.type);
//    }

    /**
     * 获取随机数
     *
     * @return
     */
    public static String getRandomMath() {
        String temp = (int) (Math.random() * 89999 + 10000) + "";
        return temp;
    }

    /**
     * 获取记录的分类标题
     *
     * @param type
     * @return
     */
    public static String getRecordClassifyTitle(int type) {
        String title = "";
        switch (type) {
            case Const.RECORD_TYPE_1:
                title = "记录血常规";
                break;
            case Const.RECORD_TYPE_2:
                title = "记录粪便常规";
                break;
            case Const.RECORD_TYPE_3:
                title = "记录尿常规";
                break;
            case Const.RECORD_TYPE_4:
                title = "记录肝功能";
                break;
            case Const.RECORD_TYPE_5:
                title = "记录肾功能";
                break;
            case Const.RECORD_TYPE_6:
                title = "记录其他指标";
                break;
            case Const.RECORD_TYPE_7:
                title = "记录诊断报告";
                break;
            case Const.RECORD_TYPE_8:
                title = "记录病理、检验报告";
                break;
            case Const.RECORD_TYPE_9:
                title = "记录出院报告";
                break;
            case Const.RECORD_TYPE_11:
                title = "新增基因记录";
                break;
            case Const.RECORD_TYPE_12:
                title = "新增转移记录";
                break;
            case Const.RECORD_TYPE_13:
                title = "记录症状";
                break;
            case Const.RECORD_TYPE_14:
                title = "记录肿瘤大小";
                break;
            case Const.RECORD_TYPE_15:
                title = "记录指标";
                break;
        }
        return title;
    }

    /**
     * 获取记录的分类标题
     *
     * @param type
     * @return
     */
    public static String getEditRecordClassifyTitle(int type) {
        String title = "";
        switch (type) {
            case Const.RECORD_TYPE_1:
                title = "编辑血常规";
                break;
            case Const.RECORD_TYPE_2:
                title = "编辑粪便常规";
                break;
            case Const.RECORD_TYPE_3:
                title = "编辑尿常规";
                break;
            case Const.RECORD_TYPE_4:
                title = "编辑肝功能";
                break;
            case Const.RECORD_TYPE_5:
                title = "编辑肾功能";
                break;
            case Const.RECORD_TYPE_6:
                title = "编辑其他指标";
                break;
            case Const.RECORD_TYPE_7:
                title = "编辑诊断报告";
                break;
            case Const.RECORD_TYPE_8:
                title = "编辑病理、检验报告";
                break;
            case Const.RECORD_TYPE_9:
                title = "编辑出院报告";
                break;
            case Const.RECORD_TYPE_11:
                title = "编辑基因情况";
                break;
            case Const.RECORD_TYPE_12:
                title = "编辑转移情况";
                break;
            case Const.RECORD_TYPE_13:
                title = "编辑症状";
                break;
            case Const.RECORD_TYPE_14:
                title = "编辑肿瘤大小";
                break;
            case Const.RECORD_TYPE_15:
                title = "编辑肿瘤指标";
                break;
        }
        return title;
    }

    /**
     * 根据癌种获得默认选中的指标类型
     *
     * @param id 癌种ID
     * @return
     */
    public static ArrayList<String> getDefaultQuotaTypeForCancerID(String id) {
        ArrayList<String> list = new ArrayList<>();
        int ID = Integer.valueOf(id);
        switch (ID) {
            case 30:
            case 31:
            case 32://肺癌
                list.add("1");
                list.add("3");
                list.add("4");
                list.add("9");
                list.add("10");
                if (ID != 32) {
                    list.add("15");
                }
                list.add("17");
                break;
            case 34://胃癌
                list.add("1");
                list.add("5");
                list.add("6");
                list.add("7");
                list.add("8");
                break;
            case 99://甲状腺癌
                list.add("3");
                list.add("18");
                list.add("19");
                break;
            case 49:
            case 50://食管
                list.add("1");
                list.add("4");
                list.add("5");
                list.add("15");
                break;
            case 33://肝癌
                list.add("1");
                list.add("2");
                list.add("5");
                break;
            case 96://膀胱癌
                list.add("1");
                list.add("17");
                break;
            case 108://前列腺癌
                list.add("11");
                break;
            case 41:
            case 42://乳腺癌
                list.add("1");
                list.add("2");
                list.add("10");
                break;
            case 98://胰腺癌
                list.add("1");
                list.add("5");
                break;
            case 38:
            case 39://肠癌
                list.add("1");
                list.add("5");
                break;
            case 81://卵巢
                list.add("2");
                list.add("7");
                list.add("9");
                list.add("20");
                break;
            case 75://
                list.add("1");
                list.add("15");
                break;
        }
        return list;
    }

    private static String[] EnNames = new String[]{
            "CEA", "AFP", "NSE", "Cyfra21-1", "CA19-9", "CA50", "CA72-4", "CA242", "CA125", "CA15-3", "PSA", "FPSA"
            , "PAP", "β2-MG", "SCCA", "SF", "TPA", "TG", "hCT", "HCG"
    };
    private static String[] CnNames = new String[]{
            "癌胚抗原", "甲胎蛋白", "神经原特异性烯醇化酶", "细胞角蛋白19", "糖类抗原19-9", "糖类抗原50", "糖类抗原72-4"
            , "糖类抗原242", "糖类抗原125", "糖类抗原15-3", "前列腺特异抗原", "游离前列腺特异性抗原"
            , "前列腺酸性磷酸酶", "β2微球蛋白", "鳞状细胞癌抗原", "铁蛋白", "组织多肽抗原", "甲状腺球蛋白", "红细胞比容", "人绒毛膜促性腺激素"
    };

    public static String getNameENforTypeID(int TypeID) {
        if (TypeID <= 0 || TypeID > EnNames.length) return "";
        return EnNames[TypeID - 1];
    }

    public static String getNameCNforTypeID(int TypeID) {
        if (TypeID <= 0 || TypeID > CnNames.length) return "";
        return CnNames[TypeID - 1];
    }

    public static String[] OtherNames = new String[]{"心脏", "肝脏", "肾脏", "脑", "其他"};

    public static String getNameForOtherStrickenID(int id) {
        if (id <= 0 || id > OtherNames.length) return "";
        return OtherNames[id - 1];
    }

    public static String getOtherStrickenRemark(String str) {
        if (TextUtils.isEmpty(str)) return "";
        String[] args = str.split(",");
        String temp = "";
        for (int i = 0; i < args.length; i++) {
            if (i == 0) {
                temp = getNameForOtherStrickenID(Integer.valueOf(args[0]));
            } else {
                temp = temp + "," + getNameForOtherStrickenID(Integer.valueOf(args[i]));
            }
        }
        return temp;
    }

    public static String getOtherStrickenString(PatientDataEntity mEntity) {
        String other = "";
        String temp = mEntity.getOtherDisease();
        String remark = mEntity.getOtherDiseaseRemark();
        if (!OtherUtils.isEmpty(temp)) {
            other = UiUtils.getOtherStrickenRemark(temp);
        }
        if (!TextUtils.isEmpty(remark)) {
            if (TextUtils.isEmpty(other)) {
                other = remark;
            } else {
                other = other + "," + remark;
            }
        }
        return other;
    }

    public static String getUnitForType(int TypeID) {
        String result = "";
        switch (TypeID) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 11:
            case 13:
            case 15:
            case 16:
                result = "ng/ml";
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                result = "u/ml";
                break;
            case 12:
                result = "-";
                break;
            case 14:
                result = "mg/l";
                break;
            case 17:
                result = "u/l";
                break;
            case 18:
                result = "μg/l";
                break;
            case 19:
                result = "%";
                break;
            case 20:
                result = "nmol/l";
                break;
        }
        return result;
    }

    private static int[] colors = new int[]{R.color.color_record_chart_1,
            R.color.color_record_chart_2,
            R.color.color_record_chart_3,
            R.color.color_record_chart_4,
            R.color.color_record_chart_5,
            R.color.color_record_chart_6};

    public static int getColorForTypeID(int TypeID) {
        while (TypeID > 5) {
            TypeID = TypeID % 6;
        }
        return colors[TypeID];
    }

    private static int[] colors_bg = new int[]{R.drawable.bg_chart_1,
            R.drawable.bg_chart_2,
            R.drawable.bg_chart_3,
            R.drawable.bg_chart_4,
            R.drawable.bg_chart_5,
            R.drawable.bg_chart_6};

    public static int getBGColorForTypeID(int TypeID) {
        while (TypeID > 5) {
            TypeID = TypeID % 6;
        }
        return colors_bg[TypeID];
    }

    public static int getTypeIDForTypeID(int TypeID) {
        int temp = TypeID;
        while (temp > 5) {
            temp = temp % 6;
        }
        return temp;
    }

    private static int[] show_colors_bg = new int[]{R.drawable.chart_selector_change_bg_1,
            R.drawable.chart_selector_change_bg_2,
            R.drawable.chart_selector_change_bg_3,
            R.drawable.chart_selector_change_bg_4,
            R.drawable.chart_selector_change_bg_5,
            R.drawable.chart_selector_change_bg_6};

    public static int getSelectBgForPosition(int position) {
        int temp = position;
        while (temp > 5) {
            temp = temp % 6;
        }
        return show_colors_bg[temp];
    }

    private static int[] show_imgs = new int[]{
            R.mipmap.now_step_select_circle_small,
            R.mipmap.chart_select_circle_2,
            R.mipmap.chart_select_circle_3,
            R.mipmap.chart_select_circle_4,
            R.mipmap.chart_select_circle_5,
            R.mipmap.chart_select_circle_6
    };

    public static int getShowRightForPosition(int position) {
        int temp = position;
        while (temp > 5) {
            temp = temp % 6;
        }
        return show_imgs[temp];
    }

    public static String getRecordTitleNameForTypeID(int TypeID) {
        String title = "";
        switch (TypeID) {
            case Const.RECORD_TYPE_1:
                title = "记录血常规";
                break;
            case Const.RECORD_TYPE_2:
                title = "记录粪便常规";
                break;
            case Const.RECORD_TYPE_3:
                title = "记录尿常规";
                break;
            case Const.RECORD_TYPE_4:
                title = "记录肝功能";
                break;
            case Const.RECORD_TYPE_5:
                title = "记录肾功能";
                break;
            case Const.RECORD_TYPE_6:
                title = "记录其他功能";
                break;
            case Const.RECORD_TYPE_7:
                title = "记录诊断报告";
                break;
            case Const.RECORD_TYPE_8:
                title = "记录病理检验报告";
                break;
            case Const.RECORD_TYPE_9:
                title = "记录出院报告";
                break;
            case Const.RECORD_TYPE_11:
                title = "记录基因突变";
                break;
            case Const.RECORD_TYPE_12:
                title = "记录转移";
                break;
            case Const.RECORD_TYPE_13:
                title = "记录症状";
                break;
            case Const.RECORD_TYPE_14:
                title = "记录肿瘤大小";
                break;
            case Const.RECORD_TYPE_15:
                title = "记录肿瘤指标";
                break;
        }
        return title;
    }

    public static int getRecordImageResForTypeID(int TypeID) {
        int res = R.mipmap.record_pic_type_5;
        switch (TypeID) {
            case Const.RECORD_TYPE_7:
                res = R.mipmap.record_pic_type_3;
                break;
            case Const.RECORD_TYPE_8:
                res = R.mipmap.record_pic_type_4;
                break;
            case Const.RECORD_TYPE_9:
                res = R.mipmap.record_pic_type_2;
                break;
            case Const.RECORD_TYPE_11:
                res = R.mipmap.record_pic_type_11;
                break;
            case Const.RECORD_TYPE_12:
                res = R.mipmap.record_pic_type_12;
                break;
            case Const.RECORD_TYPE_13:
                res = R.mipmap.record_pic_type_13;
                break;
            case Const.RECORD_TYPE_14:
                res = R.mipmap.record_pic_type_15;
                break;
            case Const.RECORD_TYPE_15:
                res = R.mipmap.record_pic_type_14;
                break;
        }
        return res;
    }

    public static int getCancerTypeImage(String str) {
        int res = R.mipmap.decision_other;
        if (!OtherUtils.isEmpty(str)) {
            String parent = getCancerParentID(str);
            int type = Integer.valueOf(parent);
            switch (type) {
                case 29:
                    res = R.mipmap.decision_29;
                    break;
                case 33:
                    res = R.mipmap.decision_33;
                    break;
                case 34:
                    res = R.mipmap.decision_34;
                    break;
                case 36:
                    res = R.mipmap.decision_36;
                    break;
                case 37:
                    res = R.mipmap.decision_37;
                    break;
                case 48:
                    res = R.mipmap.decision_48;
                    break;
            }
        }
        return res;
    }

    //跳转到个人中心
    public static void jumpToInfoCenter(Context context, String infoId) {
        context.startActivity(new Intent(context, InfoCenterActivity.class)
                .putExtra(Const.InfoCenterID, infoId));
    }

    //跳转到文章详情
    public static void jumpToArticleDetail(Context context, String articleId) {
        context.startActivity(new Intent(context, ArticleDetailActivity.class).
                putExtra(Const.INTENT_ARTICLE_ID, articleId));
    }

    //跳转到帖子详情
    public static void jumpToForumDetail(Context context, String forumId, String authorId) {
        context.startActivity(new Intent(context, ForumDetailActivity.class)
                .putExtra(Const.FORUM_ID, forumId)
                .putExtra(Const.AUTHORID, authorId));
    }
}

package com.zeyuan.kyq.service;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.meelive.ingkee.sdk.plugin.InKeSdkPluginAPI;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.zeyuan.kyq.Entity.LiveBaseBean;
import com.zeyuan.kyq.Entity.LiveItemEntity;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.ArticleDetailActivity;
import com.zeyuan.kyq.view.ForumDetailActivity;
import com.zeyuan.kyq.view.HeadLineActivity;
import com.zeyuan.kyq.view.HomeSymptomActivity;
import com.zeyuan.kyq.view.MainActivity;
import com.zeyuan.kyq.view.MedicalRecordActivity;
import com.zeyuan.kyq.view.MessageDetailActivity;
import com.zeyuan.kyq.view.MoreCircleNewActivity;
import com.zeyuan.kyq.view.MyReplyActivity;
import com.zeyuan.kyq.view.NewCircleActivity;
import com.zeyuan.kyq.view.NewsCenterActivity;
import com.zeyuan.kyq.view.PatientDetailActivity;
import com.zeyuan.kyq.view.PerfectDataActivity;
import com.zeyuan.kyq.view.ReleaseForumActivity;
import com.zeyuan.kyq.view.ShowDiscuzActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/29.
 * <p>
 * jump:
 * <p>
 * 1、普通链接（需要带用户信息）
 * <p>
 * "extra":{"url":"www.xxx.com","jump":"1"}
 * 2、文章
 * "extra":{"jump":"2","appparam":"id=545"}
 * 3、帖子
 * <p>
 * "extra":{"jump":"3","appparam":"id=545"}
 * 4、文章列表
 * <p>
 * "extra":{"jump":"4","appparam":"type=1"}
 * 5、圈子
 * <p>
 * "extra":{"jump":"5","appparam":"id=545"}
 * 6、我的评论
 * <p>
 * "extra":{"jump":"6"}
 * 7、我的病历
 * <p>
 * "extra":{"jump":"7"}
 * 8、消息中心
 * <p>
 * "extra":{"jump":"8"}
 * 9、患者详情
 * <p>
 * "extra":{"jump":"9"}
 * 10、查症状
 * <p>
 * "extra":{"jump":"10"}
 * 11、智能问诊
 * <p>
 * "extra":{"jump":"11"}
 * 12、发帖
 * <p>
 * "extra":
 * {"jump":"12","appparam":"circleid=54,21,65"}
 * 13、关注更多圈子
 * <p>
 * "extra":{"jump":"13"}
 * 14、圈子主页
 * "extra":{"jump":"14"}
 *
 * @author wwei
 */
public class ZYNotificationClickHandler extends UmengNotificationClickHandler {

    private static final String APP_PARAM = "appparam";
    private static final String BREAK = "=";
    private static final String INDEX = "index";

    @Override
    public void dealWithCustomAction(Context context, UMessage msg) {
        Map<String, String> map = getExtraMap(msg);
        String InfoID = UserinfoData.getInfoID(context);
        String IsHaveStep = UserinfoData.getIsHaveStep(context);
        boolean flag = false;
        if ("0".equals(IsHaveStep)) {
            flag = true;
        }
        if (map != null && map.get("jump") != null && !TextUtils.isEmpty(InfoID)) {
            Intent intent = new Intent();
            int jump = Integer.valueOf(map.get("jump"));
            switch (jump) {
                case 0:
                    /*int num = UserinfoData.getUserPuskMsg(context);
                    UserinfoData.saveUserPushMsg(context,num+1);*/
                    break;
                case 1:
                    intent.setClassName(context, ShowDiscuzActivity.class.getName());
                    try {
                        String url = map.get("url");
                        if (url.contains("?")) {
                            url += "&kaq=" + getRandomMath() + UserinfoData.getInfoID(context) + "&lt=2&Type=2";
                        } else {
                            url += "?kaq=" + getRandomMath() + UserinfoData.getInfoID(context) + "&lt=2&Type=2";
                        }
                        intent.putExtra(Const.SHOW_HTML_MAIN_TOP, url);
                    } catch (Exception e) {
                        intent.putExtra(Const.SHOW_HTML_MAIN_TOP, "http://bbs.kaqcn.com");
                    }
                    break;
                case 2:
                    intent.setClassName(context, ArticleDetailActivity.class.getName());
                    try {
                        String temp = map.get(APP_PARAM);
                        String[] args = temp.split(BREAK);
                        intent.putExtra(Const.INTENT_ARTICLE_ID, args[1]);
                    } catch (Exception e) {
                        intent.putExtra(Const.INTENT_ARTICLE_ID, "3067");
                    }
                    break;
                case 3:
                    intent.setClassName(context, ForumDetailActivity.class.getName());
                    try {
                        String temp = map.get(APP_PARAM);
                        String[] args = temp.split(BREAK);
                        intent.putExtra(Const.FORUM_ID, args[1]);
                    } catch (Exception e) {
                        intent.putExtra(Const.FORUM_ID, "1047");
                    }
                    break;
                case 4:

                    break;
                case 5:
                    intent.setClassName(context, NewCircleActivity.class.getName());
                    try {
                        String temp = map.get(APP_PARAM);
                        String[] args = temp.split(BREAK);
                        intent.putExtra(Contants.CircleID, args[1]);
                    } catch (Exception e) {
                        intent.putExtra(Contants.CircleID, "30");
                    }
                    break;
                case 12:
                    intent.setClassName(context, ReleaseForumActivity.class.getName());
                    try {
                        String temp = map.get(APP_PARAM);
                        if (!TextUtils.isEmpty(temp)) {
                            String[] args = temp.split(BREAK);
                            List<String> list = new ArrayList<>();
                            if (args[1].contains(",")) {
                                String[] args1 = args[1].split(",");
                                for (String str : args1) {
                                    list.add(str);
                                }
                            } else {
                                list.add(args[1]);
                            }
                            intent.putExtra(Const.DEFAULT_CIRCLE, (Serializable) list);
                        }
                    } catch (Exception e) {

                    }
                    break;
                case 6:
                    intent.setClassName(context, MyReplyActivity.class.getName());
                    break;
                case 7:
                    intent.setClassName(context, MedicalRecordActivity.class.getName());
                    break;
                case 8:
                    intent.setClassName(context, NewsCenterActivity.class.getName());
                    break;
                case 9:
                    intent.setClassName(context, PatientDetailActivity.class.getName());
                    break;
                case 10:
                    intent.setClassName(context, HomeSymptomActivity.class.getName());
                    break;
                case 11:
                    intent.setClassName(context, PerfectDataActivity.class.getName());
                    break;
                case 13:
                    intent.setClassName(context, MoreCircleNewActivity.class.getName());
                    break;
                case 14:
                    intent.setClassName(context, MainActivity.class.getName());
                    break;
                case 15:
                    intent.setClassName(context, HeadLineActivity.class.getName());
                    try {
                        String temp = map.get(APP_PARAM);
                        String catid = map.get("catid");
                        if (!TextUtils.isEmpty(catid) && !TextUtils.isEmpty(temp)) {
                            String[] args = temp.split(BREAK);
                            intent.putExtra(Const.HEAD_LIST_TAG_URL, catid).putExtra(Const.HEAD_LIST_INFO_TEXT,
                                    args[1]);
                        }
                    } catch (Exception e) {

                    }
                    break;
                case 16://跳转到直播间
                    String temp = map.get(APP_PARAM);
                    if (!TextUtils.isEmpty(temp)) {
                        String[] args = temp.split(BREAK);
                        toInkeLive(args[1], context);
                    }
                    break;
            }
            if (jump != 0) {
                if (flag && (jump == 7 || jump == 10 || jump == 11)) {

                } else {
                    intent.putExtra(Const.INTENT_FROM, Const.FM);
                    intent.addFlags(268435456);
                    ((MessageDetailActivity) context).getCurrentContext().startActivity(intent);
                }
            }
        }
    }

    @Override
    public void openActivity(Context var1, UMessage var2) {
        if (var2.activity != null && !TextUtils.isEmpty(var2.activity.trim())) {
            Intent intent = new Intent();
            Map<String, String> map = getExtraMap(var2);
            int jump = Integer.valueOf(map.get("jump"));
            switch (jump) {
                case 1:
                    try {
                        String url = map.get("url");
                        if (url.contains("?")) {
                            url += "&kaq=" + getRandomMath() + UserinfoData.getInfoID(var1);
                        } else {
                            url += "?kaq=" + getRandomMath() + UserinfoData.getInfoID(var1);
                        }
                        intent.putExtra(Const.SHOW_HTML_MAIN_TOP, url);
                    } catch (Exception e) {
                        intent.putExtra(Const.SHOW_HTML_MAIN_TOP, "http://bbs.kaqcn.com");
                    }
                    break;
                case 2:
                    try {
                        String temp = map.get(APP_PARAM);
                        String[] args = temp.split(BREAK);
                        intent.putExtra(Const.INTENT_SHOW_DISCUZ_ID, args[1]);
                    } catch (Exception e) {
                        intent.putExtra(Const.INTENT_SHOW_DISCUZ_ID, "3067");
                    }
                    break;
                case 3:
                    try {
                        String temp = map.get(APP_PARAM);
                        String[] args = temp.split(BREAK);
                        intent.putExtra(Const.FORUM_ID, args[1]);
                    } catch (Exception e) {
                        intent.putExtra(Const.FORUM_ID, "1047");
                    }
                    break;
                case 4:
                    try {
                        String temp = map.get(APP_PARAM);
                        String[] args = temp.split(BREAK);
                        int type = Integer.valueOf(args[1]);
                        intent.putExtra(INDEX, type - 1);
                    } catch (Exception e) {
                        intent.putExtra(INDEX, 0);
                    }
                    break;
                case 5:
                    try {
                        String temp = map.get(APP_PARAM);
                        String[] args = temp.split(BREAK);
                        intent.putExtra(Contants.CircleID, args[1]);
                    } catch (Exception e) {
                        intent.putExtra(Contants.CircleID, "30");
                    }
                    break;
                case 12:
                    try {
                        String temp = map.get(APP_PARAM);
                        if (!TextUtils.isEmpty(temp)) {
                            String[] args = temp.split(BREAK);
                            List<String> list = new ArrayList<>();
                            if (args[1].contains(",")) {
                                String[] args1 = args[1].split(",");
                                for (String str : args1) {
                                    list.add(str);
                                }
                            } else {
                                list.add(args[1]);
                            }
                            intent.putExtra(Const.DEFAULT_CIRCLE, (Serializable) list);
                        }
                    } catch (Exception e) {

                    }
                    break;

            }

            intent.setClassName(var1, var2.activity);
            intent.putExtra(Const.INTENT_FROM, Const.FM);
            intent.addFlags(268435456);
            ((MessageDetailActivity) var1).getCurrentContext().startActivity(intent);
        }
    }

    private Map<String, String> getExtraMap(UMessage var2) {
        if (var2 != null && var2.extra != null) {
            Iterator iterator = var2.extra.entrySet().iterator();
            Map<String, String> map = new HashMap<>();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                if (key != null) {
                    map.put(key, (String) entry.getValue());
                }
            }
            return map;
        }
        return null;
    }

    private String getRandomMath() {
        String temp = (int) (Math.random() * 89999 + 10000) + "";
        return temp;
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

}

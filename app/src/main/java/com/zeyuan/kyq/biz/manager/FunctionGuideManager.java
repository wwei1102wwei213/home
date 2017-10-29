package com.zeyuan.kyq.biz.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.SharePrefUtil;
import com.zeyuan.kyq.view.FocusFriendGuideActivity;
import com.zeyuan.kyq.view.FunctionGuideActivity;

/**
 * Created by Administrator on 2017/8/15.
 * 功能引导管理器
 */

public class FunctionGuideManager {
    private static FunctionGuideManager instance;

    private FunctionGuideManager() {
    }

    public static synchronized FunctionGuideManager getInstance() {
        if (instance == null)
            instance = new FunctionGuideManager();
        return instance;
    }

    //发帖提示
    public void showReleaseForumGuide(Activity activity, int[] xy, int width, int height) {
        if (!getIsShowed(activity.getApplicationContext(), 1)) {
            showGuideWithViewLocation(activity, 1, xy, width, height);
            setIsShowed(activity.getApplicationContext(), 1);
        }

    }

    //病历分享
    public void showMedicalRecordShareGuide(Activity activity) {
        //先判断病历功能提示有没有展示过，在判断病历分享提示有没有展示过
        if (getIsShowed(activity.getApplicationContext(), 2) && !getIsShowed(activity.getApplicationContext(), 201)) {
            showGuideWithoutViewLocation(activity, 201);
            setIsShowed(activity.getApplicationContext(), 201);
        }
    }

    //病历功能提示
    public void showMedicalRecordGuide(Activity activity) {
        if (!getIsShowed(activity.getApplicationContext(), 2)) {
            showGuideWithoutViewLocation(activity, 2);
            setIsShowed(activity.getApplicationContext(), 2);
        }

    }


    //文章点赞提示
    public void showArticleLikeGuide(Activity activity, int[] xy, int width, int height) {
        if (!getIsShowed(activity.getApplicationContext(), 3)) {
            showGuideWithViewLocation(activity, 3, xy, width, height);
            setIsShowed(activity.getApplicationContext(), 3);
        }

    }

    //精准内容提示
    public void showAccurateGuide(Activity activity, int[] xy, int width, int height) {
        if (!getIsShowed(activity.getApplicationContext(), 4)) {
            showGuideWithViewLocation(activity, 4, xy, width, height);
            setIsShowed(activity.getApplicationContext(), 4);
        }

    }

    //圈圈助手的病情提示
    public void showOverallSituationGuide(Activity activity, int[] xy, int width, int height) {
        if (!getIsShowed(activity.getApplicationContext(), 5)) {
            showGuideWithViewLocation(activity, 5, xy, width, height);
            setIsShowed(activity.getApplicationContext(), 5);
        }

    }

    //文章作者提示
    public void showArticleAuthorGuide(Activity activity, int[] xy, int width, int height) {
        if (getIsShowed(activity.getApplicationContext(), 3) && !getIsShowed(activity.getApplicationContext(), 6)) {
            showGuideWithViewLocation(activity, 6, xy, width, height);
            setIsShowed(activity.getApplicationContext(), 6);
        }

    }

    //成功关注好友提示
    public void showFocusFriendGuide(Activity activity) {
        if (!getIsShowed(activity.getApplicationContext(), 7)) {
            showGuideWithoutViewLocation(activity, 7);
            setIsShowed(activity.getApplicationContext(), 1);
        }

    }

    //成功关注大v
    public void showFocusV(Activity activity) {
        showGuideWithoutViewLocation(activity, 8);
    }

    //朋友圈
    public void showFriendForumGuide(Activity activity) {
        if (!getIsShowed(activity.getApplicationContext(), 9)) {
            showGuideWithoutViewLocation(activity, 9);
            setIsShowed(activity.getApplicationContext(), 9);
        }

    }

    //项目
    public void showProjectForumGuide(Activity activity) {
        if (!getIsShowed(activity.getApplicationContext(), 10)) {
            showGuideWithoutViewLocation(activity, 10);
            setIsShowed(activity.getApplicationContext(), 10);
        }

    }

    //同城圈
    public void showLocalForumGuide(Activity activity) {
        if (!getIsShowed(activity.getApplicationContext(), 11)) {
            showGuideWithoutViewLocation(activity, 11);
            setIsShowed(activity.getApplicationContext(), 11);
        }

    }

    //显示选择当前阶段的提示
    public void showSelectCurrentStepGuide(Activity activity) {
        if (!getIsShowed(activity.getApplicationContext(), 12)) {
            showGuideWithoutViewLocation(activity, 12);
            setIsShowed(activity.getApplicationContext(), 12);
        }
    }

    //关注相同癌种用户(专门页面)
    public void showFocusSameCancerUserGuide(Activity activity) {
        activity.startActivityForResult(new Intent(activity, FocusFriendGuideActivity.class).putExtra("userType", 1), 9);
        activity.overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_alpha_out);
    }

    //关注相同城市用户(专门页面)
    public void showFocusSameCityUserGuide(Activity activity) {
        activity.startActivity(new Intent(activity, FocusFriendGuideActivity.class).putExtra("userType", 2));
        activity.overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_alpha_out);
    }

    //提示框位置已固定或通过单张图片展示的功能引导
    private void showGuideWithoutViewLocation(Activity activity, int witch) {

        activity.startActivity(new Intent(activity, FunctionGuideActivity.class).putExtra("witch", witch));
        activity.overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_alpha_out);

    }

    //需要提供透明框位置的功能引导
    private void showGuideWithViewLocation(Activity activity, int witch, int[] xy, int width, int height) {
        if (!activity.isFinishing()) {
            activity.startActivity(new Intent(activity, FunctionGuideActivity.class).putExtra("witch", witch).putExtra("xy", xy).putExtra("width", width).putExtra("height", height));
            activity.overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_alpha_out);
        }


    }

    //获取某个提示框的显示记录
    private boolean getIsShowed(Context context, int witch) {
        return SharePrefUtil.getBoolean(context, witch + "", false);
    }

    //添加某个提示框的显示记录
    private void setIsShowed(Context context, int witch) {
        SharePrefUtil.saveBoolean(context, witch + "", true);
    }
}

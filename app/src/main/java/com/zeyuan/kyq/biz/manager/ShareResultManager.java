package com.zeyuan.kyq.biz.manager;

import android.app.Activity;
import android.util.Log;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.zeyuan.kyq.bean.ShareJFBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.IntegrationPopupWindow;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/11.
 * 分享结果管理
 */

public class ShareResultManager {
    private static ShareResultManager instance;
    private ShareListenerImpl shareListener;

    private ShareResultManager() {
    }

    public static synchronized ShareResultManager getInstance() {

        if (instance == null) {
            instance = new ShareResultManager();
        }

        return instance;
    }

    //新建一个qq分享的回调监听器（在不同activity里监听器不能重用）
    public void createShareListener(Activity activity) {
        this.shareListener = new ShareListenerImpl(activity);
    }

    public ShareListenerImpl getShareListener() {
        return shareListener;
    }

    public void WXShareSuccess() {
        if (shareListener != null)
            shareListener.onComplete(null);
    }

    //清除回调监听器，避免内存泄漏
    public void clearListener() {
        shareListener = null;
    }

    private class ShareListenerImpl implements IUiListener {
        private Activity activity;

        ShareListenerImpl(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onComplete(Object response) {
            Log.i(Const.TAG.ZY_TEST, "doComplete");
            if (activity != null)
                shareSuccess(activity);
        }

        @Override
        public void onError(UiError e) {
            Log.i(Const.TAG.ZY_TEST, "msg:" + e.errorMessage + "\ndetail:" + e.errorDetail);
            //  Toast.makeText(activity.getApplicationContext(), "分享错误！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Log.i(Const.TAG.ZY_TEST, "cancel");
            //  Toast.makeText(activity.getApplicationContext(), "分享已取消！", Toast.LENGTH_SHORT).show();
        }


        private void shareSuccess(final Activity activity) {
            Factory.postPhp(new HttpResponseInterface() {
                @Override
                public Map getParamInfo(int tag) {
                    Map<String, String> params = new HashMap<>();
                    params.put("typeID", "7");
                    params.put("InfoID", UserinfoData.getInfoID(activity.getApplicationContext()));
                    return params;
                }

                @Override
                public byte[] getPostParams(int flag) {
                    return new byte[0];
                }

                @Override
                public void toActivity(Object response, int flag) {
                    if (response != null) {
                        ShareJFBean shareJFBean = (ShareJFBean) response;
                        if (Contants.OK_DATA.equals(shareJFBean.getiResult())) {
                            IntegrationManager.getInstance().addIntegration(shareJFBean.getJF() + "");
                            String shareS;
                            if (shareJFBean.getJF() > 0) {
                                shareS = "分享成功+" + shareJFBean.getJF() + "积分";
                            } else {
                                shareS = "分享成功，已达每日积分上限！";
                            }
                            IntegrationPopupWindow integrationPopupWindow = new IntegrationPopupWindow(activity, shareS);
                            integrationPopupWindow.showPopupWindow(activity);
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
            }, Const.PApi_AppShareIntegral);
        }
    }

}

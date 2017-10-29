package com.zeyuan.kyq.presenter;

import android.util.Log;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.bean.LoginZYBean;
import com.zeyuan.kyq.bean.UserInfoQQBean;
import com.zeyuan.kyq.bean.UserInfoWXBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.GuideActivity;
import com.zeyuan.kyq.view.ViewDataListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/15.
 * 登录泽源公司服务器
 */
public final class LoginZYPresenter {
    /*private GetDataBiz biz;
    private ViewDataListener mainFragmentView;

    public LoginZYPresenter(ViewDataListener mainFragmentView ) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }

    *//**
     * 连接网络获取数据
     *//*
    public void getData(int loginType, Object obj) {
        mainFragmentView.showLoading(0);

        Map<String, String> map = new HashMap<>();
        {
            switch (loginType) {
                case Contants.AppCfg.LOGIN_TYPE__QQ: {
                    UserInfoQQBean infoQQBean = (UserInfoQQBean) obj;
                    map.put(Contants.OpenID, infoQQBean.getOpenid());
                    GlobalData.mUserHeadUrl_ = infoQQBean.getFigureurl_qq_2();
                    GlobalData.type = "1";
                    GlobalData.mUserNickname = infoQQBean.getNickname();
                    UserinfoData.saveLoginType((GuideActivity) mainFragmentView, GlobalData.type);
                    Log.i("ZYA", "登录：" + UserinfoData.getLoginType((GuideActivity) mainFragmentView));
                    UserinfoData.saveAvatarUrl((GuideActivity)mainFragmentView,GlobalData.mUserHeadUrl_);
                    Log.i("ZYA", "头像：" + UserinfoData.getAvatarUrl((GuideActivity) mainFragmentView));
                    UserinfoData.saveInfoname((GuideActivity) mainFragmentView, GlobalData.mUserNickname);
                    Log.i("ZYA", "名字：" + UserinfoData.getInfoname((GuideActivity) mainFragmentView));
                    map.put(Contants.LoginType, "" + Contants.AppCfg.LOGIN_TYPE__QQ);
                    break;
                }
                case Contants.AppCfg.LOGIN_TYPE__WX: {
                    UserInfoWXBean infoWxBean = (UserInfoWXBean) obj;
                    GlobalData.mUserHeadUrl_ = infoWxBean.getHeadimgurl();
                    GlobalData.mUserNickname = infoWxBean.getNickname();
                    GlobalData.type = "2";
                    UserinfoData.saveLoginType((GuideActivity)mainFragmentView,GlobalData.type);
                    Log.i("ZYA", "登录：" + UserinfoData.getLoginType((GuideActivity) mainFragmentView));
                    UserinfoData.saveAvatarUrl((GuideActivity) mainFragmentView, GlobalData.mUserHeadUrl_);
                    Log.i("ZYA", "头像：" + UserinfoData.getAvatarUrl((GuideActivity) mainFragmentView));
                    UserinfoData.saveInfoname((GuideActivity) mainFragmentView, GlobalData.mUserNickname);
                    Log.i("ZYA", "名字：" + UserinfoData.getInfoname((GuideActivity) mainFragmentView));
                    UserinfoData.saveUnionID((GuideActivity) mainFragmentView, GlobalData.UnionId);
                    Log.i("ZYA", "UnionID：" + UserinfoData.getUnionID((GuideActivity) mainFragmentView));
                    map.put(Contants.OpenID, infoWxBean.getOpenid());
                    map.put(Contants.LoginType, "" + Contants.AppCfg.LOGIN_TYPE__WX);
                    map.put(Contants.UnionID, GlobalData.UnionId);
                    break;
                }
            }
            map.put(Contants.AppType, Contants.AppCfg.APP_TYPE__ANDROID);
        }
        biz.getData(map, Contants.LOGIN, new OkHttpClientManager.ResultCallback<LoginZYBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(0);
            }

            @Override
            public void onResponse(LoginZYBean response) {
                mainFragmentView.hideLoading(0);
                mainFragmentView.toActivity(response, NetNumber.VIEWDATALISTENER_TOACTIVITY_ZEYUAN);
            }
        });
    }*/
}

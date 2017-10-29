package com.zeyuan.kyq.presenter;

import android.util.Log;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.UserInfoQQBean;
import com.zeyuan.kyq.bean.LoginQQBean;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/10/15.
 * 新增/更新症状
 */
public class LoginQQPresenter {

    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;
    private LoginQQBean mQQLoginBean;

    public LoginQQPresenter(ViewDataListener mainFragmentView, LoginQQBean qqLoginBean) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
        mQQLoginBean = qqLoginBean;
    }

    public void getData() {
        HashMap<String, String> params = new HashMap<>(3);
        {
            params.put("access_token", mQQLoginBean.getAccess_token());
            params.put("openid", mQQLoginBean.getOpenid());
            params.put("oauth_consumer_key", Contants.AppCfg.QQ__OAUTH_CONSUMER_KEY);
        }
        LogCustom.i(Const.TAG.ZY_HTTP, "access_token:" + mQQLoginBean.getAccess_token() + "\n" +
                "openid:" + mQQLoginBean.getOpenid()
                + "oauth_consumer_key:" + Contants.AppCfg.QQ__OAUTH_CONSUMER_KEY);
        biz.getData(params, Contants.QQ__GET_USERINFO, new OkHttpClientManager.ResultCallback<UserInfoQQBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(1);
            }

            @Override
            public void onResponse(UserInfoQQBean response) {
                mainFragmentView.hideLoading(0);
                Log.i("ZYH", "DATA:"+response.toString());
                response.setOpenid(mQQLoginBean.getOpenid());
                response.setAccess_token(mQQLoginBean.getAccess_token());
                mainFragmentView.toActivity(response, Const.GUIDE_GET_USER_INFO_QQ);
//                new LoginZYPresenter(mainFragmentView).getData(Contants.AppCfg.LOGIN_TYPE__QQ,response);
            }
        });
    }
}

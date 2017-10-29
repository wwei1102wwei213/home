package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.bean.LoginWXBean;
import com.zeyuan.kyq.bean.UserInfoWXBean;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.GuideActivity;
import com.zeyuan.kyq.view.ViewDataListener;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/10/15.
 *
 */
public class LoginWXPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public LoginWXPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }

    /**
     * 通过code获取access_token
     */
    public void getData() {
        mainFragmentView.showLoading(1);

        biz.getData(mainFragmentView.getParamInfo(1), Contants.WEIXIN_CODE, new OkHttpClientManager.ResultCallback<LoginWXBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(1);
            }

            @Override
            public void onResponse(LoginWXBean response) {
                try {
                    mainFragmentView.hideLoading(1);
                    if (response!=null&&response.getOpenid()!=null){
                        GlobalData.UnionId = response.getUnionid();
                        getUserInfo(response.getAccess_token(), response.getOpenid());
                    }else {
                        mainFragmentView.showError(1);
                    }
                }catch (Exception e){
                    ExceptionUtils.ExceptionToUM(e,(GuideActivity)mainFragmentView,"LoginWXBean");
                }
            }
        });


    }

    private void getUserInfo(String access_token, String openid) {
        HashMap<String, String> params = new HashMap<>();
        {
            params.put("access_token", access_token);
            params.put("openid", openid);
        }
        biz.getData(params, Contants.WEIXIN__USERINFO, new OkHttpClientManager.ResultCallback<UserInfoWXBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(Const.GUIDE_GET_USER_INFO_WX);
            }

            @Override
            public void onResponse(UserInfoWXBean response) {
                mainFragmentView.hideLoading(Const.GUIDE_GET_USER_INFO_WX);
                mainFragmentView.toActivity(response,Const.GUIDE_GET_USER_INFO_WX);
            }
        });
    }
}

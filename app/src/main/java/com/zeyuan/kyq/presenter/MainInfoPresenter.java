package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.MainPageInfoBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.view.ViewDataListener;
import com.zeyuan.kyq.utils.OkHttpClientManager;

/**
 * Created by Administrator on 2015/10/15.
 * 主页的present
 */
public class MainInfoPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;



    public MainInfoPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }


    public  void getData() {
        mainFragmentView.showLoading(0);
        biz.getData(mainFragmentView.getParamInfo(0),Contants.MAIN_PATH, new OkHttpClientManager.ResultCallback<MainPageInfoBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(0);
            }

            @Override
            public void onResponse(MainPageInfoBean response) {
                LogUtil.i("ok",response.toString());
                mainFragmentView.toActivity(response,0);
                mainFragmentView.hideLoading(0);
            }
        });
    }
}

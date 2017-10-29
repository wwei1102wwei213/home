package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.AllStepBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2015/10/15.
 * 获取所有阶段
 */
public class GetAllStepPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public GetAllStepPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }

    /**
     * 连接网络获取数据
     */
    public void getData() {
        mainFragmentView.showLoading(0);
        biz.getData(mainFragmentView.getParamInfo(0), Contants.GET_ALL_STEP, new OkHttpClientManager.ResultCallback<AllStepBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.hideLoading(0);
            }

            @Override
            public void onResponse(AllStepBean response) {
                mainFragmentView.hideLoading(0);
                mainFragmentView.toActivity(response,0);
            }
        });
    }
}

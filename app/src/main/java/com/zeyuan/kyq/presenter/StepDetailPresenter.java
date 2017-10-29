package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.StepDetailsBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2015/10/16.
 * 更行阶段详情
 */
public class StepDetailPresenter {

    private GetDataBiz biz;
    private ViewDataListener viewDataListener;

    public StepDetailPresenter(ViewDataListener viewDataListener) {
        biz = new GetDataBiz();
        this.viewDataListener = viewDataListener;
    }

    public void getData() {
        viewDataListener.showLoading(1);
        biz.getData(viewDataListener.getParamInfo(1), Contants.STEP_DETAIL, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                viewDataListener.showError(1);

            }

            @Override
            public void onResponse(String response) {
                viewDataListener.toActivity(response,1);
                viewDataListener.hideLoading(1);
            }
        });
    }
}

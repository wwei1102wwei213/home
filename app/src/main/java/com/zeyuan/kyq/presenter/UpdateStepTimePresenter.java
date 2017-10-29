package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2015/10/15.
 * 主页的present
 */
public class UpdateStepTimePresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public UpdateStepTimePresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }


    public void getData() {
        mainFragmentView.showLoading(1);
        biz.getData(mainFragmentView.getParamInfo(1), Contants.UPDATE_STEP_TIME, new OkHttpClientManager.ResultCallback<BaseBean>() {
            @Override
            public void onError(Request request, Exception e) {
            mainFragmentView.showError(1);
            }

            @Override
            public void onResponse(BaseBean response) {
                mainFragmentView.toActivity(response,1);
                mainFragmentView.hideLoading(1);
            }
        });
    }
}

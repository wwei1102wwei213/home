package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.CommBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2315/13/15.
 * 普通决策树 接口详情
 */
public final class GetCommDetailPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public GetCommDetailPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }

    /**
     * 连接网络获取数据
     */
    public void getData() {
        mainFragmentView.showLoading(3);
        biz.getData(mainFragmentView.getParamInfo(3), Contants.GET_COMMDETAIL, new OkHttpClientManager.ResultCallback<CommBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(3);
            }

            @Override
            public void onResponse(CommBean response) {
                mainFragmentView.hideLoading(3);
                mainFragmentView.toActivity(response,3);
            }
        });
    }
}

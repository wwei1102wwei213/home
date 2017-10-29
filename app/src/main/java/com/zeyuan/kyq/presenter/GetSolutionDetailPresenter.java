package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.CaseDetailBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2015/10/15.
 * 方案详情
 */
public final class GetSolutionDetailPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public GetSolutionDetailPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }

    /**
     * 连接网络获取数据
     */
    public void getData() {
        mainFragmentView.showLoading(0);
        biz.getData(mainFragmentView.getParamInfo(0), Contants.GET_SOLUTION_DETAIL, new OkHttpClientManager.ResultCallback<CaseDetailBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(0);
            }

            @Override
            public void onResponse(CaseDetailBean response) {
                mainFragmentView.hideLoading(0);
                mainFragmentView.toActivity(response,0);
            }
        });
    }
}

package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.CancerResuletBean;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2015/10/15.
 * 肿瘤决策树
 *
 * 决策树自定义单选要把所有的结果都返回给服务器，没有选就传0
 */
public final class GetResultPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public GetResultPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }

    /**
     * 连接网络获取数据
     */
    public void getData() {
        mainFragmentView.showLoading(1);
        biz.getData(mainFragmentView.getParamInfo(1), Contants.GET_RESULT_DETAIL, new OkHttpClientManager.ResultCallback<CancerResuletBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(1);
            }

            @Override
            public void onResponse(CancerResuletBean response) {
                mainFragmentView.hideLoading(1);
                mainFragmentView.toActivity(response, Const.EGetResultDetail);
            }
        });
    }
}

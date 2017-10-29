package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.CancerForumBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2015/10/15.
 * 14.获取抗癌圈的帖子数量和人数
 */
public final class CancerForumPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public CancerForumPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }

    /**
     * 连接网络获取数据
     */
    public void getData() {
        mainFragmentView.showLoading(0);
        biz.getData(mainFragmentView.getParamInfo(0), Contants.CANCER_FORUM, new OkHttpClientManager.ResultCallback<CancerForumBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(0);
            }

            @Override
            public void onResponse(CancerForumBean response) {
                mainFragmentView.hideLoading(0);
                mainFragmentView.toActivity(response,0);
            }
        });
    }
}

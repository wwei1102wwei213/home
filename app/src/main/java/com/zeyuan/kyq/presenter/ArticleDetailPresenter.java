package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.ArticleDetailBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2015/10/15.
 * 新增/更新症状
 */
public final class ArticleDetailPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public ArticleDetailPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }

    /**
     * 连接网络获取数据
     */
    public void getData() {
        mainFragmentView.showLoading(0);
        biz.getData(mainFragmentView.getParamInfo(0), Contants.ARTICLE_DETIAL, new OkHttpClientManager.ResultCallback<ArticleDetailBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(0);
            }

            @Override
            public void onResponse(ArticleDetailBean response) {
                mainFragmentView.hideLoading(0);
                mainFragmentView.toActivity(response,0);
            }
        });
    }
}

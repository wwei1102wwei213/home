package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.PostForumBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2015/10/15.
 * 主页的present
 */
public class PostForumPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public PostForumPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();

        this.mainFragmentView = mainFragmentView;
    }


    public void getData() {
        mainFragmentView.showLoading(0);
        biz.getData(mainFragmentView.getParamInfo(0), Contants.PostForum, new OkHttpClientManager.ResultCallback<PostForumBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(0);

            }

            @Override
            public void onResponse(PostForumBean response) {
                mainFragmentView.toActivity(response,0);
                mainFragmentView.hideLoading(0);
            }
        });
    }
}

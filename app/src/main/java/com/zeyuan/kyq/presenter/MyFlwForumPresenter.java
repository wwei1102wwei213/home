package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.FlwForumBean;
import com.zeyuan.kyq.bean.ForumListBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2NetNumber.GET_MY_FLW_FORUM15/1NetNumber.GET_MY_FLW_FORUM/15.
 * 我收藏的帖子
 */
public final class MyFlwForumPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public MyFlwForumPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }

    /**
     * 连接网络获取数据
     */
    public void getData() {
        mainFragmentView.showLoading(NetNumber.GET_MY_FLW_FORUM);
        biz.getData(mainFragmentView.getParamInfo(NetNumber.GET_MY_FLW_FORUM), Contants.MY_FOLLOW_FORUM, new OkHttpClientManager.ResultCallback<FlwForumBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(NetNumber.GET_MY_FLW_FORUM);
            }

            @Override
            public void onResponse(FlwForumBean response) {
                mainFragmentView.hideLoading(NetNumber.GET_MY_FLW_FORUM);
                mainFragmentView.toActivity(response,NetNumber.GET_MY_FLW_FORUM);
            }
        });
    }
}

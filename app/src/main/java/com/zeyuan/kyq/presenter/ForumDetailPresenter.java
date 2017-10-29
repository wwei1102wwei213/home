package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.ForumDetailBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2NetNumber.FORUM_DETAIL15/1NetNumber.FORUM_DETAIL/15.
 * 获取帖子详情
 */
public class ForumDetailPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public ForumDetailPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }


    public void getData() {
        mainFragmentView.showLoading(NetNumber.FORUM_DETAIL);
        biz.getData(mainFragmentView.getParamInfo(NetNumber.FORUM_DETAIL), Contants.FORUM_DETAIL, new OkHttpClientManager.ResultCallback<ForumDetailBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.hideLoading(NetNumber.FORUM_DETAIL);
            }

            @Override
            public void onResponse(ForumDetailBean response) {
                mainFragmentView.hideLoading(NetNumber.FORUM_DETAIL);
                mainFragmentView.toActivity(response,NetNumber.FORUM_DETAIL);
            }
        });
    }
}

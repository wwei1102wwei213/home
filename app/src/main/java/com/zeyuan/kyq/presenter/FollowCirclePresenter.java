package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.FollowBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2NetNumber.FOLLOW_CIRCLE15/1NetNumber.FOLLOW_CIRCLE/15.
 * 收藏帖子
 * 关注圈子
 */
public class FollowCirclePresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;

    public FollowCirclePresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }

    /**
     * 连接网络获取数据
     *
     */
    public void getData() {
        mainFragmentView.showLoading(NetNumber.FOLLOW_CIRCLE);
        biz.getData(mainFragmentView.getParamInfo(NetNumber.FOLLOW_CIRCLE), Contants.Follow_CIRCLE, new OkHttpClientManager.ResultCallback<FollowBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(NetNumber.FOLLOW_CIRCLE);
            }

            @Override
            public void onResponse(FollowBean response) {
                mainFragmentView.hideLoading(NetNumber.FOLLOW_CIRCLE);
                mainFragmentView.toActivity(response,NetNumber.FOLLOW_CIRCLE);
            }
        });
    }
}

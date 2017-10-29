package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.ReplyForum;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 20NetNumber.REPLY_FORUM5/NetNumber.REPLY_FORUM0/NetNumber.REPLY_FORUM9.
 *
 *
 *
 */
public class ReplyForumPresent {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public ReplyForumPresent(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }


    public void getData() {
        mainFragmentView.showLoading(NetNumber.REPLY_FORUM);
        biz.getData(mainFragmentView.getParamInfo(NetNumber.REPLY_FORUM), Contants.REPLY_FORUM, new OkHttpClientManager.ResultCallback<ReplyForum>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(NetNumber.REPLY_FORUM);
            }

            @Override
            public void onResponse(ReplyForum response) {
                mainFragmentView.toActivity(response,NetNumber.REPLY_FORUM);
                mainFragmentView.hideLoading(NetNumber.REPLY_FORUM);
            }
        });
    }
}

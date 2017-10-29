package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.bean.ReplyListBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2NetNumber.REPLY_LIST15/1NetNumber.REPLY_LIST/15.
 * 主页的present
 */
public class GetReplyListPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;



    public GetReplyListPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }


    public  void getData() {
        mainFragmentView.showLoading(NetNumber.REPLY_LIST);
        biz.getData(mainFragmentView.getParamInfo(NetNumber.REPLY_LIST),Contants.GET_REPLY_LIST, new OkHttpClientManager.ResultCallback<ReplyListBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(NetNumber.REPLY_LIST);

            }

            @Override
            public void onResponse(ReplyListBean response) {
                mainFragmentView.toActivity(response,NetNumber.REPLY_LIST);
                mainFragmentView.hideLoading(NetNumber.REPLY_LIST);
            }
        });
    }
}

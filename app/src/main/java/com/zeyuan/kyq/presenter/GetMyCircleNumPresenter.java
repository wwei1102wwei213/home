package com.zeyuan.kyq.presenter;

import android.util.Log;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.MyDataBean;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2NetNumber.GET_MY_CIRCLE_NUM15/1NetNumber.GET_MY_CIRCLE_NUM/15.
 * 这个是获取我的  里面的信息
 */
public final class GetMyCircleNumPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public GetMyCircleNumPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }

    /**
     * 连接网络获取数据
     */
    public void getData() {
        mainFragmentView.showLoading(NetNumber.GET_MY_CIRCLE_NUM);
        biz.getData(mainFragmentView.getParamInfo(Const.EGetMyForumNum), Contants.MY_CIRCLE_NUM, new OkHttpClientManager.ResultCallback<MyDataBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(NetNumber.GET_MY_CIRCLE_NUM);
            }

            @Override
            public void onResponse(MyDataBean response) {
                mainFragmentView.hideLoading(NetNumber.GET_MY_CIRCLE_NUM);
                Log.i("ok",response.toString());
                mainFragmentView.toActivity(response,NetNumber.GET_MY_CIRCLE_NUM);
            }
        });
    }
}

package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.WSZLBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2NetNumber.CONFIM_SECOND15/1NetNumber.CONFIM_SECOND/15.
 * 肿瘤决策树 完善资料
 */
public final class ConfirmSecondPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public ConfirmSecondPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }

    /**
     * 连接网络获取数据
     */
    public void getData() {
        mainFragmentView.showLoading(NetNumber.CONFIM_SECOND);
        biz.getData(mainFragmentView.getParamInfo(NetNumber.CONFIM_SECOND), Contants.GET_CONFIRM_SECOND, new OkHttpClientManager.ResultCallback<WSZLBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(NetNumber.CONFIM_SECOND);
            }

            @Override
            public void onResponse(WSZLBean response) {
                mainFragmentView.hideLoading(NetNumber.CONFIM_SECOND);
//                LogUtil.i("MainActivity",response);
                mainFragmentView.toActivity(response,NetNumber.CONFIM_SECOND);
            }
        });
    }
}

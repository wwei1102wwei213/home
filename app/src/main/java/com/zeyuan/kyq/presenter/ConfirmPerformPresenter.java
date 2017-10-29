package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 20NetNumber.CONFIRM_SYMPTOM5/NetNumber.CONFIRM_SYMPTOM0/NetNumber.CONFIRM_SYMPTOM5.
 * 新增/更新症状
 */
public final class ConfirmPerformPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public ConfirmPerformPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }

    /**
     * 连接网络获取数据
     */
    public void getData() {
        mainFragmentView.showLoading(NetNumber.CONFIRM_SYMPTOM);
        biz.getData(mainFragmentView.getParamInfo(NetNumber.CONFIRM_SYMPTOM), Contants.CONFIRM_PERFORM, new OkHttpClientManager.ResultCallback<BaseBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(NetNumber.CONFIRM_SYMPTOM);
            }

            @Override
            public void onResponse(BaseBean response) {
                mainFragmentView.hideLoading(NetNumber.CONFIRM_SYMPTOM);
                mainFragmentView.toActivity(response,NetNumber.CONFIRM_SYMPTOM);
            }
        });
    }
}

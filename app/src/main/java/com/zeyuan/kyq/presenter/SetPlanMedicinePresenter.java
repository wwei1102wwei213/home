package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 20NetNumber.SET_PLAN_MEDICINE5/NetNumber.SET_PLAN_MEDICINE0/NetNumber.SET_PLAN_MEDICINE5.
 * 添加到计划用药
 */
public final class SetPlanMedicinePresenter {

    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public SetPlanMedicinePresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }

    /**
     * 连接网络获取数据
     */
    public void getData() {
        mainFragmentView.showLoading(NetNumber.SET_PLAN_MEDICINE);
        biz.getData(mainFragmentView.getParamInfo(NetNumber.SET_PLAN_MEDICINE), Contants.SET_PLAN_MEDICINE, new OkHttpClientManager.ResultCallback<BaseBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(NetNumber.SET_PLAN_MEDICINE);
            }

            @Override
            public void onResponse(BaseBean response) {
                mainFragmentView.hideLoading(NetNumber.SET_PLAN_MEDICINE);
                mainFragmentView.toActivity(response,NetNumber.SET_PLAN_MEDICINE);
            }
        });
    }
}

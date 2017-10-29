package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.PatientDetailBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2NetNumber.PATIENT_DETAIL15/1NetNumber.PATIENT_DETAIL/15.
 */
public class PatientDetailPresenter {
    private GetDataBiz biz;
    private ViewDataListener viewDataListener;

    public PatientDetailPresenter(ViewDataListener viewDataListener) {
        biz = new GetDataBiz();
        this.viewDataListener = viewDataListener;
    }

    public void getData() {
        viewDataListener.showLoading(NetNumber.PATIENT_DETAIL);
        biz.getData(viewDataListener.getParamInfo(NetNumber.PATIENT_DETAIL), Contants.PATIENT_DETAIL, new OkHttpClientManager.ResultCallback<PatientDetailBean>() {
            @Override
            public void onError(Request request, Exception e) {
                viewDataListener.showError(NetNumber.PATIENT_DETAIL);
            }
            @Override
            public void onResponse(PatientDetailBean response) {
                viewDataListener.toActivity(response,NetNumber.PATIENT_DETAIL);
                viewDataListener.hideLoading(NetNumber.PATIENT_DETAIL);
            }
        });
    }

}

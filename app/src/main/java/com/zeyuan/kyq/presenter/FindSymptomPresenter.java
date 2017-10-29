package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.FindSymtomBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2NetNumber.FIND_SYMPTOM15/1NetNumber.FIND_SYMPTOM/15.
 * 查症状
 */
public final class FindSymptomPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public FindSymptomPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }

    /**
     * 连接网络获取数据
     */
    public void getData() {
        mainFragmentView.showLoading(NetNumber.FIND_SYMPTOM);
        biz.getData(mainFragmentView.getParamInfo(NetNumber.FIND_SYMPTOM), Contants.GET_CANCER_PROCESS, new OkHttpClientManager.ResultCallback<FindSymtomBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(NetNumber.FIND_SYMPTOM);
            }

            @Override
            public void onResponse(FindSymtomBean response) {
                mainFragmentView.hideLoading(NetNumber.FIND_SYMPTOM);
                mainFragmentView.toActivity(response,NetNumber.FIND_SYMPTOM);
            }
        });
    }
}

package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.SimilarListCaseBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2015/10/15.
 * 9.	获取当前用户相似案例列表
 */
public class SimilarCaseListPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;



    public SimilarCaseListPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }


    public  void getMainInfo() {
        mainFragmentView.showLoading(0);
        biz.getData(mainFragmentView.getParamInfo(0),Contants.MAIN_PATH, new OkHttpClientManager.ResultCallback<SimilarListCaseBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(0);

            }

            @Override
            public void onResponse(SimilarListCaseBean response) {
                mainFragmentView.toActivity(response,0);
                mainFragmentView.hideLoading(0);
            }
        });
    }
}

package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.ForumListBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2015/10/15.
 * 21.	圈子--获取某圈子下的帖子列表
 */
public class GetForumListPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;



    public GetForumListPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }


    public  void getData() {
        mainFragmentView.showLoading(0);
        biz.getData(mainFragmentView.getParamInfo(0),Contants.FORUM_LIST, new OkHttpClientManager.ResultCallback<ForumListBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(0);

            }

            @Override
            public void onResponse(ForumListBean response) {
                mainFragmentView.hideLoading(0);
                mainFragmentView.toActivity(response,0);
            }
        });
    }
}

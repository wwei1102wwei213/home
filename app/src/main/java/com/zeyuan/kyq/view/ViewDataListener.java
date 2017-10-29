package com.zeyuan.kyq.view;

import java.util.Map;

/**
 * Created by Administrator on 2015/10/15.
 * 这个用来与presenter交互
 */
public interface ViewDataListener {
    Map getParamInfo(int tag);

    void toActivity(Object t, int tag);

    void showLoading(int tag);

    void hideLoading(int tag);

    void showError(int tag);

}

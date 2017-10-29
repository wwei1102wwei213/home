package com.zeyuan.kyq.bean;

import com.zeyuan.kyq.Entity.Advertising;

import java.util.List;

/**
 * Created by Administrator on 2017/5/11.
 */

public class AdverticingListBean {
    private String iResult;
    private List<Advertising> data;

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public List<Advertising> getData() {
        return data;
    }

    public void setData(List<Advertising> data) {
        this.data = data;
    }
}

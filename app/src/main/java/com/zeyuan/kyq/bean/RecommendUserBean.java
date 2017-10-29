package com.zeyuan.kyq.bean;

import com.zeyuan.kyq.Entity.RecommendUserInfoEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/8/30.
 * 推荐好友
 */

public class RecommendUserBean {
    private String iResult;
    private List<RecommendUserInfoEntity> data;

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public List<RecommendUserInfoEntity> getData() {
        return data;
    }

    public void setData(List<RecommendUserInfoEntity> data) {
        this.data = data;
    }
}

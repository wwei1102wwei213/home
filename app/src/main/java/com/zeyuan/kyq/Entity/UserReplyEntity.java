package com.zeyuan.kyq.Entity;

import java.util.List;

/**
 * Created by Administrator on 2016/11/2.
 */
public class UserReplyEntity {

    private String iResult;
    private List<NewReplyEntity> data;

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public List<NewReplyEntity> getData() {
        return data;
    }

    public void setData(List<NewReplyEntity> data) {
        this.data = data;
    }
}

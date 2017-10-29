package com.zeyuan.kyq.bean;

import com.zeyuan.kyq.Entity.MsgEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 * 消息中心数据
 */

public class MsgBean {
    private String iResult;
    private List<MsgEntity> data;

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public List<MsgEntity> getData() {
        return data;
    }

    public void setData(List<MsgEntity> data) {
        this.data = data;
    }
}

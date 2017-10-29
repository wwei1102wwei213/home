package com.zeyuan.kyq.bean;

import com.zeyuan.kyq.Entity.ForumInfoEntity;

import java.io.Serializable;

/**
 * Created by huyongbiao on 2017/5/25.
 */

public class ForumInfoBean implements Serializable {
    private String iResult;
    private ForumInfoEntity data;

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public ForumInfoEntity getData() {
        return data;
    }

    public void setData(ForumInfoEntity data) {
        this.data = data;
    }
}

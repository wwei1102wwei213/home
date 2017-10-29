package com.zeyuan.kyq.bean;

import com.zeyuan.kyq.Entity.MsgSettingsEntity;

/**
 * Created by Administrator on 2017/7/24.
 * 友盟推送设置数据
 */

public class UMSettingsBean {
    private String iResult;
    private MsgSettingsEntity data;

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public MsgSettingsEntity getData() {
        return data;
    }

    public void setData(MsgSettingsEntity data) {
        this.data = data;
    }
}

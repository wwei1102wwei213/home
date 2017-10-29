package com.zeyuan.kyq.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/23.
 * 肿瘤实体类
 */
public class SlaverCancer implements Serializable {

    private int SlaverCancerLength;
    private int SlaverCancerWidth;
    private String SlaverCancerName;

    public int getSlaveLen() {
        return SlaverCancerLength;
    }

    public int getSlaveWidth() {
        return SlaverCancerWidth;
    }

    public String getSlaveName() {
        return SlaverCancerName;
    }

    public void setSlaveLen(int slaveLen) {
        SlaverCancerLength = slaveLen;
    }

    public void setSlaveWidth(int slaveWidth) {
        SlaverCancerWidth = slaveWidth;
    }

    public void setSlaveName(String slaveName) {
        SlaverCancerName = slaveName;
    }



}

package com.zeyuan.kyq.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/15.
 */
public class AddStepBean implements Serializable{

    private String iResult;
    private String StepUID;
    private String InfoID;

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public String getStepUID() {
        return StepUID;
    }

    public void setStepUID(String stepUID) {
        StepUID = stepUID;
    }

    @Override
    public String toString() {
        return "AddStepBean{" +
                "iResult='" + iResult + '\'' +
                ", StepUID='" + StepUID + '\'' +
                ", InfoID='" + InfoID + '\'' +
                '}';
    }
}

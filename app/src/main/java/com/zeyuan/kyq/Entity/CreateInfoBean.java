package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/27.
 */
public class CreateInfoBean implements Serializable{

    private String infoid;
    private String iResult;
    private String IsHaveCreateInfo;

    public String getInfoID() {
        return infoid;
    }

    public void setInfoID(String infoID) {
        infoid = infoID;
    }

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public String getIsHaveCreateInfo() {
        return IsHaveCreateInfo;
    }

    public void setIsHaveCreateInfo(String isHaveCreateInfo) {
        IsHaveCreateInfo = isHaveCreateInfo;
    }

    @Override
    public String toString() {
        return "CreateInfoBean{" +
                "InfoID='" + infoid + '\'' +
                ", iResult='" + iResult + '\'' +
                ", IsHaveCreateInfo='" + IsHaveCreateInfo + '\'' +
                '}';
    }
}

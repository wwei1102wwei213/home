package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/23.
 *
 *
 * @author wwei
 */
public class BindMobileEntity implements Serializable{

    private String iResult;
    private String TimeOutSeconds;
    private String ErrMsg;
    private String Mobile;

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public String getTimeOutSeconds() {
        return TimeOutSeconds;
    }

    public void setTimeOutSeconds(String timeOutSeconds) {
        TimeOutSeconds = timeOutSeconds;
    }

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String errMsg) {
        ErrMsg = errMsg;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    @Override
    public String toString() {
        return "BindMobileEntity{" +
                "iResult='" + iResult + '\'' +
                ", TimeOutSeconds='" + TimeOutSeconds + '\'' +
                ", ErrMsg='" + ErrMsg + '\'' +
                ", Mobile='" + Mobile + '\'' +
                '}';
    }
}

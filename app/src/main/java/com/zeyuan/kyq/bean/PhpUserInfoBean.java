package com.zeyuan.kyq.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/26.
 *
 * @author wwei
 */
public class PhpUserInfoBean implements Serializable {

    public String iResult;
    public String ErrMsg;
    public String msg;
    public String uid;
    public String InfoID;
    public String NowTime;
    public String doLogin;
    private String jf;//评论&回复获取的积分数

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String errMsg) {
        ErrMsg = errMsg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getInfoID() {
        return InfoID;
    }

    public void setInfoID(String infoID) {
        InfoID = infoID;
    }

    public String getNowTime() {
        return NowTime;
    }

    public void setNowTime(String nowTime) {
        NowTime = nowTime;
    }

    public String getDoLogin() {
        return doLogin;
    }

    public void setDoLogin(String doLogin) {
        this.doLogin = doLogin;
    }

    public String getJf() {
        return jf;
    }

    public void setJf(String jf) {
        this.jf = jf;
    }

    @Override
    public String toString() {
        return "PhpUserInfoBean{" +
                "iResult='" + iResult + '\'' +
                ", ErrMsg='" + ErrMsg + '\'' +
                ", msg='" + msg + '\'' +
                ", uid='" + uid + '\'' +
                ", InfoID='" + InfoID + '\'' +
                ", NowTime='" + NowTime + '\'' +
                ", doLogin='" + doLogin + '\'' +
                ", jf='" + jf + '\'' +
                '}';
    }
}

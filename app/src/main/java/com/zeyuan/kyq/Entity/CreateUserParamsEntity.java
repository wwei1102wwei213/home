package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/8.
 * 创建用户的参数实体
 */

public class CreateUserParamsEntity implements Serializable{
    private String mOpenID = null;
    private String mUnionID = null;
    private String lt = "1";
    private String phone = null;
    private String IsSelf;
    private String Current;

    public String getmOpenID() {
        return mOpenID;
    }

    public void setmOpenID(String mOpenID) {
        this.mOpenID = mOpenID;
    }

    public String getmUnionID() {
        return mUnionID;
    }

    public void setmUnionID(String mUnionID) {
        this.mUnionID = mUnionID;
    }

    public String getLt() {
        return lt;
    }

    public void setLt(String lt) {
        this.lt = lt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIsSelf() {
        return IsSelf;
    }

    public void setIsSelf(String isSelf) {
        IsSelf = isSelf;
    }

    public String getCurrent() {
        return Current;
    }

    public void setCurrent(String current) {
        Current = current;
    }
}

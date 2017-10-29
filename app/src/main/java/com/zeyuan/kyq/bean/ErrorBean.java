package com.zeyuan.kyq.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/30.
 */
public class ErrorBean implements Serializable{

    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ErrorBean{" +
                "error='" + error + '\'' +
                '}';
    }
}

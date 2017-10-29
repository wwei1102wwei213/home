package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/30.
 */
public class PerformEntity implements Serializable{

    private int performid;
    private String performname;

    public int getPerformid() {
        return performid;
    }

    public void setPerformid(int performid) {
        this.performid = performid;
    }

    public String getPerformname() {
        return performname;
    }

    public void setPerformname(String performname) {
        this.performname = performname;
    }

    @Override
    public String toString() {
        return "PerformEntity{" +
                "performid='" + performid + '\'' +
                ", performname='" + performname + '\'' +
                '}';
    }
}

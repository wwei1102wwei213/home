package com.zeyuan.kyq.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/4.
 */
public class TNMObj implements Serializable{
    private String cancerId;
    private String tid;
    private String nid;
    private String mid;
    private String digitId;


    public String getCancerId() {
        return cancerId;
    }

    public void setCancerId(String cancerId) {
        this.cancerId = cancerId;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getDigitId() {
        return digitId;
    }

    public void setDigitId(String digitId) {
        this.digitId = digitId;
    }

    @Override
    public String toString() {
        return "TNMObj{" +
                "cancerId='" + cancerId + '\'' +
                ", tid='" + tid + '\'' +
                ", nid='" + nid + '\'' +
                ", mid='" + mid + '\'' +
                ", digitId='" + digitId + '\'' +
                '}';
    }
}

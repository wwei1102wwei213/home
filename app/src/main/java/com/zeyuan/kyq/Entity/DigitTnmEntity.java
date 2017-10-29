package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/22.
 */
public class DigitTnmEntity implements Serializable{

    private int cancerid;
    private String t;
    private String n;
    private String m;

    public int getCancerid() {
        return cancerid;
    }

    public void setCancerid(int cancerid) {
        this.cancerid = cancerid;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    @Override
    public String toString() {
        return "DigitTnmEntity{" +
                "cancerid='" + cancerid + '\'' +
                ", t='" + t + '\'' +
                ", n='" + n + '\'' +
                ", m='" + m + '\'' +
                '}';
    }
}

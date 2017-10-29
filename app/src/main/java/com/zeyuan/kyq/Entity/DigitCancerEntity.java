package com.zeyuan.kyq.Entity;

/**
 * Created by Administrator on 2016/3/22.
 * 数字分期实体类
 *
 */
public class DigitCancerEntity {

    private int cancerid;
    private String digitid;

    public int getCancerid() {
        return cancerid;
    }

    public void setCancerid(int cancerid) {
        this.cancerid = cancerid;
    }

    public String getDigitid() {
        return digitid;
    }

    public void setDigitid(String digitid) {
        this.digitid = digitid;
    }

    @Override
    public String toString() {
        return "DigitCancerEntity{" +
                "cancerid='" + cancerid + '\'' +
                ", digitid='" + digitid + '\'' +
                '}';
    }
}

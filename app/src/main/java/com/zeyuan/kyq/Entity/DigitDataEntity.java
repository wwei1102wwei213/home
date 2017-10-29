package com.zeyuan.kyq.Entity;

/**
 * Created by Administrator on 2016/3/22.
 * 分期digitdata实体类封装
 *
 */
public class DigitDataEntity {

    private int digitid;
    private String digitvalue;

    public int getDigitid() {
        return digitid;
    }

    public void setDigitid(int digitid) {
        this.digitid = digitid;
    }

    public String getDigitvalue() {
        return digitvalue;
    }

    public void setDigitvalue(String digitvalue) {
        this.digitvalue = digitvalue;
    }

    @Override
    public String toString() {
        return "DigitDataEntity{" +
                "digitid='" + digitid + '\'' +
                ", digitvalue='" + digitvalue + '\'' +
                '}';
    }
}

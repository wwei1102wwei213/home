package com.zeyuan.kyq.utils;

/**
 * Created by Administrator on 2016/1/11.
 * 阶段里面
 * 时间判断类
 */
public class TimeBean {
    public TimeBean(){}

    public TimeBean(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private String startTime;
    private String endTime;

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getLength() {
        return Integer.valueOf(endTime) - Integer.valueOf(startTime);
    }

}

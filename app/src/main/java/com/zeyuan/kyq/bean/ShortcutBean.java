package com.zeyuan.kyq.bean;

import com.zeyuan.kyq.Entity.Shortcut;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/17.
 */

public class ShortcutBean implements Serializable {
    private String iResult;
    private String [] pointType;
    private List<Shortcut> data1;
    private List<Shortcut> data2;
    private List<Shortcut> data3;
    private List<Shortcut> data4;

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public String[] getPointType() {
        return pointType;
    }

    public void setPointType(String[] pointType) {
        this.pointType = pointType;
    }

    public List<Shortcut> getData1() {
        return data1;
    }

    public void setData1(List<Shortcut> data1) {
        this.data1 = data1;
    }

    public List<Shortcut> getData2() {
        return data2;
    }

    public void setData2(List<Shortcut> data2) {
        this.data2 = data2;
    }

    public List<Shortcut> getData3() {
        return data3;
    }

    public void setData3(List<Shortcut> data3) {
        this.data3 = data3;
    }

    public List<Shortcut> getData4() {
        return data4;
    }

    public void setData4(List<Shortcut> data4) {
        this.data4 = data4;
    }
}

package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/4.
 */
public class CureEntity implements Serializable{

    private int cureid;
    private int cureparentid;
    private String curename;

    public int getCureid() {
        return cureid;
    }

    public void setCureid(int cureid) {
        this.cureid = cureid;
    }

    public int getCureparentid() {
        return cureparentid;
    }

    public void setCureparentid(int cureparentid) {
        this.cureparentid = cureparentid;
    }

    public String getCurename() {
        return curename;
    }

    public void setCurename(String curename) {
        this.curename = curename;
    }

    @Override
    public String toString() {
        return "CureEntity{" +
                "cureid=" + cureid +
                ", cureparentid=" + cureparentid +
                ", curename='" + curename + '\'' +
                '}';
    }
}

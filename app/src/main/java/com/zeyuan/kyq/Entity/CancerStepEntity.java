package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/4.
 */
public class CancerStepEntity implements Serializable{

    private int cancerid;
    private String cancerstepdata;

    public int getCancerid() {
        return cancerid;
    }

    public void setCancerid(int cancerid) {
        this.cancerid = cancerid;
    }

    public String getCancerstepdata() {
        return cancerstepdata;
    }

    public void setCancerstepdata(String cancerstepdata) {
        this.cancerstepdata = cancerstepdata;
    }

    @Override
    public String toString() {
        return "CancerStepEntity{" +
                "cancerid=" + cancerid +
                ", cancerstepdata='" + cancerstepdata + '\'' +
                '}';
    }
}

package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/30.
 * 转移部位实体类
 *
 * @author wwei
 */
public class TransferPosEntity implements Serializable{

    private int tranrferid;
    private String tranrfername;

    public int getTranrferid() {
        return tranrferid;
    }

    public void setTranrferid(int tranrferid) {
        this.tranrferid = tranrferid;
    }

    public String getTranrfername() {
        return tranrfername;
    }

    public void setTranrfername(String tranrfername) {
        this.tranrfername = tranrfername;
    }

    @Override
    public String toString() {
        return "TransferPosEntity{" +
                "tranrferid='" + tranrferid + '\'' +
                ", tranrfername='" + tranrfername + '\'' +
                '}';
    }
}

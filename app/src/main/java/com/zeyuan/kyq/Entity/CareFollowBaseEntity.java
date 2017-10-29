package com.zeyuan.kyq.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/4.
 */
public class CareFollowBaseEntity implements Serializable{

    private String iResult;
    private int num;
    private String careNum;
    private List<CareFollowEntity> data;

    public String getiResult() {
        return iResult;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getCareNum() {
        return careNum;
    }

    public void setCareNum(String careNum) {
        this.careNum = careNum;
    }

    public List<CareFollowEntity> getData() {
        return data;
    }

    public void setData(List<CareFollowEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CareFollowBaseEntity{" +
                "iResult='" + iResult + '\'' +
                ", num=" + num +
                ", careNum='" + careNum + '\'' +
                ", data=" + data +
                '}';
    }
}

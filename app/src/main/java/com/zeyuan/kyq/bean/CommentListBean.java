package com.zeyuan.kyq.bean;

import java.util.List;

/**
 * Created by Administrator on 2017-12-03.
 */

public class CommentListBean {

    /* "iResult":"0",
    "AcommentNum":2,
    "HcommentNum":2,
    "McommentNum":0,
    "LcommentNum":0,
    "AvecommentNum":"4.0",
    "data":[*/

    private String iResult,AvecommentNum;
    private int AcommentNum,HcommentNum,McommentNum,LcommentNum;
    private List<CommentListItem> data;

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public String getAvecommentNum() {
        return AvecommentNum;
    }

    public void setAvecommentNum(String avecommentNum) {
        AvecommentNum = avecommentNum;
    }

    public int getAcommentNum() {
        return AcommentNum;
    }

    public void setAcommentNum(int acommentNum) {
        AcommentNum = acommentNum;
    }

    public int getHcommentNum() {
        return HcommentNum;
    }

    public void setHcommentNum(int hcommentNum) {
        HcommentNum = hcommentNum;
    }

    public int getMcommentNum() {
        return McommentNum;
    }

    public void setMcommentNum(int mcommentNum) {
        McommentNum = mcommentNum;
    }

    public int getLcommentNum() {
        return LcommentNum;
    }

    public void setLcommentNum(int lcommentNum) {
        LcommentNum = lcommentNum;
    }

    public List<CommentListItem> getData() {
        return data;
    }

    public void setData(List<CommentListItem> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CommentListBean{" +
                "iResult='" + iResult + '\'' +
                ", AvecommentNum='" + AvecommentNum + '\'' +
                ", AcommentNum=" + AcommentNum +
                ", HcommentNum=" + HcommentNum +
                ", McommentNum=" + McommentNum +
                ", LcommentNum=" + LcommentNum +
                ", data=" + data +
                '}';
    }
}

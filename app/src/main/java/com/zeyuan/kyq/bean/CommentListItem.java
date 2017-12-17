package com.zeyuan.kyq.bean;

import android.text.TextUtils;

import com.zeyuan.kyq.utils.DecryptUtils;

import java.util.List;

/**
 * Created by Administrator on 2017-12-03.
 */

public class CommentListItem {

    /*"Mobile":"17520487755",
            "CommnetNum":4,
            "CommentTxt":"R29vZGRkZC4%3D",
            "TGNum":5,
            "YXNum":5,
            "FWNum":5,
            "dateline":1510214610*/

    private String Mobile,CommentTxt;

    private int CommnetNum,TGNum,YXNum,FWNum,dateline;

    private List<String> PicUrl,PicTxt;

    public List<String> getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(List<String> picUrl) {
        PicUrl = picUrl;
    }

    public List<String> getPicTxt() {
        return PicTxt;
    }

    public void setPicTxt(List<String> picTxt) {
        PicTxt = picTxt;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getCommentTxt() {
        if (TextUtils.isEmpty(CommentTxt)) return "";
        String result = "";
        try {
            result = DecryptUtils.URLAnddecodeBase64(CommentTxt);
        }catch (Exception e){

        }
        return result;
    }

    public void setCommentTxt(String commentTxt) {
        CommentTxt = commentTxt;
    }

    public int getCommnetNum() {
        return CommnetNum;
    }

    public void setCommnetNum(int commnetNum) {
        CommnetNum = commnetNum;
    }

    public int getTGNum() {
        return TGNum;
    }

    public void setTGNum(int TGNum) {
        this.TGNum = TGNum;
    }

    public int getYXNum() {
        return YXNum;
    }

    public void setYXNum(int YXNum) {
        this.YXNum = YXNum;
    }

    public int getFWNum() {
        return FWNum;
    }

    public void setFWNum(int FWNum) {
        this.FWNum = FWNum;
    }

    public int getDateline() {
        return dateline;
    }

    public void setDateline(int dateline) {
        this.dateline = dateline;
    }

    @Override
    public String toString() {
        return "CommentListItem{" +
                "Mobile='" + Mobile + '\'' +
                ", CommentTxt='" + CommentTxt + '\'' +
                ", CommnetNum=" + CommnetNum +
                ", TGNum=" + TGNum +
                ", YXNum=" + YXNum +
                ", FWNum=" + FWNum +
                ", dateline=" + dateline +
                '}';
    }
}

package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/8.
 *
 *
 * @author wwei
 */
public class HelpItemEntity implements Serializable{

    private String InfoName;
    private String Title;
    private String HeadUrl;
    private String dateline;
    private String Price;
    private String typeName;
    private int View;
    private int PicNum;
    private int uid;
    private int followNum;
    private String endTime;

    public String getInfoName() {
        return InfoName;
    }

    public void setInfoName(String infoName) {
        InfoName = infoName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getHeadUrl() {
        return HeadUrl;
    }

    public void setHeadUrl(String headUrl) {
        HeadUrl = headUrl;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public int getPicNum() {
        return PicNum;
    }

    public void setPicNum(int picNum) {
        PicNum = picNum;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getView() {
        return View;
    }

    public void setView(int view) {
        View = view;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getFollowNum() {
        return followNum;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }

    @Override
    public String toString() {
        return "HelpItemEntity{" +
                "InfoName='" + InfoName + '\'' +
                ", Title='" + Title + '\'' +
                ", HeadUrl='" + HeadUrl + '\'' +
                ", dateline='" + dateline + '\'' +
                ", Price='" + Price + '\'' +
                ", typeName='" + typeName + '\'' +
                ", View=" + View +
                ", PicNum=" + PicNum +
                ", uid=" + uid +
                ", followNum=" + followNum +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}

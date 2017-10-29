package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/16.
 *
 * 主页banner封装类
 *
 * @author wwei
 */
public class MainBannerEntity implements Serializable{

    private String id;
    private String powerNum;
    private String tagtype;
    private String tagurl;
    private String infotext;
    private String imgurl;
    private String titlename;
    private String tagposition;
    private String starttime;
    private String endtime;
    private String startNum;
    private String isdeflg;
    private String cattype;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getInfotext() {
        return infotext;
    }

    public void setInfotext(String infotext) {
        this.infotext = infotext;
    }

    public String getTagurl() {
        return tagurl;
    }

    public void setTagurl(String tagurl) {
        this.tagurl = tagurl;
    }

    public String getTagtype() {
        return tagtype;
    }

    public void setTagtype(String tagtype) {
        this.tagtype = tagtype;
    }

    public String getPowerNum() {
        return powerNum;
    }

    public void setPowerNum(String powerNum) {
        this.powerNum = powerNum;
    }

    public String getTitlename() {
        return titlename;
    }

    public void setTitlename(String titlename) {
        this.titlename = titlename;
    }

    public String getTagposition() {
        return tagposition;
    }

    public void setTagposition(String tagposition) {
        this.tagposition = tagposition;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStartNum() {
        return startNum;
    }

    public void setStartNum(String startNum) {
        this.startNum = startNum;
    }

    public String getIsdeflg() {
        return isdeflg;
    }

    public void setIsdeflg(String isdeflg) {
        this.isdeflg = isdeflg;
    }

    public String getCattype() {
        return cattype;
    }

    public void setCattype(String cattype) {
        this.cattype = cattype;
    }

    @Override
    public String toString() {
        return "MainBannerEntity{" +
                "id='" + id + '\'' +
                ", powerNum='" + powerNum + '\'' +
                ", tagtype='" + tagtype + '\'' +
                ", tagurl='" + tagurl + '\'' +
                ", infotext='" + infotext + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", titlename='" + titlename + '\'' +
                ", tagposition='" + tagposition + '\'' +
                ", starttime='" + starttime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", startNum='" + startNum + '\'' +
                ", isdeflg='" + isdeflg + '\'' +
                ", cattype='" + cattype + '\'' +
                '}';
    }
}

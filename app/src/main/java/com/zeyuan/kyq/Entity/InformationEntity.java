package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/7.
 *
 *
 * @author wwei
 */
public class InformationEntity implements Serializable{


    private String title;
    private String ThumbURL;
    private String dateline;
    private String author;
    private String summary;
    private int LikeNum;
    private int viewnum;
    private int aid;
    private String anchorid;
    private int starttime;
    private int endtime;
    private int vid;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbURL() {
        return ThumbURL;
    }

    public void setThumbURL(String thumbURL) {
        ThumbURL = thumbURL;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public int getViewnum() {
        return viewnum;
    }

    public void setViewnum(int viewnum) {
        this.viewnum = viewnum;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getLikeNum() {
        return LikeNum;
    }

    public void setLikeNum(int likeNum) {
        LikeNum = likeNum;
    }

    public String getAnchorid() {
        return anchorid;
    }

    public void setAnchorid(String anchorid) {
        this.anchorid = anchorid;
    }

    public int getStarttime() {
        return starttime;
    }

    public void setStarttime(int starttime) {
        this.starttime = starttime;
    }

    public int getEndtime() {
        return endtime;
    }

    public void setEndtime(int endtime) {
        this.endtime = endtime;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    @Override
    public String toString() {
        return "InformationEntity{" +
                "title='" + title + '\'' +
                ", ThumbURL='" + ThumbURL + '\'' +
                ", dateline='" + dateline + '\'' +
                ", author='" + author + '\'' +
                ", summary='" + summary + '\'' +
                ", LikeNum=" + LikeNum +
                ", viewnum=" + viewnum +
                ", aid=" + aid +
                ", anchorid='" + anchorid + '\'' +
                ", starttime=" + starttime +
                ", endtime=" + endtime +
                ", vid=" + vid +
                '}';
    }
}

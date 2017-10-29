package com.zeyuan.kyq.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/11.
 *
 *
 * @author wwei
 */
public class ArticleCommentEntity implements Serializable{

    private String InfoID;
    private String InfoName;
    private String HeadUrl;
    private int DateLine;
    private String Content;
    private int ToUserId;
    private String ToUserName;
    private int Cid;
    private int LikeNum;
    private int HaveLike;
    private List<ArticleCommentEntity> Son;

    public String getInfoID() {
        return InfoID;
    }

    public void setInfoID(String infoID) {
        InfoID = infoID;
    }

    public String getInfoName() {
        return InfoName;
    }

    public void setInfoName(String infoName) {
        InfoName = infoName;
    }

    public String getHeadUrl() {
        return HeadUrl;
    }

    public void setHeadUrl(String headUrl) {
        HeadUrl = headUrl;
    }

    public int getDateLine() {
        return DateLine;
    }

    public void setDateLine(int dateLine) {
        DateLine = dateLine;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getToUserId() {
        return ToUserId;
    }

    public void setToUserId(int toUserId) {
        ToUserId = toUserId;
    }

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public int getCid() {
        return Cid;
    }

    public void setCid(int cid) {
        Cid = cid;
    }

    public int getLikeNum() {
        return LikeNum;
    }

    public void setLikeNum(int likeNum) {
        LikeNum = likeNum;
    }

    public List<ArticleCommentEntity> getSon() {
        return Son;
    }

    public void setSon(List<ArticleCommentEntity> son) {
        Son = son;
    }

    public int getHaveLike() {
        return HaveLike;
    }

    public void setHaveLike(int haveLike) {
        HaveLike = haveLike;
    }

    @Override
    public String toString() {
        return "ArticleCommentEntity{" +
                "InfoID='" + InfoID + '\'' +
                ", InfoName='" + InfoName + '\'' +
                ", HeadUrl='" + HeadUrl + '\'' +
                ", DateLine='" + DateLine + '\'' +
                ", Content='" + Content + '\'' +
                ", ToUserId=" + ToUserId +
                ", ToUserName='" + ToUserName + '\'' +
                ", Cid=" + Cid +
                ", LikeNum=" + LikeNum +
                ", HaveLike=" + HaveLike +
                ", Son=" + Son +
                '}';
    }
}

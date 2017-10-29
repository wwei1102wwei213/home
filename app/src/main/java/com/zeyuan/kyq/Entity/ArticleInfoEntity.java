package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/10.
 *
 *
 * @author wwei
 */
public class ArticleInfoEntity implements Serializable{

    private String iResult;
    private String ArtInfoName;
    private String ArtInfoID;
    private String ArtInfoHeadUrl;
    private int IsLike;
    private int IsFav;
    private int ArtCommentNum;
    private int LikeNum;

    public String getiResult() {
        return iResult;
    }

    public String getArtInfoName() {
        return ArtInfoName;
    }

    public void setArtInfoName(String artInfoName) {
        ArtInfoName = artInfoName;
    }

    public String getArtInfoID() {
        return ArtInfoID;
    }

    public void setArtInfoID(String artInfoID) {
        ArtInfoID = artInfoID;
    }

    public String getArtInfoHeadUrl() {
        return ArtInfoHeadUrl;
    }

    public void setArtInfoHeadUrl(String artInfoHeadUrl) {
        ArtInfoHeadUrl = artInfoHeadUrl;
    }

    public int getIsLike() {
        return IsLike;
    }

    public void setIsLike(int isLike) {
        IsLike = isLike;
    }

    public int getIsFav() {
        return IsFav;
    }

    public void setIsFav(int isFav) {
        IsFav = isFav;
    }

    public int getArtCommentNum() {
        return ArtCommentNum;
    }

    public void setArtCommentNum(int artCommentNum) {
        ArtCommentNum = artCommentNum;
    }

    public int getLikeNum() {
        return LikeNum;
    }

    public void setLikeNum(int likeNum) {
        LikeNum = likeNum;
    }

    @Override
    public String toString() {
        return "ArticleInfoEntity{" +
                "iResult='" + iResult + '\'' +
                ", ArtInfoName='" + ArtInfoName + '\'' +
                ", ArtInfoID='" + ArtInfoID + '\'' +
                ", ArtInfoHeadUrl='" + ArtInfoHeadUrl + '\'' +
                ", IsLike=" + IsLike +
                ", IsFav=" + IsFav +
                ", ArtCommentNum=" + ArtCommentNum +
                ", LikeNum=" + LikeNum +
                '}';
    }
}

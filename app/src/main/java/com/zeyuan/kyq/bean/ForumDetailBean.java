package com.zeyuan.kyq.bean;

import android.text.TextUtils;

import com.zeyuan.kyq.utils.DecryptUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/9/11.
 * 16.	获取帖子详情
 */
public class ForumDetailBean implements Serializable{

    /**
     * iResult : 0
     * Author :
     * ForumNum : 0
     * UserNum : 0
     * Index : 34
     * PostTime : 1448425831
     * Title : 测试
     * CircleId : 1002
     * Headimgurl : http://bucketn1.oss-cn-qingdao.aliyuncs.com/1010HeadImg14484261276613031912.png
     * OwnerID : 1010
     * ImgUrlNum : ["http://bucketn1.oss-cn-qingdao.aliyuncs.com/1010ForumImg144842583126223566020.png","http://bucketn1.oss-cn-qingdao.aliyuncs.com/1010ForumImg144842583128778994921.png"]
     * Content : 测试
     */

    private String iResult;
    private String Author;
    private String ReplyNum;
    private String ForumNum;
    private String UserNum;
    private String Index;
    private int PostTime;
    private String Title;
    private String CircleId;
    private String HeadImgUrl;
    private String OwnerID;
    private String Content;
    private List<String> ImgUrlNum;

    public String getReplyNum() {
        if (TextUtils.isEmpty(ReplyNum)) {
            return "0";
        }
        return ReplyNum;
    }



    public void setReplyNum(String replyNum) {
        ReplyNum = replyNum;
    }

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setAuthor(String Author) {
        this.Author = Author;
    }

    public void setForumNum(String ForumNum) {
        this.ForumNum = ForumNum;
    }

    public void setUserNum(String UserNum) {
        this.UserNum = UserNum;
    }

    public void setIndex(String Index) {
        this.Index = Index;
    }

    public void setPostTime(int PostTime) {
        this.PostTime = PostTime;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public void setCircleId(String CircleId) {
        this.CircleId = CircleId;
    }

    public void setHeadimgurl(String Headimgurl) {
        this.HeadImgUrl = Headimgurl;
    }

    public void setOwnerID(String OwnerID) {
        this.OwnerID = OwnerID;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public void setImgUrlNum(List<String> ImgUrlNum) {
        this.ImgUrlNum = ImgUrlNum;
    }

    public String getIResult() {
        return iResult;
    }

    public String getAuthor() {
        return Author;
    }

    public String getForumNum() {
        return ForumNum;
    }

    public String getUserNum() {
        return UserNum;
    }

    public String getIndex() {
        return Index;
    }

    public int getPostTime() {
        return PostTime;
    }

    public String getTitle() {
        return Title;
    }

    public String getCircleId() {
        return CircleId;
    }

    public String getHeadimgurl() {
        return HeadImgUrl;
    }

    public String getOwnerID() {
        return OwnerID;
    }

    public String getContent() {
        return DecryptUtils.decodeBase64(Content);
    }

    public List<String> getImgUrlNum() {
        return ImgUrlNum;
    }

    @Override
    public String toString() {
        return "ForumDetailBean{" +
                "iResult='" + iResult + '\'' +
                ", Author='" + Author + '\'' +
                ", ReplyNum='" + ReplyNum + '\'' +
                ", ForumNum='" + ForumNum + '\'' +
                ", UserNum='" + UserNum + '\'' +
                ", Index='" + Index + '\'' +
                ", PostTime=" + PostTime +
                ", Title='" + Title + '\'' +
                ", CircleId='" + CircleId + '\'' +
                ", HeadImgUrl='" + HeadImgUrl + '\'' +
                ", OwnerID='" + OwnerID + '\'' +
                ", Content='" + Content + '\'' +
                ", ImgUrlNum=" + ImgUrlNum +
                '}';
    }
}

package com.zeyuan.kyq.bean;

import com.zeyuan.kyq.utils.DecryptUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/24.
 */
public class ForumBeanEntity implements Serializable{
    private String CommentId;
    private String UserId;
    private String FromUser;
    private String ToUser;
    private String ReplyTime;
    private String Content;
    private String title;
    private String index;
    private String HeadImgUrl;

    public String getHeadImgurl() {
        return HeadImgUrl;
    }

    public void setHeadImgurl(String headImgurl) {
        HeadImgUrl = headImgurl;
    }

    public void setCommentId(String CommentId) {
        this.CommentId = CommentId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public void setFromUser(String FromUser) {
        this.FromUser = FromUser;
    }

    public void setToUser(String ToUser) {
        this.ToUser = ToUser;
    }

    public void setReplyTime(String ReplyTime) {
        this.ReplyTime = ReplyTime;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getCommentId() {
        return CommentId;
    }

    public String getUserId() {
        return UserId;
    }

    public String getFromUser() {
        return FromUser;
    }

    public String getToUser() {
        return ToUser;
    }

    public String getReplyTime() {
        return ReplyTime;
    }

    public String getContent() {
        return DecryptUtils.decodeBase64(Content);
    }

    public String getTitle() {
        return title;
    }

    public String getIndex() {
        return index;
    }

}

package com.zeyuan.kyq.Entity;


import java.io.Serializable;

public class NewReplyEntity implements Serializable{

    private String InfoName;
    private String HeadUrl;
    private String CommentContent;
    private String Title;
    private String Type;
    private String Pic;
    private int Id;
    private int ToUserID;
    private String ToUserName;
    private int ReplyTime;
    private int ReplyNum;

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

    public String getCommentContent() {
        return CommentContent;
    }

    public void setCommentContent(String commentContent) {
        CommentContent = commentContent;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getToUserID() {
        return ToUserID;
    }

    public void setToUserID(int toUserID) {
        ToUserID = toUserID;
    }

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public int getReplyTime() {
        return ReplyTime;
    }

    public void setReplyTime(int replyTime) {
        ReplyTime = replyTime;
    }

    public int getReplyNum() {
        return ReplyNum;
    }

    public void setReplyNum(int replyNum) {
        ReplyNum = replyNum;
    }
}

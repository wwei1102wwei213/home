package com.zeyuan.kyq.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/16.
 */
public class ForumBaseEntity implements Serializable{

    private String Index;
    private String Title;
    private String Author;
    private String HeadImgUrl;
    private String ReplyNum;
    private String LikeNum;
    private List<String> CircleId;//这个是圈子首页中带有circleid 在某个圈子下的帖子的列表 没有。
    private List<String> Pic;
    private String PostTime;
    private String DateLine;
    private String OwnerID;
    private String Posttype;//置顶1 精华2 普通0
    private String digest;
    private String displayorder;
    private String attachment;
    private String content;
    private String type;
    private String summary;

    public String getIndex() {
        return Index;
    }

    public void setIndex(String index) {
        Index = index;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getHeadImgUrl() {
        return HeadImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        HeadImgUrl = headImgUrl;
    }

    public String getReplyNum() {
        return ReplyNum;
    }

    public void setReplyNum(String replyNum) {
        ReplyNum = replyNum;
    }

    public List<String> getCircleId() {
        return CircleId;
    }

    public void setCircleId(List<String> circleId) {
        CircleId = circleId;
    }

    public String getPostTime() {
        return PostTime;
    }

    public void setPostTime(String postTime) {
        PostTime = postTime;
    }

    public String getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(String ownerID) {
        OwnerID = ownerID;
    }

    public String getPosttype() {
        return Posttype;
    }

    public void setPosttype(String posttype) {
        Posttype = posttype;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getDisplayorder() {
        return displayorder;
    }

    public void setDisplayorder(String displayorder) {
        this.displayorder = displayorder;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLikeNum() {
        return LikeNum;
    }

    public void setLikeNum(String likeNum) {
        LikeNum = likeNum;
    }

    public List<String> getPic() {
        return Pic;
    }

    public void setPic(List<String> pic) {
        Pic = pic;
    }

    public String getDateLine() {
        return DateLine;
    }

    public void setDateLine(String dateLine) {
        DateLine = dateLine;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "ForumBaseEntity{" +
                "Index='" + Index + '\'' +
                ", Title='" + Title + '\'' +
                ", Author='" + Author + '\'' +
                ", HeadImgUrl='" + HeadImgUrl + '\'' +
                ", ReplyNum='" + ReplyNum + '\'' +
                ", LikeNum='" + LikeNum + '\'' +
                ", CircleId=" + CircleId +
                ", Pic=" + Pic +
                ", PostTime='" + PostTime + '\'' +
                ", DateLine='" + DateLine + '\'' +
                ", OwnerID='" + OwnerID + '\'' +
                ", Posttype='" + Posttype + '\'' +
                ", digest='" + digest + '\'' +
                ", displayorder='" + displayorder + '\'' +
                ", attachment='" + attachment + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }
}

package com.zeyuan.kyq.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/18.
 *
 *
 * @author wwei
 */
public class FavEntity implements Serializable{

    private int FavID;
    private int ID;
    private String Title;
    private String Author;
    private int AuthorID;
    private int ViewNum;
    private List<String> ThreadImg;
    private String pic;
    private String DateLine;
    private String time;
    private String Content;
    private int type;
    private int LikeNum;
    private int PostType;

    public int getFavID() {
        return FavID;
    }

    public void setFavID(int favID) {
        FavID = favID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public int getAuthorID() {
        return AuthorID;
    }

    public void setAuthorID(int authorID) {
        AuthorID = authorID;
    }

    public int getViewNum() {
        return ViewNum;
    }

    public void setViewNum(int viewNum) {
        ViewNum = viewNum;
    }

    public List<String> getThreadImg() {
        return ThreadImg;
    }

    public void setThreadImg(List<String> threadImg) {
        ThreadImg = threadImg;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDateLine() {
        return DateLine;
    }

    public void setDateLine(String dateLine) {
        DateLine = dateLine;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLikeNum() {
        return LikeNum;
    }

    public void setLikeNum(int likeNum) {
        LikeNum = likeNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPostType() {
        return PostType;
    }

    public void setPostType(int postType) {
        PostType = postType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    @Override
    public String toString() {
        return "FavEntity{" +
                "FavID=" + FavID +
                ", ID=" + ID +
                ", Title='" + Title + '\'' +
                ", Author='" + Author + '\'' +
                ", AuthorID=" + AuthorID +
                ", ViewNum=" + ViewNum +
                ", ThreadImg=" + ThreadImg +
                ", pic='" + pic + '\'' +
                ", DateLine='" + DateLine + '\'' +
                ", time='" + time + '\'' +
                ", Type=" + type +
                ", LikeNum=" + LikeNum +
                ", PostType=" + PostType +
                '}';
    }
}

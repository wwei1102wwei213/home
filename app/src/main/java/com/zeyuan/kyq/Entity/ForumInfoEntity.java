package com.zeyuan.kyq.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/25.
 */

public class ForumInfoEntity implements Serializable {
    private String CircleID;
    private List<String> ImgUrl;
    private String is_share;
    private String tid;
    private String author;
    private String authorid;
    private String subject;
    private String message;

    public String getCircleID() {
        return CircleID;
    }

    public void setCircleID(String circleID) {
        CircleID = circleID;
    }

    public List<String> getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(List<String> imgUrl) {
        ImgUrl = imgUrl;
    }

    public String getIs_share() {
        return is_share;
    }

    public void setIs_share(String is_share) {
        this.is_share = is_share;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

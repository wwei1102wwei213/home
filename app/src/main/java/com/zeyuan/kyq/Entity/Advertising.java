package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/11.
 * 圈圈助手广告实体
 */

                  /*"id":"6",
                    "powerNum":"10",
                    "location":"1",
                    "title":"海外靶向代购标题",
                    "infotext":"海外靶向代购广告文案",
                    "imgurl":"http://oss-cn-shenzhen.aliyuncs.com/zeyuan1/14938048683697.jpg",
                    "tagtype":"2",
                    "tagurl":"454"
                    */
public class Advertising implements Serializable {
    private String id;
    private String powerNum;
    private String location;//广告类型（位置）
    private String title;
    private String infotext;
    private String imgurl;
    private String tagtype;
    private String skiptype;
    private String sign_a;
    private String sign_b;
    private String tagurl;
    private String pic_oos;//防护与营养指导的图片
    private String pic_oss;//广告图

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPowerNum() {
        return powerNum;
    }

    public void setPowerNum(String powerNum) {
        this.powerNum = powerNum;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfotext() {
        return infotext;
    }

    public void setInfotext(String infotext) {
        this.infotext = infotext;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getTagtype() {
        return tagtype;
    }

    public void setTagtype(String tagtype) {
        this.tagtype = tagtype;
    }

    public String getTagurl() {
        return tagurl;
    }

    public void setTagurl(String tagurl) {
        this.tagurl = tagurl;
    }

    public String getPic_oos() {
        return pic_oos;
    }

    public void setPic_oos(String pic_oos) {
        this.pic_oos = pic_oos;
    }

    public String getSkiptype() {
        return skiptype;
    }

    public void setSkiptype(String skiptype) {
        this.skiptype = skiptype;
    }

    public String getSign_a() {
        return sign_a;
    }

    public void setSign_a(String sign_a) {
        this.sign_a = sign_a;
    }

    public String getSign_b() {
        return sign_b;
    }

    public void setSign_b(String sign_b) {
        this.sign_b = sign_b;
    }

    public String getPic_oss() {
        return pic_oss;
    }

    public void setPic_oss(String pic_oss) {
        this.pic_oss = pic_oss;
    }

    @Override
    public String toString() {
        return "Advertising{" +
                "id='" + id + '\'' +
                ", powerNum='" + powerNum + '\'' +
                ", location='" + location + '\'' +
                ", title='" + title + '\'' +
                ", infotext='" + infotext + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", tagtype='" + tagtype + '\'' +
                ", skiptype='" + skiptype + '\'' +
                ", sign_a='" + sign_a + '\'' +
                ", sign_b='" + sign_b + '\'' +
                ", tagurl='" + tagurl + '\'' +
                ", pic_oos='" + pic_oos + '\'' +
                ", pic_oss='" + pic_oss + '\'' +
                '}';
    }
}

package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/17.
 * 圈子的快速入口类
 * {
 * "id": "0",
 * "name": "肺腺癌",
 * "powerNum": "0",
 * "location": "1",
 * "text": "",
 * "skiptype": "5",
 * "sign_a": "30",
 * "sign_b": ""
 * },
 */

public class Shortcut implements Serializable{
    private String id;
    private int tag;// 0,无 1,Hot 2,New
    private String name;
    private int powerNum;
    private String location;
    private String text;
    private String skiptype;
    private String sign_a;
    private String sign_b;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPowerNum() {
        return powerNum;
    }

    public void setPowerNum(int powerNum) {
        this.powerNum = powerNum;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    @Override
    public String toString() {
        return "Shortcut{" +
                "id='" + id + '\'' +
                ", tag=" + tag +
                ", name='" + name + '\'' +
                ", powerNum='" + powerNum + '\'' +
                ", location='" + location + '\'' +
                ", text='" + text + '\'' +
                ", skiptype='" + skiptype + '\'' +
                ", sign_a='" + sign_a + '\'' +
                ", sign_b='" + sign_b + '\'' +
                '}';
    }
}

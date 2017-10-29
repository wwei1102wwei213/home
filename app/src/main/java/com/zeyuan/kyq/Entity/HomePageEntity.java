package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/11.
 *
 * @author wwei
 */
public class HomePageEntity implements Serializable {
    private String tag;//直播状态0.回顾 1.直播 2.预告
    private String id;
    private String name;
    private String pic_oss;
    private String sign_a;
    private String sign_b;
    private String skiptype;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic_oss() {
        return pic_oss;
    }

    public void setPic_oss(String pic_oss) {
        this.pic_oss = pic_oss;
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

    public String getSkiptype() {
        return skiptype;
    }

    public void setSkiptype(String skiptype) {
        this.skiptype = skiptype;
    }

    @Override
    public String toString() {
        return "HomePageEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pic_oss='" + pic_oss + '\'' +
                ", sign_a='" + sign_a + '\'' +
                ", sign_b='" + sign_b + '\'' +
                ", skiptype='" + skiptype + '\'' +
                '}';
    }
}

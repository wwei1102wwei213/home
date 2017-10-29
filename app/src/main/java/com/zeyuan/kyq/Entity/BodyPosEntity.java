package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/30.
 */
public class BodyPosEntity implements Serializable{

    private int id;
    private String name;
    private String performs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPerforms() {
        return performs;
    }

    public void setPerforms(String performs) {
        this.performs = performs;
    }

    @Override
    public String toString() {
        return "BodyPosEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", performs='" + performs + '\'' +
                '}';
    }
}

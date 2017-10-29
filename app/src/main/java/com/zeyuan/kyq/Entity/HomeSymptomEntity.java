package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/10.
 *
 * 症状分类封装实体类
 *
 * @author wwei
 */
public class HomeSymptomEntity implements Serializable{

    private String id;
    private String name;
    private int res;

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

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    @Override
    public String toString() {
        return "HomeSymptomEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", res=" + res +
                '}';
    }
}

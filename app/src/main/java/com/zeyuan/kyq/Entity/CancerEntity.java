package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/29.
 * 癌症种类实体类
 *
 * @author wwei
 */
public class CancerEntity implements Serializable{

    private int id;
    private String name;
    private String level;
    private int parentid;

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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    @Override
    public String toString() {
        return "CancerEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", level='" + level + '\'' +
                ", parentid=" + parentid +
                '}';
    }
}

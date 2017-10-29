package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/15.
 *
 * 省份和城市的实体类
 *
 */
public class ProvinceEntity implements Serializable{

    private String id;
    private String name;
    private String cityarray;

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

    public String getCityarray() {
        return cityarray;
    }

    public void setCityarray(String cityarray) {
        this.cityarray = cityarray;
    }

    @Override
    public String toString() {
        return "ProvinceEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ctiy=" + cityarray +
                '}';
    }


}

package com.zeyuan.kyq.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/6/17.
 *
 *
 *
 * @author wwei
 */
public class QuotaItemEntity implements Serializable{

    private String id;
    private String name;
    private List<QuotaItemChildEntity> Data;

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

    public List<QuotaItemChildEntity> getData() {
        return Data;
    }

    public void setData(List<QuotaItemChildEntity> data) {
        Data = data;
    }

    @Override
    public String toString() {
        return "QuotaItemEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", Data=" + Data +
                '}';
    }
}

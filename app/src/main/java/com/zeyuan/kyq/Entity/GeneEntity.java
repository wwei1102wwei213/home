package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/30.
 * 基因实体类，封装基因的id和name
 *
 * @author wwei
 */
public class GeneEntity implements Serializable{

    private int geneid;
    private String genname;

    public int getGeneid() {
        return geneid;
    }

    public void setGeneid(int geneid) {
        this.geneid = geneid;
    }

    public String getGenname() {
        return genname;
    }

    public void setGenname(String genname) {
        this.genname = genname;
    }

    @Override
    public String toString() {
        return "GeneEntity{" +
                "geneid='" + geneid + '\'' +
                ", genname='" + genname + '\'' +
                '}';
    }
}

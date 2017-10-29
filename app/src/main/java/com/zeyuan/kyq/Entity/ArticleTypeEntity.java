package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/8.
 *
 *
 * @author wwei
 */
public class ArticleTypeEntity implements Serializable{

    private int catid;
    private String catname;
    private int cattype;

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public int getCattype() {
        return cattype;
    }

    public void setCattype(int cattype) {
        this.cattype = cattype;
    }

    @Override
    public String toString() {
        return "{\"cattype\":" + cattype +",\"catid\":" + catid +", \"catname\":\""+ catname + "\"}";
    }
}

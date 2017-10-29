package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/30.
 * 基因帮助实体类，癌种所对应的基因id列表
 *
 * @author wwei
 */
public class CancerGeneEntity implements Serializable{

    private String cancerid;
    private String genelist;

    public String getCancerid() {
        return cancerid;
    }

    public void setCancerid(String cancerid) {
        this.cancerid = cancerid;
    }

    public String getGenelist() {
        return genelist;
    }

    public void setGenelist(String genelist) {
        this.genelist = genelist;
    }

    @Override
    public String toString() {
        return "CancerGeneEntity{" +
                "cancerid='" + cancerid + '\'' +
                ", genelist='" + genelist + '\'' +
                '}';
    }
}

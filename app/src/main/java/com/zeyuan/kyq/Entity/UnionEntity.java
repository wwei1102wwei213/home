package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/4.
 * 联合用药实体类
 *
 * @author wwei
 */
public class UnionEntity implements Serializable{

    private int cancerid;
    private String uniondata;

    public int getCancerid() {
        return cancerid;
    }

    public void setCancerid(int cancerid) {
        this.cancerid = cancerid;
    }

    public String getUniondata() {
        return uniondata;
    }

    public void setUniondata(String uniondata) {
        this.uniondata = uniondata;
    }

    @Override
    public String toString() {
        return "UnionEntity{" +
                "cancerid=" + cancerid +
                ", uniondata='" + uniondata + '\'' +
                '}';
    }
}

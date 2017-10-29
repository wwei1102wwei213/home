package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/30.
 * 圈子实体类 分同城圈和抗癌圈 id>10000为抗癌圈
 *
 * @author wwei
 */
public class CircleEntity implements Serializable{

    private int circleid;
    private String circlename;

    public int getCircleid() {
        return circleid;
    }

    public void setCircleid(int circleid) {
        this.circleid = circleid;
    }

    public String getCirclename() {
        return circlename;
    }

    public void setCirclename(String circlename) {
        this.circlename = circlename;
    }

    @Override
    public String toString() {
        return "CircleEntity{" +
                "circleid='" + circleid + '\'' +
                ", circlename='" + circlename + '\'' +
                '}';
    }
}

package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/4.
 * 其他阶段实体类
 *
 * @author wwei
 */
public class OtherStepEntity implements Serializable {

    private int otherstepid;
    private String otherstepname;

    public int getOtherstepid() {
        return otherstepid;
    }

    public void setOtherstepid(int otherstepid) {
        this.otherstepid = otherstepid;
    }

    public String getOtherstepname() {
        return otherstepname;
    }

    public void setOtherstepname(String otherstepname) {
        this.otherstepname = otherstepname;
    }

    @Override
    public String toString() {
        return "OtherStepEntity{" +
                "otherstepid=" + otherstepid +
                ", otherstepname='" + otherstepname + '\'' +
                '}';
    }
}

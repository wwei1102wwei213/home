package com.zeyuan.kyq.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/27.
 */
public class ChemStep implements Serializable{


    public String time;
    public int step;
    public String chemName;
    @Override
    public String toString() {
        return "ChemStep [time=" + time + ", step=" + step + ", chemName="
                + chemName + "]";
    }
}

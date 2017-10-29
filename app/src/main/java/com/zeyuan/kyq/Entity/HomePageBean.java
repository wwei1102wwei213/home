package com.zeyuan.kyq.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 *
 * @author wwei
 */
public class HomePageBean implements Serializable{

    private String iResult;
    private List<HomePageEntity> data;

    public String getiResult() {
        return iResult;
    }

    public List<HomePageEntity> getData() {
        return data;
    }

    public void setData(List<HomePageEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HomePageBean{" +
                "iResult='" + iResult + '\'' +
                ", data=" + data +
                '}';
    }
}

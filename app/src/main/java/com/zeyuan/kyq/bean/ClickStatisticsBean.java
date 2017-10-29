package com.zeyuan.kyq.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/8.
 * 点击统计bean
 */

public class ClickStatisticsBean implements Serializable{

   private String iResult;

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }
}

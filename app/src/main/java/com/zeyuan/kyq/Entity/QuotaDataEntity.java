package com.zeyuan.kyq.Entity;

import java.util.List;

/**
 * Created by Administrator on 2016/6/17.
 *
 *
 *
 * @author wwei
 */
public class QuotaDataEntity {

    private String iResult;

    private List<QuotaItemEntity> Data;

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public List<QuotaItemEntity> getData() {
        return Data;
    }

    public void setData(List<QuotaItemEntity> data) {
        Data = data;
    }

    @Override
    public String toString() {
        return "QuotaDataEntity{" +
                "iResult='" + iResult + '\'' +
                ", Data=" + Data +
                '}';
    }
}

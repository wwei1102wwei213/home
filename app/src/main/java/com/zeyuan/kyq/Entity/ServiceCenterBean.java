package com.zeyuan.kyq.Entity;

import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 *
 * @author wwei
 */
public class ServiceCenterBean {

    private String iResult;

    private List<ServiceCenterItemEntity> data;

    public String getiResult() {
        return iResult;
    }

    public List<ServiceCenterItemEntity> getData() {
        return data;
    }

    public void setData(List<ServiceCenterItemEntity> data) {
        this.data = data;
    }
}

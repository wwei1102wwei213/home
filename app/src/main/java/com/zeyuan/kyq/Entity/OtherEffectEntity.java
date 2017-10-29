package com.zeyuan.kyq.Entity;

import com.zeyuan.kyq.bean.PolicyDataEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/6/17.
 */
public class OtherEffectEntity {

    private List<PolicyDataEntity> Data;

    public List<PolicyDataEntity> getData() {
        return Data;
    }

    public void setData(List<PolicyDataEntity> data) {
        Data = data;
    }

    @Override
    public String toString() {
        return "OtherEffectEntity{" +
                "Data=" + Data +
                '}';
    }
}

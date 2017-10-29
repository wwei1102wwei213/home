package com.zeyuan.kyq.Entity;

import com.zeyuan.kyq.bean.PolicyDataEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/6/14.
 *
 *
 * @author wwei
 */
public class StepItemEntity implements Serializable{

    private String StepID;
    private String CommonName;
    private List<PolicyDataEntity> Data;

    public String getStepID() {
        return StepID;
    }

    public void setStepID(String stepID) {
        StepID = stepID;
    }

    public String getStepName() {
        return CommonName;
    }

    public void setStepName(String stepName) {
        CommonName = stepName;
    }

    public List<PolicyDataEntity> getData() {
        return Data;
    }

    public void setData(List<PolicyDataEntity> data) {
        Data = data;
    }

    @Override
    public String toString() {
        return "StepItemEntity{" +
                "StepID='" + StepID + '\'' +
                ", StepName='" + CommonName + '\'' +
                ", Data=" + Data +
                '}';
    }
}

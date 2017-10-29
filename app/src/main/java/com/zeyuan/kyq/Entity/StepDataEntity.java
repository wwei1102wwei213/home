package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/20.
 */
public class StepDataEntity implements Serializable{

    private StepUpdataEntity StepData;

    public StepUpdataEntity getStepData() {
        return StepData;
    }

    @Override
    public String toString() {
        return "{\"StepData\":" + StepData +"}";
    }

    public void setStepData(StepUpdataEntity stepData) {
        StepData = stepData;
    }
}

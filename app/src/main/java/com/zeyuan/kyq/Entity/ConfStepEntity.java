package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/27.
 *
 * 查药实体类封装
 *
 * @author wwei
 */
public class ConfStepEntity implements Serializable{

    private String StepID;
    private String StepName;
    private String CancerID;
    private String CureConfID;
    private String FlagSelector;
    private String isDel;

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getStepID() {
        return StepID;
    }

    public void setStepID(String stepID) {
        StepID = stepID;
    }

    public String getFlagSelector() {
        return FlagSelector;
    }

    public void setFlagSelector(String flagSelector) {
        FlagSelector = flagSelector;
    }

    public String getCureConfID() {
        return CureConfID;
    }

    public void setCureConfID(String cureConfID) {
        CureConfID = cureConfID;
    }

    public String getCancerID() {
        return CancerID;
    }

    public void setCancerID(String cancerID) {
        CancerID = cancerID;
    }

    public String getStepName() {
        return StepName;
    }

    public void setStepName(String stepName) {
        StepName = stepName;
    }

    @Override
    public String toString() {
        return "{\"StepID\":\""+StepID+"\",\"StepName\":\""+StepName+"\",\"CancerID\":\""+CancerID+"\",\"CureConfID\":\""+CureConfID+"\",\"FlagSelector\":\""+FlagSelector+"\",\"isDel\":\""+isDel+"\"}";

    }
}

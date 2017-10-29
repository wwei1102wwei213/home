package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/7.
 * 编辑用户信息用到的对象
 */

public class EditInfoEntity implements Serializable {
    private String InfoID;
    private String CancerID;
    private String City;
    private String Province;
    private String DiscoverTime;
    private String PeriodID;
    private String PeriodType = "1";
    private String IsHaveStep;
    private String StepID;
    private String CureStartTime;
    private String isMedicineValid;

    public String getInfoID() {
        return InfoID;
    }

    public void setInfoID(String infoID) {
        InfoID = infoID;
    }

    public String getCancerID() {
        return CancerID;
    }

    public void setCancerID(String cancerID) {
        CancerID = cancerID;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getDiscoverTime() {
        return DiscoverTime;
    }

    public void setDiscoverTime(String discoverTime) {
        DiscoverTime = discoverTime;
    }

    public String getPeriodID() {
        return PeriodID;
    }

    public void setPeriodID(String periodID) {
        PeriodID = periodID;
    }

    public String getPeriodType() {
        return PeriodType;
    }

    public void setPeriodType(String periodType) {
        PeriodType = periodType;
    }

    public String getIsHaveStep() {
        return IsHaveStep;
    }

    public void setIsHaveStep(String isHaveStep) {
        IsHaveStep = isHaveStep;
    }

    public String getStepID() {
        return StepID;
    }

    public void setStepID(String stepID) {
        StepID = stepID;
    }

    public String getCureStartTime() {
        return CureStartTime;
    }

    public void setCureStartTime(String cureStartTime) {
        CureStartTime = cureStartTime;
    }

    public String getIsMedicineValid() {
        return isMedicineValid;
    }

    public void setIsMedicineValid(String isMedicineValid) {
        this.isMedicineValid = isMedicineValid;
    }
}

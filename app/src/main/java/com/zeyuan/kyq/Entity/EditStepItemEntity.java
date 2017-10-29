package com.zeyuan.kyq.Entity;

import android.text.TextUtils;

import com.zeyuan.kyq.utils.DecryptUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/9.
 *
 *
 *
 * @author wwei
 */
public class EditStepItemEntity implements Serializable{

    private int ID;
    private int StepID;
    private int CureConfID;
    private int StartTime;
    private int EndTime;
    private int isMedicineValid;
    private int IsNowStep;
    private String Remark;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getStepID() {
        return StepID;
    }

    public void setStepID(int stepID) {
        StepID = stepID;
    }

    public int getCureConfID() {
        return CureConfID;
    }

    public void setCureConfID(int cureConfID) {
        CureConfID = cureConfID;
    }

    public int getStartTime() {
        return StartTime;
    }

    public void setStartTime(int startTime) {
        StartTime = startTime;
    }

    public int getEndTime() {
        return EndTime;
    }

    public void setEndTime(int endTime) {
        EndTime = endTime;
    }

    public int getIsMedicineValid() {
        return isMedicineValid;
    }

    public void setIsMedicineValid(int isMedicineValid) {
        this.isMedicineValid = isMedicineValid;
    }

    public int getIsNowStep() {
        return IsNowStep;
    }

    public void setIsNowStep(int isNowStep) {
        IsNowStep = isNowStep;
    }

    public String getRemark() {
        if (TextUtils.isEmpty(Remark)){
            return "";
        }
        return DecryptUtils.decodeBase64(Remark);
    }

    public void setRemark(String remark) {
        if (TextUtils.isEmpty(remark)){
            Remark = "";
        }else {
            Remark = DecryptUtils.encode(remark);
        }
    }

    @Override
    public String toString() {
        return "{" +
                "\"ID\":" + ID +
                ",\"StepID\":" + StepID +
                ",\"StartTime\":" + StartTime +
                ",\"EndTime\":" + EndTime +
                ",\"isMedicineValid\":" + isMedicineValid +
                ",\"IsNowStep\":" + IsNowStep +
                ",\"Remark\":\"" + Remark + "\"" + "}";
    }
}

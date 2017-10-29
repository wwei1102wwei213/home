package com.zeyuan.kyq.Entity;

import android.text.TextUtils;

import com.zeyuan.kyq.utils.DecryptUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/8.
 *
 * @author wwei
 */
public class StepUserEntity implements Serializable{

    private String StepName;
    private int CureConfID;
    private String CureName;
    private String remark;
    private int ID;
    private long StartTime;
    private long EndTime;
    private int isMedicineValid;
    private int Type;
    private RecordItemEntity RI;
    private List<StepUserEntity> child;

    public String getStepName() {
        return StepName;
    }

    public void setStepName(String stepName) {
        StepName = stepName;
    }

    public String getCureName() {
        return CureName;
    }

    public void setCureName(String cureName) {
        CureName = cureName;
    }

    public String getRemark() {
        if (TextUtils.isEmpty(remark)){
            return "";
        }
        return DecryptUtils.decodeBase64(remark);
    }

    public int getCureConfID() {
        return CureConfID;
    }

    public void setCureConfID(int cureConfID) {
        CureConfID = cureConfID;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public long getStartTime() {
        return StartTime;
    }

    public void setStartTime(long startTime) {
        StartTime = startTime;
    }

    public long getEndTime() {
        return EndTime;
    }

    public void setEndTime(long endTime) {
        EndTime = endTime;
    }

    public int getIsMedicineValid() {
        return isMedicineValid;
    }

    public void setIsMedicineValid(int isMedicineValid) {
        this.isMedicineValid = isMedicineValid;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public RecordItemEntity getRI() {
        return RI;
    }

    public void setRI(RecordItemEntity RI) {
        this.RI = RI;
    }

    public List<StepUserEntity> getChild() {
        return child;
    }

    public void setChild(List<StepUserEntity> child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return "StepUserEntity{" +
                "StepName='" + StepName + '\'' +
                ", CureConfID='" + CureConfID + '\'' +
                ", CureName='" + CureName + '\'' +
                ", remark='" + remark + '\'' +
                ", ID=" + ID +
                ", StartTime=" + StartTime +
                ", EndTime=" + EndTime +
                ", isMedicineValid=" + isMedicineValid +
                ", Type=" + Type +
                ", RI=" + RI +
                ", child=" + child +
                '}';
    }
}

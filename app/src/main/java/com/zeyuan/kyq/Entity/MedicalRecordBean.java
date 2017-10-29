package com.zeyuan.kyq.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/8.
 *
 * @author wwei
 */
public class MedicalRecordBean implements Serializable{

    private List<StepUserEntity> StepUser;
    private List<RecordItemEntity> CancerMarkData;
    private List<RecordItemEntity> TumourInfoData;
    private List<RecordItemEntity> StepDetailPerform;
    private List<RecordItemEntity> TransferRecord;
    private List<RecordItemEntity> TransferGene;
    private List<RecordItemEntity> PresentationOther;

    public List<StepUserEntity> getStepUser() {
        return StepUser;
    }

    public void setStepUser(List<StepUserEntity> stepUser) {
        StepUser = stepUser;
    }

    public List<RecordItemEntity> getCancerMarkData() {
        return CancerMarkData;
    }

    public void setCancerMarkData(List<RecordItemEntity> cancerMarkData) {
        CancerMarkData = cancerMarkData;
    }

    public List<RecordItemEntity> getTumourInfoData() {
        return TumourInfoData;
    }

    public void setTumourInfoData(List<RecordItemEntity> tumourInfoData) {
        TumourInfoData = tumourInfoData;
    }

    public List<RecordItemEntity> getStepDetailPerform() {
        return StepDetailPerform;
    }

    public void setStepDetailPerform(List<RecordItemEntity> stepDetailPerform) {
        StepDetailPerform = stepDetailPerform;
    }

    public List<RecordItemEntity> getTransferRecord() {
        return TransferRecord;
    }

    public void setTransferRecord(List<RecordItemEntity> transferRecord) {
        TransferRecord = transferRecord;
    }

    public List<RecordItemEntity> getTransferGene() {
        return TransferGene;
    }

    public void setTransferGene(List<RecordItemEntity> transferGene) {
        TransferGene = transferGene;
    }

    public List<RecordItemEntity> getPresentationOther() {
        return PresentationOther;
    }

    public void setPresentationOther(List<RecordItemEntity> presentationOther) {
        PresentationOther = presentationOther;
    }

    @Override
    public String toString() {
        return "MedicalRecordBean{" +
                "StepUser=" + StepUser +
                ", CancerMarkData=" + CancerMarkData +
                ", TumourInfoData=" + TumourInfoData +
                ", StepDetailPerform=" + StepDetailPerform +
                ", TransferRecord=" + TransferRecord +
                ", TransferGene=" + TransferGene +
                ", PresentationOther=" + PresentationOther +
                '}';
    }
}

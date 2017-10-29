package com.zeyuan.kyq.Entity;

import android.text.TextUtils;

import com.zeyuan.kyq.utils.DecryptUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/23.
 *
 * 患者资料实体类
 *
 * @author wwei
 */
public class PatientDataEntity implements Serializable{

    private int Sex;
    private int Age;
    private int Cancer;
    private long DiscoverTime;
    private int PatientHeight;
    private int PatientWeight;
    private String OtherDisease;
    private String OtherDiseaseRemark;
    private int PeriodID;
    private int PeriodType;
    private int BodyStatus;
    private String TransferRecord;
    private String TransferGene;

    public int getSex() {
        return Sex;
    }

    public void setSex(int sex) {
        Sex = sex;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public int getCancer() {
        return Cancer;
    }

    public void setCancer(int cancer) {
        Cancer = cancer;
    }

    public long getDiscoverTime() {
        return DiscoverTime;
    }

    public void setDiscoverTime(long discoverTime) {
        DiscoverTime = discoverTime;
    }

    public int getPatientHeight() {
        return PatientHeight;
    }

    public void setPatientHeight(int patientHeight) {
        PatientHeight = patientHeight;
    }

    public int getPatientWeight() {
        return PatientWeight;
    }

    public void setPatientWeight(int patientWeight) {
        PatientWeight = patientWeight;
    }

    public String getOtherDisease() {
        return OtherDisease;
    }

    public void setOtherDisease(String otherDisease) {
        OtherDisease = otherDisease;
    }

    public String getOtherDiseaseRemarkForSecret() {
        if (TextUtils.isEmpty(OtherDiseaseRemark)) OtherDiseaseRemark = "";
        return OtherDiseaseRemark;
    }

    public String getOtherDiseaseRemark() {
        if (TextUtils.isEmpty(OtherDiseaseRemark)) OtherDiseaseRemark = "";
        return DecryptUtils.URLAnddecodeBase64(OtherDiseaseRemark);
    }

    public void setOtherDiseaseRemark(String otherDiseaseRemark) {
        if (TextUtils.isEmpty(otherDiseaseRemark)){
            this.OtherDiseaseRemark = "";
        }else {
            this.OtherDiseaseRemark = DecryptUtils.encodeAndURL(otherDiseaseRemark);
        }
    }

    /*public String getOtherDiseaseRemark() {
        return OtherDiseaseRemark;
    }

    public void setOtherDiseaseRemark(String otherDiseaseRemark) {
        OtherDiseaseRemark = otherDiseaseRemark;
    }*/

    public int getPeriodID() {
        return PeriodID;
    }

    public void setPeriodID(int periodID) {
        PeriodID = periodID;
    }

    public int getPeriodType() {
        return PeriodType;
    }

    public void setPeriodType(int periodType) {
        PeriodType = periodType;
    }

    public int getBodyStatus() {
        return BodyStatus;
    }

    public void setBodyStatus(int bodyStatus) {
        BodyStatus = bodyStatus;
    }

    public String getTransferRecord() {
        return TransferRecord;
    }

    public void setTransferRecord(String transferRecord) {
        TransferRecord = transferRecord;
    }

    public String getTransferGene() {
        return TransferGene;
    }

    public void setTransferGene(String transferGene) {
        TransferGene = transferGene;
    }

    @Override
    public String toString() {
        return "PatientDataEntity{" +
                "Sex=" + Sex +
                ", Age=" + Age +
                ", Cancer=" + Cancer +
                ", DiscoverTime=" + DiscoverTime +
                ", PatientHeight=" + PatientHeight +
                ", PatientWeight=" + PatientWeight +
                ", OtherDisease='" + OtherDisease +'\'' +
                ", OtherDiseaseRemark='" + OtherDiseaseRemark + '\'' +
                ", PeriodID=" + PeriodID +
                ", PeriodType=" + PeriodType +
                ", BodyStatus=" + BodyStatus +
                ", TransferRecord='" + TransferRecord + '\'' +
                ", TransferGene='" + TransferGene + '\'' +
                '}';
    }
}

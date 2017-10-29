package com.zeyuan.kyq.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/2.
 * 获取患者详情
 * GetPatientdetail
 */
public class PatientDetailBean extends BaseBean implements Serializable{

    /**
     * Headimgurl : http://bucketn1.oss-cn-qingdao.aliyuncs.com/9270HeadImg1452565382339649702.png
     * CancerID : 30
     * DiscoverTime : 1640752021
     * sex : 0
     * age : 0
     * city : 34465
     * Province : 10001
     * TransferPos : 9008,9005,9001
     * Gene :
     * InfoName : dafdafdsa1452565381751
     * PeriodType : 1
     * PeriodID : 2
     */

    private String HeadImgUrl;
    private String CancerID;
    private String DiscoverTime;
    private String sex;
    private String age;
    private String city;
    private String Province;
    private String TransferPos;
    private String Gene;
    private String InfoName;
    private String PeriodType;
    private String PeriodID;
    private String StepID;
    private String StepUID;
    private String CureConfID;
    private String IsHaveStep;
    private String BindOpenID;
    private String WXNickName;
    private int[] K;
    private String WD;
    private String TP;

    public String getWD() {
        return WD;
    }

    public void setWD(String WD) {
        this.WD = WD;
    }

    public void setTP(String TP) {
        this.TP = TP;
    }

    public String getTP() {
        return TP;
    }

    public String getBindOpenID() {
        return BindOpenID;
    }

    public void setBindOpenID(String bindOpenID) {
        BindOpenID = bindOpenID;
    }

    public String getWXNickName() {
        return WXNickName;
    }

    public void setWXNickName(String WXNickName) {
        this.WXNickName = WXNickName;
    }

    public int[] getK() {
        return K;
    }

    public void setK(int[] k) {
        K = k;
    }

    public void setHeadImgUrl(String headImgUrl) {
        HeadImgUrl = headImgUrl;
    }

    public String getStepID() {
        return StepID;
    }

    public void setStepID(String stepID) {
        StepID = stepID;
    }

    public String getStepUID() {
        return StepUID;
    }

    public void setStepUID(String stepUID) {
        StepUID = stepUID;
    }

    public String getCureConfID() {
        return CureConfID;
    }

    public void setCureConfID(String cureConfID) {
        CureConfID = cureConfID;
    }

    public String getIsHaveStep() {
        return IsHaveStep;
    }

    public void setIsHaveStep(String isHaveStep) {
        IsHaveStep = isHaveStep;
    }

    public void setCancerID(String CancerID) {
        this.CancerID = CancerID;
    }

    public void setDiscoverTime(String DiscoverTime) {
        this.DiscoverTime = DiscoverTime;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setProvince(String Province) {
        this.Province = Province;
    }

    public void setTransferPos(String TransferPos) {
        this.TransferPos = TransferPos;
    }

    public void setGene(String Gene) {
        this.Gene = Gene;
    }

    public void setInfoName(String InfoName) {
        this.InfoName = InfoName;
    }

    public void setPeriodType(String PeriodType) {
        this.PeriodType = PeriodType;
    }

    public void setPeriodID(String PeriodID) {
        this.PeriodID = PeriodID;
    }

    public String getHeadimgurl() {
        return HeadImgUrl;
    }

    public String getCancerID() {
        return CancerID;
    }

    public String getDiscoverTime() {
        return DiscoverTime;
    }

    public String getSex() {
        return sex;
    }

    public String getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return Province;
    }

    public String getTransferPos() {
        return TransferPos;
    }

    public String getGene() {
        return Gene;
    }

    public String getInfoName() {
        return InfoName;
    }

    public String getPeriodType() {
        return PeriodType;
    }

    public String getPeriodID() {
        return PeriodID;
    }
}

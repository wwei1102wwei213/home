package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/28.
 *
 * PUserInfo接口返回数据封装（个人中心页面）
 *
 * @author wwei
 */
public class InfoCenterEntity implements Serializable{

    private String iResult;
    private String HeadUrl;
    private String level;
    private String InfoName;
    private String IsCare;
    private String HaveAct;
    private String groupType;
    private String AutoTxt;
    private String Cancer;
    private String PeriodID;
    private String StepName;
    private String Time;
    private int CareNum;
    private int FollowNum;
    private int FavNum;
    private int DiscoverTime;

    public String getIResult() {
        return iResult;
    }

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public String getHeadUrl() {
        return HeadUrl;
    }

    public void setHeadUrl(String headUrl) {
        HeadUrl = headUrl;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getInfoName() {
        return InfoName;
    }

    public void setInfoName(String infoName) {
        InfoName = infoName;
    }

    public int getCareNum() {
        return CareNum;
    }

    public void setCareNum(int careNum) {
        CareNum = careNum;
    }

    public int getFollowNum() {
        return FollowNum;
    }

    public void setFollowNum(int followNum) {
        FollowNum = followNum;
    }

    public int getFavNum() {
        return FavNum;
    }

    public void setFavNum(int favNum) {
        FavNum = favNum;
    }

    public int getDiscoverTime() {
        return DiscoverTime;
    }

    public void setDiscoverTime(int discoverTime) {
        DiscoverTime = discoverTime;
    }

    public String getIsCare() {
        return IsCare;
    }

    public void setIsCare(String isCare) {
        IsCare = isCare;
    }

    public String getHaveAct() {
        return HaveAct;
    }

    public void setHaveAct(String haveArt) {
        HaveAct = haveArt;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getAutoTxt() {
        return AutoTxt;
    }

    public void setAutoTxt(String autoTxt) {
        AutoTxt = autoTxt;
    }

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public String getCancer() {
        return Cancer;
    }

    public void setCancer(String cancer) {
        Cancer = cancer;
    }

    public String getPeriodID() {
        return PeriodID;
    }

    public void setPeriodID(String periodID) {
        PeriodID = periodID;
    }

    public String getStepName() {
        return StepName;
    }

    public void setStepName(String stepName) {
        StepName = stepName;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    @Override
    public String toString() {
        return "InfoCenterEntity{" +
                "iResult='" + iResult + '\'' +
                ", HeadUrl='" + HeadUrl + '\'' +
                ", level='" + level + '\'' +
                ", InfoName='" + InfoName + '\'' +
                ", IsCare='" + IsCare + '\'' +
                ", HaveAct='" + HaveAct + '\'' +
                ", groupType='" + groupType + '\'' +
                ", AutoTxt='" + AutoTxt + '\'' +
                ", Cancer='" + Cancer + '\'' +
                ", PeriodID='" + PeriodID + '\'' +
                ", StepName='" + StepName + '\'' +
                ", Time='" + Time + '\'' +
                ", CareNum=" + CareNum +
                ", FollowNum=" + FollowNum +
                ", FavNum=" + FavNum +
                ", DiscoverTime=" + DiscoverTime +
                '}';
    }
}

package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/11.
 *
 * 数据库主表实体类
 *
 *
 * @author wwei
 */
public class MainTableEntity implements Serializable{

    //id,操作标志
    private int id;
    //为空标志位 0为空 1不为空
    private int emptyFlag;
    //版本号
    private String versionName;
    //分期数据填充标志
    private int digitIsHave;
    //conf数据填充标志
    private int synconfIsHave;
    //分期数据
    private String digitData;
    //conf数据
    private String synconfData;
    //分期数据解析封装完成标志
    private int digitParseFlag;
    //conf数据解析封装完成标志
    private int synconfParseFlag;
    //其他备用字段
    private String otherId;
    private String otherData;
    private int otherFlag;
    private int otherIsHave;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmptyFlag() {
        return emptyFlag;
    }

    public void setEmptyFlag(int emptyFlag) {
        this.emptyFlag = emptyFlag;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getDigitIsHave() {
        return digitIsHave;
    }

    public void setDigitIsHave(int digitIsHave) {
        this.digitIsHave = digitIsHave;
    }

    public int getSynconfIsHave() {
        return synconfIsHave;
    }

    public void setSynconfIsHave(int synconfIsHave) {
        this.synconfIsHave = synconfIsHave;
    }

    public String getDigitData() {
        return digitData;
    }

    public void setDigitData(String digitData) {
        this.digitData = digitData;
    }

    public String getSynconfData() {
        return synconfData;
    }

    public void setSynconfData(String synconfData) {
        this.synconfData = synconfData;
    }

    public int getDigitParseFlag() {
        return digitParseFlag;
    }

    public void setDigitParseFlag(int digitParseFlag) {
        this.digitParseFlag = digitParseFlag;
    }

    public int getSynconfParseFlag() {
        return synconfParseFlag;
    }

    public void setSynconfParseFlag(int synconfParseFlag) {
        this.synconfParseFlag = synconfParseFlag;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public String getOtherData() {
        return otherData;
    }

    public void setOtherData(String otherData) {
        this.otherData = otherData;
    }

    public int getOtherFlag() {
        return otherFlag;
    }

    public void setOtherFlag(int otherFlag) {
        this.otherFlag = otherFlag;
    }

    public int getOtherIsHave() {
        return otherIsHave;
    }

    public void setOtherIsHave(int otherIsHave) {
        this.otherIsHave = otherIsHave;
    }

    @Override
    public String toString() {
        return "MainTableEntity{" +
                "id=" + id +
                ", emptyFlag=" + emptyFlag +
                ", versionName='" + versionName + '\'' +
                ", digitIsHave=" + digitIsHave +
                ", synconfIsHave=" + synconfIsHave +
                ", digitData='" + digitData + '\'' +
                ", synconfData='" + synconfData + '\'' +
                ", digitParseFlag=" + digitParseFlag +
                ", synconfParseFlag=" + synconfParseFlag +
                ", otherId='" + otherId + '\'' +
                ", otherData='" + otherData + '\'' +
                ", otherFlag=" + otherFlag +
                ", otherIsHave=" + otherIsHave +
                '}';
    }
}

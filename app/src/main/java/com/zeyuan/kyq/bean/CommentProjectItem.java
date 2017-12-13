package com.zeyuan.kyq.bean;

import android.text.TextUtils;

import com.zeyuan.kyq.utils.DecryptUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-11-25.
 */

public class CommentProjectItem implements Serializable{

    /*"PicUrl":"http://oss-cn-shenzhen.aliyuncs.com/zeyuan1/15102167946713.jpg",
            "CureTypeName":"",
            "id":1,
            "Pname":"中医治疗、康复项目",
            "Psubject":"",
            "HComNum":2,
            "MComNum":0,
            "LComNum":0,
            "TagType":0,
            "TagUrl":"0",
            "TagOpen":1*/
    /*"PicUrl":"",
            "job":"",
            "id":4,
            "Pname":"李医生",
            "HComRate":100,
            "Psubject":"",
            "HComNum":1,
            "MComNum":0,
            "LComNum":0,
            "TagType":0,
            "TagUrl":"",
            "TagOpen":0*/
    /*"PicUrl":"",
            "CureTypeName":"",
            "id":4,
            "Pname":"北大深圳医院",
            "HospitalLevelName":"无",
            "Psubject":null,
            "HComNum":1,
            "MComNum":0,
            "LComNum":0,
            "TagType":0,
            "TagUrl":"0",
            "TagOpen":0*/

    private String PicUrl,CureTypeName,Pname,Psubject,TagUrl,job,HospitalLevelName;
    private int id,HComNum,MComNum,LComNum,TagType,TagOpen;
    private String HComRate;

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getCureTypeName() {
        return CureTypeName;
    }

    public void setCureTypeName(String cureTypeName) {
        CureTypeName = cureTypeName;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public String getPsubject() {
        if (TextUtils.isEmpty(Psubject)) return "";
        String result = "";
        try {
            result = DecryptUtils.URLAnddecodeBase64(Psubject);
        }catch (Exception e){

        }
        return result;
    }

    public void setPsubject(String psubject) {
        Psubject = psubject;
    }

    public String getTagUrl() {
        return TagUrl;
    }

    public void setTagUrl(String tagUrl) {
        TagUrl = tagUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHComNum() {
        return HComNum;
    }

    public void setHComNum(int HComNum) {
        this.HComNum = HComNum;
    }

    public int getMComNum() {
        return MComNum;
    }

    public void setMComNum(int MComNum) {
        this.MComNum = MComNum;
    }

    public int getLComNum() {
        return LComNum;
    }

    public void setLComNum(int LComNum) {
        this.LComNum = LComNum;
    }

    public int getTagType() {
        return TagType;
    }

    public void setTagType(int tagType) {
        TagType = tagType;
    }

    public int getTagOpen() {
        return TagOpen;
    }

    public void setTagOpen(int tagOpen) {
        TagOpen = tagOpen;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getHospitalLevelName() {
        return HospitalLevelName;
    }

    public void setHospitalLevelName(String hospitalLevelName) {
        HospitalLevelName = hospitalLevelName;
    }

    public String getHComRate() {
        return HComRate;
    }

    public void setHComRate(String HComRate) {
        this.HComRate = HComRate;
    }

    @Override
    public String toString() {
        return "CommentProjectItem{" +
                "PicUrl='" + PicUrl + '\'' +
                ", CureTypeName='" + CureTypeName + '\'' +
                ", Pname='" + Pname + '\'' +
                ", Psubject='" + Psubject + '\'' +
                ", TagUrl='" + TagUrl + '\'' +
                ", job='" + job + '\'' +
                ", HospitalLevelName='" + HospitalLevelName + '\'' +
                ", id=" + id +
                ", HComNum=" + HComNum +
                ", MComNum=" + MComNum +
                ", LComNum=" + LComNum +
                ", TagType=" + TagType +
                ", TagOpen=" + TagOpen +
                ", HComRate=" + HComRate +
                '}';
    }
}

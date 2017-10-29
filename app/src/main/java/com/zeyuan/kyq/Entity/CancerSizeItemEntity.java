package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/1.
 *
 * @author wwei
 */
public class CancerSizeItemEntity implements Serializable{

    private int InfoID;
    private long RecordTime;
    private int TypeID;
    private String Size;
    private String name;
    private int PatientID;
    private int delflag;
    private int is_show;
    private long dateline;

    public int getInfoID() {
        return InfoID;
    }

    public void setInfoID(int infoID) {
        InfoID = infoID;
    }

    public long getRecordTime() {
        return RecordTime;
    }

    public void setRecordTime(long recordTime) {
        RecordTime = recordTime;
    }

    public int getTypeID() {
        return TypeID;
    }

    public void setTypeID(int typeID) {
        TypeID = typeID;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public int getPatientID() {
        return PatientID;
    }

    public void setPatientID(int patientID) {
        PatientID = patientID;
    }

    public int getDelflag() {
        return delflag;
    }

    public void setDelflag(int delflag) {
        this.delflag = delflag;
    }

    public long getDateline() {
        return dateline;
    }

    public void setDateline(long dateline) {
        this.dateline = dateline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIs_show() {
        return is_show;
    }

    public void setIs_show(int is_show) {
        this.is_show = is_show;
    }

    @Override
    public String toString() {
        return "{" +
                "\"InfoID\":" + InfoID +
                ",\"RecordTime\":" + RecordTime +
                ",\"TypeID\":" + TypeID +
                ",\"Size\":\"" + Size + '\"' +
                ",\"name\":\"" + name + '\"' +
                ",\"PatientID\":" + PatientID +
                ",\"delflag\":" + delflag +
                ",\"dateline\":" + dateline +
                '}';
    }
}

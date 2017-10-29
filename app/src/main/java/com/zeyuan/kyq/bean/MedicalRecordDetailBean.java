package com.zeyuan.kyq.bean;

import com.zeyuan.kyq.Entity.RecordItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/6.
 * 用户的病历记录详情
 */

public class MedicalRecordDetailBean implements Serializable {
    private String iResult;
    private List<RecordItemEntity> data;

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public List<RecordItemEntity> getData() {
        return data;
    }

    public void setData(List<RecordItemEntity> data) {
        this.data = data;
    }
}

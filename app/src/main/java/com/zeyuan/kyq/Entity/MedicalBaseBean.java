package com.zeyuan.kyq.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/8.
 *
 * @author WWEI
 */
public class MedicalBaseBean implements Serializable{

    private String iResult;
    private MedicalRecordBean data;
    private List<RecordItemEntity> Report;
    private List<RecordItemEntity> Diagnosis;

    public String getiResult() {
        return iResult;
    }

    public MedicalRecordBean getData() {
        return data;
    }

    public void setData(MedicalRecordBean data) {
        this.data = data;
    }

    public List<RecordItemEntity> getReport() {
        return Report;
    }

    public List<RecordItemEntity> getDiagnosis() {
        return Diagnosis;
    }
}

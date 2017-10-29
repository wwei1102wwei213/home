package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/24.
 *
 * @author wwei
 */
public class PatientDataBean implements Serializable{

    private String iResult;
    private PatientDataEntity data;

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public PatientDataEntity getData() {
        return data;
    }

    public void setData(PatientDataEntity data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PatientDataBean{" +
                "iResult='" + iResult + '\'' +
                ", data=" + data +
                '}';
    }
}

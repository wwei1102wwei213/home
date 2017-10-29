package com.zeyuan.kyq.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/2/9.
 *
 * @author wwei
 */
public class EditStepBean implements Serializable{

    private String iResult;
    private List<EditStepItemEntity> data;

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public List<EditStepItemEntity> getData() {
        return data;
    }

    public void setData(List<EditStepItemEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "EditStepBean{" +
                "iResult='" + iResult + '\'' +
                ", data=" + data +
                '}';
    }
}

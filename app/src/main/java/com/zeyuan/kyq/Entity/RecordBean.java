package com.zeyuan.kyq.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/2/21.
 *
 * @author wwei
 */
public class RecordBean implements Serializable{

    private String iResult;
    private List<RecordItemEntity> data;

    public List<RecordItemEntity> getData() {
        return data;
    }

    public void setData(List<RecordItemEntity> data) {
        this.data = data;
    }

    public String getiResult() {
        return iResult;
    }



}

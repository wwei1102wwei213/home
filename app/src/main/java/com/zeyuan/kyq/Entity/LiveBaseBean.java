package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/5.
 */
public class LiveBaseBean implements Serializable{

    private String iResult;
    private LiveItemEntity data;

    public String IResult() {
        return iResult;
    }

    public LiveItemEntity getData() {
        return data;
    }

    public void setData(LiveItemEntity data) {
        this.data = data;
    }
}

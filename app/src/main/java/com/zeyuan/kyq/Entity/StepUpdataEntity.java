package com.zeyuan.kyq.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/6/20.
 */
public class StepUpdataEntity implements Serializable{

    private String maxtimestamp;
    private List<ConfStepEntity> data;

    public String getMaxtimestamp() {
        return maxtimestamp;
    }

    public void setMaxtimestamp(String maxtimestamp) {
        this.maxtimestamp = maxtimestamp;
    }

    public List<ConfStepEntity> getData() {
        return data;
    }

    public void setData(List<ConfStepEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{\"maxtimestamp\":\"" + maxtimestamp + "\", \"data\":" + data +"}";
    }
}

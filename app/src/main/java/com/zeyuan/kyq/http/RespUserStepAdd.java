package com.zeyuan.kyq.http;

import java.io.Serializable;

/**
 * Created by guogzhao on 16/1/18.
 */
public class RespUserStepAdd extends RespBase implements Serializable {

    private String AddStepUID;

    public String getAddStepUID() {
        return AddStepUID;
    }

    public void setAddStepUID(String addStepUID) {
        AddStepUID = addStepUID;
    }
}

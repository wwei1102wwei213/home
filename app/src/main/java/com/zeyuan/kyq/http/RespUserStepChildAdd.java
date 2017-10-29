package com.zeyuan.kyq.http;

import java.io.Serializable;

/**
 * Created by guogzhao on 16/1/18.
 */
public class RespUserStepChildAdd extends RespBase implements Serializable{

    //    private String BlankStepUID;
    private String AddQuotaID;
    private String AddPerformID;
    private String BlankStepUID;

    public String getAddQuotaID() {
        return AddQuotaID;
    }

    public void setAddQuotaID(String addQuotaID) {
        AddQuotaID = addQuotaID;
    }

    public String getAddPerformID() {
        return AddPerformID;
    }

    public void setAddPerformID(String addPerformID) {
        AddPerformID = addPerformID;
    }

    public String getBlankStepUID() {
        return BlankStepUID;
    }

    public void setBlankStepUID(String blankStepUID) {
        BlankStepUID = blankStepUID;
    }
}

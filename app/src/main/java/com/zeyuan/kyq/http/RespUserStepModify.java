package com.zeyuan.kyq.http;

import java.io.Serializable;

/**
 * Created by guogzhao on 16/1/18.
 *
 *
 * @author
 */
public class RespUserStepModify extends RespBase implements Serializable{

//    private String DelQuotaID;//:10,11
//    private String DelPerformID;//:15,16
    private String ConflictQuotaID;//:10,11
    private String ConflictPerformID;//:15,16
//    "":"50",
//            "":"",
    public String getDelQuotaID() {
        return ConflictQuotaID;
    }

    public void setDelQuotaID(String delQuotaID) {
        ConflictQuotaID = delQuotaID;
    }

    public String getDelPerformID() {
        return ConflictPerformID;
    }

    public void setDelPerformID(String delPerformID) {
        ConflictPerformID = delPerformID;
    }
}

package com.zeyuan.kyq.bean;

import java.io.Serializable;

/**
 * User: zeyuan
 * Date: 2015-11-03
 * Time: 15:24
 * FIXME
 */


public class FollowBean implements Serializable{

    /**
     * iResult : 0
     * InfoId : 9
     * CircleID : 10001
     */

    private String iResult;
    private String InfoId;
    private String CircleID;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setInfoId(String InfoId) {
        this.InfoId = InfoId;
    }

    public void setCircleID(String CircleID) {
        this.CircleID = CircleID;
    }

    public String getIResult() {
        return iResult;
    }

    public String getInfoId() {
        return InfoId;
    }

    public String getCircleID() {
        return CircleID;
    }


    @Override
    public String toString() {
        return "FollowBean{" +
                "iResult='" + iResult + '\'' +
                ", InfoId='" + InfoId + '\'' +
                ", CircleID='" + CircleID + '\'' +
                '}';
    }
}

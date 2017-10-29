package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/17.
 *
 *
 * @author wwei
 */
public class QuotaItemChildEntity implements Serializable{

    private String SpID;
    private String SpName;

    public String getSpID() {
        return SpID;
    }

    public void setSpID(String spID) {
        SpID = spID;
    }

    public String getSpName() {
        return SpName;
    }

    public void setSpName(String spName) {
        SpName = spName;
    }

    @Override
    public String toString() {
        return "QuotaItemChildEntity{" +
                "SpID='" + SpID + '\'' +
                ", SpName='" + SpName + '\'' +
                '}';
    }
}

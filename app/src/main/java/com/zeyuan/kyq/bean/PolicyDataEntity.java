package com.zeyuan.kyq.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/14.
 *
 *
 *
 * @author wwei
 */
public class PolicyDataEntity implements Serializable{

    private String PolicyName;
    private String PolicyID;

    public String getPolicyName() {
        return PolicyName;
    }

    public void setPolicyName(String policyName) {
        PolicyName = policyName;
    }

    public String getPolicyID() {
        return PolicyID;
    }

    public void setPolicyID(String policyID) {
        PolicyID = policyID;
    }

    @Override
    public String toString() {
        return "PolicyDataEntity{" +
                "PolicyName='" + PolicyName + '\'' +
                ", PolicyID='" + PolicyID + '\'' +
                '}';
    }

}

package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/24.
 *
 *
 *
 * @author wwei
 */
public class CommPolicyNewEntity implements Serializable{

    private String CommPolicyType;
    private String CommPolicyID;
    private String CommPolicyName;
    private String QueryKey;//1为阶段，2为阶段类型
    private String QueryValue;

    public String getQueryKey() {
        return QueryKey;
    }

    public void setQueryKey(String queryKey) {
        QueryKey = queryKey;
    }

    public String getQueryValue() {
        return QueryValue;
    }

    public void setQueryValue(String queryValue) {
        QueryValue = queryValue;
    }

    public String getCommPolicyType() {
        return CommPolicyType;
    }

    public void setCommPolicyType(String commPolicyType) {
        CommPolicyType = commPolicyType;
    }

    public String getCommPolicyName() {
        return CommPolicyName;
    }

    public void setCommPolicyName(String commPolicyName) {
        CommPolicyName = commPolicyName;
    }

    public String getCommPolicyID() {
        return CommPolicyID;
    }

    public void setCommPolicyID(String commPolicyID) {
        CommPolicyID = commPolicyID;
    }

    @Override
    public String toString() {
        return "CommPolicyNewEntity{" +
                "CommPolicyType='" + CommPolicyType + '\'' +
                ", CommPolicyID='" + CommPolicyID + '\'' +
                ", CommPolicyName='" + CommPolicyName + '\'' +
                ", QueryKey='" + QueryKey + '\'' +
                ", QueryValue='" + QueryValue + '\'' +
                '}';
    }
}

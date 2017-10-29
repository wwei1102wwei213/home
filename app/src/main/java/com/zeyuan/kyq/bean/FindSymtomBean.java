package com.zeyuan.kyq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-12-21
 * Time: 13:47
 * FIXME
 */


public class FindSymtomBean implements Serializable{


    /**
     * iResult : 0
     * IsHaveCancerProcess : 0
     * CommPolicy : [{"CommPolicyType":"10","CommPolicyTypeName":"放疗副作用","CommPolicyID":"9","CommPolicyName":"放疗后身体反应","WithPerform":["146","147"]}]
     */

    private String iResult;
    private String IsHaveCancerProcess;
    private String IsShareSucc;

    public String getIsShareSucc() {
        return IsShareSucc;
    }

    public void setIsShareSucc(String isShareSucc) {
        IsShareSucc = isShareSucc;
    }

    /**
     * CommPolicyType : 10
     * CommPolicyTypeName : 放疗副作用
     * CommPolicyID : 9
     * CommPolicyName : 放疗后身体反应
     * WithPerform : ["146","147"]
     */



    private List<CommPolicyEntity> CommPolicy;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setIsHaveCancerProcess(String IsHaveCancerProcess) {
        this.IsHaveCancerProcess = IsHaveCancerProcess;
    }

    public void setCommPolicy(List<CommPolicyEntity> CommPolicy) {
        this.CommPolicy = CommPolicy;
    }

    public String getIResult() {
        return iResult;
    }

    public String getIsHaveCancerProcess() {
        return IsHaveCancerProcess;
    }

    public List<CommPolicyEntity> getCommPolicy() {
        return CommPolicy;
    }

    public static class CommPolicyEntity implements Serializable{
        private String CommPolicyType;
        private String CommPolicyTypeName;
        private String CommPolicyID;
        private String CommPolicyName;
        private List<String> WithPerform;
        private int order = 0;

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public void setCommPolicyType(String CommPolicyType) {
            this.CommPolicyType = CommPolicyType;
        }

        public void setCommPolicyTypeName(String CommPolicyTypeName) {
            this.CommPolicyTypeName = CommPolicyTypeName;
        }

        public void setCommPolicyID(String CommPolicyID) {
            this.CommPolicyID = CommPolicyID;
        }

        public void setCommPolicyName(String CommPolicyName) {
            this.CommPolicyName = CommPolicyName;
        }

        public void setWithPerform(List<String> WithPerform) {
            this.WithPerform = WithPerform;
        }

        public String getCommPolicyType() {
            return CommPolicyType;
        }

        public String getCommPolicyTypeName() {
            return CommPolicyTypeName;
        }

        public String getCommPolicyID() {
            return CommPolicyID;
        }

        public String getCommPolicyName() {
            return CommPolicyName;
        }

        public List<String> getWithPerform() {
            return WithPerform;
        }
    }
}

package com.zeyuan.kyq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-10-27
 * Time: 09:56
 * FIXME
 */


public class AllStepBean implements Serializable{

    /**
     * iResult : 0
     * StepNum : [{"StepUID":"1","StepUnionID":"5001,5002,5003","IsMedicineValid":"1","BeginTime":"1361864526","EndTime":"1388043726"},{"StepUID":"2","StepUnionID":"5601","IsMedicineValid":"1","BeginTime":"1395819845","EndTime":"1403768645"},{"StepUID":"3","StepUnionID":"5001,5002","IsMedicineValid":"1","BeginTime":"1406360673","EndTime":"1416987873"}]
     */

    private String iResult;
    /**
     * StepUID : 1
     * StepUnionID : 5001,5002,5003
     * IsMedicineValid : 1
     * BeginTime : 1361864526
     * EndTime : 1388043726
     */

    private List<StepNumEntity> StepNum;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }
    public String getIResult() {
        return iResult;
    }
    public void setStepNum(List<StepNumEntity> StepNum) {
        this.StepNum = StepNum;
    }



    public List<StepNumEntity> getStepNum() {
        return StepNum;
    }

    public static class StepNumEntity {
        private String StepUID;//第几个阶段
        private String StepID;//药物的id
        private String IsMedicineValid;//是否有效
        private String BeginTime;//开始时间
        private String EndTime;//结束时间

        public void setStepUID(String StepUID) {
            this.StepUID = StepUID;
        }

        public void setStepUnionID(String StepUnionID) {
            this.StepID = StepUnionID;
        }

        public void setIsMedicineValid(String IsMedicineValid) {
            this.IsMedicineValid = IsMedicineValid;
        }

        public void setBeginTime(String BeginTime) {
            this.BeginTime = BeginTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public String getStepUID() {
            return StepUID;
        }

        public String getStepID() {
            return StepID;
        }

        public String getIsMedicineValid() {
            return IsMedicineValid;
        }

        public String getBeginTime() {
            return BeginTime;
        }

        public String getEndTime() {
            return EndTime;
        }

        @Override
        public String toString() {
            return "StepNumEntity{" +
                    "StepUID='" + StepUID + '\'' +
                    ", StepUnionID='" + StepID + '\'' +
                    ", IsMedicineValid='" + IsMedicineValid + '\'' +
                    ", BeginTime='" + BeginTime + '\'' +
                    ", EndTime='" + EndTime + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AllStepBean{" +
                "iResult='" + iResult + '\'' +
                ", StepNum=" + StepNum +
                '}';
    }
}

package com.zeyuan.kyq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/9/2.
 * 获取某个阶段详情
 */
public class StepDetailsBean extends BaseBean implements Serializable{

    /**
     * RecordNum : [{"CEA":"12","PerformORQuota":"2","TransferPos":["3001","3002","3003"],"SlaverCancerNum":[{"SlaverCancerLength":"23","SlaverCancerWidth":"43","SlaverCancerName":"副肿瘤1"},{"SlaverCancerLength":"41","SlaverCancerWidth":"33","SlaverCancerName":"副肿瘤2"}],"RecordTime":1441690384,"MasterCancerWidth":"43","MasterCancerLength":"23","MasterCancerName":"主肿瘤"},{"RecordTime":1441690384,"Remark":"Remark0","PerformORQuota":"1","PerformStep":["2001","2002","2003"]}]
     */

    private List<RecordNumEntity> RecordNum;

    public void setRecordNum(List<RecordNumEntity> RecordNum) {
        this.RecordNum = RecordNum;
    }

    public List<RecordNumEntity> getRecordNum() {
        return RecordNum;
    }

    public static class RecordNumEntity {
        /**
         * CEA : 12
         * PerformORQuota : 2
         * TransferPos : ["3001","3002","3003"]
         * SlaverCancerNum : [{"SlaverCancerLength":"23","SlaverCancerWidth":"43","SlaverCancerName":"副肿瘤1"},{"SlaverCancerLength":"41","SlaverCancerWidth":"33","SlaverCancerName":"副肿瘤2"}]
         * RecordTime : 1441690384
         * MasterCancerWidth : 43
         * MasterCancerLength : 23
         * MasterCancerName : 主肿瘤
         */

        private String CEA;
        private String PerformORQuota;
        private int RecordTime;
        private String MasterCancerWidth;
        private String MasterCancerLength;
        private String MasterCancerName;
        private List<String> TransferPos;
        private List<SlaverCancerNumEntity> SlaverCancerNum;

        public void setCEA(String CEA) {
            this.CEA = CEA;
        }

        public void setPerformORQuota(String PerformORQuota) {
            this.PerformORQuota = PerformORQuota;
        }

        public void setRecordTime(int RecordTime) {
            this.RecordTime = RecordTime;
        }

        public void setMasterCancerWidth(String MasterCancerWidth) {
            this.MasterCancerWidth = MasterCancerWidth;
        }

        public void setMasterCancerLength(String MasterCancerLength) {
            this.MasterCancerLength = MasterCancerLength;
        }

        public void setMasterCancerName(String MasterCancerName) {
            this.MasterCancerName = MasterCancerName;
        }

        public void setTransferPos(List<String> TransferPos) {
            this.TransferPos = TransferPos;
        }

        public void setSlaverCancerNum(List<SlaverCancerNumEntity> SlaverCancerNum) {
            this.SlaverCancerNum = SlaverCancerNum;
        }

        public String getCEA() {
            return CEA;
        }

        public String getPerformORQuota() {
            return PerformORQuota;
        }

        public int getRecordTime() {
            return RecordTime;
        }

        public String getMasterCancerWidth() {
            return MasterCancerWidth;
        }

        public String getMasterCancerLength() {
            return MasterCancerLength;
        }

        public String getMasterCancerName() {
            return MasterCancerName;
        }

        public List<String> getTransferPos() {
            return TransferPos;
        }

        public List<SlaverCancerNumEntity> getSlaverCancerNum() {
            return SlaverCancerNum;
        }

        public static class SlaverCancerNumEntity {
            /**
             * SlaverCancerLength : 23
             * SlaverCancerWidth : 43
             * SlaverCancerName : 副肿瘤1
             */

            private String SlaverCancerLength;
            private String SlaverCancerWidth;
            private String SlaverCancerName;

            public void setSlaverCancerLength(String SlaverCancerLength) {
                this.SlaverCancerLength = SlaverCancerLength;
            }

            public void setSlaverCancerWidth(String SlaverCancerWidth) {
                this.SlaverCancerWidth = SlaverCancerWidth;
            }

            public void setSlaverCancerName(String SlaverCancerName) {
                this.SlaverCancerName = SlaverCancerName;
            }

            public String getSlaverCancerLength() {
                return SlaverCancerLength;
            }

            public String getSlaverCancerWidth() {
                return SlaverCancerWidth;
            }

            public String getSlaverCancerName() {
                return SlaverCancerName;
            }
        }
    }


    @Override
    public String toString() {
        return "StepDetailsBean [RecordNum=" + RecordNum + "]";
    }
}

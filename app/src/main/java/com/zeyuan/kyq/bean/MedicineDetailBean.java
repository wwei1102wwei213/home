package com.zeyuan.kyq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/9/11.
 * 15.	获取药物详情
 */
public class MedicineDetailBean implements Serializable{


    /**
     * MedicineNum : [{"MedicineDescrip":"易瑞沙介绍","MedicineName":"易瑞沙","MaybeSideEffect":["副作用1","副作用2"],"AlertInfo":"警告、禁忌","StepID":"5005"},{"MedicineDescrip":"卡博替尼介绍","MedicineName":"卡博替尼","MaybeSideEffect":["副作用1","副作用2"],"AlertInfo":"警告、禁忌","StepID":"5004"}]
     * iResult : 0
     * Usage : 用法
     * SolutionDescrip : 方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述
     */

    private String iResult;
    private String Usage;
    private String SolutionDescrip;
    private List<MedicineNumEntity> MedicineNum;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setUsage(String Usage) {
        this.Usage = Usage;
    }

    public void setSolutionDescrip(String SolutionDescrip) {
        this.SolutionDescrip = SolutionDescrip;
    }

    public void setMedicineNum(List<MedicineNumEntity> MedicineNum) {
        this.MedicineNum = MedicineNum;
    }

    public String getIResult() {
        return iResult;
    }

    public String getUsage() {
        return Usage;
    }

    public String getSolutionDescrip() {
        return SolutionDescrip;
    }

    public List<MedicineNumEntity> getMedicineNum() {
        return MedicineNum;
    }

    public static class MedicineNumEntity {
        /**
         * MedicineDescrip : 易瑞沙介绍
         * MedicineName : 易瑞沙
         * MaybeSideEffect : ["副作用1","副作用2"]
         * AlertInfo : 警告、禁忌
         * StepID : 5005
         */

        private String MedicineDescrip;
        private String MedicineName;
        private String AlertInfo;
        private String StepID;
        private List<String> MaybeSideEffect;

        public void setMedicineDescrip(String MedicineDescrip) {
            this.MedicineDescrip = MedicineDescrip;
        }

        public void setMedicineName(String MedicineName) {
            this.MedicineName = MedicineName;
        }

        public void setAlertInfo(String AlertInfo) {
            this.AlertInfo = AlertInfo;
        }

        public void setStepID(String StepID) {
            this.StepID = StepID;
        }

        public void setMaybeSideEffect(List<String> MaybeSideEffect) {
            this.MaybeSideEffect = MaybeSideEffect;
        }

        public String getMedicineDescrip() {
            return MedicineDescrip;
        }

        public String getMedicineName() {
            return MedicineName;
        }

        public String getAlertInfo() {
            return AlertInfo;
        }

        public String getStepID() {
            return StepID;
        }

        public List<String> getMaybeSideEffect() {
            return MaybeSideEffect;
        }
    }
}

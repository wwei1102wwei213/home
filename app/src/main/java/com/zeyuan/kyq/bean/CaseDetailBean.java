package com.zeyuan.kyq.bean;

import com.zeyuan.kyq.Entity.CommPolicyNewEntity;
import com.zeyuan.kyq.utils.DecryptUtils;

import java.io.Serializable;
import java.util.List;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-12-25
 * Time: 11:39  方案详情的bean
 * FIXME
 */


public class CaseDetailBean implements Serializable {

    /**
     * MedicineNum : [{"EnglishName":"Iressa","StepID":"559","usage":"方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述","Brain":"弱","caution":"已知对该活性物质或该产品任一赋形剂有严重过敏反应这，不得使用该药物。","Gene":"EGFR18、EGFR19、EGFR20、EGFR21、C_MET","Suit":"本品适用于治疗既往接受过化学治疗的局部晚期或转移性非小细胞肺癌（NSCLC）。既往华夏治疗主要是指铂剂和多西紫杉醇治疗。","MaybeSideEffect":["副作用1 id","副作用2 id"],"StepName":"Gefitinib/吉非替尼/易瑞沙/Iressa","MedicineDesc":"易瑞沙介绍 19世纪60年代初期由Ozegowski和其同事在德国耶拿的微生物试验协会研制。本品的抗肿瘤和杀细胞作用主要机理为DNA单链和双联通过烷化作用交联，这打乱了DNA的功能和DNA的合成，也会使DNA和蛋白之间，以及蛋白和蛋白之间产生交联，从而发挥抗肿瘤作用。 ","CureConfID":"4205","CommonName":"易瑞沙"},{"EnglishName":"Iressa","StepID":"560","usage":"方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述","Brain":"弱","caution":"已知对该活性物质或该产品任一赋形剂有严重过敏反应这，不得使用该药物。","Gene":"EGFR18、EGFR19、EGFR20、EGFR21、C_MET","Suit":"本品适用于治疗既往接受过化学治疗的局部晚期或转移性非小细胞肺癌（NSCLC）。既往华夏治疗主要是指铂剂和多西紫杉醇治疗。","MaybeSideEffect":["副作用1 id","副作用2 id"],"StepName":"Gefitinib/吉非替尼/易瑞沙/Iressa","MedicineDesc":" 易瑞沙介绍 19世纪60年代初期由Ozegowski和其同事在德国耶拿的微生物试验协会研制。本品的抗肿瘤和杀细胞作用主要机理为DNA单链和双联通过烷化作用交联，这打乱了DNA的功能和DNA的合成，也会使DNA和蛋白之间，以及蛋白和蛋白之间产生交联，从而发挥抗肿瘤作用。","CureConfID":"4205","CommonName":"易瑞沙"}]
     * iResult : 0
     * usage : 用法
     * StepType : 2
     * remark : 方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述
     * AllDesc : 稳定后停药观察1个月稳定后，易瑞沙联合卡博替尼2周，之后易瑞沙单用一个月后联合2周卡博替尼。循环1个月内进展 则换药：易瑞沙联合卡博替尼、易瑞沙联合克唑替尼、易瑞沙联合INC280
     */
    private String stepId;//添加到计划用药的id

    private String iResult;
    private String usage;//用法
    private String StepType;//1是单药2.是多药
    private String remark;//备注
    private String AllDesc;
    private List<MedicineNumEntity> NewMaybeEffect;
    private List<MedicineNumEntity> NewMaybebingfazheng;

    @Override
    public String toString() {
        return "CaseDetailBean{" +
                "stepId='" + stepId + '\'' +
                ", iResult='" + iResult + '\'' +
                ", usage='" + usage + '\'' +
                ", StepType='" + StepType + '\'' +
                ", remark='" + remark + '\'' +
                ", AllDesc='" + AllDesc + '\'' +
                ", MedicineNum=" + MedicineNum.toString() +
                '}';
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    /**
     * EnglishName : Iressa
     * StepID : 559
     * usage : 方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述方案描述
     * Brain : 弱
     * caution : 已知对该活性物质或该产品任一赋形剂有严重过敏反应这，不得使用该药物。
     * Gene : EGFR18、EGFR19、EGFR20、EGFR21、C_MET
     * Suit : 本品适用于治疗既往接受过化学治疗的局部晚期或转移性非小细胞肺癌（NSCLC）。既往华夏治疗主要是指铂剂和多西紫杉醇治疗。
     * MaybeSideEffect : ["副作用1 id","副作用2 id"]
     * StepName : Gefitinib/吉非替尼/易瑞沙/Iressa
     * MedicineDesc : 易瑞沙介绍 19世纪60年代初期由Ozegowski和其同事在德国耶拿的微生物试验协会研制。本品的抗肿瘤和杀细胞作用主要机理为DNA单链和双联通过烷化作用交联，这打乱了DNA的功能和DNA的合成，也会使DNA和蛋白之间，以及蛋白和蛋白之间产生交联，从而发挥抗肿瘤作用。
     * CureConfID : 4205
     * CommonName : 易瑞沙
     */

    private List<MedicineNumEntity> MedicineNum;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public void setStepType(String StepType) {
        this.StepType = StepType;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setAllDesc(String AllDesc) {
        this.AllDesc = AllDesc;
    }

    public void setMedicineNum(List<MedicineNumEntity> MedicineNum) {
        this.MedicineNum = MedicineNum;
    }

    public String getIResult() {
        return iResult;
    }

    public String getUsage() {
        return DecryptUtils.decodeBase64(usage);
    }

    public String getStepType() {
        return StepType;
    }

    public String getRemark() {
        return DecryptUtils.decodeBase64(remark);
    }

    public String getAllDesc() {

            return DecryptUtils.decodeBase64(AllDesc);
    }

    public List<MedicineNumEntity> getMedicineNum() {
        return MedicineNum;
    }

    public static class MedicineNumEntity implements Serializable {
        private String EnglishName;
        private String StepID;
        private String usage;
        private String Brain;
        private String caution;
        private String Gene;
        private String Suit;
        private String StepName;
//        private String MedicineDesc;
        private String CureConfID;
        private String CommonName;
        private List<String> MaybeEffect;
        private List<CommPolicyNewEntity> NewMaybeEffect;
        private List<CommPolicyNewEntity> NewMaybebingfazheng;

        public List<CommPolicyNewEntity> getNewMaybeEffect() {
            return NewMaybeEffect;
        }

        public void setNewMaybeEffect(List<CommPolicyNewEntity> newMaybeEffect) {
            NewMaybeEffect = newMaybeEffect;
        }

        public List<CommPolicyNewEntity> getNewMaybebingfazheng() {
            return NewMaybebingfazheng;
        }

        public void setNewMaybebingfazheng(List<CommPolicyNewEntity> newMaybebingfazheng) {
            NewMaybebingfazheng = newMaybebingfazheng;
        }

        public void setEnglishName(String EnglishName) {
            this.EnglishName = EnglishName;
        }

        public void setStepID(String StepID) {
            this.StepID = StepID;
        }

        public void setUsage(String usage) {
            this.usage = usage;
        }

        public void setBrain(String Brain) {
            this.Brain = Brain;
        }

        public void setCaution(String caution) {
            this.caution = caution;
        }

        public void setGene(String Gene) {
            this.Gene = Gene;
        }

        public void setSuit(String Suit) {
            this.Suit = Suit;
        }

        public void setStepName(String StepName) {
            this.StepName = StepName;
        }

//        public void setMedicineDesc(String MedicineDesc) {
//            this.MedicineDesc = MedicineDesc;
//        }

        public void setCureConfID(String CureConfID) {
            this.CureConfID = CureConfID;
        }

        public void setCommonName(String CommonName) {
            this.CommonName = CommonName;
        }

        public void setMaybeSideEffect(List<String> MaybeSideEffect) {
            this.MaybeEffect = MaybeSideEffect;
        }

        public String getEnglishName() {
            return EnglishName;
        }

        public String getStepID() {
            return StepID;
        }

        public String getUsage() {
            return DecryptUtils.decodeBase64(usage);
        }

        public String getBrain() {
            return Brain;
        }

        public String getCaution() {
            return DecryptUtils.decodeBase64(caution);
        }

        public String getGene() {
            return Gene;
        }

        public String getSuit() {
            return DecryptUtils.decodeBase64(Suit);
        }

        public String getStepName() {
            return StepName;
        }

//        public String getMedicineDesc() {
//            return MedicineDesc;
//        }

        public String getCureConfID() {
            return CureConfID;
        }

        public String getCommonName() {
            return CommonName;
        }

        public List<String> getMaybeSideEffect() {
            return MaybeEffect;
        }

        @Override
        public String toString() {
            return "MedicineNumEntity{" +
                    "EnglishName='" + EnglishName + '\'' +
                    ", StepID='" + StepID + '\'' +
                    ", usage='" + usage + '\'' +
                    ", Brain='" + Brain + '\'' +
                    ", caution='" + caution + '\'' +
                    ", Gene='" + Gene + '\'' +
                    ", Suit='" + Suit + '\'' +
                    ", StepName='" + StepName + '\'' +
                    ", CureConfID='" + CureConfID + '\'' +
                    ", CommonName='" + CommonName + '\'' +
                    ", MaybeEffect=" + MaybeEffect +
                    ", NewMaybeEffect=" + NewMaybeEffect +
                    ", NewMaybebingfazheng=" + NewMaybebingfazheng +
                    '}';
        }
    }
}

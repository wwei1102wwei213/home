package com.zeyuan.kyq.Entity;

import com.zeyuan.kyq.bean.PolicyDataEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/6/14.
 *
 * 查并发症和副作用的封装类
 *
 * @author wwei
 */
public class SearchPolicyEntity implements Serializable{

    private String iResult;
    private List<StepItemEntity> StepEffect;
    private CureConfEffectEntity CureConfEffect;
    private CancerEffectEntity CancerEffect;
    private OtherEffectEntity OtherEffect;

    public static class CureConfEffectEntity implements Serializable{

        private String CureConfID;
        private String CureConfName;
        private List<PolicyDataEntity> Data;

        public String getCureConfID() {
            return CureConfID;
        }

        public void setCureConfID(String cureConfID) {
            CureConfID = cureConfID;
        }

        public String getCureConfName() {
            return CureConfName;
        }

        public void setCureConfName(String cureConfName) {
            CureConfName = cureConfName;
        }

        public List<PolicyDataEntity> getData() {
            return Data;
        }

        public void setData(List<PolicyDataEntity> data) {
            Data = data;
        }

        @Override
        public String toString() {
            return "CureConfEffectEntity{" +
                    "CureConfID='" + CureConfID + '\'' +
                    ", CureConfName='" + CureConfName + '\'' +
                    ", Data=" + Data +
                    '}';
        }


    }

    public static class CancerEffectEntity implements Serializable{

        private String CancerID;
        private String CancerName;
        private List<PolicyDataEntity> Data;

        public String getCancerID() {
            return CancerID;
        }

        public void setCancerID(String cancerID) {
            CancerID = cancerID;
        }

        public String getCancerName() {
            return CancerName;
        }

        public void setCancerName(String cancerName) {
            CancerName = cancerName;
        }

        public List<PolicyDataEntity> getData() {
            return Data;
        }

        public void setData(List<PolicyDataEntity> data) {
            Data = data;
        }

        @Override
        public String toString() {
            return "CancerEffectEntity{" +
                    "CancerID='" + CancerID + '\'' +
                    ", CancerName='" + CancerName + '\'' +
                    ", Data=" + Data +
                    '}';
        }


    }

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public List<StepItemEntity> getStepEffect() {
        return StepEffect;
    }

    public void setStepEffect(List<StepItemEntity> stepEffect) {
        StepEffect = stepEffect;
    }

    public CureConfEffectEntity getCureConfEffect() {
        return CureConfEffect;
    }

    public void setCureConfEffect(CureConfEffectEntity cureConfEffect) {
        CureConfEffect = cureConfEffect;
    }

    public CancerEffectEntity getCancerEffect() {
        return CancerEffect;
    }

    public void setCancerEffect(CancerEffectEntity cancerEffect) {
        CancerEffect = cancerEffect;
    }

    public OtherEffectEntity getOtherEffect() {
        return OtherEffect;
    }

    public void setOtherEffect(OtherEffectEntity otherEffect) {
        OtherEffect = otherEffect;
    }

    @Override
    public String toString() {
        return "SearchPolicyEntity{" +
                "iResult='" + iResult + '\'' +
                ", StepEffect=" + StepEffect +
                ", CureConfEffect=" + CureConfEffect +
                ", CancerEffect=" + CancerEffect +
                ", OtherEffect=" + OtherEffect +
                '}';
    }
}

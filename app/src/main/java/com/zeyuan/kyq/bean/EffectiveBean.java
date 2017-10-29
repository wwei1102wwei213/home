package com.zeyuan.kyq.bean;

/**
 * Created by Administrator on 2017/8/30.
 * 药物有效性的bean
 */

public class EffectiveBean {
    private String iResult;
    private String AllNum;
    private String UnknownNum;
    private String InvalidNum;
    private String EffectiveNum;

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public String getAllNum() {
        return AllNum;
    }

    public void setAllNum(String allNum) {
        AllNum = allNum;
    }

    public String getUnknownNum() {
        return UnknownNum;
    }

    public void setUnknownNum(String unknownNum) {
        UnknownNum = unknownNum;
    }

    public String getInvalidNum() {
        return InvalidNum;
    }

    public void setInvalidNum(String invalidNum) {
        InvalidNum = invalidNum;
    }

    public String getEffectiveNum() {
        return EffectiveNum;
    }

    public void setEffectiveNum(String effectiveNum) {
        EffectiveNum = effectiveNum;
    }
}

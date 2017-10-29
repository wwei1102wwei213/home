package com.zeyuan.kyq.bean;

import com.zeyuan.kyq.Entity.Advertising;
import com.zeyuan.kyq.Entity.InformationEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */
public class StepSummaryBean {


    /**
     * iResult : 0
     *
     */

    private String iResult;
    private int EffectNum;
    private String  EffectStr;
    private int ComplicationNum;
    private String ComplicationStr;
    private String sumcount;

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public int getEffectNum() {
        return EffectNum;
    }

    public void setEffectNum(int effectNum) {
        EffectNum = effectNum;
    }

    public String getEffectStr() {
        return EffectStr;
    }

    public void setEffectStr(String effectStr) {
        EffectStr = effectStr;
    }

    public int getComplicationNum() {
        return ComplicationNum;
    }

    public void setComplicationNum(int complicationNum) {
        ComplicationNum = complicationNum;
    }

    public String getComplicationStr() {
        return ComplicationStr;
    }

    public void setComplicationStr(String complicationStr) {
        ComplicationStr = complicationStr;
    }

    public String getSumcount() {
        return sumcount;
    }

    public void setSumcount(String sumcount) {
        this.sumcount = sumcount;
    }
//    private List<DataEntity> data;
    private Advertising advertising_2;
    private Advertising advertising_3;

    public Advertising getAdvertising_2() {
        return advertising_2;
    }

    public void setAdvertising_2(Advertising advertising_2) {
        this.advertising_2 = advertising_2;
    }

    public Advertising getAdvertising_3() {
        return advertising_3;
    }

    public void setAdvertising_3(Advertising advertising_3) {
        this.advertising_3 = advertising_3;
    }

    private List<InformationEntity> data;


    public String getIResult() {
        return iResult;
    }

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public List<InformationEntity> getData() {
        return data;
    }

    public void setData(List<InformationEntity> data) {
        this.data = data;
    }

    private List<Advertising>advertising_1;

    public List<Advertising> getAdvertising_1() {
        return advertising_1;
    }

    public void setAdvertising_1(List<Advertising> advertising_1) {
        this.advertising_1 = advertising_1;
    }
}

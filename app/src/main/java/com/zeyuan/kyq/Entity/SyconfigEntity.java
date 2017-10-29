package com.zeyuan.kyq.Entity;

import com.zeyuan.kyq.utils.DecryptUtils;

/**
 * Created by Administrator on 2016/3/16.
 * config数据封装
 *
 * @author wwei
 */
public class SyconfigEntity{

    private int id;

    private String config;
    /*城市*/
    private String city;
    /*分期*/
    private String digitdata;
    private String digitcancer;
    private String digintnm;
    private String tnmdigin;
    /*癌肿*/
    private String cancer;
    /*cuer*/
    private String cancer2step;
    /*身体部位*/
    private String bodypos;
    /*圈子*/
    private String circle;
    /*基因*/
    private String gene;
    /*症状*/
    private String perform;
    /*联合用药*/
    private String union;
    /*转移部位*/
    private String transferpos;

    private String otherstep;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDigitdata() {
        return digitdata;
    }

    public void setDigitdata(String digitdata) {
        this.digitdata = digitdata;
    }

    public String getCancer() {
        return cancer;
    }

    public void setCancer(String cancer) {
        this.cancer = cancer;
    }

    public String getBodypos() {
        return bodypos;
    }

    public void setBodypos(String bodypos) {
        this.bodypos = bodypos;
    }

    public String getCancer2step() {
        return cancer2step;
    }

    public void setCancer2step(String cancer2step) {
        this.cancer2step = cancer2step;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getGene() {
        return gene;
    }

    public void setGene(String gene) {
        this.gene = gene;
    }

    public String getPerform() {
        return perform;
    }

    public void setPerform(String perform) {
        this.perform = perform;
    }

    public String getUnion() {
        return union;
    }

    public void setUnion(String union) {
        this.union = union;
    }

    public String getTransferpos() {
        return transferpos;
    }

    public void setTransferpos(String transferpos) {
        this.transferpos = transferpos;
    }

    public String getOtherstep() {
        return otherstep;
    }

    public void setOtherstep(String otherstep) {
        this.otherstep = otherstep;
    }

    public String getDigitcancer() {
        return digitcancer;
    }

    public void setDigitcancer(String digitcancer) {
        this.digitcancer = digitcancer;
    }

    public String getTnmdigin() {
        return tnmdigin;
    }

    public void setTnmdigin(String tnmdigin) {
        this.tnmdigin = tnmdigin;
    }

    public String getDigintnm() {
        return DecryptUtils.decodeBase64(digintnm);
    }

    public void setDigintnm(String digintnm) {
        this.digintnm = digintnm;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}

package com.zeyuan.kyq.bean;

import java.io.Serializable;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-11-06
 * Time: 13:54
 * FIXME
 * 登录之后自己服务器返回的数据
 */


public class UserInfoQQBean implements Serializable{

    /**
     * iResult : 0
     * InfoID : 130
     * OpenID : 4FF54AECF1A7458321CA337681CBFBB4
     */


    /**
     * ret : 0
     * msg :
     * nickname : Peter
     * figureurl : http://qzapp.qlogo.cn/qzapp/111111/942FEA70050EEAFBD4DCE2C1FC775E56/30
     * figureurl_1 : http://qzapp.qlogo.cn/qzapp/111111/942FEA70050EEAFBD4DCE2C1FC775E56/50
     * figureurl_2 : http://qzapp.qlogo.cn/qzapp/111111/942FEA70050EEAFBD4DCE2C1FC775E56/100
     * figureurl_qq_1 : http://q.qlogo.cn/qqapp/100312990/DE1931D5330620DBD07FB4A5422917B6/40
     * figureurl_qq_2 : http://q.qlogo.cn/qqapp/100312990/DE1931D5330620DBD07FB4A5422917B6/100
     * gender : 男
     * is_yellow_vip : 1
     * vip : 1
     * yellow_vip_level : 7
     * level : 7
     * is_yellow_year_vip : 1
     */

    private int ret;
    private String msg;
    private String nickname;
    private String figureurl;
    private String figureurl_1;
    private String figureurl_2;
    private String figureurl_qq_1;
    private String figureurl_qq_2;
    private String gender;
    private String is_yellow_vip;
    private String vip;
    private String yellow_vip_level;
    private String level;
    private String is_yellow_year_vip;

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    private String openid;
    public String getOpenid() {
        return openid;
    }
    private String access_token;
    public String getAccess_token() {
        return access_token;
    }
    @Override
    public String toString() {
        return " {" +
                ", ret=" + ret +
                ", msg='" + msg + '\'' +
                ", nickname='" + nickname + '\'' +
                ", figureurl='" + figureurl + '\'' +
                ", figureurl_1='" + figureurl_1 + '\'' +
                ", figureurl_2='" + figureurl_2 + '\'' +
                ", figureurl_qq_1='" + figureurl_qq_1 + '\'' +
                ", figureurl_qq_2='" + figureurl_qq_2 + '\'' +
                ", gender='" + gender + '\'' +
                ", is_yellow_vip='" + is_yellow_vip + '\'' +
                ", vip='" + vip + '\'' +
                ", yellow_vip_level='" + yellow_vip_level + '\'' +
                ", level='" + level + '\'' +
                ", is_yellow_year_vip='" + is_yellow_year_vip + '\'' +
                '}';
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setFigureurl(String figureurl) {
        this.figureurl = figureurl;
    }

    public void setFigureurl_1(String figureurl_1) {
        this.figureurl_1 = figureurl_1;
    }

    public void setFigureurl_2(String figureurl_2) {
        this.figureurl_2 = figureurl_2;
    }

    public void setFigureurl_qq_1(String figureurl_qq_1) {
        this.figureurl_qq_1 = figureurl_qq_1;
    }

    public void setFigureurl_qq_2(String figureurl_qq_2) {
        this.figureurl_qq_2 = figureurl_qq_2;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setIs_yellow_vip(String is_yellow_vip) {
        this.is_yellow_vip = is_yellow_vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public void setYellow_vip_level(String yellow_vip_level) {
        this.yellow_vip_level = yellow_vip_level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setIs_yellow_year_vip(String is_yellow_year_vip) {
        this.is_yellow_year_vip = is_yellow_year_vip;
    }

    public int getRet() {
        return ret;
    }

    public String getMsg() {
        return msg;
    }

    public String getNickname() {
        return nickname;
    }

    public String getFigureurl() {
        return figureurl;
    }

    public String getFigureurl_1() {
        return figureurl_1;
    }

    public String getFigureurl_2() {
        return figureurl_2;
    }

    public String getFigureurl_qq_1() {
        return figureurl_qq_1;
    }

    public String getFigureurl_qq_2() {
        return figureurl_qq_2;
    }

    public String getGender() {
        return gender;
    }

    public String getIs_yellow_vip() {
        return is_yellow_vip;
    }

    public String getVip() {
        return vip;
    }

    public String getYellow_vip_level() {
        return yellow_vip_level;
    }

    public String getLevel() {
        return level;
    }

    public String getIs_yellow_year_vip() {
        return is_yellow_year_vip;
    }
}

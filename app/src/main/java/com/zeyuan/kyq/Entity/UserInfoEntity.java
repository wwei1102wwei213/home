package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/9.
 *
 *
 *
 * @author wwei
 */
public class UserInfoEntity implements Serializable{

    private String mills;
    private String InfoID;
    private String InfoName;
    private String DeviceID;
    private String UMengToken;
    private String Ver;
    private String LoginType;
    private String HeadImgUrl;
    private String OpenID;
    private String UnionID;
    private String Extras;
    private String PhoneType;

    public String getMills() {
        return mills;
    }

    public void setMills(String mills) {
        this.mills = mills;
    }

    public String getInfoID() {
        return InfoID;
    }

    public void setInfoID(String infoID) {
        InfoID = infoID;
    }

    public String getInfoName() {
        return InfoName;
    }

    public void setInfoName(String infoName) {
        InfoName = infoName;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getUMengToken() {
        return UMengToken;
    }

    public void setUMengToken(String UMengToken) {
        this.UMengToken = UMengToken;
    }

    public String getVer() {
        return Ver;
    }

    public void setVer(String ver) {
        Ver = ver;
    }

    public String getLoginType() {
        return LoginType;
    }

    public void setLoginType(String loginType) {
        LoginType = loginType;
    }

    public String getHeadImgUrl() {
        return HeadImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        HeadImgUrl = headImgUrl;
    }

    public String getOpenID() {
        return OpenID;
    }

    public void setOpenID(String openID) {
        OpenID = openID;
    }

    public String getUnionID() {
        return UnionID;
    }

    public void setUnionID(String unionID) {
        UnionID = unionID;
    }

    public String getExtras() {
        return Extras;
    }

    public void setExtras(String extras) {
        Extras = extras;
    }

    public String getPhoneType() {
        return PhoneType;
    }

    public void setPhoneType(String phoneType) {
        PhoneType = phoneType;
    }

    @Override
    public String toString() {
        return " {\"mills\":\"" + mills + "\", \"InfoID\":\"" + InfoID
                + "\", \"InfoName\":\"" + InfoName + "\", \"DeviceID\":\""
                + DeviceID + "\", \"UMengToken\":\"" + UMengToken
                + "\", \"Ver\":\"" + Ver + "\", \"LoginType\":\"" + LoginType
                + "\", \"HeadImgUrl\":\"" + HeadImgUrl + "\", \"OpenID\":\""
                + OpenID + "\", \"UnionID\":\"" + UnionID + "\", \"Extras\":\""
                + Extras + "\", \"PhoneType\":\"" + PhoneType + "\"}";
    }
}

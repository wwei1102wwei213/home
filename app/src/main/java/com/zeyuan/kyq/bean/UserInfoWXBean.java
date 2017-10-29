package com.zeyuan.kyq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-11-04
 * Time: 19:56
 * FIXME
 * 请求微信登录返回的数据
 */


public class UserInfoWXBean implements Serializable{

    /**
     * access_token : OezXcEiiBSKSxW0eoylIeBWeHIYBoBoqxDEKxa0Zd4CrkGe_KTdJb0jJ-vVywg_MXiSNxlySzyL8yddkWKQ4pUlK2rCWntKq1-ymOIDaZsAEmTwGjQx30U0dfXDbmP2_JQPP0wueKO94lON_eTTZYA
     * expires_in : 7200
     * refresh_token : OezXcEiiBSKSxW0eoylIeBWeHIYBoBoqxDEKxa0Zd4CrkGe_KTdJb0jJ-vVywg_MynSOMG9oJdoRh1MKPejxuHyycEpFANUE-zbOeNshk6WjeUKPN1f0wemXjLALgvq95G1cJQIrDQioXRY7WMefAQ
     * openid : oal1Vs7SILqRzm1iswE82BkR2mh4
     * scope : snsapi_userinfo
     * unionid : oAObtv-EmB_qjTBQI-rGIlo7vx60
     */
    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;
    /**
     * nickname : NICKNAME
     * sex : 1
     * province : PROVINCE
     * city : CITY
     * country : COUNTRY
     * headimgurl : http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0
     * privilege : ["PRIVILEGE1","PRIVILEGE2"]
     */

    private String nickname;
    private int sex;
    private String province;
    private String city;
    private String country;
    private String headimgurl;
    private List<String> privilege;

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public String getScope() {
        return scope;
    }

    public String getUnionid() {
        return unionid;
    }


    @Override
    public String toString() {
        return " {" +
                "access_token='" + access_token + '\'' +
                ", expires_in=" + expires_in +
                ", refresh_token='" + refresh_token + '\'' +
                ", openid='" + openid + '\'' +
                ", scope='" + scope + '\'' +
                ", unionid='" + unionid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", privilege=" + privilege +
                '}';
    }

    //ggz+
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public void setPrivilege(List<String> privilege) {
        this.privilege = privilege;
    }

    public String getNickname() {
        return nickname;
    }

    public int getSex() {
        return sex;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public List<String> getPrivilege() {
        return privilege;
    }
    //
}

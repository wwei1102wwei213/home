package com.zeyuan.kyq.bean;

import java.io.Serializable;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-11-06
 * Time: 12:18
 * FIXME
 */


public class LoginQQBean implements Serializable{

    /**
     * ret : 0
     * pay_token : 4EE82704824FE627BC6E45D59A3940D2
     * pf : desktop_m_qq-10000144-android-2002-
     * query_authority_cost : 189
     * authority_cost : -104816397
     * openid : 4FF54AECF1A7458321CA337681CBFBB4
     * expires_in : 7776000
     * pfkey : 49f3fef98ae4881adf452a68b12d80c7
     * msg :
     * access_token : A8647E5090F4DE79FE7EE8AA4E7D8854
     * login_cost : 699
     */

    private int ret;
    private String pay_token;
    private String pf;
    private int query_authority_cost;
    private int authority_cost;
    private String openid;
    private int expires_in;
    private String pfkey;
    private String msg;
    private String access_token;
    private int login_cost;


    public void setRet(int ret) {
        this.ret = ret;
    }

    public void setPay_token(String pay_token) {
        this.pay_token = pay_token;
    }

    public void setPf(String pf) {
        this.pf = pf;
    }

    public void setQuery_authority_cost(int query_authority_cost) {
        this.query_authority_cost = query_authority_cost;
    }

    public void setAuthority_cost(int authority_cost) {
        this.authority_cost = authority_cost;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public void setPfkey(String pfkey) {
        this.pfkey = pfkey;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setLogin_cost(int login_cost) {
        this.login_cost = login_cost;
    }

    public int getRet() {
        return ret;
    }

    public String getPay_token() {
        return pay_token;
    }

    public String getPf() {
        return pf;
    }

    public int getQuery_authority_cost() {
        return query_authority_cost;
    }

    public int getAuthority_cost() {
        return authority_cost;
    }

    public String getOpenid() {
        return openid;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public String getPfkey() {
        return pfkey;
    }

    public String getMsg() {
        return msg;
    }

    public String getAccess_token() {
        return access_token;
    }

    public int getLogin_cost() {
        return login_cost;
    }

    @Override
    public String toString() {
        return " {" +
                "ret=" + ret +
                ", pay_token='" + pay_token + '\'' +
                ", pf='" + pf + '\'' +
                ", query_authority_cost=" + query_authority_cost +
                ", authority_cost=" + authority_cost +
                ", openid='" + openid + '\'' +
                ", expires_in=" + expires_in +
                ", pfkey='" + pfkey + '\'' +
                ", msg='" + msg + '\'' +
                ", access_token='" + access_token + '\'' +
                ", login_cost=" + login_cost +
                '}';
    }
}

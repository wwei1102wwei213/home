package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/11.
 *
 * @author wwei
 */
public class PayWxEntity implements Serializable{

    private String iResult;
    private String prepayid;
    private String noncestr;
    private String timestamp;
    private String sign;
    private String pay;

    public String getiResult() {
        return iResult;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSign() {
        return sign;
    }

    public String getPay() {
        return pay;
    }

    @Override
    public String toString() {
        return "PayWxEntity{" +
                "iResult='" + iResult + '\'' +
                ", prepayid='" + prepayid + '\'' +
                ", noncestr='" + noncestr + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", sign='" + sign + '\'' +
                ", pay='" + pay + '\'' +
                '}';
    }
}

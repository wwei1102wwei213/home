package com.zeyuan.kyq.bean;

import com.zeyuan.kyq.Entity.YouzanLoginEntity;

/**
 * Created by Administrator on 2017/6/26.
 * 有赞登录的Bean
 * {
 "msg" : "登录成功",
 "data" : {
 "cookie_value" : "e74c10db3b70d32204922f9f4e",
 "cookie_key" : "open_cookie_c2a7b9269fd095fa5b1467769433688",
 "access_token" : "cddc46b2fcf73739ad0caacfaebf4561"
 },
 "code" : 0
 }
 */

public class YouzanLoginBean {
    private String msg;
    private int code;
    private YouzanLoginEntity data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public YouzanLoginEntity getData() {
        return data;
    }

    public void setData(YouzanLoginEntity data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "YouzanLoginBean{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}

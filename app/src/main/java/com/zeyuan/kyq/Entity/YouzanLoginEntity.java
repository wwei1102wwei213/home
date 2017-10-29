package com.zeyuan.kyq.Entity;

/**
 * Created by Administrator on 2017/6/26.
 * {
 * "cookie_value" : "e74c10db3b70d32204922f9f4e",
 * "cookie_key" : "open_cookie_c2a7b9269fd095fa5b1467769433688",
 * "access_token" : "cddc46b2fcf73739ad0caacfaebf4561"
 * }
 */

public class YouzanLoginEntity {
    private String cookie_value;
    private String cookie_key;
    private String access_token;

    public String getCookie_value() {
        return cookie_value;
    }

    public void setCookie_value(String cookie_value) {
        this.cookie_value = cookie_value;
    }

    public String getCookie_key() {
        return cookie_key;
    }

    public void setCookie_key(String cookie_key) {
        this.cookie_key = cookie_key;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    @Override
    public String toString() {
        return "YouzanLoginEntity{" +
                "cookie_value='" + cookie_value + '\'' +
                ", cookie_key='" + cookie_key + '\'' +
                ", access_token='" + access_token + '\'' +
                '}';
    }
}

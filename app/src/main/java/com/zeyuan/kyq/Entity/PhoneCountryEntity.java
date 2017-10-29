package com.zeyuan.kyq.Entity;

/**
 * Created by huyongbiao on 2017/6/26.
 * 号码所属的国家或地区
 */

public class PhoneCountryEntity {
    private String initialLetter;//首字母
    private String name;//国家或地区的名字
    private String code;//国家或地区的代码

    public String getInitialLetter() {
        return initialLetter;
    }

    public void setInitialLetter(String initialLetter) {
        this.initialLetter = initialLetter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

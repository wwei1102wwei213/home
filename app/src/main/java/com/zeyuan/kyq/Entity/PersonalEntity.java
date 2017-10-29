package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/22.
 *
 * @author wwei
 */
public class PersonalEntity implements Serializable{

    private String headUrl;
    private String InfoName;
    private String City;
    private String ProvinceID;
    private int UserSex;
    private int UserAge;
    private int IsSelf;

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getInfoName() {
        return InfoName;
    }

    public void setInfoName(String infoName) {
        InfoName = infoName;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getProvinceID() {
        return ProvinceID;
    }

    public void setProvinceID(String provinceID) {
        ProvinceID = provinceID;
    }

    public int getUserSex() {
        return UserSex;
    }

    public void setUserSex(int userSex) {
        UserSex = userSex;
    }

    public int getUserAge() {
        return UserAge;
    }

    public void setUserAge(int userAge) {
        UserAge = userAge;
    }

    public int getIsSelf() {
        return IsSelf;
    }

    public void setIsSelf(int isSelf) {
        IsSelf = isSelf;
    }

    @Override
    public String toString() {
        return "PersonalEntity{" +
                "headUrl='" + headUrl + '\'' +
                ", InfoName='" + InfoName + '\'' +
                ", City='" + City + '\'' +
                ", ProvinceID='" + ProvinceID + '\'' +
                ", UserSex=" + UserSex +
                ", UserAge=" + UserAge +
                ", IsSelf=" + IsSelf +
                '}';
    }

}

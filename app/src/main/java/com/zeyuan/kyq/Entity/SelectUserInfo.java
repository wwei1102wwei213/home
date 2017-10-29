package com.zeyuan.kyq.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/5/31.
 * 选择@的好友
 */

public class SelectUserInfo implements Parcelable {
    private String InfoID;
    private String InfoName;
    private String HeadImgUrl;
    private boolean isSelected;
    private String initialLetter;

    public String getInitialLetter() {
        return initialLetter;
    }

    public void setInitialLetter(String initialLetter) {
        this.initialLetter = initialLetter;
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

    public String getHeadImgUrl() {
        return HeadImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        HeadImgUrl = headImgUrl;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "SelectUserInfo{" +
                "InfoID='" + InfoID + '\'' +
                ", InfoName='" + InfoName + '\'' +
                ", HeadImgUrl='" + HeadImgUrl + '\'' +
                ", isSelected=" + isSelected +
                ", initialLetter='" + initialLetter + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.InfoID);
        dest.writeString(this.InfoName);
        dest.writeString(this.HeadImgUrl);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeString(this.initialLetter);
    }

    public SelectUserInfo() {
    }

    protected SelectUserInfo(Parcel in) {
        this.InfoID = in.readString();
        this.InfoName = in.readString();
        this.HeadImgUrl = in.readString();
        this.isSelected = in.readByte() != 0;
        this.initialLetter = in.readString();
    }

    public static final Creator<SelectUserInfo> CREATOR = new Creator<SelectUserInfo>() {
        @Override
        public SelectUserInfo createFromParcel(Parcel source) {
            return new SelectUserInfo(source);
        }

        @Override
        public SelectUserInfo[] newArray(int size) {
            return new SelectUserInfo[size];
        }
    };
}

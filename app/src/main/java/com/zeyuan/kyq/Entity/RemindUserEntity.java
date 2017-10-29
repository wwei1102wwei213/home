package com.zeyuan.kyq.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/12.
 *
 * @ 到的圈友
 */

public class RemindUserEntity implements Parcelable {
    private String InfoId;
    private String InfoName;
    private String oss_request_url;

    public String getInfoId() {
        return InfoId;
    }

    public void setInfoId(String infoId) {
        InfoId = infoId;
    }

    public String getInfoName() {
        return InfoName;
    }

    public void setInfoName(String infoName) {
        InfoName = infoName;
    }

    public String getOss_request_url() {
        return oss_request_url;
    }

    public void setOss_request_url(String oss_request_url) {
        this.oss_request_url = oss_request_url;
    }

    @Override
    public String toString() {
        return "RemindUserEntity{" +
                "InfoId='" + InfoId + '\'' +
                ", InfoName='" + InfoName + '\'' +
                ", oss_request_url='" + oss_request_url + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.InfoId);
        dest.writeString(this.InfoName);
        dest.writeString(this.oss_request_url);
    }

    public RemindUserEntity() {
    }

    protected RemindUserEntity(Parcel in) {
        this.InfoId = in.readString();
        this.InfoName = in.readString();
        this.oss_request_url = in.readString();
    }

    public static final Creator<RemindUserEntity> CREATOR = new Creator<RemindUserEntity>() {
        @Override
        public RemindUserEntity createFromParcel(Parcel source) {
            return new RemindUserEntity(source);
        }

        @Override
        public RemindUserEntity[] newArray(int size) {
            return new RemindUserEntity[size];
        }
    };
}

package com.zeyuan.kyq.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/11/3.
 *
 * @author wwei
 */
public class CareFollowEntity implements Parcelable {


    private String HeadUrl;
    private String InfoName;
    private int InfoID;
    private int DiscoverTime;
    private int careStatus;
    private int care;
    private String groupType;
    private String AutoTxt;
    private String wish;
    private String CancerName;
    private List<String> CureConf;
    private boolean isSelected;//是否被选中
    private String initialLetter;//首字母

    public String getHeadUrl() {
        return HeadUrl;
    }

    public void setHeadUrl(String headUrl) {
        HeadUrl = headUrl;
    }

    public String getInfoName() {
        return InfoName;
    }

    public void setInfoName(String infoName) {
        InfoName = infoName;

    }

    public int getInfoID() {
        return InfoID;
    }

    public void setInfoID(int infoID) {
        InfoID = infoID;
    }

    public int getDiscoverTime() {
        return DiscoverTime;
    }

    public void setDiscoverTime(int discoverTime) {
        DiscoverTime = discoverTime;
    }

    public int getCareStatus() {
        return careStatus;
    }

    public void setCareStatus(int carestatus) {
        this.careStatus = carestatus;
    }

    public int getCare() {
        return care;
    }

    public void setCare(int care) {
        this.care = care;
    }

    public String getCancerName() {
        return CancerName;
    }

    public void setCancerName(String cancerName) {
        CancerName = cancerName;
    }

    public List<String> getCureConf() {
        return CureConf;
    }

    public void setCureConf(List<String> cureConf) {
        CureConf = cureConf;
    }

    public String getWish() {
        return wish;
    }

    public void setWish(String wish) {
        this.wish = wish;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getAutoTxt() {
        return AutoTxt;
    }

    public void setAutoTxt(String autoTxt) {
        AutoTxt = autoTxt;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getInitialLetter() {
        return initialLetter == null ? "#" : initialLetter;
    }

    public void setInitialLetter(String initialLetter) {
        this.initialLetter = initialLetter;
    }

    @Override
    public String toString() {
        return "CareFollowEntity{" +
                "HeadUrl='" + HeadUrl + '\'' +
                ", InfoName='" + InfoName + '\'' +
                ", InfoID=" + InfoID +
                ", DiscoverTime=" + DiscoverTime +
                ", careStatus=" + careStatus +
                ", care=" + care +
                ", groupType='" + groupType + '\'' +
                ", AutoTxt='" + AutoTxt + '\'' +
                ", wish='" + wish + '\'' +
                ", CancerName='" + CancerName + '\'' +
                ", CureConf=" + CureConf +
                '}';
    }

    public CareFollowEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.HeadUrl);
        dest.writeString(this.InfoName);
        dest.writeInt(this.InfoID);
        dest.writeInt(this.DiscoverTime);
        dest.writeInt(this.careStatus);
        dest.writeInt(this.care);
        dest.writeString(this.groupType);
        dest.writeString(this.AutoTxt);
        dest.writeString(this.wish);
        dest.writeString(this.CancerName);
        dest.writeStringList(this.CureConf);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeString(this.initialLetter);
    }

    protected CareFollowEntity(Parcel in) {
        this.HeadUrl = in.readString();
        this.InfoName = in.readString();
        this.InfoID = in.readInt();
        this.DiscoverTime = in.readInt();
        this.careStatus = in.readInt();
        this.care = in.readInt();
        this.groupType = in.readString();
        this.AutoTxt = in.readString();
        this.wish = in.readString();
        this.CancerName = in.readString();
        this.CureConf = in.createStringArrayList();
        this.isSelected = in.readByte() != 0;
        this.initialLetter = in.readString();
    }

    public static final Creator<CareFollowEntity> CREATOR = new Creator<CareFollowEntity>() {
        @Override
        public CareFollowEntity createFromParcel(Parcel source) {
            return new CareFollowEntity(source);
        }

        @Override
        public CareFollowEntity[] newArray(int size) {
            return new CareFollowEntity[size];
        }
    };
}

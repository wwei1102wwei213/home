package com.zeyuan.kyq.Entity;

import android.text.TextUtils;

import com.zeyuan.kyq.utils.DecryptUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 *
 *
 * @author wwei
 */
public class RecordItemEntity implements Serializable{

    private int Type;
    private String TransferBody;
    private String perform;
    private String GeneID;
    private int ID;
    private long RecordTime;
    private int is_show;
    private String remark;
    private String doctor;
    private String hospital;
    private List<String> pic;
    private List<CancerSizeItemEntity> TumourInfo;
    private List<CancerSizeItemEntity> CancerMark;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public long getRecordTime() {
        return RecordTime;
    }

    public void setRecordTime(long recordTime) {
        RecordTime = recordTime;
    }

    public int getIs_show() {
        return is_show;
    }

    public void setIs_show(int is_show) {
        this.is_show = is_show;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getRemark() {
        if (TextUtils.isEmpty(remark)) remark = "";
        return DecryptUtils.URLAnddecodeBase64(remark);
    }

    public void setRemark(String remark) {
        if (TextUtils.isEmpty(remark)){
            this.remark = "";
        }else {
            this.remark = DecryptUtils.encodeAndURL(remark);
        }
    }

    public List<String> getPic() {
        return pic;
    }

    public void setPic(List<String> pic) {
        this.pic = pic;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getTransferBody() {
        return TransferBody;
    }

    public void setTransferBody(String transferBody) {
        TransferBody = transferBody;
    }

    public String getPerform() {
        return perform;
    }

    public void setPerform(String perform) {
        this.perform = perform;
    }

    public String getGeneID() {
        return GeneID;
    }

    public void setGeneID(String geneID) {
        GeneID = geneID;
    }

    public List<CancerSizeItemEntity> getTumourInfo() {
        return TumourInfo;
    }

    public void setTumourInfo(List<CancerSizeItemEntity> tumourInfo) {
        TumourInfo = tumourInfo;
    }

    public List<CancerSizeItemEntity> getCancerMark() {
        return CancerMark;
    }

    public void setCancerMark(List<CancerSizeItemEntity> cancerMark) {
        CancerMark = cancerMark;
    }


    @Override
    public String toString() {
        return "RecordItemEntity{" +
                "Type=" + Type +
                ", TransferBody='" + TransferBody + '\'' +
                ", perform='" + perform + '\'' +
                ", GeneID='" + GeneID + '\'' +
                ", ID=" + ID +
                ", RecordTime=" + RecordTime +
                ", is_show=" + is_show +
                ", remark='" + remark + '\'' +
                ", doctor='" + doctor + '\'' +
                ", hospital='" + hospital + '\'' +
                ", pic=" + pic +
                ", TumourInfo=" + TumourInfo +
                ", CancerMark=" + CancerMark +
                '}';
    }
}

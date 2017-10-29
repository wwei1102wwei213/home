package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/17.
 *
 * @author wwei
 */
public class GeneAndTranslateEntity implements Serializable{

    private int RecordTime;
    private int dateline;
    private String GeneID;
    private String TransferBody;

    public int getRecordTime() {
        return RecordTime;
    }

    public void setRecordTime(int recordTime) {
        RecordTime = recordTime;
    }

    public int getDateline() {
        return dateline;
    }

    public void setDateline(int dateline) {
        this.dateline = dateline;
    }

    public String getGeneID() {
        return GeneID;
    }

    public void setGeneID(String geneID) {
        GeneID = geneID;
    }

    public String getTransferBody() {
        return TransferBody;
    }

    public void setTransferBody(String transferBody) {
        TransferBody = transferBody;
    }

    @Override
    public String toString() {
        return "GeneAndTranslateEntity{" +
                "RecordTime=" + RecordTime +
                ", dateline=" + dateline +
                ", GeneID='" + GeneID + '\'' +
                ", TransferBody='" + TransferBody + '\'' +
                '}';
    }
}

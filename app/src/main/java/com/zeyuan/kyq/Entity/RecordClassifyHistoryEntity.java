package com.zeyuan.kyq.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/2.
 *
 * @author wwei
 */
public class RecordClassifyHistoryEntity implements Serializable{

    private String iResult;
    private List<CancerSizeItemEntity> TumourInfo;
    private List<CancerSizeItemEntity> CancerMark;
    private List<GeneAndTranslateEntity> TransferRecord;
    private List<GeneAndTranslateEntity> TransferGene;
    private MarkTypeEntity MarkType;

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
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

    public MarkTypeEntity getMarkType() {
        return MarkType;
    }

    public List<GeneAndTranslateEntity> getTransferRecord() {
        return TransferRecord;
    }

    public void setTransferRecord(List<GeneAndTranslateEntity> transferRecord) {
        TransferRecord = transferRecord;
    }

    public List<GeneAndTranslateEntity> getTransferGene() {
        return TransferGene;
    }

    public void setTransferGene(List<GeneAndTranslateEntity> transferGene) {
        TransferGene = transferGene;
    }

    @Override
    public String toString() {
        return "RecordClassifyHistoryEntity{" +
                "iResult='" + iResult + '\'' +
                ", TumourInfo=" + TumourInfo +
                '}';
    }

}

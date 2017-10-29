package com.zeyuan.kyq.Entity;

/**
 * Created by Administrator on 2016/5/12.
 *
 * 分期数据封装类
 *
 * @author wwei
 */
public class DigitConfEntity {

    private int id;
    private String versionNum;
    private String maxtimestamp;
    private String savetimepoint;
    private String data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum;
    }

    public String getMaxtimestamp() {
        return maxtimestamp;
    }

    public void setMaxtimestamp(String maxtimestamp) {
        this.maxtimestamp = maxtimestamp;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSavetimepoint() {
        return savetimepoint;
    }

    public void setSavetimepoint(String savetimepoint) {
        this.savetimepoint = savetimepoint;
    }

    @Override
    public String toString() {
        return "DigitConfEntity{" +
                "id=" + id +
                ", versionNum='" + versionNum + '\'' +
                ", maxtimestamp='" + maxtimestamp + '\'' +
                ", savetimepoint='" + savetimepoint + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}

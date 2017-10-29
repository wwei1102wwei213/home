package com.zeyuan.kyq.Entity;

/**
 * Created by Administrator on 2016/5/31.
 */
public class StepConfEntity {

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

    public String getMaxtimestamp() {
        return maxtimestamp;
    }

    public void setMaxtimestamp(String maxtimestamp) {
        this.maxtimestamp = maxtimestamp;
    }

    public String getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum;
    }

    @Override
    public String toString() {
        return "StepConfEntity{" +
                "id=" + id +
                ", versionNum='" + versionNum + '\'' +
                ", maxtimestamp='" + maxtimestamp + '\'' +
                ", savetimepoint='" + savetimepoint + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}

package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/5.
 *
 * @author wwei
 */
public class LiveItemEntity implements Serializable{

    private String vid;
    private String type;
    private String sign;
    private String liveid;
    private String anchorid;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getLiveid() {
        return liveid;
    }

    public void setLiveid(String liveid) {
        this.liveid = liveid;
    }

    public String getAnchorid() {
        return anchorid;
    }

    public void setAnchorid(String anchorid) {
        this.anchorid = anchorid;
    }

    @Override
    public String toString() {
        return "LiveItemEntity{" +
                "vid='" + vid + '\'' +
                ", type='" + type + '\'' +
                ", sign='" + sign + '\'' +
                ", liveid='" + liveid + '\'' +
                ", anchorid='" + anchorid + '\'' +
                '}';
    }
}

package com.zeyuan.kyq.Entity;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/29.
 *
 *
 * @author wwei
 */
public class PushMsgEntity implements Serializable{

    private int msgid;
    private int InfoId;
    private String topimg;
    private String InfoName;
    private int type;
    private String time;
    private PushTypeContentEntity typeContent;
    private String oldPush;
    private int read = 0;

    public int getMsgid() {
        return msgid;
    }

    public void setMsgid(int msgid) {
        this.msgid = msgid;
    }

    public int getInfoId() {
        return InfoId;
    }

    public void setInfoId(int infoId) {
        InfoId = infoId;
    }

    public String getTopimg() {
        return topimg;
    }

    public void setTopimg(String topimg) {
        this.topimg = topimg;
    }

    public String getInfoName() {
        return InfoName;
    }

    public void setInfoName(String infoName) {
        InfoName = infoName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        if (TextUtils.isEmpty(time)) return "0";
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public PushTypeContentEntity getTypeContent() {
        return typeContent;
    }

    public void setTypeContent(PushTypeContentEntity typeContent) {
        this.typeContent = typeContent;
    }

    public String getOldPush() {
        return oldPush;
    }

    public void setOldPush(String oldPush) {
        this.oldPush = oldPush;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    @Override
    public String toString() {
        return "PushMsgEntity{" +
                "msgid=" + msgid +
                ", InfoId=" + InfoId +
                ", topimg='" + topimg + '\'' +
                ", InfoName='" + InfoName + '\'' +
                ", type=" + type +
                ", time='" + time + '\'' +
                ", typeContent=" + typeContent +
                ", oldPush='" + oldPush + '\'' +
                ", read=" + read +
                '}';
    }
}

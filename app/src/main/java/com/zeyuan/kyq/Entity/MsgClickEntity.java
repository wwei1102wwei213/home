package com.zeyuan.kyq.Entity;

/**
 * Created by Administrator on 2017/7/21.
 * 消息点击记录
 */

public class MsgClickEntity {
    private int msgid;
    private String read;

    public int getMsgid() {
        return msgid;
    }

    public void setMsgid(int msgid) {
        this.msgid = msgid;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }
}

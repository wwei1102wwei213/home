package com.zeyuan.kyq.Entity;

/**
 * Created by Administrator on 2016/7/29.
 *
 *
 * @author wwei
 */
public class PushNewEntity {

    private long time;
    private String msg;
    private int read;
    private String flag = "";
    private int tag = 0;

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "{\"time\":\"" + time +"\",\"read\":\"" + read +"\",\"flag\":\"" + flag +"\",\"tag\":\"" + tag +"\", \"msg\":\"" + msg +"\"}";
    }


}

package com.zeyuan.kyq.Entity;

/**
 * Created by Administrator on 2016/12/29.
 */
public class PushTypeContentEntity {

    private int aid;
    private int pid;
    private String pic;
    private String title;
    private String msg;

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "PushTypeContentEntity{" +
                "aid=" + aid +
                ", pid=" + pid +
                ", pic='" + pic + '\'' +
                ", title='" + title + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}

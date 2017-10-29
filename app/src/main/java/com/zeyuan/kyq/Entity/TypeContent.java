package com.zeyuan.kyq.Entity;

/**
 * Created by Administrator on 2017/7/17.
 * 消息的内容
 * ['aid']	文章id
 * ['pic']	文章封面
 * ['title'] 	文章标题
 * ['msg']	消息内容显示
 * ['pid']	帖子id
 * ['title']	帖子标题
 */

public class TypeContent {
    private String aid;
    private String pic;
    private String title;
    private String msg;
    private String pid;
    private String touserid;//被回复者的id
    private String tousername;//被回复者的名字
    private String groupid;//本对话所在的分组

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTouserid() {
        return touserid;
    }

    public void setTouserid(String touserid) {
        this.touserid = touserid;
    }

    public String getTousername() {
        return tousername;
    }

    public void setTousername(String tousername) {
        this.tousername = tousername;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
}

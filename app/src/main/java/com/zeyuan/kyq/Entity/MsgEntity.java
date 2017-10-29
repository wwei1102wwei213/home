package com.zeyuan.kyq.Entity;

/**
 * Created by Administrator on 2017/7/6.
 * 消息实体
 */

public class MsgEntity {

    private int msgid;
    private String InfoId;
    private String topimg;
    private String InfoName;
    private int type; //1.文章内点赞 2.关注 3.文章内评论回复 4.帖子内评论回复
    private String time;
    private TypeContent typeContent;
    private String read;//阅读次数
    private String flag = "";
    private int tag = 0;

    public int getMsgid() {
        return msgid;
    }

    public void setMsgid(int msgid) {
        this.msgid = msgid;
    }

    public String getInfoId() {
        return InfoId;
    }

    public void setInfoId(String infoId) {
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
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public TypeContent getTypeContent() {
        return typeContent;
    }

    public void setTypeContent(TypeContent typeContent) {
        this.typeContent = typeContent;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
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
}

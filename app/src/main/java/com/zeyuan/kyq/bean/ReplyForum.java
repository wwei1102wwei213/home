package com.zeyuan.kyq.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/11.
 * 19.圈子—回复某个帖子
 */
public class ReplyForum implements Serializable {

    /**
     * iResult : 0
     * Commentid : 1009
     */

    private String iResult;
    private String Commentid;
    private String jf;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setCommentid(String Commentid) {
        this.Commentid = Commentid;
    }

    public String getIResult() {
        return iResult;
    }

    public String getCommentid() {
        return Commentid;
    }

    public String getJf() {
        return jf;
    }

    public void setJf(String jf) {
        this.jf = jf;
    }

    @Override
    public String toString() {
        return "ReplyForum{" +
                "iResult='" + iResult + '\'' +
                ", Commentid='" + Commentid + '\'' +
                ", jf='" + jf + '\'' +
                '}';
    }
}

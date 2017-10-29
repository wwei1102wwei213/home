package com.zeyuan.kyq.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/11.
 * <p>
 * 20.圈子--发布帖子
 */
public class PostForumBean implements Serializable {


    /**
     * iResult : 0
     * index : 1001
     * CircleID : 10
     */

    private String iResult;
    private String index;
    //    private String CircleID;
    private String displayorder;
    private String jf;//发布帖子奖励的积分

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    /*public void setCircleID(String CircleID) {
        this.CircleID = CircleID;
    }*/

    public String getIResult() {
        return iResult;
    }

    public String getIndex() {
        return index;
    }

    /*public String getCircleID() {
        return CircleID;
    }*/

    public String getDisplayorder() {
        return displayorder;
    }

    public void setDisplayorder(String displayorder) {
        this.displayorder = displayorder;
    }

    public String getJf() {
        return jf;
    }

    public void setJf(String jf) {
        this.jf = jf;
    }

    @Override
    public String toString() {
        return "PostForumBean{" +
                "iResult='" + iResult + '\'' +
                ", index='" + index + '\'' +
                ", displayorder='" + displayorder + '\'' +
                ", jf='" + jf + '\'' +
                '}';
    }
}

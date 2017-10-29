package com.zeyuan.kyq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/9/9.
 * 14.获取抗癌圈的帖子数量和人数
 */
public class CancerForumBean implements Serializable{

    /**
     * iResult : 0
     * Num : [{"CircleID":"10001","ForumNum":"501","Usernum":"100"},{"CircleID":"10002","ForumNum":"501","Usernum":"100"},{"CircleID":"10003","ForumNum":"501","Usernum":"100"}]
     */

    private String iResult;
    private List<NumEntity> Num;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setNum(List<NumEntity> Num) {
        this.Num = Num;
    }

    public String getIResult() {
        return iResult;
    }

    public List<NumEntity> getNum() {
        return Num;
    }

    public static class NumEntity {
        /**
         * CircleID : 10001
         * ForumNum : 501
         * Usernum : 100
         */

        private String CircleID;
        private String ForumNum;
        private String Usernum;

        public void setCircleID(String CircleID) {
            this.CircleID = CircleID;
        }

        public void setForumNum(String ForumNum) {
            this.ForumNum = ForumNum;
        }

        public void setUsernum(String Usernum) {
            this.Usernum = Usernum;
        }

        public String getCircleID() {
            return CircleID;
        }

        public String getForumNum() {
            return ForumNum;
        }

        public String getUsernum() {
            return Usernum;
        }
    }
}

package com.zeyuan.kyq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-11-03
 * Time: 14:21
 * FIXME
 * 同城圈的bean
 */


public class CityCancerForumBean implements Serializable{

    /**
     * iResult : 0
     * Num : [{"CircleID":"1001","ForumNum":"100","Usernum":"200"},{"CircleID":"1002","ForumNum":"102","Usernum":"203"},{"CircleID":"1003","ForumNum":"101","Usernum":"204"},{"CircleID":"1004","ForumNum":"100","Usernum":"205"}]
     */

    private String iResult;
    /**
     * CircleID : 1001
     * ForumNum : 100
     * Usernum : 200
     */

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

    public static class NumEntity implements Serializable{
        private String CircleID;
        private String ForumNum;
        private String Usernum;
        private String CancerID;
        private boolean isFollow;
        private String CancerName;

        public boolean isFollow() {
            return isFollow;
        }

        public void setIsFollow(boolean isFollow) {
            this.isFollow = isFollow;
        }

        public String getCancerID() {
            return CancerID;
        }

        public void setCancerID(String cancerID) {
            CancerID = cancerID;
        }

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

        public String getCancerName() {
            return CancerName;
        }

        public void setCancerName(String cancerName) {
            CancerName = cancerName;
        }

        @Override
        public String toString() {
            return "NumEntity{" +
                    "CircleID='" + CircleID + '\'' +
                    ", ForumNum='" + ForumNum + '\'' +
                    ", Usernum='" + Usernum + '\'' +
                    ", CancerID='" + CancerID + '\'' +
                    ", isFollow=" + isFollow +
                    ", CancerName='" + CancerName + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CityCancerForumBean{" +
                "iResult='" + iResult + '\'' +
                ", Num=" + Num.toString()+
                '}';
    }
}

package com.zeyuan.kyq.bean;

import com.zeyuan.kyq.utils.DecryptUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/13.
 */
public class MyDataBean implements Serializable{
    /**
     * iResult : 0
     * CircleNum : 3
     * ForumNum : 0
     * lastForumTitle :
     * lastFavorTitle :
     * FavorNum : 2
     * ReplyNum : 11
     *
     */

    private String iResult;
    private String CircleNum;
    private String ForumNum;
    private String lastForumTitle;
    private String lastFavorTitle;
    private String FavorNum;
    private String ReplyNum;

    /**
     * content : dWhiYmJiYg==
     * fromuser :
     * touser :
     */

    private ReplyEntity Reply;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setCircleNum(String CircleNum) {
        this.CircleNum = CircleNum;
    }

    public void setForumNum(String ForumNum) {
        this.ForumNum = ForumNum;
    }

    public void setLastForumTitle(String lastForumTitle) {
        this.lastForumTitle = lastForumTitle;
    }

    public void setLastFavorTitle(String lastFavorTitle) {
        this.lastFavorTitle = lastFavorTitle;
    }

    public void setFavorNum(String FavorNum) {
        this.FavorNum = FavorNum;
    }

    public void setReplyNum(String ReplyNum) {
        this.ReplyNum = ReplyNum;
    }

    public void setReply(ReplyEntity Reply) {
        this.Reply = Reply;
    }

    public String getIResult() {
        return iResult;
    }

    public String getCircleNum() {
        return CircleNum;
    }

    public String getForumNum() {
        return ForumNum;
    }

    public String getLastForumTitle() {
        return lastForumTitle;
    }

    public String getLastFavorTitle() {
        return lastFavorTitle;
    }

    public String getFavorNum() {
        return FavorNum;
    }

    public String getReplyNum() {
        return ReplyNum;
    }

    public ReplyEntity getReply() {
        return Reply;
    }



    public static class ReplyEntity {
        private String content;
        private String fromuser;
        private String touser;

        public void setContent(String content) {
            this.content = content;
        }

        public void setFromuser(String fromuser) {
            this.fromuser = fromuser;
        }

        public void setTouser(String touser) {
            this.touser = touser;
        }

        public String getContent() {
            return DecryptUtils.decodeBase64(content);
        }

        public String getFromuser() {
            return fromuser;
        }

        public String getTouser() {
            return touser;
        }

        @Override
        public String toString() {
            return "ReplyEntity{" +
                    "content='" + content + '\'' +
                    ", fromuser='" + fromuser + '\'' +
                    ", touser='" + touser + '\'' +
                    '}';
        }
    }
}

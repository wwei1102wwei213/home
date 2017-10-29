package com.zeyuan.kyq.bean;

import com.zeyuan.kyq.Entity.ForumInfoEntity;
import com.zeyuan.kyq.Entity.RemindUserEntity;
import com.zeyuan.kyq.utils.DecryptUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-11-04
 * Time: 11:00
 * FIXME
 */


public class ReplyListBean implements Serializable {


    /**
     * iResult : 0
     * index : 31
     * PageNum : 1
     * ReplyNum : [{"CommentId":"29","UserId":"9","FromUser":"FromUserName","ToUser":"ToUserName","ReplyTime":"250172880","Content":"2","fn":"N","bc":"N"},{"CommentId":"30","UserId":"9","FromUser":"FromUserName","ToUser":"ToUserName","ReplyTime":"250172880","Content":"2","fn":"N","bc":"N"},{"CommentId":"31","UserId":"9","FromUser":"FromUserName","ToUser":"ToUserName","ReplyTime":"250172880","Content":"2","fn":"N","bc":"N"},{"CommentId":"32","UserId":"9","FromUser":"FromUserName","ToUser":"ToUserName","ReplyTime":"250172880","Content":"2","fn":"N","bc":"N"},{"CommentId":"33","UserId":"9","FromUser":"FromUserName","ToUser":"ToUserName","ReplyTime":"250172880","Content":"2","fn":"N","bc":"N"},{"CommentId":"34","UserId":"9","FromUser":"FromUserName","ToUser":"ToUserName","ReplyTime":"250172880","Content":"2","fn":"N","bc":"N"},{"CommentId":"35","UserId":"9","FromUser":"FromUserName","ToUser":"ToUserName","ReplyTime":"250172880","Content":"dfa","fn":"N","bc":"N"},{"CommentId":"36","UserId":"9","FromUser":"FromUserName","ToUser":"ToUserName","ReplyTime":"250172880","Content":"dfa","fn":"N","bc":"N"},{"CommentId":"37","UserId":"9","FromUser":"FromUserName","ToUser":"ToUserName","ReplyTime":"250172880","Content":"dfa","fn":"N","bc":"N"},{"CommentId":"38","UserId":"9","FromUser":"FromUserName","ToUser":"ToUserName","ReplyTime":"250172880","Content":"dfa","fn":"N","bc":"N"},{"CommentId":"39","UserId":"9","FromUser":"FromUserName","ToUser":"ToUserName","ReplyTime":"250172880","Content":"dfa","fn":"N","bc":"N"},{"CommentId":"29","UserId":"9","FromUser":"FromUserName","ToUser":"ToUserName","ReplyTime":"250172880","Content":"2","fn":"N","bc":"N"},{"CommentId":"30","UserId":"9","FromUser":"FromUserName","ToUser":"ToUserName","ReplyTime":"250172880","Content":"2","fn":"N","bc":"N"},{"CommentId":"31","UserId":"9","FromUser":"FromUserName","ToUser":"ToUserName","ReplyTime":"250172880","Content":"2","fn":"N","bc":"N"},{"CommentId":"32","UserId":"9","FromUser":"FromUserName","ToUser":"ToUserName","ReplyTime":"250172880","Content":"2","fn":"N","bc":"N"},{"CommentId":"33","UserId":"9","FromUser":"FromUserName","ToUser":"ToUserName","ReplyTime":"250172880","Content":"2","fn":"N","bc":"N"},{"CommentId":"34","UserId":"9","FromUser":"FromUserName","ToUser":"ToUserName","ReplyTime":"250172880","Content":"2","fn":"N","bc":"N"},{"CommentId":"35","UserId":"9","FromUser":"FromUserName","ToUser":"ToUserName","ReplyTime":"250172880","Content":"dfa","fn":"N","bc":"N"},{"CommentId":"36","UserId":"9","FromUser":"FromUserName","ToUser":"ToUserName","ReplyTime":"250172880","Content":"dfa","fn":"N","bc":"N"},{"CommentId":"37","UserId":"9","FromUser":"FromUserName","ToUser":"ToUserName","ReplyTime":"250172880","Content":"dfa","fn":"N","bc":"N"}]
     */

    private String iResult;
    private String index;
    private String PageNum;
    private String OwnerID;
    private List<String> CircleId;
    private List<ForumInfoEntity> thread;//作者的其他帖子
    private String ReplyCount;
    private String headUrl;
    private String InfoName;
    private ArrayList<RemindUserEntity> RemindUserInfo;//@ 到的好友
    private String V;
    private int care;

    public List<ForumInfoEntity> getThread() {
        return thread;
    }

    public void setThread(List<ForumInfoEntity> thread) {
        this.thread = thread;
    }

    public ArrayList<RemindUserEntity> getRemindUserInfo() {
        return RemindUserInfo;
    }

    public void setRemindUserInfo(ArrayList<RemindUserEntity> remindUserInfo) {
        RemindUserInfo = remindUserInfo;
    }

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getInfoName() {
        return InfoName;
    }

    public void setInfoName(String infoName) {
        InfoName = infoName;
    }

    public String getV() {
        return V;
    }

    public void setV(String v) {
        V = v;
    }

    public int getCare() {
        return care;
    }

    public void setCare(int care) {
        this.care = care;
    }

    public String getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(String ownerID) {
        OwnerID = ownerID;
    }

    public List<String> getCircleId() {
        return CircleId;
    }

    public void setCircleId(List<String> circleId) {
        CircleId = circleId;
    }

    public String getReplyCount() {
        return ReplyCount;
    }

    public void setReplyCount(String replyCount) {
        ReplyCount = replyCount;
    }

    /**
     * CommentId : 29
     * UserId : 9
     * FromUser : FromUserName
     * ToUser : ToUserName
     * ReplyTime : 250172880
     * Content : 2
     * fn : N
     * bc : N
     */

    private List<ReplyNumEntity> ReplyNum;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setPageNum(String PageNum) {
        this.PageNum = PageNum;
    }

    public void setReplyNum(List<ReplyNumEntity> ReplyNum) {
        this.ReplyNum = ReplyNum;
    }

    public String getIResult() {
        return iResult;
    }

    public String getIndex() {
        return index;
    }

    public String getPageNum() {
        return PageNum;
    }

    public List<ReplyNumEntity> getReplyNum() {
        return ReplyNum;
    }

    public static class ReplyNumEntity {
        private String CommentId;
        private String UserId;
        private String FromUser;
        private String ToUser;
        private String ReplyTime;
        private String Content;
        private String fn;
        private String bc;
        private String HeadImgUrl;
        private String OwnerID;
        private String CircleId;
        private boolean isHost;//是不是楼主、


        public String getOwnerID() {
            return OwnerID;
        }

        public void setOwnerID(String ownerID) {
            OwnerID = ownerID;
        }

        public String getCircleId() {
            return CircleId;
        }

        public void setCircleId(String circleId) {
            CircleId = circleId;
        }

        public boolean isHost() {
            return isHost;
        }

        public void setIsHost(boolean isHost) {
            this.isHost = isHost;
        }

        public String getHeadimgurl() {
            return HeadImgUrl;
        }

        public void setHeadimgurl(String headimgurl) {
            HeadImgUrl = headimgurl;
        }

        @Override
        public String toString() {
            return "ReplyNumEntity{" +
                    "CommentId='" + CommentId + '\'' +
                    ", UserId='" + UserId + '\'' +
                    ", FromUser='" + FromUser + '\'' +
                    ", ToUser='" + ToUser + '\'' +
                    ", ReplyTime='" + ReplyTime + '\'' +
                    ", Content='" + Content + '\'' +
                    ", fn='" + fn + '\'' +
                    ", bc='" + bc + '\'' +
                    '}';
        }

        public void setCommentId(String CommentId) {
            this.CommentId = CommentId;
        }

        public void setUserId(String UserId) {
            this.UserId = UserId;
        }

        public void setFromUser(String FromUser) {
            this.FromUser = FromUser;
        }

        public void setToUser(String ToUser) {
            this.ToUser = ToUser;
        }

        public void setReplyTime(String ReplyTime) {
            this.ReplyTime = ReplyTime;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }

        public void setFn(String fn) {
            this.fn = fn;
        }

        public void setBc(String bc) {
            this.bc = bc;
        }

        public String getCommentId() {
            return CommentId;
        }

        public String getUserId() {
            return UserId;
        }

        public String getFromUser() {
            return FromUser;
        }

        public String getToUser() {
            return ToUser;
        }

        public String getReplyTime() {
            return ReplyTime;
        }

        public String getContent() {
            return DecryptUtils.decodeBase64(Content);
//            return Content;
        }

        public String getFn() {
            return fn;
        }

        public String getBc() {
            return bc;
        }
    }

    @Override
    public String toString() {
        return "ReplyListBean{" +
                "iResult='" + iResult + '\'' +
                ", index='" + index + '\'' +
                ", PageNum='" + PageNum + '\'' +
                ", ReplyNum=" + ReplyNum +
                '}';
    }
}

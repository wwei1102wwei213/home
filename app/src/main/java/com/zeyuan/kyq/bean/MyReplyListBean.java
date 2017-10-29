package com.zeyuan.kyq.bean;

import com.zeyuan.kyq.utils.DecryptUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/1/8.
 */
public class MyReplyListBean implements Serializable{

    /**
     * iResult : 0
     * PageNum : 1
     * ReplyNum : [{"CommentId":"143","UserId":"8990","FromUser":"","ToUser":"","ReplyTime":"1452252026","Content":"了剧痛","title":"母亲凯美纳三个月，可能耐药了，医生让放疗，求助以马内利","index":"117"}]
     */

    private String iResult;
    private String PageNum;
    /**
     * CommentId : 143
     * UserId : 8990
     * FromUser :
     * ToUser :
     * ReplyTime : 1452252026
     * Content : 了剧痛
     * title : 母亲凯美纳三个月，可能耐药了，医生让放疗，求助以马内利
     * index : 117
     */

    private List<ReplyNumEntity> ReplyNum;

    public void setIResult(String iResult) {
        this.iResult = iResult;
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
        private String title;
        private String index;
        private String HeadImgUrl;
        private String OwnerID;
        private String CircleId;


        public String getCircleId() {
            return CircleId;
        }

        public void setCircleId(String circleId) {
            CircleId = circleId;
        }

        public String getOwnerID() {
            return OwnerID;
        }

        public void setOwnerID(String ownerID) {
            OwnerID = ownerID;
        }

        public String getHeadImgurl() {
            return HeadImgUrl;
        }

        public void setHeadImgurl(String headImgurl) {
            HeadImgUrl = headImgurl;
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

        public void setTitle(String title) {
            this.title = title;
        }

        public void setIndex(String index) {
            this.index = index;
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
        }

        public String getTitle() {
            return title;
        }

        public String getIndex() {
            return index;
        }
    }
}

package com.zeyuan.kyq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/11/17.
 * 我发布的帖子
 */
public class MyForumReleaseBean implements Serializable{

    /**
     * iResult : 0
     * ForumNum : [{"Title":"gghj","Index":"15","ReplyNum":"0","PostType":"0","PostTime":"1447758234"}]
     */

    private String iResult;
    /**
     * Title : gghj
     * Index : 15
     * ReplyNum : 0
     * PostType : 0
     * PostTime : 1447758234
     */

//    private List<ForumBeanEntity> ForumNum;
    private List<ForumListBean.ForumnumEntity> ForumNum;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public String getIResult() {
        return iResult;
    }

    public List<ForumListBean.ForumnumEntity> getForumNum() {
        return ForumNum;
    }

    public void setForumNum(List<ForumListBean.ForumnumEntity> forumNum) {
        ForumNum = forumNum;
    }

    public static class ForumBeanEntity {
        private String Title;
        private String Index;
        private String ReplyNum;
        private String PostType;
        private String PostTime;
        private String HeadImgUrl;
        private String CircleId;
        private String OwnerID;

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

        public String getHeadImgUrl() {
            return HeadImgUrl;
        }

        public void setHeadImgUrl(String headImgUrl) {
            HeadImgUrl = headImgUrl;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public void setIndex(String Index) {
            this.Index = Index;
        }

        public void setReplyNum(String ReplyNum) {
            this.ReplyNum = ReplyNum;
        }

        public void setPostType(String PostType) {
            this.PostType = PostType;
        }

        public void setPostTime(String PostTime) {
            this.PostTime = PostTime;
        }

        public String getTitle() {
            return Title;
        }

        public String getIndex() {
            return Index;
        }

        public String getReplyNum() {
            return ReplyNum;
        }

        public String getPostType() {
            if(PostType==null){
                return "0";
            }
            return PostType;
        }

        public String getPostTime() {
            return PostTime;
        }

        @Override
        public String toString() {
            return "ForumBeanEntity{" +
                    "Title='" + Title + '\'' +
                    ", Index='" + Index + '\'' +
                    ", ReplyNum='" + ReplyNum + '\'' +
                    ", PostType='" + PostType + '\'' +
                    ", PostTime='" + PostTime + '\'' +
                    ", HeadImgUrl='" + HeadImgUrl + '\'' +
                    ", CircleId='" + CircleId + '\'' +
                    ", OwnerID='" + OwnerID + '\'' +
                    '}';
        }
    }
}

package com.zeyuan.kyq.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/9/11.
 * <p/>
 * 17.	获圈子--获取某圈子下的帖子列表
 */
public class ForumListBean implements Serializable{

    private String iResult;
    private List<ForumnumEntity> ForumAllNum;
    private String ForumNum;
    private String UserNum;
    private String description;

    public String getUserNum() {
        return UserNum;
    }

    public void setUserNum(String userNum) {
        UserNum = userNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ForumnumEntity> getForumAllNum() {
        return ForumAllNum;
    }

    public void setForumAllNum(List<ForumnumEntity> forumAllNum) {
        ForumAllNum = forumAllNum;
    }

    public String getForumNum() {
        return ForumNum;
    }

    public void setForumNum(String forumNum) {
        ForumNum = forumNum;
    }

    @Override
    public String toString() {
        return "ForumListBean{" +
                "iResult='" + iResult + '\'' +
                ", Forumnum=" + ForumAllNum +
                '}';
    }

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setForumnum(List<ForumnumEntity> ForumAllNum) {
        this.ForumAllNum = ForumAllNum;
    }

    public String getIResult() {
        return iResult;
    }

    public List<ForumnumEntity> getForumnum() {
        return ForumAllNum;
    }

    public static class ForumnumEntity {
        private String Index;
        private String Title;
        private String Author;
        private String HeadImgUrl;
        private String PostType;//置顶1 精华2 普通0
        private String ReplyNum;
        private List<String> CircleId;//这个是圈子首页中带有circleid 在某个圈子下的帖子的列表 没有。
        private String PostTime;
        private String OwnerID;
        private String Posttype;

        public String getPosttype() {
            return Posttype;
        }

        public void setPosttype(String posttype) {
            Posttype = posttype;
        }

        public String getOwnerID() {
            return OwnerID;
        }

        public void setOwnerID(String ownerID) {
            OwnerID = ownerID;
        }

        public String getHeadimgurl() {
            return HeadImgUrl;
        }
        public void setHeadimgurl(String headimgurl) {
            HeadImgUrl = headimgurl;
        }

        public List<String> getCircleId() {
            return CircleId;
        }

        public void setCircleId(List<String> circleId) {
            CircleId = circleId;
        }

        public void setIndex(String Index) {
            this.Index = Index;
        }
        public void setTitle(String Title) {
            this.Title = Title;
        }
        public void setAuthor(String Author) {
            this.Author = Author;
        }
        public void setPostType(String PostType) {
            this.PostType = PostType;
        }
        public void setReplyNum(String ReplyNum) {
            this.ReplyNum = ReplyNum;
        }
        public String getIndex() {
            return Index;
        }
        public String getTitle() {
            return Title;
        }
        public String getAuthor() {
            return Author;
        }

        public String getHeadImgUrl() {
            return HeadImgUrl;
        }

        public void setHeadImgUrl(String headImgUrl) {
            HeadImgUrl = headImgUrl;
        }

        public String getPostTime() {
            return PostTime;
        }

        public void setPostTime(String postTime) {
            PostTime = postTime;
        }

        public String getPostType() {
            if (TextUtils.isEmpty(PostType)) {
                return "0";
            }
            return PostType;
        }

        public String getReplyNum() {
            return ReplyNum;
        }

        @Override
        public String toString() {
            return "ForumnumEntity{" +
                    "Index='" + Index + '\'' +
                    ", Title='" + Title + '\'' +
                    ", Author='" + Author + '\'' +
                    ", HeadImgUrl='" + HeadImgUrl + '\'' +
                    ", PostType='" + PostType + '\'' +
                    ", ReplyNum='" + ReplyNum + '\'' +
                    ", CircleId='" + CircleId + '\'' +
                    ", PostTime='" + PostTime + '\'' +
                    '}';
        }
    }
}

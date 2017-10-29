package com.zeyuan.kyq.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/11.
 * <p/>
 * 13.获取我的圈子
 */
public class MyCircleBean {

    /**
     * iResult : 0
     * FollowCircleNum : [{"CircleName":"深圳圈","CircleID":"1004"}]
     * AllPostNum : [{"Author":"","Index":"15","CircleId":"1","PostType":"0","ReplyNum":"0","Title":"gghj"},{"Author":"","Index":"14","CircleId":"1","PostType":"0","ReplyNum":"0","Title":"ghj"},{"Author":"zhangsan","Index":"13","CircleId":"1004","PostType":"0","ReplyNum":"0","Title":"base64 2"},{"Author":"zhangsan","Index":"12","CircleId":"1004","PostType":"0","ReplyNum":"0","Title":"base64"},{"Author":"zhangsan","Index":"11","CircleId":"1004","PostType":"0","ReplyNum":"0","Title":"」cccc"},{"Author":"","Index":"10","CircleId":"1","PostType":"0","ReplyNum":"0","Title":"adsfasd"},{"Author":"zhangsan","Index":"9","CircleId":"1004","PostType":"0","ReplyNum":"0","Title":"emo123"},{"Author":"zhangsan","Index":"8","CircleId":"1004","PostType":"0","ReplyNum":"0","Title":"123444324"},{"Author":"zhangsan","Index":"7","CircleId":"1004","PostType":"0","ReplyNum":"0","Title":"12345"},{"Author":"zhangsan","Index":"6","CircleId":"1004","PostType":"0","ReplyNum":"0","Title":"1234"},{"Author":"zhangsan","Index":"5","CircleId":"1004","PostType":"0","ReplyNum":"0","Title":"unicode123"},{"Author":"zhangsan","Index":"4","CircleId":"1004","PostType":"0","ReplyNum":"0","Title":"123"},{"Author":"zhangsan","Index":"3","CircleId":"1004","PostType":"0","ReplyNum":"0","Title":"test11111"},{"Author":"zhangsan","Index":"2","CircleId":"1004","PostType":"0","ReplyNum":"0","Title":"214啊的身份"},{"Author":"zhangsan","Index":"1","CircleId":"1004","PostType":"0","ReplyNum":"0","Title":"2243242观赏性"}]
     */

    private String iResult;
    /**
     * CircleName : 深圳圈
     * CircleID : 1004
     */

    private List<String> FollowCircleID;
    /**
     * Author :
     * Index : 15
     * CircleId : 1
     * PostType : 0
     * ReplyNum : 0
     * Title : gghj
     */

    private List<ForumListBean.ForumnumEntity> AllPostNum;

    private List<List<String>> defaultCircle;
    private Map<String, String> threadCircle;
    private int RemindNum;//最多可@的好有数

    public List<List<String>> getDefaultCircle() {
        return defaultCircle;
    }

    public Map<String, String> getThreadCircle() {
        return threadCircle;
    }

    public void setThreadCircle(Map<String, String> threadCircle) {
        this.threadCircle = threadCircle;
    }

    public void setDefaultCircle(List<List<String>> defaultCircle) {
        this.defaultCircle = defaultCircle;
    }

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setFollowCircleNum(List<String> FollowCircleNum) {
        this.FollowCircleID = FollowCircleNum;
    }

    public void setAllPostNum(List<ForumListBean.ForumnumEntity> AllPostNum) {
        this.AllPostNum = AllPostNum;
    }

    public String getIResult() {
        return iResult;
    }

    public int getRemindNum() {
        return RemindNum;
    }

    public void setRemindNum(int remindNum) {
        RemindNum = remindNum;
    }

    public List<String> getFollowCircleNum() {
        return FollowCircleID;
    }

    public List<ForumListBean.ForumnumEntity> getAllPostNum() {
        return AllPostNum;
    }

    public static class FollowCircleNumEntity {
        private String CircleName;
        private String CircleID;

        public void setCircleName(String CircleName) {
            this.CircleName = CircleName;
        }

        public void setCircleID(String CircleID) {
            this.CircleID = CircleID;
        }

        public String getCircleName() {
            return CircleName;
        }

        public String getCircleID() {
            return CircleID;
        }
    }

    @Override
    public String toString() {
        return "MyCircleBean{" +
                "iResult='" + iResult + '\'' +
                ", FollowCircleNum=" + FollowCircleID +
                ", AllPostNum=" + AllPostNum +
                '}';
    }
}

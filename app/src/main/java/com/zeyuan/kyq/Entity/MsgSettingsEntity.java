package com.zeyuan.kyq.Entity;

/**
 * Created by Administrator on 2017/7/24.
 * 设置数据
 */

public class MsgSettingsEntity {
    //    call,@的开关状态
//            follower,关注的开关状态
//            comment;评论的开关状态
    private int comment;
    private int follower;
    private int call;

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    public int getCall() {
        return call;
    }

    public void setCall(int call) {
        this.call = call;
    }
}

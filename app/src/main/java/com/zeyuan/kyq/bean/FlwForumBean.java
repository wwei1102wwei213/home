package com.zeyuan.kyq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/9/11.
 * <p/>
 * 17.	获圈子--获取某圈子下的帖子列表
 */
public class FlwForumBean implements Serializable{

    /**
     * iResult : 0
     * ForumNum : [{"Title":"firsttitle","Index":"124","ReplyNum":"0","PostType":"0","PostTime":"1452309040"}]
     */

    private String iResult;
    /**
     * Title : firsttitle
     * Index : 124
     * ReplyNum : 0
     * PostType : 0
     * PostTime : 1452309040
     */

    private List<ForumListBean.ForumnumEntity> ForumNum;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setForumNum(List<ForumListBean.ForumnumEntity> ForumNum) {
        this.ForumNum = ForumNum;
    }

    public String getIResult() {
        return iResult;
    }

    public List<ForumListBean.ForumnumEntity> getForumNum() {
        return ForumNum;
    }

}


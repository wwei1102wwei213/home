package com.zeyuan.kyq.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/16.
 *
 *
 * @author wwei
 */
public class ForumBaseBean implements Serializable{

    private String iResult;
    private String description;
    private List<ForumBaseEntity> ForumAllNum;
    private List<String> CareHeadUrl;

    public String getiResult() {
        return iResult;
    }

    public List<ForumBaseEntity> getForumAllNum() {
        return ForumAllNum;
    }

    public void setForumAllNum(List<ForumBaseEntity> forumAllNum) {
        ForumAllNum = forumAllNum;
    }

    public List<String> getCareHeadUrl() {
        return CareHeadUrl;
    }

    public void setCareHeadUrl(List<String> careHeadUrl) {
        CareHeadUrl = careHeadUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ForumBaseBean{" +
                "iResult='" + iResult + '\'' +
                ", ForumAllNum=" + ForumAllNum +
                ", CareHeadUrl=" + CareHeadUrl +
                '}';
    }
}

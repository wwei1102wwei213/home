package com.zeyuan.kyq.Entity;

/**
 * Created by Administrator on 2017/8/30.
 * 推荐用户的实体
 */

public class RecommendUserInfoEntity {
    private String InfoID;
    private String InfoName;
    private String oss_request_url;
    private boolean isSelected = true;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getInfoID() {
        return InfoID;
    }

    public void setInfoID(String infoID) {
        InfoID = infoID;
    }

    public String getInfoName() {
        return InfoName;
    }

    public void setInfoName(String infoName) {
        InfoName = infoName;
    }

    public String getOss_request_url() {
        return oss_request_url;
    }

    public void setOss_request_url(String oss_request_url) {
        this.oss_request_url = oss_request_url;
    }
}

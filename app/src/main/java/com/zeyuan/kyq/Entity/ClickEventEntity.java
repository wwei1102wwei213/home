package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/8.
 * 点击统计的实体
 */

public class ClickEventEntity implements Serializable {
    /*点击位置1、app横幅 2、主页快速入口 3、直播 4、公益活动 5、圈子快速入口 6、广告列表*/
    private String looktype;//
    private String looktag;//点击的id
    private String clicktime;//点击时间戳

    public String getLooktype() {
        return looktype;
    }

    public void setLooktype(String looktype) {
        this.looktype = looktype;
    }

    public String getLooktag() {
        return looktag;
    }

    public void setLooktag(String looktag) {
        this.looktag = looktag;
    }

    public String getClicktime() {
        return clicktime;
    }

    public void setClicktime(String clicktime) {
        this.clicktime = clicktime;
    }
}

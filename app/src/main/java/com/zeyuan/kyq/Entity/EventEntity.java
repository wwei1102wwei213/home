package com.zeyuan.kyq.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/6.
 *
 *
 * @author wwei
 */
public class EventEntity implements Serializable{

    private String iResult;
    private List<MainBannerEntity> Banner;
    private List<MainBannerEntity> Event;

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public List<MainBannerEntity> getBanner() {
        return Banner;
    }

    public void setBanner(List<MainBannerEntity> banner) {
        Banner = banner;
    }

    public List<MainBannerEntity> getEvent() {
        return Event;
    }

    public void setEvent(List<MainBannerEntity> event) {
        Event = event;
    }

    @Override
    public String toString() {
        return "EventEntity{" +
                "iResult='" + iResult + '\'' +
                ", Banner=" + Banner +
                ", Event=" + Event +
                '}';
    }
}

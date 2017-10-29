package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/11.
 */
public class HelpItemMainEntity implements Serializable{

    private String name;
    private String cash;
    private String time;
    private String title;
    private String url;
    private String watch;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWatch() {
        return watch;
    }

    public void setWatch(String watch) {
        this.watch = watch;
    }

    @Override
    public String toString() {
        return "HelpItemMainEntity{" +
                "name='" + name + '\'' +
                ", cash='" + cash + '\'' +
                ", time='" + time + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}

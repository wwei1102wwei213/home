package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/11.
 */
public class ArticleItemEntity implements Serializable{

    private String title;
    private String watch;
    private String url;
    private String from;
    private int head;

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWatch() {
        return watch;
    }

    public void setWatch(String watch) {
        this.watch = watch;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return "ArticleItemEntity{" +
                "title='" + title + '\'' +
                ", watch='" + watch + '\'' +
                ", url='" + url + '\'' +
                ", from='" + from + '\'' +
                '}';
    }
}

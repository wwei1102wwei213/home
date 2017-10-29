package com.zeyuan.kyq.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/14.
 */
public class ArticleBaseEntity implements Serializable{


    private String iResult;
    private List<ArticleCommentEntity> data;

    public String getiResult() {
        return iResult;
    }

    public List<ArticleCommentEntity> getData() {
        return data;
    }

    public void setData(List<ArticleCommentEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ArticleBaseEntity{" +
                "iResult='" + iResult + '\'' +
                ", data=" + data +
                '}';
    }
}

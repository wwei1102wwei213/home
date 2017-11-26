package com.zeyuan.kyq.bean;

import java.util.List;

/**
 * Created by Administrator on 2017-11-25.
 */

public class CommentProjectBean {

    private String iResult;
    private List<CommentProjectItem> data;

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public List<CommentProjectItem> getData() {
        return data;
    }

    public void setData(List<CommentProjectItem> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CommentProjectBean{" +
                "iResult='" + iResult + '\'' +
                ", data=" + data +
                '}';
    }
}

package com.zeyuan.kyq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/5/13.
 */
public class HeadLineBean implements Serializable{

    private String iResult;

    private List<HeadListBean> HeadList;

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public List<HeadListBean> getHeadList() {
        return HeadList;
    }

    public void setHeadList(List<HeadListBean> headList) {
        HeadList = headList;
    }

    public static class HeadListBean{
        private String from;
        private String PostTime;
        private String Title;
        private String ThumbURL;
        private String PageViewNum;
        private String ArticleIndex;
        private String Summary;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getPostTime() {
            return PostTime;
        }

        public void setPostTime(String postTime) {
            PostTime = postTime;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getThumbURL() {
            return ThumbURL;
        }

        public void setThumbURL(String thumbURL) {
            ThumbURL = thumbURL;
        }

        public String getPageViewNum() {
            return PageViewNum;
        }

        public void setPageViewNum(String pageViewNum) {
            PageViewNum = pageViewNum;
        }

        public String getArticleIndex() {
            return ArticleIndex;
        }

        public void setArticleIndex(String articleIndex) {
            ArticleIndex = articleIndex;
        }

        public String getSummary() {
            return Summary;
        }

        public void setSummary(String summary) {
            Summary = summary;
        }

        @Override
        public String toString() {
            return "HeadListBean{" +
                    "from='" + from + '\'' +
                    ", PostTime='" + PostTime + '\'' +
                    ", Title='" + Title + '\'' +
                    ", ThumbURL='" + ThumbURL + '\'' +
                    ", PageViewNum='" + PageViewNum + '\'' +
                    ", ArticleIndex='" + ArticleIndex + '\'' +
                    ", Summary='" + Summary + '\'' +
                    '}';
        }
    }

}

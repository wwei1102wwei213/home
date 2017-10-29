package com.zeyuan.kyq.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-12-15
 * Time: 11:17
 * FIXME
 * 文章列表的bean 出现在主页的点击进入更多（除了相似案例）里面
 */


public class ArticleListBean implements Serializable{

    /**
     * iResult : 0
     * Articlenum : [{"ArticleID":"20","Title":"伽马刀","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490269888553.jpg"},{"ArticleID":"22","Title":"普通放疗","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490269448539.jpg"},{"ArticleID":"23","Title":"三维适形调强放疗","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490267816892.jpg"},{"ArticleID":"24","Title":"三维适形放疗","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490267184607.jpg"},{"ArticleID":"25","Title":"中子刀","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490266698803.jpg"},{"ArticleID":"26","Title":"质子刀","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490265963655.jpg"},{"ArticleID":"27","Title":"tomo刀","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490265649693.jpg"},{"ArticleID":"28","Title":"射波刀","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490265332294.jpg"},{"ArticleID":"29","Title":"EDGE速锋刀","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490264422999.jpg"},{"ArticleID":"30","Title":"其他根治性放疗","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490262524630.jpg"},{"ArticleID":"40","Title":"肺腺癌术后辅助化疗","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490262018167.jpg"},{"ArticleID":"41","Title":"顺铂","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490261264564.jpg"},{"ArticleID":"42","Title":"卡铂","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490260583438.jpg"},{"ArticleID":"43","Title":"奈达铂","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490259559453.jpg"},{"ArticleID":"44","Title":"奥沙利铂","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490259287546.jpg"},{"ArticleID":"45","Title":"乐铂","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490258957434.jpg"},{"ArticleID":"46","Title":"依铂","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490258048838.jpg"},{"ArticleID":"47","Title":"其他正在临床试验的铂类抗肿瘤药","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490257478590.jpg"},{"ArticleID":"48","Title":"晚期非小细胞肺癌的靶向治疗","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490256073680.jpg"},{"ArticleID":"49","Title":"非小细胞肺癌新辅助化疗的基本共识","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490251353074.jpeg"},{"ArticleID":"50","Title":"原发和继发性c-MET信号通路改变","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490250733104.jpg"},{"ArticleID":"51","Title":"原发和继发性c-MET信号通路改变","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490248984922.jpg"},{"ArticleID":"52","Title":"几种吃梨缓解不同类型咳嗽的方法","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490235916715.jpg"},{"ArticleID":"54","Title":"测试1是打发","ThumbURL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14500600722924.jpg"}]
     */

    private String iResult;
    /**
     * ArticleID : 20
     * Title : 伽马刀
     * ThumbURL : http://oss-cn-qingdao.aliyuncs.com/bucketn1/14490269888553.jpg
     */

    private ArrayList<ArticlenumEntity> Articlenum;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setArticlenum(ArrayList<ArticlenumEntity> Articlenum) {
        this.Articlenum = Articlenum;
    }

    public String getIResult() {
        return iResult;
    }

    public ArrayList<ArticlenumEntity> getArticlenum() {
        return Articlenum;
    }

    public static class ArticlenumEntity implements Serializable{
        private String ArticleIndex;
        private String Title;
        private String ThumbURL;
        private String Summary;
        private String dateline;
        private String PageViewNum;

        public void setArticleID(String ArticleID) {
            this.ArticleIndex = ArticleID;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public void setThumbURL(String ThumbURL) {
            this.ThumbURL = ThumbURL;
        }

        public String getArticleID() {
            return ArticleIndex;
        }

        public String getTitle() {
            return Title;
        }

        public String getThumbURL() {
            return ThumbURL;
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

        public String getDateline() {
            return dateline;
        }

        public void setDateline(String dateline) {
            this.dateline = dateline;
        }

        public String getPageViewNum() {
            return PageViewNum;
        }

        public void setPageViewNum(String pageViewNum) {
            PageViewNum = pageViewNum;
        }

        @Override
        public String toString() {
            return "ArticlenumEntity{" +
                    "ArticleIndex='" + ArticleIndex + '\'' +
                    ", Title='" + Title + '\'' +
                    ", ThumbURL='" + ThumbURL + '\'' +
                    ", Summary='" + Summary + '\'' +
                    ", dateline='" + dateline + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ArticleListBean{" +
                "iResult='" + iResult + '\'' +
                ", Articlenum=" + Articlenum +
                '}';
    }
}

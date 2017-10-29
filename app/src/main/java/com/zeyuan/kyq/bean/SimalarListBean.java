package com.zeyuan.kyq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/1/24.
 */
public class SimalarListBean implements Serializable{

    /**
     * iResult : 0
     * AllPostNum : [{"Index":191,"Title":" 31岁，易瑞沙、2992、化疗都无效，现在吃6244中，血小板很低","PostTime":1452597816,"HeadImgUrl":" /upload/images/14524831972252.jpg","Author":" 奇迹就在眼前","ReplyNum":80},{"Index":192,"Title":" KRAS突变，放疗、多吉美、6244同时用，肺部控制住，肝和腹部进展","PostTime":1452596388,"HeadImgUrl":" /upload/images/14524831152551.jpg","Author":" 相信会有奇迹999","ReplyNum":80},{"Index":193,"Title":" 31岁，易瑞沙、2992、化疗都无效，现在吃6244中，血小板很低","PostTime":1452596187,"HeadImgUrl":" /upload/images/14524830414793.jpg","Author":" 心中有爱","ReplyNum":80},{"Index":194,"Title":" 面对妈妈的病情，有些不知所措","PostTime":1452595991,"HeadImgUrl":" /upload/images/14524828229386.jpg","Author":" 顺安","ReplyNum":80},{"Index":201,"Title":" 父，肺腺癌4期，服用易瑞沙13个月，19突与T790M突","PostTime":1452573863,"HeadImgUrl":" /upload/images/14524819932974.jpg","Author":" 祈祷的兔子","ReplyNum":80},{"Index":202,"Title":" 父亲肺鳞癌的治疗贴","PostTime":1452572921,"HeadImgUrl":" /upload/images/14523240591831.jpg","Author":" 梦想飞翔","ReplyNum":80},{"Index":203,"Title":" 肺腺癌半年治疗效果---靶向化疗三次，陀螺刀，DC-CIK","PostTime":1452571159,"HeadImgUrl":" /upload/images/14523237929039.jpg","Author":" 祝福我们","ReplyNum":80},{"Index":204,"Title":" 老树的NSCLC抗癌进行曲：低概率的高向往","PostTime":1452570785,"HeadImgUrl":" /upload/images/14523235749314.jpg","Author":" 一切都好","ReplyNum":80},{"Index":214,"Title":" 84岁老妈肺腺，易、299804、184有效，特、2992、凡德、阿西无效，现在4002联184","PostTime":1453425480,"HeadImgUrl":" http://bucketn1.oss-cn-qingdao.aliyuncs.com/1HeadImg145268246269889607018.jpg","Author":" 抗癌高","ReplyNum":0},{"Index":222,"Title":" 爸爸肺腺癌35个月零10天，病理性骨折+下肢静脉血栓+INC280，我相信总有一天他还会回来","PostTime":1395035726,"HeadImgUrl":" /upload/images/14524822673129.jpg","Author":" ","ReplyNum":0},{"Index":224,"Title":" \u201c谋事在人，成事在天\u201d\u201419号突变，低分化肺腺癌27个月。","PostTime":1376980992,"HeadImgUrl":" /upload/images/14524824658818.jpg","Author":" ","ReplyNum":0},{"Index":229,"Title":" 肺腺癌骨脑转19突变,EGFR,CMET扩增","PostTime":1398932368,"HeadImgUrl":" /upload/images/14524831152551.jpg","Author":" ","ReplyNum":0}]
     */

    private String iResult;
    /**
     * Index : 191
     * Title :  31岁，易瑞沙、2992、化疗都无效，现在吃6244中，血小板很低
     * PostTime : 1452597816
     * HeadImgUrl :  /upload/images/14524831972252.jpg
     * Author :  奇迹就在眼前
     * ReplyNum : 80
     */

    private List<ForumListBean.ForumnumEntity> AllPostNum;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setAllPostNum(List<ForumListBean.ForumnumEntity> AllPostNum) {
        this.AllPostNum = AllPostNum;
    }

    public String getIResult() {
        return iResult;
    }

    public List<ForumListBean.ForumnumEntity> getAllPostNum() {
        return AllPostNum;
    }
//
//    public static class AllPostNumEntity {
//        private int Index;
//        private String Title;
//        private int PostTime;
//        private String HeadImgUrl;
//        private String Author;
//        private int ReplyNum;
//
//        public void setIndex(int Index) {
//            this.Index = Index;
//        }
//
//        public void setTitle(String Title) {
//            this.Title = Title;
//        }
//
//        public void setPostTime(int PostTime) {
//            this.PostTime = PostTime;
//        }
//
//        public void setHeadImgUrl(String HeadImgUrl) {
//            this.HeadImgUrl = HeadImgUrl;
//        }
//
//        public void setAuthor(String Author) {
//            this.Author = Author;
//        }
//
//        public void setReplyNum(int ReplyNum) {
//            this.ReplyNum = ReplyNum;
//        }
//
//        public int getIndex() {
//            return Index;
//        }
//
//        public String getTitle() {
//            return Title;
//        }
//
//        public int getPostTime() {
//            return PostTime;
//        }
//
//        public String getHeadImgUrl() {
//            return HeadImgUrl;
//        }
//
//        public String getAuthor() {
//            return Author;
//        }
//
//        public int getReplyNum() {
//            return ReplyNum;
//        }
//    }
}

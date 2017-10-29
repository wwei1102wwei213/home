package com.zeyuan.kyq.bean;

import com.zeyuan.kyq.Entity.HomePageEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/9/2.
 * 获取当前主页的bean
 * GetMainPage
 */
public class MainPageInfoBean extends BaseBean implements Serializable {

    /**
     * StepUID : 5005
     * StepID : Step1
     * CureConfID : 2501
     * StepUserSelectIDs : ["123","234"]
     * DiscoverTime : 1234756789
     * SimilarcaseNum : [{"title":"title1","imageUrl":"http://www.url.com/1","index":"1111222","author":"abc"},{"title":"title1","imageUrl":"http://www.url.com/1","index":"11112222332","author":"abc的"}]
     * RecoverCenterNum : [{"title":"title1223213","imageUrl":"http://www.url.com/12dadas","Replynum":"1153123","index":"11112222332"},{"title":"title1223213","imageUrl":"http://www.url.com/12dadas","Replynum":"1153123","index":"11112222332"}]
     * Knowlege : [{"title":"title1223213","imageUrl":"http://www.url.com/12dadas","Replynum":"1153123","index":"11112222332"},{"title":"title1223213","imageUrl":"http://www.url.com/12dadas","Replynum":"1153123","index":"11112222332"}]
     * Effect : [{"title":"title1223213","imageUrl":"http://www.url.com/12dadas","Replynum":"1153123","index":"11112222332"},{"title":"title1223213","imageUrl":"http://www.url.com/12dadas","Replynum":"1153123","index":"11112222332"}]
     * NurseNum : [{"Pageid":"1","StepID":"2313"},{"Pageid":"2","effectid":["2313","2314","2315"]},{"Pageid":"3","Performid":"300"}]
     */

    private String Mobile;

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    private List<String> StepUserSelectIDs;//联合用药的id
    /**
     * title : title1
     * imageUrl : http://www.url.com/1
     * index : 1111222
     * author : abc
     */

    private List<SimilarcaseNumEntity> SimilarcaseNum;
    /**
     * title : title1223213
     * imageUrl : http://www.url.com/12dadas
     * Replynum : 1153123
     * index : 11112222332
     */

    private List<MainItemEntity> RecoverCenterNum;
    /**
     * title : title1223213
     * imageUrl : http://www.url.com/12dadas
     * Replynum : 1153123
     * index : 11112222332
     */

    private List<MainItemEntity> Knowlege;
    /**
     * title : title1223213
     * imageUrl : http://www.url.com/12dadas
     * Replynum : 1153123
     * index : 11112222332
     */

    private List<MainItemEntity> Effect;
    /**
     * Pageid : 1
     * StepID : 2313
     */

    private List<NurseNumEntity> NurseNum;

    private List<HeadListEntity> HeadList;

    private List<HomePageEntity> bannerList;

    private List<HomePageEntity> FbannerList;

    private List<HomePageEntity> tagBannerList;

    public List<HomePageEntity> getTagBannerList() {
        return tagBannerList;
    }

    public void setTagBannerList(List<HomePageEntity> tagBannerList) {
        this.tagBannerList = tagBannerList;
    }

    private String bannerListState;

    private String FbannerListState;

    public String getBannerListState() {
        return bannerListState;
    }

    public void setBannerListState(String bannerListState) {
        this.bannerListState = bannerListState;
    }

    public String getFbannerListState() {
        return FbannerListState;
    }

    public void setFbannerListState(String fbannerListState) {
        FbannerListState = fbannerListState;
    }

    public List<HomePageEntity> getFbannerList() {
        return FbannerList;
    }

    public void setFbannerList(List<HomePageEntity> fbannerList) {
        FbannerList = fbannerList;
    }

    public void setStepUserSelectIDs(List<String> StepUserSelectIDs) {
        this.StepUserSelectIDs = StepUserSelectIDs;
    }

    public void setSimilarcaseNum(List<SimilarcaseNumEntity> SimilarcaseNum) {
        this.SimilarcaseNum = SimilarcaseNum;
    }

    public void setRecoverCenterNum(List<MainItemEntity> RecoverCenterNum) {
        this.RecoverCenterNum = RecoverCenterNum;
    }

    public void setKnowlege(List<MainItemEntity> Knowlege) {
        this.Knowlege = Knowlege;
    }

    public void setEffect(List<MainItemEntity> Effect) {
        this.Effect = Effect;
    }

    public void setNurseNum(List<NurseNumEntity> NurseNum) {
        this.NurseNum = NurseNum;
    }


    public List<String> getStepUserSelectIDs() {
        return StepUserSelectIDs;
    }

    public List<SimilarcaseNumEntity> getSimilarcaseNum() {
        return SimilarcaseNum;
    }

    public List<MainItemEntity> getRecoverCenterNum() {
        return RecoverCenterNum;
    }

    public List<MainItemEntity> getKnowlege() {
        return Knowlege;
    }

    public List<MainItemEntity> getEffect() {
        return Effect;
    }

    public List<NurseNumEntity> getNurseNum() {
        return NurseNum;
    }

    public List<HeadListEntity> getHeadList() {
        return HeadList;
    }


    public void setHeadList(List<HeadListEntity> headList) {
        HeadList = headList;
    }

    private UpEntity Up;

    public UpEntity getUp() {
        return Up;
    }

    public void setUp(UpEntity up) {
        Up = up;
    }

    public static class UpEntity {
        private String u;//是否需要更新，数字，1代表强制更新，2代表不强制更新，3代表不更新
        private String m;//弹出文本框消息内容，字符串
        private String l;//需要更新的url地址
        private String v;//最新的版本号  字符串


        public String getM() {
            return m;
        }

        public void setM(String m) {
            this.m = m;
        }

        public String getL() {
            return l;
        }

        public void setL(String l) {
            this.l = l;
        }

        public String getV() {
            return v;
        }

        public void setV(String v) {
            this.v = v;
        }

        public String getU() {
            return u;
        }

        public void setU(String u) {
            this.u = u;
        }

        @Override
        public String toString() {
            return "UpEntity{" +
                    "u='" + u + '\'' +
                    ", m='" + m + '\'' +
                    ", l='" + l + '\'' +
                    ", v='" + v + '\'' +
                    '}';
        }
    }

    public static class HeadListEntity implements Serializable {
        private String from;
        private String PostTime;
        private String Title;
        private String ThumbURL;
        private String PageViewNum;
        private String ArticleIndex;

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

        @Override
        public String toString() {
            return "HeadListEntity{" +
                    "from='" + from + '\'' +
                    ", PostTime='" + PostTime + '\'' +
                    ", Title='" + Title + '\'' +
                    ", ThumbURL='" + ThumbURL + '\'' +
                    ", PageViewNum='" + PageViewNum + '\'' +
                    ", ArticleIndex='" + ArticleIndex + '\'' +
                    '}';
        }
    }


    public static class SimilarcaseNumEntity {
        private String Title;
        private String HeadImgUrl;
        private String Index;
        private String Author;

        public void setTitle(String title) {
            this.Title = title;
        }

        public void setImageUrl(String imageUrl) {
            this.HeadImgUrl = imageUrl;
        }

        public void setIndex(String index) {
            this.Index = index;
        }

        public void setAuthor(String author) {
            this.Author = author;
        }

        public String getTitle() {
            return Title;
        }

        public String getImageUrl() {
            return HeadImgUrl;
        }

        public String getIndex() {
            return Index;
        }

        public String getAuthor() {
            return Author;
        }
    }
//
//    public static class RecoverCenterNumEntity {
//        private String title;
//        private String imageUrl;
//        private String Replynum;
//        private String index;
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//        public void setImageUrl(String imageUrl) {
//            this.imageUrl = imageUrl;
//        }
//
//        public void setReplynum(String Replynum) {
//            this.Replynum = Replynum;
//        }
//
//        public void setIndex(String index) {
//            this.index = index;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public String getImageUrl() {
//            return imageUrl;
//        }
//
//        public String getReplynum() {
//            return Replynum;
//        }
//
//        public String getIndex() {
//            return index;
//        }
//    }

    public static class MainItemEntity {
        private String Title;//标题
        private String ThumbURL;//图片请求地址
        private String PageViewNum;
        private String ArticleIndex;
        private String Summary;

        public void setTitle(String title) {
            this.Title = title;
        }

        public void setImageUrl(String imageUrl) {
            this.ThumbURL = imageUrl;
        }

        public void setReplynum(String Replynum) {
            this.PageViewNum = Replynum;
        }

        public void setIndex(String index) {
            this.ArticleIndex = index;
        }

        public String getTitle() {
            return Title;
        }

        public String getImageUrl() {
            return ThumbURL;
        }

        public String getReplynum() {
            return PageViewNum;
        }

        public String getIndex() {
            return ArticleIndex;
        }

        public String getSummary() {
            return Summary;
        }

        public void setSummary(String summary) {
            Summary = summary;
        }

        @Override
        public String toString() {
            return "MainItemEntity{" +
                    "title='" + Title + '\'' +
                    ", imageUrl='" + ThumbURL + '\'' +
                    ", Replynum='" + PageViewNum + '\'' +
                    ", index='" + ArticleIndex + '\'' +
                    '}';
        }
    }

//    public static class EffectEntity {
//        private String title;
//        private String imageUrl;
//        private String Replynum;
//        private String index;
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//        public void setImageUrl(String imageUrl) {
//            this.imageUrl = imageUrl;
//        }
//
//        public void setReplynum(String Replynum) {
//            this.Replynum = Replynum;
//        }
//
//        public void setIndex(String index) {
//            this.index = index;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public String getImageUrl() {
//            return imageUrl;
//        }
//
//        public String getReplynum() {
//            return Replynum;
//        }
//
//        public String getIndex() {
//            return index;
//        }
//    }

    public static class NurseNumEntity {
        private String Pageid;
        private String StepID;
        private String Performid;
        /**
         * 症状id
         */
        private String Content;
        private String Type;//1.默认文本
        // 2.普通文本
        //3.文章
        //4.帖子


        public String getType() {
            return Type;
        }

        public void setType(String type) {
            Type = type;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String content) {
            Content = content;
        }

        public String getPerformid() {
            return Performid;
        }

        public void setPerformid(String performid) {
            Performid = performid;
        }

        public void setPageid(String Pageid) {
            this.Pageid = Pageid;
        }

        public void setStepID(String StepID) {
            this.StepID = StepID;
        }

        public String getPageid() {
            return Pageid;
        }

        public String getStepID() {
            return StepID;
        }

        @Override
        public String toString() {
            return "NurseNumEntity{" +
                    "Pageid='" + Pageid + '\'' +
                    ", StepID='" + StepID + '\'' +
                    ", Performid='" + Performid + '\'' +
                    ", Content='" + Content + '\'' +
                    ", Type='" + Type + '\'' +
                    '}';
        }
    }

    public List<HomePageEntity> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<HomePageEntity> bannerList) {
        this.bannerList = bannerList;
    }

    private List<String> ArticleDesc;

    public List<String> getArticleDesc() {
        return ArticleDesc;
    }

    public void setArticleDesc(List<String> articleDesc) {
        ArticleDesc = articleDesc;
    }

    //积分数据
    private int integral;//用户积分总数
    private String loginInfo;
    private int gzNum;//被关注数量
    private int gzJf;//被关注的积分系数
    private int scNum;//被收藏次数
    private int scJf;//被收藏加积分的系数
    private int jhNum;//被加精华的帖子数量
    private int jhJf;//帖子被加精华的加积分的系数
    private int jf;


    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public String getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(String loginInfo) {
        this.loginInfo = loginInfo;
    }

    public int getGzNum() {
        return gzNum;
    }

    public void setGzNum(int gzNum) {
        this.gzNum = gzNum;
    }

    public int getGzJf() {
        return gzJf;
    }

    public void setGzJf(int gzJf) {
        this.gzJf = gzJf;
    }

    public int getScNum() {
        return scNum;
    }

    public void setScNum(int scNum) {
        this.scNum = scNum;
    }

    public int getScJf() {
        return scJf;
    }

    public void setScJf(int scJf) {
        this.scJf = scJf;
    }

    public int getJhNum() {
        return jhNum;
    }

    public void setJhNum(int jhNum) {
        this.jhNum = jhNum;
    }

    public int getJhJf() {
        return jhJf;
    }

    public void setJhJf(int jhJf) {
        this.jhJf = jhJf;
    }

    public int getJf() {
        return jf;
    }

    public void setJf(int jf) {
        this.jf = jf;
    }

    @Override
    public String toString() {
        return "MainPageInfoBean{" +
                "Mobile='" + Mobile + '\'' +
                ", StepUserSelectIDs=" + StepUserSelectIDs +
                ", SimilarcaseNum=" + SimilarcaseNum +
                ", RecoverCenterNum=" + RecoverCenterNum +
                ", Knowlege=" + Knowlege +
                ", Effect=" + Effect +
                ", NurseNum=" + NurseNum +
                ", HeadList=" + HeadList +
                ", bannerList=" + bannerList +
                ", FbannerList=" + FbannerList +
                ", tagBannerList=" + tagBannerList +
                ", bannerListState='" + bannerListState + '\'' +
                ", FbannerListState='" + FbannerListState + '\'' +
                ", Up=" + Up +
                ", ArticleDesc=" + ArticleDesc +
                '}';
    }
}

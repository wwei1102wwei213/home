package com.zeyuan.kyq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/9/7.
 * 10.获取当前用户相似案例详情
 */
public class SimilarCaseBean implements Serializable {

    /**
     * iResult : 0
     * CircleId : 10001
     * data : [{"ImageUrl":"http://img0.imgtn.bdimg.com/it/u=3181282067,3590745083&fm=21&gp=0.jpg","Author":"作者","PostTime":1441690384,"Index":"1005","ReplyNum":"998","PostType ":"2","Title":"标题"},{"ImageUrl":"http://img0.imgtn.bdimg.com/it/u=3181282067,3590745083&fm=21&gp=0.jpg","Author":"作者","PostTime":1441630384,"Index":"1001","ReplyNum":"998","PostType":"2","Title":"标题"},{"ImageUrl":"http://img0.imgtn.bdimg.com/it/u=3181282067,3590745083&fm=21&gp=0.jpg","Author":"作者","PostTime":1441660384,"Index":"1002","ReplyNum":"998","PostType":"2","Title":"标题"},{"ImageUrl":"http://img0.imgtn.bdimg.com/it/u=3181282067,3590745083&fm=21&gp=0.jpg","Author":"作者","PostTime":1441690384,"Index":"2005","ReplyNum":"998","PostType":"1","Title":"标题"},{"ImageUrl":"http://img0.imgtn.bdimg.com/it/u=3181282067,3590745083&fm=21&gp=0.jpg","Author":"作者","PostTime":1441630384,"Index":"2001","CircleId":"10001","ReplyNum":"998","PostType":"1","Title":"标题"}]
     */

    private String iResult;
    private String CircleId;
    private int countNum;
    private List<DataEntity> data;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setCircleId(String CircleId) {
        this.CircleId = CircleId;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public String getIResult() {
        return iResult;
    }

    public String getCircleId() {
        return CircleId;
    }

    public int getCountNum() {
        return countNum;
    }

    public void setCountNum(int countNum) {
        this.countNum = countNum;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * ImageUrl : http://img0.imgtn.bdimg.com/it/u=3181282067,3590745083&fm=21&gp=0.jpg
         * Author : 作者
         * PostTime : 1441690384
         * Index : 1005
         * ReplyNum : 998
         * PostType  : 2
         * Title : 标题
         */
        /**
         * {
         * "is_care":"0",
         * "InfoID":111080,
         * "InfoName":"父亲肺鳞癌",
         * "HeaderUrl":"http://zeyuan1.oss-cn-shenzhen.aliyuncs.com/111080HeadImg147528928715030095702.png",
         * "Percentage":175,
         * "CancerID":31,
         * "StepID":669,
         * "TransferRecord":"骨头,脑,其他->骨头,脑,其他->骨头,脑,其他->骨头,脑,其他->骨头,脑,其他->骨头,脑,其他->骨头,脑,其他",
         * "TransferGene":"EGFR->EGFR"
         * }
         */
        private String is_care;
        private String CancerID;
        private String InfoID;
        private String InfoName;
        private String HeaderUrl;
        private String Percentage;
        private String StepID;
        private String TransferRecord;
        private String TransferGene;

        private String ImageUrl;
        private String Author;
        private int PostTime;
        private String Index;
        private String ReplyNum;
        private String PostType;
        private String Title;

        public String getIs_care() {
            return is_care;
        }

        public void setIs_care(String is_care) {
            this.is_care = is_care;
        }

        public String getCancerID() {
            return CancerID;
        }

        public void setCancerID(String cancerID) {
            CancerID = cancerID;
        }

        public String getInfoID() {
            return InfoID;
        }

        public void setInfoID(String infoID) {
            InfoID = infoID;
        }

        public String getInfoName() {
            return InfoName;
        }

        public void setInfoName(String infoName) {
            InfoName = infoName;
        }

        public String getHeaderUrl() {
            return HeaderUrl;
        }

        public void setHeaderUrl(String headerUrl) {
            HeaderUrl = headerUrl;
        }

        public String getPercentage() {
            return Percentage;
        }

        public void setPercentage(String percentage) {
            Percentage = percentage;
        }

        public String getStepID() {
            return StepID;
        }

        public void setStepID(String stepID) {
            StepID = stepID;
        }

        public String getTransferRecord() {
            return TransferRecord;
        }

        public void setTransferRecord(String transferRecord) {
            TransferRecord = transferRecord;
        }

        public String getTransferGene() {
            return TransferGene;
        }

        public void setTransferGene(String transferGene) {
            TransferGene = transferGene;
        }

        public void setImageUrl(String ImageUrl) {
            this.ImageUrl = ImageUrl;
        }

        public void setAuthor(String Author) {
            this.Author = Author;
        }

        public void setPostTime(int PostTime) {
            this.PostTime = PostTime;
        }

        public void setIndex(String Index) {
            this.Index = Index;
        }

        public void setReplyNum(String ReplyNum) {
            this.ReplyNum = ReplyNum;
        }

        public void setPostType(String PostType) {
            this.PostType = PostType;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getImageUrl() {
            return ImageUrl;
        }

        public String getAuthor() {
            return Author;
        }

        public int getPostTime() {
            return PostTime;
        }

        public String getIndex() {
            return Index;
        }

        public String getReplyNum() {
            return ReplyNum;
        }

        public String getPostType() {
            return PostType;
        }

        public String getTitle() {
            return Title;
        }
    }


    @Override
    public String toString() {
        return "SimilarCaseBean [iResult=" + iResult + ", CircleId=" + CircleId
                + ", data=" + data + "]";
    }
}

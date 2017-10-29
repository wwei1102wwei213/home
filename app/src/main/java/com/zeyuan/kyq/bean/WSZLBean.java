package com.zeyuan.kyq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-12-22
 * Time: 14:46
 * FIXME
 */


public class WSZLBean implements Serializable {

    /**
     * iResult : 0
     * uiSetBit : 98
     * LastTargetID : ["950","951"]
     * LastChemistryID : ["1000048","1000049"]
     * SelfContent : [{"SelfID":"468","Content":"多处复发或者其他部位有转移111"},{"SelfID":"469","Content":"多处复发或者其他部位有转移22233333"}]
     * SelfSelect : [{"QuestionID":"1000","Content":"患者化疗现状","Answer":[{"AnswerID":"5001","Content":"化疗六个月以上复发"},{"AnswerID":"5002","Content":"化疗三个月至六个月内复发"},{"AnswerID":"5003","Content":"化疗三个月内复发"}]}]
     */

    private String iResult;
    private String uiSetBit;
    private List<String> LastTargetID;
    private List<String> LastChemistryID;
    /**
     * SelfID : 468
     * Content : 多处复发或者其他部位有转移111
     */

    private List<SelfContentEntity> SelfContent;


    /**
     * QuestionID : 1000
     * Content : 患者化疗现状
     * Answer : [{"AnswerID":"5001","Content":"化疗六个月以上复发"},{"AnswerID":"5002","Content":"化疗三个月至六个月内复发"},{"AnswerID":"5003","Content":"化疗三个月内复发"}]
     */

    private List<SelfSelectEntity> SelfSelect;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setUiSetBit(String uiSetBit) {
        this.uiSetBit = uiSetBit;
    }

    public void setLastTargetID(List<String> LastTargetID) {
        this.LastTargetID = LastTargetID;
    }

    public void setLastChemistryID(List<String> LastChemistryID) {
        this.LastChemistryID = LastChemistryID;
    }

    public void setSelfContent(List<SelfContentEntity> SelfContent) {
        this.SelfContent = SelfContent;
    }

    public void setSelfSelect(List<SelfSelectEntity> SelfSelect) {
        this.SelfSelect = SelfSelect;
    }

    public String getIResult() {
        return iResult;
    }

    public String getUiSetBit() {
        return uiSetBit;
    }

    public List<String> getLastTargetID() {
        return LastTargetID;
    }

    public List<String> getLastChemistryID() {
        return LastChemistryID;
    }

    public List<SelfContentEntity> getSelfContent() {
        return SelfContent;
    }

    public List<SelfSelectEntity> getSelfSelect() {
        return SelfSelect;
    }

    /**
     * 自定义文本
     */

    public static class SelfContentEntity implements Serializable {
        private String SelfID;
        private String Content;
        public void setSelfID(String SelfID) {
            this.SelfID = SelfID;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }

        public String getSelfID() {
            return SelfID;
        }

        public String getContent() {
            return Content;
        }

        @Override
        public String toString() {
            return "SelfContentEntity{" +
                    "SelfID='" + SelfID + '\'' +
                    ", Content='" + Content + '\'' +
                    '}';
        }
    }

    /**
     * 自定义单选
     */
    public static class SelfSelectEntity implements Serializable {
        private String QuestionID;
        private String Content;
        /**
         * AnswerID : 5001
         * Content : 化疗六个月以上复发
         */

        private List<AnswerEntity> Answer;

        public void setQuestionID(String QuestionID) {
            this.QuestionID = QuestionID;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }

        public void setAnswer(List<AnswerEntity> Answer) {
            this.Answer = Answer;
        }

        public String getQuestionID() {
            return QuestionID;
        }

        public String getContent() {
            return Content;
        }

        public List<AnswerEntity> getAnswer() {
            return Answer;
        }

        public static class AnswerEntity implements Serializable{
            private String AnswerID;
            private String Content;

            public void setAnswerID(String AnswerID) {
                this.AnswerID = AnswerID;
            }

            public void setContent(String Content) {
                this.Content = Content;
            }

            public String getAnswerID() {
                return AnswerID;
            }

            public String getContent() {
                return Content;
            }

            @Override
            public String toString() {
                return "AnswerEntity{" +
                        "AnswerID='" + AnswerID + '\'' +
                        ", Content='" + Content + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "SelfSelectEntity{" +
                    "QuestionID='" + QuestionID + '\'' +
                    ", Content='" + Content + '\'' +
                    ", Answer=" + Answer.toString() +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "WSZLBean{" +
                "iResult='" + iResult + '\'' +
                ", uiSetBit='" + uiSetBit + '\'' +
                ", LastTargetID=" + LastTargetID +
                ", LastChemistryID=" + LastChemistryID +
                ", SelfContent=" + SelfContent.toString() +
                ", SelfSelect=" + SelfSelect.toString() +
                '}';
    }
}

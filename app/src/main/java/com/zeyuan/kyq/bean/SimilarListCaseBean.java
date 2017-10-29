package com.zeyuan.kyq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/9/9.
 * 获取当前用户相似案例列表
 */
public class SimilarListCaseBean implements Serializable{

    /**
     * iResult : 0
     * listnum : [{"index":123456789,"Title":"Title1","Author":"Author0","Posttime":123456890,"Replynum":100},{"index":123456589,"Title":"Title2","Author":"Author2","Posttime":123456990,"Replynum":110},{"index":123436589,"Title":"Title3","Author":"Author3","Posttime":123436990,"Replynum":111}]
     */

    private String iResult;
    private List<ListnumEntity> listnum;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setListnum(List<ListnumEntity> listnum) {
        this.listnum = listnum;
    }

    public String getIResult() {
        return iResult;
    }

    public List<ListnumEntity> getListnum() {
        return listnum;
    }

    public static class ListnumEntity {
        /**
         * index : 123456789
         * Title : Title1
         * Author : Author0
         * Posttime : 123456890
         * Replynum : 100
         */

        private int index;
        private String Title;
        private String Author;
        private int Posttime;
        private int Replynum;

        public void setIndex(int index) {
            this.index = index;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public void setAuthor(String Author) {
            this.Author = Author;
        }

        public void setPosttime(int Posttime) {
            this.Posttime = Posttime;
        }

        public void setReplynum(int Replynum) {
            this.Replynum = Replynum;
        }

        public int getIndex() {
            return index;
        }

        public String getTitle() {
            return Title;
        }

        public String getAuthor() {
            return Author;
        }

        public int getPosttime() {
            return Posttime;
        }

        public int getReplynum() {
            return Replynum;
        }
    }


    @Override
    public String toString() {
        return "SimilarListCaseBean [iResult=" + iResult + ", listnum="
                + listnum + "]";
    }
}

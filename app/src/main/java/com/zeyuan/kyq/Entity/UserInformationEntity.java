package com.zeyuan.kyq.Entity;

import java.util.List;

/**
 * Created by Administrator on 2016/11/7.
 *
 *
 * @author wwei
 */
public class UserInformationEntity {

    private String iResult;
    private List<InformationEntity> data;
    private List<HelpItemEntity> help;
    private List<ArticleTypeEntity> catShowList;
    private List<ArticleTypeEntity> catHideList;
    private List<SameEntity> taglist;
    private List<SameEntity> steplist;

    public String getiResult() {
        return iResult;
    }

    public List<InformationEntity> getData() {
        return data;
    }

    public void setData(List<InformationEntity> data) {
        this.data = data;
    }

    public List<HelpItemEntity> getHelp() {
        return help;
    }

    public void setHelp(List<HelpItemEntity> help) {
        this.help = help;
    }

    public List<ArticleTypeEntity> getCatShowList() {
        return catShowList;
    }

    public void setCatShowList(List<ArticleTypeEntity> catShowList) {
        this.catShowList = catShowList;
    }

    public List<ArticleTypeEntity> getCatHideList() {
        return catHideList;
    }

    public void setCatHideList(List<ArticleTypeEntity> catHideList) {
        this.catHideList = catHideList;
    }

    public List<SameEntity> getTaglist() {
        return taglist;
    }

    public void setTaglist(List<SameEntity> taglist) {
        this.taglist = taglist;
    }

    public List<SameEntity> getSteplist() {
        return steplist;
    }

    public void setSteplist(List<SameEntity> steplist) {
        this.steplist = steplist;
    }

    @Override
    public String toString() {
        return "UserInformationEntity{" +
                "iResult='" + iResult + '\'' +
                ", data=" + data +
                ", help=" + help +
                ", catShowList=" + catShowList +
                ", catHideList=" + catHideList +
                ", taglist=" + taglist +
                ", steplist=" + steplist +
                '}';
    }
}

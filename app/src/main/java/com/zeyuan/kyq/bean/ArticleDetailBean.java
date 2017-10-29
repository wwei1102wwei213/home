package com.zeyuan.kyq.bean;

import com.zeyuan.kyq.utils.DecryptUtils;

import java.io.Serializable;
import java.util.List;

/**
 * User: zeyuan
 * Date: 2015-12-15
 * Time: 17:21
 * FIXME
 */

/***
 * 文章详情类
 */
public class ArticleDetailBean implements Serializable{

    /**
     * iResult : 0
     * ArticleID : 7
     * Articlenum : [{"ContentType":"1","Content":"5L2T6YOo5Ly9546b5YiA6YCC5a6c6IK655mM44CB6IKd55mM44CB6IOw6IW655mM44CB6IK+5LiK6IW66IK/55ik44CB6IK+55mM44CB57q16ZqU6IK/55ik44CB6IW56Iac5ZCO6IK/55ik5Y+K55uG6IWU6IK/55ik562J55qE5rK755aX44CC5L2T6YOo5Ly9546b5YiA5Y+v5Y2V54us55So5LqO5bCP6IK/55ik5qC55rK75rK755aX77yM5Lmf5Y+v57uT5ZCI5aSa56eN5pS+55aX5oqA5pyv44CCDQogICAgICDkvZPpg6jkvL3njpvliIDlnKjmsrvnlpfml7bkuI3lvIDliIDjgIHkuI3purvphonvvIzml6DliJvkvKTjgIHlia/kvZznlKjovbvvvIzml6DpnIDkvY/pmaLvvIzlm6DmraTnibnliKvpgILlrpzlubTogIHkvZPlvLHkuI3og73ogJDlj5fmiYvmnK/nmoTmgqPogIXmiJblm6DmgZDmg6fmi5Lnu53miYvmnK/nmoTmgqPogIXnmoTmsrvnlpfjgII="},{"ContentType":"2","ImageW":"510","ImageH":"383","URL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14485290089924.jpg"},{"ContentType":"1","Content":"5Ly9546b5YiA5Y+I56ew56uL5L2T5a6a5ZCR5Ly9546b5bCE57q/5pS+5bCE5rK755aX57O757uf77yM5piv5LiA56eN6J6N5ZCI546w5Luj6K6h566X5py65oqA5pyv44CB56uL5L2T5a6a5ZCR5oqA5pyv5ZKM5aSW56eR5oqA5pyv5LqO5LiA5L2T55qE5rK755aX5oCn6K6+5aSH77yM5a6D5bCG6ZK0LTYw5Y+R5Ye655qE5Ly9546b5bCE57q/5Yeg5L2V6IGa54Sm77yM6ZuG5Lit5bCE5LqO55eF54G277yM5LiA5qyh5oCn44CB6Ie05q275oCn55qE5pGn5q+B6Z2254K55YaF55qE57uE57uH77yM6ICM5bCE57q/57uP6L+H5Lq65L2T5q2j5bi457uE57uH5Yeg5LmO5peg5Lyk5a6z77yM5bm25LiU5YmC6YeP6ZSQ5YeP77yM5Zug5q2k5YW25rK755aX54Wn5bCE6IyD5Zu05LiO5q2j5bi457uE57uH55WM6ZmQ6Z2e5bi45piO5pi+77yM6L6557yY5aaC5YiA5Ymy5LiA5qC377yM5Lq65Lus5b2i6LGh55qE56ew5LmL5Li64oCc5Ly9546b5YiA4oCd44CC5Ly9546b5YiA5bm25LiN5piv55yf5q2j55qE5omL5pyv5YiA77yM6ICM5piv5LiA56eN6Z2e5bi45YWI6L+b55qE5pS+5bCE5rK755aX6K6+5aSH77yM5YW25YWo56ew5piv77ya5Ly9546b5bCE57q/56uL5L2T5a6a5ZCR5rK755aX57O757uf44CC5a6D5bCG6K645aSa5p2f5b6I57uG55qE5Ly9546b5bCE57q/5LuO5LiN5ZCM55qE6KeS5bqm5ZKM5pa55ZCR54Wn5bCE6L+H5Lq65L2T77yM5bm25L2/5a6D5Lus6YO95Zyo5LiA54K55LiK5rGH6IGa6LW35p2l5b2i5oiQ54Sm54K544CC5pyJ5aS06YOo5Ly9546b5YiA5ZKM5L2T6YOo5Ly9546b5YiA44CC"},{"ContentType":"2","ImageW":"2044","ImageH":"1541","URL":"http://oss-cn-qingdao.aliyuncs.com/bucketn1/14485288132076.jpg"},{"ContentType":"1","Content":"6YCC55So6IyD5Zu057yW6L6RDQogICAgICAg5Ly9546b5YiA5YiG5Li65aS06YOo5Ly9546b5YiA5ZKM5L2T6YOo5Ly9546b5YiA44CC5aS06YOo5Ly9546b5YiA5pyJ6Z2Z5oCB5byP5Ly9546b5YiA5ZKM5peL6L2s5byP5Ly9546b5YiA77yM6Z2Z5oCB5byP5Ly9546b5YiA5piv5bCG5aSa5Liq6ZK05rqQ5a6J6KOF5Zyo5LiA5Liq55CD5Z6L5aS055uU5YaF77yM5L2/5LmL6IGa54Sm5LqO6aKF5YaF55qE5p+Q5LiA54K577yM5peL6L2s5byP5Ly9546b5YiA5piv5Zyo6Z2Z5oCB5byP55qE5Z+656GA5LiK5pS56L+b6ICM5p2l77yM5YW35aSH6K645aSa5LyY54K577yM5piv5Lit5Zu955qE5LiT5Yip44CCDQogICAgICDlpLTpg6jkvL3njpvliIDlj6/ml6DliJvmoLnmsrvkuInlj4nnpZ7nu4/nl5vvvIjkuInlj4nnpZ7nu4/nlrzvvInjgIHog7botKjnmKTjgIHohJHohpznmKTjgIHlkKznpZ7nu4/nmKTjgIHlnoLkvZPnmKTjgIHpooXlkr3nrqHnmKTnrYnjgIINCiAgICAgIOS9k+mDqOS8veeOm+WIgOS4u+imgeeUqOS6juayu+eWl+WFqOi6q+WQhOenjeiCv+eYpOOAgg0KICAgICAgMeOAgTMw5q+r57Gz5Lul5LiL55qE5ZCs56We57uP55ik44CB5Z6C5L2T55ik5ZKM6ISR6Iac55ik562J6aKF5YaF6Imv5oCn6IK/55ik77ybDQogICAgICAy44CB5bCP6ICM5rex55qE6aKF5YaF5Yqo6Z2Z6ISJ55W45b2i77ybDQogICAgICAz44CB5LiA5Lqb5omL5pyv5LiN6IO95YiH6Zmk5bmy5YeA55qE6ImvKuiCv+eYpO+8mw0KICAgICAgNOOAgei+g+Wwj+iAjOi+uee8mOa4healmueahOmiheWGhei9rOenu+eZjO+8mw0KICAgICAgNeOAgeW4lemHkeajruawj+eXheOAgeeZq+eXq+eXheOAgeeyvuelnueXheetieWKn+iDveaAp+eWvueXheOAgg=="}]
     */

    private String iResult;
    private String ArticleID;
    private String Title;
    private String PostTime;


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

    /**
     * ContentType : 1
     * Content : 5L2T6YOo5Ly9546b5YiA6YCC5a6c6IK655mM44CB6IKd55mM44CB6IOw6IW655mM44CB6IK+5LiK6IW66IK/55ik44CB6IK+55mM44CB57q16ZqU6IK/55ik44CB6IW56Iac5ZCO6IK/55ik5Y+K55uG6IWU6IK/55ik562J55qE5rK755aX44CC5L2T6YOo5Ly9546b5YiA5Y+v5Y2V54us55So5LqO5bCP6IK/55ik5qC55rK75rK755aX77yM5Lmf5Y+v57uT5ZCI5aSa56eN5pS+55aX5oqA5pyv44CCDQogICAgICDkvZPpg6jkvL3njpvliIDlnKjmsrvnlpfml7bkuI3lvIDliIDjgIHkuI3purvphonvvIzml6DliJvkvKTjgIHlia/kvZznlKjovbvvvIzml6DpnIDkvY/pmaLvvIzlm6DmraTnibnliKvpgILlrpzlubTogIHkvZPlvLHkuI3og73ogJDlj5fmiYvmnK/nmoTmgqPogIXmiJblm6DmgZDmg6fmi5Lnu53miYvmnK/nmoTmgqPogIXnmoTmsrvnlpfjgII=
     */

    private List<ArticlenumEntity> Articlenum;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setArticleID(String ArticleID) {
        this.ArticleID = ArticleID;
    }

    public void setArticlenum(List<ArticlenumEntity> Articlenum) {
        this.Articlenum = Articlenum;
    }

    public String getIResult() {
        return iResult;
    }

    public String getArticleID() {
        return ArticleID;
    }

    public List<ArticlenumEntity> getArticlenum() {
        return Articlenum;
    }

    public static class ArticlenumEntity {
        private String ContentType;
        private String Content;
        private String ImageW;
        private String ImageH;
        private String URL;

        public String getImageW() {
            return ImageW;
        }

        public void setImageW(String imageW) {
            ImageW = imageW;
        }

        public String getImageH() {
            return ImageH;
        }

        public void setImageH(String imageH) {
            ImageH = imageH;
        }

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }

        public void setContentType(String ContentType) {
            this.ContentType = ContentType;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }

        public String getContentType() {
            return ContentType;
        }

        public String getContent() {
            return DecryptUtils.decodeBase64(Content);

//            return Content;
        }

        @Override
        public String toString() {
            return "ArticlenumEntity{" +
                    "ContentType='" + ContentType + '\'' +
                    ", Content='" + Content + '\'' +
                    ", ImageW='" + ImageW + '\'' +
                    ", ImageH='" + ImageH + '\'' +
                    ", URL='" + URL + '\'' +
                    '}';
        }
    }



    @Override
    public String toString() {
        return "ArticleDetailBean{" +
                "iResult='" + iResult + '\'' +
                ", ArticleID='" + ArticleID + '\'' +
                ", Articlenum=" + Articlenum + '\'' +
                ", PostTime=" + PostTime+
                '}';
    }
}

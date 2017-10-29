package com.zeyuan.kyq.bean;

import com.zeyuan.kyq.utils.DecryptUtils;

import java.io.Serializable;
import java.util.List;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-12-23
 * Time: 14:36
 * FIXME肿瘤决策树的结果
 */


public class CancerResuletBean implements Serializable{

    /**
     * iResult : 0
     * Step : [{"Combineid":"26","alias":"VEGFR靶向药","IsTry":"0","CombineStepid":[{"stepid":"574","IsTry":"224","templateid":"17"},{"stepid":"575","IsTry":"224","templateid":"17"},{"stepid":"570","IsTry":"224","templateid":"17"},{"stepid":"598","IsTry":"224","templateid":"17"},{"stepid":"581","IsTry":"224","templateid":"17"},{"stepid":"584","IsTry":"224","templateid":"17"},{"stepid":"572","IsTry":"224","templateid":"17"},{"stepid":"562","IsTry":"224","templateid":"17"}]},{"Combineid":"34","alias":"肺腺癌免疫方案","IsTry":"0","CombineStepid":[{"stepid":"617","IsTry":"224","templateid":"83"},{"stepid":"622","IsTry":"224","templateid":"83"},{"stepid":"615","IsTry":"224","templateid":"83"},{"stepid":"624","IsTry":"224","templateid":"83"},{"stepid":"616","IsTry":"224","templateid":"83"},{"stepid":"614","IsTry":"224","templateid":"83"},{"stepid":"623","IsTry":"224","templateid":"83"},{"stepid":"1000077","IsTry":"224","templateid":"83"}]}]
     * Conclusion : 6IK65Yqf6IO95qOA5rWL77yM5pSv5rCU566h6ZWc5qOA5p+l77yM57q16ZqU5reL5be057uT55eF55CG6K+E5Lyw77yMUEVUL0NU5omr5o+P
     * Summary : VkVHRlLlnKhOU0NMQ+S4remrmOihqOi+vizpmLPmgKfnjofkuLo2My4xJSzkuJTor6Xln7rlm6DnmoTooajovr7kuI7ogr/nmKTliIbljJbnqIsg5bqm44CBVE5N5YiG5pyf44CB5reL5be057uT6L2s56e744CB56We57uP5L616KKt5pyJ5pi+6JGX55u45YWz5oCn44CCVkVHRlLnmoTooajovr7kuI5OU0NMQ+eahOmihOWQjuebuOWFsyzpmLPmgKfooajovr7or6Xln7rlm6DnmoTmgqPogIXnmoTlubPlnYfkuK3kvY3nlJ/lrZjmnJ8g5q+U6Zi05oCn6KGo6L6+55qE5oKj6ICF6ZW/MTLkuKrmnIjjgILnu5Porro6VkVHRlItMuWfuuWboOWcqOmdnuWwj+e7huiDnuiCuueZjOeahOWPkeeUn+OAgeWPkeWxlei/h+eoi+WPiumihOWQjuWFt+aciemHjeimgeS9nOeUqOOAgnZlZ2Zy5oqR5Yi25YmC5piv5LuO6KGA566h5YaF6YOo5oqR5Yi25paw55qE6KGA566h5b2i5oiQ77yM5pyJ6L6D5by655qE5Ymv5L2c55So77yM55uu5YmN5pu05aSa55qE5L2/55So5Zyo5LiJ5Zub57q/5rK755aX5LiK6Z2i44CC
     */

    private String iResult;
    private String Conclusion;
    private String Summary;
    /**
     * Combineid : 26
     * alias : VEGFR靶向药
     * IsTry : 0
     * CombineStepid : [{"stepid":"574","IsTry":"224","templateid":"17"},{"stepid":"575","IsTry":"224","templateid":"17"},{"stepid":"570","IsTry":"224","templateid":"17"},{"stepid":"598","IsTry":"224","templateid":"17"},{"stepid":"581","IsTry":"224","templateid":"17"},{"stepid":"584","IsTry":"224","templateid":"17"},{"stepid":"572","IsTry":"224","templateid":"17"},{"stepid":"562","IsTry":"224","templateid":"17"}]
     */

    private List<StepEntity> Step;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setConclusion(String Conclusion) {
        this.Conclusion = Conclusion;
    }

    public void setSummary(String Summary) {
        this.Summary = Summary;
    }

    public void setStep(List<StepEntity> Step) {
        this.Step = Step;
    }

    public String getIResult() {
        return iResult;
    }

    public String getConclusion() {
        return DecryptUtils.decodeBase64(Conclusion);
    }

    public String getSummary() {
        return DecryptUtils.decodeBase64(Summary);
    }

    public List<StepEntity> getStep() {
        return Step;
    }

    public static class StepEntity implements Serializable{
        private String stepid;
        private String Combineid;
        private String alias;
        private String IsTry;
        private String templateid;
        private String CureConfID;

        public String getCureConfID() {
            return CureConfID;
        }

        public void setCureConfID(String cureConfID) {
            CureConfID = cureConfID;
        }

        public String getStepid() {
            return stepid;
        }

        public void setStepid(String stepid) {
            this.stepid = stepid;
        }

        public String getTemplateid() {
            return templateid;
        }

        public void setTemplateid(String templateid) {
            this.templateid = templateid;
        }

        /**
         * stepid : 574
         * IsTry : 224
         * templateid : 17
         */

        private List<CombineStepidEntity> CombineStepid;

        public void setCombineid(String Combineid) {
            this.Combineid = Combineid;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public void setIsTry(String IsTry) {
            this.IsTry = IsTry;
        }

        public void setCombineStepid(List<CombineStepidEntity> CombineStepid) {
            this.CombineStepid = CombineStepid;
        }

        public String getCombineid() {
            return Combineid;
        }

        public String getAlias() {
            return alias;
        }

        public String getIsTry() {
            return IsTry;
        }

        public List<CombineStepidEntity> getCombineStepid() {
            return CombineStepid;
        }

        public static class CombineStepidEntity implements Serializable{
            private String stepid;
            private String IsTry;
            private String templateid;

            public void setStepid(String stepid) {
                this.stepid = stepid;
            }

            public void setIsTry(String IsTry) {
                this.IsTry = IsTry;
            }

            public void setTemplateid(String templateid) {
                this.templateid = templateid;
            }

            public String getStepid() {
                return stepid;
            }

            public String getIsTry() {
                return IsTry;
            }

            public String getTemplateid() {
                return templateid;
            }
        }

        @Override
        public String toString() {
            return "StepEntity{" +
                    "stepid='" + stepid + '\'' +
                    ", Combineid='" + Combineid + '\'' +
                    ", alias='" + alias + '\'' +
                    ", IsTry='" + IsTry + '\'' +
                    ", templateid='" + templateid + '\'' +
                    ", CureConfID='" + CureConfID + '\'' +
                    ", CombineStepid=" + CombineStepid +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CancerResuletBean{" +
                "iResult='" + iResult + '\'' +
                ", Conclusion='" + Conclusion + '\'' +
                ", Summary='" + Summary + '\'' +
                ", Step=" + Step +
                '}';
    }
}

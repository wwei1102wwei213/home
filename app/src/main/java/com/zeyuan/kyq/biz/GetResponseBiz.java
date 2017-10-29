package com.zeyuan.kyq.biz;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.zeyuan.kyq.Entity.BindMobileEntity;
import com.zeyuan.kyq.Entity.CreateInfoBean;
import com.zeyuan.kyq.Entity.EventEntity;
import com.zeyuan.kyq.Entity.QuotaDataEntity;
import com.zeyuan.kyq.Entity.SearchPolicyEntity;
import com.zeyuan.kyq.bean.AddStepBean;
import com.zeyuan.kyq.bean.ArticleDetailBean;
import com.zeyuan.kyq.bean.ArticleListBean;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.bean.CancerResuletBean;
import com.zeyuan.kyq.bean.CaseDetailBean;
import com.zeyuan.kyq.bean.CityCancerForumBean;
import com.zeyuan.kyq.bean.CommBean;
import com.zeyuan.kyq.bean.FindSymtomBean;
import com.zeyuan.kyq.bean.FlwForumBean;
import com.zeyuan.kyq.bean.FollowBean;
import com.zeyuan.kyq.bean.ForumDetailBean;
import com.zeyuan.kyq.bean.ForumListBean;
import com.zeyuan.kyq.bean.HeadLineBean;
import com.zeyuan.kyq.bean.LoginZYBean;
import com.zeyuan.kyq.bean.MainPageInfoBean;
import com.zeyuan.kyq.bean.MyCircleBean;
import com.zeyuan.kyq.bean.MyDataBean;
import com.zeyuan.kyq.bean.MyForumReleaseBean;
import com.zeyuan.kyq.bean.MyReplyListBean;
import com.zeyuan.kyq.bean.PatientDetailBean;
import com.zeyuan.kyq.bean.PostForumBean;
import com.zeyuan.kyq.bean.ReplyForum;
import com.zeyuan.kyq.bean.ReplyListBean;
import com.zeyuan.kyq.bean.SimalarListBean;
import com.zeyuan.kyq.bean.WSZLBean;
import com.zeyuan.kyq.http.RespBase;
import com.zeyuan.kyq.http.RespGetAllStep;
import com.zeyuan.kyq.http.RespGetStepDetal;
import com.zeyuan.kyq.http.RespUserStepModify;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.ZYHttpClientManager;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/4/21.
 *
 * 发送请求业务类
 *
 * @author wwei
 */
public class GetResponseBiz implements KeyBiz.GetKeyCallBack{

    private HttpResponseInterface sendPage;
    private int flag;
    private Gson mGson;
    private int angin = 0;

    /***
     *
     * 构造业务逻辑对象
     *
     * @param responseInterface 回调
     * @param flag 请求标识
     */
    public GetResponseBiz(HttpResponseInterface responseInterface,int flag){
        this.sendPage = responseInterface;
        this.flag = flag;
        this.mGson = new Gson();
    }

    /***
     *
     * 发送POST请求
     *
     */
    public void postResponse(){
        sendPage.showLoading(flag);
        ZYHttpClientManager.zyPostAsyn(getPostUrl(), sendPage.getPostParams(flag),
                new ZYHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        try {
                            LogCustom.i(Const.TAG.ZY_HTTP, "请求失败：" + request.toString());
                            sendPage.showError(flag);
                        } catch (Exception ex) {
                            sendPage.showError(flag);
                        }
                    }

                    @Override
                    public void onKeyError(Request request, int keyFlag) {
                        try {
                            angin++;
                            Const.KEY_ANGIN_TIMES++;
                            if (keyFlag == 101 && angin < 2 && Const.KEY_ANGIN_TIMES < Const.KEY_MAX) {
                                KeyBiz.getKey(GetResponseBiz.this);
                            } else {
                                sendPage.showError(flag);
                            }
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            sendPage.hideLoading(flag);
                            Object o;
                            o = getGsonType(response);
                            if (o != null) {
                                sendPage.toActivity(o, flag);
                            }
                        } catch (Exception e) {

                        }
                    }
                }, 0);
    }

    /***
     *
     * 发送GET请求
     *
     */
    public void getResponse(){
        sendPage.showLoading(flag);
        ZYHttpClientManager.getAsyn(getPostUrl(), new HashMap<String, String>(), new ZYHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                LogCustom.i(Const.TAG.ZY_HTTP, "请求失败：" + request.toString());
                sendPage.showError(flag);
            }

            @Override
            public void onKeyError(Request request, int keyFlag) {
                angin++;
                Const.KEY_ANGIN_TIMES++;
                if (keyFlag == 101 && angin < 2 && Const.KEY_ANGIN_TIMES < Const.KEY_MAX) {
                    KeyBiz.getKey(GetResponseBiz.this);
                } else {
                    sendPage.showError(flag);
                }
            }

            @Override
            public void onResponse(String response) {
                sendPage.hideLoading(flag);
                sendPage.toActivity(response, flag);
            }
        });
    }


    /***
     *
     * 获取请求地址
     *
     * @return
     */
    private String getPostUrl(){
        String url = null;

        switch (flag){
            case Const.EGetMainPage:
                url = Const.E_GET_MAIN_PAGE;
                break;
            case Const.EGetCancerHeaderList:
                url = Const.E_GET_CANCER_HEADER_LIST;
                break;
            case Const.EGetPatientDetail:
                url = Const.E_GET_PATIENT_DETAIL;
                break;
            case Const.EGetAllStep:
                url = Const.E_GET_ALL_STEP;
                break;
            case Const.EGetStepDetail:
                url = Const.E_GET_STEP_DETAIL;
                break;
            case Const.EGetCancerProcess:
                url = Const.E_GET_CANCER_PROCESS;
                break;
            /*case Const.ESyncConf:
                url = Const.E_SYNC_CONF;
                break;*/
            case Const.ESyncConfDigit:
                url = Const.P_SYNC_CONF_DIGIT;
                break;
            case Const.ELogin:
                url = Const.E_LOGIN;
                break;
            case Const.EGetConfirmSecond:
                url = Const.E_GET_CONFIRM_SECOND;
                break;
            case Const.ESetPerform:
                url = Const.E_SET_PERFORM;
                break;
            case Const.ESetQuota:
                url = Const.E_SET_QUOTA;
                break;
            case Const.ESetPlanMedicine:
                url = Const.E_SET_PLAN_MEDICINE;
                break;
            case Const.EUpdatePatientDetail:
                url = Const.E_UPDATA_PATIENT_DETAIL;
                break;
            case Const.ERegister:
                url = Const.E_REGISTER;
                break;
            case Const.ECreateInfo:
                url = Const.E_CREATE_INFO;
                break;
            case Const.EGetResultDetail:
                url = Const.E_GET_RESULT_DETAIL;
                break;
            case Const.EGetSolutionDetail:
                url = Const.E_GET_SOLUTION_DETAIL;
                break;
            case Const.EGetCommDetail:
                url = Const.E_GET_COMM_DETAIL;
                break;
            case Const.EConfirmPerform:
                url = Const.E_CONFIRM_PERFORM;
                break;
            case Const.EDelStepQuotaPerform:
                url = Const.E_DEL_STEP_QUOTA_PERFORM;
                break;
            case Const.EUpdateStepTime:
                url = Const.E_UPDATE_STEP_TIME;
                break;
            case Const.EGetMycircle:
                url = Const.E_GET_MY_CIRCLE;
                break;
            case Const.EGetMyForumNum:
                url = Const.E_GET_MY_FORUM_NUM;
                break;
            case Const.EGetArticleList:
                url = Const.E_GET_ARTICLE_LIST;
                break;
            case Const.EGetArticleDetail:
                url = Const.E_GET_ARTICLE_DETAIL;
                break;
            case Const.EGetMyReplyList:
                url = Const.E_GET_MY_REPLY_LIST;
                break;
            case Const.EGetReplyList:
                url = Const.E_GET_REPLY_LIST;
                break;
            case Const.EGetSimilarList:
                url = Const.E_GET_SIMILAR_LIST;
                break;
            case Const.EGetCancerForum:
                url = Const.E_GET_CANCER_FORUM;
                break;
            case Const.EGetCityForum:
                url = Const.E_GET_CITY_FORUM;
                break;
            case Const.EGetForumDetail:
                url = Const.E_GET_FORUM_DETAIL;
                break;
            case Const.EFavorForum:
                url = Const.E_FAVOR_FORUM;
                break;
            case Const.EReplyForum:
                url = Const.E_REPLY_FORUM;
                break;
            case Const.EGetMyForum:
                url = Const.E_GET_MY_FORUM;
                break;
            case Const.EPostForum:
                url = Const.E_POST_FORUM;
                break;
            case Const.EGetMyFavor:
                url = Const.E_GET_MY_FAVOR;
                break;
            case Const.EFollowCircle:
                url = Const.E_FOLLOW_CIRCLE;
                break;
            case Const.EGetForumList:
                url = Const.E_GET_FORUM_LIST;
                break;
            case Const.ESyncConfStep:
                url = Const.P_SYNC_CONF_STEP;
                break;
            case Const.EGetSearchToPolicy:
                url = Const.E_GET_SEARCH_TO_POLICY;
                break;
            case Const.EGetSearchToSpecification:
                url = Const.E_GET_SEARCH_TO_SPECIFIACTION;
                break;
            case Const.EAddUserStep:
                url = Const.E_ADD_USER_STEP;
                break;
            case Const.EPostForumMoreCircle:
                url = Const.E_Post_Forum_More_Circle;
                break;
            case Const.ESetTokenDevice:
                url = Const.E_Set_Token_Device;
                break;
            case Const.EMobile:
                url = Const.E_MOBILE;
                break;
            case Const.EEvent:
                url = Const.E_EVENT;
                break;
        }

        LogCustom.i(Const.TAG.ZY_HTTP, "请求地址：" + url);
        return url;
    }

    /***
     *
     * 获取返回数据类型
     *
     * @param response
     * @return
     */
    private Object getGsonType(String response){
        Object o = null;
        try {
            switch (flag){
                case Const.EGetMainPage:
                    LogCustom.f(response, 20);
                    o = mGson.fromJson(response, MainPageInfoBean.class);
                    break;
                case Const.EGetCancerHeaderList:
                    o = mGson.fromJson(response, HeadLineBean.class);
                    break;
                case Const.EGetPatientDetail:
                    o = mGson.fromJson(response, PatientDetailBean.class);
                    break;
                case Const.EGetMyForumNum:
                    o = mGson.fromJson(response, MyDataBean.class);
                    break;
                case Const.EGetCancerProcess:
                    o = mGson.fromJson(response, FindSymtomBean.class);
                    break;
                case Const.ESetPlanMedicine:
                    o = mGson.fromJson(response, BaseBean.class);
                    break;
                case Const.EGetAllStep:
                    o = mGson.fromJson(response, RespGetAllStep.class);
                    break;
                case Const.EGetMycircle:
                    o = mGson.fromJson(response, MyCircleBean.class);
                    break;
                case Const.EGetStepDetail:
                    o = mGson.fromJson(response, RespGetStepDetal.class);
                    break;
                case Const.EUpdatePatientDetail:
                    o = mGson.fromJson(response, BaseBean.class);
                    break;
                case Const.ELogin:
                    o = mGson.fromJson(response, LoginZYBean.class);
                    break;
                case Const.EGetResultDetail:
                    o = mGson.fromJson(response, CancerResuletBean.class);
                    break;
                case Const.EGetConfirmSecond:
                    o = mGson.fromJson(response, WSZLBean.class);
                    break;
                case Const.EGetSolutionDetail:
                    o = mGson.fromJson(response, CaseDetailBean.class);
                    break;
                case Const.EGetCommDetail:
                    o = mGson.fromJson(response, CommBean.class);
                    break;
                case Const.EConfirmPerform:
                    o = mGson.fromJson(response, BaseBean.class);
                    break;
                case Const.EDelStepQuotaPerform:
                    o = mGson.fromJson(response, RespBase.class);
                    break;
                case Const.EUpdateStepTime:
                    o = mGson.fromJson(response, RespUserStepModify.class);
                    break;
                case Const.EGetArticleList:
                    o = mGson.fromJson(response, ArticleListBean.class);
                    break;
                case Const.EGetArticleDetail:
                    o = mGson.fromJson(response, ArticleDetailBean.class);
                    break;
                case Const.EGetCancerForum:
                    o = mGson.fromJson(response, CityCancerForumBean.class);
                    break;
                case Const.EGetCityForum:
                    o = mGson.fromJson(response, CityCancerForumBean.class);
                    break;
                case Const.EFollowCircle:
                    o = mGson.fromJson(response, FollowBean.class);
                    break;
                case Const.EGetForumList:
                    o = mGson.fromJson(response, ForumListBean.class);
                    break;
                case Const.EGetMyForum:
                    o = mGson.fromJson(response, MyForumReleaseBean.class);
                    break;
                case Const.EGetForumDetail:
                    o = mGson.fromJson(response, ForumDetailBean.class);
                    break;
                case Const.EGetReplyList:
                    o = mGson.fromJson(response, ReplyListBean.class);
                    break;
                case Const.EFavorForum:
                    o = mGson.fromJson(response, BaseBean.class);
                    break;
                case Const.EReplyForum:
                    o = mGson.fromJson(response, ReplyForum.class);
                    break;
                case Const.ECreateInfo:
                    o = mGson.fromJson(response, CreateInfoBean.class);
                    break;
                case Const.EGetMyFavor:
                    o = mGson.fromJson(response, FlwForumBean.class);
                    break;
                case Const.EGetMyReplyList:
                    o = mGson.fromJson(response, MyReplyListBean.class);
                    break;
                case Const.EGetSimilarList:
                    o = mGson.fromJson(response, SimalarListBean.class);
                    break;
                case Const.EPostForum:
                    o = mGson.fromJson(response, PostForumBean.class);
                    break;
                case Const.EPostForumMoreCircle:
                    o = mGson.fromJson(response, PostForumBean.class);
                    break;
                case Const.ESetPerform:
                    o = mGson.fromJson(response, RespBase.class);
                    break;

                case Const.ESyncConfStep:
                    o = response;
                    break;
                case Const.EGetSearchToSpecification:
                    o = mGson.fromJson(response, QuotaDataEntity.class);
                    break;
                case Const.EGetSearchToPolicy:
                    o = mGson.fromJson(response, SearchPolicyEntity.class);
                    break;
                case Const.EAddUserStep:
                    o = mGson.fromJson(response, AddStepBean.class);
                    break;
                case Const.ESetTokenDevice:
                    o = mGson.fromJson(response, BaseBean.class);
                    break;
                case Const.EMobile:
                    o = mGson.fromJson(response, BindMobileEntity.class);
                    break;
                case Const.EEvent:
                    o = mGson.fromJson(response, EventEntity.class);
                    break;
            }

        }catch (com.google.gson.JsonParseException e){
            LogCustom.i(Const.TAG.ZY_HTTP, "请求成功,JSON异常@" + flag + "@" + e.toString());
            sendPage.showError(flag);
        }

        return o;
    }

    @Override
    public void getKeySuccess(int[] keys) {
        try {
            String temp = HttpSecretUtils.getSaveKeyString(keys);
            HttpSecretUtils.TEA.setKey(Const.KEY_FINAL);
            String save = HttpSecretUtils.TEA.encryptByTea(temp);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "保存KEY失败");
        }
        HttpSecretUtils.TEA.setKey(keys);
        if(flag==Const.ESyncConf||flag==Const.ESyncConfDigit){
            getResponse();
        }else{
            postResponse();
        }
    }

    @Override
    public void getKeyFailed() {

    }

    private String getNewPostUrl(){

        String url = "";
        switch (flag){
            case Const.ESyncConf:
                url = Const.P_SYNC_CONF;
                break;
            case Const.ESyncConfDigit:
                url = Const.P_SYNC_CONF_DIGIT;
                break;
        }

        return url;
    }

}

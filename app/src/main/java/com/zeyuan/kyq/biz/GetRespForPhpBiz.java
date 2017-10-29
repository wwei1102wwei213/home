package com.zeyuan.kyq.biz;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.zeyuan.kyq.Entity.ArticleBaseEntity;
import com.zeyuan.kyq.Entity.ArticleInfoEntity;
import com.zeyuan.kyq.Entity.CareFollowBaseEntity;
import com.zeyuan.kyq.Entity.EditStepBean;
import com.zeyuan.kyq.Entity.FavBaseEntity;
import com.zeyuan.kyq.Entity.ForumBaseBean;
import com.zeyuan.kyq.Entity.HomePageBean;
import com.zeyuan.kyq.Entity.InfoCenterEntity;
import com.zeyuan.kyq.Entity.LiveBaseBean;
import com.zeyuan.kyq.Entity.MedicalBaseBean;
import com.zeyuan.kyq.Entity.PatientDataBean;
import com.zeyuan.kyq.Entity.PayWxEntity;
import com.zeyuan.kyq.Entity.PersonalBean;
import com.zeyuan.kyq.Entity.PushMsgBean;
import com.zeyuan.kyq.Entity.RecordBean;
import com.zeyuan.kyq.Entity.RecordClassifyHistoryEntity;
import com.zeyuan.kyq.Entity.ServiceCenterBean;
import com.zeyuan.kyq.Entity.UserInformationEntity;
import com.zeyuan.kyq.Entity.UserReplyEntity;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.AdverticingListBean;
import com.zeyuan.kyq.bean.ClickStatisticsBean;
import com.zeyuan.kyq.bean.CurrentNumBean;
import com.zeyuan.kyq.bean.EffectiveBean;
import com.zeyuan.kyq.bean.ForumInfoBean;
import com.zeyuan.kyq.bean.MedicalRecordDetailBean;
import com.zeyuan.kyq.bean.MsgBean;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.bean.PostForumBean;
import com.zeyuan.kyq.bean.RecommendUserBean;
import com.zeyuan.kyq.bean.ReplyListBean;
import com.zeyuan.kyq.bean.ShareJFBean;
import com.zeyuan.kyq.bean.ShortcutBean;
import com.zeyuan.kyq.bean.SimilarCaseBean;
import com.zeyuan.kyq.bean.StepSummaryBean;
import com.zeyuan.kyq.bean.UMSettingsBean;
import com.zeyuan.kyq.bean.YouzanLoginBean;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.OkHttpClientManager;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/23.
 * <p>
 * PHP服务端接口
 *
 * @author wwei
 */
public class GetRespForPhpBiz {

    private HttpResponseInterface sendPage;
    private int flag;
    private Gson mGson;

    public GetRespForPhpBiz(HttpResponseInterface responseInterface, int flag) {
        this.sendPage = responseInterface;
        this.flag = flag;
        this.mGson = new Gson();
    }

    public void postResponse() {
        sendPage.showLoading(flag);
        OkHttpClientManager.postNewAsyn(getPostUrl(), getParamMap(sendPage.getParamInfo(flag)),
                new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        LogCustom.i(Const.TAG.ZY_HTTP, "请求失败：" + request.toString());
                        sendPage.showError(flag);
                    }

                    @Override
                    public void onResponse(String response) {
                        sendPage.hideLoading(flag);
                        Object o = getGsonType(response);
                        if (o != null) {
                            LogCustom.i(Const.TAG.ZY_HTTP, "请求成功：" + o.toString());
                            sendPage.toActivity(o, flag);
                        } else {
                            LogCustom.i(Const.TAG.ZY_HTTP, "请求成功但数据为空");
                            sendPage.showError(flag);
                        }
                    }
                });
    }

    private String getPostUrl() {
        String url = null;
        switch (flag) {
            case Const.PMobile:
                url = Const.P_Mobile_Url;
                break;
            case Const.PCheckOpen:
                url = Const.P_Check_Open_Url;
                break;
            case Const.PCheckInfoPin:
                url = Const.P_Check_Info_Pin_Url;
                break;
            case Const.PCreateUserInfo:
                url = Const.P_Create_User_Info_Url;
                break;
            case Const.PBindWX:
                url = Const.P_BindWX;
                break;
            case Const.PUserInfo:
                url = Const.P_UserInfo;
                break;
            case Const.PCareAdd:
                url = Const.P_CareAdd;
                break;
            case Const.PCareDel:
                url = Const.P_CareDel;
                break;
            case Const.PCareList:
                url = Const.P_CareList;
                break;
            case Const.PFollowList:
                url = Const.P_FollowList;
                break;
            case Const.PCircleFRList:
                url = Const.P_CircleFRList;
                break;
            case Const.PGetPorCommentList:
                url = Const.P_GetPorCommentList;
                break;
            case Const.PUserCommentInfo:
                url = Const.P_UserCommentInfo;
                break;
            case Const.PAddPorComment:
                url = Const.P_AddPorComment;
                break;
            case Const.PArticleInfo:
                url = Const.P_ArticleInfo;
                break;
            case Const.PHomeArticleInfo:
                url = Const.P_HomeArticleInfo;
                break;
            case Const.PEditUserCat:
                url = Const.P_EditUserCat;
                break;
            case Const.PArticleOtherInfo:
                url = Const.P_ArticleOtherInfo;
                break;
            case Const.PCareArc:
                url = Const.P_CareArc;
                break;
            case Const.PGiveLike:
                url = Const.P_GiveLike;
                break;
            case Const.PGetForumList_bank:
                url = Const.P_GetForumList_bank;
                break;
            case Const.PGetUserFavInfo:
                url = Const.P_GetUserFavInfo;
                break;
            case Const.PUserLoginAdd:
                url = Const.P_UserLoginAdd;
                break;
            case Const.PShowInfoMsg:
                url = Const.P_Show_Info_Msg;
                break;
            case Const.PArticleAccurate:
                url = Const.P_Article_Accurate;
                break;
            case Const.PGetAppStepUser:
                url = Const.P_GetAppStepUser;
                break;
            case Const.PEditAppStepUser:
                url = Const.P_EditAppStepUser;
                break;
            case Const.PDelAppStepUser:
                url = Const.P_DelAppStepUser;
                break;
            case Const.PAddAppStepUser:
                url = Const.P_AddAppStepUser;
                break;
            case Const.PAddPresentationOther:
                url = Const.P_AddPresentationOther;
                break;
            case Const.PGetPresentationOther:
                url = Const.P_GetPresentationOther;
                break;
            case Const.PShowPresentationOther:
                url = Const.P_ShowPresentationOther;
                break;
            case Const.PEditPresentationOther:
                url = Const.P_EditPresentationOther;
                break;
            case Const.PDelPresentationOther:
                url = Const.P_DelPresentationOther;
                break;
            case Const.PGetUserSelfForApp:
                url = Const.P_GetUserSelfForApp;
                break;
            case Const.PEditUserSelfForApp:
                url = Const.P_EditUserSelfForApp;
                break;
            case Const.PGetUserInfoForApp:
                url = Const.P_GetUserInfoForApp;
                break;
            case Const.PEditUserInfoForApp:
                url = Const.P_EditUserInfoForApp;
                break;
            case Const.PAddStep2Perform:
                url = Const.P_AddStep2Perform;
                break;
            case Const.PAddTransferRecord:
                url = Const.P_AddTransferRecord;
                break;
            case Const.PAddTransferGen:
                url = Const.P_AddTransferGen;
                break;
            case Const.PGetCancerInfoForApp:
                url = Const.P_GetCancerInfoForApp;
                break;
            case Const.PAddQuotaMasterSlave:
                url = Const.P_AddQuotaMasterSlave;
                break;
            case Const.PAddCancerMark:
                url = Const.P_AddCancerMark;
                break;
            case Const.PGetRecordBookForApp:
                url = Const.P_GetRecordBookForApp;
                break;
            case Const.PGetStepUserOtherInfo:
                url = Const.P_GetStepUserOtherInfo;
                break;
            case Const.PDelTransferRecord:
                url = Const.P_DelTransferRecord;
                break;
            case Const.PDelTransferGen:
                url = Const.P_DelTransferGen;
                break;
            case Const.PDelStep2Perform:
                url = Const.P_DelStep2Perform;
                break;
            case Const.PDelQuotaMasterSlave:
                url = Const.P_DelQuotaMasterSlave;
                break;
            case Const.PDelCancerMark:
                url = Const.P_DelCancerMark;
                break;
            case Const.PAddMarkTypeByUser:
                url = Const.P_AddMarkTypeByUser;
                break;
            case Const.PShowMarkAndTumourInfo:
                url = Const.P_ShowMarkAndTumourInfo;
                break;
            case Const.PApiServeCenterlist:
                url = Const.P_ApiServeCenterlist;
                break;
            case Const.PCateinkeorder:
                url = Const.P_Cateinkeorder;
                break;
            case Const.PApi_inkevideo_liveidid:
                url = Const.P_Api_inkevideo_liveidid;
                break;
            case Const.PApi_App_shortcut:
                url = Const.P_Api_App_shortcut;
                break;
            case Const.PApi_getStepSummary:
                url = Const.P_Api_getStepSummary;
                break;
            case Const.PApi_getSimilarCase:
                url = Const.p_Api_getSimilarCase;
                break;
            case Const.PApi_getAdvertising_more:
                url = Const.p_Api_getAdvertising_more;
                break;
            case Const.PApi_getProject:
                url = Const.p_Api_getProject;
                break;
            case Const.PApi_getCircleShortcut:
                url = Const.p_Api_get_Circle_shortcut;
                break;
            case Const.PApi_getThreadByType:
                url = Const.p_Api_getThreadByType;
                break;
            case Const.PApi_getThreadInfo:
                url = Const.p_Api_getThreadInfo;
                break;
            case Const.PApi_editThread:
                url = Const.p_Api_editThread;
                break;
            case Const.PApi_delThread:
                url = Const.p_Api_delThread;
                break;
            case Const.PApi_getReplyList_2:
                url = Const.p_Api_getReplyList2;
                break;
            case Const.PApi_getUserInfoByTypeForApp:
                url = Const.p_Api_getUserInfoByTypeForApp;
                break;
            case Const.PApi_LookCount:
                url = Const.p_Api_LoolCount;
                break;
            case Const.Api_YouzanLogin:
                url = Const.Url_YouzanLogin;
                break;
            case Const.PApi_ThreadForMoreCircle:
                url = Const.p_Api_ThreadForMoreCircle;
                break;
            case Const.PApi_AppShareIntegral:
                url = Const.p_Api_AppShareIntegral;
                break;
            case Const.PApi_ShowInfoMsg:
                url = Const.p_Api_ShowInfoMsg;
                break;
            case Const.PApi_UM_Switch:
                url = Const.p_Api_UM_Switch;
                break;
            case Const.PApi_EditInfo:
                url = Const.p_Api_EditInfo;
                break;
            case Const.PApi_GetCurrentNum:
                url = Const.p_Api_getCurrentNum;
                break;
            case Const.PApi_getStepForUserNum:
                url = Const.p_Api_getStepForUserNum;
                break;
            case Const.PApi_getRecommendUser:
                url = Const.p_Api_getRecommendUser;
                break;
            case Const.PApi_Edit_Moblie:
                url = Const.p_Api_Edit_Moblie;
                break;


        }
        LogCustom.i(Const.TAG.ZY_HTTP, "请求地址：" + url);
        return url;
    }


    private Object getGsonType(String response) {
        Object o = null;
        LogCustom.i(Const.TAG.ZY_HTTP, "请求数据：" + response);
        try {
            switch (flag) {
                case Const.PMobile:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PCheckOpen:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PCheckInfoPin:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PCreateUserInfo:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PBindWX:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PUserInfo:
                    o = mGson.fromJson(response, InfoCenterEntity.class);
                    break;
                case Const.PCareList:
                    o = mGson.fromJson(response, CareFollowBaseEntity.class);
                    break;
                case Const.PFollowList:
                    o = mGson.fromJson(response, CareFollowBaseEntity.class);
                    break;
                case Const.PCareAdd:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PCareDel:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PGetPorCommentList:
                    o = mGson.fromJson(response, ArticleBaseEntity.class);
                    break;
                case Const.PAddPorComment:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PUserCommentInfo:
                    o = mGson.fromJson(response, UserReplyEntity.class);
                    break;
                case Const.PArticleInfo:
                    o = mGson.fromJson(response, UserInformationEntity.class);
                    break;
                case Const.PHomeArticleInfo:
                    o = mGson.fromJson(response, UserInformationEntity.class);
                    break;
                case Const.PEditUserCat:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PArticleOtherInfo:
                    o = mGson.fromJson(response, ArticleInfoEntity.class);
                    break;
                case Const.PGiveLike:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PCareArc:
                    o = mGson.fromJson(response, ForumBaseBean.class);
                    break;
                case Const.PGetForumList_bank:
                    o = mGson.fromJson(response, ForumBaseBean.class);
                    break;
                case Const.PCircleFRList:
                    o = mGson.fromJson(response, CareFollowBaseEntity.class);
                    break;
                case Const.PGetUserFavInfo:
                    o = mGson.fromJson(response, FavBaseEntity.class);
                    break;
                case Const.PUserLoginAdd:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PShowInfoMsg:
                    o = mGson.fromJson(response, PushMsgBean.class);
                    break;
                case Const.PArticleAccurate:
                    o = mGson.fromJson(response, UserInformationEntity.class);
                    break;
                case Const.PGetAppStepUser:
                    o = mGson.fromJson(response, EditStepBean.class);
                    break;
                case Const.PEditAppStepUser:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PDelAppStepUser:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PAddAppStepUser:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PAddPresentationOther:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PGetPresentationOther:
                    o = mGson.fromJson(response, RecordBean.class);
                    break;
                case Const.PShowPresentationOther:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PEditPresentationOther:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PDelPresentationOther:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PGetUserSelfForApp:
                    o = mGson.fromJson(response, PersonalBean.class);
                    break;
                case Const.PEditUserSelfForApp:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PGetUserInfoForApp:
                    o = mGson.fromJson(response, PatientDataBean.class);
                    break;
                case Const.PEditUserInfoForApp:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PAddStep2Perform:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PAddTransferRecord:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PAddTransferGen:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PGetCancerInfoForApp:
                    o = mGson.fromJson(response, RecordClassifyHistoryEntity.class);
                    break;
                case Const.PAddQuotaMasterSlave:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PAddCancerMark:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PGetRecordBookForApp:
                    o = mGson.fromJson(response, MedicalBaseBean.class);
                    break;
                case Const.PGetStepUserOtherInfo:
                    o = mGson.fromJson(response, MedicalBaseBean.class);
                    break;
                case Const.PDelTransferRecord:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PDelTransferGen:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PDelStep2Perform:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PDelQuotaMasterSlave:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PDelCancerMark:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PAddMarkTypeByUser:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PShowMarkAndTumourInfo:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PApiServeCenterlist:
                    o = mGson.fromJson(response, ServiceCenterBean.class);
                    break;
                case Const.PCateinkeorder:
                    o = mGson.fromJson(response, PayWxEntity.class);
                    break;
                case Const.PApi_inkevideo_liveidid:
                    o = mGson.fromJson(response, LiveBaseBean.class);
                    break;
                case Const.PApi_App_shortcut:
                    o = mGson.fromJson(response, HomePageBean.class);
                    break;
                case Const.PApi_getStepSummary:
                    o = mGson.fromJson(response, StepSummaryBean.class);
                    break;
                case Const.PApi_getSimilarCase:
                    o = mGson.fromJson(response, SimilarCaseBean.class);
                    break;
                case Const.PApi_getAdvertising_more:
                    o = mGson.fromJson(response, AdverticingListBean.class);
                    break;
                case Const.PApi_getProject:
                    o = mGson.fromJson(response, ForumBaseBean.class);
                    break;
                case Const.PApi_getCircleShortcut:
                    o = mGson.fromJson(response, ShortcutBean.class);
                    break;
                case Const.PApi_getThreadByType:
                    o = mGson.fromJson(response, ForumBaseBean.class);
                    break;
                case Const.PApi_getThreadInfo:
                    o = mGson.fromJson(response, ForumInfoBean.class);
                    break;
                case Const.PApi_editThread:
                    o = mGson.fromJson(response, PostForumBean.class);
                    break;
                case Const.PApi_delThread:
                    o = mGson.fromJson(response, PostForumBean.class);
                    break;
                case Const.PApi_getReplyList_2:
                    o = mGson.fromJson(response, ReplyListBean.class);
                    break;
                case Const.PApi_getUserInfoByTypeForApp:
                    o = mGson.fromJson(response, MedicalRecordDetailBean.class);
                    break;
                case Const.PApi_LookCount:
                    o = mGson.fromJson(response, ClickStatisticsBean.class);
                    break;
                case Const.Api_YouzanLogin:
                    o = mGson.fromJson(response, YouzanLoginBean.class);
                    break;
                case Const.PApi_ThreadForMoreCircle:
                    o = mGson.fromJson(response, PostForumBean.class);
                    break;
                case Const.PApi_AppShareIntegral:
                    o = mGson.fromJson(response, ShareJFBean.class);
                    break;
                case Const.PApi_ShowInfoMsg:
                    o = mGson.fromJson(response, MsgBean.class);
                    break;
                case Const.PApi_UM_Switch:
                    o = mGson.fromJson(response, UMSettingsBean.class);
                    break;
                case Const.PApi_EditInfo:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
                case Const.PApi_GetCurrentNum:
                    o = mGson.fromJson(response, CurrentNumBean.class);
                    break;
                case Const.PApi_getStepForUserNum:
                    o = mGson.fromJson(response, EffectiveBean.class);
                    break;
                case Const.PApi_getRecommendUser:
                    o = mGson.fromJson(response, RecommendUserBean.class);
                    break;
                case Const.PApi_Edit_Moblie:
                    o = mGson.fromJson(response, PhpUserInfoBean.class);
                    break;
            }
        } catch (com.google.gson.JsonParseException e) {
            LogCustom.i(Const.TAG.ZY_HTTP, "请求成功,JSON异常" + e.toString());
        }
        return o;
    }

    private Map getParamMap(Map map) {
        try {
            if (!TextUtils.isEmpty(ZYApplication.versionNum))
                map.put("Ver", ZYApplication.versionNum);
            if (!TextUtils.isEmpty(ZYApplication.phoneInfo))
                map.put("MD", ZYApplication.phoneInfo);
            if (!TextUtils.isEmpty(ZYApplication.deviceId))
                map.put("DeviceID", ZYApplication.deviceId);
            map.put("AT", "2");
            if (!TextUtils.isEmpty(ZYApplication.mLoginType))
                map.put("LT", ZYApplication.mLoginType);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "getRespForPhp");
        }
        return map;
    }

}

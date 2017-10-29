package com.zeyuan.kyq.biz;

import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.bean.FindSymtomBean;
import com.zeyuan.kyq.bean.MainPageInfoBean;
import com.zeyuan.kyq.bean.MyCircleBean;
import com.zeyuan.kyq.bean.MyDataBean;
import com.zeyuan.kyq.bean.PatientDetailBean;
import com.zeyuan.kyq.http.RespGetAllStep;
import com.zeyuan.kyq.http.RespGetStepDetal;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.OkHttpClientManager;

/**
 * Created by Administrator on 2016/4/25.
 *
 *
 * @author wwei
 */
public class GetResponseOldBiz {

    private HttpResponseInterface sendPage;
    private int flag;
    private Gson mGson;

    public GetResponseOldBiz(HttpResponseInterface responseInterface,int flag){
        this.sendPage = responseInterface;
        this.flag = flag;
        this.mGson = new Gson();
    }

    public void postResponse(){
        sendPage.showLoading(flag);
        OkHttpClientManager.postNewAsyn(getPostUrl(), sendPage.getParamInfo(flag),
                new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.i(Const.TAG.ZY_HTTP, "请求失败：" + request.toString());
                sendPage.showError(flag);
            }

            @Override
            public void onResponse(String response) {

                sendPage.hideLoading(flag);
                Object o = getGsonType(response);
                sendPage.toActivity(o, flag);
            }
        });
    }

    /*public void getResponse(){
        sendPage.showLoading(flag);
        ZYHttpClientManager.getAsyn(getPostUrl(), new HashMap<String, String>(), new ZYHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.i(Const.TAG.ZY_HTTP, "请求失败：" + request.toString());
                sendPage.showError(flag);
            }

            @Override
            public void onResponse(String response) {
                sendPage.hideLoading(flag);
                sendPage.toActivity(response, flag);
            }
        });
    }*/

    private String getPostUrl(){
        String url = null;

            switch (flag){
                case Const.EGetMainPage://1
                    url = Contants.MAIN_PATH;
                    break;
                case Const.EGetPatientDetail://1
                    url = Contants.PATIENT_DETAIL;
                    break;
                case Const.EGetAllStep://
                    url = Contants.GET_ALL_STEP;
                    break;
                case Const.EGetMyForumNum://1
                    url = Contants.MY_CIRCLE_NUM;
                    break;
                case Const.EGetStepDetail://1
//                    url = Contants.STEP_DETAIL;
                    url = Contants.UserStep.GET_STEP_DETAIL;
                    break;
                case Const.EGetCancerProcess://1
                    url = Contants.GET_CANCER_PROCESS;
                    break;
                case Const.ESyncConf:
                    url = Contants.SYNC_CONFIG;
                    break;
                case Const.ELogin:
                    url = Contants.LOGIN;
                    break;
                case Const.EGetConfirmSecond:
                    url = Contants.GET_CONFIRM_SECOND;
                    break;
                case Const.ESetPerform:
                    url = Contants.Set_Perform;
                    break;
                case Const.ESetQuota:
                    url = Contants.SET_QUOTA;
                    break;
                case Const.ESetPlanMedicine://1
                    url = Contants.SET_PLAN_MEDICINE;
                    break;
                case Const.EUpdatePatientDetail:
                    url = Contants.UPDATE_PATIENT_DETAIL;
                    break;
                case Const.ERegister:
                    url = Const.E_REGISTER;
                    break;
                case Const.ECreateInfo:
                    url = Contants.CREATE_INFO;
                    break;
                case Const.EGetResultDetail:
                    url = Contants.GET_RESULT_DETAIL;
                    break;
                case Const.EGetSolutionDetail:
                    url = Contants.GET_SOLUTION_DETAIL;
                    break;
                case Const.EGetCommDetail:
                    url = Contants.GET_COMMDETAIL;
                    break;
                case Const.EConfirmPerform:
                    url = Contants.CONFIRM_PERFORM;
                    break;
                case Const.EDelStepQuotaPerform:
                    url = Contants.UserStep.DEL_STEP_QUOTA_PERFORM;
                    break;
                case Const.EUpdateStepTime:
                    url = Contants.UPDATE_STEP_TIME;
                    break;
                case Const.EGetMycircle:
                    url = Contants.MY_CIRCLE;
                    break;
                case 99:
                    url = "http://help.kaqcn.com/help/check_user_info";
                    break;

            }
        Log.i(Const.TAG.ZY_HTTP, "请求地址：" + url);
        return url;
    }


    private Object getGsonType(String response){
        Object o = null;
        try {
            switch (flag) {
                case Const.EGetMainPage://1
                    o = mGson.fromJson(response, MainPageInfoBean.class);
                    break;
                case Const.EGetPatientDetail://1
                    o = mGson.fromJson(response, PatientDetailBean.class);
                    break;
                case Const.EGetMyForumNum://1
                    o = mGson.fromJson(response, MyDataBean.class);
                    break;
                case Const.EGetCancerProcess://1
                    o = mGson.fromJson(response, FindSymtomBean.class);
                    break;
                case Const.ESetPlanMedicine://1
                    o = mGson.fromJson(response, BaseBean.class);
                    break;
                case Const.EGetAllStep://1
                    o = mGson.fromJson(response, RespGetAllStep.class);
                    break;
                case Const.EGetMycircle://1
                    o = mGson.fromJson(response, MyCircleBean.class);
                    break;
                case Const.EGetStepDetail://1
                    o = mGson.fromJson(response, RespGetStepDetal.class);
                    break;
                case 99:
                    o = response;
                    break;
            }
        } catch (com.google.gson.JsonParseException e) {
            Log.i(Const.TAG.ZY_HTTP, "请求成功,JSON异常" + e.toString());
        }
        return o;
    }

}

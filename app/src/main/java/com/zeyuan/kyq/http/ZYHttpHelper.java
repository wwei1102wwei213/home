//package com.zeyuan.kyq.http;
//
//import com.squareup.okhttp.Request;
//import com.zeyuan.kyq.presenter.GetDataBiz;
//import com.zeyuan.kyq.utils.OkHttpClientManager;
//
//import java.util.HashMap;
//
///**
// * Created by guogzhao on 16/1/19.
// */
//public class ZYHttpHelper {
//
//    private static int CODE_SEED = 0;
//    public static final int ReqCode_UserStepAll = ++CODE_SEED;
//    public static final int ReqCode_UserStepDetails = ++CODE_SEED;
//
//    public static final int ReqCode_UserStepAdd = ++CODE_SEED;
//    public static final int ReqCode_UserStepMdf = ++CODE_SEED;
//    public static final int ReqCode_UserStepDel = ++CODE_SEED;
//
//    public static final int ReqCode_UserQuotaAdd = ++CODE_SEED;
//    public static final int ReqCode_UserQuotaMdf = ++CODE_SEED;
//    public static final int ReqCode_UserQuotaDel = ++CODE_SEED;
//
//    public static final int ReqCode_UserSymptAdd = ++CODE_SEED;
//    public static final int ReqCode_UserSymptMdf = ++CODE_SEED;
//    public static final int ReqCode_UserSymptDel = ++CODE_SEED;
//
//
//    this.mGetDataBiz = new GetDataBiz();
//    private static GetDataBiz mGetDataBiz;
//
//    /**
//     * 请求服务器接口通用处理方法
//     * @param reqCode
//     * @param url
//     * @param map
//     * @param t
//     * @param resp
//     * @param <T>
//     */
//    private static <T> void reqSvrData(final int reqCode, String url, HashMap<String, String> map, T t, final RespRunnable resp) {//,
//        listener.showLoading(reqCode);
//        mGetDataBiz.getData(map, url, new OkHttpClientManager.ResultCallback<T>() {
//            @Override
//            public void onError(Request request, Exception e) {
//                listener.hideLoading(reqCode);
//            }
//
//            @Override
//            public void onResponse(T response) {
//                listener.hideLoading(reqCode);
//                resp.onResp(reqCode, response);
//            }
//        });
//    }
//}

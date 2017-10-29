package com.zeyuan.kyq.biz;

import android.content.Context;

import com.zeyuan.kyq.utils.ExceptionUtils;

/**
 * Created by Administrator on 2016/4/23.
 * <p>
 * 工厂
 *
 * @author wwei
 */
public class Factory {

    //TEA加密接口请求业务
    public static void post(Object obj, int flag) {
        new GetResponseBiz((HttpResponseInterface) obj, flag).postResponse();
    }

    public static void postNoSecret(HttpResponseInterface responseInterface, int flag) {
        new GetResponseNoSecretBiz(responseInterface, flag).postResponse();
    }

    public static void get(HttpResponseInterface responseInterface, int flag) {
        new GetResponseBiz(responseInterface, flag).getResponse();
    }

    //HTTPS加密接口请求业务
    public static void postPhp(HttpResponseInterface responseInterface, int flag) {
        new GetRespForPhpBiz(responseInterface, flag).postResponse();
    }

    public static void getKey(KeyBiz.GetKeyCallBack callBack) {
        KeyBiz.getKey(callBack);
    }

    /***
     *
     * 数据模块业务
     *
     * @param flag 数据标识
     * @return 数据
     */
    public static Object getData(int flag) {
        try {
            return SqlConfigBiz.getDataForFlag(flag);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "Factory.getData");
            return null;
        }

    }

    public static Object getData(int flag, String cancerID) {
        return SqlConfigBiz.getDataForFlag(flag, cancerID);
    }

    /***
     *
     * 友盟事件业务
     *
     * @param context 页面
     * @param eventId 事件名称
     * @param eventFlag 事件标志 有需要统计信息才会有
     *                  extras 为附加参数
     *
     */
    public static void onEvent(Context context, String eventId, String eventFlag, String extras) {
        try {
            StatisticsBiz.sendToUMeng(context, eventId, eventFlag, extras);
        } catch (Exception e) {

        }

    }
}

package com.zeyuan.kyq.biz;

import android.text.TextUtils;

import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2016/4/21.
 * <p>
 * 不可说
 *
 * @author wwei
 */
public class KeyBiz {

    private static int times = 0;
    private static final int Max_Times = 3;

    public interface GetKeyCallBack {
        void getKeySuccess(int[] keys);

        void getKeyFailed();
    }


    public static void getKey(final GetKeyCallBack callBack) {
        try {
            callBack.getKeySuccess(new int[]{1701143909, 1751672936, 1835887981, 1802201963});
        } catch (Exception e) {
            callBack.getKeyFailed();
        }
    }

    public static int[] getKeyOnThread() {
        return new int[]{1701143909, 1751672936, 1835887981, 1802201963};
    }

    public static int[] getKeyResult() {
        return new int[]{1701143909, 1751672936, 1835887981, 1802201963};
    }


    static String getConfDataOnThread(int flag) {
        try {
            String url = null;
            if (flag == Const.N_DataSyncConf) {
                url = Const.P_SYNC_CONF;
                String req = OkHttpClientManager.postAsyn(url, new HashMap<String, String>(), flag);
                LogCustom.i(Const.TAG.ZY_HTTP, "请求地址：" + url + "\n请求数据：" + req);
                return req;
            } else if (flag == Const.N_DataDigitConf) {//获取分期数据
                url = Const.P_SYNC_CONF_DIGIT;
                String req = OkHttpClientManager.postAsyn(url, new HashMap<String, String>(), flag);
               // req = req.substring(req.indexOf(",") + 1, req.length());
                LogCustom.i(Const.TAG.ZY_HTTP, "请求地址：" + url + "\n请求数据：" + req);
                return req;
            } else if (flag == Const.N_DataConfStep) {//获取阶段数据
                url = Const.P_SYNC_CONF_STEP;
                String[] args = new String[]{
                        "ts", "0"
                };
                String req = OkHttpClientManager.postAsyn(url, HttpSecretUtils.getParamString(args,false), flag);
             //   req = req.substring(req.indexOf(",") + 1, req.length());
                LogCustom.i(Const.TAG.ZY_HTTP, "请求参数：" + Arrays.toString(args) + "\n请求地址：" + Const.P_SYNC_CONF_STEP
                        + "\n请求数据：" + req);
                return req;
            }
            return null;
        } catch (Exception e) {
            LogCustom.i(Const.TAG.ZY_DATA, "getConfDataOnThread ERROR");
            return null;
        }
    }

    private static Map getParamMap(Map map) {
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

    public static String getUpdataForTime(String time) {
        ExecutorService exs = Executors.newCachedThreadPool();
        Future<String> mKey = exs.submit(new getUpdataForTimeThread(time));
        String req = null;
        try {
            req = mKey.get();
        } catch (Exception e) {

        }
        return req;
    }

    public static String getConfData(int flag) {
        ExecutorService exs = Executors.newCachedThreadPool();
        Future<String> mKey = exs.submit(new getConfDataThread(flag));
        String req = null;
        try {
            req = mKey.get();
        } catch (Exception e) {

        }
        return req;
    }

    static class getUpdataForTimeThread implements Callable<String> {

        private String time;

        public getUpdataForTimeThread(String time) {
            this.time = time;
        }

        @Override
        public String call() throws Exception {
            try {
                String url = Const.P_SYNC_CONF_STEP;
                String[] args = new String[]{
                        "ts", time
                };
                String req = OkHttpClientManager.postAsyn(url, HttpSecretUtils.getParamString(args), Const.N_DataConfStep);
//                req = req.substring(req.indexOf(",") + 1, req.length());
//                req = HttpSecretUtils.TEA.decryptByTea(req);
                LogCustom.i(Const.TAG.ZY_HTTP, "请求参数：" + Arrays.toString(args) + "\n请求地址：" + Const.P_SYNC_CONF_STEP
                        + "\n请求数据：" + req);
                return req;
            } catch (Exception e) {
                return null;
            }
        }
    }

    static class getConfDataThread implements Callable<String> {

        private int flag;

        public getConfDataThread(int flag) {
            this.flag = flag;
        }

        @Override
        public String call() throws Exception {
            try {
                String url = null;
                if (flag == Const.N_DataSyncConf) {
                    url = Const.P_SYNC_CONF;
                    String req = OkHttpClientManager.postAsyn(url, new HashMap<String, String>(), flag);
                    LogCustom.i(Const.TAG.ZY_HTTP, "请求地址：" + url + "\n请求数据：" + req);
                    return req;
                } else if (flag == Const.N_DataDigitConf) {
                    url = Const.P_SYNC_CONF_DIGIT;
                    String req = OkHttpClientManager.postAsyn(url, new HashMap<String, String>(), flag);
                    //req = req.substring(req.indexOf(",") + 1, req.length());
                    LogCustom.i(Const.TAG.ZY_HTTP, "请求地址：" + url + "\n请求数据：" + req);
                    return req;
                } else if (flag == Const.N_DataConfStep) {
                    url = Const.P_SYNC_CONF_STEP;
                    String[] args = new String[]{
                            "ts", "0"
                    };
                    String req = OkHttpClientManager.postAsyn(url, HttpSecretUtils.getParamString(args), flag);
                   // req = req.substring(req.indexOf(",") + 1, req.length());
                    LogCustom.i(Const.TAG.ZY_HTTP, "请求参数：" + Arrays.toString(args) + "\n请求地址：" + Const.P_SYNC_CONF_STEP
                            + "\n请求数据：" + req);
                    return req;
                }
                return null;
            } catch (Exception e) {
                LogCustom.i("ZYL", "ERROR");
                return null;
            }
        }
    }


}

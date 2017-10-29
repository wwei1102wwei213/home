package com.zeyuan.kyq.utils;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.zeyuan.kyq.bean.ErrorBean;
import com.zeyuan.kyq.utils.PayHttp.TrustAllCerts;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * Created by Administrator on 2016/4/22.
 *
 * 泽愿发送请求管理工具
 *
 * @author wwei
 */
public class ZYHttpClientManager {


    private static ZYHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;


    private ZYHttpClientManager() {
        mOkHttpClient = new OkHttpClient();
//        cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
//        Cache cache = new Cache();//这儿可以做缓存
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(2, TimeUnit.MINUTES);
        mOkHttpClient.setWriteTimeout(2, TimeUnit.MINUTES);
        mOkHttpClient.setSslSocketFactory(createSSLSocketFactory());
        mOkHttpClient.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        mDelivery = new Handler(Looper.getMainLooper());
    }

    public static ZYHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (ZYHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new ZYHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    public static void zyPostAsyn(String url, byte[] bodyBytes, final ResultCallback callback, Object tag) {
        getInstance().postAsynWithMediaType(url, bodyBytes, MediaType.parse("application/octet-stream;charset=utf-8"), callback, tag);
    }

    /**
     * 直接将bodyBytes以写入请求体
     */
    public void postAsynWithMediaType(String url, byte[] bodyBytes, MediaType type, final ResultCallback callback, Object tag) {
        RequestBody body = RequestBody.create(type, bodyBytes);
        Request request = buildPostRequest(url, body, tag);
        noGsonResult(callback, request);
    }

    public static void zyPostAsynNoSecret(String url, byte[] bodyBytes, final ResultCallback callback, Object tag) {
        getInstance().postAsynWithNoSecret(url, bodyBytes, MediaType.parse("application/octet-stream;charset=utf-8"), callback, tag);
    }

    /**
     * 直接将bodyBytes以写入请求体
     */
    public void postAsynWithNoSecret(String url, byte[] bodyBytes, MediaType type, final ResultCallback callback, Object tag) {
        RequestBody body = RequestBody.create(type, bodyBytes);
        Request request = buildPostRequest(url, body, tag);
        noGsonNoSecretResult(callback, request);
    }

    public static void getAsyn(String url, Map<String, String> params, final ResultCallback callback) {
        getInstance().postGAsyn(url, params, callback, null);
    }

    public void postGAsyn(String url, Map<String, String> params, final ResultCallback callback, Object tag) {
        Param[] paramsArr = map2Params(params);
        postAsyn(url, paramsArr, callback, tag);
    }

    /**
     * 异步的post请求
     */
    public void postAsyn(String url, Param[] params, final ResultCallback callback, Object tag) {
//        Log.i(Const.TAG.ZY_HTTP, "请求地址：" + url);
        Request request = buildPostFormRequest(url, params, tag);
        noGsonResult(callback, request);
    }

    private Request buildPostFormRequest(String url, Param[] params, Object tag) {
        if (params == null) {
            params = new Param[0];
        }
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();
        Request.Builder reqBuilder = new Request.Builder();
        reqBuilder.url(url)
                .post(requestBody);

        if (tag != null) {
            reqBuilder.tag(tag);
        }
        return reqBuilder.build();
    }

    /**
     * post构造Request的方法
     *
     * @param url
     * @param body
     * @return
     */
    private Request buildPostRequest(String url, RequestBody body, Object tag) {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);
        if (tag != null) {
            builder.tag(tag);
        }
        Request request = builder.build();
        return request;
    }

    private Param[] map2Params(Map<String, String> params) {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    private final ResultCallback<String> DEFAULT_RESULT_CALLBACK = new ResultCallback<String>() {
        @Override
        public void onError(Request request, Exception e) {

        }

        @Override
        public void onKeyError(Request request, int keyFlag) {

        }

        @Override
        public void onResponse(String response) {

        }
    };

    private void noGsonResult(ResultCallback callback, Request request) {
        if (callback == null) callback = DEFAULT_RESULT_CALLBACK;
        final ResultCallback resCallBack = callback;
        //UI thread
        callback.onBefore(request);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                LogCustom.i(Const.TAG.ZY_HTTP, "请求失败异常信息：" + request.toString());
                sendFailedStringCallback(request, e, resCallBack);
            }

            @Override
            public void onResponse(final Response response) {
                try {
                    final String string = response.body().string();

                    LogCustom.i(Const.TAG.ZY_HTTP, "请求原始数据：" + string);

                    try {
                        Object o = null;
                        try {
                            Gson mGson = new Gson();
                            o = mGson.fromJson(string, ErrorBean.class);
                        }catch (Exception e){

                        }
                        if(o!=null){
                            ErrorBean bean = (ErrorBean)o;
                            if(bean.getError()!=null&&"KERR".equals(bean.getError())){
                                LogCustom.i(Const.TAG.ZY_HTTP, "KEY ERROR:重新请求KEY");
                                sendKeyErrorStringCallback(response.request(), resCallBack, 101);
                                return;
                            }
                        }
                    }catch (Exception e){

                    }
                    boolean secret = true;
                    String req = string.substring(string.indexOf(",")+1,string.length());
                    try {
                        req = HttpSecretUtils.TEA.decryptByTea(req);
                    }catch (Exception e){
                        ExceptionUtils.ExceptionSend(e,"req secret error");
                        LogCustom.i(Const.TAG.ZY_HTTP, "请求数据解密失败");
                        secret = false;
                    }
                    if(secret){
                        LogCustom.i(Const.TAG.ZY_HTTP, response.request().toString() + "返回数据是：" + req);
                        sendSuccessResultCallback(req, resCallBack);
                    }else {
                        sendKeyErrorStringCallback(response.request(), resCallBack, 101);
                    }
                } catch (Exception e) {
                    LogCustom.i(Const.TAG.ZY_HTTP, "请求成功,IO异常"+response.request().toString()+"异常信息：" + e.toString());
                    sendFailedStringCallback(response.request(), e, resCallBack);
                }
            }
        });
    }

    private void noGsonNoSecretResult(ResultCallback callback, Request request) {
        if (callback == null) callback = DEFAULT_RESULT_CALLBACK;
        final ResultCallback resCallBack = callback;
        //UI thread
        callback.onBefore(request);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                LogCustom.i(Const.TAG.ZY_HTTP, "请求失败异常信息：" + request.toString());
                sendFailedStringCallback(request, e, resCallBack);
            }

            @Override
            public void onResponse(final Response response) {
                try {
                    final String string = response.body().string();
                    LogCustom.i(Const.TAG.ZY_HTTP, "请求原始数据：" + string);
                    try {
                        Object o = null;
                        try {
                            Gson mGson = new Gson();
                            o = mGson.fromJson(string, ErrorBean.class);
                        }catch (Exception e){

                        }
                        if(o!=null){
                            ErrorBean bean = (ErrorBean)o;
                            if(bean.getError()!=null&&"KERR".equals(bean.getError())){
                                LogCustom.i(Const.TAG.ZY_HTTP, "KEY ERROR:重新请求KEY");
                                sendKeyErrorStringCallback(response.request(), resCallBack, 101);
                                return;
                            }
                        }
                    }catch (Exception e){

                    }

                    sendSuccessResultCallback(string, resCallBack);

                } catch (Exception e) {
                    LogCustom.i(Const.TAG.ZY_HTTP, "请求成功,IO异常"+response.request().toString()+"异常信息：" + e.toString());
                    sendFailedStringCallback(response.request(), e, resCallBack);
                }
            }
        });
    }

    private void noGsonNoSecretPhpResult(ResultCallback callback, Request request) {
        if (callback == null) callback = DEFAULT_RESULT_CALLBACK;
        final ResultCallback resCallBack = callback;
        //UI thread
        callback.onBefore(request);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                LogCustom.i(Const.TAG.ZY_HTTP, "请求失败异常信息：" + request.toString());
                sendFailedStringCallback(request, e, resCallBack);
            }

            @Override
            public void onResponse(final Response response) {
                try {
                    final String string = response.body().string();
                    LogCustom.i(Const.TAG.ZY_HTTP, "请求原始数据：" + string);

                    /*try {
                        Object o = null;
                        try {
                            Gson mGson = new Gson();
                            o = mGson.fromJson(string, ErrorBean.class);
                        }catch (Exception e){

                        }
                        if(o!=null){
                            ErrorBean bean = (ErrorBean)o;
                            if(bean.getError()!=null&&"KERR".equals(bean.getError())){
                                LogCustom.i(Const.TAG.ZY_HTTP, "KEY ERROR:重新请求KEY");
                                sendKeyErrorStringCallback(response.request(), resCallBack, 101);
                                return;
                            }
                        }
                    }catch (Exception e){

                    }*/

                    sendSuccessResultCallback(string, resCallBack);

                } catch (Exception e) {
                    LogCustom.i(Const.TAG.ZY_HTTP, "请求成功,IO异常"+response.request().toString()+"异常信息：" + e.toString());
                    sendFailedStringCallback(response.request(), e, resCallBack);
                }
            }
        });
    }

    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(request, e);
                callback.onAfter();
            }
        });
    }

    private void sendKeyErrorStringCallback(final Request request,  final ResultCallback callback, final int keyFlag) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onKeyError(request, keyFlag);
                callback.onAfter();
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object);
                callback.onAfter();
            }
        });
    }

    public static abstract class ResultCallback<T> {
        Type mType;

        public ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public void onBefore(Request request) {
        }

        public void onAfter() {
        }

        public abstract void onError(Request request, Exception e);

        public abstract void onKeyError(Request request,int keyFlag);

        public abstract void onResponse(T response);
    }

    public static class Param {
        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

        String key;
        String value;
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }
}

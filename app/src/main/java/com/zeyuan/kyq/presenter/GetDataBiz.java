package com.zeyuan.kyq.presenter;

import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.ZYHttpClientManager;

import java.util.Map;

/**
 * Created by Administrator on 2015/10/16.
 * 网络访问的数据层
 */
public class GetDataBiz {

    /**
     * 网络访问
     * @param map            请求的数据
     * @param url            url
     * @param resultCallback 请求的回调
     */
    public void getData(Map map, String url, OkHttpClientManager.ResultCallback resultCallback) {
        if (map == null || map.size() == 0) {
            LogUtil.e("error:","post data is empty");
            return;
        }
//        map.put(); seccion //服务器给的鉴权字段
        OkHttpClientManager.postAsyn(url, map, resultCallback);
    }

    public void getResponse(String url,String[] args,ZYHttpClientManager.ResultCallback resultCallback){
        if (args == null || args.length == 0) {
            LogUtil.e("error:","post data is empty");
            return;
        }
        ZYHttpClientManager.zyPostAsyn(url, HttpSecretUtils.getParamString(args),resultCallback,0);
    }
}

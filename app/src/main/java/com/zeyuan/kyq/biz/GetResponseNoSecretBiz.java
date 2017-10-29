package com.zeyuan.kyq.biz;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.ArticleDetailBean;
import com.zeyuan.kyq.bean.ForumDetailBean;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.utils.ZYHttpClientManager;

/**
 * Created by Administrator on 2016/4/29.
 *
 * 传参加密，数据不加密的请求
 *
 * @author wwei
 */
public class GetResponseNoSecretBiz implements KeyBiz.GetKeyCallBack{

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
    public GetResponseNoSecretBiz(HttpResponseInterface responseInterface,int flag){
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
        ZYHttpClientManager.zyPostAsynNoSecret(getPostUrl(), sendPage.getPostParams(flag), new ZYHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.i(Const.TAG.ZY_HTTP, "请求失败：" + request.toString());
                sendPage.showError(flag);
            }

            @Override
            public void onKeyError(Request request, int keyFlag) {
                /*angin++;
                Const.KEY_ANGIN_TIMES++;
                if(keyFlag == 101&&angin<2&&Const.KEY_ANGIN_TIMES<Const.KEY_MAX){
                    KeyBiz.getKey(GetResponseNoSecretBiz.this);
                }*/
            }

            @Override
            public void onResponse(String response) {
                sendPage.hideLoading(flag);
                Object o;
                o = getGsonType(response);
                Log.i(Const.TAG.ZY_HTTP,"O:");
                if (o != null) {
                    Log.i(Const.TAG.ZY_HTTP,"O:"+o.toString());
                    sendPage.toActivity(o, flag);
                }
            }
        }, 0);
    }

    private String getPostUrl(){
        String url = null;

        switch (flag) {
            case Const.EGetArticleDetail:
                url = Const.E_GET_ARTICLE_DETAIL;
                break;
            case Const.EGetForumDetail:
                url = Const.E_GET_FORUM_DETAIL;
                break;
            case Const.EGetInfoCash:
                url = "http://help.kaqcn.com/help/atm_wx";
                break;
        }
        Log.i(Const.TAG.ZY_HTTP, "请求地址：" + url);
        return url;
    }


    private Object getGsonType(String response){
        Object o = null;
        try {
            switch (flag){
                case Const.EGetArticleDetail://1
                    o = mGson.fromJson(response, ArticleDetailBean.class);
                    break;
                case Const.EGetForumDetail://1
                    o = mGson.fromJson(response, ForumDetailBean.class);
                    break;
                case Const.EGetInfoCash://1
                    o = response;
                    break;
            }

        }catch (com.google.gson.JsonParseException e){
            Log.i(Const.TAG.ZY_HTTP, "请求成功,JSON异常@"+flag+"@"+ e.toString());
        }

        return o;
    }

    @Override
    public void getKeySuccess(int[] keys) {
        try {
            String temp = HttpSecretUtils.getSaveKeyString(keys);
            HttpSecretUtils.TEA.setKey(Const.KEY_FINAL);
            String save = HttpSecretUtils.TEA.encryptByTea(temp);
            UserinfoData.saveKeyStr((Context) sendPage, save);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "保存KEY失败");
        }
        HttpSecretUtils.TEA.setKey(keys);
        postResponse();
    }

    @Override
    public void getKeyFailed() {

    }
}

package com.zeyuan.kyq.biz;

import android.text.TextUtils;

import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.LogCustom;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/23.
 *
 *
 *
 * @author wwei
 */
public class HttpRespBiz {

    //获取手机验证码
    public static final int P_MOBILE = 1;
    public static final String P_MOBILE_URL = "http://help.kaqcn.com/help/mobile_code_create";
    public static final int P_CHECK_OPEN = 2;
    public static final String P_Check_Open_Url = "http://help.kaqcn.com/help/check_user_info";
    public static final int P_CHECK_INFO_PIN = 3;
    public static final String P_Check_Info_Pin_Url = "http://help.kaqcn.com/help/change_user_info";

    private ReqResult callback;
    private int flag;
    private String url = null;
    private Map<String,String> map;

    public static final int UI_LOOPER = 1;
    public static final int NOT_UI_LOOPER = 2;

    public interface ReqResult{
        Map<String,String> getParams(int flag);
        void getReq(String req,int flag);
        void Fail(int flag,int tag);
    }

    public HttpRespBiz(ReqResult callback,int flag){
        this.callback = callback;
        this.flag = flag;
        getUrlForFlag();
        map = callback.getParams(flag);
    }

    public void post() {
        if (map==null||map.size()==0|| TextUtils.isEmpty(url)){
            LogCustom.i(Const.TAG.ZY_HTTP,"参数或请求地址为空");
            callback.Fail(flag,UI_LOOPER);
        }else {
            try {
                final HttpClient client = new DefaultHttpClient();
                final StringBuilder builder = new StringBuilder();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HttpPost post = new HttpPost(url);
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            String p = "";
                            for (String key:map.keySet()){
                                params.add(new BasicNameValuePair(key, map.get(key)));
                                if (TextUtils.isEmpty(p)){
                                    p += key + "=" + map.get(key);
                                }else {
                                    p += "&" + key + "=" + map.get(key);
                                }
                            }
                            LogCustom.i(Const.TAG.ZY_HTTP, "请求地址："+url+"\n请求参数："+p);
                            try {
                                post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                            } catch (UnsupportedEncodingException e1) {
                                LogCustom.i(Const.TAG.ZY_HTTP,"参数处理出错");
                                callback.Fail(flag,NOT_UI_LOOPER);
                            }

                            try {
                                HttpResponse response = client.execute(post);
                                HttpEntity entity = response.getEntity();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                                for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                                    builder.append(s);
                                }
                                LogCustom.i(Const.TAG.ZY_HTTP, "请求地址："+url+"\n请求数据："+builder.toString());
                                callback.getReq(builder.toString(), flag);
                            } catch (Exception e1) {
                                callback.Fail(flag,NOT_UI_LOOPER);
                            }

                        }catch (Exception e){
                            callback.Fail(flag,NOT_UI_LOOPER);
                        }
                    }
                }).start();
            }catch (Exception e){
                callback.Fail(flag,UI_LOOPER);
            }
        }
    }

    private String getUrlForFlag(){
        switch (flag){
            case P_MOBILE:
                url = P_MOBILE_URL;
                break;
            case P_CHECK_OPEN:
                url = P_Check_Open_Url;
                break;
            case P_CHECK_INFO_PIN:
                url = P_Check_Info_Pin_Url;
                break;
        }
        return url;
    }

}

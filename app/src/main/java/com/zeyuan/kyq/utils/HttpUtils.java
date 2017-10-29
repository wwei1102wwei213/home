package com.zeyuan.kyq.utils;

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
 * Created by Administrator on 2016/9/17.
 *
 *
 * @author wwei
 */
public class HttpUtils {

    public interface ReqResult{
        void getReq(String req);
        void Fail(int tag);
    }

    public static void postSomething(final Map<String,String> map, final ReqResult callback) {
        if (map==null||map.size()==0){
            callback.Fail(0);
        }else {
            try {
                final HttpClient client = new DefaultHttpClient();
                final StringBuilder builder = new StringBuilder();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HttpPost post = new HttpPost(Const.P_Take_Cash_Url);
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            for (String key:map.keySet()){
                                params.add(new BasicNameValuePair(key, map.get(key)));
                            }
                            try {
                                post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                            } catch (UnsupportedEncodingException e1) {
                                callback.Fail(1);
                            }
                            try {
                                HttpResponse response = client.execute(post);
                                HttpEntity entity = response.getEntity();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                                for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                                    builder.append(s);
                                }
                                callback.getReq(builder.toString());
                            } catch (Exception e1) {
                                callback.Fail(1);
                            }
                        }catch (Exception e){
                            callback.Fail(1);
                        }
                    }
                }).start();
            }catch (Exception e){
                callback.Fail(0);
            }
        }
    }
}

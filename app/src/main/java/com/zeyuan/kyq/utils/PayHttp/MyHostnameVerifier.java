package com.zeyuan.kyq.utils.PayHttp;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created by Administrator on 2016/9/5.
 */
public class MyHostnameVerifier implements HostnameVerifier{
    @Override
    public boolean verify(String hostname, SSLSession session) {
        // TODO Auto-generated method stub
        return true;
    }
}

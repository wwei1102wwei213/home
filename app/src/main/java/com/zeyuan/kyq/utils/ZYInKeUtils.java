package com.zeyuan.kyq.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import com.meelive.ingkee.sdk.plugin.entity.ShareInfo;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2017/4/12.
 *
 * @author wwei
 */
public class ZYInKeUtils {


    public static void toShare(ShareInfo share,Context context,IUiListener listener){
        String type = share.platform;
        if ("qq".equals(type)){
            onClickShare(0,context,share,listener);
        }else if ("wechat".equals(type)){
            initWechat(0, context, share);
        }else if ("moments".equals(type)){
            initWechat(1,context,share);
        }else if ("qqzone".equals(type)){
            onClickShare(1,context,share,listener);
        }
    }

    private static byte[] bytes;
    public static void initWechat(final int flag,final Context context,final ShareInfo share){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bytes = PhotoUtils.getImageFromURL(share.picUrl);
                }catch (Exception e){
                    LogCustom.i("ZYS","图片流获取异常");
                }
            }
        }).start();

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(200);
                }catch (Exception e){

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                wechatShare(flag,context,share);
            }
        }.execute();
    }

    private static void wechatShare(int flag,Context context,ShareInfo share){
        try {

            IWXAPI wxApi = WXAPIFactory.createWXAPI(context, Contants.AppCfg.WX__APPID);
            wxApi.registerApp(Contants.AppCfg.WX__APPID);
            WXWebpageObject webpage = new WXWebpageObject();
            if (flag==0){
                webpage.webpageUrl = share.url + "&kaqsharetype=1";
            }else {
                webpage.webpageUrl = share.url + "&kaqsharetype=2";
            }
            String shareTitle = share.text;
            WXMediaMessage msg = new WXMediaMessage(webpage);
            if (!TextUtils.isEmpty(shareTitle)&&shareTitle.length()>100)
                shareTitle = shareTitle.substring(0,100);
            msg.title = shareTitle;
            msg.description = share.content;
            //这里替换一张自己工程里的图片资源
            Bitmap thumb = null;
            try {
                if(bytes!=null){
                    thumb = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    LogCustom.i("ZYS","thumb1:"+thumb.getByteCount());
                    thumb = ThumbnailUtils.extractThumbnail(thumb, 100, 100);
                    LogCustom.i("ZYS","thumb2:"+thumb.getByteCount());
                    if (thumb.getByteCount()>256000){
                        thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
                        msg.setThumbImage(thumb);
                    }else {
                        msg.setThumbImage(thumb);
                    }
                }else{
                    thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
                    msg.setThumbImage(thumb);
                }
            }catch (Exception e){
                ExceptionUtils.ExceptionSend(e,"分享图片加载失败");
            }
            msg.setThumbImage(thumb);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = String.valueOf(System.currentTimeMillis());
            req.message = msg;
            req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;
            wxApi.sendReq(req);
            try {
                if(thumb!=null){
                    thumb.recycle();
                }
                if(bytes!=null){
                    bytes=null;
                }
            }catch (Exception e) {

            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, context, "InKe WXShare");
        }
    }

    private static void onClickShare(int flag,Context context,ShareInfo share,IUiListener listener) {
        try {
            Tencent mTencent = Tencent.createInstance(Contants.AppCfg.QQ__APPID, context);
            final Bundle params = new Bundle();
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            params.putString(QQShare.SHARE_TO_QQ_TITLE, share.text);
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, share.content);
            if (flag==0){
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, share.url + "&kaqsharetype=3");
            }else {
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, share.url + "&kaqsharetype=4");
            }
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, share.picUrl);
            params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "抗癌圈");
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, flag);
            mTencent.shareToQQ((BaseActivity) context, params, listener);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, context, "InKe onClickShare");
        }
    }

    public static Bitmap getImage(String path){
        ExecutorService exs= Executors.newCachedThreadPool();
        Future<Bitmap> mBmp = exs.submit(new getImageThread(path));
        Bitmap bmp = null;
        try {
            bmp = mBmp.get();
        }catch (Exception e){

        }
        return bmp;
    }

    static class getImageThread implements Callable<Bitmap> {

        private String path;

        public getImageThread(String path){
            this.path = path;
        }

        @Override
        public Bitmap call() throws Exception {
            try {
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream is = conn.getInputStream();
                return BitmapFactory.decodeStream(is);
            }catch (Exception e){
                ExceptionUtils.ExceptionSend(e,"getImageThread");
                return null;
            }
        }
    }

}

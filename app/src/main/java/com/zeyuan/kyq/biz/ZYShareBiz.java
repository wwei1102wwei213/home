package com.zeyuan.kyq.biz;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.zeyuan.kyq.R;

/**
 * Created by Administrator on 2016/4/28.
 *
 * 分享功能业务类
 *
 * @author wwei
 */
public class ZYShareBiz {

    public ZYShareBiz(){

    }
    /*private Tencent mTencent;
    private IWXAPI wxApi;*/

    public static void shareToQQ(Tencent tencent,Activity activity,IUiListener listener,String shareTitle,String shareContent,String shareURl,String shareImgUrl,int flag){
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, shareTitle);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  shareContent);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareURl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareImgUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "抗癌圈");
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, flag);
        tencent.shareToQQ(activity, params, listener);
    }

    public static void shareToWX(IWXAPI wxApi, Context context, byte[] json, String shareTitle, String shareContent, String shareURl, int flag){
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareURl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = shareTitle;
        msg.description = shareContent;
        //这里替换一张自己工程里的图片资源
        if(json!=null){
            msg.thumbData = json;
        }else{
            Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            msg.setThumbImage(thumb);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;
        wxApi.sendReq(req);
    }
}

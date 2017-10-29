package com.zeyuan.kyq.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.manager.ShareResultManager;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.PhotoUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/4/30.
 * <p>
 * 分享窗口
 *
 * @author wwei
 */
public class ShareFragment extends DialogFragment implements View.OnClickListener {

    public static final String type = "ShareFragment";
    private String shareURl;
    private Tencent mTencent;
    private IWXAPI wxApi;
    private String shareTitle;
    private String shareContent;
    private String shareImgUrl;
    private int flag;
    private byte[] json;
    private boolean updataflag = true;


    public static ShareFragment getInstance(String shareURl, String shareTitle, String shareContent
            , String shareImgUrl, int flag) {

        Bundle bundle = new Bundle();
        bundle.putString(Const.SHAREURL, shareURl);
        bundle.putString(Const.SHARETITLE, shareTitle);
        bundle.putString(Const.SHARECONTENT, shareContent);
        bundle.putInt(Const.SHAREFLAG, flag);
        if (!TextUtils.isEmpty(shareImgUrl)) {
            bundle.putString(Const.SHAREIMGURL, shareImgUrl);
        }
        ShareFragment instance = new ShareFragment();
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            shareURl = bundle.getString(Const.SHAREURL);
            checkUrl();
            shareTitle = bundle.getString(Const.SHARETITLE);
            shareContent = bundle.getString(Const.SHARECONTENT);
            if (TextUtils.isEmpty(shareContent)) {
                shareContent = getString(R.string.share_content_init);
            }
            shareImgUrl = bundle.getString(Const.SHAREIMGURL, "http://www.kaqcn.com/img/share.png");
            flag = bundle.getInt(Const.SHAREFLAG, 0);
            if (updataflag && json == null) {
                updataflag = false;
                getByteImg();
            }
        }
        wxApi = WXAPIFactory.createWXAPI(getActivity(), Contants.AppCfg.WX__APPID);
        wxApi.registerApp(Contants.AppCfg.WX__APPID);
        mTencent = Tencent.createInstance(Contants.AppCfg.QQ__APPID, getActivity());
    }


    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.dialog);
        dialog.setCancelable(false);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_share, null);
        initView(view);
        /*initData();*/
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        if (window != null) {
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            window.setAttributes(lp);
            window.setWindowAnimations(R.style.mystyle);
        }
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    private void initView(View view) {
        try {
            view.findViewById(R.id.ll_shareqq).setOnClickListener(this);
            view.findViewById(R.id.ll_sharewx).setOnClickListener(this);
            view.findViewById(R.id.ll_sharewxpyq).setOnClickListener(this);
            view.findViewById(R.id.ll_shareqqzone).setOnClickListener(this);
            view.findViewById(R.id.iv_share_close).setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, getActivity(), "ShareFragment");
        }
    }

    private void checkUrl() {
        try {
            String reg = "kaq=[0-9]{6,19}";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(shareURl);
            boolean find = m.find();
            if (find) {
                String temp = m.group(0);
                shareURl = shareURl.replace(temp, "kaq=share");
            }
            if (shareURl.contains("?")) {
                shareURl = shareURl.concat("&kaqsharetag=kaq");
            } else {
                shareURl = shareURl.concat("?kaqsharetag=kaq");
            }
            LogCustom.i("ZYS", "shareURl:" + shareURl);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "checkUrl");
        }

    }

    private void getByteImg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    json = PhotoUtils.getImageFromURL(shareImgUrl);
                } catch (Exception e) {
                    LogCustom.i("ZYS", "图片流获取异常");
                }
            }
        }).start();
    }


    private void onClickShare(int flag) {
        try {
            final Bundle params = new Bundle();
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            params.putString(QQShare.SHARE_TO_QQ_TITLE, shareTitle);
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareContent);
            if (flag == 0) {
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareURl + "&kaqsharetype=3");
            } else {
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareURl + "&kaqsharetype=4");
            }
            LogCustom.i("ZYS", "shareTitle:" + shareTitle);
            LogCustom.i("ZYS", "shareContent:" + shareContent);
            LogCustom.i("ZYS", "shareImgUrl:" + shareImgUrl);
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareImgUrl);
            params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "抗癌圈");
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, flag);
            mTencent.shareToQQ(getActivity(), params, ShareResultManager.getInstance().getShareListener());

        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, getActivity(), "StepsActivity");
        }
    }

    public void wechatShare(int flag) {

        try {
            WXWebpageObject webpage = new WXWebpageObject();
            if (flag == 0) {
                webpage.webpageUrl = shareURl + "&kaqsharetype=1";
            } else {
                webpage.webpageUrl = shareURl + "&kaqsharetype=2";
            }
            WXMediaMessage msg = new WXMediaMessage(webpage);
            if (!TextUtils.isEmpty(shareTitle) && shareTitle.length() > 100)
                shareTitle = shareTitle.substring(0, 100);
            msg.title = shareTitle;
            msg.description = shareContent;
            //这里替换一张自己工程里的图片资源
            Bitmap thumb = null;
            try {
                if (json != null) {
                    thumb = BitmapFactory.decodeByteArray(json, 0, json.length);
                    thumb = ThumbnailUtils.extractThumbnail(thumb, 100, 100);
                    if (thumb.getByteCount() > 256000) {
                        thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                        msg.setThumbImage(thumb);
                    } else {
                        msg.setThumbImage(thumb);
                    }
                } else {
                    thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    msg.setThumbImage(thumb);
                }
            } catch (Exception e) {
                Log.i("ZYS", "JSON error");
            }
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = String.valueOf(System.currentTimeMillis());
            req.message = msg;
            req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
            wxApi.sendReq(req);

            try {
                if (thumb != null) {
                    thumb.recycle();
                }
            } catch (Exception e) {

            }
            LogCustom.i("ZYS", "wechatShare OK" + req.toString());
        } catch (Exception e) {
            LogCustom.i("ZYS", "wechatShare error");
            ExceptionUtils.ExceptionToUM(e, getActivity(), "wechatShare");
        }
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_shareqq:
                Factory.onEvent(getActivity(), getEventId(), Const.EVENTFLAG, "ShareToQQ");
                onClickShare(0);
                break;
            case R.id.ll_sharewx:
                try {
                    Factory.onEvent(getActivity(), getEventId(), Const.EVENTFLAG, "ShareToWX");
                } catch (Exception e) {
                    ExceptionUtils.ExceptionToUM(e, getActivity(), "wechatShare");
                    LogCustom.i("ZYS", "Data Error:" + shareURl + "\n" + shareContent + "\n" + shareTitle);
                }
                LogCustom.i("ZYS", "Data:" + shareURl + "\n" + shareContent + "\n" + shareTitle);
                wechatShare(0);
                break;
            case R.id.ll_sharewxpyq:
                Factory.onEvent(getActivity(), getEventId(), Const.EVENTFLAG, "ShareToWXPYQ");
                wechatShare(1);
                break;
            case R.id.ll_shareqqzone:
                Factory.onEvent(getActivity(), getEventId(), Const.EVENTFLAG, "ShareToQQZone");
                onClickShare(1);
                break;
            case R.id.iv_share_close:
                dismiss();
                break;
        }
    }

    private String getEventId() {
        if (flag == 0) return Const.EVENT_ShareApp;
        String temp = Const.EVENT_ShareApp;
        switch (flag) {
            case Const.SHARE_APP:
                break;
            case Const.SHARE_RECORD:
                temp = Const.EVENT_ShareMRB;
                break;
            case Const.SHARE_ARTICLE:
                temp = Const.EVENT_ShareArticle;
                break;
            case Const.SHARE_FORUM:
                temp = Const.EVENT_ShareForum;
                break;
            case Const.SHARE_RESULT:
                temp = Const.EVENT_ShareResult;
                break;
        }
        return temp;
    }

    @Override
    public void onPause() {
        dismiss();
        super.onPause();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }
}

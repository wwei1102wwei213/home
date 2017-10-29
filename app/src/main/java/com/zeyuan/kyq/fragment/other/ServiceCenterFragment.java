package com.zeyuan.kyq.fragment.other;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.youzan.sdk.YouzanToken;
import com.youzan.sdk.event.AbsAuthEvent;
import com.youzan.sdk.event.AbsChooserEvent;
import com.youzan.sdk.event.AbsShareEvent;
import com.youzan.sdk.model.goods.GoodsShareModel;
import com.youzan.sdk.web.plugin.YouzanBrowser;
import com.youzan.sdk.web.plugin.YouzanClient;
import com.zeyuan.kyq.Entity.ServiceCenterBean;
import com.zeyuan.kyq.Entity.ServiceCenterItemEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.ServiceCenterRecyclerAdapter;
import com.zeyuan.kyq.app.BaseZyFragment;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.YouzanLoginBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.MyWebChromeClient;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.ShowImageActivity;
import com.zeyuan.kyq.widget.CustomView.InsideRecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/21.
 *
 * @author wwei
 */
public class ServiceCenterFragment extends BaseZyFragment implements HttpResponseInterface, MyWebChromeClient.WebCall
        , ServiceCenterRecyclerAdapter.onItemClickListener {

    //当前加载的url
    private String mUrl;
    //加载进度条
    private ProgressBar sb;
    //顶部栏目控件
    private InsideRecyclerView rv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_service_center, container, false);
        initView();
        initWebView();
        initData();
        return rootView;
    }


    private ServiceCenterRecyclerAdapter adapter;

    private void initView() {
        sb = (ProgressBar) findViewById(R.id.pb_service);
        rv = (InsideRecyclerView) findViewById(R.id.rv);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(manager);
        adapter = new ServiceCenterRecyclerAdapter(context, new ArrayList<ServiceCenterItemEntity>(), this);
        rv.setAdapter(adapter);
    }

    private YouzanBrowser webView;

    private void initWebView() {
        webView = (YouzanBrowser) findViewById(R.id.wv);
        final WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);    //启用JS脚本
        webView.addJavascriptInterface(this, "kaq");
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setDomStorageEnabled(true);
        MyWebChromeClient wcc = new MyWebChromeClient();
        wcc.setWebCall(this);
        WebViewClient client = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                try {
                    sb.setProgress(0);
                    sb.setVisibility(View.VISIBLE);
                    mUrl = url;
                } catch (Exception e) {

                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String nurl) {
                LogCustom.i("ZYS", "URL是：" + nurl);
                if (nurl.endsWith(".pdf")) {
                    Intent intent = new Intent();
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(nurl), "application/pdf");
                    startActivity(intent);
                    return true;
                } else if (nurl.endsWith(".jpg") || nurl.endsWith(".JPG") || nurl.endsWith(".jpeg") ||
                        nurl.endsWith(".JPEG") || nurl.endsWith(".png") || nurl.endsWith(".PNG")) {
                    startActivity(new Intent(context, ShowImageActivity.class).
                            putExtra(Contants.Avatar, nurl));
                    return true;
                } else {
                    mUrl = nurl;
                }
                return super.shouldOverrideUrlLoading(view, nurl);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                try {
                    sb.setVisibility(View.GONE);
                    view.loadUrl("javascript:" + Const.ZY_JS_FORUM);
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "onPageFinished");
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        };

        webView.setWebChromeClient(wcc);
        webView.setWebViewClient(client);

        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                        //后退
                        webView.goBack();
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
        setupYouzanView(webView);
        //获取登录有赞的信息
        Factory.postPhp(this, Const.Api_YouzanLogin);
    }

    private void initData() {
        Factory.postPhp(this, Const.PApiServeCenterlist);
    }

    private void setupYouzanView(YouzanClient client) {
        //订阅认证事件
        client.subscribe(new AbsAuthEvent() {
            /**
             * 有赞SDK认证回调.
             * 在加载有赞的页面时, SDK相应会回调该方法.
             *
             * 从自己的服务器上请求同步认证后组装成{@link com.youzan.sdk.YouzanToken}, 调用{code view.sync(token);}同步信息.
             *
             * @param view 发起回调的视图
             * @param needLogin 表示当下行为是否需要需要用户角色的认证信息, True需要.
             */
            @Override
            public void call(View view, boolean needLogin) {
                /**
                 * <pre>
                 *     处理逻辑
                 *
                 *     1. 判断是否需要需要用户角色的认证信息?
                 *     2. 是(needLogin=True) : 判断App内的用户是否登录? 已登录:  向服务端请求带用户角色的认证信息, 并同步给SDK; 未登录: 唤起App内登录界面.
                 *     3. 否(needLogin=False): 向服务端请求不需要登录态的认证信息, 并同步给SDK.
                 * </pre>
                 */

                if (needLogin) {
                    Factory.postPhp(ServiceCenterFragment.this, Const.Api_YouzanLogin);
                }

            }
        });

        //订阅文件选择事件
        client.subscribe(new AbsChooserEvent() {
            @Override
            public void call(View view, Intent intent, int requestCode) throws ActivityNotFoundException {
                //调用系统图片选择器
                startActivity(intent);
            }
        });

        //订阅分享事件
        client.subscribe(new AbsShareEvent() {
            @Override
            public void call(View view, GoodsShareModel data) {
                /**
                 * 在获取数据后, 可以使用其他分享SDK来提高分享体验.
                 * 这里调用系统分享来简单演示分享的过程.
                 */
                String content = String.format("%s %s", data.getDesc(), data.getLink());
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, content);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, data.getTitle());
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

    }

    @Override
    public void onTabItemClick(ServiceCenterItemEntity entity) {
        webView.loadUrl(entity.getUrl());
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(context));
        if (tag == Const.PApiServeCenterlist) {
            map.put("type", "1");
        } else if (tag == Const.Api_YouzanLogin) {
            map.put("client_id", ZYApplication.Youzan_client_id);
            map.put("client_secret", ZYApplication.Youzan_client_secret);
            map.put("open_user_id", UserinfoData.getInfoID(getActivity().getApplicationContext()));
            map.put("nick_name", UserinfoData.getInfoname(getActivity().getApplicationContext()));
            map.put("telephone", UserinfoData.getPhoneNum(getActivity().getApplicationContext()));
            map.put("avatar", UserinfoData.getAvatarUrl(getActivity().getApplicationContext()));
            // map.put("gender",UserinfoData.getUserInfoMsg())

        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PApiServeCenterlist) {
            ServiceCenterBean bean = (ServiceCenterBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                List<ServiceCenterItemEntity> list = bean.getData();
                if (list != null && list.size() > 0) {
                    if (list.size() == 1) {
                        rv.setVisibility(View.GONE);
                    } else {
                        rv.setVisibility(View.VISIBLE);
                        adapter.update(list);
                    }
                    webView.loadUrl(list.get(0).getUrl());
                } else {
                    rv.setVisibility(View.GONE);
                }
            } else {
                showToast("服务数据获取失败");
            }
        } else if (flag == Const.Api_YouzanLogin) {
            YouzanLoginBean bean = (YouzanLoginBean) response;
            if (bean.getCode() == 0) {
                YouzanToken youzanToken = new YouzanToken();
                youzanToken.setCookieKey(bean.getData().getCookie_key());
                youzanToken.setAccessToken(bean.getData().getAccess_token());
                youzanToken.setCookieValue(bean.getData().getCookie_value());
                // mView.sync(youzanToken);
                webView.sync(youzanToken);
            }
        }
    }

    @Override
    public void showLoading(int flag) {

    }

    @Override
    public void hideLoading(int flag) {

    }

    @Override
    public void showError(int flag) {
        if (flag == Const.PApiServeCenterlist) {

        }
        showToast("网络请求失败");
    }


    private String summary;
    private String shareImgUrl;

    @JavascriptInterface
    public void showSource(String html, String headerUrl) {
        //获取分享内容
        try {
            if (TextUtils.isEmpty(html)) {
                summary = "";
            } else {
                summary = html;
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "showSource");
        }
        //获取分享图标
        try {
            if (TextUtils.isEmpty(headerUrl)) {
                shareImgUrl = null;
            } else {
                shareImgUrl = headerUrl;
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "showHeaderUrl");
        }
    }

    /**
     * 页面title
     *
     * @param title
     */
    @Override
    public void forTitle(String title) {

    }

    /**
     * 页面加载进度
     *
     * @param pos
     */
    @Override
    public void forProgress(int pos) {
        if (pos <= 100) {
            sb.setProgress(pos);
        }
    }


    /**
     * 5.0及以上版本调用相册
     *
     * @param uploadMsg
     */
    @Override
    public void fileChose5(ValueCallback<Uri[]> uploadMsg) {
        takePhoto5(uploadMsg);
    }

    /**
     * 5.0以下调用系统相册
     *
     * @param uploadMsg
     */
    @Override
    public void fileChose(ValueCallback<Uri> uploadMsg) {
        takePhoto(uploadMsg);
    }


    private static final int RESULT_OK = -1;

    /**
     * 相册或拍照的系统回调
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent intent) {
        try {
            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == mUploadMessage)
                    return;
                Uri result = intent == null || resultCode != RESULT_OK ? null
                        : intent.getData();
                if (result == null && cameraPath != null) {
                    File f = new File(cameraPath);
                    if (f.exists() && f.length() != 0) {
                        result = photoUri;
                    }
                }
                LogCustom.i("ZYS", "URI:" + result);
                mUploadMessage.onReceiveValue(result);
                cameraPath = null;
                mUploadMessage = null;
                photoUri = null;

            } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
                if (null == mUploadMessageForAndroid5)
                    return;
                Uri result = (intent == null || resultCode != RESULT_OK) ? null
                        : intent.getData();
                if (result == null && cameraPath != null) {
                    File f = new File(cameraPath);
                    if (f.exists() && f.length() != 0) {
                        result = photoUri;
                    }
                }
                LogCustom.i("ZYS", "URI:" + result);
                if (result != null) {
                    mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
                    mUploadMessageForAndroid5 = null;
                    photoUri = null;
                } else {
                    mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
                }
                cameraPath = null;
                mUploadMessageForAndroid5 = null;
                photoUri = null;

            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "ServiceCenterFragment mUploadMessageForAndroid5");
        }
    }


    public final static int FILECHOOSER_RESULTCODE = 1;
    public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;
    public ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;

    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "文件选择"),
                FILECHOOSER_RESULTCODE);
    }

    private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
        mUploadMessageForAndroid5 = uploadMsg;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");
        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "图片选择");
        startActivityForResult(chooserIntent,
                FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
    }

    /**
     * 调用系统相册或拍照
     *
     * @param uploadMsg
     */
    public void takePhoto(final ValueCallback<Uri> uploadMsg) {
        try {
            new AlertView("上传头像方式", null, "取消", null, new String[]{"拍照", "从相册中选择"},
                    context, AlertView.Style.ActionSheet, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    switch (position) {
                        case 0:
                            fromTP(uploadMsg);
                            break;
                        case 1:
                            openFileChooserImpl(uploadMsg);
                            break;
                    }
                }
            }).setCancelable(true).show();
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, context, "ServiceCenterFragment");
        }
    }

    public void takePhoto5(final ValueCallback<Uri[]> uploadMsg) {
        try {
            new AlertView("上传头像方式", null, "取消", null, new String[]{"拍照", "从相册中选择"},
                    context, AlertView.Style.ActionSheet, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    switch (position) {
                        case 0:
                            fromTP5(uploadMsg);
                            break;
                        case 1:
                            openFileChooserImplForAndroid5(uploadMsg);
                            break;
                    }
                }
            }).setCancelable(true).show();
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, context, "ServiceCenterFragment");
        }
    }

    /**
     * 从拍照中获取图片
     */
    private void fromTP5(ValueCallback<Uri[]> uploadMsg) {
        try {
            mUploadMessageForAndroid5 = uploadMsg;
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            photoUri = getOutputMediaFileUri();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, context, "ServiceCenterFragment");
        }
    }

    private Uri photoUri = null;

    private void fromTP(ValueCallback<Uri> uploadMsg) {
        try {
            mUploadMessage = uploadMsg;
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            photoUri = getOutputMediaFileUri();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, FILECHOOSER_RESULTCODE);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, context, "ServiceCenterFragment");
        }
    }

    /**
     * 用于拍照时获取输出的Uri
     *
     * @version 1.0
     * @author
     * @return
     */
    private String cameraPath = null;

    protected Uri getOutputMediaFileUri() {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Night");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        cameraPath = mediaFile.getAbsolutePath();
        return Uri.fromFile(mediaFile);
    }

    @Override
    public void onPause() {
        super.onPause();
        //暂停WebView在后台的所有活动
        webView.onPause();
        //暂停WebView在后台的JS活动
        webView.pauseTimers();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
        webView.resumeTimers();
    }
}

package com.zeyuan.kyq.view;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.meelive.ingkee.sdk.plugin.InKeSdkPluginAPI;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.forcallback.FragmentCallBack;
import com.zeyuan.kyq.fragment.ShareFragment;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.MyWebChromeClient;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2016/4/28.
 * <p>
 * HTML页面装载页
 */
public class ShowDiscuzActivity extends BaseActivity implements MyWebChromeClient.WebCall, FragmentCallBack {

    private String url = "";
    private String id = "";
    private String title = "";
    private String summary = "";
    private String shareImgUrl = null;
    private WebView webView;
    private ProgressBar sb;

    public final static int FILECHOOSER_RESULTCODE = 1;
    public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;
    public ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_discuz);
        try {
            id = getIntent().getStringExtra(Const.INTENT_SHOW_DISCUZ_ID);
            if (!TextUtils.isEmpty(id)) {
                if (TextUtils.isEmpty(Const.SHOW_DISCUZ_ARTICLE)) {
                    Object obj = Factory.getData(Const.N_UrlAirtcleIndex);
                }
                if (!TextUtils.isEmpty(Const.SHOW_DISCUZ_ARTICLE)) {
                    url = Const.SHOW_DISCUZ_ARTICLE + id;
                } else {
                    url = "http://bbs.kaqcn.com/portal_share.php?mobile=1&aid=" + id;
                }
            }
            String temp = getIntent().getStringExtra(Const.SHOW_HTML_MAIN_TOP);
            if (!TextUtils.isEmpty(temp)) {
                url = temp;
            }
            initview();
            initlistener();

        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ShowDiscuzActivity");
        }
    }

    private TextView v_title;

    private void initview() {
        try {
            v_title = (TextView) findViewById(R.id.tv_title);
            sb = (ProgressBar) findViewById(R.id.sb_wv);
            sb.setMax(100);
            initwebview();
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ShowDiscuzActivity");
        }
    }

    private IWXAPI api;
    private static final String APP_ID = "wx02cca444de652c20";
    public static final String PARTNER_ID = "1370371202";

    private void initWX() {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(this, null);
            api.registerApp(APP_ID);
        }
    }

    private void initwebview() {
        webView = (WebView) findViewById(R.id.webview_show_discuz);
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
                    ShowDiscuzActivity.this.url = url;
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
                    startActivity(new Intent(ShowDiscuzActivity.this, ShowImageActivity.class).
                            putExtra(Contants.Avatar, nurl));
                    return true;
                } else {
                    url = nurl;
                }
                return super.shouldOverrideUrlLoading(view, nurl);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogCustom.i("ZYS", "onPageFinished：" + "onPageFinished");
                try {
                    sb.setVisibility(View.GONE);
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "onPageFinished");
                }
                try {
                    ShowDiscuzActivity.this.url = url;
                    view.loadUrl("javascript:" + mJs);
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "view.loadUrl");
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
        webView.loadUrl(url);
    }

    private ShareFragment fragment;

    private void initlistener() {
        try {
            findViewById(R.id.v_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            ImageView iv_share = (ImageView) findViewById(R.id.iv_share);
            iv_share.setImageResource(R.mipmap.share_step_def);
            iv_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        fragment = ShareFragment.getInstance(url, title, summary, shareImgUrl, 3);
                        if (fragment.getDialog() == null || !fragment.getDialog().isShowing())
                            fragment.show(getSupportFragmentManager(), ShareFragment.type);
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {

        }
    }

    private void setPdfFile(String urlString) {
//        String urlString = "http://public.dhe.ibm.com/common/ssi/ecm/en/wsd14109usen/WSD14109USEN.PDF";
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)
                    url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            //实现连接
            connection.connect();

            if (connection.getResponseCode() == 200) {
                InputStream is = connection.getInputStream();
                //以下为下载操作
                byte[] arr = new byte[1];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BufferedOutputStream bos = new BufferedOutputStream(baos);
                int n = is.read(arr);
                while (n > 0) {
                    bos.write(arr);
                    n = is.read(arr);
                }
                bos.close();
                String path = Environment.getExternalStorageDirectory()
                        + "/download/";
                String[] name = urlString.split("/");
                path = path + name[name.length - 1];

                File file = new File(path);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baos.toByteArray());
                fos.close();
                //关闭网络连接
                connection.disconnect();
                LogCustom.i("ZYS", "下载完成");
                if (file.exists()) {
                    LogCustom.i("ZYS", "打开");
                    Uri path1 = Uri.fromFile(file);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path1, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        LogCustom.i("ZYS", "打开失败");
                    }
                }
//                mHandler.sendEmptyMessage(1);
            }
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    }

    /*private void closeLoading(){
        findViewById(R.id.pbar).setVisibility(View.GONE);
    }*/

    @Override
    protected void onDestroy() {
        try {
            if (webView != null) {
                webView.removeAllViews();
                webView.destroy();
            }
        } catch (Exception e) {

        }
        super.onDestroy();
    }

    @Override
    public void finish() {
        try {
            int form = getIntent().getIntExtra(Const.INTENT_FROM, 0);
            if (form == Const.FM) {
                afterFinish();
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "showDiscuz finish");
        }
        super.finish();
    }

    /**
     * 支付事件
     *
     * @param temp
     */
    @JavascriptInterface
    public void AppPay(String temp) {
        LogCustom.i("ZYS", "TEMP:" + temp);
        sendPayReq(temp);
    }

    /**
     * 提现事件
     *
     * @param temp
     */
    @JavascriptInterface
    public void AppCash(String temp) {
        sendTakeCash(temp);
    }

    /**
     * 微信公众平台商户模块和商户约定的密钥
     * <p>
     * 注意：不能hardcode在客户端，建议genPackage这个过程由服务器端完成
     */
    private String qid = "";

    private void sendPayReq(String temp) {
        initWX();
//        LogCustom.i("ZYS","param:"+temp);
        String[] args = temp.split("&");
        Map<String, String> map = new HashMap<>();
        for (String str : args) {
            String[] p = str.split("=");
            if (p.length == 2) map.put(p[0], p[1]);
        }
        qid = map.get("qid");
        String prepayid = map.get("prepayid");
        String sign = map.get("sign");
        String timeStamp = map.get("timestamp");
        String nonceStr = map.get("noncestr");
        PayReq req = new PayReq();
        req.appId = APP_ID;
        req.partnerId = PARTNER_ID;
        req.prepayId = prepayid;
        req.nonceStr = nonceStr;
        req.timeStamp = timeStamp;
        req.packageValue = "Sign=WXPay";// + packageValue;
        req.sign = sign;
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);
    }

    /**
     * 提现事件
     *
     * @param temp
     */
    private void sendTakeCash(String temp) {
        if (TextUtils.isEmpty(temp)) {
            Toast.makeText(this, "无法获取用户信息", Toast.LENGTH_SHORT).show();
        } else {
            if (!temp.equals(UserinfoData.getInfoID(this))) {
                Toast.makeText(this, "用户信息加载出错，无法提现", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, DrawCashActivity.class));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            //如果CodeFlag为真，则支付刚刚完成，需回调js方法完成交互
            if (ZYApplication.CodeFlag) {
                LogCustom.i("ZYS", "code:" + ZYApplication.CodePay + "qid:" + qid);
                FinishCode();
                ZYApplication.CodeFlag = false;
            }
            if (webView != null && !TextUtils.isEmpty(url) && url.contains("help.kaqcn.com/help/my_help")) {
                webView.loadUrl(url);
            }
        } catch (Exception e) {

        }
    }

    private void FinishCode() {
        webView.loadUrl("javascript:AppTag('" + ZYApplication.CodePay + "','" + qid + "')");
    }

    @JavascriptInterface
    public void showSource(String html, String headerUrl) {
        LogCustom.i("ZYS", "head:" + html);
        LogCustom.i("ZYS", "headerUrl:" + headerUrl);
        try {
            if (TextUtils.isEmpty(html)) {
                summary = "";
            } else {
                summary = html;
            }
            LogCustom.i("ZYS", "head:" + html);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "showSource");
        }

        try {
            if (TextUtils.isEmpty(headerUrl)) {
                shareImgUrl = null;
            } else {
                shareImgUrl = headerUrl;
            }
            LogCustom.i("ZYS", "headerUrl:" + headerUrl);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "showHeaderUrl");
        }
        LogCustom.i("ZYS", "goback url:" + url);

    }

    @JavascriptInterface
    public void appInfoCenter(String param) {
        if (!TextUtils.isEmpty(param))
            startActivity(new Intent(this, InfoCenterActivity.class).putExtra(Const.InfoCenterID, param));
    }

    @JavascriptInterface
    public void AppCreateRecord(String param) {
        UiUtils.startIndividuationTreatment(this);
    }

    @JavascriptInterface
    public void AppWatchRecord(String param) {
        startActivity(new Intent(this, MedicalRecordActivity.class));
    }

    @JavascriptInterface
    public void AppLiveID(String param) {
        if (!TextUtils.isEmpty(param)) {
            int flag = Integer.valueOf(param.substring(0, 1));
            if (flag == 0) {
                InKeSdkPluginAPI.start(this, ZYApplication.YK_UserInfo, false, param.substring(1, param.length()));
            } else {
                InKeSdkPluginAPI.start(this, ZYApplication.YK_UserInfo, true, param.substring(1, param.length()));
            }
        }
    }

    @JavascriptInterface
    public void AppJumpToYouZan(String param) {
        if (!TextUtils.isEmpty(param)) {
            startActivity(new Intent(ShowDiscuzActivity.this, YouzanActivity.class).putExtra("uzUrl", param));
        }
    }

    @Override
    public void dataCallBack(String str, int flag, String tag, Object obj) {
        if (flag == Const.FRAGMENT_INFO_STEP) {
            if (!TextUtils.isEmpty(str)) {
                webView.loadUrl("javascript:AppResultCode('" + str + "')");
            }
        }
    }

//    private InfoStepFragment stepFragment;
//
//    private void createInfoStepFragment() {
//        if (stepFragment == null) {
//            stepFragment = InfoStepFragment.getInstance(this, InfoStepFragment.FOR_RESULT);
//        }
//        stepFragment.show(getSupportFragmentManager(), InfoStepFragment.type);
//    }

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

    @Override
    public void forTitle(String ntitle) {
        title = ntitle;
        if (!"支付页面".equals(title)) {
//            initTitle(tv_title);
            v_title.setText(title);
        }
    }

    private TextView tv_title;

    private void initTitle(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (tv_title == null) {
                tv_title = (TextView) findViewById(R.id.tv_other_title);
            }
            tv_title.setText(str);
        }
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

    /**
     * 相册或拍照的系统回调
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
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

            } else if (requestCode == 9 && resultCode == RESULT_OK) {
                webView.reload();
            }

        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "mUploadMessageForAndroid5");
        }
    }


    private static final int VER = 1;
    private static final int VER5 = 5;

    /**
     * 调用系统相册或拍照
     *
     * @param uploadMsg
     */
    public void takePhoto(final ValueCallback<Uri> uploadMsg) {
        try {
            new AlertView("上传头像方式", null, "取消", null,
                    new String[]{"拍照", "从相册中选择"},
                    this, AlertView.Style.ActionSheet, new OnItemClickListener() {
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
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    public void takePhoto5(final ValueCallback<Uri[]> uploadMsg) {
        try {
            new AlertView("上传头像方式", null, "取消", null,
                    new String[]{"拍照", "从相册中选择"},
                    this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    switch (position) {
                        case 0:
                            fromTP5(uploadMsg);
                            break;
                        case 1:
//                            fromPic();
                            openFileChooserImplForAndroid5(uploadMsg);
                            break;
                    }
                }
            }).setCancelable(true).show();
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
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
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
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
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    /**
     * 使用相机拍照
     *
     * @version 1.0
     * @author zyh
     */
    /*protected void goCamare() {
        try {
            if (selectedPicture.size() + 1 > MAX_NUM) {
                Toast.makeText(context, "最多选择" + MAX_NUM + "张", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri imageUri = getOutputMediaFileUri();
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(openCameraIntent, TAKE_PICTURE);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "SelectPictureActivity");
        }
    }*/

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
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        cameraPath = mediaFile.getAbsolutePath();
        return Uri.fromFile(mediaFile);
    }

    /**
     * 从相册中获取图片
     */
    /*private void fromPic() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
            ComponentName componentName = intent.resolveActivity(getPackageManager());
            if (componentName != null) {
                startActivityForResult(intent, PHOTOZOOM);
            } else {
                Toast.makeText(PatientDetailActivity.this, "无法连接到相册", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }*/

    private String mJs = "var kaqsummary;" +
            "var kaqheadurl;" +
            "var kaqa = document.querySelector(\"meta[name='description']\");" +
            "var kaqb = document.querySelector(\"meta[name='headerurl']\");" +
            "if (kaqa==null) {kaqsummary=null;}else {kaqsummary = kaqa.content;}" +
            "if(kaqb==null){kaqheadurl=null;}else{kaqheadurl = kaqb.content;}" +
            "window.kaq.showSource(kaqsummary,kaqheadurl);";

}

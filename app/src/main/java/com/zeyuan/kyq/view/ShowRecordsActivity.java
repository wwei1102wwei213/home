package com.zeyuan.kyq.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.fragment.ShareFragment;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogCustom;

/**
 * Created by Administrator on 2016/7/14.
 * <p>
 * 电子病历装载页
 *
 * @author wwei
 */
public class ShowRecordsActivity extends BaseActivity {


    private WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_records);

        initOtherTitle("抗癌圈电子病历", true);

        initWebView();
        initShare();

    }

    private String shareURl;
    private ShareFragment fragment;

    private void initShare() {
        ImageView shareImg = (ImageView) findViewById(R.id.iv_other_title_share);
        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment == null) {
                    fragment = ShareFragment.getInstance(shareURl, getString(R.string.share_title_init), getString(R.string.share_content_init), null, Const.SHARE_FORUM);
                }
                if (fragment.getDialog() == null || !fragment.getDialog().isShowing())
                    fragment.show(getSupportFragmentManager(), ShareFragment.type);
            }
        });
    }

    private void initWebView() {
        wv = (WebView) findViewById(R.id.wv);

        String url = getIntent().getStringExtra(Const.RECORD_URL);

        if (TextUtils.isEmpty(url)) return;
        shareURl = url;
        WebSettings settings = wv.getSettings();

        settings.setJavaScriptEnabled(true);    //启用JS脚本
        settings.setSupportZoom(true);
        settings.setDomStorageEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setDomStorageEnabled(true);

        WebViewClient client = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogCustom.i("ZYS", "onPageStarted：" + "onPageStarted");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String nurl) {
                if (nurl.endsWith(".jpg") || nurl.endsWith(".JPG") || nurl.endsWith(".jpeg") || nurl.endsWith(".JPEG") || nurl.endsWith(".png") || nurl.endsWith(".PNG")) {
                    Log.i("ZYS", "URL是true：" + nurl);
                    startActivity(new Intent(ShowRecordsActivity.this, ShowImageActivity.class).putExtra(Contants.Avatar, nurl));
                    return true;
                } else {
                    if (nurl.startsWith("http://help.kaqcn.com/Api/getStepUser")) {
                        startActivity(new Intent(ShowRecordsActivity.this, ShowRecordsActivity.class).putExtra(Const.RECORD_URL, nurl));
                        return true;
                    } else {
                        /*url = nurl;
                        view.loadUrl(url);
                        return super.shouldOverrideUrlLoading(view, nurl);*/
                        view.loadUrl(nurl);  //加载新的url
                        return super.shouldOverrideUrlLoading(view, nurl);
                    }
                }

//                return false;
            }

            @Override
            public void onPageFinished(WebView view, String furl) {
                super.onPageFinished(view, furl);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        };
        wv.setWebViewClient(client);

        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        wv.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && wv.canGoBack()) {
                        //后退
                        wv.goBack();
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
        wv.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        if (wv != null) {
            wv.removeAllViews();
            wv.destroy();
        }
        super.onDestroy();
    }
}

package com.zeyuan.kyq.view;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.YouzanLoginBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.fragment.ShareFragment;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/26.
 * 有赞页面
 */

public class YouzanActivity extends BaseActivity implements HttpResponseInterface {
    private static final int CODE_REQUEST_LOGIN = 0x101;
    private ProgressBar pb_youzan;
    private YouzanBrowser bView;
    TextView tv_title;
    String mUrl = "https://h5.youzan.com/v2/feature/d4g89e5e";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youzan);
        tv_title = (TextView) findViewById(R.id.tv_white_title);
        ImageView iv_other_title_share = (ImageView) findViewById(R.id.iv_other_title_share);
        iv_other_title_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareView();
            }
        });
        pb_youzan = (ProgressBar) findViewById(R.id.pb_youzan);
        pb_youzan.setMax(100);
        bView = (YouzanBrowser) findViewById(R.id.yb_youzan);
        initWebView();
        initWhiteTitle("抗癌圈商城");
        setupYouzanView(bView);
        String uzUrl = getIntent().getStringExtra("uzUrl");

        if (!TextUtils.isEmpty(uzUrl)) {
            mUrl = uzUrl;
        }

        bView.loadUrl(mUrl);
        Factory.postPhp(YouzanActivity.this, Const.Api_YouzanLogin);
    }

    private void initWebView() {
        WebViewClient client = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                try {
                    pb_youzan.setProgress(0);
                    pb_youzan.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "initWebView");
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                shareURl = url;
                pb_youzan.setVisibility(View.GONE);
            }

        };
        WebChromeClient webChromeClient = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                pb_youzan.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (TextUtils.isEmpty(title)) {
                    shareTitle = "抗癌圈商城";
                } else {
                    shareTitle = title;
                }
                tv_title.setText(shareTitle);
            }


        };
        bView.setWebViewClient(client);
        bView.setWebChromeClient(webChromeClient);

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
                    Factory.postPhp(YouzanActivity.this, Const.Api_YouzanLogin);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            /**
             * 用户登录成功返回, 从自己的服务器上请求同步认证后组装成{@link com.youzan.sdk.YouzanToken},
             * 调用{code view.sync(token);}同步信息.
             */
            if (CODE_REQUEST_LOGIN == requestCode) {

            } else {
                //处理文件上传
                //mView.receiveFile(requestCode, data);
                bView.receiveFile(requestCode, data);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!bView.pageGoBack()) {
            super.onBackPressed();
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == Const.PApiServeCenterlist) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(getApplicationContext()));
            map.put("type", "1");

        } else if (tag == Const.Api_YouzanLogin) {
            map.put("client_id", ZYApplication.Youzan_client_id);
            map.put("client_secret", ZYApplication.Youzan_client_secret);
            map.put("open_user_id", UserinfoData.getInfoID(getApplicationContext()));
            map.put("nick_name", UserinfoData.getInfoname(getApplicationContext()));
            map.put("telephone", UserinfoData.getPhoneNum(getApplicationContext()));
            map.put("avatar", UserinfoData.getAvatarUrl(getApplicationContext()));
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
                    //mView.loadUrl(list.get(0).getUrl());
                    bView.loadUrl(list.get(0).getUrl());
                    Factory.postPhp(YouzanActivity.this, Const.Api_YouzanLogin);
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
                bView.sync(youzanToken);
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

    }

    private String shareURl;
    private String shareTitle = "分享了一个商品给你!";
    private String shareIcon = null;

    //分享
    private void showShareView() {
        ShareFragment fragment = ShareFragment.getInstance(shareURl, shareTitle, getString(R.string.share_content), shareIcon, 0);
        if (fragment.getDialog() == null || !fragment.getDialog().isShowing())
            fragment.show(getSupportFragmentManager(), ShareFragment.type);
    }
    @Override
    protected void onResume() {
        super.onResume();
        bView.onResume();
        bView.resumeTimers();
    }
}

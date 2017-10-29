package com.zeyuan.kyq.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.meelive.ingkee.sdk.plugin.InKeSdkPluginAPI;
import com.zeyuan.kyq.Entity.ArticleInfoEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.KeyboardChangeListener;
import com.zeyuan.kyq.biz.manager.FunctionGuideManager;
import com.zeyuan.kyq.biz.manager.IntegrationManager;
import com.zeyuan.kyq.fragment.ShareFragment;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MyWebChromeClient;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CircleImageView;
import com.zeyuan.kyq.widget.IntegrationPopupWindow;
import com.zeyuan.kyq.widget.MyLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/11.
 * <p>
 * 文章详情页面
 *
 * @author wwei
 */
public class ArticleDetailActivity extends BaseActivity implements ViewDataListener,
        View.OnClickListener, HttpResponseInterface, MyWebChromeClient.WebCall, KeyboardChangeListener.KeyBoardListener {
    //, MyLayout.OnSoftKeyboardListener
    private static final String TAG = "ArticleDetailActivity";

    //文章id
    private String aid = "";
    //文章url
    private String url = "";
    //分享窗口
    private ShareFragment fragment;
    //分享图标
    private ImageView shareImg;
    //分享相关参数
    private String shareURl;
    private String shareTitle = "";
    private String shareContent = "";
    private String shareIcon = null;
    //顶部文章作者图片
    private CircleImageView civ;
    //文章作者昵称
    private TextView name;
    private TextView tv_fav;
    private TextView tv_like;
    private TextView tv_pl;
    private View v_menu;
    //编辑框
    private EditText et;
    private View v_et;
    private View v_edit;
    private View v_layout_bottom;
    //    private View v_clear;
    private View v_cancle;
    //外部控件
    private MyLayout layout;
    //发送按钮
    private TextView tv_send;
    //加载网页控件
    private WebView webView_article_content;
    //页面加载进度条
    private ProgressBar sb;
    //兼容html中的file标签
    public final static int FILECHOOSER_RESULTCODE = 1;
    public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;
    public ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;

    private int CommentNum = 0;
    private int LikeNum = 0;

    private View v_bottom;

    private TextView tv_like_num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_article_detail);

        try {
            //初始化文章id和url
            aid = getIntent().getStringExtra(Const.INTENT_ARTICLE_ID);
            if (TextUtils.isEmpty(aid)) {
                throw new RuntimeException("ArticleDetailActivity error , aid is empty!");
            }
            //保存文章阅读记录
            saveReadState();
            //文章url
            url = "http://help.kaqcn.com/Api/ArticleViewInfo?" + "aid=" + aid + "&" + getParamKaqID() + "&Type=2";
            //初始化UI控件
            initView();
            //初始化数据
            initData();
            //初始化WebView
            initWebView();

        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "OnCreate");
        }

    }


    //初始化数据
    private void initData() {
        Factory.postPhp(this, Const.PArticleOtherInfo);
    }

    private void saveReadState() {
        try {
            int id = Integer.valueOf(aid);
            UserinfoData.getRecordArticleArray().put(id, true);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "文章阅读记录ID转数字出错");
        }
    }

    private View v_author;
    private String author = "";

    //初始化控件
    private void initView() {

        v_et = findViewById(R.id.tv_edit_txt);
        v_et.setOnClickListener(this);

        v_layout_bottom = findViewById(R.id.layout_bottom);
        v_layout_bottom.setVisibility(View.VISIBLE);

        v_edit = findViewById(R.id.v_edit);
        v_cancle = findViewById(R.id.btn_no_dialog);
        v_cancle.setOnClickListener(this);


        v_menu = findViewById(R.id.v_menu);
        //进度条
        sb = (ProgressBar) findViewById(R.id.pb_art);
        sb.setMax(100);
        //头像和昵称
        civ = (CircleImageView) findViewById(R.id.civ);
        name = (TextView) findViewById(R.id.name);
        findViewById(R.id.iv_back).setOnClickListener(this);


        //底部控件
        v_bottom = findViewById(R.id.v_bottom);
        tv_pl = (TextView) findViewById(R.id.tv_pl);
        tv_fav = (TextView) findViewById(R.id.tv_fav);
        tv_like = (TextView) findViewById(R.id.tv_like);
        tv_like_num = (TextView) findViewById(R.id.tv_like_num);
        if (ZYApplication.typeFace != null) {
            tv_pl.setTypeface(ZYApplication.typeFace);
            tv_like_num.setTypeface(ZYApplication.typeFace);
        }
        tv_pl.setOnClickListener(this);
        tv_fav.setOnClickListener(this);
        tv_like.setOnClickListener(this);
//        findViewById(R.id.v_bottom).setOnClickListener(this);
        tv_send = (TextView) findViewById(R.id.send_message);
        tv_send.setOnClickListener(this);
        layout = (MyLayout) findViewById(R.id.my_layout);
        //  layout.setOnSoftKeyboardListener(this);
        et = (EditText) findViewById(R.id.edit_txt);
        //键盘开关监听器
        KeyboardChangeListener keyboardChangeListener = new KeyboardChangeListener(this);
        keyboardChangeListener.setKeyBoardListener(this);
        /*String temp = UserinfoData.getDtaftArticleArray().get(Integer.valueOf(aid));
        if (!TextUtils.isEmpty(temp)){
            et.setText(temp);
            et.setSelection(temp.length());
        }
*/
        //分享图标
        shareImg = (ImageView) findViewById(R.id.iv_share);
        shareImg.setClickable(false);
        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (fragment == null) {
                        fragment = ShareFragment.getInstance(url, shareTitle, shareContent, shareIcon, Const.SHARE_ARTICLE);
                    }
                    if (fragment.getDialog() == null || !fragment.getDialog().isShowing())
                        fragment.show(getSupportFragmentManager(), ShareFragment.type);
                } catch (Exception e) {
                    ExceptionUtils.ExceptionToUM(e, ArticleDetailActivity.this, "Share");
                }
            }
        });
        //文章作者点击事件
        v_author = findViewById(R.id.v_author);
        v_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(author))
                    startActivity(new Intent(ArticleDetailActivity.this, InfoCenterActivity.class)
                            .putExtra(Const.InfoCenterID, author));
            }
        });
        findViewById(R.id.v_half).setOnClickListener(this);
    }

    android.os.Handler handler = new android.os.Handler();

    //初始化WebView
    private void initWebView() {
        webView_article_content = (WebView) findViewById(R.id.wv);
        final WebSettings settings = webView_article_content.getSettings();

        settings.setJavaScriptEnabled(true);    //启用JS脚本
        webView_article_content.addJavascriptInterface(this, "kaq");
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        settings.setAllowFileAccess(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setDisplayZoomControls(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        //5.0以上系统必须加，不然无法播放视频
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        MyWebChromeClient wcc = new MyWebChromeClient();
        wcc.setWebCall(this);

        WebViewClient client = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                try {
                    sb.setProgress(0);
                    sb.setVisibility(View.VISIBLE);
                } catch (Exception e) {

                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String nurl) {
                if (nurl.endsWith(".pdf")) {
                    Intent intent = new Intent();
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(nurl), "application/pdf");
                    startActivity(intent);
                    return true;
                } else if (nurl.endsWith(".jpg") || nurl.endsWith(".JPG") || nurl.endsWith(".jpeg") ||
                        nurl.endsWith(".JPEG") || nurl.endsWith(".png") || nurl.endsWith(".PNG")) {
                    LogCustom.i("ZYS", "图片URL是：" + nurl);
                    startActivity(new Intent(ArticleDetailActivity.this, ShowImageActivity.class).
                            putExtra(Contants.Avatar, nurl));
                    return true;
                }
                return true;
//                return super.shouldOverrideUrlLoading(view, nurl);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                try {
                    sb.setVisibility(View.GONE);
                    shareImg.setClickable(true);
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "onPageFinished");
                }
                try {
                    view.loadUrl("javascript:" + Const.ZY_JS_ARTICLE);
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "view.loadUrl");
                }



            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        };

        webView_article_content.setWebChromeClient(wcc);
        webView_article_content.setWebViewClient(client);

        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        webView_article_content.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView_article_content.canGoBack()) {
                        //后退
                        webView_article_content.goBack();
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
        LogCustom.i("ZYS", "加载文章Url：" + url);
        webView_article_content.loadUrl(url);
    }

    @JavascriptInterface
    public void showSource(String html, String headerUrl) {
        try {
            if (TextUtils.isEmpty(html)) {
                shareContent = "";
            } else {
                shareContent = html;
            }
            LogCustom.i("ZYS", "head:" + html);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "showSource");
        }

        try {
            if (TextUtils.isEmpty(headerUrl)) {
                shareIcon = null;
            } else {
                shareIcon = headerUrl;
            }
            LogCustom.i("ZYS", "shareIcon:" + headerUrl);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "showHeaderUrl");
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == Const.PArticleOtherInfo) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
            map.put("aid", aid);
        } else if (tag == Const.PGiveLike) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
            map.put("TypeID", "1");
            map.put("ContentID", aid);
        } else if (tag == Const.PGetForumList_bank) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        } else if (tag == Const.PAddPorComment) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
            map.put(Contants.InfoName, UserinfoData.getInfoname(this));
            map.put("ArtID", aid);
            map.put("MessAge", content);
        }
        return map;
    }

    String isFlw = "0";

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if (flag == Const.EFavorForum) {
            if (tv_fav.isSelected()) {
                isFlw = "1";
            } else {
                isFlw = "0";
            }
            args = new String[]{
                    Contants.InfoID, Const.InfoID,
                    Contants.index, aid,
                    Contants.strTitle, shareTitle,
                    Contants.isCancel, isFlw,
                    "is_art", "1"
            };
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object t, int tag) {
        if (tag == Const.PArticleOtherInfo) {
            ArticleInfoEntity entity = (ArticleInfoEntity) t;
            if (Const.RESULT.equals(entity.getiResult())) {
                name.setText(TextUtils.isEmpty(entity.getArtInfoName()) ? "" : entity.getArtInfoName());
                try {
                    if (!TextUtils.isEmpty(entity.getArtInfoHeadUrl())) {
                        Glide.with(this).load(entity.getArtInfoHeadUrl())
                                .signature(new IntegerVersionSignature(1))
                                .into(civ);
                    }
                } catch (Exception e) {
                    ExceptionUtils.ExceptionToUM(e, this, "ArticleDetailActivity toActivity civ");
                }

                if (entity.getIsFav() == 1) {
                    tv_fav.setSelected(true);
                } else {
                    tv_fav.setSelected(false);
                }
                if (entity.getIsLike() == 1) {
                    tv_like.setSelected(true);
                } else {
                    tv_like.setSelected(false);
                }
                author = entity.getArtInfoID();
                CommentNum = entity.getArtCommentNum();
                LikeNum = entity.getLikeNum();
                tv_pl.setText(CommentNum + "");
                tv_like_num.setText(LikeNum + "");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!ArticleDetailActivity.this.isFinishing()) {
                            int[] xy = new int[2];
                            v_author.getLocationInWindow(xy);
                            int width = v_author.getWidth();
                            int height = v_author.getHeight();
                            FunctionGuideManager.getInstance().showArticleAuthorGuide(ArticleDetailActivity.this, xy, width, height);
                        }
                    }
                }, 400);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!ArticleDetailActivity.this.isFinishing()) {
                            int[] xy = new int[2];
                            tv_like.getLocationInWindow(xy);
                            int width = tv_like.getWidth();
                            int height = tv_like.getHeight();
                            FunctionGuideManager.getInstance().showArticleLikeGuide(ArticleDetailActivity.this, xy, width, height);
                        }

                    }
                }, 500);
            } else {
                Toast.makeText(this, "文章作者信息加载失败", Toast.LENGTH_SHORT).show();
            }
        } else if (tag == Const.EFavorForum) {//这个是收藏帖子的数据
            LogUtil.i(TAG, t.toString());
            BaseBean bean = (BaseBean) t;
            if (bean.iResult.equals(Contants.RESULT)) {
                if ("1".equals(isFlw)) {
                    showToast("取消收藏成功");
                    tv_fav.setSelected(false);
                } else {
                    showToast(getString(R.string.flw_success));
                    tv_fav.setSelected(true);
                }

            } else {
                if ("1".equals(isFlw)) {
                    showToast("取消收藏失败");
                } else {
                    showToast("收藏失败");
                }
            }
            tv_fav.setClickable(true);
        } else if (tag == Const.PGiveLike) {
            PhpUserInfoBean bean = (PhpUserInfoBean) t;
            if (Const.RESULT.equals(bean.getiResult())) {
                showToast("点赞成功");
                tv_like.setSelected(true);
                initData();
            } else {
                showToast("点赞失败");
            }
            tv_like.setClickable(true);
        } else if (tag == Const.PAddPorComment) {
            PhpUserInfoBean bean = (PhpUserInfoBean) t;
            if (Const.RESULT.equals(bean.getiResult())) {
                //清空输入框
                content = "";
                et.setText("");
                //清空草稿数据
                try {
                    String temp = UserinfoData.getDtaftArticleArray().get(Integer.valueOf(aid));
                    if (!TextUtils.isEmpty(temp))
                        UserinfoData.getDtaftArticleArray().delete(Integer.valueOf(aid));
                } catch (Exception e) {

                }
                showToast("回复成功");//收起输入法 刷新帖子
                try {
                    hideKeyboard(getCurrentFocus().getWindowToken());
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "回复成功");
                }
                //刷新页面以及数据
                webView_article_content.loadUrl(url);
                initData();

                String jfString;
                if (!TextUtils.isEmpty(bean.getJf()) && !bean.getJf().equals("0")) {
                    jfString = "评论成功 +" + bean.getJf() + "积分";
                    IntegrationManager.getInstance().addIntegration(bean.getJf());
                } else {
                    jfString = "评论成功 已达每日积分上限";
                }
                IntegrationPopupWindow integrationPopupWindow = new IntegrationPopupWindow(ArticleDetailActivity.this, jfString);
                integrationPopupWindow.showPopupWindow(ArticleDetailActivity.this);

            }
            tv_send.setClickable(true);
        }
    }

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {

    }

    @Override
    public void showError(int tag) {
        if (tag == Const.PAddPorComment) {
            tv_send.setClickable(true);
        } else if (tag == Const.PGiveLike) {
            tv_like.setClickable(true);
        } else if (tag == Const.EFavorForum) {
            tv_fav.setClickable(true);
        }
        showToast("网络请求失败");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_author:
                if (!TextUtils.isEmpty(author))
                    startActivity(new Intent(this, InfoCenterActivity.class)
                            .putExtra(Const.InfoCenterID, author));
                break;
            case R.id.iv_back:
//                toFinish();
                finish();
                break;
            case R.id.tv_fav:
                if (!TextUtils.isEmpty(aid)) {
                    tv_fav.setClickable(false);
                    Factory.post(this, Const.EFavorForum);
                }
                break;
            case R.id.tv_like:
                if (tv_like.isSelected()) {
                    showToast("您已经赞过了");
                    return;
                }
                if (!TextUtils.isEmpty(aid)) {
                    tv_like.setClickable(false);
                    Factory.postPhp(this, Const.PGiveLike);
                }
                break;
            case R.id.tv_pl:
                if (CommentNum == 0) {
                    showToast("该文章还没有评论");
                    return;
                }
                if (!TextUtils.isEmpty(aid)) {
                    startActivity(new Intent(this, ArticleCommentActivity.class)
                            .putExtra(Const.INTENT_ARTICLE_ID, aid)
                            .putExtra(Const.INTENT_AUTHOR_ID, author));
                }
                break;
            case R.id.send_message:
                try {
                    content = et.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        showToast(getString(R.string.empty_reply));
                        return;
                    }
                    tv_send.setClickable(false);
                    Factory.postPhp(this, Const.PAddPorComment);
                } catch (Exception e) {
                    ExceptionUtils.ExceptionToUM(e, this, "ForumDetailActivity");
                }
                break;
            case R.id.tv_edit_txt:
                showSoftInput();
                break;
            case R.id.v_half:
            case R.id.btn_no_dialog:
                try {
                    hideKeyboard(getCurrentFocus().getWindowToken());
                    //  onHidden();
                } catch (Exception e) {

                }
                break;
            /*case R.id.btn_clear_edit:
                content = "";
                et.setText("");
                break;*/
        }
    }

    @Override
    public void fileChose(ValueCallback<Uri> uploadMsg) {
        takePhoto(uploadMsg);
    }

    @Override
    public void fileChose5(ValueCallback<Uri[]> uploadMsg) {
        takePhoto5(uploadMsg);
    }

    @Override
    public void forTitle(String title) {
        shareTitle = title;
    }

    @Override
    public void forProgress(int pos) {
        if (pos <= 100) {
            sb.setProgress(pos);
        }
    }

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
                            openFileChooserImplForAndroid5(uploadMsg);
                            break;
                    }
                }
            }).setCancelable(true).show();
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ArticleDetailActivity");
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
            ExceptionUtils.ExceptionToUM(e, this, "ArticleDetailActivity");
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
            ExceptionUtils.ExceptionToUM(e, this, "ArticleDetailActivity");
        }
    }

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
                LogCustom.i("ZYS", "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        cameraPath = mediaFile.getAbsolutePath();
        return Uri.fromFile(mediaFile);
    }


    //  private boolean FauosFlag = false;

//    @Override
//    public void onShown() {
//        try {
//            FauosFlag = true;
//            flag = true;
//            findViewById(R.id.v_half).setVisibility(View.VISIBLE);
//            if (!TextUtils.isEmpty(content)) {
//                et.setText(content);
//                et.setSelection(content.length());
//            } else {
//                String temp = UserinfoData.getDtaftArticleArray().get(Integer.valueOf(aid));
//                LogCustom.i("ZYS", "temp:" + temp);
//                if (!TextUtils.isEmpty(temp)) {
//                    et.setText(temp);
//                    et.setSelection(temp.length());
//                }
//            }
//        } catch (Exception e) {
//            ExceptionUtils.ExceptionToUM(e, this, "ForumDetailActivity");
//        }
//    }
//
//    @Override
//    public void onHidden() {
//        if (flag) {
//            hideInput();
//        }
//    }

    private void showSoftInput() {
        try {
            v_layout_bottom.setVisibility(View.GONE);
            v_edit.setVisibility(View.VISIBLE);
            et.setFocusableInTouchMode(true);
            et.requestFocus();
            InputMethodManager inputManager =
                    (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(et, 0);
            Animation a = AnimationUtils.loadAnimation(this, R.anim.dialog_enter_anim);
            v_edit.startAnimation(a);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ForumDetailActivity");
        }
    }

    private void hideInput() {
        try {
            findViewById(R.id.v_half).setVisibility(View.GONE);
            content = et.getText().toString();
            if (!TextUtils.isEmpty(content)) {
                UserinfoData.getDtaftArticleArray().put(Integer.valueOf(aid), content.trim());
            }
            flag = false;
            clearReplyData();
            v_edit.setVisibility(View.GONE);
            v_layout_bottom.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ForumDetailActivity");
        }
    }

    private String content = "";

    private void hideKeyboard(IBinder token) {
        try {
            if (token != null) {
                content = et.getText().toString();
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ForumDetailActivity");
        }
    }

    private boolean first = true;

    /**
     * 隐藏输入法后 清空 回复某人的数据
     */
    private void clearReplyData() {
        try {

            et.setText("");


//            touser = author;
//            toinfoid = ownerID;
            et.setHint("我想说");
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ForumDetailActivity");
        }
    }

    @JavascriptInterface
    public void appJumpAdvert(String param) {
        LogCustom.i("ZYS", "appArticleDetail被调用，参数是：" + param);
        String[] params = param.split("&");
        if (params.length > 0) {
            Map<String, String> map = new HashMap<>();
            for (String str : params) {
                String[] args = str.split("=");
                if (args.length == 2) {
                    map.put(args[0], args[1]);
                }
            }
            toJumpOther(map);
        }
    }

    @JavascriptInterface
    public void appArticleDetail(String param) {
        if (!TextUtils.isEmpty(param)) {
            startActivity(new Intent(this, ArticleDetailActivity.class)
                    .putExtra(Const.INTENT_ARTICLE_ID, param));
        }
    }

    @JavascriptInterface
    public void appArticleList() {
        startActivity(new Intent(this, ArticleCommentActivity.class).
                putExtra(Const.INTENT_ARTICLE_ID, aid).putExtra(Const.INTENT_AUTHOR_ID, author));
    }

    @JavascriptInterface
    public void appInfoCenter(String param) {
        if (!TextUtils.isEmpty(param))
            startActivity(new Intent(this, InfoCenterActivity.class).putExtra(Const.InfoCenterID, param));
    }

    @JavascriptInterface
    public void appShowReply(String param) {
        if (TextUtils.isEmpty(param)) return;
        if ("0".equals(param)) {
            v_bottom.setVisibility(View.GONE);
        } else {
            v_bottom.setVisibility(View.VISIBLE);
        }
    }

    @JavascriptInterface
    public void AppLiveID(String param) {
        LogCustom.i("ZYS", "param:" + param);
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
            startActivity(new Intent(ArticleDetailActivity.this, YouzanActivity.class).putExtra("uzUrl", param));
        }
    }

    private void toJumpOther(Map<String, String> map) {
        String tempUrl = map.get("tagurl");
        if (!TextUtils.isEmpty(map.get("tagtype"))) {
            final int type = Integer.valueOf(map.get("tagtype"));
            switch (type) {
                case 1:
                    if (!TextUtils.isEmpty(map.get("id"))) {

                        if (!TextUtils.isEmpty(tempUrl)) {
                            if (tempUrl.contains("?")) {
                                tempUrl += "&kaq=" + getRandomMath() + UserinfoData.getInfoID(this) + "&lt=2";
                            } else {
                                tempUrl += "?kaq=" + getRandomMath() + UserinfoData.getInfoID(this) + "&lt=2";
                            }
                            startActivity(new Intent(this, ShowDiscuzActivity.class).
                                    putExtra(Const.SHOW_HTML_MAIN_TOP, tempUrl));
                        }

                    }
                    break;
                case 2:
                    if (!TextUtils.isEmpty(tempUrl)) {
                        startActivity(new Intent(this, ArticleDetailActivity.class).
                                putExtra(Const.INTENT_ARTICLE_ID, tempUrl));
                    }
                    break;
                case 3:
                    if (!TextUtils.isEmpty(tempUrl)) {
                        startActivity(new Intent(this, ForumDetailActivity.class)
                                .putExtra(Const.FORUM_ID, tempUrl));
                    }
                    break;
                case 4:
                    Intent intent = new Intent(this, HeadLineActivity.class);
                    if (!TextUtils.isEmpty(map.get("infotext")) && !TextUtils.isEmpty(tempUrl)) {
                        intent.putExtra(Const.HEAD_LIST_TAG_URL, tempUrl)
                                .putExtra(Const.HEAD_LIST_INFO_TEXT, map.get("infotext"));
                    }
                    startActivity(intent);
                    break;
                case 5:
                    startActivity(new Intent(this, NewCircleActivity.class).putExtra(Contants.CircleID, tempUrl));
                    break;
            }
        }
    }

    private String getRandomMath() {
        String temp = (int) (Math.random() * 89999 + 10000) + "";
        return temp;
    }


    /* private void toFinish(){
         try {
             if (!TextUtils.isEmpty(content)){
                 UserinfoData.getDtaftArticleArray().put(Integer.valueOf(aid),content);
             }else {
                 finish();
             }
         }catch (Exception e){
             ExceptionUtils.ExceptionSend(e,"onBackPressed");
             finish();
         }
     }*/
    @Override
    protected void onPause() {
        super.onPause();
//暂停WebView在后台的所有活动
        webView_article_content.onPause();
//暂停WebView在后台的JS活动
        webView_article_content.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView_article_content.onResume();
        webView_article_content.resumeTimers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView_article_content != null) {
            //clearWebViewCache();
            webView_article_content.destroy();
            webView_article_content = null;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    private boolean flag = false;// 这个控制隐藏键盘的时候 回调只被调用一次

    @Override
    public void onKeyboardChange(boolean isShow, int keyboardHeight) {
        if (isShow) {
            try {
                flag = true;
                findViewById(R.id.v_half).setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(content)) {
                    et.setText(content);
                    et.setSelection(content.length());
                } else {
                    String temp = UserinfoData.getDtaftArticleArray().get(Integer.valueOf(aid));
                    LogCustom.i("ZYS", "temp:" + temp);
                    if (!TextUtils.isEmpty(temp)) {
                        et.setText(temp);
                        et.setSelection(temp.length());
                    }
                }
            } catch (Exception e) {
                ExceptionUtils.ExceptionToUM(e, this, "ForumDetailActivity");
            }
        } else {
            if (flag) {
                hideInput();
            }
        }
    }
}


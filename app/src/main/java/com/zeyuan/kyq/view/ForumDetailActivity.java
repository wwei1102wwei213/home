package com.zeyuan.kyq.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.ForumInfoEntity;
import com.zeyuan.kyq.Entity.RemindUserEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.ReplyForumItemAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.bean.PostForumBean;
import com.zeyuan.kyq.bean.ReplyForum;
import com.zeyuan.kyq.bean.ReplyListBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.KeyboardChangeListener;
import com.zeyuan.kyq.biz.manager.IntegrationManager;
import com.zeyuan.kyq.fragment.ShareFragment;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ConstUtils;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DecryptUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.OtherUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CircleImageView;
import com.zeyuan.kyq.widget.CustomView.CustomLoadingListView;
import com.zeyuan.kyq.widget.CustomView.CustomRefreshListView;
import com.zeyuan.kyq.widget.CustomView.LoadingScrollView;
import com.zeyuan.kyq.widget.CustomView.OnCustomRefreshListener;
import com.zeyuan.kyq.widget.FlowLayout;
import com.zeyuan.kyq.widget.ForumSharePopupWindow;
import com.zeyuan.kyq.widget.IntegrationPopupWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zeyuan.kyq.utils.Const.InfoCenterID;

/**
 * 帖子详情
 */
public class ForumDetailActivity extends BaseActivity implements View.OnClickListener, ViewDataListener,
        AdapterView.OnItemClickListener, ReplyForumItemAdapter.ShowInput
        , HttpResponseInterface, LoadingScrollView.LoadingScrollViewListstener, OnCustomRefreshListener, KeyboardChangeListener.KeyBoardListener {

    public static final String TAG = "ForumDetailActivity";

    private CustomLoadingListView mListView_listview;//回复的listview
    private ReplyForumItemAdapter adapter;//回复列表的itemadapter
    private List<ReplyListBean.ReplyNumEntity> replyListData;//回复列表的list
    private List<ReplyListBean.ReplyNumEntity> data;
    //private MyLayout myLayout;//用来监听软键盘的弹出 和收起
    private String shareURl;
    private String url;
    private String shareTitle = "";
    private String shareContent = "";
    private ShareFragment fragment;
    private String mOwnerID;

    private View ll_author_info;
    private CircleImageView civ_head_img;
    private ImageView v_big_v;
    private TextView tv_author_name;
    private TextView tv_author_info;
    private TextView cb_care;
    private LinearLayout ll_author_forum;
    private LinearLayout ll_forum_list;

    private LinearLayout ll_repay_list;
    private LoadingScrollView mScrollView;
    private TextView huifu_num;
    private ImageView shareImg;
    //帖子内容的webview
    private WebView wv_forum_content;
    private int page = 0;

    private EditText et;
    private View v_et;
    private View v_edit;
    private View v_layout_bottom;
    private View v_cancel;
    private TextView tv_fav;
    private TextView tv_pl;
    private View v_send;
    private TextView tv_edit_title;
    private boolean isMyForum;//是否为自己的帖子
    private ForumSharePopupWindow forumSharePopupWindow;
    private static final int REQUEST_CODE_EDIT_FORUM = 55;

    //这篇帖子@的好友
    private View ll_reminded_friends_view;
    private TextView tv_remind_tag;
    private LinearLayout ll_reminded_friends;
    //键盘开关监听器
    private KeyboardChangeListener keyboardChangeListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_detail);
        try {
            initOtherTitle(getString(R.string.forum_detail), true);
            forum_id = getIntent().getStringExtra(Const.FORUM_ID);

            if (TextUtils.isEmpty(forum_id)) {
                throw new RuntimeException("ForumDetailActivity error entrance!");
            }
            if (TextUtils.isEmpty(Const.SHOW_DISCUZ_FORUM)) {
                String temp = Factory.getData(Const.N_UrlForumIndex).toString();
            }

            LogCustom.i(Const.TAG.ZY_DATA, Const.SHOW_DISCUZ_FORUM);
            shareURl = Const.SHOW_DISCUZ_FORUM + forum_id;
            LogCustom.i(Const.TAG.ZY_DATA, "shareURl:" + shareURl);
//            url = shareURl;
            url = shareURl.replace("mobile=2", "mobile=1");
            LogCustom.i(Const.TAG.ZY_DATA, "url:" + url);
            replyListData = new ArrayList<>();
            initNormalView();
            setListener();
            initData();
            //  showJF();
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ForumDetailActivity");
        }
    }

    boolean isFtJfShowed = false;

    //如果是发帖成功后跳转过来的，需要显示积分提示
    private void showJF() {
        if (!isFtJfShowed && !TextUtils.isEmpty(getIntent().getStringExtra(Const.FROM_FT))) {
            String jfString;
            if (!TextUtils.isEmpty(getIntent().getStringExtra(Const.JF)) && !getIntent().getStringExtra(Const.JF).equals("0")) {
                jfString = "发帖成功 +" + getIntent().getStringExtra(Const.JF) + "积分";
            } else {
                jfString = "发帖成功 已达每日积分上限";
            }
            IntegrationPopupWindow integrationPopupWindow = new IntegrationPopupWindow(this, jfString);
            integrationPopupWindow.showPopupWindow(this);
            isFtJfShowed = true;
        }

    }

    private FlowLayout fl;
    private View v_from;


    private void initNormalView() {

        fl = (FlowLayout) findViewById(R.id.fl_from_circle);
        v_from = findViewById(R.id.v_from_circle);
        ll_author_info = findViewById(R.id.ll_author_info);
        huifu_num = (TextView) findViewById(R.id.huifu_num);
        // myLayout = (MyLayout) findViewById(R.id.whole_content);
        mScrollView = (LoadingScrollView) findViewById(R.id.sv);
        mScrollView.setmListener(this);
        ll_repay_list = (LinearLayout) findViewById(R.id.ll_repay_list);

        ll_reminded_friends_view = findViewById(R.id.ll_reminded_friends_view);
        tv_remind_tag = (TextView) findViewById(R.id.tv_remind_tag);
        ll_reminded_friends = (LinearLayout) findViewById(R.id.ll_reminded_friends);


        civ_head_img = (CircleImageView) findViewById(R.id.civ_head_img);
        v_big_v = (ImageView) findViewById(R.id.v_big_v);
        tv_author_name = (TextView) findViewById(R.id.tv_author_name);
        tv_author_info = (TextView) findViewById(R.id.tv_author_info);
        cb_care = (TextView) findViewById(R.id.cb_care);
        ll_author_forum = (LinearLayout) findViewById(R.id.ll_author_forum);
        ll_forum_list = (LinearLayout) findViewById(R.id.ll_forum_list);


        data = new ArrayList<>();
        mListView_listview = (CustomLoadingListView) findViewById(R.id.listview);
        adapter = new ReplyForumItemAdapter(this, data);
        adapter.setShowInput(this);
        mListView_listview.setAdapter(adapter);
        mListView_listview.setOnRefreshListener(this);

        shareImg = (ImageView) findViewById(R.id.iv_other_title_share);
        shareImg.setOnClickListener(this);

        et = (EditText) findViewById(R.id.edit_txt);

        tv_fav = (TextView) findViewById(R.id.tv_fav);
        tv_pl = (TextView) findViewById(R.id.tv_pl);
        findViewById(R.id.v_like).setVisibility(View.GONE);
        v_et = findViewById(R.id.tv_edit_txt);
        v_et.setOnClickListener(this);
        v_layout_bottom = findViewById(R.id.layout_bottom);

        v_edit = findViewById(R.id.v_edit);
        v_cancel = findViewById(R.id.btn_no_dialog);
        v_cancel.setOnClickListener(this);
        v_send = findViewById(R.id.send_message);
        tv_edit_title = (TextView) findViewById(R.id.tv_edit_title);

        wv_forum_content = (WebView) findViewById(R.id.wv_forum_detail);

        keyboardChangeListener = new KeyboardChangeListener(this);


    }

    /***
     * 初始化数据
     */
    private void initData() {
        findViewById(R.id.pd).setVisibility(View.VISIBLE);
        String authorId = getIntent().getStringExtra(Const.AUTHORID);
        if (!TextUtils.isEmpty(authorId) && authorId.equals(UserinfoData.getInfoID(this))) {
            isMyForum = true;
            shareImg.setImageResource(R.mipmap.ic_share_more);
        } else {
            isMyForum = false;
            shareImg.setImageResource(R.mipmap.share_step_def);
        }
        initWebView();
    }

    /***
     * 设置顶部圈子视图
     * @param
     */
    private void initCircleView(List<String> data) {
        if (data != null && data.size() > 0) {
            v_from.setVisibility(View.VISIBLE);
            Map<String, String> circles = (Map<String, String>) Factory.getData(Const.N_DataCircleValues);
            fl.removeAllViews();
            for (int i = 0; i < data.size(); i++) {
                final String id = data.get(i);
                if (!TextUtils.isEmpty(id)) {
                    TextView tv = (TextView) View.inflate(this, R.layout.tv_forum_for_circle, null);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toCirclePage(id);
                        }
                    });
                    String name = circles.get(id);
                    if (!TextUtils.isEmpty(name)) {
                        tv.setText("" + name + ">");
                    } else {
                        tv.setText("");
                    }
                    fl.addView(tv);
                }
            }
        } else {
            v_from.setVisibility(View.GONE);
        }

        //v_share.setVisibility(View.VISIBLE);
        // initShareWX();
    }


    //设置作者信息
    private void setAuthorInfo(ReplyListBean bean) {
        try {
            Glide.with(this).load(bean.getHeadUrl())
                    .signature(new IntegerVersionSignature(1))
                    .error(R.mipmap.default_avatar)//这个是加载失败的
                    .into(civ_head_img);
            if (bean.getV().equals("1")) {
                v_big_v.setVisibility(View.VISIBLE);
            } else {
                v_big_v.setVisibility(View.GONE);
            }
            tv_author_name.setText(bean.getInfoName());
            tv_author_info.setVisibility(View.GONE);
            if (bean.getCare() == 1) {
                cb_care.setSelected(true);
                cb_care.setText("已关注");
            } else {
                cb_care.setSelected(false);
                cb_care.setText("+ 关注");
            }
            cb_care.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cb_care.isSelected()) {
                        cancelFollow();
                    } else {
                        Factory.postPhp(ForumDetailActivity.this, Const.PCareAdd);
                    }
                }
            });
            ll_forum_list.removeAllViews();
            if (bean.getThread() == null || bean.getThread().size() == 0) {
                ll_author_forum.setVisibility(View.GONE);
            } else {
                ll_author_forum.setVisibility(View.VISIBLE);
                for (ForumInfoEntity f :
                        bean.getThread()) {
                    TextView forum = (TextView) LayoutInflater.from(this).inflate(R.layout.tv_forum_name, ll_forum_list, false);
                    forum.setText(f.getSubject());
                    ll_forum_list.addView(forum);
                }
            }
            ll_author_info.setOnClickListener(this);

        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "setAuthorInfo");
        }
    }

    private void toCirclePage(String id) {
        startActivity(new Intent(ForumDetailActivity.this, NewCircleActivity.class).putExtra(Contants.CircleID, id));
    }

    private void showViewDelay() {

    }

    private void setListener() {
        try {
            //   myLayout.setOnSoftKeyboardListener(this);
            keyboardChangeListener.setKeyBoardListener(this);
            tv_fav.setOnClickListener(this);
            tv_pl.setOnClickListener(this);
            v_send.setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ForumDetailActivity,setListener");
        }
    }

    private void getFactoryForFlag(int flag) {
        Factory.post(this, flag);
    }

    private String getEditText() {
        return et.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {

                case R.id.tv_pl: {//定位的点击，上面有个显示数字的
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                int temp = wv_forum_content.getHeight() + ll_author_info.getHeight() + 3;
                                if (temp > 0) {
                                    mScrollView.scrollTo(0, temp);
                                }
                            } catch (Exception e) {
                                ExceptionUtils.ExceptionSend(e, "Scroll");
                            }
                        }
                    });
                    break;
                }
                case R.id.ll_author_info:
                    if (!TextUtils.isEmpty(mOwnerID))
                        startActivity(new Intent(this, InfoCenterActivity.class).putExtra(InfoCenterID, mOwnerID));
                    break;

                case R.id.send_message: {/*发送回复*/
                    getString();
                    break;
                }
                case R.id.iv_other_title_share:
                    showShare();
                    break;
                case R.id.v_half:
                case R.id.btn_no_dialog:
                    toggleSoftInput();
                    //onHidden();
                    break;
                case R.id.tv_edit_txt:
                    showSoftInput();
                    break;
                case R.id.tv_fav:
                    if (tv_fav.isSelected()) {
                        isFlw = "1";
                    } else {
                        isFlw = "0";
                    }
                    FlwForum();
                    break;
                default:
                    throw new RuntimeException("数据出错");
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ForumDetailActivity");
        }
    }

    ZYDialog dialog;

    private void cancelFollow() {
        dialog = new ZYDialog.Builder(this).setTitle("提示")
                .setMessage("确定不再关注此人?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Factory.postPhp(ForumDetailActivity.this, Const.PCareDel);
                        dialog.dismiss();
                    }
                })
                .setCancelAble(true)
                .create();
        dialog.show();
    }

    private void FlwForum() {
        try {
            getFactoryForFlag(Const.EFavorForum);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ForumDetailActivity");
        }
    }


    private void getString() {
        try {
            content = getEditText();
            if (TextUtils.isEmpty(content)) {
                showToast(getString(R.string.empty_reply));
                return;
            }
            v_send.setClickable(false);
            getFactoryForFlag(Const.EReplyForum);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ForumDetailActivity");
        }
    }


    private String summary = "";

    private void initWebView() {
        WebSettings settings = wv_forum_content.getSettings();
        settings.setJavaScriptEnabled(true);    //启用JS脚本
        wv_forum_content.addJavascriptInterface(this, "local_obj");
        wv_forum_content.addJavascriptInterface(this, "kaq");
        settings.setSupportZoom(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        WebChromeClient wcc = new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String ntitle) {
                super.onReceivedTitle(view, ntitle);
                try {
                    title = ntitle;
                    shareTitle = ntitle;
                } catch (Exception e) {
                    ExceptionUtils.ExceptionToUM(e, ForumDetailActivity.this, "WebChromeClient");
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                LogCustom.d("zys", "帖子加载进度" + newProgress);


            }
        };

        WebViewClient client = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String nurl) {
                LogCustom.i("ZYS", "URL是：" + nurl);
                if (nurl.endsWith(".jpg") || nurl.endsWith(".JPG") || nurl.endsWith(".jpeg") || nurl.endsWith(".JPEG") || nurl.endsWith(".png") || nurl.endsWith(".PNG")) {
                    startActivity(new Intent(ForumDetailActivity.this, ShowImageActivity.class).putExtra(Contants.Avatar, nurl));
                    return true;
                } else {
                    if (nurl.startsWith("http://help.kaqcn.com/Api/getStepUser")) {
                        startActivity(new Intent(ForumDetailActivity.this, ShowRecordsActivity.class).putExtra(Const.RECORD_URL, nurl));
                        return true;
                    } else {
                        return false;
                    }
                }
            }

            @Override
            public void onPageFinished(WebView view, String furl) {
                super.onPageFinished(view, furl);
                LogCustom.d("zys", "帖子内容加载完！");
                wv_forum_content.setVisibility(View.VISIBLE);
                Factory.postPhp(ForumDetailActivity.this, Const.PApi_getReplyList_2);
                wv_forum_content.loadUrl("javascript:" + Const.ZY_JS_FORUM);


            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        };
        wv_forum_content.setWebChromeClient(wcc);
        wv_forum_content.setWebViewClient(client);

        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        wv_forum_content.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && wv_forum_content.canGoBack()) {
                        //后退
                        wv_forum_content.goBack();
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
        LogCustom.i("ZYS", "urlurl：" + url);
        wv_forum_content.loadUrl(url);

    }

    private String forum_id;//帖子的id
    private String content;//回复帖子的内容
    /**
     * 下面2个参数是评论列表中的某个人所需要带上的参数
     */
    private String toinfoid;//平论人的id
    private String touser;//评论这个人的参数

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, Const.InfoID);
        if (tag == Const.PApi_delThread) {
            map.put("tid", forum_id);
        } else if (tag == Const.PApi_getReplyList_2) {
            map.put(Contants.InfoID, Const.InfoID);
            map.put(Contants.index, forum_id);
            map.put(Contants.page, page + "");
            map.put("pagesize", "30");
        } else if (tag == Const.PCareAdd) {
            map.put("uid", UserinfoData.getInfoID(this));
            map.put("careuid", mOwnerID);
        } else if (tag == Const.PCareDel) {
            map.put("uid", UserinfoData.getInfoID(this));
            map.put("careuid", mOwnerID);
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if (flag == Const.EGetForumDetail) {
            args = new String[]{
                    Contants.InfoID, Const.InfoID,
                    Contants.index, forum_id,
                    Contants.CircleID, ""
            };
        } else if (flag == Const.EReplyForum) {

            List<String> list = new ArrayList<>();
            list.add(Contants.InfoID);
            list.add(Const.InfoID);
            list.add(Contants.index);
            list.add(forum_id);
            list.add(Contants.fromuser);
            list.add(UserinfoData.getInfoname(this));
            list.add(Contants.Content);
            String decodeContetn = DecryptUtils.encode(content);
            list.add(decodeContetn.trim());


            if (replyflag) {
                if (!TextUtils.isEmpty(touser)) {
                    list.add(Contants.touser);
                    list.add(touser);
                }
                if (!TextUtils.isEmpty(toinfoid)) {
                    list.add(Contants.toinfoid);
                    list.add(toinfoid);
                }
                replyflag = false;
            } else {
                if (!TextUtils.isEmpty(mOwnerID)) {
                    list.add(Contants.toinfoid);
                    list.add(mOwnerID);
                }
            }
            args = ConstUtils.getParamsForList(list);

        } else if (flag == Const.EFavorForum) {

            args = new String[]{
                    Contants.InfoID, Const.InfoID,
                    Contants.index, forum_id,
                    Contants.strTitle, title,
                    Contants.isCancel, isFlw
            };

        }
//        else if (flag == Const.PApi_getReplyList_2) {
//
//            args = new String[]{
//                    Contants.InfoID, Const.InfoID,
//                    Contants.forum_id, forum_id,
//                    Contants.page, page + "",
//                    "pagesize", "30"
//            };
//        }
        return HttpSecretUtils.getParamString(args);
    }

    private String isFlw = "1";//取消收藏 1;   收藏 0
    private String title;

    @Override
    public void toActivity(Object t, int position) {
        try {

            if (position == Const.EReplyForum) {//这个是回复帖子
                ReplyForum replyForum = (ReplyForum) t;
                if (Contants.OK_DATA.equals(replyForum.getIResult())) {
                    showToast("回复成功");//收起输入法 刷新帖子
                    toggleSoftInput();
                    clearReplyData();
                    //清空草稿数据
                    try {
                        String temp = UserinfoData.getDtaftForumArray().get(Integer.valueOf(forum_id));
                        if (!TextUtils.isEmpty(temp))
                            UserinfoData.getDtaftForumArray().delete(Integer.valueOf(forum_id));
                    } catch (Exception e) {

                    }
                    String jfString;
                    if (!TextUtils.isEmpty(replyForum.getJf()) && !replyForum.getJf().equals("0")) {
                        jfString = "回帖成功 +" + replyForum.getJf() + "积分";
                        IntegrationManager.getInstance().addIntegration(replyForum.getJf());
                    } else {
                        jfString = "回帖成功 已达每日积分上限";
                    }
                    IntegrationPopupWindow integrationPopupWindow = new IntegrationPopupWindow(ForumDetailActivity.this, jfString);
                    integrationPopupWindow.showPopupWindow(ForumDetailActivity.this);

                    Factory.postPhp(ForumDetailActivity.this, Const.PApi_getReplyList_2);
                }
                v_send.setClickable(true);
            }
            if (position == Const.PApi_getReplyList_2) {//这个是getreplylist返回的数据

                ReplyListBean bean = (ReplyListBean) t;

                if (ll_repay_list.getVisibility() != View.VISIBLE) {
                    ll_repay_list.setVisibility(View.VISIBLE);
                }
                if (mListView_listview.getVisibility() != View.VISIBLE) {
                    mListView_listview.setVisibility(View.VISIBLE);
                }
                if (!Input_Flag && v_layout_bottom.getVisibility() != View.VISIBLE) {
                    v_layout_bottom.setVisibility(View.VISIBLE);
                }
                if (ll_author_info.getVisibility() != View.VISIBLE) {
                    ll_author_info.setVisibility(View.VISIBLE);
                }
                if (page == 0)
                    setRemindedFriends(bean.getRemindUserInfo());
                if (Const.RESULT.equals(bean.getIResult())) {
                    if (bean.getPageNum().equals("0"))
                        setAuthorInfo(bean);
                    mOwnerID = bean.getOwnerID();
                    List<ReplyListBean.ReplyNumEntity> list = bean.getReplyNum();
                    if (page == 0) {
                        initCircleView(bean.getCircleId());
                        String count = bean.getReplyCount();
                        if (OtherUtils.isEmpty(count)) {
                            tv_pl.setText("0");
                            huifu_num.setText("暂无评论");
                        } else {
                            tv_pl.setText(count);
                            huifu_num.setText("最新评论(" + count + ")");
                        }
                    }

                    if (list == null || list.size() == 0) {
                        if (page == 0) {

                        } else {
                            if (loading) {
                                page--;
                                mListView_listview.hideFooterView(CustomRefreshListView.LOADING_MAX, true);
                            }
                        }
                    } else {
                        if (page == 0) {
                            data = new ArrayList<>();
                        }
                        data.addAll(list);

                        adapter.update(data, mOwnerID);
                        if (loading) {
                            mListView_listview.hideFooterView(CustomRefreshListView.SUCCEED, true);
                        }
                    }
                } else {

                    if (loading) {
                        page--;
                        mListView_listview.hideFooterView(CustomRefreshListView.FAIL, true);
                    }
                }
                setLoadingAble();
            }

            if (position == Const.EFavorForum) {//这个是收藏帖子的数据
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
            }
            if (position == Const.PApi_delThread) {
                PostForumBean postForumBean = (PostForumBean) t;
                if (postForumBean.getIResult().equals(Contants.RESULT)) {
                    showToast("删除成功!");
                    finish();
                } else {
                    showToast("删除失败!");
                }
            }
            if (position == Const.PCareAdd) {
                PhpUserInfoBean bean = (PhpUserInfoBean) t;
                if (Const.RESULT.equals(bean.getiResult())) {
                    cb_care.setSelected(true);
                    cb_care.setText("已关注");
                    showToast("关注成功");
                } else {
                    showToast("关注失败");
                }
            }
            if (position == Const.PCareDel) {
                PhpUserInfoBean bean = (PhpUserInfoBean) t;
                if (Const.RESULT.equals(bean.getiResult())) {
                    cb_care.setSelected(false);
                    cb_care.setText("＋ 关注");
                    showToast("取消关注成功");
                } else {
                    showToast("取消关注失败");
                }
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ForumDetailActivity");
        }
    }

    //设置@到的好友
    private void setRemindedFriends(final ArrayList<RemindUserEntity> remindUserInfo) {
        try {
            if (remindUserInfo != null && remindUserInfo.size() > 0) {
                ll_reminded_friends_view.setVisibility(View.VISIBLE);
                tv_remind_tag.setText("@到的圈友:");
                ll_reminded_friends.removeAllViews();
                LayoutInflater inflater = LayoutInflater.from(this);
                int showCount = remindUserInfo.size() > 6 ? 6 : remindUserInfo.size();//最多展示6名被@的圈友
                for (int i = 0; i < showCount; i++) {
                    final RemindUserEntity remindUserEntity = remindUserInfo.get(i);
                    ImageView imageView = (ImageView) inflater.inflate(R.layout.layout_heard, ll_reminded_friends, false);
                    ll_reminded_friends.addView(imageView);
                    Glide.with(this).load(remindUserEntity.getOss_request_url()).error(R.mipmap.loading_fail).into(imageView);
                }
                ll_reminded_friends_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ForumDetailActivity.this, RemindedFriendsListActivity.class).putParcelableArrayListExtra("data", remindUserInfo));
                    }
                });
            } else {
                ll_reminded_friends_view.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "setRemindedFriends");
        }
    }

    private void setLoadingAble() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(500);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    mScrollView.hideLoading();
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "onDownPullRefresh");
                }
            }
        }.execute(new Void[]{});
    }

    private boolean refresh = false;
    private boolean loading = false;

    @Override
    public void onDownPullRefresh() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(1000);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    LogCustom.i(Const.TAG.ZY_OTHER, "下拉刷新");
                    refresh = true;
                    loading = false;
                    page = 0;
                    Factory.postPhp(ForumDetailActivity.this, Const.PApi_getReplyList_2);
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "onDownPullRefresh");
                }
            }
        }.execute(new Void[]{});
    }

    @Override
    public void onLoadingMore() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(1000);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    LogCustom.i(Const.TAG.ZY_OTHER, "加载更多");
                    refresh = false;
                    loading = true;
                    page++;
                    Factory.postPhp(ForumDetailActivity.this, Const.PApi_getReplyList_2);
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "onLoadMore");
                }
            }
        }.execute(new Void[]{});
    }

    private void toggleSoftInput() {
        try {
            View v = getCurrentFocus();
            hideKeyboard(v.getWindowToken());
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ForumDetailActivity");
        }
    }

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {
        if (tag == Const.PApi_getReplyList_2) {
            if (findViewById(R.id.pd).getVisibility() == View.VISIBLE) {
                findViewById(R.id.pd).setVisibility(View.GONE);
                showJF();
            }

        }
    }

    @Override
    public void showError(int tag) {
        if (tag == Const.PApi_getReplyList_2) {
            findViewById(R.id.pd).setVisibility(View.GONE);
            setLoadingAble();
            if (loading) {
                page--;
                mListView_listview.hideFooterView(CustomRefreshListView.FAIL, true);
            }
        } else if (tag == Const.EReplyForum) {
            v_send.setClickable(true);
        } else if (tag == Const.PApi_delThread) {
            showToast("删除失败！");
        }
    }

    //关闭键盘
    private void hideInput() {
        try {
            findViewById(R.id.v_half).setVisibility(View.GONE);
            content = et.getText().toString();
            if (!TextUtils.isEmpty(content)) {
                UserinfoData.getDtaftForumArray().put(Integer.valueOf(forum_id), content.trim());
            }
            flag = false;
            clearReplyData();
            v_edit.setVisibility(View.GONE);
            Input_Flag = false;
            v_layout_bottom.setVisibility(View.VISIBLE);
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
            tv_edit_title.setText("我想说...");
            touser = "";
            toinfoid = "";
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ForumDetailActivity");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private boolean replyflag = false;

    /**
     * 回复列表中的某项被点击了
     *
     * @param position
     */
    @Override
    public void showInput(int position) {
        try {
            replyflag = true;
            ReplyListBean.ReplyNumEntity entity = adapter.getItem(position);
            tv_edit_title.setText("回复:" + entity.getFromUser());

            showSoftInput();
            toinfoid = entity.getUserId();
            touser = entity.getFromUser();
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ForumDetailActivity");
        }
    }

    private boolean Input_Flag = false;

    private void showSoftInput() {
        try {
            v_layout_bottom.setVisibility(View.GONE);
            Input_Flag = true;
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideKeyboard(v, ev)) {
                    hideKeyboard(v.getWindowToken());
                }
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ForumDetailActivity");
        }
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            return true;
        }

    }

    @Override
    public void onLoadingData() {
        LogCustom.i("ZYS", "到达底部");
        mListView_listview.setLoading();
    }

    @Override
    public void finish() {
        int form = getIntent().getIntExtra(Const.INTENT_FROM, 0);
        if (form == Const.FM) {
            afterFinish();
        }
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wv_forum_content != null) {
            //   clearWebViewCache();
            wv_forum_content.clearCache(false);
            wv_forum_content.removeAllViews();
            wv_forum_content.destroy();
            wv_forum_content = null;
        }
    }

    /**
     * @param v     editText对象
     * @param event 点击事件
     * @return 是否忽略
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        try {
            if (v != null) {
                int[] m = {0, 0};
                try {
                    v_edit.getLocationInWindow(m);
                } catch (Exception e) {
                    ExceptionUtils.ExceptionToUM(e, this, "ForumDetailActivity,isShouldHideKeyboard");
                }

                int layoutTop = m[1];
                // 点击EditText的事件，忽略它。
                return layoutTop > event.getY();
//                if (layoutTop < event.getY()) {
//                    // 点击EditText的事件，忽略它。
//                    return false;
//                } else {
//                    return true;
//                }
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "isShouldHideKeyboard");
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    private void hideKeyboard(IBinder token) {
        try {
            if (token != null) {
                content = getEditText();
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ForumDetailActivity");
        }
    }

//    private IWXAPI wxApi;
//
//    private void initShareWX() {
//        wxApi = WXAPIFactory.createWXAPI(this, Contants.AppCfg.WX__APPID);
//        wxApi.registerApp(Contants.AppCfg.WX__APPID);
//    }
//
//    public void wechatShare(int flag) {
//        try {
//            WXWebpageObject webpage = new WXWebpageObject();
//            webpage.webpageUrl = shareURl;
//            WXMediaMessage msg = new WXMediaMessage(webpage);
//            msg.tv_title = shareTitle;
//            if (TextUtils.isEmpty(shareContent))
//                shareContent = getString(R.string.share_content_init);
//            msg.description = shareContent;
//            //这里替换一张自己工程里的图片资源
//            Bitmap thumb = null;
//            try {
//                thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//                msg.setThumbImage(thumb);
//            } catch (Exception e) {
//                LogCustom.i("ZYS", "JSON error");
//            }
//
//            SendMessageToWX.Req req = new SendMessageToWX.Req();
//            req.transaction = String.valueOf(System.currentTimeMillis());
//            req.message = msg;
//            req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
//            wxApi.sendReq(req);
//            try {
//                if (thumb != null) {
//                    thumb.recycle();
//                }
//            } catch (Exception e) {
//
//            }
//            LogCustom.i("ZYS", "wechatShare OK" + req.toString());
//        } catch (Exception e) {
//            LogCustom.i("ZYS", "wechatShare error");
//            ExceptionUtils.ExceptionToUM(e, this, "wechatShare");
//        }
//    }

    private String shareIcon = null;

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

    private void showShare() {
        if (isMyForum) {
            if (forumSharePopupWindow == null) {
                forumSharePopupWindow = new ForumSharePopupWindow(this);
                forumSharePopupWindow.setOnItemClickListener(new ForumSharePopupWindow.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        switch (position) {
                            case 0:
                                showShareView();
                                break;
                            case 1:
                                startActivityForResult(new Intent(ForumDetailActivity.this, ReleaseForumActivity.class).putExtra("isForumEdit", true).putExtra(Const.FORUM_ID, forum_id), REQUEST_CODE_EDIT_FORUM);
                                break;
                            case 2:
                                showShantie();
                                break;
                        }
                    }
                });
            }
            forumSharePopupWindow.showPopupWindow(this);
        } else {
            showShareView();
        }


    }

    //分享
    private void showShareView() {
        if (fragment == null) {
            fragment = ShareFragment.getInstance(shareURl, shareTitle, shareContent, shareIcon, Const.SHARE_FORUM);
        }
        if (fragment.getDialog() == null || !fragment.getDialog().isShowing())
            fragment.show(getSupportFragmentManager(), ShareFragment.type);
    }

    private void showShantie() {
        ZYDialog.Builder dialog = new ZYDialog.Builder(this);
        dialog.setTitle("删除帖子");
        dialog.setMessage("您确定删除本帖？");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //回调 callback
                dialog.cancel();
                Factory.postPhp(ForumDetailActivity.this, Const.PApi_delThread);
                finish();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setCancelAble(false);
        dialog.create().show();
    }

    @JavascriptInterface
    public void appInfoCenter(String param) {
        if (!TextUtils.isEmpty(param))
            startActivity(new Intent(this, InfoCenterActivity.class).putExtra(InfoCenterID, param));
    }

    @JavascriptInterface
    public void AppJumpToYouZan(String param) {
        if (!TextUtils.isEmpty(param)) {
            startActivity(new Intent(ForumDetailActivity.this, YouzanActivity.class).putExtra("uzUrl", param));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_FORUM && resultCode == RESULT_OK) {//帖子编辑成功
            finish();
        }

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
                    String temp = UserinfoData.getDtaftForumArray().get(Integer.valueOf(forum_id));
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

    @Override
    protected void onPause() {
        super.onPause();
        //暂停WebView在后台的所有活动
        wv_forum_content.onPause();
        //暂停WebView在后台的JS活动
        wv_forum_content.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        wv_forum_content.onResume();
        wv_forum_content.resumeTimers();
    }
}
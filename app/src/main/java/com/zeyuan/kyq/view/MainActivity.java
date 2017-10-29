package com.zeyuan.kyq.view;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.meelive.ingkee.sdk.plugin.IInkeCallback;
import com.meelive.ingkee.sdk.plugin.InKeSdkPluginAPI;
import com.meelive.ingkee.sdk.plugin.entity.ShareInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;
import com.zeyuan.kyq.Entity.HomePageEntity;
import com.zeyuan.kyq.Entity.PayWxEntity;
import com.zeyuan.kyq.Entity.PushNewEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.MyFragmentAdapter;
import com.zeyuan.kyq.app.AppManager;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.LoginQQBean;
import com.zeyuan.kyq.bean.MyDataBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.FragmentCallBack;
import com.zeyuan.kyq.biz.manager.ClickStatisticsManager;
import com.zeyuan.kyq.biz.manager.IntegrationManager;
import com.zeyuan.kyq.biz.manager.MyCircleManager;
import com.zeyuan.kyq.biz.manager.PointManager;
import com.zeyuan.kyq.db.DBHelper;
import com.zeyuan.kyq.fragment.main.CircleFragment;
import com.zeyuan.kyq.fragment.main.DecisionFragment;
import com.zeyuan.kyq.fragment.main.EventFragment;
import com.zeyuan.kyq.fragment.main.HomeBannerFragment;
import com.zeyuan.kyq.fragment.main.HomeFragment;
import com.zeyuan.kyq.fragment.main.MoreNewFragment;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.utils.ZYInKeUtils;
import com.zeyuan.kyq.widget.DrawCircleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 固定方向
 * 主页面
 */
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener,
        ViewDataListener, HttpResponseInterface, FragmentCallBack {
    private static final String TAG = "MainActivity";

    private DecisionFragment decisionFragment;
    private HomeFragment homeFragment;
    private CircleFragment circleFragment;
    private MoreNewFragment moreFragment;
    private EventFragment eventFragment;
    private ImageView redpoint_main;
    private boolean hideMore = false;
    private boolean exit = false;
    private int form = 0;
    private static final String YK_APP_ID = "1000300001";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent();
        setContentView(R.layout.activity_main);
        try {
            if (savedInstanceState != null) {
                finish();
            }
            overridePendingTransition(R.anim.activity_change_in, R.anim.activity_change_out);
            initStatusBar();
            //主页开启标识
            ZYApplication.mainPageFlag = true;

            AppManager.getAppManager().addActivity(this);
            form = getIntent().getIntExtra(Const.INTENT_FROM, 0);
            //初始化积分统计工具
            IntegrationManager.getInstance().init();
            //初始化我的圈子
            MyCircleManager.getInstance().init(getApplicationContext());


            //初始化um
            initUmeng();
            //初始化
            initView();
            initListener();
            //注册映客 1:不包含大厅 0:不修改主题色
            InKeSdkPluginAPI.register(inkeCallback, YK_APP_ID, 1, 0);
            //登入统计
            Factory.postPhp(this, Const.PUserLoginAdd);

        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, TAG);
        }
    }

    //初始化友盟
    private void initUmeng() {
        try {
            MobclickAgent.onProfileSignIn(UserinfoData.getInfoID(this));
            if (!TextUtils.isEmpty(ZYApplication.UmToken))
                Factory.post(MainActivity.this, Const.ESetTokenDevice);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "initUmeng");
        }
    }

    private TextView tv_push_num;

    //设置消息数量
    private void initNewsNum() {
        try {
            List<PushNewEntity> data = DBHelper.getInstance().
                    queryPush(ZYApplication.application);
            if (tv_push_num == null) {
                tv_push_num = (TextView) findViewById(R.id.tv_news_num_main);
            }
            if (data != null && data.size() > 0) {
                int n = 0;
                for (PushNewEntity entity : data) {
                    if (0 == entity.getRead()) {
                        n++;
                    }
                }
                if (n != 0) {
                    tv_push_num.setVisibility(View.VISIBLE);
                    tv_push_num.setText(n + "");
                    if (selectedIndex == 0 && homeFragment != null) {
                        homeFragment.setNewsView(n);
                    }
                } else {
                    if (selectedIndex == 0 && homeFragment != null) {
                        homeFragment.setNewsView(0);
                    }
                    tv_push_num.setVisibility(View.GONE);
                }
            } else {
                tv_push_num.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "initNewsNum");
        }
    }

    private ImageView v3;
    private ImageView v4;
    private TextView tv4;
    private RadioButton[] btns;
    private Fragment[] fragments = null;
    private View v_news_all;
    private View v_refresh;
    private View redpoint_main_circle;//圈子的小红点

    //设置视图
    private void initView() {
        try {
            //设置消息视图
            v_news_all = findViewById(R.id.v_news_all);
            v_news_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, NewsCenterActivity.class));
                }
            });
            //设置消息数目
            initNewsNum();
            findViewById(R.id.v_main_middle_btn).setOnClickListener(new MyBottomListener());
            v3 = (ImageView) findViewById(R.id.iv_main_middle_no_selected);
            v4 = (ImageView) findViewById(R.id.iv_main_middle_selected);
            tv4 = (TextView) findViewById(R.id.tv_main_middle_btn);
            redpoint_main_circle = findViewById(R.id.redpoint_main_circle);
            //新回复红点控件
            redpoint_main = (ImageView) findViewById(R.id.redpoint_main);
            try {
                DisplayMetrics metric = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metric);
                int width = metric.widthPixels; // 屏幕宽度（像素）
                redpoint_main.setPadding(0, 0, width / 10 - 36, 0);
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "红点位置计算出错");
            }
            btns = new RadioButton[5];
            btns[0] = (RadioButton) findViewById(R.id.btn_main);
            btns[1] = (RadioButton) findViewById(R.id.btn_event);
            btns[2] = (RadioButton) findViewById(R.id.btn_circle);
            btns[3] = (RadioButton) findViewById(R.id.btn_more);
            btns[4] = (RadioButton) findViewById(R.id.btn_empty);
            if (form == Const.FM) {
                btns[2].setChecked(true);
            } else {
                btns[0].setChecked(true);
            }

            homeFragment = new HomeFragment();
            eventFragment = new EventFragment();
            decisionFragment = new DecisionFragment();
            circleFragment = new CircleFragment();
            moreFragment = new MoreNewFragment();
            fragments = new Fragment[]{homeFragment, eventFragment, circleFragment, moreFragment, decisionFragment};
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (form == Const.FM) {
                currentIndex = 2;
                transaction.add(R.id.main_content, circleFragment);
                transaction.show(circleFragment);
            } else {
                transaction.add(R.id.main_content, homeFragment);
                transaction.show(homeFragment);
            }
            transaction.commit();
            //刷新按钮
            v_refresh = findViewById(R.id.v_main_frush_btn);
            PointManager.getInstance().addOnPointNumChangedListener(new PointManager.OnPointNumChangedListener() {
                @Override
                public void onPointNumChanged(String pointString) {
                    if (pointString.contains("2") || pointString.contains("3") || pointString.contains("4")) {
                        refreshCirclePointStatus(true);
                    } else {
                        refreshCirclePointStatus(false);
                    }
                }
            });
            PointManager.getInstance().refreshPointData(getApplicationContext());
            ClickStatisticsManager.getInstance().uploadClickData(this);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "initView");
        }
    }

    //设置监听
    private void initListener() {
        MyBottomListener myButtonListener = new MyBottomListener();
        for (int i = 0; i < btns.length; i++) {
            btns[i].setOnClickListener(myButtonListener);
        }
        findViewById(R.id.btn_main_frush).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toHomeRefresh();
            }
        });
    }

    //点击主页刷新
    private void toHomeRefresh() {
        if (homeFragment != null) {
            homeFragment.toRefresh();
        }
    }

    //显示或隐藏刷新按钮
    public void showRefreshView(boolean flag) {
        if (flag) {
            v_refresh.setVisibility(View.VISIBLE);
        } else {
            v_refresh.setVisibility(View.GONE);
        }
    }

    @Override
    public void finish() {
        if (form == Const.FM) {
            afterFinish();
        }
        super.finish();
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (form == Const.FM) {
                finish();
            } else {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    try {
                        exit = true;
                        if (!TextUtils.isEmpty(ZYApplication.UmToken))
                            Factory.post(this, Const.ESetTokenDevice);
                        MobclickAgent.onProfileSignOff();
                        System.gc();
                    } catch (Exception e) {
                        ExceptionUtils.ExceptionToUM(e, this, "onKeyDown(),MainActivity");
                    }
                    ZYApplication.mainPageFlag = false;
                    form = 0;
                    finish();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap();
        if (Const.InfoID == null) {
            Const.InfoID = UserinfoData.getInfoID(this);
        }
        if (tag == Const.EGetMyForumNum) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        } else if (tag == Const.PUserLoginAdd) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        } else if (tag == Const.ESyncConf) {
            map = new HashMap<>();
        } else if (tag == Const.PCateinkeorder) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
            map.put("pay", orderId);
            map.put("callString", callString);
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if (flag == Const.ESetTokenDevice) {
            if (exit) {
                args = new String[]{
                        Contants.InfoID, UserinfoData.getInfoID(this),
                        "DeviceID", ZYApplication.deviceId,
                        "UploadType", "3",
                        "TokenType", "1",
                        "Token", ZYApplication.UmToken
                };
            } else {
                args = new String[]{
                        Contants.InfoID, UserinfoData.getInfoID(this),
                        "DeviceID", ZYApplication.deviceId,
                        "UploadType", "2",
                        "TokenType", "1",
                        "Token", ZYApplication.UmToken
                };
            }
        } else {
            args = new String[]{Contants.InfoID, UserinfoData.getInfoID(this)};
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object t, int tag) {
        try {
            if (tag == Const.EGetMyForumNum) {
                MyDataBean myDataBean = (MyDataBean) t;
                if (Contants.RESULT.equals(myDataBean.getIResult())) {
                    try {
                        CircleIsUpdate(myDataBean);
                    } catch (Exception e) {
                        ExceptionUtils.ExceptionToUM(e, this, "CircleIsUpdate()");
                    }
                }
            } else if (tag == Const.PCateinkeorder) {
                PayWxEntity bean = (PayWxEntity) t;
                if (Const.RESULT.equals(bean.getiResult())) {
                    sendPayReq(bean);
                } else {
                    InKeSdkPluginAPI.dealPay(orderId, false);
                }
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "toActivity()");
        }
    }

    //转跳支付页面
    private void sendPayReq(PayWxEntity entity) {
        startActivity(new Intent(this, InKePayActivity.class).putExtra(Const.INTENT_INKE_PAY, entity));
    }

    @Override
    public void showLoading(int tag) {
    }

    @Override
    public void hideLoading(int tag) {
    }

    @Override
    public void showError(int tag) {
        if (tag == Const.PCateinkeorder) {
            InKeSdkPluginAPI.dealPay(orderId, false);
        }
    }

    //更新红点状态
    public void CircleIsUpdate(MyDataBean myDataBean) {
        try {
            if (myDataBean == null || redpoint_main == null) {
                return;
            }
            int forumNum = Integer.valueOf(myDataBean.getReplyNum());
            int mReplyNum = UserinfoData.getReplyNum(this);

            if (forumNum != 0 && forumNum != mReplyNum) {
                redpoint_main.setVisibility(View.VISIBLE);
            } else {
                redpoint_main.setVisibility(View.GONE);
            }

            try {
                if (moreFragment != null && hideMore) {
                    moreFragment.bindView(myDataBean);
                }
            } catch (Exception e) {
                ExceptionUtils.ExceptionToUM(e, this, "不能刷新更多页面");
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "CircleIsUpdate()");
        }
    }

    public void forceCircleUpdate(boolean f) {
        if (redpoint_main != null) {
            if (f) {
                redpoint_main.setVisibility(View.VISIBLE);
            } else {
                redpoint_main.setVisibility(View.GONE);
            }
        }
    }

    //控制圈子的小红点
    public void refreshCirclePointStatus(boolean isShow) {
        if (redpoint_main_circle != null) {
            if (isShow) {
                redpoint_main_circle.setVisibility(View.VISIBLE);
            } else {
                redpoint_main_circle.setVisibility(View.GONE);
            }
        }
    }

    public void setPlanDrug(String planID) {
    }

    public void setPerformChange(String performID) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZYApplication.mainPageFlag = false;
        try {
            if (UiUtils.isOnMainThread()) {
                Glide.with(ZYApplication.application).pauseRequests();
            }
            PointManager.getInstance().clearOnPointNumChangedListener();
            ClickStatisticsManager.getInstance().saveEventData(this);
            IntegrationManager.getInstance().clearOnIntegrationChangedListener();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "Main onDestroy");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            initNewsNum();
            Factory.post(this, Const.EGetMyForumNum);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "Main Resume");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void showUI(int flag) {
        super.showUI(flag);
    }

    //刷新图标是否显示
    private boolean refreshFlag = false;

    //保存刷新是否显示状态
    private void saveRefreshFlag() {
        if (v_refresh.getVisibility() == View.VISIBLE) {
            refreshFlag = true;
        } else {
            refreshFlag = false;
        }
    }

    //选中的下标
    private int selectedIndex;
    //当前的下标
    private int currentIndex = 0;

    //底部按钮点击事件
    class MyBottomListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                switch (v.getId()) {
                    case R.id.btn_main:
                        changeViewGone(true);
                        selectedIndex = 0;
                        ZYApplication.homeMoveFlag = true;
                        ZYApplication.mainMoveFlag = false;
                        ZYApplication.EventMoveFlag = false;
                        ZYApplication.CircleMoveFlag = false;
                        showRefreshView(refreshFlag);
                        break;
                    case R.id.btn_event:
                        selectedIndex = 1;
                        ZYApplication.homeMoveFlag = false;
                        ZYApplication.mainMoveFlag = false;
                        ZYApplication.CircleMoveFlag = false;
                        break;
                    case R.id.btn_circle:
                        changeViewGone(true);
                        selectedIndex = 2;
                        ZYApplication.homeMoveFlag = false;
                        ZYApplication.mainMoveFlag = false;
                        ZYApplication.EventMoveFlag = false;
                        ZYApplication.CircleMoveFlag = true;
                        if (!ZYApplication.toCircleAble) {
                            Factory.onEvent(MainActivity.this,
                                    Const.EVENT_EnterCirclePerLaunch, Const.EVENTFLAG, null);
                        }
                        saveRefreshFlag();
                        showRefreshView(false);
                        refreshCirclePointStatus(false);
                        break;
                    case R.id.btn_more:
                        if (!hideMore) {
                            hideMore = true;
                        }
                        changeViewGone(false);
                        selectedIndex = 3;
                        ZYApplication.homeMoveFlag = false;
                        ZYApplication.mainMoveFlag = false;
                        ZYApplication.EventMoveFlag = false;
                        ZYApplication.CircleMoveFlag = false;
                        saveRefreshFlag();
                        showRefreshView(false);
                        break;
                    case R.id.v_main_middle_btn:
                        changeViewGone(false);
                        selectedIndex = 4;
                        ZYApplication.homeMoveFlag = false;
                        ZYApplication.mainMoveFlag = true;
                        ZYApplication.EventMoveFlag = false;
                        ZYApplication.CircleMoveFlag = false;
                        saveRefreshFlag();
                        showRefreshView(false);
                        break;
                }

                if (selectedIndex != currentIndex && selectedIndex != 1) {
                    FragmentTransaction transaction = getSupportFragmentManager()
                            .beginTransaction();
                    transaction.hide(fragments[currentIndex]);

                    if (!fragments[selectedIndex].isAdded()) {

                        transaction.add(R.id.main_content,
                                fragments[selectedIndex]);
                    }
                    transaction.show(fragments[selectedIndex]);
                    transaction.commit();
                    if (currentIndex == 4) {
                        changeMiddleBtn(false);
                    }
                    if (selectedIndex == 4) {
                        changeMiddleBtn(true);
                    }

                    btns[selectedIndex].setChecked(true);
                    currentIndex = selectedIndex;
                }
                if (selectedIndex == 1) {
                    btns[currentIndex].setChecked(true);
                    btns[selectedIndex].setChecked(false);
                    toServiceCenter(0);
                }
            } catch (Exception e) {
                ExceptionUtils.ExceptionToUM(e, MainActivity.this, "onTabClicked()");
            }
        }
    }

    //去服务中心
    private void toServiceCenter(int type) {
        startActivity(new Intent(this, ServiceCenterActivity.class)
                .putExtra(Const.FLAG_SERVICE_CENTER, type));
    }

    //转跳圈子或圈圈助手
    //flag: 2为圈子 4为圈圈助手
    public void toBottomPage(int flag) {
        try {
            selectedIndex = flag;
            ZYApplication.homeMoveFlag = false;
            ZYApplication.EventMoveFlag = false;
            if (flag == 2) {
                changeViewGone(true);
                ZYApplication.CircleMoveFlag = true;
                ZYApplication.mainMoveFlag = false;
            } else {
                changeViewGone(false);
                ZYApplication.CircleMoveFlag = false;
                ZYApplication.mainMoveFlag = true;
            }
            saveRefreshFlag();
            showRefreshView(false);
            if (selectedIndex != currentIndex) {
                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction();
                transaction.hide(fragments[currentIndex]);
                if (!fragments[selectedIndex].isAdded()) {
                    transaction.add(R.id.main_content, fragments[selectedIndex]);
                }
                transaction.show(fragments[selectedIndex]);
                transaction.commit();
                if (currentIndex == 4) {
                    changeMiddleBtn(false);
                }
                if (selectedIndex == 4) {
                    changeMiddleBtn(true);
                }
                btns[selectedIndex].setChecked(true);
                currentIndex = selectedIndex;
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "toBottomPage Error");
        }
    }

    public void changeViewGone(boolean gf) {
        if (v_news_all == null) return;
        if (gf) {
            v_news_all.setVisibility(View.GONE);
        } else {
            v_news_all.setVisibility(View.VISIBLE);
        }
    }

    public void changeViewGone(boolean gf, int flag) {
        if (v_news_all == null) return;
        if (gf && !IsShow) {
            v_news_all.setVisibility(View.GONE);
        } else {
            v_news_all.setVisibility(View.VISIBLE);
        }
    }

    private boolean IsShow = true;

    public void changeViewGone(boolean gf, boolean IsShow) {
        this.IsShow = IsShow;
        changeViewGone(gf, 0);
    }

    //圈圈助手显示切换
    public void changeMiddleBtn(boolean gf) {
        if (gf) {
            Animation a = AnimationUtils.loadAnimation(this, R.anim.iv_change_selected);
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v3.setVisibility(View.GONE);
                    Animation b = AnimationUtils.loadAnimation(MainActivity.this, R.anim.iv_enter_alpha);
                    v4.startAnimation(b);
                    v4.setVisibility(View.VISIBLE);
                    tv4.setSelected(true);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            v3.startAnimation(a);
        } else {
            Animation a = AnimationUtils.loadAnimation(this, R.anim.iv_change_exit);
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v4.setVisibility(View.GONE);
                    Animation b = AnimationUtils.loadAnimation(MainActivity.this, R.anim.iv_change_no_selected);
                    v3.startAnimation(b);
                    v3.setVisibility(View.VISIBLE);
                    tv4.setSelected(false);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            v4.startAnimation(a);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private ViewPager vp_banner;
    private DrawCircleView dcv;
    private FrameLayout fl_home;

    //显示广告banner
    public void showHomeBanner(List<HomePageEntity> banners) {
        try {
            fl_home = (FrameLayout) findViewById(R.id.fl_home);
            View v = View.inflate(this, R.layout.dialog_home_banner, null);
            v.findViewById(R.id.v_more).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        toServiceCenter(1);
                    } catch (Exception e) {
                        ExceptionUtils.ExceptionSend(e, "toEvent Click");
                    }
                    fl_home.removeAllViews();
                    fl_home.setVisibility(View.GONE);
                }
            });
            v.findViewById(R.id.v_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fl_home.removeAllViews();
                    fl_home.setVisibility(View.GONE);
                }
            });
            if (banners != null && banners.size() > 0) {
                vp_banner = (ViewPager) v.findViewById(R.id.vp_banner);
                dcv = (DrawCircleView) v.findViewById(R.id.dcv);
                ArrayList<Fragment> fragments = new ArrayList<>();
                HomeBannerFragment fragment;
                Bundle bundle;
                for (int i = 0; i < banners.size(); i++) {
                    fragment = new HomeBannerFragment();
                    fragment.setPageFlag(false);
                    HomePageEntity entity = banners.get(i);
                    bundle = new Bundle();
                    bundle.putSerializable(HomeBannerFragment.BANNER_DATA, entity);
                    fragment.setArguments(bundle);
//                fragment.setCallback(this);
                    fragments.add(fragment);
                }
                dcv.setDrawCricle(fragments.size(), 6, Color.parseColor("#4c4c4c"), Color.parseColor("#FFFFFF"));
                dcv.redraw(0);
                MyFragmentAdapter mAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments);
                vp_banner.setAdapter(mAdapter);
                vp_banner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        dcv.redraw(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
            }
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            fl_home.addView(v);
            fl_home.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "showHomeBanner");
        }
    }

    @Override
    public void dataCallBack(String str, int flag, String tag, Object obj) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogCustom.i("ZYS", "onActivityResult MainActivity:" + requestCode + "resultCode:" + resultCode);
        switch (resultCode) {
            case Const.RESULT_CODE_ALL_MENU_FLAG:
                int flag = data.getIntExtra(Const.RESULT_ALL_MENU_FLAG, 0);
                if (flag == 2 || flag == 4) toBottomPage(flag);
                break;
        }
        //完成新手引导后，提醒fragment刷新数据
        if (requestCode == 9 && resultCode == RESULT_OK) {
            if (homeFragment != null && homeFragment.isAdded())
                homeFragment.onActivityResult(requestCode, resultCode, data);
            if (decisionFragment != null && decisionFragment.isAdded())
                decisionFragment.onActivityResult(requestCode, resultCode, data);

        }//完成手机号修改后，刷新号码显示
        else if (requestCode == 8 && resultCode == RESULT_OK) {
            if (moreFragment != null && moreFragment.isAdded()) {
                moreFragment.setPhoneNum();
            }
        }

    }

    IInkeCallback inkeCallback = new IInkeCallback() {
        @Override
        public void commonTrigger(String s) {

        }

        @Override
        public void loginTrigger() {
        }

        @Override
        public void payTrigger(String orderId, String callString) {
            toPay(orderId, callString);
        }

        @Override
        public void shareTrigger(ShareInfo share) {
            ZYInKeUtils.toShare(share, MainActivity.this, new ShareBaseUiListener());
        }

        @Override
        public void createLiveReturnTrigger(String s) {
        }

        @Override
        public void stopLiveTrigger(String s) {
        }
    };

    private String orderId;
    private String callString;

    //去支付
    private void toPay(String orderId, String callString) {
        this.orderId = orderId;
        this.callString = callString;
        Factory.postPhp(this, Const.PCateinkeorder);
    }

    //分享所需的回调
    private class ShareBaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
        }

        protected void doComplete(LoginQQBean values) {
        }

        @Override
        public void onError(UiError e) {
        }

        @Override
        public void onCancel() {
        }
    }

}

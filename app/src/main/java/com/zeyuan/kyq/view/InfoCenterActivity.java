package com.zeyuan.kyq.view;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.zeyuan.kyq.Entity.HomePageEntity;
import com.zeyuan.kyq.Entity.InfoCenterEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.app.BaseZyFragment;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.BlurBiz;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.fragment.other.UserArticleFragment;
import com.zeyuan.kyq.fragment.other.UserForumFragment;
import com.zeyuan.kyq.fragment.other.UserReleaseFragment;
import com.zeyuan.kyq.utils.BlurUtil.BlurColor;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DensityUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.OtherUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CircleImageView;
import com.zeyuan.kyq.widget.PullToRefresh.PullToRefreshLayout;
import com.zeyuan.kyq.widget.PullToRefresh.UserPullableScrollView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人中心
 * <p>
 * Time:2016-10-28 12:00
 *
 * @author wwei
 */
public class InfoCenterActivity extends BaseActivity implements View.OnClickListener, ViewDataListener
        , HttpResponseInterface, BlurBiz.BlurInterface, AdapterView.OnItemClickListener
        , UserPullableScrollView.OnScrollListener, PullToRefreshLayout.OnRefreshListener {

    //个人中心用户ID
    private String InfoCenterID;
    //个人中心用户呢称
    private String InfoCenterName;
    //头像
    private CircleImageView civ_avatar;
    //大V标识
    private View v_big;
    //祈福内容
    private TextView tv_wash;
    //返回图标
    private ImageView back;
    //滑动器
    private UserPullableScrollView sv;
    //模糊对象
    private BlurColor bc;
    private BlurColor bc_t;
    //被模糊图片
    private ImageView iv_blur;
    private ImageView iv_blur_title;
    //模糊背景图层
    private TextView tv_blur;
    private TextView tv_blur_title;
    //抗癌天数
    private TextView tv_day;
    private View v_day;
    //用户昵称
    private TextView tv_name;
    //粉丝点击区域
    private View v_follower;
    //关注点击区域
    private View v_care;
    //收藏点击区域
    private View v_keep;
    //收藏数
    private TextView tv_keep_num;
    //关注数
    private TextView tv_care_num;
    //粉丝数
    private TextView tv_follower_num;
    //是否关注
    private TextView cb_box;
    //状态栏高度
    private int statusBarHeight;
    //状态栏 仅用于占位
    private View statusBar1;
    //颜色改变区
    private View viewColorChange;
    //高度换算
    private int ch = 0;
    private int ChangeHeight;
    //是否有文章
    private boolean haveArt = false;
    //滑动器外层控件
    private PullToRefreshLayout layout;
    //布局整体显示隐藏控件
    private View v;

    //病人的癌症信息
    private LinearLayout ll_cancer_info;
    private TextView tv_cancer_info;
    private TextView tv_cancer_info_more;
    //头部隐藏控件
    private View v_tab_top;
    private View v_art2;
    private View v_forum2;
    private View v_reply2;
    private View line_art2;
    private View line_forum2;
    private View line_reply2;
    private TextView tv_art2;
    private TextView tv_forum2;
    private TextView tv_reply2;
    private ImageView iv_three12;
    private ImageView iv_three22;
    //Tab条目相关控件
    private View v_tab;
    private View v_art;
    private View v_forum;
    private View v_reply;
    private View line_art;
    private View line_forum;
    private View line_reply;
    private TextView tv_art;
    private TextView tv_forum;
    private TextView tv_reply;
    private ImageView iv_three1;
    private ImageView iv_three2;
    //粉丝关注收藏
    private int FavNum = 0;
    private int CareNum = 0;
    private int FollowNum = 0;

    private int mTempY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent();
        setContentView(R.layout.activity_info_center);
        try {
            InfoCenterID = getIntent().getStringExtra(Const.InfoCenterID);
            if (OtherUtils.isEmpty(InfoCenterID)) {
                showToast("用户ID为空\n获取信息出错");
                throw new RuntimeException("个人中心用户ID出错");
            } else {
                //设置标题栏和状态栏
                initBar();
                //设置视图
                initView();
                //设置数据
                initData();
                //设置监听事件
                setListener();

            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "InfoCenterActivity");
        }
    }

    //设置标题栏及状态栏
    private void initBar() {
        try {
            statusBarHeight = getStatusBarHeight();
            statusBar1 = findViewById(R.id.statusBar1);
            viewColorChange = findViewById(R.id.view_color_change);

            ViewGroup.LayoutParams params1 = statusBar1.getLayoutParams();
            params1.height = statusBarHeight;
            statusBar1.setLayoutParams(params1);

            ViewGroup.LayoutParams params3 = viewColorChange.getLayoutParams();
            ch = statusBarHeight + DensityUtils.dp2px(this, 40);
            LogCustom.i("ZYS", "CH:" + ch);
            params3.height = statusBarHeight + DensityUtils.dp2px(this, 40);
            viewColorChange.setLayoutParams(params3);
            viewColorChange.setAlpha(0);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "initBar");
        }
    }


    //设置视图
    private void initView() {
        try {
            v = findViewById(R.id.whole_content);
            v_tab_top = findViewById(R.id.tab_top);
            //滑动器部分控件初始化
            layout = (PullToRefreshLayout) findViewById(R.id.pull_layout_headline);
            layout.setOnRefreshListener(this);
            sv = (UserPullableScrollView) findViewById(R.id.sv);
            sv.setOnScrollListener(this);
            //毛玻璃控件设置
            back = (ImageView) findViewById(R.id.btn_back);
            tv_blur = (TextView) findViewById(R.id.tv_blur);
            iv_blur = (ImageView) findViewById(R.id.iv_blur);
            iv_blur_title = (ImageView) findViewById(R.id.iv_blur_title);
            tv_blur_title = (TextView) findViewById(R.id.tv_blur_title);
            bc = new BlurColor(this, iv_blur, tv_blur, 1);
            bc_t = new BlurColor(this, iv_blur_title, tv_blur_title, 1);
            //基本控件设置
            civ_avatar = (CircleImageView) findViewById(R.id.civ_head_img);
            tv_day = (TextView) findViewById(R.id.tv_day);
            cb_box = (TextView) findViewById(R.id.cb_care);
            tv_name = (TextView) findViewById(R.id.tv_name);
            v_follower = findViewById(R.id.v_follower);
            v_care = findViewById(R.id.v_care);
            v_keep = findViewById(R.id.v_keep);
            tv_follower_num = (TextView) findViewById(R.id.tv_follower_num);
            tv_care_num = (TextView) findViewById(R.id.tv_care_num);
            tv_keep_num = (TextView) findViewById(R.id.tv_keep_num);
            v_big = findViewById(R.id.v_big_v);
            v_day = findViewById(R.id.v_day);
            tv_wash = (TextView) findViewById(R.id.tv_auto);
            //癌症信息
            ll_cancer_info = (LinearLayout) findViewById(R.id.ll_cancer_info);
            tv_cancer_info = (TextView) findViewById(R.id.tv_cancer_info);
            tv_cancer_info_more = (TextView) findViewById(R.id.tv_cancer_info_more);
            //数字字体设置
            if (ZYApplication.typeFace != null) {
                tv_day.setTypeface(ZYApplication.typeFace);
                tv_follower_num.setTypeface(ZYApplication.typeFace);
                tv_care_num.setTypeface(ZYApplication.typeFace);
                tv_keep_num.setTypeface(ZYApplication.typeFace);
            }
            //Tab控件设置
            v_tab = findViewById(R.id.v_tab_rl);
            mTempY = v_tab.getTop();
            LogCustom.i("ZYS", "mTempY:" + mTempY + "getH:" + v_tab.getHeight() + "getM:" + v_tab.getMeasuredHeight());
            v_art = findViewById(R.id.v_art);
            v_forum = findViewById(R.id.v_forum);
            v_reply = findViewById(R.id.v_reply);
            line_art = findViewById(R.id.line_art);
            line_forum = findViewById(R.id.line_forum);
            line_reply = findViewById(R.id.line_reply);
            tv_art = (TextView) findViewById(R.id.tv_art);
            tv_forum = (TextView) findViewById(R.id.tv_forum);
            tv_reply = (TextView) findViewById(R.id.tv_reply);
            iv_three1 = (ImageView) findViewById(R.id.iv_three_1);
            iv_three2 = (ImageView) findViewById(R.id.iv_three_2);
            v_art2 = findViewById(R.id.v_art2);
            v_forum2 = findViewById(R.id.v_forum2);
            v_reply2 = findViewById(R.id.v_reply2);
            line_art2 = findViewById(R.id.line_art2);
            line_forum2 = findViewById(R.id.line_forum2);
            line_reply2 = findViewById(R.id.line_reply2);
            tv_art2 = (TextView) findViewById(R.id.tv_art2);
            tv_forum2 = (TextView) findViewById(R.id.tv_forum2);
            tv_reply2 = (TextView) findViewById(R.id.tv_reply2);
            iv_three12 = (ImageView) findViewById(R.id.iv_three_12);
            iv_three22 = (ImageView) findViewById(R.id.iv_three_22);

            //当布局的状态或者控件的可见性发生改变回调的接口
            findViewById(R.id.parent_layout).getViewTreeObserver().
                    addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                        @Override
                        public void onGlobalLayout() {
                            //这一步很重要，使得上面的布局和下面的布局重合
                            int mScrollY = sv.getScrollY();
                            onScroll(mScrollY);
                        }
                    });

        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "InfoCenterActivity");
        }
    }

    //设置监听事件
    private void setListener() {
        try {
            back.setOnClickListener(this);//返回的点击
            civ_avatar.setOnClickListener(this);//头像的点击
            v_follower.setOnClickListener(this);//粉丝区点击
            v_care.setOnClickListener(this);//关注区点击
            v_keep.setOnClickListener(this);//收藏区点击
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "InfoCenterActivity");
        }
    }

    private ZYDialog dialog;

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
                        cb_box.setClickable(false);
                        Factory.postPhp(InfoCenterActivity.this, Const.PCareDel);
                        dialog.dismiss();
                    }
                })
                .setCancelAble(true)
                .create();
        dialog.show();
    }

    //设置数据
    private void initData() {
        Factory.postPhp(this, Const.PUserInfo);
    }

    private String headImgUrl;

    //设置病历资料
    private void setCancer_info(final InfoCenterEntity entity) {
        if (!TextUtils.isEmpty(entity.getAutoTxt())) {//认证用户不显示病历信息
            ll_cancer_info.setVisibility(View.GONE);
        } else {
            ll_cancer_info.setVisibility(View.VISIBLE);
            String stepName = entity.getStepName();//治疗阶段
            String cancer = entity.getCancer() + "，";//癌种名称
            String Peroid = entity.getPeriodID();//分期ID
            Peroid = TextUtils.isEmpty(Peroid) || "未知".equals(Peroid) ? "未知分期" : Peroid + "期";
            String discover_time = entity.getTime() + "确诊";
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
            stringBuilder.append("病人在");
            stringBuilder.append(stepName);
            stringBuilder.append("，所患癌症种为");
            stringBuilder.append(cancer);
            stringBuilder.append(Peroid);
            stringBuilder.append("，于");
            stringBuilder.append(discover_time);
            stringBuilder.setSpan(new StyleSpan(Typeface.BOLD), 3, 3 + stepName.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            stringBuilder.setSpan(new StyleSpan(Typeface.BOLD), 10 + stepName.length(), 10 + stepName.length() + cancer.length() + Peroid.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            stringBuilder.setSpan(new StyleSpan(Typeface.BOLD), 12 + stepName.length() + cancer.length() + Peroid.length(), 10 + stepName.length() + cancer.length() + Peroid.length() + discover_time.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            tv_cancer_info.setText(stringBuilder);
            tv_cancer_info_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomePageEntity homePageEntity=new HomePageEntity();
                    homePageEntity.setSkiptype("1");
                    homePageEntity.setSign_a("http://help.kaqcn.com/Api/getStepUser?InfoID="+InfoCenterID);
                    UiUtils.toMenuJump(InfoCenterActivity.this,homePageEntity,null,false,null);
               //     startActivity(new Intent(InfoCenterActivity.this,MedicalRecordActivity.class).putExtra(Contants.InfoID,"http://help.kaqcn.com/Api/getStepUser?InfoID="+InfoCenterID));
                }
            });
        }
    }

    //刷新UI
    private void bindView(InfoCenterEntity entity) {
        try {
            setCancer_info(entity);
            String name = entity.getInfoName();
            InfoCenterName = TextUtils.isEmpty(name) ? "" : name;
            tv_name.setText(InfoCenterName);

            FavNum = entity.getFavNum();
            tv_keep_num.setText(FavNum + "");
            CareNum = entity.getCareNum();
            tv_care_num.setText(CareNum + "");
            FollowNum = entity.getFollowNum();
            tv_follower_num.setText(FollowNum + "");
            String big = entity.getGroupType();
            if (!TextUtils.isEmpty(big) && "1".equals(big)) {
                v_big.setVisibility(View.VISIBLE);
                v_day.setVisibility(View.GONE);
            } else {
                tv_day.setText(entity.getDiscoverTime() + "");
                v_big.setVisibility(View.GONE);
                v_day.setVisibility(View.VISIBLE);
            }
            String auto = entity.getAutoTxt();

            if (TextUtils.isEmpty(auto)) {
                tv_wash.setText("");
            } else {
                tv_wash.setText(Html.fromHtml("<font color=\"#F5A127\">抗癌圈认证：</font>" + auto));
            }

            if (InfoCenterID.equals(UserinfoData.getInfoID(this))) {//是本人
                cb_box.setSelected(false);
                cb_box.setText("编辑资料");
            } else {
                if (Const.RESULT.equals(entity.getIsCare())) {
                    cb_box.setSelected(false);
                    cb_box.setText("＋ 关注");
                } else {
                    cb_box.setSelected(true);
                    cb_box.setText("已关注");
                }
            }

            cb_box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (InfoCenterID.equals(UserinfoData.getInfoID(InfoCenterActivity.this))) {//是本人
                        startActivity(new Intent(InfoCenterActivity.this, PersonalDataActivity.class));
                    } else {
                        if (cb_box.isSelected()) {
                            cancelFollow();
                        } else {
                            Factory.postPhp(InfoCenterActivity.this, Const.PCareAdd);
                        }
                    }
                }
            });
            int h = findViewById(R.id.v_top_s).getMeasuredHeight();
            ChangeHeight = h - DensityUtils.dpToPx(this, 60) - getStatusBarHeight();
        } catch (Exception e) {

        }

        try {
            headImgUrl = entity.getHeadUrl();
            if (!TextUtils.isEmpty(headImgUrl)) {
                Glide.with(InfoCenterActivity.this).load(headImgUrl)
                        .signature(new IntegerVersionSignature(1))
                        .error(R.mipmap.default_avatar)//这个是加载失败的
                        .into(civ_avatar);
                Glide.with(InfoCenterActivity.this).load(headImgUrl)
                        .signature(new IntegerVersionSignature(1))
                        .error(R.mipmap.default_avatar)//这个是加载失败的
                        .into(new GlideDrawableImageViewTarget(iv_blur) {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<?
                                    super GlideDrawable> animation) {
                                super.onResourceReady(resource, animation);
                                bc.applyBlur();
                            }
                        });
                Glide.with(InfoCenterActivity.this).load(headImgUrl)
                        .signature(new IntegerVersionSignature(1))
                        .error(R.mipmap.default_avatar)//这个是加载失败的
                        .into(new GlideDrawableImageViewTarget(iv_blur_title) {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<?
                                    super GlideDrawable> animation) {
                                super.onResourceReady(resource, animation);
                                bc_t.applyBlur();
                            }
                        });
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "InfoCenterActivity");
        }
        if (!TextUtils.isEmpty(entity.getHaveAct()) && "1".equals(entity.getHaveAct())) {
            haveArt = true;
        } else {
            haveArt = false;
        }
        //初始化窗口
        initFragments();
        //初始化tab条目
        initTab();
        v.setVisibility(View.VISIBLE);
    }

    private int selectedIndex;
    private int currentIndex = 0;
    private UserArticleFragment userArticleFragment;
    private UserForumFragment userForumFragment;
    private UserReleaseFragment userReleaseFragment;
    private BaseZyFragment[] fragments;

    private void initFragments() {
        try {
            userForumFragment = new UserForumFragment();
            userReleaseFragment = new UserReleaseFragment();
            userForumFragment.setInfoCenterID(InfoCenterID);
            userReleaseFragment.setInfoCenterID(InfoCenterID);
            userForumFragment.setInfoCenterName(InfoCenterName);
            userReleaseFragment.setInfoCenterName(InfoCenterName);
            if (haveArt) {
                userArticleFragment = new UserArticleFragment();
                userArticleFragment.setInfoCenterID(InfoCenterID);
                userArticleFragment.setInfoCenterName(InfoCenterName);
                fragments = new BaseZyFragment[]{userArticleFragment, userForumFragment, userReleaseFragment};
            } else {
                fragments = new BaseZyFragment[]{userForumFragment, userReleaseFragment};
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            //设置标识初始值
            currentIndex = 0;
            selectedIndex = 0;
            //初始化窗口UI
            transaction.add(R.id.info_center_fl, fragments[0]);
            transaction.show(fragments[0]);
            transaction.commit();
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "MainMoreActivity");
        }
    }

    //初始化条目
    private void initTab() {
        if (!haveArt) {
            iv_three2.setVisibility(View.GONE);
            v_forum.setVisibility(View.GONE);
            tv_art.setText("帖子");
            iv_three22.setVisibility(View.GONE);
            v_forum2.setVisibility(View.GONE);
            tv_art2.setText("帖子");
        } else {
            tv_art.setText("文章");
            tv_forum.setText("帖子");
            v_forum.setOnClickListener(this);
            iv_three2.setVisibility(View.INVISIBLE);
            line_forum.setVisibility(View.INVISIBLE);
            tv_art2.setText("文章");
            tv_forum2.setText("帖子");
            v_forum2.setOnClickListener(this);
            iv_three22.setVisibility(View.INVISIBLE);
            line_forum2.setVisibility(View.INVISIBLE);
        }
        tv_reply.setText("回复");
        v_art.setSelected(true);
        tv_art.setSelected(true);
        iv_three1.setImageResource(R.mipmap.three2);
        line_reply.setVisibility(View.INVISIBLE);
        tv_reply2.setText("回复");
        v_art2.setSelected(true);
        tv_art2.setSelected(true);
        iv_three12.setImageResource(R.mipmap.three2);
        line_reply2.setVisibility(View.INVISIBLE);

        //设置点击事件
        v_art.setOnClickListener(this);
        v_reply.setOnClickListener(this);
        v_art2.setOnClickListener(this);
        v_reply2.setOnClickListener(this);

        findViewById(R.id.tab_top2).setVisibility(View.VISIBLE);
        findViewById(R.id.v_white2).setVisibility(View.VISIBLE);
    }

    //切换条目
    private void changeToTab(int tag) {
        if (tag > fragments.length - 1) return;
        clearSelect();
        if (tag == 0) {
            v_art.setSelected(true);
            tv_art.setSelected(true);
            line_art.setVisibility(View.VISIBLE);
            iv_three1.setVisibility(View.VISIBLE);
            iv_three1.setImageResource(R.mipmap.three2);
            v_art2.setSelected(true);
            tv_art2.setSelected(true);
            line_art2.setVisibility(View.VISIBLE);
            iv_three12.setVisibility(View.VISIBLE);
            iv_three12.setImageResource(R.mipmap.three2);
        } else if (tag == 2) {
            v_reply.setSelected(true);
            tv_reply.setSelected(true);
            line_reply.setVisibility(View.VISIBLE);
            iv_three2.setVisibility(View.VISIBLE);
            iv_three2.setImageResource(R.mipmap.three1);
            v_reply2.setSelected(true);
            tv_reply2.setSelected(true);
            line_reply2.setVisibility(View.VISIBLE);
            iv_three22.setVisibility(View.VISIBLE);
            iv_three22.setImageResource(R.mipmap.three1);
        } else if (tag == 1) {
            if (haveArt) {
                v_forum.setSelected(true);
                tv_forum.setSelected(true);
                line_forum.setVisibility(View.VISIBLE);
                iv_three1.setVisibility(View.VISIBLE);
                iv_three1.setImageResource(R.mipmap.three1);
                iv_three2.setVisibility(View.VISIBLE);
                iv_three2.setImageResource(R.mipmap.three2);
                v_forum2.setSelected(true);
                tv_forum2.setSelected(true);
                line_forum2.setVisibility(View.VISIBLE);
                iv_three12.setVisibility(View.VISIBLE);
                iv_three12.setImageResource(R.mipmap.three1);
                iv_three22.setVisibility(View.VISIBLE);
                iv_three22.setImageResource(R.mipmap.three2);
            } else {
                v_reply.setSelected(true);
                tv_reply.setSelected(true);
                line_reply.setVisibility(View.VISIBLE);
                iv_three1.setVisibility(View.VISIBLE);
                iv_three1.setImageResource(R.mipmap.three1);
                v_reply2.setSelected(true);
                tv_reply2.setSelected(true);
                line_reply2.setVisibility(View.VISIBLE);
                iv_three12.setVisibility(View.VISIBLE);
                iv_three12.setImageResource(R.mipmap.three1);
            }
        }
        selectedIndex = tag;
        changeFragment();
    }

    //清除选中
    private void clearSelect() {
        v_reply.setSelected(false);
        v_art.setSelected(false);
        line_reply.setVisibility(View.INVISIBLE);
        line_art.setVisibility(View.INVISIBLE);
        iv_three1.setVisibility(View.INVISIBLE);
        v_reply2.setSelected(false);
        v_art2.setSelected(false);
        line_reply2.setVisibility(View.INVISIBLE);
        line_art2.setVisibility(View.INVISIBLE);
        iv_three12.setVisibility(View.INVISIBLE);
        if (haveArt) {
            v_forum.setSelected(false);
            line_forum.setVisibility(View.INVISIBLE);
            iv_three2.setVisibility(View.INVISIBLE);
            v_forum2.setSelected(false);
            line_forum2.setVisibility(View.INVISIBLE);
            iv_three22.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btn_back:
                    finish();
                    break;
                case R.id.v_art:
                    if (!v_art.isSelected()) {
                        changeToTab(0);
                    }
                    break;
                case R.id.v_art2:
                    if (!v_art2.isSelected()) {
                        changeToTab(0);
                    }
                    break;
                case R.id.v_forum:
                    if (haveArt && !v_forum.isSelected()) {
                        changeToTab(1);
                    }
                    break;
                case R.id.v_forum2:
                    if (haveArt && !v_forum2.isSelected()) {
                        changeToTab(1);
                    }
                    break;
                case R.id.v_reply:
                    if (!v_reply.isSelected()) {
                        if (haveArt) {
                            changeToTab(2);
                        } else {
                            changeToTab(1);
                        }
                    }
                    break;
                case R.id.v_reply2:
                    if (!v_reply2.isSelected()) {
                        if (haveArt) {
                            changeToTab(2);
                        } else {
                            changeToTab(1);
                        }
                    }
                    break;
                case R.id.v_care:
                    if (CareNum > 0) {
                        startActivity(new Intent(InfoCenterActivity.this, CareListActivity.class)
                                .putExtra(Const.InfoCenterID, InfoCenterID)
                                .putExtra(Const.CareListType, 2));
                    } else {
                        showToast("TA还没有关注");
                    }
                    break;
                case R.id.v_follower:
                    if (FollowNum > 0) {
                        startActivity(new Intent(InfoCenterActivity.this, CareListActivity.class)
                                .putExtra(Const.InfoCenterID, InfoCenterID)
                                .putExtra(Const.CareListType, 1));
                    } else {
                        showToast("TA还没有粉丝");
                    }
                    break;
                case R.id.v_keep:
                    if (FavNum > 0) {
                        startActivity(new Intent(InfoCenterActivity.this, MyFlwForumActivity.class)
                                .putExtra(Const.InfoCenterID, InfoCenterID));
                    } else {
                        showToast("TA还没有收藏");
                    }
                    break;
                case R.id.civ_head_img:
                    if (!TextUtils.isEmpty(headImgUrl)) {
                        List<String> temp = new ArrayList<>();
                        temp.add(headImgUrl);
                        startActivity(new Intent(this, ShowPhotoActivity.class).putExtra("list",
                                (Serializable) temp).putExtra("position", 0));
                    }
                    break;
                case R.id.tv_cancer_info_more:

                    break;
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "InfoCenterActivity");
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == Const.PUserInfo) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
            map.put(Contants.OtherUID, InfoCenterID);
        } else if (tag == Const.PCareAdd) {
            map.put("uid", UserinfoData.getInfoID(this));
            map.put("careuid", InfoCenterID);
        } else if (tag == Const.PCareDel) {
            map.put("uid", UserinfoData.getInfoID(this));
            map.put("careuid", InfoCenterID);
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if (flag == Const.EGetPatientDetail) {
            args = new String[]{
                    Contants.InfoID, UserinfoData.getInfoID(this)
            };
        } else if (flag == Const.EGetMyForum) {
            args = new String[]{Contants.InfoID, InfoCenterID};
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object t, int pos) {
        try {
            if (pos == Const.PUserInfo) {
                InfoCenterEntity entity = (InfoCenterEntity) t;
                if (Const.RESULT.equals(entity.getIResult())) {
                    bindView(entity);
                }
            } else if (pos == Const.PCareAdd) {
                PhpUserInfoBean bean = (PhpUserInfoBean) t;
                cb_box.setClickable(true);
                if (Const.RESULT.equals(bean.getiResult())) {
                    cb_box.setSelected(true);
                    cb_box.setText("已关注");
                    showString("关注成功");
                } else {
                    showString("关注失败");
                }
            } else if (pos == Const.PCareDel) {
                PhpUserInfoBean bean = (PhpUserInfoBean) t;
                cb_box.setClickable(true);
                if (Const.RESULT.equals(bean.getiResult())) {
                    cb_box.setSelected(false);
                    cb_box.setText("＋ 关注");
                    showString("取消关注成功");
                } else {
                    showString("取消关注失败");
                }
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "InfoCenterActivity");
        }
    }

    @Override
    public void showLoading(int tag) {
        if (tag == Const.PUserInfo) {
            findViewById(R.id.pd).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading(int tag) {
        if (tag == Const.PUserInfo) {
            findViewById(R.id.pd).setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(int tag) {
        Toast.makeText(this, "网络请求失败", Toast.LENGTH_SHORT).show();
        if (tag == Const.PUserInfo) {
            findViewById(R.id.pd).setVisibility(View.GONE);
        } else if (tag == Const.PCareAdd || tag == Const.PCareDel) {
            cb_box.setClickable(true);
        }
    }

    @Override
    public void setBlur(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= 16) {
            tv_blur.setBackground(new BitmapDrawable(this.getResources(), bitmap));
        }
    }

    @Override
    public void onScroll(int scrollY) {
//        LogCustom.i("ZYS","sy:"+scrollY+",top:"+v_tab.getTop());
        if (scrollY + ch <= v_tab.getTop()) {
            if (v_tab_top.getVisibility() != View.GONE) {
                v_tab_top.setVisibility(View.GONE);
            }
        } else {
            if (v_tab_top.getVisibility() != View.VISIBLE) {
                v_tab_top.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onScrollChange(int x, int y, int oldx, int oldy) {
        if (y >= ChangeHeight) {
            viewColorChange.setAlpha(1);
        } else {
            viewColorChange.setAlpha((float) y / (float) ChangeHeight);
        }
    }

    @Override
    public void onScrollBottom(boolean y) {
        try {
            if (!haveArt && currentIndex == 0 && userForumFragment != null) {
                userForumFragment.loadingMore();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        try {
            if (currentIndex > fragments.length - 1 || currentIndex < 0) {
                overLoading(2, true);
            } else {
                fragments[currentIndex].loadingMore();
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "onLoadMore Error");
        }
    }

    public void overLoading(int tag, boolean fit) {
        layout.loadmoreFinish(tag, fit, true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    //文章 贴子 回复切换
    private void changeFragment() {
        if (selectedIndex != currentIndex) {
            // 不是当前的
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            // 当前hide
            transaction.hide(fragments[currentIndex]);
            // show你选中

            if (!fragments[selectedIndex].isAdded()) {
                // 以前没添加过
                transaction.add(R.id.info_center_fl,
                        fragments[selectedIndex]);
            }
            // 事务
            transaction.show(fragments[selectedIndex]);
            transaction.commit();
            currentIndex = selectedIndex;
        }
    }

    private void showString(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

}

package com.zeyuan.kyq.fragment.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.bumptech.glide.Glide;
import com.meelive.ingkee.sdk.plugin.entity.UserInfo;
import com.zeyuan.kyq.Entity.ArticleTypeEntity;
import com.zeyuan.kyq.Entity.HomePageBean;
import com.zeyuan.kyq.Entity.HomePageEntity;
import com.zeyuan.kyq.Entity.InformationEntity;
import com.zeyuan.kyq.Entity.UserInformationEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.BannerPagerAdapter;
import com.zeyuan.kyq.adapter.HomeHelpRecyclerAdapter;
import com.zeyuan.kyq.adapter.HomeTabRecyclerAdapter;
import com.zeyuan.kyq.app.BaseZyFragment;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.MainPageInfoBean;
import com.zeyuan.kyq.bean.PatientDetailBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.FragmentCallBack;
import com.zeyuan.kyq.biz.manager.ClickStatisticsManager;
import com.zeyuan.kyq.biz.manager.IntegrationManager;
import com.zeyuan.kyq.filedownloader.JFileDownloadListener;
import com.zeyuan.kyq.filedownloader.JFileDownloader;
import com.zeyuan.kyq.fragment.EditTabFragment;
import com.zeyuan.kyq.fragment.dialog.UpdateDialog;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.service.ZYKaqService;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.AllMenuActivity;
import com.zeyuan.kyq.view.ArticleTypeActivity;
import com.zeyuan.kyq.view.CommentProjectActivity;
import com.zeyuan.kyq.view.HeadlineHomeActivity;
import com.zeyuan.kyq.view.HomeSymptomActivity;
import com.zeyuan.kyq.view.InfoCenterActivity;
import com.zeyuan.kyq.view.MainActivity;
import com.zeyuan.kyq.view.NewsCenterActivity;
import com.zeyuan.kyq.view.SearchDrugActivity;
import com.zeyuan.kyq.view.ShowDiscuzActivity;
import com.zeyuan.kyq.view.SimilarActivity;
import com.zeyuan.kyq.widget.CircleImageView;
import com.zeyuan.kyq.widget.CustomView.InsideRecyclerView;
import com.zeyuan.kyq.widget.DrawCircleView;
import com.zeyuan.kyq.widget.IntegrationPopupWindow;

import java.io.File;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2016/12/26.
 * <p>
 * 新的主页fragment
 *
 * @author wwei
 */
public class HomeFragment extends BaseZyFragment implements HomeTabRecyclerAdapter.OnItemClickListener, HttpResponseInterface
        , ViewPager.OnPageChangeListener, FragmentCallBack{

    //高度变化区域最小值
//    private int MAX_CHANGE;
    //高度变化区域最大值
//    private int MIN_CHANGE;

    private int page;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_new, container, false);
        try {
            ZYApplication.homeMoveFlag = true;
            page = 0;
            ZYApplication.homeHelpPageFlag = true;
            //初始化控件
            initView();
            //设置监听事件
            setListener();
            //初始化数据
            initData();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "HomeFragment onCreateView");
        }
        return rootView;
    }

    //导航栏控件
    private InsideRecyclerView rv_top;
    //加号控件
    private ImageView add;
    //导航栏适配器
    private HomeTabRecyclerAdapter adapter_top;
    //导航栏数据
    private List<ArticleTypeEntity> list_top;
    private LinearLayoutManager manager;
    //屏幕宽度像素
    private int mWidth;
    //消息点击控件
    private View v_news_num;
    //消息数量显示
    private TextView tv_news_num;
    //高度变化区域
//    private View v_change;
    //顶部banner
    private ViewPager mVp;
    private BannerPagerAdapter mAdapter;
    private DrawCircleView mDcv;

    //积分总数
    private TextView tv_integration_sum;

    //滑动器
    private XRefreshView xv;
    //文章列表控件
    private RecyclerView rv;
    private View headerView;
    //列表适配器
    private HomeHelpRecyclerAdapter adapter;
    //列表数据
    private List<InformationEntity> data;

    private FragmentCallBack callback;

    private List<HomePageEntity> banners;
    private void initView() {
        try {
            DisplayMetrics metric = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
            mWidth = metric.widthPixels; // 屏幕宽度（像素）
            v_news_num = findViewById(R.id.v_news_num_home);
            xv = (XRefreshView) findViewById(R.id.xrv);
            rv = (RecyclerView) findViewById(R.id.rv);
            LinearLayoutManager manager1 = new LinearLayoutManager(context);
            manager1.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(manager1);
            rv.setHasFixedSize(true);
            data = new ArrayList<>();
            if (banners == null || banners.size() == 0) banners = new ArrayList<>();
            adapter = new HomeHelpRecyclerAdapter(context, data, rv, banners);
            headerView = adapter.setHeaderView(R.layout.head_home_fragment, rv);
            initHeaderView();
            rv.setAdapter(adapter);
            //滑动控件设置
            xv.setPinnedTime(1000);
            xv.setPullLoadEnable(true);
            xv.setMoveForHorizontal(true);
            adapter.setCustomLoadMoreView(new XRefreshViewFooter(context));
            xv.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
                @Override
                public void onRefresh() {
                    refresh = true;
                    page = 0;
                    setData();
                }
                @Override
                public void onLoadMore(boolean isSilence) {
                    loading = true;
                    page++;
                    setData();
                }
            });
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, context, "initView");
        }

    }

    private FrameLayout fl_add;
    private CircleImageView head_civ;
    private TextView[] menuTvs;
    private CircleImageView[] menuCivs;
    private View[] menuVs;
    private TextView tv_head_1,tv_head_2;
    private void initHeaderView() {
        if (headerView == null) return;
        //设置控件
        add = (ImageView) headerView.findViewById(R.id.add);
        rv_top = (InsideRecyclerView) headerView.findViewById(R.id.rv_top);

        tv_news_num = (TextView) findViewById(R.id.tv_news_num_home);
        tv_integration_sum = (TextView) headerView.findViewById(R.id.tv_integration_sum);
        //布局设置
        manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_top.setLayoutManager(manager);
        //适配器设置
        list_top = new ArrayList<>();
        adapter_top = new HomeTabRecyclerAdapter(context, list_top, this, 0);
        rv_top.setAdapter(adapter_top);
        mVp = (ViewPager)headerView.findViewById(R.id.vp_top_banner);
        mDcv = (DrawCircleView)headerView.findViewById(R.id.dcv_top_banner);
        head_civ = (CircleImageView)headerView.findViewById(R.id.civ);
        fl_add = (FrameLayout) headerView.findViewById(R.id.fl_add);
        if ("0".equals(UserinfoData.getIsHaveStep(context))) {
            initUserTypeView();
        }
        tv_head_1 = (TextView) headerView.findViewById(R.id.tv_head_1);
        tv_head_2 = (TextView) headerView.findViewById(R.id.tv_head_2);

        menuTvs = new TextView[7];
        menuTvs[0] = (TextView) headerView.findViewById(R.id.menu_tv_1);
        menuTvs[1] = (TextView) headerView.findViewById(R.id.menu_tv_2);
        menuTvs[2] = (TextView) headerView.findViewById(R.id.menu_tv_3);
        menuTvs[3] = (TextView) headerView.findViewById(R.id.menu_tv_4);
        menuTvs[4] = (TextView) headerView.findViewById(R.id.menu_tv_9);
        menuTvs[5] = (TextView) headerView.findViewById(R.id.menu_tv_10);
        menuTvs[6] = (TextView) headerView.findViewById(R.id.menu_tv_11);
        menuCivs = new CircleImageView[7];
        menuCivs[0] = (CircleImageView) headerView.findViewById(R.id.civ_menu_1);
        menuCivs[1] = (CircleImageView) headerView.findViewById(R.id.civ_menu_2);
        menuCivs[2] = (CircleImageView) headerView.findViewById(R.id.civ_menu_3);
        menuCivs[3] = (CircleImageView) headerView.findViewById(R.id.civ_menu_4);
        menuCivs[4] = (CircleImageView) headerView.findViewById(R.id.civ_menu_9);
        menuCivs[5] = (CircleImageView) headerView.findViewById(R.id.civ_menu_10);
        menuCivs[6] = (CircleImageView) headerView.findViewById(R.id.civ_menu_11);
        menuVs = new View[7];
        menuVs[0] = headerView.findViewById(R.id.v_menu_1);
        menuVs[1] = headerView.findViewById(R.id.v_menu_2);
        menuVs[2] = headerView.findViewById(R.id.v_menu_3);
        menuVs[3] = headerView.findViewById(R.id.v_menu_4);
        menuVs[4] = headerView.findViewById(R.id.v_menu_9);
        menuVs[5] = headerView.findViewById(R.id.v_menu_10);
        menuVs[6] = headerView.findViewById(R.id.v_menu_11);
    }

    private View userTypeView;
    private void initUserTypeView(){
        userTypeView = LayoutInflater.from(context).inflate(R.layout.view_diy_recommend, fl_add, false);
        if (userTypeView==null) return;
        fl_add.removeAllViews();
        fl_add.addView(userTypeView);
        userTypeView.findViewById(R.id.v_recommend_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl_add.removeAllViews();
                adapter.notifyDataSetChanged();
            }
        });
        userTypeView.findViewById(R.id.v_recommend_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiUtils.startIndividuationTreatment(getActivity());
                // createInfoStepFragment();
            }
        });
    }


    private void setListener() {
        try {
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEditTab();
                }
            });
            v_news_num.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, NewsCenterActivity.class));
                }
            });
            head_civ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, InfoCenterActivity.class)
                            .putExtra(Const.InfoCenterID, UserinfoData.getInfoID(context)));
                }
            });
            headerView.findViewById(R.id.v_menu_12).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, AllMenuActivity.class));
                }
            });
            headerView.findViewById(R.id.v_menu_8).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, SimilarActivity.class));
                }
            });
            headerView.findViewById(R.id.v_menu_7).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UiUtils.toIfJump(6, context, HomeFragment.this, getActivity());
                }
            });
            headerView.findViewById(R.id.v_menu_6).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, HomeSymptomActivity.class));
                }
            });
            headerView.findViewById(R.id.v_menu_5).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, SearchDrugActivity.class));
                }
            });
            headerView.findViewById(R.id.v_headline).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (art_list!=null&&art_list.size()>0){
                        context.startActivity(new Intent(context, HeadlineHomeActivity.class)
                                .putExtra("Headline_List", (Serializable) art_list));
                    }
                }
            });
            headerView.findViewById(R.id.menu_main_3_1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toMenu("http://www.kaqcn.com/act/tropho_fx");
                }
            });
            headerView.findViewById(R.id.menu_main_3_2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomePageEntity mEntity = new HomePageEntity();
                    mEntity.setId("11");
                    mEntity.setName("怎么吃");
                    mEntity.setSign_a("31");
                    mEntity.setSign_b("1");
                    mEntity.setSkiptype("11");
                    context.startActivity(new Intent(context, ArticleTypeActivity.class)
                            .putExtra(Const.INTENT_ARTICLE_TYPE_ENTITY, mEntity));

                }
            });
            headerView.findViewById(R.id.menu_main_3_3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toMenu("https://h5.youzan.com/v2/goods/361m5pz4nd47q?reft=1512370759106&spm=f47100560");
                }
            });
            headerView.findViewById(R.id.v_menu_big_1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, CommentProjectActivity.class)
                            .putExtra("Comment_Project_Type",1)
                    .putExtra("Comment_Project_Type_PT",1));
                }
            });
            headerView.findViewById(R.id.v_menu_big_2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, CommentProjectActivity.class)
                            .putExtra("Comment_Project_Type",1)
                            .putExtra("Comment_Project_Type_PT",2));
                }
            });

        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, context, "initView");
        }
    }

    private void toMenu(String url){
        if (!TextUtils.isEmpty(url)) {
            //加上登录信息
            if (url.contains("?")) {
                url += "&kaq=" + UiUtils.getRandomMath()
                        + UserinfoData.getInfoID(context) + "&lt=2&Type=2";
            } else {
                url += "?kaq=" + UiUtils.getRandomMath()
                        + UserinfoData.getInfoID(context) + "&lt=2&Type=2";
            }
            context.startActivity(new Intent(context, ShowDiscuzActivity.class)
                    .putExtra(Const.SHOW_HTML_MAIN_TOP, url));
        }
    }

    //设置数据
    private void initData() {
        Factory.post(this, Const.EGetPatientDetail);
    }

    //设置banner数据和迭代数据
    private void getMainData() {
        Factory.post(this, Const.EGetMainPage);
    }

    private boolean postFlag = false;//防止多次post
    //    private List<HomePageEntity> banners;
    private List<HomePageEntity> bannerEntities;

    private void bindView(MainPageInfoBean bean) {
        try {
            //主页横幅
            try {
                banners = bean.getFbannerList();//头条banner数据
                bannerEntities = bean.getBannerList();
                if (bannerEntities == null) bannerEntities = new ArrayList<>();
                mAdapter = new BannerPagerAdapter(context, bannerEntities);
                mVp.setAdapter(mAdapter);
                mDcv.setDrawCricle(bannerEntities.size(), 6, Color.parseColor("#4c4c4c"), Color.parseColor("#FFFFFF"));
                mDcv.redraw(0);
                mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        bannerIndex = position;
                        mDcv.redraw(position % bannerEntities.size());
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
                if (!postFlag) {
                    mHandler.post(bannerRun);
                    postFlag = true;
                }


                //广告，每24小时弹出一次
                final List<HomePageEntity> tagBannerList = bean.getTagBannerList();
                if (tagBannerList != null && tagBannerList.size() > 0) {
                    if (UserinfoData.compareHomeTime(context)) {
                        ((MainActivity) context).showHomeBanner(tagBannerList);
                        UserinfoData.saveHomeRemindTime(context, System.currentTimeMillis() + "");
                    }
                }
                //弹出积分信息
                String jfString = "";
                if (bean.getJf() > 0) {
                    jfString = "登录成功 +" + bean.getJf() + "积分";
                }
                if (bean.getLoginInfo().equals("0")) {
                    if (bean.getGzNum() > 0) {
                        jfString = jfString + "\n昨日被关注 +" + bean.getGzJf() + "×" + bean.getGzNum() + "积分";
                    }
                    if (bean.getScNum() > 0) {
                        jfString = jfString + "\n昨日被收藏 +" + bean.getScJf() + "×" + bean.getScNum() + "积分";
                    }
                    if (bean.getJhNum() > 0) {
                        jfString = jfString + "\n帖子加精华+" + bean.getJhJf() + "x" + bean.getJhNum() + "积分";
                    }
                }
                if (!TextUtils.isEmpty(jfString)) {
                    IntegrationPopupWindow integrationPopupWindow = new IntegrationPopupWindow(getContext(), jfString);
                    integrationPopupWindow.delayShowPopupWindow(2000, getActivity());
                }

                IntegrationManager.getInstance().addOnIntegrationChangedListener(new IntegrationManager.OnIntegrationChangedListener() {
                    @Override
                    public void onIntegrationChanged(String sum) {
                        tv_integration_sum.setText("" + sum + "积分");
                    }
                });
                IntegrationManager.getInstance().setSum(bean.getIntegral());
                tv_integration_sum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HomePageEntity homePageEntity = new HomePageEntity();
                        homePageEntity.setSkiptype("1");
                        homePageEntity.setSign_a("http://help.kaqcn.com/Api/getUserIntegral?InfoID=" + UserinfoData.getInfoID(context));
                        UiUtils.toMenuJump(context, homePageEntity, null, false, null);
                    }
                });
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "toEvent Click");
            }

            //版本迭代
            MainPageInfoBean.UpEntity upEntity = bean.getUp();
            initUpDateType(upEntity);

            //加载快速入口数据
            Factory.postPhp(this, Const.PApi_App_shortcut);
            //加载文章数据
            Factory.postPhp(this, Const.PHomeArticleInfo);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, getActivity(), "MainFragment,bindView");
        }
    }

    private void setData() {
        Factory.postPhp(this, Const.PHomeArticleInfo);
    }
    private List<ArticleTypeEntity> art_list;
    private List<ArticleTypeEntity> showCatMain;
    private List<ArticleTypeEntity> hideCatMain;
    private int articleIndex = 0;
    //    private ArrayList<Fragment> fragments;
    //推荐列表窗口设置
    HomeHelpFragment hFragment;

    //设置文章数据
    private void initHomeArticle(UserInformationEntity entity) {
        try {
            //数据获取及为空判断
            if (entity == null) return;
            List<ArticleTypeEntity> cat = entity.getCatShowList();
            showCatMain = new ArrayList<>();
            showCatMain = cat;
            hideCatMain = new ArrayList<>();
            hideCatMain = entity.getCatHideList();
            if (cat == null || cat.size() == 0) return;

            art_list = new ArrayList<>();
            ArticleTypeEntity mEntity = new ArticleTypeEntity();
            mEntity.setCatid(0);
            mEntity.setCatname("推荐");
            art_list.add(mEntity);
            //文章导航栏数据添加
            if (cat.size() > 0) {
                for (int i = 0; i < cat.size(); i++) {
                    art_list.add(cat.get(i));
                }
            }
            //文章导航栏视图设置
            adapter_top.update(art_list);
            //设置文章列表视图
            articleIndex = 0;
            List<InformationEntity> temp = entity.getData();
            if (temp!=null&&temp.size()>1){
                String title1 = temp.get(0).getTitle();
                tv_head_1.setText(TextUtils.isEmpty(title1)?"":title1);
                title1 = temp.get(1).getTitle();
                tv_head_2.setText(TextUtils.isEmpty(title1)?"":title1);
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, context, "initHomeArticle");
        }
    }

    //主页刷新按钮点击时执行
    public void toRefresh() {
        try {
//            ((BaseHomeNoDestroyFragment) fragments.get(articleIndex)).toRefresh();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "toRefresh");
        }
    }

    //导航栏目编辑
    private void showEditTab() {
        try {
            EditTabFragment eFragment = EditTabFragment.getInstance(this);
            eFragment.setShowCat(showCatMain);
            eFragment.setHideCat(hideCatMain);
            eFragment.show(getActivity().getSupportFragmentManager(), EditTabFragment.type);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "showEditTab");
        }
    }

    //修正用户头像
    private void correctHeadImgUrl(String url) {
        try {
            if (!TextUtils.isEmpty(url) && url.contains(Const.IMG_URL_HEAD)) return;
            String temp = null;
            if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
                temp = url;
            } else {
                if (!TextUtils.isEmpty(UserinfoData.getTempHeadImgUrl(context))) {
                    temp = UserinfoData.getTempHeadImgUrl(context);
                }
            }
            if (!TextUtils.isEmpty(temp)) {
                try {
                    LogCustom.i(Const.TAG.ZY_OTHER, "用户图片不正常");
                    Intent intent = new Intent(getActivity(), ZYKaqService.class);
                    intent.setAction(Const.SERVICE_CORRECT_HEAD_IMG);
                    intent.setPackage(getActivity().getPackageName());
                    intent.putExtra(Contants.Headimgurl, temp);
                    getActivity().startService(intent);
                } catch (Exception e) {
                    ExceptionUtils.ExceptionToUM(e, getActivity(), "启动图片修正服务出错");
                }
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, context, "图片修正判断出错");
        }
    }

    private String downurl = "";
    private String updateMessage = "";
    private String versionNum = "";
    private String updateType = "";

    //根据updateType判断下一步操作
    private void initUpDateType(MainPageInfoBean.UpEntity upEntity) {
        if (upEntity != null) {
            downurl = upEntity.getL();//**新版本的下载地址*/
            updateMessage = upEntity.getM();//**新版本的提示内容*/
            updateType = upEntity.getU();//**新版本的更新方式*/
            versionNum = upEntity.getV();//**新版本的版本号*/
        }
        try {
            if ("3".equals(updateType)) {

            } else {
                //  initUpdateVersion();
                showUpdateDialog();
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, getActivity(), "initUpDateType()");
        }
    }


    //初始化版本迭代界面
    private void initUpdateVersion() {
        Dialog mdialog;
        ZYDialog.Builder builder = new ZYDialog.Builder(getActivity());
        if ("1".equals(updateType)) {
            builder.setTitle("当前最新版本:" + versionNum)
                    .setMessage(
                            "更新内容：\n" +
                                    "\n" +
                                    updateMessage +
                                    "\n")
                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            updateVersion();
                        }
                    })
                    .disMissLine(true)
                    .setCancelAble(false);
        } else {
            builder.setTitle("当前最新版本:" + versionNum)
                    .setMessage("\n" +
                            "更新内容：\n" +
                            "\n" +
                            updateMessage +
                            "\n")
                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            updateVersion();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setCancelAble(false);
        }
        mdialog = builder.create();
        mdialog.show();
    }

    //显示更新提示对话框
    private void showUpdateDialog() {
        UpdateDialog.Builder builder = new UpdateDialog.Builder(getActivity());
        if ("1".equals(updateType)) {//强制更新
            builder.setContent("Hi 抗癌圈的小伙伴们\n" + "经过一段时间的内测，抗癌圈" + versionNum + "版本终于上线啦\n" +
                    "\n主要的更新有：\n" +
                    "\n" +
                    updateMessage +
                    "\n").setCancelAble(false).setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    updateVersion();
                }
            });
        } else {
            builder.setContent("Hi 抗癌圈的小伙伴们\n" + "经过一段时间的内测，抗癌圈" + versionNum + "版本终于上线啦\n" +
                    "\n主要的更新有：\n" +
                    "\n" +
                    updateMessage +
                    "\n").setCancelAble(true).setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    updateVersion();
                }
            });
        }
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private File file;
    private static final String KAQ_DISK_PATH = Environment.getExternalStorageDirectory().getPath() + "/kaq/APK/";

    //下载功能模块
    private void updateVersion() {
        String apkname = downurl.substring(downurl.lastIndexOf("/") + 1);//文件名
        file = new File(KAQ_DISK_PATH + apkname);
        //判断父目录是否存在
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        //**文件是否已存在，如果存在则转跳安装方法，不存在则去下载*/
        if (file.exists()) {
            updataApp();
        } else {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setTitle("当前最新版本:" + versionNum);
            dialog.setMessage("\n" +
                    "更新内容：\n" +
                    "\n" +
                    updateMessage +
                    "\n" +
                    "\n" +
                    "正在下载：");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(100);
            //**判断是否问强制更新*/
            if ("1".equals(updateType)) {
                dialog.setCancelable(false);
            } else {
                dialog.setButton(ProgressDialog.BUTTON_POSITIVE, "后台更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.setCanceledOnTouchOutside(false);
            }
            dialog.show();
            //**下载器设置*/
            JFileDownloader downloader = new JFileDownloader(downurl, file.getAbsolutePath());
            downloader.setFileDownloadListener(new JFileDownloadListener() {
                @Override
                public void downloadProgress(int progress, double speed, long remainTime) {
                    if (dialog.isShowing()) {
                        dialog.setProgress(progress);
                    }
                }

                @Override
                public void downloadCompleted(File file, long downloadTime) {
                    dialog.cancel();
                    mHandler.sendEmptyMessage(UPDATA_VERSION_SUCCESS);
                }
            });
            //**启动下载线程*/
            new DownloaderThread(downloader).start();
        }
    }

    //App安装程序
    private void updataApp() {
        String apkname = downurl.substring(downurl.lastIndexOf("/") + 1);
        file = new File(KAQ_DISK_PATH + apkname);
        if (file.exists()) {
            Intent install = new Intent();
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setAction(android.content.Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            startActivity(install);
        }
    }

    //版本迭代线程内部类
    class DownloaderThread extends Thread {
        private JFileDownloader downloader;

        public DownloaderThread(JFileDownloader downloader) {
            this.downloader = downloader;
        }

        @Override
        public void run() {
            try {
                downloader.startDownload();
            } catch (Exception e) {
                LogCustom.i(Const.TAG.ZY_OTHER, "下载出错");
                e.printStackTrace();
            }
        }
    }

    //导航栏点击
    @Override
    public void OnRecyclerItemClick(View v, int position, String typeID) {
        try {
            if (!"9999".equals(typeID)) {
                if (position==0){
                    adapter.setFlag(0);
                    if ("0".equals(UserinfoData.getIsHaveStep(context))) {
                        if (userTypeView!=null){
                            fl_add.removeAllViews();
                            fl_add.addView(userTypeView);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    if ("0".equals(UserinfoData.getIsHaveStep(context))) {
                        if (userTypeView!=null){
                            fl_add.removeAllViews();
                            adapter.notifyDataSetChanged();
                        }
                    }
                    adapter.setFlag(1);
                }
                articleIndex = position;
                page = 0;
                setData();
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "OnRecyclerItemClick");
        }
    }

    private static final int pageSize = 20;

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(context));
        if (tag == Const.PHomeArticleInfo) {
            if (art_list==null||art_list.size()==0){
                map.put(Contants.CatID, "0");
            } else {
                map.put(Contants.CatID, art_list.get(articleIndex).getCatid()+"");
            }
            map.put(Contants.Page, page + "");
            map.put(Contants.PageSize, pageSize + "");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = new String[]{};
        if (flag == Const.EGetMainPage) {//获取主页详情
            String cityID = UserinfoData.getCityID(context);
            String provinceID;
            if ("0".equals(cityID)) {
                provinceID = "0";
                UserinfoData.saveProvinceID(context, provinceID);
                UserinfoData.saveCityID(context, cityID);
            } else {
                provinceID = cityID.substring(0, 2) + "0000";
                UserinfoData.saveProvinceID(context, provinceID);
            }
            if (Const.NO_STEP.equals(UserinfoData.getIsHaveStep(getActivity()))) {
                args = new String[]{
                        Contants.InfoID, UserinfoData.getInfoID(context),
                        Contants.CityID, cityID,
                        Contants.ProvinceID, provinceID,
                        Contants.CancerID, UserinfoData.getCancerID(context),
                        "v", ZYApplication.versionNum,
                        "t", "2",//1是ios 2是android
                        Const.ISHAVESTEP, "0"
                };
            } else {
                args = new String[]{
                        Contants.InfoID, UserinfoData.getInfoID(context),
                        Contants.StepID, UserinfoData.getStepID(context),
                        Contants.CityID, cityID,
                        Contants.ProvinceID, provinceID,
                        Contants.CancerID, UserinfoData.getCancerID(context),
                        Contants.CureConfID, MapDataUtils.getAllCureconfID(UserinfoData.getStepID(context)),
                        "v", ZYApplication.versionNum,
                        "t", "2",//1是ios 2是android
                        Const.ISHAVESTEP, "1"
                };
            }

        } else if (flag == Const.EGetPatientDetail) {
            args = new String[]{
                    Contants.InfoID, UserinfoData.getInfoID(context)
            };
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.EGetPatientDetail) {//患者详情
            PatientDetailBean patientDetailBean = (PatientDetailBean) response;
            if (Contants.RESULT.equals(patientDetailBean.iResult)) {
                //保存用户最新信息
                UserinfoData.saveUserData(context, patientDetailBean);
                //图像修正，可以不调用
                correctHeadImgUrl(patientDetailBean.getHeadimgurl());
                //设置用户呢称和抗癌天数
                ((TextView) findViewById(R.id.tv_name)).setText(UserinfoData.getInfoname(context));
                TextView time = (TextView) findViewById(R.id.tv_discover_time);
                time.setTypeface(ZYApplication.typeFace);
                time.setText(Html.fromHtml("<font><b>成功抗癌："
                        + DataUtils.getDayForStepDetail(UserinfoData.getDiscoverTime(context), 0) + "天</b></font>"));
                if (!TextUtils.isEmpty(UserinfoData.getAvatarUrl(context))) {
                    Glide.with(context).load(UserinfoData.getAvatarUrl(context)).error(R.mipmap.default_avatar)
                            .into(head_civ);
                }
                //设置映客所需用户数据
                ZYApplication.YK_UserInfo = new UserInfo(UserinfoData.getInfoID(context), "kaq", UserinfoData.getInfoname(context),
                        "1".equals(patientDetailBean.getSex()) ? 1 : 0, UserinfoData.getAvatarUrl(context));
                //获取banner和迭代数据
                getMainData();
            } else {
                showError(flag);
            }
        } else if (flag == Const.EGetMainPage) {
            MainPageInfoBean infoBean = (MainPageInfoBean) response;
            if (Contants.RESULT.equals(infoBean.iResult)) {
                if (!TextUtils.isEmpty(infoBean.getMobile())) {
                    ZYApplication.isBind = true;
                    ZYApplication.PhoneNum = infoBean.getMobile();
                    UserinfoData.savePhoneNum(getActivity().getApplicationContext(), infoBean.getMobile());
                }
                bindView(infoBean);
                adapter.notifyDataSetChanged();
            }

        } else if (flag == Const.PHomeArticleInfo) {
            UserInformationEntity entity = (UserInformationEntity) response;
            try {
                if (Const.RESULT.equals(entity.getiResult())) {
                    if (articleIndex==0&&page==0){
                        initHomeArticle(entity);
                    }
                    if (page == 0) {
                        data = new ArrayList<>();
                        if (articleIndex==0){
                            adapter.setHelp(entity.getHelp());
                            adapter.updateBanners(banners);
                        } else {
//                            adapter.setHelp(null);
                        }
                    }
                    List<InformationEntity> temp = entity.getData();
                    if (temp != null && temp.size() != 0) {
                        data.addAll(temp);
                        if (art_list==null||art_list.size()==0){
                            adapter.update(data, 0);
                        } else {
                            adapter.update(data, art_list.get(articleIndex).getCattype());
                        }
                        overLoading(0);
                    } else {
                        overLoading(2);
                    }
                } else {
                    overLoading(1);
                }

            } catch (Exception e) {
                overLoading(1);
                ExceptionUtils.ExceptionSend(e, "toActivity PHomeArticleInfo");
            }

        } else if (flag == Const.PApi_App_shortcut) {
            int h = 0;
            HomePageBean bean = (HomePageBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                List<HomePageEntity> mHomeList = bean.getData();
                if (mHomeList != null && mHomeList.size() > 0) {
                    List<HomePageEntity> temp = new ArrayList<>();
                    if (mHomeList.size() > 7) {

                        for (int i = 0; i < 7; i++) {
                            final HomePageEntity entity = mHomeList.get(i);
                            String titleName = mHomeList.get(i).getName();
                            Glide.with(context).load(mHomeList.get(i).getPic_oss()).error(R.mipmap.loading_fail).into(menuCivs[i]);
                            menuTvs[i].setText(TextUtils.isEmpty(titleName)?"未知栏目":titleName);
                            menuVs[i].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    UiUtils.toMenuJump(context, entity, HomeFragment.this, true, getActivity());
                                    ClickStatisticsManager.getInstance().addClickEvent(Const.CLICK_EVENT_2, entity.getId());
                                }
                            });
                        }
                    }
                }
            } else {
                Toast.makeText(context, "菜单数据加载失败", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private boolean refresh = false;
    private boolean loading = false;

    private void overLoading(int tag) {
        if (tag == 0) {
            if (refresh) {
                xv.stopRefresh();
                xv.setLoadComplete(false);
            }
            if (loading) {
                xv.stopLoadMore();
            }
        } else if (tag == 1) {
            if (refresh) {
                xv.stopRefresh();
            }
            if (loading) {
                page--;
                xv.stopLoadMore();
            }
        } else if (tag == 2) {
            if (refresh) {
                xv.stopRefresh();
            }
            if (loading) {
                page--;
                xv.setLoadComplete(true);
            }
        }
        if (refresh) refresh = false;
        if (loading) loading = false;
    }

    @Override
    public void showLoading(int flag) {
        if (flag == Const.EGetMainPage) {
            findViewById(R.id.pd).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading(int flag) {
        if (flag == Const.EGetMainPage) {
            findViewById(R.id.pd).setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(int flag) {
        if (flag == Const.EGetPatientDetail) {
            findViewById(R.id.pd).setVisibility(View.GONE);
            Toast.makeText(context, "获取用户信息失败", Toast.LENGTH_LONG).show();
        } else if (flag == Const.PHomeArticleInfo) {
            Toast.makeText(context, "文章数据加载失败", Toast.LENGTH_SHORT).show();
            overLoading(2);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 9 && resultCode == -1) {
            if (hFragment != null && hFragment.isAdded()) {
                hFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    //设置消息UI
    public void setNewsView(int num) {
        if (tv_news_num == null) return;
        if (num == 0 || num < 0) {
            tv_news_num.setVisibility(View.GONE);
        } else {
            tv_news_num.setVisibility(View.VISIBLE);
            tv_news_num.setText(num + "");
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        try {
            articleIndex = position;
            adapter_top.update(articleIndex);
            manager.scrollToPositionWithOffset(articleIndex, getOffset(position));
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "HomeFragment onPageSelected");
        }
    }

    @Override
    public void dataCallBack(String str, int flag, String tag, Object obj) {
        if (flag == Const.FRAGMENT_EDIT_TAB) {
            //加载文章数据
            articleIndex = 0;
            page = 0;
            adapter.setFlag(0);
            Factory.postPhp(this, Const.PHomeArticleInfo);
        } else if (flag == Const.FRAGMENT_INFO_STEP) {
            //用户没有阶段变成有阶段之后的操作
            fl_add.removeAllViews();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



    private void setBannerChange() {
        mVp.setCurrentItem(bannerIndex);
    }

    private int bannerIndex = -1;
    private static final int CHANGE_BANNER = 121;
    private Runnable bannerRun = new Runnable() {
        @Override
        public void run() {
            try {
                if (ZYApplication.mainPageFlag) {
                    if (ZYApplication.homeHelpPageFlag && ZYApplication.homeMoveFlag) {
                        bannerIndex++;
                        mHandler.sendEmptyMessage(CHANGE_BANNER);
                    }
                    mHandler.postDelayed(bannerRun, 5000);
                }
            } catch (Exception e) {
                ExceptionUtils.ExceptionToUM(e, context, "HomeFragment bannerRun");
            }
        }
    };

    private static final int UPDATA_VERSION_SUCCESS = 123;
    private static final int CHANGE_VIEW_FLAG = 120;
    private static final int CHANGE_SIMALAR_TITLE = 124;
    private static final int CHANGE_HEADLINE_TITLE = 125;
    private static final int CHANGE_TOP_PAGE = 126;
    private static final int CHANGE_MIDDLE_PAGE = 122;
    private final MyHandler mHandler = new MyHandler(this);

    //Handler静态内部类
    private static class MyHandler extends Handler {
        private final WeakReference<HomeFragment> mFragment;

        public MyHandler(HomeFragment fragment) {
            mFragment = new WeakReference<>(fragment);
        }

        private HomeFragment fragment;

        @Override
        public void handleMessage(Message msg) {
            fragment = mFragment.get();
            if (msg.what == UPDATA_VERSION_SUCCESS) {
                fragment.updataApp();
            } else if (msg.what == CHANGE_BANNER) {
                fragment.setBannerChange();
            } else if (msg.what == CHANGE_VIEW_FLAG) {
//                fragment.changeView();
            }
        }
    }

    private int getOffset(int position) {
        int offset = 0;
        try {
            int width = manager.findViewByPosition(position).getMeasuredWidth();
            offset = (mWidth - width) / 2;
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "getOffset");
        }
        if (offset > 0) return offset;
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        ZYApplication.homeMoveFlag = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        ZYApplication.homeMoveFlag = false;
    }

    private int space;
    private boolean UP_OR_DOWN = true;
    private boolean isChanging = false;


    private int changeIndex = 0;
    private int changeMax = 5;
    private Runnable homeRun = new Runnable() {
        @Override
        public void run() {

            if (UP_OR_DOWN) {//向上
                changeIndex--;
            } else {//向下
                changeIndex++;
            }
            mHandler.sendEmptyMessage(CHANGE_VIEW_FLAG);
        }
    };

    private void changeView() {
        /*if (changeIndex < 0 || changeIndex > changeMax) {
            isChanging = false;
            return;
        }
        ViewGroup.LayoutParams params = v_change.getLayoutParams();
        if (changeIndex == 0) {
            params.height = MIN_CHANGE;
            v_top_menu.setVisibility(View.VISIBLE);
            v_top_menu.setAlpha(1);
            v_change.setAlpha(0);
            if (!mRef) {
                mRef = true;
                setMainRefresh(true);
            }
        } else if (changeIndex == changeMax) {
            params.height = MAX_CHANGE;
            v_top_menu.setVisibility(View.GONE);
            v_top_menu.setAlpha(0);
            v_change.setAlpha(0);
            if (mRef) {
                mRef = false;
                setMainRefresh(false);
            }
        } else {
            int h = (changeIndex * (MAX_CHANGE - MIN_CHANGE)) / changeMax + MIN_CHANGE;
            params.height = h;
            v_top_menu.setVisibility(View.VISIBLE);
            float a = (float) h / (MAX_CHANGE - MIN_CHANGE);
            a = 1 - a;
            v_top_menu.setAlpha(a);
            v_change.setAlpha(a);
            if (mRef) {
                mRef = false;
                setMainRefresh(false);
            }
            mHandler.postDelayed(homeRun, 40);
        }
        v_change.setLayoutParams(params);
        if (changeIndex == 0 || changeIndex == changeMax) isChanging = false;*/

    }

    private boolean mRef = false;

    private void setMainRefresh(boolean flag) {
        ((MainActivity) context).showRefreshView(flag);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}

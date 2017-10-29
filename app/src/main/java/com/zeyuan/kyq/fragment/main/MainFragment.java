package com.zeyuan.kyq.fragment.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.ArticleTypeEntity;
import com.zeyuan.kyq.Entity.HelpItemEntity;
import com.zeyuan.kyq.Entity.HomePageEntity;
import com.zeyuan.kyq.Entity.InformationEntity;
import com.zeyuan.kyq.Entity.MainBannerEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.ArticleMainAdapter;
import com.zeyuan.kyq.adapter.MyFragmentAdapter;
import com.zeyuan.kyq.adapter.RecyclerArticleAdapter;
import com.zeyuan.kyq.adapter.RecyclerHelpAdapter;
import com.zeyuan.kyq.app.BaseZyFragment;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.CancerResuletBean;
import com.zeyuan.kyq.bean.FindSymtomBean;
import com.zeyuan.kyq.bean.MainPageInfoBean;
import com.zeyuan.kyq.bean.PatientDetailBean;
import com.zeyuan.kyq.bean.WSZLBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.FragmentCallBack;
import com.zeyuan.kyq.biz.forcallback.PlanMainInterface;
import com.zeyuan.kyq.filedownloader.JFileDownloadListener;
import com.zeyuan.kyq.filedownloader.JFileDownloader;
import com.zeyuan.kyq.fragment.InfoStepFragment;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.service.ZYKaqService;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.DensityUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.EditStepNewActivity;
import com.zeyuan.kyq.view.HomeSymptomActivity;
import com.zeyuan.kyq.view.MedicalRecordActivity;
import com.zeyuan.kyq.view.NewCircleActivity;
import com.zeyuan.kyq.view.PerfectDataActivity;
import com.zeyuan.kyq.view.PersonalDataActivity;
import com.zeyuan.kyq.view.RecordClassifyActivity;
import com.zeyuan.kyq.view.ResultDetailActivity;
import com.zeyuan.kyq.view.SearchComplicationActivity;
import com.zeyuan.kyq.view.SearchDrugActivity;
import com.zeyuan.kyq.view.SearchQuotaActivity;
import com.zeyuan.kyq.view.ShowDiscuzActivity;
import com.zeyuan.kyq.view.ViewDataListener;
import com.zeyuan.kyq.view.WSZLActivity;
import com.zeyuan.kyq.widget.CircleImageView;
import com.zeyuan.kyq.widget.CustomView.InsideRecyclerView;
import com.zeyuan.kyq.widget.CustomView.InsideViewPager;
import com.zeyuan.kyq.widget.DrawCircleView;
import com.zeyuan.kyq.widget.PullToRefresh.PullToRefreshLayout;
import com.zeyuan.kyq.widget.PullToRefresh.PullableScrollView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**Fragment ‘主页’ 展示*/
public class MainFragment extends BaseZyFragment implements View.OnClickListener,ViewDataListener, AdapterView.OnItemClickListener,
        HttpResponseInterface ,PullToRefreshLayout.OnRefreshListener,ViewPager.OnPageChangeListener,PlanMainInterface,FragmentCallBack,
        InsideViewPager.OnSingleTouchListener,RecyclerArticleAdapter.OnItemClickListener,InsideRecyclerView.OnSingleTouchListener,
        PullableScrollView.OnScrollListener,RecyclerHelpAdapter.OnHelpItemClickListener{

    private static final String TAG = "MainFragment";

    private Button tv_main_menu_bingli;
    private Button tv_main_menu_step;
//    private Button tv_main_menu_zhengzhuang;
    private Button tv_main_menu_jilu;
    private Button ll_main_zhinengwenzheng;
    private Button ll_main_chafuzuoyong;
    private Button ll_main_chabingfazheng;
    private Button ll_main_chazhibiao;
    private ImageButton ll_find_symptom_main;
    private RelativeLayout rl_main_head_img;
    private TextView tv_drug_name;
    private TextView tv_discover_days;

    private RelativeLayout pos1, pos2;//用来判断位置的

    private InsideViewPager vp;

    private PullToRefreshLayout layout;//下拉刷新控件
    private String stepId;
    private String cancerId;
    private String PlanID;//立即用药的id
    private String PerformID;//查看解决方案的id
    private String CureConfID;
    private String discoverTime;
    private boolean isBindFlag = false;
    private static final int pagesize = 20;

    private List<List<InformationEntity>> listBottom;

    private List<List<InformationEntity>> mInformationData;
    private SparseIntArray mInformationPage;
    private List<ArticleTypeEntity> art_list;
    private List<HelpItemEntity> help;
    private ArticleMainAdapter adapter_bm1;
    private ArticleMainAdapter adapter_bm2;
    private RecyclerHelpAdapter adapter_rv;
//    private View v_bottom;
//    private View line_help;
//    private View v_help;
    private View v_help_action;

    private static final int SUCCESS = 0;
    private static final int FAIL = 1;
    private static final int LOADING_MAX = 2;

    private View statusBar2;
    private View statusBar1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_new, container, false);
        //初始化状态栏
        initStatusBar();
        statusBar2 = findViewById(R.id.statusBar2);
        statusBar2.setAlpha(0);
        statusBar1 = findViewById(R.id.statusBar1);
        statusBar1.setAlpha(0);
        //初始化控件
        initView();
        //设置监听事件
        setListener();
        //初始化数据
        initData();
        return rootView;
    }

    /***
     * 设置监听事件
     */
    private void setListener() {
        try {
            layout.setOnRefreshListener(this);
            tv_main_menu_bingli.setOnClickListener(this);
//            tv_main_menu_zhengzhuang.setOnClickListener(this);
            tv_main_menu_step.setOnClickListener(this);
            tv_main_menu_jilu.setOnClickListener(this);
            ll_main_zhinengwenzheng.setOnClickListener(this);
            ll_main_chazhibiao.setOnClickListener(this);
            ll_main_chabingfazheng.setOnClickListener(this);
            ll_main_chafuzuoyong.setOnClickListener(this);
            ll_find_symptom_main.setOnClickListener(this);
            rl_main_head_img.setOnClickListener(this);
//            findViewById(R.id.iv_edit_tab).setOnClickListener(this);
//            findViewById(R.id.iv_edit_tab_top).setOnClickListener(this);
            RelativeLayout ll_search_drug = (RelativeLayout)findViewById(R.id.ll_search_drug);
            ll_search_drug.setOnClickListener(this);
            RelativeLayout rl_linchuang_zhaomu = (RelativeLayout)findViewById(R.id.rl_linchuang_zhaomu);
            rl_linchuang_zhaomu.setOnClickListener(this);
            RelativeLayout rl_zhihuan = (RelativeLayout)findViewById(R.id.ll_zhihuan);
            rl_zhihuan.setOnClickListener(this);
//            findViewById(R.id.tv_user_pull).setOnClickListener(this);
        }catch(Exception e){
            ExceptionUtils.ExceptionToUM(e,getActivity(),"MainFragment");
        }
    }

    /**
     * 从网络中获取数据
     */
    private void initData() {
        try {
            Factory.post(this,Const.EGetPatientDetail);
        }catch(Exception e){
            ExceptionUtils.ExceptionToUM(e,getActivity(),"MainFragment");
        }
    }

    /***
     * mainfragment数据请求
     */
    private void getMainData() {
        Factory.post(this,Const.EGetMainPage);
    }

    private String stepMedicaName;
    private boolean articleFlag = false;
    private void bindView(MainPageInfoBean bean) {
        try {
            //主页横幅
            List<HomePageEntity> bannerEntities = bean.getBannerList();
            String bannerState = bean.getBannerListState();
            initbanner(bannerEntities,bannerState);
            initStepView();
            if (findViewById(R.id.child).getVisibility()!=View.VISIBLE){
                findViewById(R.id.child).setVisibility(View.VISIBLE);
            }
            //刷新成功
            try {
                if(layout!=null){
                    layout.refreshFinish(PullToRefreshLayout.SUCCEED, true);
                }
            }catch (Exception e){
                ExceptionUtils.ExceptionToUM(e,getActivity(),"MainFragment,下拉刷新失败");
            }
        }catch(Exception e){
            ExceptionUtils.ExceptionToUM(e,getActivity(),"MainFragment,bindView");
        }
    }



    private ProgressBar bar;
    private DrawCircleView dcv;
    private PullableScrollView mSv;
    private CircleImageView headImg;
    private int ChangeHeight = 0;

    /**
     * 初始化view
     */
    private void initView() {
        try {
            vp = (InsideViewPager)findViewById(R.id.vp_main_top);
            vp.setOnSingleTouchListner(this);

            //肿瘤头条和相似案例的控件
            layout = (PullToRefreshLayout)findViewById(R.id.pull_layout_headline);

            //新版首页菜单控件
            tv_main_menu_bingli = (Button)findViewById(R.id.tv_main_menu_bingli);
            tv_main_menu_step = (Button)findViewById(R.id.tv_main_menu_step);
//            tv_main_menu_zhengzhuang = (Button)findViewById(R.id.tv_main_menu_zhengzhuang);
            tv_main_menu_jilu = (Button)findViewById(R.id.tv_main_menu_jilu);

            ll_main_zhinengwenzheng = (Button)findViewById(R.id.btn_main_zhinengwenzheng);
            ll_main_chafuzuoyong = (Button)findViewById(R.id.btn_main_chafuzuoyong);
            ll_main_chabingfazheng = (Button)findViewById(R.id.btn_main_chabingfazheng);
            ll_main_chazhibiao = (Button)findViewById(R.id.btn_main_chazhibiao);
            ll_find_symptom_main = (ImageButton)findViewById(R.id.btn_main_chazhengzhuang);
            rl_main_head_img = (RelativeLayout)findViewById(R.id.rl_main_head_img);

            tv_drug_name = (TextView)findViewById(R.id.tv_drug_name);
            tv_discover_days = (TextView)findViewById(R.id.tv_discover_days);
            headImg = (CircleImageView)findViewById(R.id.iv_main_head_img);

            mSv = (PullableScrollView)findViewById(R.id.my_sv);
            mSv.setOnScrollListener(this);
            mSv.setPullUpFlag(false);



//            v_tab_top = findViewById(R.id.v_tab_top);

            dcv = (DrawCircleView)findViewById(R.id.dcv_main_top);

            listBottom = new ArrayList<>();


            v_help_action = findViewById(R.id.v_help_action);

            ChangeHeight = DensityUtils.dpToPx(context,165);
        }catch(Exception e){
            ExceptionUtils.ExceptionToUM(e,getActivity(),"initView,MainFragment");
        }
    }



    private void initStepView(){
        try {
            if(Const.NO_STEP.equals(UserinfoData.getIsHaveStep(getActivity()))){
                tv_drug_name.setText(Html.fromHtml("<i>治疗方式未知  </i>"));
                findViewById(R.id.iv_main_to_write).setVisibility(View.VISIBLE);
                findViewById(R.id.rl_name_step).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createInfoStepFragment();
                    }
                });
            }else {
                findViewById(R.id.iv_main_to_write).setVisibility(View.GONE);
                stepMedicaName = MapDataUtils.getAllStepName(stepId);
                if(TextUtils.isEmpty(stepMedicaName)){//暂未找到相似案例

                }else{
                    mainStepName = stepMedicaName;
                    tv_drug_name.setText(stepMedicaName);//设置阶段所使用的药物。
                    findViewById(R.id.rl_name_step).setClickable(false);
                }
            }
            if(tv_drug_name.getVisibility()!=View.VISIBLE){
                tv_drug_name.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,getActivity(),"MainFragment,initStepView");
        }
    }


    private void initViewPager(List<HomePageEntity> bannerList,String state){

        try {
            ArrayList<Fragment> fragments = new ArrayList<>();
            TypeImgFragment imgFragment;
            Bundle bundle;
            for(int i=0;i<bannerList.size();i++){
                imgFragment = new TypeImgFragment();
                imgFragment.setPageFlag(false);
                HomePageEntity entity = bannerList.get(i);
                bundle = new Bundle();
                bundle.putSerializable(TypeImgFragment.BANNER_DATA, entity);
                imgFragment.setArguments(bundle);
                fragments.add(imgFragment);
            }
            dcv.setDrawCricle(fragments.size(), 6, Color.parseColor("#4c4c4c"), Color.parseColor("#FFFFFF"));
            dcv.redraw(0);
            MyFragmentAdapter fAdapter = new MyFragmentAdapter(getActivity().getSupportFragmentManager(),fragments);
            vp.setAdapter(fAdapter);
            vp.addOnPageChangeListener(this);
            mTopPageMax = fragments.size();
            if ("1".equals(state)){
                ZYApplication.mainMoveFlag = true;
                mTopPageFlag = true;
                showTopPageChange();
            }else {
                mTopPageFlag = false;
                mTopPageIndex = 0;
                changeTopPage();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,getActivity(),"MainFragment,initViewPager");
        }
    }

    private DrawCircleView dcv_middle;
    private InsideViewPager vp_middle;
    private void initMiddleViewPager(List<MainBannerEntity> bannerList,String state){
        try {
            if (dcv_middle==null){
                dcv_middle = (DrawCircleView)findViewById(R.id.dcv_middle_banner);
            }
            if (vp_middle==null){
                vp_middle = (InsideViewPager)findViewById(R.id.vp_middle_banner);
            }
            ArrayList<Fragment> fragments = new ArrayList<>();
            MiddleImgFragment imgFragment;
            Bundle bundle;
            for(int i=0;i<bannerList.size();i++){
                imgFragment = new MiddleImgFragment();
                imgFragment.setPageFlag(false);
                MainBannerEntity entity = bannerList.get(i);
                bundle = new Bundle();
                bundle.putSerializable(MiddleImgFragment.BANNER_DATA, entity);
                imgFragment.setArguments(bundle);
                fragments.add(imgFragment);
            }
            dcv_middle.setDrawCricle(fragments.size(), 6, Color.parseColor("#4c4c4c"), Color.parseColor("#FFFFFF"));
            dcv_middle.redraw(0);
            MyFragmentAdapter fAdapter = new MyFragmentAdapter(getActivity().getSupportFragmentManager(),fragments);
            vp_middle.setAdapter(fAdapter);
            vp_middle.addOnPageChangeListener(new MiddlePageChange());
            mMiddleMax = fragments.size();
            if ("1".equals(state)){
                ZYApplication.mainMoveFlag = true;
                mMiddleFlag = true;
                showMiddleChange();
            }else {
                mMiddleFlag = false;
                mMiddleIndex = 0;
                changeMiddlePage();
            }

        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,getActivity(),"MainFragment,initViewPager");
        }
    }

    @Override
    public void onSingleTouch(boolean flag) {
        LogCustom.i(Const.TAG.ZY_TEST,"flag:"+flag);
        if (flag){
            if (mSv.isPullDownFlag()) {
                mSv.setPullDownFlag(false);
            }
            mTopPageFlag = false;
        }else {
            if (!mSv.isPullDownFlag()) {
                mSv.setPullDownFlag(true);
            }
            mTopPageFlag = true;
        }

    }

    @Override
    public void onRySingleTouch(boolean flag) {
        LogCustom.i(Const.TAG.ZY_TEST,"flag:"+flag);
        if (flag){
            if (mSv.isPullDownFlag()) {
                mSv.setPullDownFlag(false);
            }
            mTopPageFlag = false;
        }else {
            if (!mSv.isPullDownFlag()) {
                mSv.setPullDownFlag(true);
            }
            mTopPageFlag = true;
        }
    }

    private int titleMax;
    private String[] titles;
    /***
     * 更新UI界面
     * @param list 服务器返回的数据
     */
    private void bindPagerView(List<MainPageInfoBean.SimilarcaseNumEntity> list) {
        try {
            titles = new String[list.size()];
            for(int i =0;i<list.size();i++){
                titles[i] = list.get(i).getTitle();
            }
            ZYApplication.mainMoveFlag = true;
            titleFlag = true;
            titleMax = list.size();
            showSimalarTitles();
            LogCustom.i(TAG, "MAX:" + titleMax);
        }catch(Exception e){
            ExceptionUtils.ExceptionToUM(e,getActivity(),"bindPagerView,MainFragment");
        }
    }

    private boolean headtitleFlag = false;


    public void changeTopPage(){
        try {
            vp.setCurrentItem(mTopPageIndex);
        }catch(Exception e){
            ExceptionUtils.ExceptionToUM(e,getActivity(),"MainFragment");
        }
    }

    private void changeMiddlePage(){
        try {
            vp_middle.setCurrentItem(mMiddleIndex);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,getActivity(),"MainFragment");
        }
    }

    private boolean titleFlag = false;
    private int titleIndex = 0;
    private Runnable simalarRun = new Runnable() {
        @Override
        public void run() {
            try {
                if(ZYApplication.mainPageFlag){
                    if(titleFlag&&ZYApplication.mainMoveFlag){
                        titleIndex++;
                        if(titleIndex>=titleMax){
                            titleIndex = 0;
                        }
                        mHandler.sendEmptyMessage(CHANGE_SIMALAR_TITLE);
                    }
                    mHandler.postDelayed(simalarRun,5000);
                }
            }catch(Exception e){
                ExceptionUtils.ExceptionToUM(e,getActivity(),"MainFragment");
            }
        }
    };



    private int mTopPageMax = 0;
    private int mTopPageIndex = -1;
    private boolean mTopPageFlag = false;

    private Runnable mTopPageRun = new Runnable() {
        @Override
        public void run() {
            try {
                if(ZYApplication.mainPageFlag){
                    if(mTopPageFlag&&ZYApplication.mainMoveFlag){
                        mTopPageIndex++;
                        if(mTopPageIndex>=mTopPageMax){
                            mTopPageIndex = 0;
                        }
                        mHandler.sendEmptyMessage(CHANGE_TOP_PAGE);
                    }
                    mHandler.postDelayed(mTopPageRun,5000);
                }
            }catch(Exception e){
                ExceptionUtils.ExceptionToUM(e,getActivity(),"MainFragment");
            }
        }
    };

    private int mMiddleMax = 0;
    private int mMiddleIndex = -1;
    private boolean mMiddleFlag = false;

    private Runnable mMiddleRun = new Runnable() {
        @Override
        public void run() {
            try {
                if (ZYApplication.mainPageFlag){
                    if (mMiddleFlag&&ZYApplication.mainMoveFlag){
                        mMiddleIndex++;
                        if (mMiddleIndex>=mMiddleMax){
                            mMiddleIndex = 0;
                        }
                        mHandler.sendEmptyMessage(CHANGE_MIDDLE_PAGE);
                    }
                    mHandler.postDelayed(mMiddleRun,5000);
                }
            }catch (Exception e){

            }
        }
    };


    private boolean isTopPagePost = false;

    private void showTopPageChange(){
        if(!isTopPagePost){
            mHandler.post(mTopPageRun);
            isTopPagePost = true;
        }
    }

    private boolean isMiddlePost = false;

    private void showMiddleChange(){
        if(!isMiddlePost){
            mHandler.post(mMiddleRun);
            isMiddlePost = true;
        }
    }

    private boolean isSimalarPost = false; //防止多次post线程
    private void showSimalarTitles(){
        if(!isSimalarPost){
            mHandler.post(simalarRun);
            isSimalarPost = true;
        }
    }


    /***
     * 点击事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_main_head_img://点击用户头像
                Factory.onEvent(getActivity(),Const.EVENT_PatientDetail,Const.EVENTFLAG,null);
                startActivity(new Intent(getActivity(), PersonalDataActivity.class));
                break;
            case R.id.btn_main_chazhengzhuang: //点击查症状
                if(Const.RESULT.equals(UserinfoData.getIsHaveStep(getActivity()))){
                    getNoStepHintDailog();
                }else {
                    Factory.onEvent(getActivity(),Const.EVENT_SymptomsQuery,Const.EVENTFLAG,null);
                    startActivity(new Intent(getActivity(), HomeSymptomActivity.class));
                }
                break;
            case R.id.tv_main_menu_jilu: //点击记录病情
                if(Const.RESULT.equals(UserinfoData.getIsHaveStep(getActivity()))){
                    getNoStepHintDailog();
                }else {
                    startActivity(new Intent(getActivity(), RecordClassifyActivity.class));
                }
                break;
            case R.id.tv_main_menu_zhengzhuang: //点击添加症状
                /*if(Const.RESULT.equals(UserinfoData.getIsHaveStep(getActivity()))){
                    getNoStepHintDailog();
                }else {
                    startActivity(new Intent(getActivity(), RecordSymptomActivity.class));
                }*/
                break;
            case R.id.tv_main_menu_step: //点击编辑阶段
                if(Const.RESULT.equals(UserinfoData.getIsHaveStep(getActivity()))){
                    getNoStepHintDailog();
                }else {
                    Factory.onEvent(getActivity(),Const.EVENT_UpdateStep,null,null);
                    startActivity(new Intent(getActivity(), EditStepNewActivity.class));
                }
                break;
            case R.id.tv_main_menu_bingli://点击我的病历
                if(Const.RESULT.equals(UserinfoData.getIsHaveStep(getActivity()))){
                    getNoStepHintDailog();
                }else {
                    Factory.onEvent(getActivity(),Const.EVENT_AllStep,Const.EVENTFLAG,null);
                    startActivity(new Intent(getActivity(), MedicalRecordActivity.class));
                }
                break;
            case R.id.ll_search_drug://点击查药
                startActivity(new Intent(getActivity(), SearchDrugActivity.class));
                break;
            case R.id.btn_main_chafuzuoyong://点击查副作用
                Factory.onEvent(getActivity(), Const.EVENT_SideEffect,Const.EVENTFLAG,null);
                startActivity(new Intent(getActivity(), SearchComplicationActivity.class)
                        .putExtra(Const.SEARCH_POLICY_TYPE, Const.TYPE_EFFECT));
                break;
            case R.id.btn_main_zhinengwenzheng://点击智能问诊
                if(Const.RESULT.equals(UserinfoData.getIsHaveStep(getActivity()))){
                    getNoStepHintDailog();
                }else {
                    Factory.onEvent(getActivity(), Const.EVENT_WenZheng,Const.EVENTFLAG,null);
                    String diagnoseTime = UserinfoData.getDiagnoseTime(getActivity());
                    if(!DataUtils.isToday(diagnoseTime)){
                        startActivity(new Intent(getActivity(), PerfectDataActivity.class));
                    }else {
                        Factory.post(this, Const.EGetConfirmSecond);
                    }
                }
                break;
            case R.id.btn_main_chazhibiao://点击查指标
                Factory.onEvent(getActivity(), Const.EVENT_SearchQuota,Const.EVENTFLAG,null);
                startActivity(new Intent(getActivity(), SearchQuotaActivity.class));
                break;
            case R.id.btn_main_chabingfazheng://点击查并发症
                Factory.onEvent(getActivity(), Const.EVENT_SideEffect,Const.EVENTFLAG,null);
                startActivity(new Intent(getActivity(), SearchComplicationActivity.class)
                        .putExtra(Const.SEARCH_POLICY_TYPE, Const.TYPE_COMPLICATION));
                break;
            case R.id.rl_linchuang_zhaomu:
                Factory.onEvent(getActivity(), Const.EVENT_LinChuangZhaoMu,Const.EVENTFLAG,null);
                startActivity(new Intent(getActivity(), NewCircleActivity.class)
                        .putExtra(Contants.CircleID, "7003"));
                break;
            case R.id.ll_zhihuan:
                Factory.onEvent(getActivity(), Const.EVENT_ZhiHuan,Const.EVENTFLAG,null);
                startActivity(new Intent(getActivity(), NewCircleActivity.class).
                        putExtra(Contants.CircleID, "7007"));
                break;

            default: {
                Toast.makeText(getActivity(), "unkonw onclick id", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**设置查看解决办法数据*/
    public void resultDetail(String performID) {
        try {
            PerformID = performID;
            Factory.post(this, Const.EGetCancerProcess);
        }catch(Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "initdata,mainfragment");
        }
    }


    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(context));
        if (tag == Const.ESetPlanMedicine) {//添加到计划用药
            map.put(Contants.PlanStepID, PlanID);
        }
        //查症状
        if (tag == Const.EGetCancerProcess) {
            map.put(Contants.StepID, UserinfoData.getStepID(getActivity()));
            map.put(Contants.CancerID, UserinfoData.getCancerID(getActivity()));
            map.put(Contants.PerformID, PerformID);
        }
        if (tag == Const.PHomeArticleInfo){
            if (articleFlag){
                map.put("catid","0");
                map.put("page","0");
            }else {
                map.put("catid",art_list.get(articleIndex).getCatid()+"");
                map.put("page",mInformationPage.get(articleIndex)+"");
            }
            map.put("pagesize",pagesize+"");
        }
        return map;
    }

    private String[] shareParams;
    @Override
    public byte[] getPostParams(int flag) {
        String[] args = new String[]{};
        if (flag == Const.EGetMainPage) {//获取主页详情
            String cityID = UserinfoData.getCityID(context);
            String provinceID;
            if("0".equals(cityID)){
                provinceID = "0";
                UserinfoData.saveProvinceID(context,provinceID);
                UserinfoData.saveCityID(context,cityID);
            }else{
                provinceID= cityID.substring(0,2)+"0000";
                UserinfoData.saveProvinceID(context,provinceID);
            }
            if(Const.NO_STEP.equals(UserinfoData.getIsHaveStep(getActivity()))){
                args = new String[]{
                        Contants.InfoID, UserinfoData.getInfoID(context),
                        Contants.CityID, cityID,
                        Contants.ProvinceID,provinceID ,
                        Contants.CancerID, UserinfoData.getCancerID(context),
                        "v", ZYApplication.versionNum,
                        "t", "2",//1是ios 2是android
                        Const.ISHAVESTEP,"0"
                };
            }else {
                args = new String[]{
                        Contants.InfoID, UserinfoData.getInfoID(context),
                        Contants.StepID, stepId,
                        Contants.CityID, cityID,
                        Contants.ProvinceID,provinceID ,
                        Contants.CancerID, UserinfoData.getCancerID(context),
                        Contants.CureConfID, MapDataUtils.getAllCureconfID(stepId),
                        "v", ZYApplication.versionNum,
                        "t", "2",//1是ios 2是android
                        Const.ISHAVESTEP,"1"
                };
            }

        }else if (flag == Const.ESetPlanMedicine) {//添加到计划用药
            args = new String[]{
                    Contants.InfoID, UserinfoData.getInfoID(getActivity()),
                    Contants.PlanStepID, PlanID
            };
        }else if (flag == Const.EGetCancerProcess) {
            if(Const.NO_STEP.equals(UserinfoData.getIsHaveStep(getActivity()))){
                args = new String[]{
                        Contants.InfoID, UserinfoData.getInfoID(getActivity()),
                        Contants.CancerID, UserinfoData.getCancerID(context),
                        Contants.PerformID, PerformID,
                        Const.ISNEWVERSION,Const.ISNEWVERSION_FINAL,
                        Const.ISHAVESTEP,"0"
                };
                shareParams = args;
            }else {
                args = new String[]{
                        Contants.InfoID, UserinfoData.getInfoID(getActivity()),
                        Contants.StepID, UserinfoData.getStepID(context),
                        Contants.CancerID, UserinfoData.getCancerID(context),
                        Contants.PerformID, PerformID,
                        Contants.CureConfID,MapDataUtils.getAllCureconfID(UserinfoData.getStepID(context)),
                        Const.ISNEWVERSION,Const.ISNEWVERSION_FINAL,
                        Const.ISHAVESTEP,"1"
                };
                shareParams = args;
            }

        }else if (flag == Const.EGetPatientDetail) {
            args = new String[]{
                    Contants.InfoID, UserinfoData.getInfoID(getActivity())
            };
        }else if (flag == Const.EGetConfirmSecond){
            if(Const.NO_STEP.equals(UserinfoData.getIsHaveStep(getActivity()))){
                args = new String[]{
                        Contants.InfoID, UserinfoData.getInfoID(getActivity()),
                        Contants.CancerID, UserinfoData.getCancerID(getActivity()),
                        Contants.PeriodID, UserinfoData.getPeriodID(getActivity()),
                        Const.ISHAVESTEP,"0"
                };
            }else{
                args = new String[]{
                        Contants.InfoID, UserinfoData.getInfoID(getActivity()),
                        Contants.StepID, UserinfoData.getStepID(getActivity()),
                        Contants.CancerID, UserinfoData.getCancerID(getActivity()),
                        Contants.PeriodID, UserinfoData.getPeriodID(getActivity()),
                        Const.ISHAVESTEP,"1"
                };
            }
        }else if(flag == Const.EGetResultDetail){
            args = new String[]{
                    Contants.InfoID,UserinfoData.getInfoID(getActivity()),
                    Contants.StepID,UserinfoData.getStepID(getActivity()),
                    Contants.CancerID,UserinfoData.getCancerID(getActivity()),
                    Contants.PeriodID,UserinfoData.getPeriodID(getActivity()),
                    Contants.BodyStatusID,"2"
            };
            shareParams = args;
        }

        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object t, int position) {
        if(t==null) return;
        if (position == Const.EGetMainPage) {
            MainPageInfoBean infoBean = (MainPageInfoBean) t;
            if(Contants.RESULT.equals(infoBean.iResult)){
                if(TextUtils.isEmpty(infoBean.getMobile())){
                    bindView(infoBean);
                }else {
                    ZYApplication.isBind = true;
                    ZYApplication.PhoneNum = infoBean.getMobile();
                    bindView(infoBean);
                }
            }
        }

        if (position == Const.EGetPatientDetail) {//患者详情
            PatientDetailBean patientDetailBean = (PatientDetailBean) t;
            if (Contants.RESULT.equals(patientDetailBean.iResult)) {
                UserinfoData.saveUserData(context, patientDetailBean);
                cancerId = UserinfoData.getCancerID(context);
                stepId = UserinfoData.getStepID(context);
                correctHeadImgUrl(patientDetailBean.getHeadimgurl());
                showAvatar();
                setCancerDays();

                getMainData();
            }
        }
        if (position == Const.EGetCancerProcess) {
            FindSymtomBean bean = (FindSymtomBean) t;
            if (Contants.RESULT.equals(bean.getIResult())) {
                UserinfoData.performid = null;
                toResult(bean);
            }
        }
        if(position == Const.EGetConfirmSecond){
            WSZLBean bean = (WSZLBean) t;
            if(Const.RESULT.equals(bean.getIResult())){
                if("0".equals(bean.getUiSetBit())){
                    Factory.post(this,Const.EGetResultDetail);
                }else{
                    startActivity(new Intent(getActivity(), WSZLActivity.class)
                            .putExtra(Contants.WSZLBean, bean));
                }
            }
        }
        if(position == Const.EGetResultDetail){
            CancerResuletBean bean = (CancerResuletBean)t;
            if(Const.RESULT.equals(bean.getIResult())){
                startActivity(new Intent(getActivity(), ResultDetailActivity.class)
                        .putExtra(Contants.CancerResuletBean, bean)
                        .putExtra(Const.RESULT_PARAMS_FOR_SHARE, shareParams));
            }
        }


    }

    private void toResult(FindSymtomBean bean) {

    }


    /**
     * 打开浏览器
     * @param url
     */
    private void openUrl(String url) {
        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
        startActivity(it);
    }

    private void initbanner(List<HomePageEntity> bannerList,String state){
        if(bannerList!=null&&bannerList.size()!=0){
            initViewPager(bannerList,state);
        }else {
            mTopPageFlag = false;
        }
    }

    private View v_other_banner;
    private void initotherbanner(List<MainBannerEntity> bannerother,String state){
        /*if (v_other_banner == null){
            v_other_banner = findViewById(R.id.rl_main_middle_banner);
        }*/
        if (bannerother!=null&&bannerother.size()>0){
            v_help_action.setVisibility(View.VISIBLE);
            initMiddleViewPager(bannerother,state);
        }else {
            mMiddleFlag = false;
            v_help_action.setVisibility(View.GONE);
        }
    }


    private String downurl = "";
    private String updateMessage = "";
    private String versionNum = "";
    private String updateType = "";
    /***
     * 根据updateType判断下一步操作
     */
    private void initUpDateType(MainPageInfoBean.UpEntity upEntity){
        if(upEntity!=null){
            /**新版本的下载地址*/
            downurl = upEntity.getL();
            /**新版本的提示内容*/
            updateMessage = upEntity.getM();
            /**新版本的更新方式*/
            updateType = upEntity.getU();
            /**新版本的版本号*/
            versionNum = upEntity.getV();
        }
        try {
            if("3".equals(updateType)){

            }else{
                initUpdateVersion();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,getActivity(),"initUpDateType()");
        }
    }

    private Dialog mdialog;
    /***
     * 初始化版本迭代界面
     */
    private void initUpdateVersion(){
        ZYDialog.Builder builder = new ZYDialog.Builder(getActivity());
        if("1".equals(updateType)){
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
            builder.create().show();
        }else{
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
            builder.create().show();
        }
    }

    private File file;
    private static final String KAQ_DISK_PATH = Environment.getExternalStorageDirectory().getPath() + "/kaq/APK/";
    /***
     * 下载功能模块
     */
    private void updateVersion(){
        /**设置下载dialog*/
//        mdialog.cancel();

        /**下载功能*/
        String apkname = downurl.substring(downurl.lastIndexOf("/")+1);//文件名
        file = new File(KAQ_DISK_PATH+apkname);
        /**判断父目录是否存在*/
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        /**文件是否已存在，如果存在则转跳安装方法，不存在则去下载*/
        if(file.exists()){
            updataApp();
        }else{
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
            /**判断是否问强制更新*/
            if("1".equals(updateType)){
                dialog.setCancelable(false);
            }else{
                dialog.setButton(ProgressDialog.BUTTON_POSITIVE, "后台更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.setCanceledOnTouchOutside(false);
            }

            dialog.show();
            /**下载器设置*/
            JFileDownloader downloader = new JFileDownloader(downurl,file.getAbsolutePath());
            downloader.setFileDownloadListener(new JFileDownloadListener() {
                @Override
                public void downloadProgress(int progress, double speed, long remainTime) {
                    if(dialog.isShowing()){
                        dialog.setProgress(progress);
                    }
                }
                @Override
                public void downloadCompleted(File file, long downloadTime) {
                    dialog.cancel();
//                    Message message = new Message();
                    mHandler.sendEmptyMessage(UPDATA_VERSION_SUCCESS);
                }
            });
            /**启动下载线程*/
            new DownloaderThread(downloader).start();
        }
    }

    /***
     * App安装程序
     */
    private void updataApp(){
        String apkname = downurl.substring(downurl.lastIndexOf("/")+1);
        file = new File(KAQ_DISK_PATH+apkname);
        if(file.exists()){
            Intent install = new Intent();
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setAction(android.content.Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
            startActivity(install);
        }
    }

    /***
     * 版本迭代线程内部类
     */
    class DownloaderThread extends Thread{
        private JFileDownloader downloader;

        public DownloaderThread(JFileDownloader downloader){
            this.downloader = downloader;
        }
        @Override
        public void run() {
            try {
                downloader.startDownload();
            } catch (Exception e) {
                Log.i(TAG,"下载出错");
                e.printStackTrace();
            }
        }
    }


    private static final int UPDATA_VERSION_SUCCESS = 123;
    private static final int CHANGE_SIMALAR_TITLE = 124;
    private static final int CHANGE_HEADLINE_TITLE = 125;
    private static final int CHANGE_TOP_PAGE = 126;
    private static final int CHANGE_MIDDLE_PAGE = 122;
    private final MyHandler mHandler = new MyHandler(this);
    /**
     *
     * Handler静态内部类
     *
     */
    private static class MyHandler extends Handler{
        private final WeakReference<MainFragment> mFragment;
        public MyHandler(MainFragment fragment){
            mFragment = new WeakReference<>(fragment);
        }
        private MainFragment fragment;
        @Override
        public void handleMessage(Message msg) {
            fragment = mFragment.get();
            if(msg.what == UPDATA_VERSION_SUCCESS){
                fragment.updataApp();
            }else if(msg.what == CHANGE_SIMALAR_TITLE){

            }else if(msg.what == CHANGE_HEADLINE_TITLE){

            }else if(msg.what == CHANGE_TOP_PAGE){
                fragment.changeTopPage();
            }else if(msg.what == CHANGE_MIDDLE_PAGE){
                fragment.changeMiddlePage();
            }
        }
    }

    @Override
    public void showLoading(int tag) {
        if(tag == Const.EGetMainPage){
            showBar();
        }
    }

    @Override
    public void hideLoading(int tag) {
        if(tag == Const.EGetMainPage){
            cancleBar();
        }
    }

    @Override
    public void showError(int tag) {
        if(tag == Const.EGetMainPage){
            cancleBar();
            overRefresh(1);
        }else if (tag == Const.PHomeArticleInfo){
            if (articleFlag){
                Toast.makeText(context,"文章数据加载失败",Toast.LENGTH_SHORT).show();
                articleFlag = false;
//                showNoArticleView();
            }
            if (loading){
                loadingErrorView(1);
            }
        }
    }

    private void showAvatar(){
        String headImgUrl = UserinfoData.getAvatarUrl(getActivity());
        if(TextUtils.isEmpty(headImgUrl)){
            headImg.setImageResource(R.mipmap.default_avatar);
        }else {
            Glide.with(getActivity()).load(headImgUrl).signature(new IntegerVersionSignature(1)).
                    error(R.mipmap.default_avatar).into(headImg);
        }
    }

    private boolean startTitleFlag = false;
    private String mainStepName;
    @Override
    public void onResume() {
        super.onResume();
        LogCustom.i("ZYS", "MainFragment onResume");

        try {
            if(ZYApplication.mainPageFlag){
                titleFlag = true;
                headtitleFlag = true;
                mTopPageFlag = true;
            }

            if (headImg != null) {
                showAvatar();
            }

            //用户在其他界面修改了阶段药物
            if(!TextUtils.isEmpty(discoverTime)&&!discoverTime.
                    equals(UserinfoData.getDiscoverTime(context))||ZYApplication.disTimeChangeFlag){
                setCancerDays();
            }
            //用户是否更改了 癌肿和阶段信息 是 则重新请求数据
            try {
                if (isBindFlag||ZYApplication.stepChangeFlag
                        ||(!TextUtils.isEmpty(cancerId) && !cancerId.equals(UserinfoData.getCancerID(context)))
                        || (!TextUtils.isEmpty(stepId) && !stepId.equals(UserinfoData.getStepID(context)))) {
                    if (isBindFlag) isBindFlag = false;//该标识执行之后需要重置
                    initData();
                }
//                Factory.post(this,Const.EGetAllStep);
            }catch (Exception e){

            }

        }catch(Exception e){
            ExceptionUtils.ExceptionToUM(e,getActivity(),"initdata,mainfragment");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LogCustom.i("ZYS", "MainFragment PAUSE");
        try {
            mTopPageFlag = false;
            titleFlag = false;
            headtitleFlag = false;
        }catch(Exception e){
            ExceptionUtils.ExceptionToUM(e,getActivity(),"MainFaragment");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LogCustom.i("ZYS", "MainFragment onItemClick");

    }

    private void setCancerDays(){
        try {
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                    "font/BEBASNEUE.OTF");
            discoverTime = UserinfoData.getDiscoverTime(context);
            if (!TextUtils.isEmpty(discoverTime)&&tv_discover_days!=null) {
                try {
                    tv_discover_days.setTypeface(tf);
                    String timeDay = DataUtils.getDayForStepDetail(discoverTime,0);
                    tv_discover_days.setText(
                            Html.fromHtml("成功抗癌"+"<font color=\"#353535\"><b>"+timeDay+"</b></font>"+"天"));
                }catch (Exception e){

                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,getActivity(),"setCancerDays");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try {
            if(isVisibleToUser){
                Log.i(Const.TAG.ZY_HTTP, "MainFragment onRem");
            }else {
                Log.i(Const.TAG.ZY_HTTP, "MainFragment onPau");
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,getActivity(),"circlefragment");
        }
    }


    private boolean refreshFlag = false;
    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        refreshFlag = true;
        getMainData();
    }

    /**
     * 完成刷新
     *
     * @param tag 0：刷新成功
     *            1：刷新失败
     */
    private void overRefresh(int tag){

    }

    /**
     * 完成加载
     *
     * @param tag 0：加载成功
     *            1：加载失败
     *            2：没有更多了
     */
    private void overLoading(int tag,boolean fit){
        loading = false;
        layout.loadmoreFinish(tag,fit,true);
    }

    private void loadingErrorView(int tag){
        mInformationPage.put(articleIndex,mInformationPage.get(articleIndex)-1);
        overLoading(tag,true);
    }

    private boolean loading = false;
    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

        if (!loading){
            List<InformationEntity> temp = mInformationData.get(articleIndex);
            if (temp==null||temp.size()<pagesize-1){
                overLoading(2,true);
            }else {
                loading = true;
                mInformationPage.put(articleIndex,mInformationPage.get(articleIndex)+1);
                Factory.postPhp(this, Const.PHomeArticleInfo);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTopPageIndex = position;
        dcv.redraw(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void PlanClickBack(String planID) {

    }

    private void showBar(){
        if(bar == null){
            bar = (ProgressBar)findViewById(R.id.pd);
        }
        if(bar.getVisibility() != View.VISIBLE){
            bar.setVisibility(View.VISIBLE);
        }
    }

    private void cancleBar(){
        if(bar == null){
            bar = (ProgressBar)findViewById(R.id.pd);
        }
        if(bar.getVisibility() != View.GONE){
            bar.setVisibility(View.GONE);
        }
    }

    private InfoStepFragment stepFragment;
    private void getNoStepHintDailog(){
        ZYDialog.Builder dialog=new ZYDialog.Builder(getActivity());
        dialog.setTitle("缺少治疗信息");
        dialog.setMessage("如需使用该功能，请完善最新治疗方案。若不知道具体信息，可通过主治医生或陪床家人获取。");
        dialog.setPositiveButton("去完善", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    createInfoStepFragment();
                    dialog.dismiss();
                } catch (Exception e) {
                    ExceptionUtils.ExceptionToUM(e, context, "checkNetworkState");
                }
            }
        });

        dialog.setNegativeButton("暂不使用", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create().show();
    }

    private void createInfoStepFragment(){
        if(stepFragment==null){
            stepFragment = InfoStepFragment.getInstance(this,0);
        }
        stepFragment.show(getActivity().getSupportFragmentManager(), InfoStepFragment.type);
    }

    /**
     * 修正用户头像
     *
     * @param url
     */
    private void correctHeadImgUrl(String url){
        try {
            if(!TextUtils.isEmpty(url)&&url.contains(Const.IMG_URL_HEAD)) return;
            String temp = null;
            if (!TextUtils.isEmpty(url)&&url.startsWith("http")){
                temp = url;
            }else {
                if(!TextUtils.isEmpty(UserinfoData.getTempHeadImgUrl(context))){
                    temp = UserinfoData.getTempHeadImgUrl(context);
                }
            }
            if(!TextUtils.isEmpty(temp)){
                try {
                    LogCustom.i(Const.TAG.ZY_OTHER, "用户图片不正常");
                    Intent intent = new Intent(getActivity(), ZYKaqService.class);
                    intent.setAction(Const.SERVICE_CORRECT_HEAD_IMG);
                    intent.setPackage(getActivity().getPackageName());
                    intent.putExtra(Contants.Headimgurl,temp);
                    getActivity().startService(intent);
                }catch (Exception e){
                    ExceptionUtils.ExceptionToUM(e,getActivity(),"启动图片修正服务出错");
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,context,"图片修正判断出错");
        }

    }

    @Override
    public void dataCallBack(String str, int flag, String tag, Object obj) {
        if (flag == Const.FRAGMENT_INFO_STEP){
            initData();
        }else if (flag == Const.FRAGMENT_EDIT_TAB){
            //加载文章数据
            articleFlag = true;
            Factory.postPhp(this, Const.PHomeArticleInfo);
        }
    }


    class MiddlePageChange implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mMiddleIndex = position;
            dcv_middle.redraw(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    private int articleIndex = 0;
    @Override
    public void OnRecyclerItemClick(View v, int position, String typeID) {
        try {

        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"OnRecyclerItemClick");
        }
    }

    private boolean newsFlag = false;
    private int TAB_HEIGHT = 0;
    @Override
    public void onScroll(int scrollY) {
        if (statusBar2!=null){
            if (scrollY>=DensityUtils.dpToPx(context,165)){
                statusBar2.setAlpha(1);
            }else {
                statusBar2.setAlpha(0);
                if (scrollY==0){
                    statusBar1.setAlpha(0);
                }else {
                    statusBar1.setAlpha((float) scrollY / (float) ChangeHeight);
                }
            }
        }
    }


    @Override
    public void onHelpItemClick(int helpID) {
        if (helpID!=0){
            String kaq = getParamKaqID();
            String helpUrl = "http://help.kaqcn.com/help/question_index?qid=" + helpID +"&" +kaq +"&lt=2";
            startActivity(new Intent(context, ShowDiscuzActivity.class)
                    .putExtra(Const.SHOW_HTML_MAIN_TOP,helpUrl));
        }
    }
}

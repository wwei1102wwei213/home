package com.zeyuan.kyq.fragment.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.Advertising;
import com.zeyuan.kyq.Entity.EditStepBean;
import com.zeyuan.kyq.Entity.EditStepItemEntity;
import com.zeyuan.kyq.Entity.HomePageEntity;
import com.zeyuan.kyq.Entity.InformationEntity;
import com.zeyuan.kyq.Entity.UserInformationEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.AdverticingAdapter;
import com.zeyuan.kyq.adapter.ArticleMainAdapter;
import com.zeyuan.kyq.app.BaseZyFragment;
import com.zeyuan.kyq.bean.StepSummaryBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.FragmentCallBack;
import com.zeyuan.kyq.biz.manager.ClickStatisticsManager;
import com.zeyuan.kyq.biz.manager.FunctionGuideManager;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.AdverticingListActivity;
import com.zeyuan.kyq.view.ArticleDetailActivity;
import com.zeyuan.kyq.view.HomeSymptomActivity;
import com.zeyuan.kyq.view.MedicalRecordActivity;
import com.zeyuan.kyq.view.PersonalizedRecommendActivity;
import com.zeyuan.kyq.view.SearchComplicationActivity;
import com.zeyuan.kyq.view.SimilarActivity;
import com.zeyuan.kyq.widget.CustomArcProgressBar.MyCircleProgressBar;
import com.zeyuan.kyq.widget.DrawCircleView;
import com.zeyuan.kyq.widget.MyListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/12/5.
 *
 * @author wwei
 */
public class DecisionFragment extends BaseZyFragment implements View.OnClickListener, HttpResponseInterface, FragmentCallBack {
    XRefreshView xrv_decision;
    //病人没填写病历提示
    private View view_complete_record;
    private Button btn_to_complete_record;
    //病人的总体情况
    private View view_overall_situation;
    //身体部位图片
    private ImageView iv_body;
    //部位左侧癌种分期显示
    private TextView tv_cancer_left;
    //顶部癌种分期显示
    private TextView tv_cancer_top;
    //当前分期天数显示
    private TextView tv_now_step;
    //确诊时间
    private TextView tv_discover_time;
    //点击修改/查看
    private TextView tv_look_change;
    //总体情况文本控件
    private TextView tv_info_msg;
    //讨论区列表
//    private MyListView lv_discussion;
    //防护与指导
    private LinearLayout ll_guard_advice;
    private LinearLayout ll_guard;//防护
    private TextView tv_content_guard;
    private ImageView iv_content_guard;
    private LinearLayout ll_advice;//指导
    private TextView tv_content_advice;
    private ImageView iv_content_advice;


    //个性化推荐列表
    private MyListView lv_recommend;
    private TextView tv_recommend_more;
    private ArticleMainAdapter adapter;
    private MyCircleProgressBar pb1;
    private MyCircleProgressBar pb2;
    private TextView tv_num_eff;
    private TextView tv_num_comp;
    private TextView tv_eff_str;
    private TextView tv_comp_str;
    private TextView tv_fight_days;
    private MyHandler pgHandler;

    private LinearLayout ll_ad;//广告模块
    ViewPager vp_ad;
    AdverticingAdapter adverticingAdapter;
    DrawCircleView dcv_ad;
    List<Advertising> advertisings = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_decision, container, false);

        initStatusBar2();
        initView();
        setLocalDataAndGetData();
        initAllMessage();
        // getData();
        initListener();

        return rootView;
    }

    //阶段显示控件
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;

    private void initView() {
        pgHandler = new MyHandler(this);
        try {
            xrv_decision = (XRefreshView) findViewById(R.id.xrv_decision);
            xrv_decision.setPinnedTime(1000);
            xrv_decision.setPullLoadEnable(false);
            xrv_decision.setMoveForHorizontal(true);
            view_complete_record = findViewById(R.id.view_complete_record);
            btn_to_complete_record = (Button) findViewById(R.id.btn_to_complete_record);
            view_overall_situation = findViewById(R.id.view_overall_situation);
            iv_body = (ImageView) findViewById(R.id.iv_body);
            tv_cancer_left = (TextView) findViewById(R.id.tv_cancer_left_top);
            tv_cancer_top = (TextView) findViewById(R.id.tv_cancer_top);
            tv_now_step = (TextView) findViewById(R.id.tv_now_step_day);
            tv_discover_time = (TextView) findViewById(R.id.tv_discover_time);
            tv_look_change = (TextView) findViewById(R.id.tv_look_change);
            tv_num_eff = (TextView) findViewById(R.id.tv_num_eff);
            tv_num_comp = (TextView) findViewById(R.id.tv_num_comp);
            tv_eff_str = (TextView) findViewById(R.id.tv_eff_str);
            tv_comp_str = (TextView) findViewById(R.id.tv_comp_str);
            tv_fight_days = (TextView) findViewById(R.id.tv_fight_days);
            tv1 = (TextView) findViewById(R.id.tv1);
            tv2 = (TextView) findViewById(R.id.tv2);
            tv3 = (TextView) findViewById(R.id.tv3);
            pb1 = (MyCircleProgressBar) findViewById(R.id.cpb_1);
            pb2 = (MyCircleProgressBar) findViewById(R.id.cpb_2);

            ll_guard_advice = (LinearLayout) findViewById(R.id.ll_guard_advice);
            ll_guard = (LinearLayout) findViewById(R.id.ll_guard);
            tv_content_guard = (TextView) findViewById(R.id.tv_content_guard);
            iv_content_guard = (ImageView) findViewById(R.id.iv_content_guard);
            ll_advice = (LinearLayout) findViewById(R.id.ll_advice);
            tv_content_advice = (TextView) findViewById(R.id.tv_content_advice);
            iv_content_advice = (ImageView) findViewById(R.id.iv_content_advice);

//            lv_discussion = (MyListView) findViewById(R.id.lv_discussion);
//            lv_discussion.setFocusable(false);
            tv_recommend_more = (TextView) findViewById(R.id.tv_recommend_more);
            lv_recommend = (MyListView) findViewById(R.id.lv_recommend);
            lv_recommend.setFocusable(false);
            adapter = new ArticleMainAdapter(getActivity(), new ArrayList<InformationEntity>(), 0);
            lv_recommend.setAdapter(adapter);
            lv_recommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(getActivity(), ArticleDetailActivity.class).
                            putExtra(Const.INTENT_ARTICLE_ID, "" + adapter.getItem(position).getAid()));
                    ClickStatisticsManager.getInstance().addClickEvent(Const.CLICK_EVENT_6, adapter.getItem(position).getAid() + "");
                }
            });

            ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
            vp_ad = (ViewPager) findViewById(R.id.vp_ad);
            dcv_ad = (DrawCircleView) findViewById(R.id.dcv_ad);
            xrv_decision.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
                @Override
                public void onRefresh() {
                    pgHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setLocalDataAndGetData();
                        }
                    }, 500);
                }
            });
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "initView");
        }
    }

    private void getData() {
        if (UserinfoData.getIsHaveStep(context).equals("1")) {
            Factory.postPhp(this, Const.PGetAppStepUser);//阶段数据
        }
        Factory.postPhp(this, Const.PApi_getStepSummary);
//        Factory.postPhp(this, Const.PHomeArticleInfo);
    }

    //显示 病情总体情况or提醒用户完善病历
    private void setViewVisibility() {
        if (UserinfoData.getIsHaveStep(context).equals("1")) {//病人已填写阶段数据
            view_complete_record.setVisibility(View.GONE);
            btn_to_complete_record.setEnabled(false);
            view_overall_situation.setVisibility(View.VISIBLE);
        } else {//病人未填写阶段数据
            view_complete_record.setVisibility(View.VISIBLE);
            btn_to_complete_record.setEnabled(true);
            view_overall_situation.setVisibility(View.GONE);
        }
    }

    //加载本地的病情等数据,然后获取其他网络数据
    private void setLocalDataAndGetData() {
        Observable.create(new ObservableOnSubscribe<Message>() {
            @Override
            public void subscribe(ObservableEmitter<Message> e) throws Exception {
                Message message = new Message();
                int cancerImgId = UiUtils.getCancerTypeImage(UserinfoData.getCancerID(context));
                message.what = 1;
                message.obj = cancerImgId;
                e.onNext(message);

                message = new Message();
                String CancerId = UserinfoData.getCancerID(context);//癌种ID
                String cancer = MapDataUtils.getCancerValues(CancerId);//癌种名称
                String Peroid = UserinfoData.getPeriodID(context);//分期ID
                String digit = MapDataUtils.getDigitValues(Peroid);
                digit = TextUtils.isEmpty(digit) || "未知".equals(digit) ? "未知分期" : digit + "期";
                message.what = 3;
                message.obj = cancer + "," + digit;
                e.onNext(message);

                message = new Message();
                String hasStep = UserinfoData.getIsHaveStep(context);//是否有阶段
                message.what = 4;
                message.obj = hasStep;
                e.onNext(message);

                message = new Message();
                String discover_time = DataUtils.getRecordDate(Long.parseLong(UserinfoData.getDiscoverTime(context))) + "\n确诊";
                message.what = 5;
                message.obj = discover_time;
                e.onNext(message);

                message = new Message();
                String fight_time = "(成功抗癌" + DataUtils.getDayForStepDetail(UserinfoData.getDiscoverTime(context), 0) + "天)";
                message.what = 6;
                message.obj = fight_time;
                e.onNext(message);

                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Message>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Message message) {
                        switch (message.what) {
                            case 1:
                                iv_body.setImageResource((Integer) message.obj);
                                break;
                            case 3:
                                tv_cancer_top.setText("病人" + message.obj + "经历过：");
                                String info = ((String) message.obj).replace(",", "");
                                tv_cancer_left.setText("");
                                for (char c : info.toCharArray()) {
                                    tv_cancer_left.append("\n" + c);
                                }
                                break;
                            case 4:
                                if (message.obj.equals("0")) {
                                    tv_now_step.setText("当前还没有经历过治疗阶段");
                                }
                                break;
                            case 5:
                                tv_discover_time.setText((String) message.obj);
                                break;
                            case 6:
                                tv_fight_days.setText((String) message.obj);
                                break;

                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        setViewVisibility();
                        getData();
                    }
                });
    }

    private void initListener() {
        btn_to_complete_record.setOnClickListener(this);
        findViewById(R.id.v_eff).setOnClickListener(this);
        findViewById(R.id.v_comp).setOnClickListener(this);
        findViewById(R.id.tv_symptom).setOnClickListener(this);
        findViewById(R.id.tv_diagnosis).setOnClickListener(this);
        findViewById(R.id.tv_similar).setOnClickListener(this);
        findViewById(R.id.ll_medicalRecord).setOnClickListener(this);
        tv_recommend_more.setOnClickListener(this);
        ll_guard.setOnClickListener(this);
        ll_advice.setOnClickListener(this);
    }


    private void initAllMessage() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_look:

                break;
            case R.id.btn_to_complete_record://完善病历
                String isHas = UserinfoData.getIsHaveStep(context.getApplicationContext());
                if (isHas.equals("0")) {
                    UiUtils.startIndividuationTreatment(getActivity());
                    // showInfoStepFragment();
                } else {
                    startActivity(new Intent(getActivity(), MedicalRecordActivity.class));
                }
                break;
            case R.id.ll_medicalRecord:
                startActivity(new Intent(getActivity(), MedicalRecordActivity.class));
                break;
            case R.id.v_eff://副作用
                Factory.onEvent(getActivity(), Const.EVENT_SideEffect, Const.EVENTFLAG, null);
                startActivity(new Intent(getActivity(), SearchComplicationActivity.class)
                        .putExtra(Const.SEARCH_POLICY_TYPE, Const.TYPE_EFFECT));
                break;
            case R.id.v_comp://并发症
                Factory.onEvent(getActivity(), Const.EVENT_SideEffect, Const.EVENTFLAG, null);
                startActivity(new Intent(getActivity(), SearchComplicationActivity.class)
                        .putExtra(Const.SEARCH_POLICY_TYPE, Const.TYPE_COMPLICATION));
                break;
            case R.id.tv_symptom://症状百科
                startActivity(new Intent(context, HomeSymptomActivity.class));
                break;
            case R.id.tv_diagnosis://预后方案
                HomePageEntity homePageEntity = new HomePageEntity();
                homePageEntity.setSkiptype("10");
                homePageEntity.setSign_a("6");
                UiUtils.toMenuJump(context, homePageEntity, null, false, getActivity());
//                startActivity(new Intent(context, PrognosisProgramActivity.class));
                break;
            case R.id.tv_similar:
                startActivity(new Intent(context, SimilarActivity.class));
                break;
            case R.id.ll_guard:
                startActivity(new Intent(context, AdverticingListActivity.class).putExtra("location", "2"));
                break;
            case R.id.ll_advice:
                startActivity(new Intent(context, AdverticingListActivity.class).putExtra("location", "3"));
                break;
            case R.id.tv_recommend_more:
                startActivity(new Intent(context, PersonalizedRecommendActivity.class));
                break;
            case R.id.tv_ad_name:
                try {
                    HomePageEntity homePageEntity1 = new HomePageEntity();
                    homePageEntity1.setSkiptype(advertisings.get(adPosition % advertisings.size()).getSkiptype());
                    homePageEntity1.setSign_a(advertisings.get(adPosition % advertisings.size()).getSign_a());
                    homePageEntity1.setSign_b(advertisings.get(adPosition % advertisings.size()).getSign_b());
                    UiUtils.toMenuJump(context, homePageEntity1, null, false, null);
                    ClickStatisticsManager.getInstance().addClickEvent(Const.CLICK_EVENT_6, advertisings.get(adPosition % advertisings.size()).getId());
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "tv_ad_name");
                }
                break;
        }
    }

    //填写完患者资料的回调方法
    @Override
    public void dataCallBack(String str, int flag, String tag, Object obj) {
        if (flag == Const.FRAGMENT_INFO_STEP) {
            setLocalDataAndGetData();
        }
    }

    //    InfoStepFragment stepFragment;
//
//    //显示选择治疗方案页面
//    private void showInfoStepFragment() {
//        if (stepFragment == null) {
//            stepFragment = InfoStepFragment.getInstance(this, 0);
//        }
//        if (stepFragment.getDialog() == null || !stepFragment.getDialog().isShowing())
//            stepFragment.show(getActivity().getSupportFragmentManager(), InfoStepFragment.type);
//    }


    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(context));
        if (tag == Const.PApi_getStepSummary) {
            if (UserinfoData.getIsHaveStep(context).equals("1")) {
                map.put("cancerid", UserinfoData.getCancerID(context));
                map.put("IsHaveStep", "1");
                map.put("StepID", UserinfoData.getStepID(context));
                map.put("CureID", MapDataUtils.getAllCureconfID(UserinfoData.getStepID(context)));
            } else {
                map.put("cancerid", UserinfoData.getCancerID(context));
                map.put("IsHaveStep", "0");
                map.put("StepID", "0");
                map.put("CureID", "0");
            }

        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        xrv_decision.stopRefresh();
        if (flag == Const.PHomeArticleInfo) {
            UserInformationEntity entity = (UserInformationEntity) response;
            if (Const.RESULT.equals(entity.getiResult())) {
                List<InformationEntity> temp = entity.getData();
                if (temp != null && temp.size() > 0) {
                    if (temp.size() < 3) {
                        adapter.update(temp);
                    } else {
                        List<InformationEntity> temp_del = new ArrayList<>();
                        for (int i = 0; i < 3; i++) {
                            temp_del.add(temp.get(i));
                        }
                        adapter.update(temp_del);
                    }
                }
            } else {

            }

        } else if (flag == Const.PGetPorCommentList) {

        } else if (flag == Const.PApi_getStepSummary) {
            final StepSummaryBean bean = (StepSummaryBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                // final int i = 0;
                setProgress(1, bean.getEffectNum());
                setProgress(2, bean.getComplicationNum());
                tv_num_eff.setText("" + bean.getEffectNum() + "个");
                tv_num_comp.setText("" + bean.getComplicationNum() + "个");
                tv_eff_str.setText(bean.getEffectStr());
                tv_comp_str.setText(bean.getComplicationStr());
                setGuardAndAdvice(bean.getAdvertising_2(), bean.getAdvertising_3());

                List<InformationEntity> temp = bean.getData();
                if (temp != null && temp.size() > 0) {
                    adapter.update(temp);
                }
                setAdData(bean.getAdvertising_1());
            }
        } else if (flag == Const.PGetAppStepUser) {
            EditStepBean bean = (EditStepBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                List<EditStepItemEntity> stepDataList = bean.getData();
                setStepInfo(stepDataList);
            }
        }
    }


    private void setStepInfo(List<EditStepItemEntity> stepDataList) {
        if (stepDataList == null || stepDataList.size() == 0)
            return;
        if (pgHandler != null) {
            pgHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!getActivity().isFinishing()) {
                        int[] xy = new int[2];
                        view_overall_situation.getLocationInWindow(xy);
                        int width = view_overall_situation.getWidth();
                        int height = view_overall_situation.getHeight();
                        FunctionGuideManager.getInstance().showOverallSituationGuide(getActivity(), xy, width, height);
                    }

                }
            }, 1000);
        }


        EditStepItemEntity currentE = null;
        for (EditStepItemEntity e : stepDataList
                ) {
            if (e.getIsNowStep() == 1) {
                currentE = e;
            }
        }
        if (currentE != null) {
            tv_now_step.setText(MapDataUtils.getAllStepName(currentE.getStepID() + "") + DataUtils.getDayForStepDetail(currentE.getStartTime() + "", currentE.getEndTime() + ""));
            stepDataList.remove(currentE);
        }
        if (stepDataList.size() == 0) {
            tv2.setVisibility(View.VISIBLE);
            tv2.setText("点击并完善更多治疗经历");
            return;
        }

        tv1.setVisibility(View.GONE);
        tv2.setVisibility(View.GONE);
        tv3.setVisibility(View.GONE);
        switch (stepDataList.size()) {
            case 1:
                tv1.setVisibility(View.VISIBLE);
                tv1.setText(MapDataUtils.getAllStepName(stepDataList.get(0).getStepID() + "") + "(" + DataUtils.getDayForStepDetail(stepDataList.get(0).getStartTime() + "", stepDataList.get(0).getEndTime() + "") + ")");
                break;
            case 2:
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv1.setText(MapDataUtils.getAllStepName(stepDataList.get(0).getStepID() + "") + "(" + DataUtils.getDayForStepDetail(stepDataList.get(0).getStartTime() + "", stepDataList.get(0).getEndTime() + "") + ")");
                tv2.setText(MapDataUtils.getAllStepName(stepDataList.get(1).getStepID() + "") + "(" + DataUtils.getDayForStepDetail(stepDataList.get(1).getStartTime() + "", stepDataList.get(1).getEndTime() + "") + ")");
                break;
            case 3:
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
                tv1.setText(MapDataUtils.getAllStepName(stepDataList.get(0).getStepID() + "") + "(" + DataUtils.getDayForStepDetail(stepDataList.get(0).getStartTime() + "", stepDataList.get(0).getEndTime() + "") + ")");
                tv2.setText(MapDataUtils.getAllStepName(stepDataList.get(1).getStepID() + "") + "(" + DataUtils.getDayForStepDetail(stepDataList.get(1).getStartTime() + "", stepDataList.get(1).getEndTime() + "") + ")");
                tv3.setText(MapDataUtils.getAllStepName(stepDataList.get(2).getStepID() + "") + "(" + DataUtils.getDayForStepDetail(stepDataList.get(2).getStartTime() + "", stepDataList.get(2).getEndTime() + "") + ")");
                break;
            default:
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
                tv1.setText(MapDataUtils.getAllStepName(stepDataList.get(0).getStepID() + "") + "(" + DataUtils.getDayForStepDetail(stepDataList.get(0).getStartTime() + "", stepDataList.get(0).getEndTime() + "") + ")");
                tv2.setText("...");
                tv3.setText(MapDataUtils.getAllStepName(stepDataList.get(stepDataList.size() - 1).getStepID() + "") + "(" + DataUtils.getDayForStepDetail(stepDataList.get(stepDataList.size() - 1).getStartTime() + "", stepDataList.get(stepDataList.size() - 1).getEndTime() + "") + ")");
                break;
        }

    }

    //设置防护支持与营养指导的数据
    private void setGuardAndAdvice(Advertising guard, Advertising advice) {
        ll_guard_advice.setVisibility(View.GONE);
        ll_guard.setVisibility(View.GONE);
        ll_advice.setVisibility(View.GONE);
        try {
            if (TextUtils.isEmpty(guard.getPic_oos()) && TextUtils.isEmpty(advice.getPic_oos())) {
                return;
            } else {
                ll_guard_advice.setVisibility(View.VISIBLE);
            }
            if (!TextUtils.isEmpty(guard.getPic_oos())) {
                ll_guard.setVisibility(View.VISIBLE);
                tv_content_guard.setText("针对" + MapDataUtils.getAllStepName(UserinfoData.getStepID(context)) + "治疗肿瘤进行身体补偿性改善");
                Glide.with(context).load(guard.getPic_oos()).error(R.mipmap.loading_fail).into(iv_content_guard);
            }
            if (!TextUtils.isEmpty(advice.getPic_oos())) {
                ll_advice.setVisibility(View.VISIBLE);
                tv_content_advice.setText("针对" + MapDataUtils.getAllStepName(UserinfoData.getStepID(context)) + "治疗肿瘤营养知识支持");
                Glide.with(context).load(advice.getPic_oos()).error(R.mipmap.loading_fail).into(iv_content_advice);
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "setGuardAndAdvice");
        }
    }

    //设置广告数据
    private void setAdData(final List<Advertising> advertisings) {
        if (advertisings != null) {
            this.advertisings.clear();
            this.advertisings.addAll(advertisings);
            final TextView tv_ad_name = (TextView) findViewById(R.id.tv_ad_name);
            if (advertisings.size() > 0) {
                ll_ad.setVisibility(View.VISIBLE);
                tv_ad_name.setOnClickListener(this);
                if (!TextUtils.isEmpty(advertisings.get(0).getTitle()))
                    tv_ad_name.setText(advertisings.get(0).getTitle());
                adverticingAdapter = new AdverticingAdapter(context, advertisings);
                vp_ad.setAdapter(adverticingAdapter);
                startAdVp();
                dcv_ad.setVisibility(View.VISIBLE);
                dcv_ad.setDrawCricle(advertisings.size(), 6, Color.parseColor("#4c4c4c"), Color.parseColor("#FFFFFF"));
                dcv_ad.redraw(0);
                vp_ad.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        try {
                            dcv_ad.redraw(position % advertisings.size());
                            adPosition = position;
                            tv_ad_name.setText(advertisings.get(position % advertisings.size()).getTitle());
                        } catch (Exception e) {
                            ExceptionUtils.ExceptionSend(e, "addOnPageChangeListener");
                        }

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            } else {
                ll_ad.setVisibility(View.GONE);
                dcv_ad.setVisibility(View.GONE);
                stopAdVp();
            }


        } else {
            ll_ad.setVisibility(View.GONE);
        }
    }

    private void setProgress(int witch, int progress) {
//        if (progress == 0)
//            return;
        ProgressThread progressThread = new ProgressThread(witch, progress * 4);
        Thread thread = new Thread(progressThread);
        thread.start();
    }

    public void updateProgressBar1(int pg) {
        pb1.setProgress(pg);

    }

    public void updateProgressBar2(int pg) {
        pb2.setProgress(pg);
    }

    @Override
    public void showLoading(int flag) {

    }

    @Override
    public void hideLoading(int flag) {

    }

    @Override
    public void showError(int flag) {
        xrv_decision.stopRefresh();
    }

    private static class MyHandler extends Handler {
        WeakReference<DecisionFragment> weakReference;

        public MyHandler(DecisionFragment decisionFragment) {
            weakReference = new WeakReference<>(decisionFragment);
        }

        DecisionFragment decisionFragment;

        @Override
        public void handleMessage(Message msg) {
            decisionFragment = weakReference.get();
            switch (msg.what) {
                case 1:
                    decisionFragment.updateProgressBar1((int) msg.obj);
                    break;
                case 2:
                    decisionFragment.updateProgressBar2((int) msg.obj);
                    break;
            }
        }
    }

    private class ProgressThread implements Runnable {
        int max;
        int witchPG;

        private ProgressThread(int witch, int max) {
            this.max = max;
            this.witchPG = witch;
        }

        @Override
        public void run() {
            for (int i = 0; i <= max; i++) {
                if (isFinished)
                    return;
                Message msg = new Message();
                msg.what = witchPG;
                msg.obj = i;
                SystemClock.sleep(16);
                pgHandler.sendMessage(msg);
            }
        }
    }

    private int adPosition = 0;
    private boolean isStop;
    //切换广告的任务
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isStop) {
                try {
                    adPosition++;
                    vp_ad.setCurrentItem(adPosition);
                    pgHandler.postDelayed(runnable, 5000);
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "广告runnable");
                }
            }
        }
    };

    //开启轮播
    private void startAdVp() {
        adPosition = 0;
        pgHandler.removeCallbacks(runnable);
        pgHandler.postDelayed(runnable, 5000);

    }

    //关闭轮播
    private void stopAdVp() {
        pgHandler.removeCallbacks(runnable);
        isStop = true;
    }

    boolean isFinished = false;

    @Override
    public void onDestroy() {
        isFinished = true;
        stopAdVp();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 9 && resultCode == -1) {
            showToast("圈圈助手收到选好癌种的消息！");
            setLocalDataAndGetData();
        }
    }
}

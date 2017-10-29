package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.CancerSizeItemEntity;
import com.zeyuan.kyq.Entity.MedicalBaseBean;
import com.zeyuan.kyq.Entity.PatientDataBean;
import com.zeyuan.kyq.Entity.PatientDataEntity;
import com.zeyuan.kyq.Entity.RecordClassifyHistoryEntity;
import com.zeyuan.kyq.Entity.RecordItemEntity;
import com.zeyuan.kyq.Entity.StepUserEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.CancerSizeChartAdapter;
import com.zeyuan.kyq.adapter.MedicalRecordAdapter;
import com.zeyuan.kyq.adapter.PathologyPatientAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.onEditItemListener;
import com.zeyuan.kyq.biz.manager.FunctionGuideManager;
import com.zeyuan.kyq.fragment.ShareFragment;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ConstUtils;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.DensityUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.MyListView;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/6.
 * <p>
 * 病历页面
 *
 * @author wwei
 */
public class MedicalRecordActivity extends BaseActivity implements View.OnClickListener, HttpResponseInterface
        , onEditItemListener {

    private int InfoID;
    private static final String SHARE_URL = "http://help.kaqcn.com/Api/getStepUser?InfoID=";
    private boolean FRIST_IN = true;
    private MyHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_medical_record);
        //初始化
        init();
        //设置视图
        initView();
        //设置监听
        initListener();
        //设置数据
        initData();
    }

    private View v_loading;
    private View v_body;

    private void init() {
        try {
            mHandler = new MyHandler(this);
            v_loading = findViewById(R.id.v_loading);
            v_body = findViewById(R.id.v_body);
            iv_move = findViewById(R.id.iv_move);
            a = AnimationUtils.loadAnimation(this, R.anim.move_medical_record);
            a.setAnimationListener(new ReStartAnimationListener());
            a.setFillAfter(false);
            iv_move.startAnimation(a);
            iv_move.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "init");
        }
    }

    private TextView tv_patient_detail;
    private TextView tv_basic_detail;
    private TextView tv_status;
    private TextView tv_other_strichen;
    private TextView tv_translate;
    private TextView tv_gene;
    private LinearLayout v_cancer_chart;
    private LinearLayout v_quota_chart;
    private View v_pathology;
    private MyListView lv_pathology;
    private PathologyPatientAdapter adapter_pathology;
    private View v_diagnosis;
    private MyListView lv_diagnosis;
    private PathologyPatientAdapter adapter_diagnosis;
    private MedicalRecordAdapter adapter;
    private MyListView lv;
    private TextView tv_discover_time;
    private TextView tv_discover_days;
    private View v_basic;
    private View v_other_stricken;
    private View v_status;
    //加载动画
    private View iv_move;
    private Animation a;

    private void initView() {
        InfoID = Integer.valueOf(UserinfoData.getInfoID(this));
        //患者资料控件
        tv_patient_detail = (TextView) findViewById(R.id.tv_patient_detail);
        tv_basic_detail = (TextView) findViewById(R.id.tv_basic_detail);
        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_other_strichen = (TextView) findViewById(R.id.tv_other_stricken);
        tv_translate = (TextView) findViewById(R.id.tv_translate);
        tv_gene = (TextView) findViewById(R.id.tv_gene);
        v_basic = findViewById(R.id.v_basic_detail);
        v_other_stricken = findViewById(R.id.v_other_stricken);
        v_status = findViewById(R.id.v_status);
        //肿瘤大小控件
        v_cancer_chart = (LinearLayout) findViewById(R.id.v_chart_cancer_size);
        //指标变化控件
        v_quota_chart = (LinearLayout) findViewById(R.id.v_chart_quota_change);
        //病理报告控件
        v_pathology = findViewById(R.id.v_pathology);
        lv_pathology = (MyListView) findViewById(R.id.lv_pathology);
        adapter_pathology = new PathologyPatientAdapter(this, getPathologyPhotoWidth(),
                new ArrayList<RecordItemEntity>(), Const.RECORD_TYPE_8);
        lv_pathology.setAdapter(adapter_pathology);
        v_diagnosis = findViewById(R.id.v_diagnosis);
        lv_diagnosis = (MyListView) findViewById(R.id.lv_diagnosis);
        adapter_diagnosis = new PathologyPatientAdapter(this, getPathologyPhotoWidth(),
                new ArrayList<RecordItemEntity>(), Const.RECORD_TYPE_7);
        lv_diagnosis.setAdapter(adapter_diagnosis);
        //阶段列表
        lv = (MyListView) findViewById(R.id.lv);
        adapter = new MedicalRecordAdapter(this, new ArrayList<StepUserEntity>(), getPathologyPhotoWidth(), this);
        lv.setAdapter(adapter);
        tv_discover_time = (TextView) findViewById(R.id.tv_discover_time);
        tv_discover_days = (TextView) findViewById(R.id.tv_discover_days);

    }

    private void initListener() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_edit_patient_msg).setOnClickListener(this);
        findViewById(R.id.tv_look_all_cancer_size).setOnClickListener(this);
        findViewById(R.id.tv_look_all_quota_change).setOnClickListener(this);
        findViewById(R.id.tv_look_all_pathology).setOnClickListener(this);
        findViewById(R.id.tv_look_all_diagnosis).setOnClickListener(this);
        findViewById(R.id.v_edit_step).setOnClickListener(this);
        findViewById(R.id.v_to_record).setOnClickListener(this);
        findViewById(R.id.v_save).setOnClickListener(this);
        findViewById(R.id.tv_help).setOnClickListener(this);
    }

    private void initData() {
        Factory.postPhp(this, Const.PGetUserInfoForApp);
        Factory.postPhp(this, Const.PGetCancerInfoForApp);
        Factory.postPhp(this, Const.PGetRecordBookForApp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.v_edit_step://去编辑资料
                startActivityForResult(new Intent(this, EditStepNewActivity.class)
                                .putExtra(Const.RECORD_REQUEST_FLAG, true)
                        , Const.REQUEST_CODE_MEDICAL_RECORD_ACTIVITY);
                break;
            case R.id.v_to_record://去记录
                startActivityForResult(new Intent(this, RecordClassifyActivity.class)
                                .putExtra(Const.RECORD_REQUEST_FLAG, true)
                        , Const.REQUEST_CODE_MEDICAL_RECORD_ACTIVITY);
                break;
            case R.id.tv_edit_patient_msg://编辑资料
                startActivityForResult(new Intent(this, PatientDataActivity.class)
                                .putExtra(Const.RECORD_REQUEST_FLAG, true)
                        , Const.REQUEST_CODE_MEDICAL_RECORD_ACTIVITY);
                break;
            case R.id.tv_look_all_cancer_size://查看更多--肿瘤
                startActivityForResult(new Intent(this, RecordChartActivity.class)
                                .putExtra(Const.RECORD_CHART_TYPE, Const.RECORD_TYPE_14)
                        , Const.REQUEST_CODE_MEDICAL_RECORD_ACTIVITY);
                break;
            case R.id.tv_look_all_quota_change://查看更多--指标
                startActivityForResult(new Intent(this, RecordChartActivity.class)
                                .putExtra(Const.RECORD_CHART_TYPE, Const.RECORD_TYPE_15)
                        , Const.REQUEST_CODE_MEDICAL_RECORD_ACTIVITY);
                break;
            case R.id.tv_look_all_pathology://查看更多--病理报告
                startActivityForResult(new Intent(this, PathologyActivity.class)
                                .putExtra(Const.RECORD_REQUEST_FLAG, true)
                                .putExtra(Const.RECORD_CLASSIFY_TYPE, Const.RECORD_TYPE_8)
                        , Const.REQUEST_CODE_MEDICAL_RECORD_ACTIVITY);
                break;
            case R.id.tv_look_all_diagnosis://查看更多--诊断书
                startActivityForResult(new Intent(this, PathologyActivity.class)
                                .putExtra(Const.RECORD_CLASSIFY_TYPE, Const.RECORD_TYPE_7)
                                .putExtra(Const.RECORD_REQUEST_FLAG, true)
                        , Const.REQUEST_CODE_MEDICAL_RECORD_ACTIVITY);
                break;
            case R.id.v_save://分享
                toShare();
                break;
            case R.id.tv_help://帮助
                startActivity(new Intent(this, ArticleDetailActivity.class)
                        .putExtra(Const.INTENT_ARTICLE_ID, "5514"));
                break;
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, InfoID + "");
        if (tag == Const.PGetCancerInfoForApp) {
            map.put("ViewType", "1");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    private List<CancerSizeItemEntity> cancerChartList = null;
    private List<CancerSizeItemEntity> quotaChartList = null;

    @Override
    public void toActivity(Object response, int flag) {

        if (flag == Const.PGetRecordBookForApp) {
            MedicalBaseBean bean = (MedicalBaseBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                //设置病理报告
                List<RecordItemEntity> other = bean.getReport();
                if (other != null && other.size() > 0) {
                    v_pathology.setVisibility(View.VISIBLE);
                    adapter_pathology.update(other);
                } else {
                    v_pathology.setVisibility(View.GONE);
                }
                //设置诊断报告
                List<RecordItemEntity> diagnosis = bean.getDiagnosis();
                if (diagnosis != null && diagnosis.size() > 0) {
                    v_diagnosis.setVisibility(View.VISIBLE);
                    adapter_diagnosis.update(diagnosis);
                } else {
                    v_diagnosis.setVisibility(View.GONE);
                }
                List<StepUserEntity> list = ConstUtils.getListForMedicalRecord(bean.getData());
                LogCustom.i("ZYS", "LIST:" + list.toString());
                adapter.update(list);
            }
        } else if (flag == Const.PGetUserInfoForApp) {
            PatientDataBean bean = (PatientDataBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                setViews(bean.getData());
            }
        } else if (flag == Const.PGetCancerInfoForApp) {
            RecordClassifyHistoryEntity entity = (RecordClassifyHistoryEntity) response;
            if (Const.RESULT.equals(entity.getiResult())) {
                cancerChartList = entity.getTumourInfo();
                initChartCancer(cancerChartList);
                quotaChartList = entity.getCancerMark();
                initChartQuota(quotaChartList);
            } else {
                showToast("未找到图表信息");
            }
        }

    }


    @Override
    public void showLoading(int flag) {

    }


    @Override
    public void hideLoading(int flag) {
        if (flag == Const.PGetUserInfoForApp) {
            if (FRIST_IN) {
                toFirst(flag);
            }
        } else if (flag == Const.PGetCancerInfoForApp) {
            if (FRIST_IN) {
                toFirst(flag);
            }
        } else if (flag == Const.PGetRecordBookForApp) {
            if (FRIST_IN) {
                toFirst(flag);
            }
        }
    }

    @Override
    public void showError(int flag) {
        if (flag == Const.PGetUserInfoForApp) {
            if (FRIST_IN) {
                toFirst(flag);
            }
        } else if (flag == Const.PGetCancerInfoForApp) {
            if (FRIST_IN) {
                toFirst(flag);
            }
        } else if (flag == Const.PGetRecordBookForApp) {
            if (FRIST_IN) {
                toFirst(flag);
            }
        }
        showToast("网络请求失败");
    }

    private boolean f1 = false;
    private boolean f2 = false;
    private boolean f3 = false;

    private void toFirst(int flag) {
        try {
            if (flag == Const.PGetUserInfoForApp) {
                f1 = true;
            } else if (flag == Const.PGetCancerInfoForApp) {
                f2 = true;
            } else if (flag == Const.PGetRecordBookForApp) {
                f3 = true;
            }
            if (f1 && f2 && f3) {

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iv_move.clearAnimation();
                        FRIST_IN = false;
                        mHandler.sendEmptyMessage(LOADING_FINISH);
                    }
                }, 1000);

            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "toFirst");
        }

    }

    private void loadingFinish() {
        Animation b = AnimationUtils.loadAnimation(this, R.anim.activity_alpha_out_fast);
        v_loading.setVisibility(View.GONE);
        v_loading.startAnimation(b);
        Animation c = AnimationUtils.loadAnimation(this, R.anim.activity_alpha_in);
        v_body.setVisibility(View.VISIBLE);
        v_body.startAnimation(c);
        FunctionGuideManager.getInstance().showMedicalRecordShareGuide(MedicalRecordActivity.this);
        FunctionGuideManager.getInstance().showMedicalRecordGuide(MedicalRecordActivity.this);
    }

    private final String[] Body_Status = new String[]{"活动能力完全正常", "能自由走动及从事轻体力活动，但不能从事较重的体力活动",
            "能自由走动及生活自理，没有工作能力，一大半时间起床活动", "卧床不起，生活不能自理", "死亡/频危"};

    //患者资料UI设置
    private void setViews(PatientDataEntity entity) {
        try {
            String cancer = MapDataUtils.getCancerValues(entity.getCancer() + "");
            String period = MapDataUtils.getDigitValues(entity.getPeriodID() + "");
            String time = DataUtils.getPatientDate(entity.getDiscoverTime() + "");
            tv_status.setText(entity.getBodyStatus() > 0 ? getBodyStatusString(entity.getBodyStatus() - 1) : "未填写");
            if (TextUtils.isEmpty(entity.getTransferGene())) {
                tv_gene.setText(Html.fromHtml("无" +
                        "<font color=\"#999999\" font-size=20px><small>（请到记录病情中记录）</small></font>"));
            } else {
                tv_gene.setText(Html.fromHtml(entity.getTransferGene()
                        + "<font color=\"#999999\"><small>（修改请到相应的记录中点击编辑）</small></font>"));
            }
            if (TextUtils.isEmpty(entity.getTransferRecord())) {
                tv_translate.setText(Html.fromHtml("无" +
                        "<font color=\"#999999\" font-size=20px><small>（请到记录病情中记录）</small></font>"));
            } else {
                tv_translate.setText(Html.fromHtml(entity.getTransferRecord()
                        + "<font color=\"#999999\"><small>（修改请到相应的记录中点击编辑）</small></font>"));
            }
            tv_patient_detail.setText("");
            tv_patient_detail.append("病人目前阶段：");
            tv_patient_detail.append(Html.fromHtml("<font><b>" +
                    MapDataUtils.getAllStepName(UserinfoData.getStepID(this)) + "</b></font>"));
            tv_patient_detail.append(",所患癌种为");
            tv_patient_detail.append(Html.fromHtml("<font><b>" + cancer + "," +
                    (TextUtils.isEmpty(period) ? "未知分期" : (period + "期")) + "</b></font>"));
            tv_patient_detail.append(",于");
            tv_patient_detail.append(Html.fromHtml("<font><b>" + time + "</b></font>"));
            tv_patient_detail.append("确诊");
            tv_discover_time.setText(time);
            tv_discover_days.setText("成功抗癌" + DataUtils.getDayForEditStep((int) (entity.getDiscoverTime())
                    , Integer.valueOf(System.currentTimeMillis() / 1000 + "")));

            if (entity.getSex() == 0 && entity.getAge() == 0 && entity.getPatientHeight() == 0 && entity.getPatientWeight() == 0) {
                v_basic.setVisibility(View.GONE);
            } else {
                String basic = (entity.getSex() == 1 ? "男" : (entity.getSex() == 2 ? "女" : "未填写性别")) + "；" +
                        (entity.getAge() == 0 ? "未填写年龄" : entity.getAge() + "岁") + "；" +
                        (entity.getPatientHeight() == 0 ? "未填写身高" : "高" + entity.getPatientHeight() + "cm") + "；" +
                        (entity.getPatientWeight() == 0 ? "未填写体重" : ("重" + entity.getPatientWeight() + "kg"));
                v_basic.setVisibility(View.VISIBLE);
                tv_basic_detail.setText(basic);
            }

            String other = UiUtils.getOtherStrickenString(entity);
            if (TextUtils.isEmpty(other)) {
                v_other_stricken.setVisibility(View.GONE);
            } else {
                v_other_stricken.setVisibility(View.VISIBLE);
                tv_other_strichen.setText(other);
            }

            if (entity.getBodyStatus() == 0) {
                v_status.setVisibility(View.GONE);
            } else {
                v_status.setVisibility(View.VISIBLE);
                tv_status.setText(getBodyStatusString(entity.getBodyStatus() - 1));
            }

        } catch (Exception e) {

        }
    }

    //获取体能现状字符串，防止数组越界
    private String getBodyStatusString(int position) {
        if (position < 0 || position > Body_Status.length - 1) {
            return "";
        }
        return Body_Status[position];
    }

    //肿瘤大小图表UI
    private void initChartCancer(List<CancerSizeItemEntity> list) {
        if (list == null || list.size() == 0) {
            v_cancer_chart.setVisibility(View.GONE);
        } else {
            List<CancerSizeItemEntity> temp = new ArrayList<>();
            for (CancerSizeItemEntity entity : list) {
                if (entity.getIs_show() == 1) {
                    temp.add(entity);
                }
            }
            if (temp.size() > 0) {
                v_cancer_chart.setVisibility(View.VISIBLE);
                v_cancer_chart.removeAllViews();
                SparseArray<List<CancerSizeItemEntity>> array = ConstUtils.getArrayForCancerSizeList(temp, InfoID);
                if (array != null && array.size() > 0) {
                    int[] TypeIDArray = ConstUtils.getTypeIDArrayAll(array);
                    for (int i = 0; i < TypeIDArray.length; i++) {
                        getChartItemView(TypeIDArray[i], array, false);
                    }
                }
            } else {
                v_cancer_chart.setVisibility(View.GONE);
            }
        }
    }

    //肿标变化图表UI
    private void initChartQuota(List<CancerSizeItemEntity> list) {
        if (list == null || list.size() == 0) {
            v_quota_chart.setVisibility(View.GONE);
        } else {
            List<CancerSizeItemEntity> temp = new ArrayList<>();
            for (CancerSizeItemEntity entity : list) {
                if (entity.getIs_show() == 1) {
                    temp.add(entity);
                }
            }
            if (temp.size() > 0) {
                v_quota_chart.setVisibility(View.VISIBLE);
                v_quota_chart.removeAllViews();
                SparseArray<List<CancerSizeItemEntity>> array = ConstUtils.getArrayForCancerSizeList(temp, InfoID);
                if (array != null && array.size() > 0) {
                    int[] TypeIDArray = ConstUtils.getTypeIDArrayAll(array);
                    for (int i = 0; i < TypeIDArray.length; i++) {
                        getChartItemView(TypeIDArray[i], array, true);
                    }
                }
            } else {
                v_quota_chart.setVisibility(View.GONE);
            }
        }
    }

    //单个图表
    private void getChartItemView(int TypeID, SparseArray<List<CancerSizeItemEntity>> array, boolean isQuota) {
        try {
            View v = LayoutInflater.from(this).inflate(R.layout.layout_chart_item_patient, v_cancer_chart, false);
            List<CancerSizeItemEntity> temp = array.get(TypeID);
            if (temp != null && temp.size() > 0) {
                v.findViewById(R.id.v_color_1).setBackgroundResource(UiUtils.getBGColorForTypeID(TypeID));
                v.findViewById(R.id.v_color_2).setBackgroundResource(UiUtils.getColorForTypeID(TypeID));
                TextView name = (TextView) v.findViewById(R.id.tv_name);
                String nameStr;
                if (isQuota) {
                    nameStr = UiUtils.getNameENforTypeID(TypeID) + "(" + UiUtils.getUnitForType(TypeID) + ")";
                    name.setText(nameStr);
                } else {
                    nameStr = array.get(TypeID).get(0).getName();
                    name.setText(TextUtils.isEmpty(nameStr) ? "未知(mm*mm)" : nameStr + "(mm*mm)");
                }
                RecyclerView rv = (RecyclerView) v.findViewById(R.id.rv);
                LinearLayoutManager manager = new LinearLayoutManager(this);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                rv.setLayoutManager(manager);
                CancerSizeChartAdapter adapter;
                if (isQuota) {
                    adapter = new CancerSizeChartAdapter(this,
                            ConstUtils.getQuotaChartData(temp), UiUtils.getTypeIDForTypeID(TypeID), true);
                } else {
                    adapter = new CancerSizeChartAdapter(this,
                            ConstUtils.getCancerChartData(temp), UiUtils.getTypeIDForTypeID(TypeID));
                }
                rv.setAdapter(adapter);
            }
            if (isQuota) {
                v_quota_chart.addView(v);
            } else {
                v_cancer_chart.addView(v);
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "getCancerItemView");
        }
    }

    private ShareFragment fragment;

    //点击分享
    private void toShare() {

        if (fragment == null) {
            String content = "患者为" + MapDataUtils.getCancerValues(UserinfoData.getCancerID(this));
            String digit = MapDataUtils.getDigitValues(UserinfoData.getPeriodID(this));
            if (!TextUtils.isEmpty(digit)) {
                content += "(" + digit + "分期)";
            }
            content += "，正在使用" + MapDataUtils.getAllStepName(UserinfoData.getStepID(this)) + "治疗";

            fragment = ShareFragment.getInstance(SHARE_URL + UserinfoData.getInfoID(this),
                    UserinfoData.getInfoname(this) + "分享了一个电子病历", content,
                    UserinfoData.getAvatarUrl(this), Const.SHARE_RECORD);
        }
        if (fragment.getDialog() == null || !fragment.getDialog().isShowing())
            fragment.show(getSupportFragmentManager(), ShareFragment.type);
    }

    private int getPathologyPhotoWidth() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int result = (width - DensityUtils.dpToPx(this, 50 + 36 + 16)) / 3;//左右padding各18，space 8*2
        return result;
    }

    @Override
    public void onEditItem(RecordItemEntity entity, int position, int itemType, boolean isChild, int childPos) {
        try {
            Intent intent = new Intent(this, EditRecordActivity.class);
            intent.putExtra(Const.RECORD_CLASSIFY_TYPE, itemType);
            intent.putExtra(Const.RECORD_CLASSIFY_DATA, entity);
            intent.putExtra(Const.RECORD_REQUEST_FLAG, true);
            intent.putExtra(Const.RECORD_EDIT_FROM_MEDICAL, true);
            intent.putExtra(Const.RECORD_EDIT_POSITION, position);
            intent.putExtra(Const.RECORD_EDIT_CHILD_POSITION, childPos);
            intent.putExtra(Const.RECORD_EDIT_IS_CHILD, isChild);
            if (itemType == Const.RECORD_TYPE_14) {
                if (cancerChartList == null) cancerChartList = new ArrayList<>();
                intent.putExtra(Const.RECORD_CLASSIFY_CANCER_SIZE, (Serializable) cancerChartList);
            }
            if (itemType == Const.RECORD_TYPE_15) {
                if (cancerChartList == null) quotaChartList = new ArrayList<>();
                intent.putExtra(Const.RECORD_CLASSIFY_CANCER_SIZE, (Serializable) quotaChartList);
            }
            startActivityForResult(intent, Const.REQUEST_CODE_MEDICAL_RECORD_ACTIVITY);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "MedicalRecordActivity onEditItem");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean temp = false;
        switch (resultCode) {
            case Const.RESULT_CODE_PATHOLOGY_TO_MEDICAL://从病理报告页面返回
                temp = data.getBooleanExtra(Const.RESULT_FLAG_FOR_DATA_CHANGED, false);
                if (temp) {
                    Factory.postPhp(this, Const.PGetRecordBookForApp);
                }
                break;
            case Const.RESULT_CODE_PATIENT_DATA_TO_MEDICAL://从用户资料页面返回
                temp = data.getBooleanExtra(Const.RESULT_FLAG_FOR_DATA_CHANGED, false);
                if (temp) {
                    Factory.postPhp(this, Const.PGetUserInfoForApp);
                }
                break;
            case Const.RESULT_CODE_EDIT_STEP_TO_MEDICAL:
                temp = data.getBooleanExtra(Const.RESULT_FLAG_FOR_DATA_CHANGED, false);
                if (temp) {
                    Factory.postPhp(this, Const.PGetRecordBookForApp);
                }
                break;
            case Const.RESULT_CODE_RERORD_CLASSIFY_TO_MEDICAL:
                temp = data.getBooleanExtra(Const.RESULT_FLAG_FOR_DATA_CHANGED, false);
                if (temp) {
                    Factory.postPhp(this, Const.PGetCancerInfoForApp);
                    Factory.postPhp(this, Const.PGetRecordBookForApp);
                }
                break;
            case Const.RESULT_CODE_EDIT_RECORD_TO_MEDICAL:
                temp = data.getBooleanExtra(Const.RESULT_FLAG_FOR_DATA_CHANGED, false);
                if (temp) {
                    int itemType = data.getIntExtra(Const.RECORD_CLASSIFY_TYPE, 1);
                    if (itemType == Const.RECORD_TYPE_14 || itemType == Const.RECORD_TYPE_15) {
                        Factory.postPhp(this, Const.PGetCancerInfoForApp);
                        Factory.postPhp(this, Const.PGetRecordBookForApp);
                    } else {
                        try {
                            if (itemType == Const.RECORD_TYPE_11 || itemType == Const.RECORD_TYPE_12) {
                                Factory.postPhp(this, Const.PGetUserInfoForApp);
                            }
                        } catch (Exception e) {

                        }
                        try {
                            RecordItemEntity entity = (RecordItemEntity) data.getSerializableExtra(Const.RECORD_EDIT_RESULT_DATA);
                            boolean isChild = data.getBooleanExtra(Const.RECORD_EDIT_IS_CHILD, false);
                            int pos = data.getIntExtra(Const.RECORD_EDIT_POSITION, 0);
                            int childPos = data.getIntExtra(Const.RECORD_EDIT_CHILD_POSITION, 0);
                            adapter.updateEdit(entity, pos, itemType, isChild, childPos);
                        } catch (Exception e) {
                            ExceptionUtils.ExceptionSend(e, "RESULT_CODE_EDIT_RECORD_TO_MEDICAL");
                        }
                    }
                }
                break;
            case Const.RESULT_CODE_RECORD_CHART_TO_MEDICAL:
                temp = data.getBooleanExtra(Const.RESULT_FLAG_FOR_DATA_CHANGED, false);
                if (temp) {
                    Factory.postPhp(this, Const.PGetCancerInfoForApp);
                }
                break;
        }
    }

    /**
     * 重复启动动画
     */
    private class ReStartAnimationListener implements Animation.AnimationListener {
        public void onAnimationEnd(Animation animation) {
            // TODO Auto-generated method stub
            iv_move.setVisibility(View.GONE);
            if (FRIST_IN) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iv_move.startAnimation(a);
                        iv_move.setVisibility(View.VISIBLE);
                    }
                }, 480);
            }
        }

        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub
        }

        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub
        }
    }

    private static final int LOADING_FINISH = 1;

    private static class MyHandler extends Handler {
        private final WeakReference<MedicalRecordActivity> mActivity;

        public MyHandler(MedicalRecordActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        private MedicalRecordActivity activity;

        @Override
        public void handleMessage(Message msg) {
            activity = mActivity.get();
            if (msg.what == LOADING_FINISH) {
                activity.loadingFinish();
            }
        }
    }

}

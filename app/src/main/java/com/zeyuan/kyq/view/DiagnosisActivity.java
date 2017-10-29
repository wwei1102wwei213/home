package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.DiagnosisResultAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.CommBean;
import com.zeyuan.kyq.bean.FindSymtomBean;
import com.zeyuan.kyq.bean.WSZLBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.fragment.EmptyPageFragment;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DensityUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.InitZYUtils;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CustomProgressDialog;
import com.zeyuan.kyq.widget.MyListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/30.
 *
 * 诊断结果
 *
 * @author wwei
 */
public class DiagnosisActivity extends BaseActivity implements View.OnClickListener,HttpResponseInterface
        ,AdapterView.OnItemClickListener{

    private String PerformID;
    private String performName = null;
    //用户信息
    private TextView tv_able;
    //状态栏占位控件
    private View v_status;
    //肿瘤进展视图
    private View v_cancer;
    //副作用并发症视图
    private View v_eff;
    //其他可能视图
    private View v_other;
    //图表相关控件
        /*private View item_cancer;
        private View item_eff;
        private View item_cancer_eff;
        private View item_other;
        private View v_item_eff;
        private View v_item_cancer_eff;
        private View v_item_other;
        private TextView tv_item_eff;
        private TextView tv_item_cancer_eff;
        private TextView tv_item_other;*/

        private View v_other_btm;
        private View v_eff_btm;
        private View v_eff_text;
        private View v_other_text;
        private View v_eff_img;
        private View v_other_img;
        private View v_cancer_able_btm;
    private View v_cancer_able_text;
        private TextView tv_eff_num;
        private TextView tv_other_num;
    //图标高度的阶梯值
    private int GradeHeight = 0;
    //滑动器控件
    private View sv;
    //副作用并发症列表适配器
    private DiagnosisResultAdapter eAdapter;
    //其他列表适配器
    private DiagnosisResultAdapter oAdapter;
    //副作用并发症列表控件
    private MyListView lv_eff;
    //其他列表控件
    private MyListView lv_other;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent();
        setContentView(R.layout.activity_diagnosis);
        initStatusBar();
        PerformID = getIntent().getStringExtra(Contants.PerformID);
        initView();
        initData();
        Factory.onEvent(this, Const.EVENT_SymptomsDiagnoseResult, Const.EVENTFLAG, null);
        InitZYUtils.initFeedback(this);
    }

    private void initView(){
        //设置控件

        //设置昵称
        ((TextView)findViewById(R.id.name)).setText("HI,"+UserinfoData.getInfoname(this));
        //图表控件
        /*item_cancer = findViewById(R.id.item_cancer);
        item_eff = findViewById(R.id.item_eff);
        item_cancer_eff = findViewById(R.id.item_cancer_eff);
        item_other = findViewById(R.id.item_other);
        v_item_eff = findViewById(R.id.v_item_eff);
        v_item_cancer_eff = findViewById(R.id.v_item_cancer_eff);
        v_item_other = findViewById(R.id.v_item_other);
        tv_item_eff = (TextView)findViewById(R.id.tv_item_eff);
        tv_item_cancer_eff = (TextView)findViewById(R.id.tv_item_cancer_eff);
        tv_item_other = (TextView)findViewById(R.id.tv_item_other);*/

        v_other_btm = findViewById(R.id.v_other_btm);
        v_eff_btm = findViewById(R.id.v_eff_btm);
        v_eff_text = findViewById(R.id.v_eff_btm_text);
        v_other_text = findViewById(R.id.v_other_btm_text);
        v_eff_img = findViewById(R.id.v_eff_img);
        v_other_img = findViewById(R.id.v_other_img);
        tv_eff_num = (TextView)findViewById(R.id.tv_eff_num);
        tv_other_num = (TextView)findViewById(R.id.tv_other_num);
        v_cancer_able_btm = findViewById(R.id.v_cancer_able_btm);
        v_cancer_able_text = findViewById(R.id.v_cancer_able_btm_text);

        //设置返回图标
        ImageView iv_back = (ImageView)findViewById(R.id.iv_back);
        //设置文本
        tv_able = (TextView)findViewById(R.id.tv_able);
        initParams();
        setTVAble();

        //列表视图
        v_cancer = findViewById(R.id.v_cancer);
        v_eff = findViewById(R.id.v_eff);
        v_other = findViewById(R.id.v_other);
        lv_eff = (MyListView)findViewById(R.id.lv_eff);
        lv_other = (MyListView)findViewById(R.id.lv_other);
        lv_eff.setFocusable(false);
        lv_other.setFocusable(false);
        eAdapter = new DiagnosisResultAdapter(this,new ArrayList<FindSymtomBean.CommPolicyEntity>()
                ,DiagnosisResultAdapter.RED);
        oAdapter = new DiagnosisResultAdapter(this,new ArrayList<FindSymtomBean.CommPolicyEntity>()
                ,DiagnosisResultAdapter.BLUE);
        lv_eff.setAdapter(eAdapter);
        lv_other.setAdapter(oAdapter);
        //sv
        sv = findViewById(R.id.sv);

        //设置监听
        iv_back.setOnClickListener(this);
        v_cancer.setOnClickListener(this);
        lv_eff.setOnItemClickListener(this);
        lv_other.setOnItemClickListener(this);
        findViewById(R.id.tv_to_edit).setOnClickListener(this);
    }

    private String CancerID;//癌症的id
    private String StepID;//当前阶段ID
    private String PeriodID;
    private void initParams(){
        try {
            CancerID = UserinfoData.getCancerID(this);
            StepID = UserinfoData.getStepID(this);
            PeriodID = UserinfoData.getPeriodID(this);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"Diagnosis initParams");
        }
    }

    private void initData() {
        Intent intent = getIntent();
        FindSymtomBean bean = (FindSymtomBean) intent.getSerializableExtra(Contants.FindSymtomBean);
        initData(bean);
    }

    private void initData(FindSymtomBean bean){
        List temp = bean.getCommPolicy();
        if (temp == null || temp.isEmpty()) {//这儿显示为空
            toEmptyResult();
            sv.setVisibility(View.GONE);
            return;
        }

        String hasCancer = bean.getIsHaveCancerProcess();
        if ("1".equals(hasCancer)) {//说明有肿瘤进展
            v_cancer.setVisibility(View.VISIBLE);
            v_cancer_able_btm.setVisibility(View.VISIBLE);
            v_cancer_able_text.setVisibility(View.VISIBLE);
        }else{
            v_cancer.setVisibility(View.GONE);
            v_cancer_able_btm.setVisibility(View.GONE);
            v_cancer_able_text.setVisibility(View.GONE);
        }

        List<FindSymtomBean.CommPolicyEntity> list = bean.getCommPolicy();
        if (list != null && list.size() > 0) {
            try {
                Collections.sort(list, new Comparator<FindSymtomBean.CommPolicyEntity>() {
                    @Override
                    public int compare(FindSymtomBean.CommPolicyEntity c1, FindSymtomBean.CommPolicyEntity c2) {
                        return Integer.valueOf(c1.getCommPolicyType()) - Integer.valueOf(c2.getCommPolicyType());
                    }
                });
            }catch (Exception e){
                ExceptionUtils.ExceptionToUM(e, this, "CommPolicyEntity排序失败");
            }
            List<FindSymtomBean.CommPolicyEntity> stepList = new ArrayList<>();
            List<FindSymtomBean.CommPolicyEntity> maybeList = new ArrayList<>();
            for(FindSymtomBean.CommPolicyEntity entity:list){
                try {
                    String tempType = entity.getCommPolicyType();
                    if(!TextUtils.isEmpty(tempType)){
                        int tempInt = Integer.valueOf(tempType);
                        if(tempInt==8||tempInt==11){
                            stepList.add(entity);
                        }else {
                            maybeList.add(entity);
                        }
                    }
                }catch (Exception e){
                    ExceptionUtils.ExceptionToUM(e,this,"类型转换失败");
                }
            }
            if(stepList.size()>0){
                v_eff_btm.setVisibility(View.VISIBLE);
                v_eff_text.setVisibility(View.VISIBLE);
                tv_eff_num.setText("" + stepList.size());
                setViewWidth(v_eff_img, stepList.size());
                update(DiagnosisResultAdapter.RED, stepList);
                v_eff.setVisibility(View.VISIBLE);
            }else {
                v_eff_btm.setVisibility(View.GONE);
                v_eff_text.setVisibility(View.GONE);
                v_eff.setVisibility(View.GONE);
            }
            if(maybeList.size()>0){
                v_other_btm.setVisibility(View.VISIBLE);
                v_other_text.setVisibility(View.VISIBLE);
                tv_other_num.setText("" + maybeList.size());
                setViewWidth(v_other_img, maybeList.size());
                update(DiagnosisResultAdapter.BLUE, maybeList);
                v_other.setVisibility(View.VISIBLE);
            }else {
                v_other_btm.setVisibility(View.GONE);
                v_other_text.setVisibility(View.GONE);
                v_other.setVisibility(View.GONE);
            }
        } else {//没有结果
            if (!"1".equals(hasCancer)) {
                sv.setVisibility(View.GONE);
                toEmptyResult();
            }
        }
    }

    private void setTVAble(){
        try {
            String cancer = MapDataUtils.getCancerValues(CancerID);
            String step = MapDataUtils.getAllStepName(StepID);
            String digit = MapDataUtils.getDigitValues(PeriodID);
            tv_able.setText("");
            tv_able.append("病人正在进行");
            tv_able.append(Html.fromHtml("<font color=\"#f44f4f\" font-size:\"30px\"><b>"+step+"</b></font>"));
            tv_able.append("治疗");
            tv_able.append(Html.fromHtml("<font color=\"#f44f4f\" font-size:\"30px\"><b>" + cancer +
                    getDigitText(digit) + "</b></font>"));
//            tv_able.append(getClickableSpan());
//            tv_able.append("，如果出现  "+ MapDataUtils.getPerfromValues(PerformID) +"  ，您可能有以下几种方式的治疗建议，其中包括：");
            tv_able.append("，如果出现");
            tv_able.append(Html.fromHtml("<font color=\"#666666\" font-size:\"30px\"><b>"
                    +MapDataUtils.getPerfromValues(PerformID)+"</b></font>"));
            tv_able.append("，您可能有以下几种方式的治疗建议，其中包括：");
            tv_able.setMovementMethod(LinkMovementMethod.getInstance());
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"Diagnosis setTVAble");
        }
    }

    private String getDigitText(String digit){
        if (TextUtils.isEmpty(digit)) return "";
        if ("未知".equals(digit)){
            return "未知分期";
        }else {
            return digit + "期";
        }
    }

    private void update(int flag,List<FindSymtomBean.CommPolicyEntity> list){
        switch (flag){
            case DiagnosisResultAdapter.RED:
                eAdapter.update(list);
                break;
            case DiagnosisResultAdapter.BLUE:
                oAdapter.update(list);
                break;
        }
    }

    private void setViewHeight(View v,int size){
        ViewGroup.LayoutParams params = v.getLayoutParams();
        if (size>9){
            params.height = 10*getGradeHeight();
        }else {
            params.height = size*getGradeHeight();
        }
        v.setLayoutParams(params);
    }

    private void setViewWidth(View v,int size){
        ViewGroup.LayoutParams params = v.getLayoutParams();
        if (size>5){
            params.width = 5*getGradeHeight();
        }else {
            params.width = size*getGradeHeight();
        }
        v.setLayoutParams(params);
    }

    private int getGradeHeight(){
        if (GradeHeight == 0) {
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            int width = metric.widthPixels;     // 屏幕宽度（像素）

            GradeHeight = (width - DensityUtils.dip2px(this,100))/5;
        }
        return GradeHeight;
    }

    private void toEmptyResult(){
        setEmptyPageFragment(R.mipmap.no_result, "圈圈努力搜查后，未发现匹配的结果，可能是：\n" +
                "1、该症状不常见于肿瘤并发症；\n" +
                "2、该副作用及并发症不常见于患者所处的治疗阶段；\n" +
                "3、该症状未被圈圈收录，请耐心等待。", "去反馈", new EmptyPageFragment.EmptyClickListener() {
            @Override
            public void onEmptyClickListener() {
                FeedbackAPI.openFeedbackActivity();
                finish();
            }
        }, R.id.fl ,1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.v_cancer:
                Factory.post(this, Const.EGetConfirmSecond);
                break;
            case R.id.tv_to_edit:
                startActivity(new Intent(DiagnosisActivity.this, PerfectDataActivity.class)
                        .putExtra("Diagnosis", 1));
                break;
        }
    }

    private String CommPolicyID;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_eff:
                CommPolicyID = eAdapter.getItem(position);
                Factory.post(this, Const.EGetCommDetail);
                break;
            case R.id.lv_other:
                CommPolicyID = oAdapter.getItem(position);
                Factory.post(this, Const.EGetCommDetail);
                break;
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        return null;
    }

    private String[] shareToParams;
    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        switch (flag){
            case Const.EGetConfirmSecond:
                if(Const.NO_STEP.equals(UserinfoData.getIsHaveStep(this))){
                    args = new String[]{
                            Contants.InfoID, UserinfoData.getInfoID(this),
                            Contants.CancerID, UserinfoData.getCancerID(this),
                            Contants.PeriodID, UserinfoData.getPeriodID(this),
                            Contants.PeriodType, "1",
                            Const.ISHAVESTEP,Const.NO_STEP
                    };
                }else {
                    args = new String[]{
                            Contants.InfoID, UserinfoData.getInfoID(this),
                            Contants.StepID, UserinfoData.getStepID(this),
                            Contants.CancerID, UserinfoData.getCancerID(this),
                            Contants.PeriodID, UserinfoData.getPeriodID(this),
                            Contants.PeriodType, "1",
                            Const.ISHAVESTEP,Const.HAVE_STEP
                    };
                }
                break;
            case Const.EGetCommDetail:
                args = new String[]{
                        Contants.InfoID, UserinfoData.getInfoID(this),
                        Contants.CommPolicyID, CommPolicyID,
                        Contants.PerformID,PerformID,
                        Contants.Type, "3"//通过查症状进入
                };
                shareToParams = args;
                break;
            case Const.EGetCancerProcess:
                if(Const.NO_STEP.equals(UserinfoData.getIsHaveStep(this))){
                    args = new String[]{
                            Contants.InfoID,UserinfoData.getInfoID(this),
                            Contants.CancerID,UserinfoData.getCancerID(this),
                            Contants.PerformID, PerformID,
                            Const.ISNEWVERSION,Const.ISNEWVERSION_FINAL,
                            Const.ISHAVESTEP,Const.NO_STEP
                    };
                }else{
                    String cureConfID = MapDataUtils.getAllCureconfID(UserinfoData.getStepID(this));
                    args = new String[]{
                            Contants.InfoID,UserinfoData.getInfoID(this),
                            Contants.StepID,UserinfoData.getStepID(this),
                            Contants.CancerID,UserinfoData.getCancerID(this),
                            Contants.PerformID, PerformID,
                            Contants.CureConfID,cureConfID,
                            Const.ISNEWVERSION,Const.ISNEWVERSION_FINAL,
                            Const.ISHAVESTEP,Const.HAVE_STEP
                    };
                }
                shareToParams = args;
                break;
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object t, int flag) {
        if (flag == Const.EGetConfirmSecond) {
            WSZLBean bean = (WSZLBean) t;
            startActivity(new Intent(this, WSZLActivity.class).putExtra(Contants.WSZLBean, bean));              //这儿跳转到完善资料
        }

        if (flag == Const.EGetCommDetail) {//普通决策树
            CommBean bean = (CommBean) t;
            startActivity(new Intent(this, ResultDetailActivity.class).putExtra(Contants.IS_CANCER, false)
                            .putExtra(Contants.CommBean, bean)
                            .putExtra(Const.RESULT_PARAMS_FOR_SHARE,shareToParams)
            );
        }
        if (flag == Const.EGetCancerProcess) {
            FindSymtomBean bean = (FindSymtomBean) t;
            if (Contants.RESULT.equals(bean.getIResult())) {
                initData(bean);
                showToast("诊断结果已更新");
            }
        }


    }

    private CustomProgressDialog mProgressDialog;
    @Override
    public void showLoading(int tag) {
        mProgressDialog = CustomProgressDialog.createCustomDialog(this);
        mProgressDialog.show();
    }

    @Override
    public void hideLoading(int tag) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showError(int tag) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            if(!CancerID.equals(UserinfoData.getCancerID(this))||!StepID.equals(UserinfoData.getStepID(this))
                    ||!PeriodID.equals(UserinfoData.getPeriodID(this))){
                initParams();
                setTVAble();
                Factory.post(this, Const.EGetCancerProcess);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"Diagnosis onRestart");
        }
    }

    private SpannableString getClickableSpan(){
        View.OnClickListener l = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(DiagnosisActivity.this, PerfectDataActivity.class)
                        .putExtra("Diagnosis", 1));
            }
        };
        SpannableString spanableInfo = new SpannableString("（不正确？去修改>）");
        int start = 0;
        int end = spanableInfo.length();
        spanableInfo.setSpan(new Clickable(l), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanableInfo;
    }

    class Clickable extends ClickableSpan implements View.OnClickListener{
        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener l){
            mListener = l;
        }

        @Override
        public void onClick(View v){
            mListener.onClick(v);
        }
    }
}



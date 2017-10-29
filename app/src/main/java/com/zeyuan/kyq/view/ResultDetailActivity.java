package com.zeyuan.kyq.view;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.ResultAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.bean.CancerResuletBean;
import com.zeyuan.kyq.bean.CaseDetailBean;
import com.zeyuan.kyq.bean.CommBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.fragment.ShareFragment;
import com.zeyuan.kyq.fragment.empty.EmptyResulDetalFragment;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ConstUtils;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CustomExpandableListView;
import com.zeyuan.kyq.widget.CustomProgressDialog;
import com.zeyuan.kyq.widget.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/6.
 * 结果详情--
 * 肿瘤决策树和普通决策树 装数据就好
 * 只有肿瘤决策树有添加到计划用药和小圆点（待定）
 * 肿瘤 决策树 进入结果详情 需要传入TemplateID  getSulutionDetail
 * 普通决策树进入结果详情    不需要传入TemplateID
 * 从资料确认2进入结果详情需要传templateid
 */
public class ResultDetailActivity extends BaseActivity implements ViewDataListener, ResultAdapter.CheckBoxCheckListener,
        ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener,
        View.OnClickListener ,HttpResponseInterface{
    private static final String TAG = "ZYS";
    private static final String BXY_ID = "4205";
    private CustomExpandableListView topListview;//可能的治疗方案listview
    private CustomExpandableListView bottomListview;//未做基因者可尝试的listview
    private ResultAdapter topAdapter;
    private ResultAdapter bottomAdapter;
    private Button confirm;//底部的按钮
    private boolean isCancer = false;//判断试普通决策树（false） 还是 肿瘤决策树（true）
    private String shareUrl;
    private static final String URL_HEAD = "http://www.kaqcn.com/r.html?";
    private String[] shareParams;
    private int type;

    private void initData() {
        topData = new ArrayList<>();
        bottomData = new ArrayList<>();
        topAdapter = new ResultAdapter(this, topData, isCancer);
        topAdapter.setCheckBoxCheckListener(this);
        topListview.setAdapter(topAdapter);

        bottomAdapter = new ResultAdapter(this, bottomData, isCancer);
        bottomAdapter.setCheckBoxCheckListener(this);
        bottomListview.setAdapter(bottomAdapter);
        if (isCancer) {
            PolicyType = "1";
            bindView(mCancerResuletBean);
        } else {
            PolicyType = "2";
            bindView(mCommBean,type);
        }
    }

    private CommBean mCommBean;//有这个说明是普通决策树
    private CancerResuletBean mCancerResuletBean;
    private boolean isEffect = false;//false表示 不是从副作用进来的 true表示从副作用进来的
    private TextView tv_pos_cure;//可能的治疗方案文本

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancer_development);
        try {
            Intent inetent = null;
            try {
                initWhiteTitle(getString(R.string.result_detail),true);
                inetent = getIntent();
            }catch (Exception e){
                Log.i(TAG,"1");
            }
            try {
                PerformID = inetent.getStringExtra(Contants.PerformID);
            }catch (Exception e){
                Log.i(TAG,"2");
            }
            try {
                mCancerResuletBean = (CancerResuletBean) inetent.getSerializableExtra(Contants.CancerResuletBean);
            }catch (Exception e){
                Log.i(TAG,"3");
            }
            try {
                mCommBean = (CommBean) inetent.getSerializableExtra(Contants.CommBean);
                type = inetent.getIntExtra(Const.SET_QUOTA_TYPE,0);
                LogCustom.i(TAG,"mCommBean:"+mCommBean.toString());
            }catch (Exception e){
                Log.i(TAG,"4");
            }
            if (mCommBean == null) {
                isCancer = true;
            }
            try {
                shareParams = (String[])inetent.getCharSequenceArrayExtra(Const.RESULT_PARAMS_FOR_SHARE);
                LogCustom.i(TAG, "ZYS" + Arrays.toString(shareParams));
            }catch (Exception e){
                Log.i(TAG,"5");
            }


            try {
                initView();
            }catch (Exception e){
                Log.i(TAG,"5");
            }

            try {
                initData();
            }catch (Exception e){
                Log.i(TAG,"7");
            }

            try {
                setListener();
            }catch (Exception e){
                Log.i(TAG,"8");
            }

            try {
                Factory.onEvent(this, Const.EVENT_DiagnoseResultDetail, Const.EVENTFLAG, null);
            }catch (Exception e){

            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"ResultDetailActivity");
        }
    }

    private void setListener() {
        try {
            topListview.setOnGroupClickListener(this);
            topListview.setOnChildClickListener(this);
            bottomListview.setOnGroupClickListener(this);
            bottomListview.setOnChildClickListener(this);
            confirm.setOnClickListener(this);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"ResultDetailActivity");
        }
    }

    private ExpandableTextView cancel_decri;//头部描述的文本
    private TextView conclusion;//确诊条件
    private TextView check_gene;//是否进行基因检查
    private TextView type_name;//最顶部的文本
    private TextView confirm_condition;//文本确诊条件
    private TextView tv_tip;//文本确诊条件

    private void initView() {
        try {
            ImageView iv_share = (ImageView)findViewById(R.id.iv_white_title_share);
            iv_share.setOnClickListener(this);

            cancel_decri = (ExpandableTextView) findViewById(R.id.cancel_decri);
            tv_tip = (TextView) findViewById(R.id.tv_tip);
            tv_pos_cure = (TextView) findViewById(R.id.pos_cure);

            check_gene = (TextView) findViewById(R.id.check_gene);
            type_name = (TextView) findViewById(R.id.type_name);
            confirm_condition = (TextView) findViewById(R.id.confirm_condition);
            conclusion = (TextView) findViewById(R.id.conclusion);
            confirm = (Button) findViewById(R.id.confirm);
            topListview = (CustomExpandableListView) findViewById(R.id.top_listview);
            bottomListview = (CustomExpandableListView) findViewById(R.id.bottom_listview);
            bottomListview.setGroupIndicator(null);//设置没有 指示符
            topListview.setGroupIndicator(null);//设置没有 指示符
            if (isCancer) {
//                List<CancerResuletBean.StepEntity> list = mCancerResuletBean.getStep();
            } else {
                bottomListview.setVisibility(View.GONE);
                check_gene.setVisibility(View.GONE);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"ResultDetailActivity");
        }

    }



    private String StepID;//TemplateID
    private String TemplateID;//模板id

    private String CureConfID;
    private String PerformID;
    private String PolicyType;

    /**
     * 肿瘤决策树为空的情况
     */
    private void emptyCancerFragment() {
        try {
            EmptyResulDetalFragment fragment = new EmptyResulDetalFragment();
            fragment.setListener(new EmptyResulDetalFragment.OnFinishListener() {
                @Override
                public void onFinish() {
                    finish();
                }
            });
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fl, fragment);
            ft.commit();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"ResultDetailActivity");
        }
    }


    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        if (tag == 0) {//点击 进入方案详情 需要的templateid
            map.put(Contants.StepID, StepID);
            map.put(Contants.PolicyType, PolicyType);

            if (!TextUtils.isEmpty(TemplateID)) {
                map.put(Contants.TemplateID, TemplateID);
            }

            if (!TextUtils.isEmpty(CureConfID)) {
                map.put(Contants.CureConfID, CureConfID);
            }

            if (!TextUtils.isEmpty(PerformID)) {
                map.put(Contants.PerformID, PerformID);
            }
        }

        if (tag == NetNumber.SET_PLAN_MEDICINE) {//添加到计划用药
            map.put(Contants.PlanStepID, PlanStepID);
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if(flag == Const.EGetSolutionDetail){
            List<String> list = new ArrayList<>();
            list.add(Contants.InfoID);
            list.add(Const.InfoID);
            list.add(Contants.StepID);
            list.add(StepID);
            list.add(Contants.PolicyType);
            list.add(PolicyType);
            if (!TextUtils.isEmpty(TemplateID)) {
                list.add(Contants.TemplateID);
                list.add(TemplateID);
            }

            if (!TextUtils.isEmpty(CureConfID)) {
                list.add(Contants.CureConfID);
                list.add(CureConfID);
            }

            if (!TextUtils.isEmpty(PerformID)) {
                list.add(Contants.PerformID);
                list.add(PerformID);
            }

                list.add(Const.ISNEWVERSION);
                list.add(Const.ISNEWVERSION_FINAL);

            args = ConstUtils.getParamsForList(list);

        }else if(flag == Const.ESetPlanMedicine){
            args = new String[]{
                    Contants.InfoID,UserinfoData.getInfoID(this),
                    Contants.PlanStepID, PlanStepID
            };
        }
        return HttpSecretUtils.getParamString(args);
    }

    private List<CancerResuletBean.StepEntity> topData;
    private List<CancerResuletBean.StepEntity> bottomData;

    @Override
    public void toActivity(Object t, int tag) {
        try {

            if (tag == Const.EGetSolutionDetail) {//点击某项 请求数据
                CaseDetailBean bean = (CaseDetailBean) t;
                if (Contants.RESULT.equals(bean.getIResult())) {
                    if (bean == null) {
                        return;
                    }
                    bean.setStepId(StepID);
                    startActivity(new Intent(this, CaseDetailActivity.class).putExtra(Contants.CaseDetailBean, bean).putExtra(Contants.ISCANCER, isCancer));
                }
            }
            if (tag == Const.ESetPlanMedicine) {//添加到计划用药
                BaseBean bean = (BaseBean) t;
                if (Contants.RESULT.equals(bean.iResult)) {
                    showToast("添加成功");
                    UserinfoData.planId = PlanStepID;
                    Factory.onEvent(this,Const.EVENT_AddMedicineToPlan,Const.EVENTFLAG,null);
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"ResultDetailActivity");
        }
    }

    private TextView tv_suit;
    private TextView tv_policytext;
    private void bindView(CommBean bean,int type) {
        try {
            confirm.setVisibility(View.GONE);
            findViewById(R.id.line1).setVisibility(View.GONE);
            findViewById(R.id.line2).setVisibility(View.GONE);
            tv_tip.setVisibility(View.GONE);

            String describ = null;
            String CommonPolicyTypeName;
            String PolicyText = null;
            if(type==4){
                CommonPolicyTypeName = bean.getSpName();
                describ = bean.getSpSummary();
                findViewById(R.id.ll_condition).setVisibility(View.GONE);
                if(TextUtils.isEmpty(bean.getSpSuit())){
                    findViewById(R.id.ll_suit).setVisibility(View.GONE);
                }else{
                    if(tv_suit==null){
                        tv_suit = (TextView)findViewById(R.id.tv_spsuit);
                    }
                    tv_suit.setText(bean.getSpSuit());
                }

            }else {
                if(TextUtils.isEmpty(bean.getClinicPerform())){
                    findViewById(R.id.ll_suit).setVisibility(View.GONE);
                }else {
                    if(tv_suit==null){
                        tv_suit = (TextView)findViewById(R.id.tv_spsuit);
                    }
                    tv_suit.setText(bean.getClinicPerform());
                }

                if(TextUtils.isEmpty(bean.getDiagnoseCondition())){
                    findViewById(R.id.ll_condition).setVisibility(View.GONE);
                }else{
                    conclusion.setText(bean.getDiagnoseCondition());
                }
                describ = bean.getSummary();

                if(!TextUtils.isEmpty(bean.getCommonPolicyName())){
                    CommonPolicyTypeName = bean.getCommonPolicyName();
                }else {
                    CommonPolicyTypeName = bean.getCommonPolicyTypeName();
                }
            }
            PolicyText = bean.getPolicyText();
            if(!TextUtils.isEmpty(PolicyText)){

                findViewById(R.id.ll_policy_text).setVisibility(View.VISIBLE);
                if(tv_policytext==null){
                    tv_policytext = (TextView)findViewById(R.id.tv_policytext);
                }
                tv_policytext.setText(PolicyText);
            }


            if(!TextUtils.isEmpty(CommonPolicyTypeName)){
                type_name.setText(CommonPolicyTypeName);
            }

            TextView tv_no = (TextView)findViewById(R.id.tv_no_other);


            List<CancerResuletBean.StepEntity> list = bean.getSolution();
            if(!TextUtils.isEmpty(describ)){

                if(type==4){
                    if((list == null || list.size() == 0)&&TextUtils.isEmpty(PolicyText)&&TextUtils.isEmpty(bean.getSpSuit())){
                        cancel_decri.setVisibility(View.GONE);
                        findViewById(R.id.ett_temp).setVisibility(View.GONE);
                        tv_no.setVisibility(View.VISIBLE);
                        tv_no.setText(describ);
                    }else {
                        cancel_decri.setText(describ);
                    }
                }else {
                    if((list == null || list.size() == 0)&&TextUtils.isEmpty(PolicyText)&&TextUtils.isEmpty(bean.getDiagnoseCondition())){
                        cancel_decri.setVisibility(View.GONE);
                        findViewById(R.id.ett_temp).setVisibility(View.GONE);
                        tv_no.setVisibility(View.VISIBLE);
                        tv_no.setText(describ);
                    }else{
                        cancel_decri.setText(describ);
                    }
                }
            }
            if (list != null && list.size() > 0) {
                Log.i("ZYS", "list:" + list.size());
                try {
                    topData.addAll(list);
                    topAdapter.update(topData);
                }catch (Exception e){
                    Log.i("ZYS","mCommBean exc:");
                }

            } else {
                tv_pos_cure.setText(R.string.no_result_cure);
                if (TextUtils.isEmpty(describ) && TextUtils.isEmpty(CommonPolicyTypeName)&&TextUtils.isEmpty(PolicyText)) {
                    findViewById(R.id.whole_content).setVisibility(View.GONE);
                    emptyCommnFragment(-1, null, null, null);
//                    emptyCancerFragment();
//                    emptyCommnFragment(R.mipmap.no_result, getString(R.string.empty_dignose1), "", "");
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"ResultDetailActivity");
        }
    }


    private void bindView(CancerResuletBean bean) {
        findViewById(R.id.ll_suit).setVisibility(View.GONE);
        try {
            String conclusiong = bean.getConclusion();
            String summary = bean.getSummary();
            conclusion.setText(conclusiong);
            if(!TextUtils.isEmpty(summary)){
                cancel_decri.setText(summary);
            }
            List<CancerResuletBean.StepEntity> list = bean.getStep();
            if (list != null && list.size() > 0) {
                for (CancerResuletBean.StepEntity item : list) {
                    if ("0".equals(item.getIsTry())) {//非试药
                        topData.add(item);
                    } else {
                        bottomData.add(item);
                    }
                }
                if (isHasBXY(topData)) {
                    check_gene.setVisibility(View.VISIBLE);
                } else {
                    check_gene.setVisibility(View.GONE);
                }
                topAdapter.update(topData);
                if (bottomData.size() > 0) {
                    tv_tip.setVisibility(View.VISIBLE);
                } else {
                    tv_tip.setVisibility(View.GONE);
                }
                bottomAdapter.update(bottomData);
                confirm.setVisibility(View.VISIBLE);
            } else {
                //改成未找到治疗方案
                tv_pos_cure.setText("未找到治疗方案");
                if (TextUtils.isEmpty(conclusiong) && TextUtils.isEmpty(summary)) {
                    confirm.setVisibility(View.GONE);
                    findViewById(R.id.whole_content).setVisibility(View.GONE);
                    findViewById(R.id.iv_white_title_share).setVisibility(View.INVISIBLE);
                    emptyCancerFragment();
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"ResultDetailActivity");
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

    private String PlanStepID;

    /**
     * 列表选择后 这个回调会调用
     *
     * @param adapter
     * @param groupPosition
     * @param childPosition
     * @param item
     */
    @Override
    public void checkItem(BaseExpandableListAdapter adapter, int groupPosition, int childPosition, String item) {
        LogCustom.i("ZYA", "groupPosition:" + groupPosition + "\nchildPosition:" + childPosition + "\nitem :" + item);
        try {
            if (TextUtils.isEmpty(item)) {//这个说明他是取消了checkbox的选择
                confirm.setText("请选择方案");
                return;
            }
            PlanStepID = item;
            //判断试哪一个的listview
            confirm.setText("添加到计划用药");
            if (adapter == topAdapter && topAdapter != null) {
                bottomAdapter.setSelectChildPosition();//清除这个项目的选中效果
            }

            if (adapter == bottomAdapter && bottomAdapter != null) {
                topAdapter.setSelectChildPosition();//清除这个项目的选中效果
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"ResultDetailActivity");
        }
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        LogCustom.i(TAG, "onGroupClick");
        try {
            CancerResuletBean.StepEntity group = null;
            if (parent == topListview) {
                group = topAdapter.getGroup(groupPosition);
            }

            if (parent == bottomListview) {
                group = bottomAdapter.getGroup(groupPosition);
            }

            if (!TextUtils.isEmpty(group.getCombineid())) {//说明他有展开项 说明他有组合用药
                if (parent == topListview) {
                    topListview.setSelectedGroup(groupPosition);
                }
                if (parent == bottomListview) {
                    bottomListview.setSelectedGroup(groupPosition);
                }
                return false;
            }

            StepID = group.getStepid();
            TemplateID = group.getTemplateid();

            String cureconfid = MapDataUtils.getAllCureconfID(StepID);
            if (!TextUtils.isEmpty(cureconfid)) {
                CureConfID = cureconfid;
            }
            Factory.post(this,Const.EGetSolutionDetail);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"ResultDetailActivity");
        }
        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        LogCustom.i(TAG, "onChildClick");

        try {
            CancerResuletBean.StepEntity.CombineStepidEntity child = null;
            if (parent == topListview) {
                child = topAdapter.getChild(groupPosition, childPosition);
            }
            if (parent == bottomListview) {
                child = bottomAdapter.getChild(groupPosition, childPosition);
            }
            TemplateID = child.getTemplateid();
            StepID = child.getStepid();
            String cureconfid = MapDataUtils.getAllCureconfID(StepID);
            if (!TextUtils.isEmpty(cureconfid)) {
                CureConfID = cureconfid;
            }
            LogCustom.i("ZYS","CCID:"+CureConfID+"STEPID:"+StepID);
            Factory.post(this,Const.EGetSolutionDetail);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"ResultDetailActivity");
        }
        return false;
    }

    private ShareFragment fragment;
    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.confirm: {
                    if (TextUtils.isEmpty(PlanStepID)) {
                        Toast.makeText(this, "请选择方案", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Factory.post(this, Const.ESetPlanMedicine);
                    break;
                }
                case R.id.iv_white_title_share:
                    if(fragment==null){
                        String temp = getParamsFoot();
                        if(TextUtils.isEmpty(temp)){
                            return;
                        }
                        shareUrl = URL_HEAD + temp;
                        String name = UserinfoData.getInfoname(this);
                        if(TextUtils.isEmpty(name)){
                            name = "抗癌圈用户";
                        }
                        String shareTitle = name + "所分享的抗癌圈病情分析";
                        fragment = ShareFragment.getInstance(shareUrl,shareTitle,getString(R.string.share_content_init),null,Const.SHARE_RESULT);
                    }
                    try {
                        if (fragment.getDialog() == null || !fragment.getDialog().isShowing())
                        fragment.show(getSupportFragmentManager(),ShareFragment.type);
                    }catch (Exception e){

                    }
                    break;
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"ResultDetailActivity");
        }
    }

    private String getParamsFoot(){
        String params = "";
        String temp = "";
        if(shareParams==null||shareParams.length==0) return null;
        if(shareParams.length%2!=0) return null;
        for(int i = 0 ; i<shareParams.length ; i++){

            switch (shareParams[i]){
                case Contants.InfoID:
                    temp = "&&" + "in=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.StepID:
                    temp = "&&" + "st=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.CancerID:
                    temp = "&&" + "ca=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.PeriodID:
                    temp = "&&" + "pe=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.PerformID:
                    temp = "&&" + "pf=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.SelfSelectNum:
                    temp = "&&" + "ssn=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.GeneID:
                    temp = "&&" + "ge=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.CureConfID:
                    temp = "&&" + "cc=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.QuestionID + "0":
                    temp = "&&" + "q0=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.AnswerID + "0":
                    temp = "&&" + "a0=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.BodyStatusID:
                    temp = "&&" + "bs=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.Type:
                    temp = "&&" + "ty=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.ChemistryID:
                    temp = "&&" + "ch=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.TargetID:
                    temp = "&&" + "ta=" + shareParams[i+1];
                    params += temp;
                    break;
                case Contants.CommPolicyID:
                    temp = "&&" + "cp=" + shareParams[i+1];
                    params += temp;
                    break;
                case "SpID":
                    temp = "&&" + "sp=" + shareParams[i+1];
                    params += temp;
                    break;
            }
        }
        if(params.length()>=2){
            params = params.substring(2,params.length());
            LogCustom.i("ZYS","分享参数："+params);
        }else{
            return null;
        }
        return params;
    }

    /**
     * 上面有靶向药就不显示
     *
     * @param topData
     * @return true 显示 false 不显示
     */
    private boolean isHasBXY(List<CancerResuletBean.StepEntity> topData) {
        try {
            for (CancerResuletBean.StepEntity entity : topData) {
                String stepid = entity.getStepid();
                if (!TextUtils.isEmpty(stepid)) {
                    String cureConfID = MapDataUtils.getAllCureconfID(stepid);
                    LogUtil.i(TAG, stepid);
                    if (BXY_ID.equals(cureConfID)) {
                        return false;
                    }
                }
                List<CancerResuletBean.StepEntity.CombineStepidEntity> list = entity.getCombineStepid();
                if (list != null && list.size() > 0) {
                    for (CancerResuletBean.StepEntity.CombineStepidEntity entity1 : list) {
                        String stepid2 = entity1.getStepid();
                        if (!TextUtils.isEmpty(stepid2)) {
                            String cureConfID = MapDataUtils.getAllCureconfID(stepid2);
                            LogUtil.i(TAG, cureConfID);
                            if (BXY_ID.equals(cureConfID)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"ResultDetailActivity");
        }
        return true;
    }

}

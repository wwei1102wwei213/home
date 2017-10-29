package com.zeyuan.kyq.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.CommPolicyNewEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.bean.CaseDetailBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.TextSpannerUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/10.
 * 方案详情
 * 联合用药：
 * AllDesc ==> 方案描述
 * remark ==>备注
 * suit ==> 某个药的方案描述/  描述+适应症
 * <p/>
 * 单个用药：
 * 无AllDesc
 * remark ==>备注
 * suit ==> 某个药的方案描述/  描述+适应症
 */
public class CaseDetailActivity extends BaseActivity implements View.OnClickListener, HttpResponseInterface {

    private static final String TAG = "CaseDetailActivity";
    private CaseDetailBean mCaseDetailBean;
    private RelativeLayout ll_tip;
    private Button add_plan_drugs;//添加到计划用药
    private boolean isCancer;
    private Map<String, String> performValues;
//    private HtmlSpanner spanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casedetail);
        try {
            initTitlebar(getString(R.string.case_detail));
            mCaseDetailBean = (CaseDetailBean) getIntent().getSerializableExtra(Contants.CaseDetailBean);
            PlanStepID = mCaseDetailBean.getStepId();
            isCancer = getIntent().getBooleanExtra(Contants.ISCANCER, true);
//            spanner = new HtmlSpanner();
            try {
                initView();
                initData();
                initListener();
            }catch (Exception e){
                ExceptionUtils.ExceptionSend(e,"OnCreate");
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "CaseDetailActivity");
        }
    }

    private void initData() {
        try {
            if (mCaseDetailBean != null) {
                bindView(mCaseDetailBean);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "CaseDetailActivity");
        }
    }

    private void initListener() {
        add_plan_drugs.setOnClickListener(this);
    }

    private TextView usage;//用法
    private TextView tv_tip;//备注
    private TextView drug_title;
    private TextView img_tip;
    private LinearLayout content;//主要内容的

    private void initView() {
        try {
            performValues = (Map<String, String>) Factory.getData(Const.N_DataPerformValues);
            add_plan_drugs = (Button) findViewById(R.id.add_paln_drugs);
            if(!isCancer){
                add_plan_drugs.setVisibility(View.GONE);
            }
            content = (LinearLayout) findViewById(R.id.content);
            ll_dy = (RelativeLayout) findViewById(R.id.ll_dy);
            ll_tip = (RelativeLayout) findViewById(R.id.ll_tip);
            usage = (TextView) findViewById(R.id.tv_usage);
            img_tip = (TextView) findViewById(R.id.img_tip);
            tv_tip = (TextView) findViewById(R.id.tv_tip);
            drug_title = (TextView) findViewById(R.id.drug_title);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "CaseDetailActivity");
        }

    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.add_paln_drugs: {
                    Factory.post(this, Const.ESetPlanMedicine);
                    break;
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "CaseDetailActivity");
        }

    }

    private String PlanStepID;//添加到计划用药的id

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();

        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if(flag == Const.ESetPlanMedicine){
            args = new String[]{
                    Contants.InfoID,UserinfoData.getInfoID(this),
                    Contants.PlanStepID, PlanStepID
            };
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object t, int tag) {
        try {
            if (tag == Const.ESetPlanMedicine) {
                BaseBean bean = (BaseBean) t;
                if (Contants.RESULT.equals(bean.iResult)) {
                    showToast("添加成功");
                    UserinfoData.planId = PlanStepID;
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "CaseDetailActivity");
        }
    }

    RelativeLayout ll_dy;
    private static final String DY = "1";//单药

    private void bindView(CaseDetailBean bean) {
        try {
            String stepType = bean.getStepType();
            if (DY.equals(stepType)) {//说明单药
                ll_dy.setVisibility(View.GONE);//隐藏用法
            }
            String allDesc = bean.getAllDesc();
            if (!TextUtils.isEmpty(allDesc)) {
                usage.setText(bean.getAllDesc());

            }else{
                ll_dy.setVisibility(View.GONE);//隐藏用法
            }
            String remark = bean.getRemark();
            if (TextUtils.isEmpty(remark)) {//没有备注
                ll_tip.setVisibility(View.GONE);//隐藏备注
               //同时隐藏 文本方案介绍
            } else {
                tv_tip.setText(bean.getRemark());

            }
            if(TextUtils.isEmpty(allDesc)&&TextUtils.isEmpty(remark)){
                drug_title.setVisibility(View.GONE);
            }
            List<CaseDetailBean.MedicineNumEntity> list = bean.getMedicineNum();
            if (list != null && list.size() > 0) {
                initMedicaView(list);
            }

        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "CaseDetailActivity");
        }
    }

    private void initMedicaView(List<CaseDetailBean.MedicineNumEntity> list) {
        try {
            for (int i = 0, j = list.size(); i < j; i++) {
                CaseDetailBean.MedicineNumEntity item = list.get(i);
                ViewGroup medicaDec = (ViewGroup) LayoutInflater.from(this).inflate(
                        R.layout.layout_casedetails, content, false);//副作用过多显示3个
                boolean isLast = (i == (j - 1));
                initView(medicaDec, item, isLast);
                content.addView(medicaDec);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "CaseDetailActivity");
        }
    }

    private void initView(View medicaDec, final CaseDetailBean.MedicineNumEntity item, boolean isLast) {

        try {
            TextView tv_title = (TextView) medicaDec.findViewById(R.id.tv_title);
            TextView tv_desc = (TextView) medicaDec.findViewById(R.id.tv_desc);
            TextView effective = (TextView) medicaDec.findViewById(R.id.effective);
            TextView useage = (TextView) medicaDec.findViewById(R.id.use_durgs);
            if (isLast) {
                medicaDec.findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
            } else {
                medicaDec.findViewById(R.id.below_detail_line).setVisibility(View.GONE);
            }
            LinearLayout side_effect = (LinearLayout) medicaDec.findViewById(R.id.side_effect);//副作用
            LinearLayout ll_details = (LinearLayout) medicaDec.findViewById(R.id.ll_details);//详细说明
            ll_details.setOnClickListener(new View.OnClickListener() {//详细说明
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CaseDetailActivity.this, DrugActivity.class);
                    intent.putExtra(Contants.Drug, item);
                    startActivity(intent);
                }
            });
            if(!TextUtils.isEmpty(item.getUsage())){
//                useage.setText(item.getUsage());
//                LogCustom.i("ZYS","usage:"+item.getUsage());
                useage.setText(Html.fromHtml(item.getUsage()));
            }
            if (!TextUtils.isEmpty(item.getCommonName())){
//                tv_title.setText(item.getCommonName() + "介绍");
                tv_title.setText(Html.fromHtml(item.getCommonName() + "介绍"));
            }
            if(!TextUtils.isEmpty(item.getSuit())){
                String suit = TextSpannerUtils.getSpannerText(item.getSuit());
                tv_desc.setText(Html.fromHtml(suit));
            }
            List<CommPolicyNewEntity> newEffects= item.getNewMaybeEffect();
            String temp = null;

                if(newEffects!=null&&newEffects.size()>0){
                    temp = newEffects.get(0).getCommPolicyName();
                }

            try {
                if (!TextUtils.isEmpty(temp)) {
                    effective.setText(temp);
                    side_effect.setOnClickListener(new View.OnClickListener() {//副作用的点击
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(CaseDetailActivity.this, EffectNewActivity.class).
                                    putExtra(Const.INTENT_EFFECT, (Serializable) item.getNewMaybeEffect())
                                    .putExtra(Contants.CureConfID, item.getCureConfID())
                                    .putExtra(Const.INTENT_OR_TYPE,Const.TYPE_EFFECT);
                            startActivity(intent);
                        }
                    });
                }
            }catch (Exception e){
                ExceptionUtils.ExceptionToUM(e, this, "initView");
            }


            try {
                List<CommPolicyNewEntity> maybeBF = item.getNewMaybebingfazheng();
                if(maybeBF!=null&&maybeBF.size()>0){
                    LinearLayout ll_bf = (LinearLayout)medicaDec.findViewById(R.id.ll_mab_bingfa);
                    TextView tv_bf = (TextView)medicaDec.findViewById(R.id.tv_mab_bingfa);
                    String str = maybeBF.get(0).getCommPolicyName();
                    if(!TextUtils.isEmpty(str)) tv_bf.setText(str);
                    ll_bf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(CaseDetailActivity.this, EffectNewActivity.class).
                                    putExtra(Const.INTENT_EFFECT, (Serializable) item.getNewMaybebingfazheng())
                                    .putExtra(Contants.CureConfID, item.getCureConfID())
                                    .putExtra(Const.INTENT_OR_TYPE,Const.TYPE_COMPLICATION);
                            startActivity(intent);
                        }
                    });
                }
            }catch (Exception e){
                ExceptionUtils.ExceptionToUM(e, this, "initView");
            }

        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "CaseDetailActivity");
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

    }

    public void dismissFragment() {
        FragmentManager fm = getFragmentManager();
        Fragment f = fm.findFragmentByTag("fragment");
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(f);
        ft.commit();
    }

}

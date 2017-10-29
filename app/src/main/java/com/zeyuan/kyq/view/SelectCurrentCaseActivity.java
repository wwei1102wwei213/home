package com.zeyuan.kyq.view;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.Entity.EditInfoEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.EffectiveBean;
import com.zeyuan.kyq.bean.PatientDetailBean;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.ChooseTimeInterface;
import com.zeyuan.kyq.biz.manager.FunctionGuideManager;
import com.zeyuan.kyq.fragment.ChooseTimeFragment;
import com.zeyuan.kyq.fragment.dialog.CureTypeDialog;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.OtherUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/29.
 * 选择当前治疗方案
 */

public class SelectCurrentCaseActivity extends BaseActivity implements View.OnClickListener, HttpResponseInterface {
    //从之前页面传递过来的用户选择的数据
    EditInfoEntity editInfoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_current_case);
        initView();
        editInfoEntity = (EditInfoEntity) getIntent().getSerializableExtra("editInfoEntity");
        if (editInfoEntity == null) {
            showToast("数据传递错误！");
            finish();
        }
    }

    TextView tv_current_case;
    //ZYDatePicker zdp_case_time;
    TextView tv_effective, tv_invalid, tv_unknown;
    TextView tv_similarity_num;
    TextView tv_invalid_num, tv_unknown_num, tv_effective_num;
    TextView tv_case_start_date;

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_finish).setOnClickListener(this);
        findViewById(R.id.fl_current_case).setOnClickListener(this);
        findViewById(R.id.fl_case_start_date).setOnClickListener(this);
        tv_current_case = (TextView) findViewById(R.id.tv_current_case);
        tv_case_start_date = (TextView) findViewById(R.id.tv_case_start_date);
        //zdp_case_time = (ZYDatePicker) findViewById(R.id.zdp_case_time);
        tv_effective = (TextView) findViewById(R.id.tv_effective);
        tv_invalid = (TextView) findViewById(R.id.tv_invalid);
        tv_unknown = (TextView) findViewById(R.id.tv_unknown);
        tv_similarity_num = (TextView) findViewById(R.id.tv_similarity_num);
        tv_invalid_num = (TextView) findViewById(R.id.tv_invalid_num);
        tv_unknown_num = (TextView) findViewById(R.id.tv_unknown_num);
        tv_effective_num = (TextView) findViewById(R.id.tv_effective_num);
        tv_effective.setOnClickListener(this);
        tv_invalid.setOnClickListener(this);
        tv_unknown.setOnClickListener(this);
        setEffectiveStatus(2);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_finish:
                finishSelect();

                break;
            case R.id.fl_current_case:
                showCureTypeDialog();
                break;
            case R.id.fl_case_start_date:
                showSelectTimeDialog();
                break;
            case R.id.tv_effective:
                setEffectiveStatus(1);
                break;
            case R.id.tv_invalid:
                setEffectiveStatus(0);
                break;
            case R.id.tv_unknown:
                setEffectiveStatus(2);
                break;
        }
    }

    CureTypeDialog dialog;
    String StepID;
    String stepName;

    private void showCureTypeDialog() {
        if (dialog == null) {
            dialog = new CureTypeDialog();
            dialog.setCancerID(editInfoEntity.getCancerID());
            dialog.setDrugsNameListener(new DialogFragmentListener() {
                @Override
                public void getDataFromDialog(DialogFragment dialog, String data, int positions) {
                    if (!OtherUtils.isEmpty(data)) {
                        StepID = data;
                        stepName = DataUtils.loadStringToShowString(data);
                        tv_current_case.setText(stepName);
                        tv_current_case.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_blue));
                        getEffectiveNum();
                    }
                }
            });
        }
        if (dialog.getDialog() == null || !dialog.getDialog().isShowing())
            dialog.show(this.getFragmentManager(), "medica");
    }

    private ChooseTimeFragment timeFragment;

    private void showSelectTimeDialog() {
        if (timeFragment == null) {
            timeFragment = ChooseTimeFragment.getInstance(new ChooseTimeInterface() {
                @Override
                public void timeCallBack(String time) {
                    tv_case_start_date.setText(time);
                    tv_case_start_date.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_blue));
                    editInfoEntity.setCureStartTime(DataUtils.showTimeMillsForTimeStr(time) + "");
                }
            });
        }
        if (timeFragment.getDialog() == null || !timeFragment.getDialog().isShowing())
            timeFragment.show(getFragmentManager(), ChooseTimeFragment.type);
    }

    private void getEffectiveNum() {
        Factory.postPhp(this, Const.PApi_getStepForUserNum);
    }

    int effectiveCode;//有效码 1有效 0无效 2未知

    private void setEffectiveStatus(int effectiveCode) {
        switch (effectiveCode) {
            case 1:
                tv_effective.setSelected(true);
                tv_invalid.setSelected(false);
                tv_unknown.setSelected(false);
                break;
            case 0:
                tv_invalid.setSelected(true);
                tv_effective.setSelected(false);
                tv_unknown.setSelected(false);
                break;
            case 2:
                tv_unknown.setSelected(true);
                tv_effective.setSelected(false);
                tv_invalid.setSelected(false);

        }
        this.effectiveCode = effectiveCode;
    }

//    private String getSelectedTimeStamp() {
//        String tempMonth = zdp_case_time.getMonth();
//        tempMonth = Integer.valueOf(tempMonth) < 10 ? "0" + tempMonth : tempMonth;
//        String tempDay = zdp_case_time.getDay();
//        tempDay = Integer.valueOf(tempDay) < 10 ? "0" + tempDay : tempDay;
//        String chooseTime = zdp_case_time.getYear() + "-" + tempMonth + "-" + tempDay;
//        try {
//            //php后台所需的时间戳精确到秒
//            String selectStamp = DataUtils.showTimeToLoadTime(chooseTime);
//            //java时间戳精确到毫秒，需要再加上三位
//            long temp = Long.parseLong(selectStamp + "000");
//            if (temp > System.currentTimeMillis()) {
//                return "";
//            } else {
//                if (!TextUtils.isEmpty(chooseTime)) {
//                    return selectStamp;
//                }
//
//            }
//        } catch (Exception e) {
//
//            ExceptionUtils.ExceptionSend(e, "Choose Time Error");
//        }
//        return "";
//    }


    //完成数据选择
    private void finishSelect() {
        editInfoEntity.setStepID(StepID + "");
        editInfoEntity.setIsMedicineValid(effectiveCode + "");
        if (TextUtils.isEmpty(StepID)) {
            showToast("请选择一个治疗方案");
            showCureTypeDialog();
        } else if (TextUtils.isEmpty(editInfoEntity.getCureStartTime())) {
            showToast("请选择一个有效开始时间");
            showSelectTimeDialog();
        } else {
            Factory.postPhp(this, Const.PApi_EditInfo);

        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> params = new HashMap<>();
        if (tag == Const.PApi_getStepForUserNum) {
            params.put("StepID", StepID + "");
        } else if (tag == Const.PApi_EditInfo) {
            //Type=1  时 其余参数才有效 不然只有 careUid 参数 和 InfoID有效
            params.put("Type", "1");
            params.put("InfoID", UserinfoData.getInfoID(getApplicationContext()));
            params.put("CancerID", editInfoEntity.getCancerID());
            params.put("DiscoverTime", editInfoEntity.getDiscoverTime());
            params.put("StepID", editInfoEntity.getStepID());
            params.put("CureStartTime", editInfoEntity.getCureStartTime());
            params.put("PeriodType", editInfoEntity.getPeriodType());
            params.put("PeriodID", editInfoEntity.getPeriodID());
            params.put("IsHaveStep", "1");
            params.put("StepID", editInfoEntity.getStepID());
            params.put("isMedicineValid", editInfoEntity.getIsMedicineValid());

        }
        return params;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if (flag == Const.EGetPatientDetail) {
            args = new String[]{
                    Contants.InfoID, UserinfoData.getInfoID(getApplicationContext()),
                    Const.ISHAVESTEP, UserinfoData.getIsHaveStep(getApplicationContext())
            };
        }
        return HttpSecretUtils.getParamString(args);
    }


    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PApi_getStepForUserNum) {
            EffectiveBean bean = (EffectiveBean) response;
            if (bean.getiResult().equals(Const.RESULT)) {
                SpannableString sbs = new SpannableString("共有" + bean.getAllNum() + "位抗癌圈用户使用了" + stepName);
                sbs.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.text_blue)), 2, 2 + bean.getAllNum().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                tv_similarity_num.setText(sbs);
                tv_effective_num.setText("" + bean.getEffectiveNum() + "人有效");
                tv_invalid_num.setText("" + bean.getInvalidNum() + "人无效");
                tv_unknown_num.setText("" + bean.getUnknownNum() + "人未知");
            } else {
                showToast("数据获取失败！");
                tv_similarity_num.setText("共有?位抗癌圈用户使用了" + stepName + "");
                tv_effective_num.setText("?人有效");
                tv_invalid_num.setText("?人无效");
                tv_unknown_num.setText("?人未知");
            }
        } else if (flag == Const.PApi_EditInfo) {
            PhpUserInfoBean phpUserInfoBean = (PhpUserInfoBean) response;
            if (phpUserInfoBean.getiResult().equals(Const.RESULT)) {
                UserinfoData.saveCancerID(getApplicationContext(), editInfoEntity.getCancerID());
                UserinfoData.saveDiagnoseTime(getApplicationContext(), editInfoEntity.getDiscoverTime());
                if (!TextUtils.isEmpty(editInfoEntity.getPeriodID()))
                    UserinfoData.savePeriodID(getApplication(), editInfoEntity.getPeriodID());
                UserinfoData.saveIsHaveStep(getApplicationContext(), "1");
                //更新用户信息
                Factory.post(this, Const.EGetPatientDetail);
                FunctionGuideManager.getInstance().showFocusSameCancerUserGuide(SelectCurrentCaseActivity.this);
            }
        } else if (flag == Const.EGetPatientDetail) {
            PatientDetailBean bean = (PatientDetailBean) response;
            if (Const.RESULT.equals(bean.iResult)) {
                UserinfoData.saveUserData(getApplicationContext(), bean);
            } else {
                Toast.makeText(getApplicationContext(), "更新用户信息失败", Toast.LENGTH_SHORT).show();
//                callback.dataCallBack("1", Const.FRAGMENT_INFO_STEP, null, null);

            }
        }
    }

    @Override
    public void showLoading(int flag) {

    }

    @Override
    public void hideLoading(int flag) {

    }

    @Override
    public void showError(int flag) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 9 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();

        }
    }
}

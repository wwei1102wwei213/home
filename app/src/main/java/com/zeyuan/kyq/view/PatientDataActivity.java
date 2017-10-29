package com.zeyuan.kyq.view;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.zeyuan.kyq.Entity.PatientDataBean;
import com.zeyuan.kyq.Entity.PatientDataEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.bean.TNMObj;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.ChooseTimeInterface;
import com.zeyuan.kyq.biz.forcallback.FragmentCallBack;
import com.zeyuan.kyq.fragment.ChooseCancerFragment;
import com.zeyuan.kyq.fragment.ChooseTimeFragment;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.fragment.dialog.DigitDialog;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ConstUtils;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.SharePrefUtil;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CustomScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/23.
 * <p>
 * 患者资料页面
 *
 * @author wwei
 */
public class PatientDataActivity extends BaseActivity implements View.OnClickListener, HttpResponseInterface, FragmentCallBack
        , DialogFragmentListener, ChooseTimeInterface {

    private static final int DIGIT_TYPE = 1;
    private static final int DIGIT_TNM_TYPE = 2;
    private static final String NO_DATA = "0";

    private TextView tv_sex;
    private TextView tv_age;
    private TextView tv_height;
    private TextView tv_weight;
    private TextView tv_cancer;
    private TextView tv_discover_time;
    private TextView tv_physical_status;
    private TextView tv_other_stricken;
    private TextView tv_choose_digit;
    /*private TextView tv_translate;
    private TextView tv_gene;*/
    private TextView tv_tnm_1;
    private TextView tv_tnm_2;
    private TextView tv_tnm_3;

    private View v_tnm;
    private CustomScrollView sv;

    private int Sex;
    private int Age;
    private int Cancer;

    private int PeriodType = -1;//1 是数字分期 2 是tnm分期
    private String PeriodID = null;

    private PatientDataEntity mEntity;
    public boolean tempistnm;
    public boolean IsChange = false;
    private boolean Result_Flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent();
        setContentView(R.layout.activity_patient_data);
        try {
            initStatusBar();
            Result_Flag = getIntent().getBooleanExtra(Const.RECORD_REQUEST_FLAG, false);
            initView();
            initListener();
            initData();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "onCreate");
        }

    }

    private void initView() {
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_height = (TextView) findViewById(R.id.tv_height);
        tv_weight = (TextView) findViewById(R.id.tv_weight);
        tv_cancer = (TextView) findViewById(R.id.tv_cancer);
        tv_discover_time = (TextView) findViewById(R.id.tv_discover_time);
        tv_physical_status = (TextView) findViewById(R.id.tv_physical_status);
        tv_other_stricken = (TextView) findViewById(R.id.tv_other_stricken);
        tv_choose_digit = (TextView) findViewById(R.id.tv_choose_digit);
        /*tv_translate = (TextView)findViewById(R.id.tv_translate);
        tv_gene = (TextView)findViewById(R.id.tv_gene);*/
        tv_tnm_1 = (TextView) findViewById(R.id.tv_tnm_1);
        tv_tnm_2 = (TextView) findViewById(R.id.tv_tnm_2);
        tv_tnm_3 = (TextView) findViewById(R.id.tv_tnm_3);
        v_tnm = findViewById(R.id.v_tnm);
        sv = (CustomScrollView) findViewById(R.id.sv);
    }

    private void initListener() {
        findViewById(R.id.v_sex).setOnClickListener(this);
        findViewById(R.id.v_age).setOnClickListener(this);
        findViewById(R.id.v_height).setOnClickListener(this);
        findViewById(R.id.v_weight).setOnClickListener(this);
        findViewById(R.id.v_cancer).setOnClickListener(this);
        findViewById(R.id.v_discover_time).setOnClickListener(this);
        findViewById(R.id.v_physical_status).setOnClickListener(this);
        findViewById(R.id.v_other_stricken).setOnClickListener(this);
        findViewById(R.id.v_choose_digit).setOnClickListener(this);
        /*findViewById(R.id.v_translate).setOnClickListener(this);
        findViewById(R.id.v_gene).setOnClickListener(this);*/
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_save).setOnClickListener(this);
        findViewById(R.id.v_tnm_1).setOnClickListener(this);
        findViewById(R.id.v_tnm_2).setOnClickListener(this);
        findViewById(R.id.v_tnm_3).setOnClickListener(this);
    }

    private void initData() {
        Factory.postPhp(this, Const.PGetUserInfoForApp);
    }

    private void setViews() {
        try {
            tv_sex.setText(mEntity.getSex() == 1 ? "男" : (mEntity.getSex() == 2 ? "女" : "未填写"));
            tv_age.setText(mEntity.getAge() == 0 ? "未填写" : mEntity.getAge() + "");
            tv_cancer.setText(MapDataUtils.getCancerValues(mEntity.getCancer() + ""));
            tv_height.setText(mEntity.getPatientHeight() == 0 ? "未填写" : mEntity.getPatientHeight() + "");
            tv_weight.setText(mEntity.getPatientWeight() == 0 ? "未填写" : mEntity.getPatientWeight() + "");
            tv_physical_status.setText(mEntity.getBodyStatus() > 0 ? getBodyStatusString(mEntity.getBodyStatus() - 1) : "未填写");
            /*tv_gene.setText(TextUtils.isEmpty(mEntity.getTransferGene())?"无":mEntity.getTransferGene());
            tv_translate.setText(TextUtils.isEmpty(mEntity.getTransferRecord())?"无":mEntity.getTransferRecord());*/
            tv_discover_time.setText(DataUtils.getDate(mEntity.getDiscoverTime() + ""));
            String other = UiUtils.getOtherStrickenString(mEntity);
            tv_other_stricken.setText(TextUtils.isEmpty(other) ? "未填写" : other);
            setTNM();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "PatientDataActivity setViews");
        }
    }

    /*private String getOtherStrickenString(){
        String other = "";
        String temp = mEntity.getOtherDisease();
        String remark = mEntity.getOtherDiseaseRemark();
        if (!TextUtils.isEmpty(temp)){
            other = UiUtils.getOtherStrickenRemark(temp);
        }
        if (!TextUtils.isEmpty(remark)){
            if (TextUtils.isEmpty(other)){
                other = remark;
            }else {
                other = other + "," + remark;
            }
        }
        return other;
    }*/

    private void setTNM() {
        try {
            String periodID = MapDataUtils.getDigitValues(mEntity.getPeriodID() + "");//设置突变情况
            if (TextUtils.isEmpty(periodID) || NO_DATA.equals(periodID)) {
                tv_choose_digit.setText(R.string.no_data);
            }
            //设置分期
            if (UserinfoData.getTNM(this)) {
                tempistnm = true;
                v_tnm.setVisibility(View.VISIBLE);
                String tnmtext = UserinfoData.getTNMText(this);
                if (tnmtext.indexOf("(") != -1) {
                    if (periodID.equals(tnmtext.substring(0, tnmtext.indexOf("(")))) {

                        String tnmtemp = "";
                        if (tnmtext.indexOf("@") != -1) {
                            tnmtemp = tnmtext.substring(tnmtext.indexOf("@") + 1, tnmtext.length());
                            tTemp = tnmtemp.substring(0, tnmtemp.indexOf("@"));
                            nTemp = tnmtemp.substring(tnmtemp.indexOf("@") + 1, tnmtemp.lastIndexOf("@"));
                            mTemp = tnmtemp.substring(tnmtemp.lastIndexOf("@") + 1, tnmtemp.length());
                        }
                        tv_choose_digit.setText(tnmtext.substring(0, tnmtext.indexOf(")") + 1));
                        tnmtext = tnmtext.substring(tnmtext.indexOf("(") + 1, tnmtext.indexOf(")"));
                        tv_tnm_1.setText(tnmtext.substring(0, tnmtext.indexOf("N")));
                        tv_tnm_2.setText(tnmtext.substring(tnmtext.indexOf("N"), tnmtext.indexOf("M")));
                        tv_tnm_3.setText(tnmtext.substring(tnmtext.indexOf("M"), tnmtext.length()));
                    } else {
                        setTextString(tv_choose_digit, periodID);
                    }
                } else {
                    setTextString(tv_choose_digit, periodID);
                }
            } else {
                tempistnm = false;
                closeTNM();
                setTextString(tv_choose_digit, periodID);
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "PatientDataActivity setTNM");
        }
    }

    private ChooseCancerFragment cancerFragment;
    private ChooseTimeFragment timeFragment;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back://点击返回
                finish();
                break;
            case R.id.v_sex://点击性别
                if (mEntity != null)
                    showSelectSex();
                break;
            case R.id.v_age://点击年龄
                if (mEntity != null)
                    showInputAge();
                break;
            case R.id.v_height://点击身高
                if (mEntity != null)
                    showInputHeight();
                break;
            case R.id.v_weight://体重
                if (mEntity != null)
                    showInputWeight();
                break;
            case R.id.v_cancer://癌症种类
                try {
                    if (mEntity != null) {
                        if (cancerFragment == null) {
                            cancerFragment = ChooseCancerFragment.getInstance(this);
                        }
                        if (cancerFragment.getDialog() == null || !cancerFragment.getDialog().isShowing())
                            cancerFragment.show(getSupportFragmentManager(), ChooseCancerFragment.TAG);
                    }

                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "tag");
                }
                break;
            case R.id.v_discover_time://确诊时间
                if (mEntity != null) {
                    if (timeFragment == null) {
                        timeFragment = ChooseTimeFragment.getInstance(this);
                    }
                    timeFragment.show(getFragmentManager(), ChooseTimeFragment.type);
                }
                break;
            case R.id.v_physical_status://体能现状
                if (mEntity != null)
                    showBodyStatus();
                break;
            case R.id.v_other_stricken://其他重疾
                if (mEntity != null) {
                    String str = mEntity.getOtherDisease() + "";
                    ArrayList<String> temp = ConstUtils.getArrayListForStringExpZero(str);
                    LogCustom.i("ZYS", "temp:" + temp.toString() + "remark:" + mEntity.getOtherDiseaseRemark());
                    startActivityForResult(new Intent(this, OtherStrickenActivity.class)
                            .putStringArrayListExtra(Const.PATIENT_OTHER_STRICKEN_CHECKED, temp)
                            .putExtra(Const.PATIENT_OTHER_STRICKEN_REMARK, mEntity.getOtherDiseaseRemark()), 100);
                }

                break;
            case R.id.v_choose_digit://分期选择
                if (mEntity != null) {
                    if (!tempistnm) {
                        showDigitDataDialog();
                    } else {
                        openTNM();
                    }
                }

                break;
            case R.id.v_tnm_1://T分期选择
                showDigitTDialog();
                break;
            case R.id.v_tnm_2://N分期选择
                showDigitNDialog();
                break;
            case R.id.v_tnm_3://M分期选择
                showDigitMDialog();
                break;
            /*case R.id.v_translate://转移情况

                break;
            case R.id.v_gene://突变

                break;*/
            case R.id.tv_save://保存
                toSave();
                break;
        }
    }

    private void toSave() {
        if (IsChange) {
            if (tempistnm) {
                if (!TextUtils.isEmpty(temp)) {
                    UserinfoData.saveTNMText(this, temp + "@" + tTemp + "@" + nTemp + "@" + mTemp);
                }
            }
            UserinfoData.saveTNM(this, tempistnm);
            showProgressDialog();
            Factory.postPhp(this, Const.PEditUserInfoForApp);
        } else {
            showToast("没有更改");
        }
    }

    private ProgressDialog mProgressDialog;

    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在保存");
        mProgressDialog.show();
    }

    private void cancelProgressDialog() {
        if (mProgressDialog != null) mProgressDialog.dismiss();
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        if (tag == Const.PGetUserInfoForApp) {

        } else if (tag == Const.PEditUserInfoForApp) {
            map.put("Sex", mEntity.getSex() + "");
            map.put("Age", mEntity.getAge() + "");
            map.put("height", mEntity.getPatientHeight() + "");
            map.put("weight", mEntity.getPatientWeight() + "");
            map.put("CancerID", mEntity.getCancer() + "");
            map.put("DiscoverTime", mEntity.getDiscoverTime() + "");
            map.put("Status", mEntity.getBodyStatus() + "");
            map.put("OtherDisease", mEntity.getOtherDisease());
            map.put("OtherDiseaseRemark", mEntity.getOtherDiseaseRemarkForSecret());
            map.put("PeriodID", mEntity.getPeriodID() + "");
            map.put("PeriodType", mEntity.getPeriodType() + "");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {

        if (flag == Const.PGetUserInfoForApp) {
            PatientDataBean bean = (PatientDataBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                mEntity = bean.getData();
                setViews();
            }
        } else if (flag == Const.PEditUserInfoForApp) {
            PhpUserInfoBean bean = (PhpUserInfoBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                UserinfoData.saveUserDataChange(this, mEntity);
                showToast("保存成功");
                exit = 1;
                finish();
            } else {
                showToast("保存失败");
            }
        }

    }

    @Override
    public void showLoading(int flag) {

    }

    @Override
    public void hideLoading(int flag) {
        if (flag == Const.PEditUserInfoForApp) {
            cancelProgressDialog();
        }
    }

    @Override
    public void showError(int flag) {
        showToast("网络请求错误");
        if (flag == Const.PEditUserInfoForApp) {
            cancelProgressDialog();
        }
    }

    //癌种选择回调
    @Override
    public void dataCallBack(String str, int flag, String tag, Object obj) {
        if (flag == Const.FRAGMENT_CHOOSE_CANCER) {
            if (!TextUtils.isEmpty(str)) {
                try {
                    tv_cancer.setText(MapDataUtils.getCancerValues(str));
                    mEntity.setCancer(Integer.valueOf(str));
                    IsChange = true;
                    SharePrefUtil.clearMyCircleData();
                } catch (Exception e) {//防止类型转换失败

                }
            }
        }
    }

    //时间选择回调
    @Override
    public void timeCallBack(String time) {
        tv_discover_time.setText(time);
        mEntity.setDiscoverTime(DataUtils.showTimeMillsForTimeStr(time));
        IsChange = true;
    }

    //分期选择回调
    @Override
    public void getDataFromDialog(DialogFragment dialog, String data, int position) {
        try {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment digitDataDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_DIALOG);
            Fragment digitTDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_T_DIALOG);
            Fragment digitNDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_N_DIALOG);
            Fragment digitMDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_M_DIALOG);
            if (dialog == digitDataDialog) {//数字分期
                if (DigitDialog.SWITCH.equals(data)) {
                    tv_choose_digit.setText("未知");
                    openTNM();
                    return;
                }
                mEntity.setPeriodID(TextUtils.isEmpty(data) ? 0 : Integer.valueOf(data));
                mEntity.setPeriodType(DIGIT_TYPE);
                String temp = getDigitValues().get(data);
                tv_choose_digit.setText(temp);
                IsChange = true;
            }

            if (dialog == digitTDialog) {
                if (DigitDialog.SWITCH.equals(data)) {
                    closeTNM();
                    return;
                }
                tv_tnm_1.setText(getShow(data, 1));
                tTemp = data;
                chooseTNMFinish();
            }

            if (dialog == digitNDialog) {
                if (DigitDialog.SWITCH.equals(data)) {
                    closeTNM();
                    return;
                }
                tv_tnm_2.setText(getShow(data, 2));
                nTemp = data;
                chooseTNMFinish();

            }

            if (dialog == digitMDialog) {
                if (DigitDialog.SWITCH.equals(data)) {
                    closeTNM();
                    return;
                }
                tv_tnm_3.setText(getShow(data, 3));
                mTemp = data;
                chooseTNMFinish();
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "PatientDataActivity getDataFromDialog");
        }
    }

    private String temp;
    private String tTemp = "";
    private String nTemp = "";
    private String mTemp = "";

    private void chooseTNMFinish() {
        try {
            temp = "";
            if (!TextUtils.isEmpty(mEntity.getCancer() + "") && !TextUtils.isEmpty(tTemp) && !TextUtils.isEmpty(nTemp)
                    && !TextUtils.isEmpty(mTemp)) {
                List<TNMObj> list = (List<TNMObj>) Factory.getData(Const.N_DataTnmObjs);
                int size = list.size();
                TNMObj tnmTmp = null;
                for (int i = 0; i < size; i++) {
                    tnmTmp = list.get(i);
                    if ((tnmTmp.getCancerId().equals(mEntity.getCancer() + "")) &&
                            (tnmTmp.getTid().equals("0") || tnmTmp.getTid().equals(tTemp))
                            && (tnmTmp.getNid().equals("0") || tnmTmp.getNid().equals(nTemp))
                            && (tnmTmp.getMid().equals("0") || tnmTmp.getMid().equals(mTemp))) {
                        String digitID = tnmTmp.getDigitId();
                        String showdigitId = getDigitValues().get(digitID);
                        temp = showdigitId + "(" + getShow(tTemp, 1) + getShow(nTemp, 2) + getShow(mTemp, 3) + ")";
                        tv_choose_digit.setText(temp);
                        mEntity.setPeriodID(TextUtils.isEmpty(digitID) ? 0 : Integer.valueOf(digitID));
                        mEntity.setPeriodType(DIGIT_TNM_TYPE);
                        IsChange = true;
                        break;
                    }
                    mEntity.setPeriodID(0);
                    mEntity.setPeriodType(DIGIT_TNM_TYPE);
                    temp = "未知" + "(" + getShow(tTemp, 1) + getShow(nTemp, 2) + getShow(mTemp, 3) + ")";
                    tv_choose_digit.setText(temp);
                    IsChange = true;
                }
            } else {
                temp = "未知" + "(" + getShow(tTemp, 1) + getShow(nTemp, 2) + getShow(mTemp, 3) + ")";
                tv_choose_digit.setText("未知" + "(" + getShow(tTemp, 1) + getShow(nTemp, 2) + getShow(mTemp, 3) + ")");
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    private Map<String, String> DigitValues;

    private Map<String, String> getDigitValues() {
        if (DigitValues == null) {
            DigitValues = (Map<String, String>) Factory.getData(Const.N_DataDigitValues);
        }
        return DigitValues;
    }

    /**
     * 根据选中的id 来获得显示的tnm分期
     * d @param data
     *
     * @return
     */
    private String getShow(String data, int tnmtag) {
        if (TextUtils.isEmpty(data)) {
            return "";
        }
        String id = getDigitValues().get(data);
        StringBuffer temp = new StringBuffer(id);
        int index = id.indexOf(" ");
        StringBuffer sb = temp.delete(index, temp.length());
        return sb.toString();
    }

    /***
     * 隐藏TNM分期
     */
    private void closeTNM() {
        try {
            v_tnm.setVisibility(View.GONE);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "PatientDataActivity");
        }
    }

    /**
     * 显示输入的年龄
     */
    private void showInputAge() {
        try {
            DigitDialog dialog1 = DigitDialog.newInstance(DigitDialog.AGE, null);
            dialog1.setListener(new DialogFragmentListener() {
                @Override
                public void getDataFromDialog(DialogFragment dialog, String data, int position) {
                    if (!TextUtils.isEmpty(data)) {
                        tv_age.setText(data);
                        mEntity.setAge(Integer.valueOf(data));
                        IsChange = true;
                    }
                }
            });
            dialog1.show(getFragmentManager(), "age");
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "PatientDataActivity");
        }
    }

    /**
     * 显示输入的身高
     */
    private void showInputHeight() {
        try {
            DigitDialog dialog1 = DigitDialog.newInstance(DigitDialog.TYPE_HEIGHT, null);
            dialog1.setListener(new DialogFragmentListener() {
                @Override
                public void getDataFromDialog(DialogFragment dialog, String data, int position) {
                    if (!TextUtils.isEmpty(data)) {
                        tv_height.setText(data);
                        mEntity.setPatientHeight(Integer.valueOf(data));
                        IsChange = true;
                    }
                }
            });
            dialog1.show(getFragmentManager(), "height");
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "PatientDataActivity");
        }
    }

    /**
     * 显示输入的体重
     */
    private void showInputWeight() {
        try {
            DigitDialog dialog1 = DigitDialog.newInstance(DigitDialog.TYPE_WEIGHT, null);
            dialog1.setListener(new DialogFragmentListener() {
                @Override
                public void getDataFromDialog(DialogFragment dialog, String data, int position) {
                    if (!TextUtils.isEmpty(data)) {
                        tv_weight.setText(data);
                        mEntity.setPatientWeight(Integer.valueOf(data));
                        IsChange = true;
                    }
                }
            });
            dialog1.show(getFragmentManager(), "weight");
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "PatientDataActivity");
        }
    }

    /**
     * 弹出选择性别的选择框
     */
    private void showSelectSex() {
        try {
            new AlertView("选择性别", null, "取消", null,
                    new String[]{"男", "女"},
                    this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    switch (position) {
                        case 0:
                            tv_sex.setText("男");
                            mEntity.setSex(1);
                            IsChange = true;
                            break;
                        case 1:
                            tv_sex.setText("女");
                            mEntity.setSex(2);
                            IsChange = true;
                            break;
                    }
                }
            }).setCancelable(true).show();
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "PatientDataActivity");
        }
    }

    private final String[] Body_Status = new String[]{"活动能力完全正常", "能自由走动及从事轻体力活动，但不能从事较重的体力活动",
            "能自由走动及生活自理，没有工作能力，一大半时间起床活动", "卧床不起，生活不能自理", "死亡/频危"};

    /**
     * 弹出体能现状的选择框
     */
    private void showBodyStatus() {
        try {
            new AlertView("请选择体能现状", null, "取消", null, Body_Status,
                    this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    tv_physical_status.setText(getBodyStatusString(position));
                    mEntity.setBodyStatus(position + 1);
                    IsChange = true;
                }
            }).setCancelable(true).show();
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "PatientDataActivity");
        }
    }

    //获取体能现状字符串，防止数组越界
    private String getBodyStatusString(int position) {
        if (position < 0 || position > Body_Status.length - 1) {
            return "";
        }
        return Body_Status[position];
    }

    /***
     * 显示TNM分期
     */
    private void openTNM() {
        try {
            v_tnm.setVisibility(View.VISIBLE);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    sv.fullScroll(ScrollView.FOCUS_DOWN);//scrollview移动到底部
                }
            });
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "PatientDataActivity");
        }
    }

    /***
     * 显示T分期窗口
     *
     */
    private void showDigitTDialog() {
        try {
            DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_T, mEntity.getCancer() + "");
            dialog.setListener(this);
            dialog.show(getFragmentManager(), Contants.DIGIT_T_DIALOG);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "PatientDataActivity");
        }
    }

    /***
     * 显示N分期窗口
     *
     */
    private void showDigitNDialog() {
        try {
            DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_N, mEntity.getCancer() + "");
            dialog.setListener(this);
            dialog.show(getFragmentManager(), Contants.DIGIT_N_DIALOG);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "PatientDataActivity");
        }
    }

    /**
     * M分期
     */
    private void showDigitMDialog() {
        try {
            DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_M, mEntity.getCancer() + "");
            dialog.setListener(this);
            dialog.show(getFragmentManager(), Contants.DIGIT_M_DIALOG);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "PatientDataActivity");
        }
    }

    /**
     * 显示数字分期
     */
    private void showDigitDataDialog() {
        try {
            DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT, mEntity.getCancer() + "");
            dialog.setListener(this);
            dialog.show(getFragmentManager(), Contants.DIGIT_DIALOG);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "PatientDataActivity");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case Const.RESULT_CODE_OTHER_STRICKEN_TO_PATIENT:
                IsChange = true;
                ArrayList<String> temp = data.getStringArrayListExtra(Const.RESULT_OTHER_STRICKEN_CHECKED);
                mEntity.setOtherDisease(ConstUtils.getParamsForPic(temp));
                String str = data.getStringExtra(Const.RESULT_OTHER_STRICKEN_REMARK);
                mEntity.setOtherDiseaseRemark(str);
                LogCustom.i("ZYS", "temp:" + temp.toString() + "remark:" + mEntity.getOtherDiseaseRemark());
                String other = UiUtils.getOtherStrickenString(mEntity);
                tv_other_stricken.setText(TextUtils.isEmpty(other) ? "未填写" : other);
                break;
        }
    }

    private int exit = 0;

    @Override
    public void finish() {
        if (exit == 0) {//直接退出
            if (IsChange) {
                ZYDialog.Builder builder = new ZYDialog.Builder(this);
                builder.setTitle("提示")
                        .setMessage("信息已经修改，是否保存？")
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                toSave();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                exit = 2;
                                dialog.dismiss();
                                finish();
                            }
                        }).create().show();
            } else {
                super.finish();
            }
        } else if (exit == 1) {//选择保存时退出
            if (Result_Flag && IsChange) {
                setResult(Const.RESULT_CODE_PATIENT_DATA_TO_MEDICAL, getIntent()
                        .putExtra(Const.RESULT_FLAG_FOR_DATA_CHANGED, true));
            }
            super.finish();
        } else {
            super.finish();
        }
    }
}

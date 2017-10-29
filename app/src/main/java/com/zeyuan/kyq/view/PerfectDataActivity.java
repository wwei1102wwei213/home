package com.zeyuan.kyq.view;

/**
 * Created by Administrator on 2016/5/27.
 *
 * 确认资料
 *
 * @author wwei
 */

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.bean.CancerResuletBean;
import com.zeyuan.kyq.bean.PatientDetailBean;
import com.zeyuan.kyq.bean.TNMObj;
import com.zeyuan.kyq.bean.WSZLBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.forcallback.FragmentCallBack;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.fragment.ChooseCancerFragment;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.fragment.dialog.DigitDialog;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ConstUtils;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.List;
import java.util.Map;

public class PerfectDataActivity extends BaseActivity implements View.OnClickListener, DialogFragmentListener, ViewDataListener ,
        HttpResponseInterface ,FragmentCallBack{

    private static final String TAG = "PerfectDataActivity";
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_data);
        //保存确认时间
        UserinfoData.saveDiagnoseTime(this, System.currentTimeMillis() + "");
        flag = getIntent().getIntExtra("Diagnosis",0);
        initView();
        setListener();
    }

    private void setListener() {
    }

    private Button save;
    private TextView cancer_type;//癌症种类
    private TextView cure_case;//当前治疗方案
    private TextView period_start;//t分期
    private TextView linba;//n分期
    private TextView far_trsnsfo_case;//m分期
    private TextView digit;//分期
    private LinearLayout ll;//tnm分期所在的layout
    private LinearLayout ll_cancer_type;
    private LinearLayout ll_cure_case;
    private LinearLayout ll_digit;

    private void initView() {
        try {
            initWhiteTitle("完善资料");
            save = (Button) findViewById(R.id.save);
            cancer_type = (TextView) findViewById(R.id.cancer_type);
            ll = (LinearLayout) findViewById(R.id.ll);
            period_start = (TextView) findViewById(R.id.period_start);
            linba = (TextView) findViewById(R.id.linba);
            far_trsnsfo_case = (TextView) findViewById(R.id.far_trsnsfo_case);
            digit = (TextView) findViewById(R.id.digit);

            ll_cancer_type = (LinearLayout)findViewById(R.id.ll_cancer_type);
            ll_cure_case = (LinearLayout)findViewById(R.id.ll_cure_case);
            ll_digit = (LinearLayout)findViewById(R.id.ll_digit);
            ll_cancer_type.setOnClickListener(this);
            ll_cure_case.setOnClickListener(this);
            ll_digit.setOnClickListener(this);

            period_start.setOnClickListener(this);
            linba.setOnClickListener(this);
            far_trsnsfo_case.setOnClickListener(this);

            cure_case = (TextView) findViewById(R.id.cure_case);
            save.setOnClickListener(this);

        }catch(Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PerfectDataActivity");
        }
    }

    private boolean flags = true;//ture表示显示数字分期 false 表示 打开tnm分期

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.save: {
                    if(flag == 1){
                        String oldDigitId = UserinfoData.getPeriodID(this);
                        String oldCancerId = UserinfoData.getCancerID(this);
                        if (!oldDigitId.equals(digitID) || !oldCancerId.equals(cancerID)) {// 如果修改了分期 或者修改了癌肿 都要更新患者详情
                            bean = new PatientDetailBean();
                            if (!oldCancerId.equals(cancerID)) { //
                                bean.setCancerID(cancerID);
                            }
                            bean.setPeriodID(digitID);
                            UserinfoData.savePeriodID(this, digitID);
                            bean.setPeriodType("1");
                            Factory.post(this, Const.EUpdatePatientDetail);
                        }
                        finish();
                    }else{
                        if (TextUtils.isEmpty(cancerID) || Contants.NO_DATA.equals(cancerID)) {
                            Toast.makeText(this, getString(R.string.choose_cancer_type), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (TextUtils.isEmpty(stepID) || Contants.NO_DATA.equals(stepID)) {
                            Toast.makeText(this, getString(R.string.choose_cure_case), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String oldDigitId = UserinfoData.getPeriodID(this);
                        String oldCancerId = UserinfoData.getCancerID(this);
                        if (!oldDigitId.equals(digitID) || !oldCancerId.equals(cancerID)) {// 如果修改了分期 或者修改了癌肿 都要更新患者详情
                            bean = new PatientDetailBean();
                            if (!oldCancerId.equals(cancerID)) { //
                                bean.setCancerID(cancerID);
                            }
                            bean.setPeriodID(digitID);
                            UserinfoData.savePeriodID(this, digitID);
                            bean.setPeriodType("1");
                            Factory.post(this, Const.EUpdatePatientDetail);
                        }
                        Factory.post(this,Const.EGetConfirmSecond);
                    }

                    break;
                }
                case R.id.ll_cancer_type: {//癌症种类
                    showCancerType();
                    break;
                }
                case R.id.ll_cure_case: {//当前治疗方式
                    showChooseMedicaDialog();
                    break;
                }
                case R.id.ll_digit: {//请选择分期
                    if (TextUtils.isEmpty(cancerID)) {
                        Toast.makeText(this, "请先填写癌肿", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (flags) {
                        showDigitDataDialog();
                    } else {
                        openTNM();
                    }
                    break;
                }
                case R.id.period_start: {////原发肿瘤情况  T
                    showDigitTDialog();
                    break;
                }
                case R.id.far_trsnsfo_case: {//远处转移情况  M
                    showDigitMDialog();
                    break;
                }

                case R.id.linba: {//区域淋巴结情况 N
                    showDigitNDialog();
                    break;
                }
            }
        }catch(Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PerfectDataActivity");
        }
    }

    private void showChooseMedicaDialog() {
        Intent intent = new Intent(this, EditStepNewActivity.class);
        startActivity(intent);
    }

    private ChooseCancerFragment fragment;
    private void showCancerType() {
        if(fragment==null){
            fragment = ChooseCancerFragment.getInstance(this);
        }
        fragment.show(getSupportFragmentManager(),ChooseCancerFragment.TAG);
    }

    private String stepID;
    private String cancerID;
    private String digitID;


    @Override
    public void onResume() {
        super.onResume();
        try {
            stepID = UserinfoData.getStepID(this);
            if (cure_case != null && !TextUtils.isEmpty(stepID) && !Contants.NO_DATA.equals(stepID)) {
                cure_case.setText(DataUtils.loadStringToShowString(stepID));
            } else {
                cure_case.setText(R.string.no_data);
            }
            if (TextUtils.isEmpty(cancerID)) {
                cancerID = UserinfoData.getCancerID(this);
            }
            if (cancer_type != null && !TextUtils.isEmpty(cancerID)) {
                Map<String,String> cancerValues = (Map<String,String>)Factory.getData(Const.N_DataCancerValues);
                cancer_type.setText(cancerValues.get(cancerID));
            } else {
                cancer_type.setText(R.string.no_data);
            }
            if (TextUtils.isEmpty(digitID)) {
                digitID = UserinfoData.getPeriodID(this);
            }
            if (digit != null && !TextUtils.isEmpty(digitID)) {
                digit.setText(MapDataUtils.getDigitValues(digitID));
            } else {
                digit.setText(R.string.no_data);
            }
        }catch(Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PerfectDataActivity");
        }
    }

    //显示数字分期
    private void showDigitDataDialog() {
        DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT, cancerID);
        dialog.setListener(this);
        dialog.show(getFragmentManager(), Contants.DIGIT_DIALOG);
    }

    //显示T分期
    private void showDigitTDialog() {
        DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_T, cancerID);
        dialog.setListener(this);
        dialog.show(getFragmentManager(), Contants.DIGIT_T_DIALOG);
    }

    //显示N分期
    private void showDigitNDialog() {
        DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_N, cancerID);
        dialog.setListener(this);
        dialog.show(getFragmentManager(), Contants.DIGIT_N_DIALOG);
    }

    //显示M分期
    private void showDigitMDialog() {
        DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_M, cancerID);
        dialog.setListener(this);
        dialog.show(getFragmentManager(), Contants.DIGIT_M_DIALOG);
    }

    //打开TNM分期
    private void openTNM() {
        ll.setVisibility(View.VISIBLE);
    }

    //关闭TNM
    private void closeTNM() {
        ll.setVisibility(View.GONE);
    }

    //选择癌种回调
    @Override
    public void dataCallBack(String str, int flag, String tag, Object obj) {
        if(flag == Const.FRAGMENT_CHOOSE_CANCER){
            Map<String,String> cancerValues = (Map<String,String>)Factory.getData(Const.N_DataCancerValues);
            cancer_type.setText(cancerValues.get(str));//修改癌肿 把分期置为0.
            cancerID = str;
            digitID = "0";
            digit.setText("未知");
        }
    }

    @Override
    public void getDataFromDialog(DialogFragment dialog, String data, int position) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment digitDataDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_DIALOG);
        Fragment digitTDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_T_DIALOG);
        Fragment digitNDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_N_DIALOG);
        Fragment digitMDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_M_DIALOG);

        if (dialog == digitDataDialog) {//数字分期
            LogUtil.i(TAG, data);
            if (DigitDialog.SWITCH.equals(data)) {
                openTNM();
                flags = false;
                return;
            }
            String temp = getDigitValues().get(data);
            digit.setText(temp);// 要现实保存按钮
            digitID = data;
        }

        if (dialog == digitTDialog) {
            LogUtil.i(TAG, "digitTDialog:" + data);
            if (DigitDialog.SWITCH.equals(data)) {
                closeTNM();
                flags = true;
                return;
            }

            period_start.setText(getShow(data));
            tTemp = data;
            chooseTNMFinish();
        }

        if (dialog == digitNDialog) {
            LogUtil.i(TAG, "digitNDialog:" + data);
            if (DigitDialog.SWITCH.equals(data)) {
                closeTNM();
                flags = true;
                return;
            }
            linba.setText(getShow(data));
            nTemp = data;
            chooseTNMFinish();
        }

        if (dialog == digitMDialog) {
            LogUtil.i(TAG, "digitMDialog:" + data);
            if (DigitDialog.SWITCH.equals(data)) {
                closeTNM();
                flags = true;
                return;
            }
            far_trsnsfo_case.setText(getShow(data));
            mTemp = data;
            chooseTNMFinish();
        }
    }

    private Map<String, String> DigitValues;
    private Map<String, String> getDigitValues(){
        if(DigitValues==null){
            DigitValues = (Map<String, String>)Factory.getData(Const.N_DataDigitValues);
        }
        return DigitValues;
    }

    /**
     * 根据选中的id 来获得显示的tnm分期
     *
     * @param data
     * @return
     */
    private String getShow(String data) {
        if (TextUtils.isEmpty(data)) {
            return "";
        }
        String id = getDigitValues().get(data);
        StringBuffer temp = new StringBuffer(id);
        int index = id.indexOf(" ");
        StringBuffer sb = temp.delete(index, temp.length());
        return sb.toString();
    }

    private String tTemp;
    private String nTemp;
    private String mTemp;

    private PatientDetailBean bean;

    private void chooseTNMFinish() {
        if (!TextUtils.isEmpty(cancerID) && !TextUtils.isEmpty(tTemp) && !TextUtils.isEmpty(nTemp) && !TextUtils.isEmpty(mTemp)) {
            List<TNMObj> list = (List<TNMObj>) Factory.getData(Const.N_DataTnmObjs);
            int size = list.size();
            TNMObj tnmTmp = null;
            for (int i = 0; i < size; i++) {
                tnmTmp = list.get(i);
                if ((tnmTmp.getCancerId().equals(getCancerId())) &&
                        (tnmTmp.getTid().equals("0") || tnmTmp.getTid().equals(tTemp))

                        && (tnmTmp.getNid().equals("0") || tnmTmp.getNid().equals(nTemp))

                        && (tnmTmp.getMid().equals("0") || tnmTmp.getMid().equals(mTemp))) {
                    digitID = tnmTmp.getDigitId();
                    LogUtil.i(TAG, "digitid is :" + digitID);
                    String showdigitId = getDigitValues().get(digitID);
                    digit.setText(showdigitId);
                    break;
                } else {
                    digit.setText("未知");
                }
            }
        }
    }

    private String getCancerId() {
        return UserinfoData.getCancerID(this);
    }


    @Override
    public Map getParamInfo(int tag) {
        return null;
    }

    private String[] shareParams;
    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if(flag == Const.EUpdatePatientDetail){
            args = ConstUtils.getParamsForPatient(bean);
        }else if(flag == Const.EGetConfirmSecond){
            args = new String[]{
                    Contants.InfoID,UserinfoData.getInfoID(this),
                    Contants.StepID,stepID,
                    Contants.CancerID,cancerID,
                    Contants.PeriodID, digitID
            };
        }else if(flag == Const.EGetResultDetail){
            args = new String[]{
                    Contants.InfoID,UserinfoData.getInfoID(this),
                    Contants.StepID,stepID,
                    Contants.CancerID,cancerID,
                    Contants.PeriodID, digitID,
                    Contants.BodyStatusID,"2"
            };
            shareParams = args;
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object t, int tag) {
        if(tag == Const.EUpdatePatientDetail){
            BaseBean baseBean = (BaseBean)t;
            if(Contants.RESULT.equals(baseBean.iResult)){
                UserinfoData.saveUserData(this, bean);

            }
        }else if(tag == Const.EGetConfirmSecond){
            WSZLBean bean = (WSZLBean) t;
            if("0".equals(bean.getUiSetBit())){
                Factory.post(this,Const.EGetResultDetail);
            }else{
                startActivity(new Intent(this, WSZLActivity.class).putExtra(Contants.WSZLBean, bean));
            }
        }else if(tag == Const.EGetResultDetail){
            CancerResuletBean bean = (CancerResuletBean) t;
            if (Contants.RESULT.equals(bean.getIResult())) {
                startActivity(new Intent(this, ResultDetailActivity.class).putExtra(Contants.CancerResuletBean, bean)
                                .putExtra(Const.RESULT_PARAMS_FOR_SHARE,shareParams));
            }
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

    @Override
    public void finish() {
        int form = getIntent().getIntExtra(Const.INTENT_FROM,0);
        if(form == Const.FM){
            afterFinish();
        }
        super.finish();
    }

}

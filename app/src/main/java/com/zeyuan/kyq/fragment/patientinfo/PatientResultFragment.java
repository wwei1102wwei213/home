package com.zeyuan.kyq.fragment.patientinfo;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.forcallback.ChooseTimeInterface;
import com.zeyuan.kyq.biz.forcallback.DialogCallBack;
import com.zeyuan.kyq.fragment.ChooseTimeFragment;
import com.zeyuan.kyq.fragment.dialog.CureTypeDialog;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.fragment.dialog.NoStepDialog;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MapDataUtils;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-12-16
 * Time: 18:32
 * FIXME
 * 建立档案的最后一步
 */


public class PatientResultFragment extends PatientInfoFragment implements View.OnClickListener, DialogFragmentListener
        ,RadioGroup.OnCheckedChangeListener,DialogCallBack,ChooseTimeInterface{
    private static final String TAG = "PatientResultFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_info_step, container, false);
        initOtherTitle();
        initStepView();
        setStepLisenter();
        return rootView;
    }

    private LinearLayout ll_info_step;
    private LinearLayout ll_info_step_time;
    private TextView tv_info_step_name;
    private TextView tv_info_step_time;
    private RadioGroup rg;
    private TextView btn_finish;
    private TextView btn_finish_no_step;

    private void initStepView(){
        try {
            ll_info_step = (LinearLayout)findViewById(R.id.ll_info_step);
            ll_info_step_time = (LinearLayout)findViewById(R.id.ll_info_step_time);
            tv_info_step_name = (TextView)findViewById(R.id.tv_info_step_name);
            tv_info_step_time = (TextView)findViewById(R.id.tv_info_step_time);
            rg = (RadioGroup)findViewById(R.id.rg_info_step);
            btn_finish = (TextView)findViewById(R.id.btn_info_step_finish);
            btn_finish_no_step = (TextView)findViewById(R.id.btn_info_step_no);

        }catch (Exception e){

        }
    }

    private void initOtherTitle(){
        TextView title = (TextView)findViewById(R.id.tv_other_title);
        title.setText("选择治疗方案");
        findViewById(R.id.iv_other_title_back).setVisibility(View.GONE);
        LinearLayout ll_return_back = (LinearLayout)findViewById(R.id.ll_return_back);
        ll_return_back.setVisibility(View.VISIBLE);
        ll_return_back.setOnClickListener(this);
    }

    private void setStepLisenter(){

        ll_info_step.setOnClickListener(this);
        ll_info_step_time.setOnClickListener(this);
        btn_finish.setOnClickListener(this);
        btn_finish_no_step.setOnClickListener(this);
        rg.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_info_step_yes:
                IsMedicineValid = "1";
                break;
            case R.id.rb_info_step_no:
                IsMedicineValid = "0";
                break;
        }
    }

    private ChooseTimeFragment fragment;
    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.ll_info_step: {
                    CureTypeDialog dilog = new CureTypeDialog();
                    dilog.setDrugsNameListener(this);
                    dilog.show(getFragmentManager(), "dialog");
                    break;
                }
                case R.id.ll_info_step_time: {
                    try {
                        if(fragment==null){
                            fragment = ChooseTimeFragment.getInstance(this);
                        }
                        fragment.show(getFragmentManager(),ChooseTimeFragment.type);
                    }catch (Exception e){

                    }
                    break;
                }
                case R.id.ll_return_back: {
                    if (onLastStepClickListener != null) {
                        onLastStepClickListener.onLastStepClickListener(this);
                    }
                    break;
                }
                case R.id.btn_info_step_finish: {
                    if (TextUtils.isEmpty(StepID)) {
                        Toast.makeText(getActivity(), "请输入当前治疗方案", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!TextUtils.isEmpty(IsMedicineValid)) {
                        setIsMedicineValid(IsMedicineValid);
                    }
                    if (!TextUtils.isEmpty(CureStartTime)) {
                        setCureStartTime(CureStartTime);
                    }
                    setStepID(StepID);
//                    SharePrefUtil.saveString(context, Contants.StepID, StepID);
                    if (onNextStepClickListener != null) {
                        onNextStepClickListener.onNextStepClickListener(this);
                        btn_finish.setClickable(false);
                        btn_finish_no_step.setClickable(false);
                    }
                    break;
                }
                case R.id.btn_info_step_no:{
                    showNoStepDialog();
                    break;
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "patientresultfragment");
        }
    }

    private NoStepDialog mDialog;
    private void showNoStepDialog(){
        if(mDialog==null){
            mDialog = new NoStepDialog();
        }
        mDialog.setDialogCallBack(this);
        mDialog.show(getActivity().getFragmentManager(),NoStepDialog.type);
    }




    /***
     *
     * 选择方案窗口回调
     *
     * @param dialog 表明是哪个dialog
     * @param data 数据
     * @param position 数据的标记
     */
    @Override
    public void getDataFromDialog(DialogFragment dialog, String data, int position) {
        try {
            LogUtil.i(TAG, data);
            String showData = MapDataUtils.getAllStepName(data);
            tv_info_step_name.setText(showData);
            StepID = data;
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "PatientResultFragment");
        }
    }

    /**
     * 刷新控件点击事件
     */
    public void getResume() {
        if (btn_finish != null) {
            btn_finish.setClickable(true);
        }
        if(btn_finish_no_step!=null){
            btn_finish_no_step.setClickable(true);
        }
    }

    private String CureStartTime;

    /**
     * 设置方案开始时间
     * @param cureStartTime
     */
    public void setCureStartTime(String cureStartTime) {
        try {
            getPatientInfoActivity().setCureStartTime(cureStartTime);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "PatientResultFragment");
        }
    }

    private String StepID;

    /**
     * 设置治疗方案id
     * @param stepID
     */
    public void setStepID(String stepID) {
        try {
            getPatientInfoActivity().setStepID(stepID);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "patientresultfragment");
        }
    }

    private String IsMedicineValid;

    /**
     * 设置治疗方案是否有效
     * @param isMedicineValid
     */
    public void setIsMedicineValid(String isMedicineValid) {
        try {
            getPatientInfoActivity().setIsMedicineValid(isMedicineValid);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "patientresultfragment");
        }
    }

    /**
     * 设置是否填写阶段标识
     */
    public void setNoStep(){
        try {
            getPatientInfoActivity().setNoStepFlag(true);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "setNoStep");
        }
    }

    /***
     *
     * Dialog回调
     *
     * @param tag
     * @param flag
     */
    @Override
    public void getDataCallBack(String tag, int flag) {
        if(flag == NoStepDialog.NO_STEP){//如果用户选择暂不填写
            if (onNextStepClickListener != null) {
                setNoStep();
                onNextStepClickListener.onNextStepClickListener(this);
                btn_finish.setClickable(false);
                btn_finish_no_step.setClickable(false);
            }
        }
    }

    /***
     *
     * 时间控件回调
     *
     * @param time
     */
    @Override
    public void timeCallBack(String time) {
        tv_info_step_time.setText(time);
        CureStartTime = DataUtils.showTimeToLoadTime(time);
    }
}

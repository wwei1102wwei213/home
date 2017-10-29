package com.zeyuan.kyq.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.AddStepBean;
import com.zeyuan.kyq.bean.PatientDetailBean;
import com.zeyuan.kyq.biz.forcallback.ChooseTimeInterface;
import com.zeyuan.kyq.biz.forcallback.DialogCallBack;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.forcallback.FragmentCallBack;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.fragment.dialog.CureTypeDialog;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.fragment.dialog.NoStepDialog;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.SharePrefUtil;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.Map;

/**
 * Created by Administrator on 2016/7/4.
 *
 * 添加阶段窗口
 *
 * @author wwei
 */
public class InfoStepFragment extends DialogFragment implements View.OnClickListener,RadioGroup.OnCheckedChangeListener, DialogFragmentListener
        , DialogCallBack,ChooseTimeInterface,HttpResponseInterface {

    public static final String type = "InfoStepFragment";
    //退出时需要回调的标识
    public static final int FOR_RESULT = 10;
    private int IS_SUCCESS = 0;

    public static InfoStepFragment instance;
    private FragmentCallBack callback;
    private int flag;
    private LinearLayout ll_info_step;
    private LinearLayout ll_info_step_time;
    private TextView tv_info_step_name;
    private TextView tv_info_step_time;
    private RadioGroup rg;
    private TextView btn_finish;
    private TextView btn_finish_no_step;
    private String IsMedicineValid;
    private String StepID;
    private String CureStartTime;
//    public static final int SUPPLEMENT_STEP = 1;

    public static InfoStepFragment getInstance(FragmentCallBack callback,int flag){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Const.FRAGMENT_CALL_BACK,callback);
        bundle.putInt(Const.FRAGMENT_TYPE, flag);
        instance = new InfoStepFragment();
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            Bundle bundle = getArguments();
            callback = (FragmentCallBack)bundle.getSerializable(Const.FRAGMENT_CALL_BACK);
            flag = bundle.getInt(Const.FRAGMENT_TYPE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.cancer_dialog);
        dialog.setCancelable(false);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_info_step, null);
        initView(view);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.choose_step_type);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    private void initView(View v){

        TextView title = (TextView)v.findViewById(R.id.tv_other_title);
        title.setText("选择治疗方案");
        (v.findViewById(R.id.iv_other_title_back)).setOnClickListener(this);

        ll_info_step = (LinearLayout)v.findViewById(R.id.ll_info_step);
        ll_info_step_time = (LinearLayout)v.findViewById(R.id.ll_info_step_time);
        tv_info_step_name = (TextView)v.findViewById(R.id.tv_info_step_name);
        tv_info_step_time = (TextView)v.findViewById(R.id.tv_info_step_time);
        rg = (RadioGroup)v.findViewById(R.id.rg_info_step);
        btn_finish = (TextView)v.findViewById(R.id.btn_info_step_finish);
        btn_finish_no_step = (TextView)v.findViewById(R.id.btn_info_step_no);

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

    private ChooseTimeFragment mChooseTime;
    private CureTypeDialog mCureDialog;
    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.ll_info_step: {
                    mCureDialog = new CureTypeDialog();
                    mCureDialog.setDrugsNameListener(this);
                    mCureDialog.show(getActivity().getFragmentManager(), "dialog");
                    break;
                }
                case R.id.ll_info_step_time: {
                    if(mChooseTime==null){
                        mChooseTime = ChooseTimeFragment.getInstance(this);
                    }
                    mChooseTime.show(getActivity().getFragmentManager(), ChooseTimeFragment.type);
                    break;
                }
                case R.id.btn_info_step_finish: {
                    if (TextUtils.isEmpty(StepID)) {
                        Toast.makeText(getActivity(), "请输入当前治疗方案", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(IsMedicineValid)) {
                        IsMedicineValid = "1";
                    }
                    if (TextUtils.isEmpty(CureStartTime)) {
                        CureStartTime = System.currentTimeMillis()/1000+"";
                    }
                    SharePrefUtil.saveString(getActivity(), Contants.StepID, StepID);
                    btn_finish.setClickable(false);
                    btn_finish_no_step.setClickable(false);

                    Factory.post(this, Const.EAddUserStep);


                    break;
                }
                case R.id.btn_info_step_no:{
                    showNoStepDialog();
                    break;
                }
                case R.id.iv_other_title_back:{

                    dismiss();
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "InfoStepFragment");
        }
    }

    @Override
    public void getDataFromDialog(android.app.DialogFragment dialog, String data, int position) {
        try {
            String showData = MapDataUtils.getAllStepName(data);
            tv_info_step_name.setText(showData);
            StepID = data;
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "InfoStepFragment");
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

    @Override
    public void getDataCallBack(String tag, int flag) {
        if(mDialog!=null){
            mDialog.dismiss();
        }
        if(flag==NoStepDialog.NO_STEP){
            dismiss();
        }else if(flag==NoStepDialog.GO_ON){

        }
    }


    @Override
    public void timeCallBack(String time) {
        tv_info_step_time.setText(time);
        CureStartTime = DataUtils.showTimeToLoadTime(time);
    }

    @Override
    public Map getParamInfo(int tag) {
        return null;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if(flag == Const.EAddUserStep){
            args = new String[]{
                    Contants.InfoID, UserinfoData.getInfoID(getActivity()),
                    Contants.StepID,StepID,
                    Contants.StartTime,CureStartTime,
                    Contants.IsMedicineValid,IsMedicineValid,
                    Const.ISHAVESTEP,UserinfoData.getIsHaveStep(getActivity())
            };
        }else if(flag == Const.EGetPatientDetail){
            args = new String[]{
                    Contants.InfoID, UserinfoData.getInfoID(getActivity()),
                    Const.ISHAVESTEP,UserinfoData.getIsHaveStep(getActivity())
            };
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object response, int flag) {

        if(flag == Const.EAddUserStep){
            LogCustom.i("ZYS", "DATA:" + response.toString());
            AddStepBean bean = (AddStepBean)response;
            if(Const.RESULT.equals(bean.getiResult())){
                Factory.post(this, Const.EGetPatientDetail);
            }else {
                Toast.makeText(getActivity(),"添加阶段失败",Toast.LENGTH_SHORT).show();
                btn_finish.setClickable(true);
                btn_finish_no_step.setClickable(true);
            }
        }else if(flag == Const.EGetPatientDetail){
            PatientDetailBean bean = (PatientDetailBean)response;
            if(Const.RESULT.equals(bean.iResult)){
                UserinfoData.saveUserData(getActivity(), bean);
                callback.dataCallBack(null, Const.FRAGMENT_INFO_STEP, null, null);
                IS_SUCCESS = 1;
                dismiss();
            }else {
                Toast.makeText(getActivity(),"更新用户信息失败",Toast.LENGTH_SHORT).show();
//                callback.dataCallBack("1", Const.FRAGMENT_INFO_STEP, null, null);
                dismiss();
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
        if(flag == Const.EAddUserStep){
            LogCustom.i("ZYS", "补阶段失败");
            btn_finish.setClickable(true);
            btn_finish_no_step.setClickable(true);
        }else if (flag == Const.EGetPatientDetail){
//            callback.dataCallBack("1", Const.FRAGMENT_INFO_STEP, null, null);
        }
    }

    @Override
    public void dismiss() {
        if (flag == FOR_RESULT){
            if (IS_SUCCESS==1){
                callback.dataCallBack("0", Const.FRAGMENT_INFO_STEP, null, null);
                IS_SUCCESS = 2;
            }else if(IS_SUCCESS ==2 || IS_SUCCESS == 3){

            }else {
                callback.dataCallBack("1", Const.FRAGMENT_INFO_STEP, null, null);
                IS_SUCCESS = 3;
            }
        }
        super.dismiss();
    }

    @Override
    public void onPause() {
        if(mDialog!=null) {
            mDialog.dismiss();
        }
        if(mCureDialog!=null){
            mCureDialog.dismiss();
        }
        dismiss();
        super.onPause();
    }
}

package com.zeyuan.kyq.fragment.patientinfo;

import android.app.Fragment;

import com.zeyuan.kyq.app.BaseFragment;
import com.zeyuan.kyq.view.PatientInfoActivity;

/**
 * Created by Administrator on 2015/10/13.
 * 这个是建立档案 各个fragment中抽取出来的共性
 * 其中包括 返回上一步 和下一步的监听接口
 */
public class PatientInfoFragment extends BaseFragment {

    /**
     * 下一步的监听
     */
    public interface OnNextStepClickListener {
        void onNextStepClickListener(Fragment fragment);
    }
    protected OnNextStepClickListener onNextStepClickListener;
    public void setOnNextStepClickListener(OnNextStepClickListener onNextStepClickListener) {
        this.onNextStepClickListener = onNextStepClickListener;
    }

    /**
     * 上一步的监听
     */
    public interface OnLastStepClickListener {
        void onLastStepClickListener(Fragment fragment);
    }

    protected OnLastStepClickListener onLastStepClickListener;

    public void setOnLastStepClickListener(OnLastStepClickListener onLastStepClickListener) {
        this.onLastStepClickListener = onLastStepClickListener;
    }

    protected PatientInfoActivity getPatientInfoActivity() {
        if (context instanceof PatientInfoActivity) {
           return  ((PatientInfoActivity) context);
        }
        return null;
    }

    public void getResume() {

    }

}

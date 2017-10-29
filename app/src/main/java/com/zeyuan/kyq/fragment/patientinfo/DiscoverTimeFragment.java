package com.zeyuan.kyq.fragment.patientinfo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.forcallback.ChooseTimeInterface;
import com.zeyuan.kyq.fragment.ChooseTimeFragment;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.widget.DoubleTvLayout;

/**
 * 建立档案的选择患者确诊时间窗口
 */
public class DiscoverTimeFragment extends PatientInfoFragment implements View.OnClickListener ,ChooseTimeInterface{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_location, container, false);
        initView();
        return rootView;
    }

    private TextView back, next_step;//下一步
    private TextView title;
    private DoubleTvLayout time;

    private void initView() {
        try {
            time = (DoubleTvLayout) findViewById(R.id.time);
            time.setOnClickListener(this);
            ImageView img = (ImageView) findViewById(R.id.btn_back);
            img.setVisibility(View.GONE);
            title = (TextView) findViewById(R.id.title);
            title.setText(R.string.patient_info);
            back = (TextView) findViewById(R.id.text_back);
            back.setVisibility(View.VISIBLE);
            next_step = (Button) findViewById(R.id.next_step);
            back.setOnClickListener(this);
            next_step.setOnClickListener(this);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "discoverTimeFragment");
        }

    }

    private ChooseTimeFragment fragment;
    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.text_back:
                    if (onLastStepClickListener != null) {
                        onLastStepClickListener.onLastStepClickListener(this);
                    }
                    break;
                case R.id.next_step:
                    if (onNextStepClickListener != null) {
                        if(!TextUtils.isEmpty(discoverTime)) {
                            setDiscoverTime(discoverTime);
                        }
                        next_step.setClickable(false);
                        onNextStepClickListener.onNextStepClickListener(this);
                    }
                    break;
                case R.id.time:
//                    showTimePicker();
                    if(fragment==null){
                        fragment = ChooseTimeFragment.getInstance(this);
                    }
                    fragment.show(getFragmentManager(),ChooseTimeFragment.type);
                    break;
                default:
                    throw new RuntimeException();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "discovertimefragment");
        }
    }

    private String discoverTime;

    @Override
    public void timeCallBack(String ctime) {
        time.setRightTxt(ctime);
        discoverTime = DataUtils.showTimeToLoadTime(ctime);
    }

    public void getResume() {
        try {
            if (next_step != null) {
                next_step.setClickable(true);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "discovertimefragment");
        }
    }

    private void setDiscoverTime(String time) {
        try {
            getPatientInfoActivity().setDiscoverTime(time);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "discovertimefragment");
        }
    }
}

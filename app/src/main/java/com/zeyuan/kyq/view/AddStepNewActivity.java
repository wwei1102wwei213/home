package com.zeyuan.kyq.view;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.ChooseTimeNewInterface;
import com.zeyuan.kyq.biz.forcallback.EditBackInterface;
import com.zeyuan.kyq.fragment.ChooseTimeEditStepFragment;
import com.zeyuan.kyq.fragment.ChooseTimeFragment;
import com.zeyuan.kyq.fragment.CustomEditFragment;
import com.zeyuan.kyq.fragment.dialog.CureTypeDialog;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.DecryptUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.OtherUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/10.
 *
 * 新增阶段页面
 *
 * @author wwei
 */
public class AddStepNewActivity extends BaseActivity implements HttpResponseInterface{

    private int isValid = 1;
    private int isNow = 0;
    private int StepID = 0;
    private String Remark = "";
    private boolean isChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent();
        setContentView(R.layout.activity_add_step_new);
        initStatusBar();
        initView();
        initListener();
    }

    //选择阶段区域
    private View v_step;
    private TextView tv_step;
    //选择开始时间区域
    private View v_start;
    private TextView tv_start;
    //选择结束时间区域
    private View v_end;
    private TextView tv_end;
    //是否有效控件
    private TextView tv_valid;
    private TextView tv_no_valid;
    //阶段说明控件
    private TextView tv_remark;
    //保存按钮
    private TextView tv_save;
    //当前阶段选择控件
    private View v_now_step;
    private TextView tv_now_bg;
    private TextView tv_now_circle;
    private void initView(){

        v_step = findViewById(R.id.v_step);
        tv_step = (TextView)findViewById(R.id.tv_step);
        v_start = findViewById(R.id.v_start_time);
        tv_start = (TextView)findViewById(R.id.tv_start_time);
        v_end = findViewById(R.id.v_end_time);
        tv_end = (TextView)findViewById(R.id.tv_end_time);
        tv_remark = (TextView)findViewById(R.id.tv_remark);

        tv_valid = (TextView)findViewById(R.id.tv_valid);
        tv_valid.setSelected(true);
        tv_no_valid = (TextView)findViewById(R.id.tv_no_valid);
        tv_save = (TextView)findViewById(R.id.tv_save);

        v_now_step = findViewById(R.id.v_now_step);
        tv_now_bg = (TextView)findViewById(R.id.tv_select_bg);
        tv_now_circle = (TextView)findViewById(R.id.tv_select_point);

        tv_now_bg.setSelected(false);
        tv_now_circle.setSelected(false);

    }

    private void initData(){
        Factory.postPhp(this, Const.PAddAppStepUser);
    }

    private void initListener(){

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        v_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseTimeEditStepFragment fragment = ChooseTimeEditStepFragment.getInstance(new ChooseTimeNewInterface() {
                    @Override
                    public void onTimeCallBack(String time, int ViewTag, int selection) {
                        tv_start.setText(DataUtils.getStartTime(Integer.valueOf(time)));
                    }
                },false,0,0,"0");
                fragment.show(AddStepNewActivity.this.getFragmentManager(), ChooseTimeFragment.type);
            }
        });

        v_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseTimeEditStepFragment fragment = ChooseTimeEditStepFragment.getInstance(new ChooseTimeNewInterface() {
                    @Override
                    public void onTimeCallBack(String time, int ViewTag, int selection) {
                        tv_end.setText(DataUtils.getEndTime(Integer.valueOf(time)));
                    }
                },true,0,0,"0");
                fragment.show(AddStepNewActivity.this.getFragmentManager(), ChooseTimeFragment.type);
            }
        });

        tv_valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tv_valid.isSelected()){
                    tv_valid.setSelected(true);
                    tv_no_valid.setSelected(false);
                    isValid = 1;
                }
            }
        });

        tv_no_valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tv_no_valid.isSelected()){
                    tv_valid.setSelected(false);
                    tv_no_valid.setSelected(true);
                    isValid = 0;
                }
            }
        });

        tv_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remark = tv_remark.getText().toString();
                CustomEditFragment fragment = CustomEditFragment.getInstance(new EditBackInterface() {
                    @Override
                    public void editCallBack(String text) {
                        Remark = TextUtils.isEmpty(text)?"":text;
                        tv_remark.setText(Remark);
                    }
                }, TextUtils.isEmpty(remark)?"":remark, null, 0);
                fragment.show(AddStepNewActivity.this.getFragmentManager(), CustomEditFragment.type);
            }
        });

        v_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CureTypeDialog dialog = new CureTypeDialog();
                dialog.setCancerID(UserinfoData.getCancerID(AddStepNewActivity.this));
                dialog.setDrugsNameListener(new DialogFragmentListener() {
                    @Override
                    public void getDataFromDialog(DialogFragment dialog, String data, int positions) {
                        if (!OtherUtils.isEmpty(data)) {
                            StepID = Integer.valueOf(data);
                            tv_step.setText(DataUtils.loadStringToShowString(data));
                        }
                    }
                });
                dialog.show(AddStepNewActivity.this.getFragmentManager(), "medica");
            }
        });

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSave();
            }
        });

        v_now_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v_now_step.setClickable(false);
                changeNowStep();
            }
        });
    }

    private Animation a;
    private Animation b;
    private void changeNowStep(){
        if (a==null){
            a = AnimationUtils.loadAnimation(this,R.anim.switch_translate_to_right);
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    v_now_step.setClickable(true);
                    tv_now_bg.setSelected(true);
                    tv_now_circle.setSelected(true);
                    isNow = 1;
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            b = AnimationUtils.loadAnimation(this,R.anim.switch_translate_to_left);
            b.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    v_now_step.setClickable(true);
                    tv_now_bg.setSelected(false);
                    tv_now_circle.setSelected(false);
                    isNow = 0;
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
        }
        if (isNow==0){
            tv_now_circle.startAnimation(a);
        }else {
            tv_now_circle.startAnimation(b);
        }
    }

    private String startTime = "";
    private String endTime = "";
    //点击保存
    private void toSave(){
        if (StepID==0) {
            showToast("请选择治疗方案");
            return;
        }
        if (TextUtils.isEmpty(tv_start.getText())){
            showToast("请选择起始日期");
            return;
        }
        if (TextUtils.isEmpty(tv_end.getText())){
            showToast("请选择结束日期");
            return;
        }

        startTime = DataUtils.showTimeToLoadTime2(tv_start.getText().toString());
        endTime = DataUtils.showTimeToLoadTime2(tv_end.getText().toString());
        try {
            long temp = Long.valueOf(startTime) - Long.valueOf(endTime);
            if (Long.valueOf(endTime)!=0&&temp>0){
                showToast("起始日期不能大于结束日期，请重新选择");
                return;
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"开始和结束时间大小判断出错");
        }

        Factory.postPhp(this,Const.PAddAppStepUser);

    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String,String> map = new HashMap<>();
        if (tag == Const.PAddAppStepUser){
            map.put(Contants.InfoID,UserinfoData.getInfoID(this));
            map.put(Contants.StepID,StepID+"");
            map.put("BeginTime",startTime);
            map.put("EndTime",endTime);
            map.put("isMedicineValid",isValid+"");
            map.put("Remark",TextUtils.isEmpty(Remark)?"": DecryptUtils.encode(Remark));
            map.put("IsNowStep",isNow+"");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PAddAppStepUser){
            PhpUserInfoBean bean = (PhpUserInfoBean)response;
            if (Const.RESULT.equals(bean.getiResult())){
                isChanged = true;
                showToast("阶段添加成功");
                if (isNow==1){
                    UserinfoData.saveStepID(this,StepID+"");
                }
                finish();
            }else {
                showToast("阶段添加失败");
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
        showToast("网络请求失败");
    }

    @Override
    public void finish() {
        this.setResult(100, getIntent().putExtra("isChanged", isChanged));
        super.finish();
    }

}

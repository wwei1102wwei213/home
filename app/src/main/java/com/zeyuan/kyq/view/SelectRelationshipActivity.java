package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.Entity.CreateUserParamsEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.CurrentNumBean;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.SharePrefUtil;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/22.
 * 选择与患者关系
 */

public class SelectRelationshipActivity extends BaseActivity implements View.OnClickListener, HttpResponseInterface {
    TextView tv_similarity_num;
    TextView tv_relation_relative;
    TextView tv_relation_self;
    TextView tv_relation_not_all;
    Button btn_finish;
    String currentName;
    CreateUserParamsEntity createUserParamsEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_relationship);
        currentName = getIntent().getStringExtra("CurrentName");
        createUserParamsEntity = (CreateUserParamsEntity) getIntent().getSerializableExtra("createUserParamsEntity");
        if (createUserParamsEntity == null) {
            showToast("数据传输出错！");
            finish();
        }
        initView();

        Factory.postPhp(this, Const.PApi_GetCurrentNum);
    }

    private void initView() {
        tv_similarity_num = (TextView) findViewById(R.id.tv_similarity_num);
        tv_relation_relative = (TextView) findViewById(R.id.tv_relation_relative);
        tv_relation_self = (TextView) findViewById(R.id.tv_relation_self);
        tv_relation_not_all = (TextView) findViewById(R.id.tv_relation_not_all);
        Button btn_last_step = (Button) findViewById(R.id.btn_last_step);
        btn_finish = (Button) findViewById(R.id.btn_finish);
        tv_relation_relative.setOnClickListener(this);
        tv_relation_self.setOnClickListener(this);
        tv_relation_not_all.setOnClickListener(this);
        btn_last_step.setOnClickListener(this);
        btn_finish.setOnClickListener(this);
        setSelectStatus(2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_relation_self:
                setSelectStatus(1);
                break;
            case R.id.tv_relation_relative:
                setSelectStatus(2);
                break;
            case R.id.tv_relation_not_all:
                setSelectStatus(3);
                break;
            case R.id.btn_last_step:
                finish();
                break;
            case R.id.btn_finish:
                Factory.postPhp(this, Const.PCreateUserInfo);//创建用户档案
                btn_finish.setEnabled(false);
                break;
        }
    }


    int IsSelf;//1是本人 2亲戚 3都不是

    private void setSelectStatus(int witch) {
        clearSelectStatus();
        switch (witch) {
            case 1:
                tv_relation_self.setSelected(true);
                break;
            case 2:
                tv_relation_relative.setSelected(true);
                break;
            case 3:
                tv_relation_not_all.setSelected(true);
                break;
        }
        IsSelf = witch;
    }

    private void clearSelectStatus() {
        tv_relation_relative.setSelected(false);
        tv_relation_self.setSelected(false);
        tv_relation_not_all.setSelected(false);
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> params = new HashMap<>();
        if (tag == Const.PApi_GetCurrentNum) {
            params.put("Current", createUserParamsEntity.getCurrent());
        } else if (tag == Const.PCreateUserInfo) {//用户创建档案
            params.put("Current", createUserParamsEntity.getCurrent());
            params.put("IsSelf", IsSelf + "");
            params.put("InfoName", UserinfoData.getInfoname(getApplicationContext()));
            params.put("headurl", UserinfoData.getAvatarUrl(getApplicationContext()));
            params.put("apptype", "2");
            params.put("loginType", UserinfoData.getLoginType(getApplicationContext()));
            params.put("openid", createUserParamsEntity.getmOpenID());
            params.put("phone", createUserParamsEntity.getPhone());
            params.put("unid", createUserParamsEntity.getmUnionID());
        }

        return params;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PApi_GetCurrentNum) {
            CurrentNumBean currentNumBean = (CurrentNumBean) response;
            if (currentNumBean.getiResult().equals(Const.RESULT)) {
                tv_similarity_num.setText("同处于" + currentName + "阶段的患者有" + currentNumBean.getNum() + "位，一切都会好的");
            }
        } else if (flag == Const.PCreateUserInfo) {
            btn_finish.setEnabled(true);
            PhpUserInfoBean bean = (PhpUserInfoBean) response;
            if (Const.RESULT.equals(bean.getiResult()) && !TextUtils.isEmpty(bean.getInfoID())) {
                UserinfoData.savaCurrent(getApplicationContext(), createUserParamsEntity.getCurrent());
                UserinfoData.saveInfoID(this, bean.getInfoID());
                Const.InfoID = bean.getInfoID();
                UserinfoData.saveIsHaveCreateInfo21(this, "1");
                Factory.onEvent(this, Const.EVENT_CreateInfo, Const.EVENTFLAG, getExtras());
                SharePrefUtil.saveBoolean(this, Contants.hasRecord, true);//保存信息说明档案也完成
                Toast.makeText(this, R.string.cret_info_sucess, Toast.LENGTH_SHORT).show();
                UserinfoData.saveIsMyself(getApplicationContext(), IsSelf + "");
                startActivity(new Intent(this, MainActivity.class));
                setResult(RESULT_OK);
                finish();
            } else {
                showToast("用户创建失败！");
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
        btn_finish.setEnabled(true);
    }

    //获取屏幕参数
    public String getExtras() {
        String metricStr = null;
        try {
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            int width = metric.widthPixels;     // 屏幕宽度（像素）
            int height = metric.heightPixels;   // 屏幕高度（像素）
            float density = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）
            int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
            metricStr = "metric：" + width + "," + height + "," + density + "," + densityDpi;
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "initExtras,PatientInfoActivity");
        }
        if (TextUtils.isEmpty(metricStr)) {
            metricStr = "提取屏幕信息失败";
        }
        return metricStr;
    }
}

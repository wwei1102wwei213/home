package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.CreateUserParamsEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;

/**
 * Created by Administrator on 2017/8/22.
 * 选择患者当前状态
 */

public class SelectCurrentStatusActivity extends BaseActivity implements View.OnClickListener {
    TextView tv_prevent;//预防
    TextView tv_diagnosis_undetermined;//未确诊
    TextView tv_newly_diagnosed;//新确诊
    TextView tv_in_treatment;//治疗中
    TextView tv_empty_window;//康复空窗期
    TextView tv_recrudescence;//复发治疗期
    CreateUserParamsEntity createUserParamsEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_current_status);
        createUserParamsEntity = (CreateUserParamsEntity) getIntent().getSerializableExtra("createUserParamsEntity");
        if (createUserParamsEntity == null) {
            showToast("数据传输出错！");
            finish();
        }
        initView();
    }

    private void initView() {
        // View tv_look_around = findViewById(R.id.tv_look_around);
        tv_prevent = (TextView) findViewById(R.id.tv_prevent);
        tv_diagnosis_undetermined = (TextView) findViewById(R.id.tv_diagnosis_undetermined);
        tv_newly_diagnosed = (TextView) findViewById(R.id.tv_newly_diagnosed);
        tv_in_treatment = (TextView) findViewById(R.id.tv_in_treatment);
        tv_empty_window = (TextView) findViewById(R.id.tv_empty_window);
        tv_recrudescence = (TextView) findViewById(R.id.tv_recrudescence);
        View btn_next = findViewById(R.id.btn_next);

        // tv_look_around.setOnClickListener(this);
        tv_prevent.setOnClickListener(this);
        tv_diagnosis_undetermined.setOnClickListener(this);
        tv_newly_diagnosed.setOnClickListener(this);
        tv_in_treatment.setOnClickListener(this);
        tv_empty_window.setOnClickListener(this);
        tv_recrudescence.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        setSelectStatus(1);
    }

    //选择的状态
    int Current = 1;
    String currentName;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case tv_look_around://先逛逛
//
//                break;
            case R.id.tv_prevent://预防
                setSelectStatus(1);

                break;
            case R.id.tv_diagnosis_undetermined://未确诊
                setSelectStatus(2);

                break;
            case R.id.tv_newly_diagnosed://新确诊
                setSelectStatus(3);

                break;
            case R.id.tv_in_treatment://治疗中
                setSelectStatus(4);

                break;
            case R.id.tv_empty_window://康复空窗期
                setSelectStatus(5);

                break;
            case R.id.tv_recrudescence://复发治疗期
                setSelectStatus(6);

                break;
            case R.id.btn_next:
                createUserParamsEntity.setCurrent(Current + "");
                startActivityForResult(new Intent(this, SelectRelationshipActivity.class).putExtra("createUserParamsEntity", createUserParamsEntity).putExtra("CurrentName", currentName), 5);
                break;

        }
    }

    private void setSelectStatus(int witch) {
        clearSelectStatus();

        switch (witch) {
            case 1:
                tv_prevent.setSelected(true);
                currentName = "预防";
                break;
            case 2:
                tv_diagnosis_undetermined.setSelected(true);
                currentName = "未确诊";
                break;
            case 3:
                tv_newly_diagnosed.setSelected(true);
                currentName = "新确诊";
                break;
            case 4:
                tv_in_treatment.setSelected(true);
                currentName = "治疗中";
                break;
            case 5:
                tv_empty_window.setSelected(true);
                currentName = "康复空窗期";
                break;
            case 6:
                tv_recrudescence.setSelected(true);
                currentName = "复发治疗期";
                break;
        }
        Current = witch;
    }

    private void clearSelectStatus() {
        tv_prevent.setSelected(false);
        tv_diagnosis_undetermined.setSelected(false);
        tv_newly_diagnosed.setSelected(false);
        tv_in_treatment.setSelected(false);
        tv_empty_window.setSelected(false);
        tv_recrudescence.setSelected(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 5 && resultCode == RESULT_OK) {
            finish();
        }
    }
}

package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.utils.Const;

/**
 * Created by Administrator on 2017/2/9.
 *
 * 常规指标
 *
 * @author wwei
 */
public class RecordRuleQuotaActivity extends BaseActivity implements View.OnClickListener{


    private View v1;
    private View v2;
    private View v3;
    private View v4;
    private View v5;
    private View v6;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;

    private boolean isChanged = false;
    private boolean Result_Flag = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent();
        setContentView(R.layout.activity_record_rule_quota);
        initStatusBar();
        Result_Flag = getIntent().getBooleanExtra(Const.RECORD_REQUEST_FLAG,false);
        initView();
        initData();
    }

    private void initView(){
        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);
        v4 = findViewById(R.id.v4);
        v5 = findViewById(R.id.v5);
        v6 = findViewById(R.id.v6);
        v1.setOnClickListener(this);
        v2.setOnClickListener(this);
        v3.setOnClickListener(this);
        v4.setOnClickListener(this);
        v5.setOnClickListener(this);
        v6.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        tv1 = (TextView)findViewById(R.id.tv_time_1);
        tv2 = (TextView)findViewById(R.id.tv_time_2);
        tv3 = (TextView)findViewById(R.id.tv_time_3);
        tv4 = (TextView)findViewById(R.id.tv_time_4);
        tv5 = (TextView)findViewById(R.id.tv_time_5);
        tv6 = (TextView)findViewById(R.id.tv_time_6);
    }

    private void initData(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.v1:
                toRecord(Const.RECORD_TYPE_1);
                break;
            case R.id.v2:
                toRecord(Const.RECORD_TYPE_2);
                break;
            case R.id.v3:
                toRecord(Const.RECORD_TYPE_3);
                break;
            case R.id.v4:
                toRecord(Const.RECORD_TYPE_4);
                break;
            case R.id.v5:
                toRecord(Const.RECORD_TYPE_5);
                break;
            case R.id.v6:
                toRecord(Const.RECORD_TYPE_6);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public void toRecord(int type){
        startActivityForResult(new Intent(this, MedicalRecordDetailListActivity.class)
                .putExtra(Const.RECORD_CLASSIFY_TYPE, type)
                .putExtra(Const.RECORD_REQUEST_FLAG, true), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case Const.REQUEST_CODE_RECORD_ACTIVITY:
                boolean change = data.getBooleanExtra("isChanged",false);
                if (change){
                    isChanged = true;
                }
                break;
        }
    }

    @Override
    public void finish() {
        if (Result_Flag&&isChanged){
            setResult(Const.REQUEST_CODE_RECORD_ACTIVITY,getIntent()
                    .putExtra("isChanged",true));
        }
        super.finish();
    }


}

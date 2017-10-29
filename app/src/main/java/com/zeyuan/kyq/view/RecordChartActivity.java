package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.CancerSizeItemEntity;
import com.zeyuan.kyq.Entity.RecordClassifyHistoryEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.RecordChartAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ConstUtils;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 *
 * 记录图表页面
 *
 * @author wwei
 */
public class RecordChartActivity extends BaseActivity implements HttpResponseInterface{

    private int type;//1、肿瘤大小；2、肿瘤指标

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_chart);
        initType();
        initView();
        initData();
    }

    private void initType(){
        try {
            type = getIntent().getIntExtra(Const.RECORD_CHART_TYPE,Const.RECORD_TYPE_14);
            TextView title = (TextView)findViewById(R.id.title);
            title.setText(type==Const.RECORD_TYPE_14?getString(R.string.text_chart_cancer_change)
                    :getString(R.string.text_chart_quota_change));
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"initType");
        }
    }

    private ListView lv;
    private View v_no_data;
    private RecordChartAdapter adapter;
    private void initView(){
        try {
            v_no_data = findViewById(R.id.v_no_data);
            lv = (ListView)findViewById(R.id.lv);
            adapter = new RecordChartAdapter(this,new ArrayList<List<CancerSizeItemEntity>>(),type);
            lv.setAdapter(adapter);
            findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            findViewById(R.id.v_to_record).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toRecord();
                }
            });
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"initView");
        }
    }

    private void initData(){
        Factory.postPhp(this, Const.PGetCancerInfoForApp);
    }

    private void setView(List<CancerSizeItemEntity> data){
        try {
            if (data==null||data.size()==0){
                lv.setVisibility(View.GONE);
                v_no_data.setVisibility(View.VISIBLE);
            }else {
                v_no_data.setVisibility(View.GONE);
                lv.setVisibility(View.VISIBLE);
                adapter.update(ConstUtils.getListForChartList(data));
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"setView");
        }
    }

    private void toRecord(){
        if (type == Const.RECORD_TYPE_14){
            startActivityForResult(new Intent(this, RecordActivity.class)
                    .putExtra(Const.RECORD_CLASSIFY_TYPE, type)
                    .putExtra(Const.RECORD_REQUEST_FLAG, true)
                    .putExtra(Const.RECORD_CLASSIFY_CANCER_SIZE, (Serializable) bean.getTumourInfo()), 100);
        } else if (type == Const.RECORD_TYPE_15){
            int QUOTA_STATUS = bean.getMarkType().getStatus();
            Intent intent = new Intent(this,RecordActivity.class)
                    .putExtra(Const.RECORD_CLASSIFY_TYPE,type)
                    .putExtra(Const.RECORD_REQUEST_FLAG, true)
                    .putExtra(Const.RECORD_CLASSIFY_CANCER_SIZE,(Serializable) bean.getCancerMark())
                    .putExtra(Const.RECORD_CLASSIFY_QUOTA_STATUS,QUOTA_STATUS);
            if (QUOTA_STATUS==3){
                ArrayList<String> check = bean.getMarkType().getTypeList();
                intent.putExtra(Const.RECORD_CLASSIFY_QUOTA_CHECKED,check);
            }
            startActivityForResult(intent,100);
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String,String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        if (tag == Const.PGetCancerInfoForApp){
            map.put("ViewType","1");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    private RecordClassifyHistoryEntity bean;
    @Override
    public void toActivity(Object response, int flag) {

        if (flag == Const.PGetCancerInfoForApp){
            bean = (RecordClassifyHistoryEntity)response;
            if (Const.RESULT.equals(bean.getiResult())){
                setView(type==Const.RECORD_TYPE_15?bean.getCancerMark():bean.getTumourInfo());
            }else {
                showToast("数据获取错误");
            }
        }

    }

    @Override
    public void showLoading(int flag) {
        findViewById(R.id.pd).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading(int flag) {
        findViewById(R.id.pd).setVisibility(View.GONE);
    }

    @Override
    public void showError(int flag) {
        findViewById(R.id.pd).setVisibility(View.GONE);
        showToast("网络请求失败");
    }

    private boolean isChanged = false;
    @Override
    public void finish() {
        if (isChanged||adapter.isChanged()){
            setResult(Const.RESULT_CODE_RECORD_CHART_TO_MEDICAL,getIntent()
                    .putExtra(Const.RESULT_FLAG_FOR_DATA_CHANGED,true));
        }
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case Const.REQUEST_CODE_RECORD_ACTIVITY:
                boolean change = data.getBooleanExtra("isChanged",false);
                if (change){
                    isChanged = true;
                    initData();
                }
                break;
        }
    }
}

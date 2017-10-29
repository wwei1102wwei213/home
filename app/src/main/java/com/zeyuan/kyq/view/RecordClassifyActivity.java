package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.zeyuan.kyq.Entity.CancerSizeItemEntity;
import com.zeyuan.kyq.Entity.GeneAndTranslateEntity;
import com.zeyuan.kyq.Entity.RecordClassifyHistoryEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.RecordClassifyStaticAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.UserinfoData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/13.
 *
 * 记录分类页面
 *
 * @author wwei
 */
public class RecordClassifyActivity extends BaseActivity implements AdapterView.OnItemClickListener,HttpResponseInterface{


    private boolean isChanged = false;
    private boolean Result_Flag = false;
    private boolean clickAble = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_classify);
        Result_Flag = getIntent().getBooleanExtra(Const.RECORD_REQUEST_FLAG,false);
        initView();
        initData();
    }

    private void initView(){
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        GridView gv = (GridView)findViewById(R.id.gv);
        RecordClassifyStaticAdapter adapter = new RecordClassifyStaticAdapter(this);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(this);
    }

    private void initData(){
        Factory.postPhp(this,Const.PGetCancerInfoForApp);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                if (clickAble)
                toRecord(Const.RECORD_TYPE_12);
                break;
            case 1:
                toRecord(Const.RECORD_TYPE_13);
                break;
            case 2:
                if (clickAble)
                toRecord(Const.RECORD_TYPE_11);
                break;
            case 3:
                startActivityForResult(new Intent(this, RecordRuleQuotaActivity.class), 100);
                break;
            case 4:
                if (clickAble)
                toRecord(Const.RECORD_TYPE_14);
                break;
            case 5:
                if (clickAble)
                toRecord(Const.RECORD_TYPE_15);
                break;
            case 6:
                toRecord(Const.RECORD_TYPE_7);
                break;
            case 7:
                toRecord(Const.RECORD_TYPE_8);
                break;
            case 8:
                toRecord(Const.RECORD_TYPE_9);
                break;
            case 9:
                startActivity(new Intent(this,ReleaseForumActivity.class));
                break;
        }
    }

    private ArrayList<String> check= null;
    public void toRecord(int type){
        if (type == Const.RECORD_TYPE_14){
            startActivityForResult(new Intent(this, MedicalRecordDetailListActivity.class)
                    .putExtra(Const.RECORD_CLASSIFY_TYPE, type)
                    .putExtra(Const.RECORD_REQUEST_FLAG, true)
                    .putExtra(Const.RECORD_CLASSIFY_CANCER_SIZE, (Serializable) cancerList), 100);
        } else if (type == Const.RECORD_TYPE_15){
            Intent intent = new Intent(this,MedicalRecordDetailListActivity.class)
                    .putExtra(Const.RECORD_CLASSIFY_TYPE,type)
                    .putExtra(Const.RECORD_REQUEST_FLAG, true)
                    .putExtra(Const.RECORD_CLASSIFY_CANCER_SIZE,(Serializable) quotaList)
                    .putExtra(Const.RECORD_CLASSIFY_QUOTA_STATUS,QUOTA_STATUS);
            if (QUOTA_STATUS==3){
                intent.putExtra(Const.RECORD_CLASSIFY_QUOTA_CHECKED,check);
            }
            startActivityForResult(intent,100);
        } else if (type == Const.RECORD_TYPE_11){
            startActivityForResult(new Intent(this, MedicalRecordDetailListActivity.class)
                    .putExtra(Const.RECORD_CLASSIFY_TYPE, type)
                    .putExtra(Const.RECORD_REQUEST_FLAG, true)
                    .putExtra(Const.RECORD_CLASSIFY_GENE_TRANSLATE_CHECKED, getGeneOrTranslateList(type)), 100);
        } else if (type == Const.RECORD_TYPE_12){
            startActivityForResult(new Intent(this, MedicalRecordDetailListActivity.class)
                    .putExtra(Const.RECORD_CLASSIFY_TYPE, type)
                    .putExtra(Const.RECORD_REQUEST_FLAG, true)
                    .putExtra(Const.RECORD_CLASSIFY_GENE_TRANSLATE_CHECKED, getGeneOrTranslateList(type)), 100);
        } else {
            startActivityForResult(new Intent(this, MedicalRecordDetailListActivity.class)
                    .putExtra(Const.RECORD_CLASSIFY_TYPE, type)
                    .putExtra(Const.RECORD_REQUEST_FLAG, true), 100);
        }
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

    @Override
    public Map getParamInfo(int tag) {
        Map<String,String> map = new HashMap<>();
        if (tag == Const.PGetCancerInfoForApp){
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
            map.put("ViewType","3");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    private List<CancerSizeItemEntity> cancerList = null;
    private List<CancerSizeItemEntity> quotaList = null;
    private int QUOTA_STATUS = 1;
    private List<GeneAndTranslateEntity> geneList = null;
    private List<GeneAndTranslateEntity> translateList = null;
    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PGetCancerInfoForApp){
            RecordClassifyHistoryEntity entity = (RecordClassifyHistoryEntity)response;
            if (Const.RESULT.equals(entity.getiResult())){
                cancerList = entity.getTumourInfo();
                quotaList = entity.getCancerMark();
                QUOTA_STATUS = entity.getMarkType().getStatus();
                check = entity.getMarkType().getTypeList();
                LogCustom.i("ZYS",entity.toString());
                geneList = entity.getTransferGene();
                translateList = entity.getTransferRecord();
            }else {
                showToast("未找到历史记录");
            }
            clickAble = true;
        }
    }

    @Override
    public void showLoading(int flag) {
        clickAble = false;
    }

    @Override
    public void hideLoading(int flag) {

    }

    @Override
    public void showError(int flag) {
        clickAble = true;
        showToast("网络请求失败");
    }

    @Override
    public void finish() {
        if (Result_Flag&&isChanged){
            setResult(Const.RESULT_CODE_RERORD_CLASSIFY_TO_MEDICAL,getIntent()
                    .putExtra(Const.RESULT_FLAG_FOR_DATA_CHANGED,true));
        }
        super.finish();
    }

    private ArrayList<String> getGeneOrTranslateList(int type){
        ArrayList<String> temp = new ArrayList<>();
        if (type == Const.RECORD_TYPE_11){
            if (geneList!=null&&geneList.size()>0){
                temp = getLastRecord(geneList,type);
            }
        }else {
            if (translateList!=null&&translateList.size()>0){
                temp = getLastRecord(translateList,type);
            }
        }
        return temp;
    }

    private ArrayList<String> getLastRecord(List<GeneAndTranslateEntity> data,int type){
        String str = "";
        int time = 0;
        int date = 0;
        for (GeneAndTranslateEntity entity:data){
            if (entity.getRecordTime()>time){
                str = type==Const.RECORD_TYPE_11?entity.getGeneID():entity.getTransferBody();
                time = entity.getRecordTime();
                date = entity.getDateline();
            }else if (entity.getRecordTime()==time){
                if (entity.getDateline()>date){
                    str = type==Const.RECORD_TYPE_11?entity.getGeneID():entity.getTransferBody();
                    date = entity.getDateline();
                }
            }
        }
        ArrayList<String> temp = new ArrayList<>();
        if (!TextUtils.isEmpty(str)){
            String[] args = str.split(",");
            for (String s:args){
                temp.add(s);
            }
        }
        return temp;
    }

}

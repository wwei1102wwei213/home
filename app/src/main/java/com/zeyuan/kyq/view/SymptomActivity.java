package com.zeyuan.kyq.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.zeyuan.kyq.Entity.SameEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.SymptomCardAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.FindSymtomBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.TouchFlyCard.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/5.
 *
 *
 *
 * @author wwei
 */
public class SymptomActivity extends BaseActivity implements SymptomCardAdapter.SymptomCareCallBack,HttpResponseInterface{

    private Map<String,String> map = null;
    private List<SameEntity> data;
    private ArrayList<List<SameEntity>> list;
    private SwipeFlingAdapterView sfav;
    private SymptomCardAdapter adapter;
    private String PerformID;
    private String[] shareParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent();
        setContentView(R.layout.activity_symptom);
        try {
            initStatusBar();
            initView();
            initData();
            initListener();
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"onCreate");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView(){
        sfav = (SwipeFlingAdapterView)findViewById(R.id.sfav);
    }

    private void initData(){
        try {
            map = (Map<String,String>)Factory.getData(Const.N_DataPerformValues);
            data = new ArrayList<>();
            list = new ArrayList<>();
            SameEntity entity;
            for (String key:map.keySet()){
                if (!TextUtils.isEmpty(key)){
                    entity = new SameEntity();
                    entity.setId(key);
                    entity.setName(map.get(key));
                    if (data.size()<100){
                        data.add(entity);
                    }else {
                        break;
                    }
                }
            }
            List<SameEntity> temp = new ArrayList<>();
            try {
                while (data.size()!=0){
                    int n = (int)Math.random()*data.size();
                    entity = data.get(n);
                    temp.add(entity);
                    if (temp.size()==8){
                        list.add(temp);
                        temp = new ArrayList<>();
                    }
                    data.remove(entity);
                }
            }catch (Exception e){
                ExceptionUtils.ExceptionSend(e,"while");
            }
            LogCustom.i("ZYS", "list:" + list.size());
            adapter = new SymptomCardAdapter(this,list,this);
            sfav.setAdapter(adapter);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"initData");
        }
    }

    private void initListener(){

        try {
            findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            findViewById(R.id.v_look_more).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SymptomActivity.this,HomeSymptomActivity.class));
                }
            });

            sfav.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
                @Override
                public void removeFirstObjectInAdapter() {
                    adapter.update();
                }

                @Override
                public void onLeftCardExit(Object dataObject) {
                }

                @Override
                public void onRightCardExit(Object dataObject) {
                }

                @Override
                public void onAdapterAboutToEmpty(int itemsInAdapter) {
                    if (itemsInAdapter == 1) {
                        LogCustom.i("ZYS", "总数据源长度：" + list.size());
                        adapter.update(list);
                    }
                }

                @Override
                public void onScroll(float scrollProgressPercent) {
                }
            });

        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"initListener");
        }
    }

    public void right() {
        sfav.getTopCardListener().selectRight();
    }

    public void left() {
        sfav.getTopCardListener().selectLeft();
    }

    @Override
    public void OnItemClickCall(SameEntity entity) {
        if (entity!=null&&!TextUtils.isEmpty(entity.getId())){
            PerformID = entity.getId();
            Factory.post(this, Const.EGetCancerProcess);
        }
    }

    @Override
    public void OnRefreshClickCall() {
        left();
    }

    @Override
    public Map getParamInfo(int tag) {
        return null;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        switch (flag){
            case Const.EGetCancerProcess:
                String cureConfID = MapDataUtils.getAllCureconfID(UserinfoData.getStepID(this));
                args = new String[]{
                        Contants.InfoID,UserinfoData.getInfoID(this),
                        Contants.StepID,UserinfoData.getStepID(this),
                        Contants.CancerID,UserinfoData.getCancerID(this),
                        Contants.PerformID, PerformID,
                        Contants.CureConfID,cureConfID,
                        Const.ISNEWVERSION,Const.ISNEWVERSION_FINAL
                };
                shareParams = args;
                break;
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.EGetCancerProcess) {//查症状
            FindSymtomBean bean = (FindSymtomBean) response;
            if (Contants.RESULT.equals(bean.getIResult())) {
                toResult(bean);
            }else {
                showToast("网络请求错误，错误码"+bean.getIResult());
            }
        }
    }

    private ProgressDialog mProgressDialog;

    @Override
    public void showLoading(int tag) {
        try {
            if(tag == Const.EGetCancerProcess ){
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("正在查询该症状");
                mProgressDialog.show();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "FindSymtomActivity");
        }
    }

    @Override
    public void hideLoading(int tag) {
        if(tag == Const.EGetCancerProcess&&mProgressDialog!=null){
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showError(int tag) {
        if(tag == Const.EGetCancerProcess&&mProgressDialog!=null){
            mProgressDialog.dismiss();
        }
        showToast("网络请求失败");
    }

    private void toResult(FindSymtomBean bean) {
        try {
            Intent intent = new Intent(this, DiagnosisActivity.class);
            intent.putExtra(Contants.FindSymtomBean, bean);
            intent.putExtra(Contants.PerformID, PerformID);
            intent.putExtra(Const.RESULT_PARAMS_FOR_SHARE,shareParams);
            startActivity(intent);//跳转到诊断结果
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "FindSymptomActivity");
        }
    }
}

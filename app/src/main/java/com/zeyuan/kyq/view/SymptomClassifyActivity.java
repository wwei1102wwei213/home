package com.zeyuan.kyq.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zeyuan.kyq.Entity.HomeSymptomEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.SymptomAdapter;
import com.zeyuan.kyq.adapter.SymptomBodyRecyclerAdapter;
import com.zeyuan.kyq.adapter.SymptomClassifyRecyclerAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.FindSymtomBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/10.
 *
 * 症状查询页面
 *
 * @author wwei
 */
public class SymptomClassifyActivity extends BaseActivity implements SymptomClassifyRecyclerAdapter.onTabClickListener
                ,SymptomBodyRecyclerAdapter.onTabClickListener,HttpResponseInterface{

    //选中下标
    private int index = 0;
    //顶部滑动控件
    private List<HomeSymptomEntity> list;
    private RecyclerView rv;
    private SymptomClassifyRecyclerAdapter adapter;
    private int mWidth = 0;
    private LinearLayoutManager manager;
    //左边滑动控件
    private RecyclerView rv2;
    private LinearLayoutManager manager2;
    private Map<String,List<String>> map;
    private Map<String,String> performData;
    private SymptomBodyRecyclerAdapter adapter_left;
    private SymptomAdapter adapter_right;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_classify);
        initStatusBar();
        index = getIntent().getIntExtra(Contants.index,0);
        list = (List<HomeSymptomEntity>)getIntent().getSerializableExtra(Contants.List);
        performData = (Map<String,String>)Factory.getData(Const.N_DataPerformValues);
        initView();
    }


    private void initView(){

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.iv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SymptomClassifyActivity.this,SearchSymtomActivity.class));
            }
        });

        rv = (RecyclerView)findViewById(R.id.rv);
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(manager);
        adapter = new SymptomClassifyRecyclerAdapter(this,list,index,this);
        rv.setAdapter(adapter);
        try {
            manager.scrollToPosition(index);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"onTabClick");
        }

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mWidth = metric.widthPixels; // 屏幕宽度（像素）

        map = (Map<String,List<String>>) Factory.getData(Const.N_DataBodyPos);
        rv2 = (RecyclerView)findViewById(R.id.rv_left);
        manager2 = new LinearLayoutManager(this);
        manager2.setOrientation(LinearLayoutManager.VERTICAL);
        rv2.setLayoutManager(manager2);

        List<String> temp = UiUtils.getSymptomClassifyData(list.get(index).getId());
        adapter_left = new SymptomBodyRecyclerAdapter(this, temp ,0,this);
        rv2.setAdapter(adapter_left);

        lv = (ListView)findViewById(R.id.lv);
        List<String> temp2;
        if (temp.size()>1){
            temp2 = new ArrayList<>();
            for (int i=1;i<temp.size();i++){
                List<String> temp3 = map.get(temp.get(i));
                if (temp3!=null&&temp3.size()>0){
                    temp2.addAll(temp3);
                }
            }
        }else {
            temp2 = map.get(list.get(index).getId());
        }

        adapter_right = new SymptomAdapter(this,temp2);
        lv.setAdapter(adapter_right);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String perform = adapter_right.getItem(position);
                if (!TextUtils.isEmpty(perform)){
                    PerformID = perform;
                    Factory.post(SymptomClassifyActivity.this, Const.EGetCancerProcess);
                }
            }
        });
    }

    @Override
    public void onTabClick(HomeSymptomEntity entity, int pos) {
        try {
            manager.scrollToPositionWithOffset(pos, getOffset(pos));
            //刷新下面的2个列表
            index = pos;
            List<String> temp = UiUtils.getSymptomClassifyData(entity.getId());
            adapter_left.update(temp,0);
            List<String> temp2;
            if (temp.size()>1){
                temp2 = new ArrayList<>();
                for (int i=1;i<temp.size();i++){
                    List<String> temp3 = map.get(temp.get(i));
                    if (temp3!=null&&temp3.size()>0){
                        temp2.addAll(temp3);
                    }
                }
            }else {
                temp2 = map.get(list.get(index).getId());
            }

            adapter_right.update(temp2);

        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"onTabClick");
        }
    }

    @Override
    public void onRecyclerLeftClick(String id, int pos) {
        if (pos==0){
            List<String> temp = UiUtils.getSymptomClassifyData(list.get(index).getId());
            List<String> temp2;
            if (temp.size()>1){
                temp2 = new ArrayList<>();
                for (int i=1;i<temp.size();i++){
                    List<String> temp3 = map.get(temp.get(i));
                    if (temp3!=null&&temp3.size()>0){
                        temp2.addAll(temp3);
                    }
                }
            }else {
                temp2 = map.get(list.get(index).getId());
            }
            adapter_right.update(temp2);
        }else {
            List<String> temp = null;
            if (!TextUtils.isEmpty(id)){
                 temp = map.get(id);
            }
            if (temp==null||temp.size()==0) {
                temp = new ArrayList<>();
            }
            adapter_right.update(temp);
        }
    }

    private int getOffset(int position){
        int offset = 0;
        try {
            int width = manager.findViewByPosition(position).getMeasuredWidth();
            offset = (mWidth-width)/2;
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "getOffset");
        }
        if (offset>0) return offset;
        return 0;
    }

    @Override
    public Map getParamInfo(int tag) {
        return null;
    }

    private String PerformID = "";
    private String[] shareParams;
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
        try {
            if (flag == Const.EGetCancerProcess) {//查症状
                FindSymtomBean bean = (FindSymtomBean) response;
                if (Contants.RESULT.equals(bean.getIResult())) {
                    try {
                        Intent intent = new Intent(this, DiagnosisActivity.class);
                        intent.putExtra(Contants.FindSymtomBean, bean);
                        intent.putExtra(Contants.PerformID, PerformID);
                        intent.putExtra(Const.RESULT_PARAMS_FOR_SHARE,shareParams);
                        startActivity(intent);//跳转到诊断结果
                    }catch (Exception e){
                        ExceptionUtils.ExceptionToUM(e, this, "SymptomClassifyActivity toActivity Const.EGetCancerProcess");
                    }
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "SymptomClassifyActivity toActivity");
        }

    }

    private ProgressDialog mProgressDialog;
    @Override
    public void showLoading(int flag) {
        try {
            if(flag == Const.EGetCancerProcess ){
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("正在查询该症状");
                mProgressDialog.show();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "SymptomClassifyActivity");
        }
    }

    @Override
    public void hideLoading(int flag) {
        if(flag == Const.EGetCancerProcess&&mProgressDialog!=null){
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showError(int flag) {
        if(flag == Const.EGetCancerProcess&&mProgressDialog!=null){
            mProgressDialog.dismiss();
        }
    }
}

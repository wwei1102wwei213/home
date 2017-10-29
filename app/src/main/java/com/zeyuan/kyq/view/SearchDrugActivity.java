package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.zeyuan.kyq.Entity.ConfStepEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.SearchDrugLeftAdapter;
import com.zeyuan.kyq.adapter.SearchDrugRightAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.CaseDetailBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CustomView.BounceScrollView;
import com.zeyuan.kyq.widget.MyListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/27.
 *
 * 药物搜索页面
 *
 * @author wwei
 */
public class SearchDrugActivity extends BaseActivity implements HttpResponseInterface,View.OnClickListener{

    private RadioGroup rg;

    private SearchDrugLeftAdapter leftAdapter;
    private SearchDrugRightAdapter rightAdapter;
    private SparseArray<List<ConfStepEntity>> cureData;
    private int initCureConfID;
    private String StepID;
    private BounceScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_drug);
        //设置标题并显示搜索图标
        initWhiteTitle("查药",0);
        //设置视图控件
        initView();
        //设置两边的数据
        initLeftData();
        initRightData();
    }

    private void initView(){
        sv = (BounceScrollView)findViewById(R.id.rsv);
        //搜索转跳
        ImageView share = (ImageView)findViewById(R.id.iv_white_title_share);
        share.setOnClickListener(this);
    }

    /***
     * 设置左边阶段类型数据
     *
     */
    private void initLeftData(){

        int[] checkRes = new int[]{
                R.mipmap.drug_baxiang,R.mipmap.drug_hualiao,R.mipmap.drug_fangliao,R.mipmap.drug_shoushu,
                R.mipmap.drug_putong,R.mipmap.drug_yingyang,R.mipmap.drug_que_zhen,R.mipmap.drug_other,
                R.mipmap.drug_mianyi,R.mipmap.drug_fu_fa,R.mipmap.drug_kong_chuang,R.mipmap.drug_zhong_yi
                ,R.mipmap.drug_other
        };
        int[] unCheckRes = new int[]{
                R.mipmap.drug_baxiang_no,R.mipmap.drug_hualiao_no,R.mipmap.drug_fangliao_no,R.mipmap.drug_shoushu_no,
                R.mipmap.drug_putong_no,R.mipmap.drug_yingyang_no,R.mipmap.drug_que_zhen_no,R.mipmap.drug_other_no,
                R.mipmap.drug_mianyi_no,R.mipmap.drug_fu_fa_no,R.mipmap.drug_kong_chuang_no
                ,R.mipmap.drug_zhong_yi_no,R.mipmap.drug_other_no
        };

        List<String> cureConfIds = new ArrayList<>();
        List<String> cureConfNames = new ArrayList<>();

        LinkedHashMap<String,String> map = (LinkedHashMap<String,String>) Factory.getData(Const.N_DataCureConf);
        if(map==null||map.size()==0) return;
        List<String[]> tempList = new ArrayList<>();
        for(String key:map.keySet()){
            String[] args = new String[]{key,map.get(key)};
            tempList.add(args);
        }
        Collections.sort(tempList, new Comparator<String[]>() {
            @Override
            public int compare(String[] fc, String[] fd) {
                return Integer.valueOf(fc[0])-Integer.valueOf(fd[0]);
            }
        });
        for(String[] args:tempList){
            cureConfIds.add(args[0]);
            cureConfNames.add(args[1]);
        }
        String temp = cureConfIds.get(0);
        if(!TextUtils.isEmpty(temp)){
            try {
                initCureConfID = Integer.valueOf(temp);
            }catch (Exception e){
                initCureConfID = 0;
            }
        }
        MyListView leftLv = (MyListView)findViewById(R.id.lv_search_drug_left);
        leftAdapter = new SearchDrugLeftAdapter(this,cureConfIds,cureConfNames,checkRes,unCheckRes,0);
        leftLv.setAdapter(leftAdapter);
        leftLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position!=leftAdapter.getIndex()) {
                    leftAdapter.updata(position);
                    int cureConfId = leftAdapter.getCureConfId(position);
                    List<ConfStepEntity> list = cureData.get(cureConfId);
                    if(list==null){
                        list = new ArrayList<>();
                    }
                    rightAdapter.updata(list);
                    sv.scrollTo(0,0);
                }
            }
        });
    }

    /***
     * 设置右边的阶段药物数据
     *
     */
    private void initRightData(){
        cureData = (SparseArray<List<ConfStepEntity>>)Factory.getData(Const.N_DataSearchDrug);
        List<ConfStepEntity> list;
        if (initCureConfID!=0){
            list = cureData.get(initCureConfID);
        }else{
            list = new ArrayList<>();
        }
        if(list==null){
            list = new ArrayList<>();
        }
        MyListView rightLv = (MyListView)findViewById(R.id.lv_search_drug_right);
        rightAdapter = new SearchDrugRightAdapter(this,list);
        rightLv.setAdapter(rightAdapter);
        rightLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ConfStepEntity entity = rightAdapter.getItem(position);
                    StepID = entity.getStepID();
                    if(!TextUtils.isEmpty(StepID))
                        Factory.post(SearchDrugActivity.this,
                            Const.EGetSolutionDetail);
                }catch (Exception e){

                }
            }
        });
    }



    @Override
    public Map getParamInfo(int tag) {
        return null;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if(flag == Const.EGetSolutionDetail){
            args = new String[]{
                    Contants.InfoID, UserinfoData.getInfoID(this),
                    Contants.StepID,StepID,
                    Contants.PolicyType,"2",
                    Const.ISNEWVERSION,Const.ISNEWVERSION_FINAL
            };
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object response, int flag) {
        if(flag == Const.EGetSolutionDetail){
            CaseDetailBean bean = (CaseDetailBean) response;
            if (bean!=null&&Contants.RESULT.equals(bean.getIResult())) {
                bean.setStepId(StepID);
                startActivity(new Intent(this, CaseDetailActivity.class)
                        .putExtra(Contants.CaseDetailBean, bean).putExtra(Contants.ISCANCER,false));
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_white_title_share:
                startActivity(new Intent(this,SearchFunctionActivity.class)
                        .putExtra(Const.SEARCH_TYPE,Const.SEARCH_DRUG));
                break;
        }
    }
}


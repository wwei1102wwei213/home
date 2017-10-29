package com.zeyuan.kyq.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.Entity.ConfStepEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.SearchDrugAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.CaseDetailBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.SearchStringBiz;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.SearchUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/23.
 *
 * 搜索页面
 *
 * @author wwei
 */
public class SearchTypeActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener,TextWatcher
        ,View.OnKeyListener,SearchStringBiz.SearchStringInterface,HttpResponseInterface{


    private List<ConfStepEntity> list;
    private List<String> pyList;
    private List<String> fristList;
    private boolean searchflag = false;
    private List<ConfStepEntity> searchlist;
    //搜索类型
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_symtom);
        //初始化数据
        initdata();
        //初始化搜素记录
        initrecordview();
        //初始化视图控件
        initview();

    }

    private void initdata(){
        //设置搜索类型
        type = Const.RECORD_DRUG;
        //搜索数据源
        list = (List<ConfStepEntity>) Factory.getData(Const.N_DataSearchDrugList);
        //获取索引串
        new SearchStringBiz(SearchStringBiz.DRUG,this,list).execute();
    }

    private RelativeLayout rl_record;
    private SearchDrugAdapter recordAdapter;
    private Object recordObject;
    private void initrecordview(){
        try {
            String record = UserinfoData.getSearchRecord(this, type);
            if(!TextUtils.isEmpty(record)){
                recordObject = SearchUtils.getSearchRecord(record,type);
                if(recordObject!=null){
                    rl_record = (RelativeLayout)findViewById(R.id.ll_record_search_symptom);
                    rl_record.setVisibility(View.VISIBLE);
                    Button btn = (Button)findViewById(R.id.btn_clear_search_symptom);
                    btn.setOnClickListener(this);
                    ListView listview = (ListView)findViewById(R.id.lv_record_search_symptom);
                    recordAdapter = new SearchDrugAdapter(this,(List<ConfStepEntity>)recordObject);
                    listview.setAdapter(recordAdapter);
                    listview.setOnItemClickListener(this);
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"SearchSymptomActivity");
        }
    }

    private ImageView imgclose;
    private EditText et;
    private TextView tv;
    private SearchDrugAdapter adapter;

    private void initview(){

        ImageView img_back = (ImageView)findViewById(R.id.iv_back_search);
        et = (EditText)findViewById(R.id.et_search);
        tv = (TextView)findViewById(R.id.tv_dismiss_input);
        imgclose = (ImageView)findViewById(R.id.back_search_text);
        imgclose.setVisibility(View.GONE);
        searchlist = new ArrayList<>();

        ListView listview = (ListView)findViewById(R.id.lv_search_symtom);
        adapter = new SearchDrugAdapter(this,searchlist);
        listview.setAdapter(adapter);

        img_back.setOnClickListener(this);
        listview.setOnItemClickListener(this);
        et.addTextChangedListener(this);//搜索框内容改变监听
        et.setOnKeyListener(this);
        imgclose.setOnClickListener(this);
        tv.setOnClickListener(this);
    }

    //获取搜索结果
    private List<ConfStepEntity> getSearchList(String str,List<ConfStepEntity> data){
        List<ConfStepEntity> temp = new ArrayList<>();
        try {
            String name;
            String pyname;
            String pyfrist;
            str = str.replace(" ","");
            for(int i=0;i<data.size();i++){
                name = data.get(i).getStepName().toLowerCase();

                if(name.startsWith(str.toLowerCase())||name.contains(str.toLowerCase())){
                    temp.add(data.get(i));
                }else{
                    if(searchflag){
                        pyfrist = fristList.get(i);
                        if(pyfrist.startsWith(str.toLowerCase())||pyfrist.contains(str.toLowerCase())){
                            temp.add(data.get(i));
                        }else{
                            pyname = pyList.get(i);
                            if(pyname.contains(str.toLowerCase())){
                                temp.add(data.get(i));
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"SearchSymptomActivity");
        }
        return  temp;
    }

    @Override
    public void finish() {
        InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm!=null&&imm.isActive(et)){
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
        super.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_search:
                finish();
                break;
            case R.id.back_search_text:
                et.setText("");
                imgclose.setVisibility(View.GONE);
                break;
            case R.id.tv_dismiss_input:

                finish();
                break;
            case R.id.btn_clear_search_symptom:
                UserinfoData.saveSearchRecord(this, Const.RECORD_DRUG, "");
                rl_record.setVisibility(View.GONE);
                Toast.makeText(this, "记录已清空", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private String StepID;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()){
            case R.id.lv_record_search_symptom:
                ConfStepEntity recordEntity = recordAdapter.getItem(position);
                StepID = recordEntity.getStepID();
                if(!TextUtils.isEmpty(StepID)) Factory.post(SearchTypeActivity.this,Const.EGetSolutionDetail);
                break;
            case R.id.lv_search_symtom:
                ConfStepEntity entity = adapter.getItem(position);
                StepID = entity.getStepID();
                if(!TextUtils.isEmpty(StepID)) {
                    String recordStr = StepID + Const.BREAK_SMALL + entity.getStepName();
                    Log.i("ZYS","recordStr:"+recordStr);
                    String record = UserinfoData.getSearchRecord(this,Const.RECORD_DRUG);
                    Log.i("ZYS","rec:"+record);
                    String save = SearchUtils.getSaveString(record,recordStr);
                    Log.i("ZYS","save:"+save);
                    UserinfoData.saveSearchRecord(this,Const.RECORD_DRUG,save);
                    Factory.post(SearchTypeActivity.this,Const.EGetSolutionDetail);
                }
                break;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (rl_record != null && rl_record.getVisibility() != View.GONE) {
            rl_record.setVisibility(View.GONE);
        }
        String text = et.getText() + "";
        if ("".equals(text)) {
            if (imgclose.getVisibility() != View.GONE) {
                imgclose.setVisibility(View.GONE);
            }
            return;
        }
        int textlength = et.getText().length();

        if (textlength > 0) {
            imgclose.setVisibility(View.VISIBLE);
        }
        searchlist = getSearchList(text, list);
        adapter.updata(searchlist);
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void setData(List<List<String>> o) {
        if(o!=null){
            pyList = o.get(0);
            fristList = o.get(1);
            searchflag = true;
        }
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
                    Contants.PolicyType,Const.POLICY_TYPE_DRUG,
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
                startActivity(new Intent(this, CaseDetailActivity.class).putExtra(Contants.CaseDetailBean, bean)
                        .putExtra(Contants.ISCANCER, false));
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
}

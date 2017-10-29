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
import com.zeyuan.kyq.Entity.QuotaItemChildEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.SearchFunctionAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.CaseDetailBean;
import com.zeyuan.kyq.bean.CommBean;
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
 * Created by Administrator on 2016/7/22.
 *
 * 搜索页面
 *
 * @author wwei
 */
public class SearchFunctionActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener,TextWatcher
        ,View.OnKeyListener,SearchStringBiz.SearchStringInterface,HttpResponseInterface{

    private static final String TAG = "SearchFunctionActivity";

    //搜索类型
    private String type;
    private int flag;
    //搜索相关
    private ImageView imgclose;
    private EditText et;
    private TextView tv;
    private SearchFunctionAdapter adapter;
    //搜索记录相关
    private RelativeLayout rl_record;
    private SearchFunctionAdapter recordAdapter;
    private Object recordObject;
    //索引相关
    private List<String> pyList;
    private List<String> fristList;
    private boolean searchflag = false;
    //数据相关
    private Object list;
    private Object searchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_symtom);

        //设置顶部视图
        initTopView();
        //获取搜索类型
        flag = getIntent().getIntExtra(Const.SEARCH_TYPE,0);
        type = getType(flag);
        if(flag == 0){
            Toast.makeText(this, "搜索功能出错，请返回。", Toast.LENGTH_SHORT).show();
            return;
        }
        //初始化数据
        initData();
        //初始化搜素记录
        initRecordView();
        //初始化视图控件
        initView();

    }

    private void initTopView(){
        ImageView img_back = (ImageView)findViewById(R.id.iv_back_search);
        img_back.setOnClickListener(this);
        tv = (TextView)findViewById(R.id.tv_dismiss_input);
        tv.setOnClickListener(this);
    }


    private void initData(){
        switch (flag){
            case Const.SEARCH_DRUG:
                //搜索数据源
                list = Factory.getData(Const.N_DataSearchDrugList);
                //获取索引串
                new SearchStringBiz(SearchStringBiz.DRUG,this,list).execute();
                break;
            case Const.SEARCH_QUOTA:
                //搜索数据源
                list = getIntent().getSerializableExtra(Const.RECORD_QUOTA);
                //获取索引串
                new SearchStringBiz(SearchStringBiz.QUOTA,this,list).execute();
                break;
            default:
                Toast.makeText(this,"无法获取搜索类型，请返回。",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void initRecordView(){
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
                    recordAdapter = new SearchFunctionAdapter(this,recordObject,flag);
                    listview.setAdapter(recordAdapter);
                    listview.setOnItemClickListener(this);
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,TAG);
        }
    }

    private void initView(){
        try {
            et = (EditText)findViewById(R.id.et_search);

            imgclose = (ImageView)findViewById(R.id.back_search_text);
            imgclose.setVisibility(View.GONE);

            et.addTextChangedListener(this);//搜索框内容改变监听
            et.setOnKeyListener(this);
            imgclose.setOnClickListener(this);

            ListView listview = (ListView)findViewById(R.id.lv_search_symtom);
            adapter = new SearchFunctionAdapter(this,null,flag);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(this);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,TAG);
        }
    }


    private String getType(int pos){
        String temp = "";
        switch (pos){
            case Const.SEARCH_DRUG:
                temp = Const.RECORD_DRUG;
                break;
            case Const.SEARCH_QUOTA:
                temp = Const.RECORD_QUOTA;
                break;
        }
        return temp;
    }

    @Override
    public Map getParamInfo(int tag) {
        return null;
    }

    private String[] shareParams;
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
        }else if(flag == Const.EGetCommDetail){
            args = new String[]{
                    Contants.InfoID, UserinfoData.getInfoID(this),
                    "SpID",SpID,
                    "Type","4"
            };
            shareParams = args;
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object response, int flag) {
        if(flag == Const.EGetSolutionDetail){
            CaseDetailBean bean = (CaseDetailBean) response;
            if (bean!=null&& Contants.RESULT.equals(bean.getIResult())) {
                bean.setStepId(StepID);
                startActivity(new Intent(this, CaseDetailActivity.class).putExtra(Contants.CaseDetailBean, bean).putExtra(Contants.ISCANCER,false));
            }
        }else if(flag == Const.EGetCommDetail){
            CommBean data = (CommBean)response;
            if(data!=null&&Const.RESULT.equals(data.getIResult())){
                startActivity(new Intent(this, ResultDetailActivity.class)
                        .putExtra(Contants.CommBean, data)
                        .putExtra(Const.RESULT_PARAMS_FOR_SHARE,shareParams)
                        .putExtra(Const.SET_QUOTA_TYPE,4));
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
                UserinfoData.saveSearchRecord(this, type, "");
                rl_record.setVisibility(View.GONE);
                Toast.makeText(this, "记录已清空", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private String StepID;
    private String SpID;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()){
            case R.id.lv_record_search_symptom:
                switch (flag){
                    case Const.SEARCH_DRUG:
                        ConfStepEntity recordEntity = (ConfStepEntity)recordAdapter.getItem(position);
                        StepID = recordEntity.getStepID();
                        if(!TextUtils.isEmpty(StepID)) Factory.post(this, Const.EGetSolutionDetail);
                        break;
                    case Const.SEARCH_QUOTA:
                        QuotaItemChildEntity quotaEntity = (QuotaItemChildEntity)recordAdapter.getItem(position);
                        SpID = quotaEntity.getSpID();
                        if(!TextUtils.isEmpty(SpID)) Factory.post(this,Const.EGetCommDetail);
                        break;
                }
                break;
            case R.id.lv_search_symtom:

                switch (flag){
                    case Const.SEARCH_DRUG:
                        ConfStepEntity recordEntity = (ConfStepEntity)adapter.getItem(position);
                        StepID = recordEntity.getStepID();
                        if(!TextUtils.isEmpty(StepID)) {
                            String recordStr = StepID + Const.BREAK_SMALL + recordEntity.getStepName();
                            String record = UserinfoData.getSearchRecord(this, type);
                            String save = SearchUtils.getSaveString(record, recordStr);
                            UserinfoData.saveSearchRecord(this,type,save);
                            Factory.post(this,Const.EGetSolutionDetail);
                        }
                        break;
                    case Const.SEARCH_QUOTA:
                        QuotaItemChildEntity quotaEntity = (QuotaItemChildEntity)adapter.getItem(position);
                        SpID = quotaEntity.getSpID();
                        if(!TextUtils.isEmpty(SpID)) {
                            String recordStr = SpID + Const.BREAK_SMALL + quotaEntity.getSpName();
                            Log.i("ZYS", "recordStr:" + recordStr);
                            String record = UserinfoData.getSearchRecord(this, type);
                            Log.i("ZYS","rec:"+record);
                            String save = SearchUtils.getSaveString(record, recordStr);
                            Log.i("ZYS","save:"+save);
                            UserinfoData.saveSearchRecord(this,type,save);
                            Factory.post(this,Const.EGetCommDetail);
                        }
                        break;
                }

                break;
        }

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
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
        adapter.updata(searchlist,flag);
    }

    @Override
    public void setData(List<List<String>> o) {
        if(o!=null){
            pyList = o.get(0);
            fristList = o.get(1);
            searchflag = true;
        }
    }

    //获取搜索结果
    private Object getSearchList(String str,Object obj){
        List temp = new ArrayList<>();
        List data = getList(obj);
        try {
            String name;
            String pyname;
            String pyfrist;
            str = str.replace(" ","");
            for(int i=0;i<data.size();i++){
                name = getName(data, i);
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
            ExceptionUtils.ExceptionToUM(e, this, "SearchSymptomActivity");
        }
        return  temp;
    }

    private List getList(Object obj){
        if(obj==null) return null;
        List data = null;
        switch (flag){
            case Const.SEARCH_DRUG:
                data = (List<ConfStepEntity>)obj;
                break;
            case Const.SEARCH_QUOTA:
                data = (List<QuotaItemChildEntity>)obj;
                break;
        }
        return data;
    }

    private String getName(List data,int pos){
        if(data==null||data.size()==0) return "";
        String temp = "";
        switch (flag){
            case Const.SEARCH_DRUG:
                temp = ((ConfStepEntity)data.get(pos)).getStepName().toLowerCase();
                break;
            case Const.SEARCH_QUOTA:
                temp = ((QuotaItemChildEntity)data.get(pos)).getSpName().toLowerCase();
                break;
        }
        if(TextUtils.isEmpty(temp)) temp = "";
        return temp;
    }

    @Override
    public void finish() {
        InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm!=null&&imm.isActive(et)){
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
        super.finish();
    }
}

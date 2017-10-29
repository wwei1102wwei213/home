package com.zeyuan.kyq.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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

import com.zeyuan.kyq.Entity.PerformEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.SearchSymtomAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.FindSymtomBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.SearchStringBiz;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/6.
 *
 * 症状搜索页面
 *
 * @author wwei
 */
public class SearchSymtomActivity extends BaseActivity implements HttpResponseInterface,SearchStringBiz.SearchStringInterface{

    private List<PerformEntity> searchlist;
    private int performId;
    private List<String> pyList;
    private List<String> fristList;
    private boolean searchflag = false;
    private RelativeLayout rl_record;
    private Map<String, String> performValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_symtom);
        performValues = (Map<String, String>) Factory.getData(Const.N_DataPerformValues);

        initrecordview();
        initview();
    }


    /***
     * 初始化历史记录
     * 记录最多保存5条
     *
     */
    private void initrecordview(){
        try {
        List<PerformEntity> recordPerform;
        String record = UserinfoData.getRecordSymptom(this);
        if(!"".equals(record)){

            recordPerform = getRecord(record);

            rl_record = (RelativeLayout)findViewById(R.id.ll_record_search_symptom);
            rl_record.setVisibility(View.VISIBLE);
            ListView listview = (ListView)findViewById(R.id.lv_record_search_symptom);
            final SearchSymtomAdapter adapter = new SearchSymtomAdapter(this,recordPerform);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        PerformEntity performEntity = adapter.getItem(position);
                        if (performEntity != null) {
                            Log.i("SSA", "点击项：" + performEntity.toString());
                            performId = performEntity.getPerformid();
                            Factory.post(SearchSymtomActivity.this,Const.EGetCancerProcess);
                        }
                    } catch (Exception e) {
                        ExceptionUtils.ExceptionToUM(e, SearchSymtomActivity.this, "SearchSymtomActivity");
                    }
                }
            });

            Button btn = (Button)findViewById(R.id.btn_clear_search_symptom);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserinfoData.saveRecordSymptom(SearchSymtomActivity.this,"");
                    rl_record.setVisibility(View.GONE);
                    Toast.makeText(SearchSymtomActivity.this,"记录已清空",Toast.LENGTH_SHORT).show();
                }
            });
        }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"SearchSymptomActivity");
        }
    }



    /***
     * 初始化视图控件
     * 加载数据
     * 设置监听
     * 尽量少添加成员变量，写在一个方法里面了
     *
     */
    private List<PerformEntity> list;
    private void initview(){
          list= new ArrayList<>();
        try {
            Log.i("SSA", "开始：" + System.currentTimeMillis() + "");

            PerformEntity perform;
            for(String key: performValues.keySet()){
                perform = new PerformEntity();
                perform.setPerformid(Integer.valueOf(key));
                perform.setPerformname(performValues.get(key));
                list.add(perform);
            }
            Collections.sort(list, new Comparator<PerformEntity>() {
                @Override
                public int compare(PerformEntity p1, PerformEntity p2) {
                    return p1.getPerformname().compareTo(p2.getPerformname());
                }
            });

            new SearchStringBiz(SearchStringBiz.SYMPTOM,this,list).execute();

        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,SearchSymtomActivity.this,"SearchSymtomActivity");
        }
        ImageView img_back = (ImageView)findViewById(R.id.iv_back_search);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchlist = new ArrayList<>();
        ListView listview = (ListView)findViewById(R.id.lv_search_symtom);
        final SearchSymtomAdapter adapter = new SearchSymtomAdapter(this,searchlist);
        listview.setAdapter(adapter);

        /**listview点击监听*/
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    PerformEntity performEntity = adapter.getItem(position);
                    if (performEntity != null) {
                        Log.i("SSA", "点击项：" + performEntity.toString());
                        performId = performEntity.getPerformid();
                        String record = UserinfoData.getRecordSymptom(SearchSymtomActivity.this);
                        if ("".equals(record)) {
                            UserinfoData.saveRecordSymptom(SearchSymtomActivity.this, performId + "/" + performEntity.getPerformname());
                        } else {
                            UserinfoData.saveRecordSymptom(SearchSymtomActivity.this, record + "@" + performId + "/" + performEntity.getPerformname());
                        }
                        Factory.post(SearchSymtomActivity.this,Const.EGetCancerProcess);
                    }
                } catch (Exception e) {
                    ExceptionUtils.ExceptionToUM(e, SearchSymtomActivity.this, "SearchSymtomActivity");
                }
            }
        });

        final ImageView imgclose = (ImageView)findViewById(R.id.back_search_text);
        imgclose.setVisibility(View.GONE);
        final EditText et = (EditText)findViewById(R.id.et_search);
        final TextView tv = (TextView)findViewById(R.id.tv_dismiss_input);

        /**搜索框内容改变监听*/
        et.addTextChangedListener(new TextWatcher() {
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
        });

        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });

        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
                imgclose.setVisibility(View.GONE);
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm!=null){
                    imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                }
            }
        });
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String,String> map = new HashMap<>();
        if(tag == NetNumber.FIND_SYMPTOM) {
            map.put(Contants.CancerID, UserinfoData.getCancerID(this));
            map.put(Contants.StepID, UserinfoData.getStepID(this));
            map.put(Contants.PerformID, performId+"");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if(flag == Const.EGetCancerProcess){
            String cureConfID = MapDataUtils.getAllCureconfID(UserinfoData.getStepID(this));
            args = new String[]{
                    Contants.InfoID,UserinfoData.getInfoID(this),
                    Contants.CancerID, UserinfoData.getCancerID(this),
                    Contants.StepID, UserinfoData.getStepID(this),
                    Contants.CureConfID,cureConfID,
                    Contants.PerformID, performId+"",
                    Const.ISNEWVERSION,Const.ISNEWVERSION_FINAL
            };
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object t, int tag) {
        if(tag == Const.EGetCancerProcess){
            FindSymtomBean bean = (FindSymtomBean) t;
            if(Contants.RESULT.equals(bean.getIResult())){
                Intent intent = new Intent(this, DiagnosisActivity.class);
                intent.putExtra(Contants.FindSymtomBean, bean);
                intent.putExtra(Contants.PerformID, performId+"");
                startActivity(intent);//跳转到诊断结果
            }
        }
    }

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {

    }

    @Override
    public void showError(int tag) {

    }

    private List<PerformEntity> getRecord(String record){
        List<PerformEntity> recordPerform = new ArrayList<>();
        PerformEntity entity;
        if(record.contains("@")){
            Log.i("SSA","record = "+record.toString());
            String[] records =  record.split("@");
            Log.i("SSA","records = "+ Arrays.toString(records));
            String[] temps;
            for(int i = records.length-1;i>=0;i--){
                entity = new PerformEntity();
                temps = records[i].split("/");
                entity.setPerformid(Integer.valueOf(temps[0]));
                entity.setPerformname(temps[1]);
                recordPerform.add(entity);
                if(recordPerform.size()==5) break;
            }
        }else{
            String[] records =  record.split("/");
            Log.i("SSA", "records = " + records.toString());
            entity = new PerformEntity();
            entity.setPerformid(Integer.valueOf(records[0]));
            entity.setPerformname(records[1]);
            recordPerform.add(entity);
        }
        return recordPerform;
    }

    private List<PerformEntity> getSearchList(String str,List<PerformEntity> data){
        List<PerformEntity> temp = new ArrayList<>();
        try {
        String name;
        String pyname;
        String pyfrist;
        str = str.replace(" ","");
        for(int i=0;i<data.size();i++){
            name = data.get(i).getPerformname().toLowerCase();

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
    public void setData(List<List<String>> o) {
        if(o!=null){
        pyList = o.get(0);
        fristList = o.get(1);
        searchflag = true;
        }
    }
}

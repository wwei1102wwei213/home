package com.zeyuan.kyq.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.SearchCircleAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.CityCancerForumBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.SearchStringBiz;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/13.
 *
 * 搜索圈子页面
 *
 * @author wwei
 */
public class SearchCircleActivity  extends BaseActivity implements SearchStringBiz.SearchStringInterface{

    private List<String> pylist;
    private List<String> firstlist;
    private SearchCircleAdapter adapter;
    private List<CityCancerForumBean.NumEntity> searchlist;
    private List<CityCancerForumBean.NumEntity> list;
    private boolean searchflag = false;
    private RelativeLayout rl_record;
    private ImageView imgclose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_article);
        initrecordview();
        initdata();
        initview();
    }

    private void initrecordview(){
        try {

            final List<CityCancerForumBean.NumEntity> recordCircle;
            String record = UserinfoData.getRecordCircle(this);
            if(record.length()!=0){
                recordCircle = new ArrayList<>();
                CityCancerForumBean.NumEntity entity;
                if(record.contains("@")){
                    Log.i("SCA","record = "+record.toString());
                    String[] records =  record.split("@");
                    Log.i("SCA","records = "+ Arrays.toString(records));
                    String[] temps;
                    for(int i = records.length-1;i>=0;i--){
                        entity = new CityCancerForumBean.NumEntity();
                        temps = records[i].split("/");
                        entity.setCircleID(temps[0]);
                        entity.setCancerName(temps[1]);
                        entity.setCancerID(temps[2]);
                        recordCircle.add(entity);
                        if(recordCircle.size()==5) break;
                    }
                }else{
                    String[] records =  record.split("/");
                    Log.i("SSA", "records = " + records.toString());
                    entity = new CityCancerForumBean.NumEntity();
                    entity.setCircleID(records[0]);
                    entity.setCancerName(records[1]);
                    entity.setCancerID(records[2]);
                    recordCircle.add(entity);
                }
                Log.i("ZYS","record:"+recordCircle.toString());
                rl_record = (RelativeLayout)findViewById(R.id.ll_record_search);
                rl_record.setVisibility(View.VISIBLE);
                ListView listview = (ListView)findViewById(R.id.lv_record_search);
                adapter = new SearchCircleAdapter(this,recordCircle);
                listview.setAdapter(adapter);
//                adapter.updata(recordCircle);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            String circleid = recordCircle.get(position).getCircleID();
                            startActivity(new Intent(SearchCircleActivity.this,NewCircleActivity.class).putExtra(Contants.CircleID,circleid));
                        } catch (Exception e) {
                            ExceptionUtils.ExceptionToUM(e, SearchCircleActivity.this, "SearchCircleActivity.setOnItemClickListener");
                        }
                    }
                });

                Button btn = (Button)findViewById(R.id.btn_clear_search);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserinfoData.saveRecordCircle(SearchCircleActivity.this,"");
                        rl_record.setVisibility(View.GONE);
                        Toast.makeText(SearchCircleActivity.this, "记录已清空", Toast.LENGTH_SHORT);
                    }
                });
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"SearchSymptomActivity");
        }
    }

    private void initdata(){
        try {
            Intent intent =  getIntent();
            List<CityCancerForumBean.NumEntity> listct = (List<CityCancerForumBean.NumEntity>)intent.getSerializableExtra("citysearchlist");
            List<CityCancerForumBean.NumEntity> listcc = (List<CityCancerForumBean.NumEntity>)intent.getSerializableExtra("cancersearchlist");

            if(listcc!=null&&listct!=null){
                listct.addAll(listcc);
                list = listct;
            }
            intent = null;
            listct = null;
            listcc = null;
            //写入圈子名字 实体类字段有误 需注意
            String temp;
            try {
                for (int i=0;i<list.size();i++) {
                    temp = list.get(i).getCircleID();
                    Map<String,String> circleValues = (Map<String,String>) Factory.getData(Const.N_DataCircleValues);
                    list.get(i).setCancerName(circleValues.get(temp));
                }
            }catch (Exception e){
                ExceptionUtils.ExceptionToUM(e,this,"SearchCircleActivity");
            }

            Log.i("SCA", "同城圈" + list.size());

            /**起个工作线程，拿索引字符串集合*/
            Log.i("SCA", "开始：" + System.currentTimeMillis() + "");
            new SearchStringBiz(SearchStringBiz.CIRCLE,this,list).execute();


        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"SearchCircleActivity");
        }
    }

    private EditText et;
    private void initview(){
        try {
            /**设置返回图标*/
            imgclose = (ImageView)findViewById(R.id.back_search_article_text);

            ImageView imgback = (ImageView)findViewById(R.id.iv_back_search_article);
            imgback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            /**设置listview展示*/
            searchlist = new ArrayList<>();
            ListView listview = (ListView)findViewById(R.id.lv_search_article);
            adapter = new SearchCircleAdapter(this,searchlist);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CityCancerForumBean.NumEntity  entity= adapter.getItem(position);
                    String circleid = entity.getCircleID();

                    String temp = "";
                    if(TextUtils.isEmpty(entity.getCancerID())){
                        temp = circleid + "/" + entity.getCancerName() +"/" +"同城圈";
                    }else{
                        temp = circleid + "/" + entity.getCancerName() +"/" +entity.getCancerID();
                    }

                    String record = UserinfoData.getRecordCircle(SearchCircleActivity.this);
                    if("".equals(record)){
                        UserinfoData.saveRecordCircle(SearchCircleActivity.this,temp);
                    }else{
                        temp = record +"@"+temp;
                        String[] temps = temp.split("@");
                        if(temps.length>5){
                            temp = temp.substring(temp.indexOf("@")+1,temp.length());
                        }
                        UserinfoData.saveRecordCircle(SearchCircleActivity.this,temp);
                    }
                    startActivity(new Intent(SearchCircleActivity.this, NewCircleActivity.class).putExtra(Contants.CircleID, circleid));
                }
            });

            /**设置输入控件*/
            et = (EditText)findViewById(R.id.et_search_article);

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
                    searchlist = getSearchList(text);
                    adapter.updata(searchlist);
                }
            });

            imgclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    et.setText("");
                    imgclose.setVisibility(View.GONE);

                }
            });

            TextView tv = (TextView)findViewById(R.id.tv_dismiss_input);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm!=null){
                        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                    }

                }
            });
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "SearchArticleActivity");
        }
    }

    private List<CityCancerForumBean.NumEntity> temp;

    /***
     * 获取搜索字符串
     *
     * @param str
     * @return
     */
    private List<CityCancerForumBean.NumEntity> getSearchList(String str){
        temp = new ArrayList<>();
        try {
            String name;
            String pyname;
            String pyfrist;
            str = str.replace(" ","");
            for(int i=0;i<list.size();i++){
                name = list.get(i).getCancerName().toLowerCase();
                if(name.startsWith(str.toLowerCase())||name.contains(str.toLowerCase())){
                    temp.add(list.get(i));
                }else{
                    if(searchflag){
                        pyfrist = firstlist.get(i);
                        if(pyfrist.startsWith(str.toLowerCase())){
                            temp.add(list.get(i));
                        }else{
                            pyname = pylist.get(i);
                            if(pyname.contains(str.toLowerCase())){
                                temp.add(list.get(i));
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"SearchCircleActivity");
        }
        return temp;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(list!=null){
            list = null;
        }
        if(searchlist!=null){
            searchlist = null;
        }
        if(pylist!=null){
            pylist = null;
        }
        if(firstlist!=null){
            firstlist = null;
        }
    }

    @Override
    public void setData(List<List<String>> o) {
        try {
            pylist = o.get(0);
            firstlist = o.get(1);
            searchflag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

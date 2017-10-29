package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.zeyuan.kyq.Entity.HomeSymptomEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.HomeSymptomGvAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/9.
 *
 * 查症状(选择分类)
 *
 * @author wwei
 */
public class HomeSymptomActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_home);
        initStatusBar();
        initView();
    }


    private GridView gv;
    private HomeSymptomGvAdapter adapter;
    private Map<String,String> map;
    private ArrayList<HomeSymptomEntity> list;
    private void initView(){
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gv = (GridView)findViewById(R.id.gv);
        int[] res = {R.mipmap.gv_img_1,R.mipmap.gv_img_2,R.mipmap.gv_img_3,
                R.mipmap.gv_img_4,R.mipmap.gv_img_5,R.mipmap.gv_img_6,
                R.mipmap.gv_img_7,R.mipmap.gv_img_8,R.mipmap.gv_img_9,
                R.mipmap.gv_img_10,R.mipmap.gv_img_11,R.mipmap.gv_img_12};
        map = (LinkedHashMap<String,String>) Factory.getData(Const.N_DataBodyPosValues);
        int[] IDS = {40,41,42,999,43,998,997,996,995,994,993,14};
        list = new ArrayList<>();
        if (map==null||map.size()==0){
                showToast("数据加载出错");
        }else {
            HomeSymptomEntity entity;
            for (int i=0;i<IDS.length;i++){
                entity = new HomeSymptomEntity();
                entity.setId(IDS[i]+"");
                entity.setName(getPosName(IDS[i]));
                entity.setRes(res[i]);
                list.add(entity);
            }
        }
        adapter = new HomeSymptomGvAdapter(this,list);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(HomeSymptomActivity.this,SymptomClassifyActivity.class)
                        .putExtra(Contants.index,position)
                        .putExtra(Contants.List, list));
            }
        });
    }

    private String getPosName(int id){
        String name = "";
        switch (id){
            case 999:
                name = "血液";
                break;
            case 998:
                name = "排泄系统";
                break;
            case 997:
                name = "头部";
                break;
            case 996:
                name = "颈喉部";
                break;
            case 995:
                name = "躯干";
                break;
            case 994:
                name = "内脏";
                break;
            case 993:
                name = "四肢";
                break;
            default:
                if (map!=null&&map.size()>0){
                    name = map.get(id+"");
                    if (TextUtils.isEmpty(name)) name = "";
                }
                break;
        }
        return name;
    }
}

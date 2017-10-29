package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.CommPolicyNewEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.EffectNewAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.CommBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.MyListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/1.
 *
 * 副作用和并发症页面
 *
 * @author wwei
 */
public class EffectNewActivity extends BaseActivity implements HttpResponseInterface{

    private String CureConfID;
    private List<CommPolicyNewEntity> list;
    private int type;
    private String typeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_effect);
        type = getIntent().getIntExtra(Const.INTENT_OR_TYPE,Const.TYPE_ISCANCER);
        list = (List<CommPolicyNewEntity>) getIntent().getSerializableExtra(Const.INTENT_EFFECT);
        CureConfID = getIntent().getStringExtra(Contants.CureConfID);

        if(type == Const.TYPE_COMPLICATION){
            typeName = "并发症";
        }else{
            typeName = "副作用";
        }

        initWhiteTitle(typeName);

        initdata();
    }

    /***
     * 设置数据
     */
    private void initdata(){
        List<CommPolicyNewEntity> temp = new ArrayList<>();
        if(list!=null&&list.size()!=0){

            for(int i = 0;i<list.size();i++){
                int key = Integer.valueOf(list.get(i).getQueryKey());
                if(key==1){
                    temp.add(list.get(i));
                }
            }

            if(temp.size()>0){
                setStepView(temp);
                list.removeAll(temp);
                if(list.size()>0){
                    setCureView(list);
                }
            }else {
                setCureView(list);
            }
        }
    }

    private String CommPolicyID;

    /***
     * 设置阶段相关视图
     *
     * @param temp
     */
    private void setStepView(List<CommPolicyNewEntity> temp){
        TextView tv = (TextView)findViewById(R.id.tv_step_more);
        tv.setVisibility(View.VISIBLE);
        String str = MapDataUtils.getAllStepName(temp.get(0).getQueryValue());
        tv.setText("与"+str+"相关的"+typeName);

        MyListView lv = (MyListView)findViewById(R.id.lv_step_more);
        lv.setVisibility(View.VISIBLE);
        lv.setFocusable(false);
        final EffectNewAdapter adapter = new EffectNewAdapter(this,temp);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommPolicyID = adapter.getItem(position);
                Factory.post(EffectNewActivity.this,Const.EGetCommDetail);
            }
        });
    }

    /***
     * 设置简短类型相关视图
     *
     * @param temp
     */
    private void setCureView(List<CommPolicyNewEntity> temp){
        TextView tv = (TextView)findViewById(R.id.tv_cureconf_more);
        tv.setVisibility(View.VISIBLE);
        String str = MapDataUtils.getCureValues(temp.get(0).getQueryValue());
        tv.setText("与"+str+"相关的"+typeName);

        MyListView lv = (MyListView)findViewById(R.id.lv_cureconf_more);
        lv.setVisibility(View.VISIBLE);
        lv.setFocusable(false);
        final EffectNewAdapter adapter = new EffectNewAdapter(this,temp);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommPolicyID = adapter.getItem(position);
                Factory.post(EffectNewActivity.this, Const.EGetCommDetail);
            }
        });
    }

    @Override
    public Map getParamInfo(int tag) {
        return null;
    }

    private String[] shareParems;
    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if(flag == Const.EGetCommDetail){
            args = new String[]{
                    Contants.InfoID, UserinfoData.getInfoID(this),
                    Contants.CommPolicyID,CommPolicyID,
                    "Type","3"
            };
            shareParems = args;
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object response, int flag) {
        if(flag == Const.EGetCommDetail){
            CommBean data = (CommBean)response;
            if(data!=null&&Const.RESULT.equals(data.getIResult())){
                startActivity(new Intent(this, ResultDetailActivity.class)
                        .putExtra(Contants.CommBean, data)
                        .putExtra(Const.RESULT_PARAMS_FOR_SHARE,shareParems));
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

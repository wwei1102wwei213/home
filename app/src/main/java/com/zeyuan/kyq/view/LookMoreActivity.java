package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.PolicyChildAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.CommBean;
import com.zeyuan.kyq.bean.PolicyDataEntity;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.MyListView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/15.
 *
 * 查看更多（副作用和并发症）
 *
 * @author wwei
 */
public class LookMoreActivity extends BaseActivity implements HttpResponseInterface{

    public static final String TYPE_POLICY = "TYPE_POLICY";//并发症或副作用相关
    public static final String SHOW_NAME = "SHOW_NAME";//需要显示的名字
    public static final String SHOW_DATA = "SHOW_DATA";//需要显示的名字

    private int type;
    private String name;
    private List<PolicyDataEntity> list;
    private String PolicyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_more);

        //获取启动type
        type = getIntent().getIntExtra(TYPE_POLICY,1);
        //获取
        name = getIntent().getStringExtra(SHOW_NAME);
        list = (List<PolicyDataEntity>)getIntent().getSerializableExtra(SHOW_DATA);

        initTitle();
        initview();
    }

    /***
     * 设置标题
     *
     */
    private void initTitle(){
        if(type==3){
            name = getString(R.string.other_mable) + "副作用";
        }else if(type==4){
            name = getString(R.string.other_mable) + "并发症";
        }else{
            if(TextUtils.isEmpty(name)){
                if(type==1){
                    name = "相关的副作用";
                }else if(type==2){
                    name = "相关的并发症";
                }
            }else {
                if(type==1){
                    name = "与" + name + "相关的副作用";
                }else if(type==2){
                    name = "与" + name + "相关的并发症";
                }
            }
        }
        initWhiteTitle(name);
    }

    /***
     * 设置视图
     *
     */
    private void initview(){
        MyListView lv = (MyListView) findViewById(R.id.lv_look_more);
        final PolicyChildAdapter adapter = new PolicyChildAdapter(this,list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PolicyDataEntity entity = adapter.getItem(position);
                if(entity!=null&&!TextUtils.isEmpty(entity.getPolicyID())){
                    PolicyID = entity.getPolicyID();
                    Factory.post(LookMoreActivity.this, Const.EGetCommDetail);
                }
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
                    Contants.CommPolicyID,PolicyID,
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

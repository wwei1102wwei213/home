package com.zeyuan.kyq.view;

import android.os.Bundle;
import android.util.Log;

import com.zeyuan.kyq.Entity.SearchPolicyEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.Map;

/**
 * Created by Administrator on 2016/5/27.
 *
 * 查副作用页面
 *
 * @author wwei
 */
public class SearchEffectActivity extends BaseActivity implements HttpResponseInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_effect);

        initTitle();
        initView();
        initData();


    }

    private void initTitle(){
        initWhiteTitle("查副作用");
    }

    private void initData(){
        Factory.post(this,Const.EGetSearchToPolicy);
    }

    private void initView(){

    }

    @Override
    public Map getParamInfo(int tag) {
        return null;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if(flag == Const.EGetSearchToPolicy){
            String CureConfID = MapDataUtils.getAllCureconfID(UserinfoData.getStepID(this));
            args = new String[]{
                    Contants.InfoID, UserinfoData.getInfoID(this),
                    Contants.StepID, UserinfoData.getStepID(this),
                    Contants.CancerID,UserinfoData.getCancerID(this),
                    Contants.CureConfID,CureConfID,
                    "Type","1"
            };
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object response, int flag) {

        if(flag == Const.EGetSearchToPolicy){
            SearchPolicyEntity data = (SearchPolicyEntity)response;
            if(data!=null&&Const.RESULT.equals(data.getiResult())){
                Log.i("ZYS", "DATA:"+data.toString());
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

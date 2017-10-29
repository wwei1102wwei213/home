package com.zeyuan.kyq.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.ChooseQuotaTypeAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ConstUtils;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/3.
 *
 * 选择要记录的肿标页面
 *
 * @author wwei
 */
public class ChooseQuotaTypeActivity extends BaseActivity implements HttpResponseInterface{

    private ChooseQuotaTypeAdapter adapter;
    private List<String> old;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_quota_type);
        initView();
        initData();
    }

    private void initView(){
        int flag = getIntent().getIntExtra(Const.RECORD_CLASSIFY_QUOTA_STATUS, 1);
        if (flag==1){
            old = UiUtils.getDefaultQuotaTypeForCancerID(UserinfoData.getCancerID(this));
            adapter = new ChooseQuotaTypeAdapter(this,
                    UiUtils.getDefaultQuotaTypeForCancerID(UserinfoData.getCancerID(this)));
        }else if (flag == 2){
            ArrayList<String> temp = new ArrayList<>();
            old = new ArrayList<>();
            adapter = new ChooseQuotaTypeAdapter(this,temp);
        }else {
            ArrayList<String> temp = getIntent().getStringArrayListExtra(Const.RECORD_CLASSIFY_QUOTA_CHECKED);
            old = new ArrayList<>();
            if (temp!=null&&temp.size()>0){
                for (String str:temp){
                    old.add(str);
                }
            }
            adapter = new ChooseQuotaTypeAdapter(this,temp);
        }
        ListView lv = (ListView)findViewById(R.id.lv);
        lv.setAdapter(adapter);
        findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSave();
            }
        });
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData(){

    }

    /*private void toBack(){
        if (isChanged()){
            ZYDialog.Builder builder = new ZYDialog.Builder(this);
            builder.setTitle("提示")
                .setMessage("不保存就退出吗？")
                    .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            toSave();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).create().show();
        }
    }*/

    private boolean isChanged(){
        List<String> temp = adapter.getCheck();
        if (temp==null) return true;
        if (temp.size()==old.size()){
            if (temp.size()==0){
                return false;
            }else {
                for (int i=0;i<temp.size();i++){
                    if (!temp.get(i).equals(old.get(i))) return true;
                }
            }
        }else {
            return true;
        }
        return false;
    }

    private boolean clickAble = true;
    private void toSave(){
        if (clickAble){
            clickAble = false;
            Factory.postPhp(this,Const.PAddMarkTypeByUser);
        }
    }

    private int exit = 0;
    @Override
    public void finish() {
        if (exit == 0){
            if (isChanged()){
                ZYDialog.Builder builder = new ZYDialog.Builder(this);
                builder.setTitle("提示")
                        .setMessage("不保存就退出吗？")
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                toSave();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                exit = 1;
                                dialog.dismiss();
                                finish();
                            }
                        }).create().show();
            }else {
                super.finish();
            }
        }else {
            super.finish();
        }

    }

    private void toFinish(){
        setResult(Const.REQUEST_CODE_CHOOSE_QUOTA_TYPE,getIntent().
                putExtra(Const.RECORD_REQUEST_QUOTA_TYPE,adapter.getCheck()));
        exit = 1;
        finish();
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String,String> map = new HashMap<>();
        map.put(Contants.InfoID,UserinfoData.getInfoID(this));
        map.put("TypeList", ConstUtils.getParamsForPic(adapter.getCheck()));
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {

        if (flag == Const.PAddMarkTypeByUser){
            PhpUserInfoBean bean = (PhpUserInfoBean)response;
            if (Const.RESULT.equals(bean.getiResult())){
                showToast("保存成功");
                toFinish();
            }else {
                showToast("保存失败");
            }
            clickAble = true;
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
        clickAble = true;
        showToast("网络请求失败");
    }
}

package com.zeyuan.kyq.fragment.other;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.CircleCancerAdapter;
import com.zeyuan.kyq.app.BaseZyFragment;
import com.zeyuan.kyq.bean.FollowBean;
import com.zeyuan.kyq.biz.forcallback.AdapterCallback;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.NewCircleActivity;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/11.
 *
 * 更多圈子 抗癌圈 列表
 *
 * @author wwei
 */
public class CircleCancerFragment extends BaseZyFragment implements AdapterCallback,AdapterView.OnItemClickListener
                    ,HttpResponseInterface{

    private CircleCancerAdapter adapter;
    private String CircleID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_circle_cancer,container,false);
        initData();
        initView();
        LogCustom.i("ZYS","CircleCancerFragment onCreateView");
        return rootView;
    }

    private void initData(){

    }

    private void initView(){
        ListView lv = (ListView)findViewById(R.id.lv_circle_follow);
        adapter = new CircleCancerAdapter(getActivity(),this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    @Override
    public Map getParamInfo(int tag) {
        return null;
    }

    private String followFlag = "1";
    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if(flag == Const.EFollowCircle){
            args = new String[]{
                    Contants.InfoID, UserinfoData.getInfoID(getActivity()),
                    Contants.CircleID, CircleID,
                    Contants.followOrcancel, followFlag,
                    "type", "2"
            };
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object response, int flag) {

        if (flag == Const.EFollowCircle){
            try {
                FollowBean bean = (FollowBean) response;
                UiUtils.setFollowUI(getActivity(),followFlag,bean.getIResult(),CircleID);
                adapter.updata();
            }catch (Exception e){

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
    public void forAdapterCallback(int pos, int tag, String id, boolean flag, Object obj) {
        try {
            if(flag){
                followFlag = "1";
            }else {
                followFlag = "2";
            }
            if (!TextUtils.isEmpty(id)){
                CircleID = id;
                Factory.post(this, Const.EFollowCircle);
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            CircleID = adapter.getItem(position);
            if (!TextUtils.isEmpty(CircleID)){
                startActivity(new Intent(getActivity(), NewCircleActivity.class)
                        .putExtra(Contants.CircleID,CircleID));
            }
        }catch (Exception e){

        }
    }
}

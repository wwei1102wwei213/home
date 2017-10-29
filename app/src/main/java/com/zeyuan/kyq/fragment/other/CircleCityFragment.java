package com.zeyuan.kyq.fragment.other;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.zeyuan.kyq.Entity.CircleNewEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.CircleOtherAdapter;
import com.zeyuan.kyq.app.BaseZyFragment;
import com.zeyuan.kyq.bean.FollowBean;
import com.zeyuan.kyq.biz.forcallback.AdapterCallback;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.NewCircleActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/12.
 *
 * 更多圈子城市圈窗口
 *
 * @author wwei
 */
public class CircleCityFragment extends BaseZyFragment implements AdapterCallback,AdapterView.OnItemClickListener,HttpResponseInterface
                            ,View.OnClickListener{

    private CircleOtherAdapter adapter;
    private String CircleID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_circle_city,container,false);

        initView();
        LogCustom.i("ZYS", "CircleCityFragment onCreateView");

        return rootView;
    }

    private CheckBox f1;
    private CheckBox f2;
    private CheckBox f3;
    private CheckBox f4;

    private void initView(){

        ListView lv = (ListView)findViewById(R.id.lv_circle_follow);
//        Map<String,List<String>> map = (Map<String,List<String>>) Factory.getData(Const.N_DataCircleCancer);
        List<CircleNewEntity> data = (List<CircleNewEntity>) Factory.getData(Const.N_DataCircleForSearch);
        List<String> list  = new ArrayList<>();
        for(int i = 0 ; i<data.size() ;i++){
            if(data.get(i).getCircleType()==2){
                list.add(data.get(i).getCircleID());
                CircleNewEntity entity=data.get(i);
                LogCustom.i("ZYS", "city:" + entity.getCircleID() + "/name:" + entity.getCircleName());
            }
        }
        list.remove("1001");
        list.remove("1002");
        list.remove("1003");
        list.remove("1004");
        adapter = new CircleOtherAdapter(getActivity(),list,this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        findViewById(R.id.v_1001).setOnClickListener(this);
        findViewById(R.id.v_1002).setOnClickListener(this);
        findViewById(R.id.v_1003).setOnClickListener(this);
        findViewById(R.id.v_1004).setOnClickListener(this);
        f1 = (CheckBox)findViewById(R.id.follow1);
        f2 = (CheckBox)findViewById(R.id.follow2);
        f3 = (CheckBox)findViewById(R.id.follow3);
        f4 = (CheckBox)findViewById(R.id.follow4);
        f1.setOnClickListener(this);
        f2.setOnClickListener(this);
        f3.setOnClickListener(this);
        f4.setOnClickListener(this);
        List<String> follows = UserinfoData.getFocusCircle(getActivity());
        if(follows!=null&&follows.size()>0){
            if(follows.contains("1001")){
                f1.setChecked(true);
            }
            if(follows.contains("1002")){
                f2.setChecked(true);
            }
            if(follows.contains("1003")){
                f3.setChecked(true);
            }
            if(follows.contains("1004")){
                f4.setChecked(true);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.v_1001:
                startActivity(new Intent(getActivity(),NewCircleActivity.class).putExtra(Contants.CircleID,"1001"));
                break;
            case R.id.v_1002:
                startActivity(new Intent(getActivity(),NewCircleActivity.class).putExtra(Contants.CircleID,"1002"));
                break;
            case R.id.v_1003:
                startActivity(new Intent(getActivity(),NewCircleActivity.class).putExtra(Contants.CircleID,"1003"));
                break;
            case R.id.v_1004:
                startActivity(new Intent(getActivity(),NewCircleActivity.class).putExtra(Contants.CircleID,"1004"));
                break;
            case R.id.follow1:
                toFollowCircle("1001", f1.isChecked());
                break;
            case R.id.follow2:
                toFollowCircle("1002", f2.isChecked());
                break;
            case R.id.follow3:
                toFollowCircle("1003", f3.isChecked());
                break;
            case R.id.follow4:
                toFollowCircle("1004", f4.isChecked());
                break;
        }
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
            FollowBean bean = (FollowBean) response;
            UiUtils.setFollowUI(getActivity(), followFlag, bean.getIResult(), CircleID);
            adapter.updata();
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

    private void toFollowCircle(String id, boolean flag){
        try {
            if (!TextUtils.isEmpty(id)){
                if(flag){
                    followFlag = "1";
                }else {
                    followFlag = "2";
                }
                CircleID = id;
                Factory.post(this, Const.EFollowCircle);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"EXC");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            CircleID = adapter.getItem(position);
            if (!TextUtils.isEmpty(CircleID)){
                startActivity(new Intent(getActivity(), NewCircleActivity.class).putExtra(Contants.CircleID,CircleID));
            }
        }catch (Exception e){

        }
    }
}

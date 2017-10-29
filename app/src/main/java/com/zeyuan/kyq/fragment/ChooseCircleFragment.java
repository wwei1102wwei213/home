package com.zeyuan.kyq.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.ChooseCircleAdapter;
import com.zeyuan.kyq.biz.forcallback.CircleGridCallback;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.forcallback.FragmentMoreCallBack;
import com.zeyuan.kyq.utils.BlurUtil.BlurColor;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.view.ReleaseForumActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/4.
 *
 * 选择圈子窗口
 *
 * @author wwei
 */
public class ChooseCircleFragment extends DialogFragment implements CircleGridCallback{

    public static final String type = "ChooseCircleFragment";

    private FragmentMoreCallBack callback;
    private static ChooseCircleFragment instance;
    private List<String> theme;

    public void setTheme(List<String> theme) {
        this.theme = theme;
    }

    public static ChooseCircleFragment getInstance(FragmentMoreCallBack callback,ArrayList<String> theme){
        try {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Const.FRAGMENT_CALL_BACK,callback);
            bundle.putSerializable("ChooseCircle",theme);
            instance = new ChooseCircleFragment();
            instance.setArguments(bundle);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "getInstance");
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if(getArguments()!=null){
                Bundle bundle = getArguments();
                callback = (FragmentMoreCallBack)bundle.getSerializable(Const.FRAGMENT_CALL_BACK);
                theme = (List<String>)bundle.getSerializable("ChooseCircle");
            }
        }catch (Exception e){

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.cancer_dialog);
        try {
            dialog.setCancelable(false);
            View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_release_circle, null);
            initData();
            initView(rootView);
            dialog.setContentView(rootView);
            Window window = dialog.getWindow();
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
            window.setWindowAnimations(R.style.choose_step_type);
            dialog.setCanceledOnTouchOutside(true);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"ChooseCircleFragment");
        }
        return dialog;
    }

    private List<List<String>> mList;
    private List<String> temp;
    private Map<String,Integer> mapCheck;
    private List<String> tempTheme;
    private void initData(){
        Map<String,List<String>> map = (Map<String,List<String>>) Factory.getData(Const.N_DataCircleCancer);
        mapCheck = new HashMap<>();
        checked = new ArrayList<>();
        mList = new ArrayList<>();
        tempTheme = new ArrayList<>();
        String id;
        for(int i=0;i<theme.size();i++){
            id = theme.get(i);
            mapCheck.put(id,0);
            temp = map.get(id);
            if(temp!=null){
                mList.add(temp);
                tempTheme.add(theme.get(i));
            }
        }
    }

    private View f_body;
    private View bg;
    private BlurColor bc;
    private void initView(View v){
        f_body = v.findViewById(R.id.f_body);
        initBlur();
        ListView lv = (ListView)v.findViewById(R.id.lv_release_forum);
        ChooseCircleAdapter adapter = new ChooseCircleAdapter(getActivity(),this,tempTheme,mList,new ArrayList<String>());
        lv.setAdapter(adapter);
        Button btn = (Button)v.findViewById(R.id.btn_release);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.dataCallBack(null,0,null,checked,mapCheck,null);
                dismiss();
            }
        });
    }

    private void initBlur(){

        bg = ((ReleaseForumActivity)getActivity()).getV_body();
        try {
            bc = new BlurColor(getActivity(),bg,f_body,8,20,BlurColor.BLUR_CUSTOM);
            bc.applyBlurView();
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"applyBlur");
        }
    }

    private List<String> checked;
    @Override
    public void setItemChange(String id,boolean ch,String pid) {

        if(ch){
            checked.add(id);
            int n = mapCheck.get(pid);
            n++;
            mapCheck.put(pid,n);
        }else {
            checked.remove(id);
            int n = mapCheck.get(pid);
            if(n>0) n--;
            mapCheck.put(pid, n);
        }
        LogCustom.i("ZYS", "checked:" + checked.toString());
    }

    @Override
    public void onPause() {
        dismiss();
        super.onPause();
    }
}

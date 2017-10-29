package com.zeyuan.kyq.app;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.zeyuan.kyq.utils.ExceptionUtils;


/**
 * Created by Administrator on 2015/9/7.
 * fragment的父类
 */
public class BaseFragment extends Fragment {
    protected Context context;
    protected View rootView;
    protected OnDataFromFragment onDataFromFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    public View getRootView() {
        return rootView;
    }

    protected View findViewById(int id) {
        return rootView.findViewById(id);
    }

    public interface OnDataFromFragment {
        void dataFromFragment(Fragment fragment,String data,String tag);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
//            LogCustom.i("ZYS", "Name1:" + this.getClass().getSimpleName());
            MobclickAgent.onPageStart(this.getClass().getSimpleName());
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,context,"BaseFragment onResume");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
//            LogCustom.i("ZYS","Name1:"+this.getClass().getSimpleName());
            MobclickAgent.onPageEnd(this.getClass().getSimpleName());
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,context,"BaseFragment onPause");
        }
    }
}

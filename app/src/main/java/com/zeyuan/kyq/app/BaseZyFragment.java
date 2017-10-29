package com.zeyuan.kyq.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.forcallback.SomeFinishCallback;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.UserinfoData;

/**
 * Created by Administrator on 2016/5/26.
 *
 *
 *
 * @author wwei
 */
public class BaseZyFragment extends Fragment{

    protected Context context;
    protected View rootView;
    protected String InfoCenterID;
    protected String InfoCenterName;
    //事件完成回调
    protected SomeFinishCallback callback;
    //友盟页面统计标识 默认为统计
    protected boolean PageFlag = true;

    public void setPageFlag(boolean flag){this.PageFlag = flag;}

    public void setCallback(SomeFinishCallback callback) {
        this.callback = callback;
    }

    public void setInfoCenterID(String infoCenterID) {
        InfoCenterID = infoCenterID;
    }

    public void setInfoCenterName(String infoCenterName) {
        InfoCenterName = infoCenterName;
    }

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

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (PageFlag){
                MobclickAgent.onPageStart(this.getClass().getSimpleName());
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,context,"BaseFragment onResume");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            if (PageFlag){
                MobclickAgent.onPageEnd(this.getClass().getSimpleName());
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, context, "BaseFragment onPause");
        }
    }

    private String getRandomMath(){
        String temp = (int)(Math.random()*89999 + 10000)+"";
        return temp;
    }

    protected String getParamKaqID(){
        return "kaq="+getRandomMath()+ UserinfoData.getInfoID(context);
    }

    //上拉加载
    public void loadingMore(){}

    //下拉刷新
    public void refreshMore(){}

    public void initStatusBar(){
        try {
            View statusBar1 = rootView.findViewById(R.id.statusBar1);
            View statusBar2 = rootView.findViewById(R.id.statusBar2);
            ViewGroup.LayoutParams params1=statusBar1.getLayoutParams();
            params1.height=getStatusBarHeight();
            ViewGroup.LayoutParams params2=statusBar2.getLayoutParams();
            params2.height=getStatusBarHeight();
            statusBar1.setLayoutParams(params1);
            statusBar2.setLayoutParams(params2);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,context,"initStatusBar BaseZYFragment");
        }
    }

    public void initStatusBar2(){
        try {
            View statusBar2 = rootView.findViewById(R.id.statusBar2);
            ViewGroup.LayoutParams params2=statusBar2.getLayoutParams();
            params2.height=getStatusBarHeight();
            statusBar2.setLayoutParams(params2);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,context,"initStatusBar2 BaseZYFragment");
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        try {
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = getResources().getDimensionPixelSize(resourceId);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "获取状态栏高度失败");
        }
        return result;
    }

    public void showToast(String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }

}

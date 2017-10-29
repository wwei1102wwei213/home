package com.zeyuan.kyq.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.forcallback.SomeFinishCallback;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.UserinfoData;

/**
 * Created by Administrator on 2017/1/3.
 *
 *
 * @author wwei
 */
public abstract class BaseNoDestroyFragment extends Fragment{

    protected Context context;
    protected View rootView;
    protected String InfoCenterID;
    protected String InfoCenterName;
    protected boolean isVisible;
    private boolean isPrepared;
    private boolean isFirst = true;
    //事件完成回调
    protected SomeFinishCallback callback;
    //友盟页面统计标识 默认为统计
    protected boolean PageFlag = true;

    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

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

    public BaseNoDestroyFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        setHasOptionsMenu(true);
        LogCustom.i("TAG", "fragment->onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = initView(inflater,container,savedInstanceState);
            LogCustom.d("TAG", "fragment->onCreateView 新建rootView");
        }
        LogCustom.d("TAG", "fragment->onCreateView 复用rootView");
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogCustom.d("TAG", "fragment->onActivityCreated");
        isPrepared = true;
        lazyLoad();
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
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogCustom.d("TAG", "fragment->setUserVisibleHint 视图可见");
        if (getUserVisibleHint()) {
            isVisible = true;
            lazyLoad();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void lazyLoad() {
        if (!isPrepared || !isVisible || !isFirst) {
            return;
        }
        LogCustom.d("TAG", getClass().getName() + "->initData() ");
        initData();
        isFirst = false;
    }

    //do something
    protected void onInvisible() {

    }

    @Override
    public void onPause() {
        super.onPause();
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

    public abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void initData();

}

package com.zeyuan.kyq.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.widget.CustomArcProgressBar.MyCircleProgressBar;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/12/8.
 *
 * 药物详情页面，替代原来的方案详情页面
 *
 * @author wwei
 */
public class DrugDetailActivity extends BaseActivity implements View.OnClickListener{


    //进度条
    private MyCircleProgressBar cpb1;
    private MyCircleProgressBar cpb2;
    private MyCircleProgressBar cpb3;
    private static final int CPB_1 = 1;
    private static final int CPB_2 = 2;
    private static final int CPB_3 = 3;
    //Handler对象
    private MyHandler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏透明
        setStatusBarTranslucent();
        setContentView(R.layout.activity_drug_detail);
        //设置填充状态栏高度
        initStatusBar();
        //初始化视图控件
        initView();
        //设置数据
        initData();
        //设置监听
        initListener();
    }

    private void initView(){
        cpb1 = (MyCircleProgressBar)findViewById(R.id.cpb_1);
        cpb2 = (MyCircleProgressBar)findViewById(R.id.cpb_2);
        cpb3 = (MyCircleProgressBar)findViewById(R.id.cpb_3);
    }

    private void initData(){
        mHandler = new MyHandler(this);
        addProrgress(CPB_1,61);
    }

    private void initListener(){
        findViewById(R.id.iv_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void updateProgressBar(int flag,int progress){
        switch (flag){
            case CPB_1:
                cpb1.setProgress(progress);
                cpb1.postInvalidate();
                if (progress==61){
                    addProrgress(CPB_2,32);
                }
                break;
            case CPB_2:
                cpb2.setProgress(progress);
                cpb2.postInvalidate();
                if (progress==32){
                    addProrgress(CPB_3,88);
                }
                break;
            case CPB_3:
                cpb3.setProgress(progress);
                cpb3.postInvalidate();
                break;
        }
    }

    private static class MyHandler extends Handler{
        private final WeakReference<DrugDetailActivity> weakReference;
        public MyHandler(DrugDetailActivity activity){
            weakReference = new WeakReference<>(activity);
        }
        private DrugDetailActivity activity;
        @Override
        public void handleMessage(Message msg) {
            activity = weakReference.get();
            int flag = (int)msg.obj;
            activity.updateProgressBar(flag,msg.what);
        }
    }

    public void addProrgress(int flag, int Max) {
        Thread thread = new Thread(new ProgressThread(flag,Max));
        thread.start();
    }

    class ProgressThread implements Runnable{
        private int i= 0;
        private int Max = 1;
        private int flag;
        public ProgressThread(int flag,int Max) {
            this.flag = flag;
            if (Max>100) Max = 100;
            this.Max = Max;
        }
        @Override
        public void run() {
            for(;i<=Max;i++){
                if(isFinishing()){
                    break;
                }
                Message msg = new Message();
                msg.what = i;
                msg.obj = flag;
                SystemClock.sleep(30);
                mHandler.sendMessage(msg);
            }
        }
    }

}

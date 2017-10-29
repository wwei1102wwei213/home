package com.zeyuan.kyq.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.umeng.analytics.MobclickAgent;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.PatientDetailBean;
import com.zeyuan.kyq.biz.ComeIntoBiz;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.service.ZYKaqService;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.NetworkUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * 初始页面
 */
public class SplashActivity extends BaseActivity implements ViewSwitcher.ViewFactory,View.OnTouchListener,HttpResponseInterface{

    private static final String TAG = SplashActivity.class.getSimpleName();
    private MyHandler mHandler;
    private boolean isLive = true;
    private boolean isfinish22 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            //初始化唯一标识
            initUUID();
            //启动service
            initService();
            //初始化友盟
//            initUmeng();
            //显示版本号
            initView();
            //初始化APP
            initApp();
        } catch (Exception e){

        }
    }

    private void initUUID(){
        try {
            final TelephonyManager tm = (TelephonyManager) getBaseContext().
                    getSystemService(Context.TELEPHONY_SERVICE);
            final String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(),
                    ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
            ZYApplication.deviceId = deviceUuid.toString();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"获取设备位移标识失败");
        }
    }

    //初始化APP
    private void initApp(){
        //检测网络状态
        int mNetWorkState = NetworkUtils.getNetworkState(this);
        if(mNetWorkState!=0){
            init();
        }else {
            //没有网络则弹出提示窗口，并启动等待网络线程
            mHandler = new MyHandler(this);
            NetworkUtils.checkNetworkState(this);
            mHandler.post(checkNetWork);
        }
    }

    //监听网络状态的线程
    private Runnable checkNetWork = new Runnable() {
        @Override
        public void run() {
            if(isLive){
                if(ZYApplication.mNetWorkState==0){
                    mHandler.postDelayed(checkNetWork,1500);
                }else{
                    mHandler.sendEmptyMessage(NETWORK_IS_OK);
                }
            }
        }
    };

    //注册友盟推送
    private void initUmeng(){
        /*try {
            MobclickAgent.openActivityDurationTrack(false);
            PushAgent mPushAgent = PushAgent.getInstance(this);
//            String device_token = mPushAgent.getRegistrationId();
//            Log.i("ZYS", "device_token:" + device_token);
            mPushAgent.enable();//开启推送
            mPushAgent.setPushIntentServiceClass(ZYPushIntentService.class);

        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"UM.push.Error");
        }*/
        try {
            MobclickAgent.openActivityDurationTrack(false);
            /*PushAgent mPushAgent = PushAgent.getInstance(this);
            mPushAgent.enable();//开启推送
            mPushAgent.setPushIntentServiceClass(ZYPushIntentService.class);*/
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"initPush");
        }

    }

    private ImageSwitcher is;
    //显示闪屏版本号
    private void initView(){
        /*if ("2.2".equals(ZYApplication.versionNum)&& UserinfoData.getFlagForVer22(this)==0){
            is = (ImageSwitcher)findViewById(R.id.is);
            is.setFactory(this);
            imgIds = new int[]{R.mipmap.yd_1,R.mipmap.yd_2,R.mipmap.yd_3};
            is.setImageResource(imgIds[0]);
            is.setOnTouchListener(this);
            is.setVisibility(View.VISIBLE);
        }else {*/
        TextView tv = (TextView)findViewById(R.id.tv_splash);
        tv.setText("V" + ZYApplication.versionNum);
        findViewById(R.id.rl_splash_temp).setVisibility(View.VISIBLE);
//        }
    }

    //初始化配置数据
    private void init(){
        new ComeIntoBiz(this).execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isLive = false;
    }

    /***
     * 启动Service组件
     *
     */
    private void initService(){
        try {
            Intent intent = new Intent(this, ZYKaqService.class);
            intent.setAction(Const.SERVICE_START);
            intent.setPackage(getPackageName());
            this.startService(intent);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"InitService()");
        }
        /*try {
            Intent pushIntent = new Intent(this,ZYPushIntentService.class);
            pushIntent.setAction(Const.SERVICE_START);
            pushIntent.setPackage(getPackageName());
            this.startService(pushIntent);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"InitPushService()");
        }*/
    }

    /*private Runnable runnable22 = new Runnable() {
        @Override
        public void run() {
            if (isfinish22){
                mHandler.sendEmptyMessage(Ver_22_IS_OK);
            }else {
                mHandler.postDelayed(runnable22,1000);
            }
        }
    };*/

    public void toNextActivity(boolean next) {
        try {
            if (next){
                toNext();
            }else {
                Toast.makeText(this,"获取配置信息失败\n请检查网络连接",Toast.LENGTH_LONG).show();
                new AsyncTask<Void,Void,Void>(){
                    @Override
                    protected Void doInBackground(Void... params) {
                        SystemClock.sleep(3500);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        finish();
                    }
                }.execute();
            }

        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"toNextActivity()");
        }
    }

    private void toNext22(){
        UserinfoData.setFlagForVer22(this,1);
        toNext();
    }

    private void toNext(){
//        Factory.post(this, Const.EGetPatientDetail);
        String infoID = UserinfoData.getInfoID(this);
        String isHaveCreateInfo = UserinfoData.getIsHaveCreateInfo(this);
        if(!TextUtils.isEmpty(infoID)&&"1".equals(isHaveCreateInfo)){
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }else{
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
        }
        finish();
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String,String> map = new HashMap<>();
        map.put(Contants.InfoID, "9810");
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        args = new String[]{
                Contants.InfoID,UserinfoData.getInfoID(this)
        };
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object response, int flag) {
        PatientDetailBean bean = (PatientDetailBean)response;
        if (Const.RESULT.equals(bean.iResult)){
            UserinfoData.saveUserData(this, bean);
            toNextStep();
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

    private void toNextStep(){
        try {
            /*UserinfoData.saveInfoID(this,"108650");
            UserinfoData.saveIsHaveCreateInfo21(this,"1");*/
            String infoID = UserinfoData.getInfoID(this);
            String isHaveCreateInfo = UserinfoData.getIsHaveCreateInfo(this);
            if(!TextUtils.isEmpty(infoID)&&"1".equals(isHaveCreateInfo)){
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }else{
                startActivity(new Intent(SplashActivity.this, GuideActivity.class));
            }
            finish();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"toNextActivity()");
        }
    }

    private static final int NETWORK_IS_OK = 1;
    private static final int Ver_22_IS_OK = 2;
    /**
     *
     * Handler静态内部类
     *
     */
    private static class MyHandler extends Handler {
        private final WeakReference<SplashActivity> mActivity;
        public MyHandler(SplashActivity activity){
            mActivity = new WeakReference<>(activity);
        }
        private SplashActivity activity;
        @Override
        public void handleMessage(Message msg) {
            activity = mActivity.get();
            if(msg.what == NETWORK_IS_OK){
                activity.init();
            }else if (msg.what == Ver_22_IS_OK){
                activity.toNext22();
            }
        }
    }

    @Override
    public View makeView() {
        final ImageView i = new ImageView(this);
        i.setBackgroundColor(0xff000000);
        i.setScaleType(ImageView.ScaleType.CENTER_CROP);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        return i ;
    }

    /**
     * 图片id数组
     */
    private int[] imgIds;
    /**
     * 当前选中的图片id序号
     */
    private int currentPosition;
    /**
     * 按下点的X坐标
     */
    private float downX;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:{
                //手指按下的X坐标
                downX = event.getX();
                break;
            }
            case MotionEvent.ACTION_UP:{
                float lastX = event.getX();
                //抬起的时候的X坐标大于按下的时候就显示上一张图片
                if(lastX > downX){
                    if(currentPosition > 0){
                        //设置动画，这里的动画比较简单，不明白的去网上看看相关内容
                        is.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.is_left_in));
                        is.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.is_right_out));
                        currentPosition --;
                        is.setImageResource(imgIds[currentPosition % imgIds.length]);

                    }else{
                        Toast.makeText(getApplication(), "已经是第一张", Toast.LENGTH_SHORT).show();
                    }
                }
                if(lastX < downX){
                    if(currentPosition < imgIds.length - 1){
                        is.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.is_right_in));
                        is.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.is_left_out));
                        currentPosition ++ ;
                        is.setImageResource(imgIds[currentPosition]);
                        if (currentPosition==imgIds.length-1){
                            isfinish22 = true;
                        }
                    }else{
                        Toast.makeText(getApplication(), "到了最后一张", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
        return true;
    }



}

/*try {
                    Intent intent = getIntent();
                    if(intent!=null){
                        Uri uri = intent.getData();
                        String id = intent.getStringExtra("ForumID");
                        if(!TextUtils.isEmpty(id)){
                            Log.i("ZYS","id:"+id);
                            startActivity(new Intent(this,ForumDetailActivity.class).putExtra(Const.FORUM_ID,id));
                            finish();
                        }else {
                            Log.i("ZYS","id为空");
                        }
                        if (uri == null) {
                            Log.i("ZYS","uri为空");
                        }else {
                            Log.i("ZYS","uri不为空:"+uri.toString());
                        }
                    }else {
                        Log.i("ZYS","参数为空");
                    }
                }catch (Exception e){
                    Log.i("ZYS","外部打开出错");
                }*/

//测试外部链接打开app并传递参数
        /*try {
            Intent intent = getIntent();
            if(intent!=null){
                Uri uri = intent.getData();
                String id = intent.getStringExtra("ForumID");
                if(!TextUtils.isEmpty(id)){
                    Log.i("ZYS","id:"+id);
                    startActivity(new Intent(this,ForumDetailActivity.class).putExtra(Const.FORUM_ID,id));
                }else {
                    Log.i("ZYS","id为空");
                }
                if (uri == null) {
                    Log.i("ZYS","uri为空");
                }else {
                    Log.i("ZYS","uri不为空:"+uri.toString());
                }
            }else {
                Log.i("ZYS","参数为空");
            }
        }catch (Exception e){
            Log.i("ZYS","外部打开出错");
        }*/


        /*ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        int heapSize = manager.getMemoryClass();
        Log.i(Const.TAG.ZY_HTTP, "你的手机分配给app的内存为：" + heapSize + "MB");*/
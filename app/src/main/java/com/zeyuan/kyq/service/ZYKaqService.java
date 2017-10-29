package com.zeyuan.kyq.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.zeyuan.kyq.Entity.ConfStepEntity;
import com.zeyuan.kyq.Entity.DigitDataEntity;
import com.zeyuan.kyq.Entity.StepDataEntity;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.KeyBiz;
import com.zeyuan.kyq.biz.SqlConfigBiz;
import com.zeyuan.kyq.biz.UpdataPhotoBiz;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/22.
 * 启动抗癌圈服务组件，采用两种方式混合启动，界面退出时service继续运行
 *
 * @author wwei
 */
public class ZYKaqService extends Service implements HttpResponseInterface,UpdataPhotoBiz.UpdataPhotoCallBack{

    private static final String TAG = "ZYP";

    private MyHandler mHandler;
    private static final int STEP_DATA_CHANGE = 101;
    private boolean updataFlag = true;
    private String mHeadImgURL;
    private BootReceiver mReceiver;

    public ZYKaqService(){}

    /***
     * 绑定方式启动service时执行
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ZYBinder();
    }

    /***
     * 服务组件创建时的处理，该方法执行后，startService()只会触发onStartCommand()
     *
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new MyHandler(this);
//        initReceiver();
        LogCustom.i("ZYS", "Service onCreate");
    }

    private void initReceiver(){
        mReceiver = new BootReceiver();
        IntentFilter localIntentFilter = new IntentFilter("android.intent.action.USER_PRESENT");
        localIntentFilter.addAction("android.intent.action.PACKAGE_RESTARTED");
        localIntentFilter.setPriority(Integer.MAX_VALUE);// 整形最大值
        registerReceiver(mReceiver, localIntentFilter);
    }

    /***
     *
     * 服务组件退出时进行的操作
     *
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        LogCustom.i("ZYS", "service stop");
//        System.exit(0);
        Intent localIntent = new Intent();
        localIntent.setClass(this, ZYKaqService.class); // 销毁时重新启动Service
        this.startService(localIntent);

//        unregisterReceiver(mReceiver);
    }

    /***
     * 处理Intent消息事件
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent==null){}else{
            String action = intent.getAction();
            try {
                if(Const.SERVICE_START.equals(action)){
                    LogCustom.i(Const.TAG.ZY_OTHER, "SERVICE_START");

                }else if (Const.SERVICE_APP_EXIT.equals(action)){
                    updataFlag = false;
                    LogCustom.i(Const.TAG.ZY_OTHER, "SERVICE_STOP");
                }else if (Const.SERVICE_STEP_UPDATA_START.equals(action)){
                    LogCustom.i(Const.TAG.ZY_DATA, "数据同步方法开始执行");
                    startStepUpdata();
                }else if (Const.SERVICE_CORRECT_HEAD_IMG.equals(action)){
                    String temp = intent.getStringExtra(Contants.Headimgurl);
                    LogCustom.i(Const.TAG.ZY_OTHER, "temp:" + temp);
                    correctHeadImgUrl(temp);
                }
            }catch (Exception e){
                ExceptionUtils.ExceptionToUM(e, this, "service");
            }
        }
        return START_STICKY;
//        return START_REDELIVER_INTENT;
    }


    @Override
    public Map getParamInfo(int tag) {
        return null;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        switch (flag){
            case Const.EUpdatePatientDetail:
                args = new String[]{
                        Contants.InfoID, UserinfoData.getInfoID(this),
                        Contants.Headimgurl,mHeadImgURL,
                        Contants.SetBit,"1"
                };
                break;
            case Const.ESetTokenDevice:
                args = new String[]{
                        Contants.InfoID,UserinfoData.getInfoID(this),
                        "DeviceID",ZYApplication.deviceId,
                        "UploadType","3",
                        "TokenType","1",
                        "Token",ZYApplication.UmToken
                };
                break;
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object response, int flag) {
        if(flag == Const.EUpdatePatientDetail){
            BaseBean bean = (BaseBean)response;
            if(Const.RESULT.equals(bean.iResult)){
                LogCustom.i("ZYS","用户图片修复成功");
            }
        }else if(flag == Const.ESetTokenDevice){
            BaseBean bean = (BaseBean)response;
            if(Const.RESULT.equals(bean.iResult)){
                LogCustom.i("ZYS","用户登出发送成功");
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
    public void updataFinish(Object o) {
        try {
            toUpdataHeadImgURL(o.toString());
        }catch (Exception e){

        }
    }

    @Override
    public void updataFial() {

    }

    private void correctHeadImgUrl(String temp){
        try {
            if(TextUtils.isEmpty(temp)) return;
            if(temp.contains(Const.IMG_URL_HEAD)){
                toUpdataHeadImgURL(temp);
            }else {
                LogCustom.i(Const.TAG.ZY_OTHER,"开始执行修正操作" + temp);
                new UpdataPhotoBiz(this,false,temp,null).execute();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"correctHeadImgUrl");
        }

    }

    private void toUpdataHeadImgURL(String temp){
        try {
            if(TextUtils.isEmpty(temp)) return;
            mHeadImgURL = temp;
            Factory.post(this,Const.EUpdatePatientDetail);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"toUpdataHeadImgURL");
        }
    }

    private boolean isFristUP = true;
    private Runnable start = new Runnable() {
        @Override
        public void run() {
            try {
                if(updataFlag){
                    if(isFristUP||ZYApplication.isAppShow){
                        isFristUP = false;
                        LogCustom.i(Const.TAG.ZY_DATA, "同步开始");
                        String time = getUpdataTime();
                        String req = KeyBiz.getUpdataForTime(time);
                        updataStepData(req,time);
                    }
                    mHandler.postDelayed(start,1800000);
                }
            }catch (Exception e){
                ExceptionUtils.ExceptionSend(e,"同步线程出错");
            }

        }
    };

    private void startStepUpdata(){
        mHandler.post(start);
    }

    private void updataStepData(String req,String time){
        LogCustom.i(Const.TAG.ZY_DATA, "客户端时间：" + time + "\n同步数据为：" + req);
        try {
            Gson mGson = new Gson();
            Object o = mGson.fromJson(req, StepDataEntity.class);
            StepDataEntity entity = (StepDataEntity)o;
            if(!time.equals(entity.getStepData().getMaxtimestamp())){
                List<ConfStepEntity> updataList = entity.getStepData().getData();
                if(updataList!=null&&updataList.size()!=0){
                    toUpdata(updataList,entity.getStepData().getMaxtimestamp());
                }
            }
        }catch (Exception e){

        }
    }

    private void toUpdata(List<ConfStepEntity> updataList,String upTime){
        try {
            SparseArray<ConfStepEntity> array = (SparseArray<ConfStepEntity>)Factory.getData(Const.N_DataDrugNames);
            if(array==null){
                LogCustom.i("ZYD","array为空");
            }
            Object obj = Factory.getData(Const.N_DataStepData);
            StepDataEntity entity = (StepDataEntity)obj;
            List<ConfStepEntity> list = entity.getStepData().getData();
            for(ConfStepEntity item:updataList){
                int temp = Integer.valueOf(item.getStepID());
                ConfStepEntity stepEntity = array.get(temp);
                if(stepEntity!=null){
                    if(!TextUtils.isEmpty(item.getIsDel())&&"1".equals(item.getIsDel())){
                        LogCustom.i(Const.TAG.ZY_DATA, "同步数据删除，删除数据：" + stepEntity.toString());
                        list.remove(stepEntity);
                    }else {
                        LogCustom.i(Const.TAG.ZY_DATA, "同步数据修改，原数据：" + stepEntity.toString());
                        int position = SqlConfigBiz.getPositionForStepID(temp,list);
                        LogCustom.i(Const.TAG.ZY_DATA, "同步数据修改，原数据下标：" + position);
                        if(position!=-1){
                            LogCustom.i(Const.TAG.ZY_DATA, "同步数据修改，原数据：" + list.get(position).toString());
                            list.set(position,item);
                        }
                        LogCustom.i(Const.TAG.ZY_DATA, "同步数据修改，新数据：" + item.toString());
                    }
                }else{
                    LogCustom.i(Const.TAG.ZY_DATA, "同步数据新增，新数据：" + item.toString());
                    list.add(item);
                }
            }
            entity.getStepData().setData(list);
            entity.getStepData().setMaxtimestamp(upTime);
            SqlConfigBiz.updataStep(entity);
            SparseArray<ConfStepEntity> array1 = (SparseArray<ConfStepEntity>)Factory.getData(Const.N_DataDrugNames);
            LogCustom.i(Const.TAG.ZY_DATA, "新数据array1：" + array1.size());

        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"ToUpdata");
        }
    }

    private String getUpdataTime(){
        String time = null;
        try {
            Object obj = Factory.getData(Const.N_DataStepData);
            StepDataEntity entity = (StepDataEntity)obj;
            time = entity.getStepData().getMaxtimestamp();
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"getUpDataTime");
        }
        return time;
    }

    /***
     * 提供activity获取service对象的方法
     *
     */
    public class ZYBinder extends Binder {
        public ZYKaqService getService(){
            return ZYKaqService.this;
        }
    }



    /***
     * 数据封装完成后调用该方法
     *
     */
    public void uploadSconfFinish(){

    }

    private void initExecutor(String response){
        try {
            JSONObject object = new JSONObject(response);
            String temp;
            JSONObject digit = object.getJSONObject("digit");//节点digit
            JSONObject digitData = digit.getJSONObject("digitdata");//节点digit的digitData
            Iterator<String> keysIterator = digitData.keys();
            DigitDataEntity digitDataEntity = new DigitDataEntity();
            while (keysIterator.hasNext()) {
                temp = keysIterator.next();
                digitDataEntity.setDigitid(Integer.valueOf(temp));
                digitDataEntity.setDigitvalue(digitData.getString(temp));
                LogCustom.i("ZYS", "digitdata封装：" + digitDataEntity.toString());
            }
        }catch (Exception e){
            LogCustom.i("ZYS", "digit解析失败：");
        }
    }


    private boolean isMain(){
        if(Thread.currentThread().getName().equals("main")){
            LogCustom.i(TAG,"当前线程为主线程");
            return true;
        }
        LogCustom.i(TAG,"当前线程为工作线程");
        return false;
    }

    /**
     *
     * Handler静态内部类
     *
     */
    private static class MyHandler extends Handler {
        private final WeakReference<ZYKaqService> mService;
        public MyHandler(ZYKaqService service){
            mService = new WeakReference<>(service);
        }
        private ZYKaqService service;
        @Override
        public void handleMessage(Message msg) {

        }
    }

}

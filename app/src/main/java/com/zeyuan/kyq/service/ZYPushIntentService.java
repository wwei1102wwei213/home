package com.zeyuan.kyq.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.ta.util.db.TASQLiteDatabase;
import com.umeng.message.UmengMessageService;
import com.umeng.message.entity.UMessage;
import com.zeyuan.kyq.Entity.MessageItem;
import com.zeyuan.kyq.Entity.PushNewEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.view.MessageDetailActivity;

import org.android.agoo.common.AgooConstants;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/1.
 *
 *
 *
 * @author wwei
 */
public class ZYPushIntentService extends UmengMessageService {

    private static final String TAG = ZYPushIntentService.class.getName();

    //如果需要打开Activity，请调用Intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)；否则无法打开Activity。
    //无参构造方法，没有写此方法某些手机会无法使用Service，例如华为
    public ZYPushIntentService(){
    }

    @Override
    public void onMessage(Context context, Intent intent) {
        // 需要调用父类的函数，否则无法统计到消息送达

        LogCustom.i(TAG, "zyp=" + "接收到推送消息");
        try {
            //可以通过MESSAGE_BODY取得消息体


            String message = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
            UMessage msg = new UMessage(new JSONObject(message));
//            LogCustom.d(TAG, "message=" + message);
//            Log.d(TAG, "custom=" + msg.custom);    //自定义消息的内容
            long mills = System.currentTimeMillis() / 1000;
            try {
                PushNewEntity entity = new PushNewEntity();
                entity.setMsg(message);
                Map<String,String> map = getExtraMap(msg);
                if (map==null||map.size()==0){
                    entity.setRead(1);
                }else {
                    String jump = map.get("jump");
                    if (TextUtils.isEmpty(jump)){
                        entity.setRead(1);
                    }else {
                        if ("0".equals(jump)||"8".equals(jump)){
                            entity.setRead(1);
                        }else {
                            entity.setRead(0);
                        }
                    }
                }
                entity.setTime(mills);
                TASQLiteDatabase tasqLiteDatabase = ZYApplication.application.getSQLiteDatabasePool().getSQLiteDatabase();
                tasqLiteDatabase.insert(entity);
                ZYApplication.application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
            }catch (Exception e){
                LogCustom.e(TAG, "ZYPushIntentService");
                ExceptionUtils.ExceptionSend(e, "ZYPushIntentService");
            }

            try {
                Intent intentAct = new Intent();
                intentAct.setClass(context, MessageDetailActivity.class);
                intentAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentAct.putExtra("MessageAll", message);
                intentAct.putExtra("MessageTime",mills);
                Bundle bundle = new Bundle();
                MessageItem item=new MessageItem();//自定义的消息bean
                item.setMsmType("PUSH");
                item.setMsmcontent(msg.text);//获取推送的消息内容
                item.setTitle(msg.title);//获取推送的消息标题
                bundle.putSerializable("message", item);//传递一个序列化参数
                intentAct.putExtras(bundle);
                showNotification(context, msg, intentAct);//必须要有，不然收不到推送的消息
            }catch (Exception e){

            }
            // 完全自定义消息的处理方式，点击或者忽略
            /*boolean isClickOrDismissed = true;
            if(isClickOrDismissed) {
                //完全自定义消息的点击统计
                UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
            } else {
                //完全自定义消息的忽略统计
                UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
            }*/

        } catch (Exception e) {
            LogCustom.e(TAG,"ZYPushIntentService");
            ExceptionUtils.ExceptionSend(e,"ZYPushIntentService");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogCustom.i(TAG, "ZYPushIntentService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    // 通知栏显示当前播放信息，利用通知和 PendingIntent来启动对应的activity
    public void showNotification(Context context,UMessage msg,Intent intent) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(msg.title)
               .setContentText(msg.text)
               .setSmallIcon(R.drawable.ic_launcher);

        builder.setAutoCancel(true);
        Notification mNotification = builder.build();
//        mNotification.icon = R.drawable.ic_launcher;//notification通知栏图标
        mNotification.iconLevel = R.drawable.ic_launcher;//notification通知栏图标
        mNotification.defaults |= Notification.DEFAULT_SOUND;
        mNotification.defaults |= Notification.DEFAULT_VIBRATE;
        mNotification.tickerText=msg.ticker;
/*
        //自定义布局
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_umeng_push);
        contentView.setImageViewResource(R.id.Umeng_view, R.drawable.ic_launcher);
        contentView.setTextViewText(R.id.push_title, msg.title);
        contentView.setTextViewText(R.id.push_content, msg.text);
                mNotification.contentView = contentView;*/
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);//不是Intent
//        mNotification.flags = Notification.FLAG_NO_CLEAR;// 永久在通知栏里
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
        //使用自定义下拉视图时，不需要再调用setLatestEventInfo()方法，但是必须定义contentIntent
        mNotification.contentIntent = contentIntent;
        mNotificationManager.notify(103, mNotification);
    }

    private Map<String,String> getExtraMap(UMessage var2){
        if(var2!=null&&var2.extra!=null){
            Iterator iterator = var2.extra.entrySet().iterator();
            Map<String,String> map = new HashMap<>();
            while (iterator.hasNext()){
                Map.Entry entry = (Map.Entry)iterator.next();
                String key = (String)entry.getKey();
                if(key!=null){
                    map.put(key,(String)entry.getValue());
                }
            }
            return map;
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Intent localIntent = new Intent();
//        localIntent.setClass(this, ZYPushIntentService.class); // 销毁时重新启动Service
//        this.startService(localIntent);
    }
}

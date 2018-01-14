package com.zeyuan.kyq.application;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.meelive.ingkee.sdk.plugin.entity.UserInfo;
import com.morgoo.droidplugin.PluginHelper;
import com.ta.TAApplication;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.youzan.sdk.YouzanSDK;
import com.zeyuan.kyq.bean.MainPageInfoBean;
import com.zeyuan.kyq.db.DBHelper;
import com.zeyuan.kyq.db.MemoryCache;
import com.zeyuan.kyq.service.ZYKaqService;
import com.zeyuan.kyq.service.ZYPushIntentService;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.DensityUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.UserinfoData;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Created by Administrator on 2016/3/15.
 * <p>
 * 初始化application
 *
 * @author wwei
 */
public class ZYApplication extends TAApplication {

    public static final String UPDATE_STATUS_ACTION = "com.umeng.message.example.action.UPDATE_STATUS";
    public static boolean isPubish = false;
    public final static String TAG = "ZYApplication";
    public ZYKaqService service;
    //Application全局对象
    public static ZYApplication application;
    public ArrayList<Activity> listActivity = new ArrayList<>();
    //首页网络自动判断标识
    public static boolean isFirst = true;
    //app网络状态标志
    public static int mNetWorkState;
    //网络状态切换延时标志
    public static boolean mNetDelayFlag = false;
    //版本号
    public static String versionNum;
    //UUID
    public static String deviceId;
    //手机型号
    public static String phoneInfo;
    //友盟token
    public static String UmToken;
    public static boolean toCircleAble = false;
    //固定参数
    public static String[] appendParams;
    public static String addParams;
    //app是否显示
    public static boolean isAppShow = true;
    //阶段改变标志
    public static boolean stepChangeFlag = false;
    //癌种改变标识
    public static boolean cancerChangeFlag = false;
    //开始时间改变标识
    public static boolean disTimeChangeFlag = false;
    //主页显示状态标识
    public static boolean mainPageFlag = false;
    //新主页显示状态标识
    public static boolean homeMoveFlag = false;
    public static boolean homeHelpPageFlag = false;
    //主页活动UI标识
    public static boolean mainMoveFlag = false;
    //公益页面活动UI标识
    public static boolean EventMoveFlag = false;
    //圈子页面活动UI标识
    public static boolean CircleMoveFlag = false;
    //是否绑定手机标识
    public static boolean isBind = true;
    //绑定的手机号码
    public static String PhoneNum = "";
    //支付标识及返回码
    public static int CodePay = -1;
    public static boolean CodeFlag = false;
    //微信授权
    public static String CodeWXLogin = null;
    //微信授权标识
    public static boolean FlagWXLogin = false;
    //版本更新信息类
    public static MainPageInfoBean.UpEntity mUpVer = null;
    public static String mLoginType = "";
    //数字的字体
    public static Typeface typeFace;
    //用户的默认发帖圈子
    public static List<List<String>> DefaultCircles;
    //用户发帖可选的圈子
    public static Map<String, String> threadCircle;
    public static int RemindNum;//发帖时可@的最大用户数
    public static int HEIGHT62;

    public static UserInfo YK_UserInfo;

    public static final String FEEDBACK_DEFAULT_APPKEY = "23566950";

    private static final String YK_APP_ID = "1000300001";

    public static final String Youzan_client_id = "c9f9d47084962b087f";
    public static final String Youzan_client_secret = "9bc4349f389f92e9cdf5ebf9afa335f4";


    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 进行全局变量的初始化等工作
         * 数据库表单创建工作
         * 内存缓存开启
         *
         */

        try {

            //提升兼容性
            MultiDex.install(this);
            //获得application全局对象
            application = this;
            //获得版本号全局对象
            versionNum = DataUtils.getVersionName(this);
            //获得手机型号
            phoneInfo = Build.BRAND + Build.MODEL;
            //获得手机唯一标识
            deviceId = getDeviceID();
            //创建数据库表单
            CreateDatabase();
            //初始化内存缓存
            initCache();
            //初始化typeFace字体
            initTypeFace();

            //获取登录方式，并设置网络请求附加参数
            if (TextUtils.isEmpty(UserinfoData.getLoginType(this))) {
                initAppendParams();
            } else {
                setLoginType(UserinfoData.getLoginType(this));
            }
            //初始化用户反馈，业务方默认只需要init一次, 然后直接openFeedbackActivity/getFeedbackUnreadCount即可
            try {
                initFeedbackAPI();
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "用户反馈初始化失败");
            }
            try {
                HEIGHT62 = DensityUtils.dp2px(this, 62);
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "屏幕像素换算");
            }
            try {
                //初始化映客
                PluginHelper.getInstance().applicationOnCreate(getBaseContext());
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "初始化映客");
            }
            //推送
            try {
                initPush();
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "友盟推送初始化");
            }
            //初始化有赞
            YouzanSDK.init(this, Youzan_client_id);
            YouzanSDK.isDebug(true);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, TAG + "数据库表单创建失败");
        }
    }

    private void CreateDatabase() {
        DBHelper.getInstance().initCreateDigitTable(this);//分期数据表
        DBHelper.getInstance().initCreateSyncTable(this);//常用数据表
        DBHelper.getInstance().initCreateStepTable(this);//阶段数据
        DBHelper.getInstance().initCreatePushTable(this);//推送数据
        DBHelper.getInstance().initCreateMsgClickTable(this);//消息点击数据
    }

    private void initCache() {
        MemoryCache.openCache();
    }

    private void initTypeFace() {
        try {
            typeFace = Typeface.createFromAsset(getApplicationContext().getAssets(), "font/BEBASNEUE.OTF");
        } catch (Exception e) {

        }
    }

    public void appexit() {
        try {
            for (Activity activity : listActivity) {
                activity.finish();
            }
            System.exit(0);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "exit");
        }
    }

    /***
     *
     *
     *   1 AT==>AppType
     *   2 LT==>LoginType
     *   3 Ver==>Version
     *   4 MD==>MachineDetail
     *
     * @param type
     */
    public static void setLoginType(String type) {
        if (TextUtils.isEmpty(type)) {
            return;
        }
        mLoginType = type;
        appendParams = new String[]{
                "Ver", versionNum,
                "MD", phoneInfo,
                "AT", "2",
                "DeviceID", deviceId,
                "LT", type
        };
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < appendParams.length; i += 2) {
            try {
                appendParams[i] = URLEncoder.encode(appendParams[i], "UTF-8");
                appendParams[i + 1] = URLEncoder.encode(appendParams[i + 1], "UTF-8");
            } catch (Exception e) {

            }
            sb.append(appendParams[i]).append("=").append(appendParams[i + 1]).append("&");
        }
        sb = sb.deleteCharAt(sb.lastIndexOf("&"));
        addParams = sb.toString();
        LogCustom.i("ZYS", "sb：" + sb.toString());
    }

    private void initAppendParams() {
        appendParams = new String[]{
                "Ver", versionNum,
                "MD", phoneInfo,
                "DeviceID", deviceId,
                "AT", "2"
        };
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < appendParams.length; i += 2) {
            try {
                appendParams[i] = URLEncoder.encode(appendParams[i], "UTF-8");
                appendParams[i + 1] = URLEncoder.encode(appendParams[i + 1], "UTF-8");
            } catch (Exception e) {

            }
            sb.append(appendParams[i]).append("=").append(appendParams[i + 1]).append("&");
        }
        sb = sb.deleteCharAt(sb.lastIndexOf("&"));
        addParams = sb.toString();
    }

    private String getDeviceID() {
        try {
            final TelephonyManager tm = (TelephonyManager) getBaseContext().
                    getSystemService(Context.TELEPHONY_SERVICE);
            final String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(),
                    ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            return deviceUuid.toString();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "getDeviceID");
            return "DeviceID_Error";
        }
    }

    private void initFeedbackAPI() {
        try {
            FeedbackAPI.init(this, ZYApplication.FEEDBACK_DEFAULT_APPKEY);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "用户反馈初始化失败");
        }
    }

    private void initPush() {
        try {
            MobclickAgent.openActivityDurationTrack(false);
            PushAgent mPushAgent = PushAgent.getInstance(this);
            //注册推送服务，每次调用register方法都会回调该接口
            mPushAgent.register(new IUmengRegisterCallback() {

                @Override
                public void onSuccess(String deviceToken) {
                    //注册成功会返回device token
                    LogCustom.i("ZYS", "deviceToken：" + deviceToken);
                    if (!TextUtils.isEmpty(deviceToken))
                        ZYApplication.UmToken = deviceToken;
                }

                @Override
                public void onFailure(String s, String s1) {

                }
            });
            mPushAgent.setPushIntentServiceClass(ZYPushIntentService.class);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "initPush");
        }

    }


    @Override
    protected void attachBaseContext(Context base) {
        PluginHelper.getInstance().applicationAttachBaseContext(base);
        super.attachBaseContext(base);
    }

}



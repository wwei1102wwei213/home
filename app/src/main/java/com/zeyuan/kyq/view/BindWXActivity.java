package com.zeyuan.kyq.view;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zeyuan.kyq.Entity.BindMobileEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.PatientDetailBean;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.bean.UserInfoWXBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.presenter.LoginWXPresenter;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/9/9.
 *
 * 绑定微信页面
 *
 * @author wwei
 */
public class BindWXActivity extends BaseActivity implements View.OnClickListener,
        ViewDataListener,HttpResponseInterface{

    private TextView pin;
    private Button bind;
    private TextView bind_wx;
    private EditText et_pin;
    public IWXAPI api;
    private String code;
    private String OpenID;
    private String WXName;
    private ProgressDialog dialog;
    private String type = "5";
    private String mobile;
    private String mPin;
    private static final String REGEX_PIN = "^[0-9]{6}$";
    private MyHandler mHandler;
    private boolean live = true;
    private boolean FlagBind = false;
    private BindWXBroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_wx);

        initWhiteTitle("绑定提现账号");
        initWx();
        initReceiver();
        initView();

    }

    private void initWx(){
        api = WXAPIFactory.createWXAPI(this, Contants.AppCfg.WX__APPID, true);
        api.registerApp(Contants.AppCfg.WX__APPID);
    }

    private void initView(){

        //视图设置
        bind_wx = (TextView)findViewById(R.id.tv_bind_wx);
        bind = (Button)findViewById(R.id.btn_bind);
        pin = (TextView)findViewById(R.id.tv_pin);
        et_pin = (EditText)findViewById(R.id.et_pin);

        //监听事件
        bind_wx.setOnClickListener(this);
        bind.setOnClickListener(this);
        pin.setOnClickListener(this);

        //提示用户绑定的手机号
        TextView tv_hint_num = (TextView)findViewById(R.id.tv_phone_num_hint);
        String temp = "";
        try {
            temp = ZYApplication.PhoneNum.substring(0,3) + "****" +
                    ZYApplication.PhoneNum.substring(ZYApplication.PhoneNum.length()-4,
                            ZYApplication.PhoneNum.length());
            Typeface tf = Typeface.createFromAsset(this.getAssets(), "font/BEBASNEUE.OTF");
            tv_hint_num.setTypeface(tf);
        }catch (Exception e){

        }
        tv_hint_num.setText("您绑定的手机号是：" + temp);

    }

    private void initReceiver(){
        try {
            receiver = new BindWXBroadcastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.setPriority(998);
            filter.addAction("android.provider.Telephony.SMS_RECEIVED");
            registerReceiver(receiver, filter);
            this.registerReceiver(receiver, filter);
        }catch (Exception e){

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_bind_wx:
                bind_wx.setClickable(false);
                WXLogin();
                break;
            case R.id.btn_bind:
                toBind();
                break;
            case R.id.tv_pin:
                toPin();
                break;
        }
    }

    /***
     * 微信登录
     */
    private void WXLogin() {
        try {
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = Contants.AppCfg.WX__SCOPE;
            req.state = Contants.AppCfg.WX__STATE;
            api.sendReq(req);
            if (dialog == null) {
                dialog = new ProgressDialog(this);
            }
            dialog.setCancelable(false);
            dialog.setMessage("正在拉取授权");
            dialog.show();
        }catch (Exception e){
            bind_wx.setClickable(true);
            ExceptionUtils.ExceptionToUM(e, this, "WXLogin");
        }
    }

    /**
     * 获取验证码
     */
    private void toPin(){
        //判断用户是否已获取微信授权
        if (!FlagBind){
            Toast.makeText(this,getString(R.string.take_cash_hint_no_bind),Toast.LENGTH_SHORT).show();
            return;
        }
        //获取验证码和手机号
        type = "5";
        pin.setClickable(false);
        pin.setSelected(true);
        setPinHint();
        Factory.postPhp(this, Const.PMobile);

    }

    /**
     * 绑定微信账号
     */
    private void toBind(){
        //判断用户是否已获取微信授权
        if (!FlagBind){
            Toast.makeText(this,getString(R.string.take_cash_hint_no_bind),Toast.LENGTH_SHORT).show();
            return;
        }
        //判断是否获取验证码
         if (TextUtils.isEmpty(ZYApplication.PhoneNum)){
            Toast.makeText(this,"请获取验证码",Toast.LENGTH_SHORT).show();
            return;
        }
        //判断验证码是否符合规则
        String pin_input = et_pin.getText().toString();
        if(TextUtils.isEmpty(pin_input)) {
            Toast.makeText(this,"验证码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isPin(pin_input)){
            Toast.makeText(this,"验证码为6位数字",Toast.LENGTH_SHORT).show();
            return;
        }
        mPin = pin_input;
        type = "6";
        bind.setClickable(false);
        Factory.postPhp(this, Const.PBindWX);
    }

    private boolean isPin(String str){
        Pattern p = Pattern.compile(REGEX_PIN);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dialog!=null){
            dialog.dismiss();
        }
        if (bind_wx!=null){
            bind_wx.setClickable(true);
        }
        if (ZYApplication.FlagWXLogin){
            code = ZYApplication.CodeWXLogin;
            ZYApplication.FlagWXLogin = false;
            new LoginWXPresenter(this).getData();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (dialog!=null){
            dialog.dismiss();
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String,String> map = null;
        if (tag == 1) {
            map = new HashMap<>();
            map.put(Contants.appid, Contants.AppCfg.WX__APPID);
            map.put(Contants.secret, Contants.AppCfg.WX__SECRET);
            map.put(Contants.code, code);
            map.put(Contants.grant_type, Contants.AppCfg.WX__GRANT_TYPE);
        } else if (tag == Const.PMobile){
            map = new HashMap<>();
            map.put("phone",ZYApplication.PhoneNum);
        } else if (tag == Const.PBindWX){
            map = new HashMap<>();
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
            map.put("BindOpenID",OpenID);
            map.put("WXNickName",WXName);
            map.put("Mobile",ZYApplication.PhoneNum);
            map.put("VerifyCode",mPin);
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = new String[]{};
        if (flag == Const.PMobile){
            if ("5".equals(type)){
                args = new String[]{
                    Contants.InfoID, UserinfoData.getInfoID(this),
                    "MobileType",type
                };
            }else if("6".equals(type)){
                args = new String[]{
                        Contants.InfoID, UserinfoData.getInfoID(this),
                        "BindOpenID",OpenID,
                        "WXNickName",WXName,
                        "Mobile",mobile,
                        "VerifyCode",mPin,
                        "MobileType",type
                };
            }
        }else if (flag == Const.EGetPatientDetail){
            args = new String[]{
                    Contants.InfoID, UserinfoData.getInfoID(this)
            };
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object t, int tag) {

        if (tag == Const.GUIDE_GET_USER_INFO_WX){
            UserInfoWXBean user_info_wx = (UserInfoWXBean)t;
            if (user_info_wx!=null&&user_info_wx.getOpenid()!=null) {
                    OpenID = user_info_wx.getOpenid();
                    WXName = user_info_wx.getNickname();
                    bind_wx.setText("微信授权成功");
                    FlagBind = true;
                }else {
                    showError(1);
                }
        } else if (tag == Const.EMobile){
            bind.setClickable(true);
            BindMobileEntity entity = (BindMobileEntity)t;
            if(Const.RESULT.equals(entity.getiResult())){
                if("5".equals(type)){
                    mobile = entity.getMobile();
                    Toast.makeText(this,"验证码已发送，请注意查收",Toast.LENGTH_SHORT).show();
                }else if("6".equals(type)){
                    Toast.makeText(this,"绑定微信账号成功",Toast.LENGTH_SHORT).show();
                    finish();
//                    Factory.post(this,Const.EGetPatientDetail);
                }
            }else {
                String[] args = UiUtils.getErrorMsg(entity,type,this);
                String msg = args[0];
                if(!TextUtils.isEmpty(msg))
                    Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
                String other = args[1];
                if (!TextUtils.isEmpty(other)) tills = 1;
            }
        } else if(tag == Const.EGetPatientDetail){
            PatientDetailBean bean = (PatientDetailBean)t;
            if ("0".equals(bean.iResult)){
                LogCustom.i("ZYS", "bean:"+bean.getBindOpenID());
            }
        }else if(tag == Const.PMobile){
            PhpUserInfoBean bean = (PhpUserInfoBean) t;
            if (Const.RESULT.equals(bean.getiResult())){
                Toast.makeText(this,"验证码已发送，请注意查收",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"验证码请求失败，请稍候重试",Toast.LENGTH_SHORT).show();
            }
        }else if(tag == Const.PBindWX){
            bind.setClickable(true);
            PhpUserInfoBean bean = (PhpUserInfoBean) t;
            if (Const.RESULT.equals(bean.getiResult())){
                Toast.makeText(this,"绑定微信账号成功",Toast.LENGTH_SHORT).show();
                finish();
            }else if ("11".equals(bean.getiResult())){
                Toast.makeText(this,"验证码错误",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"未知错误，错误码100"+bean.getiResult(),Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {
        if (dialog!=null){
            dialog.dismiss();
        }
    }

    @Override
    public void showError(int tag) {
        if (dialog != null) {
            dialog.dismiss();
        }
        if (tag == 1||tag == Const.GUIDE_GET_USER_INFO_WX){
            bind_wx.setClickable(true);
            Toast.makeText(this,"获取授权失败，请重试。",Toast.LENGTH_SHORT).show();
        }else if(tag == Const.EMobile){
            if ("6".equals(type))  bind.setClickable(true);
        }
    }

    private Runnable mPinRunnable = new Runnable() {
        @Override
        public void run() {
            if(live){
                tills--;
                if(tills>0){
                    mHandler.sendEmptyMessage(UPDATA_TIME);
                    mHandler.postDelayed(mPinRunnable,1000);
                }else if (tills==0){
                    mHandler.sendEmptyMessage(UPDATA_STOP);
                }
            }
        }
    };

    private void setPinHint(){
        if (mHandler==null){
            mHandler = new MyHandler(this);
        }
        tills = 60;
        mHandler.post(mPinRunnable);
    }

    private int tills = 60;
    private void updateTime(int flag){
        if(flag == UPDATA_TIME){
            pin.setText("重新发送("+tills+"秒)");
        }else {
            pin.setClickable(true);
            pin.setSelected(false);
            pin.setText("获取验证码");
        }
    }

    private static final int UPDATA_TIME = 1;
    private static final int UPDATA_STOP = 2;
    private static class MyHandler extends Handler {
        private final WeakReference<BindWXActivity> mActivity;
        public MyHandler(BindWXActivity activity){
            mActivity = new WeakReference<>(activity);
        }
        private BindWXActivity activity;
        @Override
        public void handleMessage(Message msg) {
            activity = mActivity.get();
            if(msg.what == UPDATA_TIME){
                activity.updateTime(UPDATA_TIME);
            }else if(msg.what == UPDATA_STOP){
                activity.updateTime(UPDATA_STOP);
            }
        }

    }

    class BindWXBroadcastReceiver extends BroadcastReceiver {
        public BindWXBroadcastReceiver(){}
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                LogCustom.i("ZYS", "onReceive1");
                if (intent == null ) return;
                LogCustom.i("ZYS", "onReceive");
                Bundle bundle = intent.getExtras();
                Object[] objects = (Object[]) bundle.get("pdus");
                if (objects == null) return;
                for(Object obj : objects){
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                    String body = smsMessage.getDisplayMessageBody();
                    String address = smsMessage.getDisplayOriginatingAddress();
                    long date = smsMessage.getTimestampMillis();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String dateStr = format.format(date);
                    LogCustom.i("ZYS", address + " 于  " + dateStr + "给你发了以下内容: " + body);
                    if(address.endsWith("12062")){
                        String temp = body.substring(body.length()-6,body.length());
                        if (isPin(temp)){
                            et_pin.setText(temp);
                            LogCustom.i("ZYS","temp:"+temp);
                        }
                    }
                }
            }catch (Exception e){
                ExceptionUtils.ExceptionToUM(e,BindWXActivity.this,"BindWXBroadcastReceiver");
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog!=null){
            dialog.dismiss();
        }
        live = false;
        if(receiver!=null){
            this.unregisterReceiver(receiver);
        }
    }
}

package com.zeyuan.kyq.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.Entity.CreateUserParamsEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.AppManager;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/8/22.
 * <p>
 * 绑定手机页面
 *
 * @author wwei
 */
public class BindingPhoneActivity extends BaseActivity implements View.OnClickListener, HttpResponseInterface {

    private static final String TAG = "BindingPhoneActivity";

    private static final int UPDATA_TIME = 1;
    private static final int UPDATA_STOP = 2;

    private static final String REGEX_PHONE = "^[1][0-9]{10}$";
    private static final String REGEX_PIN = "^[0-9]{6}$";
    private TextView tv_local_phone;
    private EditText et1;
    private EditText et2;
    private TextView pin;
    private TextView next;
    private TextView tv_name;
    private TextView tv_login;
    private View v_change;
    private MsgBroadcastReceiver receiver;
    private MyHandler mHandler;
    private boolean live = true;
    private int tills = 60;
    private boolean cancle = false;

    private String mOpenID = null;
    private String mUnionID = null;
    private String lt = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_phone);
        AppManager.getAppManager().addActivity(this);
//        cancle = getIntent().getBooleanExtra(Const.INTENT_BIND_CANCLE,true);
        mOpenID = getIntent().getStringExtra(Contants.OpenID);
        mUnionID = getIntent().getStringExtra(Contants.UnionID);
        lt = UserinfoData.getLoginType(this);
        initWhiteTitle("绑定手机号");
        initReceiver();
        initView();
    }

    private void initBindView() {

        TextView tv = (TextView) findViewById(R.id.tv_bind_num);
        tv.setText("您绑定的手机号是：\n" + ZYApplication.PhoneNum);
        findViewById(R.id.v_bind).setVisibility(View.GONE);

    }

    private void initReceiver() {
        receiver = new MsgBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(997);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(receiver, filter);
        this.registerReceiver(receiver, filter);
    }

    private boolean numFlag = false;
    private boolean pinFlag = false;

    private void initView() {

        findViewById(R.id.iv_white_title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BindingPhoneActivity.this.finish();
            }
        });
        tv_local_phone = (TextView) findViewById(R.id.tv_local_phone);
        et1 = (EditText) findViewById(R.id.et_num);
        et2 = (EditText) findViewById(R.id.et_pin);
        pin = (TextView) findViewById(R.id.tv_pin);
        next = (TextView) findViewById(R.id.tv_next);
        tv_name = (TextView) findViewById(R.id.tv_name);
        String name = UserinfoData.getInfoname(this);
        if (TextUtils.isEmpty(name)) name = "";
        tv_name.setText(name);
        tv_login = (TextView) findViewById(R.id.tv_login);
        String lt = UserinfoData.getLoginType(this);
        if (TextUtils.isEmpty(lt)) {
            tv_login.setText("");
        } else {
            if ("1".equals(lt)) {
                tv_login.setText("(微信登录)");
            } else {
                tv_login.setText("(QQ登录)");
            }
        }
        v_change = findViewById(R.id.v_change);
        tv_local_phone.setOnClickListener(this);
        v_change.setOnClickListener(this);
        pin.setOnClickListener(this);
        next.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_local_phone:
                startActivityForResult(new Intent(this, SelectPhoneLocalActivity.class), 1);
                break;
            case R.id.tv_pin:
                getVerificationCode();
                break;
            case R.id.tv_next:
                toBind();
                break;

            case R.id.v_change:
                /*startActivity(new Intent(this,GuideActivity.class));
                UserinfoData.clearMermory(this);
                Const.InfoID = null;
                force = true;
                AppManager.getAppManager().finishAllActivity();*/
                finish();
                break;
        }

    }

    String local_code = "86";//国家代号

    private void toBind() {

        String NNum = et1.getText().toString().trim();
        if (TextUtils.isEmpty(NNum)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (local_code.equals("86") && !isPhoneNum(NNum)) {
            Toast.makeText(this, "手机号格式错误", Toast.LENGTH_SHORT).show();
            return;
        }

        phoneNum = NNum;

        if (TextUtils.isEmpty(phoneNum)) {
            Toast.makeText(this, "请输入手机号，并获取验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isPhoneNum(phoneNum)) {
            Toast.makeText(this, "手机号格式错误", Toast.LENGTH_SHORT).show();
            return;
        }
        String mNum = et2.getText().toString().trim();
        if (TextUtils.isEmpty(mNum)) {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isPin(mNum)) {
            Toast.makeText(this, "验证码为6位数字", Toast.LENGTH_SHORT).show();
            return;
        }
        type = "4";
        next.setClickable(false);
        mPin = mNum;
        Factory.postPhp(this, Const.PCheckInfoPin);
    }

    //检查手机号并获取验证码
    private void getVerificationCode() {
        String mNum = et1.getText().toString().trim();
        if (TextUtils.isEmpty(mNum)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (local_code.equals("86") && !isPhoneNum(mNum)) {
            Toast.makeText(this, "手机号格式错误", Toast.LENGTH_SHORT).show();
            return;
        }
        type = "2";
        pin.setClickable(false);
        pin.setSelected(true);
        setPinHint();
        phoneNum = mNum;
        Factory.postPhp(this, Const.PMobile);
    }

    private boolean isPhoneNum(String str) {
        Pattern p = Pattern.compile(REGEX_PHONE);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    private boolean isPin(String str) {
        Pattern p = Pattern.compile(REGEX_PIN);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    private void setPinHint() {
        if (mHandler == null) {
            mHandler = new MyHandler(this);
        }
        tills = 60;
        mHandler.post(mPinRunnable);
    }

    private void updataTime(int flag) {
        if (flag == UPDATA_TIME) {
            pin.setText("重新发送(" + tills + "秒)");
        } else {
            pin.setClickable(true);
            pin.setSelected(false);
            pin.setText("获取验证码");
        }
    }

    private Runnable mPinRunnable = new Runnable() {
        @Override
        public void run() {
            if (live) {
                tills--;
                if (tills > 0) {
                    mHandler.sendEmptyMessage(UPDATA_TIME);
                    mHandler.postDelayed(mPinRunnable, 1000);
                } else if (tills == 0) {
                    mHandler.sendEmptyMessage(UPDATA_STOP);
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String local_code = data.getStringExtra("local_code");
                    if (!TextUtils.isEmpty(local_code)) {
                        tv_local_phone.setText("+" + local_code + "");
                        this.local_code = local_code;
                    }

                }
                break;
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == Const.PMobile) {
            if (TextUtils.isEmpty(local_code) || local_code.equals("86")) {//大陆不加国际区号
                map.put("phone", phoneNum);
            } else {
                map.put("phone", "00" + local_code + phoneNum);
            }

        } else if (tag == Const.PCheckInfoPin) {
            map.put("openid", mOpenID);
            map.put("apptype", "2");
            map.put("loginType", lt);
            map.put("phone", phoneNum);
            if ("1".equals(lt)) {
                map.put("unid", mUnionID);
            }
            map.put("VerifyCode", mPin);
        }
        return map;
    }

    String type = "2";
    String mPin = "";
    String phoneNum = "";

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if ("2".equals(type)) {
            args = new String[]{
                    Contants.InfoID, UserinfoData.getInfoID(this),
                    "Mobile", phoneNum,
                    "MobileType", type
            };
        } else if ("4".equals(type)) {
            args = new String[]{
                    Contants.InfoID, UserinfoData.getInfoID(this),
                    "Mobile", phoneNum,
                    "VerifyCode", mPin,
                    "MobileType", type
            };
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PMobile) {
            PhpUserInfoBean bean = (PhpUserInfoBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                Toast.makeText(this, "验证码已发送，请注意查收", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "验证码请求失败，请稍候重试", Toast.LENGTH_SHORT).show();
            }
        } else if (flag == Const.PCheckInfoPin) {
            PhpUserInfoBean bean = (PhpUserInfoBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                cancle = true;
                String InfoID = bean.getInfoID();
                if (!TextUtils.isEmpty(InfoID)) {
                    UserinfoData.saveInfoID(this, bean.getInfoID());
                    Const.InfoID = bean.getInfoID();
                    UserinfoData.saveIsHaveCreateInfo21(this, "1");
                    Toast.makeText(this, "绑定成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BindingPhoneActivity.this, MainActivity.class));//如果有就去mainactivity
                    force = true;//退出页面
                    finish();
                } else {
                    Toast.makeText(this, "请创建档案", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(BindingPhoneActivity.this, PatientInfoActivity.class)
//                            .putExtra("phone", phoneNum)
//                            .putExtra(Contants.OpenID, mOpenID)
//                            .putExtra(Contants.UnionID, mUnionID));//如果有就去mainactivity
                    UserinfoData.savePhoneNum(getApplicationContext(), phoneNum);
                    CreateUserParamsEntity paramsEntity = new CreateUserParamsEntity();
                    paramsEntity.setmOpenID(mOpenID);
                    paramsEntity.setmUnionID(mUnionID);
                    paramsEntity.setPhone(phoneNum);
                    startActivity(new Intent(BindingPhoneActivity.this, SelectCurrentStatusActivity.class).putExtra("createUserParamsEntity", paramsEntity));
                    force = true;//退出页面
                    finish();
                }
            } else {
                next.setClickable(true);
                if ("11".equals(bean.getiResult())) {
                    Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
                } else if ("20".equals(bean.getiResult())) {
                    showErrorMsg(20);
                } else if ("21".equals(bean.getiResult())) {
                    showErrorMsg(21);
                } else {
                    Toast.makeText(this, "信息验证失败，请重试", Toast.LENGTH_SHORT).show();
                }
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
        if (flag == Const.PMobile) {
            Toast.makeText(this, "验证码请求失败，请稍候重试", Toast.LENGTH_SHORT).show();
        } else if (flag == Const.PCheckInfoPin) {
            next.setClickable(true);
            Toast.makeText(this, "请求失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        live = false;
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
    }

    private void showErrorMsg(int flag) {
        try {
            ZYDialog.Builder builder = new ZYDialog.Builder(this);
            builder.setTitle("提示");
            if (flag == 20) {
                builder.setMessage("该手机号已绑定其他微信，请输入其他手机号码");
            } else if (flag == 21) {
                builder.setMessage("该手机号已绑定其他QQ，请输入其他手机号码");
            } else {
                builder.setMessage("该账号已绑定其他账号，请输入其他手机号码");
            }
            builder.disMissLine(true);
            builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();

        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "showErrorMsg");
        }
    }

    class MsgBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) return;
            /*String action = intent.getAction();
            if (TextUtils.isEmpty(action)) return;*/
            LogCustom.i("ZYS", "onReceive");
            Bundle bundle = intent.getExtras();
            Object[] objects = (Object[]) bundle.get("pdus");
            for (Object obj : objects) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String body = smsMessage.getDisplayMessageBody();
                String address = smsMessage.getDisplayOriginatingAddress();
                long date = smsMessage.getTimestampMillis();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String dateStr = format.format(date);
                LogCustom.i("ZYS", address + " 于  " + dateStr + "给你发了以下内容: " + body);
                if (address.endsWith("12062")) {
                    String temp = body.substring(body.length() - 6, body.length());
                    if (isPin(temp)) {
                        et2.setText(temp);
                        LogCustom.i("ZYS", "temp:" + temp);
                    }
                }
            }
        }
    }


    private static class MyHandler extends Handler {
        private final WeakReference<BindingPhoneActivity> mActivity;

        public MyHandler(BindingPhoneActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        private BindingPhoneActivity activity;

        @Override
        public void handleMessage(Message msg) {
            activity = mActivity.get();
            if (msg.what == UPDATA_TIME) {
                activity.updataTime(UPDATA_TIME);
            } else if (msg.what == UPDATA_STOP) {
                activity.updataTime(UPDATA_STOP);
            }
        }
    }

    private boolean force = false;

    @Override
    public void finish() {
        /*if(!cancle&&!force){
            Toast.makeText(this,"你尚未绑定手机，请先绑定。",Toast.LENGTH_SHORT).show();
        }else {
            super.finish();
        }*/
        if (!force) {
            startActivity(new Intent(this, GuideActivity.class));
        }
        super.finish();
    }
}

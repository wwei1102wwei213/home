package com.zeyuan.kyq.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.UserinfoData;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/3.
 * 确定修改手机号页面
 */

public class ModifyPhoneConfirmActivity extends BaseActivity implements HttpResponseInterface, View.OnClickListener {
    String phone_old;
    String phone_new;
    String local_code_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone_confirm);
        phone_old = getIntent().getStringExtra("phone_old");
        phone_new = getIntent().getStringExtra("phone_new");
        local_code_new = getIntent().getStringExtra("local_code_new");
        if (TextUtils.isEmpty(phone_old) || TextUtils.isEmpty(phone_new)) {
            finish();
            return;
        }
        initView();
        Factory.postPhp(this, Const.PMobile);
    }

    TextView tv_re_send;
    TextView tv_confirm;
    EditText et_input_pin_number;
    TextView tv_send_pin_hint;
    TextView tv_to_bind_phone;

    private void initView() {
        initTitlebar("修改手机号码");
        tv_to_bind_phone = (TextView) findViewById(R.id.tv_to_bind_phone);
        tv_send_pin_hint = (TextView) findViewById(R.id.tv_send_pin_hint);
        et_input_pin_number = (EditText) findViewById(R.id.et_input_pin_number);

        tv_re_send = (TextView) findViewById(R.id.tv_re_send);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        tv_re_send.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_re_send:
                Factory.postPhp(this, Const.PMobile);
                tv_re_send.setEnabled(false);
                break;
            case R.id.tv_confirm:
                if (checkPin()) {
                    Factory.postPhp(this, Const.PApi_Edit_Moblie);
                    tv_confirm.setEnabled(false);
                }
                break;
        }
    }

    String type = "2";


    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == Const.PMobile) {
            if (TextUtils.isEmpty(local_code_new) || local_code_new.equals("86")) {//大陆不加国际区号
                map.put("phone", phone_new);
            } else {
                map.put("phone", "00" + local_code_new + phone_new);
            }

        } else if (tag == Const.PApi_Edit_Moblie) {
            map.put("InfoID", UserinfoData.getInfoID(getApplicationContext()));
            map.put("VerifyCode", mPin);
            map.put("newphone", phone_new);
            map.put("oldphone", phone_old);

        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PMobile) {
            PhpUserInfoBean bean = (PhpUserInfoBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                tv_send_pin_hint.setText("验证码已发送至新手机: ");
                tv_to_bind_phone.setText(phone_new);
                setPinHint();
            } else {
                tv_re_send.setEnabled(true);
                tv_send_pin_hint.setText("验证码请求失败，请稍候重试");
                tv_to_bind_phone.setText("");
                Toast.makeText(this, "验证码请求失败，请稍候重试", Toast.LENGTH_SHORT).show();
            }
        } else if (flag == Const.PApi_Edit_Moblie) {
            tv_confirm.setEnabled(true);
            PhpUserInfoBean phpUserInfoBean = (PhpUserInfoBean) response;
            if (phpUserInfoBean.getiResult().equals(Const.RESULT)) {
                Toast.makeText(this, "手机号码修改成功！", Toast.LENGTH_SHORT).show();
                UserinfoData.savePhoneNum(getApplicationContext(), phone_new);
                ZYApplication.PhoneNum = phone_new;
                setResult(RESULT_OK);
            } else {
                Toast.makeText(this, "手机号码修改失败！", Toast.LENGTH_SHORT).show();
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

    private int tills = 60;
    private MyHandler mHandler;

    //启动重发倒计时
    private void setPinHint() {
        if (mHandler == null) {
            mHandler = new MyHandler(this);
        }
        tills = 60;
        mHandler.post(mPinRunnable);

    }

    private void updataTime(int flag) {
        if (flag == UPDATA_TIME) {
            tv_re_send.setText("重新发送(" + tills + "秒)");
        } else {
            tv_re_send.setEnabled(true);
            tv_re_send.setSelected(false);
            tv_re_send.setText("获取验证码");
        }
    }

    private boolean live = true;
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
    private static final int UPDATA_TIME = 1;
    private static final int UPDATA_STOP = 2;

    private static class MyHandler extends Handler {
        private final WeakReference<ModifyPhoneConfirmActivity> mActivity;

        public MyHandler(ModifyPhoneConfirmActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        private ModifyPhoneConfirmActivity activity;

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


    //验证码
    String mPin = "";

    //检查验证码
    private boolean checkPin() {
        mPin = et_input_pin_number.getText().toString().trim();
        if (TextUtils.isEmpty(mPin)) {
            showToast("验证码不能为空！");
            return false;
        }
        return true;

    }

    @Override
    protected void onDestroy() {
        live = false;
        super.onDestroy();
    }
}

package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/8/1.
 * 修改手机号码获取验证码
 */

public class ModifyPhoneGetCodeActivity extends BaseActivity implements View.OnClickListener {
    private static final String REGEX_PHONE = "^[1][0-9]{10}$";
    private static final String REGEX_PIN = "^[0-9]{6}$";
    private TextView tv_local_phone_old;
    private TextView tv_local_phone_new;
    private EditText et_phone_old;
    private EditText et_phone_new;
    private TextView tv_pin;
    //老手机号码的国际代号，和新手机号码的国际代号
    private String local_code_old = "86", local_code_new = "86";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone_getcode);
        initView();
    }

    public void initView() {
        initTitlebar("修改手机号码");
        tv_local_phone_old = (TextView) findViewById(R.id.tv_local_phone_old);
        tv_local_phone_new = (TextView) findViewById(R.id.tv_local_phone_new);
        et_phone_old = (EditText) findViewById(R.id.et_phone_old);
        et_phone_new = (EditText) findViewById(R.id.et_phone_new);
        tv_pin = (TextView) findViewById(R.id.tv_pin);
        tv_local_phone_new.setOnClickListener(this);
        tv_local_phone_old.setOnClickListener(this);
        tv_pin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_local_phone_old:
                startActivityForResult(new Intent(this, SelectPhoneLocalActivity.class), 1);
                break;
            case R.id.tv_local_phone_new:
                startActivityForResult(new Intent(this, SelectPhoneLocalActivity.class), 2);
                break;
            case R.id.tv_pin:
                getCode();
                break;

        }
    }

    private void getCode() {
        if (checkPhoneNumber()) {
            startActivityForResult(new Intent(this, ModifyPhoneConfirmActivity.class).putExtra("phone_old", phone_old).putExtra("phone_new", phone_new).putExtra("local_code_new", local_code_new), 8);
        }
    }

    //用户填写的旧号码
    String phone_old;
    //用户填写的新号码
    String phone_new;

    //检查输入的手机号码
    private boolean checkPhoneNumber() {
        //老号码
        String mNum_old = et_phone_old.getText().toString().trim();
        if (TextUtils.isEmpty(mNum_old)) {
            Toast.makeText(this, "之前绑定的手机号不能为空", Toast.LENGTH_SHORT).show();
            et_phone_old.setSelection(0);
            return false;
        }
        //如果是大陆的号码，验证号码格式
        if (local_code_old.equals("86") && !isPhoneNum(mNum_old)) {
            Toast.makeText(this, "之前绑定的手机号格式错误", Toast.LENGTH_SHORT).show();
            et_phone_old.setSelection(et_phone_old.getText().length());
            return false;
        }
        //判断输入的老号码是否正确
        if (!mNum_old.equals(UserinfoData.getPhoneNum(getApplicationContext()))) {
            Toast.makeText(this, "之前绑定的手机号输入错误", Toast.LENGTH_SHORT).show();
            et_phone_old.setSelection(et_phone_old.getText().length());
            return false;
        }
        phone_old = mNum_old;
        //新号码
        String mNum_new = et_phone_new.getText().toString().trim();
        if (TextUtils.isEmpty(mNum_new)) {
            Toast.makeText(this, "新的手机号不能为空", Toast.LENGTH_SHORT).show();
            et_phone_new.setSelection(0);
            return false;
        }
        //如果是大陆的号码，验证号码格式
        if (local_code_new.equals("86") && !isPhoneNum(mNum_new)) {
            Toast.makeText(this, "新的手机号格式错误", Toast.LENGTH_SHORT).show();
            et_phone_new.setSelection(et_phone_new.getText().length());
            return false;
        }
        if (mNum_new.equals(phone_old)){
            showToast("新旧手机号码不能相同！");
            return false;
        }
        phone_new = mNum_new;
        return true;
    }

    private boolean isPhoneNum(String str) {
        Pattern p = Pattern.compile(REGEX_PHONE);
        Matcher m = p.matcher(str);
        return m.matches();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String local_code = data.getStringExtra("local_code");
                    if (!TextUtils.isEmpty(local_code)) {
                        tv_local_phone_old.setText("+" + local_code + "");
                        this.local_code_old = local_code;
                    }

                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    String local_code = data.getStringExtra("local_code");
                    if (!TextUtils.isEmpty(local_code)) {
                        tv_local_phone_new.setText("+" + local_code + "");
                        this.local_code_new = local_code;
                    }

                }
                break;
            case 8:
                if (resultCode == RESULT_OK) {
                    setResult(8);
                    finish();
                }

        }
    }

}

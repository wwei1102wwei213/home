package com.zeyuan.kyq.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.PatientDetailBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.HttpUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.Secret.MovingSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/9.
 *
 * 提现页面
 *
 * @author wwei
 */
public class DrawCashActivity extends BaseActivity implements View.OnClickListener
            ,HttpResponseInterface,HttpUtils.ReqResult{

    private Button cash;//提现按钮
    private TextView change;//修改绑定微信
    private Typeface tf;
    private TextView cash_all;//显示总余额
    private TextView cash_ok;//显示可提现
    private EditText et;//提现输入框
    private TextView tv_name;//显示微信钱包
    private double TP;//总余额
    private double WD;//可用余额
    private boolean FlagCash = false;

    /** 输入框小数的位数*/
    private static final int DECIMAL_DIGITS = 2;
    /**
     *  设置小数位数控制
     */
    private InputFilter lengthfilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            // 删除等特殊字符，直接返回
            if ("".equals(source.toString())) {
                return null;
            }
            String dValue = dest.toString();
            String[] splitArray = dValue.split(".");
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                int diff = dotValue.length() + 1 - DECIMAL_DIGITS;
                if (diff > 0) {
                    return source.subSequence(start, end - diff);
                }
            }
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_cash);

        initWhiteTitle("提现");
        initView();

    }

    /***
     *
     * 设置视图
     */
    private void initView(){

        tv_name = (TextView)findViewById(R.id.tv_name_wx);
        cash = (Button)findViewById(R.id.btn_cash);
        change = (TextView)findViewById(R.id.tv_change_bind_wx);
        cash_all = (TextView)findViewById(R.id.tv_cash_all);
        cash_ok = (TextView)findViewById(R.id.tv_cash_ok);
        et = (EditText)findViewById(R.id.et_cash);
        et.setFilters(new InputFilter[]{lengthfilter});
        change.setOnClickListener(this);
        cash.setOnClickListener(this);

        //设置字体样式
        try {
            tf = Typeface.createFromAsset(this.getAssets(),"font/BEBASNEUE.OTF");
            et.setTypeface(tf);
            cash_all.setTypeface(tf);
            cash_ok.setTypeface(tf);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"设置字体样式出错");
        }
    }

    /***
     *
     * 设置数据
     */
    private void initData(){
        Factory.post(this, Const.EGetPatientDetail);
    }

    /***
     *
     * 点击事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_change_bind_wx:
                startActivity(new Intent(this,BindWXActivity.class));
                break;
            case R.id.btn_cash:
//                startActivity(new Intent(this, CashSuccessActivity.class));
                toCash();
                break;
        }
    }


    private String cashNum;//提现数量
    private String OpenID;//绑定微信
    private String WXName;//微信昵称
    /***
     *
     * 提现处理
     */
    private void toCash(){
        if (OpenID==null||"".equals(OpenID)) {
            Toast.makeText(this,getString(R.string.take_cash_hint_no_bind),Toast.LENGTH_SHORT).show();
            return;
        }

        double temp = 0;
        try {
            if (TextUtils.isEmpty(cashMax)) cashMax = "0";
            temp = Double.valueOf(cashMax);
        }catch (Exception e){
            temp = 0;
        }

        if (temp < 20){
            Toast.makeText(this,"账户余额小于20不能提现",Toast.LENGTH_SHORT).show();
            return;
        }

        cashNum = et.getText().toString();
        if (TextUtils.isEmpty(cashNum)){
            Toast.makeText(this,getString(R.string.take_cash_hint_no_cash),Toast.LENGTH_SHORT).show();
            return;
        }
        double num = Double.valueOf(cashNum.trim());
        if (num<20){
            Toast.makeText(this,getString(R.string.take_case_min_num_10),Toast.LENGTH_SHORT).show();
            return;
        }

        if (num>temp){
            Toast.makeText(this,"您的最高可提现金额为："+temp+"元",Toast.LENGTH_SHORT).show();
            return;
        }
        if (num>2000){
            Toast.makeText(this,getString(R.string.take_case_max_num_2000),Toast.LENGTH_SHORT).show();
            return;
        }
        cash.setClickable(false);
        showHint();
        int n = (int)(num*100);
        cashNum = n+"";
        Map<String,String> map = new HashMap<>();
        map.put("InfoID",UserinfoData.getInfoID(this));
        map.put("openid",OpenID);
        map.put("price", cashNum);
        HttpUtils.postSomething(map,this);
    }

    private ProgressDialog dialog;
    private void showHint(){
        if (dialog == null) {
            dialog = new ProgressDialog(this);
        }
        dialog.setMessage("正在提现，请稍候");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void cancleHint(){
        if (dialog!=null){
            dialog.dismiss();
        }
    }

    private void setWXView(String id ,String name){

        if (TextUtils.isEmpty(id)) {
            tv_name.setText("");
            change.setText("绑定");
        } else {
            OpenID = id;
            change.setText("修改");
            if (TextUtils.isEmpty(name)){
                tv_name.setText("微信钱包");
            }else {
                tv_name.setText("微信钱包("+name+")");
            }
        }

    }

    private String cashMax = "0";
    private void setCashView(int[] key,String wd,String tp){
        //验证密钥
        if (key==null||key.length!=4){
            cash_ok.setText("查询失败");
            cash_all.setText("查询失败");
        }else {
            MovingSecretUtils.TEA.setKey(key);
            if (TextUtils.isEmpty(wd)){
                cash_ok.setText("0.00");
            }else {
                if ("0,".equals(wd)){
                    cashMax = "0.00";
                    cash_ok.setText(cashMax);
                }else {
                    if (wd.contains(",")){
                        String[] args = wd.split(",");
                        String temp = args[1];
                        LogCustom.i("ZYS","temp:"+temp);
                        if (TextUtils.isEmpty(temp)){
                            cashMax = "0.00";
                            cash_ok.setText(cashMax);
                        }else {
                            temp = MovingSecretUtils.TEA.decryptByTea(temp);
                            cashMax = temp;
                            cash_ok.setText(temp);
                        }

                    } else {
                        cash_ok.setText("0.00");
                    }
                }
                if ("0,".equals(tp)){
                    cash_all.setText("0.00");
                }else {
                    if (tp.contains(",")){
                        String[] args = tp.split(",");
                        String temp = args[1];
                        LogCustom.i("ZYS","temp:"+temp);
                        if (TextUtils.isEmpty(temp)){
                            cash_all.setText("0.00");
                        }else {
                            temp = MovingSecretUtils.TEA.decryptByTea(temp);
                            cash_all.setText(temp);
                        }
                    } else {
                        cash_all.setText("0.00");
                    }
                }


            }
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        return null;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = new String[]{};
        if (flag == Const.EGetPatientDetail){
            args = new String[]{
                 Contants.InfoID,UserinfoData.getInfoID(this)
            };
        }else if (flag == Const.EGetInfoCash){
            args = new String[]{
                 Contants.InfoID,UserinfoData.getInfoID(this),
                    "openid",OpenID,
                    "price",cashNum
            };
            return HttpSecretUtils.getParamString(args,false);
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object response, int flag) {

        if (flag==Const.EGetPatientDetail){
            PatientDetailBean bean = (PatientDetailBean)response;
            if (Const.RESULT.equals(bean.iResult)){
                String id = bean.getBindOpenID();
                String name = bean.getWXNickName();
                int[] key = bean.getK();
                String wd = bean.getWD();
                String tp = bean.getTP();
                setWXView(id,name);
                setCashView(key,wd,tp);
            }
        }else if(flag == Const.EGetInfoCash){
            LogCustom.i("ZYS", "rep:" + response.toString());

//            Toast.makeText(this,response.toString(),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void getReq(final String req) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cash.setClickable(true);
                cancleHint();
                if (TextUtils.isEmpty(req)){
                    Toast.makeText(DrawCashActivity.this,getString(R.string.take_case_error)
                            ,Toast.LENGTH_SHORT).show();
                }else {
                    LogCustom.i("ZYS", "rep:" + req);
                    if ("1".equals(req)){
                        startActivity(new Intent(DrawCashActivity.this, CashSuccessActivity.class));
//                        finish();
                    }else if ("-1".equals(req)){
                        Toast.makeText(DrawCashActivity.this,getString(R.string.take_case_error)
                                ,Toast.LENGTH_SHORT).show();
                    }else if ("-2".equals(req)){
                        tv_name.setText("");
                        change.setText("绑定");
                        OpenID = "";
                        Toast.makeText(DrawCashActivity.this,"授权过期，请重新绑定微信账号"
                                ,Toast.LENGTH_SHORT).show();
                    }else if ("-3".equals(req)){
                        Toast.makeText(DrawCashActivity.this,"您的余额小于可提现金额",Toast.LENGTH_SHORT)
                                .show();
                    }else if ("-4".equals(req)){
                        Toast.makeText(DrawCashActivity.this,getString(R.string.take_case_error)
                                ,Toast.LENGTH_SHORT).show();
                    }else if ("-5".equals(req)){
                        Toast.makeText(DrawCashActivity.this,"您的可提现金额为0",Toast.LENGTH_SHORT)
                                .show();
                    }else if ("-6".equals(req)){
                        Toast.makeText(DrawCashActivity.this,"您的最高可提现金额为："+cashMax+"元"
                                ,Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(DrawCashActivity.this,getString(R.string.take_case_error)
                                ,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void Fail(int tag) {
        LogCustom.i("ZYS", "Fail:" + tag);
        if (tag == 0){
            cash.setClickable(true);
            cancleHint();
            Toast.makeText(this,getString(R.string.take_case_error),Toast.LENGTH_SHORT).show();
        }else if (tag == 1){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cash.setClickable(true);
                    cancleHint();
                    Toast.makeText(DrawCashActivity.this,getString(R.string.take_case_error),
                            Toast.LENGTH_SHORT).show();
                }
            });
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
        Toast.makeText(this,getString(R.string.net_req_error),Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}

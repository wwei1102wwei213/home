package com.zeyuan.kyq.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.meelive.ingkee.sdk.plugin.InKeSdkPluginAPI;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zeyuan.kyq.Entity.PayWxEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.LogCustom;

/**
 * Created by Administrator on 2017/4/12.
 *
 * 支付页面（映客支付调用）
 *
 * @author wwei
 */
public class InKePayActivity extends BaseActivity{

    private PayWxEntity entity;
    private boolean finishAble = false;
    private int flag;//1为成功，2为失败

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inke_pay);
        entity = (PayWxEntity)getIntent().getSerializableExtra(Const.INTENT_INKE_PAY);
        initWX();
        initView();
        initData();
    }

    private IWXAPI api;
    private static final String APP_ID = "wx02cca444de652c20";
    private static final String PARTNER_ID = "1370371202";
    private void initWX(){
        if (api==null){
            api = WXAPIFactory.createWXAPI(this, null);
            api.registerApp(APP_ID);
        }
    }

    private ImageView iv;
    private TextView tv;
    private ProgressBar pb;
    private ImageView iv_s;
    private ImageView iv_f;
    private Button btn;
    private void initView(){
        iv = (ImageView)findViewById(R.id.iv_back);
        tv = (TextView)findViewById(R.id.tv);
        pb = (ProgressBar)findViewById(R.id.pb);
        iv_s = (ImageView)findViewById(R.id.iv_s);
        iv_f = (ImageView)findViewById(R.id.iv_f);
        btn = (Button)findViewById(R.id.btn_finish);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setFinishView(){
        pb.setVisibility(View.GONE);
        if (flag==1){
            iv_s.setVisibility(View.VISIBLE);
            tv.setText("支付成功");
        }else {
            iv_f.setVisibility(View.VISIBLE);
            tv.setText("支付失败");
        }
        iv.setVisibility(View.VISIBLE);
        btn.setVisibility(View.VISIBLE);
    }

    private void initData(){
        String prepayid = entity.getPrepayid();
        String sign = entity.getSign();
        String timeStamp = entity.getTimestamp();
        String nonceStr = entity.getNoncestr();
        PayReq req = new PayReq();
        req.appId = APP_ID;
        req.partnerId = PARTNER_ID;
        req.prepayId = prepayid;
        req.nonceStr = nonceStr;
        req.timeStamp = timeStamp;
        req.packageValue = "Sign=WXPay";// + packageValue;
        req.sign = sign;
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (ZYApplication.CodeFlag){
            ZYApplication.CodeFlag = false;
            if (ZYApplication.CodePay==0){
                flag = 1;
            }else {
                flag = 2;
            }
            setFinishView();
            finishAble = true;
        }
    }

    @Override
    public void finish() {
        if (finishAble){
            LogCustom.i("ZYS","orderID:"+entity.getPay());
            if (flag==1){
                InKeSdkPluginAPI.dealPay(entity.getPay(),true);
            }else {
                InKeSdkPluginAPI.dealPay(entity.getPay(),false);
            }
            super.finish();
        }
    }
}

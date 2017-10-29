package com.zeyuan.kyq.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogCustom;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "ZYS";
	private static final String APP_ID = "wx02cca444de652c20";
	private static final String PARTNER_ID = "1370371202";
	public static final String PAY_FLAG = "is_pay_flag";
	public static final String PAY_PARAMS = "is_pay_params";

    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);
		overridePendingTransition(0,0);
		api = WXAPIFactory.createWXAPI(this, Contants.AppCfg.WX__APPID);
		api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		LogCustom.i(TAG, "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			Toast.makeText(this,"支付结果：" + String.valueOf(resp.errCode),Toast.LENGTH_SHORT).show();
			ZYApplication.CodePay = resp.errCode;
			ZYApplication.CodeFlag = true;
		}
		finish();
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0,0);
	}
}
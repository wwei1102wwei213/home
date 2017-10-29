package com.zeyuan.kyq.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.biz.manager.ShareResultManager;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.LogUtil;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-11-04
 * Time: 16:44
 * FIXME
 * <p/>
 * 这个是微信所加的activity
 * <p/>
 * qq登录和微信登录
 * 微信登录要在请求获取openid 而qq登录直接返回openid
 * 2个接口 一个是请求微信的openid（tag== 0） 另一个是 登录公司服务器的接口（tag== 0）
 */


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXEntryActivity";
    public IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);
//        overridePendingTransition(0,0);
        regToWX();
        api.handleIntent(getIntent(), this);
    }

    /**
     * 注册微信
     */
    public void regToWX() {
        api = WXAPIFactory.createWXAPI(this, Contants.AppCfg.WX__APPID, true);
        api.registerApp(Contants.AppCfg.WX__APPID);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        LogUtil.i(TAG, "transaction:" + baseReq.transaction);
        LogUtil.i(TAG, "openId:" + baseReq.openId);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:

                if (baseResp.openId == null) {
                    String code = ((SendAuth.Resp) baseResp).code; //即为所需的code
                    LogCustom.i("ZYS", "wx return:" + code);
                    if (!TextUtils.isEmpty(code)) {
                        GlobalData.code = code;
                        ZYApplication.CodeWXLogin = code;
                        ZYApplication.FlagWXLogin = true;

                    }

                } else {
                    LogCustom.i("ZYS", "wx openid:" + baseResp.openId);
                    ShareResultManager.getInstance().WXShareSuccess();
                }
                finish();

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL: {//分享取消
                finish();
            }
        }
        /*if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("支付结果"+ String.valueOf(baseResp.errCode+""));
            builder.show();
        }*/
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}

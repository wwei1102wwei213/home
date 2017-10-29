package com.zeyuan.kyq.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.connect.common.Constants;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.MyFragmentAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.LoginQQBean;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.bean.UserInfoQQBean;
import com.zeyuan.kyq.bean.UserInfoWXBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.fragment.GuideFragment;
import com.zeyuan.kyq.presenter.LoginQQPresenter;
import com.zeyuan.kyq.presenter.LoginWXPresenter;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.DrawCircleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/***
 *
 * 登录页面
 *
 * @author zeyuan
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener, ViewDataListener
        ,HttpResponseInterface ,ViewPager.OnPageChangeListener,IWXAPIEventHandler {

    private static final String TAG = "ZYS";
    private String Headimgurl;
    private RelativeLayout mRelativeLayout_weixin_login;
    private RelativeLayout mRelativeLayout_qq_login;
    public static IWXAPI api;
    public static Tencent mTencent;
    private String openId;
    private String loginType;
    private String code;
    private ProgressDialog dialog;

    private boolean isWxLogin = false;//一个表示用来判断是否为微信登录
    private IUiListener loginListener = new GuiBaseUiListener() {
        @Override
        protected void doComplete(LoginQQBean values) {
            if(values==null||values.getOpenid()==null){
                showError(1);
                return;
            }
            openId = values.getOpenid();
            loginType = Contants.AppCfg.LOGIN__QQ_TYPE;
            new LoginQQPresenter(GuideActivity.this, values).getData();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        try {
//            overridePendingTransition(R.anim.activity_alpha_in,R.anim.activity_alpha_out);
            if (mTencent == null) {
                mTencent = Tencent.createInstance(Contants.AppCfg.QQ__APPID, this.getApplicationContext());
            }
            regToWX();
            initView();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"onCreate");
        }
    }



    /**
     * 注册微信
     */
    public void regToWX() {
//        wx22a57dd27430e724
        api = WXAPIFactory.createWXAPI(this, Contants.AppCfg.WX__APPID, true);
        api.registerApp(Contants.AppCfg.WX__APPID);
        /*api = WXAPIFactory.createWXAPI(this, "wx22a57dd27430e724", true);
        api.registerApp("wx22a57dd27430e724");*/
    }

    private DrawCircleView dcv;
    /**
     * 初始化控件
     * */
    private void initView() {
        try {
            mRelativeLayout_weixin_login = (RelativeLayout) findViewById(R.id.weixin_login);
            mRelativeLayout_qq_login = (RelativeLayout) findViewById(R.id.qq_login);
            mRelativeLayout_weixin_login.setOnClickListener(this);
            mRelativeLayout_qq_login.setOnClickListener(this);
            ViewPager vg = (ViewPager) findViewById(R.id.vg);
            ArrayList<Fragment> fragmentList = new ArrayList<>();
            GuideFragment ft;
            for(int i = 1;i<=4;i++){
                ft = new GuideFragment();
                ft.setPosition(i);
                fragmentList.add(ft);
            }
            MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(),fragmentList);
            vg.setAdapter(adapter);
            dcv = (DrawCircleView)findViewById(R.id.dcv_guide);
            dcv.setDrawCricle(4, 12, Color.parseColor("#22000000"), Color.parseColor("#17cbd1"));
            vg.addOnPageChangeListener(this);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"G,initView");
        }
    }

    /***
     * 点击事件监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qq_login://qq登录
                isWxLogin = false;
                mTencent.login(this, "all", loginListener);
                if (dialog == null) {
                    dialog = new ProgressDialog(this);
                }
                dialog.setCancelable(false);
                dialog.setMessage("正在拉取授权");
                dialog.show();
                break;

            case R.id.weixin_login://微信登录
                isWxLogin = true;
                weixinLogin();
                if (dialog == null) {
                    dialog = new ProgressDialog(this);
                }
                dialog.setCancelable(false);
                dialog.setMessage("正在拉取授权");
                dialog.show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (dialog != null) {
                dialog.dismiss();
            }
        }catch (Exception e){

        }
    }

    /***
     * 微信登录
     */
    private void weixinLogin() {
        try {
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = Contants.AppCfg.WX__SCOPE;
            req.state = Contants.AppCfg.WX__STATE;
            api.sendReq(req);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"weixinLogin()");
        }
    }

    private UserInfoWXBean user_info_wx;
    private UserInfoQQBean user_info_qq;
    private String mOpenID = null;
    private String mUnionID = null;
    /***
     * 回调接口方法，设置发请求的参数
     * @param tag
     * @return
     */
    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();

        if (tag == 1) {//向微信请求数据
            map.put(Contants.appid, Contants.AppCfg.WX__APPID);
            map.put(Contants.secret, Contants.AppCfg.WX__SECRET);
            map.put(Contants.code, code);
            map.put(Contants.grant_type, Contants.AppCfg.WX__GRANT_TYPE);
        }
        if (tag == Const.PCheckOpen){
            if(isWxLogin){
                try {
                    UserInfoWXBean infoWxBean = user_info_wx;
                    try {
                        if(!TextUtils.isEmpty(infoWxBean.getHeadimgurl())){
                            UserinfoData.saveAvatarUrl(this, infoWxBean.getHeadimgurl());
                            UserinfoData.saveTempHeadImgUrl(this,infoWxBean.getHeadimgurl());
                        }
                    }catch (Exception e){
                        ExceptionUtils.ExceptionToUM(e,this,"WX登录图片参数异常");
                    }
                    UserinfoData.saveLoginType(this, "1");
                    UserinfoData.saveInfoname(this, infoWxBean.getNickname());
                    mUnionID = infoWxBean.getUnionid();
                    UserinfoData.saveUnionID(this, infoWxBean.getUnionid());
                    mOpenID = infoWxBean.getOpenid();
                    UserinfoData.saveOpenID(this, infoWxBean.getOpenid());
                    map.put("unid",infoWxBean.getUnionid());
                    map.put("openid",infoWxBean.getOpenid());
                    map.put("apptype","2");
                    map.put("loginType","1");
                }catch (Exception e){
                    ExceptionUtils.ExceptionToUM(e,this,"WX登录参数异常");
                }
            }else{
                UserInfoQQBean infoQQBean = user_info_qq;
                UserinfoData.saveLoginType(this, "2");
                UserinfoData.saveAvatarUrl(this, infoQQBean.getFigureurl_qq_2());
                UserinfoData.saveTempHeadImgUrl(this, infoQQBean.getFigureurl_qq_2());
                UserinfoData.saveInfoname(this, infoQQBean.getNickname());
                mOpenID = infoQQBean.getOpenid();
                UserinfoData.saveOpenID(this, infoQQBean.getOpenid());
                map.put("openid",infoQQBean.getOpenid());
                map.put("apptype", "2");
                map.put("loginType","2");
            }
        }

        return map;

    }

    private PhpUserInfoBean bean;

    /***
     * 回调接口方法，处理数据
     * @param t 服务器返回的数据
     * @param position 发请求时的标示
     */
    @Override
    public void toActivity(Object t, int position) {
        if(position == Const.PCheckOpen){//NetNumber.VIEWDATALISTENER_TOACTIVITY_ZEYUAN
            bean = (PhpUserInfoBean) t;
            if (Const.RESULT.equals(bean.getiResult())){
                String doLogin = bean.getDoLogin();
                String InfoID = bean.getInfoID();
                if (!TextUtils.isEmpty(InfoID)&&!TextUtils.isEmpty(doLogin)&&"1".equals(doLogin)){
                    UserinfoData.saveInfoID(this, bean.getInfoID());
                    Const.InfoID = bean.getInfoID();
                    UserinfoData.saveIsHaveCreateInfo21(this, "1");
                    startActivity(new Intent(GuideActivity.this, MainActivity.class));//如果有就去mainactivity
                    finish();
                }else {
                    startActivity(new Intent(GuideActivity.this, BindingPhoneActivity.class)
                            .putExtra(Const.INTENT_BIND_CANCLE, false)
                            .putExtra(Contants.OpenID,mOpenID)
                            .putExtra(Contants.UnionID,mUnionID));
                    finish();
                }
            }else {
//                Toast.makeText(GuideActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                showError(Const.PCheckOpen);
            }

        }else if(position == Const.GUIDE_GET_USER_INFO_WX){
            user_info_wx = (UserInfoWXBean)t;
            if (user_info_wx!=null){
                Factory.postPhp(this, Const.PCheckOpen);
            }
        }else if(position == Const.GUIDE_GET_USER_INFO_QQ){
            user_info_qq = (UserInfoQQBean)t;
            if(user_info_qq!=null){
                Factory.postPhp(this, Const.PCheckOpen);
            }
        }
    }

    @Override
    public void showLoading(int tag) {
        if (tag == Const.PCheckOpen) {
            if (dialog == null) {
                dialog = new ProgressDialog(this);
            }
            dialog.setMessage("正在登录");
            dialog.setCancelable(false);
            dialog.show();
            mRelativeLayout_weixin_login.setClickable(false);
            mRelativeLayout_qq_login.setClickable(false);
        }else {
            if (dialog == null) {
                dialog = new ProgressDialog(this);
                dialog.setMessage("正在登录");
                dialog.setCancelable(false);
                dialog.show();
            } else {
                dialog.setMessage("正在拉取授权");
            }
        }
    }

    @Override
    public void hideLoading(int tag) {
        if(tag == Const.PCheckOpen){
            if (dialog != null) {
                dialog.dismiss();
            }
        }else {
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void showError(int tag) {
        if(tag == Const.PCheckOpen){
            if (dialog != null) {
                dialog.dismiss();
            }
            Toast.makeText(this,"登录失败，请重试！",Toast.LENGTH_SHORT).show();
            mRelativeLayout_weixin_login.setClickable(true);
            mRelativeLayout_qq_login.setClickable(true);
        }else {
            if (dialog != null) {
                dialog.dismiss();
            }
            Toast.makeText(this,"授权失败，请重试！",Toast.LENGTH_SHORT).show();
        }
    }

    /***
     * 监听登陆请求所返回的数据
     */
    private class GuiBaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            Gson gson = new Gson();
            LoginQQBean qqLoginBean = gson.fromJson(response.toString(), LoginQQBean.class);
            doComplete(qqLoginBean);
        }
        protected void doComplete(LoginQQBean values) {
            LogUtil.i(TAG, "doComplete");
        }
        @Override
        public void onError(UiError e) {
            Log.i(TAG, e.toString());
        }

        @Override
        public void onCancel() {
            Log.i(TAG, "cancel");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == Constants.REQUEST_LOGIN) {
                if (resultCode == Constants.ACTIVITY_OK) {
                    mTencent.handleLoginData(data, loginListener);
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"onactivityresult,guideactivity");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (isWxLogin && !TextUtils.isEmpty(GlobalData.code)) {//这个说明这儿是从微信登录返回来的数据
                code = GlobalData.code;
                ZYApplication.FlagWXLogin = false;
                new LoginWXPresenter(this).getData();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"onResume()");
        }
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        dcv.redraw(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onReq(BaseReq baseReq) {
        LogCustom.i("WXE", baseReq.toString());
    }

    @Override
    public void onResp(BaseResp baseResp) {
        LogCustom.i("WXE",baseResp.toString());
    }


}

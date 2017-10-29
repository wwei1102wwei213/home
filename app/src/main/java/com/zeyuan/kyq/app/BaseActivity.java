package com.zeyuan.kyq.app;

import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.biz.manager.ShareResultManager;
import com.zeyuan.kyq.fragment.EmptyPageFragment;
import com.zeyuan.kyq.fragment.empty.NoResultFragment;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.DeviceUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.NetworkUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2015/9/6.
 *
 * @author wwei
 */
public class BaseActivity extends AppCompatActivity implements NoResultFragment.OnClickCircleLister {

    private static List<BaseActivity> activities;
    private SystemBarTintManager tintManager;
    public BaseActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (activities == null) {
                activities = new ArrayList<>();
            }
            getInfoID();
            PushAgent.getInstance(this).onAppStart();

        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "BaseActivity,onCreate");
        }

    }


    protected void setFont() {
        try {
            changeFont((ViewGroup) this.getWindow().getDecorView());
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "BaseActivity,changeFont");
        }
    }

    protected void changeFont(ViewGroup root) {
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "font/RobotoCondensed-Regular.ttf");
        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(tf);
            } else if (v instanceof Button) {
                ((Button) v).setTypeface(tf);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(tf);
            } else if (v instanceof ViewGroup) {
                changeFont((ViewGroup) v);
            }
        }
    }

    public void showUI(int flag) {

    }

    public Context getCurrentContext() {
        return this;
    }

    @Override
    public void onClickCircle() {
        try {
//            AppManager.getAppManager().finishAllActivity();
            finish();
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "BaseActivity,onClickCircle");
        }

    }

    public class ClickBack implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                finish();
            } catch (Exception e) {

            }
        }
    }

    protected void initTitlebar(String title) {
        try {
            ImageView back = (ImageView) findViewById(R.id.btn_back);
            back.setOnClickListener(new ClickBack());
            TextView title_tv = (TextView) findViewById(R.id.title);
            title_tv.setText(title);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "initTitlebar");
        }
    }

    protected void initOtherTitle(String titleStr) {
        try {
            ImageView back = (ImageView) findViewById(R.id.iv_other_title_back);
            back.setOnClickListener(new ClickBack());
            TextView title = (TextView) findViewById(R.id.tv_other_title);
            title.setText(titleStr);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "initOtherTitle");
        }
    }

    protected void initOtherTitle(String titleStr, boolean flag) {
        try {
            ImageView back = (ImageView) findViewById(R.id.iv_other_title_back);
            back.setOnClickListener(new ClickBack());
            TextView title = (TextView) findViewById(R.id.tv_other_title);
            title.setText(titleStr);
            if (flag) {
                ImageView share = (ImageView) findViewById(R.id.iv_other_title_share);
                share.setImageResource(R.mipmap.share_step_def);
            }
        } catch (Exception e) {

        }
    }

    protected void initWhiteTitle(String titleStr) {
        try {
            ImageView back = (ImageView) findViewById(R.id.iv_white_title_back);
            back.setOnClickListener(new ClickBack());
            TextView title = (TextView) findViewById(R.id.tv_white_title);
            title.setText(titleStr);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "initWhiteTitle");
        }
    }

    protected void initWhiteTitle(String titleStr, int flag) {
        try {
            ImageView back = (ImageView) findViewById(R.id.iv_white_title_back);
            back.setOnClickListener(new ClickBack());
            TextView title = (TextView) findViewById(R.id.tv_white_title);
            title.setText(titleStr);
            if (flag == 0) {
                ImageView share = (ImageView) findViewById(R.id.iv_white_title_share);
                share.setImageResource(R.mipmap.search_big_blue);
            }
        } catch (Exception e) {

        }
    }

    protected void initWhiteTitle(String titleStr, boolean flag) {
        try {
            ImageView back = (ImageView) findViewById(R.id.iv_white_title_back);
            back.setOnClickListener(new ClickBack());
            TextView title = (TextView) findViewById(R.id.tv_white_title);
            title.setText(titleStr);
            if (flag) {
                ImageView share = (ImageView) findViewById(R.id.iv_white_title_share);
                share.setImageResource(R.mipmap.share_step_def);
            }
        } catch (Exception e) {

        }
    }


    /**
     * 释放imageview的图片内存
     *
     * @param imageView
     */
    public void clearBitmapMomery(ImageView imageView) {
        if (imageView != null && imageView.getDrawable() != null) {
            Bitmap oldBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            imageView.setImageDrawable(null);
            if (oldBitmap != null) {
                oldBitmap.recycle();
                oldBitmap = null;
            }
        }
        System.gc();
    }

    protected void showToast(String content) {
        try {
            Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }
    }

    /**
     * 释放view的背景内存
     *
     * @param view
     */

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void clearBackgroundMemery(View view) {
        if (view != null && view.getBackground() != null) {
            Bitmap oldBitmap = ((BitmapDrawable) view.getBackground()).getBitmap();
            view.setBackground(null);
            if (oldBitmap != null) {
                oldBitmap.recycle();
                oldBitmap = null;
            }
        }
        System.gc();
    }

    /**
     * @param textView 要设置的textview
     * @param string   字符串
     *                 工具方法 抽取出来的
     */
    protected void setTextString(TextView textView, String string) {
        try {
            if (!TextUtils.isEmpty(string)) {
                textView.setText(string);
            } else {
                textView.setText(R.string.no_data);
            }
        } catch (Exception e) {

        }
    }

    private int index = 0;

    /**
     * 上传图片的命名规则
     *
     * @param context
     * @param isAvatar true 为头像上传 false 为其他类型图片上传
     * @return
     */
    protected String getImgName(Context context, boolean isAvatar) {
        StringBuilder builder = new StringBuilder();
        builder.append(UserinfoData.getInfoID(context));//infoid
        if (isAvatar) {//模块名称 头像是HeadImg 其余都是ForumImg
            builder.append("HeadImg");
        } else {
            builder.append("ForumImg");
        }
        builder.append(System.currentTimeMillis());//当前时间戳 微秒
        String random = getRandom();
        builder.append(random);//随机码（0~999）
        builder.append(index++);
        builder.append("2");//标记码  android 为2 ios为1
        if (isAvatar) {
            return builder.toString() + ".png";
        } else {
            return builder.toString() + ".png";
        }
    }

    /**
     * 小图的url
     */
    private static final String THUMB = "thumb";

    protected String insertThumb(String imageName) {
        StringBuilder sb = new StringBuilder(imageName);
        int index = sb.indexOf(".");
        return sb.insert(index, THUMB).toString();
    }

    /**
     * 生成一个1到10^6的随机数
     *
     * @return
     */
    protected String getRandom() {
        Random random = new Random();
        int i = random.nextInt(1000000);
//        int i = 1 + (int) (Math.random() * 1000000);
        if (i / 100 > 0) {
            return i + "";
        } else if (i / 10 > 0) {
            return "0" + i;
        } else {
            return "00" + i;
        }
    }

    protected void emptyCommnFragment(int resId, String str1, String str2, String str3) {
        try {
            NoResultFragment fragment = new NoResultFragment();
            fragment.setResId(resId);
            fragment.setStr_tv1(str1);
            fragment.setStr_tv2(str2);
            fragment.setStr_tv3(str3);
            fragment.setOnClickCircleLister(this);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fl, fragment);
            ft.commit();
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "emptyCommnFragment失败");
        }
    }

    protected void setEmptyPageFragment(int resId, String hint, String btnText,
                                        EmptyPageFragment.EmptyClickListener listener, int replaceId) {
        try {
            EmptyPageFragment fragment = new EmptyPageFragment();
            fragment.setResId(resId);
            fragment.setHint(hint);
            fragment.setBtnText(btnText);
            if (listener == null) {
                fragment.setShowBtn(false);
            } else {
                fragment.setEmptyClickListener(listener);
            }
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(replaceId, fragment);
            ft.commit();
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "显示空白页出错");
        }
    }

    protected void setEmptyPageFragment(int resId, String hint, String btnText,
                                        EmptyPageFragment.EmptyClickListener listener, int replaceId, int tag) {
        try {
            EmptyPageFragment fragment = new EmptyPageFragment();
            fragment.setResId(resId);
            fragment.setHint(hint);
            fragment.setBtnText(btnText);
            if (listener == null) {
                fragment.setShowBtn(false);
            } else {
                fragment.setEmptyClickListener(listener);
            }
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(replaceId, fragment);
            ft.commit();
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "显示空白页出错");
        }
    }

    public void afterFinish() {
        try {
            Intent var3 = this.getPackageManager().getLaunchIntentForPackage(this.getPackageName());
            if (var3 == null) {

            } else {
                var3.setPackage((String) null);
                var3.addFlags(268435456);
                this.startActivity(var3);
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "BaseActivity afterFinish");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            ZYApplication.isAppShow = true;
            MobclickAgent.onPageStart(this.getClass().getSimpleName());
            MobclickAgent.onResume(this);
            ShareResultManager.getInstance().createShareListener(this);

        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "BaseActivity onResume 1");
        }
        try {
            Glide.with(this).resumeRequests();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "BaseActivity onResume 2");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            ZYApplication.isAppShow = false;
            MobclickAgent.onPageEnd(this.getClass().getSimpleName());
            MobclickAgent.onPause(this);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "BaseActivity onPause 1");
        }
        try {
            Glide.with(this).pauseRequests();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "BaseActivity onPause 2");
        }
    }

    public void getInfoID() {
        try {
            if (TextUtils.isEmpty(Const.InfoID)) {
                Const.InfoID = UserinfoData.getInfoID(this);
            }
        } catch (Exception e) {

        }
    }

    private long spaceTime = 0;

    public void onNetworkResumed(int state) {
        try {
            if (state == 0 && ZYApplication.isAppShow) {
                if ((System.currentTimeMillis() - spaceTime) > 5000) {
                    NetworkUtils.openNetworkHint(activity);
                    spaceTime = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "onNetworkResumed失败");
        }
    }

    /**
     * 设置状态栏颜色
     *
     * @param colorResId
     */
    public void setStatusBarColor(int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //如果状态栏管理变量为空则创建
            if (tintManager == null) {
                tintManager = new SystemBarTintManager(this);
            }
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(ContextCompat.getColor(this, colorResId));
        }
    }

    /**
     * 设置状态栏为透明
     */
    public void setStatusBarTranslucent() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        } catch (Exception e) {

        }
    }

    public void initStatusBar() {
        try {
            View statusBar1 = findViewById(R.id.statusBar1);
            ViewGroup.LayoutParams params1 = statusBar1.getLayoutParams();
            params1.height = getStatusBarHeight();
            statusBar1.setLayoutParams(params1);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "initStatusBar");
        }
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    /**
     * 设置导航栏颜色
     *
     * @param colorResId
     */
    public void setNavigationBarColor(int colorResId) {
        if (DeviceUtils.checkDeviceHasNavigationBar(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // 创建状态栏的管理实例
                if (tintManager == null) {
                    tintManager = new SystemBarTintManager(this);
                }
                this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                // 激活导航栏设置
                tintManager.setNavigationBarTintEnabled(true);
                tintManager.setNavigationBarTintColor(ContextCompat.getColor(this, colorResId));
            }
        }
    }

    /**
     * 设置导航栏透明
     */
    public void setNavigationBarTranslucent() {
        if (DeviceUtils.checkDeviceHasNavigationBar(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private String getRandomMath() {
        String temp = (int) (Math.random() * 89999 + 10000) + "";
        return temp;
    }

    protected String getParamKaqID() {
        return "kaq=" + getRandomMath() + UserinfoData.getInfoID(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShareResultManager.getInstance().createShareListener(this);
        Tencent.onActivityResultData(requestCode, resultCode, data, ShareResultManager.getInstance().getShareListener());
    }

    @Override
    protected void onDestroy() {
        ShareResultManager.getInstance().clearListener();
        super.onDestroy();
    }

    // 清除cookie即可彻底清除缓存
    public void clearWebViewCache() {
        try {
            CookieSyncManager.createInstance(this);
            CookieManager.getInstance().removeAllCookie();
        } catch (Exception e) {
            LogCustom.e("clearWebViewCache", e.getMessage());
        }

    }

    Handler handler;

    //避免按钮重复点击
    public void setDoubleClickDelay(final View view) {
        if (handler == null) {
            handler = new Handler();
        }
        if (view != null)
            view.setEnabled(false);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (view != null)
                    view.setEnabled(true);
            }
        }, 1000);

    }
}

package com.zeyuan.kyq.fragment.main;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.alibaba.sdk.android.feedback.util.IUnreadCountCallback;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.meelive.ingkee.sdk.plugin.InKeSdkPluginAPI;
import com.youzan.sdk.YouzanSDK;
import com.zeyuan.kyq.Entity.HomePageEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseZyFragment;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.MainPageInfoBean;
import com.zeyuan.kyq.bean.MyDataBean;
import com.zeyuan.kyq.biz.BlurBiz;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.manager.IntegrationManager;
import com.zeyuan.kyq.biz.manager.MyCircleManager;
import com.zeyuan.kyq.db.DBHelper;
import com.zeyuan.kyq.filedownloader.JFileDownloadListener;
import com.zeyuan.kyq.filedownloader.JFileDownloader;
import com.zeyuan.kyq.fragment.dialog.ExitDialog;
import com.zeyuan.kyq.fragment.dialog.ForumFinishDialog;
import com.zeyuan.kyq.fragment.dialog.SignInDialog;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.InitZYUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.AboutActivity;
import com.zeyuan.kyq.view.BindingPhoneActivity;
import com.zeyuan.kyq.view.DrawCashActivity;
import com.zeyuan.kyq.view.GuideActivity;
import com.zeyuan.kyq.view.InfoCenterActivity;
import com.zeyuan.kyq.view.MainActivity;
import com.zeyuan.kyq.view.ModifyPhoneGetCodeActivity;
import com.zeyuan.kyq.view.MyFlwForumActivity;
import com.zeyuan.kyq.view.MyForumActivity;
import com.zeyuan.kyq.view.MyFosCircleActivity;
import com.zeyuan.kyq.view.MyReplyActivity;
import com.zeyuan.kyq.view.PatientDataActivity;
import com.zeyuan.kyq.view.PersonalDataActivity;
import com.zeyuan.kyq.view.ShowDiscuzActivity;
import com.zeyuan.kyq.widget.CircleImageView;
import com.zeyuan.kyq.widget.CustomScrollView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/6.
 *
 * @author wwei
 */
public class MoreNewFragment extends BaseZyFragment implements View.OnClickListener, ExitDialog.ExitCallback
        , HttpResponseInterface, CustomScrollView.onScrollCustomChange, BlurBiz.BlurInterface, OnDismissListener {

    private static final String TAG = "MoreNewFragment";
    //    private static final String DEFAULT_APPKEY = "23566950";
    private boolean isGetting = false;
    private boolean isOpen = false;
    private static final int CAMERA_PERMISSIONS = 1;
    private static final int STORAGE_AND_CAMERA_PERMISSIONS = 2;
    private TextView name;
    private TextView nameOther;
    private CircleImageView avatar;
    private Typeface tf;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new_more, container, false);
        try {
            tf = Typeface.createFromAsset(getActivity().getAssets(), "font/BEBASNEUE.OTF");
        } catch (Exception e) {

        }
        initStatusBar();
        initView();
        initFeedback();
        initData();
        return rootView;
    }

    private TextView tv_cash_balance;//余额
    private TextView tv_integration_sum;//积分总数
    private View to_recommend_friend_layout;
    //    private DynamicWave wave;
    private ImageView point;
    private TextView tv_menu_circle;
    private TextView tv_menu_forum;
    private TextView tv_menu_reply;
    private TextView tv_menu_follow;
    private TextView tv_is_bind;
    private ImageView iv_is_bind;
    private CustomScrollView csv;
    /*private View v_blur1;
    private View v_blur2;*/
    private View v_up;

    //设置视图
    private void initView() {

        try {
            csv = (CustomScrollView) findViewById(R.id.csv_more);
            csv.setScrollCustomChange(this);
            to_recommend_friend_layout = findViewById(R.id.to_recommend_friend_layout);
            to_recommend_friend_layout.setOnClickListener(this);
            name = (TextView) findViewById(R.id.name);
            nameOther = (TextView) findViewById(R.id.name_other);
            avatar = (CircleImageView) findViewById(R.id.avatar);
            showAvatar();

            point = (ImageView) findViewById(R.id.reply_redpoint);
            tv_is_bind = (TextView) findViewById(R.id.tv_is_bind);

            try {
                DisplayMetrics metric = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
                int width = metric.widthPixels; // 屏幕宽度（像素）
                width = (width * 3) / 8 - 70;
                point.setPadding(0, 0, width, 0);
            } catch (Exception e) {

            }

            findViewById(R.id.to_cash_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), DrawCashActivity.class));
                }
            });

            findViewById(R.id.about).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, AboutActivity.class));
                }
            });

            /*findViewById(R.id.more_f_help).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), UserHelperActivity.class));

                }
            });*/

            findViewById(R.id.to_patient_data).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, PatientDataActivity.class));
                }
            });

            findViewById(R.id.to_join_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, ShowDiscuzActivity.class)
                            .putExtra(Const.SHOW_HTML_MAIN_TOP,
                                    "http://120.24.14.34/Api/ArticleRule?" + getParamKaqID() + "&lt=2&Type=2"));
                }
            });

            findViewById(R.id.more_f_bind).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(ZYApplication.PhoneNum)) {
                        getActivity().startActivityForResult(new Intent(getActivity(), ModifyPhoneGetCodeActivity.class), 8);
                    } else {
                        startActivity(new Intent(getActivity(), BindingPhoneActivity.class));
                    }
                }
            });
            try {
                if (tf != null) {
                    tv_is_bind.setTypeface(tf);
                }
            } catch (Exception e) {

            }

            setPhoneNum();
            setInforName(name);
            String type = UserinfoData.getLoginType(context);
            if ("1".equals(type)) {
                nameOther.setText("(微信登录)");
            } else {
                nameOther.setText("(QQ登录)");
            }
            tv_cash_balance = (TextView) findViewById(R.id.tv_cash_balance);
            View to_integration_layout = findViewById(R.id.to_integration_layout);
            tv_integration_sum = (TextView) findViewById(R.id.tv_integration_sum);
            tv_menu_circle = (TextView) findViewById(R.id.tv_menu_circle);
            tv_menu_forum = (TextView) findViewById(R.id.tv_menu_forum);
            tv_menu_reply = (TextView) findViewById(R.id.tv_menu_reply);
            tv_menu_follow = (TextView) findViewById(R.id.tv_menu_follow);
            tv_menu_circle.setOnClickListener(this);
            tv_menu_forum.setOnClickListener(this);
            tv_menu_reply.setOnClickListener(this);
            tv_menu_follow.setOnClickListener(this);
            findViewById(R.id.v_to_info_center).setOnClickListener(this);
            findViewById(R.id.tv_edit_for_more).setOnClickListener(this);
            findViewById(R.id.iv_setting).setOnClickListener(this);
            to_integration_layout.setOnClickListener(this);
            tv_integration_sum.setText(IntegrationManager.getInstance().getSum());
            IntegrationManager.getInstance().addOnIntegrationChangedListener(new IntegrationManager.OnIntegrationChangedListener() {
                @Override
                public void onIntegrationChanged(String sum) {
                    tv_integration_sum.setText(sum);
                }
            });
            tv_cash_balance.setText(UserinfoData.getWD());
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, getActivity(), TAG);
        }
    }

    private EditText etName;
    private String LiveID = "";

    /**
     * 修改名字 弹出对话框
     */
    private void showInputName() {
        try {
            ViewGroup extView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.alertext_form, null);
            etName = (EditText) extView.findViewById(R.id.etName);
            if (!TextUtils.isEmpty(LiveID)) {
                etName.setText(LiveID);
            } else {
                etName.setHint("请输入直播ID");
            }
            new AlertView(null, "请输入直播ID", "取消", null, new String[]{"确定"},
                    context, AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    if (position == 0) {
                        String name = etName.getText().toString().trim();
                        if (!TextUtils.isEmpty(name)) {
                            LiveID = name;
                            InKeSdkPluginAPI.start(context, ZYApplication.YK_UserInfo, false, name);
                        }
                    }
                    closeKeyboard();
                }
            }).addExtView(extView).setOnDismissListener(this).setCancelable(true).show();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "PatientDetailActivity");
        }
    }

    private InputMethodManager imm;

    /***
     * 关闭软键盘
     *
     */
    private void closeKeyboard() {
        //关闭软键盘
        if (etName != null) {
            if (imm == null)
                imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
        }
    }

    @Override
    public void onDismiss(Object o) {
        closeKeyboard();
    }

    private String getRandomMath() {
        String temp = (int) (Math.random() * 89999 + 10000) + "";
        return temp;
    }

    protected String getParamKaqID() {
        return "kaq=" + getRandomMath() + UserinfoData.getInfoID(context);
    }

    private void setSignIn() {
        SignInDialog dialog = new SignInDialog.Builder(getActivity())
                .setHtmlMessage
                        ("坚持签到好运到来，增加<font color=\"#17cbd1\">20</font>积分<br>已经连续签到<font color=\"#17cbd1\">5</font>天")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    private void setForumSuccess() {
        ForumFinishDialog dialog = new ForumFinishDialog.Builder(getActivity())
                .setHtmlMessage
                        ("获得<font color=\"#17cbd1\">20</font>积分")
                .create();
        dialog.show();
    }

    @Override
    public void setBlur(Bitmap bitmap) {
        /*if (bitmap!=null&& Build.VERSION.SDK_INT>=16){
            v_blur2.setBackground(new BitmapDrawable(context.getResources(), bitmap));
        }*/
    }

    //设置数据
    private void initData() {
        Factory.post(this, Const.EGetMyForumNum);
    }

    @Override
    public Map getParamInfo(int tag) {
        return null;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if (flag == Const.EGetMyForumNum) {
            args = new String[]{Contants.InfoID, UserinfoData.getInfoID(getActivity())};
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object response, int flag) {
        try {
            if (flag == Const.EGetMyForumNum) {
                MyDataBean bean = (MyDataBean) response;
                if (Contants.RESULT.equals(bean.getIResult())) {
                    bindView(bean);
                }
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, getActivity(), TAG);
        }
    }

    private int mReplyNum = 0;

    public void bindView(MyDataBean bean) {
        try {
            /**
             * 数字设置
             */

            try {
                if (tf != null) {
                    tv_menu_circle.setTypeface(tf);
                    tv_menu_forum.setTypeface(tf);
                    tv_menu_reply.setTypeface(tf);
                    tv_menu_follow.setTypeface(tf);
                }
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "setTypeface");
            }

            List<String> tempS = UserinfoData.getFocusCircle(getActivity());
            if (tempS != null && tempS.size() > 0) {
                tv_menu_circle.setText(Html.fromHtml("圈子 " + "<font color=\"#575757\"><b>" + tempS.size() + "<b></font>"));
            } else {
                String circleNum = bean.getCircleNum();
                if (TextUtils.isEmpty(circleNum)) {
                    tv_menu_circle.setText(Html.fromHtml("圈子 " + "<font color=\"#575757\"><b>" + 0 + "<b></font>"));
                } else {
                    tv_menu_circle.setText(Html.fromHtml("圈子 " + "<font color=\"#575757\"><b>" + circleNum + "<b></font>"));
                }
            }
            try {
                String forumNumNum = bean.getForumNum();
                tv_menu_forum.setText(Html.fromHtml("帖子 " + "<font color=\"#575757\"><b>" + forumNumNum + "<b></font>"));
                String favorNim = bean.getFavorNum();
                tv_menu_follow.setText(Html.fromHtml("收藏 " + "<font color=\"#575757\"><b>" + favorNim + "<b></font>"));
                String replyNum = bean.getReplyNum();
                tv_menu_reply.setText(Html.fromHtml("回复 " + "<font color=\"#575757\"><b>" + replyNum + "<b></font>"));
            } catch (Exception e) {

            }
            try {
                mReplyNum = Integer.valueOf(bean.getReplyNum());
                int temp = UserinfoData.getReplyNum(getActivity());
                if (mReplyNum != 0 && temp != mReplyNum) {
                    point.setVisibility(View.VISIBLE);
                    ((MainActivity) context).forceCircleUpdate(true);
                }
            } catch (Exception e) {
                ExceptionUtils.ExceptionToUM(e, getActivity(), TAG + ":提示红点出错");
            }

//            initUp(bean);

        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, getActivity(), TAG);
        }
    }

    //设置手机号码
    public void setPhoneNum() {
        if (!TextUtils.isEmpty(ZYApplication.PhoneNum)) {
            String temp = "";
            try {
                temp = ZYApplication.PhoneNum.substring(0, 3) + "****" +
                        ZYApplication.PhoneNum.substring(ZYApplication.PhoneNum.length() - 4,
                                ZYApplication.PhoneNum.length());
            } catch (Exception e) {

            }
            tv_is_bind.setText(temp);
        } else {
            tv_is_bind.setText(getString(R.string.no_bind_text));
        }
    }

    private void initUp(MyDataBean bean) {


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

    //设置友盟反馈
    private void initFeedback() {
        try {
            InitZYUtils.initFeedback(context);
            ImageView iv = (ImageView) findViewById(R.id.iv_fb_in);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isGetting) {
                        isGetting = true;
                        isOpen = true;
                        checkForOpenOrGet(true);
                    }
                }
            });
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "initFeedback");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == STORAGE_AND_CAMERA_PERMISSIONS) {
            isGetting = false;
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openOrGet(isOpen);
            } else {
                Toast.makeText(context, "没有拍照或相册权限", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // 数据同步
    private void sync(final ImageView iv) {

    }


  //  private ShareFragment shareFragment;

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.to_recommend_friend_layout:
                    try {
//                        if (shareFragment == null) {
//                            shareFragment = ShareFragment.getInstance(getString(R.string.share_url_init),
//                                    getString(R.string.share_title_init),
//                                    getString(R.string.share_content_init),
//                                    null, Const.SHARE_APP);
//                        }
//                        if (shareFragment.getDialog() == null || !shareFragment.getDialog().isShowing())
//                            shareFragment.show(getActivity().getSupportFragmentManager(), ShareFragment.type);
                        context.startActivity(new Intent(context, ShowDiscuzActivity.class)
                                .putExtra(Const.SHOW_HTML_MAIN_TOP, "http://help.kaqcn.com/Api/InvitedCode?InfoID=" + UserinfoData.getInfoID(getActivity())));
                    } catch (Exception e) {
                        LogCustom.e("to_recommend_friend_layout", e.getMessage());
                    }
                    break;
                case R.id.iv_setting:
                    showExitDialog();
                    break;
                case R.id.tv_menu_circle:
                    startActivity(new Intent(getActivity(), MyFosCircleActivity.class));
                    break;
                case R.id.tv_menu_forum:
                    startActivity(new Intent(getActivity(), MyForumActivity.class));
                    break;
                case R.id.tv_menu_follow:
                    startActivity(new Intent(getActivity(), MyFlwForumActivity.class));
                    break;
                case R.id.tv_menu_reply:
                    if (point.getVisibility() != View.GONE) {
                        UserinfoData.saveReplyNum(getActivity(), mReplyNum);
                        point.setVisibility(View.GONE);
                        ((MainActivity) getActivity()).forceCircleUpdate(false);
                    }
                    startActivity(new Intent(getActivity(), MyReplyActivity.class));
                    break;
                case R.id.v_to_info_center:
                    startActivity(new Intent(context, InfoCenterActivity.class)
                            .putExtra(Const.InfoCenterID, UserinfoData.getInfoID(context)));
                    break;
                case R.id.tv_edit_for_more:
                    startActivity(new Intent(context, PersonalDataActivity.class));
                    break;
                case R.id.to_integration_layout:
                    HomePageEntity homePageEntity = new HomePageEntity();
                    homePageEntity.setSkiptype("1");
                    homePageEntity.setSign_a("http://help.kaqcn.com/Api/getUserIntegral?InfoID=" + UserinfoData.getInfoID(context));
                    UiUtils.toMenuJump(context, homePageEntity, null, false, null);
                    break;
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, context, TAG);
        }
    }

    private ExitDialog eDialog;

    private void showExitDialog() {
        try {
            if (eDialog == null) {
                eDialog = new ExitDialog();
                eDialog.setCallback(this);
            }
            eDialog.show(getActivity().getFragmentManager(), "exit");
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, getActivity(), TAG);
        }
    }

    @Override
    public void onExitClick() {
        try {
            clearData();
            startActivity(new Intent(context, GuideActivity.class));
            ((MainActivity) context).finish();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "onExitClick");
        }
    }

    private void clearData() {
        try {
            UserinfoData.clearMermory(context);
            Const.InfoID = null;
            MyCircleManager.getInstance().clearCircle();
            DBHelper.getInstance().dropMsgClickTable();//清除消息点击记录
            YouzanSDK.userLogout(getActivity());//退出有赞登录
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, getActivity(), TAG);
        }
    }


    private void setInforName(TextView mTextView_name) {
        try {
            String inforName = UserinfoData.getInfoname(context);
            if (!TextUtils.isEmpty(inforName)) {
                mTextView_name.setText(inforName);
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, getActivity(), TAG);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (name != null && !name.getText().equals(UserinfoData.getInfoname(context))) {
            setInforName(name);
        }

        showAvatar();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
//            FeedbackAPI.cleanActivity();
            if (eDialog != null) {
                eDialog.dismiss();
            }
        } catch (Exception e) {

        }


    }

    protected void showAvatar() {
        try {
            String imageUrl = UserinfoData.getAvatarUrl(getActivity());
            if (!TextUtils.isEmpty(imageUrl)) {
                Glide.with(context).load(imageUrl).signature(new IntegerVersionSignature(1))
                        .error(R.mipmap.default_avatar).into(avatar);
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "showAvatar");
        }
    }


    private Animation a;

    @Override
    public void forScrollCustomChange(boolean fit) {

    }


    private void checkForOpenOrGet(boolean isOpenFeedback) {
        //打开页面前需要先申请相册和拍照的权限
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , STORAGE_AND_CAMERA_PERMISSIONS);
        } else {
            openOrGet(isOpenFeedback);
        }
    }

    /**
     * @param isOpenFeedback 打开网页or获取未读数
     */
    private void openOrGet(final boolean isOpenFeedback) {
        //接入方不需要这样调用, 因为扫码预览, 同时为了服务器发布后能做到实时预览效果, 所有每次都init.

        //如果500ms内init未完成, openFeedbackActivity会失败, 可以延长时间>500ms
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isOpenFeedback) {
                    FeedbackAPI.openFeedbackActivity();
                    isGetting = false;
                } else {
                    FeedbackAPI.getFeedbackUnreadCount(new IUnreadCountCallback() {
                        @Override
                        public void onSuccess(final int unreadCount) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toast = Toast.makeText(context, "未读数：" + unreadCount, Toast.LENGTH_SHORT);
                                    toast.show();
                                    isGetting = false;
                                }
                            });
                        }

                        @Override
                        public void onError(int i, String s) {
                            isGetting = false;
                        }
                    });
                }

            }
        }, 300);
    }

    private String downurl = "";
    private String updateMessage = "";
    private String versionNum = "";
    private String updateType = "";

    /***
     * 根据updateType判断下一步操作
     */
    private void initUpDateType(MainPageInfoBean.UpEntity upEntity) {
        if (upEntity != null) {
            /**新版本的下载地址*/
            downurl = upEntity.getL();
            /**新版本的提示内容*/
            updateMessage = upEntity.getM();
            /**新版本的更新方式*/
            updateType = upEntity.getU();
            /**新版本的版本号*/
            versionNum = upEntity.getV();
        }
        try {
            if ("3".equals(updateType)) {

            } else {
                initUpdateVersion();
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, getActivity(), "initUpDateType()");
        }
    }

    private Dialog mdialog;

    /***
     * 初始化版本迭代界面
     */
    private void initUpdateVersion() {
        ZYDialog.Builder builder = new ZYDialog.Builder(getActivity());
        if ("1".equals(updateType)) {
//            mdialog = new android.app.AlertDialog.Builder(getActivity())
            builder.setTitle("当前最新版本:" + versionNum)
                    .setMessage("\n" +
                            "更新内容：\n" +
                            "\n" +
                            updateMessage +
                            "\n" +
                            "\n")
                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            updateVersion();
                        }
                    })
                    .setCancelAble(false);
            builder.create().show();
        } else {
            builder.setTitle("当前最新版本:" + versionNum)
                    .setMessage("\n" +
                            "更新内容：\n" +
                            "\n" +
                            updateMessage +
                            "\n" +
                            "\n")
                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            updateVersion();
                        }
                    })
                    .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setCancelAble(false);
            builder.create().show();
        }
    }

    private File file;
    private static final String KAQ_DISK_PATH = Environment.getExternalStorageDirectory().getPath() + "/kaq/APK/";

    /***
     * 下载功能模块
     */
    private void updateVersion() {
        /**设置下载dialog*/
//        mdialog.cancel();

        /**下载功能*/
        String apkname = downurl.substring(downurl.lastIndexOf("/") + 1);//文件名
        file = new File(KAQ_DISK_PATH + apkname);
        /**判断父目录是否存在*/
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        /**文件是否已存在，如果存在则转跳安装方法，不存在则去下载*/
        if (file.exists()) {
            updataApp();
        } else {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setTitle("当前最新版本:" + versionNum);
            dialog.setMessage("\n" +
                    "更新内容：\n" +
                    "\n" +
                    updateMessage +
                    "\n" +
                    "\n" +
                    "正在下载：");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(100);
            /**判断是否问强制更新*/
            if ("1".equals(updateType)) {
                dialog.setCancelable(false);
            } else {
                dialog.setButton(ProgressDialog.BUTTON_POSITIVE, "后台更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        initData();
                    }
                });
                dialog.setCanceledOnTouchOutside(false);
            }

            dialog.show();
            /**下载器设置*/
            JFileDownloader downloader = new JFileDownloader(downurl, file.getAbsolutePath());
            downloader.setFileDownloadListener(new JFileDownloadListener() {
                @Override
                public void downloadProgress(int progress, double speed, long remainTime) {
                    if (dialog.isShowing()) {
                        dialog.setProgress(progress);
                    }
                }

                @Override
                public void downloadCompleted(File file, long downloadTime) {
                    dialog.cancel();
//                    Message message = new Message();
                    mHandler.sendEmptyMessage(UPDATA_VERSION_SUCCESS);
                }
            });
            /**启动下载线程*/
            new DownloaderThread(downloader).start();
        }
    }

    /***
     * App安装程序
     */
    private void updataApp() {
        String apkname = downurl.substring(downurl.lastIndexOf("/") + 1);
        file = new File(KAQ_DISK_PATH + apkname);
        if (file.exists()) {
            Intent install = new Intent();
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setAction(android.content.Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            startActivity(install);
        }
    }


    /***
     * 版本迭代线程内部类
     */
    class DownloaderThread extends Thread {
        private JFileDownloader downloader;

        public DownloaderThread(JFileDownloader downloader) {
            this.downloader = downloader;
        }

        @Override
        public void run() {
            try {
                downloader.startDownload();
            } catch (Exception e) {
                Log.i(TAG, "下载出错");
                e.printStackTrace();
            }
        }
    }

    private static final int UPDATA_VERSION_SUCCESS = 101;
    private final MyHandler mHandler = new MyHandler(this);

    /**
     * Handler静态内部类
     */
    private static class MyHandler extends Handler {
        private final WeakReference<MoreNewFragment> mFragment;

        public MyHandler(MoreNewFragment fragment) {
            mFragment = new WeakReference<>(fragment);
        }

        private MoreNewFragment fragment;

        @Override
        public void handleMessage(Message msg) {
            fragment = mFragment.get();
            if (msg.what == UPDATA_VERSION_SUCCESS) {
                fragment.updataApp();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        try {
//            if (shareFragment != null) {
//                shareFragment.onActivityResult(requestCode, resultCode, data);
//            }
//        } catch (Exception e) {
//            ExceptionUtils.ExceptionSend(e, "onActivityResult");
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

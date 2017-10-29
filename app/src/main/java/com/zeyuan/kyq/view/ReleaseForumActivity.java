package com.zeyuan.kyq.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.zeyuan.kyq.Entity.CareFollowEntity;
import com.zeyuan.kyq.Entity.ForumInfoEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.GridAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.ForumInfoBean;
import com.zeyuan.kyq.bean.PostForumBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.FragmentMoreCallBack;
import com.zeyuan.kyq.biz.manager.IntegrationManager;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.fragment.main.ChooseReleaseCircleFragment;
import com.zeyuan.kyq.utils.CDNHelper;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DecryptUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.PhotoUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.SharePrefUtil;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CustomScrollView;
import com.zeyuan.kyq.widget.MyGridView;
import com.zeyuan.kyq.widget.MyLayout;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 发布帖子
 */
public class ReleaseForumActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, ViewDataListener,
        AdapterView.OnItemLongClickListener, HttpResponseInterface, FragmentMoreCallBack, MyLayout.OnSoftKeyboardListener {

    private static final String TAG = "ReleaseForumActivity";
    private static final int REQUEST_PICK = 0;//选取图片的请求码
    private static final int REQUEST_SELECT_FRIENDS = 1;//选择@好友的请求码
    private TextView releaseForum;
    private MyGridView gridView;//show the photo which is choosed
    private List<String> selectedPicture;//containts  photo which is choosed path
    private GridAdapter adapter;
    private View v_body;
    private CustomScrollView sv;
    private View v_title;
    private MyLayout mLayout;
    private View view_selected_friends;
    private TextView tv_selected_friends;
    private ArrayList<CareFollowEntity> selected_UserInfos = new ArrayList<>();//选择@的好友
    private boolean isForumEdit;//是否为编辑自己的帖子
    private String forumId;//帖子id
    private View view_bottom;//底部栏

    public View getV_body() {
        return v_body;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_forum);
        try {
            selectedPicture = new ArrayList<>();
            initView();
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ReleaseForumActivity");
        }
    }


    private EditText title;
    private EditText content;
    /**
     * 点击展开收起 关注的圈子
     */
    // private LinearLayout ll;

    private ImageView delete_bt_content;//输入帖子标题右边的删除按钮

    private TextView deleteAll;//头部的文本
    //private Button chooseCirclesConfrim;
    private CheckBox cb;
    private View tv_selected_friends_tag;//选择了好友的标志
    private View iv_select_friends;
    private View iv_select_img;
    private View iv_switch_keyboard;

    private void initView() {
        try {
            isForumEdit = getIntent().getBooleanExtra("isForumEdit", false);
            sv = (CustomScrollView) findViewById(R.id.sv);
            v_title = findViewById(R.id.v_title);
            v_body = findViewById(R.id.v_body);
            cb = (CheckBox) findViewById(R.id.cb_show_detail);
            deleteAll = (TextView) findViewById(R.id.delte_all);
            delete_bt_content = (ImageView) findViewById(R.id.delete_bt_content);
            gridView = (MyGridView) findViewById(R.id.show_pic);
            releaseForum = (TextView) findViewById(R.id.tv_release);
            content = (EditText) findViewById(R.id.content);
            title = (EditText) findViewById(R.id.titles);
            mLayout = (MyLayout) findViewById(R.id.my_layout);
            view_selected_friends = findViewById(R.id.view_selected_friends);
            tv_selected_friends = (TextView) findViewById(R.id.tv_selected_friends);
            view_bottom = findViewById(R.id.temp_bottom);
            tv_selected_friends_tag = findViewById(R.id.tv_selected_friends_tag);
            iv_select_friends = findViewById(R.id.iv_select_friends);
            iv_select_img = findViewById(R.id.iv_select_img);
            iv_switch_keyboard = findViewById(R.id.iv_switch_keyboard);
            view_selected_friends.setOnClickListener(this);
            mLayout.setOnSoftKeyboardListener(this);
            ImageView back = (ImageView) findViewById(R.id.btn_back);
            releaseForum.setOnClickListener(this);
            delete_bt_content.setOnClickListener(this);
            back.setOnClickListener(this);
            adapter = new GridAdapter(this, selectedPicture);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(this);
            gridView.setOnItemLongClickListener(this);
            deleteAll.setOnClickListener(this);
            iv_select_friends.setOnClickListener(this);
            iv_select_img.setOnClickListener(this);
            iv_switch_keyboard.setOnClickListener(this);
            setForumData();
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ReleaseForumActivity");
        }
    }

    private void setForumData() {
        if (isForumEdit) {//如果是编辑帖子，则先获取帖子数据
            forumId = getIntent().getStringExtra(Const.FORUM_ID);
            Factory.postPhp(ReleaseForumActivity.this, Const.PApi_getThreadInfo);
        } else if (SharePrefUtil.isHadForumData(ReleaseForumActivity.this)) {//有缓存数据
            String[] ss = SharePrefUtil.getForumData(ReleaseForumActivity.this);
            title.setText(ss[0]);
            content.setText(ss[1]);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btn_back: {
                    onBackPressed();
                    break;
                }
                case R.id.tv_release: {
                    getString();
                    break;
                }
                case R.id.delete_bt_content: {
                    title.setText("");
                    break;
                }
                case R.id.delte_all: {
                    String temp = content.getText().toString();
                    if (!TextUtils.isEmpty(temp)) {
                        setDeleteHint();
                    }
                    break;
                }
                case R.id.view_selected_friends:
                    selectFriends();
                    break;
                case R.id.iv_select_friends:
                    selectFriends();
                    break;
                case R.id.iv_select_img:
                    selectImg();
                    break;
                case R.id.iv_switch_keyboard:
                    //dismissInput();
                    switchInput();
                    break;

            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ReleaseForumActivity");
        }
    }

    //选择图片
    private void selectImg() {
        int index = adapter.getCount();
        if (index >= 10) {
            showToast("最多上传9张图片");
            return;
        }
        selectedIndex = index;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , STORAGE_AND_CAMERA_PERMISSIONS);
        } else {
            Intent intent = new Intent(this, SelectPictureActivity.class);
            index = 9 - index;
            intent.putExtra(SelectPictureActivity.INTENT_MAX_NUM, index);
            startActivityForResult(
                    intent, REQUEST_PICK);
        }
    }

    //选择@的好友
    private void selectFriends() {
        if (isForumEdit) {
            showToast("编辑帖子时不能修改此项哦！");
        } else {
            Intent intent = new Intent(this, SelectFriendsActivity.class);
            if (selected_UserInfos != null && selected_UserInfos.size() > 0) {
                intent.putParcelableArrayListExtra("selected_UserInfos", selected_UserInfos);
            }
            startActivityForResult(intent, REQUEST_SELECT_FRIENDS);
        }
    }

    private void setFinishHint() {

        ZYDialog dialog = new ZYDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确认退出？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ReleaseForumActivity.this.finish();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();

    }

    @Override
    public void onBackPressed() {
        String temp = content.getText().toString();
        String temp1 = title.getText().toString();
        if ((!TextUtils.isEmpty(temp) || !TextUtils.isEmpty(temp1))) {
            setFinishHint();
        } else {
            finish();
//            super.onBackPressed();
        }

    }

    private void setDeleteHint() {
        ZYDialog dialog = new ZYDialog.Builder(this)
                .setTitle("确认清除所有内容？")
                .setMessage("清除后所填写内容将会被删除，且无法找回")
                .setPositiveButton("确认清除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        content.setText("");
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }


    /*
    * 根据flag判断是编辑or新发布，并发起请求
    * */
    private void getFactoryForFlag(int flag) {
        try {
            if (flag == Const.PApi_ThreadForMoreCircle)
                Factory.postPhp(this, flag);
            else if (flag == Const.PApi_editThread)
                Factory.postPhp(this, flag);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "getFactoryForFlag");
        }


    }

    /**
     * 发布帖子
     */
    private void postForum(final int flag) {
        try {
            releaseForum.setClickable(false);
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("正在发布帖子");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            if (selectedPicture != null && selectedPicture.size() > 0) {//说明是有图片
                uploadPhoto(flag);
            } else {
                getFactoryForFlag(flag);
            }
        } catch (Exception e) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            ExceptionUtils.ExceptionToUM(e, this, "ReleaseForumActivity");
        }
    }

    private List<String> oldUrls = new ArrayList<>();//这个里面装的是编辑帖子时，帖子里面的老图片
    private List<String> urls;//这个里面装着已经上传到cdn的url
    //    private int index = 0; //这个表示长传成功图片的数量
    private ProgressDialog mProgressDialog;
    private File tempFile;

    private void uploadPhoto(final int flag) {
        try {
            urls = new ArrayList<>();
            selectedPicture.removeAll(oldUrls);//oldurls里的地址已经是网络地址了，不需要再上传一遍
            final int index = selectedPicture.size();
            if (index == 0) {//说明没有新添加图片
                urls.addAll(oldUrls);//加上之前帖子里有图片
                getFactoryForFlag(flag);
                return;
            }
            Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                    for (int i = 0; i < selectedPicture.size(); i++) {
                        final CDNHelper get = new CDNHelper(ReleaseForumActivity.this);
                        try {
                            String imageName = getImgName(ReleaseForumActivity.this, false);
                            //上传大图
                            final File big = PhotoUtils.scal(selectedPicture.get(i), PhotoUtils.SCAL_IMAGE_100);
                            get.uploadFile(big.getPath(), imageName, new SaveCallback() {
                                @Override
                                public void onProgress(String s, int i, int i1) {
                                }

                                @Override
                                public void onFailure(String s, OSSException e) {
                                }

                                @Override
                                public void onSuccess(String s) {
                                    urls.add(get.getResourseURL());
                                    try {
                                        big.delete();
                                    } catch (Exception e) {
                                        ExceptionUtils.ExceptionSend(e, "删除临时图片（大）失败");
                                    }
                                    if (urls.size() == index) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                urls.addAll(oldUrls);//加上之前帖子里有图片
                                                getFactoryForFlag(flag);

                                            }
                                        });
                                    }
                                    LogCustom.i(Const.TAG.ZY_OTHER, "大图的URL:" + urls.toString());
                                }
                            });

                            //上传缩略图
                            final CDNHelper gets = new CDNHelper(ReleaseForumActivity.this);
                            final File small = PhotoUtils.scal(selectedPicture.get(i), PhotoUtils.SCAL_IMAGE_30);
                            gets.uploadFile(small.getPath(), insertThumb(imageName), new SaveCallback() {
                                @Override
                                public void onSuccess(String s) {
                                    try {
                                        small.delete();
                                    } catch (Exception e) {
                                        ExceptionUtils.ExceptionSend(e, "删除临时图片（小）失败");
                                    }
                                }

                                @Override
                                public void onProgress(String s, int i, int i1) {
                                }

                                @Override
                                public void onFailure(String s, OSSException e) {
                                }
                            });

                        } catch (Exception exp) {
                            ExceptionUtils.ExceptionToUM(exp, ReleaseForumActivity.this, "ReleaseForumActivity");
                            if (mProgressDialog != null) {
                                mProgressDialog.dismiss();
                            }
                        }
                    }
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Integer integer) {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });

        } catch (Exception e) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            ExceptionUtils.ExceptionToUM(e, this, "ReleaseForumActivity");
        }
    }

    private void getString() {
        try {
            Title = title.getText().toString().trim();
            if (TextUtils.isEmpty(Title)) {
                showToast("请输入标题");
                return;
            }
            Content = content.getText().toString().trim();
            if (TextUtils.isEmpty(Content)) {
                Content = "";
            }

            dismissInput();
            toReleaseForum();

        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ReleaseForumActivity");

        }
    }

    /***
     *
     * 判断是否需要选择主题分类
     */
    private void toReleaseForum() {
        try {
            if (TextUtils.isEmpty(forumId))//如果是新发布帖子，则需要选择圈子
                getReleaseForumFragment();
            else//如果是编辑帖子，则直接发布
                postForum(Const.PApi_editThread);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "initBlur");
        }

    }

    //    private ChooseCircleFragment fm;
    private ChooseReleaseCircleFragment chooseReleaseCircleFragment;

    private void getReleaseForumFragment() {
        try {
//            fm = ChooseCircleFragment.getInstance(this, circleIds);
            //       fm.show(getFragmentManager(), ChooseCircleFragment.type);
            if (chooseReleaseCircleFragment == null) {
                chooseReleaseCircleFragment = new ChooseReleaseCircleFragment();
            }
            chooseReleaseCircleFragment.setCallback(this);
            if (chooseReleaseCircleFragment.getDialog() == null || !chooseReleaseCircleFragment.getDialog().isShowing())//避免用户双击发布按钮导致闪退
                chooseReleaseCircleFragment.show(getFragmentManager(), "ChooseReleaseCircleFragment");
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "fm");
        }
    }

    private String circleIdString = "";//需要发布的圈子id

    @Override
    public void dataCallBack(String str, int flag, String tag, List<String> listForData, Map<String, Integer> map, Object obj) {

        circleIdString = str;
        if (!TextUtils.isEmpty(circleIdString))
            postForum(Const.PApi_ThreadForMoreCircle);
        else
            showToast("发布失败！");

    }

    protected void initTitlebar(String title) {
        try {
            ImageView back = (ImageView) findViewById(R.id.title1).findViewById(R.id.btn_back);
            back.setOnClickListener(new ClickBack());
            TextView title_tv = (TextView) findViewById(R.id.title1).findViewById(R.id.title);
            title_tv.setText(title);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ReleaseForumActivity");
        }
    }


    private String Title;//
    private String Content;//
    private String CircleID;//

    private String URLNum;//表示多少张图片的url
    private String URL;//这个是图片的url 如果一张就是url0 二张 就是url1

    //    private String InfoName = "dafdafdsa"+ new Random().nextInt(100);
    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        try {
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
            if (tag == Const.PApi_getThreadInfo) {
                if (!TextUtils.isEmpty(forumId))
                    map.put("tid", forumId);
            } else if (tag == Const.PApi_editThread) {//编辑帖子参数
                map.put("tid", forumId);
                map.put(Contants.InfoName, UserinfoData.getInfoname(this));
                map.put(Contants.Title, Title);
                String jiami = DecryptUtils.encode(Content);
                map.put(Contants.Content, jiami);
                if (urls != null && urls.size() > 0) {
                    map.put(Contants.URLNum, urls.size() + "");
                    for (int i = 0; i < urls.size(); i++) {
                        map.put(Contants.URL + i, urls.get(i) + "");
                    }
                }
                if (cb.isChecked()) {
                    map.put(Const.IsAttachMedRecord, "1");
                } else {
                    map.put(Const.IsAttachMedRecord, "0");
                }
            } else if (tag == Const.PApi_ThreadForMoreCircle) {//发帖参数
                map.put(Contants.InfoName, UserinfoData.getInfoname(this));
                map.put(Contants.Title, Title);
                String jiami = DecryptUtils.encodeAndURL(Content);
                map.put(Contants.Content, jiami);
                map.put(Contants.CircleID, circleIdString);
                map.put("RemindUid", getRemindUid());
                if (urls != null && urls.size() > 0) {
                    map.put(Contants.URLNum, urls.size() + "");
                    for (int i = 0; i < urls.size(); i++) {
                        map.put(Contants.URL + i, urls.get(i) + "");
                    }
                }else {
                    map.put(Contants.URLNum, "0");
                }
                if (cb.isChecked()) {
                    map.put(Const.IsAttachMedRecord, "1");
                } else {
                    map.put(Const.IsAttachMedRecord, "0");
                }
            }
        } catch (Exception e) {
            showToast("参数错误！");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
//        if (flag == Const.EPostForumMoreCircle) {
//            List<String> list = new ArrayList<>();
//            list.add(Contants.InfoID);
//            list.add(Const.InfoID);
//            list.add(Contants.InfoName);
//            list.add(UserinfoData.getInfoname(this));
//            list.add(Contants.Title);
//            list.add(Title);
//            String jiami = DecryptUtils.encode(Content);
//            list.add(Contants.Content);
//            list.add(jiami);
//            list.add(Contants.CircleID);
//            list.add(circleIdString);
//            list.add("RemindUid");
//            list.add(getRemindUid());
//            // list.add(getCircleIds());
//            if (urls != null && urls.size() > 0) {
//                list.add(Contants.URLNum);
//                list.add(urls.size() + "");
//                for (int i = 0; i < urls.size(); i++) {
//                    list.add(Contants.URL + i);
//                    list.add(urls.get(i) + "");
//                }
//            }
//            if (cb.isChecked()) {
//                list.add(Const.IsAttachMedRecord);
//                list.add("1");
//            } else {
//                list.add(Const.IsAttachMedRecord);
//                list.add("0");
//            }
//            args = ConstUtils.getParamsForList(list);
//        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object t, int position) {
        try {
            //老接口Const.EPostForumMoreCircle
            if (position == Const.PApi_ThreadForMoreCircle) {
                PostForumBean postForumBean = (PostForumBean) t;
                LogUtil.i(TAG, t.toString());
                if (Contants.OK_DATA.equals(postForumBean.getIResult())) {
                    title.setText("");
                    content.setText("");
                    SharePrefUtil.clearForumData(ReleaseForumActivity.this);//清理缓存数据
                    String dis = postForumBean.getDisplayorder();
                    UserinfoData.setReleaseContent(null);
                    UserinfoData.setRepleaseTitle(null);
                    IntegrationManager.getInstance().addIntegration(postForumBean.getJf());
                    if (!TextUtils.isEmpty(dis)) {
                        int f = Integer.valueOf(dis);
                        if (f < 0) {
                            setShengHe(postForumBean.getIndex());//弹出需要审核提示
                        } else {
                            showToast("发布成功");

                            Intent intent = new Intent(ReleaseForumActivity.this, ForumDetailActivity.class);
                            intent.putExtra(Const.FORUM_ID, postForumBean.getIndex());
                            intent.putExtra(Const.FROM_FT, "1");
                            intent.putExtra(Const.JF, postForumBean.getJf());
                            intent.putExtra(Const.AUTHORID, UserinfoData.getInfoID(ReleaseForumActivity.this));
                            startActivity(intent);

                            finish();
                        }
                    } else {
                        showToast("发布成功");

                        Intent intent = new Intent(ReleaseForumActivity.this, ForumDetailActivity.class);
                        intent.putExtra(Const.FORUM_ID, postForumBean.getIndex());
                        intent.putExtra(Const.FROM_FT, "1");
                        intent.putExtra(Const.JF, postForumBean.getJf());
                        intent.putExtra(Const.AUTHORID, UserinfoData.getInfoID(ReleaseForumActivity.this));
                        startActivity(intent);

                        finish();
                    }

                } else {
                    showToast("发布失败");
                    releaseForum.setClickable(true);
                }
            } else if (position == Const.PApi_getThreadInfo) {//编辑帖子时先获取帖子详情
                ForumInfoBean forumInfoBean = (ForumInfoBean) t;
                if (Contants.OK_DATA.equals(forumInfoBean.getiResult())) {
                    ForumInfoEntity forumBaseEntity = forumInfoBean.getData();
                    if (!TextUtils.isEmpty(forumBaseEntity.getSubject()))
                        title.setText(forumBaseEntity.getSubject());
                    if (!TextUtils.isEmpty(forumBaseEntity.getMessage()))
                        content.setText(forumBaseEntity.getMessage());
                    if (forumBaseEntity.getImgUrl() != null) {
                        oldUrls.clear();
                        oldUrls.addAll(forumBaseEntity.getImgUrl());
                        selectedPicture.addAll(oldUrls);
                        adapter.updateDate(selectedPicture);
                    }
                }
            } else if (position == Const.PApi_editThread) {
                PostForumBean postForumBean = (PostForumBean) t;
                if (Contants.OK_DATA.equals(postForumBean.getIResult())) {
                    showToast("编辑成功");
                    setResult(RESULT_OK);
                    finish();
                }
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ReleaseForumActivity");
        }
    }

    private void setShengHe(final String index) {
        ZYDialog.Builder dialog = new ZYDialog.Builder(this);
        dialog.setTitle("发布成功");
        dialog.setMessage("您选择的部分圈子需要审核，帖子将在审核后自动发布。");
        //打开网络
        dialog.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //回调 callback
                dialog.cancel();
                startActivity(new Intent(ReleaseForumActivity.this, ForumDetailActivity.class)
                        .putExtra(Const.FORUM_ID, index)
                        .putExtra(Const.AUTHORID, UserinfoData.getInfoID(ReleaseForumActivity.this)));
                ReleaseForumActivity.this.finish();
            }
        });
        dialog.setCancelAble(false);
        dialog.create().show();
    }

    @Override
    public void showLoading(int tag) {
    }

    @Override
    public void hideLoading(int tag) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showError(int tag) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        if (tag == Const.PApi_ThreadForMoreCircle) {
            releaseForum.setClickable(true);
            showToast("发布失败！");
        } else if (tag == Const.PApi_editThread) {
            releaseForum.setClickable(true);
            showToast("编辑失败！");
        }
    }

    private static final int STORAGE_AND_CAMERA_PERMISSIONS = 12;
    private int selectedIndex = 0;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        try {
            startActivity(new Intent(this, ShowPhotoActivity.class).putExtra("list",
                    (Serializable) selectedPicture).putExtra("position", position));

        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "selectImage");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == STORAGE_AND_CAMERA_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, SelectPictureActivity.class);
                selectedIndex = 9 - selectedIndex;
                intent.putExtra(SelectPictureActivity.INTENT_MAX_NUM, selectedIndex);
                startActivityForResult(
                        intent, REQUEST_PICK);
            } else {
                Toast.makeText(this, "没有拍照或相册权限", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            switch (requestCode) {
                case REQUEST_PICK://选择图片的结果
                    if (resultCode == RESULT_OK) {
                        List<String> list = (ArrayList<String>) data
                                .getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);
                        LogUtil.i(TAG, "选择的图片uri是：" + list.toString());
                        selectedPicture.addAll(list);
                        adapter.updateDate(selectedPicture);
                    }
                    break;
                case REQUEST_SELECT_FRIENDS://选择@的好友的结果
                    if (resultCode == RESULT_OK) {
                        ArrayList<CareFollowEntity> temp = data.getParcelableArrayListExtra("selected_UserInfos");
                        if (selected_UserInfos == null) {
                            selected_UserInfos = temp;
                        } else {
                            selected_UserInfos.clear();
                            selected_UserInfos.addAll(temp);
                        }
                        setSelectedFriendsData();
                    }
                    break;
            }

        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ReleaseForumActivity");
        }
    }

    //设置已选择的好友
    private void setSelectedFriendsData() {
        String sus = "";
        if (selected_UserInfos != null && selected_UserInfos.size() > 0) {
            tv_selected_friends_tag.setSelected(true);
            for (CareFollowEntity su : selected_UserInfos
                    ) {
                sus = sus + "/" + su.getInfoName();
            }
        } else {
            tv_selected_friends_tag.setSelected(false);
        }
        tv_selected_friends.setText(sus);
    }

    //获取需要提醒的好友的id
    private String getRemindUid() {
        String uids = "";
        if (selected_UserInfos != null && selected_UserInfos.size() > 0) {
            for (CareFollowEntity su : selected_UserInfos
                    ) {
                if (TextUtils.isEmpty(uids))
                    uids = su.getInfoID() + "";
                else
                    uids = uids + "," + su.getInfoID();
            }
        }

        return uids;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tempFile != null) {
            tempFile.delete();//删除掉 文件
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        String temp = content.getText().toString();
        String temp1 = title.getText().toString();
        LogUtil.i(TAG, "onPause");
        if (!isForumEdit && (!TextUtils.isEmpty(temp) || !TextUtils.isEmpty(temp1))) {//保存输入内容
            SharePrefUtil.saveForumData(this, temp1, temp);
        } else if (!isForumEdit && TextUtils.isEmpty(temp) && TextUtils.isEmpty(temp1)) {//清除以保存的输入内容
            SharePrefUtil.clearForumData(this);
        }

    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
       /* if (position == adapter.getCount() - 1) {
            return true;
        }*/
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(ReleaseForumActivity.this, AlertDialog.THEME_HOLO_LIGHT);
            builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectedPicture.remove(position);
                    if (position < oldUrls.size())
                        oldUrls.remove(position);
                    adapter.updateDate(selectedPicture);
                }
            });
            builder.create().show();
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ReleaseForumActivity");
        }
        return true;
    }

    private boolean flag = false;// 这个控制隐藏键盘的时候 回调只被调用一次

    @Override
    public void onShown() {
        try {
            flag = true;
            //view_bottom.setVisibility(View.VISIBLE);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    SystemClock.sleep(20);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    sv.scrollTo(0, v_title.getTop());
                }
            }.execute(new Void[]{});
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ForumDetailActivity");
        }
    }

    @Override
    public void onHidden() {
        if (flag) {
            flag = false;
            //  view_bottom.setVisibility(View.GONE);
        }
    }

    //开关键盘
    private void switchInput() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                if (flag && (imm.isActive(content) || imm.isActive(title))) {
                    imm.hideSoftInputFromWindow(content.getWindowToken(), 0);
                } else {
                    if (content.isFocused()) {
                        imm.showSoftInput(content, 0);
                    } else if (title.isFocused()) {
                        imm.showSoftInput(title, 0);
                    } else {
                        title.requestFocus();
                        if (!TextUtils.isEmpty(title.getText()))
                            title.setSelection(title.getText().length());
                        imm.showSoftInput(title, 0);
                    }
                }
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "输入法开关错误");
        }
    }

    private void dismissInput() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && (imm.isActive(content) || imm.isActive(title))) {
                imm.hideSoftInputFromWindow(content.getWindowToken(), 0);
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "输入法收起错误");
        }
    }

    @Override
    public void finish() {
        try {
            int form = getIntent().getIntExtra(Const.INTENT_FROM, 0);
            if (form == Const.FM) {
                afterFinish();
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "ReleaseForum finish");
        }
        super.finish();
    }

}

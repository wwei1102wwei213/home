package com.zeyuan.kyq.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.RecordPhotoAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.utils.CDNHelper;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ConstUtils;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DecryptUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.PhotoUtils;
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

/**
 * Created by Administrator on 2017-11-12.
 */

public class AddCommentActivity extends BaseActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener
        ,MyLayout.OnSoftKeyboardListener,View.OnClickListener,HttpResponseInterface {

    private static final int REQUEST_PICK = 0;
    private static final int REQUEST_SUMMARY = 1;
    //记录分类标识
    private int type;
    //已选择图片路径列表
    private List<String> selectedPicture;
    //图片选择适配器
    private RecordPhotoAdapter adapter;
    //有返回的启动标识
    private boolean REQUEST_FLAG = false;
    //启动返回时是否有修改
    private boolean Request_Is_Changed = false;
    //选择的数据
//    private ArrayList<String> check = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        initType();
        initView();
        initListener();
        initData();
    }

    //标题文本框
    private TextView tv_title;
    //设置记录类型
    private void initType(){
        type = getIntent().getIntExtra(Const.INTENT_ADD_FLAG_TYPE, 1);
        REQUEST_FLAG = getIntent().getBooleanExtra(Const.RECORD_REQUEST_FLAG, false);
        tv_title = (TextView)findViewById(R.id.tv_title);
        String titleName;
        if (type==1){
            titleName = "项目";
        } else if (type==2){
            titleName = "医生";
        } else {
            titleName = "医院";
        }
        tv_title.setText("推荐"+titleName);
    }

    //备注输入框
    private EditText et_remark;
    //医院输入框
    private EditText et_hospital;
    //保存按钮
    private TextView tv_save;
    //添加照片控件
    private MyGridView gv;
    //监听输入法控件
    private MyLayout mLayout;
    //医院区域框
    private TextView tv_type_name,tv_type_txt;
    //滑动器
    private CustomScrollView sv;
    //新增TYPE选项
    private TextView tv_type_1,tv_type_2,tv_type_3;
    private void initView(){
        tv_save = (TextView)findViewById(R.id.tv_save);

        et_remark = (EditText)findViewById(R.id.et_remark);
        et_hospital = (EditText)findViewById(R.id.et_hospital);

        tv_type_name = (TextView) findViewById(R.id.tv_type_name);
        tv_type_txt = (TextView) findViewById(R.id.tv_type_txt);
        gv = (MyGridView)findViewById(R.id.gv);
        selectedPicture = new ArrayList<>();
        adapter = new RecordPhotoAdapter(this,selectedPicture);
        gv.setAdapter(adapter);
        sv = (CustomScrollView)findViewById(R.id.sv);
        mLayout = (MyLayout)findViewById(R.id.my_layout);

        tv_type_1 = (TextView) findViewById(R.id.tv_add_type_1);
        tv_type_2 = (TextView) findViewById(R.id.tv_add_type_2);
        tv_type_3 = (TextView) findViewById(R.id.tv_add_type_3);


        tv_type_1.setOnClickListener(this);
        tv_type_2.setOnClickListener(this);
        tv_type_3.setOnClickListener(this);

        setTypeView(type);
    }
//1.项目2.医生3.医院
    //设置监听事件
//    String hint = "";
    String[] hints = {"请输入项目名称","请输入医生姓名", "请输入医院名称"};
    String[] typeNames = {"项目名称","医生姓名", "医院名称" };
    String[] typeTxts = {"对项目的描述","对医生的描述", "对医院的描述" };
    String[] mHints = {
            "输入您推荐项目（或有特殊疗效的医生）的理由及具体情况，提交后，我们会尽快认证，并尽快上线该项目（或医生）。",
            "输入医生所在医院及你知道的更多医生信息，提交后，我们会尽快认证，并尽快上线该医生。",
            "在此输入内容，提交后，我们会尽快认证，并尽快上线该医院."
    };
    private void initListener(){
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_save.setOnClickListener(this);
        mLayout.setOnSoftKeyboardListener(this);
        gv.setOnItemClickListener(this);
        gv.setOnItemLongClickListener(this);
        et_hospital.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String temp = "";
                    if (!TextUtils.isEmpty(et_hospital.getText())) {
                        temp = et_hospital.getText().toString();
                        et_hospital.setHint("");
                        et_hospital.setText(temp);
                        et_hospital.setSelection(temp.length());
                    }

                } else {
                    if (TextUtils.isEmpty(et_hospital.getText())) {
                        et_hospital.setHint(hints[type-1]);
                    }
                }
            }
        });
        et_remark.setHint(mHints[type-1]);
        et_hospital.setHint(hints[type-1]);
    }

    private void initData(){}

    private void setTypeView(int index){
        switch (index){
            case 1:
                tv_type_1.setSelected(true);
                tv_type_2.setSelected(false);
                tv_type_3.setSelected(false);
                break;
            case 2:
                tv_type_1.setSelected(false);
                tv_type_2.setSelected(true);
                tv_type_3.setSelected(false);
                break;
            case 3:
                tv_type_1.setSelected(false);
                tv_type_2.setSelected(false);
                tv_type_3.setSelected(true);
                break;
        }
        tv_type_name.setText(typeNames[index-1]);
        et_hospital.setText("");
        tv_type_txt.setText(typeTxts[index-1]);
    }

    private int InfoID = Integer.valueOf(UserinfoData.getInfoID(this));

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_save:
                toSaveData();
                break;
            case R.id.tv_add_type_1:
                if (type!=1){
                    type = 1;
                    setTypeView(1);
                }
                break;
            case R.id.tv_add_type_2:
                if (type!=2){
                    type = 2;
                    setTypeView(2);
                }
                break;
            case R.id.tv_add_type_3:
                if (type!=3){
                    type = 3;
                    setTypeView(3);
                }
                break;
        }
    }



    private void toSaveData(){
        toSave();
    }

    private ProgressDialog mProgressDialog;
    private void toSave(){
        if (TextUtils.isEmpty(et_hospital.getText().toString())){
            showToast("请输入名称");
            return;
        }
        /*&&(selectedPicture==null||selectedPicture.size()==0)*/
        if (TextUtils.isEmpty(et_remark.getText().toString())){
            showToast("请输入描述");
            return;
        }
        toUpdateData();
    }

    private void toUpdateData(){
        tv_save.setClickable(false);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在保存");
        mProgressDialog.show();
        if (selectedPicture != null && selectedPicture.size() > 0) {//说明是有图片的
            uploadPhoto();
        } else {
            toPost();
        }
    }

    private void toPost(){
        Factory.postPhp(this, Const.PApi_addProjectInfo);
    }

    //图片url集合
    private List<String> urls;
    private void uploadPhoto() {
        try {
            urls = new ArrayList<>();
            final int index = selectedPicture.size();
            for (int i = 0; i < selectedPicture.size(); i++) {
                final CDNHelper get = new CDNHelper(this);
                try {
                    String imageName = getImgName(this, false);
                    final File big = PhotoUtils.scal(selectedPicture.get(i), PhotoUtils.SCAL_IMAGE_100);
                    get.uploadFile(big.getPath(),imageName, new SaveCallback() {
                        @Override
                        public void onProgress(String s, int i, int i1) {}
                        @Override
                        public void onFailure(String s, OSSException e) {}
                        @Override
                        public void onSuccess(String s) {
                            urls.add(get.getResourseURL());
                            try {
                                big.delete();
                            }catch (Exception e){
                                ExceptionUtils.ExceptionSend(e,"删除临时图片（大）失败");
                            }
                            if (urls.size() == index) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        toPost();
                                    }
                                });
                            }
                        }
                    });

                    final CDNHelper gets = new CDNHelper(this);
                    final File small = PhotoUtils.scal(selectedPicture.get(i), PhotoUtils.SCAL_IMAGE_30);
                    gets.uploadFile(small.getPath(),insertThumb(imageName), new SaveCallback() {
                        @Override
                        public void onSuccess(String s) {
                            try {
                                small.delete();
                            }catch (Exception e){
                                ExceptionUtils.ExceptionSend(e,"删除临时图片（小）失败");
                            }
                        }
                        @Override
                        public void onProgress(String s, int i, int i1) {}
                        @Override
                        public void onFailure(String s, OSSException e) {}
                    });

                } catch (Exception e) {
                    ExceptionUtils.ExceptionToUM(e, this, "ReleaseForumActivity");
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "ReleaseForumActivity");
        }
    }






    @Override
    public Map getParamInfo(int tag) {
        Map<String,String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        if (tag == Const.PApi_addProjectInfo){
            if (urls!=null&&urls.size()>0){
                map.put("PicUrl", ConstUtils.getParamsForPic(urls));
            }
            String Pname = et_hospital.getText().toString();
            if (!TextUtils.isEmpty(Pname)){
                map.put("Pname",Pname);
            }
            String Ptext = et_remark.getText().toString();
            if (!TextUtils.isEmpty(Ptext)){
                map.put("Ptext", DecryptUtils.encodeAndURL(Ptext));
            }
            map.put("TypeID",type+"");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PApi_addProjectInfo ){
            PhpUserInfoBean bean = (PhpUserInfoBean)response;
            if (Const.RESULT.equals(bean.getiResult())){
//                showToast("新增成功");
                if (REQUEST_FLAG){
                    Request_Is_Changed = true;
                }
                exit = 1;
                startActivity(new Intent(this, AddCommentSuccessActivity.class).putExtra("add_success_type",0));
                finish();
            }  else {
                showToast("新增失败");
            }
        }
    }

    @Override
    public void showLoading(int flag) {

    }

    @Override
    public void hideLoading(int flag) {
        if (flag == Const.PApi_addProjectInfo ){
            if (mProgressDialog!=null) mProgressDialog.dismiss();
            tv_save.setClickable(true);
        }
    }

    @Override
    public void showError(int flag) {
        if (flag == Const.PApi_addProjectInfo ){
            if (mProgressDialog!=null) mProgressDialog.dismiss();
            tv_save.setClickable(true);
        }
        showToast("网络请求失败");
    }

    private static final int STORAGE_AND_CAMERA_PERMISSIONS = 12;
    private int selectedIndex = 0;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId()==R.id.gv){
            try {
                if (position == selectedPicture.size()) {
                    int index = adapter.getCount();
                    if (index >= 10) {
                        showToast("最多上传9张图片");
                        return;
                    }
                    selectedIndex = index;
                    if ( ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                                    PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                , STORAGE_AND_CAMERA_PERMISSIONS);
                    } else {
                        Intent intent = new Intent(this, SelectPictureActivity.class);
                        index = 10 - index;
                        intent.putExtra(SelectPictureActivity.INTENT_MAX_NUM, index);
                        startActivityForResult(
                                intent, REQUEST_PICK);
                    }
                } else {
                    startActivity(new Intent(this, ShowPhotoActivity.class).putExtra("list",
                            (Serializable) selectedPicture).putExtra("position", position));
                    /*startActivityForResult(new Intent(this, AddPhotoSummaryActivity.class).putExtra("list",
                            (Serializable) selectedPicture).putExtra("position", position),REQUEST_SUMMARY);*/
                }
            }catch (Exception e){
                ExceptionUtils.ExceptionToUM(e, this, "ReleaseForumActivity");
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (position == adapter.getCount() - 1) {
            return true;
        }
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectedPicture.remove(position);
                    adapter.updateDate(selectedPicture);
                }
            });
            builder.create().show();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "ReleaseForumActivity");
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == STORAGE_AND_CAMERA_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, SelectPictureActivity.class);
                selectedIndex = 10 - selectedIndex;
                intent.putExtra(SelectPictureActivity.INTENT_MAX_NUM, selectedIndex);
                startActivityForResult(intent, REQUEST_PICK);
            } else {
                Toast.makeText(this, "没有拍照或相册权限", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean hasSymptomRemark = false;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_OK) {
                List list = (ArrayList<String>) data.getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);
                LogCustom.i("ZYS", "选择的图片uri是：" + list.toString());
                selectedPicture.addAll(list);
                adapter.updateDate(selectedPicture);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "RecordActivity onActivityResult");
        }
    }

    private boolean flag = false;// 这个控制隐藏键盘的时候 回调只被调用一次

    @Override
    public void onShown() {
        flag = true;
    }

    @Override
    public void onHidden() {
        if (flag) {
            flag = false;
        }
    }

    private int exit = 0;

    @Override
    public void finish() {
        if (exit==0){
            if (isChanged()){
                ZYDialog.Builder builder = new ZYDialog.Builder(this);
                builder.setTitle("提示")
                        .setMessage("不保存就退出吗？")
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                toSaveData();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                exit = 2;
                                dialog.dismiss();
                                finish();
                            }
                        }).create().show();
            } else {
                super.finish();
            }
//            super.finish();
        }else if (exit == 1){
            if (REQUEST_FLAG){
                this.setResult(1, getIntent().putExtra("isChanged", Request_Is_Changed));
            }
            super.finish();
        }else {
            super.finish();
        }

    }

    //是否有更改
    private boolean isChanged(){
        if (!TextUtils.isEmpty(et_hospital.getText().toString())
                ||!TextUtils.isEmpty(et_remark.getText().toString())
                ||(selectedPicture!=null&&selectedPicture.size()>0)){
            return true;
        }
        return false;
    }

}

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
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.PhotoUtils;
import com.zeyuan.kyq.utils.UiUtils;
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
        tv_title.setText(UiUtils.getRecordClassifyTitle(type));
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
    private View v_hospital;
    //滑动器
    private CustomScrollView sv;
    //新增TYPE选项
    private TextView tv_type_1,tv_type_2,tv_type_3,tv_type_4;
    private void initView(){
        tv_save = (TextView)findViewById(R.id.tv_save);

        et_remark = (EditText)findViewById(R.id.et_remark);
        et_hospital = (EditText)findViewById(R.id.et_hospital);

        v_hospital = findViewById(R.id.v_hospital);
        gv = (MyGridView)findViewById(R.id.gv);
        selectedPicture = new ArrayList<>();
        adapter = new RecordPhotoAdapter(this,selectedPicture);
        gv.setAdapter(adapter);
        sv = (CustomScrollView)findViewById(R.id.sv);
        mLayout = (MyLayout)findViewById(R.id.my_layout);

        tv_type_1 = (TextView) findViewById(R.id.tv_add_type_1);
        tv_type_2 = (TextView) findViewById(R.id.tv_add_type_2);
        tv_type_3 = (TextView) findViewById(R.id.tv_add_type_3);
        tv_type_4 = (TextView) findViewById(R.id.tv_add_type_4);

        setTypeView(type);
    }

    //设置监听事件
    String hint = "";
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
        if (type==1){
            hint = "请输入医生姓名";
        } else if (type==2){
            hint = "请输入医生姓名";
        } else if (type==3){
            hint = "请输入医院名称";
        } else {
            hint = "请输入名称";
        }
        et_hospital.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String temp = "";
                    if (!TextUtils.isEmpty(et_hospital.getText())) {
                        temp = et_hospital.getText().toString();
                    }
                    et_hospital.setHint("");
                    et_hospital.setText(temp);
                    et_hospital.setSelection(temp.length());
                } else {
                    if (TextUtils.isEmpty(et_hospital.getText())) {
                        et_hospital.setHint(hint);
                    }
                }
            }
        });
    }

    private void initData(){}

    private void setTypeView(int index){
        switch (index){
            case 1:
                tv_type_1.setSelected(true);
                tv_type_2.setSelected(false);
                tv_type_3.setSelected(false);
                tv_type_4.setSelected(false);
                break;
            case 2:
                tv_type_1.setSelected(false);
                tv_type_2.setSelected(true);
                tv_type_3.setSelected(false);
                tv_type_4.setSelected(false);
                break;
            case 3:
                tv_type_1.setSelected(false);
                tv_type_2.setSelected(false);
                tv_type_3.setSelected(true);
                tv_type_4.setSelected(false);
                break;
            case 4:
                tv_type_1.setSelected(false);
                tv_type_2.setSelected(false);
                tv_type_3.setSelected(false);
                tv_type_4.setSelected(true);
                break;
        }
    }

    private int InfoID = Integer.valueOf(UserinfoData.getInfoID(this));

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_save:
                toSaveData();
                break;
        }
    }



    private void toSaveData(){
        /*if (type == Const.RECORD_TYPE_13){
            if ((check==null||check.size()==0)&&!hasSymptomRemark && TextUtils.isEmpty(et_hospital.getText().toString())
                    &&TextUtils.isEmpty(et_doctor.getText().toString())
                    &&TextUtils.isEmpty(et_remark.getText().toString())
                    &&(selectedPicture==null||selectedPicture.size()==0)){
                showToast("请至少填写一项数据");
                return;
            }
            if (TextUtils.isEmpty(RecordTime)){
                showToast("请选择日期");
                return;
            }
            toUpdateData();
        } else if (type == Const.RECORD_TYPE_11 || type == Const.RECORD_TYPE_12){
            if ((check==null||check.size()==0)&& TextUtils.isEmpty(et_hospital.getText().toString())
                    &&TextUtils.isEmpty(et_doctor.getText().toString())
                    &&TextUtils.isEmpty(et_remark.getText().toString())
                    &&(selectedPicture==null||selectedPicture.size()==0)){
                showToast("请至少填写一项数据");
                return;
            }
            if (TextUtils.isEmpty(RecordTime)){
                showToast("请选择日期");
                return;
            }
            toUpdateData();
        } else if(type == Const.RECORD_TYPE_14){
            if (TextUtils.isEmpty(RecordTime)){
                showToast("请选择日期");
                return;
            }

            toUpdateData();

        } else if(type == Const.RECORD_TYPE_15){

            if (TextUtils.isEmpty(RecordTime)){
                showToast("请选择日期");
                return;
            }

            toUpdateData();
        } else {
            toSave();
        }*/
    }

    private ProgressDialog mProgressDialog;
    private void toSave(){

        /*if (TextUtils.isEmpty(RecordTime)){
            showToast("请选择日期");
            return;
        }

        if (TextUtils.isEmpty(et_hospital.getText().toString())
                &&TextUtils.isEmpty(et_doctor.getText().toString())
                &&TextUtils.isEmpty(et_remark.getText().toString())
                &&(selectedPicture==null||selectedPicture.size()==0)){
            showToast("请至少填写一项数据");
            return;
        }
        toUpdateData();*/
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
        switch (type){
            case Const.RECORD_TYPE_13:
                Factory.postPhp(this, Const.PAddStep2Perform);
                break;
            case Const.RECORD_TYPE_11:
                Factory.postPhp(this, Const.PAddTransferGen);
                break;
            case Const.RECORD_TYPE_12:
                Factory.postPhp(this, Const.PAddTransferRecord);
                break;
            case Const.RECORD_TYPE_14:
                Factory.postPhp(this, Const.PAddQuotaMasterSlave);
                break;
            case Const.RECORD_TYPE_15:
                Factory.postPhp(this, Const.PAddCancerMark);
                break;
            default:
                Factory.postPhp(this, Const.PAddPresentationOther);
                break;
        }
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




    private Map<String,String> geneValues;
    //获取基因配置数据
    private void getGeneData(){
        if (geneValues==null){
            geneValues = (Map<String,String>) Factory.getData(Const.N_DataGeneValues);
        }
    }
    private Map<String,String> transValues;
    //获取转移部位配置数据
    private void getTransData(){
        if (transValues==null){
            transValues =  (Map<String,String>)Factory.getData(Const.N_DataTransferPos);
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String,String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        String hospital = et_hospital.getText().toString();
        if (!TextUtils.isEmpty(hospital)){
            map.put("hospital",hospital);
        }

        if (urls!=null&&urls.size()>0){
            map.put("pic", ConstUtils.getParamsForPic(urls));
        }
        if (tag == Const.PAddPresentationOther){
            map.put("Type",type+"");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PAddPresentationOther || flag == Const.PAddStep2Perform
                || flag == Const.PAddTransferGen || flag == Const.PAddTransferRecord
                || flag == Const.PAddQuotaMasterSlave || flag == Const.PAddCancerMark){
            PhpUserInfoBean bean = (PhpUserInfoBean)response;
            if (Const.RESULT.equals(bean.getiResult())){
                showToast("保存成功");
                if (REQUEST_FLAG){
                    Request_Is_Changed = true;
                }
                exit = 1;
                finish();
            }else {
                showToast("保存失败");
            }
        }
    }

    @Override
    public void showLoading(int flag) {

    }

    @Override
    public void hideLoading(int flag) {
        if (flag == Const.PAddPresentationOther || flag == Const.PAddStep2Perform
                || flag == Const.PAddTransferGen || flag == Const.PAddTransferRecord
                || flag == Const.PAddQuotaMasterSlave){
            if (mProgressDialog!=null) mProgressDialog.dismiss();
            tv_save.setClickable(true);
        }
    }

    @Override
    public void showError(int flag) {
        if (flag == Const.PAddPresentationOther || flag == Const.PAddStep2Perform
                || flag == Const.PAddTransferGen || flag == Const.PAddTransferRecord
                || flag == Const.PAddQuotaMasterSlave){
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
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                                    PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}
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
        }else if (exit == 1){
            if (REQUEST_FLAG){
                this.setResult(Const.REQUEST_CODE_RECORD_ACTIVITY, getIntent().putExtra("isChanged", Request_Is_Changed));
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

package com.zeyuan.kyq.view;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.PersonalBean;
import com.zeyuan.kyq.Entity.PersonalEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.fragment.dialog.CityDialog;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.fragment.dialog.DigitDialog;
import com.zeyuan.kyq.utils.CDNHelper;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.PhotoUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CircleImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/22.
 *
 * 个人资料页面
 *
 * @author wwei
 */
public class PersonalDataActivity extends BaseActivity implements View.OnClickListener,HttpResponseInterface
        ,OnDismissListener,DialogFragmentListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent();
        setContentView(R.layout.activity_personal_data);
        initStatusBar();
        initView();
        initData();
    }

    //头像点击区域
    private View v_head;
    //头像控件
    private CircleImageView civ;
    //呢称点击控件
    private View v_name;
    //昵称控件
    private TextView tv_name;
    //地址点击区域
    private View v_where;
    //地址控件
    private TextView tv_where;
    //性别点击控件
    private View v_sex;
    //性别控件
    private TextView tv_sex;
    //年龄点击区域
    private View v_age;
    //年龄控件
    private TextView tv_age;
    //关系点击区域
    private View v_relationship;
    //关系控件
    private TextView tv_relationship;
    private void initView(){

        v_head = findViewById(R.id.v_head);
        civ = (CircleImageView)findViewById(R.id.iv_head_url);
        v_name = findViewById(R.id.v_name);
        tv_name = (TextView)findViewById(R.id.tv_name);
        v_where = findViewById(R.id.v_where);
        tv_where = (TextView)findViewById(R.id.tv_where);
        v_sex = findViewById(R.id.v_sex);
        tv_sex = (TextView)findViewById(R.id.tv_sex);
        v_age = findViewById(R.id.v_age);
        tv_age = (TextView)findViewById(R.id.tv_age);
        v_relationship = findViewById(R.id.v_relationship);
        tv_relationship = (TextView)findViewById(R.id.tv_relationship);

        v_head.setOnClickListener(this);
        v_name.setOnClickListener(this);
        v_where.setOnClickListener(this);
        v_sex.setOnClickListener(this);
        v_age.setOnClickListener(this);
        v_relationship.setOnClickListener(this);

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_save).setOnClickListener(this);
    }

    private int Age = 0;
    private int Sex = 0;
    private int IsSelf = 0;
    private String CityID;
    private String ProvinceID;
    private String InfoName;
    private String HeadUrl;
    private void setViews(PersonalEntity entity){

        if (entity==null) return;

        Age = entity.getUserAge();
        Sex = entity.getUserSex();
        IsSelf = entity.getIsSelf();
        CityID = entity.getCity();
        ProvinceID = entity.getProvinceID();
        InfoName = entity.getInfoName();
        HeadUrl = entity.getHeadUrl();

        tv_age.setText(Age+"");
        tv_sex.setText(Sex==0?"未填写":(Sex==1?"男":"女"));
        tv_relationship.setText(IsSelf==0?"未填写":(IsSelf==1?"本人":"其他人"));
        tv_name.setText(TextUtils.isEmpty(InfoName) ? "" : InfoName);
        Glide.with(this).load(HeadUrl).error(R.mipmap.default_avatar).into(civ);
        //设置城市
        tv_where.setText(getCityString(CityID,ProvinceID));
    }

    private String getCityString(String cID,String pID){
        String result = TextUtils.isEmpty(pID)?"":MapDataUtils.getCityName(pID);
        if (!TextUtils.isEmpty(cID)){
            if (TextUtils.isEmpty(result)){
                result = MapDataUtils.getCityName(cID);
            }else {
                result += ",";
                result += MapDataUtils.getCityName(cID);
            }
        }
        return  result;
    }

    private void initData(){
        Factory.postPhp(this, Const.PGetUserSelfForApp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.v_head:
                takePhoto();
                break;
            case R.id.v_name:
                showInputName();
                break;
            case R.id.v_sex:
                showSelectSex();
                break;
            case R.id.v_age:
                showInputAge();
                break;
            case R.id.v_relationship:
                showRelationship();
                break;
            case R.id.v_where:
                CityDialog dialog = new CityDialog();
                dialog.setOnOnCitySelListener(this);
                dialog.show(getFragmentManager(), Contants.CITY_DIALOG);
                break;
            case R.id.tv_save:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("正在保存");
                mProgressDialog.show();
                if (tempFile!=null){
                    updateAvatar(tempFile);
                }else {
                    Factory.postPhp(this,Const.PEditUserSelfForApp);
                }
                break;
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String,String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        if (tag == Const.PGetUserSelfForApp){

        }else if (tag == Const.PEditUserSelfForApp){
            map.put(Contants.InfoName, InfoName);
            map.put("oss_request_url",HeadUrl);
            map.put("City",CityID);
            map.put("ProvinceID",ProvinceID);
            map.put("Sex",Sex+"");
            map.put("IsSelf",IsSelf+"");
            map.put("Age",Age+"");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {

        if (flag == Const.PGetUserSelfForApp){
            PersonalBean bean = (PersonalBean)response;
            if (Const.RESULT.equals(bean.getiResult())){
                setViews(bean.getData());
            }
        } else if (flag == Const.PEditUserSelfForApp){
            PhpUserInfoBean bean = (PhpUserInfoBean)response;
            if (Const.RESULT.equals(bean.getiResult())){
                UserinfoData.saveUserDataChange(this,InfoName,HeadUrl,CityID);
                showToast("保存成功");
                finish();
            }else {
                showToast("保存失败");
            }
        }

    }


    @Override
    public void showLoading(int flag) {

    }

    private ProgressDialog mProgressDialog;
    @Override
    public void hideLoading(int flag) {
        if (flag == Const.PEditUserSelfForApp){
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        }
    }

    @Override
    public void showError(int flag) {
        showToast("网络请求失败");
    }

    @Override
    public void getDataFromDialog(DialogFragment dialog, String data, int position) {
        try {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment cityDialog = fragmentManager.findFragmentByTag(Contants.CITY_DIALOG);
            if (dialog==cityDialog){
                CityID = data;
                ProvinceID = position + "";
                tv_where.setText(getCityString(CityID,ProvinceID));
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"PersonalDataActivity getDataFromDialog");
        }
    }

    /**
     * 弹出选择性别的选择框
     */
    private void showRelationship() {
        try {
            new AlertView("与患者的关系", null, "取消", null,
                    new String[]{"本人", "其他人" },
                    this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    switch (position) {
                        case 0:
                            tv_relationship.setText("本人");
                            IsSelf = 1;
                            break;
                        case 1:
                            tv_relationship.setText("其他人");
                            IsSelf = 2;
                            break;
                    }
                }
            }).setOnDismissListener(this).setCancelable(true).show();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PersonalDataActivity");
        }
    }


    /**
     * 显示输入的年龄
     */
    private void showInputAge() {
        try {
            DigitDialog dialog1 = DigitDialog.newInstance(DigitDialog.AGE, null);
            dialog1.setListener(new DialogFragmentListener() {
                @Override
                public void getDataFromDialog(DialogFragment dialog, String data, int position) {
                    if (!TextUtils.isEmpty(data)){
                        tv_age.setText(data);
                        Age = Integer.valueOf(data);
                    }
                }
            });
            dialog1.show(getFragmentManager(), "age");
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    /**
     * 弹出选择性别的选择框
     */
    private void showSelectSex() {
        try {
            new AlertView("选择性别", null, "取消", null,
                    new String[]{"男", "女"},
                    this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    switch (position) {
                        case 0:
                            tv_sex.setText("男");
                            Sex = 1;
                            break;
                        case 1:
                            tv_sex.setText("女");
                            Sex = 2;
                            break;
                    }
                }
            }).setOnDismissListener(this).setCancelable(true).show();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PersonalDataActivity");
        }
    }

    private EditText etName;
    /**
     * 修改名字 弹出对话框
     */
    private void showInputName() {
        try {
            ViewGroup extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.alertext_form, null);
            etName = (EditText) extView.findViewById(R.id.etName);
            new AlertView(null, "请输入呢称", "取消", null, new String[]{"完成"},
                    this, AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    if (position == 0) {
                        String name = etName.getText().toString().trim();
                        if (!TextUtils.isEmpty(name)) {
                            InfoName = name;
                            tv_name.setText(InfoName);
                        }
                    }
                    closeKeyboard();
                }
            }).addExtView(extView).setOnDismissListener(this).setCancelable(true).show();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    @Override
    public void onDismiss(Object o) {
        closeKeyboard();
    }

    private InputMethodManager imm;
    /***
     * 关闭软键盘
     *
     */
    private void closeKeyboard() {
        //关闭软键盘
        if (etName != null) {
            if (imm == null) imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
        }
    }


    /**
     * 拍照用到的
     */
    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESOULT = 3;// 结果
    public static final int NONE = 0;
    public static final String IMAGE_UNSPECIFIED = "image/*";
    /**
     * 弹出底下的dialog
     */
    public void takePhoto() {
        try {
            new AlertView("上传头像方式", null, "取消", null,
                    new String[]{"拍照", "从相册中选择"},
                    this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    switch (position) {
                        case 0:
                            fromTP();
                            break;
                        case 1:
                            fromPic();
                            break;
                    }
                }
            }).setCancelable(true).show();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PersonalDataActivity");
        }
    }


    /**
     * 从拍照中获取图片
     */
    private void fromTP() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, PHOTOHRAPH);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PersonalDataActivity");
        }
    }

    /**
     * 从相册中获取图片
     */
    private void fromPic() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
            ComponentName componentName = intent.resolveActivity(getPackageManager());
            if (componentName != null) {
                startActivityForResult(intent, PHOTOZOOM);
            } else {
                Toast.makeText(this, "无法连接到相册", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PersonalDataActivity");
        }
    }

    private Uri tempUri = null;
    /***
     * 开启系统图库及裁剪功能
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        try {

            tempUri = uri;
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
            intent.putExtra("crop", "true");
            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            // outputX outputY 是裁剪图片宽高
            intent.putExtra("outputX", 600);
            intent.putExtra("outputY", 600);

            intent.putExtra("scale", true);
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//不启用人脸识别
            intent.putExtra("noFaceDetection", false);
            ComponentName componentName = intent.resolveActivity(getPackageManager());
            if (componentName != null) {
                startActivityForResult(intent, PHOTORESOULT);
            } else {
                Toast.makeText(this, "无法连接到系统裁剪功能", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PersonalDataActivity");
        }
    }


    /***
     * 隐式意图参数回传处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (resultCode == NONE) {
                Toast.makeText(this, R.string.choose_no_photo, Toast.LENGTH_SHORT).show();
                return;
            }
            // 拍照
            if (requestCode == PHOTOHRAPH) {
                try {
                    Uri uri = data.getData();
                    //设置文件保存路径这里放在跟目录下
                    Bitmap cameraPhoto = data.getExtras().getParcelable("data");//从流中得到图片
                    FileOutputStream foss = null;
                    tempFile = new File(Environment.getExternalStorageDirectory(),
                            getPhotoFileName());
                    try {
                        foss = new FileOutputStream(tempFile);
                        cameraPhoto.compress(Bitmap.CompressFormat.JPEG, 100, foss);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        if (foss != null) {
                            try {
                                foss.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    startPhotoZoom(Uri.fromFile(tempFile));
                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e,"requestCode");
                }
            }

            if (data == null)
                return;
            // 读取相册缩放图片
            if (requestCode == PHOTOZOOM) {
                startPhotoZoom(data.getData());
            }
            // 处理结果
            if (requestCode == PHOTORESOULT) {
                try {
                    Uri reUrl = data.getData();
                    if (reUrl == null&&tempUri!=null){
                        reUrl = tempUri;
                        tempUri = null;
                    }
                    Bitmap photo = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(reUrl));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int options = 100;
                    photo.compress(Bitmap.CompressFormat.JPEG, options, baos);
                    FileOutputStream fos = null;
                    tempFile = new File(Environment.getExternalStorageDirectory(),
                            getPhotoFileName());
                    try {
                        fos = new FileOutputStream(tempFile);
                        fos.write(baos.toByteArray());
                        fos.flush();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    civ.setImageBitmap(photo);

                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e,"photoResult");
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PersonalDataActivity");
        }
    }

    private File tempFile;//处理图片临时文件
    /***
     * 上传图像到阿里云
     * 大小头像都需上传
     * 小头像压缩到100*100
     * @param photo 图像文件
     */
    private void updateAvatar(File photo) {
        try {
            final CDNHelper getDemo = new CDNHelper(this);
            final CDNHelper littleDemo = new CDNHelper(this);
            try {
                String imageName = getImgName(this, true);
                LogCustom.i(Const.TAG.ZY_OTHER, "图片地址：" + photo.getPath());
                final File bigFile = PhotoUtils.scal(photo.getPath(), PhotoUtils.SCAL_IMAGE_80);
                getDemo.uploadFile(bigFile.getPath(), imageName, new SaveCallback() {
                    @Override
                    public void onSuccess(String s) {
                        LogCustom.i(Const.TAG.ZY_OTHER, "上传头像成功.名字是：" + s);
                        HeadUrl = getDemo.getResourseURL();
                        LogCustom.i(Const.TAG.ZY_OTHER, "上传头像成功.rul是：" + HeadUrl);
                        bigFile.delete();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Factory.postPhp(PersonalDataActivity.this,Const.PEditUserSelfForApp);
                            }
                        });
                    }

                    @Override
                    public void onProgress(String s, int i, int i1) {
                        LogCustom.i(Const.TAG.ZY_OTHER, "onProgress:" + i);
                    }

                    @Override
                    public void onFailure(String s, OSSException e) {
                        LogCustom.i(Const.TAG.ZY_OTHER, "onFailure:" + s+e.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PersonalDataActivity.this, "上传头像失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });


                final File smallFile = PhotoUtils.scal(photo.getPath(),PhotoUtils.SCAL_IMAGE_30);
                littleDemo.uploadFile(smallFile.getPath(), insertThumb(imageName), new SaveCallback() {
                    @Override
                    public void onSuccess(String s) {
                        smallFile.delete();
                        LogCustom.i(Const.TAG.ZY_OTHER, "头像小图的url是：" + s);
                    }
                    @Override
                    public void onProgress(String s, int i, int i1) {
                    }
                    @Override
                    public void onFailure(String s, OSSException e) {
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PersonalDataActivity");
        }
    }

    public String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(date);
    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    private boolean isMain(){
        if(Thread.currentThread().getName().equals("main")){
            LogCustom.i("ZYS","当前线程为主线程");
            return true;
        }
        LogCustom.i("ZYS","当前线程为工作线程");
        return false;
    }

}

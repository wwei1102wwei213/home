package com.zeyuan.kyq.view;


import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.bean.PatientDetailBean;
import com.zeyuan.kyq.bean.TNMObj;
import com.zeyuan.kyq.biz.BlurBiz;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.ChooseTimeInterface;
import com.zeyuan.kyq.biz.forcallback.FragmentCallBack;
import com.zeyuan.kyq.fragment.ChooseCancerFragment;
import com.zeyuan.kyq.fragment.ChooseTimeFragment;
import com.zeyuan.kyq.fragment.dialog.CityDialog;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.fragment.dialog.DigitDialog;
import com.zeyuan.kyq.fragment.dialog.GeneDialog;
import com.zeyuan.kyq.utils.BlurUtil.BlurColor;
import com.zeyuan.kyq.utils.CDNHelper;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ConstUtils;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.DensityUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.PhotoUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CircleImageView;
import com.zeyuan.kyq.widget.CustomScrollView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 基本资料
 *
 *
 * @author zeyuan
 */
public class PatientDetailActivity extends BaseActivity implements View.OnClickListener,
        ViewDataListener, DialogFragmentListener, OnDismissListener
        ,HttpResponseInterface,ChooseTimeInterface,FragmentCallBack,BlurBiz.BlurInterface{

    private static final String TAG = "PatientDetailActivity";
    private static final String DIGIT_TYPE = "1";
    private static final String DIGIT_TNM_TYPE = "2";
    private static final String NO_DATA = "0";

    private CircleImageView CircleImageView_avatar;//头像
    private TextView mTextView_name;//名字
    private TextView mTextView_sex;//性别
    private TextView mTextView_age;//年龄
    private TextView mTextView_location;//所在地
    private TextView mTextView_cancer_type;//癌症种类
    private TextView mTextView_transfer_pos;//转移情况
    private TextView mTextView_gene;//基因类型
    private TextView mTextView_period_case;//突变类型
    private TextView cancer_time;//抗癌开始时间
    private LinearLayout ll;//这个是第三的三个展开项
    private TextView period_start;//原发肿瘤情况 T
    private TextView linba;//区域淋巴结情况 N
    private TextView far_trsnsfo_case;//远处转移情况 M
    private Button save;//修改信息之后的保存按钮 此处可以精简代码
    public SparseArray<String> citys;//城市索引类键值对
    private Map<String,String> transferpos;
    /**
     * 拍照用到的
     */
    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESOULT = 3;// 结果
    public static final int NONE = 0;
    public static final String IMAGE_UNSPECIFIED = "image/*";
    private ImageView back;
    private AlertDialog dialog;
    public boolean tempistnm;
    private ChooseTimeFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent();
        setContentView(R.layout.activity_patient_msg);

        try {
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            initBar();
            initView();
            initData();
            setListener();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }

    }

    private int statusBarHeight;
    private View statusBar1;
    private View statusTitle;
    private View viewColorChange;
    private int ch = 0;
    private void initBar(){

        try {

            statusBarHeight = getStatusBarHeight();
            statusBar1 = findViewById(R.id.statusBar1);
            statusTitle = findViewById(R.id.statusTitle);
            viewColorChange = findViewById(R.id.view_color_change);

            ViewGroup.LayoutParams params1=statusBar1.getLayoutParams();
            params1.height=statusBarHeight;
            statusBar1.setLayoutParams(params1);

            ViewGroup.LayoutParams params3=viewColorChange.getLayoutParams();
            ch = statusBarHeight + DensityUtils.dp2px(this, 55);
            params3.height=statusBarHeight + DensityUtils.dp2px(this, 55);
            viewColorChange.setLayoutParams(params3);
            viewColorChange.setAlpha(0);

        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"initBar");
        }

    }

    /**
     * 网络访问层
     */
    private void bindView(PatientDetailBean patientDetailBean) {

        try {

            String name = patientDetailBean.getInfoName();
            setTextString(mTextView_name, name);//设置名字
            String headImgUrl = patientDetailBean.getHeadimgurl();
            if (!TextUtils.isEmpty(headImgUrl)) {
                Glide.with(PatientDetailActivity.this).load(headImgUrl)
                        .signature(new IntegerVersionSignature(1))
                        .error(R.mipmap.default_avatar)//这个是加载失败的
                        .into(CircleImageView_avatar);
                Glide.with(PatientDetailActivity.this).load(headImgUrl)
                        .signature(new IntegerVersionSignature(1))
                        .error(R.mipmap.default_avatar)//这个是加载失败的
                        .into(new GlideDrawableImageViewTarget(iv_blur) {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                super.onResourceReady(resource, animation);
                                bc.applyBlur();
                            }
                        });
            }

            /**
             * 1是男 2是女
             */
            if("1".equals(patientDetailBean.getSex())||"2".equals(patientDetailBean.getSex())){
                mTextView_sex.setText(("1".equals(patientDetailBean.getSex()) ? getString(R.string.man) : getString(R.string.women)));//设置性别
            }else{
                mTextView_sex.setText("未填写");
            }
            String age = patientDetailBean.getAge();
            setTextString(mTextView_age, age);//设置年龄
            try {
                String location = patientDetailBean.getCity();
                SparseArray<String> cityes = (SparseArray<String>)Factory.getData(Const.N_DataAllCity);
                setTextString(mTextView_location, cityes.get(Integer.valueOf(location)));//设置城市
            }catch (Exception e){

            }

            String data = String.valueOf(patientDetailBean.getDiscoverTime());
            setTextString(cancer_time, DataUtils.getDate(data));//设置抗癌开始时间

            CancerID = String.valueOf(patientDetailBean.getCancerID());
            Map<String,String> cancerValues = (Map<String,String>)Factory.getData(Const.N_DataCancerValues);
            setTextString(mTextView_cancer_type, cancerValues.get(CancerID));//设置癌症种类

            showGene(patientDetailBean.getGene());

            showTransData(patientDetailBean.getTransferPos());//设置突变情况

            String periodID = MapDataUtils.getDigitValues(patientDetailBean.getPeriodID());//设置突变情况
            if (TextUtils.isEmpty(periodID) || NO_DATA.equals(periodID)) {
                mTextView_period_case.setText(R.string.no_data);
            }
            //设置分期
            if(UserinfoData.getTNM(this)){
                tempistnm = true;
                ll.setVisibility(View.VISIBLE);
                String tnmtext = UserinfoData.getTNMText(this);
                if(tnmtext.indexOf("(")!=-1){
                    if(periodID.equals(tnmtext.substring(0, tnmtext.indexOf("(")))){

                        String tnmtemp = "";
                        if(tnmtext.indexOf("@")!=-1){
                            tnmtemp = tnmtext.substring(tnmtext.indexOf("@")+1,tnmtext.length());
                            tTemp = tnmtemp.substring(0,tnmtemp.indexOf("@"));
                            nTemp = tnmtemp.substring(tnmtemp.indexOf("@")+1,tnmtemp.lastIndexOf("@"));
                            mTemp = tnmtemp.substring(tnmtemp.lastIndexOf("@")+1,tnmtemp.length());
                        }
                        mTextView_period_case.setText(tnmtext.substring(0,tnmtext.indexOf(")")+1));
                        tnmtext = tnmtext.substring(tnmtext.indexOf("(")+1,tnmtext.indexOf(")"));
                        period_start.setText(tnmtext.substring(0, tnmtext.indexOf("N")));
                        linba.setText(tnmtext.substring(tnmtext.indexOf("N"), tnmtext.indexOf("M")));
                        far_trsnsfo_case.setText(tnmtext.substring(tnmtext.indexOf("M"), tnmtext.length()));
                    }else{
                        setTextString(mTextView_period_case, periodID);
                    }
                }else{
                    setTextString(mTextView_period_case, periodID);
                }
            }else{
                tempistnm = false;
                closeTNM();
                setTextString(mTextView_period_case, periodID);
            }
//            setProgressGone();//
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    private CustomScrollView sv;
    private InputMethodManager imm;

    /***
     *
     * 初始化数据
     *
     */
    private void initData() {
        try {
            transferpos =  (Map<String,String>)Factory.getData(Const.N_DataTransferPos);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
        Factory.post(this, Const.EGetPatientDetail);
    }

    private void setListener() {
        try {
            back.setOnClickListener(this);//返回的点击
            CircleImageView_avatar.setOnClickListener(this);//头像的点击
            cancer_time.setOnClickListener(this);//抗癌开始日的点击
            mTextView_age.setOnClickListener(this);//年龄的点击
            mTextView_period_case.setOnClickListener(this);
            mTextView_sex.setOnClickListener(this);//性别的点击
            mTextView_name.setOnClickListener(this);//名字的点击
            mTextView_location.setOnClickListener(this);//位置的点击
            mTextView_cancer_type.setOnClickListener(this);//癌症的点击
            save.setOnClickListener(this);//保存的点击
            mTextView_transfer_pos.setOnClickListener(this);//脑转
            mTextView_gene.setOnClickListener(this);//基因
            far_trsnsfo_case.setOnClickListener(this);
            linba.setOnClickListener(this);
            period_start.setOnClickListener(this);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    private BlurColor bc;
    private ImageView iv_blur;
    private TextView tv_blur;
    private void initView() {
        try {
            save = (Button) findViewById(R.id.save);
            sv = (CustomScrollView) findViewById(R.id.sv);
            sv.setOnScrollListener(new CustomScrollView.OnScrollListener() {
                @Override
                public void onScroll(int x, int t, int oldx, int oldy) {
                    if(t>=600-ch){
                        viewColorChange.setAlpha(1);
                    }else{
                        viewColorChange.setAlpha((float) t / (float) (600-ch));
                    }
                }
            });
//            LogCustom.i("ZYO",DensityUtils.dp2px(this,96));
            sv.setMaxScrollHeight(DensityUtils.dp2px(this,96));
            ll = (LinearLayout) findViewById(R.id.ll);
            back = (ImageView) findViewById(R.id.btn_back);
            /*TextView tv_title = (TextView) findViewById(R.id.tv_title);
            tv_title.setText(getString(R.string.patient_detail));*/

            tv_blur = (TextView)findViewById(R.id.tv_blur);
            iv_blur = (ImageView)findViewById(R.id.iv_blur);
            bc = new BlurColor(this,iv_blur,tv_blur,1);


            CircleImageView_avatar = (CircleImageView) findViewById(R.id.avatar);
            mTextView_name = (TextView) findViewById(R.id.name);
            mTextView_sex = (TextView) findViewById(R.id.sex);
            cancer_time = (TextView) findViewById(R.id.cancer_time);
            mTextView_age = (TextView) findViewById(R.id.age);
            mTextView_location = (TextView) findViewById(R.id.location);
            mTextView_cancer_type = (TextView) findViewById(R.id.cancer_type);
            mTextView_transfer_pos = (TextView) findViewById(R.id.transfer_pos);
            mTextView_gene = (TextView) findViewById(R.id.gene);
            mTextView_period_case = (TextView) findViewById(R.id.period_case);
            far_trsnsfo_case = (TextView) findViewById(R.id.far_trsnsfo_case);
            linba = (TextView) findViewById(R.id.linba);
            period_start = (TextView) findViewById(R.id.period_start);



        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    private boolean flags = true;//ture表示显示数字分期 false 表示 打开tnm分期

    private ChooseCancerFragment ccFragment;
    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btn_back: {
                    finish();
                    break;
                }
                case R.id.avatar: {
                    takePhoto();
                    break;
                }
                case R.id.name: {
                    showInputName();
                    break;
                }
                case R.id.sex: {
                    showSelectSex();
                    break;
                }
                case R.id.age: {
                    showInputAge();
                    break;
                }
                case R.id.cancer_type: {
                    try {
                        if(ccFragment==null){
                            ccFragment = ChooseCancerFragment.getInstance(this);
                        }
                        ccFragment.show(getSupportFragmentManager(),ChooseCancerFragment.TAG);
                    }catch (Exception e){
                        ExceptionUtils.ExceptionSend(e,"tag");
                    }

//                    showCancerType();
                    break;
                }
                case R.id.transfer_pos: {//显示转移
                    createTransDialog();
                    break;
                }
                case R.id.gene: {//基因
                    if (TextUtils.isEmpty(CancerID)) {
                        Toast.makeText(PatientDetailActivity.this, "请选择癌肿", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showGeneDialog();
                    break;
                }
                case R.id.period_case: {//请选择分期
                    if (TextUtils.isEmpty(CancerID)) {
                        Toast.makeText(PatientDetailActivity.this, "请选择癌肿", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!tempistnm) {
                        showDigitDataDialog();
                    } else {
                        openTNM();
                    }
                    break;
                }
                case R.id.cancer_time: {
                    if(fragment==null){
                        fragment = ChooseTimeFragment.getInstance(this);
                    }
                    fragment.show(getFragmentManager(),ChooseTimeFragment.type);
                    break;
                }
                case R.id.location: {
                    CityDialog dialog = new CityDialog();
                    dialog.setOnOnCitySelListener(this);
                    dialog.show(getFragmentManager(), Contants.CITY_DIALOG);
                    break;
                }
                case R.id.save: {
                    /**
                     * 当更换过头像 逻辑是先上传到cdn 在 上传到服务器
                     */
                    if(tempistnm){
                        if(temp!=null&&!"".equals(temp)){
                            UserinfoData.saveTNMText(this,temp+"@"+tTemp+"@"+nTemp+"@"+mTemp);
                        }
                    }
                    UserinfoData.saveTNM(this,tempistnm);
                    if (tempFile == null) {
                        updatePaitentDetail();
                    } else {
                        updateAvatar(tempFile);
                    }
                    break;
                }
                case R.id.far_trsnsfo_case: {//远处转移情况  M
                    showDigitMDialog();
                    break;
                }
                case R.id.period_start: {//原发肿瘤情况  T
                    showDigitTDialog();
                    break;
                }
                case R.id.linba: {//区域淋巴结情况 N
                    showDigitNDialog();
                    break;
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    @Override
    public void timeCallBack(String time) {
        cancer_time.setText(time);
        DiscoverTime = DataUtils.showTimeToLoadTime(time);
        showsave();
    }


    private void showsave(){
        if(save.getVisibility()!=View.VISIBLE){
            save.setVisibility(View.VISIBLE);
        }
    }




    private List<String> chooseGeneList;

    /***
     * 突变窗口控件处理
     *
     */
    private void showGeneDialog() {
        try {
            Map<String, List<String>> gene = (Map<String, List<String>>)Factory.getData(Const.N_DataGene);
            List<String> data = gene.get(CancerID);
            if (data == null || data.isEmpty()) {
                Toast.makeText(this, R.string.cancer_has_no_gene,Toast.LENGTH_LONG).show();
                return;
            }
            GeneDialog dialog;
            if (chooseGeneList != null && chooseGeneList.size() > 0) {
                dialog = new GeneDialog(data, chooseGeneList, 0);
            } else {
                dialog = new GeneDialog(data, null, 0);
            }
            dialog.setTitle("请选择突变情况");
            dialog.setDialogFragmentListener(this);
            dialog.show(getFragmentManager(), Contants.GENE_DIALOG);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }


    private boolean flag = true;//控制原始肿瘤情况显示

    private void showTNM() {
        if (flag) {
            openTNM();
            flag = false;
        } else {
            closeTNM();
            flag = true;
        }
    }

    /***
     * 隐藏TNM分期
     *
     */
    private void closeTNM() {
        try {
            ll.setVisibility(View.GONE);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    /***
     * 显示TNM分期
     *
     */
    private void openTNM() {
        try {
            ll.setVisibility(View.VISIBLE);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    sv.fullScroll(ScrollView.FOCUS_DOWN);//scrollview移动到底部
                }
            });
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }


    /***
     * 显示T分期窗口
     *
     */
    private void showDigitTDialog() {
        try {
            DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_T, CancerID);
            dialog.setListener(this);
            dialog.show(getFragmentManager(), Contants.DIGIT_T_DIALOG);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    /***
     * 显示N分期窗口
     *
     */
    private void showDigitNDialog() {
        try {
            DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_N, CancerID);
            dialog.setListener(this);
            dialog.show(getFragmentManager(), Contants.DIGIT_N_DIALOG);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    /**
     * M分期
     *
     */
    private void showDigitMDialog() {
        try {
            DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_M, CancerID);
            dialog.setListener(this);
            dialog.show(getFragmentManager(), Contants.DIGIT_M_DIALOG);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    /**
     * 显示数字分期
     *
     */
    private void showDigitDataDialog() {
        try {

            DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT, CancerID);
            dialog.setListener(this);
            dialog.show(getFragmentManager(), Contants.DIGIT_DIALOG);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    /***
     * 显示癌肿
     *
     */
    /*private void showCancerType() {
        try {
            CancerTypeDialog dialog = new CancerTypeDialog();
            dialog.setOnCancerTyperListener(this);
            dialog.show(getFragmentManager(), Contants.CANCER_DIALOG);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }*/


    /***
     * 关闭软键盘
     *
     */
    private void closeKeyboard() {
        //关闭软键盘
        if (etName != null) {
            imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
        }
        //恢复位置
//        mAlertViewExt.setMarginBottom(0);
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
                    setChageAge(data);
                }
            });
            dialog1.show(getFragmentManager(), "age");
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    /***
     * 变更年龄
     *
     * @param inputName
     */
    private void setChageAge(String inputName) {
        try {
            mTextView_age.setText(inputName);
            Age = inputName;
            setViewVisible();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    private boolean cancerChanged = false;
    /**
     * 后台请求数据更新
     */
    private PatientDetailBean updatePatientbean;

    /***
     * 上传用户信息到服务器
     *
     */
    private void updatePaitentDetail() {
        try {
            updatePatientbean = new PatientDetailBean();
            {
                updatePatientbean.setGene(Gene);
                updatePatientbean.setAge(Age);
                if (cancerChanged) {
                    updatePatientbean.setCancerID(CancerID);
                }
                updatePatientbean.setCity(City);
                updatePatientbean.setDiscoverTime(DiscoverTime);
                updatePatientbean.setHeadImgUrl(Headimgurl);
                updatePatientbean.setPeriodID(PeriodID);
                updatePatientbean.setSex(Sex);
                updatePatientbean.setInfoName(InfoName);
                updatePatientbean.setTransferPos(TransferPos);
                updatePatientbean.setProvince(ProvinceID);
                updatePatientbean.setPeriodType(PeriodType);
            }
            Factory.post(this, Const.EUpdatePatientDetail);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
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
            new AlertView(null, "请完善你的个人资料！", "取消", null, new String[]{"完成"}, this, AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    LogUtil.i(TAG, "个人资料填的是哪个位置：" + position);
                    if (position == 0) {
                        String name = etName.getText().toString().trim();
                        if (!TextUtils.isEmpty(name)) {
                            setChangeName(name);
                        }
                    }
                    closeKeyboard();
                }
            }).addExtView(extView).setOnDismissListener(this).setCancelable(true).show();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }

    }

    /***
     * 变更呢称
     *
     * @param inputName
     */
    private void setChangeName(String inputName) {
        try {
            mTextView_name.setText(inputName);
            InfoName = inputName;
            setViewVisible();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    /**
     * 修改setbit的值，并显示保存按钮
     *
     * @param
     */
    private void setViewVisible() {
        try {
            save.setVisibility(View.VISIBLE);
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
                            setSexMan();
                            break;
                        case 1:
                            setSexWomen();
                            break;
                    }
                }
            }).setOnDismissListener(this).setCancelable(true).show();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    /***
     * 改变性别为女性
     *
     */
    private void setSexWomen() {
        try {
            mTextView_sex.setText(R.string.women);
            Sex = "2";
            setViewVisible();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    /***
     * 改变性别为男性
     *
     */
    private void setSexMan() {
        try {
            mTextView_sex.setText(R.string.man);
            Sex = "1";
            setViewVisible();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

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
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
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
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
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
                Toast.makeText(PatientDetailActivity.this, "无法连接到相册", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
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
                Toast.makeText(PatientDetailActivity.this, "无法连接到系统裁剪功能", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
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
                    CircleImageView_avatar.setImageBitmap(photo);
                    iv_blur.setImageBitmap(photo);
                    bc.applyBlur();
                    setViewVisible();

                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e,"photoResult");
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private File tempFile;//这个文件 看名字

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
                String imageName = getImgName(PatientDetailActivity.this, true);
                LogCustom.i(Const.TAG.ZY_OTHER, "图片地址：" + photo.getPath());
                final File bigFile = PhotoUtils.scal(photo.getPath(),PhotoUtils.SCAL_IMAGE_80);
                getDemo.uploadFile(bigFile.getPath(), imageName, new SaveCallback() {
                    @Override
                    public void onSuccess(String s) {
                        LogCustom.i(Const.TAG.ZY_OTHER, "上传头像成功.名字是：" + s);
                        Headimgurl = getDemo.getResourseURL();
                        LogCustom.i(Const.TAG.ZY_OTHER, "上传头像成功.rul是：" + Headimgurl);
                        bigFile.delete();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updatePaitentDetail();
                            }
                        });
                    }

                    @Override
                    public void onProgress(String s, int i, int i1) {
                        LogCustom.i(Const.TAG.ZY_OTHER, "onProgress:" + i);
                    }

                    @Override
                    public void onFailure(String s, OSSException e) {
                        LogCustom.i(Const.TAG.ZY_OTHER, "onFailure:" + s);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PatientDetailActivity.this, "上传头像失败", Toast.LENGTH_SHORT).show();
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
//            Toast.makeText(PatientDetailActivity.this, R.string.fail_file, Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
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

    /**
     * 生成100 * 100像素的缩率图
     * @param fileName
     * @param width
     * @param heigth
     * @return
     */
    private File thumbnailBitmap(String fileName, int width, int heigth) {
        Bitmap bitmap = BitmapFactory.decodeFile(fileName);//可能内存溢出
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, heigth);
        return compressBitmap(bitmap);
    }


    private File compressBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
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
        return tempFile;
    }

    /**
     * 读取图片的旋转角度
     *
     * @param path 图片的路径
     * @return
     */
    public int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * @param bitmap  图片
     * @param degress 旋转的角度
     * @return 旋转后图片
     */
    public Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

    private String InfoName;
    private String Headimgurl;
    private String CancerID;
    private String TransferPos;
    private String Gene;
    private String DiscoverTime;
    private String Sex;//0是nv 1是男
    private String Age;
    private String City;
    private String ProvinceID;
    private String digitT;
    private String digitN;
    private String digitM;
    private String Digit;
    private String PeriodType;//1 是数字分期 2 是tnm分期
    private String PeriodID;

    private int setBit = 0;//这个表示那个数据项目修改了。便于使后台少做一次查询

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if(flag == Const.EGetPatientDetail){
            args = new String[]{
                    Contants.InfoID,Const.InfoID
            };
        }
        if(flag == Const.EUpdatePatientDetail){
            args = ConstUtils.getParamsForPatient(updatePatientbean);
        }
        return HttpSecretUtils.getParamString(args);
    }

    /***
     * 癌肿ID改变标志
     *
     * @param cancerid
     * @return
     */
    private boolean isChangeCancerID(String cancerid) {
        if (CancerID.equals(cancerid)) {
            return false;
        }
        return true;
    }


    @Override
    public void toActivity(Object t, int positon) {
        try {
            if (positon == Const.EGetPatientDetail) {
                PatientDetailBean patientDetailBean = (PatientDetailBean) t;
                if(Const.RESULT.equals(patientDetailBean.iResult)){

                    UserinfoData.saveUserData(this, patientDetailBean);
                    bindView(patientDetailBean);

                }
            }else if (positon == Const.EUpdatePatientDetail) {//这个是更新患者详情后返回的参数
                BaseBean beans = (BaseBean) t;
                if (Contants.RESULT.equals(beans.iResult)) {
                    UserinfoData.saveUserDataChange(this, updatePatientbean);
                    Toast.makeText(PatientDetailActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    private ProgressDialog mProgressDialog;

    @Override
    public void showLoading(int tag) {
        if (tag == Const.EUpdatePatientDetail) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("正在保存");
            mProgressDialog.show();
        }
    }

    @Override
    public void hideLoading(int tag) {
        if (tag == Const.EUpdatePatientDetail) {
            mProgressDialog.dismiss();
        }
        if(tag == Const.EGetPatientDetail){
            findViewById(R.id.pd).setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(int tag) {
        if (tag == Const.EUpdatePatientDetail) {
            mProgressDialog.dismiss();
        }
        if(tag == Const.EGetPatientDetail){
            findViewById(R.id.pd).setVisibility(View.GONE);
        }
    }


    private String tTemp = "";
    private String nTemp = "";
    private String mTemp = "";

    @Override
    public void getDataFromDialog(DialogFragment dialog, String data, int position) {
        try {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment cancerType = fragmentManager.findFragmentByTag(Contants.CANCER_DIALOG);
            Fragment citydialog = fragmentManager.findFragmentByTag(Contants.CITY_DIALOG);

            Fragment digitDataDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_DIALOG);
            Fragment digitTDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_T_DIALOG);
            Fragment digitNDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_N_DIALOG);
            Fragment digitMDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_M_DIALOG);
            Fragment geneDialog = fragmentManager.findFragmentByTag(Contants.GENE_DIALOG);
            if (dialog == geneDialog) {
                try {
                    setGeneChange(data);
                    if (TextUtils.isEmpty(data)) {
                        mTextView_gene.setText("");
                        if (chooseGeneList!=null)
                            chooseGeneList.clear();
                        return;
                    }
                    showGene(data);
                }catch (Exception e){

                }
            }

            if (dialog == citydialog) {
                City = data;
                setViewVisible();
                ProvinceID = String.valueOf(position);
                SparseArray<String> cityes = (SparseArray<String>)Factory.getData(Const.N_DataAllCity);
                mTextView_location.setText(cityes.get(Integer.valueOf(data)));
            }

            if (dialog == digitDataDialog) {//数字分期
                LogUtil.i(TAG, data);
                if (DigitDialog.SWITCH.equals(data)) {
                    mTextView_period_case.setText("未知");
                    openTNM();
                    flags = false;
                    return;
                }
                PeriodType = DIGIT_TYPE;
                PeriodID = data;
                String temp = getDigitValues().get(data);
                Digit = temp;
                mTextView_period_case.setText(temp);// 要现实保存按钮
                setViewVisible();
            }

            if (dialog == digitTDialog) {
                LogUtil.i(TAG, "digitTDialog:" + data);
                if (DigitDialog.SWITCH.equals(data)) {
                    closeTNM();
                    flags = true;
                    return;
                }
                digitT = data;
                period_start.setText(getShow(data,1));
                tTemp = data;
                chooseTNMFinish();
            }

            if (dialog == digitNDialog) {
                PeriodType = "2";
                if (DigitDialog.SWITCH.equals(data)) {
                    closeTNM();
                    flags = true;
                    return;
                }
                digitN = data;
                linba.setText(getShow(data,2));
                nTemp = data;
                chooseTNMFinish();

            }

            if (dialog == digitMDialog) {
                PeriodType = "2";
                if (DigitDialog.SWITCH.equals(data)) {
                    closeTNM();
                    flags = true;
                    return;
                }
                digitM = data;
                far_trsnsfo_case.setText(getShow(data,3));
                mTemp = data;
                chooseTNMFinish();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    private void showGene(String data) {
        try {
            if (TextUtils.isEmpty(data) || NO_DATA.equals(data)) {
                mTextView_gene.setText(R.string.no_data);
                return;
            }
            String[] temp = data.split(",");
            if (chooseGeneList == null) {
                chooseGeneList = new ArrayList<>();
            }
            StringBuilder sb = new StringBuilder();
            Map<String,String> geneValues = (Map<String,String>) Factory.getData(Const.N_DataGeneValues);
            for (int i = 0; i < temp.length; i++) {
                chooseGeneList.add(temp[i]);
                if (i != 0) {
                    sb.append(",");
                }
                sb.append(geneValues.get(temp[i]));
            }
            mTextView_gene.setText(sb);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    private void setGeneChange(String data) {
        try {
            Gene = data;
            setViewVisible();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    private String temp;

    private void chooseTNMFinish() {
        try {
            temp = "";
            if (!TextUtils.isEmpty(CancerID) && !TextUtils.isEmpty(tTemp) && !TextUtils.isEmpty(nTemp) && !TextUtils.isEmpty(mTemp)) {
                List<TNMObj> list = (List<TNMObj>) Factory.getData(Const.N_DataTnmObjs);
                int size = list.size();
                TNMObj tnmTmp = null;
                for (int i = 0; i < size; i++) {
                    tnmTmp = list.get(i);
                    if ((tnmTmp.getCancerId().equals(CancerID)) &&
                            (tnmTmp.getTid().equals("0") || tnmTmp.getTid().equals(tTemp))
                            && (tnmTmp.getNid().equals("0") || tnmTmp.getNid().equals(nTemp))
                            && (tnmTmp.getMid().equals("0") || tnmTmp.getMid().equals(mTemp))) {
                        String digitID = tnmTmp.getDigitId();
                        String showdigitId = getDigitValues().get(digitID);
                        LogUtil.i(TAG, "digitid是：" + digitID + "showdigit是" + showdigitId);
                        temp = showdigitId + "(" + getShow(tTemp,1) + getShow(nTemp,2) + getShow(mTemp,3) + ")";
                        mTextView_period_case.setText(temp);
                        PeriodID = digitID;
                        PeriodType = DIGIT_TNM_TYPE;
                        setViewVisible();
                        break;
                    }
                    PeriodID = "0";
                    PeriodType = DIGIT_TNM_TYPE;
                    temp = "未知" + "(" + getShow(tTemp,1) + getShow(nTemp,2) + getShow(mTemp,3) + ")";
                    mTextView_period_case.setText(temp);
                    setViewVisible();
                }
            } else {
                temp = "未知" + "(" + getShow(tTemp,1) + getShow(nTemp,2) + getShow(mTemp,3) + ")";
                mTextView_period_case.setText("未知" + "(" + getShow(tTemp,1) + getShow(nTemp,2) + getShow(mTemp,3) + ")");
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    private Map<String, String> DigitValues;
    private Map<String, String> getDigitValues(){
        if(DigitValues==null){
            DigitValues = (Map<String, String>)Factory.getData(Const.N_DataDigitValues);
        }
        return DigitValues;
    }

    /**
     * 根据选中的id 来获得显示的tnm分期
     * d @param data
     *
     * @return
     */
    private String getShow(String data,int tnmtag) {
        if (TextUtils.isEmpty(data)) {
            return "";
        }
        String id = getDigitValues().get(data);
        StringBuffer temp = new StringBuffer(id);
        int index = id.indexOf(" ");
        StringBuffer sb = temp.delete(index, temp.length());
        return sb.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (tempFile != null) {
                tempFile.delete();
            }
        }catch (Exception e){

        }
    }


    private void createTransDialog() {
        try {

            List<String> transPosList = new ArrayList<>();
            Set<String> set = transferpos.keySet();
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                transPosList.add(key);
            }
            GeneDialog dialog;
            if (chooseTransData != null && chooseTransData.size() > 0) {
                dialog = new GeneDialog(transPosList, chooseTransData, Contants.diolog_type);
            } else {
                dialog = new GeneDialog(transPosList, null, Contants.diolog_type);
            }
            dialog.setTitle("请选择转移情况");
            dialog.setDialogFragmentListener(new DialogFragmentListener() {
                @Override
                public void getDataFromDialog(DialogFragment dialog, String data, int position) {
                    TransferPos = data;
                    setViewVisible();
                    showTransData(data);
                }
            });
            dialog.show(getFragmentManager(), "transpos");
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    private List<String> chooseTransData;

    public void showTransData(String data) {
        try {
            if (TextUtils.isEmpty(data) || NO_DATA.equals(data)) {
                mTextView_transfer_pos.setText(R.string.no_data);
                return;
            }
            if (chooseTransData == null) {
                chooseTransData = new ArrayList<>();
            }
            String[] strings = data.split(",");
            StringBuilder sb = new StringBuilder();
            if (strings.length > 0) {
                for (String str : strings) {
                    chooseTransData.add(str);
                    if (sb.length() == 0) {
                        sb.append(transferpos.get(str));
                    } else {
                        sb.append("," + transferpos.get(str));
                    }
                }
            }
            mTextView_transfer_pos.setText(sb);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDismiss(Object o) {
        closeKeyboard();
    }

    @Override
    public void dataCallBack(String str, int flag, String tag, Object obj) {
        if(flag == Const.FRAGMENT_CHOOSE_CANCER){
            if(!TextUtils.isEmpty(str)){
                CancerID = str;
                Map<String,String> cancerValues = (Map<String,String>)Factory.getData(Const.N_DataCancerValues);
                mTextView_cancer_type.setText(cancerValues.get(str));
                setViewVisible();
                cancerChanged = true;
            }
        }
    }

    @Override
    public void setBlur(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT>=16){
            tv_blur.setBackground(new BitmapDrawable(this.getResources(),bitmap));
        }
    }
}

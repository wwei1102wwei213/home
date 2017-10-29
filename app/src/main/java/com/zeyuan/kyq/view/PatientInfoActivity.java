package com.zeyuan.kyq.view;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.zeyuan.kyq.Entity.CreateInfoBean;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.fragment.patientinfo.CancerChooseFragment;
import com.zeyuan.kyq.fragment.patientinfo.DiscoverTimeFragment;
import com.zeyuan.kyq.fragment.patientinfo.LcGnFragment;
import com.zeyuan.kyq.fragment.patientinfo.PatientInfoFragment;
import com.zeyuan.kyq.fragment.patientinfo.PatientResultFragment;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ConstUtils;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.SharePrefUtil;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CustomProgressDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PatientInfoActivity
 * 患者信息，获得用户注册信息
 */
public class PatientInfoActivity extends BaseActivity implements PatientInfoFragment.OnNextStepClickListener,
        PatientInfoFragment.OnLastStepClickListener, HttpResponseInterface {

    private static final String TAG = "PatientInfoActivity";

    private CancerChooseFragment cancerChooseFragment;//患者信息设置癌症种类
    private DiscoverTimeFragment mDiscoverTimeFragment;//设置患者确诊时间
    private LcGnFragment mLcGnFragment;//设置地点和分期
    private PatientResultFragment mPatientResultFragment;//最后一步
    private List<PatientInfoFragment> fragments;
    private String metricParam;
    private boolean jump = false;
    private String token;
    private String mOpenID = null;
    private String mUnionID = null;
    private String lt = "1";
    private String phone = null;

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);
        try {
            mOpenID = getIntent().getStringExtra(Contants.OpenID);
            mUnionID = getIntent().getStringExtra(Contants.UnionID);
            lt = UserinfoData.getLoginType(this);
            phone = getIntent().getStringExtra("phone");
            metricParam = initExtras();
            cancerChooseFragment = new CancerChooseFragment();
            cancerChooseFragment.setOnNextStepClickListener(this);
            fragments = new ArrayList<>();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.id_content, cancerChooseFragment, "cancerType");
            ft.commit();
            fragments.add(cancerChooseFragment);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, PatientInfoActivity.this, "PatientInfoActivity");
        }
    }

    public String initExtras(){
        String metricStr = null;
        try {
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            int width = metric.widthPixels;     // 屏幕宽度（像素）
            int height = metric.heightPixels;   // 屏幕高度（像素）
            float density = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）
            int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
            metricStr = "metric：" + width + "," + height + "," + density + "," + densityDpi;
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"initExtras,PatientInfoActivity");
        }
        if(TextUtils.isEmpty(metricStr)){
            metricStr = "提取屏幕信息失败";
        }
        return metricStr;
    }

    @Override
    public void onLastStepClickListener(Fragment fragment) {
        onBackPressed();
    }

    @Override
    public void onNextStepClickListener(Fragment fragment) {
        try {

            if (fragment == cancerChooseFragment) {//点击位置中的下一步
                if(jump){
                    Factory.postPhp(this, Const.PCreateUserInfo);
                }else {
                    if (mLcGnFragment == null) {
                        mLcGnFragment = new LcGnFragment();
                        mLcGnFragment.setOnLastStepClickListener(this);
                        mLcGnFragment.setOnNextStepClickListener(this);
                    }
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.hide(cancerChooseFragment).add(R.id.id_content, mLcGnFragment, "stepfragment");
                    ft.commit();
                    fragments.add(mLcGnFragment);
                }
            }
            if (fragment == mLcGnFragment) {//这个是地点和分期中的完成
                if (mPatientResultFragment == null) {
                    mPatientResultFragment = new PatientResultFragment();
                    mPatientResultFragment.setOnLastStepClickListener(this);
                    mPatientResultFragment.setOnNextStepClickListener(this);
                }
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.hide(mLcGnFragment).add(R.id.id_content, mPatientResultFragment, "infofragment");
                ft.commit();
                fragments.add(mPatientResultFragment);
            }
            if (fragment == mPatientResultFragment) {//最后一步的完成
                Factory.postPhp(this, Const.PCreateUserInfo);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, PatientInfoActivity.this, "PatientInfoActivity");
        }
    }

    @Override
    public void onBackPressed() {
        if (fragments.size() > 1) {
            try {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.remove(fragments.get(fragments.size() - 1)).show(fragments.get(fragments.size() - 2));
                fragments.get(fragments.size() - 2).getResume();
                fragments.remove(fragments.size() - 1);
                ft.commit();
            }catch (Exception e){
                ExceptionUtils.ExceptionToUM(e, PatientInfoActivity.this, "PatientInfoActivity");
            }
        } else {
            super.onBackPressed();
        }
    }

    private File tempFile;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tempFile != null) {
            tempFile.delete();
        }
    }

    /**
     * 这些是填写完，之后赋值的信息
     */

    private String CancerID;
    private String DiscoverTime;
    private String Headimgurl = null;
    private String Province;
    private String CureStartTime;
    private String StepID;
    private String City = "";
    private String PeriodType ;//1 是数字分期 2 是tnm分期
    private String PeriodID = "0";
    private String IsMedicineValid = "1";
    private boolean noStepFlag = false;

    public void setNoStepFlag(boolean noStepFlag) {
        this.noStepFlag = noStepFlag;
    }

    public void setIsMedicineValid(String isMedicineValid) {
        IsMedicineValid = isMedicineValid;
    }

    public void setHeadimgurl(String headimgurl) {
        Headimgurl = headimgurl;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public void setCureStartTime(String cureStartTime) {
        CureStartTime = cureStartTime;
    }

    public void setStepID(String stepID) {
        StepID = stepID;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setPeriodType(String periodType) {
        PeriodType = periodType;
    }

    public void setPeriodID(String periodID) {
        PeriodID = periodID;
    }

    public void setDiscoverTime(String discoverTime) {
        DiscoverTime = discoverTime;
    }

    public String getDiscoverTime() {
        return DiscoverTime;
    }

    public String getCity() {
        return City;
    }

    public String getPeriodType() {
        return PeriodType;
    }

    public String getCancerID() {
        return CancerID;
    }

    public void setCancerID(String CancerID) {
        this.CancerID = CancerID;
        UserinfoData.saveCancerID(this,CancerID);
    }


    /***
     * 处理发请求所需的参数
     *
     * @param tag 发请求的标示
     * @return
     */
    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == Const.PCreateUserInfo){
            if(jump){
                String name = UserinfoData.getInfoname(this);
                if(name!=null){
                    map.put(Contants.InfoName,name);
                }
                map.put(Contants.CancerID, "30");
                if(TextUtils.isEmpty(City)){
                    City = "110100";
                }
                map.put(Contants.City, City);
                Province = City.substring(0,2) + "0000";
                map.put(Contants.Province, Province);
                UserinfoData.saveCityID(this, City);
                UserinfoData.saveProvinceID(this, Province);
                String disTime = String.valueOf(System.currentTimeMillis() / 1000);
                map.put(Contants.DiscoverTime, disTime);
                String headUrl = UserinfoData.getAvatarUrl(this);
                map.put("headurl", headUrl);
                map.put(Const.ISHAVESTEP,"0");
            }else {
                String name = UserinfoData.getInfoname(this);
                if(name!=null){
                    map.put(Contants.InfoName,name);
                }
                map.put(Contants.CancerID, "30");
                if(TextUtils.isEmpty(City)){
                    City = "110100";
                }
                map.put(Contants.City, City);
                Province = City.substring(0,2) + "0000";
                map.put(Contants.Province, Province);
                UserinfoData.saveCityID(this, City);
                UserinfoData.saveProvinceID(this, Province);

                if (!TextUtils.isEmpty(PeriodID)) {
                    map.put(Contants.PeriodID,PeriodID);
                }
                if (!TextUtils.isEmpty(PeriodType)) {
                    map.put(Contants.PeriodType, PeriodType);
                }
                if (!TextUtils.isEmpty(DiscoverTime)&&!Const.RESULT.equals(DiscoverTime)) {
                    map.put(Contants.DiscoverTime, DiscoverTime);
                } else {
                    String time = String.valueOf(System.currentTimeMillis() / 1000);
                    map.put(Contants.DiscoverTime, time);
                }
                String headUrl = UserinfoData.getAvatarUrl(this);
                map.put("headurl", headUrl);

                if(!noStepFlag){
                    if (!TextUtils.isEmpty(CureStartTime)) {//当前阶段的时间
                        map.put(Contants.CureStartTime, CureStartTime);
                    } else {
                        String time = String.valueOf(System.currentTimeMillis() / 1000);
                        map.put(Contants.CureStartTime, time);
                    }

                    map.put(Contants.StepID, StepID);
                    map.put("isMedicineValid", IsMedicineValid);
                    map.put(Const.ISHAVESTEP, "1");
                }else {
                    map.put(Const.ISHAVESTEP, "0");
                }
            }
            map.put("openid",mOpenID);
            map.put("apptype","2");
            map.put("loginType",lt);
            if ("1".equals(lt)){
                map.put("unid",mUnionID);
            }
            map.put("phone",phone);
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if(flag == Const.ECreateInfo){

            List<String> list = new ArrayList<>();
            list.add(Const.LoginToken);
            list.add(token);
            if(jump){
                String name = UserinfoData.getInfoname(this);
                if(name!=null){
                    list.add(Contants.InfoName);
                    list.add(name);
                }
                list.add(Contants.CancerID);
                list.add("30");
                if(TextUtils.isEmpty(City)){
                    City = "110100";
                }
                list.add(Contants.City);
                list.add(City);
                Province = City.substring(0,2) + "0000";
                list.add(Contants.Province);
                list.add(Province);
                Log.i("ZYS", "CityID2:" + City);
                UserinfoData.saveCityID(this, City);
                UserinfoData.saveProvinceID(this, Province);

                String disTime = String.valueOf(System.currentTimeMillis() / 1000);
                list.add(Contants.DiscoverTime);
                list.add(disTime);

                String headUrl = UserinfoData.getAvatarUrl(this);
                list.add(Contants.Headimgurl);
                list.add(headUrl);
                list.add(Const.ISHAVESTEP);
                list.add("0");
            }else {
                String name = UserinfoData.getInfoname(this);
                if(name!=null){
                    list.add(Contants.InfoName);
                    list.add(name);
                }
                list.add(Contants.CancerID);
                list.add(CancerID);
                Log.i("ZYS", "CityID3:" + City);
                if(TextUtils.isEmpty(City)){
                    City = "110100";
                }
                list.add(Contants.City);
                list.add(City);
                Province = City.substring(0,2) + "0000";
                list.add(Contants.Province);
                list.add(Province);
                UserinfoData.saveCityID(this, City);
                UserinfoData.saveProvinceID(this, Province);

                if (!TextUtils.isEmpty(PeriodID)) {
                    list.add(Contants.PeriodID);
                    list.add(PeriodID);
                }
                if (!TextUtils.isEmpty(PeriodType)) {
                    list.add(Contants.PeriodType);
                    list.add(PeriodType);
                }
                if (!TextUtils.isEmpty(DiscoverTime)&&!Const.RESULT.equals(DiscoverTime)) {
                    list.add(Contants.DiscoverTime);
                    list.add(DiscoverTime);
                } else {
                    String time = String.valueOf(System.currentTimeMillis() / 1000);
                    list.add(Contants.DiscoverTime);
                    list.add(time);
                }
                String headUrl = UserinfoData.getAvatarUrl(this);
                list.add(Contants.Headimgurl);
                list.add(headUrl);

                if(!noStepFlag){
                    if (!TextUtils.isEmpty(CureStartTime)) {//当前阶段的时间
                        list.add(Contants.CureStartTime);
                        list.add(CureStartTime);
                    } else {
                        String time = String.valueOf(System.currentTimeMillis() / 1000);
                        list.add(Contants.CureStartTime);
                        list.add(time);
                    }
                    list.add(Contants.StepID);
                    list.add(StepID);
                    list.add(Contants.IsMedicineValid);
                    list.add(IsMedicineValid);
                    list.add(Const.ISHAVESTEP);
                    list.add("1");
                }else {
                    list.add(Const.ISHAVESTEP);
                    list.add("0");
                }
            }
            args = ConstUtils.getParamsForList(list);
        }
        return HttpSecretUtils.getParamString(args);
    }

    /**
     * 回调
     * 处理服务器返回的信息
     * @param t
     * @param position
     */
    @Override
    public void toActivity(Object t, int position) {
        if (position == Const.ECreateInfo) {
            CreateInfoBean bean = (CreateInfoBean) t;
            LogUtil.i(TAG, bean.toString());
            dialog.dismiss();
            if (Const.RESULT.equals(bean.getiResult())&&!TextUtils.isEmpty(bean.getInfoID())) {
                UserinfoData.saveInfoID(this, bean.getInfoID());
                Const.InfoID = bean.getInfoID();
                UserinfoData.saveIsHaveCreateInfo(this, "1");
                Factory.onEvent(this, Const.EVENT_CreateInfo, Const.EVENTFLAG, metricParam);
                SharePrefUtil.saveBoolean(this, Contants.hasRecord, true);//保存信息说明档案也完成
                Toast.makeText(this, R.string.cret_info_sucess, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }else {
                try {
                    if(cancerChooseFragment!=null){
                        cancerChooseFragment.setClickAble();
                    }
                }catch (Exception e){

                }
                Toast.makeText(this, R.string.cret_info_faild, Toast.LENGTH_SHORT).show();
            }
        }
        if (position == Const.PCreateUserInfo) {
            PhpUserInfoBean bean = (PhpUserInfoBean) t;
            LogCustom.i(TAG, bean.toString());
            if (dialog!=null) dialog.dismiss();
            if (Const.RESULT.equals(bean.getiResult())&&!TextUtils.isEmpty(bean.getInfoID())) {
                UserinfoData.saveInfoID(this, bean.getInfoID());
                Const.InfoID = bean.getInfoID();
                UserinfoData.saveIsHaveCreateInfo21(this, "1");
                Factory.onEvent(this, Const.EVENT_CreateInfo, Const.EVENTFLAG, metricParam);
                SharePrefUtil.saveBoolean(this, Contants.hasRecord, true);//保存信息说明档案也完成
                Toast.makeText(this, R.string.cret_info_sucess, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }else {
                try {
                    if (jump){
                        jump = false;
                    }
                    if(cancerChooseFragment!=null){
                        cancerChooseFragment.setClickAble();
                    }
                }catch (Exception e){

                }
                Toast.makeText(this, R.string.cret_info_faild, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private CustomProgressDialog dialog;

    /***
     * 回调
     * 发送请求前显示窗口，提示用户正在创建
     * @param tag 请求标识
     */
    @Override
    public void showLoading(int tag) {
        dialog = CustomProgressDialog.createCustomDialog(this);
        dialog.setMessage("正在创建...");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }

    /***
     * 回调
     * 请求结束，关闭提示窗口
     * @param tag 请求标识
     */
    @Override
    public void hideLoading(int tag) {
        if (dialog!=null){
            dialog.dismiss();
        }
    }

    @Override
    public void showError(int tag) {
        try {
            if(cancerChooseFragment!=null){
                cancerChooseFragment.setClickAble();
            }
            if (jump){
                jump = false;
            }
            if (dialog!=null){
                dialog.dismiss();
            }
        }catch (Exception e){

        }
        Toast.makeText(this, R.string.cret_info_faild, Toast.LENGTH_SHORT).show();
    }
}

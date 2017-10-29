package com.zeyuan.kyq.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.zeyuan.kyq.Entity.CancerSizeItemEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.CancerSizeChartAdapter;
import com.zeyuan.kyq.adapter.RecordPhotoAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.ChooseTimeInterface;
import com.zeyuan.kyq.fragment.ChooseTimeFragment;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.fragment.dialog.GeneDialog;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.utils.CDNHelper;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ConstUtils;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.DecryptUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.OtherUtils;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/2/14.
 *
 * 记录页面
 *
 * @author wwei
 */
public class RecordActivity extends BaseActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener
        ,MyLayout.OnSoftKeyboardListener,View.OnClickListener,HttpResponseInterface,DialogFragmentListener{

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
    private ArrayList<String> check = null;
    //** 输入框小数的位数*/
    private static final int DECIMAL_DIGITS = 2;
    //设置小数位数控制
    private InputFilter lengthfilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dStart, int dEnd) {
            // 删除等特殊字符，直接返回
            if ("".equals(source.toString())) {
                return null;
            }
            String dValue = dest.toString();
            String[] splitArray = dValue.split(".");
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                int diff = dotValue.length() + 1 - DECIMAL_DIGITS;
                if (diff > 0) {
                    return source.subSequence(start, end - diff);
                }
            }
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        initType();
        initView();
        initListener();
        initData();
    }

    //标题文本框
    private TextView tv_title;
    private int QUOTA_STATUS;
    //设置记录类型
    private void initType(){
        type = getIntent().getIntExtra(Const.RECORD_CLASSIFY_TYPE, 5);
        REQUEST_FLAG = getIntent().getBooleanExtra(Const.RECORD_REQUEST_FLAG, false);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText(UiUtils.getRecordClassifyTitle(type));
    }

    //备注输入框
    private EditText et_remark;
    //医院输入框
    private EditText et_hospital;
    //医生输入框
    private EditText et_doctor;
    //记录时间文本框
    private TextView tv_record_time;
    //记录时间区域框
    private View v_record_time;
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
    //基因、转移、症状控件
    private View v_some;
    //基因、转移、症状文本
    private TextView tv_some_text;
    //基因、转移、症状数据
    private TextView tv_some;
    private void initView(){
        tv_save = (TextView)findViewById(R.id.tv_save);
        tv_record_time = (TextView)findViewById(R.id.tv_record_time);
        v_record_time = findViewById(R.id.v_record_time);
        et_remark = (EditText)findViewById(R.id.et_remark);
        et_hospital = (EditText)findViewById(R.id.et_hospital);
        et_doctor = (EditText)findViewById(R.id.et_doctor);
        v_hospital = findViewById(R.id.v_hospital);
        gv = (MyGridView)findViewById(R.id.gv);
        selectedPicture = new ArrayList<>();
        adapter = new RecordPhotoAdapter(this,selectedPicture);
        gv.setAdapter(adapter);
        sv = (CustomScrollView)findViewById(R.id.sv);
        mLayout = (MyLayout)findViewById(R.id.my_layout);
        switch (type){
            case Const.RECORD_TYPE_11://基因
                initSomeView();
                tv_some_text.setText("基因情况");
                break;
            case Const.RECORD_TYPE_12://转移
                initSomeView();
                tv_some_text.setText("转移情况");
                break;
            case Const.RECORD_TYPE_13://症状
                initSomeView();
                tv_some_text.setText("出现的症状");
                break;
            case Const.RECORD_TYPE_14://记录肿瘤大小
                List<CancerSizeItemEntity> temp =  (List<CancerSizeItemEntity>)getIntent()
                        .getSerializableExtra(Const.RECORD_CLASSIFY_CANCER_SIZE);
                InfoID = Integer.valueOf(UserinfoData.getInfoID(this));
                cancerArray = ConstUtils.getArrayForCancerSizeList(temp,InfoID);
                initCancerSizeView();
                break;
            case Const.RECORD_TYPE_15://指标
                QUOTA_STATUS = getIntent().getIntExtra(Const.RECORD_CLASSIFY_QUOTA_STATUS, 1);
                if (QUOTA_STATUS == 3){
                    check = getIntent().getStringArrayListExtra(Const.RECORD_CLASSIFY_QUOTA_CHECKED);
                }else if (QUOTA_STATUS == 2){
                    check = new ArrayList<>();
                }else {
                    check = UiUtils.getDefaultQuotaTypeForCancerID(UserinfoData.getCancerID(this));
                }
                List<CancerSizeItemEntity> temp1 =  (List<CancerSizeItemEntity>)getIntent()
                        .getSerializableExtra(Const.RECORD_CLASSIFY_CANCER_SIZE);
                InfoID = Integer.valueOf(UserinfoData.getInfoID(this));
                cancerArray = ConstUtils.getArrayForCancerSizeList(temp1, InfoID);
                if (cancerArray==null) cancerArray = new SparseArray<>();//防空指针
                initQuotaView();
                break;
        }
    }

    //设置监听事件
    private void initListener(){
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_save.setOnClickListener(this);
        v_record_time.setOnClickListener(this);
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
                    }
                    et_hospital.setHint("");
                    et_hospital.setText(temp);
                    et_hospital.setSelection(temp.length());
                } else {
                    if (TextUtils.isEmpty(et_hospital.getText())) {
                        et_hospital.setHint(getResources().getString(R.string.hint_hospital));
                    }
                }
            }
        });
        et_doctor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String temp = "";
                    if (!TextUtils.isEmpty(et_doctor.getText())) {
                        temp = et_doctor.getText().toString();
                    }
                    et_doctor.setHint("");
                    et_doctor.setText(temp);
                    et_doctor.setSelection(temp.length());
                } else {
                    if (TextUtils.isEmpty(et_doctor.getText())) {
                        et_doctor.setHint(getResources().getString(R.string.hint_doctor));
                    }
                }
            }
        });
    }

    private void initData(){}

    //初始化基因，症状，转移等的UI
    private void initSomeView(){
        try {
            check = getIntent().getStringArrayListExtra(Const.RECORD_CLASSIFY_GENE_TRANSLATE_CHECKED);
            if (check==null) check = new ArrayList<>();
            v_some = findViewById(R.id.v_some);
            tv_some_text = (TextView)findViewById(R.id.tv_some_text);
            tv_some = (TextView)findViewById(R.id.tv_some);
            String temp = "";
            if (type == Const.RECORD_TYPE_11||type == Const.RECORD_TYPE_12){
                String str = ConstUtils.getParamsForPic(check);
                temp = type == Const.RECORD_TYPE_11?MapDataUtils.getGeneForString(str)
                        :MapDataUtils.getTranslateForString(str);
            }
            if (TextUtils.isEmpty(temp)){
                tv_some.setHint(type == Const.RECORD_TYPE_11 ? "请选择基因" :
                        (type == Const.RECORD_TYPE_12 ? "请选择转移情况" : "请选择出现的症状"));
            }else {
                tv_some.setText(temp);
            }
            tv_some_text.setText(type == Const.RECORD_TYPE_11 ? "基因情况" :
                    (type == Const.RECORD_TYPE_12 ? "转移情况" : "出现的症状"));
            v_some.setVisibility(View.VISIBLE);
            v_some.setOnClickListener(this);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"initSomeView");
        }
    }

    //设置指标UI
    private void initQuotaView(){
        try {
            findViewById(R.id.v_choose_quota_type_patient).setVisibility(View.VISIBLE);
            findViewById(R.id.v_choose_quota_type).setOnClickListener(this);
            v_cancer_size = (LinearLayout)findViewById(R.id.v_cancer_size);
            v_cancer_size.setVisibility(View.VISIBLE);
            getQuotaItem();
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"initQuotaView");
        }
    }

    //设置指标图表
    private void getQuotaItem(){
        try {
            v_cancer_size.removeAllViews();
            addArray = ConstUtils.getQuotaCharArray(cancerArray,check,InfoID,false);
            if (addArray!=null&&addArray.size()>0){
                int[] TypeIDArray = ConstUtils.getTypeIDArray(addArray);
                for (int i=0;i<TypeIDArray.length;i++){
                    int TypeID = TypeIDArray[i];
                    getQuotaItemView(TypeID,false);
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"getQuotaItem");
        }
    }

    //设置指标图表数据
    private void getQuotaItemView(final int TypeID,boolean isFirst){
        final View v = LayoutInflater.from(this).inflate(R.layout.layout_quota_record_item,v_cancer_size,false);
        final TextView tv = (TextView)v.findViewById(R.id.tv);
        final EditText et = (EditText)v.findViewById(R.id.et);
        et.setFilters(new InputFilter[]{lengthfilter});
        if (isFirst){
            v.findViewById(R.id.v_space_top).setVisibility(View.GONE);
        }
        tv.setText(UiUtils.getNameENforTypeID(TypeID));
        String size = addArray.get(TypeID).getSize();
        if (!TextUtils.isEmpty(size)){
            et.setText(size);
        }else {
            et.setHint("请输入"+UiUtils.getNameENforTypeID(TypeID)+"指标");
        }
        List<CancerSizeItemEntity> temp = cancerArray.get(TypeID);
        if (temp!=null&&temp.size()>0){
            v.findViewById(R.id.v_chart).setVisibility(View.VISIBLE);
            v.findViewById(R.id.v_color_1).setBackgroundResource(UiUtils.getBGColorForTypeID(TypeID));
            v.findViewById(R.id.v_color_2).setBackgroundResource(UiUtils.getColorForTypeID(TypeID));
            TextView name = (TextView)v.findViewById(R.id.tv_name);
            String nameStr = UiUtils.getNameENforTypeID(TypeID) + "(" + UiUtils.getUnitForType(TypeID) +")";
            name.setText(nameStr);
            RecyclerView rv = (RecyclerView)v.findViewById(R.id.rv);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rv.setLayoutManager(manager);
            CancerSizeChartAdapter adapter = new CancerSizeChartAdapter(this,
                    ConstUtils.getQuotaChartData(temp),UiUtils.getTypeIDForTypeID(TypeID),true);
            rv.setAdapter(adapter);
        }
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String t2 = et.getText().toString();
                if (!OtherUtils.isEmpty(t2)) {
                    addArray.get(TypeID).setSize(t2);
                } else {
                    addArray.get(TypeID).setSize("");
                }
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String temp = "";
                    if (!TextUtils.isEmpty(et.getText())) {
                        temp = et.getText().toString();
                    }
                    et.setHint("");
                    et.setText(temp);
                    et.setSelection(temp.length());
                } else {
                    if (TextUtils.isEmpty(et.getText())) {
                        et.setHint("请输入"+UiUtils.getNameENforTypeID(TypeID)+"指标");
                    }
                }
            }
        });
        v_cancer_size.addView(v);
    }

    private LinearLayout v_cancer_size;
    private int InfoID = Integer.valueOf(UserinfoData.getInfoID(this));
    private void initCancerSizeView(){
        try {
            v_cancer_size = (LinearLayout)findViewById(R.id.v_cancer_size);
            v_cancer_size.setVisibility(View.VISIBLE);
            getCancerSizeItem();
            getAddCancerView();
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"initCancerSizeView");
        }
    }

    private SparseArray<List<CancerSizeItemEntity>> cancerArray = null;
    private SparseArray<CancerSizeItemEntity> addArray = new SparseArray<>();
    private void getCancerSizeItem(){
        addArray = ConstUtils.getCancerSizeCharArray(cancerArray,InfoID);
        int[] TypeIDArray = ConstUtils.getTypeIDArray(addArray);
        for (int i=0;i<TypeIDArray.length;i++){
            int TypeID = TypeIDArray[i];
            if (TypeID==1){
                getMainCancerView();
            }else {
                getOtherCancerView(TypeID,false);
            }
        }
    }

    //主肿瘤UI
    private void getMainCancerView(){
        try {
            View v = LayoutInflater.from(this).inflate(R.layout.layout_cancer_size_main,v_cancer_size,false);
            final EditText et2 = (EditText)v.findViewById(R.id.et2);
            final EditText et3 = (EditText)v.findViewById(R.id.et3);

            try {
                String size = addArray.get(1).getSize();
                if (!OtherUtils.isEmpty(size)){
                    int index = size.indexOf("*");
                    String width = size.substring(0,index);
                    String height = size.substring(index+1,size.length());
                    et2.setText(width);
                    et3.setText(height);
                }
            }catch (Exception e){
                ExceptionUtils.ExceptionSend(e,"getMainCancerView 肿瘤大小拆分错误");
            }
            List<CancerSizeItemEntity> temp = cancerArray.get(1);
            if (temp!=null&&temp.size()>0){
                v.findViewById(R.id.v_chart).setVisibility(View.VISIBLE);
                RecyclerView rv = (RecyclerView)v.findViewById(R.id.rv);
                LinearLayoutManager manager = new LinearLayoutManager(this);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                rv.setLayoutManager(manager);
                CancerSizeChartAdapter adapter = new CancerSizeChartAdapter(this,ConstUtils.getCancerChartData(temp),0);
                rv.setAdapter(adapter);
            }
            et2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    String t2 = et2.getText().toString();
                    String t3 = et3.getText().toString();
                    if (!OtherUtils.isEmpty(t2)&&!OtherUtils.isEmpty(t3)){
                        addArray.get(1).setSize(t2 + "*" + t3);
                    }else {
                        if (OtherUtils.isEmpty(t3)&&OtherUtils.isEmpty(t2)) {
                            addArray.get(1).setSize("");
                        }else {
                            addArray.get(1).setSize("0");
                        }
                    }
                }
            });
            et3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    String t2 = et2.getText().toString();
                    String t3 = et3.getText().toString();
                    if (!OtherUtils.isEmpty(t2)&&!OtherUtils.isEmpty(t3)){
                        addArray.get(1).setSize(t2 + "*" + t3);
                    }else {
                        if (OtherUtils.isEmpty(t3)&&OtherUtils.isEmpty(t2)) {
                            addArray.get(1).setSize("");
                        }else {
                            addArray.get(1).setSize("0");
                        }
                    }
                }
            });
            v_cancer_size.addView(v);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"getMainCancerView");
        }
    }

    //副肿瘤UI
    private void getOtherCancerView(final int TypeID,boolean isAdd){
        final View v = LayoutInflater.from(this).inflate(R.layout.layout_cancer_size_item,v_cancer_size,false);
        final EditText et1 = (EditText)v.findViewById(R.id.et1);
        final EditText et2 = (EditText)v.findViewById(R.id.et2);
        final EditText et3 = (EditText)v.findViewById(R.id.et3);

        if (!isAdd){
            try {
                String size = addArray.get(TypeID).getSize();
                int index = size.indexOf("*");
                String width = size.substring(0,index);
                String height = size.substring(index+1,size.length());
                et2.setText(width);
                et3.setText(height);
            }catch (Exception e){
                ExceptionUtils.ExceptionSend(e,"getOtherCancerView 肿瘤大小拆分错误");
            }
            List<CancerSizeItemEntity> temp = cancerArray.get(TypeID);
            if (temp!=null&&temp.size()>0){
                v.findViewById(R.id.v_chart).setVisibility(View.VISIBLE);
                v.findViewById(R.id.v_color_1).setBackgroundResource(UiUtils.getBGColorForTypeID(TypeID));
                v.findViewById(R.id.v_color_2).setBackgroundResource(UiUtils.getColorForTypeID(TypeID));
                TextView name = (TextView)v.findViewById(R.id.tv_name);
                String nameStr = addArray.get(TypeID).getName();
                name.setText(TextUtils.isEmpty(nameStr)?"未知(mm*mm)":nameStr+"(mm*mm)");
                RecyclerView rv = (RecyclerView)v.findViewById(R.id.rv);
                LinearLayoutManager manager = new LinearLayoutManager(this);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                rv.setLayoutManager(manager);
                CancerSizeChartAdapter adapter = new CancerSizeChartAdapter(this,
                        ConstUtils.getCancerChartData(temp),UiUtils.getTypeIDForTypeID(TypeID));
                rv.setAdapter(adapter);
            }
        }
        TextView tv_delete = (TextView)v.findViewById(R.id.tv_delete);
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v_cancer_size.removeView(v);
                addArray.remove(TypeID);
            }
        });
        et1.setText(addArray.get(TypeID).getName());
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String t1 = et1.getText().toString();
                if (!TextUtils.isEmpty(t1)) {
                    addArray.get(TypeID).setName(t1);
                }
            }
        });
        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String t2 = et2.getText().toString();
                String t3 = et3.getText().toString();
                /*if (!OtherUtils.isEmpty(t2) && !OtherUtils.isEmpty(t3)) {
                    addArray.get(TypeID).setSize(t2 + "*" + t3);
                }*/
                if (!OtherUtils.isEmpty(t2) && !OtherUtils.isEmpty(t3)) {
                    addArray.get(TypeID).setSize(t2 + "*" + t3);
                } else {
                    if (OtherUtils.isEmpty(t3) && OtherUtils.isEmpty(t2)) {
                        addArray.get(TypeID).setSize("");
                    } else {
                        addArray.get(TypeID).setSize("0");
                    }
                }
            }
        });
        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String t2 = et2.getText().toString();
                String t3 = et3.getText().toString();
                if (!OtherUtils.isEmpty(t2) && !OtherUtils.isEmpty(t3)) {
                    addArray.get(TypeID).setSize(t2 + "*" + t3);
                } else {
                    if (OtherUtils.isEmpty(t3) && OtherUtils.isEmpty(t2)) {
                        addArray.get(TypeID).setSize("");
                    } else {
                        addArray.get(TypeID).setSize("0");
                    }
                }
            }
        });
        v_cancer_size.addView(v);
    }

    private View v_add;
    private void getAddCancerView(){
        v_add = LayoutInflater.from(this).inflate(R.layout.layout_cancer_size_add,v_cancer_size,false);
        TextView tv = (TextView)v_add.findViewById(R.id.tv_add);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAdd();
            }
        });
        v_cancer_size.addView(v_add);
    }

    private void toAdd(){
        v_cancer_size.removeView(v_add);
        int TypeID = ConstUtils.getTypeIDNext(cancerArray);
        CancerSizeItemEntity entity = ConstUtils.getNextCancerEntity(TypeID, Integer.valueOf(UserinfoData.getInfoID(this)));
        addArray.put(TypeID, entity);
        List<CancerSizeItemEntity> temp = new ArrayList<>();
        temp.add(entity);
        if (cancerArray==null) cancerArray = new SparseArray<>();
        cancerArray.put(TypeID,temp);
        getOtherCancerView(TypeID,true);
        getAddCancerView();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_save:
                toSaveData();
                break;
            case R.id.v_record_time:
                toRecordTime();
                break;
            case R.id.v_some:
                if (type == Const.RECORD_TYPE_13){
                    startActivityForResult(new Intent(this, ChooseSymptomActivity.class).putExtra("CheckedList",check), 100);
                }else if (type == Const.RECORD_TYPE_11){
                    showGeneDialog();
                }else if (type == Const.RECORD_TYPE_12){
                    showGeneDialog();
                }
                break;
            case R.id.v_choose_quota_type:
                startActivityForResult(new Intent(this,ChooseQuotaTypeActivity.class)
                        .putExtra(Const.RECORD_CLASSIFY_QUOTA_STATUS, QUOTA_STATUS)
                        .putExtra(Const.RECORD_CLASSIFY_QUOTA_CHECKED, check),100);
                break;
        }
    }

    private String RecordTime = "";
    private void toRecordTime(){
        ChooseTimeFragment fragment = ChooseTimeFragment.getInstance(new ChooseTimeInterface() {
            @Override
            public void timeCallBack(String time) {
                tv_record_time.setText(TextUtils.isEmpty(time)?"":time);
                RecordTime = DataUtils.showTimeToLoadTime(time);
            }
        });
        fragment.show(getFragmentManager(),ChooseTimeFragment.type);
    }

    private void toSaveData(){
        if (type == Const.RECORD_TYPE_13){
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
            if (ConstUtils.isEmptyForDataString(addArray)){
                if (TextUtils.isEmpty(et_hospital.getText().toString())
                        &&TextUtils.isEmpty(et_doctor.getText().toString())
                        &&TextUtils.isEmpty(et_remark.getText().toString())
                        &&(selectedPicture==null||selectedPicture.size()==0)){
                    showToast("请至少填写一项数据");
                    return;
                }
            }else {
                CancerSizeItemEntity entity;
                for (int i=0;i<addArray.size();i++){
                    entity = addArray.get(addArray.keyAt(i));
                    if (TextUtils.isEmpty(entity.getName())){
                        showToast("肿瘤名称不能为空");
                        return;
                    }
                    if ("0".equals(entity.getSize())){
                        showToast("\""+entity.getName()+"\"的肿瘤长度或宽度为空,请填写");
                        return;
                    }
                }
            }
            toUpdateData();

        } else if(type == Const.RECORD_TYPE_15){

            if (TextUtils.isEmpty(RecordTime)){
                showToast("请选择日期");
                return;
            }
            if (ConstUtils.isEmptyForDataString(addArray)&&TextUtils.isEmpty(et_hospital.getText().toString())
                    &&TextUtils.isEmpty(et_doctor.getText().toString())
                    &&TextUtils.isEmpty(et_remark.getText().toString())
                    &&(selectedPicture==null||selectedPicture.size()==0)){
                showToast("请至少填写一项数据");
                return;
            }
            toUpdateData();
        } else {
            toSave();
        }
    }

    private ProgressDialog mProgressDialog;
    private void toSave(){

        if (TextUtils.isEmpty(RecordTime)){
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

    //基因选择窗口
    private void showGeneDialog() {
        try {
            List<String> data = new ArrayList<>();
            if (type == Const.RECORD_TYPE_11){
                String CancerID = UserinfoData.getCancerID(this);
                if (TextUtils.isEmpty(CancerID)){
                    showToast("癌种获取失败");
                    return;
                }
                Map<String, List<String>> gene = (Map<String, List<String>>)Factory.getData(Const.N_DataGene);
                data = gene.get(CancerID);
                if (data == null || data.isEmpty()) {
                    showToast(getString(R.string.cancer_has_no_gene));
                    return;
                }

            }else if (type == Const.RECORD_TYPE_12){
                getTransData();
                Set<String> set = transValues.keySet();
                Iterator<String> iterator = set.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    data.add(key);
                }
            }
            GeneDialog dialog = new GeneDialog(data, check, type==Const.RECORD_TYPE_11?0:1);
            dialog.setTitle(type == Const.RECORD_TYPE_11?"请选择突变情况":"请选择转移部位");
            dialog.setDialogFragmentListener(this);
            dialog.show(getFragmentManager(), Contants.GENE_DIALOG);

        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "PatientDetailActivity");
        }
    }

    //选择基因和转移部位回调方法
    @Override
    public void getDataFromDialog(DialogFragment dialog, String data, int position) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment geneDialog = fragmentManager.findFragmentByTag(Contants.GENE_DIALOG);
        if (dialog == geneDialog) {
            try {
                check = new ArrayList<>();
                if (OtherUtils.isEmpty(data)){
                    tv_some.setText("");
                }else {
                    getGeneData();
                    String[] temp = data.split(",");
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < temp.length; i++) {
                        check.add(temp[i]);
                        if (i != 0) {
                            sb.append(",");
                        }
                        sb.append(type==Const.RECORD_TYPE_11?geneValues.get(temp[i]):transValues.get(temp[i]));
                    }
                    tv_some.setText(sb.toString());
                }
            }catch (Exception e){
                ExceptionUtils.ExceptionSend(e,"getDataFromDialog RecordActivity");
            }
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
        String doctor = et_doctor.getText().toString();
        if (!TextUtils.isEmpty(doctor)){
            map.put("doctor",doctor);
        }
        String remark = et_remark.getText().toString();
        if (!TextUtils.isEmpty(remark)){
            map.put("Remark", DecryptUtils.encodeAndURL(remark));
        }
        map.put("RecordTime",RecordTime);
        if (urls!=null&&urls.size()>0){
            map.put("pic", ConstUtils.getParamsForPic(urls));
        }
        if (tag == Const.PAddPresentationOther){
            map.put("Type",type+"");
        }else if (tag == Const.PAddStep2Perform){
            if (check!=null&&check.size()>0){
                map.put("perform",ConstUtils.getParamsForPic(check));
            }
        }else if (tag == Const.PAddTransferGen){
            if (check!=null&&check.size()>0){
                map.put("GeneID",ConstUtils.getParamsForPic(check));
            }
        }else if (tag == Const.PAddTransferRecord){
            if (check!=null&&check.size()>0){
                map.put("TransferBody",ConstUtils.getParamsForPic(check));
            }
        }else if (tag == Const.PAddQuotaMasterSlave){
            for (int i=0;i<addArray.size();i++){
                addArray.get(addArray.keyAt(i)).setRecordTime(Long.parseLong(RecordTime));
                addArray.get(addArray.keyAt(i)).setDateline(System.currentTimeMillis()/1000);
            }
            map.put("TumourData",ConstUtils.getTumourDataString(addArray));
        }else if (tag == Const.PAddCancerMark){
            for (int i=0;i<addArray.size();i++){
                addArray.get(addArray.keyAt(i)).setRecordTime(Long.parseLong(RecordTime));
                addArray.get(addArray.keyAt(i)).setDateline(System.currentTimeMillis()/1000);
            }
            map.put("CancerMark",ConstUtils.getTumourDataString(addArray));
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
            }else if (resultCode == Const.REQUEST_CODE_CHOOSE_SYMPTOM){
                check = data.getStringArrayListExtra("CheckList");
                if (check!=null&&check.size()>0){
                    String perfrom = "";
                    int i = 0;
                    for (String str:check){
                        if (i==0){
                            perfrom = MapDataUtils.getPerfromValues(str);
                            i++;
                        }else {
                            perfrom += "," + MapDataUtils.getPerfromValues(str);
                        }
                    }
                    tv_some.setText(perfrom);
                }
                String temp = data.getStringExtra("SymptomRemark");
                if (!TextUtils.isEmpty(temp)){
                    String remark = et_remark.getText().toString();
                    if (TextUtils.isEmpty(remark)){
                        et_remark.setText("今天出现了“"+temp+"”\n");
                    }else {
                        remark = "今天出现了“"+temp+"”\n" + remark;
                        et_remark.setText(remark);
                    }
                    hasSymptomRemark = true;
                }
            }else if (resultCode == Const.REQUEST_CODE_CHOOSE_QUOTA_TYPE){
                Request_Is_Changed = true;
                check = data.getStringArrayListExtra(Const.RECORD_REQUEST_QUOTA_TYPE);
                if (check==null) check = new ArrayList<>();
                getQuotaItem();
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
                ||!TextUtils.isEmpty(et_doctor.getText().toString())
                ||!TextUtils.isEmpty(et_remark.getText().toString())
                ||(selectedPicture!=null&&selectedPicture.size()>0)){
            return true;
        }
        switch (type){
            case Const.RECORD_TYPE_11:
            case Const.RECORD_TYPE_12:
            case Const.RECORD_TYPE_13:
                if (check!=null&&check.size()>0) return true;
                break;
        }
        return false;
    }

}

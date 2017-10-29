package com.zeyuan.kyq.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.zeyuan.kyq.Entity.CancerSizeItemEntity;
import com.zeyuan.kyq.Entity.RecordItemEntity;
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
 * Created by Administrator on 2017/2/22.
 * <p>
 * 编辑记录
 *
 * @author wwei
 */
public class EditRecordActivity extends BaseActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener
        , MyLayout.OnSoftKeyboardListener, View.OnClickListener, HttpResponseInterface, DialogFragmentListener {


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
    //待编辑的记录数据
    private RecordItemEntity mEntity;
    private RecordItemEntity copyEntity;
    private boolean isMedical = false;
    private int QUOTA_STATUS;

    /**
     * 输入框小数的位数
     */
    private static final int DECIMAL_DIGITS = 2;
    /**
     * 设置小数位数控制
     */
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

    private ArrayList<String> check;

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

    private void initType() {

        type = getIntent().getIntExtra(Const.RECORD_CLASSIFY_TYPE, Const.RECORD_TYPE_8);
        mEntity = (RecordItemEntity) getIntent().getSerializableExtra(Const.RECORD_CLASSIFY_DATA);
        LogCustom.i("ZYS", mEntity.toString());
        REQUEST_FLAG = getIntent().getBooleanExtra(Const.RECORD_REQUEST_FLAG, false);
        isMedical = getIntent().getBooleanExtra(Const.RECORD_EDIT_FROM_MEDICAL, false);
        LogCustom.i("ZYS", "isMedical" + isMedical);
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

    private LinearLayout v_cancer_size;

    private void initView() {

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(UiUtils.getEditRecordClassifyTitle(type));

        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_record_time = (TextView) findViewById(R.id.tv_record_time);
        v_record_time = findViewById(R.id.v_record_time);

        et_remark = (EditText) findViewById(R.id.et_remark);
        et_hospital = (EditText) findViewById(R.id.et_hospital);
        et_doctor = (EditText) findViewById(R.id.et_doctor);
        v_hospital = findViewById(R.id.v_hospital);

        gv = (MyGridView) findViewById(R.id.gv);
        selectedPicture = new ArrayList<>();
        List<String> temp = mEntity.getPic();
        if (temp == null) temp = new ArrayList<>();
        adapter = new RecordPhotoAdapter(this, temp, true);
        gv.setAdapter(adapter);

        sv = (CustomScrollView) findViewById(R.id.sv);

        mLayout = (MyLayout) findViewById(R.id.my_layout);

        tv_record_time.setText(DataUtils.getRecordDate(mEntity.getRecordTime()));
        et_hospital.setText(TextUtils.isEmpty(mEntity.getHospital()) ? "" : mEntity.getHospital());
        et_doctor.setText(TextUtils.isEmpty(mEntity.getDoctor()) ? "" : mEntity.getDoctor());
        String temp_remark = mEntity.getRemark();
        et_remark.setText(TextUtils.isEmpty(temp_remark) ? "" : temp_remark);

        switch (type) {
            case Const.RECORD_TYPE_11://基因
                initSomeView();
                tv_some_text.setText("基因情况");
                String g_str = mEntity.getGeneID();
                tv_some.setText(MapDataUtils.getGeneForString(g_str));
                check = ConstUtils.getArrayListForString(g_str);
                break;
            case Const.RECORD_TYPE_12://转移
                initSomeView();
                tv_some_text.setText("转移情况");
                String t_str = mEntity.getTransferBody();
                tv_some.setText(MapDataUtils.getTranslateForString(t_str));
                check = ConstUtils.getArrayListForString(t_str);
                break;
            case Const.RECORD_TYPE_13://症状
                initSomeView();
                tv_some_text.setText("出现的症状");
                String p_str = mEntity.getPerform();
                tv_some.setText(MapDataUtils.getPerformForString(p_str));
                check = ConstUtils.getArrayListForString(p_str);
                break;
            case Const.RECORD_TYPE_14://记录肿瘤大小
                List<CancerSizeItemEntity> temp2 = (List<CancerSizeItemEntity>) getIntent()
                        .getSerializableExtra(Const.RECORD_CLASSIFY_CANCER_SIZE);
                InfoID = Integer.valueOf(UserinfoData.getInfoID(this));
                cancerArray = ConstUtils.getArrayForCancerSizeList(temp2, InfoID);
                initCancerSizeView();
                break;
            case Const.RECORD_TYPE_15://指标

                List<CancerSizeItemEntity> id_checked = mEntity.getCancerMark();
                check = new ArrayList<>();
                if (id_checked != null && id_checked.size() > 0) {
                    for (CancerSizeItemEntity entity : id_checked) {
                        check.add(entity.getTypeID() + "");
                    }
                }
                List<CancerSizeItemEntity> temp1 = (List<CancerSizeItemEntity>) getIntent()
                        .getSerializableExtra(Const.RECORD_CLASSIFY_CANCER_SIZE);
                InfoID = Integer.valueOf(UserinfoData.getInfoID(this));
                cancerArray = ConstUtils.getArrayForCancerSizeList(temp1, InfoID);
                if (cancerArray == null) cancerArray = new SparseArray<>();//防空指针
                initQuotaView();
                break;
        }

    }


    //基因、转移、症状控件
    private View v_some;
    //基因、转移、症状文本
    private TextView tv_some_text;
    //基因、转移、症状数据
    private TextView tv_some;

    private void initSomeView() {
        try {
            v_some = findViewById(R.id.v_some);
            tv_some_text = (TextView) findViewById(R.id.tv_some_text);
            tv_some = (TextView) findViewById(R.id.tv_some);
            tv_some_text.setText(type == Const.RECORD_TYPE_11 ? "基因情况" :
                    (type == Const.RECORD_TYPE_12 ? "转移情况" : "出现的症状"));
            v_some.setVisibility(View.VISIBLE);
            v_some.setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "initSomeView");
        }
    }

    private void initQuotaView() {
        try {
            findViewById(R.id.v_choose_quota_type_patient).setVisibility(View.VISIBLE);
            findViewById(R.id.v_choose_quota_type).setOnClickListener(this);
            v_cancer_size = (LinearLayout) findViewById(R.id.v_cancer_size);
            v_cancer_size.setVisibility(View.VISIBLE);
            getQuotaItem();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "initQuotaView");
        }
    }

    private void getQuotaItem() {
        try {
            v_cancer_size.removeAllViews();
            addArray = ConstUtils.getQuotaCharArrayEdit(cancerArray, check, InfoID, mEntity, true);
            if (addArray != null && addArray.size() > 0) {
                int[] TypeIDArray = ConstUtils.getTypeIDArray(addArray);
                for (int i = 0; i < TypeIDArray.length; i++) {
                    int TypeID = TypeIDArray[i];
                    getQuotaItemView(TypeID, false);
                }
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "getQuotaItem");
        }
    }

    private void getQuotaItemView(final int TypeID, boolean isFirst) {
        final View v = LayoutInflater.from(this).inflate(R.layout.layout_quota_record_item, v_cancer_size, false);
        final TextView tv = (TextView) v.findViewById(R.id.tv);
        final EditText et = (EditText) v.findViewById(R.id.et);
        et.setFilters(new InputFilter[]{lengthfilter});
        if (isFirst) {
            v.findViewById(R.id.v_space_top).setVisibility(View.GONE);
        }
        tv.setText(UiUtils.getNameENforTypeID(TypeID));
        String size = addArray.get(TypeID).getSize();
        if (!TextUtils.isEmpty(size)) {
            et.setText(size);
        } else {
            et.setHint("请输入" + UiUtils.getNameENforTypeID(TypeID) + "指标");
        }
        List<CancerSizeItemEntity> temp = cancerArray.get(TypeID);
        if (temp != null && temp.size() > 0) {
            v.findViewById(R.id.v_chart).setVisibility(View.VISIBLE);
            v.findViewById(R.id.v_color_1).setBackgroundResource(getBGColorForTypeID(TypeID));
            v.findViewById(R.id.v_color_2).setBackgroundResource(getColorForTypeID(TypeID));
            TextView name = (TextView) v.findViewById(R.id.tv_name);
            String nameStr = UiUtils.getNameENforTypeID(TypeID) + "(" + UiUtils.getUnitForType(TypeID) + ")";
            name.setText(nameStr);
            RecyclerView rv = (RecyclerView) v.findViewById(R.id.rv);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rv.setLayoutManager(manager);
            CancerSizeChartAdapter adapter = new CancerSizeChartAdapter(this,
                    ConstUtils.getQuotaChartData(temp), getTypeIDForTypeID(TypeID), true);
            rv.setAdapter(adapter);
        }
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

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
                        et.setHint("请输入" + UiUtils.getNameENforTypeID(TypeID) + "指标");
                    }
                }
            }
        });
        v_cancer_size.addView(v);
    }


    private int InfoID = Integer.valueOf(UserinfoData.getInfoID(this));

    private void initCancerSizeView() {
        try {
            v_cancer_size = (LinearLayout) findViewById(R.id.v_cancer_size);
            v_cancer_size.setVisibility(View.VISIBLE);
            getCancerSizeItem();
            getAddCancerView();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "initCancerSizeView");
        }
    }

    private SparseArray<List<CancerSizeItemEntity>> cancerArray = null;
    private SparseArray<CancerSizeItemEntity> addArray = new SparseArray<>();

    private void getCancerSizeItem() {
        SparseArray<CancerSizeItemEntity> temp = ConstUtils.getCancerSizeCharArray(cancerArray, InfoID);
        addArray = ConstUtils.getEditCancerChartArray(temp, mEntity, InfoID);
        int[] TypeIDArray = ConstUtils.getTypeIDArray(addArray);
        for (int i = 0; i < TypeIDArray.length; i++) {
            int TypeID = TypeIDArray[i];
            if (TypeID == 1) {
                getMainCancerView();
            } else {
                getOtherCancerView(TypeID, false);
            }
        }
    }

    //主肿瘤UI
    private void getMainCancerView() {
        try {
            View v = LayoutInflater.from(this).inflate(R.layout.layout_cancer_size_main, v_cancer_size, false);
            final EditText et2 = (EditText) v.findViewById(R.id.et2);
            final EditText et3 = (EditText) v.findViewById(R.id.et3);

            try {
                String size = addArray.get(1).getSize();
                if (!OtherUtils.isEmpty(size)) {
                    int index = size.indexOf("*");
                    String width = size.substring(0, index);
                    String height = size.substring(index + 1, size.length());
                    et2.setText(width);
                    et3.setText(height);
                }
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "getMainCancerView 肿瘤大小拆分错误");
            }
            List<CancerSizeItemEntity> temp = cancerArray.get(1);
            if (temp != null && temp.size() > 0) {
                v.findViewById(R.id.v_chart).setVisibility(View.VISIBLE);
                RecyclerView rv = (RecyclerView) v.findViewById(R.id.rv);
                LinearLayoutManager manager = new LinearLayoutManager(this);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                rv.setLayoutManager(manager);
                CancerSizeChartAdapter adapter = new CancerSizeChartAdapter(this, ConstUtils.getCancerChartData(temp), 0);
                rv.setAdapter(adapter);
            }
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
                    if (!OtherUtils.isEmpty(t2) && !OtherUtils.isEmpty(t3)) {
                        addArray.get(1).setSize(t2 + "*" + t3);
                    } else {
                        if (OtherUtils.isEmpty(t3) && OtherUtils.isEmpty(t2)) {
                            addArray.get(1).setSize("");
                        } else {
                            addArray.get(1).setSize("0");
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
                        addArray.get(1).setSize(t2 + "*" + t3);
                    } else {
                        if (OtherUtils.isEmpty(t3) && OtherUtils.isEmpty(t2)) {
                            addArray.get(1).setSize("");
                        } else {
                            addArray.get(1).setSize("0");
                        }
                    }
                }
            });
            v_cancer_size.addView(v);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "getMainCancerView");
        }
    }

    private void getOtherCancerView(final int TypeID, boolean isAdd) {
        final View v = LayoutInflater.from(this).inflate(R.layout.layout_cancer_size_item, v_cancer_size, false);
        final EditText et1 = (EditText) v.findViewById(R.id.et1);
        final EditText et2 = (EditText) v.findViewById(R.id.et2);
        final EditText et3 = (EditText) v.findViewById(R.id.et3);

        if (!isAdd) {
            try {
                String size = addArray.get(TypeID).getSize();
                int index = size.indexOf("*");
                String width = size.substring(0, index);
                String height = size.substring(index + 1, size.length());
                et2.setText(width);
                et3.setText(height);
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "getOtherCancerView 肿瘤大小拆分错误");
            }
            List<CancerSizeItemEntity> temp = cancerArray.get(TypeID);
            if (temp != null && temp.size() > 0) {
                v.findViewById(R.id.v_chart).setVisibility(View.VISIBLE);
                v.findViewById(R.id.v_color_1).setBackgroundResource(getBGColorForTypeID(TypeID));
                v.findViewById(R.id.v_color_2).setBackgroundResource(getColorForTypeID(TypeID));
                TextView name = (TextView) v.findViewById(R.id.tv_name);
                String nameStr = addArray.get(TypeID).getName();
                name.setText(TextUtils.isEmpty(nameStr) ? "未知(mm*mm)" : nameStr + "(mm*mm)");
                RecyclerView rv = (RecyclerView) v.findViewById(R.id.rv);
                LinearLayoutManager manager = new LinearLayoutManager(this);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                rv.setLayoutManager(manager);
                CancerSizeChartAdapter adapter = new CancerSizeChartAdapter(this,
                        ConstUtils.getCancerChartData(temp), getTypeIDForTypeID(TypeID));
                rv.setAdapter(adapter);
            }
        }
        TextView tv_delete = (TextView) v.findViewById(R.id.tv_delete);
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

    private void getAddCancerView() {
        v_add = LayoutInflater.from(this).inflate(R.layout.layout_cancer_size_add, v_cancer_size, false);
        TextView tv = (TextView) v_add.findViewById(R.id.tv_add);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAdd();
            }
        });
        v_cancer_size.addView(v_add);
    }

    private void toAdd() {
        v_cancer_size.removeView(v_add);
        int TypeID = ConstUtils.getTypeIDNext(cancerArray);
        CancerSizeItemEntity entity = ConstUtils.getNextCancerEntity(TypeID, Integer.valueOf(UserinfoData.getInfoID(this)));
        addArray.put(TypeID, entity);
        List<CancerSizeItemEntity> temp = new ArrayList<>();
        temp.add(entity);
        if (cancerArray == null) cancerArray = new SparseArray<>();
        cancerArray.put(TypeID, temp);
        getOtherCancerView(TypeID, true);
        getAddCancerView();
    }


    private void initListener() {

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

    private void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:
                // toSaveData();
                toSave();
                break;
            case R.id.v_record_time:
                toRecordTime();
                break;
            case R.id.v_some:
                if (type == Const.RECORD_TYPE_13) {
                    startActivityForResult(new Intent(this, ChooseSymptomActivity.class).putExtra("CheckedList", check), 100);
                } else if (type == Const.RECORD_TYPE_11) {
                    showGeneDialog();
                } else if (type == Const.RECORD_TYPE_12) {
                    showGeneDialog();
                }
                break;
            case R.id.v_choose_quota_type:
                startActivityForResult(new Intent(this, ChooseQuotaTypeActivity.class)
                        .putExtra(Const.RECORD_CLASSIFY_QUOTA_STATUS, QUOTA_STATUS)
                        .putExtra(Const.RECORD_CLASSIFY_QUOTA_CHECKED, check), 100);
                break;
        }
    }

    //基因选择窗口
    private void showGeneDialog() {
        try {
            List<String> data = new ArrayList<>();
            if (type == Const.RECORD_TYPE_11) {
                String CancerID = UserinfoData.getCancerID(this);
                if (TextUtils.isEmpty(CancerID)) {
                    showToast("癌种获取失败");
                    return;
                }
                Map<String, List<String>> gene = (Map<String, List<String>>) Factory.getData(Const.N_DataGene);
                data = gene.get(CancerID);
                if (data == null || data.isEmpty()) {
                    showToast(getString(R.string.cancer_has_no_gene));
                    return;
                }

            } else if (type == Const.RECORD_TYPE_12) {
                getTransData();
                Set<String> set = transValues.keySet();
                Iterator<String> iterator = set.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    data.add(key);
                }
            }
            GeneDialog dialog = new GeneDialog(data, check, type == Const.RECORD_TYPE_11 ? 0 : 1);
            dialog.setTitle(type == Const.RECORD_TYPE_11 ? "请选择突变情况" : "请选择转移部位");
            dialog.setDialogFragmentListener(this);
            dialog.show(getFragmentManager(), Contants.GENE_DIALOG);

        } catch (Exception e) {
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
                if (OtherUtils.isEmpty(data)) {
                    tv_some.setText("");
                } else {
                    getGeneData();
                    String[] temp = data.split(",");
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < temp.length; i++) {
                        check.add(temp[i]);
                        if (i != 0) {
                            sb.append(",");
                        }
                        sb.append(type == Const.RECORD_TYPE_11 ? geneValues.get(temp[i]) : transValues.get(temp[i]));
                    }
                    tv_some.setText(sb.toString());
                }
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "getDataFromDialog RecordActivity");
            }
        }
    }

    private Map<String, String> geneValues;

    //获取基因配置数据
    private void getGeneData() {
        if (geneValues == null) {
            geneValues = (Map<String, String>) Factory.getData(Const.N_DataGeneValues);
        }
    }

    private Map<String, String> transValues;

    //获取转移部位配置数据
    private void getTransData() {
        if (transValues == null) {
            transValues = (Map<String, String>) Factory.getData(Const.N_DataTransferPos);
        }
    }

    private String RecordTime = "";

    private void toRecordTime() {
        try {
            ChooseTimeFragment fragment = ChooseTimeFragment.getInstance(new ChooseTimeInterface() {
                @Override
                public void timeCallBack(String time) {
                    tv_record_time.setText(TextUtils.isEmpty(time) ? "" : time);
                    RecordTime = DataUtils.showTimeToLoadTime(time);
                }
            });
            fragment.show(getFragmentManager(), ChooseTimeFragment.type);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "EditRecordActivity");
        }
    }

    private ProgressDialog mProgressDialog;

    private void toSaveData() {
        if (type == Const.RECORD_TYPE_13) {
            if ((check == null || check.size() == 0) && !hasSymptomRemark) {
                showToast("请选择症状");
                return;
            }

            toUpdateData();
        } else if (type == Const.RECORD_TYPE_11 || type == Const.RECORD_TYPE_12) {
            if ((check == null || check.size() == 0)) {
                showToast(type == Const.RECORD_TYPE_11 ? "请选择基因" : "请选择转移部位");
                return;
            }

            toUpdateData();
        } else if (type == Const.RECORD_TYPE_14) {
            try {
                if (addArray == null || addArray.size() == 0) return;
                CancerSizeItemEntity entity;
                for (int i = 0; i < addArray.size(); i++) {
                    entity = addArray.get(addArray.keyAt(i));
                    if (TextUtils.isEmpty(entity.getName())) {
                        showToast("肿瘤名称不能为空");
                        return;
                    }
                    if ("0".equals(entity.getSize())) {
                        showToast("\"" + entity.getName() + "\"的肿瘤长度或宽度为空,请填写");
                        return;
                    }
                }

                toUpdateData();
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "toSaveData");
            }
        } else if (type == Const.RECORD_TYPE_15) {

            if ((addArray == null || addArray.size() == 0) && TextUtils.isEmpty(et_hospital.getText().toString())
                    && TextUtils.isEmpty(et_doctor.getText().toString())
                    && TextUtils.isEmpty(et_remark.getText().toString())
                    && (selectedPicture == null || selectedPicture.size() == 0)) {
                showToast("请至少填写一项数据");
                return;
            }
            toUpdateData();
        } else {
            toSave();
        }
    }

    private void toSave() {

        if (TextUtils.isEmpty(et_hospital.getText().toString())
                && TextUtils.isEmpty(et_doctor.getText().toString())
                && TextUtils.isEmpty(et_remark.getText().toString())
                && (selectedPicture == null || selectedPicture.size() == 0)) {
            showToast("请至少填写一项数据");
            return;
        }
        toUpdateData();
    }

    private void toUpdateData() {
        try {
            tv_save.setClickable(false);
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("正在保存");
            mProgressDialog.show();
            if (selectedPicture != null && selectedPicture.size() > 0) {//说明是有图片的
                uploadPhoto();
            } else {
                toPost();
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "toUpdateData");
        }
    }

    private void toPost() {
        switch (type) {
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

    /*private void toSave(){

        if (TextUtils.isEmpty(et_hospital.getText().toString())
                &&TextUtils.isEmpty(et_doctor.getText().toString())
                &&TextUtils.isEmpty(et_remark.getText().toString())
                &&((selectedPicture==null||selectedPicture.size()==0)&&adapter.getUrlCount()==0)){
            showToast("请至少填写一项数据");
            return;
        }

        tv_save.setClickable(false);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在保存");
        mProgressDialog.show();
        if (selectedPicture != null && selectedPicture.size() > 0) {//说明是有图片的
            uploadPhoto();
        } else {
            Factory.postPhp(this, Const.PAddPresentationOther);
        }
    }*/

    private List<String> urls;//这个里面装着上传到cdn的url

    private void uploadPhoto() {
        try {
            urls = new ArrayList<>();
            final int index = selectedPicture.size();
            for (int i = 0; i < selectedPicture.size(); i++) {
                final CDNHelper get = new CDNHelper(this);
                try {
                    String imageName = getImgName(this, false);
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
                                        toPost();
                                    }
                                });
                            }
                        }
                    });

                    final CDNHelper gets = new CDNHelper(this);
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

                } catch (Exception e) {
                    ExceptionUtils.ExceptionToUM(e, this, "EditRecordActivity");
                }
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "EditRecordActivity");
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        copyEntity = ConstUtils.copyRecordItemEntity(mEntity);
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        String hospital = et_hospital.getText().toString();
        if (!TextUtils.isEmpty(hospital)) {
            map.put("hospital", hospital);
            copyEntity.setHospital(hospital);
        } else {
            map.put("hospital", "");
            copyEntity.setHospital("");
        }
        String doctor = et_doctor.getText().toString();
        if (!TextUtils.isEmpty(doctor)) {
            map.put("doctor", doctor);
            copyEntity.setDoctor(doctor);
        } else {
            map.put("doctor", "");
            copyEntity.setDoctor("");
        }
        String remark = et_remark.getText().toString();
        if (!TextUtils.isEmpty(remark)) {
            map.put("Remark", DecryptUtils.encodeAndURL(remark));
            copyEntity.setRemark(remark);
        } else {
            map.put("Remark", "");
            copyEntity.setRemark("");
        }

        RecordTime = TextUtils.isEmpty(RecordTime) ? mEntity.getRecordTime() + "" : RecordTime;

        map.put("RecordTime", RecordTime);
        copyEntity.setRecordTime(Long.parseLong(RecordTime));
        map.put("ID", mEntity.getID() + "");
        List<String> temp = adapter.getUrlList();
        if (urls != null && urls.size() > 0) {
            temp.addAll(urls);
        }
        if (temp.size() == 0) {
            map.put("pic", "");
        } else {
            map.put("pic", ConstUtils.getParamsForPic(temp));
        }
        copyEntity.setPic(temp);
        if (tag == Const.PAddPresentationOther) {
            map.put("Type", type + "");
        } else if (tag == Const.PAddStep2Perform) {
            if (check != null && check.size() > 0) {
                map.put("perform", ConstUtils.getParamsForPic(check));
                copyEntity.setPerform(ConstUtils.getParamsForPic(check));
            } else {
                map.put("perform", "");
                copyEntity.setPerform("");
            }
        } else if (tag == Const.PAddTransferGen) {
            if (check != null && check.size() > 0) {
                map.put("GeneID", ConstUtils.getParamsForPic(check));
                copyEntity.setGeneID(ConstUtils.getParamsForPic(check));
            } else {
                map.put("GeneID", "");
                copyEntity.setGeneID("");
            }
        } else if (tag == Const.PAddTransferRecord) {
            if (check != null && check.size() > 0) {
                map.put("TransferBody", ConstUtils.getParamsForPic(check));
                copyEntity.setTransferBody(ConstUtils.getParamsForPic(check));
            } else {
                map.put("TransferBody", "");
                copyEntity.setTransferBody("");
            }
        } else if (tag == Const.PAddQuotaMasterSlave) {
            for (int i = 0; i < addArray.size(); i++) {
                addArray.get(addArray.keyAt(i)).setRecordTime(Long.parseLong(RecordTime));
                addArray.get(addArray.keyAt(i)).setDateline(System.currentTimeMillis() / 1000);
                addArray.get(addArray.keyAt(i)).setInfoID(InfoID);
                addArray.get(addArray.keyAt(i)).setPatientID(InfoID);
            }
            map.put("TumourData", ConstUtils.getTumourDataString(addArray));
        } else if (tag == Const.PAddCancerMark) {
            for (int i = 0; i < addArray.size(); i++) {
                addArray.get(addArray.keyAt(i)).setRecordTime(Long.parseLong(RecordTime));
                addArray.get(addArray.keyAt(i)).setDateline(System.currentTimeMillis() / 1000);
                addArray.get(addArray.keyAt(i)).setInfoID(InfoID);
                addArray.get(addArray.keyAt(i)).setPatientID(InfoID);
            }
            map.put("CancerMark", ConstUtils.getTumourDataString(addArray));
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
                || flag == Const.PAddQuotaMasterSlave || flag == Const.PAddCancerMark) {
            PhpUserInfoBean bean = (PhpUserInfoBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                showToast("编辑成功");
                if (REQUEST_FLAG) {
                    Request_Is_Changed = true;
                }
                finish();
            } else {
                showToast("编辑失败");
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
                || flag == Const.PAddQuotaMasterSlave || flag == Const.PAddCancerMark) {
            if (mProgressDialog != null) mProgressDialog.dismiss();
            tv_save.setClickable(true);
        }
    }

    @Override
    public void showError(int flag) {
        if (flag == Const.PAddPresentationOther || flag == Const.PAddStep2Perform
                || flag == Const.PAddTransferGen || flag == Const.PAddTransferRecord
                || flag == Const.PAddQuotaMasterSlave || flag == Const.PAddCancerMark) {
            if (mProgressDialog != null) mProgressDialog.dismiss();
            tv_save.setClickable(true);
        }
        showToast("网络请求失败");
    }

    private static final int STORAGE_AND_CAMERA_PERMISSIONS = 12;
    private int selectedIndex = 0;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.gv) {
            try {
                if (position == adapter.getLastPosition()) {
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
                        index = 10 - index;
                        intent.putExtra(SelectPictureActivity.INTENT_MAX_NUM, index);
                        startActivityForResult(
                                intent, REQUEST_PICK);
                    }
                } else {
                    startActivity(new Intent(this, ShowPhotoActivity.class).putExtra("list",
                            (Serializable) adapter.getList()).putExtra("position", position));
                }
            } catch (Exception e) {
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
                    try {
                        if (adapter.getUrlCount() == 0 || position > adapter.getUrlCount() - 1) {
                            if (adapter.getUrlCount() == 0) {
                                selectedPicture.remove(position);
                            } else {
                                selectedPicture.remove(position - adapter.getUrlCount() + 1);
                            }
                        }
                        adapter.removeItemForPosition(position);
                    } catch (Exception e) {
                        ExceptionUtils.ExceptionSend(e, "item_long");
                    }
                }
            });
            builder.create().show();
        } catch (Exception e) {
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
                startActivityForResult(
                        intent, REQUEST_PICK);
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
                //注意对象内存地址指向问题
                List<String> temp = adapter.getUrlList();
                List<String> all = new ArrayList<>();
                all.addAll(temp);
                all.addAll(selectedPicture);
                adapter.updateDate(all);
            } else if (resultCode == Const.REQUEST_CODE_CHOOSE_SYMPTOM) {
                check = data.getStringArrayListExtra("CheckList");
                if (check != null && check.size() > 0) {
                    String perfrom = "";
                    int i = 0;
                    for (String str : check) {
                        if (i == 0) {
                            perfrom = MapDataUtils.getPerfromValues(str);
                            i++;
                        } else {
                            perfrom += "," + MapDataUtils.getPerfromValues(str);
                        }
                    }
                    tv_some.setText(perfrom);
                }
                String temp = data.getStringExtra("SymptomRemark");
                if (!TextUtils.isEmpty(temp)) {
                    String remark = et_remark.getText().toString();
                    if (TextUtils.isEmpty(remark)) {
                        et_remark.setText("今天出现了“" + temp + "”\n");
                    } else {
                        remark = "今天出现了“" + temp + "”\n" + remark;
                        et_remark.setText(remark);
                    }
                } else {
                    hasSymptomRemark = true;
                }
            } else if (resultCode == Const.REQUEST_CODE_CHOOSE_QUOTA_TYPE) {
                check = data.getStringArrayListExtra(Const.RECORD_REQUEST_QUOTA_TYPE);
                if (check == null) check = new ArrayList<>();
                getQuotaItem();
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "EditRecordActivity onActivityResult");
        }
    }

    private boolean flag = false;// 这个控制隐藏键盘的时候 回调只被调用一次

    @Override
    public void onShown() {
        try {
            flag = true;
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "EditRecordActivity onShown");
        }
    }

    @Override
    public void onHidden() {
        if (flag) {
            flag = false;
        }
    }

    @Override
    public void finish() {
        if (REQUEST_FLAG) {
            LogCustom.i("ZYS", "fin");
            if (isMedical) {
                LogCustom.i("ZYS", "fin2");
                if (Request_Is_Changed) {
                    LogCustom.i("ZYS", "fin3");
                    this.setResult(Const.RESULT_CODE_EDIT_RECORD_TO_MEDICAL, getIntent()
                            .putExtra(Const.RESULT_FLAG_FOR_DATA_CHANGED, true)
                            .putExtra(Const.RECORD_EDIT_RESULT_DATA, copyEntity));
                }
            } else {
                this.setResult(Const.REQUEST_CODE_RECORD_ACTIVITY, getIntent().putExtra("isChanged", Request_Is_Changed));
            }
        }
        super.finish();
    }

    //收起输入法
    private void dismissInput() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && imm.isActive(et_remark)) {
                imm.hideSoftInputFromWindow(et_remark.getWindowToken(), 0);
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "输入法收起错误");
        }
    }


    private int[] colors = new int[]{R.color.color_record_chart_1,
            R.color.color_record_chart_2,
            R.color.color_record_chart_3,
            R.color.color_record_chart_4,
            R.color.color_record_chart_5,
            R.color.color_record_chart_6};

    private int getColorForTypeID(int TypeID) {
        while (TypeID > 5) {
            TypeID = TypeID % 6;
        }
        return colors[TypeID];
    }

    private int[] colors_bg = new int[]{R.drawable.bg_chart_1,
            R.drawable.bg_chart_2,
            R.drawable.bg_chart_3,
            R.drawable.bg_chart_4,
            R.drawable.bg_chart_5,
            R.drawable.bg_chart_6};

    private int getBGColorForTypeID(int TypeID) {
        while (TypeID > 5) {
            TypeID = TypeID % 6;
        }
        return colors_bg[TypeID];
    }

    private int getTypeIDForTypeID(int TypeID) {
        int temp = TypeID;
        while (temp > 5) {
            temp = temp % 6;
        }
        return temp;
    }


}

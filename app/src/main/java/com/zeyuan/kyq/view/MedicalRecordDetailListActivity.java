package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.zeyuan.kyq.Entity.RecordItemEntity;
import com.zeyuan.kyq.Entity.StepUserEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.MedicalRecordDetailAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.MedicalRecordDetailBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.onEditItemListener;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.DensityUtil;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zeyuan.kyq.utils.Const.REQUEST_CODE_MEDICAL_RECORD_ACTIVITY;

/**
 * 病历详情列表页，用户在记录病情之前先展示之前的记录
 */
public class MedicalRecordDetailListActivity extends BaseActivity implements HttpResponseInterface, View.OnClickListener, onEditItemListener {
    private TextView tv_title;
    private TextView tv_record_empty;
    private List<StepUserEntity> list = new ArrayList<>();
    private XRefreshView xrv_record_detail_list;
    private ListView lv_item;
    private MedicalRecordDetailAdapter medicalRecordDetailAdapter;
    boolean refresh;
    private int RECORD_CLASSIFY_TYPE;//编辑记录所需的typeID
    private String typeId = "";//获取记录所需的typeid
    private String otherId = "";//当typeId为4时，需要传这个id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_record_detail_list);
        initView();
        initData();
    }


    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_record_empty = (TextView) findViewById(R.id.tv_record_empty);
        View v_to_record = findViewById(R.id.v_to_record);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        v_to_record.setOnClickListener(this);
        xrv_record_detail_list = (XRefreshView) findViewById(R.id.xrv_record_detail_list);
        xrv_record_detail_list.setPinnedTime(1000);
        xrv_record_detail_list.setPullLoadEnable(false);
        xrv_record_detail_list.setMoveForHorizontal(true);
        lv_item = (ListView) findViewById(R.id.lv_item);
        medicalRecordDetailAdapter = new MedicalRecordDetailAdapter(this, list, DensityUtil.dip2px(this, 60), this, 0);
        lv_item.setAdapter(medicalRecordDetailAdapter);
        xrv_record_detail_list.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh = true;
                        Factory.postPhp(MedicalRecordDetailListActivity.this, Const.PApi_getUserInfoByTypeForApp);
                    }
                }, 500);
            }
        });
    }

    /*其中  TypeID 1.用户转移部位 2.记录症状3.基因转移4.其他5.肿标6.肿瘤
    当    TypeID 为4时 要传 OtherID
    OtherID 1.出院2.诊断书3.病理报告4.血常规5.粪便常规6.尿常规7.肝功能8.肾功能9.其他指标*/
    private void initData() {
        Intent intent = getIntent();
        RECORD_CLASSIFY_TYPE = intent.getIntExtra(Const.RECORD_CLASSIFY_TYPE, -1);
        String title = "";
        if (RECORD_CLASSIFY_TYPE != -1) {
            switch (RECORD_CLASSIFY_TYPE) {
                case Const.RECORD_TYPE_12://转移
                    title = "转移记录";
                    typeId = "1";
                    break;
                case Const.RECORD_TYPE_13://症状
                    title = "症状记录";
                    typeId = "2";
                    break;
                case Const.RECORD_TYPE_11://基因
                    title = "基因记录";
                    typeId = "3";
                    break;
                case Const.RECORD_TYPE_14://肿瘤大小
                    title = "肿瘤大小记录";
                    typeId = "6";
                    break;
                case Const.RECORD_TYPE_15://肿瘤指标
                    title = "肿瘤指标记录";
                    typeId = "5";
                    break;
                default://其他情况
                    typeId = "4";
                    otherId = RECORD_CLASSIFY_TYPE + "";
                    title = getOtherTitle(RECORD_CLASSIFY_TYPE);
                    break;

            }
        }

        if (!TextUtils.isEmpty(title))
            tv_title.setText(title);
        Factory.postPhp(MedicalRecordDetailListActivity.this, Const.PApi_getUserInfoByTypeForApp);
    }

    private String getOtherTitle(int type) {
        String otherTitle = "";
        switch (type) {
            case 1:
                otherTitle = "出院报告记录";
                break;
            case 2:
                otherTitle = "诊断书记录";
                break;
            case 3:
                otherTitle = "病理报告记录";
                break;
            case 4:
                otherTitle = "血常规记录";
                break;
            case 5:
                otherTitle = "粪便常规记录";
                break;
            case 6:
                otherTitle = "尿常规记录";
                break;
            case 7:
                otherTitle = "肝功能记录";
                break;
            case 8:
                otherTitle = "肾功能记录";
                break;
            case 9:
                otherTitle = "其他指标记录";
                break;
        }
        return otherTitle;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_to_record:
                if (RECORD_CLASSIFY_TYPE != -1)
                    toRecord(RECORD_CLASSIFY_TYPE);
                break;

        }
    }

    @Override
    public void onEditItem(RecordItemEntity entity, int position, int itemType, boolean isChild, int childPos) {
        try {
            Intent oldIntent = getIntent();
            Intent intent = new Intent(this, EditRecordActivity.class);
            intent.putExtra(Const.RECORD_CLASSIFY_TYPE, itemType);
            intent.putExtra(Const.RECORD_CLASSIFY_DATA, entity);
            intent.putExtra(Const.RECORD_REQUEST_FLAG, true);
            intent.putExtra(Const.RECORD_EDIT_FROM_MEDICAL, true);
            intent.putExtra(Const.RECORD_EDIT_POSITION, position);
            intent.putExtra(Const.RECORD_EDIT_CHILD_POSITION, childPos);
            intent.putExtra(Const.RECORD_EDIT_IS_CHILD, isChild);
            if (itemType == Const.RECORD_TYPE_14) {
               // intent.putExtra(Const.RECORD_CLASSIFY_CANCER_SIZE, oldIntent.getSerializableExtra(Const.RECORD_CLASSIFY_CANCER_SIZE));
                intent.putExtra(Const.RECORD_CLASSIFY_CANCER_SIZE, (Serializable) entity.getTumourInfo());
            }
            if (itemType == Const.RECORD_TYPE_15) {

                intent.putExtra(Const.RECORD_CLASSIFY_CANCER_SIZE, oldIntent.getSerializableExtra(Const.RECORD_CLASSIFY_CANCER_SIZE));
            }
            startActivityForResult(intent, REQUEST_CODE_MEDICAL_RECORD_ACTIVITY);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "MedicalRecordActivity onEditItem");
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put("InfoID", UserinfoData.getInfoID(this));
        if (tag == Const.PApi_getUserInfoByTypeForApp) {
            map.put("TypeID", typeId);
            if (typeId.equals("4"))
                map.put("OtherID", otherId);
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        xrv_record_detail_list.stopRefresh();
        try {
            if (flag == Const.PApi_getUserInfoByTypeForApp) {
                dealWithData((MedicalRecordDetailBean) response);
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "PApi_getUserInfoByTypeForApp");
        }

    }

    @Override
    public void showLoading(int flag) {

    }

    @Override
    public void hideLoading(int flag) {

    }

    @Override
    public void showError(int flag) {
        xrv_record_detail_list.stopRefresh();
    }

    //将返回数据进行封装，以符合适配器要求
    private void dealWithData(MedicalRecordDetailBean medicalRecordDetailBean) {
        List<RecordItemEntity> data = medicalRecordDetailBean.getData();
        list.clear();
        if (data != null && data.size() > 0) {
            for (RecordItemEntity ri : data) {
                StepUserEntity stepUserEntity = new StepUserEntity();
                stepUserEntity.setType(RECORD_CLASSIFY_TYPE);
                stepUserEntity.setRI(ri);
                list.add(stepUserEntity);
            }
            tv_record_empty.setVisibility(View.GONE);
            medicalRecordDetailAdapter.notifyDataSetChanged();
        } else {
            medicalRecordDetailAdapter.notifyDataSetChanged();
            tv_record_empty.setVisibility(View.VISIBLE);
        }

    }


    ///去记录
    public void toRecord(int type) {
        Intent oldIntent = getIntent();
        if (type == Const.RECORD_TYPE_14) {
            startActivityForResult(new Intent(this, RecordActivity.class)
                    .putExtra(Const.RECORD_CLASSIFY_TYPE, type)
                    .putExtra(Const.RECORD_REQUEST_FLAG, true)
                    .putExtra(Const.RECORD_CLASSIFY_CANCER_SIZE, oldIntent.getSerializableExtra(Const.RECORD_CLASSIFY_CANCER_SIZE)), 100);
        } else if (type == Const.RECORD_TYPE_15) {
            int QUOTA_STATUS = oldIntent.getIntExtra(Const.RECORD_CLASSIFY_QUOTA_STATUS, 1);
            Intent intent = new Intent(this, RecordActivity.class)
                    .putExtra(Const.RECORD_CLASSIFY_TYPE, type)
                    .putExtra(Const.RECORD_REQUEST_FLAG, true)
                    .putExtra(Const.RECORD_CLASSIFY_CANCER_SIZE, oldIntent.getSerializableExtra(Const.RECORD_CLASSIFY_CANCER_SIZE))
                    .putExtra(Const.RECORD_CLASSIFY_QUOTA_STATUS, QUOTA_STATUS);
            if (QUOTA_STATUS == 3) {
                intent.putExtra(Const.RECORD_CLASSIFY_QUOTA_CHECKED, oldIntent.getSerializableExtra(Const.RECORD_CLASSIFY_QUOTA_CHECKED));
            }
            startActivityForResult(intent, 100);
        } else if (type == Const.RECORD_TYPE_11) {
            startActivityForResult(new Intent(this, RecordActivity.class)
                    .putExtra(Const.RECORD_CLASSIFY_TYPE, type)
                    .putExtra(Const.RECORD_REQUEST_FLAG, true)
                    .putExtra(Const.RECORD_CLASSIFY_GENE_TRANSLATE_CHECKED, oldIntent.getSerializableExtra(Const.RECORD_CLASSIFY_GENE_TRANSLATE_CHECKED)), 100);
        } else if (type == Const.RECORD_TYPE_12) {
            startActivityForResult(new Intent(this, RecordActivity.class)
                    .putExtra(Const.RECORD_CLASSIFY_TYPE, type)
                    .putExtra(Const.RECORD_REQUEST_FLAG, true)
                    .putExtra(Const.RECORD_CLASSIFY_GENE_TRANSLATE_CHECKED, oldIntent.getSerializableExtra(Const.RECORD_CLASSIFY_GENE_TRANSLATE_CHECKED)), 100);
        } else {
            startActivityForResult(new Intent(this, RecordActivity.class)
                    .putExtra(Const.RECORD_CLASSIFY_TYPE, type)
                    .putExtra(Const.RECORD_REQUEST_FLAG, true), 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == Const.REQUEST_CODE_RECORD_ACTIVITY) {
                    Factory.postPhp(MedicalRecordDetailListActivity.this, Const.PApi_getUserInfoByTypeForApp);
                    setResult(Const.REQUEST_CODE_RECORD_ACTIVITY, data);
                }
                break;
            case REQUEST_CODE_MEDICAL_RECORD_ACTIVITY:
                Factory.postPhp(MedicalRecordDetailListActivity.this, Const.PApi_getUserInfoByTypeForApp);
                setResult(Const.REQUEST_CODE_MEDICAL_RECORD_ACTIVITY, data);
                break;
        }
    }
}

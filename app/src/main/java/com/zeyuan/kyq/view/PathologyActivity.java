package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.RecordBean;
import com.zeyuan.kyq.Entity.RecordItemEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.PathologyAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DensityUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/17.
 *
 * 病理报告（记录分类报告）
 *
 * @author wwei
 */
public class PathologyActivity extends BaseActivity implements HttpResponseInterface , PathologyAdapter.EditRecordItemListener{


    private PathologyAdapter adapter;
    private List<RecordItemEntity> list;
    private int Type;
    private static final int REQUEST_CODE_101 = 101;
    private boolean isChanged = false;
    private boolean Result_Flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathology);
        Type = getIntent().getIntExtra(Const.RECORD_CLASSIFY_TYPE,Const.RECORD_TYPE_8);
        Result_Flag = getIntent().getBooleanExtra(Const.RECORD_REQUEST_FLAG,false);
        initView();
        initData();
    }

    private ListView lv;
    private View v_empty;
    private void initView(){

        TextView title = (TextView)findViewById(R.id.tv_title);
        title.setText(Type==Const.RECORD_TYPE_7?"诊断报告":"病理报告");

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.tv_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(PathologyActivity.this, RecordActivity.class)
                        .putExtra(Const.RECORD_CLASSIFY_TYPE, Type)
                        .putExtra(Const.RECORD_REQUEST_FLAG, true), REQUEST_CODE_101);
            }
        });

        lv = (ListView)findViewById(R.id.lv);
        list = new ArrayList<>();
        adapter = new PathologyAdapter(this,getPhotoWidth(),list,Type,this);
        lv.setAdapter(adapter);

        v_empty = findViewById(R.id.v_no_data);
    }

    private void initData(){
        Factory.postPhp(this, Const.PGetPresentationOther);
    }

    private int getPhotoWidth(){
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int result = (width - DensityUtils.dpToPx(this,36+16))/3;//左右padding各18，space 8*2
        return result;
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String,String> map = new HashMap<>();
        if (tag == Const.PGetPresentationOther){
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
            map.put("Type",Type+"");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PGetPresentationOther){
            RecordBean bean = (RecordBean)response;
            if (Const.RESULT.equals(bean.getiResult())){
                list = bean.getData();
                if (list!=null&&list.size()>0){
                    adapter.update(list);
                    if (Request_Flag){
                        lv.setSelection(0);
                    }
                    v_empty.setVisibility(View.GONE);
                    lv.setVisibility(View.VISIBLE);
                } else {
                    lv.setVisibility(View.GONE);
                    v_empty.setVisibility(View.VISIBLE);
                }
            }else {
                showToast("请求失败");
            }
            if (Request_Flag) Request_Flag = false;
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
        if (Request_Flag) Request_Flag = false;
        showToast("网络请求错误");
    }

    private boolean Request_Flag = false;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case Const.REQUEST_CODE_RECORD_ACTIVITY:
                boolean change = data.getBooleanExtra("isChanged",false);
                if (change){
                    isChanged = true;
                    Request_Flag = true;
                    initData();
                }
                break;
        }
    }

    @Override
    public void toEditRecordItem(RecordItemEntity entity) {
        startActivityForResult(new Intent(PathologyActivity.this, EditRecordActivity.class)
                .putExtra(Const.RECORD_CLASSIFY_TYPE, Type)
                .putExtra(Const.RECORD_CLASSIFY_DATA, entity)
                .putExtra(Const.RECORD_REQUEST_FLAG, true), REQUEST_CODE_101);
    }

    @Override
    public void finish() {
        if (Result_Flag&&(isChanged||adapter.isChanged())){
            setResult(Const.RESULT_CODE_PATHOLOGY_TO_MEDICAL,getIntent()
                    .putExtra(Const.RESULT_FLAG_FOR_DATA_CHANGED,true));
        }
        super.finish();
    }
}

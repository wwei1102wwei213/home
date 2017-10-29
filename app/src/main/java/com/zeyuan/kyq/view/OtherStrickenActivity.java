package com.zeyuan.kyq.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.ChooseOtherStrickenAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.widget.MyListView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/18.
 *
 * 其他重疾
 *
 * @author wwei
 */
public class OtherStrickenActivity extends BaseActivity{

    private ArrayList<String> check;
    private String OtherRemark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_stricken);

        check = getIntent().getStringArrayListExtra(Const.PATIENT_OTHER_STRICKEN_CHECKED);
        if (check == null) check = new ArrayList<>();
        OtherRemark = getIntent().getStringExtra(Const.PATIENT_OTHER_STRICKEN_REMARK);
        if (OtherRemark==null) OtherRemark = "";
        initView();
        initData();

    }

    private EditText et;
    private ChooseOtherStrickenAdapter adapter;
    private void initView(){

        try {
            MyListView lv = (MyListView)findViewById(R.id.lv);
            adapter = new ChooseOtherStrickenAdapter(this,check);
            lv.setAdapter(adapter);
            et = (EditText)findViewById(R.id.et);
            if (!TextUtils.isEmpty(OtherRemark)) et.setText(OtherRemark);
            findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = 1;
                    finish();
                }
            });
            findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"initView");
        }

    }

    private void initData(){

    }

    private boolean isRemarkChanged(){
        String remark = et.getText().toString().trim();
        if (OtherRemark.equals(remark)) return false;
        return true;
    }

    private int flag = 0;
    @Override
    public void finish() {
        if (flag == 0){//直接退出
            if (isRemarkChanged()||adapter.isChange()){
                ZYDialog.Builder builder = new ZYDialog.Builder(this);
                builder.setTitle("提示")
                        .setMessage("不保存就退出吗？")
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                flag = 1;
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                flag = 2;
                                dialog.dismiss();
                                finish();
                            }
                        }).create().show();
            } else {
                super.finish();
            }
        } else if (flag==1){//选择保存时退出
            if (isRemarkChanged()||adapter.isChange()){
                setResult(Const.RESULT_CODE_OTHER_STRICKEN_TO_PATIENT, getIntent()
                        .putExtra(Const.RESULT_OTHER_STRICKEN_CHECKED, adapter.getCheck())
                        .putExtra(Const.RESULT_OTHER_STRICKEN_REMARK, et.getText().toString().trim()));
            }
            super.finish();
        }else {
            super.finish();
        }
    }
}

package com.zeyuan.kyq.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.EditInfoEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.bean.SimilarCaseBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.ChooseTimeInterface;
import com.zeyuan.kyq.fragment.ChooseTimeFragment;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CircleImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static anet.channel.util.Utils.context;

/**
 * Created by Administrator on 2017/8/24.
 * 选择确诊时间
 */

public class SelectDiagnosisTimeActivity extends BaseActivity implements View.OnClickListener, HttpResponseInterface {
    //    ZYDatePicker zydate_picker;
    TextView tv_similarity_num;
    //从之前页面传递过来的用户选择的数据
    EditInfoEntity editInfoEntity;
    TextView tv_diagnosis_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_diagnosis_time);
        editInfoEntity = (EditInfoEntity) getIntent().getSerializableExtra("editInfoEntity");
        if (editInfoEntity == null) {
            showToast("数据传输错误！");
        }
        initView();
    }

    List<SimilarCaseBean.DataEntity> similarList = new ArrayList<>();
    CircleImageView civ_similar_avatar;
    TextView tv_similar_name;
    TextView tv_cancer_name;
    TextView tv_similar_degree;
    TextView tv_similar_gene;
    TextView tv_change;
    Button btn_focus;

    private void initView() {
        View iv_back = findViewById(R.id.iv_back);
        View tv_next = findViewById(R.id.tv_next);
        findViewById(R.id.fl_case_start_date).setOnClickListener(this);
        tv_diagnosis_time = (TextView) findViewById(R.id.tv_diagnosis_time);
        // zydate_picker = (ZYDatePicker) findViewById(R.id.zydate_picker);
        tv_similarity_num = (TextView) findViewById(R.id.tv_similarity_num);
        civ_similar_avatar = (CircleImageView) findViewById(R.id.civ_similar_avatar);
        tv_similar_name = (TextView) findViewById(R.id.tv_similar_name);
        tv_cancer_name = (TextView) findViewById(R.id.tv_cancer_name);
        tv_similar_degree = (TextView) findViewById(R.id.tv_similar_degree);
        tv_similar_gene = (TextView) findViewById(R.id.tv_similar_gene);
        tv_change = (TextView) findViewById(R.id.tv_change);
        btn_focus = (Button) findViewById(R.id.btn_focus);
        iv_back.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        tv_change.setOnClickListener(this);
        Factory.postPhp(this, Const.PApi_getSimilarCase);
    }

    private ChooseTimeFragment timeFragment;

    private void showSelectTimeDialog() {
        if (timeFragment == null) {
            timeFragment = ChooseTimeFragment.getInstance(new ChooseTimeInterface() {
                @Override
                public void timeCallBack(String time) {
                    tv_diagnosis_time.setText(time);
                    tv_diagnosis_time.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_blue));
                    editInfoEntity.setDiscoverTime(DataUtils.showTimeMillsForTimeStr(time) + "");
                }
            });
        }
        if (timeFragment.getDialog() == null || !timeFragment.getDialog().isShowing())
            timeFragment.show(getFragmentManager(), ChooseTimeFragment.type);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.fl_case_start_date:
                showSelectTimeDialog();
                break;
            case R.id.tv_change:
                changeRecommend();
                break;
            case R.id.tv_next:
                if (TextUtils.isEmpty(editInfoEntity.getDiscoverTime())) {
                    showToast("请选择一个确诊时间");
                    showSelectTimeDialog();
                } else {
                    startActivityForResult(new Intent(this, SelectCancerStagingActivity.class).putExtra("editInfoEntity", editInfoEntity), 9);
                }
                break;
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(getApplicationContext()));
        if (tag == Const.PApi_getSimilarCase) {
            map.put("CancerID", editInfoEntity.getCancerID());
            map.put("BodyID", "");//转移情况
            map.put("StepID", "");
            map.put("GenID", "");
            map.put("page", "0");
            map.put("pageSize", "15");
        } else if (tag == Const.PCareDel || tag == Const.PCareAdd) {
            map.put("uid", UserinfoData.getInfoID(context));
            map.put("careuid", careuid);
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        try {
            if (flag == Const.PApi_getSimilarCase) {
                SimilarCaseBean scb = (SimilarCaseBean) response;

                setSimilarityNum(scb.getCountNum() + "");

                similarList.addAll(scb.getData());
                changeRecommend();
            } else if (flag == Const.PCareAdd || flag == Const.PCareDel) {
                btn_focus.setEnabled(true);
                tv_change.setEnabled(true);
                PhpUserInfoBean bean = (PhpUserInfoBean) response;
                if (Const.RESULT.equals(bean.getiResult())) {
                    if (flag == Const.PCareAdd) {
                        btn_focus.setText("已关注");
                        btn_focus.setSelected(true);
                        showToast("关注成功");
                        similarList.get(currentRecommend).setIs_care("1");
                    } else {
                        showToast("取消关注成功");
                        btn_focus.setText("关注");
                        btn_focus.setSelected(false);
                        similarList.get(currentRecommend).setIs_care("0");
                    }
                } else {
                    if (flag == Const.PCareAdd) {
                        showToast("关注失败");

                    } else {
                        showToast("取消关注失败");

                    }
                }
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "");
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

    }

    private void setSimilarityNum(String num) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append("您有");
        ssb.append(num);
        ssb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.text_blue)), 2, 2 + num.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append("位");
        ssb.append(((Map<String, String>) Factory.getData(Const.N_DataCancerValues)).get(editInfoEntity.getCancerID()));
        ssb.append("战友哦");
        tv_similarity_num.setText(ssb);
    }

    int currentRecommend = -1;
    private String careuid;

    private void changeRecommend() {
        if (similarList != null && similarList.size() > 0) {
            try {
                currentRecommend++;
                if (currentRecommend >= similarList.size())
                    currentRecommend = 0;
                final SimilarCaseBean.DataEntity dataEntity = similarList.get(currentRecommend);
                Glide.with(this).load(dataEntity.getHeaderUrl()).into(civ_similar_avatar);
                tv_similar_name.setText(dataEntity.getInfoName());
                tv_cancer_name.setText(((Map<String, String>) Factory.getData(Const.N_DataCancerValues)).get(dataEntity.getCancerID()));
                tv_similar_degree.setText("匹配度" + dataEntity.getPercentage() + "%");
                tv_similar_gene.setText(dataEntity.getTransferGene());
                if (dataEntity.getIs_care().equals("0")) {
                    btn_focus.setSelected(false);
                    btn_focus.setText("关注");
                } else {
                    btn_focus.setSelected(true);
                    btn_focus.setText("已关注");
                }
                btn_focus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        careuid = dataEntity.getInfoID();

                        if (dataEntity.getIs_care().equals("0")) {
                            Factory.postPhp(SelectDiagnosisTimeActivity.this, Const.PCareAdd);
                        } else {
                            cancelFollow();
                        }
                        btn_focus.setEnabled(false);
                        tv_change.setEnabled(false);
                    }
                });

            } catch (Exception e) {
                LogCustom.e("ZYE", e.toString());
            }

        }
    }

    ZYDialog dialog;

    private void cancelFollow() {
        dialog = new ZYDialog.Builder(this).setTitle("提示")
                .setMessage("确定不再关注此人?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        btn_focus.setEnabled(true);
                        tv_change.setEnabled(true);
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Factory.postPhp(SelectDiagnosisTimeActivity.this, Const.PCareDel);
                        dialog.dismiss();
                    }
                })
                .setCancelAble(true)
                .create();
        dialog.show();
    }

//    private String getSelectedTimeStamp() {
//        String tempMonth = zydate_picker.getMonth();
//        tempMonth = Integer.valueOf(tempMonth) < 10 ? "0" + tempMonth : tempMonth;
//        String tempDay = zydate_picker.getDay();
//        tempDay = Integer.valueOf(tempDay) < 10 ? "0" + tempDay : tempDay;
//        String chooseTime = zydate_picker.getYear() + "-" + tempMonth + "-" + tempDay;
//        try {
//            //php后台所需的时间戳精确到秒
//            String selectStamp = DataUtils.showTimeToLoadTime(chooseTime);
//            //java时间戳精确到毫秒，需要再加上三位
//            long temp = Long.parseLong(selectStamp + "000");
//            if (temp > System.currentTimeMillis()) {
//                return "";
//            } else {
//                if (!TextUtils.isEmpty(chooseTime)) {
//                    return selectStamp;
//                }
//
//            }
//        } catch (Exception e) {
//
//            ExceptionUtils.ExceptionSend(e, "Choose Time Error");
//        }
//        return "";
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 9 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }


}

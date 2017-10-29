package com.zeyuan.kyq.view;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.RecommendUserInfoEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.GuideGvAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.bean.RecommendUserBean;
import com.zeyuan.kyq.bean.SimilarCaseBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/21.
 * 批量关注好友提示
 */

public class FocusFriendGuideActivity extends BaseActivity implements View.OnClickListener, HttpResponseInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_guide_selector);
        initView_grid();
        getData();
    }

    TextView tv_num_hint;
    TextView tv_change;
    Button btn_focus_selected;
    ImageView iv_close;
    GridView gv_user;
    GuideGvAdapter guideGvAdapter;
    List<RecommendUserInfoEntity> userInfoEntityList;

    //初始化关注好友的引导view
    private void initView_grid() {
        tv_num_hint = (TextView) findViewById(R.id.tv_num_hint);
        tv_change = (TextView) findViewById(R.id.tv_change);
        btn_focus_selected = (Button) findViewById(R.id.btn_focus_selected);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        gv_user = (GridView) findViewById(R.id.gv_user);
        userInfoEntityList = new ArrayList<>();
        guideGvAdapter = new GuideGvAdapter(this, userInfoEntityList);
        gv_user.setAdapter(guideGvAdapter);
        tv_change.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        btn_focus_selected.setOnClickListener(this);

    }

    String Type = "1";

    private void getData() {
        int userType = getIntent().getIntExtra("userType", 1);
        switch (userType) {
            case 1://相同癌种的用户
                Factory.postPhp(this, Const.PApi_getSimilarCase);
                Factory.postPhp(this, Const.PApi_getRecommendUser);
                Type = "1";
                break;
            case 2://同城的用户
                Type = "2";
                tv_num_hint.setText("这些活泼可爱的圈友们和您在一个地方哦");
                Factory.postPhp(this, Const.PApi_getRecommendUser);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.tv_change:
                Factory.postPhp(this, Const.PApi_getRecommendUser);
                break;
            case R.id.btn_focus_selected:
                focusUser();
                break;
        }
    }


    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(getApplicationContext()));
        if (tag == Const.PApi_getRecommendUser) {
            map.put("Type", Type);
            map.put("CancerID", UserinfoData.getCancerID(getApplicationContext()));
            map.put("City", UserinfoData.getCityID(getApplicationContext()));
        } else if (tag == Const.PApi_getSimilarCase) {
            map.put("CancerID", UserinfoData.getCancerID(getApplicationContext()));
            map.put("BodyID", "");//转移情况
            map.put("StepID", "");
            map.put("GenID", "");
            map.put("page", "0");
            map.put("pageSize", "1");
        } else if (tag == Const.PApi_EditInfo) {
            map.put("Type", "0");//Type!=1  时只有 careUid 参数 和 InfoID有效
            map.put("careUid", careUid);
        }


        return map;
    }

    //选中的用户
    String careUid;

    private void focusUser() {
        careUid = "";
        for (RecommendUserInfoEntity userInfoEntity : userInfoEntityList) {
            if (userInfoEntity.isSelected()) {
                careUid += userInfoEntity.getInfoID();
                careUid += ",";
            }
        }
        if (TextUtils.isEmpty(careUid)) {
            showToast("请至少选择一名用户");
            return;
        } else {
            careUid = careUid.substring(0, careUid.length() - 1);
        }
        Factory.postPhp(this, Const.PApi_EditInfo);
        btn_focus_selected.setEnabled(false);
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PApi_getRecommendUser) {
            RecommendUserBean bean = (RecommendUserBean) response;
            if (bean.getiResult().equals(Const.RESULT)) {
                userInfoEntityList.clear();
                userInfoEntityList.addAll(bean.getData());
                guideGvAdapter.notifyDataSetChanged();
            }
        } else if (flag == Const.PApi_getSimilarCase) {
            SimilarCaseBean scb = (SimilarCaseBean) response;
            setSimilarityNum(scb.getCountNum() + "");
        } else if (flag == Const.PApi_EditInfo) {
            btn_focus_selected.setEnabled(true);
            PhpUserInfoBean userInfoBean = (PhpUserInfoBean) response;
            if (userInfoBean.getiResult().equals(Const.RESULT)) {
                showToast("关注成功！");
                setResult(RESULT_OK);
                finish();
            } else {
                showToast("关注失败！");
            }
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
        ssb.append("您现在可以与");
        ssb.append(num);
        ssb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.text_blue)), 6, 6 + num.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append("位");
        ssb.append(((Map<String, String>) Factory.getData(Const.N_DataCancerValues)).get(UserinfoData.getCancerID(getApplicationContext())));
        ssb.append("患者/关注者交流啦");
        tv_num_hint.setText(ssb);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_alpha_out);
    }
}

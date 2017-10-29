package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.zeyuan.kyq.Entity.QuotaDataEntity;
import com.zeyuan.kyq.Entity.QuotaItemChildEntity;
import com.zeyuan.kyq.Entity.QuotaItemEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.SearchQuotaLeftAdapter;
import com.zeyuan.kyq.adapter.SearchQuotaRightAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.CommBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.FragmentCallBack;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CustomScrollView;
import com.zeyuan.kyq.widget.MyListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/27.
 * <p>
 * 查指标页面
 *
 * @author wwei
 */
public class SearchQuotaActivity extends BaseActivity implements HttpResponseInterface, AdapterView.OnItemClickListener
        , FragmentCallBack, View.OnClickListener {

    private SearchQuotaLeftAdapter leftAdapter;
    private SearchQuotaRightAdapter rightAdapter;
    private List<QuotaItemEntity> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_quota);
        try {
            initTitle();
            initStepHint();
            initView();
            initData();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "OnCreate SearchQuota");
        }

    }

    private void initTitle() {
        initWhiteTitle("查指标", 0);
    }

    //老版本开启个性化治疗窗口
    //private InfoStepFragment fragment;

    private void initStepHint() {
        try {
            if (Const.RESULT.equals(UserinfoData.getIsHaveStep(this))) {
                findViewById(R.id.layout_step_hint).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_show_info_step).setOnClickListener(this);
            } else {
                findViewById(R.id.layout_step_hint).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "initStepHint SearchQuota");
        }

    }

    private void initData() {
        Factory.post(this, Const.EGetSearchToSpecification);
    }

    private CustomScrollView sv;

    private void initView() {
        try {
            sv = (CustomScrollView) findViewById(R.id.rsv);
            ImageView iv = (ImageView) findViewById(R.id.iv_white_title_share);
            iv.setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "initView SearchQuota");
        }
    }

    private void initLeftView(List<QuotaItemEntity> data) {

        try {
            int[] checkRes = new int[]{
                    R.mipmap.queta_blood, R.mipmap.queta_niao, R.mipmap.queta_fenbian, R.mipmap.queta_gan,
                    R.mipmap.quota_normal
            };
            int[] unCheckRes = new int[]{
                    R.mipmap.queta_blood_no, R.mipmap.queta_niao_no, R.mipmap.queta_fenbian_no, R.mipmap.queta_gan_no,
                    R.mipmap.quota_normal_no
            };

            MyListView lv = (MyListView) findViewById(R.id.lv_search_quota_left);
            leftAdapter = new SearchQuotaLeftAdapter(this, data, checkRes, unCheckRes, 0);
            lv.setAdapter(leftAdapter);
            lv.setOnItemClickListener(this);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "initLeftView SearchQuota");
        }


    }

    private MyListView lv;

    private void initRightView(List<QuotaItemEntity> data) {

        try {
            lv = (MyListView) findViewById(R.id.lv_search_quota_right);
            List<QuotaItemChildEntity> rlist = list.get(0).getData();
            if (list == null) {
                rlist = new ArrayList<>();
            }
            rightAdapter = new SearchQuotaRightAdapter(this, rlist);
            lv.setAdapter(rightAdapter);
            lv.setOnItemClickListener(this);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "initRightView SearchQuota");
        }

    }

    @Override
    public Map getParamInfo(int tag) {
        return null;
    }

    private String[] shareParems;
    private String PolicyID;

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if (flag == Const.EGetSearchToSpecification) {
            if (Const.NO_STEP.equals(UserinfoData.getIsHaveStep(this))) {
                args = new String[]{
                        Contants.InfoID, UserinfoData.getInfoID(this),
                        Contants.CancerID, UserinfoData.getCancerID(this),
                        Const.ISHAVESTEP, "0"
                };
            } else {
                String CureConfID = MapDataUtils.getAllCureconfID(UserinfoData.getStepID(this));
                args = new String[]{
                        Contants.InfoID, UserinfoData.getInfoID(this),
                        Contants.StepID, UserinfoData.getStepID(this),
                        Contants.CancerID, UserinfoData.getCancerID(this),
                        Contants.CureConfID, CureConfID,
                        Const.ISHAVESTEP, "1"
                };
            }

        } else if (flag == Const.EGetCommDetail) {
            args = new String[]{
                    Contants.InfoID, UserinfoData.getInfoID(this),
                    "SpID", PolicyID,
                    "Type", "4"
            };
            shareParems = args;
        }
        return HttpSecretUtils.getParamString(args);
    }


    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.EGetSearchToSpecification) {
            QuotaDataEntity data = (QuotaDataEntity) response;
            if (Const.RESULT.equals(data.getiResult())) {
                list = data.getData();
                if (list != null && list.size() > 0) {
                    Collections.sort(list, new Comparator<QuotaItemEntity>() {
                        @Override
                        public int compare(QuotaItemEntity lhs, QuotaItemEntity rhs) {
                            return Integer.valueOf(lhs.getId()) - Integer.valueOf(rhs.getId());
                        }
                    });
                    initLeftView(list);
                    initRightView(list);
                }
            }
        } else if (flag == Const.EGetCommDetail) {
            CommBean data = (CommBean) response;
            if (data != null && Const.RESULT.equals(data.getIResult())) {
                startActivity(new Intent(this, ResultDetailActivity.class)
                        .putExtra(Contants.CommBean, data)
                        .putExtra(Const.RESULT_PARAMS_FOR_SHARE, shareParems)
                        .putExtra(Const.SET_QUOTA_TYPE, 4));
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.lv_search_quota_left) {
            if (position != leftAdapter.getIndex()) {
                leftAdapter.updata(position);
                QuotaItemEntity entity = leftAdapter.getItem(position);
                if (entity != null) {
                    List<QuotaItemChildEntity> list = entity.getData();
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    rightAdapter.updata(list);
                    sv.scrollTo(0, 0);
                }
            }
        } else if (parent.getId() == R.id.lv_search_quota_right) {
            QuotaItemChildEntity entity = rightAdapter.getItem(position);
            if (entity != null && !TextUtils.isEmpty(entity.getSpID())) {
                PolicyID = entity.getSpID();
                Factory.post(this, Const.EGetCommDetail);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_white_title_share:
                List<QuotaItemChildEntity> temp = new ArrayList<>();
                try {
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i) != null) {
                                temp.addAll(list.get(i).getData());
                            }
                        }
                        startActivity(new Intent(this, SearchFunctionActivity.class)
                                .putExtra(Const.SEARCH_TYPE, Const.SEARCH_QUOTA)
                                .putExtra(Const.RECORD_QUOTA, (Serializable) temp));
                    }
                } catch (Exception e) {

                }
                break;
            case R.id.tv_show_info_step:
                try {
//                    if (fragment == null) {
//                        fragment = InfoStepFragment.getInstance(this, 0);
//                    }
//                    fragment.show(getSupportFragmentManager(), InfoStepFragment.type);
                    UiUtils.startIndividuationTreatment(SearchQuotaActivity.this);
                } catch (Exception e) {

                }
                break;
        }
    }

    @Override
    public void dataCallBack(String str, int flag, String tag, Object obj) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==9&&resultCode==RESULT_OK){
            initStepHint();
            initData();
        }
    }
}

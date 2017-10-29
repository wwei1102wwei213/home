package com.zeyuan.kyq.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.Entity.OtherEffectEntity;
import com.zeyuan.kyq.Entity.SearchPolicyEntity;
import com.zeyuan.kyq.Entity.StepItemEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.SearchPolocyAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.CaseDetailBean;
import com.zeyuan.kyq.bean.CommBean;
import com.zeyuan.kyq.bean.PolicyDataEntity;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.FragmentCallBack;
import com.zeyuan.kyq.biz.forcallback.SearchPolityInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.MyListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2016/6/14.
 * <p>
 * 查并发症/副作用页面
 *
 * @author wwei
 */
public class SearchComplicationActivity extends BaseActivity implements HttpResponseInterface, SearchPolityInterface,
        FragmentCallBack {

    private int type;
    private int tempType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_effect);

        //获取启动type
        tempType = getIntent().getIntExtra(Const.SEARCH_POLICY_TYPE, 8);
        //转换启动type，8为副作用==>1,11为并发症==>2
        if (tempType == 8) {
            type = 1;
        } else {
            type = 2;
        }
        //设置标题
        initTitle();
        //设置阶段提示
        initStepHint();
        //设置视图控件
        initView();
        //设置数据
        initData();
    }

    private List<PolicyDataEntity> addList;

    private void initTitle() {
        if (type == 1) {
            initWhiteTitle("查副作用");
        } else {
            initWhiteTitle("查并发症");
        }
    }

    private void initData() {
        Factory.post(this, Const.EGetSearchToPolicy);
    }

    private void initView() {

        if (lv_step == null) {
            lv_step = (MyListView) findViewById(R.id.lv_step_policy);
        }
        List<StepItemEntity> list = new ArrayList<>();
        if (stepAdapter == null) {
            stepAdapter = new SearchPolocyAdapter(this, list, type);
        }
        lv_step.setAdapter(stepAdapter);
    }
    //老版本开启个性化治疗的初始页
    // private InfoStepFragment fragment;


    private void initStepHint() {
        if (Const.NO_STEP.equals(UserinfoData.getIsHaveStep(this))) {
            findViewById(R.id.layout_step_hint).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_show_info_step).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (fragment == null) {
//                        fragment = InfoStepFragment.getInstance(SearchComplicationActivity.this, 0);
//                    }
//                    fragment.show(getSupportFragmentManager(), InfoStepFragment.type);
                    //新版本的开启个性化治疗的初始页
                    UiUtils.startIndividuationTreatment(SearchComplicationActivity.this);
                }
            });
        } else {
            findViewById(R.id.layout_step_hint).setVisibility(View.GONE);
        }
    }

    private MyListView lv_step;
    private SearchPolocyAdapter stepAdapter;

    private TextView tv_name_cureconf;
    private LinearLayout lv_cureconf;
    private TextView tv_name_cancer;
    private LinearLayout lv_cancer;
    private String ccname = "";

    /***
     *
     * 根据数据刷新视图
     *
     * @param data 数据
     */
    private void setViews(SearchPolicyEntity data) {

        if (Const.NO_STEP.equals(UserinfoData.getIsHaveStep(this))) {

        } else {
            //设置阶段相关
            try {
                List<StepItemEntity> list = data.getStepEffect();
                if (list != null && list.size() != 0) {
                    lv_step.setVisibility(View.VISIBLE);
                    stepAdapter.updata(list);
                } else {
                    lv_step.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                Log.i("ZYS", "setViews出错");
            }

            //设置药物类型相关
            if (tv_name_cureconf == null) {
                tv_name_cureconf = (TextView) findViewById(R.id.tv_name_cureconf_policy);
            }
            if (lv_cureconf == null) {
                lv_cureconf = (LinearLayout) findViewById(R.id.lv_cureconf_policy);
            }
            try {
                final SearchPolicyEntity.CureConfEffectEntity ccEntity = data.getCureConfEffect();
                if (ccEntity != null) {
                    String name;
                    final List<PolicyDataEntity> cureList = ccEntity.getData();
                    lv_cureconf.removeAllViews();
                    if (cureList != null && cureList.size() != 0) {
                        tv_name_cureconf.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(ccEntity.getCureConfName())) {
                            if (type == 1) {
                                tv_name_cureconf.setText("与" + ccEntity.getCureConfName() + "相关的副作用");
                            } else {
                                tv_name_cureconf.setText("与" + ccEntity.getCureConfName() + "相关的并发症");
                            }

                        } else {
                            if (type == 1) {
                                tv_name_cureconf.setText("与 相关的副作用");
                            } else {
                                tv_name_cureconf.setText("与 相关的并发症");
                            }
                        }
                        if (cureList.size() > 4) {
                            for (int i = 0; i < 4; i++) {

                                View v = View.inflate(this, R.layout.item_child_policy, null);
                                TextView tv = (TextView) v.findViewById(R.id.tv_child_policy);
                                final PolicyDataEntity pEntity = cureList.get(i);
                                name = pEntity.getPolicyName();
                                if (!TextUtils.isEmpty(name)) {
                                    tv.setText(name);
                                } else {
                                    tv.setText("");
                                }
                                if (i % 2 == 0) {
                                    tv.setBackgroundColor(Color.parseColor("#ffffff"));
                                } else {
                                    tv.setBackgroundColor(Color.parseColor("#F5F5F5"));
                                }
                                tv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        toResultDetail(null, pEntity, type, 0);
                                    }
                                });
                                lv_cureconf.addView(v);

                            }
                            View v = View.inflate(this, R.layout.layout_look_more, null);
                            TextView tv = (TextView) v.findViewById(R.id.tv_child_look_more);
                            tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    toLookMore(cureList, ccEntity.getCureConfName(), type);
                                }
                            });
                            lv_cureconf.addView(v);
                        } else {
                            for (int i = 0; i < cureList.size(); i++) {
                                View v = View.inflate(this, R.layout.item_child_policy, null);
                                TextView tv = (TextView) v.findViewById(R.id.tv_child_policy);
                                final PolicyDataEntity pEntity = cureList.get(i);
                                name = pEntity.getPolicyName();
                                if (!TextUtils.isEmpty(name)) {
                                    tv.setText(name);
                                } else {
                                    tv.setText("");
                                }
                                if (i % 2 == 0) {
                                    tv.setBackgroundColor(Color.parseColor("#ffffff"));
                                } else {
                                    tv.setBackgroundColor(Color.parseColor("#F5F5F5"));
                                }
                                tv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        toResultDetail(null, pEntity, type, 0);
                                    }
                                });
                                lv_cureconf.addView(v);
                            }
                        }
                    } else {
                        tv_name_cureconf.setVisibility(View.GONE);
                        lv_cureconf.setVisibility(View.GONE);
                    }
                } else {
                    tv_name_cureconf.setVisibility(View.GONE);
                    lv_cureconf.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                Log.i("ZYS", "setViews出错");
            }

        }
        //设置癌种类型相关
        tv_name_cancer = (TextView) findViewById(R.id.tv_name_cancer_policy);
        lv_cancer = (LinearLayout) findViewById(R.id.ll_cancer_policy);
        try {
            final SearchPolicyEntity.CancerEffectEntity ccEntity = data.getCancerEffect();
            OtherEffectEntity otherentity = data.getOtherEffect();

            if ((ccEntity == null || ccEntity.getData() == null || ccEntity.getData().size() == 0) &&
                    (otherentity == null || otherentity.getData() == null || otherentity.getData().size() == 0)) {
                tv_name_cancer.setVisibility(View.GONE);
                lv_cancer.setVisibility(View.GONE);
            } else {

                if (ccEntity != null && !TextUtils.isEmpty(ccEntity.getCancerName())) {
                    ccname = ccEntity.getCancerName();
                    if (type == 1) {
                        tv_name_cancer.setText("与" + ccEntity.getCancerName() + "相关的副作用");
                    } else {
                        tv_name_cancer.setText("与" + ccEntity.getCancerName() + "相关的并发症");
                    }
                } else {
                    String cancerName = "";
                    if (UserinfoData.getCancerID(this) != null) {
                        cancerName = MapDataUtils.getCancerValues(UserinfoData.getCancerID(this));
                    }
                    ccname = cancerName;
                    if (!TextUtils.isEmpty(cancerName)) {

                        if (type == 1) {
                            tv_name_cancer.setText("与" + cancerName + "相关的副作用");
                        } else {
                            tv_name_cancer.setText("与" + cancerName + "相关的并发症");
                        }
                    }
                }
                String name;
                List<PolicyDataEntity> tempList;
                if (ccEntity == null) {
                    tempList = new ArrayList<>();
                } else {
                    tempList = ccEntity.getData();
                }
                if (tempList == null || tempList.size() == 0) {
                    addList = otherentity.getData();
                } else {
                    if (otherentity.getData() == null || otherentity.getData().size() == 0) {
                        addList = tempList;
                    } else {
                        tempList.addAll(otherentity.getData());
                        addList = tempList;
                    }
                }
                lv_cancer.removeAllViews();
                if (addList != null && addList.size() != 0) {
                    tv_name_cancer.setVisibility(View.VISIBLE);
                    if (addList.size() > 4) {
                        for (int i = 0; i < 4; i++) {

                            View v = View.inflate(this, R.layout.item_child_policy, null);
                            TextView tv = (TextView) v.findViewById(R.id.tv_child_policy);
                            final PolicyDataEntity pEntity = addList.get(i);
                            name = pEntity.getPolicyName();
                            if (!TextUtils.isEmpty(name)) {
                                tv.setText(name);
                            } else {
                                tv.setText("");
                            }
                            if (i % 2 == 0) {
                                tv.setBackgroundColor(Color.parseColor("#ffffff"));
                            } else {
                                tv.setBackgroundColor(Color.parseColor("#F5F5F5"));
                            }
                            tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    toResultDetail(null, pEntity, type, 0);
                                }
                            });
                            lv_cancer.addView(v);

                        }
                        View v = View.inflate(this, R.layout.layout_look_more, null);
                        TextView tv = (TextView) v.findViewById(R.id.tv_child_look_more);
                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toLookMore(addList, ccname, type);
                            }
                        });
                        lv_cancer.addView(v);
                    } else {
                        for (int i = 0; i < addList.size(); i++) {
                            View v = View.inflate(this, R.layout.item_child_policy, null);
                            TextView tv = (TextView) v.findViewById(R.id.tv_child_policy);
                            final PolicyDataEntity pEntity = addList.get(i);
                            name = pEntity.getPolicyName();
                            if (!TextUtils.isEmpty(name)) {
                                tv.setText(name);
                            } else {
                                tv.setText("");
                            }
                            if (i % 2 == 0) {
                                tv.setBackgroundColor(Color.parseColor("#ffffff"));
                            } else {
                                tv.setBackgroundColor(Color.parseColor("#F5F5F5"));
                            }
                            tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    toResultDetail(null, pEntity, type, 0);
                                }
                            });
                            lv_cancer.addView(v);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.i("ZYS", "setViews出错");
        }
    }


    @Override
    public Map getParamInfo(int tag) {

        return null;
    }

    private String PolicyID;
    private String[] shareParems;
    private String StepID;

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;

        if (flag == Const.EGetSearchToPolicy) {
            if (Const.NO_STEP.equals(UserinfoData.getIsHaveStep(this))) {
                args = new String[]{
                        Contants.InfoID, UserinfoData.getInfoID(this),
                        Contants.CancerID, UserinfoData.getCancerID(this),
                        "Type", tempType + "",
                        Const.ISHAVESTEP, "0"
                };
            } else {
                args = new String[]{
                        Contants.InfoID, UserinfoData.getInfoID(this),
                        Contants.StepID, UserinfoData.getStepID(this),
                        Contants.CancerID, UserinfoData.getCancerID(this),
                        Contants.CureConfID, MapDataUtils.getAllCureconfID(UserinfoData.getStepID(this)),
                        "Type", tempType + "",
                        Const.ISHAVESTEP, "1"
                };
            }

        } else if (flag == Const.EGetCommDetail) {
            args = new String[]{
                    Contants.InfoID, UserinfoData.getInfoID(this),
                    Contants.CommPolicyID, PolicyID,
                    "Type", "3"
            };
            shareParems = args;
        } else if (flag == Const.EGetSolutionDetail) {
            if (Const.NO_STEP.equals(UserinfoData.getIsHaveStep(this))) {
                args = new String[]{
                        Contants.InfoID, UserinfoData.getInfoID(this),
                        Contants.PolicyType, type + "",
                        Const.ISNEWVERSION, Const.ISNEWVERSION_FINAL,
                        Const.ISHAVESTEP, "0"
                };
            } else {
                args = new String[]{
                        Contants.InfoID, UserinfoData.getInfoID(this),
                        Contants.StepID, StepID,
                        Contants.PolicyType, type + "",
                        Const.ISNEWVERSION, Const.ISNEWVERSION_FINAL,
                        Const.ISHAVESTEP, "1"
                };
            }
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object response, int flag) {

        if (flag == Const.EGetSearchToPolicy) {
            SearchPolicyEntity data = (SearchPolicyEntity) response;
            if (data != null && Const.RESULT.equals(data.getiResult())) {
                LogCustom.i("ZYS", "DATA:" + data.toString());
                setViews(data);
                findViewById(R.id.v_bottom_empty).setVisibility(View.VISIBLE);
            }
        } else if (flag == Const.EGetCommDetail) {
            CommBean data = (CommBean) response;
            if (data != null && Const.RESULT.equals(data.getIResult())) {
                LogCustom.i("ZYS", "DATA:" + data.toString());
                startActivity(new Intent(this, ResultDetailActivity.class)
                        .putExtra(Contants.CommBean, data)
                        .putExtra(Const.RESULT_PARAMS_FOR_SHARE, shareParems));
            }
        } else if (flag == Const.EGetSolutionDetail) {
            CaseDetailBean bean = (CaseDetailBean) response;
            if (bean != null && Contants.RESULT.equals(bean.getIResult())) {
                bean.setStepId(StepID);
                startActivity(new Intent(this, CaseDetailActivity.class).putExtra(Contants.CaseDetailBean, bean).putExtra(Contants.ISCANCER, false));
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
    public void toDrugDetail(StepItemEntity entity, int type) {

        if (entity == null || TextUtils.isEmpty(entity.getStepID())) {
            Toast.makeText(this, "数据加载失败", Toast.LENGTH_SHORT).show();
            return;
        }
        StepID = entity.getStepID();
        Factory.post(this, Const.EGetSolutionDetail);
    }

    @Override
    public void toLookMore(List<PolicyDataEntity> entities, String name, int type) {
        LogCustom.i("ZYS", "toLookMore:" + type);
        if (entities != null && entities.size() != 0) {
            startActivity(new Intent(this, LookMoreActivity.class)
                    .putExtra(LookMoreActivity.TYPE_POLICY, type)
                    .putExtra(LookMoreActivity.SHOW_NAME, name)
                    .putExtra(LookMoreActivity.SHOW_DATA, (Serializable) entities));
        }
    }

    @Override
    public void toResultDetail(Object entity, PolicyDataEntity pEntity, int type, int flag) {
        if (pEntity == null) return;
        if (!TextUtils.isEmpty(pEntity.getPolicyID())) {
            PolicyID = pEntity.getPolicyID();
            Factory.post(this, Const.EGetCommDetail);
        }
    }

    @Override
    public void dataCallBack(String str, int flag, String tag, Object obj) {
        //旧版填写完阶段后刷新数据和界面
        //initStepHint();
        //initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 9) {
            //新版填写完阶段后刷新数据和界面
            initStepHint();
            initData();
        }
    }
}

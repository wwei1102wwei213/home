package com.zeyuan.kyq.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.SimilarCaseRecAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.bean.SimilarCaseBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.AdapterCallback;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.RippleBackground;
import com.zeyuan.kyq.widget.selector.SimilarCaseSelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimilarActivity extends BaseActivity implements HttpResponseInterface, View.OnClickListener, SimilarCaseSelector.OnSelectorItemSelectedListener {
    FrameLayout fl_loading;
    TextView tv_user_name;
    //    TextView tv_search_tip;
    FrameLayout fl_tips_or_selector;
    TextView tv_search_result_tips;
    SimilarCaseSelector similarCaseSelector;
    RippleBackground rp_content;
    // DiffuseView dv_search;
    Context context;
    List<SimilarCaseBean.DataEntity> similars = new ArrayList<>();
    boolean isDataLoadComplected = false;
    boolean isShowed_5s = false;
    boolean refresh;
    boolean loading;
    XRefreshView xrv_similar_case;
    RecyclerView rcv_similar_case;
    LinearLayoutManager layoutManager;
    SimilarCaseRecAdapter similarCaseAdapter;
    //搜索相似患者所需的参数
    String cancerId = UserinfoData.getCancerID(context), bodyId = "", stepId = UserinfoData.getStepID(context), genId = "";
    int page = 0;
    String careuid;//点击 +关注/取消关注 的用户id
    int carePos;//点击 +关注/取消关注 的item位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        //设置状态栏
//        setStatusBarTranslucent();
        setContentView(R.layout.activity_similar);
//        initStatusBar();
        initView();
        startLoadingTimer();
        initData();
    }

    private void initView() {
        //设置返回键
        findViewById(R.id.ll_back).setOnClickListener(this);
        TextView tv_other_title = (TextView) findViewById(R.id.tv_other_title);
        tv_other_title.setText("相似案例");
        fl_loading = (FrameLayout) findViewById(R.id.fl_loading);
        rp_content = (RippleBackground) findViewById(R.id.rp_content);
        //dv_search = (DiffuseView) findViewById(R.id.dv_search);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
//        tv_search_tip = (TextView) findViewById(R.id.tv_search_tip);

        fl_tips_or_selector = (FrameLayout) findViewById(R.id.fl_tips_or_selector);
        tv_search_result_tips = (TextView) findViewById(R.id.tv_search_result_tips);
        similarCaseSelector = new SimilarCaseSelector(this, fl_tips_or_selector);
        similarCaseSelector.setOnSelectorItemSelectedListener(this);
        xrv_similar_case = (XRefreshView) findViewById(R.id.xrv_similar_case);
        rcv_similar_case = (RecyclerView) findViewById(R.id.rcv_similar_case);
        similarCaseAdapter = new SimilarCaseRecAdapter(context, similars, new AdapterCallback() {
            @Override
            public void forAdapterCallback(int pos, int tag, String id, boolean flag, Object obj) {
                careuid = id;
                carePos = pos;
                if (flag) {
                    Factory.postPhp(SimilarActivity.this, Const.PCareAdd);
                } else {
                    cancelFollow();
                }
            }
        });
        layoutManager = new LinearLayoutManager(context);
        rcv_similar_case.setHasFixedSize(true);
        rcv_similar_case.setLayoutManager(layoutManager);
        rcv_similar_case.setAdapter(similarCaseAdapter);
        similarCaseAdapter.setCustomLoadMoreView(new XRefreshViewFooter(context));
        xrv_similar_case.setPinnedTime(1000);
        xrv_similar_case.setPullLoadEnable(true);
        xrv_similar_case.setMoveForHorizontal(true);
        xrv_similar_case.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh = true;
                        page = 0;
                        initData();
                        Factory.postPhp(SimilarActivity.this, Const.PApi_getSimilarCase);
                    }
                }, 500);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        loading = true;
                        page++;
                        Factory.postPhp(SimilarActivity.this, Const.PApi_getSimilarCase);

                    }
                }, 500);
            }
        });
        xrv_similar_case.setStateCallback(new XRefreshView.XRefreshViewScrollStateChangedCallback() {
            @Override
            public void XRVScrollStateChangedCallback(int scrollState) {
                if (scrollState == 2) {
                    Glide.with(context).pauseRequests();
                } else {
                    Glide.with(context).resumeRequests();
                }
            }
        });
        Glide.with(context).load(UserinfoData.getAvatarUrl(this)).signature(new IntegerVersionSignature(1))
                .error(R.mipmap.default_avatar)
                .into((ImageView) findViewById(R.id.civ_my_head));
        Factory.postPhp(this, Const.PApi_getSimilarCase);
    }

    private void initData() {
        tv_user_name.setText(UserinfoData.getInfoname(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }

    @Override
    public void onSelectorItemSelected(String value, String name, int type) {
        switch (type) {
            case SimilarCaseSelector.TYPE_CANCER:
                cancerId = value;
                page = 0;
                isNeedShowSearchResult = true;
                Factory.postPhp(this, Const.PApi_getSimilarCase);
                xrv_similar_case.setLoadComplete(false);
                break;
            case SimilarCaseSelector.TYPE_TRANSFER:
                bodyId = value;
                page = 0;
                isNeedShowSearchResult = true;
                Factory.postPhp(this, Const.PApi_getSimilarCase);
                xrv_similar_case.setLoadComplete(false);
                break;
            case SimilarCaseSelector.TYPE_STEP:
                stepId = value;
                page = 0;
                isNeedShowSearchResult = true;
                xrv_similar_case.setLoadComplete(false);
                Factory.postPhp(this, Const.PApi_getSimilarCase);
                break;
            case SimilarCaseSelector.TYPE_MUTATION:
                genId = value;
                page = 0;
                isNeedShowSearchResult = true;
                xrv_similar_case.setLoadComplete(false);
                Factory.postPhp(this, Const.PApi_getSimilarCase);
                break;

        }
        // Toast.makeText(this, value + "  " + type, Toast.LENGTH_SHORT).show();
    }

    ZYDialog dialog;

    private void cancelFollow() {
        dialog = new ZYDialog.Builder(context).setTitle("提示")
                .setMessage("确定不再关注此人?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Factory.postPhp(SimilarActivity.this, Const.PCareDel);
                        dialog.dismiss();
                    }
                })
                .setCancelAble(true)
                .create();
        dialog.show();
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(context));
        if (tag == Const.PApi_getSimilarCase) {
            map.put("CancerID", cancerId);
            map.put("BodyID", bodyId);//转移情况
            map.put("StepID", stepId);
            map.put("GenID", genId);
            map.put("page", page + "");
            map.put("pageSize", "10");
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

    private boolean isNeedShowSearchResult = true;//是不是显示搜索结果

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PApi_getSimilarCase) {
            SimilarCaseBean scb = (SimilarCaseBean) response;
            isDataLoadComplected = true;
            setSimilarCaseCount(scb.getCountNum());
            hideLoading();
            if (scb.getData() != null && scb.getData().size() > 0) {
                if (page == 0) {
                    similars.clear();
                    similarCaseAdapter.setStepId(stepId);
                }
                similars.addAll(scb.getData());
                if (fl_loading.getVisibility() != View.VISIBLE) {//如果搜索页面正在显示，不更新recycleView
                    similarCaseAdapter.notifyDataSetChanged();
                }
                overLoading(0);
            } else {
                overLoading(2);
            }

        } else if (flag == Const.PCareAdd || flag == Const.PCareDel) {
            PhpUserInfoBean bean = (PhpUserInfoBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                if (flag == Const.PCareAdd) {
                    similarCaseAdapter.update(carePos, 1, true);
                    showToast("关注成功");
                } else {
                    showToast("取消关注成功");
                    similarCaseAdapter.update(carePos, 0, true);
                }
            } else {
                if (flag == Const.PCareAdd) {
                    showToast("关注失败");
                    similarCaseAdapter.update(carePos, 0, false);
                } else {
                    showToast("取消关注失败");
                    similarCaseAdapter.update(carePos, 1, false);
                }
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
        if (flag == Const.PApi_getSimilarCase) {
            overLoading(1);
            showToast("网络请求失败");
        } else if (flag == Const.PCareAdd) {
            showToast("关注失败");
            similarCaseAdapter.update(carePos, 0, false);
        } else if (flag == Const.PCareDel) {
            showToast("取消关注失败");
            similarCaseAdapter.update(carePos, 1, false);
        }
    }

    private void startLoadingTimer() {
        rp_content.startRippleAnimation();
        //匹配界面倒计时
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isShowed_5s = true;
                rp_content.stopRippleAnimation();
                hideLoading();
            }
        }, 5000);
    }

    //关闭搜索界面并显示匹配的数量
    private void hideLoading() {
        //第一次打开的时候至少显示5s匹配页面，并且数据已加载完成，并且匹配页面是在显示状态
        if (isShowed_5s && isDataLoadComplected && fl_loading.getVisibility() == View.VISIBLE) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.1f);
            alphaAnimation.setDuration(100);
            fl_loading.startAnimation(alphaAnimation);
            fl_loading.setVisibility(View.GONE);
            showSimilarCaseCount();
            similarCaseAdapter.notifyDataSetChanged();
        } else if (isShowed_5s && isDataLoadComplected) {
            showSimilarCaseCount();
        }
    }

    private void overLoading(int tag) {
        if (tag == 0) {
            if (refresh) {
                xrv_similar_case.stopRefresh();
                xrv_similar_case.setLoadComplete(false);
            }
            if (loading) {
                xrv_similar_case.stopLoadMore();
            }
        } else if (tag == 1) {
            if (refresh) {
                xrv_similar_case.stopRefresh();
            }
            if (loading) {
                page--;
                xrv_similar_case.stopLoadMore();
            }
        } else if (tag == 2) {
            if (refresh) {
                xrv_similar_case.stopRefresh();
            }
            if (loading) {
                page--;
                xrv_similar_case.setLoadComplete(true);
            }
        }
        if (refresh) refresh = false;
        if (loading) loading = false;
    }

    Handler handler = new Handler();

    //设置匹配出的相似案例数量
    private void setSimilarCaseCount(int count) {
        String scc;
        if (count > 10000)
            scc = "9999+";
        else
            scc = count + "";
        tv_search_result_tips.setText("成功匹配出" + scc + "位有对应抗癌经验的人");
    }

    private void showSimilarCaseCount() {
        if (isNeedShowSearchResult) {
            similarCaseSelector.hide();
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration(300);
            tv_search_result_tips.startAnimation(alphaAnimation);
            tv_search_result_tips.setVisibility(View.VISIBLE);
            isNeedShowSearchResult = false;
            showSelector();
        }
    }

    //显示条件选择器
    private void showSelector() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                alphaAnimation.setDuration(300);
                tv_search_result_tips.startAnimation(alphaAnimation);
                tv_search_result_tips.setVisibility(View.GONE);
                if (fl_tips_or_selector.getChildCount() == 1) {
                    fl_tips_or_selector.addView(similarCaseSelector.getView());
                }
                similarCaseSelector.show();

            }
        }, 3000);
    }


}

package com.zeyuan.kyq.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.InformationEntity;
import com.zeyuan.kyq.Entity.UserInformationEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.ArticleMainAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.manager.ClickStatisticsManager;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 个性化推荐
 */
public class PersonalizedRecommendActivity extends BaseActivity implements View.OnClickListener, HttpResponseInterface {
    private XRefreshView xrv_personalized_recommend;
    ListView rcv_personalized_recommend;
    LinearLayoutManager layoutManager;
    private Context context;
    private List<InformationEntity> informationEntities;
    private ArticleMainAdapter adapter;
    boolean refresh;
    boolean loading;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalized_recommend);
        context = this;
        initData();
        initView();
    }

    private void initData() {
        informationEntities = new ArrayList<>();

    }

    private void initView() {
        //设置返回键
        findViewById(R.id.ll_back).setOnClickListener(this);
        TextView tv_other_title = (TextView) findViewById(R.id.tv_other_title);
        tv_other_title.setText("个性化推荐");
        layoutManager = new LinearLayoutManager(context);
        xrv_personalized_recommend = (XRefreshView) findViewById(R.id.xrv_personalized_recommend);
        adapter = new ArticleMainAdapter(context, informationEntities, 0);
        rcv_personalized_recommend = (ListView) findViewById(R.id.rcv_personalized_recommend);
        rcv_personalized_recommend.setAdapter(adapter);
        xrv_personalized_recommend.setPinnedTime(1000);
        xrv_personalized_recommend.setPullLoadEnable(true);
        xrv_personalized_recommend.setMoveForHorizontal(true);
        xrv_personalized_recommend.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh = true;
                        page = 0;
                        Factory.postPhp(PersonalizedRecommendActivity.this, Const.PArticleAccurate);
                    }
                }, 500);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                loading = true;
                page++;
                Factory.postPhp(PersonalizedRecommendActivity.this, Const.PArticleAccurate);
            }

        });
        xrv_personalized_recommend.setStateCallback(new XRefreshView.XRefreshViewScrollStateChangedCallback() {
            @Override
            public void XRVScrollStateChangedCallback(int scrollState) {
                if (scrollState == 2) {
                    Glide.with(context).pauseRequests();
                } else {
                    Glide.with(context).resumeRequests();
                }
            }
        });
        rcv_personalized_recommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(context, ArticleDetailActivity.class).
                        putExtra(Const.INTENT_ARTICLE_ID, "" + adapter.getItem(position).getAid()));
                ClickStatisticsManager.getInstance().addClickEvent(Const.CLICK_EVENT_6,adapter.getItem(position).getAid()+"");
            }
        });
        Factory.postPhp(PersonalizedRecommendActivity.this, Const.PArticleAccurate);
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
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(context));
        if (tag == Const.PArticleAccurate) {
            map.put("cancer", UserinfoData.getCancerID(context));
            map.put("page", page + "");
            map.put("pagesize", "15");
            // map.put("step", UserinfoData.getStepID(context));
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PArticleAccurate) {
            UserInformationEntity entity = (UserInformationEntity) response;
            if (Const.RESULT.equals(entity.getiResult())) {
                if (entity.getData() != null && entity.getData().size() > 0) {
                    overLoading(0);
                    if (page == 0) {
                        informationEntities.clear();
                    }
                    informationEntities.addAll(entity.getData());
                    adapter.notifyDataSetChanged();
                } else {
                    overLoading(2);
                }
            } else {
                overLoading(1);
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
        overLoading(1);
    }

    private void overLoading(int tag) {
        try {

            if (tag == 0) {//正常完成加载
                if (refresh) {
                    xrv_personalized_recommend.stopRefresh();
                    xrv_personalized_recommend.setLoadComplete(false);
                }
                if (loading) {
                    xrv_personalized_recommend.stopLoadMore();
                }
            } else if (tag == 1) {//加载错误
                if (refresh) {
                    xrv_personalized_recommend.stopRefresh();
                }
                if (loading) {
                    page--;
                    xrv_personalized_recommend.stopLoadMore();
                }
            } else if (tag == 2) {//加载完成
                if (refresh) {
                    xrv_personalized_recommend.stopRefresh();
                }
                if (loading) {
                    page--;
                    xrv_personalized_recommend.setLoadComplete(true);
                }
            }
            if (refresh) refresh = false;
            if (loading) loading = false;
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "overLoading");
        }
    }
}

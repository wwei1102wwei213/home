package com.zeyuan.kyq.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.ForumBaseBean;
import com.zeyuan.kyq.Entity.ForumBaseEntity;
import com.zeyuan.kyq.Entity.Shortcut;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.CircleFindRecyclerAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FourmListActivity extends BaseActivity implements HttpResponseInterface, View.OnClickListener {
    private int page = 0;//数据的页数
    private String TypeID;//圈子id
    private ProgressBar pd;
    private TextView tv_title;
    private XRefreshView xRefreshView;
    LinearLayoutManager layoutManager;
    private CircleFindRecyclerAdapter adapter;
    private List<ForumBaseEntity> forumListDatas;//装帖子列表的数据
    private boolean refresh = false;
    private boolean loading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourm_list);
        initView();
        getData();
    }

    private void initView() {
        try {
            findViewById(R.id.ll_back).setOnClickListener(this);
            tv_title = (TextView) findViewById(R.id.tv_other_title);
            pd = (ProgressBar) findViewById(R.id.pd);
            xRefreshView = (XRefreshView) findViewById(R.id.xrv);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);

            recyclerView.setHasFixedSize(true);
            forumListDatas = new ArrayList<>();
            adapter = new CircleFindRecyclerAdapter(FourmListActivity.this, new ArrayList<Shortcut>(), forumListDatas);
            // 设置静默加载模式
//        xRefreshView1.setSilenceLoadMore();
            layoutManager = new LinearLayoutManager(FourmListActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            // 静默加载模式不能设置footerview
            recyclerView.setAdapter(adapter);
            //设置刷新完成以后，headerview固定的时间
            xRefreshView.setPinnedTime(1000);
            xRefreshView.setPullLoadEnable(true);
            xRefreshView.setMoveForHorizontal(true);
//        xRefreshView1.setAutoLoadMore(true);
            adapter.setCustomLoadMoreView(new XRefreshViewFooter(FourmListActivity.this));

            xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refresh = true;
                            page = 0;
                            Factory.postPhp(FourmListActivity.this, Const.PApi_getThreadByType);
                        }
                    }, 500);
                }

                @Override
                public void onLoadMore(boolean isSilence) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {

                            loading = true;
                            page++;
                            Factory.postPhp(FourmListActivity.this, Const.PApi_getThreadByType);

                        }
                    }, 1000);
                }
            });

            xRefreshView.setStateCallback(new XRefreshView.XRefreshViewScrollStateChangedCallback() {
                                              @Override
                                              public void XRVScrollStateChangedCallback(int scrollState) {
                                                  try {
                                                      if (scrollState == 2) {
                                                          Glide.with(FourmListActivity.this).pauseRequests();
                                                      } else {
                                                          Glide.with(FourmListActivity.this).resumeRequests();
                                                      }
                                                  } catch (Exception e) {
                                                      ExceptionUtils.ExceptionSend(e, "scrollStatus");
                                                  }

                                              }
                                          }

            );

        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "initView");
        }
    }

    private void getData() {
        TypeID = getIntent().getStringExtra(Const.TypeID);
        String title = getIntent().getStringExtra("title");
        setTitle(title);
        Factory.postPhp(this, Const.PApi_getThreadByType);
    }

    //设置标题
    private void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
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
        if (tag == Const.PApi_getThreadByType) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(FourmListActivity.this));
            map.put("page", page + "");
            map.put("pagesize", "20");
            if (!TextUtils.isEmpty(TypeID))
                map.put(Const.TypeID, TypeID);
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        showUI();
        if (flag == Const.PApi_getThreadByType) {
            ForumBaseBean bean = (ForumBaseBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                bindListView(bean);
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
        showUI();
    }

    private void bindListView(ForumBaseBean bean) {
        try {
            List<ForumBaseEntity> list = bean.getForumAllNum();
            if (page == 0) {
                forumListDatas.clear();
            }
            if (list != null) {
                forumListDatas.addAll(list);
                overLoading(0);
            } else {
                overLoading(2);
            }
            adapter.notifyDataSetChanged();//刷新list
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, FourmListActivity.this, "FourmListActivity");
        }
    }

    private void overLoading(int tag) {
        if (tag == 0) {
            if (refresh) {
                xRefreshView.stopRefresh();
                xRefreshView.setLoadComplete(false);
            }
            if (loading) {
                xRefreshView.stopLoadMore();
            }
        } else if (tag == 1) {
            if (refresh) {
                xRefreshView.stopRefresh();
            }
            if (loading) {
                page--;
                xRefreshView.stopLoadMore();
            }
        } else if (tag == 2) {
            if (refresh) {
                xRefreshView.stopRefresh();
            }
            if (loading) {
                page--;
                xRefreshView.setLoadComplete(true);
            }
        }
        if (refresh) refresh = false;
        if (loading) loading = false;
    }

    private void showUI() {
        if (pd.getVisibility() == View.VISIBLE) {
            try {
                pd.setVisibility(View.GONE);
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "onRefresh");
            }
        }
    }
}

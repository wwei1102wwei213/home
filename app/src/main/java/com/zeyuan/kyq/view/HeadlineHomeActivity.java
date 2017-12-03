package com.zeyuan.kyq.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.zeyuan.kyq.Entity.ArticleTypeEntity;
import com.zeyuan.kyq.Entity.InformationEntity;
import com.zeyuan.kyq.Entity.UserInformationEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.HomeArticleRecyclerAdapter;
import com.zeyuan.kyq.adapter.HomeTabRecyclerAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CustomView.InsideRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static anet.channel.util.Utils.context;

/**
 * Created by Administrator on 2017-12-03.
 */

public class HeadlineHomeActivity extends BaseActivity implements HttpResponseInterface, HomeTabRecyclerAdapter.OnItemClickListener{


    private List<ArticleTypeEntity> art_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headline_home);

        art_list = (List<ArticleTypeEntity>) getIntent().getSerializableExtra("Headline_List");

        initView();
        initData();

    }

    //滑动器
    private XRefreshView xv;
    //文章列表控件
    private RecyclerView rv;

    private List<InformationEntity> list;

    private HomeArticleRecyclerAdapter adapter;


    //导航栏控件
    private InsideRecyclerView rv_top;

    //导航栏适配器
    private HomeTabRecyclerAdapter adapter_top;

    private LinearLayoutManager manager;
    private void initView(){
        try {
            findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            articleIndex = 0;
            rv_top = (InsideRecyclerView) findViewById(R.id.rv_top);
            //布局设置
            manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rv_top.setLayoutManager(manager);
            //适配器设置
//            list_top = new ArrayList<>();
            adapter_top = new HomeTabRecyclerAdapter(this, art_list, this, 0);
            rv_top.setAdapter(adapter_top);

            xv = (XRefreshView) findViewById(R.id.xrv);
            rv = (RecyclerView) findViewById(R.id.rv);
            LinearLayoutManager manager1 = new LinearLayoutManager(this);
            manager1.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(manager1);
            rv.setHasFixedSize(true);
            list = new ArrayList<>();
            adapter = new HomeArticleRecyclerAdapter(this, list);
            rv.setAdapter(adapter);
            //滑动控件设置
            xv.setPinnedTime(1000);
            xv.setPullLoadEnable(true);
            xv.setMoveForHorizontal(true);
            adapter.setCustomLoadMoreView(new XRefreshViewFooter(this));
            xv.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
                @Override
                public void onRefresh() {
                    refresh = true;
                    page = 0;
                    initData();
                }
                @Override
                public void onLoadMore(boolean isSilence) {
                    loading = true;
                    page++;
                    initData();
                }
            });
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "initView");
        }
    }

    private void setTabView(UserInformationEntity entity){
        List<ArticleTypeEntity> cat = entity.getCatShowList();
        if (cat == null || cat.size() == 0) return;
        art_list = new ArrayList<>();
        ArticleTypeEntity mEntity = new ArticleTypeEntity();
        mEntity.setCatid(0);
        mEntity.setCatname("推荐");
        art_list.add(mEntity);
        //文章导航栏数据添加
        if (cat.size() > 0) {
            for (int i = 0; i < cat.size(); i++) {
                art_list.add(cat.get(i));
            }
        }
        //文章导航栏视图设置
        adapter_top.update(art_list);
    }

    private void initData(){
        Factory.postPhp(this, Const.PHomeArticleInfo);
    }

    private int page = 0;
    private int pageSize = 20;
    private boolean refresh = false;
    private boolean loading = false;
    private void overLoading(int tag) {
        if (tag == 0) {
            if (refresh) {
                xv.stopRefresh();
                xv.setLoadComplete(false);
            }
            if (loading) {
                xv.stopLoadMore();
            }
        } else if (tag == 1) {
            if (refresh) {
                xv.stopRefresh();
            }
            if (loading) {
                page--;
                xv.stopLoadMore();
            }
        } else if (tag == 2) {
            if (refresh) {
                xv.stopRefresh();
            }
            if (loading) {
                page--;
                xv.setLoadComplete(true);
            }
        }
        if (refresh) refresh = false;
        if (loading) loading = false;
    }

    private int articleIndex = 0;
    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        if (tag == Const.PHomeArticleInfo) {
            if (art_list==null||art_list.size()==0){
                map.put(Contants.CatID, "0");
            } else {
                map.put(Contants.CatID, art_list.get(articleIndex).getCatid()+"");
            }
            map.put(Contants.Page, page + "");
            map.put(Contants.PageSize, pageSize + "");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PHomeArticleInfo) {
            UserInformationEntity entity = (UserInformationEntity) response;
            try {
                if (Const.RESULT.equals(entity.getiResult())) {
                    if (articleIndex==0&&page==0){
                        setTabView(entity);
                    }
                    if (page == 0) {
                        list = new ArrayList<>();
                    }
                    List<InformationEntity> temp = entity.getData();
                    if (temp != null && temp.size() != 0) {
                        list.addAll(temp);
                        adapter.update(list, 0);
                        overLoading(0);
                    } else {
                        overLoading(2);
                    }
                } else {
                    overLoading(1);
                }
            } catch (Exception e) {
                overLoading(1);
                ExceptionUtils.ExceptionSend(e, "toActivity PHomeArticleInfo");
            }

        }
    }

    @Override
    public void showLoading(int flag) {
        findViewById(R.id.pd).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading(int flag) {
        findViewById(R.id.pd).setVisibility(View.GONE);
    }

    @Override
    public void showError(int flag) {
        if (flag == Const.PHomeArticleInfo) {
            findViewById(R.id.pd).setVisibility(View.GONE);
            Toast.makeText(context, "文章数据加载失败", Toast.LENGTH_SHORT).show();
            overLoading(2);
        }
    }

    @Override
    public void OnRecyclerItemClick(View v, int position, String typeID) {
        try {
            if (!"9999".equals(typeID)) {
                articleIndex = position;
                page = 0;
                initData();
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "OnRecyclerItemClick");
        }
    }
}

package com.zeyuan.kyq.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.zeyuan.kyq.Entity.ArticleTypeEntity;
import com.zeyuan.kyq.Entity.HomePageEntity;
import com.zeyuan.kyq.Entity.InformationEntity;
import com.zeyuan.kyq.Entity.UserInformationEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.HomeHelpRecyclerAdapter;
import com.zeyuan.kyq.app.BaseHomeNoDestroyFragment;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.FragmentCallBack;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/28.
 *
 * @author wwei
 */
public class HomeHelpFragment extends BaseHomeNoDestroyFragment implements HttpResponseInterface, FragmentCallBack {

    //文章类实体类
    private ArticleTypeEntity entity;
    //文章类型ID
    private String CatID;
    //文章类型标识
    private int CatType = 0;
    //列表分页
    private int page = 0;
    //列表分页长度
    private int pageSize = 20;
    //滑动器
    private XRefreshView xv;
    //文章列表控件
    private RecyclerView rv;
    //列表适配器
    private HomeHelpRecyclerAdapter adapter;
    //列表数据
    private List<InformationEntity> data;

    private FragmentCallBack callback;

    private List<HomePageEntity> banners;

    public void setBanners(List<HomePageEntity> banners) {
        this.banners = banners;
    }

    public void setFragmentCallback(FragmentCallBack callback) {
        this.callback = callback;
    }

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_child, container, false);

        page = 0;
        setView();
        setData();

        return rootView;
    }*/

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_child, container, false);

        page = 0;
        ZYApplication.homeHelpPageFlag = true;
        setView();
        setData();

        return rootView;
    }

    @Override
    public void initData() {

    }

    @Override
    public void toRefresh() {
        if (xv != null) {
            xv.scrollTo(0, 0);
            xv.startRefresh();
        }
    }

    @Override
    public boolean isTopFlag() {
        if (xv != null && xv.isTop()) return true;
        return false;
    }

    private View headerView;

    private void setView() {

        xv = (XRefreshView) findViewById(R.id.xrv);
        xv.setCallback(mHomeCallBack);
        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager manager1 = new LinearLayoutManager(context);
        manager1.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager1);
        rv.setHasFixedSize(true);
        data = new ArrayList<>();
        if (banners == null || banners.size() == 0) banners = new ArrayList<>();
        adapter = new HomeHelpRecyclerAdapter(context, data, rv, banners);
        if ("0".equals(UserinfoData.getIsHaveStep(context))) {
            headerView = adapter.setHeaderView(R.layout.view_diy_recommend, rv);
            initHeaderView();
        }

        rv.setAdapter(adapter);

        //滑动控件设置
        xv.setPinnedTime(1000);
        xv.setPullLoadEnable(true);
        xv.setMoveForHorizontal(true);
        adapter.setCustomLoadMoreView(new XRefreshViewFooter(context));

        xv.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                refresh = true;
                page = 0;
                setData();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                loading = true;
                page++;
                setData();
            }

        });

    }

    private void initHeaderView() {
        if (headerView == null) return;
        headerView.findViewById(R.id.v_recommend_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setHeaderView(null, rv);
                adapter.notifyDataSetChanged();
            }
        });
        headerView.findViewById(R.id.v_recommend_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiUtils.startIndividuationTreatment(getActivity());
                // createInfoStepFragment();
            }
        });
    }

    private void setData() {
        Factory.postPhp(this, Const.PHomeArticleInfo);
    }

//    private InfoStepFragment stepFragment;
//    //为用户创建阶段
//    private void createInfoStepFragment(){
//        if (callback==null) return;
//        if(stepFragment==null){
//            stepFragment = InfoStepFragment.getInstance(this, 0);
//        }
//        stepFragment.show(getActivity().getSupportFragmentManager(), InfoStepFragment.type);
//    }

    @Override
    public void dataCallBack(String str, int flag, String tag, Object obj) {
        if (flag == Const.FRAGMENT_INFO_STEP) {
            adapter.setHeaderView(null, rv);
            if (callback != null) {
                callback.dataCallBack(str, flag, tag, obj);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //新版开启个性化治疗的返回
        if (requestCode == 9 && resultCode == -1) {
            adapter.setHeaderView(null, rv);
//            if (callback!=null){
//                callback.dataCallBack(str,Const.FRAGMENT_INFO_STEP,tag,obj);
//            }
        }
    }

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

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(context));
        if (tag == Const.PHomeArticleInfo) {
            map.put(Contants.CatID, "0");
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
            if (Const.RESULT.equals(entity.getiResult())) {
                if (page == 0) {
                    data = new ArrayList<>();
                    adapter.setHelp(entity.getHelp());
                }
                List<InformationEntity> temp = entity.getData();
                if (temp != null && temp.size() != 0) {
                    data.addAll(temp);
                    adapter.update(data, 0);
                    overLoading(0);
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
        if (flag == Const.PHomeArticleInfo) {
            overLoading(1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {

        }
        ZYApplication.homeHelpPageFlag = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        ZYApplication.homeHelpPageFlag = false;
    }
}

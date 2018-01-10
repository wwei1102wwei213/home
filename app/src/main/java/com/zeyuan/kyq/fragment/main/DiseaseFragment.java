package com.zeyuan.kyq.fragment.main;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.ForumBaseBean;
import com.zeyuan.kyq.Entity.ForumBaseEntity;
import com.zeyuan.kyq.Entity.Shortcut;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.CircleFindRecyclerAdapter;
import com.zeyuan.kyq.adapter.RecyclerCircleAdapter;
import com.zeyuan.kyq.app.LazyFragment;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.FragmentCallBack;
import com.zeyuan.kyq.fragment.ChooseCancerFragment;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CircleImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-12-23.
 */

public class DiseaseFragment extends LazyFragment implements HttpResponseInterface, RecyclerCircleAdapter.OnItemClickListener, FragmentCallBack{

    private String CancerParentID = "29";
    private String CircleID;
    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_disease, container, false);
        CancerParentID = UiUtils.getCancerParentID(UserinfoData.getCancerID(context), false);
        CircleID = UserinfoData.getCancerID(context);
        initView();
        return rootView;
    }

    @Override
    protected void initData() {
        try {
            Factory.postPhp(this,Const.PGetForumList_bank);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "Disease");
        }
    }

    @Override
    protected void setDefaultFragmentTitle(String title) {

    }

    private List<ForumBaseEntity> data;
    private RecyclerView recyclerView;
    private CircleFindRecyclerAdapter adapter;
    private XRefreshView xRefreshView;
    private View headerView;
    private LinearLayoutManager layoutManager;
    private void initView(){

        try {

            xRefreshView = (XRefreshView) findViewById(R.id.xrv);
            xRefreshView.setPullLoadEnable(true);
            recyclerView = (RecyclerView) findViewById(R.id.rv);
            recyclerView.setHasFixedSize(true);

            data = new ArrayList<>();
            List<Shortcut> temp = new ArrayList<>();
            adapter = new CircleFindRecyclerAdapter(context,temp,data);

            // 设置静默加载模式
            layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            headerView = adapter.setHeaderView(R.layout.layout_head_disease, recyclerView);
            initHeaderView();
            recyclerView.setAdapter(adapter);
            xRefreshView.setPullLoadEnable(true);
//        xRefreshView.setAutoLoadMore(false);
            xRefreshView.setPinnedTime(1000);
            xRefreshView.setMoveForHorizontal(true);
            adapter.setCustomLoadMoreView(new XRefreshViewFooter(context));
            xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
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
            xRefreshView.setStateCallback(new XRefreshView.XRefreshViewScrollStateChangedCallback() {
                @Override
                public void XRVScrollStateChangedCallback(int scrollState) {
                    if (scrollState==2){
                        Glide.with(context).pauseRequests();
                    }else {
                        Glide.with(context).resumeRequests();
                    }
                }
            });
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "initView");
        }
    }

    private View v_rv_circle;
    private ImageView civ;
    private TextView circleName;
    private View v_change_view;
    private void initHeaderView(){
        try {
//圈子信息
            civ = (CircleImageView) headerView.findViewById(R.id.avatar);
            civ.setImageResource(UiUtils.getCancerImage(UserinfoData.getCancerID(context)));
            circleName = (TextView) headerView.findViewById(R.id.title);
            circleName.setText(MapDataUtils.getCircleValues(CircleID));
            v_change_view = headerView.findViewById(R.id.v_change_cancer);
            v_change_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toChangeCancer();
                }
            });
            v_rv_circle = headerView.findViewById(R.id.rv_disease_tag);
            initRecyclerView();
            initSelectorView();
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "initHeaderView");
        }

    }

    private void toChangeCancer(){
        ShowChooseCancer();
    }

    /**
     * 圈子分级栏目视图
     *
     */
    RecyclerCircleAdapter mAdapter;
    private void initRecyclerView(){
        try {
            Map<String,List<String>> map = (Map<String,List<String>>)Factory.getData(Const.N_DataCircleCancer);
            List<String> temp = map.get(CircleID);
            if(temp==null||temp.size()==0){
                v_rv_circle.setVisibility(View.GONE);
            }else {
                List<String> add = new ArrayList<>();
                add.add(CircleID);
                add.addAll(temp);
                v_rv_circle.setVisibility(View.VISIBLE);
                RecyclerView rv = (RecyclerView) headerView.findViewById(R.id.rv_disease);
                LinearLayoutManager manager = new LinearLayoutManager(context);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                rv.setLayoutManager(manager);
                rv.setItemAnimator(new DefaultItemAnimator());
                LogCustom.i("ZYS", "add:" + add.toString());
                mAdapter = new RecyclerCircleAdapter(context,add,this,0);
                rv.setAdapter(mAdapter);
            }
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "initRecyclerView");
        }
    }

    private void updateRecyclerView(){
        try {
            Map<String,List<String>> map = (Map<String,List<String>>)Factory.getData(Const.N_DataCircleCancer);
            List<String> temp = map.get(CircleID);
            if(temp==null||temp.size()==0){
                v_rv_circle.setVisibility(View.GONE);
            }else {
                List<String> add = new ArrayList<>();
                add.add(CircleID);
                add.addAll(temp);
                v_rv_circle.setVisibility(View.VISIBLE);
                mAdapter.update(add);
            }
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "initRecyclerView");
        }
    }

    private View v1,v2,v3;
    private View[] lines;
    private TextView[] selects;
    private void initSelectorView(){
        v1 = headerView.findViewById(R.id.v1);
        v2 = headerView.findViewById(R.id.v2);
        v3 = headerView.findViewById(R.id.v3);
        selects = new TextView[3];
        selects[0] = (TextView) headerView.findViewById(R.id.tv1);
        selects[0].setSelected(true);
        selects[1] = (TextView) headerView.findViewById(R.id.tv2);
        selects[2] = (TextView) headerView.findViewById(R.id.tv3);
        lines = new View[3];
        lines[0] = headerView.findViewById(R.id.line1);
        lines[0].setVisibility(View.VISIBLE);
        lines[1] = headerView.findViewById(R.id.line2);
        lines[2] = headerView.findViewById(R.id.line3);
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelect(0);
            }
        });
        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelect(1);
            }
        });
        v3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelect(2);
            }
        });
    }

    private int currentSelect = 0;
    private int sort = 0;
    private void setSelect(int index){
        if (currentSelect == index) return;
        for (int i=0;i<3;i++){
            lines[i].setVisibility(i==index?View.VISIBLE:View.INVISIBLE);
            selects[i].setSelected(i==index);
        }
        sort = index;
        currentSelect = index;
        initData();
    }

    @Override
    public void OnRecyclerItemClick(View v, int position, String typeID) {
        this.typeID = typeID;
        if(position==0){
            typeFlag = false;
        }else{
            typeFlag = true;
        }
        page = 0;
        initData();
    }

    private int pageSize = 15;
    private int page = 0;
    private String typeID;
    private boolean typeFlag = false;
    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == Const.PGetForumList_bank){
            map.put(Contants.InfoID,UserinfoData.getInfoID(context));
//            map.put(Contants.CircleID,CircleID);
            map.put("page",page+"");
            if ("0".equals(CancerParentID)){

            } else {
                map.put("pid", CancerParentID);
            }
            map.put("CircleID", CircleID);
            map.put("pagesize",pageSize+"");
            if (typeFlag){
                map.put("TypeID",typeID);
            }
            map.put("sort", sort+"");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PGetForumList_bank){
            try {
                ForumBaseBean bean = (ForumBaseBean)response;
                if (Const.RESULT.equals(bean.getiResult())){
                    //圈子的简介
                    List<ForumBaseEntity> list = bean.getForumAllNum();
                    if (list != null && list.size() > 0) {
                        if(page==0){
                            data = new ArrayList<>();
                        }
                        data.addAll(list);
                        adapter.update(data);//刷新列表
                        overPullDown(0);
                        overCallUp(0);
                    }else{
                        if (page==0){
                            data = new ArrayList<>();
                            adapter.update(data);//刷新列表
                            showToast("该圈子还没任何帖子哦\n" +"快来发个帖和大家交流");
                        }
                        overPullDown(0);
                        overCallUp(2);
                    }
                }else{
                    overPullDown(1);
                    overCallUp(1);
                }
            } catch (Exception e){

            }

        }
    }

    @Override
    public void showLoading(int tag) {
        if(tag == Const.PGetForumList_bank){
            findViewById(R.id.pd).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading(int tag) {
        if(tag == Const.PGetForumList_bank){
            findViewById(R.id.pd).setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(int tag) {
        if(tag == Const.PGetForumList_bank){
            findViewById(R.id.pd).setVisibility(View.GONE);
            overPullDown(1);
            overCallUp(1);
        }
    }

    private boolean refresh;
    private boolean loading;
    private void overPullDown(int down){
        if (refresh&&xRefreshView!=null){
            refresh = false;
            xRefreshView.stopRefresh();
            xRefreshView.setLoadComplete(false);
        }
    }

    private void overCallUp(int up){
        if (loading&&xRefreshView!=null){
            loading = false;
            if (up==2){
                xRefreshView.setLoadComplete(true);
            }else {
                xRefreshView.stopLoadMore();
            }
            if (up!=0) page--;
        }
    }

    private ChooseCancerFragment fragment;
    private void ShowChooseCancer(){
        if (fragment==null){
            fragment = ChooseCancerFragment.getInstance(this);
//            fragment.setFlagParent(true);
        }
        fragment.show(getChildFragmentManager(),ChooseCancerFragment.TAG);
    }

    @Override
    public void dataCallBack(String str, int flag, String tag, Object obj) {
        if (flag == Const.FRAGMENT_CHOOSE_CANCER){
            if (!TextUtils.isEmpty(str)){
                CancerParentID = UiUtils.getCancerParentID(str, false);
                CircleID = str;
                circleName.setText(MapDataUtils.getCircleValues(CircleID));
                civ.setImageResource(UiUtils.getCancerImage(str));
                page = 0;
                initData();
                updateRecyclerView();
            }
        }
    }


}

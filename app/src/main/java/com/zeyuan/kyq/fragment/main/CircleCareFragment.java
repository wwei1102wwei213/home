package com.zeyuan.kyq.fragment.main;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.ForumBaseBean;
import com.zeyuan.kyq.Entity.ForumBaseEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.CircleCareRecyclerAdapter;
import com.zeyuan.kyq.app.BaseZyFragment;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DensityUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.MyListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/15.
 *
 *
 * @author wwei
 */
public class CircleCareFragment extends BaseZyFragment implements HttpResponseInterface{

    private int page = 0;
    private int pagesize = 15;
    private XRefreshView xRefreshView;
    private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.item_fragment_care,container,false);

        initView();
        initData();

        return rootView;
    }

    private MyListView lv;
    private CircleCareRecyclerAdapter adapter;
    private List<ForumBaseEntity> forumListDatas;//装帖子列表的数据
    private int MAX_CARE = 0;
    private LinearLayout v_care;
    private View body;
    private View v_lv;
    private View v_fl;

    private void initView(){

        try {
            initMaxCare();
            body = findViewById(R.id.body);
            v_fl = findViewById(R.id.fl);
            xRefreshView = (XRefreshView)findViewById(R.id.xrv);
            recyclerView = (RecyclerView)findViewById(R.id.rv);

            recyclerView.setHasFixedSize(true);
            forumListDatas = new ArrayList<>();
            adapter = new CircleCareRecyclerAdapter(context,new ArrayList<String>(),forumListDatas,MAX_CARE);
            // 设置静默加载模式
//        xRefreshView1.setSilenceLoadMore();
            layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
            // 静默加载模式不能设置footerview
            recyclerView.setAdapter(adapter);
            //设置刷新完成以后，headerview固定的时间
            xRefreshView.setPinnedTime(1000);
            xRefreshView.setPullLoadEnable(true);
            xRefreshView.setMoveForHorizontal(true);
//        xRefreshView1.setAutoLoadMore(true);
            adapter.setCustomLoadMoreView(new XRefreshViewFooter(context));

            xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refresh = true;
                            page = 0;
                            initData();

                        }
                    }, 500);
                }

                @Override
                public void onLoadMore(boolean isSilence) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {

                            loading = true;
                            page++;
                            initData();

                        }
                    }, 1000);
                }
            });

            xRefreshView.setStateCallback(new XRefreshView.XRefreshViewScrollStateChangedCallback() {
                @Override
                public void XRVScrollStateChangedCallback(int scrollState) {
                    if (scrollState == 2) {
                        Glide.with(context).pauseRequests();
                    } else {
                        Glide.with(context).resumeRequests();
                    }
                }
            });

            findViewById(R.id.tv_empty_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toFriendFragment();
                }
            });

        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"initView");
        }
    }

    private void initData(){
        Factory.postPhp(this, Const.PCareArc);
    }

    private void initMaxCare(){
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int item = DensityUtils.dpToPx(context, 38);
        int space = DensityUtils.dpToPx(context,108);
        MAX_CARE = (width-space)/item;
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String,String> map = new HashMap<>();
        if (tag == Const.PCareArc){
            map.put(Contants.InfoID, UserinfoData.getInfoID(context));
            map.put("page",page+"");
            map.put("pagesize",pagesize+"");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return null;
    }

    @Override
    public void toActivity(Object response, int flag) {
        try {
            if (flag == Const.PCareArc){
                findViewById(R.id.pd).setVisibility(View.GONE);
                ForumBaseBean bean = (ForumBaseBean)response;
                if (Const.RESULT.equals(bean.getiResult())){
                        bindListView(bean);
                }else {
                    overLoading(1);
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"toAct");
        }
    }




    private void bindListView(ForumBaseBean bean) {
        try {
            List list = bean.getForumAllNum();
            if (list != null && list.size() > 0) {
                LogCustom.i("ZYS", "CircleCare SIZE:" + list.size());
                if(page==0){
                    isSpace = false;
                    forumListDatas = new ArrayList<>();
                    forumListDatas.addAll(list);
                    adapter.update(bean);
                }else {
                    forumListDatas.addAll(list);
                    adapter.update(forumListDatas);
                }
                overLoading(0);
                if (xRefreshView.getVisibility()!=View.VISIBLE){
                    xRefreshView.setVisibility(View.VISIBLE);
                    v_fl.setVisibility(View.GONE);
                }
            }else{
                if (page==0){
                    isSpace = true;
//                    Toast.makeText(context,"暂未发现任何动态\n到圈中去关注好友",Toast.LENGTH_SHORT).show();
                    xRefreshView.setVisibility(View.GONE);
                    v_fl.setVisibility(View.VISIBLE);
//                    LogCustom.i("EXCO","HERE");
                    setEmptyPage();
                }
                overLoading(2);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "CircleCareFragment");
        }
    }

    private void overLoading(int tag){
        if (tag == 0){
            if(refresh){
                xRefreshView.stopRefresh();
                xRefreshView.setLoadComplete(false);
            }
            if(loading){
                xRefreshView.stopLoadMore();
            }
        }else if (tag == 1){
            if(refresh){
                xRefreshView.stopRefresh();
            }
            if(loading){
                page--;
                xRefreshView.stopLoadMore();
            }
        }else if (tag == 2){
            if(refresh){
                xRefreshView.stopRefresh();
            }
            if(loading){
                page--;
                xRefreshView.setLoadComplete(true);
            }
        }
        if (refresh) refresh = false;
        if (loading) loading = false;
    }



    @Override
    public void showLoading(int flag) {

    }

    @Override
    public void hideLoading(int flag) {

    }

    @Override
    public void showError(int flag) {
        if (flag == Const.PCareArc){
            findViewById(R.id.pd).setVisibility(View.GONE);
            overLoading(1);
        }
    }

    private boolean refresh = false;
    private boolean loading = false;

    private void setEmptyPage(){
        try {

        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,context,"显示空白页出错");
        }
    }

    private boolean isSpace = false;

    public boolean isSpace() {
        return isSpace;
    }

    public void releaseData(){
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (isSpace){
                initData();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"动态刷新空白出错");
        }
    }

    private void toFriendFragment(){
        callback.doEmptyPage(null,0,0,false,null,null);
    }
}

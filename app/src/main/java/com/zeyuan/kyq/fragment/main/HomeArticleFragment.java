package com.zeyuan.kyq.fragment.main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.zeyuan.kyq.Entity.ArticleTypeEntity;
import com.zeyuan.kyq.Entity.InformationEntity;
import com.zeyuan.kyq.Entity.UserInformationEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.HomeArticleRecyclerAdapter;
import com.zeyuan.kyq.app.BaseHomeNoDestroyFragment;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.OtherUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/28.
 *
 *
 * @author wwei
 */
public class HomeArticleFragment extends BaseHomeNoDestroyFragment implements HttpResponseInterface{

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
    private HomeArticleRecyclerAdapter adapter;
    //列表数据
    private List<InformationEntity> data;

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_child,container,false);

        try {


            Bundle bundle = getArguments();
            entity = (ArticleTypeEntity)bundle.getSerializable(Contants.CatID);
            if (entity==null|| OtherUtils.isEmpty(entity.getCatid()+"")) return rootView;

            CatID = entity.getCatid()+"";
            CatType = entity.getCattype();

            page = 0;

            setView();
            setData();
        }catch (Exception e){

        }

        return rootView;
    }*/

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_child,container,false);
        try {
            Bundle bundle = getArguments();
            entity = (ArticleTypeEntity)bundle.getSerializable(Contants.CatID);
            if (entity==null|| OtherUtils.isEmpty(entity.getCatid()+"")) return rootView;
            CatID = entity.getCatid()+"";
            CatType = entity.getCattype();
            page = 0;
            setView();
            setData();
        }catch (Exception e){

        }
        return rootView;
    }

    @Override
    public void initData() {
    }

    @Override
    public void toRefresh() {
        if (xv!=null){
            xv.scrollTo(0,0);
            xv.startRefresh();
        }
    }

    @Override
    public boolean isTopFlag() {
        if (xv!=null&&xv.isTop()) return true;
        return false;
    }

    @Override
    protected void onInvisible() {

    }

    private void setView(){
        try {

            xv = (XRefreshView)findViewById(R.id.xrv);
            xv.setCallback(mHomeCallBack);
            rv = (RecyclerView)findViewById(R.id.rv);
            LinearLayoutManager manager1 = new LinearLayoutManager(context);
            manager1.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(manager1);
            rv.setHasFixedSize(true);
            data = new ArrayList<>();
            adapter = new HomeArticleRecyclerAdapter(context,data);
            rv.setAdapter(adapter);

            //滑动控件设置
            xv.setPinnedTime(1000);
            xv.setPullLoadEnable(true);
            xv.setMoveForHorizontal(true);
            adapter.setCustomLoadMoreView(new XRefreshViewFooter(context));

            xv.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener(){
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
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"initView");
        }
    }

    private void setData(){
        Factory.postPhp(this, Const.PHomeArticleInfo);
    }

    private boolean refresh = false;
    private boolean loading = false;

    private void overLoading(int tag){
        if (tag == 0){
            if(refresh){
                xv.stopRefresh();
                xv.setLoadComplete(false);
            }
            if(loading){
                xv.stopLoadMore();
            }
        }else if (tag == 1){
            if(refresh){
                xv.stopRefresh();
            }
            if(loading){
                page--;
                xv.stopLoadMore();
            }
        }else if (tag == 2){
            if(refresh){
                xv.stopRefresh();
            }
            if(loading){
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
        if (tag == Const.PHomeArticleInfo){
            map.put(Contants.CatID,CatID);
            map.put(Contants.Page,page+"");
            map.put(Contants.PageSize,pageSize+"");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        if(flag == Const.PHomeArticleInfo){
            UserInformationEntity entity = (UserInformationEntity)response;
            if (Const.RESULT.equals(entity.getiResult())){
                if(page==0) data = new ArrayList<>();
                List<InformationEntity> temp = entity.getData();
                if (temp!=null&&temp.size()!=0){
                    data.addAll(temp);
                    adapter.update(data,CatType);
                    overLoading(0);
                }else {
                    if(page==0){
                        Toast.makeText(context,"暂未发现任何文章\n关注大V，订阅文章",Toast.LENGTH_SHORT).show();
                    }
                    overLoading(2);
                }
            }else {
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
        if (flag == Const.PHomeArticleInfo){
            overLoading(1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (adapter!=null){
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e){

        }
    }
}

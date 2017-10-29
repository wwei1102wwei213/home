package com.zeyuan.kyq.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.zeyuan.kyq.Entity.HomePageEntity;
import com.zeyuan.kyq.Entity.InformationEntity;
import com.zeyuan.kyq.Entity.UserInformationEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.HomeArticleRecyclerAdapter;
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

/**
 * Created by Administrator on 2017/4/11.
 *
 * 文章分类列表
 *
 * @author wwei
 */
public class ArticleTypeActivity extends BaseActivity implements HttpResponseInterface{

    private HomePageEntity entity;
    private String CatID;
    private int CatType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_type);
        entity = (HomePageEntity)getIntent().getSerializableExtra(Const.INTENT_ARTICLE_TYPE_ENTITY);
        //设置标题
        TextView title = (TextView)findViewById(R.id.tv_title);
        title.setText(TextUtils.isEmpty(entity.getName())?"未知分类":entity.getName());
        //判断文章分类ID是否为空
        CatID = entity.getSign_a();
        if (TextUtils.isEmpty(CatID)) {
            showToast("分类标识出错");
            return;
        }
        //设置文章UI类型
        String temp = entity.getSign_b();
        CatType = TextUtils.isEmpty(temp)?0:Integer.valueOf(temp);
        //设置视图
        initView();
        //设置数据
        initData();
    }

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
    //刷新标识
    private boolean refresh = false;
    //加载标识
    private boolean loading = false;
    private void initView(){
        try {
            xv = (XRefreshView)findViewById(R.id.xrv);
            rv = (RecyclerView)findViewById(R.id.rv);
            LinearLayoutManager manager1 = new LinearLayoutManager(this);
            manager1.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(manager1);
            rv.setHasFixedSize(true);
            data = new ArrayList<>();
            adapter = new HomeArticleRecyclerAdapter(this,data);
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
            findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "initView");
        }
    }

    private void initData(){
        Factory.postPhp(this,Const.PHomeArticleInfo);
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
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
                        showToast("暂未发现任何文章\n" + "关注大V，订阅文章");
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
            showToast("网络请求失败");
        }

    }

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
}

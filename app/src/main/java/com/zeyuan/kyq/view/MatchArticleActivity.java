package com.zeyuan.kyq.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.zeyuan.kyq.Entity.InformationEntity;
import com.zeyuan.kyq.Entity.SameEntity;
import com.zeyuan.kyq.Entity.UserInformationEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.MatchArticleRecyclerAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.FragmentCallBack;
import com.zeyuan.kyq.fragment.ChooseCancerFragment;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/4.
 *
 * 精准内容
 *
 * @author wwei
 */
public class MatchArticleActivity extends BaseActivity implements HttpResponseInterface,FragmentCallBack
        ,MatchArticleRecyclerAdapter.onGvItemClickListener {

    //癌种ID
    private String CancerID = null;
    //阶段类型ID
    private String CureConfID = null;
    //标签ID
    private List<SameEntity> list;
    //显示隐藏总控件
    private View body;
    //TAB控件
    private TabLayout tl;
    //无数据控件
    private View fl;
    //滑动控件
    private XRefreshView xv;
    //列表控件
    private RecyclerView rv;
    //列表适配器
    private MatchArticleRecyclerAdapter adapter;
    //列表数据
    private List<InformationEntity> data;
    //TAB标识
    private int index = -2;
    //TagID
    private String TagID = null;
    //
    private int HTTP_TAG = 0;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_article);
        initStatusBar();
        try {

            CancerID = UserinfoData.getCancerID(this);

            list = new ArrayList<>();

            initView();

            initData();

        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"onCreate");
        }
    }

    private boolean refresh = false;
    private boolean loading = false;
    private int page = 0;
    private final String pageSize = "15";
    private void initView(){
        try {


            //设置标题
            title = (TextView)findViewById(R.id.title);
            try {
                title.setText(MapDataUtils.getCancerValues(CancerID));
            }catch (Exception e){
                ExceptionUtils.ExceptionSend(e,"getCancerValues");
            }


            findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            findViewById(R.id.tv_change).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowChooseCancer();
                }
            });

            body = findViewById(R.id.body);
            fl = findViewById(R.id.fl);

            //设置标签选项控件
            tl = (TabLayout)findViewById(R.id.tl);
            tl.setTabMode(TabLayout.MODE_SCROLLABLE);
            tl.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int pos = tl.getSelectedTabPosition();
                    LogCustom.i("ZYS","pos:"+pos);
                    if (pos!=-1&&pos!=index){//判断非当前标签
                        index = pos;
                        if (index!=-2){
                            page = 0;
                            xv.setLoadComplete(false);
                            if (pos==0){//判断参数标识
                                HTTP_TAG = 0;
                            }else {
                                HTTP_TAG = 1;
                            }
                            initData();
                        }
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"initView");
        }

        //
        try {
            xv = (XRefreshView)findViewById(R.id.xrv);
            rv = (RecyclerView)findViewById(R.id.rv);
            LinearLayoutManager manager1 = new LinearLayoutManager(this);
            manager1.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(manager1);
            rv.setHasFixedSize(true);
            data = new ArrayList<>();
            adapter = new MatchArticleRecyclerAdapter(this,data,this);
            rv.setAdapter(adapter);

            //滑动控件设置
            xv.setPinnedTime(1000);
            xv.setPullLoadEnable(true);
            xv.setMoveForHorizontal(true);
            adapter.setCustomLoadMoreView(new XRefreshViewFooter(this));

            xv.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener(){
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
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "initView");
        }

    }

    private void initData(){
        Factory.postPhp(this, Const.PArticleAccurate);
    }

    private void setTabView(){
        try {
            tl.removeAllTabs();
            tl.addTab(tl.newTab().setText("全部"));
            for (SameEntity entity:list){
                tl.addTab(tl.newTab().setText(TextUtils.isEmpty(entity.getName())?"未知标签":entity.getName()));
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"setTabView");
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String,String> map = new HashMap<>();
        if (tag == Const.PArticleAccurate){
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
            map.put("cancer",CancerID);
            map.put("page",page+"");
            map.put("pagesize",pageSize);
            if (HTTP_TAG==1){
                if (index!=-2&&index!=0&&list!=null&&!TextUtils.isEmpty(list.get(index-1).getId())){
                    CureConfID = list.get(index-1).getId();
                    map.put("step",CureConfID);
                }
            }else if(HTTP_TAG==2){
                map.put("step",CureConfID);
                map.put("tag",TagID);
            }
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    private int select = -1;
    @Override
    public void toActivity(Object response, int flag) {
        if(flag == Const.PArticleAccurate){
            UserInformationEntity entity = (UserInformationEntity)response;
            try {

                if (Const.RESULT.equals(entity.getiResult())){
                    if (index==-2){
                        list = entity.getSteplist();
                        if (list!=null&&list.size()>0){
                            setTabView();
                        }else {
                            tl.setVisibility(View.GONE);
                        }
                    }
                    if(page==0) data = new ArrayList<>();
                    List<InformationEntity> temp = entity.getData();
                    if (temp!=null&&temp.size()!=0){
                        data.addAll(temp);

                        if (HTTP_TAG==1){
                            List <SameEntity> sameEntities = entity.getTaglist();
                            if (sameEntities==null){
                                sameEntities = new ArrayList<>();
                            }
                            adapter.update(data,sameEntities,0);
                        }else if(HTTP_TAG==2){
                            adapter.update(data, 0 ,select);
                        }else {
                            adapter.update(data, 0);
                        }

                        if (page==0){
                            if(xv.getVisibility()!=View.VISIBLE){
                                xv.setVisibility(View.VISIBLE);
                            }
                            if (fl.getVisibility()!=View.GONE){
                                fl.setVisibility(View.GONE);
                            }
                        }
                        overLoading(0);
                    }else {
                        if(HTTP_TAG==2){
                            page = tempPage;
                            showToast("该标签还没有内容哦");
                        }else {
                            if (page==0){
                                if(fl.getVisibility()!=View.VISIBLE){
                                    fl.setVisibility(View.VISIBLE);
                                }
                                if (xv.getVisibility()!=View.GONE){
                                    xv.setVisibility(View.GONE);
                                }
                            }
                        }
                        overLoading(2);
                    }
                }else {
                    overLoading(1);
                }
            }catch (Exception e){
                ExceptionUtils.ExceptionSend(e,"toActivity");
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
        if(flag == Const.PArticleAccurate){
            showToast("网络请求出错");
            if(page==0){
                overLoading(0);
            }else {
                overLoading(1);
            }
            if (HTTP_TAG==2){
                page = tempPage;
            }
        }
    }

    private void overLoading(int tag){
        try {

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
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"overLoading");
        }
    }

    private ChooseCancerFragment fragment;
    private void ShowChooseCancer(){
        if (fragment==null){
            fragment = ChooseCancerFragment.getInstance(this);
        }
        fragment.show(getSupportFragmentManager(),ChooseCancerFragment.TAG);
    }

    @Override
    public void dataCallBack(String str, int flag, String tag, Object obj) {
        if (flag == Const.FRAGMENT_CHOOSE_CANCER){
            if (!TextUtils.isEmpty(str)&&!str.equals(CancerID)){
                CancerID = str;
                title.setText(MapDataUtils.getCancerValues(CancerID));
                page = 0;
                index = -2;
                HTTP_TAG = 0;
                initData();
            }
        }
    }

    private int tempPage = 0;
    @Override
    public void onGvItemClick(String id, int pos) {
        if (pos==-1){
            page = 0;
            xv.setLoadComplete(false);
            HTTP_TAG = 1;
            initData();
        }else {
            tempPage = page;
            page = 0;
            HTTP_TAG = 2;
            TagID = id;
            select = pos;
            initData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogCustom.i("ZYS","onDestroy");
    }
}

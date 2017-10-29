package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.ForumBaseBean;
import com.zeyuan.kyq.Entity.ForumBaseEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.CircleListRecyclerAdapter;
import com.zeyuan.kyq.adapter.RecyclerCircleAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.FollowBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CircleImageView;
import com.zeyuan.kyq.widget.CustomView.OnCustomRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/5.
 *
 * 新圈子页面
 *
 * @author wwei
 */
public class NewCircleActivity extends BaseActivity implements View.OnClickListener, ViewDataListener,OnCustomRefreshListener,
        CompoundButton.OnCheckedChangeListener ,HttpResponseInterface,RecyclerCircleAdapter.OnItemClickListener{

    private static final String TAG = "NewCircleActivity";

    private List<ForumBaseEntity> data;
    private CheckBox isfollow;//是否关注
    private int page = 0;
    private int pagesize = 30;
    private boolean topFlag = false;
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_circle);
        try {
            CircleID = getIntent().getStringExtra(Contants.CircleID);//测试注释了
            if (TextUtils.isEmpty(CircleID)) {
                initWhiteTitle(getString(R.string.froum_list));
                throw new RuntimeException("NewCircleActivity error entrance!");
            }else {
                initWhiteTitle(MapDataUtils.getCircleValues(CircleID));
                initRecycler();
                initView();
                initData();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, TAG);
        }
    }

    private RecyclerView recyclerView;
    private CircleListRecyclerAdapter adapter;
    private XRefreshView xRefreshView;
    private View headerView;
    private LinearLayoutManager layoutManager;
    private void initRecycler(){

        xRefreshView = (XRefreshView) findViewById(R.id.xrv);
        xRefreshView.setPullLoadEnable(true);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);

        data = new ArrayList<>();
        adapter = new CircleListRecyclerAdapter(this,data);

        // 设置静默加载模式
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        headerView = adapter.setHeaderView(R.layout.view_circle_top, recyclerView);
        initHeaderView();
        recyclerView.setAdapter(adapter);
        xRefreshView.setPullLoadEnable(true);
//        xRefreshView.setAutoLoadMore(false);
        xRefreshView.setPinnedTime(1000);
        xRefreshView.setMoveForHorizontal(true);
        adapter.setCustomLoadMoreView(new XRefreshViewFooter(this));
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
                    Glide.with(NewCircleActivity.this).pauseRequests();
                }else {
                    Glide.with(NewCircleActivity.this).resumeRequests();
                }
            }
        });

    }

    private View v_rv_circle;
    private void initHeaderView(){
        //圈子信息
        civ = (CircleImageView) headerView.findViewById(R.id.avatar);
        civ.setImageResource(UiUtils.getCancerImage(CircleID));
        circleName = (TextView) headerView.findViewById(R.id.title);
        initTopName();
        isfollow = (CheckBox) headerView.findViewById(R.id.isfollow);
        setFollowStatus();

        v_rv_circle = headerView.findViewById(R.id.rl_rv_circle);
        tv_description = (TextView)headerView.findViewById(R.id.cancer);
        tv_description.setVisibility(View.GONE);
        isfollow.setOnCheckedChangeListener(this);
        initRecyclerView();
    }

    private void initTopName(){
        String name = MapDataUtils.getCircleValues(CircleID);
        if(TextUtils.isEmpty(name)){
            circleName.setVisibility(View.GONE);
        }else {
            circleName.setVisibility(View.VISIBLE);
            circleName.setText(MapDataUtils.getCircleValues(CircleID));
        }
    }

    /***
     * 请求数据
     *
     */
    private void initData() {
        try {
            Factory.postPhp(this,Const.PGetForumList_bank);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, TAG);
        }
    }

    private void getFactoryForFlag(int flag){
        Factory.post(this, flag);
    }

    private TextView circleName;
    private TextView tv_description;
    private ImageView iv_ft;
    private ImageView iv_frush;

    /***
     * 视图初始化
     *
     */
    public void initView() {
        try {
            //发帖按钮
            iv_ft = (ImageView)findViewById(R.id.iv_ft);
            iv_frush = (ImageView)findViewById(R.id.iv_frush);
            iv_ft.setOnClickListener(this);
            iv_frush.setOnClickListener(this);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, TAG);
        }
    }

    /**
     * 圈子分级栏目视图
     *
     */
    private void initRecyclerView(){
        Map<String,List<String>> map = (Map<String,List<String>>)Factory.getData(Const.N_DataCircleCancer);
        List<String> temp = map.get(CircleID);
        if(temp==null||temp.size()==0){
            v_rv_circle.setVisibility(View.GONE);
        }else {
            List<String> add = new ArrayList<>();
            add.add(CircleID);
            add.addAll(temp);
            v_rv_circle.setVisibility(View.VISIBLE);
            RecyclerView rv = (RecyclerView) headerView.findViewById(R.id.rv_circle);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rv.setLayoutManager(manager);
            rv.setItemAnimator(new DefaultItemAnimator());
            LogCustom.i("ZYS", "add:" + add.toString());
            RecyclerCircleAdapter mAdapter = new RecyclerCircleAdapter(this,add,this,0);
            rv.setAdapter(mAdapter);
        }
    }

    /***
     *
     * 获得关注的圈子列表
     * 圈子是否关注
     *
     */
    private void setFollowStatus() {
        try {
            List<String> focusCircles = UserinfoData.getFocusCircle(this);
            if (focusCircles!=null&&focusCircles.contains(CircleID)) {
                isfollow.setChecked(true);
            } else {
                isfollow.setChecked(false);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, TAG);
        }
    }


    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btn_back:
                    finish();
                    break;
                case R.id.iv_ft:
                    List<String> temp = UserinfoData.getFocusCircle(this);
                    if(temp == null){
                        startActivity(new Intent(this,ReleaseForumActivity.class));
                    }else {
                        if(temp.size()>0&&temp.contains(CircleID)){
                            temp = new ArrayList<>();
                            temp.add(CircleID);
                            startActivity(new Intent(this,ReleaseForumActivity.class)
                                    .putExtra(Const.DEFAULT_CIRCLE,(Serializable)temp));
                        }else {
                            Toast.makeText(this,"您尚未关注该圈子，不能在该圈子发帖，请您先关注！",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case R.id.iv_frush:
                    xRefreshView.scrollTo(0,0);
                    xRefreshView.startRefresh();
                    break;

            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, TAG);
        }
    }

    private String CircleID;
    private String followOrcancel;////1关注 2

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == Const.PGetForumList_bank){
            map.put(Contants.InfoID,UserinfoData.getInfoID(this));
            map.put(Contants.CircleID,CircleID);
            map.put("page",page+"");
            map.put("pagesize",pagesize+"");
            if (typeFlag){
                map.put("TypeID",typeID);
            }
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if(flag == Const.EGetForumList){
            if(typeFlag){
                args = new String[]{
                        Contants.InfoID,UserinfoData.getInfoID(this),
                        Contants.CircleID, CircleID,
                        "Typeid",typeID,
                        "page",page+"",
                        "pagesize",pagesize+""
                };
            }else{
                args = new String[]{
                        Contants.InfoID,UserinfoData.getInfoID(this),
                        Contants.CircleID, CircleID,
                        "page",page+"",
                        "pagesize",pagesize+""
                };
            }

        }else if(flag == Const.EFollowCircle){
            String type;
            if (Integer.valueOf(CircleID) < 10000) {
                type = "1";
            } else {
                type = "2";
            }
            args = new String[]{
                    Contants.InfoID,UserinfoData.getInfoID(this),
                    Contants.CircleID, CircleID,
                    Contants.followOrcancel, followOrcancel,
                    "type", type
            };
        }
        return HttpSecretUtils.getParamString(args);
    }

    private CircleImageView civ;
    @Override
    public void toActivity(Object t, int position) {
        try {
            if (position == Const.EGetForumList) {

            }
            if (position == Const.PGetForumList_bank){
                ForumBaseBean bean = (ForumBaseBean)t;
                if (Const.RESULT.equals(bean.getiResult())){
                    //圈子的简介
                    if(!topFlag){
                        String description = bean.getDescription();
                        if(!TextUtils.isEmpty(description)){
                            tv_description.setVisibility(View.VISIBLE);
                            tv_description.setText(description);
                        }
                        topFlag = true;
                    }
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
            }

            if (position == Const.EFollowCircle) {

                FollowBean bean = (FollowBean) t;
                if (Contants.OK_DATA.equals(bean.getIResult())) {
                    if ("1".equals(followOrcancel)) {
                        UserinfoData.addFocusCircle(this, CircleID);
                        Toast.makeText(this, "关注成功", Toast.LENGTH_SHORT).show();
                    } else {
                        UserinfoData.removeFocusCircle(this, CircleID);
                        Toast.makeText(this, "取消关注成功", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if("-2".equals(bean.getIResult())){
                        if ("1".equals(followOrcancel)) {
                            changeFlag  = false;
                            isfollow.setChecked(false);
                            changeFlag  = true;
                            Toast.makeText(this, "暂时无法关注该圈子", Toast.LENGTH_SHORT).show();

                        } else {
                            UserinfoData.removeFocusCircle(this, CircleID);
                            changeFlag  = false;
                            isfollow.setChecked(true);
                            changeFlag  = true;
                            Toast.makeText(this, "暂时无法取消关注该圈子", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, TAG);
        }
    }

    private boolean changeFlag = true;
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        try {
            if(changeFlag){
                if (isChecked) {//关注
                    followOrcancel = "1";
                } else {//取消关注
                    followOrcancel = "2";
                }
                folowCircle();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "circleactivity");
        }
    }

    /***
     * 发送关注圈子请求
     *
     */
    private void folowCircle() {
        try {
            getFactoryForFlag(Const.EFollowCircle);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "circleactivity");
        }
    }

    private boolean refresh = false;
    private boolean loading = false;



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

    @Override
    public void onDownPullRefresh() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(1000);
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                try {
                    LogCustom.i("ZYS", "下拉刷新");
                    refresh = true;
                    loading = false;
                    page = 0;
                    initData();
                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e,"onRefresh");
                }
            }
        }.execute(new Void[]{});
    }

    @Override
    public void onLoadingMore() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(1000);
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                try {
                    LogCustom.i(Const.TAG.ZY_OTHER,"加载更多");
                    refresh = false;
                    loading = true;
                    page++;
//                    btn.setSelected(false);
                    initData();
                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e,"onLoadMore");
                }
            }
        }.execute(new Void[]{});
    }

    private String typeID;
    private boolean typeFlag = false;



    /***
     * 分级栏目点击事件回调
     *
     * @param v
     * @param position
     */
    @Override
    public void OnRecyclerItemClick(View v, int position ,String typeID) {
        this.typeID = typeID;
        if(position==0){
            typeFlag = false;
        }else{
            typeFlag = true;
        }
        page = 0;
        initData();
    }

    @Override
    public void finish() {
        int from = getIntent().getIntExtra(Const.INTENT_FROM,0);
        if(from==Const.FM){
            afterFinish();
        }
        super.finish();
    }
}

package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.ForumAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.FollowBean;
import com.zeyuan.kyq.bean.ForumListBean;
import com.zeyuan.kyq.biz.forcallback.CancerZmInterface;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.fragment.CancerZmFragment;
import com.zeyuan.kyq.fragment.EmptyPageFragment;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CircleImageView;
import com.zeyuan.kyq.widget.CustomView.CustomRefreshListView;
import com.zeyuan.kyq.widget.CustomView.OnCustomRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 此页面已废弃~~
 *
 * 进入某个圈子
 * 获取某个圈子下的帖子列表
 * 进入这个activity 必须带上circleid 的参数
 * 才能拉数据
 */
public class CircleActivity extends BaseActivity implements View.OnClickListener, ViewDataListener, AdapterView.OnItemClickListener,
        CompoundButton.OnCheckedChangeListener ,HttpResponseInterface,OnCustomRefreshListener,CancerZmInterface
        ,CustomRefreshListView.onCustomSrcoll{
    private static final String TAG = "CircleActivity";
    private CustomRefreshListView listview;
    private ForumAdapter mForumAdapter;
    private List<ForumListBean.ForumnumEntity> data;
    private CheckBox isfollow;//是否关注
    private int page = 0;
    private int pagesize = 30;

    private ProgressBar bar;
    private int circleType = 0;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        try {
            CircleID = getIntent().getStringExtra(Contants.CircleID);//测试注释了
            if (TextUtils.isEmpty(CircleID)) {
                throw new RuntimeException("CircleActivity error entrance!");
            }
            if("7003".equals(CircleID)){
                initTitlebar(MapDataUtils.getCircleValues("7003"));
                circleType = 1;
            }else if("7007".equals(CircleID)){
                initTitlebar(MapDataUtils.getCircleValues("7007"));
                circleType = 2;
            }else{
                circleType = 0;
                initTitlebar(getString(R.string.froum_list));
            }
            data = new ArrayList();
            initData();
            initView();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, TAG);
        }
    }

    /***
     * 请求数据
     *
     */
    private void initData() {
        try {
            getFactoryForFlag(Const.EGetForumList);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, TAG);
        }
    }

    private void getFactoryForFlag(int flag){
        Factory.post(this, flag);
    }

    private TextView circleName;
    private TextView topicNum;//话题
    private TextView number;//人数
    private TextView tv_cancer;

    /***
     * 视图初始化
     *
     */
    public void initView() {
        try {
            if(circleType!=0){
                findViewById(R.id.rl_cancer_zm).setVisibility(View.VISIBLE);
                findViewById(R.id.rl_cancer_zm).setOnClickListener(this);
                if(circleType==1){
                    tv_cancer = (TextView)findViewById(R.id.tv_cancer_zm);
                    tv_cancer.setText("全部癌种");
                }else if(circleType==2){
                    tv_cancer = (TextView)findViewById(R.id.tv_cancer_zm);
                    tv_cancer.setText("全部");
                }
            }

            btn = (Button)findViewById(R.id.release_forum);
            isfollow = (CheckBox) findViewById(R.id.isfollow);
            setFollowStatus();
            topicNum = (TextView) findViewById(R.id.topic_number);
            number = (TextView) findViewById(R.id.number);
            circleName = (TextView) findViewById(R.id.circle_name).findViewById(R.id.title);
            ImageView back = (ImageView) findViewById(R.id.btn_back);
            back.setOnClickListener(this);
            listview = (CustomRefreshListView) findViewById(R.id.listview);
            listview.setOnRefreshListener(this);
            listview.setmSrcoll(this);
            mForumAdapter = new ForumAdapter(this, data);
            listview.setAdapter(mForumAdapter);
            listview.setOnItemClickListener(this);
            isfollow.setOnCheckedChangeListener(this);

            //新改版代码
            /*RecyclerView rv = (RecyclerView)findViewById(R.id.rv_circle);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rv.setLayoutManager(manager);
            rv.setItemAnimator(new DefaultItemAnimator());
            List<String> test = new ArrayList<>();
            test.add("测试");
            test.add("测试测试测试");
            test.add("测试测试");
            test.add("测试测试");
            test.add("测试测试");
            test.add("测试试");
            test.add("测试");
            test.add("测试测试测试测试测试");
            RecyclerCircleAdapter mAdapter = new RecyclerCircleAdapter(this,test,this);
            rv.setAdapter(mAdapter);*/

        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, TAG);
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

    private CancerZmFragment fragment;
    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btn_back:
                    finish();
                    break;
                case R.id.rl_cancer_zm:
                    try {
                        if(fragment==null){
                            fragment = CancerZmFragment.getInstance(this,Integer.valueOf(CircleID));
                        }
                        fragment.show(getFragmentManager(),CancerZmFragment.type);
                    }catch (Exception e){

                    }

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
        }else if(flag == Const.EGetForumList){

        }
        return HttpSecretUtils.getParamString(args);
    }

    private CircleImageView civ;
    @Override
    public void toActivity(Object t, int position) {
        try {
            if (position == Const.EGetForumList) {
                ForumListBean bean = (ForumListBean) t;
                LogUtil.i(TAG, bean.toString());
                if (Contants.OK_DATA.equals(bean.getIResult())) {
                    try {
                        circleName.setText(MapDataUtils.getCircleValues(CircleID));
                        if(circleType!=0){
                            if(civ==null){
                                civ = (CircleImageView)findViewById(R.id.avatar);
                            }
                            if(circleType==1){
                                civ.setImageResource(R.mipmap.linchuang_circle_img);
                            }else if(circleType==2){
                                civ.setImageResource(R.mipmap.circle_7007);
                            }
                        }
                        topicNum.setText(getString(R.string.forum_num) +""+ bean.getForumNum());
                        number.setText(getString(R.string.person_num) + "" + bean.getUserNum());
                        List<ForumListBean.ForumnumEntity> list = bean.getForumnum();

                        if ((list == null|| list.size() == 0)&&page==0){
                            try {
                                findViewById(R.id.listview).setVisibility(View.GONE);
                                setEmptyPageFragment(R.mipmap.no_froum_relust, "该圈子还没任何帖子哦\n" +
                                        "快来发个帖和大家交流", "去发帖", new EmptyPageFragment.EmptyClickListener() {
                                    @Override
                                    public void onEmptyClickListener() {
                                        startActivity(new Intent(CircleActivity.this, ReleaseForumActivity.class));
                                    }
                                }, R.id.fl);
                            }catch (Exception e){
                                ExceptionUtils.ExceptionToUM(e,this,"空白提示出错");
                            }
                        }else{
                            if(list!=null&&list.size()>0){
                                if(page==0){
                                    data = new ArrayList();
                                    data = MapDataUtils.getZhiDingList(list);
                                    mForumAdapter.update(data);
                                }else{
                                    data = MapDataUtils.getZhiDingList(list,data);
                                    mForumAdapter.update(data);
                                }
                                if(refresh){
                                    listview.hideHeaderView(CustomRefreshListView.SUCCEED, true);
                                }
                                if(loading){
                                    listview.hideFooterView(CustomRefreshListView.SUCCEED,true);
                                }
                            }else{
                                if(refresh){
                                    listview.hideHeaderView(CustomRefreshListView.FAIL, true);
                                }
                                if(loading){
                                    page--;
                                    listview.hideFooterView(CustomRefreshListView.LOADING_MAX,true);
                                }
                            }
                        }
                    }catch (Exception e){
                        ExceptionUtils.ExceptionToUM(e,this,"CircleActivity");
                    }
                }else {

                    if(refresh){
                        listview.hideHeaderView(CustomRefreshListView.FAIL, true);
                    }
                    if(loading){
                        page--;
                        listview.hideFooterView(CustomRefreshListView.FAIL,true);
                    }
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
        if(tag == Const.EGetForumList){
            findViewById(R.id.pd).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading(int tag) {
        if(tag == Const.EGetForumList){
            findViewById(R.id.pd).setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(int tag) {
        if(tag == Const.EGetForumList){
            findViewById(R.id.pd).setVisibility(View.GONE);
            if(tag == Const.EGetForumList){
                if(refresh){
                    listview.hideHeaderView(CustomRefreshListView.FAIL, true);
                }
                if(loading){
                    page--;
                    listview.hideFooterView(CustomRefreshListView.FAIL,true);
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            startActivity(new Intent(this, ForumDetailActivity.class)
                    .putExtra(Const.FORUM_ID, String.valueOf(id)));
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "circleactivity");
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
                    Log.i("ZYS","下拉刷新");
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
                    Log.i(Const.TAG.ZY_OTHER,"加载更多");
                    refresh = false;
                    loading = true;
                    page++;
                    initData();
                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e,"onLoadMore");
                }
            }
        }.execute(new Void[]{});
    }

    private String typeID;
    private boolean typeFlag = false;

    @Override
    public void chooseCallBack(String titles,String typeID, int tag) {
        this.typeID = typeID;
        if(tag==0){
            typeFlag = false;
        }else{
            typeFlag = true;
        }
        if(!TextUtils.isEmpty(titles)) tv_cancer.setText(titles);
        page = 0;
        getFactoryForFlag(Const.EGetForumList);
    }

    @Override
    public void forSrcollListener(boolean fit) {
        Log.i("ZYS","执行状态切换");
        btn.setSelected(fit);
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

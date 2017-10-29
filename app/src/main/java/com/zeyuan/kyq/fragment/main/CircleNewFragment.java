package com.zeyuan.kyq.fragment.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.ForumAdapter;
import com.zeyuan.kyq.adapter.RecyclerFollowAdapter;
import com.zeyuan.kyq.app.BaseZyFragment;
import com.zeyuan.kyq.bean.ForumListBean;
import com.zeyuan.kyq.bean.MyCircleBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.ForumDetailActivity;
import com.zeyuan.kyq.view.MoreCircleNewActivity;
import com.zeyuan.kyq.view.NewCircleActivity;
import com.zeyuan.kyq.view.ReleaseForumActivity;
import com.zeyuan.kyq.view.ViewDataListener;
import com.zeyuan.kyq.widget.CircleImageView;
import com.zeyuan.kyq.widget.CustomView.CustomRefreshListView;
import com.zeyuan.kyq.widget.CustomView.OnCustomRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/5.
 *
 *
 *
 * @author wwei
 */
public class CircleNewFragment extends BaseZyFragment implements AdapterView.OnItemClickListener, View.OnClickListener, ViewDataListener
        ,HttpResponseInterface,OnCustomRefreshListener,CustomRefreshListView.onCustomSrcoll {

    private static final String TAG = "CircleNewFragment";
    private RecyclerView mRecyclerView; //顶部关注的圈子
    private CustomRefreshListView xListView;//寻常的帖子的listview
    private ImageView addCircle;//头部的添加圈子
    private ForumAdapter forumAdapter;//寻常帖子的adapter
    private List<ForumListBean.ForumnumEntity> forumListDatas;//装帖子列表的数据
    private ImageView redpoint;
    private Button set_forum;
    private List<String> lists;
    private RecyclerFollowAdapter adapter;
    private CircleImageView avatar;
    private int page = 0;
    private int pagesize = 30;
    private ImageView iv_circle_add;
    private View v_rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new_circle, container, false);
        try {
            initView();
            setListener();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), TAG);
        }
        return rootView;
    }

    /***
     * 处理数据
     */
    public void initData() {
        try {
            getFactoryForFlag(Const.EGetMycircle);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,getActivity(),TAG);
        }
    }

    private void getFactoryForFlag(int flag){
        Factory.post(this, flag);
    }

    /***
     * 设置监听事件
     */
    private void setListener() {
        try {
            addCircle.setOnClickListener(this);
            set_forum.setOnClickListener(this);
            iv_circle_add.setOnClickListener(this);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,getActivity(),TAG);
        }
    }

    /***
     * 初始化控件
     */
    private void initView() {
        try {

            v_rv = findViewById(R.id.v_rv);

            mRecyclerView = (RecyclerView) findViewById(R.id.rv);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerView.setLayoutManager(layoutManager);

            iv_circle_add = (ImageView)findViewById(R.id.iv_circle_add);

            addCircle = (ImageView) findViewById(R.id.add_circle);
            set_forum = (Button) findViewById(R.id.release_forum);
            set_forum.setOnClickListener(this);
            xListView = (CustomRefreshListView) findViewById(R.id.listview);
            xListView.setTimeMills(System.currentTimeMillis() / 1000 + "");
            xListView.setOnRefreshListener(this);
            xListView.setOnItemClickListener(this);
            xListView.setmSrcoll(this);
            forumListDatas = new ArrayList<>();
            forumAdapter = new ForumAdapter(getActivity(), forumListDatas);
            xListView.setAdapter(forumAdapter);

        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,getActivity(),TAG);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            startActivity(new Intent(getActivity(), ForumDetailActivity.class)
                    .putExtra(Const.FORUM_ID, String.valueOf(id)));
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,getActivity(),TAG);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.release_forum://这个是点击发布帖子
                List<String> temp = new ArrayList<>();
                if(defaultList!=null&&defaultList.size()>0){
                    for(int i=0;i<defaultList.size();i++){
                        List<String> temp1 = defaultList.get(i);
                        if(temp1!=null&&temp1.size()>0){
                            for(int j=0;j<temp1.size();j++){
                                temp.add(temp1.get(j));
                            }
                        }
                    }
                }
                startActivity(new Intent(getActivity(), ReleaseForumActivity.class)
                        .putExtra(Const.DEFAULT_CIRCLE,(Serializable)temp));
                break;

            case R.id.add_circle:
                Factory.onEvent(getActivity(),Const.EVENT_MoreCircle,null,null);
                startActivityForResult(new Intent(getActivity(), MoreCircleNewActivity.class), 0);
                break;

            case R.id.iv_circle_add:
                startActivity(new Intent(getActivity(), MoreCircleNewActivity.class));
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            Log.i(Const.TAG.ZY_VIEW_LIFE, "Circle onResume");
            page = 0 ;
            getFactoryForFlag(Const.EGetMycircle);
        }catch (Exception e){
            Log.i(Const.TAG.ZY_VIEW_LIFE, "Circle onResume Error");
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(Const.TAG.ZY_VIEW_LIFE, "Circle onPause");
    }



    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if (flag == Const.EGetMycircle) {
            String temp = "";
            String StepID = UserinfoData.getStepID(context);
            String CancerID = UserinfoData.getCancerID(context);
            String CureConfID = MapDataUtils.getAllCureconfID(StepID);
            String ProvinceID;
            String CityID = UserinfoData.getCityID(context);
            temp = Contants.InfoID +",,,"+ UserinfoData.getInfoID(getActivity());
            if(Const.NO_STEP.equals(UserinfoData.getIsHaveStep(getActivity()))){
                temp += ",,,"+Const.ISHAVESTEP+",,,"+"0";
            }else {
                if (!TextUtils.isEmpty(StepID)) {
                    temp += ",,,"+Contants.StepID+",,,"+StepID;
                }
                if (!TextUtils.isEmpty(CureConfID)) {
                    temp += ",,,"+Contants.CureConfID+",,,"+CureConfID;
                }else{
                    temp += ",,,"+Contants.CureConfID+",,,"+"0";
                }
                temp += ",,,"+Const.ISHAVESTEP+",,,"+"1";
            }

            if (!TextUtils.isEmpty(CancerID)) {
                temp += ",,,"+Contants.CancerID+",,,"+CancerID;
            }
            if("0".equals(CityID)) {
                ProvinceID = "0";
                temp += ",,,"+Contants.CityID+",,,"+CityID;
                temp += ",,,"+Contants.ProvinceID+",,,"+ProvinceID;
            }else {
                ProvinceID = CityID.substring(0,2)+"0000";
                temp += ",,,"+Contants.CityID+",,,"+CityID;
                temp += ",,,"+Contants.ProvinceID+",,,"+ProvinceID;
            }
            UserinfoData.saveProvinceID(getActivity(),ProvinceID);


            temp += ",,,page,,,"+page;
            temp += ",,,pagesize,,,"+pagesize;
            args = new String[]{};
            args = temp.split(",,,");
//            Log.i("ZYS", Arrays.toString(args));
        }

        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object t, int position) {
        if (position == Const.EGetMycircle) {
            try {
                MyCircleBean bean = (MyCircleBean)t;
                if(v_rv.getVisibility()!=View.VISIBLE){
                    v_rv.setVisibility(View.VISIBLE);
                }
                if (Const.RESULT.equals(bean.getIResult())){
                    bindView((MyCircleBean) t);//第一次加载
                }else{
                    if(refresh){
                        xListView.hideHeaderView(CustomRefreshListView.FAIL,true);
                    }
                    if(loading){
                        page--;
                        xListView.hideFooterView(CustomRefreshListView.LOADING_MAX,true);
                    }
                }
            }catch (Exception e){
                ExceptionUtils.ExceptionSend(e,TAG);
            }

        }
    }

    private void bindListView(MyCircleBean bean) {
        try {
            List list = bean.getAllPostNum();
            if (list != null && list.size() > 0) {
                if(page==0){
                    forumListDatas = new ArrayList<>();
                    forumListDatas = MapDataUtils.getZhiDingList(list);
                }else{
                    forumListDatas = MapDataUtils.getZhiDingList(list,forumListDatas);
                }
                forumAdapter.update(forumListDatas);//这儿是列表
                if(refresh){
                    xListView.hideHeaderView(CustomRefreshListView.SUCCEED,true);
                }
                if(loading){
                    xListView.hideFooterView(CustomRefreshListView.SUCCEED,true);
                }
            }else{
                if(refresh){
                    xListView.hideHeaderView(CustomRefreshListView.SUCCEED,true);
                }
                if(loading){
                    page--;
                    xListView.hideFooterView(CustomRefreshListView.LOADING_MAX,true);
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,getActivity(),TAG);
        }
    }

    private List<List<String>> defaultList;
    private void bindView(MyCircleBean t) {
        try {
            MyCircleBean bean = t;
            LogUtil.i(TAG, bean.toString());
            if (Contants.OK_DATA.equals(bean.getIResult())) {
                bindListView(bean);
                defaultList = bean.getDefaultCircle();
                lists = bean.getFollowCircleNum();
                if (lists != null && lists.size() >= 0) {
                    if (lists.size() > 0) {//存圈子
                        List<String> list1 = new ArrayList<>();
                        for (String entity : lists) {
                            list1.add(entity);
                        }
                        UserinfoData.saveFocusCirlce(context, list1);
                    }
                    adapter = new RecyclerFollowAdapter(getActivity(), lists, new RecyclerFollowAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemFollowClick(String id) {
                            toCircle(id);
                        }
                    });
                    mRecyclerView.setAdapter(adapter);
                }
            } else {
                if(refresh){
                    xListView.hideHeaderView(CustomRefreshListView.FAIL,true);
                }
                if(loading){
                    page--;
                    xListView.hideFooterView(CustomRefreshListView.FAIL,true);
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,getActivity(),TAG);
        }
    }

    private void toCircle(String circleID) {
        try {
            Intent intent = new Intent(context, NewCircleActivity.class);
            intent.putExtra(Contants.CircleID, circleID);
            startActivity(intent);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,getActivity(),TAG);
        }
    }

    @Override
    public void showLoading(int tag) {
        if(tag == Const.EGetMycircle){
            findViewById(R.id.pd).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading(int tag) {
        if (tag == Const.EGetMycircle){
            findViewById(R.id.pd).setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(int tag) {
        try {
            if (tag == Const.EGetMycircle){
                findViewById(R.id.pd).setVisibility(View.GONE);
                if(refresh){
                    xListView.hideHeaderView(CustomRefreshListView.FAIL,true);
                }
                if(loading){
                    page--;
                    xListView.hideFooterView(CustomRefreshListView.FAIL,true);
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,getActivity(),TAG);
        }

    }

    private boolean refresh = false;
    private boolean loading = false;

    protected void showAvatar() {
        try {
            String imageUrl = UserinfoData.getAvatarUrl(getActivity());
            if (!TextUtils.isEmpty(imageUrl)) {
                Glide.with(context).load(imageUrl).signature(new IntegerVersionSignature(1)).error(R.mipmap.default_avatar).into(avatar);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"showAvatar");
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
                    Log.i("ZYS","加载更多");
                    refresh = false;
                    loading = true;
                    page++;
//                    set_forum.setSelected(false);
                    initData();
                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e,"onLoadMore");
                }
            }
        }.execute(new Void[]{});
    }

    @Override
    public void forSrcollListener(boolean fit) {
            try {
                set_forum.setSelected(fit);
            }catch (Exception e){
                ExceptionUtils.ExceptionToUM(e,getActivity(),"Srcoll");
            }
    }
}

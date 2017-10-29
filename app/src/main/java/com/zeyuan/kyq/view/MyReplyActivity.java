package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.MyReplyAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.MyReplyListBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.fragment.EmptyPageFragment;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CustomView.CustomRefreshListView;
import com.zeyuan.kyq.widget.CustomView.OnCustomRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/6.
 * 我的回复列表
 */
public class MyReplyActivity extends BaseActivity implements AdapterView.OnItemClickListener, ViewDataListener ,
        HttpResponseInterface,OnCustomRefreshListener{
    private static final String TAG = "MyReplyActivity";
    private MyReplyAdapter adapter;
    private CustomRefreshListView listview;
    private List<MyReplyListBean.ReplyNumEntity> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myreply);
        try {
            try {
                UserinfoData.saveRedPointShow(this, false);
            }catch (Exception e){
                ExceptionUtils.ExceptionToUM(e, this, "MyReplyActivity");
            }
            initWhiteTitle(getString(R.string.comment_reply));
            data = new ArrayList();
            initView();
            initData();

        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "MyReplyActivity");
        }
    }

    private void initData() {
        try {
            getFactoryForFlag(Const.EGetMyReplyList);
//            Factory.postPhp(this, Const.PUserCommentInfo);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "MyReplyActivity");
        }
    }



    private void initView() {
        try {
            listview = (CustomRefreshListView) findViewById(R.id.listview);
            listview.setOnItemClickListener(this);
            listview.setOnRefreshListener(this);
            adapter = new MyReplyAdapter(this, data);
            listview.setAdapter(adapter);

        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "MyReplyActivity");
        }
    }

    private void getFactoryForFlag(int flag){
        Factory.post(this, flag);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
                Intent intent = new Intent(this, ForumDetailActivity.class);
                intent.putExtra(Const.FORUM_ID, String.valueOf(id));
                startActivity(intent);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "MyReplyActivity");
        }
    }

    private int page = 0;
    private int pagesize = 30;
    @Override
    public Map getParamInfo(int tag) {
        Map<String,String> map = new HashMap<>();
        if (tag == Const.PUserCommentInfo){
            map.put(Contants.InfoID,UserinfoData.getInfoID(this));
        }
        return map;

    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if(flag == Const.EGetMyReplyList){
            args = new String[]{
                    Contants.InfoID, Const.InfoID,
                    Contants.page, page+"",
                    "pagesize",pagesize+""
            };
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object t, int tag) {
        try {
            if (tag == Const.EGetMyReplyList) {
                MyReplyListBean bean = (MyReplyListBean) t;
                List<MyReplyListBean.ReplyNumEntity> list;
                if(listview.getVisibility()!=View.VISIBLE){
                    listview.setVisibility(View.VISIBLE);
                }
                if(Const.RESULT.equals(bean.getIResult())){
                    list = bean.getReplyNum();
                    if (list != null && list.size() > 0) {
                        if (page==0){
                            data = new ArrayList<>();
                        }
                        data.addAll(list);
                        adapter.update(data);
                        if(refresh){
                            listview.hideHeaderView(CustomRefreshListView.SUCCEED,true);
                        }
                        if(loading){
                            listview.hideFooterView(CustomRefreshListView.SUCCEED, true);
                        }
                    }else {//显示空白提醒
                        if (page==0){
                            if(refresh){
                                listview.hideHeaderView(CustomRefreshListView.FAIL,false);
                            }
                            listview.setVisibility(View.GONE);
                            setEmptyPageFragment(R.mipmap.no_reply_relust, "还没有评论，快去跟帖吧~", "去圈子", new EmptyPageFragment.EmptyClickListener() {
                                @Override
                                public void onEmptyClickListener() {
                                    MyReplyActivity.this.finish();
                                }
                            },R.id.fl);
                        }else {
                            if(loading){
                                page--;
                                listview.hideFooterView(CustomRefreshListView.LOADING_MAX,true);
                            }
                        }
                    }
                }else {
                    if(refresh){
                        listview.hideHeaderView(CustomRefreshListView.FAIL,true);
                    }
                    if(loading){
                        page--;
                        listview.hideFooterView(CustomRefreshListView.FAIL, true);
                    }

                }
            }else if (tag == Const.PUserCommentInfo){

            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "MyReplyActivity");
        }
    }

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {
        if(tag == Const.EGetMyReplyList){
            findViewById(R.id.pd).setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(int tag) {
        if(tag == Const.EGetMyReplyList){
            findViewById(R.id.pd).setVisibility(View.GONE);
            if(page==0){
                if(refresh){
                    listview.hideHeaderView(CustomRefreshListView.FAIL,true);
                }
            }else {
                if(loading){
                    page--;
                    listview.hideFooterView(CustomRefreshListView.FAIL, true);
                }
            }
        }
    }

    private boolean refresh = false;
    private boolean loading = false;
    @Override
    public void onDownPullRefresh() {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(1000);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    Log.i(Const.TAG.ZY_OTHER, "下拉刷新");
                    refresh = true;
                    loading = false;
                    page = 0;
                    initData();
                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e,"onDownPullRefresh");
                }
            }
        }.execute(new Void[]{});
    }

    @Override
    public void onLoadingMore() {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(1000);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    Log.i(Const.TAG.ZY_OTHER, "加载更多");
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

    @Override
    public void finish() {
        int form = getIntent().getIntExtra(Const.INTENT_FROM,0);
        if(form == Const.FM){
            afterFinish();
        }
        super.finish();
    }
}

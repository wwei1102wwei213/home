package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.HeadLineAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.HeadLineBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CustomView.CustomRefreshListView;
import com.zeyuan.kyq.widget.CustomView.OnCustomRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/11.
 *
 * 肿瘤头条页面
 *
 * @author wwei
 */
public class HeadLineActivity extends BaseActivity implements AdapterView.OnItemClickListener,HttpResponseInterface,
        OnCustomRefreshListener{

    private List<HeadLineBean.HeadListBean> list;
    private int page = 0;
    private HeadLineAdapter adapter;
    private CustomRefreshListView lv;
    private String catid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headline);
        try {
            Intent intent = getIntent();
            String tagUrl = intent.getStringExtra(Const.HEAD_LIST_TAG_URL);
            String tempTitle = intent.getStringExtra(Const.HEAD_LIST_INFO_TEXT);
            if(!TextUtils.isEmpty(tagUrl)&&!TextUtils.isEmpty(tempTitle)){
                catid = tagUrl;
                initOtherTitle(tempTitle);
            }else {
                initOtherTitle(getString(R.string.title_headline));
            }
            initview();
            initdata();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"HeadLineActivity");
        }
    }

    /***
     *
     * 设置视图
     *
     */
    private void initview(){
        try {
            list = new ArrayList<>();
            lv = (CustomRefreshListView)findViewById(R.id.lv_pull_headline);
            lv.setOnRefreshListener(this);
            adapter = new HeadLineAdapter(this,list);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(this);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"HeadLineActivity");
        }
    }

    /***
     *
     * 设置数据
     *
     */
    private void initdata(){
        try {
            Factory.post(this, Const.EGetCancerHeaderList);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"HeadLineActivity");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {

            startActivity(new Intent(this, ShowDiscuzActivity.class)
                    .putExtra(Const.INTENT_SHOW_DISCUZ_ID, String.valueOf(id)));

        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"HeadLineActivity");
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        return null;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if(flag == Const.EGetCancerHeaderList){
            if(TextUtils.isEmpty(catid)){
                args = new String[]{
                        Contants.InfoID, UserinfoData.getInfoID(this),
                        "pagesize","20",
                        "page","" + page
                };
            }else{
                args = new String[]{
                        Contants.InfoID, UserinfoData.getInfoID(this),
                        "pagesize","20",
                        "page","" + page,
                        "catid",catid
                };
            }
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object response, int flag) {
        try {
            if(flag == Const.EGetCancerHeaderList){
                HeadLineBean t = (HeadLineBean)response;
                if(Const.RESULT.equals(t.getiResult())){
                    List<HeadLineBean.HeadListBean> temp = t.getHeadList();
                    if(page==0){
                        list = new ArrayList<>();
                        if(temp!=null&&temp.size()!=0){
                            list.addAll(temp);
                        }
                        adapter.updata(list);
                        if(refresh){
                            lv.hideHeaderView(CustomRefreshListView.SUCCEED, true);
                        }
                        if(loading){
                            lv.hideFooterView(CustomRefreshListView.LOADING_MAX, true);
                        }
                    }else {
                        if(temp!=null&&temp.size()!=0){
                            list.addAll(temp);
                            adapter.updata(list);
                            if(refresh){
                                lv.hideHeaderView(CustomRefreshListView.SUCCEED, true);
                            }
                            if(loading){
                                lv.hideFooterView(CustomRefreshListView.SUCCEED, true);
                            }
                        }else{
                            if(refresh){
                                lv.hideHeaderView(CustomRefreshListView.FAIL, true);
                            }
                            if(loading){
                                page--;
                                lv.hideFooterView(CustomRefreshListView.LOADING_MAX, true);
                            }
                        }
                    }
                }else {
                    if(refresh){
                        lv.hideHeaderView(CustomRefreshListView.FAIL, true);
                    }
                    if(loading){
                        page--;
                        lv.hideFooterView(CustomRefreshListView.FAIL, true);
                    }
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"toActivity");
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
        if(flag == Const.EGetCancerHeaderList){
            try {
                if(refresh){
                    lv.hideHeaderView(CustomRefreshListView.FAIL, true);
                }
                if(loading){
                    page--;
                    lv.hideFooterView(CustomRefreshListView.FAIL, true);
                }
            }catch(Exception e){
                ExceptionUtils.ExceptionToUM(e, this, "HeadLineActivity");
            }
        }
    }

   /* @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        try {
            layout.refreshFinish(PullToRefreshLayout.SUCCEED,false);
        }catch(Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "HeadLineActivity");
        }
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        try {
            if(!indexMax){
                index++;
                initdata();
            }else{
                layout.loadmoreFinish(PullToRefreshLayout.LOADING_MAX,true);
            }
        }catch(Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "HeadLineActivity");
        }
    }*/

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
                    Log.i("ZYS", "下拉刷新");
                    refresh = true;
                    loading = false;
                    page = 0;
                    initdata();
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
                    initdata();
                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e,"onLoadMore");
                }
            }
        }.execute(new Void[]{});
    }
}

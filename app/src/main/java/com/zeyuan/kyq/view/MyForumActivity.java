package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.ForumMyAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.ForumListBean;
import com.zeyuan.kyq.bean.MyForumReleaseBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.fragment.EmptyPageFragment;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.MyListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的帖子
 *
 */
public class MyForumActivity extends BaseActivity implements View.OnClickListener, ViewDataListener,
        AdapterView.OnItemClickListener,HttpResponseInterface {

    private static final String TAG = "MyForumActivity";
    private ForumMyAdapter adapter;
    private MyListView mListView;
    private List<ForumListBean.ForumnumEntity> datas;//这个是数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_my_forum);
        try {
            datas = new ArrayList<>();
            initView();
            initWhiteTitle(getString(R.string.my_forum));
            initData();
            setListener();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "MyForumActivity");
        }
    }

    private void initData() {
        try {
            getFactoryForFlag(Const.EGetMyForum);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "MyForumActivity");
        }
    }


    private void getFactoryForFlag(int flag){
        Factory.post(this,flag);
    }

    private void setListener() {    }


    private void initView() {
        try {
            mListView = (MyListView) findViewById(R.id.listview);
            datas = new ArrayList<>();
            adapter = new ForumMyAdapter(this, datas);
            adapter.setTop(false);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(this);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "MyForumActivity");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == 0) {//这个是我发布的帖子
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if(flag == Const.EGetMyForum){
            args = new String[]{Contants.InfoID,UserinfoData.getInfoID(this)};
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object t, int position) {
        try {
            if (position == Const.EGetMyForum) {//这个是我发布的帖子
                Log.i("ZYS","ok");
                MyForumReleaseBean bean = (MyForumReleaseBean) t;
                if (Contants.OK_DATA.equals(bean.getIResult())) {
                    mListView.setVisibility(View.VISIBLE);
                    List list = bean.getForumNum();
                    if (list != null && list.size() > 0) {
                        datas.addAll(list);
                        adapter.update(datas);
                    } else {
                        mListView.setVisibility(View.GONE);
                        setEmptyPageFragment(R.mipmap.no_froum_relust, "还没有帖子，去发一篇吧~", "去发帖",
                                new EmptyPageFragment.EmptyClickListener() {
                                    @Override
                                    public void onEmptyClickListener() {
                                        startActivity(new Intent(MyForumActivity.this,ReleaseForumActivity.class));
                                    }
                                },R.id.fl);
                    }
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "MyForumActivity");
        }
    }

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {
        if(tag == Const.EGetMyForum){
            findViewById(R.id.pd).setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(int tag) {
        if(tag == Const.EGetMyForum){
            findViewById(R.id.pd).setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Intent intent = new Intent(this, ForumDetailActivity.class);
            intent.putExtra(Const.FORUM_ID, adapter.getItem(position).getIndex());//帖子id
            intent.putExtra(Const.AUTHORID,adapter.getItem(position).getOwnerID());//作者id
            startActivity(intent);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "MyForumActivity");
        }
    }
}

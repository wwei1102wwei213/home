package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.zeyuan.kyq.Entity.FavBaseEntity;
import com.zeyuan.kyq.Entity.FavEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.MyFavAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.fragment.EmptyPageFragment;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.OtherUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CustomView.OnCustomRefreshListener;
import com.zeyuan.kyq.widget.MyListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的收藏
 */
public class MyFlwForumActivity extends BaseActivity implements ViewDataListener, AdapterView.OnItemClickListener,
        HttpResponseInterface, OnCustomRefreshListener, MyFavAdapter.onImgClickCallback {

    private static final String TAG = "MyFlwForumActivity";
    private MyListView mListView;
    private MyFavAdapter adapter;
    private List<FavEntity> data;
    private int page = 0;
    private int pagesize = 30;
    private String InfoCenterID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_my_forum);
        try {
            InfoCenterID = getIntent().getStringExtra(Const.InfoCenterID);
            if (TextUtils.isEmpty(InfoCenterID)) {
                InfoCenterID = UserinfoData.getInfoID(this);
                initWhiteTitle(getString(R.string.my_flw));
            } else {
                if (InfoCenterID.equals(UserinfoData.getInfoID(this))) {
                    initWhiteTitle(getString(R.string.my_flw));
                } else {
                    initWhiteTitle(getString(R.string.user_flw));
                }
            }
            initView();
            initData();
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "MyFlwForumActivity");
        }
    }

    private void initView() {
        try {
            mListView = (MyListView) findViewById(R.id.listview);
            data = new ArrayList<>();
            adapter = new MyFavAdapter(this, data, this);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(this);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "MyFlwForumActivity");
        }
    }

    private void initData() {
        try {
            Factory.postPhp(this, Const.PGetUserFavInfo);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "MyFlwForumActivity");
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == Const.PGetUserFavInfo) {
            map.put(Contants.InfoID, InfoCenterID);
            map.put("page", page + "");
            map.put("pagesize", pagesize + "");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if (flag == Const.EGetMyFavor) {
            if (TextUtils.isEmpty(InfoCenterID)) {
                args = new String[]{
                        Contants.InfoID, Const.InfoID,
                        "page", page + "",
                        "pagesize", pagesize + ""
                };
            } else {
                args = new String[]{
                        Contants.InfoID, InfoCenterID,
                        "page", page + "",
                        "pagesize", pagesize + ""
                };
            }

        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object t, int tag) {
        try {
            if (tag == Const.PGetUserFavInfo) {
                FavBaseEntity bean = (FavBaseEntity) t;
                mListView.setVisibility(View.VISIBLE);
                if (Contants.RESULT.equals(bean.getiResult())) {
                    List<FavEntity> list = bean.getData();
                    if (list != null && list.size() > 0) {
                        data = new ArrayList<>();
                        data.addAll(list);
                        adapter.update(data);
                    } else {

                        if (UserinfoData.getInfoID(this).equals(InfoCenterID)) {
                            mListView.setVisibility(View.GONE);
                            setEmptyPageFragment(R.mipmap.no_keep_relust, "还没有收藏内容，去圈子逛逛吧~", "去圈子",
                                    new EmptyPageFragment.EmptyClickListener() {
                                        @Override
                                        public void onEmptyClickListener() {
                                            MyFlwForumActivity.this.finish();
                                        }
                                    }, R.id.fl);
                        } else {

                        }
                    }
                } else {
                    showToast("请求错误\n错误码:" + bean.getiResult());
                }
            }

        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "MyFlwForumActivity");
        }
    }

    @Override
    public void showLoading(int tag) {
        if (tag == Const.PGetUserFavInfo) {
            findViewById(R.id.pd).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading(int tag) {
        if (tag == Const.PGetUserFavInfo) {
            findViewById(R.id.pd).setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(int tag) {
        Toast.makeText(this, "网络请求失败", Toast.LENGTH_SHORT).show();
        if (tag == Const.PGetUserFavInfo) {
            findViewById(R.id.pd).setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            String tempID = adapter.getItem(position).getID() + "";
            int type = adapter.getItem(position).getType();
            if (!OtherUtils.isEmpty(tempID)) {
                if (type == 1) {
                    startActivity(new Intent(this, ArticleDetailActivity.class)
                            .putExtra(Const.INTENT_ARTICLE_ID, tempID));
                } else if (type == 2) {
                    startActivity(new Intent(this, ForumDetailActivity.class)
                            .putExtra(Const.FORUM_ID, tempID)
                            .putExtra(Const.AUTHORID, adapter.getItem(position).getAuthorID()));
                }
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "MyFlwForumActivity");
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
            protected void onPostExecute(Void aVoid) {
                try {
                    Log.i(Const.TAG.ZY_OTHER, "下拉刷新");
                    refresh = true;
                    loading = false;
                    page = 0;
                    initData();
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "onDownPullRefresh");
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
            protected void onPostExecute(Void aVoid) {
                try {
                    Log.i(Const.TAG.ZY_OTHER, "加载更多");
                    refresh = false;
                    loading = true;
                    page++;
                    initData();
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "onLoadMore");
                }
            }
        }.execute(new Void[]{});
    }

    @Override
    public void ImgClickCallback(List<String> images, int pos) {
        if (images != null && images.size() > 0) {
            startActivity(new Intent(this, ShowPhotoActivity.class)
                    .putExtra("list", (Serializable) images)
                    .putExtra("position", pos));
        }
    }

    @Override
    public void ToInfoCenterCallback(String index) {
        if (!OtherUtils.isEmpty(index)) {
            startActivity(new Intent(this, InfoCenterActivity.class)
                    .putExtra(Const.InfoCenterID, index));
        }
    }
}

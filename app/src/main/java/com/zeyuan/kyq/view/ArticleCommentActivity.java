package com.zeyuan.kyq.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.zeyuan.kyq.Entity.ArticleBaseEntity;
import com.zeyuan.kyq.Entity.ArticleCommentEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.CommentRecyclerAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.manager.IntegrationManager;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.OtherUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CustomView.OnCustomRefreshListener;
import com.zeyuan.kyq.widget.IntegrationPopupWindow;
import com.zeyuan.kyq.widget.MyLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/11.
 * <p>
 * 文章评论页面
 *
 * @author wwei
 */
public class ArticleCommentActivity extends BaseActivity implements View.OnClickListener, HttpResponseInterface,
        CommentRecyclerAdapter.OnSonClickListener, OnCustomRefreshListener, MyLayout.OnSoftKeyboardListener {

    private String aid = "";
    private String author = "";
    private String Cid = "";
    private boolean liking = false;
    private List<ArticleCommentEntity> datas;
    private int pos = -1;
    private int page = 0;
    private int pagesize = 20;
    //编辑框
    private EditText et;
    //外部控件
    private MyLayout layout;
    //发送按钮
    private TextView tv_send;
    private View v_edit;
    private View v_cancel;
    private View v_half;
    private TextView tv_edit_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_comment);
        try {
            aid = getIntent().getStringExtra(Const.INTENT_ARTICLE_ID);
            author = getIntent().getStringExtra(Const.INTENT_AUTHOR_ID);
            if (TextUtils.isEmpty(aid)) throw new RuntimeException("aid is empty!");
            initView();
            initData();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "ArticleCommentActivity onCreate");
        }
    }


    private XRefreshView xRefreshView;
    private RecyclerView recyclerView;
    private CommentRecyclerAdapter adapter;
    private LinearLayoutManager layoutManager;

    private void initView() {
        try {
            //返回设置
            findViewById(R.id.iv_back).setOnClickListener(this);

            //评论列表设置
            xRefreshView = (XRefreshView) findViewById(R.id.xrv);
            recyclerView = (RecyclerView) findViewById(R.id.rv);
            recyclerView.setHasFixedSize(true);
            datas = new ArrayList<>();
            adapter = new CommentRecyclerAdapter(this, datas, author, this);
            layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            //设置刷新完成以后，headerview固定的时间
            xRefreshView.setPinnedTime(1000);
            xRefreshView.setPullLoadEnable(true);
            xRefreshView.setMoveForHorizontal(true);
            adapter.setCustomLoadMoreView(new XRefreshViewFooter(this));
            xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            page = 0;
                            refresh = true;
                            initData();
                        }
                    }, 500);
                }

                @Override
                public void onLoadMore(boolean isSilence) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            page++;
                            loading = true;
                            initData();

                        }
                    }, 1000);
                }
            });

            //评论区域设置
            tv_send = (TextView) findViewById(R.id.send_message);
            tv_send.setOnClickListener(this);
            layout = (MyLayout) findViewById(R.id.my_layout);
            layout.setOnSoftKeyboardListener(this);
            et = (EditText) findViewById(R.id.edit_txt);
            v_edit = findViewById(R.id.v_edit);
            v_cancel = findViewById(R.id.btn_no_dialog);
            v_cancel.setOnClickListener(this);
            v_half = findViewById(R.id.v_half);
            v_half.setOnClickListener(this);
            tv_edit_title = (TextView) findViewById(R.id.tv_edit_title);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "initView");
        }

    }

    private void initData() {
        Factory.postPhp(this, Const.PGetPorCommentList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.send_message:
                try {
                    content = et.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        showToast(getString(R.string.empty_reply));
                        return;
                    }
                    tv_send.setClickable(false);
                    Factory.postPhp(this, Const.PAddPorComment);
                } catch (Exception e) {
                    ExceptionUtils.ExceptionToUM(e, this, "ForumDetailActivity");
                }
                break;
            case R.id.v_half:
            case R.id.btn_no_dialog:
                try {
                    hideKeyboard(getCurrentFocus().getWindowToken());
                    onHidden();
                } catch (Exception e) {

                }
                break;
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == Const.PGetPorCommentList) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
            map.put("aid", aid);
            map.put("page", "" + page);
            map.put("pagesize", pagesize + "");
        } else if (tag == Const.PGiveLike) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
            map.put("TypeID", "2");
            map.put("ContentID", Cid);
        } else if (tag == Const.PAddPorComment) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
            map.put(Contants.InfoName, UserinfoData.getInfoname(this));
            map.put("ArtID", aid);
            map.put("MessAge", content);
            if (GroipTag == 1) {
                map.put("ToUserID", ToUserID);
                map.put("ToUserName", ToUserName);
            }
            map.put("GroupID", GroupID);
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if (flag == Const.EFavorForum) {

        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PGetPorCommentList) {
            try {
                ArticleBaseEntity entity = (ArticleBaseEntity) response;
                if (Const.RESULT.equals(entity.getiResult())) {
                    List<ArticleCommentEntity> list = entity.getData();
                    if (list != null && list.size() > 0) {
                        if (page == 0) {
                            datas = new ArrayList<>();
                        }
                        datas.addAll(list);
                        adapter.update(datas);
                        overLoading(0);
                    } else {
                        overLoading(2);
                    }
                } else {
                    overLoading(1);
                    showToast("获取评论列表失败");
                }
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "");
            }

        } else if (flag == Const.PGiveLike) {
            PhpUserInfoBean bean = (PhpUserInfoBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                showToast("点赞成功");
                initData();
            } else {
                showToast("点赞失败");
            }
            liking = false;
        } else if (flag == Const.PAddPorComment) {
            PhpUserInfoBean bean = (PhpUserInfoBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                content = "";
                et.setText("");
                //清空草稿数据
                try {
                    String temp = UserinfoData.getDtaftArticleArray().get(Integer.valueOf(aid));
                    if (!TextUtils.isEmpty(temp))
                        UserinfoData.getDtaftArticleArray().delete(Integer.valueOf(aid));
                } catch (Exception e) {

                }
                showToast("回复成功");//收起输入法 刷新帖子
                try {
                    hideKeyboard(getCurrentFocus().getWindowToken());
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "回复成功");
                }
                initData();

                String jfString;
                if (!TextUtils.isEmpty(bean.getJf()) && !bean.getJf().equals("0")) {
                    jfString = "评论成功 +" + bean.getJf() + "积分";
                    IntegrationManager.getInstance().addIntegration(bean.getJf());
                } else {
                    jfString = "评论成功 已达每日积分上限";
                }
                IntegrationPopupWindow integrationPopupWindow = new IntegrationPopupWindow(ArticleCommentActivity.this, jfString);
                integrationPopupWindow.showPopupWindow(ArticleCommentActivity.this);

            } else {

            }
            tv_send.setClickable(true);
        }
    }

    @Override
    public void showLoading(int flag) {
        findViewById(R.id.pd).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading(int flag) {
        findViewById(R.id.pd).setVisibility(View.GONE);
    }

    @Override
    public void showError(int flag) {
        showToast("网络请求失败");
        if (flag == Const.PGiveLike) {
            liking = false;
        } else if (flag == Const.PGetPorCommentList) {
            overLoading(1);
        } else if (flag == Const.PAddPorComment) {
            tv_send.setClickable(true);
        }
    }

    @Override
    public void toItemInfoCenter(ArticleCommentEntity entity, int tag) {
        if (!TextUtils.isEmpty(entity.getInfoID()))
            startActivity(new Intent(this, InfoCenterActivity.class)
                    .putExtra(Const.InfoCenterID, entity.getInfoID()));
    }

    private String GroupID = "";
    private String ToUserID = "";
    private String ToUserName = "";
    private int GroipTag = 0;

    @Override
    public void toItemReply(ArticleCommentEntity entity, int cid, int tag) {
        if (cid == 0) return;
        GroupID = cid + "";
        GroipTag = tag;
        if (!TextUtils.isEmpty(GroupID)) {
            ToUserID = TextUtils.isEmpty(entity.getInfoID()) ? "" : entity.getInfoID();
            if (ToUserID.equals(UserinfoData.getInfoID(this)) && tag == 1) {
                showToast("不能评论自己");
            } else {
                ToUserName = TextUtils.isEmpty(entity.getInfoName()) ? "" : entity.getInfoName();
                tv_edit_title.setText("回复：" + ToUserName);
                showSoftInput();
            }
        }
    }

    @Override
    public void toItemLike(ArticleCommentEntity entity, int pos) {
        if (entity.getHaveLike() == 1) {
            showToast("您已经赞过了");
        } else {
            if (!liking && !OtherUtils.isEmpty(entity.getCid() + "")) {
                liking = true;
                this.pos = pos;
                Cid = entity.getCid() + "";
                Factory.postPhp(this, Const.PGiveLike);
            }
        }
    }

    private void overLoading(int tag) {
        if (tag == 0) {
            if (refresh) {
                xRefreshView.stopRefresh();
                xRefreshView.setLoadComplete(false);
            }
            if (loading) {
                xRefreshView.stopLoadMore();
            }
        } else if (tag == 1) {
            if (refresh) {
                xRefreshView.stopRefresh();
            }
            if (loading) {
                page--;
                xRefreshView.stopLoadMore();
            }
        } else if (tag == 2) {
            if (refresh) {
                xRefreshView.stopRefresh();
            }
            if (loading) {
                page--;
                xRefreshView.setLoadComplete(true);
            }
        }
        if (refresh) refresh = false;
        if (loading) loading = false;
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
            protected void onPostExecute(Void aVoid) {
                try {
                    Log.i(Const.TAG.ZY_OTHER, "加载更多");
                    refresh = true;
                    page = 0;
                    initData();
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "onLoadMore");
                }
            }
        }.execute(new Void[]{});
    }

    private boolean refresh = false;
    private boolean loading = false;

    @Override
    public void onLoadingMore() {
        if (!loading) {
            loading = true;
            if (datas == null || datas.size() < pagesize - 1) {
                overLoading(2);
            } else {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        SystemClock.sleep(1000);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        try {
                            page++;
                            initData();
                        } catch (Exception e) {
                            ExceptionUtils.ExceptionSend(e, "onLoadMore");
                        }
                    }
                }.execute(new Void[]{});
            }
        }
    }

    private boolean flag = false;// 这个控制隐藏键盘的时候 回调只被调用一次

    @Override
    public void onShown() {
        try {
            flag = true;
            v_half.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(content)) {
                et.setText(content);
                et.setSelection(content.length());
            } else {
                String temp = UserinfoData.getDtaftArticleArray().get(Integer.valueOf(aid));
                if (!TextUtils.isEmpty(temp)) {
                    et.setText(temp);
                    et.setSelection(temp.length());
                }
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ArticleCommentActivity");
        }
    }

    @Override
    public void onHidden() {
        if (flag) {
            hideInput();
        }
    }

    private void showSoftInput() {
        try {
            v_edit.setVisibility(View.VISIBLE);
            findViewById(R.id.v_bottom).setVisibility(View.VISIBLE);
            et.setFocusableInTouchMode(true);
            et.requestFocus();
            InputMethodManager inputManager =
                    (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(et, 0);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ArticleCommentActivity");
        }
    }

    private void hideInput() {
        try {
            v_half.setVisibility(View.GONE);
            content = et.getText().toString().trim();
            flag = false;
            clearReplyData();
            v_edit.setVisibility(View.GONE);
            tv_edit_title.setText("我想说...");
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ArticleCommentActivity");
        }
    }

    private String content = "";

    private void hideKeyboard(IBinder token) {
        try {
            if (token != null) {
                content = et.getText().toString().trim();
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
                Animation a = AnimationUtils.loadAnimation(this, R.anim.dialog_enter_anim);
                v_edit.startAnimation(a);
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ArticleCommentActivity");
        }
    }

    //隐藏输入法后 清空 回复某人的数据
    private void clearReplyData() {
        try {
            et.setText("");
            ToUserID = "";
            ToUserName = "";
            et.setHint("我想说");
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "ArticleCommentActivity");
        }
    }
}

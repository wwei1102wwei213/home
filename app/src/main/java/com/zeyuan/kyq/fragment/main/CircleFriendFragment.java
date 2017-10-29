package com.zeyuan.kyq.fragment.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.CareFollowBaseEntity;
import com.zeyuan.kyq.Entity.CareFollowEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.CircleFriendRecyclerAdapter;
import com.zeyuan.kyq.app.BaseZyFragment;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.AdapterCallback;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/15.
 *
 * @author wwei
 */
public class CircleFriendFragment extends BaseZyFragment implements HttpResponseInterface, AdapterCallback {

    private XRefreshView xRefreshView;
    private RecyclerView recyclerView;
    private List<CareFollowEntity> data;
    private CircleFriendRecyclerAdapter adapter;
    LinearLayoutManager layoutManager;
    private String CareID;
    private int CarePos;
    private int page = 0;
    private int pagesize = 20;
    private boolean refresh = false;
    private boolean loading = false;
    private String type = "1";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.item_fragment_friend, container, false);

        initView();
        initData();

        return rootView;
    }

    private void initView() {
        xRefreshView = (XRefreshView) findViewById(R.id.xrv);
        recyclerView = (RecyclerView) findViewById(R.id.rv);

        recyclerView.setHasFixedSize(true);
        data = new ArrayList<>();
        adapter = new CircleFriendRecyclerAdapter(context, data, this);
        // 设置静默加载模式
//        xRefreshView1.setSilenceLoadMore();
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        // 静默加载模式不能设置footerview
        recyclerView.setAdapter(adapter);
        //设置刷新完成以后，headerview固定的时间
        xRefreshView.setPinnedTime(1000);
        xRefreshView.setPullLoadEnable(true);
        xRefreshView.setMoveForHorizontal(true);
//        xRefreshView1.setAutoLoadMore(true);
        adapter.setCustomLoadMoreView(new XRefreshViewFooter(context));

        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh = true;
                        page = 0;
                        initData();
                    }
                }, 500);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        loading = true;
                        page++;
                        initData();
                    }
                }, 1000);
            }
        });

        xRefreshView.setStateCallback(new XRefreshView.XRefreshViewScrollStateChangedCallback() {
            @Override
            public void XRVScrollStateChangedCallback(int scrollState) {
                if (scrollState == 2) {
                    Glide.with(context).pauseRequests();
                } else {
                    Glide.with(context).resumeRequests();
                }
            }
        });
    }

    private void initData() {
        if (getActivity().getIntent()!=null){
            type = getActivity().getIntent().getStringExtra(Const.TypeID);
            if (TextUtils.isEmpty(type))
                type="1";
        }
        Factory.postPhp(this, Const.PCircleFRList);
    }

    public void showToast(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == Const.PCircleFRList) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(context));
            map.put("page", page + "");
            map.put("pagesize", pagesize + "");
            map.put(Const.TypeID,type);
        } else if (tag == Const.PCareDel || tag == Const.PCareAdd) {
            map.put("uid", UserinfoData.getInfoID(context));
            map.put("careuid", CareID);
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PCareAdd || flag == Const.PCareDel) {
            PhpUserInfoBean bean = (PhpUserInfoBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                if (flag == Const.PCareAdd) {
                    adapter.update(CarePos, 2, true);
                    showToast("关注成功");
                } else {
                    showToast("取消关注成功");
                    adapter.update(CarePos, 0, true);
                }
            } else {
                if (flag == Const.PCareAdd) {
                    showToast("关注失败");
                    adapter.update(CarePos, 0, false);
                } else {
                    showToast("取消关注失败");
                    adapter.update(CarePos, 2, false);
                }
            }
        } else if (flag == Const.PCircleFRList) {
            CareFollowBaseEntity entity = (CareFollowBaseEntity) response;
            if (Const.RESULT.equals(entity.getiResult())) {
                List<CareFollowEntity> temp = entity.getData();
                if (temp != null && temp.size() > 0) {
                    if (page == 0) {
                        data = new ArrayList<>();
                    }
                    data.addAll(temp);
                    adapter.update(data);
                    overLoading(0);
                } else {
                    overLoading(2);
                }
            } else {
                overLoading(1);
                showToast("请求错误\n错误码:" + entity.getiResult());
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
        if (flag == Const.PFollowList || flag == Const.PCareList) {
            showToast("网络请求失败");
        } else if (flag == Const.PCareAdd) {
            showToast("关注失败");
            adapter.update(CarePos, 0, false);
        } else if (flag == Const.PCareDel) {
            showToast("取消关注失败");
            adapter.update(CarePos, 2, false);
        } else if (flag == Const.PCircleFRList) {
            showToast("网络请求失败");
            overLoading(1);
        }
    }

    @Override
    public void forAdapterCallback(int pos, int tag, String id, boolean flag, Object obj) {
        CareID = id;
        CarePos = pos;
        if (flag) {
            Factory.postPhp(this, Const.PCareAdd);
        } else {
            cancelFollow();
        }
    }

    private ZYDialog dialog;

    private void cancelFollow() {
        dialog = new ZYDialog.Builder(context).setTitle("提示")
                .setMessage("确定不再关注此人?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Factory.postPhp(CircleFriendFragment.this, Const.PCareDel);
                        dialog.dismiss();
                    }
                })
                .setCancelAble(true)
                .create();
        dialog.show();
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
}

package com.zeyuan.kyq.view;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.andview.refreshview.XRefreshView;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.CareFollowBaseEntity;
import com.zeyuan.kyq.Entity.CareFollowEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.CareFollowAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.AdapterCallback;
import com.zeyuan.kyq.biz.manager.FunctionGuideManager;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.OtherUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/3.
 * <p>
 * 粉丝列表和关注列表
 *
 * @author wwei
 */
public class CareListActivity extends BaseActivity implements HttpResponseInterface
        , AdapterView.OnItemClickListener, AdapterCallback {
    private XRefreshView xRefreshView;
    private List<CareFollowEntity> data;
    private CareFollowAdapter adapter;
    private String InfoCenterID;
    private int type;
    private String CareID;
    private int CarePos;
    private int page = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_list);

        InfoCenterID = getIntent().getStringExtra(Const.InfoCenterID);
        type = getIntent().getIntExtra(Const.CareListType, 1);
        initView();
        initData();

    }

    private void initView() {
        xRefreshView = (XRefreshView) findViewById(R.id.xrv);
        ListView lv = (ListView) findViewById(R.id.lv);
        data = new ArrayList<>();
        adapter = new CareFollowAdapter(this, data);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        xRefreshView.setPinnedTime(1000);
        xRefreshView.setPullRefreshEnable(false);
        xRefreshView.setPullLoadEnable(true);
        xRefreshView.setMoveForHorizontal(true);
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        getData();
                    }
                }, 1000);
            }
        });
        xRefreshView.setStateCallback(new XRefreshView.XRefreshViewScrollStateChangedCallback() {
            @Override
            public void XRVScrollStateChangedCallback(int scrollState) {
                if (scrollState == 2) {
                    Glide.with(CareListActivity.this).pauseRequests();
                } else {
                    Glide.with(CareListActivity.this).resumeRequests();
                }
            }
        });
    }


    private void initData() {
        if (type == 1) {
            if (InfoCenterID.equals(UserinfoData.getInfoID(this))) {
                initWhiteTitle("我的粉丝");
            } else {
                initWhiteTitle("TA的粉丝");
            }
            Factory.postPhp(this, Const.PFollowList);
        } else {
            if (InfoCenterID.equals(UserinfoData.getInfoID(this))) {
                initWhiteTitle("我的关注");
            } else {
                initWhiteTitle("TA的关注");
            }
            Factory.postPhp(this, Const.PCareList);
        }
    }

    private void getData() {
        if (type == 1) {
            Factory.postPhp(this, Const.PFollowList);
        } else {
            Factory.postPhp(this, Const.PCareList);
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == Const.PCareList || tag == Const.PFollowList) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
            map.put("UID", InfoCenterID);
            map.put("page", page + "");
            map.put("pagesize", 20 + "");
        } else if (tag == Const.PCareDel || tag == Const.PCareAdd) {
            map.put("uid", UserinfoData.getInfoID(this));
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

        if (flag == Const.PFollowList || flag == Const.PCareList) {
            CareFollowBaseEntity entity = (CareFollowBaseEntity) response;
            if (Const.RESULT.equals(entity.getiResult())) {

                if (entity.getData() != null && entity.getData().size() > 0) {
                    data.addAll(entity.getData());
                    adapter.update(data);
                    overLoading(0);

                } else {
                    overLoading(1);
                }
            } else {
                overLoading(2);
                showToast("请求错误\n错误码:" + entity.getiResult());
            }
        } else if (flag == Const.PCareAdd || flag == Const.PCareDel) {
            PhpUserInfoBean bean = (PhpUserInfoBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                if (flag == Const.PCareAdd) {
                    adapter.update(CarePos, 2, true);
                    showToast("关注成功");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!CareListActivity.this.isFinishing()){
                                FunctionGuideManager.getInstance().showFocusFriendGuide(CareListActivity.this);
                            }

                        }
                    }, 200);
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
            overLoading(2);
            showToast("网络请求失败");
        } else if (flag == Const.PCareAdd) {
            showToast("关注失败");
            adapter.update(CarePos, 0, false);
        } else if (flag == Const.PCareDel) {
            showToast("取消关注失败");
            adapter.update(CarePos, 2, false);
        }

    }

    private void overLoading(int tag) {
        if (tag == 1) {//所有数据加载完成
            xRefreshView.setLoadComplete(true);
        } else if (tag == 2) {//加载失败
            if (page != 0)
                page--;
            xRefreshView.setLoadComplete(false);
            xRefreshView.stopLoadMore();
        } else {
            xRefreshView.setLoadComplete(false);
            xRefreshView.stopLoadMore();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LogCustom.i("ZYS", "已经进入");
        String uid = adapter.getItem(position).getInfoID() + "";
        LogCustom.i("ZYS", "uid：" + uid);
        if (!OtherUtils.isEmpty(uid)) {
            startActivity(new Intent(CareListActivity.this, InfoCenterActivity.class)
                    .putExtra(Const.InfoCenterID, uid));
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
//            Factory.postPhp(this, Const.PCareDel);
        }
    }

    private ZYDialog dialog;

    private void cancelFollow() {
        dialog = new ZYDialog.Builder(this).setTitle("提示")
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
                        Factory.postPhp(CareListActivity.this, Const.PCareDel);
                        dialog.dismiss();
                    }
                })
                .setCancelAble(true)
                .create();
        dialog.show();
    }
}

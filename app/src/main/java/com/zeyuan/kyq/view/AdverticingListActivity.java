package com.zeyuan.kyq.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.Advertising;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.AdverticingRecAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.AdverticingListBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdverticingListActivity extends BaseActivity implements View.OnClickListener, HttpResponseInterface {
    private XRefreshView xrv_adverticing;
    RecyclerView rcv_adverticing;
    LinearLayoutManager layoutManager;
    private String location = "2";
    private Context context;
    private List<Advertising> advertisingList;
    private AdverticingRecAdapter adverticingRecAdapter;
    boolean refresh;
    boolean loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_adverticing_list);
        initData();
        initView();
    }

    private void initData() {
        advertisingList = new ArrayList<>();
        location = getIntent().getStringExtra("location");
    }

    private void initView() {
        //设置返回键
        findViewById(R.id.ll_back).setOnClickListener(this);
        TextView tv_other_title = (TextView) findViewById(R.id.tv_other_title);
        if (location.equals("2"))
            tv_other_title.setText("防护支持");
        else
            tv_other_title.setText("营养指导");
        layoutManager = new LinearLayoutManager(context);
        xrv_adverticing = (XRefreshView) findViewById(R.id.xrv_adverticing);
        adverticingRecAdapter = new AdverticingRecAdapter(context, advertisingList);
        rcv_adverticing = (RecyclerView) findViewById(R.id.rcv_adverticing);
        rcv_adverticing.setHasFixedSize(true);
        rcv_adverticing.setLayoutManager(layoutManager);
        rcv_adverticing.setAdapter(adverticingRecAdapter);
        xrv_adverticing.setPinnedTime(1000);
        xrv_adverticing.setPullLoadEnable(true);
        xrv_adverticing.setMoveForHorizontal(true);
        xrv_adverticing.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh = true;
                        Factory.postPhp(AdverticingListActivity.this, Const.PApi_getAdvertising_more);
                    }
                }, 500);
            }

        });
        xrv_adverticing.setStateCallback(new XRefreshView.XRefreshViewScrollStateChangedCallback() {
            @Override
            public void XRVScrollStateChangedCallback(int scrollState) {
                if (scrollState == 2) {
                    Glide.with(context).pauseRequests();
                } else {
                    Glide.with(context).resumeRequests();
                }
            }
        });
        Factory.postPhp(AdverticingListActivity.this, Const.PApi_getAdvertising_more);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(context));
        if (tag == Const.PApi_getAdvertising_more) {
            map.put("cureid", MapDataUtils.getAllCureconfID(UserinfoData.getStepID(context)));
            map.put("location", location);
            map.put("stepid", UserinfoData.getStepID(context));
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PApi_getAdvertising_more) {
            AdverticingListBean adverticingListBean = (AdverticingListBean) response;
            if (Const.RESULT.equals(adverticingListBean.getiResult())) {
                advertisingList.clear();
                if (adverticingListBean.getData() != null)
                    advertisingList.addAll(adverticingListBean.getData());
                adverticingRecAdapter.notifyDataSetChanged();
            }
            xrv_adverticing.stopRefresh();
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
        xrv_adverticing.stopRefresh();
    }


}

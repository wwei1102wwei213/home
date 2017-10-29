package com.zeyuan.kyq.fragment.main;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.ForumBaseBean;
import com.zeyuan.kyq.Entity.ForumBaseEntity;
import com.zeyuan.kyq.Entity.PersonalBean;
import com.zeyuan.kyq.Entity.PersonalEntity;
import com.zeyuan.kyq.Entity.Shortcut;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.CircleFindRecyclerAdapter;
import com.zeyuan.kyq.app.LazyFragment;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.bean.ShortcutBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.manager.FunctionGuideManager;
import com.zeyuan.kyq.biz.manager.MyCircleManager;
import com.zeyuan.kyq.biz.manager.PointManager;
import com.zeyuan.kyq.fragment.dialog.CityDialog;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.ReleaseForumActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zeyuan.kyq.utils.Contants.InfoName;

/**
 * Created by Administrator on 2017/5/12.
 * 本地圈
 * 默认为北京圈，圈子id：1001
 */

public class CircleLocalFragment extends LazyFragment implements HttpResponseInterface, View.OnClickListener {
    private int page = 0;
    private ProgressBar pd;
    private View body;
    private XRefreshView xRefreshView;
    private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    private CircleFindRecyclerAdapter adapter;
    private List<ForumBaseEntity> forumListDatas;//装帖子列表的数据
    private boolean refresh = false;
    private boolean loading = false;
    private LinearLayout ll_empty;


    @Override
    protected void setDefaultFragmentTitle(String title) {

    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_circle_local, container, false);

        initView();
        // initData();

        return rootView;
    }


    private void initView() {
        try {
            pd = (ProgressBar) findViewById(R.id.pd);
            body = findViewById(R.id.body);
            xRefreshView = (XRefreshView) findViewById(R.id.xrv);
            recyclerView = (RecyclerView) findViewById(R.id.rv);
            ll_empty = (LinearLayout) findViewById(R.id.ll_empty);
            Button btn_to_post = (Button) findViewById(R.id.btn_to_post);


            recyclerView.setHasFixedSize(true);
            forumListDatas = new ArrayList<>();
            adapter = new CircleFindRecyclerAdapter(context, new ArrayList<Shortcut>(), forumListDatas);
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
                            getForumData();

                        }
                    }, 500);
                }

                @Override
                public void onLoadMore(boolean isSilence) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {

                            loading = true;
                            page++;
                            Factory.postPhp(CircleLocalFragment.this, Const.PGetForumList_bank);

                        }
                    }, 1000);
                }
            });

            xRefreshView.setStateCallback(new XRefreshView.XRefreshViewScrollStateChangedCallback() {
                                              @Override
                                              public void XRVScrollStateChangedCallback(int scrollState) {
                                                  try {
                                                      if (scrollState == 2) {
                                                          Glide.with(context).pauseRequests();
                                                      } else {
                                                          Glide.with(context).resumeRequests();
                                                      }
                                                  } catch (Exception e) {

                                                  }

                                              }
                                          }

            );
            btn_to_post.setOnClickListener(this);

            initSelectLocalView();

        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "initView");
        }
    }

    View view_select_local;
    private TextView tv_select_city;
    private View view_finish_select;

    //初始化选择地址的view
    private void initSelectLocalView() {
        view_select_local = findViewById(R.id.view_select_local);
        //没有城市id，显示选择地址页面
        if (TextUtils.isEmpty(UserinfoData.getCityID(getActivity())) || UserinfoData.getCityID(getActivity()).equals("0")) {
            view_select_local.setVisibility(View.VISIBLE);
        } else {
            view_select_local.setVisibility(View.GONE);
        }
        tv_select_city = (TextView) findViewById(R.id.tv_select_city);
        tv_select_city.setOnClickListener(this);
        view_finish_select = findViewById(R.id.tv_finish_select);
        view_finish_select.setOnClickListener(this);


    }

    @Override
    protected void initData() {
        //没有城市id，先获取个人信息
        if (TextUtils.isEmpty(UserinfoData.getCityID(getActivity())) || UserinfoData.getCityID(getActivity()).equals("0")) {
            Factory.postPhp(this, Const.PGetUserSelfForApp);
        } else {
            getForumData();
        }
    }

    //获取帖子数据
    private void getForumData() {
        Factory.postPhp(CircleLocalFragment.this, Const.PGetForumList_bank);
        Factory.postPhp(CircleLocalFragment.this, Const.PApi_getCircleShortcut);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_to_post:
                try {
                    List<String> temp = new ArrayList<>();
                    try {
                        //设置默认选中圈子
                        if (ZYApplication.DefaultCircles != null && ZYApplication.DefaultCircles.size() > 0) {
                            for (int i = 0; i < ZYApplication.DefaultCircles.size(); i++) {
                                List<String> temp1 = ZYApplication.DefaultCircles.get(i);
                                if (temp1 != null && temp1.size() > 0) {
                                    for (int j = 0; j < temp1.size(); j++) {
                                        temp.add(temp1.get(j));
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        ExceptionUtils.ExceptionSend(e, "DefaultCircles");
                    }
                    startActivity(new Intent(getActivity(), ReleaseForumActivity.class)
                            .putExtra(Const.DEFAULT_CIRCLE, (Serializable) temp));
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "ft");
                }
                break;
            case R.id.tv_select_city:
                showCityDialog();
                break;
            case R.id.tv_finish_select:
                view_finish_select.setEnabled(false);
                Factory.postPhp(this, Const.PEditUserSelfForApp);
                break;
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == Const.PGetForumList_bank) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(context));
            map.put("page", page + "");
            map.put("pagesize", "15");
            if (ZYApplication.DefaultCircles != null)
                map.put("CircleID", ZYApplication.DefaultCircles.get(0).get(0));
        } else if (tag == Const.PApi_getCircleShortcut) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(context));
        } else if (tag == Const.PGetUserSelfForApp) {//获取用户信息
            map.put(Contants.InfoID, UserinfoData.getInfoID(context));
        } else if (tag == Const.PEditUserSelfForApp) {//提交用户信息
            map.put(Contants.InfoID, UserinfoData.getInfoID(context));
            map.put(InfoName, UserinfoData.getInfoname(context));
            map.put("oss_request_url", UserinfoData.getAvatarUrl(context));
            map.put("City", CityID);
            map.put("ProvinceID", ProvinceID);
            if (personalEntity != null) {
                map.put("IsSelf", personalEntity.getIsSelf() + "");
                map.put("Sex", personalEntity.getUserSex() + "");
                map.put("Age", personalEntity.getUserAge() + "");
            } else {
                map.put("IsSelf", "1");
                map.put("Sex", "0");
                map.put("Age", "0");
            }

        }
        return map;

    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    PersonalEntity personalEntity;

    @Override
    public void toActivity(Object response, int flag) {
        showUI();
        if (flag == Const.PGetForumList_bank) {
            ForumBaseBean bean = (ForumBaseBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                bindListView(bean);
                FunctionGuideManager.getInstance().showLocalForumGuide(getActivity());
            } else {
                overLoading(1);
            }
        } else if (flag == Const.PApi_getCircleShortcut) {
            ShortcutBean bean = (ShortcutBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                Collections.sort(bean.getData4(), comparator);
                adapter.updateShortcut(bean.getData4());
            }
        } else if (flag == Const.PGetUserSelfForApp) {
            PersonalBean bean = (PersonalBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                personalEntity = bean.getData();
            }
        } else if (flag == Const.PEditUserSelfForApp) {
            view_finish_select.setEnabled(true);
            PhpUserInfoBean bean = (PhpUserInfoBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                UserinfoData.saveUserDataChange(context, UserinfoData.getInfoname(context), UserinfoData.getAvatarUrl(context), CityID);
                view_select_local.setVisibility(View.GONE);
                FunctionGuideManager.getInstance().showFocusSameCityUserGuide(getActivity());
                MyCircleManager.getInstance().clearCircle();
                getForumData();
            } else {
                showToast("保存失败");
            }
        }
    }

    //快速入口排序比较器
    Comparator<Shortcut> comparator = new Comparator<Shortcut>() {
        public int compare(Shortcut s1, Shortcut s2) {
            // 按权重排序(从大到小)
            if (s1.getPowerNum() != s2.getPowerNum()) {
                return -(s1.getPowerNum() - s2.getPowerNum());
            }
            return 0;

        }
    };

    @Override
    public void showLoading(int flag) {

    }

    @Override
    public void hideLoading(int flag) {

    }

    @Override
    public void showError(int flag) {
        showUI();
        if (flag == Const.PGetForumList_bank) {
            overLoading(1);
        }
    }

    private void bindListView(ForumBaseBean bean) {
        try {
            List<ForumBaseEntity> list = bean.getForumAllNum();
            if (page == 0) {
                forumListDatas.clear();
                PointManager.getInstance().setT4(context);
            }
            if (list != null) {
                forumListDatas.addAll(list);
                overLoading(0);
            } else {
                overLoading(2);
            }
            adapter.notifyDataSetChanged();//刷新list
            setEmptyView(forumListDatas.size());
        } catch (Exception e) {
            overLoading(0);
            ExceptionUtils.ExceptionToUM(e, getActivity(), "CircleFindFragment");
        }
    }

    private void setEmptyView(int item_size) {
        if (item_size > 0) {
            ll_empty.setVisibility(View.GONE);
        } else {
            ll_empty.setVisibility(View.VISIBLE);
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

    private void showUI() {
        if (body.getVisibility() != View.VISIBLE) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        pd.setVisibility(View.GONE);
                        body.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        ExceptionUtils.ExceptionSend(e, "onRefresh");
                    }
                }
            }, 300);
        }
    }

    String CityID, ProvinceID;

    //显示城市选择对话框
    private void showCityDialog() {
        CityDialog dialog = new CityDialog();
        dialog.setOnOnCitySelListener(new DialogFragmentListener() {
            @Override
            public void getDataFromDialog(DialogFragment dialog, String data, int position) {
                CityID = data;
                ProvinceID = position + "";
                tv_select_city.setText(getCityString(CityID, ProvinceID));
            }
        });
        dialog.show(getActivity().getFragmentManager(), Contants.CITY_DIALOG);
    }

    private String getCityString(String cID, String pID) {
        String result = TextUtils.isEmpty(pID) ? "" : MapDataUtils.getCityName(pID);
        if (!TextUtils.isEmpty(cID)) {
            if (TextUtils.isEmpty(result)) {
                result = MapDataUtils.getCityName(cID);
            } else {
                result += ",";
                result += MapDataUtils.getCityName(cID);
            }
        }
        return result;
    }
}

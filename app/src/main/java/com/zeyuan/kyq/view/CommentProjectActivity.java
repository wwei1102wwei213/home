package com.zeyuan.kyq.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.ProvinceEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.CancerTypeLeftAdapter;
import com.zeyuan.kyq.adapter.CancerTypeRightAdapter;
import com.zeyuan.kyq.adapter.CityDialogLeftAdapter;
import com.zeyuan.kyq.adapter.CityDialogRightAdapter;
import com.zeyuan.kyq.adapter.nww.CommentProjectRvAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.CommentProjectBean;
import com.zeyuan.kyq.bean.CommentProjectItem;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.AdapterCallback;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.SyncConfigUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.FlowLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017-11-22.
 */

public class CommentProjectActivity extends BaseActivity implements HttpResponseInterface, View.OnClickListener {

    FrameLayout fl_tips_or_selector;
    Context context;
    List<CommentProjectItem> list = new ArrayList<>();
    boolean refresh;
    boolean loading;
    XRefreshView xrv_similar_case;
    RecyclerView rcv_similar_case;
    LinearLayoutManager layoutManager;
    int page = 0;
    private int type;//1项目 2医生 3医院
    private CommentProjectRvAdapter adapter;
    private FrameLayout fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        //设置状态栏
        setContentView(R.layout.activity_comment_list);
        type = getIntent().getIntExtra("Comment_Project_Type", 1);
        initView();
        initData();
    }


    private void initView() {
        //设置返回键
        findViewById(R.id.ll_back).setOnClickListener(this);
        TextView tv_other_title = (TextView) findViewById(R.id.tv_other_title);
        fl = (FrameLayout) findViewById(R.id.fl);
        String titleName = "";
        int iv_res;
        if (type==1){
            titleName = "项目";
            iv_res = R.drawable.icon_add_project;
        } else if(type==2){
            titleName = "医生";
            iv_res = R.drawable.icon_add_dot;
        } else {
            titleName = "医院";
            iv_res = R.drawable.icon_add_host;
        }
        String tv_bottom_type = "新增"+titleName;
        ((TextView) findViewById(R.id.tv_bottom_add_type)).setText(tv_bottom_type);
        ((ImageView) findViewById(R.id.iv_bottom_add_type)).setImageResource(iv_res);
        tv_other_title.setText(titleName);

        findViewById(R.id.v_bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CommentProjectActivity.this, AddCommentActivity.class).putExtra(Const.INTENT_ADD_FLAG_TYPE, type));
                /*startActivity(new Intent(CommentProjectActivity.this, AddCommentAllActivity.class)
                        .putExtra(Const.INTENT_ADD_FLAG_TYPE, type-1)
                        .putExtra("Comment_Item_Data",list.get(0)));*/
            }
        });

        initTopView();

        fl_tips_or_selector = (FrameLayout) findViewById(R.id.fl_tips_or_selector);
        xrv_similar_case = (XRefreshView) findViewById(R.id.xrv_similar_case);
        rcv_similar_case = (RecyclerView) findViewById(R.id.rcv_similar_case);
        adapter = new CommentProjectRvAdapter(context, list, new AdapterCallback() {
            @Override
            public void forAdapterCallback(int pos, int tag, String id, boolean flag, Object obj) {
                startActivity(new Intent(CommentProjectActivity.this, ShowCommentH5Actvity.class)
                        .putExtra(Const.INTENT_SHOW_COMMENT_TYPE, Integer.valueOf(type-1))
                        .putExtra("Comment_Item_Data",(CommentProjectItem) obj)
                        .putExtra(Const.INTENT_SHOW_COMMENT_ID, id));
            }
        },type);
        layoutManager = new LinearLayoutManager(context);
        rcv_similar_case.setHasFixedSize(true);
        rcv_similar_case.setLayoutManager(layoutManager);
        rcv_similar_case.setAdapter(adapter);
        adapter.setCustomLoadMoreView(new XRefreshViewFooter(context));
        xrv_similar_case.setPinnedTime(1000);
        xrv_similar_case.setPullLoadEnable(true);
        xrv_similar_case.setMoveForHorizontal(true);
        xrv_similar_case.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

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
                }, 500);
            }
        });
        xrv_similar_case.setStateCallback(new XRefreshView.XRefreshViewScrollStateChangedCallback() {
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
        if (type==2){
            Factory.postPhp(this, Const.PApi_getDocList);
        } else if(type==3){
            Factory.postPhp(this, Const.PApi_getHosList);
        } else {
            Factory.postPhp(this, Const.PApi_getProjectList);
        }
    }

    private View v1,v2,v3,v4;
    private TextView tv1,tv2,tv3,tv4;
    private void initTopView(){
        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);
        v4 = findViewById(R.id.v4);
        tv1 = (TextView) findViewById(R.id.tv_where);
        tv2 = (TextView) findViewById(R.id.tv_cure_type);
        tv3 = (TextView) findViewById(R.id.tv_cancer_type);
        tv4 = (TextView) findViewById(R.id.tv_order);
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!showUIFLAG){
                    showUIFLAG = false;
                    showCityView();
                } else {
                    cancelView();
                }
            }
        });
        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!showUIFLAG){
                    showUIFLAG = false;
                    showCureView();
                } else {
                    cancelView();
                }
            }
        });
        v3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!showUIFLAG){
                    showUIFLAG = false;
                    showCancerView();
                } else {
                    cancelView();
                }
            }
        });
        v4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!showUIFLAG){
                    showUIFLAG = false;
                    showOrderView();
                } else {
                    cancelView();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }

    String cancerId = "";
    String city = "";
    String CureType = "";
    int OrderType = 0;
    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(context));
        if (tag == Const.PApi_getProjectList ||tag == Const.PApi_getHosList || tag == Const.PApi_getDocList) {
            map.put("CancerID", cancerId);
            map.put("CureType ", CureType);//转移情况
            map.put("City", city);
            map.put("OrderType", OrderType + "");
            map.put("page", page + "");
            map.put("pageSize", "10");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    private boolean isNeedShowSearchResult = true;//是不是显示搜索结果

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PApi_getProjectList || flag == Const.PApi_getHosList || flag == Const.PApi_getDocList) {
            CommentProjectBean bean = (CommentProjectBean) response;
            if ("0".equals(bean.getiResult())){
                if (bean.getData() != null && bean.getData().size() > 0) {
                    if (page == 0) {
                        list = new ArrayList<>();
                    }
                    list.addAll(bean.getData());
                    adapter.update(list);
                    overLoading(0);
                } else {
                    if (page==0&&(TextUtils.isEmpty(city)||TextUtils.isEmpty(cancerId)||TextUtils.isEmpty(CureType))){
                        list = new ArrayList<>();
                        adapter.update(list);
                        showToast("该条件没有结果");
                    }
                    overLoading(2);
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
        if (flag == Const.PApi_getProjectList || flag == Const.PApi_getHosList || flag == Const.PApi_getDocList) {
            overLoading(1);
            showToast("网络请求失败");
        }
    }

    private void overLoading(int tag) {
        if (tag == 0) {
            if (refresh) {
                xrv_similar_case.stopRefresh();
                xrv_similar_case.setLoadComplete(false);
            }
            if (loading) {
                xrv_similar_case.stopLoadMore();
            }
        } else if (tag == 1) {
            if (refresh) {
                xrv_similar_case.stopRefresh();
            }
            if (loading) {
                page--;
                xrv_similar_case.stopLoadMore();
            }
        } else if (tag == 2) {
            if (refresh) {
                xrv_similar_case.stopRefresh();
            }
            if (loading) {
                page--;
                xrv_similar_case.setLoadComplete(true);
            }
        }
        if (refresh) refresh = false;
        if (loading) loading = false;
    }

    private AlphaAnimation out;
    private AlphaAnimation in;
    private void initAnim(){
        if (out==null){
            out = new AlphaAnimation(1.0f, 0.1f);
            out.setDuration(100);
        }
        if (in==null){
            in = new AlphaAnimation(0.1f, 1.0f);
            in.setDuration(100);
        }
    }

    public Map<String, Integer> selectData;//标示 记录左侧列表哪个自选项被选中
    private CancerTypeLeftAdapter leftAdapter;
    private List<String> rightData;
    private List<String> leftData;
    private Map<String, List<String>> cancerData;
    private Map<String, String> cancerValues;
    private CancerTypeRightAdapter rightAdapter;
    private View cancerView;
    private void showCancerView(){
        fl.removeAllViews();
        if (cancerView==null) {
            cancerView = LayoutInflater.from(context).inflate(R.layout.popupwindow_cancer_selector, fl,false);
            selectData = new HashMap<>();
            leftData = new ArrayList<>();
            cancerData = (Map<String, List<String>>) Factory.getData(Const.N_DataCancerData);
            cancerValues = (Map<String, String>) Factory.getData(Const.N_DataCancerValues);
            if (cancerData != null && cancerData.size() > 0) {
                leftData.addAll(cancerData.get("0"));
            }
            rightData = new ArrayList<>();

            ListView leftListView = (ListView) cancerView.findViewById(R.id.left_listview);
            leftListView.setDivider(null);
            ListView rightListView = (ListView) cancerView.findViewById(R.id.rigth_listview);
            /**
             * 左边的listview
             *
             */
            leftAdapter = new CancerTypeLeftAdapter(context, leftData);
            leftListView.setAdapter(leftAdapter);
            /**
             * 右边的listview
             */
            rightAdapter = new CancerTypeRightAdapter(context, rightData, cancerValues);
            rightListView.setAdapter(rightAdapter);
            View view_bg = cancerView.findViewById(R.id.view_bg);

            view_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelView();
                }
            });
            leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String chooseItem = leftAdapter.getItem(position);//
                    List<String> list = cancerData.get(chooseItem);
                    rightData.clear();
                    if (list != null && list.size() > 0) {
                        rightData.addAll(list);

                    } else {
                        rightData.add(chooseItem);
                    }
                    rightAdapter.notifyDataSetChanged();
                    leftAdapter.setSelectedPosition(position);
                    rightAdapter.setSelectedPosition(-1);
                }
            });
            rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    rightAdapter.setSelectedPosition(position);//这个是为了点击效果 字体变蓝
                    selectData.put(leftAdapter.getItem(leftAdapter.getCount() - 1), position);
                    String childItem = rightAdapter.getItem(position);
                    onSelectorItemSelected(childItem, cancerValues.get(childItem), TYPE_CANCER);

                }
            });
        }
        fl.addView(cancerView);
        initAnim();
        fl.setVisibility(View.VISIBLE);
        fl.startAnimation(in);
    }

    private View cityView;
    public SparseArray<String> citys;
    private ListView leftListview;
    private ListView rightListview;
    private CityDialogLeftAdapter leftCityAdapter;
    private List<String> rightCityData;
    private List<String> leftCityData;
    private Map<String, List<String>> cityData;
    private CityDialogRightAdapter rightCityAdapter;
    private DialogFragmentListener onCitySelListener;
    private List<ProvinceEntity> provinceList;
    private String province;
    private int selCityPosition = -1;
    private void showCityView(){
        fl.removeAllViews();
        if (cityView==null){
            cityView = LayoutInflater.from(context).inflate(R.layout.popupwindow_cancer_selector, fl,false);
            try {

                citys = (SparseArray<String>)Factory.getData(Const.N_DataAllCity);
                leftCityData = new ArrayList<>();
                rightCityData = new ArrayList<>();

                provinceList = (List<ProvinceEntity>) Factory.getData(Const.N_DataCity);
                for(ProvinceEntity province:provinceList){
                    leftCityData.add(province.getId());
                }
                rightCityData = SyncConfigUtils.parseChildCitys(provinceList.get(0).getCityarray());

                leftListview = (ListView) cityView.findViewById(R.id.left_listview);
                rightListview = (ListView) cityView.findViewById(R.id.rigth_listview);


                leftCityAdapter = new CityDialogLeftAdapter(context, leftCityData,citys);
                leftListview.setAdapter(leftCityAdapter);

                rightCityAdapter = new CityDialogRightAdapter(context, rightCityData,citys);
                rightListview.setAdapter(rightCityAdapter);
                leftListview.setSelection(0);
                leftCityAdapter.setSelectedPosition(0);

                View view_bg = cityView.findViewById(R.id.view_bg);

                view_bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelView();
                    }
                });

                leftListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (selCityPosition == position) return;
                        selCityPosition = position;//防止重复点击出现bug
                        leftCityAdapter.setSelectedPosition(position);//设置点击变白
                        province = leftCityAdapter.getItem(position);
                        rightCityData.clear();
                        rightCityData = SyncConfigUtils.parseChildCitys(provinceList.get(position).getCityarray());
                        rightCityAdapter.update(rightCityData);
                        rightListview.setSelection(0);
                    }
                });
                rightListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        rightCityAdapter.setSelectChoose(position);//设置选中
                        onSelectorItemSelected(rightCityAdapter.getItem(position), citys.get(Integer.valueOf(rightCityData.get(position))), TYPE_CITY);
                    }
                });

            }catch (Exception e){
                ExceptionUtils.ExceptionSend(e,"initdata();数据初始化失败");
            }
        }

        fl.addView(cityView);
        initAnim();
        fl.setVisibility(View.VISIBLE);
        fl.startAnimation(in);

    }

    private View cureView;
    /*private Map<String,String> cureConf;
    private List<String> cureKeys;*/
    private void showCureView(){
        fl.removeAllViews();
        if (cureView==null){
            cureView = LayoutInflater.from(context).inflate(R.layout.popupwindow_transfer_selector, fl,false);
            View view_bg = cureView.findViewById(R.id.view_bg);
            view_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelView();
                }
            });
            Map<String,String> cureConf = (Map<String,String>) Factory.getData(Const.N_DataCureConf);
            Set<String> leftSet = cureConf.keySet();//
            List<String> cureKeys = new ArrayList<>();
            for (String str : leftSet) {//把set转换为list
                cureKeys.add(str);
            }
            Collections.sort(cureKeys, new Comparator<String>() {
                @Override
                public int compare(String lhs, String rhs) {
                    return Integer.valueOf(lhs) - Integer.valueOf(rhs);
                }
            });
            final FlowLayout orderFL = (FlowLayout) cureView.findViewById(R.id.fl);
            for (int i=0;i<cureKeys.size();i++) {
                final String id = cureKeys.get(i);
                final String name = cureConf.get(id);
                final TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_order_comment, orderFL, false);
                tv.setText(name);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tv.isSelected()) {//取消选择
                            tv.setSelected(false);
                        } else {//选中
                            //清除之前选中item的状态
                            for (int i = 0; i < orderFL.getChildCount(); i++) {
                                orderFL.getChildAt(i).setSelected(false);
                            }
                            tv.setSelected(true);
                            onSelectorItemSelected(id, name, TYPE_CURE);
                        }
                    }
                });
                orderFL.addView(tv);
            }
        }
        fl.addView(cureView);
        initAnim();
        fl.setVisibility(View.VISIBLE);
        fl.startAnimation(in);
    }

    private View orderView;
    // 0 默认 1 好评度 2 发布时间
    private String[] orderData = {"默认排序","按好评度排序","按发布时间排序"};
    private String[] orderIds = {"0","1","2"};
//    private FlowLayout orderFL;
    private void showOrderView(){
        fl.removeAllViews();
        if (orderView==null){
            orderView = LayoutInflater.from(context).inflate(R.layout.popupwindow_transfer_selector, fl,false);
            View view_bg = orderView.findViewById(R.id.view_bg);
            view_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelView();
                }
            });

            final FlowLayout orderFL = (FlowLayout) orderView.findViewById(R.id.fl);
            for (int i=0;i<orderIds.length;i++) {
                final String id = orderIds[i];
                final String name = orderData[i];
                final TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_order_comment, orderFL, false);
                tv.setText(name);
                if (i==0) tv.setSelected(true);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tv.isSelected()) {//取消选择
                            tv.setSelected(false);
                        } else {//选中
                            //清除之前选中item的状态
                            for (int i = 0; i < orderFL.getChildCount(); i++) {
                                orderFL.getChildAt(i).setSelected(false);
                            }
                            tv.setSelected(true);
                            onSelectorItemSelected(id, name, TYPE_ORDER);
                        }
                    }
                });
                orderFL.addView(tv);
            }
        }
        fl.addView(orderView);
        initAnim();
        fl.setVisibility(View.VISIBLE);
        fl.startAnimation(in);
    }

    private boolean showUIFLAG = false;
    private void cancelView(){
        initAnim();
        fl.setVisibility(View.GONE);
        fl.startAnimation(out);
        showUIFLAG = false;
    }

    public final static int TYPE_CITY = 0;
    public final static int TYPE_CURE = 1;
    public final static int TYPE_CANCER = 2;
    public final static int TYPE_ORDER = 3;
    private void onSelectorItemSelected(String id, String name, int type){
        switch (type) {
            case TYPE_CANCER:
                cancerId = id;
                page = 0;
                tv3.setText(TextUtils.isEmpty(name)?"":name);
                initData();
                xrv_similar_case.setLoadComplete(false);
                break;
            case TYPE_CITY:
                city = id;
                page = 0;
                initData();
                tv1.setText(TextUtils.isEmpty(name)?"":name);
                xrv_similar_case.setLoadComplete(false);
                break;
            case TYPE_CURE:
                CureType = id;
                page = 0;
                initData();
                tv2.setText(TextUtils.isEmpty(name)?"":name);
                xrv_similar_case.setLoadComplete(false);
                break;
            case TYPE_ORDER:
                OrderType = Integer.valueOf(id);
                page = 0;
                initData();
                tv4.setText(TextUtils.isEmpty(name)?"":name);
                xrv_similar_case.setLoadComplete(false);
                break;
        }
        cancelView();
    }
}

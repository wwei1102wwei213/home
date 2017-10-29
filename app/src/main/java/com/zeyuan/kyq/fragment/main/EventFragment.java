package com.zeyuan.kyq.fragment.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.Entity.EventEntity;
import com.zeyuan.kyq.Entity.MainBannerEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.EventAdapter;
import com.zeyuan.kyq.adapter.MyFragmentAdapter;
import com.zeyuan.kyq.app.BaseZyFragment;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.manager.ClickStatisticsManager;
import com.zeyuan.kyq.fragment.other.EventItemFragment;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.ShowDiscuzActivity;
import com.zeyuan.kyq.widget.CustomView.InsideViewPager;
import com.zeyuan.kyq.widget.DrawCircleView;
import com.zeyuan.kyq.widget.MyListView;
import com.zeyuan.kyq.widget.PullToRefresh.PullToRefreshLayout;
import com.zeyuan.kyq.widget.PullToRefresh.PullableScrollView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/6.
 * <p>
 * 公益活动页面
 *
 * @author wwei
 */
public class EventFragment extends BaseZyFragment implements View.OnClickListener, HttpResponseInterface,
        ViewPager.OnPageChangeListener, AdapterView.OnItemClickListener, InsideViewPager.OnSingleTouchListener
        , PullToRefreshLayout.OnRefreshListener {

    private static final String TAG = "EventFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_event, container, false);
        try {
            initStatusBar();
            initTopView();
            initSelectView();
            initOtherView();
            initView();
            initAnim();
            initData();
        } catch (Exception e) {

        }
        return rootView;
    }


    private RadioGroup rg;
    private RadioButton btn1;
    private RadioButton btn2;

    /***
     *
     * 设置顶部视图
     *
     */
    private void initTopView() {
        rg = (RadioGroup) findViewById(R.id.rg_event);
        btn1 = (RadioButton) findViewById(R.id.rb1_event);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1_event:

                        break;
                    case R.id.rb2_event:
                        rg.clearCheck();
                        btn1.setChecked(true);
                        startActivity(new Intent(context, ShowDiscuzActivity.class)
                                .putExtra(Const.SHOW_HTML_MAIN_TOP, "http://www.8fchou.com/web/page/toPublishProject.htm?" + "kaqid=" +
                                        getRandomMath() + UserinfoData.getInfoID(context) + "&type=2&coi=kaq"));
//                        Toast.makeText(context, "暂不开放", Toast.LENGTH_SHORT).show();

                        break;
                }
            }
        });
    }

    private int select_flag = 1;
    private int index_old = 1;
    private View line_all;
    private View line_running;
    private View line_ending;
    private TextView tv_event_ending;
    private TextView tv_event_all;
    private TextView tv_event_running;

    private void initSelectView() {
        try {
            //设置视图控件
            line_all = findViewById(R.id.line_all);
            line_running = findViewById(R.id.line_running);
            line_ending = findViewById(R.id.line_ending);
            tv_event_all = (TextView) findViewById(R.id.tv_event_all);
            tv_event_running = (TextView) findViewById(R.id.tv_event_running);
            tv_event_ending = (TextView) findViewById(R.id.tv_event_ending);

            //设置初始化状态
            line_all.setVisibility(View.INVISIBLE);
            tv_event_all.setSelected(false);
            line_running.setVisibility(View.VISIBLE);
            tv_event_running.setSelected(true);
            line_ending.setVisibility(View.INVISIBLE);
            tv_event_ending.setSelected(false);
            select_flag = 1;
            index_old = 1;

            findViewById(R.id.v_all).setOnClickListener(this);
            findViewById(R.id.v_running).setOnClickListener(this);
            findViewById(R.id.v_ending).setOnClickListener(this);

        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "tab");
        }
    }

    private InsideViewPager vp;
    private DrawCircleView dcv;

    private void initOtherView() {
        vp = (InsideViewPager) findViewById(R.id.vp_event);
        vp.setOnSingleTouchListner(this);
        dcv = (DrawCircleView) findViewById(R.id.dcv_event);
    }

    private PullToRefreshLayout layout;
    private PullableScrollView psv;

    private void initView() {
        layout = (PullToRefreshLayout) findViewById(R.id.layout_event);
        layout.setOnRefreshListener(this);
        psv = (PullableScrollView) findViewById(R.id.psv_event);

        if (lv == null) lv = (MyListView) findViewById(R.id.lv_event);
        lv.setOnItemClickListener(this);
        lv.setFocusable(false);
        adapter = new EventAdapter(context, new ArrayList<MainBannerEntity>());
        lv.setAdapter(adapter);
    }

    private Object o = null;
    private EventEntity data;

    private void initData() {
        Factory.post(this, Const.EEvent);
    }

    private MyListView lv;
    private EventAdapter adapter;

    private void bindView(List<MainBannerEntity> list) {
        try {
            adapter.update(list);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "bindView");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            MainBannerEntity entity = adapter.getItem(position);
            UiUtils.toBannerJump(context, entity);
            ClickStatisticsManager.getInstance().addClickEvent(Const.CLICK_EVENT_4, entity.getId());
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "EventFragment onItemClick");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_all:
                if (select_flag != 9) {
                    select_flag = 9;
                    changeSelector();
//                    if (events == null) events = new ArrayList<>();
                    adapter.update(getDataList(9));

                }
                break;
            case R.id.v_running:
                if (select_flag != 1) {
                    select_flag = 1;
                    changeSelector();
                    adapter.update(getDataList(1));
                }
                break;
            case R.id.v_ending:
                if (select_flag != 2) {
                    select_flag = 2;
                    changeSelector();
                    adapter.update(getDataList(2));
                }
                break;
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        return null;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = new String[]{};
        if (flag == Const.EEvent) {
            args = new String[]{
                    Contants.InfoID, UserinfoData.getInfoID(context)
            };
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object response, int flag) {

        if (flag == Const.EEvent) {
            EventEntity bean = (EventEntity) response;
            if (Contants.RESULT.equals(bean.getiResult())) {
                data = bean;
                bindBanner();
                bindEvent();
                try {
                    if (layout != null) {
                        layout.refreshFinish(PullToRefreshLayout.SUCCEED, true);
                    }
                } catch (Exception e) {
                    ExceptionUtils.ExceptionToUM(e, getActivity(), "EventFragment,下拉刷新失败");
                }
            } else {
                try {
                    if (layout != null) {
                        layout.refreshFinish(PullToRefreshLayout.FAIL, true);
                    }
                } catch (Exception e) {
                    ExceptionUtils.ExceptionToUM(e, getActivity(), "EventFragment,下拉刷新失败");
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
        try {
            if (layout != null) {
                layout.refreshFinish(PullToRefreshLayout.FAIL, true);
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, getActivity(), "EventFragment,下拉刷新失败");
        }
        Toast.makeText(context, "请求失败", Toast.LENGTH_SHORT).show();

    }

    /**
     * 设置banner视图
     */
    private void bindBanner() {

        List<MainBannerEntity> list = data.getBanner();
        list = UiUtils.sortBannerList(list);
        if (list != null && list.size() > 0) {
            findViewById(R.id.v_banner).setVisibility(View.VISIBLE);
            ArrayList<Fragment> fragments = new ArrayList<>();
            EventItemFragment imgFragment;
            Bundle bundle;
            for (int i = 0; i < list.size(); i++) {
                imgFragment = new EventItemFragment();
                MainBannerEntity entity = list.get(i);
                bundle = new Bundle();
                bundle.putSerializable(EventItemFragment.BANNER_DATA, entity);
                imgFragment.setArguments(bundle);
                fragments.add(imgFragment);
            }
            dcv.setDrawCricle(fragments.size(), 6, Color.parseColor("#4c4c4c"),
                    Color.parseColor("#FFFFFF"));
            dcv.redraw(0);
            MyFragmentAdapter fAdapter = new MyFragmentAdapter(getActivity().
                    getSupportFragmentManager(), fragments);
            vp.setAdapter(fAdapter);
            Index_Banner = 0;
            vp.addOnPageChangeListener(this);
            setBannerMove(list.size());
        } else {
            findViewById(R.id.v_banner).setVisibility(View.GONE);
        }
    }

    private boolean bannerFlag = false;
    private Runnable bannerMove = new Runnable() {
        @Override
        public void run() {
            if (ZYApplication.mainPageFlag) {
                if (bannerFlag && ZYApplication.EventMoveFlag) {
                    Index_Banner++;
                    if (Index_Banner >= MAX_BANNER) Index_Banner = 0;
                    mHandler.sendEmptyMessage(CHANGE_TOP_BANNER);
                }
                mHandler.postDelayed(bannerMove, 5000);
            }
        }
    };

    private int MAX_BANNER = 0;
    private int Index_Banner = 0;
    private boolean isBannerPost = false;

    private void setBannerMove(int size) {
        if (size > 1) {
            //初始化banner状态
            MAX_BANNER = size;
            Index_Banner = 0;
            bannerFlag = true;
            ZYApplication.EventMoveFlag = true;
            if (!isBannerPost) {
                isBannerPost = true;
                mHandler.postDelayed(bannerMove, 5000);
            }
        } else {
            bannerFlag = false;
            ZYApplication.EventMoveFlag = false;
            if (isBannerPost) {
                isBannerPost = false;
            }
        }

    }

    @Override
    public void onSingleTouch(boolean flag) {
        if (flag) {
            psv.setPullDownFlag(false);
            bannerFlag = false;
        } else {
            psv.setPullDownFlag(true);
            bannerFlag = true;
        }
    }

    private List<MainBannerEntity> events;

    /**
     * 设置活动视图
     */
    private void bindEvent() {
        findViewById(R.id.v_event).setVisibility(View.VISIBLE);
        events = data.getEvent();
        events = UiUtils.sortEventBannerList(events);
        if (events != null && events.size() > 0) {
            adapter.update(getDataList(select_flag));
            findViewById(R.id.line_bottom).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.line_bottom).setVisibility(View.GONE);
        }
    }

    /***
     *
     * 选项卡切换
     *
     */
    private void changeSelector() {
        if (select_flag == 1) {
            line_all.setVisibility(View.INVISIBLE);
            tv_event_all.setSelected(false);
            line_running.setVisibility(View.VISIBLE);
            line_ending.setVisibility(View.INVISIBLE);
            tv_event_running.setSelected(true);
            tv_event_ending.setSelected(false);
            line_running.startAnimation(left_in);
            if (index_old == 2) {
                line_ending.startAnimation(right_out);
            } else if (index_old == 9) {
                line_all.startAnimation(right_out);
            }
        } else if (select_flag == 2) {
            line_all.setVisibility(View.INVISIBLE);
            tv_event_all.setSelected(false);
            line_ending.setVisibility(View.VISIBLE);
            tv_event_ending.setSelected(true);
            line_running.setVisibility(View.INVISIBLE);
            tv_event_running.setSelected(false);
            if (index_old == 1) {
                line_ending.startAnimation(right_in);
                line_running.startAnimation(left_out);
            } else if (index_old == 9) {
                line_ending.startAnimation(left_in);
                line_all.startAnimation(right_out);
            }
        } else if (select_flag == 9) {
            line_all.setVisibility(View.VISIBLE);
            tv_event_all.setSelected(true);
            line_ending.setVisibility(View.INVISIBLE);
            tv_event_ending.setSelected(false);
            line_running.setVisibility(View.INVISIBLE);
            tv_event_running.setSelected(false);
            line_all.startAnimation(right_in);
            if (index_old == 1) {
                line_running.startAnimation(left_out);
            } else if (index_old == 2) {
                line_ending.startAnimation(left_out);
            }
        }
        index_old = select_flag;
    }

    private Animation left_in;
    private Animation left_out;
    private Animation right_in;
    private Animation right_out;

    /***
     *
     * 初始化动画
     *
     */
    private void initAnim() {
        if (left_in == null) left_in = AnimationUtils.loadAnimation(
                context, R.anim.line_left_in);
        if (left_out == null) left_out = AnimationUtils.loadAnimation(
                context, R.anim.line_left_out);
        if (right_in == null) right_in = AnimationUtils.loadAnimation(
                context, R.anim.line_right_in);
        if (right_out == null) right_out = AnimationUtils.loadAnimation(
                context, R.anim.line_right_out);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Index_Banner = position;
        dcv.redraw(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        initData();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        layout.loadmoreFinish(PullToRefreshLayout.LOADING_MAX, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        bannerFlag = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        bannerFlag = true;
    }

    private String getRandomMath() {
        String temp = (int) (Math.random() * 89999 + 10000) + "";
        return temp;
    }

    private static final int CHANGE_TOP_BANNER = 1;
    private final MyHandler mHandler = new MyHandler(this);

    /**
     * Handler静态内部类
     */
    private static class MyHandler extends Handler {
        private final WeakReference<EventFragment> mFragment;

        public MyHandler(EventFragment fragment) {
            mFragment = new WeakReference<>(fragment);
        }

        private EventFragment fragment;

        @Override
        public void handleMessage(Message msg) {
            fragment = mFragment.get();
            if (msg.what == CHANGE_TOP_BANNER) {
                fragment.changeTopBanner();
            }
        }
    }

    private void changeTopBanner() {
        vp.setCurrentItem(Index_Banner);
    }

    /**
     * 获取分类列表
     *
     * @param flag 1：进行中  2：已结束  3：全部
     * @return
     */
    private List<MainBannerEntity> getDataList(int flag) {
        List<MainBannerEntity> temp = new ArrayList<>();
        if (events != null && events.size() > 0) {
            if (flag == 1) {
                for (MainBannerEntity entity : events) {
                    if ("1".equals(entity.getStartNum())) {
                        temp.add(entity);
                    }
                }
            } else if (flag == 2) {
                for (MainBannerEntity entity : events) {
                    if ("2".equals(entity.getStartNum())) {
                        temp.add(entity);
                    }
                }
            } else if (flag == 9) {
                temp = events;
            }

        }
        return temp;
    }
}

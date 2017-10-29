package com.zeyuan.kyq.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.MyFragmentAdapter;
import com.zeyuan.kyq.app.BaseZyFragment;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.forcallback.SomeFinishCallback;
import com.zeyuan.kyq.biz.manager.FunctionGuideManager;
import com.zeyuan.kyq.biz.manager.PointManager;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.ForumDetailActivity;
import com.zeyuan.kyq.view.MoreCircleNewActivity;
import com.zeyuan.kyq.view.ReleaseForumActivity;
import com.zeyuan.kyq.widget.PullToRefresh.PullToRefreshLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 圈子页面
 */
public class CircleFragment extends BaseZyFragment implements OnItemClickListener, View.OnClickListener
        , PullToRefreshLayout.OnRefreshListener, SomeFinishCallback {

    private static final String TAG = "CircleFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_circle_2, container, false);
        initStatusBar();
        setViews();
        //  setListeners();//等发现页的圈子数据获取成功后再启用点击事件
        setPage();
        setRedPoint();
        return rootView;
    }

    private View v1;
    private View v2;
    private View v3;
    private View v4;
    private View line1;
    private View line2;
    private View line3;
    private View line4;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private View redpoint_circle_friend;
    private View redpoint_circle_project;
    private View redpoint_circle_local;
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private ViewPager vp_circle_content;
    MyFragmentAdapter myFragmentAdapter;
    private CircleFindFragment fragment1;
    //可加为好友的用户列表
    //private CircleFriendFragment fragment2;
    //好友发布的帖子
    private CircleFriendPostFragment fragment2;
    //我关注的
    //private CircleCareFragment fragment3;
    private CircleProjectFragment fragment3;
    private CircleLocalFragment fragment4;
    // private BaseZyFragment[] fragments = null;
    private int currentIndex = 0;
    private int selectedIndex = 0;
    private View v_ft;

    private void setViews() {

        v_ft = findViewById(R.id.iv_ft);

        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);
        v4 = findViewById(R.id.v4);
        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
        line3 = findViewById(R.id.line3);
        line4 = findViewById(R.id.line4);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        redpoint_circle_friend = findViewById(R.id.redpoint_circle_friend);
        redpoint_circle_project = findViewById(R.id.redpoint_circle_project);
        redpoint_circle_local = findViewById(R.id.redpoint_circle_local);
        vp_circle_content = (ViewPager) findViewById(R.id.vp_circle_content);
    }

    private void setListeners() {
        v_ft.setOnClickListener(this);
        v1.setOnClickListener(this);
        v2.setOnClickListener(this);
        v3.setOnClickListener(this);
        v4.setOnClickListener(this);
    }

    //设置红点
    private void setRedPoint() {
        String s = PointManager.getInstance().getPoint();
        if (s.contains("2"))
            redpoint_circle_friend.setVisibility(View.VISIBLE);
        else
            redpoint_circle_friend.setVisibility(View.GONE);
        if (s.contains("3"))
            redpoint_circle_project.setVisibility(View.VISIBLE);
        else
            redpoint_circle_project.setVisibility(View.GONE);
        if (s.contains("4"))
            redpoint_circle_local.setVisibility(View.VISIBLE);
        else
            redpoint_circle_local.setVisibility(View.GONE);
    }

    private void setPage() {
        fragment1 = new CircleFindFragment();
        fragment1.setCallback(this);
        fragment2 = new CircleFriendPostFragment();
        fragment2.setCallback(this);
        fragment3 = new CircleProjectFragment();
        fragment3.setCallback(this);
        fragment4 = new CircleLocalFragment();
        fragment4.setCallback(this);
        fragmentArrayList.add(fragment1);
        fragmentArrayList.add(fragment2);
        fragmentArrayList.add(fragment3);
        fragmentArrayList.add(fragment4);
        //   fragments = new BaseZyFragment[]{fragment1, fragment2, fragment3, fragment4};
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        myFragmentAdapter = new MyFragmentAdapter(fragmentManager, fragmentArrayList);
        vp_circle_content.setOffscreenPageLimit(3);//预加载三个fragment
        vp_circle_content.setAdapter(myFragmentAdapter);
        vp_circle_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedIndex = position;
                changeSelector();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.add(R.id.circle_content, fragment1);
//        //默认显示发现
//        transaction.show(fragment1);
//        transaction.commit();
//        //初始化选项卡
        changeSelector();
    }

    //子页面切换
    private void setPageChange() {
        vp_circle_content.setCurrentItem(selectedIndex);
        //选项卡切换
//        if (selectedIndex != currentIndex) {
//            changeSelector();
//
//            FragmentTransaction transaction = getActivity().getSupportFragmentManager()
//                    .beginTransaction();
//            transaction.hide(fragments[currentIndex]);
//            if (!fragments[selectedIndex].isAdded()) {
//                transaction.add(R.id.circle_content, fragments[selectedIndex]);
//            }
//            transaction.show(fragments[selectedIndex]);
//            transaction.commit();
//            currentIndex = selectedIndex;
//            v_ft.setVisibility(View.VISIBLE);
//        }
    }

    //选项卡切换
    private void changeSelector() {
        clearSelector();
        if (selectedIndex == 0) {
            tv1.setSelected(true);
            line1.setVisibility(View.VISIBLE);
            v_ft.setVisibility(View.VISIBLE);
        } else if (selectedIndex == 1) {
            tv2.setSelected(true);
            line2.setVisibility(View.VISIBLE);
            redpoint_circle_friend.setVisibility(View.GONE);
            v_ft.setVisibility(View.VISIBLE);
        } else if (selectedIndex == 2) {
            tv3.setSelected(true);
            line3.setVisibility(View.VISIBLE);
            redpoint_circle_project.setVisibility(View.GONE);
            v_ft.setVisibility(View.VISIBLE);
        } else if (selectedIndex == 3) {
            tv4.setSelected(true);
            line4.setVisibility(View.VISIBLE);
            redpoint_circle_local.setVisibility(View.GONE);
            v_ft.setVisibility(View.GONE);
        }
    }

    //清除所有选中状态
    private void clearSelector() {
        tv1.setSelected(false);
        tv2.setSelected(false);
        tv3.setSelected(false);
        tv4.setSelected(false);
        line1.setVisibility(View.INVISIBLE);
        line2.setVisibility(View.INVISIBLE);
        line3.setVisibility(View.INVISIBLE);
        line4.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        try {
//            fragments[currentIndex].refreshMore();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "onRefresh Error");
        }
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        try {
//            if (currentIndex > fragments.length - 1 || currentIndex < 0) {
//                doSomeFinish(null, 2, 2, true, null, null);
//            } else {
//                fragments[currentIndex].loadingMore();
//            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "onLoadMore Error");
        }
    }

    Handler handler = new Handler();

    @Override
    public void doSomeFinish(String tag, int flag, int type, boolean fit, String str, Object obj) {
        if (flag == CircleFindFragment.GETED_CIRCLEDATA) {//我的圈子数据获取成功
            setListeners();
            if (fit) {
                if (!TextUtils.isEmpty(UserinfoData.getCityID(getActivity()))&&!UserinfoData.getCityID(getActivity()).equals("0"))
                    tv4.setText(MapDataUtils.getCircleValues(ZYApplication.DefaultCircles.get(0).get(0)));
            }
            //发帖引导
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!getActivity().isFinishing()) {
                        int[] xy = new int[2];
                        v_ft.getLocationInWindow(xy);
                        int width = v_ft.getWidth();
                        int height = v_ft.getHeight();
                        FunctionGuideManager.getInstance().showReleaseForumGuide(getActivity(), xy, width, height);
                    }
                }
            }, 500);
        } else if (flag == Const.SELECT_OTHER_FRAGMENT) {
            switch (str) {
                case Const.Circle_Friend:
                    selectedIndex = 1;
                    setPageChange();
                    break;
                case Const.Circle_Project:
                    selectedIndex = 2;
                    setPageChange();
                    break;
                case Const.Circle_Local:
                    selectedIndex = 3;
                    setPageChange();
                    break;
            }
        } else if (flag == Const.SELECTED_CITY) {
            tv4.setText(str);
        }
    }

    @Override
    public void doEmptyPage(String tag, int flag, int type, boolean fit, String str, Object obj) {
        selectedIndex = 1;
        setPageChange();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            startActivity(new Intent(getActivity(), ForumDetailActivity.class)
                    .putExtra(Const.FORUM_ID, String.valueOf(id)));
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, getActivity(), TAG);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_circle:
                Factory.onEvent(getActivity(), Const.EVENT_MoreCircle, null, null);
                startActivityForResult(new Intent(getActivity(), MoreCircleNewActivity.class), 0);
                break;
            case R.id.v1:
                selectedIndex = 0;
                setPageChange();
                break;
            case R.id.v2:
                selectedIndex = 1;
                setPageChange();
                break;
            case R.id.v3:
                selectedIndex = 2;
                setPageChange();
                break;
            case R.id.v4:
                selectedIndex = 3;
                setPageChange();
                break;
            case R.id.iv_ft://点击发帖
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
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(Const.TAG.ZY_VIEW_LIFE, "Circle onPause");
    }
}

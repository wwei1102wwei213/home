package com.zeyuan.kyq.view;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.MainMorePagerAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.utils.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/9.
 * 从主页副作用进来
 */
public class SideEffectActivity extends BaseActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {
    private ViewPager viewPager;
    private View view1;
    private View view2;
    private View view3;
    private View view4;
    private RadioGroup rg;
    private List<View> data;
    private MainMorePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_effect);
        try {
            initTitlebar("潜在副作用");
            initView();
            setListener();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "SideEffectActivity");
        }
    }

    private void setListener() {
        try {
            rg.setOnCheckedChangeListener(this);
            rg.check(R.id.liver);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "SideEffectActivity");
        }

    }

    private void initView() {
        try {
            viewPager = (ViewPager) findViewById(R.id.viewpager);
            data = new ArrayList<View>();
            rg = (RadioGroup) findViewById(R.id.rg);
            viewPager = (ViewPager) findViewById(R.id.viewpager);
            view1 = findViewById(R.id.view1);
            view2 = findViewById(R.id.view2);
            view3 = findViewById(R.id.view3);
            view4 = findViewById(R.id.view4);
            initviews();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "SideEffectActivity");
        }
    }

    private void initviews() {

        try {
            View liver = LayoutInflater.from(this).inflate(R.layout.item_sideeffect_viewpager, null);
            View kidney = LayoutInflater.from(this).inflate(R.layout.item_sideeffect_viewpager, null);
            View heart = LayoutInflater.from(this).inflate(R.layout.item_sideeffect_viewpager, null);
            View blood = LayoutInflater.from(this).inflate(R.layout.item_sideeffect_viewpager, null);
            //在这儿初始化这个4个界面的数据

            data.add(liver);
            data.add(kidney);
            data.add(heart);
            data.add(blood);
            pagerAdapter = new MainMorePagerAdapter(data);
            viewPager.setAdapter(pagerAdapter);
            viewPager.addOnPageChangeListener(this);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "SideEffectActivity");
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        try {
            switch (position) {
                case 0:
                    rg.check(R.id.liver);

                    break;
                case 1:
                    rg.check(R.id.kidney);

                    break;
                case 2:
                    rg.check(R.id.heart);

                    break;
                case 3:
                    rg.check(R.id.blood);

                    break;
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "SideEffectActivity");
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        try {
            switch (checkedId) {
                case R.id.liver:
                    setViews(0);
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.kidney:
                    setViews(1);
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.heart:
                    setViews(2);
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.blood:
                    setViews(3);
                    viewPager.setCurrentItem(3);
                    break;
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "SideEffectActivity");
        }
    }

    private void setViews(int position) {
        try {
            View[] views = new View[]{view1, view2, view3, view4};
            for (int i = 0; i < views.length; i++) {
                if (i == position) {
                    views[position].setVisibility(View.VISIBLE);
                } else {
                    views[i].setVisibility(View.INVISIBLE);
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "SideEffectActivity");
        }
    }
}

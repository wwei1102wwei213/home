package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.EditInfoEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.MyFragmentAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.fragment.staging.DigitStagingFragment;
import com.zeyuan.kyq.fragment.staging.TNMStagingFragment;

import java.util.ArrayList;

import static com.zeyuan.kyq.R.id.tv_next;

/**
 * Created by Administrator on 2017/8/25.
 * 选择癌症分期
 */

public class SelectCancerStagingActivity extends BaseActivity implements View.OnClickListener {
    ArrayList<Fragment> fragments = new ArrayList<>();
    //从之前页面传递过来的用户选择的数据
    EditInfoEntity editInfoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_staging);
        initView();
        editInfoEntity = (EditInfoEntity) getIntent().getSerializableExtra("editInfoEntity");
        if (editInfoEntity == null) {
            showToast("数据传递错误！");
            finish();
        }
    }

    ViewPager vp_fragment;
    MyFragmentAdapter myFragmentAdapter;
    TextView tv_staging_type_hint;
    TextView tv_change_staging_type;
    DigitStagingFragment digitStagingFragment;
    TNMStagingFragment tnmStagingFragment;

    private void initView() {
        View iv_back = findViewById(R.id.iv_back);
        View tv_next = findViewById(R.id.tv_next);
        iv_back.setOnClickListener(this);
        tv_next.setOnClickListener(this);

        digitStagingFragment = new DigitStagingFragment();
        tnmStagingFragment = new TNMStagingFragment();
        fragments.add(digitStagingFragment);
        fragments.add(tnmStagingFragment);
        vp_fragment = (ViewPager) findViewById(R.id.vp_fragment);
        myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments);
        vp_fragment.setAdapter(myFragmentAdapter);

        tv_staging_type_hint = (TextView) findViewById(R.id.tv_staging_type_hint);
        tv_change_staging_type = (TextView) findViewById(R.id.tv_change_staging_type);
        tv_change_staging_type.setOnClickListener(this);
        vp_fragment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setSelectStatus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setSelectStatus(0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case tv_next:
                checkSelect();
                break;
            case R.id.tv_change_staging_type:
                if (currentFragment == 0) {
                    setSelectStatus(1);
                    vp_fragment.setCurrentItem(1);
                } else {
                    setSelectStatus(0);
                    vp_fragment.setCurrentItem(0);
                }
                break;

        }
    }

    int currentFragment = 0;

    private void setSelectStatus(int witch) {

        switch (witch) {
            case 0://数字分期
                tv_change_staging_type.setText(getString(R.string.text_tnm));
                tv_staging_type_hint.setText(getString(R.string.text_switch_tnm));
                break;
            case 1://tnm分期
                tv_change_staging_type.setText(getString(R.string.text_digit_staging));
                tv_staging_type_hint.setText(getString(R.string.text_switch_digit));
                break;
        }
        currentFragment = witch;
    }

    String selectedDigitID;

    private void checkSelect() {
        selectedDigitID = "";
        if (vp_fragment.getCurrentItem() == 0) {
            selectedDigitID = digitStagingFragment.getSelectedDigitID();
        } else {
            selectedDigitID = tnmStagingFragment.getSelectedDigitID();
        }
        editInfoEntity.setPeriodID(selectedDigitID);
        startActivityForResult(new Intent(this, SelectCurrentCaseActivity.class).putExtra("editInfoEntity",editInfoEntity),9);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==9&&resultCode==RESULT_OK){
            setResult(RESULT_OK);
            finish();
        }
    }
}

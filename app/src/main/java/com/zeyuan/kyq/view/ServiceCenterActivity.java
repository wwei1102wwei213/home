package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.app.BaseZyFragment;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.fragment.other.ServiceCenterFragment;
import com.zeyuan.kyq.fragment.other.ServiceEventFragment;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;

/**
 * Created by Administrator on 2017/3/21.
 *
 * 服务中心
 *
 * @author wwei
 */
public class ServiceCenterActivity extends BaseActivity implements View.OnClickListener{

    private int type;
    //当前窗口标识
    private int currentIndex;
    //选中窗口标识
    private int selectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_center);
        type = getIntent().getIntExtra(Const.FLAG_SERVICE_CENTER,0);
        if (type==0){
            currentIndex = 0;
            selectedIndex = 0;
        }else {
            currentIndex = 1;
            selectedIndex = 1;
        }
        initView();
        initListener();
        setPage();
    }

    //服务中心点击区域
    private View v1;
    //公益活动点击区域
    private View v2;
    //大病众筹点击区域
    private View v3;
    //服务底部选中线
    private View line1;
    //公益底部选中线
    private View line2;
    //服务文本控件
    private TextView tv_type_1;
    //公益文本控件
    private TextView tv_type_2;
    //设置视图
    private void initView(){

        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);
        line1 = findViewById(R.id.line_1);
        line2 = findViewById(R.id.line_2);
        tv_type_1 = (TextView)findViewById(R.id.tv_type_1);
        tv_type_2 = (TextView)findViewById(R.id.tv_type_2);

    }

    private void initListener(){
        v1.setOnClickListener(this);
        v2.setOnClickListener(this);
        v3.setOnClickListener(this);
        findViewById(R.id.v_close).setOnClickListener(this);

    }

    //服务窗口
    private ServiceCenterFragment fragment1;
    //公益窗口
    private ServiceEventFragment fragment2;
    //窗口数组
    private BaseZyFragment[] fragments = null;
    //设置窗口
    private void setPage(){
        fragment1 = new ServiceCenterFragment();
        fragment2 = new ServiceEventFragment();
        fragments = new BaseZyFragment[]{fragment1,fragment2};
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.body, fragments[currentIndex]);
        transaction.show(fragments[currentIndex]);
        transaction.commit();
        //初始化选项卡
        changeSelector();
    }

    //子页面切换
    private void setPageChange(){
        //选项卡切换
        if (selectedIndex!=currentIndex){
            changeSelector();
            // 不是当前的
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            // 当前hide
            transaction.hide(fragments[currentIndex]);
            // show你选中
            if (!fragments[selectedIndex].isAdded()) {
                // 以前没添加过
                transaction.add(R.id.body,fragments[selectedIndex]);
            }
            transaction.show(fragments[selectedIndex]);
            transaction.commit();
            currentIndex = selectedIndex;
        }
    }

    //选项卡切换
    private void changeSelector(){
        clearSelector();
        if (selectedIndex==0){
            ZYApplication.EventMoveFlag = false;
            tv_type_1.setSelected(true);
            line1.setVisibility(View.VISIBLE);
        }else if(selectedIndex==1){
            ZYApplication.EventMoveFlag = true;
            tv_type_2.setSelected(true);
            line2.setVisibility(View.VISIBLE);
        }
    }

    //清除所有选中状态
    private void clearSelector(){
        tv_type_1.setSelected(false);
        tv_type_2.setSelected(false);
        line1.setVisibility(View.GONE);
        line2.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.v_close:
                finish();
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
                startActivity(new Intent(this, ShowDiscuzActivity.class)
                        .putExtra(Const.SHOW_HTML_MAIN_TOP, "http://www.8fchou.com/web/page/toPublishProject.htm?" + "kaqid=" +
                                UiUtils.getRandomMath() + UserinfoData.getInfoID(this) + "&type=2&coi=kaq"));
                break;
        }
    }


}

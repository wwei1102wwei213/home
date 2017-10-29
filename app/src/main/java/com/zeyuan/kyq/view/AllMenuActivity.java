package com.zeyuan.kyq.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.zeyuan.kyq.Entity.HomePageBean;
import com.zeyuan.kyq.Entity.HomePageEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.HomeGvAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.FragmentCallBack;
import com.zeyuan.kyq.biz.manager.ClickStatisticsManager;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/11.
 * <p>
 * 更多功能页面
 *
 * @author wwei
 */
public class AllMenuActivity extends BaseActivity implements HttpResponseInterface, FragmentCallBack {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_menu);
        initView();
        initData();
    }

    private HomeGvAdapter adapter;

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        GridView gv = (GridView) findViewById(R.id.gv);
        adapter = new HomeGvAdapter(this, new ArrayList<HomePageEntity>());
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomePageEntity entity = adapter.getItem(position);
                UiUtils.toMenuJump(AllMenuActivity.this, entity, AllMenuActivity.this, false, AllMenuActivity.this);
                ClickStatisticsManager.getInstance().addClickEvent(Const.CLICK_EVENT_2, entity.getId());
            }
        });
    }

    private void initData() {
        Factory.postPhp(this, Const.PApi_App_shortcut);
    }

    public void toBottomPage(int flag) {
        exit = flag;
        finish();
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == Const.PApi_App_shortcut) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {

        if (flag == Const.PApi_App_shortcut) {
            HomePageBean bean = (HomePageBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                List<HomePageEntity> list = bean.getData();
                if (list != null && list.size() > 0) {
                    adapter.update(list);
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
        if (flag == Const.PApi_App_shortcut) {
            showToast("网络请求失败");
        }
    }

    @Override
    public void dataCallBack(String str, int flag, String tag, Object obj) {

    }

    private int exit = 0;

    @Override
    public void finish() {
        if (exit == 2 || exit == 4) {
            setResult(Const.RESULT_CODE_ALL_MENU_FLAG, getIntent().putExtra(Const.RESULT_ALL_MENU_FLAG, exit));
        }
        super.finish();
    }
}

package com.zeyuan.kyq.view;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.umeng.message.entity.UMessage;
import com.zeyuan.kyq.Entity.PushNewEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.SettingNewsAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.db.DBHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/29.
 *
 * 消息中心
 *
 * @author wwei
 */
public class SettingNewsActivity extends BaseActivity {


    private static final String APP_PARAM = "appparam";
    private static final String BREAK = "=";
    private static final String INDEX = "index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_setting);
        try {
            initWhiteTitle(getString(R.string.setting_news));
            initView();
        }catch (Exception e){

        }
    }

    private void initView(){
        try {
            List<UMessage> list = new ArrayList<>();
            List<PushNewEntity> data = DBHelper.getInstance().queryPush(ZYApplication.application);
            if(data!=null&&data.size()!=0){
                for(PushNewEntity entity:data){
                    UMessage msg = new UMessage(new JSONObject(entity.getMsg()));
                    list.add(msg);
                }
            }
            ListView lv = (ListView)findViewById(R.id.lv_news);
            SettingNewsAdapter adapter = new SettingNewsAdapter(this,data);
            lv.setAdapter(adapter);
        }catch (Exception e){

        }
    }

    /**
     * @param listView
     */
    private void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


}

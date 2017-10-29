package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.UseHelperAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.InitZYUtils;
import com.zeyuan.kyq.utils.UserinfoData;

/**
 * Created by Administrator on 2016/3/21.
 * 帮助页，已废弃
 *
 */
public class UserHelperActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_help);
        initViews();
        InitZYUtils.initFeedback(this);
    }

    /***
     *
     * 初始化视图控件
     *
     */
    private void  initViews(){
        try {

            TextView tv = (TextView)findViewById(R.id.tp_use_help);
            tv.setText(Html.fromHtml("亲爱的抗癌圈用户(ID:"+ UserinfoData.getInfoID(this)+")，若以下帮助内容依然无法解答您的疑惑，或者你在使用过程中遇到更多问题，欢迎使用" +
                    "<font color=\"#ffff00\">意见反馈</font>" +
                    "功能给我们提意见"));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FeedbackAPI.openFeedbackActivity();
                }
            });
            /**设置listview*/
            ListView lv = (ListView)findViewById(R.id.lv_use_help);
            String[] titles = new String[]{"登录或注册","初步建立患者信息","填写分期","修改患者信息","查询肿瘤发展、副作用或并发症的解决办法","查看或编辑患者病历",
                    "编辑指标或症状记录","更改或添加病例","添加症状","指标记录","与癌友交流或求助癌友","关注自己感兴趣的圈子","查看收藏的文章、已发布的帖子或回复的内容",
                    "如何推荐给好友或癌友","向官方反馈意见或建议"};
            final int[] poss = {0,4,8,10,17,20,21,24,26,28,31,33,35,37,39};
            UseHelperAdapter adapter = new UseHelperAdapter(this,R.layout.item_use_help_list,titles,poss);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(UserHelperActivity.this, HelpSymptomActivity.class);
                    intent.putExtra("HelpList_position", poss[position]);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });

            ImageView back = (ImageView)findViewById(R.id.iv_help_toback);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"initViews,userHelperActivity");
        }
    }
}

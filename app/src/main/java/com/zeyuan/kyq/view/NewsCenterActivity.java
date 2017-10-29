/*
package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.umeng.message.entity.UMessage;
import com.zeyuan.kyq.Entity.PushMsgBean;
import com.zeyuan.kyq.Entity.PushMsgEntity;
import com.zeyuan.kyq.Entity.PushNewEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.SettingNewsAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.db.DBHelper;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.UserinfoData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*/
/**
 * Created by Administrator on 2016/8/1.
 *
 * @author wwei
 *//*

public class NewsCenterActivity extends BaseActivity implements SettingNewsAdapter.JumpCallBack,HttpResponseInterface,
        TabLayout.OnTabSelectedListener{

    private static final String APP_PARAM = "appparam";
    private static final String BREAK = "=";
    private static final String INDEX = "index";
    private SettingNewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent();
        setContentView(R.layout.activity_news_center);
        initStatusBar();
        initWhiteTitle("消息中心");
        initView();
        initData();
    }

    private TabLayout tl;
    private ViewPager vp;
    private void initView(){
        tl = (TabLayout)findViewById(R.id.tl);
        tl.setTabMode(TabLayout.MODE_FIXED);
        tl.addTab(tl.newTab().setText("全部"));
        tl.addTab(tl.newTab().setText("消息"));
        tl.addTab(tl.newTab().setText("评论"));
        tl.addTab(tl.newTab().setText("粉丝"));
        tl.addTab(tl.newTab().setText("点赞"));
        tl.addTab(tl.newTab().setText("其他"));
        tl.setOnTabSelectedListener(this);
        vp = (ViewPager)findViewById(R.id.vp);
    }

    private void initData(){
        Factory.postPhp(this, Const.PShowInfoMsg);
    }

    private void setData(PushMsgBean bean){
        List<PushMsgEntity> list = bean.getData();
        try {
            if (list==null||list.size()==0) list = new ArrayList<>();
            List<PushNewEntity> data = DBHelper.getInstance().queryPush(ZYApplication.application);
            if(data!=null&&data.size()!=0){
                for (PushNewEntity entity:data){
                    PushMsgEntity pme = new PushMsgEntity();
                    pme.setTime(entity.getTime()+"");
                    pme.setType(999);
                    pme.setRead(entity.getRead());
                    pme.setOldPush(entity.getMsg());
                    list.add(pme);
                }
            }
            Collections.sort(list, new Comparator<PushMsgEntity>() {
                @Override
                public int compare(PushMsgEntity p1, PushMsgEntity p2) {
                    return  Integer.valueOf(p2.getTime()) - Integer.valueOf(p1.getTime());
                }
            });
            LogCustom.i("ZYS","List:"+list.toString());
        }catch (Exception e){

        }
        if (list!=null&&list.size()!=0){

        }else {
            findViewById(R.id.vp).setVisibility(View.GONE);
            findViewById(R.id.fl).setVisibility(View.VISIBLE);
            setEmptyPageFragment(R.mipmap.no_news_relust, "未收到任何消息", "", null, R.id.fl);
        }
    }


    private void initOldView(){
        try {
            List<PushNewEntity> data = DBHelper.getInstance().queryPush(ZYApplication.application);
            if(data!=null&&data.size()!=0){
                Collections.sort(data, new Comparator<PushNewEntity>() {
                    @Override
                    public int compare(PushNewEntity p1, PushNewEntity p2) {
                        return  (int)p2.getTime() - (int)p1.getTime();
                    }
                });
                ListView lv = (ListView)findViewById(R.id.lv_news);
                adapter = new SettingNewsAdapter(this,data);
                lv.setAdapter(adapter);
                LogCustom.i("ZYS","PUSH DATA:"+data.toString());
            }else {

            }
        }catch (Exception e){

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void finish() {
        int form = getIntent().getIntExtra(Const.INTENT_FROM,0);
        if(form == Const.FM){
            afterFinish();
        }
        super.finish();
    }

    @Override
    public void toJump(int jump, UMessage msg) {
        Map<String,String> map = msg.extra;
        String IsHaveStep = UserinfoData.getIsHaveStep(this);
        boolean flag = false;
        if("0".equals(IsHaveStep)){
            flag = true;
        }
        Intent intent = new Intent();
        switch (jump){
            case 0:

                break;
            case 1:
                intent.setClassName(this,ShowDiscuzActivity.class.getName());
                try {
                    String url =  map.get("url");
                    if(url.contains("?")){
                        url += "&kaq="+ DataUtils.getRandomMath()+ UserinfoData.getInfoID(this) + "&lt=2&Type=2" ;
                    }else {
                        url += "?kaq="+ DataUtils.getRandomMath()+UserinfoData.getInfoID(this) + "&lt=2&Type=2" ;
                    }
                    intent.putExtra(Const.SHOW_HTML_MAIN_TOP, url);
                } catch (Exception e){
                    intent.putExtra(Const.SHOW_HTML_MAIN_TOP, "http://bbs.kaqcn.com");
                }
                break;
            case 2:
                intent.setClassName(this, ArticleDetailActivity.class.getName());
                try {
                    String temp =  map.get(APP_PARAM);
                    String[] args = temp.split(BREAK);
                    intent.putExtra(Const.INTENT_ARTICLE_ID, args[1]);
                }catch (Exception e){
                    intent.putExtra(Const.INTENT_ARTICLE_ID, "3067");
                }
                break;
            case 3:
                intent.setClassName(this, ForumDetailActivity.class.getName());
                try {
                    String temp =  map.get(APP_PARAM);
                    String[] args = temp.split(BREAK);
                    intent.putExtra(Const.FORUM_ID, args[1]);
                }catch (Exception e){
                    intent.putExtra(Const.FORUM_ID, "1047");
                }
                break;
            case 4:

                break;
            case 5:
                intent.setClassName(this, NewCircleActivity.class.getName());
                try {
                    String temp =  map.get(APP_PARAM);
                    String[] args = temp.split(BREAK);
                    intent.putExtra(Contants.CircleID, args[1]);
                }catch (Exception e){
                    intent.putExtra(Contants.CircleID, "30");
                }
                break;
            case 12:
                intent.setClassName(this, ReleaseForumActivity.class.getName());
                try {
                    String temp =  map.get(APP_PARAM);
                    if(!TextUtils.isEmpty(temp)){
                        String[] args = temp.split(BREAK);
                        List<String> list = new ArrayList<>();
                        if(args[1].contains(",")){
                            String[] args1 = args[1].split(",");
                            for (String str : args1){
                                list.add(str);
                            }
                        }else {
                            list.add(args[1]);
                        }
                        intent.putExtra(Const.DEFAULT_CIRCLE, (Serializable)list);
                    }
                }catch (Exception e){

                }
                break;
            case 6:
                intent.setClassName(this, MyReplyActivity.class.getName());
                break;
            case 7:
                intent.setClassName(this, StepsActivity.class.getName());
                break;
            case 8:
                intent.setClassName(this, NewsCenterActivity.class.getName());
                break;
            case 9:
                intent.setClassName(this, PatientDetailActivity.class.getName());
                break;
            case 10:
                intent.setClassName(this, FindSymtomActivity.class.getName());
                break;
            case 11:
                intent.setClassName(this, PerfectDataActivity.class.getName());
                break;
            case 13:
                intent.setClassName(this, MoreCircleNewActivity.class.getName());
                break;
            case 14:
                intent.setClassName(this, MainActivity.class.getName());
                break;
            case 15:
                intent.setClassName(this, HeadLineActivity.class.getName());
                try {
                    String temp =  map.get(APP_PARAM);
                    String catid = map.get("catid");
                    if(!TextUtils.isEmpty(catid)&&!TextUtils.isEmpty(temp)){
                        String[] args = temp.split(BREAK);
                        intent.putExtra(Const.HEAD_LIST_TAG_URL, catid).putExtra(Const.HEAD_LIST_INFO_TEXT,
                                args[1]);
                    }
                }catch (Exception e){

                }
                break;
        }
        if(jump!=0&&jump!=8){
            if(flag&&(jump==7||jump==10||jump==11)){

            }else {
                startActivity(intent);
            }
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String,String> map = new HashMap<>();
        if (tag == Const.PShowInfoMsg){
            map.put(Contants.InfoID,UserinfoData.getInfoID(this));
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PShowInfoMsg){
            PushMsgBean bean = (PushMsgBean)response;
            if (Const.RESULT.equals(bean.IResult())){
                setData(bean);
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

    }
}
*/
package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.umeng.message.entity.UMessage;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.MyFragmentAdapter;
import com.zeyuan.kyq.adapter.SettingNewsAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.fragment.msg.MsgFragment;
import com.zeyuan.kyq.fragment.msg.PushMsgFragment;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/1.
 * <p>
 * 消息中心
 *
 * @author wwei
 */
public class NewsCenterActivity extends BaseActivity implements SettingNewsAdapter.JumpCallBack {

    private static final String APP_PARAM = "appparam";
    private static final String BREAK = "=";
    private static final String INDEX = "index";
    private SettingNewsAdapter adapter;
    private TabLayout tl_msg_type;
    private ViewPager vp_msg_type;
    private MyFragmentAdapter myFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_center);
        initWhiteTitle("消息中心");
        initView();
    }

    private void initView() {
        try {

            tl_msg_type = (TabLayout) findViewById(R.id.tl_msg_type);
            vp_msg_type = (ViewPager) findViewById(R.id.vp_msg_type);

            View right = findViewById(R.id.right);
            ArrayList<Fragment> fragments = generateFragments();
            String[] titles = new String[]{"全部", "@我的", "评论", "其他"};
            myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments, titles);
//            List<UMessage>ist = new ArrayList<>();
            vp_msg_type.setOffscreenPageLimit(3);
            vp_msg_type.setAdapter(myFragmentAdapter);
            tl_msg_type.setupWithViewPager(vp_msg_type);
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(NewsCenterActivity.this, MessageSettingsActivity.class));
                }
            });
//            List<PushNewEntity> data = DBHelper.getInstance().queryPush(ZYApplication.application);
//            if (data != null && data.size() != 0) {
//                Collections.sort(data, new Comparator<PushNewEntity>() {
//                    @Override
//                    public int compare(PushNewEntity p1, PushNewEntity p2) {
//                        return (int) p2.getTime() - (int) p1.getTime();
//                    }
//                });
//                ListView lv = (ListView)findViewById(R.id.lv_news);
//                adapter = new SettingNewsAdapter(this,data);
//                lv.setAdapter(adapter);
//                LogCustom.i("ZYS", "PUSH DATA:" + data.toString());
//            } else {
//                findViewById(R.id.v_content).setVisibility(View.GONE);
//                findViewById(R.id.fl).setVisibility(View.VISIBLE);
//                setEmptyPageFragment(R.mipmap.no_news_relust, "未收到任何消息", "", null, R.id.fl);
//            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "NewsCenterActivity initView()");
        }
    }

    private ArrayList<Fragment> generateFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            MsgFragment msgFragment = new MsgFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type", i);
            msgFragment.setArguments(bundle);
            fragments.add(msgFragment);
        }
        PushMsgFragment pushMsgFragment = new PushMsgFragment();
        fragments.add(pushMsgFragment);
        return fragments;
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (adapter!=null)
        adapter.notifyDataSetChanged();*/
    }

    @Override
    public void finish() {
        int form = getIntent().getIntExtra(Const.INTENT_FROM, 0);
        if (form == Const.FM) {
            afterFinish();
        }
        super.finish();
    }

    @Override
    public void toJump(int jump, UMessage msg) {
        Map<String, String> map = msg.extra;
        String IsHaveStep = UserinfoData.getIsHaveStep(this);
        boolean flag = false;
        if ("0".equals(IsHaveStep)) {
            flag = true;
        }
        Intent intent = new Intent();
        switch (jump) {
            case 0:

                break;
            case 1:
                intent.setClassName(this, ShowDiscuzActivity.class.getName());
                try {
                    String url = map.get("url");
                    if (url.contains("?")) {
                        url += "&kaq=" + DataUtils.getRandomMath() + UserinfoData.getInfoID(this) + "&lt=2&Type=2";
                    } else {
                        url += "?kaq=" + DataUtils.getRandomMath() + UserinfoData.getInfoID(this) + "&lt=2&Type=2";
                    }
                    intent.putExtra(Const.SHOW_HTML_MAIN_TOP, url);
                } catch (Exception e) {
                    intent.putExtra(Const.SHOW_HTML_MAIN_TOP, "http://bbs.kaqcn.com");
                }
                break;
            case 2:
                intent.setClassName(this, ArticleDetailActivity.class.getName());
                try {
                    String temp = map.get(APP_PARAM);
                    String[] args = temp.split(BREAK);
                    intent.putExtra(Const.INTENT_ARTICLE_ID, args[1]);
                } catch (Exception e) {
                    intent.putExtra(Const.INTENT_ARTICLE_ID, "3067");
                }
                break;
            case 3:
                intent.setClassName(this, ForumDetailActivity.class.getName());
                try {
                    String temp = map.get(APP_PARAM);
                    String[] args = temp.split(BREAK);
                    intent.putExtra(Const.FORUM_ID, args[1]);
                } catch (Exception e) {
                    intent.putExtra(Const.FORUM_ID, "1047");
                }
                break;
            case 4:

                break;
            case 5:
                intent.setClassName(this, NewCircleActivity.class.getName());
                try {
                    String temp = map.get(APP_PARAM);
                    String[] args = temp.split(BREAK);
                    intent.putExtra(Contants.CircleID, args[1]);
                } catch (Exception e) {
                    intent.putExtra(Contants.CircleID, "30");
                }
                break;
            case 12:
                intent.setClassName(this, ReleaseForumActivity.class.getName());
                try {
                    String temp = map.get(APP_PARAM);
                    if (!TextUtils.isEmpty(temp)) {
                        String[] args = temp.split(BREAK);
                        List<String> list = new ArrayList<>();
                        if (args[1].contains(",")) {
                            String[] args1 = args[1].split(",");
                            for (String str : args1) {
                                list.add(str);
                            }
                        } else {
                            list.add(args[1]);
                        }
                        intent.putExtra(Const.DEFAULT_CIRCLE, (Serializable) list);
                    }
                } catch (Exception e) {

                }
                break;
            case 6:
                intent.setClassName(this, MyReplyActivity.class.getName());
                break;
            case 7:
                intent.setClassName(this, MedicalRecordActivity.class.getName());
                break;
            case 8:
                intent.setClassName(this, NewsCenterActivity.class.getName());
                break;
            case 9:
                intent.setClassName(this, PatientDetailActivity.class.getName());
                break;
            case 10:
                intent.setClassName(this, HomeSymptomActivity.class.getName());
                break;
            case 11:
                intent.setClassName(this, PerfectDataActivity.class.getName());
                break;
            case 13:
                intent.setClassName(this, MoreCircleNewActivity.class.getName());
                break;
            case 14:
                intent.setClassName(this, MainActivity.class.getName());
                break;
            case 15:
                /*intent.setClassName(this, HeadLineActivity.class.getName());
                try {
                    String temp =  map.get(APP_PARAM);
                    String catid = map.get("catid");
                    if(!TextUtils.isEmpty(catid)&&!TextUtils.isEmpty(temp)){
                        String[] args = temp.split(BREAK);
                        intent.putExtra(Const.HEAD_LIST_TAG_URL, catid).putExtra(Const.HEAD_LIST_INFO_TEXT,
                                args[1]);
                    }
                }catch (Exception e){

                }*/
                break;
        }
        if (jump != 0 && jump != 8) {
            if (flag && (jump == 7 || jump == 10 || jump == 11)) {

            } else {
                startActivity(intent);
            }
        }
    }
}

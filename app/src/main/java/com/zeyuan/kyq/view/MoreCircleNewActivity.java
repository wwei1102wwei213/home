package com.zeyuan.kyq.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.CircleNewEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.CircleOtherAdapter;
import com.zeyuan.kyq.adapter.FindCircleDetailAdapter;
import com.zeyuan.kyq.adapter.MyFragmentAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.FollowBean;
import com.zeyuan.kyq.biz.forcallback.AdapterCallback;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.SearchStringBiz;
import com.zeyuan.kyq.fragment.other.CircleCancerFragment;
import com.zeyuan.kyq.fragment.other.CircleCityFragment;
import com.zeyuan.kyq.fragment.other.CircleOtherFragment;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/9.
 *
 * 新更多圈子页面
 *
 * @author wwei
 */
public class MoreCircleNewActivity extends BaseActivity implements TabLayout.OnTabSelectedListener,
        AdapterView.OnItemClickListener,View.OnClickListener,
        FindCircleDetailAdapter.FollowCircleListener ,HttpResponseInterface ,TextWatcher
        ,View.OnKeyListener,SearchStringBiz.SearchStringInterface,AdapterCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_circle_new);
        initTop();
        initView();
        initData();
    }

    private void getFactoryForFlag(int flag){
        Factory.post(this, flag);
    }

    private List<CircleNewEntity> all;
    private void initData() {
        all = (List<CircleNewEntity>) Factory.getData(Const.N_DataCircleForSearch);
        new SearchStringBiz(SearchStringBiz.CIRCLE_NEW,this,all).execute();
    }

    private void initTop(){
        findViewById(R.id.iv_back_search).setOnClickListener(this);
        findViewById(R.id.rl_search_input).setOnClickListener(this);
    }

    private TabLayout tl;
    private ViewPager vp;
    /***
     *
     * 初始化视图控件
     *
     */
    private void initView() {

        initSearchView();

        try {

            findViewById(R.id.iv_back_search).setOnClickListener(this);
            findViewById(R.id.tv_dismiss_input).setOnClickListener(this);
            tl = (TabLayout)findViewById(R.id.tl_more_circle);
            tl.setTabMode(TabLayout.MODE_FIXED);
            tl.addTab(tl.newTab().setText("其他"));
            tl.addTab(tl.newTab().setText("同城圈"));
            tl.addTab(tl.newTab().setText("抗癌圈"));

            vp = (ViewPager)findViewById(R.id.vp_circle);
            ArrayList<Fragment> fs = new ArrayList<>();
            CircleOtherFragment f1 = new CircleOtherFragment();
            fs.add(f1);
            CircleCityFragment f2 = new CircleCityFragment();
            fs.add(f2);
            CircleCancerFragment f3 = new CircleCancerFragment();
            fs.add(f3);

            MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(),fs);
            tl.setOnTabSelectedListener(this);
            TabLayout.TabLayoutOnPageChangeListener listener = new TabLayout.
                    TabLayoutOnPageChangeListener(tl);
            vp.addOnPageChangeListener(listener);
            vp.setAdapter(adapter);

        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"MoreCircleActivity");
        }

    }

    private TextView tv_search_hint;
    private EditText et;
    private ImageView clean;
    private TextView dismiss;
    private View v_search;
    private View v_record;
    private View v_content;
    private ListView lv_search;
    private CircleOtherAdapter mSearchAdapter;
    private List<String> mSearchList;
    private boolean mSearchFlag = false;
    private List<String> mFristList;
    private List<String> mPYList;
    private void initSearchView(){
        try {

            et = (EditText)findViewById(R.id.et_search);
            et.setCursorVisible(false);
            clean = (ImageView)findViewById(R.id.back_search_text);
            dismiss = (TextView)findViewById(R.id.tv_dismiss_input);
            tv_search_hint = (TextView)findViewById(R.id.tv_search_hint);
            v_content = findViewById(R.id.v_content);
            v_search = findViewById(R.id.v_search_body);
            v_record = findViewById(R.id.v_record);

            lv_search = (ListView)findViewById(R.id.lv_search);
            mSearchList = new ArrayList<>();
            mSearchAdapter = new CircleOtherAdapter(this,mSearchList,this);
            lv_search.setAdapter(mSearchAdapter);
            lv_search.setOnItemClickListener(this);

            et.setOnClickListener(this);
            et.addTextChangedListener(this);//搜索框内容改变监听
            et.setOnKeyListener(this);
            clean.setOnClickListener(this);
            dismiss.setOnClickListener(this);

        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"initSearchView");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_search:
                finish();
                break;
            case R.id.rl_search_input:

                break;
            case R.id.tv_dismiss_input:
                dismissInput();
                et.setText("");
                et.setCursorVisible(false);

                break;
            case R.id.et_search:
//                if(!et.setCursorVisible();){
                    et.setCursorVisible(true);
//                }
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

//        if(!et.isCursorVisible()){
            et.setCursorVisible(true);
//        }
        String text = et.getText() + "";
        if ("".equals(text)) {
            if (clean.getVisibility() != View.GONE) {
                clean.setVisibility(View.GONE);
            }
            toBackView();
            return;
        }else {
            if (clean.getVisibility() != View.VISIBLE) {
                clean.setVisibility(View.VISIBLE);
            }
            if (v_content!=null && v_content.getVisibility()!=View.GONE){
                v_content.setVisibility(View.GONE);
                v_content.startAnimation(AnimationUtils.loadAnimation(this,R.anim.activity_alpha_out));
            }
            if(v_search!=null&&v_search.getVisibility()!=View.VISIBLE){
                v_search.setVisibility(View.VISIBLE);
                v_search.startAnimation(AnimationUtils.loadAnimation(this,R.anim.activity_alpha_in_det150));
            }
        }
        int mTextLength = et.getText().length();

        if (mTextLength > 0) {
            clean.setVisibility(View.VISIBLE);
        }

        mSearchList = getSearchList(text, all);
        if(mSearchList.size()==0){
            tv_search_hint.setText(Html.fromHtml("搜索<font color=\"#17cbd1\">“"+text+"”</font>,无结果。"));
        }else if(mSearchList.size()>0){
            tv_search_hint.setText(Html.fromHtml("搜索<font color=\"#17cbd1\">“"+text+"”</font>,搜索结果如下："));
        }
        mSearchAdapter.updata(mSearchList);
    }

    @Override
    public void setData(List<List<String>> o) {
          if(o!=null){
              mPYList = o.get(0);
              mFristList = o.get(1);
              mSearchFlag = true;
          }
    }

    @Override
    public void forAdapterCallback(int pos, int tag, String id, boolean flag, Object obj) {
        if(!TextUtils.isEmpty(id)){
            circleID = id;
            if(flag){
                followOrcancel = "1";
            }else {
                followOrcancel = "2";
            }
            folowCircle();
        }
    }

    private void toBackView(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(240);
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                try {
                    if (v_search.getVisibility()!=View.GONE){
                        v_search.setVisibility(View.GONE);
                        v_search.startAnimation(AnimationUtils.loadAnimation(MoreCircleNewActivity.this,
                                R.anim.activity_alpha_out_fast));
                    }
                    if (v_content.getVisibility()!=View.VISIBLE){
                        v_content.setVisibility(View.VISIBLE);
                        v_content.startAnimation(AnimationUtils.loadAnimation(MoreCircleNewActivity.this,
                                R.anim.activity_alpha_in));
                    }
                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e,"toBackView");
                }
            }
        }.execute(new Void[]{});
    }

    private String circleID;
    private String followOrcancel;////1关注 2.取消

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();

        return map;
    }

    private String CircleID;
    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if(flag == Const.EGetCancerForum){
            args = new String[]{
                    Contants.InfoID,UserinfoData.getInfoID(this),
                    Contants.CancerID, UserinfoData.getCancerID(this)
            };
        }else if(flag == Const.EFollowCircle){
            String type = "";
            if (Integer.valueOf(circleID) < 6999&&Integer.valueOf(circleID)>999) {
                type = "1";
            } else {
                type = "2";
            }
            args = new String[]{
                    Contants.InfoID,UserinfoData.getInfoID(this),
                    Contants.CircleID, circleID,
                    Contants.followOrcancel, followOrcancel,
                    "type", type
            };
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object t, int position) {
        try {
            if (position == Const.EFollowCircle) {
                FollowBean bean = (FollowBean) t;
                UiUtils.setFollowUI(this, followOrcancel, bean.getIResult(), circleID);
                mSearchAdapter.updata();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"MoreCircleActivity");
        }
    }

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {

    }

    @Override
    public void showError(int tag) {

    }

    private void toCircle(String circleID) {
        try {
            Intent intent = new Intent(this, NewCircleActivity.class);
            intent.putExtra(Contants.CircleID, circleID);
            startActivity(intent);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"MoreCircleActivity");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CircleID = mSearchAdapter.getItem(position);
        startActivity(new Intent(this,NewCircleActivity.class).putExtra(Contants.CircleID,CircleID));

    }

    @Override
    public void followCircle(BaseAdapter adapter, boolean isFollow, int position) {
        try {
            circleID = (String) adapter.getItem(position);
            if (isFollow) {
                followOrcancel = "1";
            } else {
                followOrcancel = "2";
            }
            folowCircle();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"MoreCircleActivity");
        }
    }

    /***
     * 发送请求，提交圈子关注状态
     *
     */
    private void folowCircle() {
        try {
            getFactoryForFlag(Const.EFollowCircle);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"MoreCircleActivity");
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        vp.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    //获取搜索结果
    private List<String> getSearchList(String str,List<CircleNewEntity> data){
        List<String> temp = new ArrayList<>();
        try {
            String name;
            String pyname;
            String pyfrist;
            str = str.replace(" ","");
            for(int i=0;i<data.size();i++){
                name = data.get(i).getCircleName().toLowerCase();

                if(name.startsWith(str.toLowerCase())||name.contains(str.toLowerCase())){
                    temp.add(data.get(i).getCircleID());
                }else{
                    if(mSearchFlag){
                        pyfrist = mFristList.get(i);
                        if(pyfrist.startsWith(str.toLowerCase())||pyfrist.contains(str.toLowerCase())){
                            temp.add(data.get(i).getCircleID());
                        }else{
                            pyname = mPYList.get(i);
                            if(pyname.contains(str.toLowerCase())){
                                temp.add(data.get(i).getCircleID());
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"SearchSymptomActivity");
        }
        return  temp;
    }

    @Override
    public void finish() {
        dismissInput();
        int form = getIntent().getIntExtra(Const.INTENT_FROM,0);
        if(form == Const.FM){
            afterFinish();
        }
        super.finish();
    }

    private void dismissInput(){
        try {
            InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm!=null&&imm.isActive(et)){
                imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"输入法收起错误");
        }
    }
}

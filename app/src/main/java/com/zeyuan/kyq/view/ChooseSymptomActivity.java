package com.zeyuan.kyq.view;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.ChooseSymptomAdapter;
import com.zeyuan.kyq.adapter.SymptomBodyRecyclerAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.widget.MyLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/2/27.
 *
 * 选择症状页面
 *
 * @author wwei
 */
public class ChooseSymptomActivity extends BaseActivity implements SymptomBodyRecyclerAdapter.onTabClickListener
        ,View.OnClickListener,AdapterView.OnItemClickListener,ChooseSymptomAdapter.onChooseChangeListener
        ,MyLayout.OnSoftKeyboardListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_symptom);
        initData();
    }

    private LinkedHashMap<String, List<String>> allData;
    private void initData(){
        allData = MapDataUtils.getBodyPos();
        if (allData==null||allData.size()==0){
            showToast("配置数据获取出错");
            finish();
        }else {
            initView();
        }
    }

    //选择按钮
    private TextView tv_choose;
    //左边列表控件
    private RecyclerView rv;
    //右边列表控件
    private ListView lv;
    //左边列表适配器
    private SymptomBodyRecyclerAdapter adapter_left;
    //右边列表适配器
    private ChooseSymptomAdapter adapter;
    //选中的症状id
    private ArrayList<String> check;
    //底部控件
    private View v_bottom;
    private TextView tv_bottom;
    private MyLayout layout;
    private View v_edit;
    private EditText et;
    private View v_half;

    private void initView(){
        //左边列表控件设置
        rv = (RecyclerView)findViewById(R.id.rv_left);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        List<String> temp = new ArrayList<>();
        for (String key:allData.keySet()){
            temp.add(key);
        }
        check = getIntent().getStringArrayListExtra("CheckedList");
        if (check == null) check = new ArrayList<>();
        adapter_left = new SymptomBodyRecyclerAdapter(this,temp,0,this,true,check);
        rv.setAdapter(adapter_left);
        //右边列表控件设置
        lv = (ListView)findViewById(R.id.lv);
        adapter  = new ChooseSymptomAdapter(this,allData.get(temp.get(0)),check,this,0);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        //其他控件
        tv_choose = (TextView)findViewById(R.id.tv_choose);
        tv_choose.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        try {
            v_bottom = findViewById(R.id.v_bottom);
            v_bottom.setOnClickListener(this);
            tv_bottom = (TextView)findViewById(R.id.tv_bottom);
            layout = (MyLayout)findViewById(R.id.ml);
            layout.setOnSoftKeyboardListener(this);
            findViewById(R.id.send_message).setOnClickListener(this);//输入确定点击事件
            findViewById(R.id.btn_cancel).setOnClickListener(this);//输入取消点击事件
            v_edit = findViewById(R.id.v_edit);
            et = (EditText)findViewById(R.id.et);
            v_half = findViewById(R.id.v_half);
            v_half.setOnClickListener(this);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"initView");
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_choose:
                if ((check==null||check.size()==0)&&TextUtils.isEmpty(content)){
                    showToast("请选择症状或手动输入症状");
                    return;
                }
                IsChoose = true;
                finish();
                break;
            case R.id.v_bottom:
                if (!flag){
                    showSoftInput();
                }
                break;
            case R.id.send_message:
                String temp = et.getText().toString().trim();
                if (TextUtils.isEmpty(temp)) {
                    tv_bottom.setHint(getString(R.string.hint_choose_symptom));
                    content = "";
                }else {
                    tv_bottom.setText(temp);
                    content = temp;
                }
                cancelInput();
                break;
            case R.id.btn_cancel:
            case R.id.v_half:
                cancelInput();
                break;
        }
    }

    private void cancelInput(){
        try {
            hideKeyboard(getCurrentFocus().getWindowToken());
            onHidden();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "收起输入法出错");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.click(position);
    }

    @Override
    public void onRecyclerLeftClick(String id, int pos) {
        adapter.update(allData.get(id),pos);
    }

    @Override
    public void onChooseChanged(int index, List<String> check) {
        adapter_left.updateNumber(index, check);
        if (check!=null&&check.size()>0){
            tv_choose.setText("保存("+check.size()+")");
        }else {
            tv_choose.setText("保存");
        }
    }

    @Override
    public void onShown() {
        flag = true;
        //放在这里显示效果比较好
        v_half.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHidden() {
        if (flag){
            hideInput();
            flag = false;
        }
    }

    private boolean flag = false;// 这个控制隐藏键盘的时候 回调只被调用一次
    private String content = "";
    private void showSoftInput() {
        try {
            v_edit.setVisibility(View.VISIBLE);
            et.setFocusableInTouchMode(true);
            et.requestFocus();
            InputMethodManager inputManager =
                    (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(et, 0);
            Animation a = AnimationUtils.loadAnimation(this, R.anim.dialog_enter_anim);
            v_edit.startAnimation(a);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "ChooseSymptomActivity");
        }
    }

    private void hideInput() {
        try {
            v_half.setVisibility(View.GONE);
//            String temp = et.getText().toString();
            v_edit.setVisibility(View.GONE);

        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"ChooseSymptomActivity");
        }
    }

    private void hideKeyboard(IBinder token) {
        try {
            if (token != null) {
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"ChooseSymptomActivity");
        }
    }

    private boolean IsChoose = false;
    @Override
    public void finish() {
        if (IsChoose){
            setResult(Const.REQUEST_CODE_CHOOSE_SYMPTOM,getIntent().putExtra("CheckList", check).putExtra("SymptomRemark",content));
        }
        super.finish();
    }
}

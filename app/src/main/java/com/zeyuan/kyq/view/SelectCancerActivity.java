package com.zeyuan.kyq.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.Entity.EditInfoEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.manager.FunctionGuideManager;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CustomScrollView;
import com.zeyuan.kyq.widget.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/8.
 * 开启个性化治疗 选择癌种
 */

public class SelectCancerActivity extends BaseActivity implements View.OnClickListener, HttpResponseInterface {
    private Map<String, List<String>> data;
    private Map<String, String> values;
    private int type = 1;
    private String[] args = {"29", "33", "36", "34", "37", "48", "51", "55", "72", "86", "94", "96", "97", "148"};
    private String[] names = new String[14];
    private String cancer_frist;
    private String cancer_second;
    private String cancer_three;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cancer);
        initData();
        initView();
    }

    /**
     * 获取癌种配置数据
     */
    private void initData() {
        data = (Map<String, List<String>>) Factory.getData(Const.N_DataCancerData);
        values = (Map<String, String>) Factory.getData(Const.N_DataCancerValues);
        for (int i = 0; i < args.length; i++) {
            names[i] = values.get(args[i]);
        }
    }

    private View view_back;
    private TextView tv11;
    private TextView tv12;
    private TextView tv13;
    private TextView tv21;
    private TextView tv22;
    private TextView tv23;
    private TextView tv31;
    private TextView tv32;
    private TextView tv33;
    private TextView tv41;
    private TextView tv42;
    private TextView tv43;
    private TextView tv51;
    private TextView tv52;

    private View view_first;
    private View view_second;
    private View view_next;
    private ImageView iv_top_cancer;
    private TextView tv_top_cancer;
    private FlowLayout flow_second;
    private FlowLayout flow_three;
    private CustomScrollView sv;
    private TextView tv_next1;
    //    private TextView tv_next2;
    private View line_cancer_choose_top;
    private View line_three;

    private View view_first_down;
    private View view_first_top;
    private View view_second_top;
    private ImageView iv_back;

    /***
     *
     * 设置视图控件
     *
     */
    private void initView() {

//        try {
//            View statusBar1 = findViewById(R.id.statusBar1);
//            ViewGroup.LayoutParams params1 = statusBar1.getLayoutParams();
//            params1.height = getStatusBarHeight();
//            statusBar1.setLayoutParams(params1);
//        } catch (Exception e) {
//            ExceptionUtils.ExceptionSend(e, "");
//        }
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
        //设置标题
        TextView tv = (TextView) findViewById(R.id.tv_white_title);
        TextView tv_hint = (TextView) findViewById(R.id.tv_hint);
        String current = UserinfoData.getCurrent(getApplicationContext());

        if (!TextUtils.isEmpty(current) && (current.equals("1") || current.equals("2"))) {
            tv.setText("请选择您关注的癌种");
            tv_hint.setText("对待癌症的态度，也就是对待人生的态度！");
        } else {
            tv.setText("请选择检查结果");
            tv_hint.setText("没关系，一切都会好起来的！");
        }
        view_back = findViewById(R.id.ll_return_back);
        view_back.setOnClickListener(this);

        //一级视图
        tv11 = (TextView) findViewById(R.id.tv_11);
        tv12 = (TextView) findViewById(R.id.tv_12);
        tv13 = (TextView) findViewById(R.id.tv_13);
        tv21 = (TextView) findViewById(R.id.tv_21);
        tv22 = (TextView) findViewById(R.id.tv_22);
        tv23 = (TextView) findViewById(R.id.tv_23);
        tv31 = (TextView) findViewById(R.id.tv_31);
        tv32 = (TextView) findViewById(R.id.tv_32);
        tv33 = (TextView) findViewById(R.id.tv_33);
        tv41 = (TextView) findViewById(R.id.tv_41);
        tv42 = (TextView) findViewById(R.id.tv_42);
        tv43 = (TextView) findViewById(R.id.tv_43);
        tv51 = (TextView) findViewById(R.id.tv_51);
        tv52 = (TextView) findViewById(R.id.tv_52);
        tv11.setOnClickListener(this);
        tv12.setOnClickListener(this);
        tv13.setOnClickListener(this);
        tv21.setOnClickListener(this);
        tv22.setOnClickListener(this);
        tv23.setOnClickListener(this);
        tv31.setOnClickListener(this);
        tv32.setOnClickListener(this);
        tv33.setOnClickListener(this);
        tv41.setOnClickListener(this);
        tv42.setOnClickListener(this);
        tv43.setOnClickListener(this);
        tv51.setOnClickListener(this);
        tv52.setOnClickListener(this);
        view_first = findViewById(R.id.ll_first_cancer_body);
        view_second = findViewById(R.id.ll_cancer_second_body);
        view_next = findViewById(R.id.ll_show_next_tv);
        sv = (CustomScrollView) findViewById(R.id.csv_cancer_choose);

        //设置二级视图
        iv_top_cancer = (ImageView) findViewById(R.id.iv_top_cancer);
        tv_top_cancer = (TextView) findViewById(R.id.tv_top_cancer);
        flow_second = (FlowLayout) findViewById(R.id.flow_layout_second_cancer_items);
        flow_three = (FlowLayout) findViewById(R.id.flow_layout_three_cancer_items);

        line_cancer_choose_top = findViewById(R.id.line_cancer_choose_top);
        line_three = findViewById(R.id.line_two);
        tv_next1 = (TextView) findViewById(R.id.tv_next1);
//        tv_next2 = (TextView)v.findViewById(R.id.tv_next2);

        tv_next1.setOnClickListener(this);
//        tv_next2.setOnClickListener(this);
//        v.findViewById(R.id.rl_second_cancer_top).setOnClickListener(this);
        view_first_down = findViewById(R.id.view_first_down);
        view_first_top = findViewById(R.id.view_first_top);
        view_second_top = findViewById(R.id.view_second_top);
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.tv_11:
                    if (tv11.isSelected()) {
                        tv11.setSelected(false);
                        setGoneNextTV();
                    } else {
                        clearFirstSelect();
                        tv11.setSelected(true);
                        initSecondView(0);
                    }
                    break;
                case R.id.tv_12:
                    if (tv12.isSelected()) {
                        tv12.setSelected(false);
                        setGoneNextTV();
                    } else {
                        clearFirstSelect();
                        tv12.setSelected(true);
                        initSecondView(1);
                    }
                    break;
                case R.id.tv_13:
                    if (tv13.isSelected()) {
                        tv13.setSelected(false);
                        setGoneNextTV();
                    } else {
                        clearFirstSelect();
                        tv13.setSelected(true);
                        initSecondView(2);
                    }

                    break;
                case R.id.tv_21:
                    if (tv21.isSelected()) {
                        tv21.setSelected(false);
                        setGoneNextTV();
                    } else {
                        clearFirstSelect();
                        tv21.setSelected(true);
                        initSecondView(3);
                    }

                    break;
                case R.id.tv_22:
                    if (tv22.isSelected()) {
                        tv22.setSelected(false);
                        setGoneNextTV();
                    } else {
                        clearFirstSelect();
                        tv22.setSelected(true);
                        initSecondView(4);
                    }

                    break;
                case R.id.tv_23:
                    if (tv23.isSelected()) {
                        tv23.setSelected(false);
                        setGoneNextTV();
                    } else {
                        clearFirstSelect();
                        tv23.setSelected(true);
                        initSecondView(5);
                    }

                    break;
                case R.id.tv_31:
                    if (tv31.isSelected()) {
                        tv31.setSelected(false);
                        setGoneNextTV();
                    } else {
                        clearFirstSelect();
                        tv31.setSelected(true);
                        initSecondView(6);
                    }
                    break;
                case R.id.tv_32:
                    if (tv32.isSelected()) {
                        tv32.setSelected(false);
                        setGoneNextTV();
                    } else {
                        clearFirstSelect();
                        tv32.setSelected(true);
                        initSecondView(7);
                    }
                    break;
                case R.id.tv_33:
                    if (tv33.isSelected()) {
                        tv33.setSelected(false);
                        setGoneNextTV();
                    } else {
                        clearFirstSelect();
                        tv33.setSelected(true);
                        initSecondView(8);
                    }

                    break;
                case R.id.tv_41:
                    if (tv41.isSelected()) {
                        tv41.setSelected(false);
                        setGoneNextTV();
                    } else {
                        clearFirstSelect();
                        tv41.setSelected(true);
                        initSecondView(9);
                    }

                    break;
                case R.id.tv_42:
                    if (tv42.isSelected()) {
                        tv42.setSelected(false);
                        setGoneNextTV();
                    } else {
                        clearFirstSelect();
                        tv42.setSelected(true);
                        initSecondView(10);
                    }

                    break;
                case R.id.tv_43:
                    if (tv43.isSelected()) {
                        tv43.setSelected(false);
                        setGoneNextTV();
                    } else {
                        clearFirstSelect();
                        tv43.setSelected(true);
                        initSecondView(11);
                    }

                    break;

                case R.id.tv_51:
                    if (tv51.isSelected()) {
                        tv51.setSelected(false);
                        setGoneNextTV();
                    } else {
                        clearFirstSelect();
                        tv51.setSelected(true);
                        initSecondView(12);
                    }

                    break;
                case R.id.tv_52:
                    if (tv52.isSelected()) {
                        tv52.setSelected(false);
                        setGoneNextTV();
                    } else {
                        clearFirstSelect();
                        tv52.setSelected(true);
                        initSecondView(13);
                    }

                    break;
                case R.id.tv_next1:
                    Log.i("ZYS", "next1");
                    toNextPage(1);
                    break;
                case R.id.tv_next2:
                    toNextPage(2);
                    break;
                case R.id.ll_return_back:
                    setViewChangeFirst();
                    break;
                case R.id.iv_back:
                    finish();
                    break;
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "cancertypefragment");
        }
    }

    /**
     * 获取子癌种
     *
     * @param id CancerID
     * @return 子癌种列表
     */
    private List<String> getCancerItem(String id) {
        if (data == null || TextUtils.isEmpty(id)) return null;
        return data.get(id);
    }

    /***
     *
     * 根据id刷新二级视图
     *
     * @param pos
     */
    private void initSecondView(int pos) {
        try {
            Log.i(Const.TAG.ZY_TEST, "id");
            type = 1;
            if (view_next.getVisibility() != View.VISIBLE) {
                view_next.setVisibility(View.VISIBLE);
            }
            cancer_frist = args[pos];
            List<String> temp = getCancerItem(args[pos]);
            if (temp == null || temp.size() == 0) {
                LogCustom.i(Const.TAG.ZY_TEST, "temp:");

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sv.fullScroll(ScrollView.FOCUS_DOWN);
                        } catch (Exception e) {

                        }
                    }
                });
            } else {
                LogCustom.i(Const.TAG.ZY_TEST, "temp:" + temp.toString());
                freshSecondView(temp, pos);
                setViewChangeSecond();
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "CancerChoose,initSecondView");
        }
    }

    private void setGoneNextTV() {
        if (view_next.getVisibility() != View.GONE) {
            view_next.setVisibility(View.GONE);
        }
    }

    /***
     *
     * 切换二级视图UI
     *
     */
    private void setViewChangeSecond() {

        //title返回键显示
        iv_back.setVisibility(View.GONE);
        if (view_back.getVisibility() != View.VISIBLE) view_back.setVisibility(View.VISIBLE);
        view_back.startAnimation(AnimationUtils.loadAnimation(this, R.anim.activity_alpha_in));
        line_cancer_choose_top.setVisibility(View.INVISIBLE);

        //body视图切换

        if (view_first_top.getVisibility() != View.GONE) {
            view_first_top.setVisibility(View.GONE);
            view_first_top.startAnimation(AnimationUtils.loadAnimation(this, R.anim.activity_alpha_out_fast));
        }
        if (view_second_top.getVisibility() != View.VISIBLE) {
            view_second_top.setVisibility(View.VISIBLE);
            view_second_top.startAnimation(AnimationUtils.loadAnimation(this, R.anim.activity_alpha_in));
        }
        if (view_second.getVisibility() != View.VISIBLE) {
            view_second.setVisibility(View.VISIBLE);
            view_second.startAnimation(AnimationUtils.loadAnimation(this, R.anim.activity_translate_up_deilay));
        }
        if (view_first_down.getVisibility() != View.GONE) {
            view_first_down.setVisibility(View.GONE);
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    sv.fullScroll(ScrollView.FOCUS_UP);
                } catch (Exception e) {

                }
            }
        });
    }

    /***
     *
     * 切换一级视图UI
     *
     */
    private void setViewChangeFirst() {

        clearFirstSelect();
        if (view_next.getVisibility() != View.GONE) {
            view_next.setVisibility(View.GONE);
        }

        line_cancer_choose_top.setVisibility(View.VISIBLE);
        //title返回键显示
        if (view_back.getVisibility() != View.GONE) view_back.setVisibility(View.GONE);
        view_back.startAnimation(AnimationUtils.loadAnimation(this, R.anim.activity_alpha_out));

        //body视图切换

        if (view_first_top.getVisibility() != View.VISIBLE) {
            view_first_top.setVisibility(View.VISIBLE);
            view_first_top.startAnimation(AnimationUtils.loadAnimation(this, R.anim.activity_alpha_in));
        }
        if (view_second_top.getVisibility() != View.GONE) {
            view_second_top.setVisibility(View.GONE);
            view_second_top.startAnimation(AnimationUtils.loadAnimation(this, R.anim.activity_alpha_out_fast));
        }
        if (view_second.getVisibility() != View.GONE) {
            view_second.setVisibility(View.GONE);
            view_second.startAnimation(AnimationUtils.loadAnimation(this, R.anim.activity_translate_down));
        }
        iv_back.setVisibility(View.VISIBLE);
        if (view_first_down.getVisibility() != View.VISIBLE) {
            view_first_down.setVisibility(View.VISIBLE);
        }
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    sv.fullScroll(ScrollView.FOCUS_UP);
                } catch (Exception e) {

                }
            }
        });

    }

    private List<TextView> secondList;
    private List<TextView> threeList;

    /***
     *
     * 刷新二级视图
     *
     * @param list
     * @param pos
     */
    private void freshSecondView(List<String> list, final int pos) {

        try {
            setFristCancerImage(pos);
            try {
                tv_top_cancer.setText(names[pos]);
                flow_second.removeAllViews();
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "freshSecondView");
            }
            secondList = new ArrayList<>();
            for (final String str : list) {
                final String temp = values.get(str);
                if (!TextUtils.isEmpty(temp)) {
                    final TextView tv_temp = (TextView) LayoutInflater.from(this).inflate(R.layout.tv_cancer_second_item, null);
                    secondList.add(tv_temp);
                    tv_temp.setText(temp);
                    tv_temp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            type = 2;
                            cancer_second = str;
                            List<String> list = data.get(str);
                            if (tv_temp.isSelected()) {

                            } else {
                                clearSecondSelect();
                                tv_temp.setSelected(true);
                            }
                            freshThreeView(list, str);
                        }
                    });
                    flow_second.addView(tv_temp);
                }
            }
            final TextView tv_temp = (TextView) LayoutInflater.from(this).inflate(R.layout.tv_cancer_second_item, null);
            secondList.add(tv_temp);
            tv_temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!tv_temp.isSelected()) {
                        clearSecondSelect();
                        freshThreeView(new ArrayList<String>(), "");
                        tv_temp.setSelected(true);
                        type = 1;
                        cancer_frist = args[pos];
                    }
                }
            });
            flow_second.addView(tv_temp);

            flow_three.removeAllViews();
            line_three.setVisibility(View.GONE);

        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "freshSecondView");
        }
    }

    /***
     *
     * 设置二级视图对应的大类癌种图片
     *
     * @param pos
     */
    private void setFristCancerImage(int pos) {
        if (pos < 6) {
            switch (pos) {
                case 0:
                    iv_top_cancer.setImageResource(R.mipmap.cancer2_fei);
                    break;
                case 1:
                    iv_top_cancer.setImageResource(R.mipmap.cancer2_gan);
                    break;
                case 2:
                    iv_top_cancer.setImageResource(R.mipmap.cancer2_chang);
                    break;
                case 3:
                    iv_top_cancer.setImageResource(R.mipmap.cancer2_wei);
                    break;
                case 4:
                    iv_top_cancer.setImageResource(R.mipmap.cancer2_ru);
                    break;
                case 5:
                    iv_top_cancer.setImageResource(R.mipmap.cancer2_shiguan);
                    break;
            }
        } else {
            iv_top_cancer.setImageResource(R.mipmap.cancer1_other);
        }
    }

    /***
     *
     * 刷新三级视图
     *
     * @param list
     */
    private void freshThreeView(List<String> list, final String id) {
        flow_three.removeAllViews();
        if (list == null || list.size() == 0) {
            line_three.setVisibility(View.GONE);
        } else {
            line_three.setVisibility(View.VISIBLE);
            threeList = new ArrayList<>();
            for (final String str : list) {
                final String temp = values.get(str);
                if (!TextUtils.isEmpty(temp)) {
                    final TextView tv_temp = (TextView) LayoutInflater.from(this).inflate(R.layout.tv_cancer_second_item_red, null);
                    threeList.add(tv_temp);
                    tv_temp.setText(temp);
                    tv_temp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LogCustom.i("ZYS", "three1");
                            if (tv_temp.isSelected()) {

                            } else {
                                LogCustom.i("ZYS", "three2");
                                clearThreeSelect();
                                tv_temp.setSelected(true);
                                type = 3;
                                cancer_three = str;
                            }
                        }
                    });
                    flow_three.addView(tv_temp);
                }
            }
            final TextView tv_temp = (TextView) LayoutInflater.from(this).inflate(R.layout.tv_cancer_second_item_red, null);
            threeList.add(tv_temp);
            tv_temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!tv_temp.isSelected()) {
                        clearThreeSelect();
                        tv_temp.setSelected(true);
                        type = 2;
                        cancer_second = id;
                    }
                }
            });
            flow_three.addView(tv_temp);
        }
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    sv.fullScroll(ScrollView.FOCUS_DOWN);
                } catch (Exception e) {
                    LogCustom.i("", e.getMessage());
                }
            }
        });
    }

    private void clearSecondSelect() {
        if (secondList == null || secondList.size() == 0) {

        } else {
            for (TextView tv : secondList) {
                tv.setSelected(false);
            }
        }
    }

    private void clearThreeSelect() {
        if (threeList == null || threeList.size() == 0) {

        } else {
            for (TextView tv : threeList) {
                tv.setSelected(false);
            }
        }
    }

    private void clearFirstSelect() {
        tv11.setSelected(false);
        tv12.setSelected(false);
        tv13.setSelected(false);
        tv21.setSelected(false);
        tv22.setSelected(false);
        tv23.setSelected(false);
        tv31.setSelected(false);
        tv32.setSelected(false);
        tv33.setSelected(false);
        tv41.setSelected(false);
        tv42.setSelected(false);
        tv43.setSelected(false);
        tv51.setSelected(false);
        tv52.setSelected(false);
    }

    /***
     *
     * 点击确定去下个页面
     *
     * @param flag
     */
    private void toNextPage(int flag) {

        if (type == 2) {
            if (!TextUtils.isEmpty(cancer_second)) {
                LogCustom.i("ZYS", "Choose cancerID:" + cancer_second);
                setDoubleClickDelay(tv_next1);
                dealWithSelect(cancer_second);
            } else {
                Toast.makeText(this, "选择癌种出错，请重新选择", Toast.LENGTH_SHORT).show();
            }
        } else if (type == 3) {
            if (!TextUtils.isEmpty(cancer_three)) {
                LogCustom.i("ZYS", "Choose cancerID:" + cancer_three);
                setDoubleClickDelay(tv_next1);
                dealWithSelect(cancer_three);
            } else {
                Toast.makeText(this, "选择癌种出错，请重新选择", Toast.LENGTH_SHORT).show();
            }
        } else if (type == 1) {
            if (!TextUtils.isEmpty(cancer_frist)) {
                LogCustom.i("ZYS", "Choose cancerID:" + cancer_frist);
                setDoubleClickDelay(tv_next1);
                dealWithSelect(cancer_frist);
            } else {
                Toast.makeText(this, "请选择癌种", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //处理选择结果
    private void dealWithSelect(String id) {
        if (TextUtils.isEmpty(id)) return;
        Map<String, String> defaultCancerID = (Map<String, String>) Factory.getData(Const.N_DataDefaultCancer);
        if (defaultCancerID == null || defaultCancerID.size() == 0) return;
        String defaultID = defaultCancerID.get(id);
        if (TextUtils.isEmpty(defaultID)) {
            toNext(id);
        } else {
            setDefaultCancerHint(defaultID);
        }
    }

    private void setDefaultCancerHint(final String id) {
        String temp = values.get(id);
        ZYDialog dialog = new ZYDialog.Builder(this)
                .setTitle("自动为您选择 - " + temp)
                .setMessage("系统已自动为您分配患癌概率最高的癌种")
                .disMissLine(true)
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        toNext(id);
                    }
                }).create();
        dialog.show();
    }

    //当前选择的癌种
    String selected_cancerId;

    //去选择确诊时间或完成个性化治疗的资料填写
    private void toNext(String id) {
        selected_cancerId = "";
        if (TextUtils.isEmpty(id)) {
            showToast("请重新选择癌种！");
            return;
        } else {
            selected_cancerId = id;
        }
        // 用户注册时选择的当前状态：1 预防，2 未确诊的时候，选完癌种直接完成个性化治疗
        String current = UserinfoData.getCurrent(getApplicationContext());

        if (!TextUtils.isEmpty(current) && (current.equals("1") || current.equals("2"))) {
            Factory.postPhp(this, Const.PApi_EditInfo);
        } else {
            EditInfoEntity editInfoEntity = new EditInfoEntity();
            editInfoEntity.setCancerID(id);
            startActivityForResult(new Intent(this, SelectDiagnosisTimeActivity.class).putExtra("editInfoEntity", editInfoEntity), 9);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 9 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> params = new HashMap<>();
        if (tag == Const.PApi_EditInfo) {
            //Type=1  时 其余参数才有效 不然只有 careUid 参数 和 InfoID有效
            params.put("Type", "1");
            params.put("InfoID", UserinfoData.getInfoID(getApplicationContext()));
            params.put("CancerID", selected_cancerId);
            params.put("IsHaveStep", "1");
        }
        return params;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if (flag == Const.EGetPatientDetail) {
            args = new String[]{
                    Contants.InfoID, UserinfoData.getInfoID(getApplicationContext()),
                    Const.ISHAVESTEP, UserinfoData.getIsHaveStep(getApplicationContext())
            };
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PApi_EditInfo) {
            PhpUserInfoBean phpUserInfoBean = (PhpUserInfoBean) response;
            if (phpUserInfoBean.getiResult().equals(Const.RESULT)) {
                UserinfoData.saveCancerID(getApplicationContext(), selected_cancerId);
                UserinfoData.saveIsHaveStep(getApplicationContext(), "1");
                FunctionGuideManager.getInstance().showFocusSameCancerUserGuide(SelectCancerActivity.this);
                //更新用户信息
              //  Factory.post(this, Const.EGetPatientDetail);
            } else {
                showToast("信息提交失败！");
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

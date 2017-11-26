package com.zeyuan.kyq.widget.selector.popupWindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zeyuan.kyq.Entity.ConfStepEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.StepTypeLeftAdapter;
import com.zeyuan.kyq.adapter.StepTypeRightAdapter;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.widget.selector.SimilarCaseSelector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/5/5.
 * 阶段选择器
 */
public class StepTypePopupWindow extends PopupWindow implements AdapterView.OnItemClickListener {
    Activity activity;
    private View rootView;
    private StepTypeLeftAdapter leftAdapter;
    private List<String> rightData = new ArrayList<>();
    private List<String> leftData = new ArrayList<>();
    ;
    private Map<String, List<String>> cancerData;
    //    private DrugsNameListener drugsNameListener;
    private StepTypeRightAdapter rightAdapter;
    private LinkedHashMap<String, List<String>> cureData;
    private SparseArray<ConfStepEntity> sparseArray;
    private SimilarCaseSelector.OnSelectorItemSelectedListener onSelectorItemSelectedListener;


    public void setOnSelectorItemSelectedListener(SimilarCaseSelector.OnSelectorItemSelectedListener onSelectorItemSelectedListener) {
        this.onSelectorItemSelectedListener = onSelectorItemSelectedListener;
    }

    public StepTypePopupWindow(Activity activity, String cancerId) {
        this.cancerId = cancerId;
        this.activity = activity;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.popupwindow_cancer_selector, null);
        // 设置弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(rootView);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        View view_bg = rootView.findViewById(R.id.view_bg);
        view_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        initData(cancerId);
        initView();

    }

    private void initData(String cancerId) {


        cureData = (LinkedHashMap<String, List<String>>) Factory.getData(Const.N_DataCancerStepNew, cancerId);//这个填上对应癌症
        sparseArray = (SparseArray<ConfStepEntity>) Factory.getData(Const.N_DataDrugNames);
        Set<String> leftSet = cureData.keySet();//
        for (String str : leftSet) {//把set转换为list
            leftData.add(str);
        }
        Collections.sort(leftData, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return Integer.valueOf(lhs) - Integer.valueOf(rhs);
            }
        });
    }

    private void initView() {
        ListView leftListView = (ListView) rootView.findViewById(R.id.left_listview);
        leftListView.setDivider(null);
        ListView rightListView = (ListView) rootView.findViewById(R.id.rigth_listview);
        /**
         * 左边的listview
         *
         */
        leftAdapter = new StepTypeLeftAdapter(activity, leftData);
        leftListView.setAdapter(leftAdapter);
        /**
         * 右边的listview
         */
        rightAdapter = new StepTypeRightAdapter(activity, rightData, sparseArray);
        rightListView.setAdapter(rightAdapter);

        leftListView.setOnItemClickListener(this);
        rightListView.setOnItemClickListener(this);
    }

    private int selPosition = -1;
    //记录下的阶段Id
    private String selectedStepId = "";

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.left_listview: {//左边的listview的点击事件
                if (selPosition == position)
                    return;
                selPosition = position;//防止重复点击出现bug
                leftAdapter.setSelectedPosition(position, true);//这个是为了点击效果(点击变白)
                String chooseItem = leftAdapter.getItem(position);
                List<String> list = cureData.get(chooseItem);
                if (list == null) {
                    list = new ArrayList<>();
                }
                Collections.sort(list, new Comparator<String>() {
                    @Override
                    public int compare(String l1, String l2) {
                        return Integer.valueOf(l1) - Integer.valueOf(l2);
                    }
                });
                rightData.clear();
                rightData.addAll(list);
                rightAdapter.setSelectedPosition(-1, false);
                rightAdapter.notifyDataSetChanged();
                break;
            }
            case R.id.rigth_listview: {//右边listview
                rightAdapter.setSelectedPosition(position, true);//设置选中
                String cure = rightAdapter.getItem(position);
                if (onSelectorItemSelectedListener != null) {
                    if (selectedStepId.equals(cure)) {
                        onSelectorItemSelectedListener.onSelectorItemSelected("", "阶段", SimilarCaseSelector.TYPE_STEP);
                        selectedStepId = "";
                    } else {
                        onSelectorItemSelectedListener.onSelectorItemSelected(cure, sparseArray.get(Integer.valueOf(cure)).getStepName(), SimilarCaseSelector.TYPE_STEP);
                        selectedStepId = cure;
                    }

                }
                dismiss();
                break;
            }
        }
    }

    String cancerId;

    public void showPopupWindow(View parent, String cancerId) {
        if (!this.isShowing()) {
            if (!this.cancerId.equals(cancerId)) {
                leftData.clear();
                rightData.clear();
                initData(cancerId);
                selPosition = -1;
                selectedStepId = "";
                rightAdapter.clearSelectedStepId();
                leftAdapter.setSelectedPosition(-1, false);
                rightAdapter.setSelectedPosition(-1, false);
                leftAdapter.notifyDataSetChanged();
                rightAdapter.notifyDataSetChanged();
                this.cancerId = cancerId;
            }
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 1);
        } else {
            this.dismiss();
        }
    }
}

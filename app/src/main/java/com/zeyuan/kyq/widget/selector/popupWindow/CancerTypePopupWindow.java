package com.zeyuan.kyq.widget.selector.popupWindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.CancerTypeLeftAdapter;
import com.zeyuan.kyq.adapter.CancerTypeRightAdapter;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.widget.selector.SimilarCaseSelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/4.
 */
public class CancerTypePopupWindow extends PopupWindow implements AdapterView.OnItemClickListener {
    private View rootView;
    Context context;
    public Map<String, Integer> selectData;//标示 记录左侧列表哪个自选项被选中

    private CancerTypeLeftAdapter leftAdapter;
    private List<String> rightData;
    private List<String> leftData;
    private Map<String, List<String>> cancerData;
    private Map<String, String> cancerValues;
    //    private DrugsNameListener drugsNameListener;
    private CancerTypeRightAdapter rightAdapter;

    private SimilarCaseSelector.OnSelectorItemSelectedListener onSelectorItemSelectedListener;

    public void setOnSelectorItemSelectedListener(SimilarCaseSelector.OnSelectorItemSelectedListener onSelectorItemSelectedListener) {
        this.onSelectorItemSelectedListener = onSelectorItemSelectedListener;
    }

    public CancerTypePopupWindow(Activity context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.popupwindow_cancer_selector, null);
        // int h = context.getWindowManager().getDefaultDisplay().getHeight();
        //  int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setContentView(rootView);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        initData();
        initView();
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 1);
        } else {
            this.dismiss();
        }
    }

    private void initData() {
        selectData = new HashMap<>();
        leftData = new ArrayList<>();
        cancerData = (Map<String, List<String>>) Factory.getData(Const.N_DataCancerData);
        cancerValues = (Map<String, String>) Factory.getData(Const.N_DataCancerValues);
        if (cancerData != null && cancerData.size() > 0) {
            leftData.addAll(cancerData.get("0"));
        }

        rightData = new ArrayList<>();

    }

    private void initView() {
        ListView leftListView = (ListView) rootView.findViewById(R.id.left_listview);
        leftListView.setDivider(null);
        ListView rightListView = (ListView) rootView.findViewById(R.id.rigth_listview);
        /**
         * 左边的listview
         *
         */
        leftAdapter = new CancerTypeLeftAdapter(context, leftData);
        leftListView.setAdapter(leftAdapter);
        /**
         * 右边的listview
         */
        rightAdapter = new CancerTypeRightAdapter(context, rightData, cancerValues);
        rightListView.setAdapter(rightAdapter);
        View view_bg = rootView.findViewById(R.id.view_bg);

        view_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        leftListView.setOnItemClickListener(this);
        rightListView.setOnItemClickListener(this);
    }

//    private String CancerType;
    // private int selPosition = -1;//确定选中的癌症种类的位置

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.left_listview: {//左边的listview的点击事件
//                leftAdapter.setSelectedPosition(position);//这个是为了点击效果(点击变白)  变图
                String chooseItem = leftAdapter.getItem(position);//
                List<String> list = cancerData.get(chooseItem);
                rightData.clear();
                if (list != null && list.size() > 0) {
                    rightData.addAll(list);

                } else {
                    rightData.add(chooseItem);
                }
                rightAdapter.notifyDataSetChanged();
                leftAdapter.setSelectedPosition(position);
                rightAdapter.setSelectedPosition(-1);
                break;
            }


            case R.id.rigth_listview: {//右边listview
                rightAdapter.setSelectedPosition(position);//这个是为了点击效果 字体变蓝
                selectData.put(leftAdapter.getItem(leftAdapter.getCount() - 1), position);
                String childItem = rightAdapter.getItem(position);
                if (onSelectorItemSelectedListener != null) {
                    onSelectorItemSelectedListener.onSelectorItemSelected(childItem, cancerValues.get(childItem), SimilarCaseSelector.TYPE_CANCER);
                }
                this.dismiss();
                break;
            }
        }
    }
}

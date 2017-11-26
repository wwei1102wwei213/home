package com.zeyuan.kyq.widget.selector.popupWindow;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.widget.FlowLayout;
import com.zeyuan.kyq.widget.selector.SimilarCaseSelector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/5/5.
 * 转移部位选择器
 */
public class TransferPopupWindow extends PopupWindow {
    private View rootView;
    Activity context;
    private List<String> mData;
    Map<String, String> map;
    private SimilarCaseSelector.OnSelectorItemSelectedListener onSelectorItemSelectedListener;

    public void setOnSelectorItemSelectedListener(SimilarCaseSelector.OnSelectorItemSelectedListener onSelectorItemSelectedListener) {
        this.onSelectorItemSelectedListener = onSelectorItemSelectedListener;
    }

    public TransferPopupWindow(Activity context) {
        this.context = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.popupwindow_transfer_selector, null, false);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
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
        initData();
        initView();
    }

    private void initData() {
        map = (Map<String, String>) Factory.getData(Const.N_DataTransferPos);
        mData = new ArrayList<>();
        Set<String> set = map.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            mData.add(key);
        }
    }

    private FlowLayout flowLayout;

    private void initView() {
        flowLayout = (FlowLayout) rootView.findViewById(R.id.fl);
        for (final String string : mData) {
            final TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_transfer, flowLayout, false);
            tv.setText(map.get(string));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv.isSelected()) {//取消选择
                        tv.setSelected(false);
                        onSelectorItemSelectedListener.onSelectorItemSelected("", "转移", SimilarCaseSelector.TYPE_TRANSFER);
                        dismiss();
                    } else {//选中
                        //清除之前选中item的状态
                        for (int i = 0; i < flowLayout.getChildCount(); i++) {
                            flowLayout.getChildAt(i).setSelected(false);
                        }
                        tv.setSelected(true);
                        onSelectorItemSelectedListener.onSelectorItemSelected(string, map.get(string), SimilarCaseSelector.TYPE_TRANSFER);
                        dismiss();
                    }
                }
            });
            flowLayout.addView(tv);
        }
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 1);
        } else {
            this.dismiss();
        }
    }
}

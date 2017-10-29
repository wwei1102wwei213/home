package com.zeyuan.kyq.widget.selector.popupWindow;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.widget.FlowLayout;
import com.zeyuan.kyq.widget.selector.SimilarCaseSelector;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/5.
 * 基因突变情况选择器
 */
public class MutationTypePopupWindow extends PopupWindow {
    private View rootView;
    Activity context;
    private List<String> mData;

    private SimilarCaseSelector.OnSelectorItemSelectedListener onSelectorItemSelectedListener;

    public void setOnSelectorItemSelectedListener(SimilarCaseSelector.OnSelectorItemSelectedListener onSelectorItemSelectedListener) {
        this.onSelectorItemSelectedListener = onSelectorItemSelectedListener;
    }

    public MutationTypePopupWindow(Activity context, String cancerId) {
        this.context = context;
        this.cancerId = cancerId;
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
        initData(cancerId);
        initView();
    }


    //基因map，根据cancerId获取基因id列表
   private Map<String, List<String>> gene = (Map<String, List<String>>) Factory.getData(Const.N_DataGene);
    //基因总数据，根据基因id获取基因名称
   private Map<String, String> map = (Map<String, String>) Factory.getData(Const.N_DataGeneValues);

    private void initData(String cancerId) {
        if (TextUtils.isEmpty(cancerId)) {
            Toast.makeText(context, "癌种获取失败", Toast.LENGTH_SHORT).show();
            return;
        }
        mData = gene.get(cancerId);
        if (mData == null || mData.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.cancer_has_no_gene), Toast.LENGTH_SHORT).show();
        }
    }

    private FlowLayout flowLayout;

    private void initView() {
        flowLayout = (FlowLayout) rootView.findViewById(R.id.fl);
        refreshView();
    }

    private void refreshView() {
        flowLayout.removeAllViews();
        if (mData == null || mData.size() == 0) {
            return;
        }
        for (final String string : mData) {
            final TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_transfer, flowLayout, false);
            tv.setText(map.get(string));
            tv.setTextSize(12);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv.isSelected()) {
                        tv.setSelected(false);
                        onSelectorItemSelectedListener.onSelectorItemSelected("","基因", SimilarCaseSelector.TYPE_MUTATION);
                        dismiss();
                    } else {
                        //清除已选中的其他条目状态
                        for (int i=0;i<flowLayout.getChildCount();i++){
                            flowLayout.getChildAt(i).setSelected(false);
                        }
                        tv.setSelected(true);
                        onSelectorItemSelectedListener.onSelectorItemSelected(string,map.get(string), SimilarCaseSelector.TYPE_MUTATION);
                        dismiss();
                    }
                }
            });
            flowLayout.addView(tv);
        }
    }
   private String cancerId;
    public void showPopupWindow(View parent, String cancerId) {
        if (!this.isShowing()) {
            if (!this.cancerId.equals(cancerId)) {
                this.cancerId = cancerId;
                initData(cancerId);
                refreshView();

            }
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 1);
        } else {
            this.dismiss();
        }
    }
}

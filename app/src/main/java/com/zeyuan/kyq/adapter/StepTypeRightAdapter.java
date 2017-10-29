package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.ConfStepEntity;
import com.zeyuan.kyq.R;

import java.util.List;

/**
 * Created by Administrator on 2017/5/5.
 * 阶段选择器右边listview的适配器
 */
public class StepTypeRightAdapter extends BaseAdapter {
    private int selectedPosition = -1;
    private List<String> datas;
    private LayoutInflater inflater;
    private SparseArray<ConfStepEntity> sparseArray;
    private String selectedStepId = "";//已选中的阶段id

    public StepTypeRightAdapter(Context context, List<String> datas, SparseArray<ConfStepEntity> sparseArray) {
        this.datas = datas;
        this.sparseArray = sparseArray;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public String getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_cancer_type_right, parent, false);
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            holder.iv_selected = (ImageView) convertView.findViewById(R.id.iv_selected);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv.setText(sparseArray.get(Integer.valueOf(datas.get(position))).getStepName());
        /*
         * 选中时改变背景
         */
        if (selectedPosition == position) {
            if (selectedStepId.equals(datas.get(position))) {//如果和之前记录stepid相同，取消选择
                holder.tv.setSelected(false);
                holder.tv.setPressed(false);
                holder.iv_selected.setVisibility(View.GONE);
                selectedStepId = "";
            } else {
                holder.tv.setSelected(true);
                holder.tv.setPressed(true);
                holder.iv_selected.setVisibility(View.VISIBLE);
                selectedStepId = datas.get(position);
            }

        } else {
            holder.tv.setSelected(false);
            holder.tv.setPressed(false);
            holder.iv_selected.setVisibility(View.GONE);
        }
        return convertView;
    }

    /**
     * 设置选中的条目
     */
    public void setSelectedPosition(int position, boolean isRefreshView) {
        selectedPosition = position;
        if (isRefreshView)
            notifyDataSetChanged();
    }

    //清除选中的stepid
    public void clearSelectedStepId() {
        selectedStepId = "";
    }

    class ViewHolder {
        TextView tv;
        ImageView iv_selected;
    }
}

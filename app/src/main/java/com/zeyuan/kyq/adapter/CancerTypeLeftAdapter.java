package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/4.
 */
public class CancerTypeLeftAdapter extends BaseAdapter {
    private int selectedPosition = -1;
    private List<String> datas;
    private LayoutInflater inflater;
    private Map<String, String> cancerValues;

    public CancerTypeLeftAdapter(Context context, List<String> datas) {
        this.datas = datas;
        inflater = LayoutInflater.from(context);
        this.cancerValues = (Map<String, String>) Factory.getData(Const.N_DataCancerValues);
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
            convertView = inflater.inflate(R.layout.item_cancer_type_left, parent, false);
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            holder.view_tag = convertView.findViewById(R.id.view_tag);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String values = datas.get(position);

//        holder.tv.setText(MapDataUtils.getCureValues(values));
        holder.tv.setText(cancerValues.get(values));
        /**
         * 选中时改变背景
         */
        if (selectedPosition == position) {
            holder.view_tag.setVisibility(View.VISIBLE);
            holder.tv.setTextSize(16);
            TextPaint tp = holder.tv.getPaint();
            tp.setFakeBoldText(true);
            convertView.setBackgroundResource(R.color.white);
        } else {
            holder.view_tag.setVisibility(View.GONE);
            holder.tv.setTextSize(12);
            TextPaint tp = holder.tv.getPaint();
            tp.setFakeBoldText(false);
            convertView.setBackgroundResource(R.color.main_color);
        }
        return convertView;
    }

    /**
     * 这个方法是为了左边的点击后变白
     */
    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView tv;
        View view_tag;
    }

}

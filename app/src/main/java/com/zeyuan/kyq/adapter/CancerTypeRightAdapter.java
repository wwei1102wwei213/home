package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.LogUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/4.
 */
public class CancerTypeRightAdapter extends BaseAdapter {
    private int selectedPosition = -1;
    private List<String> datas;
    private Context context;
    private LayoutInflater inflater;
    private List<String> data;//选中之后传过来的数据
    private Map<String, String> cancerValues;

    public CancerTypeRightAdapter(Context context, List<String> datas,Map<String, String> cancerValues) {
        this.context = context;
        this.datas = datas;
        this.cancerValues = cancerValues;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public String getItem(int position) {
        LogUtil.e("datas' size", datas.size() + "");
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

        holder.tv.setText(cancerValues.get(datas.get(position)));
        /**
         * 选中时改变背景
         */
        if (selectedPosition == position) {
            holder.tv.setSelected(true);
            holder.tv.setPressed(true);
            holder.iv_selected.setVisibility(View.VISIBLE);
        } else {
            holder.tv.setSelected(false);
            holder.tv.setPressed(false);
            holder.iv_selected.setVisibility(View.GONE);
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
        ImageView iv_selected;
    }
}

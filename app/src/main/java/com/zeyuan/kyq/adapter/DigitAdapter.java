package com.zeyuan.kyq.adapter;

import android.content.Context;
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
 * Created by Administrator on 2015/10/9.
 */
public class DigitAdapter extends BaseAdapter {
    private int selectedPosition = -1;

    private List<String> datas;
    private Context context;
    private LayoutInflater inflater;
    private boolean type;
    private Map<String, String> DigitValues;

    public DigitAdapter(Context context, List<String> datas, boolean type) {
        this.type = type;
        this.context = context;
        this.datas = datas;
        this.DigitValues = (Map<String, String>) Factory.getData(Const.N_DataDigitValues);
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
            convertView = inflater.inflate(R.layout.digit_dialog_tv, parent,false);
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String values = datas.get(position);
        if (type) {
            holder.tv.setText(DigitValues.get(values));
        } else {
            holder.tv.setText(values);
        }

        return convertView;
    }


    public void update(List<String> datas) {
        this.datas = datas;
        notifyDataSetChanged();
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
    }
}

package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeyuan.kyq.R;

import java.util.List;

/**
 * Created by Administrator on 2015/10/9.
 * 城市类 省份列表适配器
 *
 */
public class CityDialogLeftAdapter extends BaseAdapter {
    private int selectedPosition = -1;

    private List<String> datas;
    private Context context;
    private LayoutInflater inflater;
    private SparseArray<String> citys;

    public CityDialogLeftAdapter(Context context, List<String> datas,SparseArray<String> citys) {
        this.context = context;
        this.datas = datas;
        this.citys = citys;
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
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.dialog_leftlistview_item, null);
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            holder.drug_num = (TextView) convertView.findViewById(R.id.drug_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(citys.get(Integer.valueOf(datas.get(position))));
        if(data!= null) {
            int num = data[position].size();
            if(num > 0) {
                holder.drug_num.setVisibility(View.VISIBLE);
                holder.drug_num.setText(String.valueOf(num));
            }else {
                holder.drug_num.setVisibility(View.GONE);
            }
        }

        /**
         * 选中时改变背景
         */
        if (selectedPosition == position) {
            holder.tv.setSelected(true);
            holder.tv.setPressed(true);
            convertView.setBackgroundResource(R.color.white);
        } else {
            convertView.setBackgroundResource(R.color.main_color);
            holder.tv.setSelected(false);
            holder.tv.setPressed(false);
        }
        return convertView;
    }

    private List<String>[] data;
    public void updateCount(List<String>[] data) {
        this.data = data;
        notifyDataSetChanged();
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
    class ViewHolder{
        TextView tv;
        TextView drug_num;
    }
}

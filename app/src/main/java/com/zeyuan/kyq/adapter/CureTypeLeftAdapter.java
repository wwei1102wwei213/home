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
public class CureTypeLeftAdapter extends BaseAdapter {
    private int selectedPosition = -1;

    private List<String> datas;
    private Context context;
    private LayoutInflater inflater;
    private Map<String,String> cureConf;

    public CureTypeLeftAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
        cureConf = (Map<String,String>) Factory.getData(Const.N_DataCureConf);
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
        String values = datas.get(position);

//        holder.tv.setText(MapDataUtils.getCureValues(values));
        holder.tv.setText(cureConf.get(values));
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

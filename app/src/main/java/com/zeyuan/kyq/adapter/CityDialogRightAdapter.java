package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zeyuan.kyq.R;

import java.util.List;

/**
 * User: zeyuan
 * Date: 2015-11-11
 * Time: 14:56
 * FIXME
 */


public class CityDialogRightAdapter extends BaseAdapter {
    private List<String> datas;
    private Context context;
    private LayoutInflater inflater;
    private SparseArray<String> citys;

    public CityDialogRightAdapter(Context context, List<String> datas,SparseArray<String> citys) {
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
    private int selPosition = -1;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.dialog_rightlistview_item, null);
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
            holder.cb.setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(citys.get(Integer.valueOf(datas.get(position))));
        if(selPosition == position) {
            holder.tv.setTextColor(context.getResources().getColor(R.color.light_green));
        } else {
            holder.tv.setTextColor(context.getResources().getColor(R.color.text_color2));
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv;
        CheckBox cb;
    }

    /**
     * 设置check被单选
     * @param position
     */
    public void setSelectChoose(int position) {
        selPosition= position;
        notifyDataSetChanged();
    }

    /**
     * 更新数据
     * @param datas
     */
    public void update(List<String> datas) {
        this.datas = datas;
        selPosition = -1;
        notifyDataSetChanged();
    }
}

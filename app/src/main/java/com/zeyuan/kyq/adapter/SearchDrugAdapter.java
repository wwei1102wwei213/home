package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.ConfStepEntity;
import com.zeyuan.kyq.R;

import java.util.List;

/**
 * Created by Administrator on 2016/6/23.
 */
public class SearchDrugAdapter extends BaseAdapter {

    List<ConfStepEntity> data;
    Context context;

    public SearchDrugAdapter(Context context,List<ConfStepEntity> data){
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ConfStepEntity getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView==null){
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_search_systom_list,null);
            vh.tv = (TextView)convertView.findViewById(R.id.tv_search_symtom_text);
            convertView.setTag(vh);
        }
        vh = (ViewHolder)convertView.getTag();
        vh.tv.setText(data.get(position).getStepName());
        return convertView;
    }

    public void updata(List<ConfStepEntity> data){
        this.data = data;
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView tv;
    }
}

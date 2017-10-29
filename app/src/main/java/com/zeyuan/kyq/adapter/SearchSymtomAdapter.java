package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.Entity.PerformEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/4/6.
 * 搜索症状适配器
 *
 * @author wwei
 */
public class SearchSymtomAdapter extends BaseAdapter{

    List<PerformEntity> data;
    Context context;

    public SearchSymtomAdapter(Context context,List<PerformEntity> data){
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public PerformEntity getItem(int position) {
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
        vh.tv.setText(data.get(position).getPerformname());
        return convertView;
    }

    public void updata(List<PerformEntity> data){
        this.data = data;
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView tv;
    }
}

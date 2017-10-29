package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeyuan.kyq.R;

import java.util.List;

/**
 * Created by Administrator on 2015/10/9.
 * 主页中的潜在副作用
 */
public class SideEffectAdapter extends BaseAdapter {


    private List data;
    private Context context;
    private LayoutInflater inflater;
    public SideEffectAdapter(Context context ) {
        this.context = context;
        inflater= LayoutInflater.from(context);
//        this.data = data;
    }
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if(convertView==null) {
            convertView = inflater.inflate(R.layout.item_sideeffect, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.title.setText();



        return convertView;
    }
    class ViewHolder {
        TextView title;
        TextView content;

    }
}

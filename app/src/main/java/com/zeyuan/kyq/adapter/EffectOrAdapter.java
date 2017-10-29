package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.CommPolicyNewEntity;
import com.zeyuan.kyq.R;

import java.util.List;

/**
 * Created by Administrator on 2016/6/24.
 */
public class EffectOrAdapter extends BaseAdapter{

    private Context context;
    private List<CommPolicyNewEntity> list;

    public EffectOrAdapter(Context context,List<CommPolicyNewEntity> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CommPolicyNewEntity getItem(int position) {
        return list.get(position);
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
            convertView = View.inflate(context, R.layout.textview,null);
            vh.tv = (TextView)convertView;
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }
        String name = list.get(position).getCommPolicyName();
        if(!TextUtils.isEmpty(name)){
            vh.tv.setText(name);
        }else {
            vh.tv.setText("");
        }


        return convertView;
    }

    class ViewHolder{
        TextView tv;
    }
}

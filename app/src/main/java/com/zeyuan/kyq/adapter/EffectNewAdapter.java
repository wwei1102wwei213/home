package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.CommPolicyNewEntity;
import com.zeyuan.kyq.R;

import java.util.List;

/**
 * Created by Administrator on 2016/7/1.
 *
 *
 *
 * @author wwei
 */
public class EffectNewAdapter extends BaseAdapter{

    private Context context;
    private List<CommPolicyNewEntity> list;

    public EffectNewAdapter(Context context,List<CommPolicyNewEntity> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position).getCommPolicyID();
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
            convertView = View.inflate(context, R.layout.item_child_policy,null);
            vh.tv = (TextView)convertView.findViewById(R.id.tv_child_policy);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }

        if(position%2==0){
            vh.tv.setBackgroundColor(Color.parseColor("#ffffff"));
        }else {
            vh.tv.setBackgroundColor(Color.parseColor("#F5F5F5"));
        }

        String temp = list.get(position).getCommPolicyName();

        if(!TextUtils.isEmpty(temp)){
            vh.tv.setText(temp);
        }else {
            vh.tv.setText("");
        }

        return convertView;
    }

    class ViewHolder{
        TextView tv;
    }

}

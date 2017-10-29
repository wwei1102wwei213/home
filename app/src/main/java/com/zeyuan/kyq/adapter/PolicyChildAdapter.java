package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.PolicyDataEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/6/14.
 *
 *
 *
 * @author wwei
 */
public class PolicyChildAdapter extends BaseAdapter{

    private Context context;
    private List<PolicyDataEntity> list;
    private LayoutInflater inflater;

    public PolicyChildAdapter(Context context,List<PolicyDataEntity> list){
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public PolicyDataEntity getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_child_policy,null);
            vh.tv_policy_name = (TextView)convertView.findViewById(R.id.tv_child_policy);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }

        String policyName = list.get(position).getPolicyName();

        if(!TextUtils.isEmpty(policyName)){
            vh.tv_policy_name.setText(policyName);
        }else {
            vh.tv_policy_name.setText("");
        }

        if(position%2==0){
            vh.tv_policy_name.setBackgroundColor(Color.parseColor("#ffffff"));
        }else {
            vh.tv_policy_name.setBackgroundColor(Color.parseColor("#F5F5F5"));
        }

        return convertView;
    }

    class ViewHolder{
        TextView tv_policy_name;
    }

}

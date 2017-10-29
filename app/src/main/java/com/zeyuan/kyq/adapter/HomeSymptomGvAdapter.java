package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.HomeSymptomEntity;
import com.zeyuan.kyq.R;

import java.util.List;

/**
 * Created by Administrator on 2017/1/9.
 */
public class HomeSymptomGvAdapter extends BaseAdapter{

    private Context context;
    private List<HomeSymptomEntity> list;

    public HomeSymptomGvAdapter(Context context,List<HomeSymptomEntity> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position).getId();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;
        if (convertView==null){
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_gv_home_symptom,null);
            vh.iv = (ImageView)convertView.findViewById(R.id.iv);
            vh.tv = (TextView)convertView.findViewById(R.id.tv);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }
        HomeSymptomEntity entity = list.get(position);
        vh.iv.setImageResource(entity.getRes());
        vh.tv.setText(TextUtils.isEmpty(entity.getName())?"":entity.getName());

        return convertView;
    }

    class ViewHolder{
        ImageView iv;
        TextView tv;
    }
}

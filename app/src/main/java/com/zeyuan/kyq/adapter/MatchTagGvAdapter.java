package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.SameEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.LogCustom;

import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 *
 *
 *
 * @author wwei
 */
public class MatchTagGvAdapter extends BaseAdapter{

    private Context context;
    private List<SameEntity> list;
    private int select = -1;

    public MatchTagGvAdapter(Context context,List<SameEntity> list){
        this.context = context;
        this.list = list;
        this.select = -1;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SameEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;
        if (convertView == null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_match_tag,parent,false);
            vh.v = convertView;
            vh.tv = (TextView)convertView.findViewById(R.id.tv);
            vh.iv = (ImageView)convertView.findViewById(R.id.iv);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }
        String name = list.get(position).getName();
        name = TextUtils.isEmpty(name)?"":name;
        if (name.length()>4) {
            vh.tv.setTextSize(10);
        }else {
            vh.tv.setTextSize(12);
        }
        vh.tv.setText(name);
        vh.tv.setHeight(context.getResources().getDimensionPixelSize(R.dimen.padding30));
        LogCustom.i("ZYS", "select:" + select);
        if (select==position){
            vh.iv.setVisibility(View.VISIBLE);
            vh.tv.setSelected(true);
        }else {
            vh.iv.setVisibility(View.GONE);
            vh.tv.setSelected(false);
        }

        return convertView;
    }

    public void update(List<SameEntity> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void update(int select){
        this.select = select;
        notifyDataSetChanged();
    }

    public void update(List<SameEntity> list,int select){
        this.list = list;
        this.select = select;
        notifyDataSetChanged();
    }

    class ViewHolder{
        View v;
        ImageView iv;
        TextView tv;
    }

}

package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeyuan.kyq.R;

/**
 * Created by Administrator on 2016/3/21.
 * 帮助页面listview适配器
 *
 */
public class UseHelperAdapter extends BaseAdapter{

    private int resource;
    private Context context;
    private String[] titles;
    private int[] poss;

    public UseHelperAdapter(Context context,int resource,String[] titles,int[] poss){
        this.context = context;
        this.resource = resource;
        this.titles = titles;
        this.poss = poss;
    }
    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
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
            convertView = View.inflate(context,resource,null);
            vh.tv = (TextView)convertView.findViewById(R.id.tv_help_symptom_list_item);
            vh.tvnum = (TextView)convertView.findViewById(R.id.tv_help_num_list_item);
            convertView.setTag(vh);
        }
        vh = (ViewHolder)convertView.getTag();
        vh.tv.setText(titles[position]);
        if(position==0){
            vh.tvnum.setText(1+"");
        }else{
            vh.tvnum.setText((poss[position]-poss[position-1])+"");
        }

        return convertView;
    }

    class ViewHolder{
        TextView tv;
        TextView tvnum;
    }
}

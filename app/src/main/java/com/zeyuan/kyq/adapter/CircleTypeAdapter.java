package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.R;

/**
 * Created by Administrator on 2016/6/22.
 *
 *
 *
 * @author wwei
 */
public class CircleTypeAdapter extends BaseAdapter{

    private Context context;
    private int[] imgs;
    private String[] titles;
//    private String[] typeIDs;
    private LayoutInflater inflater;

    public CircleTypeAdapter(Context context,int[] imgs,String[] titles){
        this.context = context;
        this.imgs = imgs;
        this.titles = titles;
//        this.typeIDs = typeIDs;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return titles.length;
    }


    @Override
    public String getItem(int position) {
        return titles[position];
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
            convertView = inflater.inflate(R.layout.item_circle_top_right,null);
            vh.tv = (TextView)convertView.findViewById(R.id.tv_item_zm);
            vh.iv = (ImageView)convertView.findViewById(R.id.iv_item_zm);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }
        vh.tv.setText(titles[position]);
        if(imgs.length==0){
            vh.iv.setVisibility(View.GONE);
        }else {
            vh.iv.setVisibility(View.VISIBLE);
            vh.iv.setImageResource(imgs[position]);
        }

        return convertView;
    }

    class ViewHolder{
        ImageView iv;
        TextView tv;
    }

}

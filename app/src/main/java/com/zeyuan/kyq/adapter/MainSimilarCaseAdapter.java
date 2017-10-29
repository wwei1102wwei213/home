package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.MainPageInfoBean;

import java.util.List;

/**
 * Created by Administrator on 2015/9/21.
 * 主页中的相似案例
 */
public class MainSimilarCaseAdapter extends BaseAdapter {
    List<MainPageInfoBean.SimilarcaseNumEntity> similarcaseNum;
    private Context context;
    private LayoutInflater inflater;
    public MainSimilarCaseAdapter(Context context, List<MainPageInfoBean.SimilarcaseNumEntity> similarcaseNum) {
        this.context = context;
        this.similarcaseNum = similarcaseNum;
        inflater= LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return similarcaseNum.size();
    }

    @Override
    public MainPageInfoBean.SimilarcaseNumEntity getItem(int position) {
        return similarcaseNum.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_main_similar_case, null);
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.reply_number = (TextView) view.findViewById(R.id.reply_number);
            holder.lastupdatetime = (TextView) view.findViewById(R.id.lastupdatetime);
            holder.img = (ImageView) view.findViewById(R.id.img);
            view.setTag(holder);
        } else{
            holder = (ViewHolder) view.getTag();
        }
        if(position == 0) {
            holder.img.setImageResource(R.mipmap.case1);
        }else if(position == 1) {
            holder.img.setImageResource(R.mipmap.case2);

        }else {
            holder.img.setImageResource(R.mipmap.case3);
        }
        holder.title.setText(similarcaseNum.get(position).getTitle());
//        holder.reply_number.setText(similarcaseNum.get(position).getReplynum());
//        holder.lastupdatetime.setText(similarcaseNum.get(position).getLastupdatetime());
//        holder.img.
        return view;
    }

    static class ViewHolder {
        ImageView img;
        TextView title;
        TextView reply_number;
        TextView lastupdatetime;
    }
}

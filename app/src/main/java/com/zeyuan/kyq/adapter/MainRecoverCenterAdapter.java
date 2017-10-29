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
 * Created by Administrator on 2015/9/21.
 * 主页中的康复与营养中心
 */
public class MainRecoverCenterAdapter extends BaseAdapter {
//    List<MainPageInfoBean.RecoverCenterNumEntity> RecoverCenterNum;
    private Context context;
    private LayoutInflater inflater;
    public MainRecoverCenterAdapter(Context context) {
        this.context = context;
//        this.RecoverCenterNum = RecoverCenterNum;
        inflater= LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return context;
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
            view = inflater.inflate(R.layout.item_main_recover_center, null);
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.reply_number = (TextView) view.findViewById(R.id.reply_number);
            holder.lastupdatetime = (TextView) view.findViewById(R.id.lastupdatetime);
            holder.img = (ImageView) view.findViewById(R.id.img);
            view.setTag(holder);
        } else{
            holder = (ViewHolder) view.getTag();
        }
//        holder.title.setText(RecoverCenterNum.get(position).getTitle());
//        holder.reply_number.setText(RecoverCenterNum.get(position).getReplynum());
//        holder.lastupdatetime.setText(RecoverCenterNum.get(position).getLastupdatetime());
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

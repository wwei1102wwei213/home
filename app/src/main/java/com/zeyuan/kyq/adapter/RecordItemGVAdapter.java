package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/21.
 *
 * @author wwei
 */
public class RecordItemGVAdapter extends BaseAdapter{

    private Context context;
    private List<String> list;
    private int pw;
    private int max;

    public RecordItemGVAdapter(Context context,List<String> list,int pw,int max){
        this.context = context;
        this.list = list;
        this.pw = pw;
        this.max = max;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.record_photo_image, parent, false);
            vh.iv = (ImageView)convertView.findViewById(R.id.img);
            vh.tv = (TextView) convertView.findViewById(R.id.tv_img_num);
            vh.v = convertView.findViewById(R.id.v_parent);
            ViewGroup.LayoutParams params1 = vh.v.getLayoutParams();
            params1.width = pw;
            params1.height = pw;
            vh.v.setLayoutParams(params1);
            ViewGroup.LayoutParams params = vh.iv.getLayoutParams();
            params.width = pw;
            params.height = pw;
            vh.iv.setLayoutParams(params);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }

        try {
            Glide.with(context)
                    .load(TextUtils.isEmpty(list.get(position))?R.mipmap.loading_fail:list.get(position).trim())
                    .into(vh.iv);
        }catch (Exception e){

        }

        if (position == list.size()-1){
            vh.tv.setVisibility(View.VISIBLE);
            vh.tv.setText("共"+max+"张");
        }else {
            vh.tv.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void update(List<String> list){
        if (list==null) list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder{
        ImageView iv;
        TextView tv;
        View v;
    }

}

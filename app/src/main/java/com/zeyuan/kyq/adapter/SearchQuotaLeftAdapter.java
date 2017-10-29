package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.QuotaItemEntity;
import com.zeyuan.kyq.R;

import java.util.List;

/**
 * Created by Administrator on 2016/6/17.
 *
 *
 */
public class SearchQuotaLeftAdapter extends BaseAdapter{

    private Context context;
    private List<QuotaItemEntity> list;
    private int[] res;
    private int[] unRes;
    private int index;

    public SearchQuotaLeftAdapter(Context context,List<QuotaItemEntity> list,int[] res,int[] unRes,int index){
        this.context = context;
        this.list = list;
        this.res = res;
        this.unRes = unRes;
        this.index = index;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public int getIndex(){
        return index;
    }

    @Override
    public QuotaItemEntity getItem(int position) {
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
            convertView = View.inflate(context, R.layout.radio_search_drug,null);
            vh.rb = (TextView)convertView;
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }
        QuotaItemEntity entity = list.get(position);
        if(TextUtils.isEmpty(entity.getName())){
            vh.rb.setText("");
        }else {
            vh.rb.setText(entity.getName());
        }
        int p = getRes(entity.getId());
        if(position == index){
            vh.rb.setTextColor(context.getResources().getColor(R.color.light_green));
            vh.rb.setBackgroundResource(R.color.white);
            if (Build.VERSION.SDK_INT >=17)
            vh.rb.setCompoundDrawablesRelativeWithIntrinsicBounds(0, res[p], 0, 0);
           /* if(position>res.length-1){

                vh.rb.setCompoundDrawablesRelativeWithIntrinsicBounds(0, res[res.length-1], 0, 0);
            }else {
                vh.rb.setCompoundDrawablesRelativeWithIntrinsicBounds(0, res[position], 0, 0);
            }*/
        }else {
            vh.rb.setTextColor(context.getResources().getColor(R.color.text_color2));
            vh.rb.setBackgroundResource(R.color.scroll_color);
            if (Build.VERSION.SDK_INT >=17)
            vh.rb.setCompoundDrawablesRelativeWithIntrinsicBounds(0, unRes[p], 0, 0);
            /*if(position>unRes.length-1){
                vh.rb.setCompoundDrawablesRelativeWithIntrinsicBounds(0, unRes[unRes.length-1], 0, 0);
            }else {
                vh.rb.setCompoundDrawablesRelativeWithIntrinsicBounds(0, unRes[position], 0, 0);
            }*/
        }

        return convertView;
    }

    public void updata(int index){
        this.index = index;
        notifyDataSetChanged();
    }

    private int getRes(String id){
        if(TextUtils.isEmpty(id)) return 4;
        int temp = -1;
        try {
            temp = Integer.valueOf(id);
        }catch (Exception e){

        }
        if(temp==-1) return 4;

        if(temp>0&&temp<5){
            return temp-1;
        }else {
            return 4;
        }
    }

    class ViewHolder{
        TextView rb;
    }

}

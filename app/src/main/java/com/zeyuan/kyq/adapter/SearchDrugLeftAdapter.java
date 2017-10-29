package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.TempEntity;
import com.zeyuan.kyq.R;

import java.util.List;

/**
 * Created by Administrator on 2016/6/16.
 *
 * 药物类型适配器
 *
 * @author wwei
 */
public class SearchDrugLeftAdapter extends BaseAdapter{

    private Context context;
    private List<String> CureConfIds;
    private List<String> CureConfNames;
    private int index;
    private int[] checkRes;
    private int[] unCheckRes;
    private List<TempEntity> tempList;


    public SearchDrugLeftAdapter(Context context,List<String> CureConfIds,List<String> CureConfNames,int[] checkRes,int[] unCheckRes,int index){
        this.context = context;
        this.CureConfIds = CureConfIds;
        this.CureConfNames = CureConfNames;
        this.checkRes = checkRes;
        this.unCheckRes = unCheckRes;
        this.index = index;
    }

    @Override
    public int getCount() {
        return CureConfNames.size();
    }

    @Override
    public String getItem(int position) {
        return CureConfIds.get(position);
    }

    public int getIndex() {
        return index;
    }

    public int getCureConfId(int position) {
        return Integer.valueOf(CureConfIds.get(position));
    }

    @Override
    public long getItemId(int position) {
        return Integer.valueOf(CureConfIds.get(position));
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
        vh.rb.setText(CureConfNames.get(position));
        if(position == index){
            vh.rb.setTextColor(context.getResources().getColor(R.color.light_green));
            vh.rb.setBackgroundResource(R.color.white);
            try {
                if(position>checkRes.length-1){
                    vh.rb.setCompoundDrawablesRelativeWithIntrinsicBounds(0, checkRes[checkRes.length-1], 0, 0);
                }else {
                    vh.rb.setCompoundDrawablesRelativeWithIntrinsicBounds(0, checkRes[position], 0, 0);
                }
            }catch (Exception e){

            }

        }else {
            vh.rb.setTextColor(context.getResources().getColor(R.color.text_color2));
            vh.rb.setBackgroundResource(R.color.scroll_color);
            try {
                if(position>checkRes.length-1){
                    vh.rb.setCompoundDrawablesRelativeWithIntrinsicBounds(0, unCheckRes[checkRes.length-1], 0, 0);
                }else {
                    vh.rb.setCompoundDrawablesRelativeWithIntrinsicBounds(0, unCheckRes[position], 0, 0);
                }
            }catch (Exception e){

            }
        }

        return convertView;
    }

    public void updata(int index){
        this.index = index;
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView rb;
    }
}

package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.UiUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/3.
 *
 * 选择要记录的肿标
 *
 * @author wwei
 */
public class ChooseQuotaTypeAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<String> check;
    private boolean isChange = false;

    public boolean isChange() {
        return isChange;
    }

    public ChooseQuotaTypeAdapter(Context context,ArrayList<String> check){
        this.context = context;
        if (check==null) check = new ArrayList<>();
        this.check = check;
    }

    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder vh;
        if (convertView==null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_choose_quota_type,parent,false);
            vh.tv1 = (TextView)convertView.findViewById(R.id.tv1);
            vh.tv2 = (TextView)convertView.findViewById(R.id.tv2);
            vh.choose = (TextView)convertView.findViewById(R.id.tv_choose);
            vh.v = convertView;
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }
        vh.tv1.setText(UiUtils.getNameENforTypeID(position+1));
        vh.tv2.setText(UiUtils.getNameCNforTypeID(position+1));
        final String typeID = (position+1)+"";
        if (check.contains(typeID)){
            vh.choose.setVisibility(View.VISIBLE);
            vh.choose.setSelected(true);
        }else {
            vh.choose.setSelected(false);
        }
        vh.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChange = true;
                if (vh.choose.isSelected()){
                    check.remove(typeID);
                }else {
                    check.add(typeID);
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public ArrayList<String> getCheck(){
        return check;
    }

    class ViewHolder{
        TextView tv1,tv2;
        TextView choose;
        View v;
    }

}

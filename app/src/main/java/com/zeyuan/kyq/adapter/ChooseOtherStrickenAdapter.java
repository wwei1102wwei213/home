package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.UiUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/18.
 *
 * @author wwei
 */
public class ChooseOtherStrickenAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<String> check;
    private boolean isChange = false;

    public boolean isChange() {
        return isChange;
    }

    public ChooseOtherStrickenAdapter(Context context,ArrayList<String> check){
        this.context = context;
        if (check==null) check = new ArrayList<>();
        this.check = check;
    }

    @Override
    public int getCount() {
        return 5;
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

        try {
            final ViewHolder vh;
            if (convertView==null){
                vh = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_choose_other_stricken,parent,false);
                vh.tv1 = (TextView)convertView.findViewById(R.id.tv1);
                vh.choose = (TextView)convertView.findViewById(R.id.tv_choose);
                vh.v = convertView;
                convertView.setTag(vh);
            }else {
                vh = (ViewHolder)convertView.getTag();
            }
            vh.tv1.setText(UiUtils.getNameForOtherStrickenID(position + 1));
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
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"getView");
        }
        return convertView;
    }

    public ArrayList<String> getCheck(){
        return check;
    }

    class ViewHolder{
        TextView tv1;
        TextView choose;
        View v;
    }

}

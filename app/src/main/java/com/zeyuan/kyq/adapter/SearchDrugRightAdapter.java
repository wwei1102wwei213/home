package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.ConfStepEntity;
import com.zeyuan.kyq.R;

import java.util.List;

/**
 * Created by Administrator on 2016/6/16.
 *
 *
 *
 * @author wwei
 */
public class SearchDrugRightAdapter extends BaseAdapter{

    private Context context;
    private List<ConfStepEntity> list;
    public SearchDrugRightAdapter(Context context,List<ConfStepEntity> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ConfStepEntity getItem(int position) {
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
            convertView = View.inflate(context, R.layout.item_symptom_textview,null);
            vh.tv = (TextView)convertView.findViewById(R.id.text);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }
        String stepName = list.get(position).getStepName();
        if(TextUtils.isEmpty(stepName)){
            vh.tv.setText("");
        }else {
            vh.tv.setText(stepName);
        }
        return convertView;
    }

    public void updata(List<ConfStepEntity> list){
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView tv;
    }
}

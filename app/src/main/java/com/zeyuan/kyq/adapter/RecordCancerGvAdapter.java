package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.CancerSizeItemEntity;
import com.zeyuan.kyq.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/10.
 *
 * @author wwei
 */
public class RecordCancerGvAdapter extends BaseAdapter{

    private Context context;
    private List<CancerSizeItemEntity> data;

    public RecordCancerGvAdapter(Context context,List<CancerSizeItemEntity> list){
        this.context = context;
        data = new ArrayList<>();
        if (list!=null&&list.size()>0){
            for (CancerSizeItemEntity entity:list){
                data.add(entity);
            }
        }
        int size = data.size();
        while (size>2){
            size = size%3;
        }
        if (size==1) {
            CancerSizeItemEntity entity1 = new CancerSizeItemEntity();
            CancerSizeItemEntity entity2 = new CancerSizeItemEntity();
            entity1.setTypeID(-1);
            entity2.setTypeID(-1);
            data.add(entity1);
            data.add(entity2);
        } else if (size==2){
            CancerSizeItemEntity entity1 = new CancerSizeItemEntity();
            entity1.setTypeID(-1);
            data.add(entity1);
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;

        if (convertView==null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gv_record_cancer_size,parent,false);
            vh.tv_name = (TextView)convertView.findViewById(R.id.tv_gv_name);
            vh.tv_size = (TextView)convertView.findViewById(R.id.tv_gv_size);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }

        CancerSizeItemEntity entity = data.get(position);

        if (entity.getTypeID()==-1){
            vh.tv_name.setText("");

            vh.tv_size.setText("");
        }else {
            vh.tv_name.setText(TextUtils.isEmpty(entity.getName())?"":entity.getName());

            vh.tv_size.setText(TextUtils.isEmpty(entity.getSize())?"":entity.getSize());
        }


        return convertView;
    }

    class ViewHolder{
        TextView tv_name,tv_size;
    }
}

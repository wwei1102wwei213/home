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
import com.zeyuan.kyq.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/10.
 *
 * @author wwei
 */
public class RecordQuotaGvAdapter extends BaseAdapter{

    private Context context;
    private List<CancerSizeItemEntity> data;

    public RecordQuotaGvAdapter(Context context,List<CancerSizeItemEntity> list){
        this.context = context;
        data = new ArrayList<>();
        if (list!=null&&list.size()>0){
            for (CancerSizeItemEntity entity:list){
                data.add(entity);
            }
        }
        int size = data.size();
        while (size>1){
            size = size%2;
        }
        if (size!=0) {
            CancerSizeItemEntity entity = new CancerSizeItemEntity();
            entity.setTypeID(-1);
            data.add(entity);
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
            String name = UiUtils.getNameCNforTypeID(entity.getTypeID());
            String unit = UiUtils.getUnitForType(entity.getTypeID());
            vh.tv_name.setText(name + "("+unit+")");
            vh.tv_size.setText(TextUtils.isEmpty(entity.getSize())?"":entity.getSize());
        }


        return convertView;
    }

    class ViewHolder{
        TextView tv_name,tv_size;
    }


}

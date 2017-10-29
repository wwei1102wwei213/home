package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.RecordItemEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.view.ShowPhotoActivity;
import com.zeyuan.kyq.widget.CustomView.CustomExpandableTextView;
import com.zeyuan.kyq.widget.MyGridView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 *
 * @author wwei
 */
public class PathologyPatientAdapter extends BaseAdapter{


    private Context context;
    private int pw;
    private int type;
    private List<RecordItemEntity> list;
    private SparseBooleanArray array = new SparseBooleanArray();

    public PathologyPatientAdapter(Context context,int pw,List<RecordItemEntity> list,int type){
        this.context = context;
        this.pw = pw;
        this.type = type;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pathology_patient,parent,false);
            vh.date = (TextView)convertView.findViewById(R.id.tv_date);
            vh.tv_name = (TextView)convertView.findViewById(R.id.tv_type_name);
            vh.remark = (CustomExpandableTextView)convertView.findViewById(R.id.tv_remark);
            vh.v_gv = convertView.findViewById(R.id.v_gv);
            vh.gv = (MyGridView)convertView.findViewById(R.id.gv);
            vh.hospital = (TextView)convertView.findViewById(R.id.tv_hospital);
            vh.doctor = (TextView)convertView.findViewById(R.id.tv_doctor);
            vh.v_hospital = convertView.findViewById(R.id.v_hospital);
            vh.v_doctor = convertView.findViewById(R.id.v_doctor);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }
        final RecordItemEntity entity = list.get(position);
        vh.date.setText(DataUtils.getPathologyDate(entity.getRecordTime() + ""));

        vh.tv_name.setText(UiUtils.getRecordTitleNameForTypeID(type));

        String hospital = entity.getHospital();
        if (TextUtils.isEmpty(hospital)){
            vh.v_hospital.setVisibility(View.GONE);
        }else {
            vh.v_hospital.setVisibility(View.VISIBLE);
            vh.hospital.setText(hospital);
        }
        String doctor = entity.getDoctor();
        if (TextUtils.isEmpty(doctor)){
            vh.v_doctor.setVisibility(View.GONE);
        }else {
            vh.v_doctor.setVisibility(View.VISIBLE);
            vh.doctor.setText(doctor);
        }
        String remark = entity.getRemark();
        if (TextUtils.isEmpty(remark)){
            vh.remark.setVisibility(View.GONE);
            vh.remark.setText("");
        }else {
            vh.remark.setVisibility(View.VISIBLE);
            vh.remark.setConvertText(array, position, remark);
        }
        final List<String> pic = entity.getPic();
        if (pic==null||pic.size()==0){
            vh.v_gv.setVisibility(View.GONE);
        }else {
            vh.v_gv.setVisibility(View.VISIBLE);
            List<String> temp = new ArrayList<>();
            if (pic.size()>3){
                for (int i=0;i<3;i++){
                    temp.add(pic.get(i));
                }
            }else {
                temp = pic;
            }
            RecordItemGVAdapter adapter = new RecordItemGVAdapter(context,temp,pw,pic.size());
            vh.gv.setAdapter(adapter);
            vh.gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    context.startActivity(new Intent(context, ShowPhotoActivity.class).putExtra("list",
                            (Serializable) pic).putExtra("position", position));
                }
            });
        }
        return convertView;
    }

    public void update(List<RecordItemEntity> list){
        if (list==null) list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView date,hospital,doctor,tv_name;
        CustomExpandableTextView remark;
        View v_gv,v_hospital,v_doctor;
        MyGridView gv;
    }

}

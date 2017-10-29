package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.RecordItemEntity;
import com.zeyuan.kyq.Entity.StepUserEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
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
public class MedicalRecordRecyclerAdapter extends RecyclerView.Adapter<MedicalRecordRecyclerAdapter
        .MedicalRecordViewHolder> {

    private static final int TYPE_1 = 0;
    private static final int TYPE_2 = 1;
    private Context context;
    private List<StepUserEntity> list;
    private SparseBooleanArray array = new SparseBooleanArray();
    private SparseBooleanArray flag = new SparseBooleanArray();
    private int pw;

    public MedicalRecordRecyclerAdapter(Context context,List<StepUserEntity> list,int pw){
        this.context = context;
        this.list = list;
        this.pw = pw;
    }

    @Override
    public MedicalRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        if (viewType == 1) {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_rv_medical_record_2, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_rv_medical_record_1, parent, false);
        }
        return new MedicalRecordViewHolder(v,viewType,true);
    }

    @Override
    public int getItemViewType(int position) {
        int t = list.get(position).getType();
        if (t!=0){
            return TYPE_2;
        }
        return TYPE_1;
    }



    @Override
    public void onBindViewHolder(MedicalRecordViewHolder vh, int position) {
        int itemType = getItemViewType(position);
        final StepUserEntity data = list.get(position);
        if (itemType == TYPE_1){
            try {
                vh.tv_cure_name.setText(TextUtils.isEmpty(data.getCureName())?"":data.getCureName());
                vh.tv_step_num.setText(position+"");
                vh.tv_step_name.setText(TextUtils.isEmpty(data.getStepName())?"":data.getStepName());
                vh.tv_step_time.setText(DataUtils.getRecordDateForStep(data.getStartTime())+"~"
                        +DataUtils.getRecordDateForStep(data.getEndTime()));
                if (flag.get(position)){
                    vh.iv_step_right.setRotation(0);
                }else {
                    vh.iv_step_right.setRotation(90);
                }
            }catch (Exception e){
                ExceptionUtils.ExceptionSend(e,"1");
            }
        }else if (itemType == TYPE_2){
            try {
                final RecordItemEntity entity = data.getRI();
                vh.date.setText(DataUtils.getPathologyDate(entity.getRecordTime() + ""));

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
            }catch (Exception e){
                ExceptionUtils.ExceptionSend(e,"2");
            }

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /*@Override
    public MedicalRecordViewHolder getViewHolder(View view) {
        return new MedicalRecordViewHolder(view,false);
    }

    @Override
    public MedicalRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = null;
        if (viewType == 0) {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_rv_medical_record_1, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_rv_medical_record_2, parent, false);
        }
        return new MedicalRecordViewHolder(v,viewType,true);
    }*/

    /*@Override
    public void onBindViewHolder(MedicalRecordViewHolder vh, int position, boolean isItem) {

        int itemType = getAdapterItemViewType(position);
        final StepUserEntity data = list.get(position);
        if (itemType == TYPE_1){
            vh.tv_cure_name.setText(TextUtils.isEmpty(data.getCureName())?"":data.getCureName());
            vh.tv_step_num.setText(position+"");
            vh.tv_step_name.setText(TextUtils.isEmpty(data.getStepName())?"":data.getStepName());
            vh.tv_step_time.setText(DataUtils.getRecordDateForStep(data.getStartTime())+"~"
                    +DataUtils.getRecordDateForStep(data.getEndTime()));
            if (flag.get(position)){
                vh.iv_step_right.setRotation(0);
            }else {
                vh.iv_step_right.setRotation(90);
            }
        }else if (itemType == TYPE_2){
            final RecordItemEntity entity = data.getRI();
            vh.date.setText(DataUtils.getPathologyDate(entity.getRecordTime() + ""));

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

        }

    }*/

    /*@Override
    public int getAdapterItemCount() {
        return list.size();
    }

    @Override
    public int getAdapterItemViewType(int position) {
        int t = list.get(position).getType();
        if (t!=0){
            return TYPE_2;
        }
        return TYPE_1;
    }*/

    public void update(List<StepUserEntity> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public class MedicalRecordViewHolder extends RecyclerView.ViewHolder{

        TextView tv_step_name,tv_step_num,tv_step_time,tv_cure_name,tv_delete,tv_edit;
        ImageView iv_step_right,iv_type;
        TextView date,hospital,doctor;
        CustomExpandableTextView remark;
        View v_gv,v_hospital,v_doctor;
        MyGridView gv;

        public MedicalRecordViewHolder(View itemView,boolean isItem) {
            super(itemView);
            init(itemView, -1, isItem);
        }

        public MedicalRecordViewHolder(View itemView,int viewType,boolean isItem) {
            super(itemView);
            init(itemView, viewType, isItem);
        }

        private void init(View itemView,int viewType,boolean isItem){
            if (isItem){
                switch (viewType){
                    case TYPE_2:
                        date = (TextView)itemView.findViewById(R.id.tv_date);
                        tv_edit = (TextView)itemView.findViewById(R.id.tv_edit);
                        tv_delete = (TextView)itemView.findViewById(R.id.tv_delete);
                        iv_type = (ImageView)itemView.findViewById(R.id.iv_type);
                        remark = (CustomExpandableTextView)itemView.findViewById(R.id.tv_remark);
                        v_gv = itemView.findViewById(R.id.v_gv);
                        gv = (MyGridView)itemView.findViewById(R.id.gv);
                        hospital = (TextView)itemView.findViewById(R.id.tv_hospital);
                        doctor = (TextView)itemView.findViewById(R.id.tv_doctor);
                        v_hospital = itemView.findViewById(R.id.v_hospital);
                        v_doctor = itemView.findViewById(R.id.v_doctor);
                        break;
                    default:
                        tv_step_name = (TextView)itemView.findViewById(R.id.tv_step_name);
                        tv_cure_name = (TextView)itemView.findViewById(R.id.tv_conf_name);
                        tv_step_num = (TextView)itemView.findViewById(R.id.tv_num_for_step);
                        tv_step_time = (TextView)itemView.findViewById(R.id.tv_step_time);
                        iv_step_right = (ImageView)itemView.findViewById(R.id.iv_step_right);
                        if (ZYApplication.typeFace!=null){
                            tv_step_num.setTypeface(ZYApplication.typeFace);
                        }
                        break;
                }
            }
        }
    }

}

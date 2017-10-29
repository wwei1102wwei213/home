package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.Entity.RecordItemEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.ShowPhotoActivity;
import com.zeyuan.kyq.widget.CustomView.CustomExpandableTextView;
import com.zeyuan.kyq.widget.MyGridView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/20.
 *
 * 病理报告适配器
 *
 * @author wwei
 */
public class PathologyAdapter extends BaseAdapter implements HttpResponseInterface{

    private Context context;
    private int pw;
    private List<RecordItemEntity> list;
    private EditRecordItemListener callback;
    private Animation a;
    private Animation b;
    private int ShowID;
    private int ShowPosition = -1;
    private int DeletePosition = -1;
    private int DelID;
    private int Is_Show;
    private boolean animFlag = false;
    private int Type;
    private SparseBooleanArray array = new SparseBooleanArray();

    private boolean isChanged = false;



    public interface EditRecordItemListener{
        void toEditRecordItem(RecordItemEntity entity);
    }

    public PathologyAdapter(Context context,int pw,List<RecordItemEntity> list,int Type,EditRecordItemListener callback){
        this.context = context;
        this.pw = pw;
        this.list = list;
        this.Type = Type;
        this.callback = callback;
        a = AnimationUtils.loadAnimation(context, R.anim.switch_translate_to_right_false);
        b = AnimationUtils.loadAnimation(context, R.anim.switch_translate_to_left_false);
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder vh;
        if (convertView==null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pathology,parent,false);
            vh.date = (TextView)convertView.findViewById(R.id.tv_date);
            vh.week = (TextView)convertView.findViewById(R.id.tv_week);
            vh.delete = (TextView)convertView.findViewById(R.id.tv_delete);
            vh.edit = (TextView)convertView.findViewById(R.id.tv_edit);
            vh.year = (TextView)convertView.findViewById(R.id.tv_year);
            vh.remark = (CustomExpandableTextView)convertView.findViewById(R.id.tv_remark);
            vh.v_gv = convertView.findViewById(R.id.v_gv);
            vh.gv = (MyGridView)convertView.findViewById(R.id.gv);
            vh.v_select = convertView.findViewById(R.id.v_change_now_step);
            vh.select_bg = (TextView)convertView.findViewById(R.id.tv_select_bg);
            vh.select_point = (TextView)convertView.findViewById(R.id.tv_select_point);
            vh.select_point_right = (TextView)convertView.findViewById(R.id.tv_select_point_right);
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
        vh.week.setText(DataUtils.getPathologyWeek(entity.getRecordTime()+""));
        if (getYearFlag(position)){
            vh.year.setVisibility(View.VISIBLE);
            vh.year.setText(DataUtils.getPathologyYear(entity.getRecordTime())+"");
        }else {
            vh.year.setVisibility(View.GONE);
        }
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
        final int show = entity.getIs_show();
        if (animFlag&&position==ShowPosition){
            //需要执行动画
            if (show==1){
                vh.select_bg.setSelected(false);
                vh.select_point.setVisibility(View.VISIBLE);
                vh.select_point_right.setVisibility(View.GONE);
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        vh.select_point_right.setVisibility(View.VISIBLE);
                        vh.v_select.setClickable(true);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                vh.select_point.startAnimation(a);
            }else {
                vh.select_bg.setSelected(true);
                vh.select_point.setVisibility(View.GONE);
                vh.select_point_right.setVisibility(View.VISIBLE);
                b.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        vh.select_point_right.setVisibility(View.GONE);
                        vh.v_select.setClickable(true);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                vh.select_point_right.startAnimation(b);
            }
        }else {
            if (show==1){
                vh.select_bg.setSelected(true);
                vh.select_point.setVisibility(View.GONE);
                vh.select_point_right.setVisibility(View.VISIBLE);
            }else {
                vh.select_bg.setSelected(false);
                vh.select_point.setVisibility(View.VISIBLE);
                vh.select_point_right.setVisibility(View.GONE);
            }
        }

        vh.v_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vh.v_select.setClickable(false);
                ShowID = entity.getID();
                Is_Show = show;
                ShowPosition = position;
                Factory.postPhp(PathologyAdapter.this, Const.PShowPresentationOther);
            }
        });

        vh.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*DelID = entity.getID();
                DeletePosition = position;
                Factory.postPhp(PathologyAdapter.this, Const.PDelPresentationOther);*/
                toDelete(entity.getID(), position);
            }
        });

        vh.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.toEditRecordItem(entity);
            }
        });

        return convertView;
    }

    private void toDelete(final int id,final int position){
        ZYDialog.Builder builder = new ZYDialog.Builder(context);
        builder.setTitle("提示")
                .setMessage("报告删除后将无法找回，请确认是否删除？")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DelID = id;
                        DeletePosition = position;
                        Factory.postPhp(PathologyAdapter.this, Const.PDelPresentationOther);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("不删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    public void update(List<RecordItemEntity> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public boolean getYearFlag(int position){
        if (position==0) return true;
        try {
            int year1 = DataUtils.getPathologyYear(list.get(position).getRecordTime());
            int year2 = DataUtils.getPathologyYear(list.get(position-1).getRecordTime());
            if (year1!=0&&year2!=0&&year1!=year2){
                return true;
            }
        }catch (Exception e){

        }
        return false;
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String,String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(context));
        if (tag == Const.PDelPresentationOther){
            map.put("Type",Type+"");
            map.put("ID",DelID+"");
        }else if (tag == Const.PShowPresentationOther){
            map.put("Type",Type+"");
            map.put("ID",ShowID+"");
            if (Is_Show==1){
                map.put("Show","2");
            }else {
                map.put("Show","1");
            }
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {

        if (flag == Const.PShowPresentationOther){

            PhpUserInfoBean bean = (PhpUserInfoBean)response;
            if (Const.RESULT.equals(bean.getiResult())){

                isChanged = true;

                if (Is_Show==1){
                    showToast("取消显示成功");
                    list.get(ShowPosition).setIs_show(2);
                }else {
                    showToast("显示成功");
                    list.get(ShowPosition).setIs_show(1);
                }

                notifyDataSetChanged();
            }else {
                if (Is_Show==1){
                    showToast("取消显示失败");
                }else {
                    showToast("显示失败");
                }
            }

        }else if (flag == Const.PDelPresentationOther){

            PhpUserInfoBean bean = (PhpUserInfoBean)response;

            if (Const.RESULT.equals(bean.getiResult())){
                showToast("删除成功");
                isChanged = true;
                list.remove(DeletePosition);
                notifyDataSetChanged();
            }else {
                showToast("删除失败");
            }

        }

    }

    private void showToast(String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(int flag) {

    }

    @Override
    public void hideLoading(int flag) {

    }

    @Override
    public void showError(int flag) {
        showToast("网络请求失败");
    }

    public boolean isChanged() {
        return isChanged;
    }

    class ViewHolder{
        TextView year,date,week,delete,edit,select_bg,select_point,select_point_right,hospital,doctor;
        CustomExpandableTextView remark;
        View v_gv,v_select,v_hospital,v_doctor;
        MyGridView gv;
    }
}

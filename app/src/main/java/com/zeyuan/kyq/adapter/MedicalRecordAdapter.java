package com.zeyuan.kyq.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Html;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.Entity.CancerSizeItemEntity;
import com.zeyuan.kyq.Entity.MedicalBaseBean;
import com.zeyuan.kyq.Entity.RecordItemEntity;
import com.zeyuan.kyq.Entity.StepUserEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.onEditItemListener;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.utils.ConfUtils;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ConstUtils;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.ShowPhotoActivity;
import com.zeyuan.kyq.widget.CustomView.CustomExpandableTextView;
import com.zeyuan.kyq.widget.MyGridView;
import com.zeyuan.kyq.widget.MyListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/9.
 *
 * @author wwei
 */
public class MedicalRecordAdapter extends BaseAdapter implements HttpResponseInterface{

    private static final int TYPE_1 = 0;
    private static final int TYPE_2 = 1;
    private Context context;
    private List<StepUserEntity> list;
    private SparseBooleanArray array = new SparseBooleanArray();
    private SparseBooleanArray flag = new SparseBooleanArray();
    private SparseBooleanArray click = new SparseBooleanArray();
    private int pw;
    private onEditItemListener callback;


    public MedicalRecordAdapter(Context context,List<StepUserEntity> list,int pw,onEditItemListener callback){
        this.context = context;
        this.list = list;
        this.pw = pw;
        this.callback = callback;
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
    public int getItemViewType(int position) {
        int t = list.get(position).getType();
        if (t!=0){
            return TYPE_2;
        }
        return TYPE_1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        final int itemType = getItemViewType(position);
        if (convertView == null){
            vh = new ViewHolder();
            switch (itemType){
                case TYPE_2:
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.item_rv_medical_record_2, parent, false);
                    vh.date = (TextView)convertView.findViewById(R.id.tv_date);
                    vh.tv_edit = (TextView)convertView.findViewById(R.id.tv_edit);
                    vh.tv_delete = (TextView)convertView.findViewById(R.id.tv_delete);
                    vh.iv_type = (ImageView)convertView.findViewById(R.id.iv_type);
                    vh.remark = (CustomExpandableTextView)convertView.findViewById(R.id.tv_remark);
                    vh.v_gv = convertView.findViewById(R.id.v_gv);
                    vh.gv = (MyGridView)convertView.findViewById(R.id.gv);
                    vh.hospital = (TextView)convertView.findViewById(R.id.tv_hospital);
                    vh.doctor = (TextView)convertView.findViewById(R.id.tv_doctor);
                    vh.v_hospital = convertView.findViewById(R.id.v_hospital);
                    vh.v_doctor = convertView.findViewById(R.id.v_doctor);
                    vh.v_record_gene = convertView.findViewById(R.id.v_record_gene);
                    vh.v_record_translate = convertView.findViewById(R.id.v_record_translate);
                    vh.v_record_perform = convertView.findViewById(R.id.v_perform);
                    vh.tv_record_gene = (TextView)convertView.findViewById(R.id.tv_record_gene);
                    vh.tv_record_translate = (TextView)convertView.findViewById(R.id.tv_record_translate);
                    vh.tv_record_perform = (TextView)convertView.findViewById(R.id.tv_perform);
                    vh.tv_type_name = (TextView)convertView.findViewById(R.id.tv_type_name);
                    vh.v_gv_cancer = convertView.findViewById(R.id.v_record_cancer_size);
                    vh.v_gv_quota = convertView.findViewById(R.id.v_record_quota);
                    vh.gv_cancer = (MyGridView)convertView.findViewById(R.id.gv_cancer_size);
                    vh.gv_quota = (MyGridView)convertView.findViewById(R.id.gv_quota);
                    break;
                default:
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.item_rv_medical_record_1, parent, false);
                    vh.tv_space_day = (TextView)convertView.findViewById(R.id.tv_space_day);
                    vh.tv_step_name = (TextView)convertView.findViewById(R.id.tv_step_name);
                    vh.tv_cure_name = (TextView)convertView.findViewById(R.id.tv_conf_name);
                    vh.tv_step_num = (TextView)convertView.findViewById(R.id.tv_num_for_step);
                    vh.tv_step_time = (TextView)convertView.findViewById(R.id.tv_step_time);
                    vh.iv_step_right = (ImageView)convertView.findViewById(R.id.iv_step_right);
                    if (ZYApplication.typeFace!=null){
                        vh.tv_step_num.setTypeface(ZYApplication.typeFace);
                    }
                    vh.v_step_item = convertView.findViewById(R.id.v_step_item);
                    vh.lv_item = (MyListView)convertView.findViewById(R.id.lv_item);
                    vh.v_detail = convertView.findViewById(R.id.v_detail);
                    vh.v_step_remark = convertView.findViewById(R.id.v_step_remark);
                    vh.tv_step_remark = (TextView)convertView.findViewById(R.id.tv_step_remark);
                    break;
            }
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }
        try {
            final StepUserEntity data = list.get(position);
            if (itemType == TYPE_1){
                try {
                    vh.tv_space_day.setText("持续时间："+DataUtils.getDayForEditStep((int)data.getStartTime(),(int)data.getEndTime()));
                    vh.tv_cure_name.setText(TextUtils.isEmpty(data.getCureName())?"":data.getCureName());
                    vh.tv_cure_name.setTextColor(ConfUtils.getResIDForColorRecordText(context,data.getCureConfID()));
                    vh.tv_cure_name.setBackgroundResource(ConfUtils.getResIDForColorRecordBg(data.getCureConfID()));
                    vh.tv_step_num.setText(step_index.get(position,0)+"");
                    vh.tv_step_name.setText(TextUtils.isEmpty(data.getStepName()) ? "" : data.getStepName());
                    vh.tv_step_time.setText(DataUtils.getRecordDateForStep(data.getStartTime()) + "~"
                            + DataUtils.getRecordDateForStep(data.getEndTime()));
                    String step_remark = data.getRemark();
                    if (TextUtils.isEmpty(step_remark)){
                        vh.v_step_remark.setVisibility(View.GONE);
                    }else {
                        vh.v_step_remark.setVisibility(View.VISIBLE);
                        vh.tv_step_remark.setText(Html.fromHtml("<font color=\"#333333\">【<b>阶段说明</b>】</font>"+step_remark));
                        if (flag.get(position)){
                            vh.tv_step_remark.setSingleLine(false);
                        }else {
                            vh.tv_step_remark.setSingleLine(true);
                        }
                    }
                    if (flag.get(position)){
                        vh.iv_step_right.setRotation(0);
                    }else {
                        vh.iv_step_right.setRotation(90);
                    }
                    if (flag.get(position)){
                        try {
                            List<StepUserEntity> temp = data.getChild();
                            if (temp!=null&&temp.size()>0){
                                temp = ConstUtils.sort(temp);
                                MedicalRecordDetailAdapter adapter
                                        = new MedicalRecordDetailAdapter(context,temp,pw,callback,position);
                                vh.lv_item.setAdapter(adapter);
                                vh.v_detail.setVisibility(View.VISIBLE);
                            }else {
                                vh.v_detail.setVisibility(View.GONE);
                            }
                        }catch (Exception e){
                            vh.v_detail.setVisibility(View.GONE);
                            ExceptionUtils.ExceptionSend(e,"flag");
                        }
                    }else {
                        vh.v_detail.setVisibility(View.GONE);
                    }
                    vh.v_step_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (flag.get(position)){
                                flag.put(position,false);
                                notifyDataSetChanged();
                            }else {
                                toDetail(data,position);
                            }
                        }
                    });

                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e,"TYPE_1");
                }

            }else if (itemType == TYPE_2){
                try {

                    final RecordItemEntity entity = data.getRI();
                    vh.date.setText(DataUtils.getPathologyDate(entity.getRecordTime() + ""));

                    final int type = data.getType();

                    if (type == Const.RECORD_TYPE_11){
                        String gene = entity.getGeneID();
                        if (TextUtils.isEmpty(gene)){
                            vh.v_record_gene.setVisibility(View.GONE);
                        }else {
                            vh.v_record_gene.setVisibility(View.VISIBLE);
                            vh.tv_record_gene.setText(MapDataUtils.getGeneForString(gene));
                        }
                    }else {
                        vh.v_record_gene.setVisibility(View.GONE);
                    }

                    if (type == Const.RECORD_TYPE_12){
                        String translate = entity.getTransferBody();
                        if (TextUtils.isEmpty(translate)){
                            vh.v_record_translate.setVisibility(View.GONE);
                        }else {
                            vh.v_record_translate.setVisibility(View.VISIBLE);
                            vh.tv_record_translate.setText(MapDataUtils.getTranslateForString(translate));
                        }
                    }else {
                        vh.v_record_translate.setVisibility(View.GONE);
                    }

                    if (type == Const.RECORD_TYPE_13){
                        String perform = entity.getPerform();
                        if (TextUtils.isEmpty(perform)){
                            vh.v_record_perform.setVisibility(View.GONE);
                        }else {
                            vh.v_record_perform.setVisibility(View.VISIBLE);
                            vh.tv_record_perform.setText(MapDataUtils.getPerformForString(perform));
                        }
                    }else {
                        vh.v_record_perform.setVisibility(View.GONE);
                    }

                    if (type == Const.RECORD_TYPE_14){
                        List<CancerSizeItemEntity> temp = entity.getTumourInfo();
                        if (temp!=null&&temp.size()>0){
                            vh.v_gv_cancer.setVisibility(View.VISIBLE);

                            RecordCancerGvAdapter adapter = new RecordCancerGvAdapter(context,temp);
                            vh.gv_cancer.setAdapter(adapter);
                        }else {
                            vh.v_gv_cancer.setVisibility(View.GONE);
                        }
                    }else {
                        vh.v_gv_cancer.setVisibility(View.GONE);
                    }

                    if (type == Const.RECORD_TYPE_15){
                        List<CancerSizeItemEntity> temp = entity.getCancerMark();
                        if (temp!=null&&temp.size()>0){

                            vh.v_gv_quota.setVisibility(View.VISIBLE);
                            RecordQuotaGvAdapter adapter = new RecordQuotaGvAdapter(context,temp);
                            vh.gv_quota.setAdapter(adapter);
                        }else {
                            vh.v_gv_quota.setVisibility(View.GONE);
                        }
                    }else {
                        vh.v_gv_quota.setVisibility(View.GONE);
                    }

                    vh.tv_type_name.setText(UiUtils.getRecordTitleNameForTypeID(type));
                    vh.iv_type.setImageResource(UiUtils.getRecordImageResForTypeID(type));

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
                    try {
                        if (TextUtils.isEmpty(remark)){
                            vh.remark.setVisibility(View.GONE);
                            vh.remark.setText("");
                        }else {
                            vh.remark.setVisibility(View.VISIBLE);
                            vh.remark.setConvertText(array, position, remark);
                        }
                    }catch (Exception e){
                        ExceptionUtils.ExceptionSend(e,"remark");
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

                    vh.tv_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toDeleteHint(entity,position,type);
                        }
                    });
                    vh.tv_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            callback.onEditItem(entity,position,type,false,0);
                        }
                    });
                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e,"TYPE_2");
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"getView");
        }

        return convertView;
    }

    private SparseIntArray step_index = new SparseIntArray();
    public void update(List<StepUserEntity> list){
        this.list = list;
        int max = 0;
        step_index = new SparseIntArray();
        for (int i = 0;i<list.size();i++){
            if (list.get(i).getType()==0) max++;
        }
        for (int i = 0;i<list.size();i++){
            if (list.get(i).getType()==0) {
                step_index.put(i,max);
                max--;
            }
        }
        array = new SparseBooleanArray();
        click = new SparseBooleanArray();
        flag = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void updateEdit(RecordItemEntity entity,int position ,int itemType,boolean isChild,int childPosition){
        try {
            StepUserEntity mEntity = new StepUserEntity();
            mEntity.setType(itemType);
            mEntity.setRI(entity);
            if (isChild){
                list.get(position).getChild().set(childPosition,mEntity);
            }else {
                list.set(position,mEntity);
            }
            notifyDataSetChanged();
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"updateEdit");
        }

    }

    private void toDeleteHint(final RecordItemEntity entity,final int position ,final int itemType){
        ZYDialog.Builder builder = new ZYDialog.Builder(context);
        builder.setTitle("提示")
                .setMessage("记录删除后将无法找回，请确认是否删除？")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToDelete(entity,position,itemType);
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

    private boolean clickAble = true;
    private int DelID;
    private int DelPos;
    private int DelTypeID;
    private void ToDelete(RecordItemEntity entity,int position ,int itemType){
        if (clickAble){
            clickAble = false;
            DelID = entity.getID();
            DelPos = position;
            switch (itemType){
                case Const.RECORD_TYPE_1:
                case Const.RECORD_TYPE_2:
                case Const.RECORD_TYPE_3:
                case Const.RECORD_TYPE_4:
                case Const.RECORD_TYPE_5:
                case Const.RECORD_TYPE_6:
                case Const.RECORD_TYPE_7:
                case Const.RECORD_TYPE_8:
                case Const.RECORD_TYPE_9:
                    DelTypeID = itemType;
                    Factory.postPhp(this,Const.PDelPresentationOther);
                    break;
                case Const.RECORD_TYPE_11:
                    Factory.postPhp(this,Const.PDelTransferGen);
                    break;
                case Const.RECORD_TYPE_12:
                    Factory.postPhp(this,Const.PDelTransferRecord);
                    break;
                case Const.RECORD_TYPE_13:
                    Factory.postPhp(this,Const.PDelStep2Perform);
                    break;
                case Const.RECORD_TYPE_14:
                    Factory.postPhp(this,Const.PDelQuotaMasterSlave);
                    break;
                case Const.RECORD_TYPE_15:
                    Factory.postPhp(this,Const.PDelCancerMark);
                    break;
                default:
                    clickAble = true;
                    break;
            }
        }
    }

    public void toDetail(StepUserEntity entity,int position){
        if (click.get(position)){
            List<StepUserEntity> temp = list.get(position).getChild();
            if (temp==null||temp.size()==0){
                showToast("该阶段内无记录");
                if (!TextUtils.isEmpty(entity.getRemark())){
                    this.flag.put(position,true);
                    notifyDataSetChanged();
                }
            }else {
                toLoading();
                this.flag.put(position,true);
                notifyDataSetChanged();
            }
        }else {
            if (clickAble){
                clickAble = false;
                ID = entity.getID();
                pos = position;
                Factory.postPhp(this,Const.PGetStepUserOtherInfo);
            }
        }
    }

    private void toLoading(){
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("正在加载...");
        mProgressDialog.show();
        toWait();
    }



    private int ID;
    private int pos;
    @Override
    public Map getParamInfo(int tag) {
        Map<String,String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(context));
        if (tag == Const.PGetStepUserOtherInfo){
            map.put("ID",ID+"");
        } else if (tag == Const.PDelTransferGen || tag == Const.PDelTransferRecord || tag == Const.PDelStep2Perform
                || tag == Const.PDelQuotaMasterSlave || tag == Const.PDelCancerMark){
            map.put("ID",DelID + "");
        } else if (tag == Const.PDelPresentationOther){
            map.put("ID",DelID + "");
            map.put("Type",DelTypeID+"");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }


    @Override
    public void toActivity(Object response, int flag) {

        if (flag == Const.PGetStepUserOtherInfo){
            MedicalBaseBean bean = (MedicalBaseBean)response;
            if (Const.RESULT.equals(bean.getiResult())){
                StepUserEntity entity = list.get(pos);
                List<StepUserEntity> temp = ConstUtils.getListForMedicalRecord(bean.getData());
                click.put(pos,true);
                if (temp==null||temp.size()==0){
                    if (TextUtils.isEmpty(entity.getRemark())){

                    }else {
                        this.flag.put(pos,true);
                        notifyDataSetChanged();
                    }
                    if (mProgressDialog!=null){
                        mProgressDialog.dismiss();
                    }
                    showToast("该阶段内无记录");
                } else {
                    list.get(pos).setChild(temp);
                    this.flag.put(pos, true);
                    notifyDataSetChanged();
                    toWait();
                }
            }else {
                if (mProgressDialog!=null){
                    mProgressDialog.dismiss();
                }
                showToast("获取阶段内数据失败");
            }
            clickAble = true;

        }else if (flag == Const.PDelTransferGen || flag == Const.PDelTransferRecord || flag == Const.PDelStep2Perform
                || flag == Const.PDelQuotaMasterSlave || flag == Const.PDelCancerMark){
            PhpUserInfoBean bean = (PhpUserInfoBean)response;
            if (Const.RESULT.equals(bean.getiResult())){
                list.remove(DelPos);
                notifyDataSetChanged();
                showToast("删除成功");
            }else {
                showToast("删除失败");
            }
            clickAble = true;
        }else if (flag == Const.PDelPresentationOther){
            PhpUserInfoBean bean = (PhpUserInfoBean)response;
            if (Const.RESULT.equals(bean.getiResult())){
                list.remove(DelPos);
                notifyDataSetChanged();
                showToast("删除成功");
            }else {
                showToast("删除失败");
            }
            clickAble = true;
        }

    }

    private void toWait(){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(500);
                }catch (Exception e){

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (mProgressDialog!=null){
                    mProgressDialog.dismiss();
                }
            }
        }.execute();
    }

    private void showToast(String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }

    private ProgressDialog mProgressDialog;
    @Override
    public void showLoading(int flag) {
        if (flag == Const.PGetStepUserOtherInfo){
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("正在获取该阶段内记录");
            mProgressDialog.show();
        } else if (flag ==  Const.PDelTransferGen || flag == Const.PDelTransferRecord || flag == Const.PDelStep2Perform
                || flag == Const.PDelQuotaMasterSlave || flag == Const.PDelCancerMark){
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("正在删除...");
            mProgressDialog.show();
        }
    }



    @Override
    public void hideLoading(int flag) {
        if (flag == Const.PGetStepUserOtherInfo){
            if (mProgressDialog!=null){
                mProgressDialog.dismiss();
            }
        } else if (flag ==  Const.PDelTransferGen || flag == Const.PDelTransferRecord || flag == Const.PDelStep2Perform
                || flag == Const.PDelQuotaMasterSlave || flag == Const.PDelCancerMark){
            if (mProgressDialog!=null){
                mProgressDialog.dismiss();
            }
        }
    }

    @Override
    public void showError(int flag) {
        clickAble = true;
        if (flag == Const.PGetStepUserOtherInfo && mProgressDialog!=null){
            mProgressDialog.dismiss();
        }else if (flag ==  Const.PDelTransferGen || flag == Const.PDelTransferRecord || flag == Const.PDelStep2Perform
                || flag == Const.PDelQuotaMasterSlave || flag == Const.PDelCancerMark){
            if (mProgressDialog!=null){
                mProgressDialog.dismiss();
            }
        }
        showToast("网络请求失败");
    }

    class ViewHolder{
        TextView tv_step_name,tv_step_num,tv_step_time,tv_cure_name,tv_delete,tv_edit,tv_space_day;
        ImageView iv_step_right,iv_type;
        TextView date,hospital,doctor;
        CustomExpandableTextView remark;
        View v_gv,v_hospital,v_doctor,v_record_gene,v_record_translate,v_record_perform;
        View v_gv_cancer,v_gv_quota,v_step_item;
        MyGridView gv,gv_cancer,gv_quota;
        MyListView lv_item;
        View v_detail;
        TextView tv_record_gene,tv_record_translate,tv_record_perform,tv_type_name;
        TextView tv_step_remark;
        View v_step_remark;
    }


}

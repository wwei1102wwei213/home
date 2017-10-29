package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.Entity.CancerSizeItemEntity;
import com.zeyuan.kyq.Entity.RecordItemEntity;
import com.zeyuan.kyq.Entity.StepUserEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.onEditItemListener;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UiUtils;
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
 * Created by Administrator on 2017/3/10.
 *
 * @author wwei
 */
public class MedicalRecordDetailAdapter extends BaseAdapter implements HttpResponseInterface {

    private Context context;
    private List<StepUserEntity> list;
    private SparseBooleanArray array = new SparseBooleanArray();
    private int pw;
    private onEditItemListener callback;
    private int index;

    public MedicalRecordDetailAdapter(Context context, List<StepUserEntity> list, int pw, onEditItemListener callback, int index) {
        this.context = context;
        this.list = list;
        this.pw = pw;
        this.callback = callback;
        this.index = index;
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

        ViewHolder vh = null;

        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_rv_medical_record_child, parent, false);
            vh.date = (TextView) convertView.findViewById(R.id.tv_date);
            vh.tv_edit = (TextView) convertView.findViewById(R.id.tv_edit);
            vh.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
            vh.iv_type = (ImageView) convertView.findViewById(R.id.iv_type);
            vh.remark = (CustomExpandableTextView) convertView.findViewById(R.id.tv_remark);
            vh.v_gv = convertView.findViewById(R.id.v_gv);
            vh.gv = (MyGridView) convertView.findViewById(R.id.gv);
            vh.hospital = (TextView) convertView.findViewById(R.id.tv_hospital);
            vh.doctor = (TextView) convertView.findViewById(R.id.tv_doctor);
            vh.v_hospital = convertView.findViewById(R.id.v_hospital);
            vh.v_doctor = convertView.findViewById(R.id.v_doctor);
            vh.v_record_gene = convertView.findViewById(R.id.v_record_gene);
            vh.v_record_translate = convertView.findViewById(R.id.v_record_translate);
            vh.v_record_perform = convertView.findViewById(R.id.v_perform);
            vh.tv_record_gene = (TextView) convertView.findViewById(R.id.tv_record_gene);
            vh.tv_record_translate = (TextView) convertView.findViewById(R.id.tv_record_translate);
            vh.tv_record_perform = (TextView) convertView.findViewById(R.id.tv_perform);
            vh.tv_type_name = (TextView) convertView.findViewById(R.id.tv_type_name);
            vh.v_gv_cancer = convertView.findViewById(R.id.v_record_cancer_size);
            vh.v_gv_quota = convertView.findViewById(R.id.v_record_quota);
            vh.gv_cancer = (MyGridView) convertView.findViewById(R.id.gv_cancer_size);
            vh.gv_quota = (MyGridView) convertView.findViewById(R.id.gv_quota);
            vh.tv_year = (TextView) convertView.findViewById(R.id.tv_year_child);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        try {
            final StepUserEntity data = list.get(position);
            try {
                final RecordItemEntity entity = data.getRI();
                vh.date.setText(DataUtils.getPatientDateNoYear(entity.getRecordTime() + ""));
                vh.tv_year.setText(DataUtils.getPathologyYear(entity.getRecordTime()) + "年");
                final int type = data.getType();
                if (type == Const.RECORD_TYPE_11) {
                    String gene = entity.getGeneID();
                    if (TextUtils.isEmpty(gene)) {
                        vh.v_record_gene.setVisibility(View.GONE);
                    } else {
                        vh.v_record_gene.setVisibility(View.VISIBLE);
                        vh.tv_record_gene.setText(MapDataUtils.getGeneForString(gene));
                    }
                } else {
                    vh.v_record_gene.setVisibility(View.GONE);
                }

                if (type == Const.RECORD_TYPE_12) {
                    String translate = entity.getTransferBody();
                    if (TextUtils.isEmpty(translate)) {
                        vh.v_record_translate.setVisibility(View.GONE);
                    } else {
                        vh.v_record_translate.setVisibility(View.VISIBLE);
                        vh.tv_record_translate.setText(MapDataUtils.getTranslateForString(translate));
                    }
                } else {
                    vh.v_record_translate.setVisibility(View.GONE);
                }

                if (type == Const.RECORD_TYPE_13) {
                    String perform = entity.getPerform();
                    if (TextUtils.isEmpty(perform)) {
                        vh.v_record_perform.setVisibility(View.GONE);
                    } else {
                        vh.v_record_perform.setVisibility(View.VISIBLE);
                        vh.tv_record_perform.setText(MapDataUtils.getPerformForString(perform));
                    }
                } else {
                    vh.v_record_perform.setVisibility(View.GONE);
                }

                if (type == Const.RECORD_TYPE_14) {
                    List<CancerSizeItemEntity> temp = entity.getTumourInfo();
                    if (temp != null && temp.size() > 0) {
                        vh.v_gv_cancer.setVisibility(View.VISIBLE);
                        RecordCancerGvAdapter adapter = new RecordCancerGvAdapter(context, temp);
                        vh.gv_cancer.setAdapter(adapter);
                    } else {
                        vh.v_gv_cancer.setVisibility(View.GONE);
                    }
                } else {
                    vh.v_gv_cancer.setVisibility(View.GONE);
                }

                if (type == Const.RECORD_TYPE_15) {
                    List<CancerSizeItemEntity> temp = entity.getCancerMark();
                    if (temp != null && temp.size() > 0) {
                        vh.v_gv_quota.setVisibility(View.VISIBLE);
                        RecordQuotaGvAdapter adapter = new RecordQuotaGvAdapter(context, temp);
                        vh.gv_quota.setAdapter(adapter);
                    } else {
                        vh.v_gv_quota.setVisibility(View.GONE);
                    }
                } else {
                    vh.v_gv_quota.setVisibility(View.GONE);
                }

                vh.tv_type_name.setText(UiUtils.getRecordTitleNameForTypeID(type));
                vh.iv_type.setImageResource(UiUtils.getRecordImageResForTypeID(type));

                String hospital = entity.getHospital();
                if (TextUtils.isEmpty(hospital)) {
                    vh.v_hospital.setVisibility(View.GONE);
                } else {
                    vh.v_hospital.setVisibility(View.VISIBLE);
                    vh.hospital.setText(hospital);
                }
                String doctor = entity.getDoctor();
                if (TextUtils.isEmpty(doctor)) {
                    vh.v_doctor.setVisibility(View.GONE);
                } else {
                    vh.v_doctor.setVisibility(View.VISIBLE);
                    vh.doctor.setText(doctor);
                }
                String remark = entity.getRemark();
                try {
                    if (TextUtils.isEmpty(remark)) {
                        vh.remark.setVisibility(View.GONE);
                        vh.remark.setText("");
                    } else {
                        vh.remark.setVisibility(View.VISIBLE);
                        vh.remark.setConvertText(array, position, remark);
                    }
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "remark");
                }
                final List<String> pic = entity.getPic();
                if (pic == null || pic.size() == 0) {
                    vh.v_gv.setVisibility(View.GONE);
                } else {
                    vh.v_gv.setVisibility(View.VISIBLE);
                    List<String> temp = new ArrayList<>();
                    if (pic.size() > 3) {
                        for (int i = 0; i < 3; i++) {
                            temp.add(pic.get(i));
                        }
                    } else {
                        temp = pic;
                    }
                    RecordItemGVAdapter adapter = new RecordItemGVAdapter(context, temp, pw, pic.size());
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
                        toDeleteHint(entity, position, type);
                    }
                });
                vh.tv_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (callback != null)
                            callback.onEditItem(entity, index, type, true, position);
                    }
                });
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "TYPE_2");
            }


        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "getView");
        }

        return convertView;
    }

    private void toDeleteHint(final RecordItemEntity entity, final int position, final int itemType) {
        ZYDialog.Builder builder = new ZYDialog.Builder(context);
        builder.setTitle("提示")
                .setMessage("记录删除后将无法找回，请确认是否删除？")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToDelete(entity, position, itemType);
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

    private void ToDelete(RecordItemEntity entity, int position, int itemType) {
        if (clickAble) {
            clickAble = false;
            DelID = entity.getID();
            DelPos = position;
            switch (itemType) {
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
                    Factory.postPhp(this, Const.PDelPresentationOther);
                    break;
                case Const.RECORD_TYPE_11:
                    Factory.postPhp(this, Const.PDelTransferGen);
                    break;
                case Const.RECORD_TYPE_12:
                    Factory.postPhp(this, Const.PDelTransferRecord);
                    break;
                case Const.RECORD_TYPE_13:
                    Factory.postPhp(this, Const.PDelStep2Perform);
                    break;
                case Const.RECORD_TYPE_14:
                    Factory.postPhp(this, Const.PDelQuotaMasterSlave);
                    break;
                case Const.RECORD_TYPE_15:
                    Factory.postPhp(this, Const.PDelCancerMark);
                    break;
                default:
                    clickAble = true;
                    break;
            }
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(context));
        if (tag == Const.PDelTransferGen || tag == Const.PDelTransferRecord || tag == Const.PDelStep2Perform
                || tag == Const.PDelQuotaMasterSlave || tag == Const.PDelCancerMark) {
            map.put("ID", DelID + "");
        } else if (tag == Const.PDelPresentationOther) {
            map.put("ID", DelID + "");
            map.put("Type", DelTypeID + "");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {

        if (flag == Const.PDelTransferGen || flag == Const.PDelTransferRecord || flag == Const.PDelStep2Perform
                || flag == Const.PDelQuotaMasterSlave || flag == Const.PDelCancerMark) {
            PhpUserInfoBean bean = (PhpUserInfoBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                list.remove(DelPos);
                notifyDataSetChanged();
                showToast("删除成功");
            } else {
                showToast("删除失败");
            }
            clickAble = true;
        } else if (flag == Const.PDelPresentationOther) {
            PhpUserInfoBean bean = (PhpUserInfoBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                list.remove(DelPos);
                notifyDataSetChanged();
                showToast("删除成功");
            } else {
                showToast("删除失败");
            }
            clickAble = true;
        }

    }

    private void showToast(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(int flag) {

    }

    @Override
    public void hideLoading(int flag) {

    }

    @Override
    public void showError(int flag) {
        clickAble = true;
        showToast("网络请求失败");
    }

    class ViewHolder {
        TextView tv_delete, tv_edit;
        ImageView iv_type;
        TextView date, hospital, doctor;
        CustomExpandableTextView remark;
        View v_gv, v_hospital, v_doctor, v_record_gene, v_record_translate, v_record_perform;
        View v_gv_cancer, v_gv_quota;
        MyGridView gv, gv_cancer, gv_quota;
        TextView tv_record_gene, tv_record_translate, tv_record_perform, tv_type_name, tv_year;
    }

}

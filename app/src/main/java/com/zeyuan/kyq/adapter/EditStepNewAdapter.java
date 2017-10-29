package com.zeyuan.kyq.adapter;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.Entity.EditStepItemEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.ChooseTimeNewInterface;
import com.zeyuan.kyq.biz.forcallback.EditBackInterface;
import com.zeyuan.kyq.fragment.ChooseTimeEditStepFragment;
import com.zeyuan.kyq.fragment.CustomEditFragment;
import com.zeyuan.kyq.fragment.dialog.CureTypeDialog;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/8.
 *
 * @author wwei
 */
public class EditStepNewAdapter extends BaseAdapter implements ChooseTimeNewInterface,HttpResponseInterface{

    private Context context;
    private List<EditStepItemEntity> list;
    private static final int TIME_BEGIN = 1;
    private static final int TIME_END = 2;
    private int TIME_FLAG = 0;
    private boolean changed = false;
    private int DeleteID = -1;
    private int DeletePos = -1;

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public EditStepNewAdapter(Context context,List<EditStepItemEntity> list){
        this.context = context;
        this.list = list;
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

        if (convertView==null){
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_edit_step_new,null);
            vh.begin = (TextView)convertView.findViewById(R.id.tv_time_begin);
            vh.end = (TextView)convertView.findViewById(R.id.tv_time_end);
            vh.days = (TextView)convertView.findViewById(R.id.tv_days);
            vh.order = (TextView)convertView.findViewById(R.id.tv_order);
            vh.name = (TextView)convertView.findViewById(R.id.tv_step);
            vh.now = (TextView)convertView.findViewById(R.id.tv_current);
            vh.valid = (TextView)convertView.findViewById(R.id.tv_valid);
            vh.no_valid = (TextView)convertView.findViewById(R.id.tv_no_valid);
            vh.delete = convertView.findViewById(R.id.v_delete);
            vh.left = convertView.findViewById(R.id.v_remark_left);
            vh.right = convertView.findViewById(R.id.v_remark_right);
            vh.remark = (TextView)convertView.findViewById(R.id.tv_remark);
            vh.v_remark = convertView.findViewById(R.id.v_remark);
            if (ZYApplication.typeFace!=null){
                vh.begin.setTypeface(ZYApplication.typeFace);
                vh.end.setTypeface(ZYApplication.typeFace);
                vh.days.setTypeface(ZYApplication.typeFace);
                vh.order.setTypeface(ZYApplication.typeFace);
            }
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }

        try {

            final EditStepItemEntity entity = list.get(position);
            String name = DataUtils.loadStringToShowString(entity.getStepID()+"");
            vh.name.setText(name);
            vh.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setStepChange(position);
                }
            });

            vh.order.setText((list.size() - position) + "");
            if (entity.getIsMedicineValid()==1){
                vh.valid.setSelected(true);
                vh.no_valid.setSelected(false);
                vh.no_valid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setStepValid(position,0);
                    }
                });
            }else {
                vh.valid.setSelected(false);
                vh.valid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setStepValid(position,1);
                    }
                });
                vh.no_valid.setSelected(true);
            }


            if (entity.getIsNowStep()==1){
                vh.now.setSelected(true);
                vh.now.setText("当前阶段");
            }else {
                vh.now.setSelected(false);
                vh.now.setText("设为当前阶段");
            }
            vh.now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (entity.getIsNowStep()!=1){
                        setNowStep(position);
                    }
                }
            });
            vh.begin.setText(DataUtils.getStartTime(entity.getStartTime()));
            vh.begin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setShowTime(position, TIME_BEGIN ,false,"0");
                }
            });
            vh.end.setText(DataUtils.getEndTime(entity.getEndTime()));
            vh.end.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setShowTime(position, TIME_END , true,"0");
                }
            });
            vh.days.setText(DataUtils.getDayForEditStep(entity.getStartTime(),entity.getEndTime()));
            if (TextUtils.isEmpty(entity.getRemark())){
                vh.left.setVisibility(View.GONE);
                vh.right.setVisibility(View.GONE);
                vh.remark.setText("");
                vh.remark.setHint("添加阶段说明");
            }else {
                vh.left.setVisibility(View.VISIBLE);
                vh.right.setVisibility(View.VISIBLE);
                vh.remark.setText(entity.getRemark());
            }
            vh.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (entity.getIsNowStep() == 1) {
                        Toast.makeText(context, "当前阶段不能删除", Toast.LENGTH_SHORT).show();
                    } else {
                        toDeleteStep(entity.getID(),position);
                    }
                }
            });
            vh.v_remark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setEditRemark(entity.getRemark(),position);
                }
            });
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"v");
        }
        return convertView;
    }

    private void toDeleteStep(final int deleteID,final int pos){
        ZYDialog dialog = new ZYDialog.Builder(context).setTitle("提示").setMessage("阶段信息删除后不可恢复，请确认是否删除?")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteID = deleteID;
                        DeletePos = pos;
                        Factory.postPhp(EditStepNewAdapter.this, Const.PDelAppStepUser);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("不删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    private void setEditRemark(String remark, final int position){
        if (TextUtils.isEmpty(remark)) remark = "";
        CustomEditFragment fragment = CustomEditFragment.getInstance(new EditBackInterface() {
            @Override
            public void editCallBack(String text) {
                setSaveRemark(text,position);
            }
        }, remark, null, 0);
        fragment.show(((BaseActivity)context).getFragmentManager(), CustomEditFragment.type);
    }

    private void setSaveRemark(String text,int position){
        list.get(position).setRemark(text);
        changed = true;
        notifyDataSetChanged();
    }

    private void setStepChange(final int pos){
        try {
            CureTypeDialog dialog = new CureTypeDialog();
            dialog.setCancerID(UserinfoData.getCancerID(context));
            dialog.setDrugsNameListener(new DialogFragmentListener() {
                @Override
                public void getDataFromDialog(DialogFragment dialog, String data, int positions) {
                    setStepChangeUpdate(data, pos);
                }
            });
            FragmentManager fm = ((BaseActivity)context).getFragmentManager();
            dialog.show(fm, "medica");
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"setStepChange");
        }
    }

    private void setStepChangeUpdate(String data,int position){
        if (TextUtils.isEmpty(data)) return;
        if (list.get(position).getStepID()!=Integer.valueOf(data)){
            list.get(position).setStepID(Integer.valueOf(data));
            changed = true;
            notifyDataSetChanged();
        }
    }

    private void setNowStep(int position){
        for (int i=0;i<list.size();i++){
            if (i==position){
                list.get(i).setIsNowStep(1);
            }else {
                list.get(i).setIsNowStep(0);
            }
        }
        changed = true;
        notifyDataSetChanged();
    }

    public String getNowStepID(){
        if (list==null||list.size()==0) return null;
        for (int i=0;i<list.size();i++){
            if (list.get(i).getIsNowStep()==1){
                return list.get(i).getStepID()+"";
            }
        }
        return null;
    }

    private void setStepValid(int position,int flag){
        list.get(position).setIsMedicineValid(flag);
        changed = true;
        notifyDataSetChanged();
    }

    public void update(List<EditStepItemEntity> list){
        this.list = list;
        changed = false;
        sort();
        notifyDataSetChanged();
    }

    public String getDataList(){
        if (list==null||list.size()==0) return "[]";
        return list.toString();
    }

    class ViewHolder{
        TextView begin;
        TextView end;
        TextView days;
        TextView order;
        TextView name;
        TextView now;
        TextView valid;
        TextView no_valid;
        View left;
        View right;
        TextView remark;
        View v_remark;
        View delete;
    }

    private ChooseTimeEditStepFragment fragment;
    private int TIME_POSITION = -1;
    private void setShowTime(int position,int flag,boolean isEnd,String oldTime){
        try {
            ChooseTimeEditStepFragment  fragment = ChooseTimeEditStepFragment.getInstance(this,isEnd,flag,position,oldTime);
            fragment.show(((BaseActivity)context).getFragmentManager(),ChooseTimeEditStepFragment.type);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"setShowTime");
        }
    }

    /*@Override
    public void timeCallBack(String time) {
        if (TIME_POSITION!=-1){
            if (TIME_FLAG==TIME_BEGIN){
                list.get(TIME_POSITION).setStartTime(Integer.valueOf(time));
            }else if (TIME_FLAG==TIME_END){
                list.get(TIME_POSITION).setEndTime(Integer.valueOf(time));
            }
            TIME_POSITION = -1;
            TIME_FLAG = 0;
            changed = true;
            notifyDataSetChanged();
        }
    }*/

    @Override
    public void onTimeCallBack(String time, int ViewTag, int selection) {
        if (ViewTag==TIME_BEGIN){
            list.get(selection).setStartTime(Integer.valueOf(time));
        }else if (ViewTag==TIME_END){
            list.get(selection).setEndTime(Integer.valueOf(time));
        }
        changed = true;
        notifyDataSetChanged();
    }

    private void sort(){
        Collections.sort(list, new Comparator<EditStepItemEntity>() {
            @Override
            public int compare(EditStepItemEntity e1, EditStepItemEntity e2) {

                if (e1.getEndTime()==0&&e2.getEndTime()==0){
                    return e2.getStartTime() - e1.getStartTime();
                }else if(e1.getEndTime()==0){
                    return -1;
                }else if(e2.getEndTime()==0){
                    return 1;
                }else if(e1.getEndTime()==e2.getEndTime()){
                    return e2.getStartTime() - e1.getStartTime();
                }else {
                    return e2.getEndTime() - e1.getEndTime();
                }
            }
        });
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String,String> map = new HashMap<>();
        if (tag == Const.PDelAppStepUser){
            map.put(Contants.InfoID,UserinfoData.getInfoID(context));
            map.put(Contants.StepUID,DeleteID+"");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PDelAppStepUser){
            PhpUserInfoBean bean = (PhpUserInfoBean)response;
            if (Const.RESULT.equals(bean.getiResult())){
                Toast.makeText(context,"阶段删除成功",Toast.LENGTH_SHORT).show();
                list.remove(DeletePos);
                notifyDataSetChanged();
            }else {
                Toast.makeText(context,"阶段删除失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void showLoading(int flag) {

    }

    @Override
    public void hideLoading(int flag) {

    }

    @Override
    public void showError(int flag) {
        Toast.makeText(context,"网络请求失败",Toast.LENGTH_SHORT).show();
    }
}

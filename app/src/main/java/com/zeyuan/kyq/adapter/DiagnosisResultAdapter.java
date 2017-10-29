package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.FindSymtomBean;

import java.util.List;

/**
 * Created by Administrator on 2015/10/6.
 */
public class DiagnosisResultAdapter extends BaseAdapter {

    private List<FindSymtomBean.CommPolicyEntity> CommPolicy;
    private Context context;
    private LayoutInflater inflater;
    public static final int RED = 1;
    public static final int BLUE = 2;
    private int flag = 0;

    public DiagnosisResultAdapter(Context context,List<FindSymtomBean.CommPolicyEntity> CommPolicy) {
        this.context = context;
        this.CommPolicy = CommPolicy;
        inflater= LayoutInflater.from(context);
    }

    public DiagnosisResultAdapter(Context context,List<FindSymtomBean.CommPolicyEntity> CommPolicy,int flag) {
        this.context = context;
        this.CommPolicy = CommPolicy;
        this.flag = flag;
        inflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return CommPolicy.size();
    }

    @Override
    public String getItem(int position) {
        return CommPolicy.get(position).getCommPolicyID();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_diagnosis_result, null);
            holder.number = (TextView) view.findViewById(R.id.number);
            holder.diagnosis_result = (TextView) view.findViewById(R.id.diagnosis_result);
            view.setTag(holder);
        } else{
            holder = (ViewHolder) view.getTag();
        }
        holder.number.setText(String.valueOf(position + 1));
        if (flag!=0){
            if (flag == RED){
                holder.number.setBackgroundResource(R.mipmap.diag_eff_item);
            }else if (flag == BLUE){
                holder.number.setBackgroundResource(R.mipmap.diag_other_item);
            }
        }

        holder.diagnosis_result.setText(CommPolicy.get(position).getCommPolicyName());

        return view;
    }

    public void update(List<FindSymtomBean.CommPolicyEntity> list){
        CommPolicy = list;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView number;
        TextView diagnosis_result;
    }
}

package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.ChemStep;

import java.util.ArrayList;
import java.util.List;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-12-21
 * Time: 14:27
 * FIXME
 * 诊断结果中 头部的伴随症状的adapter
 */


public class FollowSymptomAdapter extends BaseAdapter {
    private List chooseList;//这个是选中的伴随症状
    private List<String> symptom;
    private Context context;

    private LayoutInflater inflater;


    public FollowSymptomAdapter(Context context, List<String> symptom) {
        chooseList = new ArrayList();
        this.symptom = symptom;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
//        return symptom.size();
        return symptom.size();
    }


    @Override
    public Object getItem(int position) {
        return symptom.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_checkbox, null);
            holder.cb = (CheckBox) convertView.findViewById(R.id.cb_result);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.cb.setText(symptom.get(position));
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    chooseList.add(symptom.get(position));
                } else {
                    chooseList.remove(symptom.get(position));
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        CheckBox cb;
    }

    public void update(List data) {
        this.symptom = data;
        notifyDataSetChanged();
    }

    public List getChooseList() {
        return chooseList;
    }

}

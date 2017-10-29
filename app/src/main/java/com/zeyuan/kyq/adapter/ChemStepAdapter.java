package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.ChemStep;

import java.util.List;

/**
 * Created by Administrator on 2015/8/27.
 */
public class ChemStepAdapter extends BaseAdapter {
    private List<ChemStep> chemSteps;
    private Context context;

    private LayoutInflater inflater;


    public ChemStepAdapter(Context context, List<ChemStep> chemSteps) {
        this.chemSteps = chemSteps;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
//        return chemSteps.size();
        return 2;
    }


    @Override
    public ChemStep getItem(int position) {
        return chemSteps.get(position);
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
            view = inflater.inflate(R.layout.item_step, null);
            holder.chem_time = (TextView) view.findViewById(R.id.chem_time);
            holder.step_number = (TextView) view.findViewById(R.id.step_number);
            holder.chem_name = (TextView) view.findViewById(R.id.chem_name);
            holder.stopChem = (Button) view.findViewById(R.id.stopChem);
            view.setTag(holder);
        } else{
            holder = (ViewHolder) view.getTag();
        }






        return view;
    }

    static class ViewHolder {
        TextView chem_name;
        TextView step_number;
        TextView chem_time;
        Button stopChem;
    }
}

package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.http.bean.UserStepBean;
import com.zeyuan.kyq.http.bean.UserStepChildBean;
import com.zeyuan.kyq.utils.DateTime;
import com.zeyuan.kyq.utils.MapDataUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/4/29.
 *
 *
 *
 * @author wwei
 */
public class StepDetailAdapter extends BaseAdapter{


    private List<UserStepBean> steps;
    private List<Boolean> flags;
    private Context context;
    private List<UserStepChildBean> stepChilds;

    public StepDetailAdapter(Context context,List<UserStepBean> steps,List<Boolean> flags,List<UserStepChildBean> stepChilds){
        this.steps = steps;
        this.flags = flags;
        this.stepChilds = stepChilds;
        this.context = context;
    }

    @Override
    public int getCount() {
        return steps.size();
    }

    @Override
    public Object getItem(int position) {
        return steps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StepViewHolder holder = null;
        if (convertView == null) {
            holder = new StepViewHolder();
            convertView = View.inflate(context,R.layout.item_group, null);
            holder.img = (ImageView) convertView.findViewById(R.id.indicator);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.step_medica = (TextView) convertView.findViewById(R.id.step_medica);
            holder.step_number = (TextView) convertView.findViewById(R.id.step_number).findViewById(R.id.number);
            convertView.setTag(holder);
        } else {
            holder = (StepViewHolder) convertView.getTag();
        }
        holder.step_number.setText("" + (steps.size() - position));
        UserStepBean group = steps.get(position);
        String begStr = group.isBegTimeFirst() ? "更早" : DateTime.from(group.getCompareDateBeg() * 1000).toDateString();
        String endStr = group.isEndTimeLast() ? "至今" : DateTime.from(group.getCompareDateEnd() * 1000).toDateString();
        String time = begStr.concat(" 至 ").concat(endStr);
        holder.time.setText(time);
        if (group.isSpace()) {
            holder.step_medica.setText("未知阶段");
        } else {
            String medica = MapDataUtils.getAllStepName(group.getStepID());
            holder.step_medica.setText(medica);
        }
        if(flags.get(position)){
            holder.img.setBackgroundResource(R.mipmap.up);

        }else {
            holder.img.setBackgroundResource(R.mipmap.down);
        }
        return null;
    }

    class StepViewHolder {
        //        View green_line_bottom;
        ImageView img;
        TextView step_medica;//阶段的药物
        TextView time;//阶段的时间
        TextView step_number;
    }

    class ViewHolder{
        TextView red_line1;
        TextView red_line2;
        LinearLayout add_content;
        TextView zhibiao_time;
        TextView zhibiao_cea;//cea
        TextView main_length;//主肿瘤
        TextView transfer_pos;//转移情况
        ImageView zhibiao_edit;
        ImageView zhibiao_delete;
    }

    class ZhibiaoViewHolder {
        TextView red_line1;
        TextView red_line2;
        LinearLayout add_content;
        TextView zhibiao_time;
        TextView zhibiao_cea;//cea
        TextView main_length;//主肿瘤
        TextView transfer_pos;//转移情况
        ImageView zhibiao_edit;
        ImageView zhibiao_delete;
    }

    class SymptomViewHolder {
        TextView red_line1;
        TextView red_line2;
        TextView symptom_time;
        TextView symptom_desc;
        TextView symptom_tips;
        ImageView symptom_edit;
        ImageView symptom_delete;
    }
}

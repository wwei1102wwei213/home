package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.CancerResuletBean;
import com.zeyuan.kyq.utils.DensityUtils;

import java.util.List;

/**
 * Created by Administrator on 2015/10/10.
 *
 *
 *
 * @author wwei
 */
public class CancerDevelopmentAdapter extends BaseAdapter {
    private static final int type1 = 0;
    private static final int type2 = 1;//联合用药

    /**
     * 用来传值给activity
     */
    public interface CheckBoxCheckListener {
        void checkItem(BaseAdapter adapter, String item, int position);
    }


    private CheckBoxCheckListener checkBoxCheckListener;
    private int selectPosition = -1;
    private List<CancerResuletBean.StepEntity> datas;
    private Context context;
    private LayoutInflater inflater;

    public CancerDevelopmentAdapter(Context context, List<CancerResuletBean.StepEntity> datas) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }


    public void setCheckBoxCheckListener(CheckBoxCheckListener checkBoxCheckListener) {
        this.checkBoxCheckListener = checkBoxCheckListener;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public CancerResuletBean.StepEntity getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    boolean tag = true;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        final ViewHolder holder;
        int type = getItemViewType(position);
        if (type == type2) {//这个是可折叠的项
//            if(convertView == null) {
//            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.cancer_result_item, null);
            TextView tv = (TextView) convertView.findViewById(R.id.content);
            tv.setText(datas.get(position).getAlias());
            LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.ll);
            List<CancerResuletBean.StepEntity.CombineStepidEntity> list = datas.get(position).getCombineStepid();
            final LinearLayout temp = new LinearLayout(context);
            temp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            temp.setOrientation(LinearLayout.VERTICAL);
            for (CancerResuletBean.StepEntity.CombineStepidEntity item : list) {
                View v = inflater.inflate(R.layout.layout_lianhe, null);
                TextView tvs = (TextView) v.findViewById(R.id.content);
                CheckBox cb = (CheckBox) v.findViewById(R.id.cb);
                String stepid = item.getStepid();
                tvs.setText(stepid);
                temp.addView(v);
                View s = new View(context);//每个项目的分割线
                s.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtils.dpToPx(context, 1)));
                s.setBackgroundResource(R.color.line_color);
                temp.addView(s);
            }
            ll.addView(temp);
            temp.setVisibility(View.GONE);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(tag) {
                        temp.setVisibility(View.GONE);
                        tag = false;
                    }else {
                        temp.setVisibility(View.VISIBLE);
                        tag = true;
                    }
                }
            });
            return convertView;
//            }
        } else {

            convertView = inflater.inflate(R.layout.layout_lianhe, null);
            TextView tv = (TextView) convertView.findViewById(R.id.content);
            CheckBox cb = (CheckBox) convertView.findViewById(R.id.cb);
            tv.setText(datas.get(position).getAlias());
            return convertView;

        }

//        if (convertView == null) {
//            holder = new ViewHolder();
//            convertView = inflater.inflate(R.layout.item_cancer_development, null);
//            holder.tv = (TextView) convertView.findViewById(R.id.tv);
//            holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    setSelectPosition(position);
//                    if (checkBoxCheckListener != null) {
////                        checkBoxCheckListener.checkItem(CancerDevelopmentAdapter.this, getItem(position), position);
//                    }
//
//                }
//            }
//        });

////        holder.tv.setText(datas.get(position));
//        if (position != selectPosition) {
//            holder.cb.setChecked(false);
//        }

//        return convertView;
    }

    class ViewHolder {
        TextView tv;
        CheckBox cb;
    }

    public void setSelectPosition(int position) {
        selectPosition = position;
        notifyDataSetChanged();
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        CancerResuletBean.StepEntity item = datas.get(position);
        String Combineid = item.getCombineid();
        if (TextUtils.isEmpty(Combineid)) {
            return type1;//说明这个不是联合用药
        } else {
            return type2;
        }
    }



    public void update(List datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }
}

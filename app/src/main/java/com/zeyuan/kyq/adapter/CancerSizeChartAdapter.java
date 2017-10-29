package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.zeyuan.kyq.Entity.ChartEntity;
import com.zeyuan.kyq.Entity.ChartFloatEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.DensityUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/2/16.
 *
 * @author wwei
 */
public class CancerSizeChartAdapter extends BaseRecyclerAdapter<CancerSizeChartAdapter.ChartViewHolder>{

    private Context context;
    private List<ChartEntity> list;
    private List<ChartFloatEntity> data;
    private int unit = 0;
    private int Max = 0;
    private float MaxFloat = 0;
    private int color = 0;
    private boolean isQuota = false;
    private int[] colors_1 = new int[]{R.drawable.bg_chart_1_not_now,
            R.drawable.bg_chart_2_not_now,
            R.drawable.bg_chart_3_not_now,
            R.drawable.bg_chart_4_not_now,
            R.drawable.bg_chart_5_not_now,
            R.drawable.bg_chart_6_not_now};
    private int[] colors_2 = new int[]{R.drawable.bg_chart_1_now,
            R.drawable.bg_chart_2_now,
            R.drawable.bg_chart_3_now,
            R.drawable.bg_chart_4_now,
            R.drawable.bg_chart_5_now,
            R.drawable.bg_chart_6_now};
    private int[] colors_3 = new int[]{R.color.color_record_chart_1_now,
            R.color.color_record_chart_2_now,
            R.color.color_record_chart_3_now,
            R.color.color_record_chart_4_now,
            R.color.color_record_chart_5_now,
            R.color.color_record_chart_6_now};

    public CancerSizeChartAdapter(Context context,List<ChartEntity> list){
        this.context = context;
        this.list = list;
        Max = getUnitMax();
        unit = DensityUtils.dpToPx(context,100)/100;
    }

    public CancerSizeChartAdapter(Context context,List<ChartEntity> list,int color){
        this.context = context;
        this.list = list;
        this.color = color%6;
        Max = getUnitMax();
        unit = DensityUtils.dpToPx(context,100)/100;
    }

    public CancerSizeChartAdapter(Context context,List<ChartFloatEntity> data,int color,boolean isQuota){
        this.context = context;
        this.data = data;
        this.color = color%6;
        this.isQuota = isQuota;
        MaxFloat = getUnitMaxFloat();
        unit = DensityUtils.dpToPx(context,100)/100;
    }

    private int getUnitMax(){
        int temp = 0;
        for (int i=0;i<list.size();i++){
            int wh = list.get(i).getWidth()*list.get(i).getHeight();
            if (wh>temp){
                temp = wh;
            }
        }
        return temp;
    }

    private float getUnitMaxFloat(){
        float temp = 0;
        for (int i=0;i<data.size();i++){
            float f = data.get(i).getSize();
            if (f>temp){
                temp = f;
            }
        }
        return temp;
    }

    @Override
    public ChartViewHolder getViewHolder(View view) {
        return new ChartViewHolder(view,false);
    }

    @Override
    public ChartViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_for_chart,parent,false);
        return new ChartViewHolder(v,true);
    }

    @Override
    public void onBindViewHolder(ChartViewHolder vh, int position, boolean isItem) {
        try {
            if (!isQuota){
                ChartEntity entity = list.get(position);
                vh.size.setText( entity.getWidth() + "*" + entity.getHeight());
                vh.date.setText( entity.getDate() );
                if (position==0||(entity.getYear()!=0&&entity.getYear()!=list.get(position-1).getYear())){
                    vh.year.setText(entity.getYear()!=0?entity.getYear()+"":"");
                }else {
                    vh.year.setText("");
                }
                vh.year.setTextColor(context.getResources().getColor(colors_3[color]));
                ViewGroup.LayoutParams params = vh.chart.getLayoutParams();
                int h = 100 * entity.getWidth() * entity.getHeight()/Max;
                if (h<5){
                    params.height = 5*unit;
                }else {
                    params.height = (h>100?100:h)*unit;
                }
                vh.chart.setLayoutParams(params);
                if (position==list.size()-1){
                    vh.chart.setBackgroundResource(colors_2[color]);
                }else {
                    vh.chart.setBackgroundResource(colors_1[color]);
                }
            }else {
                ChartFloatEntity entity = data.get(position);
                vh.size.setText( entity.getSize() + "");
                vh.date.setText( entity.getDate() );
                if (position==0||(entity.getYear()!=0&&entity.getYear()!=data.get(position-1).getYear())){
                    vh.year.setText(entity.getYear()!=0?entity.getYear()+"":"");
                }else {
                    vh.year.setText("");
                }
                vh.year.setTextColor(context.getResources().getColor(colors_3[color]));
                ViewGroup.LayoutParams params = vh.chart.getLayoutParams();
                int h = (int)(100 * entity.getSize()/MaxFloat);
                if (h<5){
                    params.height = 5*unit;
                }else {
                    params.height = (h>100?100:h)*unit;
                }
                vh.chart.setLayoutParams(params);
                if (position==data.size()-1){
                    vh.chart.setBackgroundResource(colors_2[color]);
                }else {
                    vh.chart.setBackgroundResource(colors_1[color]);
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"CancerSizeChartAdapter onBindViewHolder");
        }
    }

    @Override
    public int getAdapterItemCount() {
        if (isQuota){
            return data.size();
        }
        return list.size();
    }

    public class ChartViewHolder extends RecyclerView.ViewHolder{

        TextView date,year,size;
        View chart;

        public ChartViewHolder(View itemView , boolean isItem) {
            super(itemView);
            init(itemView,-1,isItem);
        }

        private void init(View v, int viewType, boolean isItem) {
            if (isItem){
                size = (TextView)v.findViewById(R.id.tv_cancer_size);
                date = (TextView)v.findViewById(R.id.tv_date);
                year = (TextView)v.findViewById(R.id.tv_year);
                chart = v.findViewById(R.id.v_long);
            }
        }

    }


}

package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.HomeSymptomEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.ExceptionUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/1/10.
 *
 *
 * @author wwei
 */
public class SymptomClassifyRecyclerAdapter extends RecyclerView.Adapter<SymptomClassifyRecyclerAdapter.ViewHolder>{

    private Context context;
    private List<HomeSymptomEntity> list;
    private int index = 0;
    private onTabClickListener callback;

    public SymptomClassifyRecyclerAdapter(Context context,List<HomeSymptomEntity> list,int index,onTabClickListener callback){
        this.context = context;
        this.list = list;
        this.index = index;
        this.callback = callback;
    }


    @Override
    public SymptomClassifyRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_tab_symptom_classify, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, final int position) {

        try {
            final HomeSymptomEntity entity = list.get(position);
            vh.tv.setText(TextUtils.isEmpty(entity.getName())?"":entity.getName());
            if (position == index){
                vh.tv.setSelected(true);
            }else {
                vh.tv.setSelected(false);
            }
            vh.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (index!=position){
                        update(position);
                        callback.onTabClick(entity,position);
                    }
                }
            });
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"Bind");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void update(int index){
        this.index = index;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv;
        View v;

        public ViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            tv = (TextView)itemView.findViewById(R.id.tv_tab);
        }

    }

    public interface onTabClickListener{
        void onTabClick(HomeSymptomEntity entity,int pos);
    }
}

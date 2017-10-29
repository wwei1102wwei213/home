package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.ServiceCenterItemEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.ExceptionUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 *
 * @author wwei
 */
public class ServiceCenterRecyclerAdapter extends RecyclerView.Adapter<ServiceCenterRecyclerAdapter.MyViewHolder>{

    private Context context;
    private List<ServiceCenterItemEntity> list;
    private int index = 0;
    private onItemClickListener callback;

    public ServiceCenterRecyclerAdapter(Context context,List<ServiceCenterItemEntity> list,onItemClickListener callback){
        this.context = context;
        this.list = list;
        this.callback = callback;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home_tab,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            final ServiceCenterItemEntity entity = list.get(position);
            String name = entity.getName();
            holder.tv.setText(TextUtils.isEmpty(name)?"未知栏目":name);
            if(position==index){
                holder.tv.setSelected(true);
            }else {
                holder.tv.setSelected(false);
            }
            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        index = position;
                        callback.onTabItemClick(entity);
                        notifyDataSetChanged();
                }
            });
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, context, "HomeTabRecyclerAdapter");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void update(List<ServiceCenterItemEntity> list){
        this.list = list;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        View v;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.tv_item_rv);
            v = itemView;
        }
    }

    public interface onItemClickListener{
        void onTabItemClick(ServiceCenterItemEntity entity);
    }
}

package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.MapDataUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/5.
 *
 *
 * @author wwei
 */
public class RecyclerCircleAdapter extends RecyclerView.Adapter<RecyclerCircleAdapter.MyViewHolder>{

    private Context context;
    private List<String> list;
    private Map<String,String> data;
    private OnItemClickListener listener;
    private int type = 0;
    private int index = 0;

    public RecyclerCircleAdapter(Context context,List<String> list,OnItemClickListener listener,int type){
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.type = type;
        if(type==0){
            data = (Map<String,String>) Factory.getData(Const.N_DataCircleValues);
        }else {
            data = MapDataUtils.getOtherCircleNames(type);
        }
    }

    @Override
    public RecyclerCircleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(View.inflate(context, R.layout.item_rv_circle,null));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerCircleAdapter.MyViewHolder holder, final int position) {
        try {
            String name = data.get(list.get(position));
            if(position==0){
                holder.tv.setText("全部");
            }else {
                if(!TextUtils.isEmpty(name)){
                    holder.tv.setText(name);
                }else {
                    holder.tv.setText("");
                }
            }
            if(position==index){
                holder.tv.setTextColor(Color.parseColor("#17cbd1"));
                holder.line.setVisibility(View.VISIBLE);
            }else {
                holder.tv.setTextColor(Color.parseColor("#4c4c4c"));
                holder.line.setVisibility(View.GONE);
            }
            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    index = position;
                    listener.OnRecyclerItemClick(holder.v, position,list.get(position));
                    notifyDataSetChanged();
                }
            });
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,context,"RecyclerCircleAdapter");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends ViewHolder
    {
        TextView tv;
        View line;
        View v;
        public MyViewHolder(View view)
        {
            super(view);
            tv = (TextView)view.findViewById(R.id.tv_item_rv);
            line = view.findViewById(R.id.line_item_rv);
            v = view;
        }
    }

    public interface OnItemClickListener{
        void OnRecyclerItemClick(View v,int position,String typeID);
    }
}

package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.widget.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/11/17.
 */
public class RecyclerCareAdapter extends RecyclerView.Adapter<RecyclerCareAdapter.MyViewHolder>{

    private Context context;
    private List<String> list;
    private OnItemClickListener listener;
    private int type = 0;


    public RecyclerCareAdapter(Context context,List<String> list,OnItemClickListener listener,int type){
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.type = type;
    }

    @Override
    public RecyclerCareAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(View.inflate(context, R.layout.item_rv_care, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerCareAdapter.MyViewHolder holder, final int position) {
        try {
            final String url = list.get(position);
            if (TextUtils.isEmpty(url)){
                holder.civ.setImageResource(R.mipmap.default_avatar);
            }else {
                Glide.with(context).load(url).error(R.mipmap.default_avatar).into(holder.civ);
            }
            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnRecyclerItemClick(holder.v, position,url);
                }
            });
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, context, "RecyclerCareAdapter");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void update(List<String> list){
        this.list = list;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView civ;
        View v;
        public MyViewHolder(View view)
        {
            super(view);
            civ = (CircleImageView)view.findViewById(R.id.civ);
            v = view;
        }
    }

    public interface OnItemClickListener{
        void OnRecyclerItemClick(View v,int position,String userID);
    }

}

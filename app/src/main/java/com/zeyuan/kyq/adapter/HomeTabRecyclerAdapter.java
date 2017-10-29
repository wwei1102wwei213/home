package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.ArticleTypeEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.ExceptionUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/12/29.
 *
 * @author wwei
 */
public class HomeTabRecyclerAdapter extends RecyclerView.Adapter<HomeTabRecyclerAdapter.MyViewHolder>{

    private Context context;
    private List<ArticleTypeEntity> list;

    private OnItemClickListener listener;
    private int type = 0;
    private int index = 0;

    public HomeTabRecyclerAdapter(Context context,List<ArticleTypeEntity> list,OnItemClickListener listener,int type){
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.type = type;
    }

    @Override
    public HomeTabRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(View.inflate(context, R.layout.item_home_tab, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(final HomeTabRecyclerAdapter.MyViewHolder holder, final int position) {

        try {
//            String name = data.get(list.get(position));
            final ArticleTypeEntity entity = list.get(position);
            String name = entity.getCatname();
            if(position==0){
                holder.tv.setText(name);
            }else {
                if(!TextUtils.isEmpty(name)){
                    holder.tv.setText(name);
                }else {
                    holder.tv.setText("");
                }
            }
            if(position==index&&position!=list.size()){//如果有占位控件，则减去1
                holder.tv.setSelected(true);
            }else {
                holder.tv.setSelected(false);
            }
            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position!=list.size()){//如果有占位控件，则减去1
                        index = position;
                        listener.OnRecyclerItemClick(holder.v, position,entity.getCatid()+"");
                        notifyDataSetChanged();
                    }
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

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv;
        View v;
        public MyViewHolder(View view)
        {
            super(view);
            tv = (TextView)view.findViewById(R.id.tv_item_rv);
            v = view;
        }


    }

    public interface OnItemClickListener{
        void OnRecyclerItemClick(View v,int position,String typeID);
    }

    public void update(List<ArticleTypeEntity> list){
        this.list = list;
        this.index = 0;
        notifyDataSetChanged();
    }

    public void update(int index){
        if (index<0) index = 0;
        this.index = index;
        notifyDataSetChanged();
    }

}

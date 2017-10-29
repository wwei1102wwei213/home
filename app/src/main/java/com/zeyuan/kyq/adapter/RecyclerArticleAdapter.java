package com.zeyuan.kyq.adapter;

/**
 * Created by Administrator on 2016/10/11.
 */

import android.content.Context;
import android.graphics.Color;
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
 * Created by Administrator on 2016/8/5.
 *
 *
 * @author wwei
 */
public class RecyclerArticleAdapter extends RecyclerView.Adapter<RecyclerArticleAdapter.MyViewHolder>{

    private Context context;
    private List<ArticleTypeEntity> list;

    private OnItemClickListener listener;
    private int type = 0;
    private int index = 0;

    public RecyclerArticleAdapter(Context context,List<ArticleTypeEntity> list,OnItemClickListener listener,int type){
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.type = type;
    }

    @Override
    public RecyclerArticleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(View.inflate(context, R.layout.item_rv_article, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerArticleAdapter.MyViewHolder holder, final int position) {

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
            if(position==index&&position!=list.size()-1){
                holder.tv.setTextColor(Color.parseColor("#17cbd1"));
                holder.line.setVisibility(View.VISIBLE);
            }else {
                holder.tv.setTextColor(Color.parseColor("#4c4c4c"));
                holder.line.setVisibility(View.GONE);
            }
            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position!=list.size()-1){
                        index = position;
                        listener.OnRecyclerItemClick(holder.v, position,entity.getCatid()+"");
                        notifyDataSetChanged();
                    }
                }
            });
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, context, "RecyclerCircleAdapter");
        }

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
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


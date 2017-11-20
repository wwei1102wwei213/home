package com.zeyuan.kyq.adapter.nww;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;

import java.util.List;

/**
 * Created by Administrator on 2017-11-20.
 */

public class PhotoSummaryRvAdapter extends BaseRecyclerAdapter<PhotoSummaryRvAdapter.ViewHolder> {

    private Context context;
    private List<String> list;
    private int index;
    private PhotoSelectedListener callback;

    public PhotoSummaryRvAdapter(Context context, List<String> list, int index, PhotoSelectedListener callback){
        this.list = list;
        this.index = index;
        this.context = context;
        this.callback = callback;
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view,false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_photo_summary,parent,false);
        return new ViewHolder(v, -1, true);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position, boolean isItem) {
        final String url = list.get(position);
        if (position==index){
            holder.v.setVisibility(View.VISIBLE);
        } else {
            holder.v.setVisibility(View.GONE);
        }
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position!=index){
                    index = position;
                    notifyDataSetChanged();
                    if (!TextUtils.isEmpty(url)){
                        callback.onItemSelected(url);
                    }
                }
            }
        });
        if (!TextUtils.isEmpty(url)){
            Glide.with(context).load(url).into(holder.iv);
        } else {
            holder.iv.setImageResource(R.mipmap.main_top_default_img);
        }
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView iv;
        View v;

        public ViewHolder(View itemView,boolean isItem) {
            super(itemView);
            init(itemView,-1,isItem);
        }

        public ViewHolder(View itemView,int viewType,boolean isItem){
            super(itemView);
            init(itemView, viewType, isItem);
        }

        private void init(View itemView,int viewType,boolean isItem){
            if (isItem){
                iv = (ImageView) itemView.findViewById(R.id.iv);
                v = itemView.findViewById(R.id.v_border);
            }
        }
    }

    public interface PhotoSelectedListener{
        void onItemSelected(String url);
    }
}

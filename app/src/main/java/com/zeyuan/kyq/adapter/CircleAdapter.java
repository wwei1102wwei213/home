package com.zeyuan.kyq.adapter;

/**
 * Created by Administrator on 2015/12/8.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.MapDataUtils;

import java.util.List;

public class CircleAdapter extends RecyclerView.Adapter<CircleAdapter.MyViewHolder> {
    private List<String> datas;
    private Context mContext;
    private OnItemClickListerner mOnItemClickListerner;

    public void setOnItemClickLitener(OnItemClickListerner mOnItemClickLitener) {
        this.mOnItemClickListerner = mOnItemClickLitener;
    }

    public CircleAdapter(List<String> list, Context context) {
        datas = list;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.follow_circle_item, parent,
                false));
        return holder;
    }

    public interface OnItemClickListerner {
        void onItemClick(View v, int position);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        String circleName = datas.get(position);
        holder.tv.setText(MapDataUtils.getCircleValues(circleName));
        holder.img.setBackgroundResource(R.mipmap.sceon_city);

        if (mOnItemClickListerner != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListerner.onItemClick(holder.itemView, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView img;
        View itemView;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.circle_name);
            img = (ImageView) view.findViewById(R.id.circle_img);
            itemView = view;
        }
    }
}

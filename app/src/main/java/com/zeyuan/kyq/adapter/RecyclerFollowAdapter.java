package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UiUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/8/5.
 *
 *
 * @author wwei
 */
public class RecyclerFollowAdapter extends RecyclerView.Adapter<RecyclerFollowAdapter.MyViewHolder>{

    private Context context;
    private List<String> list;
    private OnItemClickListener listener;

    public RecyclerFollowAdapter(Context context,List<String> list,OnItemClickListener listener){
        this.context = context;
        this.list = list;
        this.listener = listener;
        list.add("        ");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public RecyclerFollowAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(View.inflate(context, R.layout.item_rv_follow,null));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            final String id = list.get(position);
            if(!TextUtils.isEmpty(id)){
                if(position!=list.size()-1){
                    holder.tv.setText(MapDataUtils.getCircleValues(id));
                    holder.v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.OnItemFollowClick(id);
                        }
                    });
                }else {
                    holder.tv.setText(id);
                }
            }else {
                holder.tv.setText("");
            }
            if(position==list.size()-1){
                holder.iv.setImageBitmap(null);
            }else {
                holder.iv.setImageResource(UiUtils.getCancerImage(id));
            }

        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"RecyclerFollowAdapter");
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        View v;
        ImageView iv;
        public MyViewHolder(View view){
            super(view);
            tv = (TextView)view.findViewById(R.id.tv_rv_follow);
            v = view;
            iv = (ImageView)view.findViewById(R.id.iv_circle_follow);
        }
    }

    public interface OnItemClickListener{
        void OnItemFollowClick(String id);
    }

}

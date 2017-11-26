package com.zeyuan.kyq.adapter.nww;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.CommentProjectItem;
import com.zeyuan.kyq.biz.forcallback.AdapterCallback;
import com.zeyuan.kyq.utils.DecryptUtils;
import com.zeyuan.kyq.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-11-25.
 */

public class CommentProjectRvAdapter extends BaseRecyclerAdapter<CommentProjectRvAdapter.ViewHolder> {

    private Context context;
    private List<CommentProjectItem> list;
    private AdapterCallback callback;
    private int type;

    public CommentProjectRvAdapter(Context context, List<CommentProjectItem> list, AdapterCallback callback, int type){
        this.list = list;
        this.context = context;
        this.callback = callback;
        this.type = type;
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view,false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v;
        if (type==2){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_comment_project_1,parent,false);
        } else if (type==3){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_comment_project_2,parent,false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_comment_project,parent,false);
        }
        return new ViewHolder(v, -1, true);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position, boolean isItem) {
        final CommentProjectItem item = list.get(position);

        String name = item.getPname();
        holder.name.setText(TextUtils.isEmpty(name)?"":name);
        holder.tv_num_1.setText(item.getHComNum()+"");
        holder.tv_num_2.setText(item.getMComNum()+"");
        holder.tv_num_3.setText(item.getLComNum()+"");
        holder.tv_sub.setText(DecryptUtils.URLAnddecodeBase64(item.getPsubject()));
        String url = item.getPicUrl();
        if (!TextUtils.isEmpty(url)){
            Glide.with(context).load(url).into(holder.iv);
        } else {
            holder.iv.setImageResource(R.drawable.default_img);
        }
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.forAdapterCallback(position, 0, item.getId()+"", false, item);
            }
        });
        if (type==2){
            holder.tv_host_name.setText(TextUtils.isEmpty(item.getJob())?"未录入医院":item.getJob());
            holder.tv_host_tag.setText("");
            holder.tv_dot_type.setText("");
            holder.tv_rate.setText(item.getHComRate()+"%");
        } else if (type==3){
            if (TextUtils.isEmpty(item.getHospitalLevelName())){
                holder.host_type.setVisibility(View.GONE);
            } else {
                holder.host_type.setVisibility(View.VISIBLE);
                holder.host_type.setText(item.getHospitalLevelName());
            }
        } else {
            if (TextUtils.isEmpty(item.getCureTypeName())){
                holder.cure_name.setVisibility(View.GONE);
            } else {
                holder.cure_name.setVisibility(View.VISIBLE);
                holder.cure_name.setText(item.getCureTypeName());
            }
        }
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    public void update(List<CommentProjectItem> list){
        if (list==null) list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView iv;
        View v;
        TextView name,cure_name,tv_num_1,tv_num_2,tv_num_3,tv_sub,host_type,tv_rate,tv_host_name,tv_host_tag,tv_dot_type;

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

                v = itemView;
                name = (TextView) itemView.findViewById(R.id.name);
                tv_num_1 = (TextView) itemView.findViewById(R.id.tv_num_1);
                tv_num_2 = (TextView) itemView.findViewById(R.id.tv_num_2);
                tv_num_3 = (TextView) itemView.findViewById(R.id.tv_num_3);
                tv_sub = (TextView) itemView.findViewById(R.id.tv_sub);
                if (type==2){
                    iv = (CircleImageView) itemView.findViewById(R.id.iv);
                    tv_rate = (TextView) itemView.findViewById(R.id.tv_rate);
                    tv_host_name = (TextView) itemView.findViewById(R.id.tv_host_name);
                    tv_host_tag = (TextView) itemView.findViewById(R.id.tv_host_tag);
                    tv_dot_type = (TextView) itemView.findViewById(R.id.tv_dot_type);
                } else if (type==3){
                    iv = (ImageView) itemView.findViewById(R.id.iv);
                    host_type = (TextView) itemView.findViewById(R.id.host_type);
                } else {
                    iv = (ImageView) itemView.findViewById(R.id.iv);
                    cure_name = (TextView) itemView.findViewById(R.id.cure_name);
                }
            }
        }
    }

}

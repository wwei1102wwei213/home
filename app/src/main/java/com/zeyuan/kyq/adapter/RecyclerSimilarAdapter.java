package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.SimilarCaseNewEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.widget.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 *
 *
 * @author wwei
 */
public class RecyclerSimilarAdapter extends RecyclerView.Adapter<RecyclerSimilarAdapter.MyViewHolder>{

    private Context context;
    private List<SimilarCaseNewEntity> list;

    public RecyclerSimilarAdapter(Context context,List<SimilarCaseNewEntity> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(View.inflate(context, R.layout.simalar_case_item,null));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        try {
            SimilarCaseNewEntity entity = list.get(position);

            /*Glide.with(context).load(entity.getUrl())
                    .signature(new IntegerVersionSignature(1))
                    .error(R.mipmap.default_avatar)
                    .into(holder.avatar);*/
            holder.avatar.setImageResource(entity.getHead());
            holder.tv_cancer.setText(entity.getCancer());
            holder.tv_name.setText(entity.getName());
            if (ZYApplication.typeFace!=null) holder.tv_day.setTypeface(ZYApplication.typeFace);
            holder.tv_day.setText(entity.getDay());
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"bind error");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView avatar;
        TextView tv_name;
        TextView tv_day;
        TextView tv_cancer;
        View v;
        public MyViewHolder(View view){
            super(view);
            avatar = (CircleImageView) view.findViewById(R.id.civ_avatar);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_day = (TextView) view.findViewById(R.id.tv_day);
            tv_cancer = (TextView) view.findViewById(R.id.tv_cancer);
            v = view;
        }
    }
}

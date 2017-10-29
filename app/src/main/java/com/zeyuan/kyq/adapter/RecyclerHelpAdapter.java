package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.HelpItemEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.ShowDiscuzActivity;
import com.zeyuan.kyq.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 *
 *
 * @author wwei
 */
public class RecyclerHelpAdapter extends RecyclerView.Adapter<RecyclerHelpAdapter.MyViewHolder>{

    private Context context;
    private List<HelpItemEntity> list;
    private OnHelpItemClickListener callback;

    public RecyclerHelpAdapter(Context context,List<HelpItemEntity> list,OnHelpItemClickListener callback){
        this.context = context;
        this.list = list;
        this.callback = callback;
    }

    public RecyclerHelpAdapter(Context context,List<HelpItemEntity> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(View.inflate(context, R.layout.card_item_help_main,null));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        try {
            final HelpItemEntity entity = list.get(position);
            String url = entity.getHeadUrl();
            if (TextUtils.isEmpty(url)){
                holder.avatar.setImageResource(R.mipmap.default_avatar);
            }else {
                try {
                    Glide.with(context).load(url)
                            .signature(new IntegerVersionSignature(1))
                            .error(R.mipmap.default_avatar)
                            .into(holder.avatar);
                }catch (Exception e){

                }
            }
            holder.tv_title.setText(TextUtils.isEmpty(entity.getTitle())?"":entity.getTitle());
            holder.tv_name.setText(TextUtils.isEmpty(entity.getInfoName())?"":entity.getInfoName());
//            holder.tv_time.setText(TextUtils.isEmpty(entity.getDateline())?"":(entity.getDateline()+"前"));
            if (ZYApplication.typeFace!=null) {
                holder.tv_cash.setTypeface(ZYApplication.typeFace);
            }
//            holder.tv_type.setText(TextUtils.isEmpty(entity.getTypeName())?"":entity.getTypeName());
            holder.tv_day.setText(TextUtils.isEmpty(entity.getEndTime())?"未知":("剩余"+entity.getEndTime()));
            holder.tv_watch.setText(entity.getView()+"");
            holder.tv_follow.setText(entity.getFollowNum()+"人接力");
            String cash = entity.getPrice();
            if (TextUtils.isEmpty(cash)){
                holder.tv_cash.setVisibility(View.GONE);
            }else {
                try {
                    holder.tv_cash.setText(Integer.valueOf(cash) +"元");
                }catch (Exception e){
                    holder.tv_cash.setText(cash+"元");
                }
                holder.tv_cash.setVisibility(View.VISIBLE);
            }
            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (entity.getUid()!=0){
                        String kaq = getParamKaqID();
                        String helpUrl = "http://help.kaqcn.com/help/question_index?qid=" + entity.getUid() +"&" +kaq +"&lt=2";
                        context.startActivity(new Intent(context, ShowDiscuzActivity.class)
                                .putExtra(Const.SHOW_HTML_MAIN_TOP, helpUrl));
                    }
                }
            });


        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"help main bind error");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView avatar;
        TextView tv_name;
        TextView tv_cash;
        TextView tv_title;
//        TextView tv_time;
        TextView tv_watch;
//        TextView tv_type;
        TextView tv_day;
        TextView tv_follow;
        View v;
        public MyViewHolder(View view){
            super(view);
            avatar = (CircleImageView) view.findViewById(R.id.civ_url_help_main);
            tv_name = (TextView) view.findViewById(R.id.tv_name_help_main);
            tv_cash = (TextView) view.findViewById(R.id.tv_cash_help_main);
//            tv_time = (TextView) view.findViewById(R.id.tv_time_help_main);
            tv_title = (TextView) view.findViewById(R.id.tv_title_help_main);
            tv_watch = (TextView) view.findViewById(R.id.tv_watch_num_help_main);
//            tv_type = (TextView) view.findViewById(R.id.tv_help_type);
            tv_day = (TextView) view.findViewById(R.id.tv_end_day);
            tv_follow = (TextView) view.findViewById(R.id.tv_follow);
            v = view;
        }
    }

    public interface OnHelpItemClickListener{
        void onHelpItemClick(int helpID);
    }

    public void update(List<HelpItemEntity> list){
        if (list==null) list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    private String getRandomMath(){
        String temp = (int)(Math.random()*89999 + 10000)+"";
        return temp;
    }

    protected String getParamKaqID(){
        return "kaq="+getRandomMath()+ UserinfoData.getInfoID(context);
    }
}

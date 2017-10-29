package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.HelpItemEntity;
import com.zeyuan.kyq.Entity.InformationEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/27.
 *
 *
 * @author wwei
 */
public class HomeRecyclerAdapter extends BaseRecyclerAdapter<HomeRecyclerAdapter.HomeRecyclerViewHolder>{

    private Context context;
    private static final int TYPE_1 = 0;
    private static final int TYPE_2 = 1;
    private static final int TYPE_3 = 2;
    private int zero = 0;
    private int type = 0;
    private List<InformationEntity> list;
    private List<HelpItemEntity> help;
    private RecyclerHelpAdapter rv_adapter;

    public void setHelp(List<HelpItemEntity> help) {
        if (help==null||help.size()==0){
            help = new ArrayList<>();
        }
        this.help = help;
    }

    public HomeRecyclerAdapter(Context context,List<InformationEntity> list){
        this.context = context;
        if (list==null) list = new ArrayList<>();
        this.list = list;
        help = new ArrayList<>();
        this.rv_adapter = new RecyclerHelpAdapter(context,help);
    }

    public void update(List<InformationEntity> list,int zero,int type){
        this.zero = zero;
        this.type = type;
        if (list==null) list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public HomeRecyclerViewHolder getViewHolder(View view) {
        return new HomeRecyclerViewHolder(view,false);
    }

    @Override
    public HomeRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {

        View v = null;
        switch (viewType){
            case TYPE_2:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_article_main_2,parent,false);
                break;
            case TYPE_3:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_rv,parent,false);
                break;
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_article_main,parent,false);
                break;
        }
        return new HomeRecyclerViewHolder(v,viewType,true);
    }

    @Override
    public void onBindViewHolder(HomeRecyclerViewHolder vh, int position, boolean isItem) {

        try {

            int viewType = getAdapterItemViewType(position);

            if (viewType==TYPE_3){
                rv_adapter.update(help);
            }else {
                InformationEntity entity;
                if (zero==-1&&list.size()>1&&position>2){
                    entity = list.get(position-1);
                }else {
                    entity = list.get(position);
                }
                String url = entity.getThumbURL();
                String like = entity.getLikeNum()+"";
                String title = entity.getTitle();
                int watch = entity.getViewnum();
                switch (viewType){
                    case TYPE_2:
                        vh.watch.setText(watch+"");
                        if (TextUtils.isEmpty(url)){
                            vh.iv.setImageResource(R.mipmap.loading_fail);
                        }else {
                            try {
                                Glide.with(context).load(url).error(R.mipmap.loading_fail).into(vh.iv);
                            }catch (Exception e){

                            }
                        }
                        vh.title.setText(TextUtils.isEmpty(title) ? "" : title);
                        vh.like.setText(like);
                        String summary = entity.getSummary();
                        vh.from.setText(TextUtils.isEmpty(summary)?"暂无摘要":summary.equals("admin")?"抗癌圈":summary);
                        break;
                    default:
                        vh.watch.setText(watch+"");
                        if (TextUtils.isEmpty(url)){
                            vh.iv.setImageResource(R.mipmap.loading_fail);
                        }else {
                            try {
                                Glide.with(context).load(url).error(R.mipmap.loading_fail).into(vh.iv);
                            }catch (Exception e){

                            }
                        }
                        vh.like.setText(like);
                        vh.title.setText(TextUtils.isEmpty(title) ? "" : title);
                        String from = entity.getAuthor();
                        vh.from.setText(TextUtils.isEmpty(from) ? "未知来源" : from.equals("admin") ? "抗癌圈" : from);
                        break;
                }
            }

        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"onBindViewHolder");
        }
    }

    @Override
    public int getAdapterItemCount() {
        if (zero==-1||list.size()>1) return list.size()+1;
        return list.size();
    }

    @Override
    public int getAdapterItemViewType(int position) {
        if (zero==-1&&position==2) return TYPE_3;
        if (type==1) return TYPE_2;
        return TYPE_1;
    }

    public class HomeRecyclerViewHolder extends RecyclerView.ViewHolder{

        ImageView iv;
        TextView title,from,watch,like;
        RecyclerView rv;

        public HomeRecyclerViewHolder(View itemView,boolean isItem){
            super(itemView);
            init(itemView,-1,isItem);
        }

        public HomeRecyclerViewHolder(View itemView,int viewType,boolean isItem){
            super(itemView);
            init(itemView, viewType, isItem);
        }

        private void init(View convertView,int viewType,boolean isItem){
            if (isItem){
                switch (viewType){
                    case TYPE_2:
                        iv = (ImageView)convertView.findViewById(R.id.iv_img);
                        title = (TextView)convertView.findViewById(R.id.tv_title);
                        from = (TextView)convertView.findViewById(R.id.tv_summary);
                        like = (TextView)convertView.findViewById(R.id.tv_like);
                        watch = (TextView)convertView.findViewById(R.id.tv_watch);
                        break;
                    case TYPE_3:
                        rv = (RecyclerView)convertView.findViewById(R.id.rv_home);
                        LinearLayoutManager manager = new LinearLayoutManager(context);
                        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        rv.setLayoutManager(manager);
                        rv.setAdapter(rv_adapter);
                        break;
                    default:
                        iv = (ImageView)convertView.findViewById(R.id.iv_article_item);
                        title = (TextView)convertView.findViewById(R.id.tv_title_article_item);
                        from = (TextView)convertView.findViewById(R.id.tv_from_article_item);
                        watch = (TextView)convertView.findViewById(R.id.tv_watch_article_item);
                        like = (TextView)convertView.findViewById(R.id.tv_like_article_item);
                        break;
                }
            }
        }
    }
}

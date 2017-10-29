package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.InformationEntity;
import com.zeyuan.kyq.Entity.SameEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.OtherUtils;
import com.zeyuan.kyq.view.ArticleDetailActivity;
import com.zeyuan.kyq.widget.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 *
 *
 * @author wwei
 */
public class MatchArticleRecyclerAdapter extends BaseRecyclerAdapter<MatchArticleRecyclerAdapter.MatchRecyclerViewHolder> {

    private Context context;
    private static final int TYPE_1 = 0;
    private static final int TYPE_2 = 1;
    private static final int TYPE_3 = 2;

    private int type = 0;
    private List<InformationEntity> list;
    private List<SameEntity> tag_list;
    private MatchTagGvAdapter gv_adapter;
    private boolean flag = false;
    private onGvItemClickListener callback;
    private int select = -1;


    public MatchArticleRecyclerAdapter(Context context,List<InformationEntity> list,onGvItemClickListener callback){
        this.context = context;
        if (list==null) list = new ArrayList<>();
        this.list = list;
        this.tag_list = new ArrayList<>();
        this.callback = callback;
        this.gv_adapter = new MatchTagGvAdapter(this.context,tag_list);
    }

    private void update(){
        notifyDataSetChanged();
    }

    public void update(List<InformationEntity> list,int type){
        this.type = type;
        if (list==null) list = new ArrayList<>();
        this.list = list;
        this.tag_list = new ArrayList<>();
        this.select = -1;
        notifyDataSetChanged();
    }

    public void update(List<InformationEntity> list,List<SameEntity> tag_list,int type){
        this.type = type;
        if (list==null) list = new ArrayList<>();
        this.list = list;
        this.tag_list = tag_list;
        this.select = -1;
        notifyDataSetChanged();
    }

    public void update(List<InformationEntity> list,int type,int select){
        this.type = type;
        if (list==null) list = new ArrayList<>();
        this.list = list;
        this.select = select;
        notifyDataSetChanged();
    }


    @Override
    public MatchRecyclerViewHolder getViewHolder(View view) {
        return new MatchRecyclerViewHolder(view,false);
    }

    @Override
    public MatchRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {

        View v = null;
        switch (viewType){
            case TYPE_3:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match_tag_for_rv,parent,false);
                break;
            case TYPE_2:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_article_main_2,parent,false);
                break;
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_article_list,parent,false);
                break;
        }
        return new MatchRecyclerViewHolder(v,viewType,true);
    }


    @Override
    public void onBindViewHolder(MatchRecyclerViewHolder vh, int position, boolean isItem) {

        try {
            int viewType = getAdapterItemViewType(position);

            String url;
            int like;
            String title;
            int watch;
            final int aid;
            switch (viewType){
                case TYPE_3:
                    if (tag_list==null||tag_list.size()==0){
                        vh.gv.setVisibility(View.GONE);
                        vh.more.setVisibility(View.GONE);
                    }else {
                        vh.gv.setVisibility(View.VISIBLE);
                        vh.gv.setAdapter(gv_adapter);
                        vh.gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                                if (select!=pos){
                                    if (!TextUtils.isEmpty(tag_list.get(pos).getId())){
//                                        LogCustom.i("ZYS","select:"+select+";pos:"+pos);
                                        callback.onGvItemClick(tag_list.get(pos).getId(),pos);
                                    }
                                }else {
                                    callback.onGvItemClick(null,-1);
                                }
                            }
                        });
                        if (tag_list.size()<9){
                            vh.more.setVisibility(View.GONE);
                            gv_adapter.update(tag_list,select);
                        }else {
                            vh.more.setVisibility(View.VISIBLE);
                            if (!flag){
                                vh.more.setText("展开更多兴趣");
                                List<SameEntity> temp = new ArrayList<>();
                                for (int i=0;i<8;i++){
                                    temp.add(tag_list.get(i));
                                }
                                gv_adapter.update(temp,select);
                            }else {
                                vh.more.setText("收起标签");
                                gv_adapter.update(tag_list,select);
                            }
                            vh.more.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (flag){
                                        flag = false;
                                    }else {
                                        flag = true;
                                    }
                                    notifyDataSetChanged();
                                }
                            });

                        }

                    }
                    break;
                case TYPE_2:
                    InformationEntity entity;
                    if (list.size()<3){
                        entity = list.get(position);
                    }else {
                        if (position<3){
                            entity = list.get(position);
                        }else {
                            entity = list.get(position-1);
                        }
                    }
                    url = entity.getThumbURL();
                    like = entity.getLikeNum();
                    title = entity.getTitle();
                    watch = entity.getViewnum();
                    aid = entity.getAid();
                    vh.watch.setText(watch+"");
                    vh.like.setText(like+"");
                    String summary = entity.getSummary();
                    vh.from.setText(TextUtils.isEmpty(summary)?"暂无摘要":summary.equals("admin")?"抗癌圈":summary);
                    if (TextUtils.isEmpty(url)){
                        vh.iv.setImageResource(R.mipmap.loading_fail);
                    }else {
                        try {
                            Glide.with(context).load(url).error(R.mipmap.loading_fail).into(vh.iv);
                        }catch (Exception e){

                        }
                    }
                    vh.title.setText(TextUtils.isEmpty(title) ? "" : title);
                    vh.v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!OtherUtils.isEmpty("" + aid)) {
                                context.startActivity(new Intent(context, ArticleDetailActivity.class).
                                        putExtra(Const.INTENT_ARTICLE_ID, "" + aid));
                            }
                        }
                    });
                    break;
                default:
                    InformationEntity entity1;
                    if (list.size()<3){
                        entity1 = list.get(position);
                    }else {
                        if (position<3){
                            entity1 = list.get(position);
                        }else {
                            entity1 = list.get(position-1);
                        }
                    }
                    url = entity1.getThumbURL();
                    like = entity1.getLikeNum();
                    title = entity1.getTitle();
                    watch = entity1.getViewnum();
                    aid = entity1.getAid();
                    if (watch>99999){
                        vh.watch.setText("99999+浏览");
                    }else {
                        vh.watch.setText(watch+"浏览");
                    }
                    if (like>99999){
                        vh.like.setText("99999+点赞");
                    }else {
                        vh.like.setText(like+"点赞");
                    }
                    String from = entity1.getAuthor();
                    vh.from.setText(TextUtils.isEmpty(from) ? "未知来源" : from.equals("admin") ? "抗癌圈" : from);
                    if (TextUtils.isEmpty(url)){
                        vh.iv.setImageResource(R.mipmap.loading_fail);
                    }else {
                        try {
                            Glide.with(context).load(url).error(R.mipmap.loading_fail).into(vh.iv);
                        }catch (Exception e){

                        }
                    }
                    vh.title.setText(TextUtils.isEmpty(title) ? "" : title);
                    vh.v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!OtherUtils.isEmpty("" + aid)) {
                                context.startActivity(new Intent(context, ArticleDetailActivity.class).
                                        putExtra(Const.INTENT_ARTICLE_ID, "" + aid));
                            }
                        }
                    });
                    break;
            }


        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "onBindViewHolder");
        }
    }

    @Override
    public int getAdapterItemCount() {
        return list.size()+1;
    }

    @Override
    public int getAdapterItemViewType(int position) {
        if (type==1) return TYPE_2;
        if (list.size()<3){
            if (position==list.size()) return TYPE_3;
        }else {
            if (position==3) return TYPE_3;
        }
        return TYPE_1;
    }

    public class MatchRecyclerViewHolder extends RecyclerView.ViewHolder{

        ImageView iv;
        TextView title,from,watch,like,more;
        View v;
        MyGridView gv;

        public MatchRecyclerViewHolder(View itemView,boolean isItem){
            super(itemView);
            init(itemView,-1,isItem);
        }

        public MatchRecyclerViewHolder(View itemView,int viewType,boolean isItem){
            super(itemView);
            init(itemView, viewType, isItem);
        }

        private void init(View convertView,int viewType,boolean isItem){
            if (isItem){
                switch (viewType){
                    case TYPE_3:
                        gv = (MyGridView)convertView.findViewById(R.id.gv);
                        more = (TextView)convertView.findViewById(R.id.tv_look_more);
                        break;
                    case TYPE_2:
                        v = convertView;
                        iv = (ImageView)convertView.findViewById(R.id.iv_img);
                        title = (TextView)convertView.findViewById(R.id.tv_title);
                        from = (TextView)convertView.findViewById(R.id.tv_summary);
                        like = (TextView)convertView.findViewById(R.id.tv_like);
                        watch = (TextView)convertView.findViewById(R.id.tv_watch);
                        break;
                    default:
                        v = convertView;
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

    public interface onGvItemClickListener{
        void onGvItemClick(String id,int pos);
    }

}

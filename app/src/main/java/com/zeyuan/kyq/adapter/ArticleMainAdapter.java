package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.InformationEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.ExceptionUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 *
 *
 * @author wwei
 */
public class ArticleMainAdapter extends BaseAdapter{

    private static final int TYPE_1 = 0;
    private static final int TYPE_2 = 1;

    private Context context;
    private List<InformationEntity> list;
    private int flag;
    private boolean refresh = false;

    public ArticleMainAdapter(Context context,List<InformationEntity> list,int flag){
        this.context = context;
        this.list = list;
        if (flag!=0&&flag!=1){
            flag = 0;
        }
        this.flag = flag;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public InformationEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (flag==0){
            return TYPE_1;
        }else if (flag==1){
            return TYPE_2;
        }
        return TYPE_1;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try {
        ViewHolder vh = null;
        ViewHolder2 vh2 = null;
        int type = getItemViewType(position);
        if (convertView==null){
            switch (type){
                case TYPE_1:
                    vh = new ViewHolder();
                    convertView = View.inflate(context, R.layout.item_list_article_main,null);
                    vh.iv = (ImageView)convertView.findViewById(R.id.iv_article_item);
                    vh.title = (TextView)convertView.findViewById(R.id.tv_title_article_item);
                    vh.from = (TextView)convertView.findViewById(R.id.tv_from_article_item);
                    vh.watch = (TextView)convertView.findViewById(R.id.tv_watch_article_item);
                    vh.like = (TextView)convertView.findViewById(R.id.tv_like_article_item);
                    convertView.setTag(vh);
                    break;
                case TYPE_2:
                    vh2 = new ViewHolder2();
                    convertView = View.inflate(context, R.layout.item_list_article_main_2,null);
                    vh2.iv = (ImageView)convertView.findViewById(R.id.iv_img);
                    vh2.title = (TextView)convertView.findViewById(R.id.tv_title);
                    vh2.from = (TextView)convertView.findViewById(R.id.tv_summary);
                    vh2.like = (TextView)convertView.findViewById(R.id.tv_like);
                    vh2.watch = (TextView)convertView.findViewById(R.id.tv_watch);
                    convertView.setTag(vh2);
                    break;
            }
        }else {
            switch (type){
                case TYPE_1:
                    vh = (ViewHolder)convertView.getTag();
                    break;
                case TYPE_2:
                    vh2 = (ViewHolder2)convertView.getTag();
                    break;
            }
        }
        InformationEntity entity = list.get(position);
        String url = entity.getThumbURL();
        String like = entity.getLikeNum()+"";
        String title = entity.getTitle();
        int watch = entity.getViewnum();
        switch (type){
            case TYPE_1:
                vh.watch.setText(watch+"");
                if (TextUtils.isEmpty(url)){
                    vh.iv.setImageResource(R.mipmap.loading_fail);
                }else {
                    Glide.with(context).load(url).error(R.mipmap.loading_fail).into(vh.iv);
                }
                vh.like.setText(like);
                vh.title.setText(TextUtils.isEmpty(title) ? "" : title);
                String from = entity.getAuthor();
                vh.from.setText(TextUtils.isEmpty(from) ? "未知来源" : from.equals("admin") ? "抗癌圈" : from);
                break;
            case TYPE_2:
                vh2.watch.setText(watch+"");
                if (TextUtils.isEmpty(url)){
                    vh2.iv.setImageResource(R.mipmap.loading_fail);
                }else {
                    Glide.with(context).load(url).error(R.mipmap.loading_fail).into(vh2.iv);
                }
                vh2.title.setText(TextUtils.isEmpty(title) ? "" : title);
                vh2.like.setText(like);
                String summary = entity.getSummary();
                vh2.from.setText(TextUtils.isEmpty(summary)?"暂无摘要":summary.equals("admin")?"抗癌圈":summary);

                break;
        }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"getView");
        }

        return convertView;
    }

    public void update(List<InformationEntity> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void update(List<InformationEntity> list,int flag){
        this.flag = flag;
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder{
        ImageView iv;
        TextView title;
        TextView from;
        TextView watch;
        TextView like;
        int tag = -1;
    }



    class ViewHolder2{
        ImageView iv;
        TextView title;
        TextView from;
        TextView watch;
        TextView like;
        int tag = -1;
    }
}

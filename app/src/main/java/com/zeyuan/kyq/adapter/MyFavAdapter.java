package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.FavEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.widget.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/11/18.
 *
 *
 * @author wwei
 */
public class MyFavAdapter extends BaseAdapter{

    private static final int TYPE_1 = 0;
    private static final int TYPE_2 = 1;
    private Context context;
    private List<FavEntity> list;
    private onImgClickCallback callback;

    public MyFavAdapter(Context context,List<FavEntity> list,onImgClickCallback callback){
        this.context = context;
        this.list = list;
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public FavEntity getItem(int position) {
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
        try {
            int type = list.get(position).getType();
            if (type==1){
                return TYPE_1;
            }else if (type==2){
                return TYPE_2;
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"getType");
        }

        return TYPE_1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder1 vh1 = null;
        ViewHolder2 vh2 = null;

        int type = getItemViewType(position);

        if (convertView==null){
            switch (type){
                case TYPE_1:
                    vh1 = new ViewHolder1();
                    convertView = View.inflate(context, R.layout.item_my_fav,null);
                    vh1.img = (ImageView)convertView.findViewById(R.id.img);
                    vh1.title = (TextView)convertView.findViewById(R.id.tv_title);
                    vh1.author = (TextView)convertView.findViewById(R.id.tv_author);
                    vh1.view_num = (TextView)convertView.findViewById(R.id.tv_view_num);
                    vh1.time = (TextView)convertView.findViewById(R.id.tv_time);
                    vh1.fav_time = (TextView)convertView.findViewById(R.id.tv_fav_time);
                    convertView.setTag(vh1);
                    break;
                case TYPE_2:
                    vh2 = new ViewHolder2();
                    convertView = View.inflate(context,R.layout.item_forum_base_fav,null);
                    vh2.civ = (CircleImageView)convertView.findViewById(R.id.civ);
                    vh2.name = (TextView)convertView.findViewById(R.id.name);
                    vh2.time = (TextView)convertView.findViewById(R.id.time);
                    vh2.title = (TextView)convertView.findViewById(R.id.title);
                    vh2.content = (TextView)convertView.findViewById(R.id.content);
                    vh2.reply = (TextView)convertView.findViewById(R.id.reply);
                    vh2.like = (TextView)convertView.findViewById(R.id.like);
                    vh2.circle = (TextView)convertView.findViewById(R.id.circle);
                    vh2.cream = (ImageView)convertView.findViewById(R.id.iv_cream);
                    vh2.top = (ImageView)convertView.findViewById(R.id.iv_top);
                    vh2.layout = (LinearLayout)convertView.findViewById(R.id.layout);
                    vh2.fav_time = (TextView)convertView.findViewById(R.id.tv_fav_time);
                    vh2.v_circle = convertView.findViewById(R.id.v_circle);
                    vh2.v_fav = convertView.findViewById(R.id.v_fav);
                    convertView.setTag(vh2);
                    break;
            }
        }else {
            switch (type){
                case TYPE_1:
                    vh1 = (ViewHolder1)convertView.getTag();
                    break;
                case TYPE_2:
                    vh2 = (ViewHolder2)convertView.getTag();
                    break;
            }
        }
        try {
            final FavEntity entity = list.get(position);
            String title = TextUtils.isEmpty(entity.getTitle())?"":entity.getTitle();
            String author = TextUtils.isEmpty(entity.getAuthor())?"":entity.getAuthor();
            String fav_time = "收藏于" + DataUtils.showFormatTime(entity.getDateLine());
            String time = DataUtils.showFormatTime(entity.getTime());
            String pic = entity.getPic();

            switch (type){
                case TYPE_1:

                    vh1.title.setText(title);
                    vh1.author.setText(author);
                    vh1.time.setText(time);
                    vh1.fav_time.setText(fav_time);
                    vh1.view_num.setText(entity.getViewNum()+"浏览");
                    if (TextUtils.isEmpty(pic)){
                        vh1.img.setImageResource(R.mipmap.loading_fail);
                    }else {
                        Glide.with(context).load(pic).signature(new IntegerVersionSignature(1))
                                .error(R.mipmap.loading_fail).into(vh1.img);
                    }
                    vh1.img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callback.ToInfoCenterCallback(entity.getAuthorID()+"");
                        }
                    });
                    break;
                case TYPE_2:
                    vh2.name.setText(author);
                    vh2.v_circle.setVisibility(View.GONE);
                    vh2.v_fav.setVisibility(View.VISIBLE);
                    vh2.fav_time.setText(fav_time);
                    vh2.time.setText(time);
                    vh2.reply.setText(entity.getViewNum() + "");
                    vh2.like.setText(entity.getLikeNum() + "");
                    vh2.content.setText(entity.getContent());
                    vh2.title.setText(title);
                    //置顶和精华
                    int t = entity.getPostType();
                    if (t!=0&&(t&2)==2){
                        vh2.cream.setVisibility(View.VISIBLE);
                    }else {
                        vh2.cream.setVisibility(View.GONE);
                    }
                    if (t!=0&&(t&1)==1){
                        vh2.top.setVisibility(View.VISIBLE);
                    }else {
                        vh2.top.setVisibility(View.GONE);
                    }
                    if (TextUtils.isEmpty(pic)){
                        vh2.civ.setImageResource(R.mipmap.loading_fail);
                    }else {
                        Glide.with(context).load(pic).signature(new IntegerVersionSignature(1))
                                .error(R.mipmap.loading_fail).into(vh2.civ);
                    }

                    //帖子图片
                    final List<String> imgs = entity.getThreadImg();
                    if (imgs!=null&&imgs.size()>0){
                        vh2.layout.removeAllViews();
                        int f = imgs.size()>3?3:imgs.size();
                        for (int i = 0 ; i<f ; i++){
                            final int n = i;
                            View v = View.inflate(context,R.layout.item_forum_small_gv,null);
                            TextView tv = (TextView)v.findViewById(R.id.tv);
                            ImageView iv = (ImageView)v.findViewById(R.id.iv);
                            if (i==f-1&&f!=1){
                                tv.setVisibility(View.VISIBLE);
                                tv.setText("共"+imgs.size()+"张");
                            }else {
                                tv.setVisibility(View.GONE);
                            }
                            try {
                                Glide.with(context).load(imgs.get(i)).signature(new IntegerVersionSignature(1))
                                        .error(R.mipmap.loading_fail).into(iv);
                                iv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        callback.ImgClickCallback(imgs, n);
                                    }
                                });
                            }catch (Exception e){

                            }
                            vh2.layout.addView(v);
                        }
                        vh2.layout.setVisibility(View.VISIBLE);
                    }else {
                        vh2.layout.removeAllViews();
                        vh2.layout.setVisibility(View.GONE);
                    }
                    break;
            }

        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"adapter");
        }
        return convertView;
    }

    public void update(List<FavEntity> list){
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder1{
        ImageView img;
        TextView title;
        TextView author;
        TextView view_num;
        TextView time;
        TextView fav_time;
    }

    class ViewHolder2{
        CircleImageView civ;
        TextView name;
        TextView time;
        TextView title;
        TextView content;
        TextView circle;
        TextView like;
        TextView reply;
        ImageView cream;
        ImageView top;
        TextView fav_time;
        LinearLayout layout;
        View v_fav;
        View v_circle;
    }

    public interface onImgClickCallback{
        void ImgClickCallback(List<String> images,int pos);
        void ToInfoCenterCallback(String index);
    }
}

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
import com.zeyuan.kyq.Entity.ForumBaseEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.widget.CircleImageView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/16.
 *
 *
 * @author wwei
 */
public class ForumBaseAdapter extends BaseAdapter{

    private Context context;
    private List<ForumBaseEntity> list;
    private Map<String, String> circleValues;
    private onImgClickCallback callback;


    public ForumBaseAdapter(Context context,List<ForumBaseEntity> list,onImgClickCallback callback){
        this.context = context;
        this.list = list;
        circleValues = (Map<String, String>) Factory.getData(Const.N_DataCircleValues);
        this.callback = callback;
    }

    /*public ForumBaseAdapter(Context context,List<ForumBaseEntity> list,onImgClickCallback callback,int like){
        this.context = context;
        this.list = list;
        circleValues = (Map<String, String>) Factory.getData(Const.N_DataCircleValues);
        this.callback = callback;
        this.like = like;
    }*/

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ForumBaseEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        if (convertView==null){
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_forum_base,null);
            vh.civ = (CircleImageView)convertView.findViewById(R.id.civ);
            vh.name = (TextView)convertView.findViewById(R.id.name);
            vh.time = (TextView)convertView.findViewById(R.id.time);
            vh.title = (TextView)convertView.findViewById(R.id.title);
            vh.content = (TextView)convertView.findViewById(R.id.content);
            vh.reply = (TextView)convertView.findViewById(R.id.reply);
            vh.like = (TextView)convertView.findViewById(R.id.like);
            vh.circle = (TextView)convertView.findViewById(R.id.circle);
            vh.cream = (ImageView)convertView.findViewById(R.id.iv_cream);
            vh.top = (ImageView)convertView.findViewById(R.id.iv_top);
            vh.layout = (LinearLayout)convertView.findViewById(R.id.layout);
            vh.v_circle = convertView.findViewById(R.id.v_circle);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }

        try {
            final ForumBaseEntity entity = list.get(position);

            vh.name.setText(TextUtils.isEmpty(entity.getAuthor())?"":entity.getAuthor());
            vh.time.setText(DataUtils.showFormatTime(entity.getDateLine()));
            vh.title.setText(TextUtils.isEmpty(entity.getTitle())?"":entity.getTitle());
            if ("1".equals(entity.getType())){
                vh.like.setVisibility(View.VISIBLE);
                vh.like.setText(TextUtils.isEmpty(entity.getLikeNum())?"0":entity.getLikeNum());
            }else {
                vh.like.setVisibility(View.GONE);
            }

            vh.reply.setText(TextUtils.isEmpty(entity.getReplyNum())?"0":entity.getReplyNum());

            if (TextUtils.isEmpty(entity.getSummary())){
                vh.content.setText("");
                vh.content.setVisibility(View.GONE);
            }else {
                vh.content.setText(entity.getSummary());
                vh.content.setVisibility(View.VISIBLE);
            }
            String circle = "";
            List<String> temp = entity.getCircleId();
            if (temp!=null&&temp.size()>0){
                for (String str:temp){
                    circle += "  #" + getCircleValues(str);
                }
                vh.v_circle.setVisibility(View.VISIBLE);
            }else {
                vh.v_circle.setVisibility(View.INVISIBLE);
            }
            vh.circle.setText(circle);

            String type = entity.getPosttype();
            if (!TextUtils.isEmpty(type)){
                int t = Integer.valueOf(type);
                if (t!=0&&(t&2)==2){
                    vh.cream.setVisibility(View.VISIBLE);
                }else {
                    vh.cream.setVisibility(View.GONE);
                }
                if (t!=0&&(t&1)==1){
                    vh.top.setVisibility(View.VISIBLE);
                }else {
                    vh.top.setVisibility(View.GONE);
                }
            }else {
                vh.cream.setVisibility(View.GONE);
                vh.top.setVisibility(View.GONE);
            }

            String url = entity.getHeadImgUrl();
            if (TextUtils.isEmpty(url)){
                vh.civ.setImageResource(R.mipmap.default_avatar);
            }else {
                Glide.with(context).load(url)
                        .signature(new IntegerVersionSignature(1))
                        .error(R.mipmap.default_avatar)
                        .into(vh.civ);
            }

            vh.civ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.ToInfoCenterCallback(entity.getOwnerID());
                }
            });

            final List<String> imgs = entity.getPic();

            if (imgs!=null&&imgs.size()>0){
                vh.layout.removeAllViews();
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
                    vh.layout.addView(v);
                }
                vh.layout.setVisibility(View.VISIBLE);
            }else {
                vh.layout.removeAllViews();
                vh.layout.setVisibility(View.GONE);
            }

        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"getView");
        }
        return convertView;
    }


    class ViewHolder{
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
        LinearLayout layout;
        View v_circle;
    }

    public void update(List<ForumBaseEntity> list){
        this.list = list;
        notifyDataSetChanged();
    }

    private String getCircleValues(String id) {
        if(TextUtils.isEmpty(id)) return "";
        if(circleValues == null) return "其他圈子";
        String temp = circleValues.get(id);
        if(TextUtils.isEmpty(temp)){
            return "其他圈子";
        }else{
            return temp;
        }
    }

    public interface onImgClickCallback{
        void ImgClickCallback(List<String> images,int pos);
        void ToInfoCenterCallback(String index);
    }
}

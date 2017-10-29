package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.ArticleCommentEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.OtherUtils;
import com.zeyuan.kyq.widget.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/11/11.
 *
 *
 * @author wwei
 */
public class CommentListAdapter extends BaseAdapter{

    private Context context;
    private List<ArticleCommentEntity> list;
    private String InfoID;
    private OnSonClickListener callback;

    public CommentListAdapter(Context context,OnSonClickListener callback,List<ArticleCommentEntity> list,String InfoID){
        this.context = context;
        this.list = list;
        this.InfoID = InfoID;
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ArticleCommentEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        try {

            ViewHolder vh ;
            if (convertView==null){
                vh = new ViewHolder();
                convertView = View.inflate(context, R.layout.item_article_comment,null);
                vh.civ = (CircleImageView)convertView.findViewById(R.id.civ);
                vh.name = (TextView)convertView.findViewById(R.id.tv_name);
                vh.time = (TextView)convertView.findViewById(R.id.tv_time);
                vh.content = (TextView)convertView.findViewById(R.id.tv_content);
                vh.like = (TextView)convertView.findViewById(R.id.tv_like);
                vh.reply = (TextView)convertView.findViewById(R.id.tv_reply);
                vh.line = convertView.findViewById(R.id.line);
                vh.v_son = convertView.findViewById(R.id.v_son);
                vh.son = (FrameLayout)convertView.findViewById(R.id.son);
                convertView.setTag(vh);
            }else {
                vh = (ViewHolder)convertView.getTag();
            }
            final ArticleCommentEntity entity = list.get(position);

            vh.name.setText(TextUtils.isEmpty(entity.getInfoName())?"":entity.getInfoName());
            vh.time.setText(OtherUtils.isEmpty(entity.getDateLine()+"")?"": DataUtils.showFormatTime(entity.getDateLine()+""));
            vh.content.setText(TextUtils.isEmpty(entity.getContent())?"":entity.getContent());
            if (TextUtils.isEmpty(entity.getHeadUrl())){
                vh.civ.setImageResource(R.mipmap.default_avatar);
            }else {
                Glide.with(context).load(entity.getHeadUrl()).signature(new IntegerVersionSignature(1))
                        .error(R.mipmap.default_avatar).into(vh.civ);
            }
            vh.civ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.toItemInfoCenter(entity,0);
                }
            });
            vh.reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.toItemReply(entity,entity.getCid(),0);
                }
            });
            List<ArticleCommentEntity> sons = entity.getSon();
            if (sons!=null&&sons.size()>0){
                vh.v_son.setVisibility(View.VISIBLE);
                vh.son.removeAllViews();
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int i=0;i<sons.size();i++){
                    final ArticleCommentEntity son = sons.get(i);
                    View v = View.inflate(context,R.layout.son_comment,null);
                    TextView tv_name = (TextView)v.findViewById(R.id.author);
                    TextView tv_is = (TextView)v.findViewById(R.id.is_author);
                    TextView tv_content = (TextView)v.findViewById(R.id.son_msg);
                    if (son.getInfoID().equals(InfoID)){
                        tv_is.setVisibility(View.VISIBLE);
                    }else {
                        tv_is.setVisibility(View.GONE);
                    }
                    tv_name.setText(TextUtils.isEmpty(son.getInfoName())?"":son.getInfoName());
                    String son_id = son.getInfoID();
                    if (!TextUtils.isEmpty(son_id)){
                        tv_name.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callback.toItemInfoCenter(son,1);
                            }
                        });
                    }
                    String msg = son.getContent();
                    if (TextUtils.isEmpty(msg)){
                        tv_content.setText(":");
                    }else {
                        if (!TextUtils.isEmpty(son.getToUserName())&&son.getToUserId()!=0){
                            tv_content.setText("回复@"+son.getToUserName()+":"+msg);
                            tv_content.setText(Html.fromHtml("<font color=\"#a1a1a1\">回复@" +
                                    son.getToUserName() + "</font>:" + msg));
                        }else {
                            tv_content.setText(": "+msg);
                        }

                        tv_content.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callback.toItemReply(son,entity.getCid(),1);
                            }
                        });
                    }
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    layout.addView(v);
                }
                vh.son.addView(layout);
            }else {
                vh.v_son.setVisibility(View.GONE);
            }
            /*if (position==list.size()-1){
                vh.line.setVisibility(View.GONE);
            }else {
                vh.line.setVisibility(View.VISIBLE);
            }*/
            if (entity.getHaveLike()==1){
                vh.like.setSelected(true);
            }else {
                vh.like.setSelected(false);
            }
            vh.like.setText(entity.getLikeNum()+"");
            vh.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.toItemLike(entity,position);
                }
            });

        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"adapter");
        }
        return convertView;
    }

    public void update(List<ArticleCommentEntity> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void update(int position){
        ArticleCommentEntity entity = list.get(position);
        entity.setHaveLike(1);
        list.set(position, entity);
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView name;
        TextView time;
        TextView reply;
        TextView like;
        CircleImageView civ;
        TextView content;
        View v_son;
        FrameLayout son;
        View line;
    }

    public interface OnSonClickListener{
        void toItemInfoCenter(ArticleCommentEntity entity,int tag);
        void toItemReply(ArticleCommentEntity entity,int cid,int tag);
        void toItemLike(ArticleCommentEntity entity,int position);
    }
}

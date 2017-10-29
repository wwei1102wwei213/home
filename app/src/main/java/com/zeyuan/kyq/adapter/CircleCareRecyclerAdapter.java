package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.ForumBaseBean;
import com.zeyuan.kyq.Entity.ForumBaseEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.OtherUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.ArticleDetailActivity;
import com.zeyuan.kyq.view.CareListActivity;
import com.zeyuan.kyq.view.ForumDetailActivity;
import com.zeyuan.kyq.view.InfoCenterActivity;
import com.zeyuan.kyq.view.ShowPhotoActivity;
import com.zeyuan.kyq.widget.CircleImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/21.
 *
 *
 *
 * @author wwei
 */
public class CircleCareRecyclerAdapter extends BaseRecyclerAdapter<CircleCareRecyclerAdapter.CircleCareViewHolder>{


    private static final int TYPE_1 = 0;
    private static final int TYPE_2 = 1;
    private int MAX_CARE = 0;
    private Context context;
    private List<String> cares;
    private List<ForumBaseEntity> list;
    private Map<String, String> circleValues;

    public CircleCareRecyclerAdapter(Context context,List<String> cares,List<ForumBaseEntity> list,int max){
        this.context = context;
        this.MAX_CARE = max;
        if (cares == null) cares = new ArrayList<>();
        this.cares = cares;
        if (list == null) list = new ArrayList<>();
        this.list = list;
        circleValues = (Map<String, String>) Factory.getData(Const.N_DataCircleValues);
    }

    public CircleCareRecyclerAdapter(Context context,ForumBaseBean bean,int max){
        this.context = context;
        this.MAX_CARE = max;
        if (bean!=null){
            cares = bean.getCareHeadUrl();
            list = bean.getForumAllNum();
        }
        if (cares == null) cares = new ArrayList<>();
        if (list == null) list = new ArrayList<>();
        circleValues = (Map<String, String>) Factory.getData(Const.N_DataCircleValues);
    }

    @Override
    public CircleCareViewHolder getViewHolder(View view) {
        return new CircleCareViewHolder(view,false);
    }

    @Override
    public CircleCareViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = null;
        if (viewType == 0) {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.view_care_list_for_circle, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_forum_base_recycler, parent, false);
        }
        return new CircleCareViewHolder(v,viewType,true);
    }

    @Override
    public void onBindViewHolder(CircleCareViewHolder vh, int position, boolean isItem) {
        int type = getAdapterItemViewType(position);
        if (type == TYPE_1){
            try {
                vh.v_care.removeAllViews();
                if (cares!=null&&cares.size()>0){
                    int size = cares.size()>MAX_CARE?MAX_CARE:cares.size();
                    for (int i=0;i<size;i++){
                        View v = View.inflate(context,R.layout.item_rv_care,null);
                        CircleImageView civ = (CircleImageView)v.findViewById(R.id.civ);
                        try {
                            if (TextUtils.isEmpty(cares.get(i))){
                                civ.setImageResource(R.mipmap.default_avatar);
                            }else {
                                Glide.with(context).load(cares.get(i)).signature(new IntegerVersionSignature(1))
                                        .error(R.mipmap.default_avatar).into(civ);
                            }
                        }catch (Exception e){

                        }
                        vh.v_care.addView(v);
                    }
                }
                vh.v_top.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, CareListActivity.class)
                                .putExtra(Const.InfoCenterID, UserinfoData.getInfoID(context))
                                .putExtra(Const.CareListType, 2));
                    }
                });
            }catch (Exception e){
                ExceptionUtils.ExceptionSend(e,"TYPE_1");
            }
        } else if (type == TYPE_2){
            try {
                final ForumBaseEntity entity = list.get(position-1);

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

                String posttype = entity.getPosttype();
                if (!TextUtils.isEmpty(posttype)){
                    int t = Integer.valueOf(posttype);
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
                        if (!OtherUtils.isEmpty(entity.getOwnerID())){
                            context.startActivity(new Intent(context, InfoCenterActivity.class)
                                    .putExtra(Const.InfoCenterID, entity.getOwnerID()));
                        }
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
                                    context.startActivity(new Intent(context, ShowPhotoActivity.class)
                                            .putExtra("list", (Serializable) imgs)
                                            .putExtra("position", n));
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

                vh.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!OtherUtils.isEmpty(entity.getIndex())){
                            if ("1".equals(entity.getType())){
                                context.startActivity(new Intent(context, ArticleDetailActivity.class)
                                        .putExtra(Const.INTENT_ARTICLE_ID,entity.getIndex()));
                            }else{
                                context.startActivity(new Intent(context, ForumDetailActivity.class)
                                        .putExtra(Const.FORUM_ID, entity.getIndex()));
                            }
                        }
                    }
                });

            }catch (Exception e){
                ExceptionUtils.ExceptionSend(e, "getView");
            }
        }
    }

    @Override
    public int getAdapterItemCount() {
        return list.size()+1;
    }

    @Override
    public int getAdapterItemViewType(int position) {
        if (position==0) return TYPE_1;
        return TYPE_2;
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

    public void updateCircle(List<String> cares){
        this.cares = cares;
        notifyDataSetChanged();
    }

    public void update(List<ForumBaseEntity> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void update(List<ForumBaseEntity> list,List<String> cares){
        if (cares == null) cares = new ArrayList<>();
        this.cares = cares;
        if (list == null) list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    public void update(ForumBaseBean bean){
        if (bean!=null){
            cares = bean.getCareHeadUrl();
            list = bean.getForumAllNum();
        }
        if (cares == null) cares = new ArrayList<>();
        if (list == null) list = new ArrayList<>();
        notifyDataSetChanged();
    }

    public class CircleCareViewHolder extends RecyclerView.ViewHolder {
        View v_top;
        LinearLayout v_care;
        CircleImageView civ;
        TextView name,time,title,content,circle,like,reply;
        ImageView cream,top;
        LinearLayout layout;
        View v_circle,v;

        public CircleCareViewHolder(View itemView,boolean isItem) {
            super(itemView);
            init(itemView,-1,isItem);
        }

        public CircleCareViewHolder(View itemView,int viewType,boolean isItem){
            super(itemView);
            init(itemView, viewType, isItem);
        }

        private void init(View itemView, int viewType, boolean isItem) {
            if (isItem) {
                switch (viewType) {
                    case 0:
                        v_top = itemView;
                        v_care = (LinearLayout) itemView.findViewById(R.id.v_care);
                        v_care.setOrientation(LinearLayout.HORIZONTAL);
                        break;
                    default:
                        civ = (CircleImageView)itemView.findViewById(R.id.civ);
                        name = (TextView)itemView.findViewById(R.id.name);
                        time = (TextView)itemView.findViewById(R.id.time);
                        title = (TextView)itemView.findViewById(R.id.title);
                        content = (TextView)itemView.findViewById(R.id.content);
                        reply = (TextView)itemView.findViewById(R.id.reply);
                        like = (TextView)itemView.findViewById(R.id.like);
                        circle = (TextView)itemView.findViewById(R.id.circle);
                        cream = (ImageView)itemView.findViewById(R.id.iv_cream);
                        top = (ImageView)itemView.findViewById(R.id.iv_top);
                        layout = (LinearLayout)itemView.findViewById(R.id.layout);
                        v_circle = itemView.findViewById(R.id.v_circle);
                        v = itemView;
                        break;
                }
            }
        }

    }

    /*private void bindCareHead(ForumBaseBean bean){
        List<String> list = bean.getCareHeadUrl();
        if (list!=null){
            if (list.size()>0){
                v_care.removeAllViews();
                int f = list.size()>MAX_CARE?MAX_CARE:list.size();
                for(int i= 0;i<f;i++){
                    View v = View.inflate(context,R.layout.item_rv_care,null);
                    CircleImageView civ = (CircleImageView)v.findViewById(R.id.civ);
                    if (TextUtils.isEmpty(list.get(i))){
                        civ.setImageResource(R.mipmap.default_avatar);
                    }else {
                        Glide.with(context).load(list.get(i)).signature(new IntegerVersionSignature(1))
                                .error(R.mipmap.default_avatar).into(civ);
                    }
                    v_care.addView(v);
                }

            }else if(list.size()==0){
                v_care.removeAllViews();

            }else {

            }
        }else {

        }
    }*/

}

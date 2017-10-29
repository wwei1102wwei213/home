package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.CareFollowEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.forcallback.AdapterCallback;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.OtherUtils;
import com.zeyuan.kyq.view.InfoCenterActivity;
import com.zeyuan.kyq.widget.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/12/21.
 *
 *
 * @author wwei
 */
public class CircleFriendRecyclerAdapter extends BaseRecyclerAdapter<CircleFriendRecyclerAdapter.CircleFriendViewHolder>{

    private Context context;
    private List<CareFollowEntity> list;
    private AdapterCallback callback;
    private LayoutInflater inflater;

    public CircleFriendRecyclerAdapter(Context context,List<CareFollowEntity> list,AdapterCallback callback){
        this.context = context;
        this.list = list;
        this.callback = callback;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public CircleFriendViewHolder getViewHolder(View view) {
        return new CircleFriendViewHolder(view,false);
    }

    @Override
    public CircleFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = null;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_care_list, parent, false);
        return new CircleFriendViewHolder(v,viewType,true);
    }

    @Override
    public void onBindViewHolder(CircleFriendViewHolder vh,final int position, boolean isItem) {

        final CareFollowEntity entity = list.get(position);

        vh.name.setText(TextUtils.isEmpty(entity.getInfoName())?"":entity.getInfoName());
        if (TextUtils.isEmpty(entity.getCancerName())){
            vh.cancer.setVisibility(View.GONE);
        }else {
            vh.cancer.setText(entity.getCancerName());
            vh.cancer.setVisibility(View.VISIBLE);
        }
        if ("1".equals(entity.getGroupType())){
            vh.big.setVisibility(View.VISIBLE);
            vh.step_day.setVisibility(View.GONE);
            vh.wish.setText(TextUtils.isEmpty(entity.getAutoTxt())?"":entity.getAutoTxt());
            vh.wish.setVisibility(View.VISIBLE);
        }else {
            vh.wish.setVisibility(View.GONE);
            vh.big.setVisibility(View.GONE);
            String step = "";
            List<String> temp = entity.getCureConf();
            if (temp!=null&&temp.size()>0){
                for (String str:temp){
                    step += "[ "+str+" ]";
                }
            }else {
                step = "[ 无治疗信息 ]";
            }
            int day = entity.getDiscoverTime()==0?1:entity.getDiscoverTime();
            vh.step_day.setText(step + " 成功抗癌" + day + "天");
            vh.step_day.setVisibility(View.VISIBLE);
        }
        if ( 0 == entity.getCareStatus()){
            vh.cb.setSelected(false);
            vh.cb.setText("＋关注");
        }else {
            vh.cb.setSelected(true);
            vh.cb.setText("已关注");
        }

        final boolean check = vh.cb.isSelected();
        final String InfoID = entity.getInfoID()+"";
        vh.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!OtherUtils.isEmpty(InfoID)) {
                    callback.forAdapterCallback(position, 0, InfoID, !check, null);
                }
            }
        });

        try {
            if (!TextUtils.isEmpty(entity.getHeadUrl())){
                Glide.with(context).load(entity.getHeadUrl()).signature(new IntegerVersionSignature(1))
                        .error(R.mipmap.default_avatar)
                        .into(vh.civ);
            }else {
                vh.civ.setImageResource(R.mipmap.default_avatar);
            }
        }catch (Exception e){

        }

        vh.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!OtherUtils.isEmpty(InfoID)) {
                    context.startActivity(new Intent(context, InfoCenterActivity.class)
                            .putExtra(Const.InfoCenterID,InfoID));
                }
            }
        });
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    public void update(List<CareFollowEntity> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public class CircleFriendViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView cancer;
        TextView wish;
        TextView step_day;
        TextView cb;
        CircleImageView civ;
        View big;
        View v;
        public CircleFriendViewHolder(View itemView,boolean isItem) {
            super(itemView);
            init(itemView,-1,isItem);
        }

        public CircleFriendViewHolder(View itemView,int viewType,boolean isItem){
            super(itemView);
            init(itemView, viewType, isItem);
        }

        private void init(View convertView, int viewType, boolean isItem) {
            if (isItem) {
                v = convertView;
                name = (TextView)convertView.findViewById(R.id.tv_name);
                cancer = (TextView)convertView.findViewById(R.id.tv_cancer);
                wish = (TextView)convertView.findViewById(R.id.tv_wish);
                step_day = (TextView)convertView.findViewById(R.id.step_and_day);
                cb = (TextView)convertView.findViewById(R.id.cb);
                civ = (CircleImageView)convertView.findViewById(R.id.civ);
                big = convertView.findViewById(R.id.iv_big);
            }
        }
    }

    /**
     *
     * @param position 数据下标
     * @param tag   2 关注 ; 0 取消关注
     * @param flag  请求是否成功
     */
    public void update(int position,int tag,boolean flag){
        try {
            if (flag){
                list.get(position).setCareStatus(tag);
            }
            notifyDataSetChanged();
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "关注回调失败");
        }
    }

}

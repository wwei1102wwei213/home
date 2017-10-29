package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.CareFollowEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.forcallback.AdapterCallback;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.OtherUtils;
import com.zeyuan.kyq.widget.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/11/4.
 *
 * 关注列表和粉丝列表适配器
 *
 * @author wwei
 */
public class CareFollowAdapter extends BaseAdapter{

    private Context context;
    private List<CareFollowEntity> list;
    private AdapterCallback callback;
    private boolean followAble = true;
    private LayoutInflater inflater;

    public CareFollowAdapter(Context context,List<CareFollowEntity> list){
        this.context = context;
        this.list = list;
        this.callback = (AdapterCallback)context;
        this.inflater = LayoutInflater.from(context);
    }

    public CareFollowAdapter(Context context,List<CareFollowEntity> list,AdapterCallback callback){
        this.context = context;
        this.list = list;
        this.callback = callback;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CareFollowEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        if(convertView==null){
            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_care_list,null);
            vh.name = (TextView)convertView.findViewById(R.id.tv_name);
            vh.cancer = (TextView)convertView.findViewById(R.id.tv_cancer);
            vh.wish = (TextView)convertView.findViewById(R.id.tv_wish);
            vh.step_day = (TextView)convertView.findViewById(R.id.step_and_day);
            vh.cb = (TextView)convertView.findViewById(R.id.cb);
            vh.civ = (CircleImageView)convertView.findViewById(R.id.civ);
            vh.big = convertView.findViewById(R.id.iv_big);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }

        final CareFollowEntity entity = list.get(position);

        vh.name.setText(TextUtils.isEmpty(entity.getInfoName())?"":entity.getInfoName());
        if (TextUtils.isEmpty(entity.getCancerName())){
            vh.cancer.setVisibility(View.GONE);
        }else {
            vh.cancer.setText(entity.getCancerName());
            vh.cancer.setVisibility(View.VISIBLE);
        }
//        vh.cancer.setText(TextUtils.isEmpty(entity.getCancerName())?"未知癌种":entity.getCancerName());
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
        /*if (TextUtils.isEmpty(entity.getWish())){
            vh.wish.setVisibility(View.GONE);
        }else {
            vh.wish.setText(entity.getWish());
            vh.wish.setVisibility(View.VISIBLE);
        }*/
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
                if (followAble && !OtherUtils.isEmpty(InfoID)) {
//                    followAble = false;
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

        return convertView;
    }

    class ViewHolder{
        TextView name;
        TextView cancer;
        TextView wish;
        TextView step_day;
        TextView cb;
        CircleImageView civ;
        View big;
    }

    public void update(List<CareFollowEntity> list){
        this.list = list;
        notifyDataSetChanged();
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
//            followAble = true;
            notifyDataSetChanged();
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"关注回调失败");
        }
    }

}

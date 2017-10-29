package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.ForumListBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.FlowLayout;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/16.
 * 这个是帖子列表的adapter 区分置顶 精华和 普通
 */
public class ForumMyAdapter extends BaseAdapter {

    private static final int ZHIDING = 1;//置顶1 精华2 普通0
    private static final int JINGHUA = 2;
    private static final int PUTONG = 0;
    private static final String TAG = "ForumAdapter";
    private static final int FLAG_ZH = 0;
    private static final int FLAG_PT = 1;
    private Context context;
    private LayoutInflater inflater;
    private List<ForumListBean.ForumnumEntity> data;
    private boolean fast = false;
    private Map<String,String> map;
    private boolean Top = true;
    private boolean IsInfoCenter = false;
    private String InfoCenterID;
    private String InfoCenterName;

    public void setFast(boolean fast) {
        this.fast = fast;
    }

    public void setTop(boolean Top) {this.Top = Top;}

    public ForumMyAdapter(Context context, List<ForumListBean.ForumnumEntity> data) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
        map = (Map<String,String>) Factory.getData(Const.N_DataCircleValues);
    }

    public ForumMyAdapter(Context context, List<ForumListBean.ForumnumEntity> data,String id,String name) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
        IsInfoCenter = true;
        map = (Map<String,String>) Factory.getData(Const.N_DataCircleValues);
        this.InfoCenterID = id;
        this.InfoCenterName = name;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ForumListBean.ForumnumEntity getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        String pt = data.get(position).getPosttype();
        if(!TextUtils.isEmpty(pt)){
            int t = Integer.valueOf(pt);
            if(t!=0&&(t&Const.FLAG_FORUM_TOP)==Const.FLAG_FORUM_TOP){
                return FLAG_ZH;
            }else {
                return FLAG_PT;
            }
        }else {
            return FLAG_PT;
        }
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(data.get(position).getIndex());
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ForumListBean.ForumnumEntity item = data.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_forum_info_center, null);
            viewHolder.reply_number = (TextView) convertView.findViewById(R.id.reply_number);
            viewHolder.forum_host_name = (TextView) convertView.findViewById(R.id.forum_host_name);
            viewHolder.title = (TextView) convertView.findViewById(R.id.new_title);
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            viewHolder.zhiding = (ImageView) convertView.findViewById(R.id.new_jinghua);//这个是精华
            viewHolder.has_photo = (ImageView) convertView.findViewById(R.id.new_has_photo);//这个是精华
            viewHolder.time = (TextView)convertView.findViewById(R.id.time);
            viewHolder.fl = (FlowLayout)convertView.findViewById(R.id.fl_item_forum);
            viewHolder.setPt(FLAG_PT);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String postType = item.getPosttype();
        if (!TextUtils.isEmpty(postType)){
            int t = Integer.valueOf(postType);
            if(t!=0&&(t&Const.FLAG_FORUM_JING)==Const.FLAG_FORUM_JING){
                viewHolder.zhiding.setVisibility(View.VISIBLE);
            }else {
                viewHolder.zhiding.setVisibility(View.GONE);
            }
            if(t!=0&&(t&Const.FLAG_FORUM_IMG)==Const.FLAG_FORUM_IMG){
                viewHolder.has_photo.setVisibility(View.VISIBLE);
            }else {
                viewHolder.has_photo.setVisibility(View.GONE);
            }
        }else {
            viewHolder.has_photo.setVisibility(View.GONE);
            viewHolder.zhiding.setVisibility(View.GONE);
        }
        /*RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        p.setMargins(DensityUtils.dpToPx(context, 16), DensityUtils.dpToPx(context, 10), 0, 0);*/
        if(TextUtils.isEmpty(item.getReplyNum())){
            viewHolder.reply_number.setText("0");
        }else{
            viewHolder.reply_number.setText(item.getReplyNum());
        }
        String name = "";
        if (IsInfoCenter){
            name = InfoCenterName;
        }else {
            UserinfoData.getInfoname(context);
        }
        if(TextUtils.isEmpty(name)){
            viewHolder.forum_host_name.setText("");
        }else {
            viewHolder.forum_host_name.setText(name);
        }

        try {
            if(TextUtils.isEmpty(item.getPostTime())){
                viewHolder.time.setVisibility(View.GONE);
            }else {
                viewHolder.time.setVisibility(View.VISIBLE);
                viewHolder.time.setText(DataUtils.showFormatTime(item.getPostTime()));
            }
        }catch (Exception e){

        }
        String titles = item.getTitle();
        if(TextUtils.isEmpty(titles)){
            viewHolder.title.setText("");
        }else {
            viewHolder.title.setText(titles);
        }

        try {
            if(!TextUtils.isEmpty(item.getHeadimgurl())){
                Glide.with(context).load(item.getHeadimgurl()).signature(new IntegerVersionSignature(1))
                        .error(R.mipmap.default_avatar)
                        .into(viewHolder.avatar);
            }else {
                viewHolder.avatar.setImageResource(R.mipmap.default_avatar);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"帖子中获取用户头像失败");
        }

        try {
            List<String> cl = item.getCircleId();
            if(cl==null||cl.size()==0){
                viewHolder.fl.removeAllViews();
            }else {
                viewHolder.fl.removeAllViews();
                for(String temp:cl){
                    String tName = map.get(temp);
                    TextView tv_t = (TextView)View.inflate(context,R.layout.tv_from_circle,null);
                    tv_t.setText(tName);
                    viewHolder.fl.addView(tv_t);
                }
            }
        }catch (Exception e){

        }



        return convertView;
    }

    class ViewHolder {
        TextView reply_number;
        TextView forum_host_name;
        TextView title;
        TextView time;
        ImageView avatar;
        ImageView has_photo;
        ImageView zhiding;
        FlowLayout fl;
        private int pt = -1;

        public int getPt() {
            return pt;
        }

        public void setPt(int pt) {
            this.pt = pt;
        }
    }

    public void update(List data) {
        this.data = data;
        notifyDataSetChanged();
    }
}

package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.ForumListBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.DensityUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.widget.FlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/16.
 * 这个是帖子列表的adapter 区分置顶 精华和 普通
 */
public class ForumAdapter extends BaseAdapter {

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
    private String CircleID;
    private boolean circleFlag = false;
    private List<String> circleList = null;

    public void setFast(boolean fast) {
        this.fast = fast;
    }

    public void setTop(boolean Top) {this.Top = Top;}

    public ForumAdapter(Context context, List<ForumListBean.ForumnumEntity> data) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
        map = (Map<String,String>) Factory.getData(Const.N_DataCircleValues);
    }

    public ForumAdapter(Context context, List<ForumListBean.ForumnumEntity> data,String CircleID) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
        map = (Map<String,String>) Factory.getData(Const.N_DataCircleValues);
        if(!TextUtils.isEmpty(CircleID)){
            this.CircleID = CircleID;
            Map<String,List<String>> temp = (Map<String,List<String>>) Factory.getData(Const.N_DataCircleCancer);
            List<String> temps = temp.get(CircleID);
            if(temps==null||temps.size()==0){
                circleList = new ArrayList<>();
                circleList.add(CircleID);
            }else {
                circleList = new ArrayList<>();
                circleList.addAll(temps);
                circleList.add(CircleID);
            }
            circleFlag = true;
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position).getIndex();
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
//        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ForumListBean.ForumnumEntity item = data.get(position);
        ViewHolder viewHolder;
        int type = getItemViewType(position);
//        SecondViewHolder vh2;
        String postType = item.getPosttype();
        viewHolder = new ViewHolder();

        switch (type){
            case FLAG_ZH:
                if(convertView==null||viewHolder.getPt()!=FLAG_ZH){
                    viewHolder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.item_top_forum,null);
                    viewHolder.tv_title = (TextView)convertView.findViewById(R.id.title);
                    viewHolder.iv_zd = (ImageView)convertView.findViewById(R.id.zhiding);
                    viewHolder.iv_jh = (ImageView)convertView.findViewById(R.id.jinghua);
                    viewHolder.iv_has = (ImageView)convertView.findViewById(R.id.has_photo);
                    viewHolder.line_zd = convertView.findViewById(R.id.line_zd);
                    viewHolder.setPt(FLAG_ZH);
                    convertView.setTag(viewHolder);
                }else {
                    viewHolder = (ViewHolder)convertView.getTag();
                }

                if(position == 0){
                    if(Top){
                        viewHolder.line_zd.setVisibility(View.VISIBLE);
                    }else {
                        viewHolder.line_zd.setVisibility(View.GONE);
                    }
                }
                int postInt = Integer.valueOf(postType);
                if(postInt!=0&&(postInt&Const.FLAG_FORUM_JING)==Const.FLAG_FORUM_JING){
                    viewHolder.iv_jh.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.iv_jh.setVisibility(View.GONE);
                }
                if(postInt!=0&&(postInt&Const.FLAG_FORUM_IMG)==Const.FLAG_FORUM_IMG){
                    viewHolder.iv_has.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.iv_has.setVisibility(View.GONE);
                }
                String title = item.getTitle();
                if(!TextUtils.isEmpty(title)){
                    viewHolder.tv_title.setText(title);
                }else {
                    viewHolder.tv_title.setText("");
                }
                break;
            case FLAG_PT:
                if (convertView == null||viewHolder.getPt()!=FLAG_PT) {
                    viewHolder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.item_new_forum, null);
                    viewHolder.reply_number = (TextView) convertView.findViewById(R.id.reply_number);
                    viewHolder.forum_host_name = (TextView) convertView.findViewById(R.id.forum_host_name);
                    viewHolder.title = (TextView) convertView.findViewById(R.id.new_title);
                    viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
                    viewHolder.zhiding = (ImageView) convertView.findViewById(R.id.new_jinghua);//这个是精华
                    viewHolder.has_photo = (ImageView) convertView.findViewById(R.id.new_has_photo);//这个是精华
                    viewHolder.time = (TextView)convertView.findViewById(R.id.time);
                    viewHolder.line = convertView.findViewById(R.id.line_forum);
                    viewHolder.lint_bottom = convertView.findViewById(R.id.line_bottom);
                    viewHolder.fl = (FlowLayout)convertView.findViewById(R.id.fl_item_forum);
                    viewHolder.setPt(FLAG_PT);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                if(position==getCount()-1){
                    viewHolder.lint_bottom.setVisibility(View.GONE);
                }else {
                    viewHolder.lint_bottom.setVisibility(View.VISIBLE);
                }

                if(position>0&&FLAG_ZH==getItemViewType(position-1)){
                    viewHolder.line.setVisibility(View.VISIBLE);
                }else {
                    if(position == 0){
                        if(Top){
                            viewHolder.line.setVisibility(View.VISIBLE);
                        }else {
                            viewHolder.line.setVisibility(View.GONE);
                        }
                    }else {
                        viewHolder.line.setVisibility(View.GONE);
                    }
                }
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
                RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                p.setMargins(DensityUtils.dpToPx(context, 16), DensityUtils.dpToPx(context, 10), 0, 0);
                if(TextUtils.isEmpty(item.getReplyNum())){
                    viewHolder.reply_number.setText("0");
                }else{
                    viewHolder.reply_number.setText(item.getReplyNum());
                }
                viewHolder.forum_host_name.setText(item.getAuthor());

                try {
                    if(TextUtils.isEmpty(item.getPostTime())){
                        viewHolder.time.setVisibility(View.GONE);
//                        viewHolder.time.setText("1小时前");
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
                            if(circleFlag){
                                if (circleList.contains(temp)){
                                    String tName = map.get(temp);
                                    TextView tv_t = (TextView)View.inflate(context,R.layout.tv_from_circle,null);
                                    tv_t.setText(tName);
                                    viewHolder.fl.addView(tv_t);
                                }
                            }else {
                                String tName = map.get(temp);
                                TextView tv_t = (TextView)View.inflate(context,R.layout.tv_from_circle,null);
                                tv_t.setText(tName);
                                viewHolder.fl.addView(tv_t);
                            }
                        }
                    }
                }catch (Exception e){

                }


                break;
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
        TextView tv_title;
        ImageView iv_zd;
        ImageView iv_jh;
        ImageView iv_has;
        FlowLayout fl;
        View line;
        View line_zd;
        View lint_bottom;
        private int pt = -1;

        public int getPt() {
            return pt;
        }

        public void setPt(int pt) {
            this.pt = pt;
        }
    }

    class SecondViewHolder{
        TextView tv_title;
        ImageView iv_zd;
        ImageView iv_jh;
        ImageView iv_has;
    }

    public void update(List data) {
        this.data = data;
        notifyDataSetChanged();
    }
}

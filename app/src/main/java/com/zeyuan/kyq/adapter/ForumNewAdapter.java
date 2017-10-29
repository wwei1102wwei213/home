package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.ForumListBean;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.widget.FlowLayout;

import java.util.List;

/**
 * Created by Administrator on 2016/8/23.
 *
 *
 *
 * @author wwei
 */
public class ForumNewAdapter extends BaseAdapter{

    private Context context;
    private List<ForumListBean.ForumnumEntity> data;
    private List<ForumListBean.ForumnumEntity> top;
    private String CircleID;
    private LayoutInflater inflater;
    private boolean topFlag = false;
    private boolean needTop = true;

    public void setTop(boolean needTop) {
        this.needTop = needTop;
    }

    public ForumNewAdapter(Context context,List<ForumListBean.ForumnumEntity> data,List<ForumListBean.ForumnumEntity> top){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        this.top = top;
        if(top==null||top.size()==0){
            topFlag = false;
        }else {
            topFlag = true;
        }
    }

    public ForumNewAdapter(Context context,List<ForumListBean.ForumnumEntity> data,List<ForumListBean.ForumnumEntity> top,String CircleID){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        this.CircleID = CircleID;
        this.top = top;
        if(top==null||top.size()==0){
            topFlag = false;
        }else {
            topFlag = true;
        }
    }

    @Override
    public int getCount() {
        if (topFlag){
            return data.size() + 1;
        }
        return data.size();
    }

    @Override
    public String getItem(int position) {
        if(topFlag){
            if(position == 0){
                return "0";
            }
            return data.get(position-1).getIndex();
        }
        return data.get(position).getIndex();
    }

    @Override
    public long getItemId(int position) {
        if (topFlag){
            if (position==0){
                return 0;
            }
            return Long.valueOf(data.get(position).getIndex());
        }
        return Long.valueOf(data.get(position).getIndex());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int pos = position;
        if (topFlag) {
            if (pos == 0){
                View view = inflater.inflate(R.layout.item_empty_layout,null);
                LinearLayout layout = (LinearLayout)view;
                for (int i=0;i<top.size();i++){
                    ForumListBean.ForumnumEntity item = data.get(i);
                    View v = inflater.inflate(R.layout.item_top_forum, null);
                    TextView tv_top = (TextView)v.findViewById(R.id.title);
                    View jh = v.findViewById(R.id.jinghua);
                    View has = v.findViewById(R.id.has_photo);
                    View line_zd = v.findViewById(R.id.line_zd);
                    String postType = item.getPosttype();
                    if(i == 0){
                        if(needTop){
                            line_zd.setVisibility(View.VISIBLE);
                        }else {
                            line_zd.setVisibility(View.GONE);
                        }
                    }
                    int postInt = Integer.valueOf(postType);
                    if(postInt!=0&&(postInt& Const.FLAG_FORUM_JING)==Const.FLAG_FORUM_JING){
                        jh.setVisibility(View.VISIBLE);
                    }else {
                        jh.setVisibility(View.GONE);
                    }
                    if(postInt!=0&&(postInt&Const.FLAG_FORUM_IMG)==Const.FLAG_FORUM_IMG){
                        has.setVisibility(View.VISIBLE);
                    }else {
                        has.setVisibility(View.GONE);
                    }
                    String title = item.getTitle();
                    if(!TextUtils.isEmpty(title)){
                        tv_top.setText(title);
                    }else {
                        tv_top.setText("");
                    }

                    layout.addView(v);
                }
                return view;
            }else {
                pos = position -1;
            }
        }
        ViewHolder viewHolder = null;
        if(convertView==null){
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
            convertView.setTag(viewHolder);
        }else {

        }

        return convertView;
    }

    class ViewHolder{
        TextView reply_number;
        TextView forum_host_name;
        TextView title;
        TextView time;
        ImageView avatar;
        ImageView has_photo;
        ImageView zhiding;
        FlowLayout fl;
        View line;
        View lint_bottom;
    }

}

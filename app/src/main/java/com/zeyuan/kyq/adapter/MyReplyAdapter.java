package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.MyReplyListBean;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.List;

/**
 * Created by Administrator on 2015/9/16.
 * 这个是帖子列表的adapter 区分置顶 精华和 普通
 */
public class MyReplyAdapter extends BaseAdapter {


    private Context context;
    private LayoutInflater inflater;
    private List<MyReplyListBean.ReplyNumEntity> data;

    public MyReplyAdapter(Context context, List<MyReplyListBean.ReplyNumEntity> data) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);

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
    public long getItemId(int position) {
        return Long.valueOf(data.get(position).getIndex());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_info_reply, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.forum_host_name = (TextView) convertView.findViewById(R.id.forum_host_name);
            viewHolder.reply_number = (TextView) convertView.findViewById(R.id.time);//这个就是时间
            viewHolder.reply = (TextView) convertView.findViewById(R.id.reply);
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MyReplyListBean.ReplyNumEntity item = data.get(position);

        try {

            String time = item.getReplyTime();
            viewHolder.reply_number.setText(DataUtils.showFormatTime(time));

            String title = item.getTitle();
            if(TextUtils.isEmpty(title)){
                viewHolder.reply.setText("原文已删除！");
            }else{
                viewHolder.reply.setText("原文：" + title);
            }

            String content = item.getContent();
            if(TextUtils.isEmpty(content)||"null".equals(content)){
                content = "";
            }
            String fromUser = item.getFromUser();
            if(TextUtils.isEmpty(fromUser)){
                fromUser = "";
            }
            String toUser = item.getToUser();
            if(TextUtils.isEmpty(toUser)){
                toUser = "";
            }
            String useId = item.getUserId();
            if(TextUtils.isEmpty(useId)){
                useId = "";
            }
            if("".equals(toUser)){
                viewHolder.title.setText(content);
            }else{
                if(useId.equals(UserinfoData.getInfoID(context))){

                    viewHolder.title.setText(Html.fromHtml("回复"+"<font color=\"#9f9f9f\"> @我: </font>"+content));
                }else{

                    viewHolder.title.setText(Html.fromHtml("回复"+"<font color=\"#9f9f9f\">"+" @"+toUser+ ": "+"</font>" +content));
                }

            }

            viewHolder.forum_host_name.setText(fromUser);
            if(item.getHeadImgurl()!=null){
                Glide.with(context).load(item.getHeadImgurl()).signature(new IntegerVersionSignature(1)).into(viewHolder.avatar);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,context,"Adp");
        }
        return convertView;
    }

    class ViewHolder {
        TextView reply_number;
        TextView forum_host_name;
        TextView title;
        ImageView avatar;
        TextView reply;
    }

    public void update(List data) {
        this.data = data;
        notifyDataSetChanged();
    }
}

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
import com.zeyuan.kyq.Entity.NewReplyEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;

import java.util.List;

/**
 * Created by Administrator on 2016/11/3.
 */
public class InfoReplyAdapter extends BaseAdapter{

    private Context context;
    private List<NewReplyEntity> data;
    private LayoutInflater inflater;


    public InfoReplyAdapter(Context context,List<NewReplyEntity> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public NewReplyEntity getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
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
        NewReplyEntity item = data.get(position);

        try {
            String time = item.getReplyTime()+"";
            viewHolder.reply_number.setText(DataUtils.showFormatTime(time));

            String title = item.getTitle();
            if(TextUtils.isEmpty(title)){
                viewHolder.reply.setText("原文已删除！");
            }else{
                viewHolder.reply.setText(title);
            }

            String content = item.getCommentContent();
            if(TextUtils.isEmpty(content)||"null".equals(content)){
                content = "";
            }
            viewHolder.title.setText(content);
            String fromUser = item.getInfoName();
            if(TextUtils.isEmpty(fromUser)){
                fromUser = "";
            }

            viewHolder.forum_host_name.setText(fromUser);
            if(item.getHeadUrl()!=null){
                Glide.with(context).load(item.getHeadUrl()).signature(new IntegerVersionSignature(1)).into(viewHolder.avatar);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, context, "Adp");
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

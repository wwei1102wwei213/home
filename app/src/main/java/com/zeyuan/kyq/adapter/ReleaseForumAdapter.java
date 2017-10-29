package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.MyForumReleaseBean;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2015/9/16.
 * 我发布的梯子的adapter 主要多了一个posttime
 */
public class ReleaseForumAdapter extends BaseAdapter {

    private static final int ZHIDING = 1;//置顶1 精华2 普通0
    private static final int JINGHUA = 2;
    private static final int PUTONG = 0;

    private Context context;
    private LayoutInflater inflater;
    private List<MyForumReleaseBean.ForumBeanEntity> data;

    public ReleaseForumAdapter(Context context, List<MyForumReleaseBean.ForumBeanEntity> data) {
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

    public MyForumReleaseBean.ForumBeanEntity getEntity(int position){
        return data.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
//        return PUTONG;
        switch (data.get(position).getPostType()) {
            case "1": {
                return ZHIDING;
            }
            case "2": {
                return JINGHUA;
            }
            case "0": {
                return PUTONG;
            }
            default:
                throw new RuntimeException("switch has not choose!!");
        }
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(data.get(position).getIndex());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        int type = getItemViewType(position);
        switch (type) {
            case ZHIDING: {//置顶帖子
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.item_top_forum, null);
                    viewHolder = new ViewHolder();
                    //find views

                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }






                break;
            }
            case JINGHUA: {//精华

                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.item_forum, null);
                    viewHolder = new ViewHolder();
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }






                break;
            }
            case PUTONG: {//普通
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.item_mine_forum, null);
                    viewHolder = new ViewHolder();
                    viewHolder.reply_number = (TextView) convertView.findViewById(R.id.reply_number);
                    viewHolder.forum_host_name = (TextView) convertView.findViewById(R.id.forum_host_name);
                    viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                    viewHolder.avatar = (CircleImageView) convertView.findViewById(R.id.avatar);


                    viewHolder.time = (TextView) convertView.findViewById(R.id.time);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                MyForumReleaseBean.ForumBeanEntity item = data.get(position);

                viewHolder.reply_number.setText(item.getReplyNum());
//                viewHolder.forum_host_name.setText(item.getAuthor());//要设置成为自己的名字
                viewHolder.title.setText(item.getTitle());
                String time = item.getPostTime();
                if (!TextUtils.isEmpty(time)) {
                    viewHolder.time.setVisibility(View.VISIBLE);
                }
                String name = UserinfoData.getInfoname(context);
                if(!TextUtils.isEmpty(name)){
                    viewHolder.forum_host_name.setText(name);
                }
                viewHolder.time.setText(DataUtils.showFormatTime(time));
                String url = item.getHeadImgUrl();
                if(!TextUtils.isEmpty(url)){
                    Glide.with(context).load(url).signature(new IntegerVersionSignature(1)).into(viewHolder.avatar);
                }

//                String name = item.get
//                  viewHolder.forum_host_name.settex

                break;
            }
        }



        return convertView;
    }

    class ViewHolder {
        TextView reply_number;
        TextView forum_host_name;
        TextView title;
        TextView time;
        CircleImageView avatar;
    }

    public void update(List data) {
        this.data = data;
        notifyDataSetChanged();
    }
}

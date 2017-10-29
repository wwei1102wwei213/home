package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.zeyuan.kyq.bean.ReplyListBean;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.OtherUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.InfoCenterActivity;
import com.zeyuan.kyq.view.ViewDataListener;
import com.zeyuan.kyq.widget.CircleImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/29.
 *
 *
 * @author wwei
 */
public class ReplyForumItemAdapter extends BaseAdapter implements ViewDataListener {

    private static final String TAG = "ReplyForumItemAdapter";
    private List<ReplyListBean.ReplyNumEntity> data;
    private Context context;
    private LayoutInflater inflater;
    private String ownerID;

    public ReplyForumItemAdapter(Context context, List<ReplyListBean.ReplyNumEntity> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    public interface ShowInput {
        void showInput(int position);
//        void showInfoCenter(int position);
    }

    private ShowInput mShowInput;

    public void setShowInput(ShowInput mShowInput) {
        this.mShowInput = mShowInput;
    }

    @Override
    public ReplyListBean.ReplyNumEntity getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_reply_forum, null);
            viewHolder = new ViewHolder();
            viewHolder.img = (CircleImageView) convertView.findViewById(R.id.host_avatar);
            viewHolder.release_forum_name = (TextView) convertView.findViewById(R.id.release_forum_name);
            viewHolder.release_forum_time = (TextView) convertView.findViewById(R.id.release_forum_time);
            viewHolder.reply_textview = (TextView) convertView.findViewById(R.id.reply_textview);
            viewHolder.is_host = (ImageView) convertView.findViewById(R.id.is_host);
            viewHolder.reply_content = (TextView) convertView.findViewById(R.id.reply_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //初始化
        final ReplyListBean.ReplyNumEntity item = data.get(position);
        final String imageUrl = item.getHeadimgurl();
//        String minUrl = null;
        /*if(!TextUtils.isEmpty(imageUrl)){
               if(imageUrl.startsWith("http://zeyuan1.oss")&&imageUrl.lastIndexOf(".")!=-1){
                   String temp1 = imageUrl.substring(0,imageUrl.lastIndexOf(".")) + "thumb";
                   String temp2 = imageUrl.substring(imageUrl.lastIndexOf("."),imageUrl.length());
                   minUrl = temp1 + temp2;
               }
        }*/
        /*if(TextUtils.isEmpty(minUrl)){
            Glide.with(context).load(imageUrl).error(R.mipmap.default_avatar).into(viewHolder.img);
        }else {*/
//            Glide.with(context).load(minUrl).error(R.mipmap.default_avatar).into(viewHolder.img);//.signature(new IntegerVersionSignature(1))
//        }
        Glide.with(context).load(imageUrl).error(R.mipmap.default_avatar).into(viewHolder.img);
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!OtherUtils.isEmpty(item.getUserId())){
                    Intent intent = new Intent(context, InfoCenterActivity.class);
                    intent.putExtra(Const.InfoCenterID, item.getUserId());
                    context.startActivity(intent);
                }
            }
        });

        viewHolder.reply_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShowInput != null) {
                    mShowInput.showInput(position);
                }
            }
        });
        if(TextUtils.isEmpty(ownerID)){
            ownerID = item.getOwnerID();
        }

        if(item.getUserId().equals(ownerID)){
            viewHolder.is_host.setVisibility(View.VISIBLE);
        }else{
            viewHolder.is_host.setVisibility(View.GONE);
        }
        viewHolder.release_forum_name.setText(item.getFromUser());

        String replyTime = item.getReplyTime();
        if (!TextUtils.isEmpty(replyTime)) {
            viewHolder.release_forum_time.setText(DataUtils.showFormatTime(replyTime));
        }
//        viewHolder.is_host.setVisibility(View.GONE);
        String content = item.getContent();
        if(TextUtils.isEmpty(content)||"null".equals(content)){
            content = "";
        }
        String touser = item.getToUser();
        if (TextUtils.isEmpty(touser)) {
            viewHolder.reply_content.setText(content);
        } else {
            if(touser.equals(UserinfoData.getInfoID(context))){
                viewHolder.reply_content.setText(Html.fromHtml("回复" + "<font color=\"#9f9f9f\"> @我: </font>" + content));
//                viewHolder.reply_content.setText("回复" + " @我: " + content+"");
            }else{
                viewHolder.reply_content.setText(Html.fromHtml("回复" + "<font color=\"#9f9f9f\"> @"+touser+": </font>" + content));
//                viewHolder.reply_content.setText("回复" + " @"+touser+": " + content+"");
            }
        }
        return convertView;
    }

    /**
     * 回复某个人
     *
     * @param tag
     * @return
     */
    private String Content;//内容
    private String InfoID;
    private String fromuser;//我的infromname。
    private String index;//帖子的id
    private String toinfoid;//平论人的id
    private String touser;//评论这个人的infroname

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
//         map.put(Contants.index,index)
        return map;
    }

    @Override
    public void toActivity(Object t, int tag) {

    }

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {

    }

    @Override
    public void showError(int tag) {

    }

    class ViewHolder {
        CircleImageView img;//头像
        TextView release_forum_name;//回帖人的名字
        TextView release_forum_time;//回复的时间
        TextView reply_textview;//回复
        ImageView is_host;//设置显示来显示是否为楼主
        TextView reply_content;//回复的内容
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    public void update(List data,String ownerID) {
        this.data = data;
        this.ownerID = ownerID;
        notifyDataSetChanged();
    }


    /*private String getDateFromData(String date) {
        date = date.concat("000");
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        long dates = Long.parseLong(date);
        return format.format(new Date(dates));
    }*/

//    private void replyForum() {
//        new ReplyForumPresent(this).getData();
//    }
}

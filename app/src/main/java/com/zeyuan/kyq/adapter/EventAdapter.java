package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.MainBannerEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 *
 *
 * @author wwei
 */
public class EventAdapter extends BaseAdapter{

    private Context context;
    private List<MainBannerEntity> list;

    public EventAdapter(Context context,List<MainBannerEntity> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MainBannerEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        if(convertView == null){
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_list_event,null);
            vh.iv = (ImageView)convertView.findViewById(R.id.iv_event_item);
            vh.tv_title = (TextView)convertView.findViewById(R.id.tv_item_title);
            vh.tv_text = (TextView)convertView.findViewById(R.id.tv_infotext);
            vh.tv_time = (TextView)convertView.findViewById(R.id.tv_item_summary);
            vh.tv_flag = (TextView)convertView.findViewById(R.id.tv_running_flag);
            vh.tv_end = (TextView)convertView.findViewById(R.id.tv_end_text);
            vh.v = convertView.findViewById(R.id.v_end_face);
            vh.v_yugao = convertView.findViewById(R.id.v_yugao);
            convertView.setTag(vh);
        }
        MainBannerEntity entity = list.get(position);
        vh = (ViewHolder)convertView.getTag();
        try {
            String url = entity.getImgurl();
            Glide.with(context).load(url).into(vh.iv);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"getView");
        }

        try {
            String title = entity.getTitlename();
            if (TextUtils.isEmpty(title)){
                vh.tv_title.setText("");
            }else {
                vh.tv_title.setText(title);
            }

            String flag = entity.getStartNum();
            if ("1".equals(flag)){
                vh.tv_flag.setVisibility(View.VISIBLE);
                vh.tv_flag.setText("进行中");
                vh.tv_flag.setTextColor(Color.parseColor("#FFFFFF"));
                vh.tv_flag.setBackgroundResource(R.mipmap.event_running);
                vh.v.setVisibility(View.GONE);
            } else if ("2".equals(flag)){
                vh.tv_flag.setVisibility(View.VISIBLE);
                vh.tv_flag.setText("已结束");
                vh.tv_flag.setTextColor(Color.parseColor("#575757"));
                vh.tv_flag.setBackgroundResource(R.mipmap.event_ending);
                vh.v.setVisibility(View.VISIBLE);
                vh.v_yugao.setBackgroundResource(R.mipmap.event_end_image);
                vh.tv_end.setVisibility(View.VISIBLE);
            } else if ("3".equals(flag)){
                vh.tv_flag.setVisibility(View.VISIBLE);
                vh.tv_flag.setText("");
                vh.tv_flag.setBackgroundResource(R.mipmap.event_flag_4);
                vh.v.setVisibility(View.VISIBLE);
                vh.v_yugao.setBackgroundResource(R.mipmap.event_flag_3);
                vh.tv_end.setVisibility(View.GONE);
            }else {
                vh.tv_flag.setVisibility(View.GONE);
                vh.v.setVisibility(View.GONE);
            }

            String text = entity.getInfotext();
            if (TextUtils.isEmpty(text)||"0".equals(flag)){
                vh.tv_text.setVisibility(View.GONE);
            }else {
                vh.tv_text.setVisibility(View.VISIBLE);
                vh.tv_text.setText(text);
            }

            String startTime = entity.getStarttime();
            String start = "";
            String endTime = entity.getEndtime();
            String end = "";
            if (TextUtils.isEmpty(startTime)||"0".equals(startTime)){
                start = "暂无";
            } else {
                try {
                    start = DataUtils.getDate(startTime);
                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e,"getDate");
                }
            }
            if (TextUtils.isEmpty(endTime)||"0".equals(endTime)){
                end = "暂无";
            } else {
                try {
                    end = DataUtils.getDate(endTime);
                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e,"getDate");
                }
            }
            vh.tv_time.setText("活动时间："+start + "——" +end);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"getView");
        }

        return convertView;
    }

    public void update(List<MainBannerEntity> list){
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder{
        ImageView iv;
        TextView tv_title;
        TextView tv_time;
        TextView tv_text;
        TextView tv_flag;
        View v_yugao;
        TextView tv_end;
        View v;
    }
}

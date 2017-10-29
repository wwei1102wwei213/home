package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.HomePageEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.widget.CircleImageView;

import java.util.List;
import java.util.Random;
import java.util.Timer;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Administrator on 2017/4/10.
 *
 * @author wwei
 */
public class HomeGvAdapter extends BaseAdapter {

    private Context context;
    private List<HomePageEntity> list;
    private Random mRandom = new Random();
    private Timer mTimer = new Timer();

    public HomeGvAdapter(Context context, List<HomePageEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public HomePageEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gv_home, parent, false);
            vh.iv = (CircleImageView) convertView.findViewById(R.id.iv);
            vh.tv = (TextView) convertView.findViewById(R.id.tv);
            vh.iv_living = (GifImageView) convertView.findViewById(R.id.iv_living);
            vh.iv_live_tag = (ImageView) convertView.findViewById(R.id.iv_live_tag);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        try {
            HomePageEntity entity = list.get(position);
            String pic = entity.getPic_oss();
            if (!TextUtils.isEmpty(pic)) {
                Glide.with(context).load(pic).error(R.mipmap.loading_fail).into(vh.iv);
            } else {
                vh.iv.setImageResource(R.mipmap.gv_more);
            }
            String name = entity.getName();
            vh.tv.setText(TextUtils.isEmpty(name) ? "未知栏目" : name);

            if (!TextUtils.isEmpty(entity.getSkiptype())&&!TextUtils.isEmpty(entity.getSign_b())&&entity.getSkiptype().equals("11") && entity.getSign_b().equals("2")) {
                String tag = entity.getTag();
                if (!TextUtils.isEmpty(tag)) {
                    switch (entity.getTag()) {
                        case "0":
                            vh.iv_living.setVisibility(View.GONE);
                            vh.iv_live_tag.setVisibility(View.VISIBLE);
                            vh.iv_live_tag.setImageResource(R.mipmap.ic_play_back);
                            break;
                        case "1":
                            vh.iv_living.setVisibility(View.VISIBLE);
                            vh.iv_live_tag.setVisibility(View.GONE);
                            break;
                        case "2":
                            vh.iv_living.setVisibility(View.GONE);
                            vh.iv_live_tag.setVisibility(View.VISIBLE);
                            vh.iv_live_tag.setImageResource(R.mipmap.ic_foreshow);
                            break;
                    }
                }


            } else {
                vh.iv_living.setVisibility(View.GONE);
                vh.iv_live_tag.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "首页快速入口");
        }
        return convertView;
    }

    class ViewHolder {
        CircleImageView iv;
        TextView tv;
        ImageView iv_live_tag;//预告和录播
        GifImageView iv_living;//直播中
    }

    public void update(List<HomePageEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        cancelLiveAnim();
        mTimer = new Timer();
        super.notifyDataSetChanged();
    }

    //取消直播中的动画
    public void cancelLiveAnim() {
        if (mTimer != null)
            mTimer.cancel();
    }

    private int randomColor() {
        return Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255));
    }
}

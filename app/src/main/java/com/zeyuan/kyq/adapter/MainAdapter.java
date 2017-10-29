package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.MainPageInfoBean;
import com.zeyuan.kyq.utils.IntegerVersionSignature;

import java.util.List;

/**
 * Created by Administrator on 2015/12/31.
 */
public class MainAdapter extends BaseAdapter {
    private static final int TITLE_IMG = 1;
    private static final int COMMON_CONTENT = 2;


    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<MainPageInfoBean.MainItemEntity> datas;

    public MainAdapter(Context mContext, List<MainPageInfoBean.MainItemEntity> datas) {
        this.datas = datas;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holderImg;
        ViewHolder3 holderCommon;
        int type = getItemViewType(position);
        if (TITLE_IMG == type) {//这个只有标题和文字
            if (convertView == null) {
                holderImg = new ViewHolder();
                convertView = mLayoutInflater.inflate(R.layout.main_item_img, null);
                holderImg.img = (ImageView) convertView.findViewById(R.id.img);
                holderImg.title = (TextView) convertView.findViewById(R.id.img_title);
                convertView.setTag(holderImg);
            } else {
                holderImg = (ViewHolder) convertView.getTag();
            }
            String imgUrl = datas.get(position).getImageUrl();
            String title = datas.get(position).getTitle();
            Glide.with(mContext).load(imgUrl).signature(new IntegerVersionSignature(0)).into(holderImg.img);
            holderImg.title.setText(title);
        }
        if (COMMON_CONTENT == type) {
            // /这个是图片和标题并列
            if (convertView == null) {
                holderCommon = new ViewHolder3();
                convertView = mLayoutInflater.inflate(R.layout.include_main_item, null);
                holderCommon.img = (ImageView) convertView.findViewById(R.id.img);
                holderCommon.title = (TextView) convertView.findViewById(R.id.title);
                holderCommon.watch = (TextView) convertView.findViewById(R.id.watch);
                convertView.setTag(holderCommon);
            } else {
                holderCommon = (ViewHolder3) convertView.getTag();
            }

            String imgUrl = datas.get(position).getImageUrl();
            String title = datas.get(position).getTitle();
            String watch = datas.get(position).getReplynum();
            Glide.with(mContext).load(imgUrl).signature(new IntegerVersionSignature(0)).into(holderCommon.img);
            holderCommon.title.setText(title);
            holderCommon.watch.setText(watch);
        }
        return convertView;
    }

    class ViewHolder3 {
        ImageView img;
        TextView title;
        TextView watch;
    }

    class ViewHolder {
        ImageView img;
        TextView title;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TITLE_IMG;
        }
        return COMMON_CONTENT;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public void update(List datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }
}

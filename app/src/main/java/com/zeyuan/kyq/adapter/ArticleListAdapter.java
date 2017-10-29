package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.bean.ArticleListBean;

import java.util.List;


/**
 * User: san(853013397@qq.com)
 * Date: 2015-12-15
 * Time: 11:04
 * FIXME
 */


public class ArticleListAdapter extends BaseAdapter {
    List<ArticleListBean.ArticlenumEntity> datas;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public ArticleListAdapter(Context mContext, List<ArticleListBean.ArticlenumEntity> datas) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public ArticleListBean.ArticlenumEntity getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder3 holderCommon;
        /*if (convertView == null) {
            holderCommon = new ViewHolder3();
            convertView = mLayoutInflater.inflate(R.layout.include_main_item, null);
            holderCommon.img = (ImageView) convertView.findViewById(R.id.img);
            holderCommon.title = (TextView) convertView.findViewById(R.id.title);
            holderCommon.watch = (TextView) convertView.findViewById(R.id.watch);
            convertView.setTag(holderCommon);
        } else {
            holderCommon = (ViewHolder3) convertView.getTag();
        }
        String imageurl = datas.get(position).getThumbURL();
        if(!TextUtils.isEmpty(imageurl)) {
            Glide.with(mContext).load(imageurl).signature(new IntegerVersionSignature(1)).into(holderCommon.img);//显示图片做缓存
        }else {

        }

        String title = datas.get(position).getTitle();
        if(!TextUtils.isEmpty(title)) {
            holderCommon.title.setText(title);
        }else {
            holderCommon.title.setText("");
        }
        String watch = datas.get(position).getPageViewNum();
        if(!TextUtils.isEmpty(watch)) {
            holderCommon.watch.setText(watch);
        }else {
            holderCommon.watch.setText("0");
        }*/

        return convertView;
    }

    public void update( List<ArticleListBean.ArticlenumEntity> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    class ViewHolder3 {
        ImageView img;
        TextView title;
        TextView watch;
    }
}

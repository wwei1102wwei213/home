package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.HeadLineBean;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;

import java.util.List;


/**
 * Created by Administrator on 2016/5/12.
 *
 * 肿瘤头条列表适配器
 *
 * @author wwei
 */
public class HeadLineAdapter extends BaseAdapter{

    private Context context;
    private List<HeadLineBean.HeadListBean> list;

    public HeadLineAdapter(Context context,List<HeadLineBean.HeadListBean> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position).getArticleIndex();
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(list.get(position).getArticleIndex());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        if(convertView==null){
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_list_headlist,null);
            vh.title = (TextView)convertView.findViewById(R.id.tv_title_headline_list);
            vh.pic = (ImageView)convertView.findViewById(R.id.iv_pic_headline_list);
            vh.viewnum = (TextView)convertView.findViewById(R.id.tv_viewnum_headline_list);
            vh.form = (TextView)convertView.findViewById(R.id.tv_form_headline_list);
            vh.time = (TextView)convertView.findViewById(R.id.tv_time_headline_list);
            convertView.setTag(vh);
        }
        vh = (ViewHolder)convertView.getTag();
        HeadLineBean.HeadListBean entity = list.get(position);
        String pic = entity.getThumbURL();
        if(!TextUtils.isEmpty(pic)){
            vh.pic.setVisibility(View.VISIBLE);
            Glide.with(context).load(pic).signature(new IntegerVersionSignature(1)).override(150,150).fitCenter().into(vh.pic);
        }else {
            vh.pic.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(entity.getTitle())){
            vh.title.setText(entity.getTitle());
        }
        if(!TextUtils.isEmpty(entity.getFrom())){
            vh.form.setText(entity.getFrom());
        }else{
            vh.form.setText("未知来源");
        }
        if(!TextUtils.isEmpty(entity.getPageViewNum())){
            vh.viewnum.setText(entity.getPageViewNum()+"浏览");
        }else {
            vh.viewnum.setText("0浏览");
        }
        if(!TextUtils.isEmpty(entity.getPostTime())){
            vh.time.setText(DataUtils.showFormatTime(entity.getPostTime()));
        }else{
            vh.time.setText("未知时间");
        }

        return convertView;
    }

    public void updata(List<HeadLineBean.HeadListBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView title;
        ImageView pic;
        TextView form;
        TextView viewnum;
        TextView time;
    }
}

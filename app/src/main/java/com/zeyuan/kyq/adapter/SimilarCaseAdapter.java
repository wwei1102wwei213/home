package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.SimilarCaseBean;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.widget.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/5/3.
 */
public class SimilarCaseAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    List<SimilarCaseBean.DataEntity> similars;
    Context context;

    public SimilarCaseAdapter(Context context, List<SimilarCaseBean.DataEntity> datas) {
        this.layoutInflater = LayoutInflater.from(context);
        this.similars = datas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return similars.size();
    }

    @Override
    public Object getItem(int position) {
        return similars.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_similar_case, parent, false);
            viewHolder.civ_head_img = (CircleImageView) convertView.findViewById(R.id.civ_head_img);
            viewHolder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
            //viewHolder.tv_user_tag = (TextView) convertView.findViewById(R.id.tv_user_tag);
            viewHolder.tv_cancer_type = (TextView) convertView.findViewById(R.id.tv_cancer_type);
            viewHolder.tv_steps = (TextView) convertView.findViewById(R.id.tv_steps);
            viewHolder.tv_tag = (TextView) convertView.findViewById(R.id.tv_tag);
            viewHolder.tv_focus = (TextView) convertView.findViewById(R.id.tv_focus);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SimilarCaseBean.DataEntity dataEntity = similars.get(position);
        viewHolder.tv_user_name.setText(dataEntity.getInfoName());
        Glide.with(context).load(dataEntity.getHeaderUrl()).signature(new IntegerVersionSignature(1))
                .error(R.mipmap.default_avatar)
                .into(viewHolder.civ_head_img);
        viewHolder.tv_cancer_type.setText(MapDataUtils.getCancerValues(dataEntity.getCancerID()));
        if (dataEntity.getIs_care().equals("1")){
            viewHolder.tv_focus.setSelected(true);
            viewHolder.tv_focus.setText("已关注");
        }else {
            viewHolder.tv_focus.setSelected(false);
            viewHolder.tv_focus.setText("+ 关注");
        }
        viewHolder.tv_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }

    private class ViewHolder {
        CircleImageView civ_head_img;
        TextView tv_user_name;
        TextView tv_user_tag;
        TextView tv_cancer_type;
        TextView tv_steps;
        TextView tv_tag;
        TextView tv_focus;
    }
}

package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.CityCancerForumBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/13.
 * 查询圈子适配器
 *
 * @author wwei
 */
public class SearchCircleAdapter extends BaseAdapter{


    private List<CityCancerForumBean.NumEntity> data;
    private Context context;
    private Map<String,String> map;

    public SearchCircleAdapter(Context context,List<CityCancerForumBean.NumEntity> data){
        this.context = context;
        this.data = data;
        this.map = (Map<String,String>) Factory.getData(Const.N_DataCancerValues);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CityCancerForumBean.NumEntity getItem(int position) {
        return data.get(position);
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
            convertView = View.inflate(context, R.layout.item_search_circle_list,null);
            vh.name = (TextView)convertView.findViewById(R.id.tv_search_circle_name);
            vh.cancer = (TextView)convertView.findViewById(R.id.tv_search_circle_cancer);
            convertView.setTag(vh);
        }

        vh = (ViewHolder)convertView.getTag();
        vh.name.setText(data.get(position).getCancerName());
        //保存搜索记录时，同城圈的cancerid为空，在保存字段中null无法保存，用“同城圈”来标示
        if(TextUtils.isEmpty(data.get(position).getCancerID())||"同城圈".equals(data.get(position).getCancerID())){
            vh.cancer.setText("");
        }else{
            vh.cancer.setText(map.get(data.get(position).getCancerID()));
        }
        return convertView;
    }

    public void updata(List<CityCancerForumBean.NumEntity> data){
        this.data = data;
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView name;
        TextView cancer;
    }
}

package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.ArticleListBean;

import java.util.List;

/**
 * Created by Administrator on 2016/4/8.
 * 文章搜索适配器
 *
 * @author wwei
 */
public class SearchArticleAdapter extends BaseAdapter{

    private List<ArticleListBean.ArticlenumEntity> data;
    private Context context;

    public SearchArticleAdapter(Context context , List<ArticleListBean.ArticlenumEntity> data){
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ArticleListBean.ArticlenumEntity getItem(int position) {
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
            convertView = View.inflate(context, R.layout.item_search_article_list,null);
            vh.tv = (TextView)convertView.findViewById(R.id.tv_search_article_text);
            convertView.setTag(vh);
        }
        vh = (ViewHolder)convertView.getTag();
        vh.tv.setText(data.get(position).getTitle());

        return convertView;
    }

    public void updata(List<ArticleListBean.ArticlenumEntity> data){
        this.data = data;
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView tv;
    }
}

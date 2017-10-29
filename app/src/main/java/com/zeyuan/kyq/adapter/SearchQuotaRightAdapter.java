package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.QuotaItemChildEntity;
import com.zeyuan.kyq.R;

import java.util.List;

/**
 * Created by Administrator on 2016/6/17.
 *
 *
 * @author wwei
 */
public class SearchQuotaRightAdapter extends BaseAdapter{

    private Context context;
    private List<QuotaItemChildEntity> list;

    public SearchQuotaRightAdapter(Context context,List<QuotaItemChildEntity> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public QuotaItemChildEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        if(convertView==null){
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_symptom_textview,null);
            vh.tv = (TextView)convertView.findViewById(R.id.text);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }
        String stepName = list.get(position).getSpName();
        if(TextUtils.isEmpty(stepName)){
            vh.tv.setText("");
        }else {
            vh.tv.setText(stepName);
        }

        return convertView;
    }

    public void updata(List<QuotaItemChildEntity> list){
        this.list = list;
        notifyDataSetChanged();

        /*if(list!=null&&list.size()>0){

        }*/
    }

    class ViewHolder{
        TextView tv;
    }
}

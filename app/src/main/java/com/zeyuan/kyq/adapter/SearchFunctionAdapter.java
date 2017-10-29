package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.ConfStepEntity;
import com.zeyuan.kyq.Entity.QuotaItemChildEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.Const;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/22.
 *
 *
 * @author wwei
 */
public class SearchFunctionAdapter extends BaseAdapter{

    private Context context;
    private List list;
    private int flag;


    public SearchFunctionAdapter(Context context,Object object,int flag){
        this.context = context;
        this.flag = flag;
        setData(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
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
            convertView = View.inflate(context, R.layout.item_search_systom_list,null);
            vh.tv = (TextView)convertView.findViewById(R.id.tv_search_symtom_text);
            convertView.setTag(vh);
        }
        vh = (ViewHolder)convertView.getTag();
        switch (flag){
            case Const.SEARCH_DRUG:
                vh.tv.setText(((ConfStepEntity) list.get(position)).getStepName());
                break;
            case Const.SEARCH_QUOTA:
                vh.tv.setText(((QuotaItemChildEntity) list.get(position)).getSpName());
                break;
            default:
                vh.tv.setText("");
                break;
        }

        return convertView;
    }

    private void setData(Object object){
        if(object==null){
            list = new ArrayList();
        }else {
            switch (flag){
                case Const.SEARCH_DRUG:
                    this.list = (List<ConfStepEntity>)object;
                    break;
                case Const.SEARCH_QUOTA:
                    this.list = (List<QuotaItemChildEntity>)object;
                    break;
            }
        }
    }

    public void updata(Object object,int flag){
        this.flag = flag;
        setData(object);
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView tv;
    }
}

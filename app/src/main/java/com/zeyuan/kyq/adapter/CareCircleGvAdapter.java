package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.UiUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/15.
 *
 *
 * @author wwei
 */
public class CareCircleGvAdapter extends BaseAdapter{

    private Context context;
    private List<String> list;
    private Map<String, String> circleValues;

    public CareCircleGvAdapter(Context context,List<String> list,Map<String, String> circleValues){
        this.context = context;
        this.list = list;
        this.circleValues = circleValues;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        if (convertView ==null){
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_circle_gv,null);
            vh.iv = (ImageView)convertView.findViewById(R.id.iv);
            vh.tv = (TextView)convertView.findViewById(R.id.tv);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }
        try {
            String id = list.get(position);
            vh.iv.setImageResource(UiUtils.getCancerImage(id));
            if (position==list.size()-1){
                vh.tv.setText("更多圈子");
            }else {
                vh.tv.setText(getCircleValues(id));
            }
        }catch (Exception e){

        }
        return convertView;
    }

    class ViewHolder{
        ImageView iv;
        TextView tv;
    }

    private String getCircleValues(String id) {
        if(TextUtils.isEmpty(id)) return "";
        if(circleValues == null) return "其他圈子";
        String temp = circleValues.get(id);
        if(TextUtils.isEmpty(temp)){
            return "其他圈子";
        }else{
            return temp;
        }
    }

    public void update(List<String> list){
        this.list = list;
        notifyDataSetChanged();
    }
}

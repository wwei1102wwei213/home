package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.forcallback.CircleGridCallback;

import java.util.List;

/**
 * Created by Administrator on 2016/8/4.
 */
public class CircleGridAdapter extends BaseAdapter{

    private Context context;
    private List<String> list;
    private CircleGridCallback callback;

    public CircleGridAdapter(Context context,CircleGridCallback callback,List<String> list){
        this.context = context;
        this.callback = callback;
        this.list = list;
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

        final ViewHolder vh;
        if(convertView==null){
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_gv_check_box,null);
            vh.box = (CheckBox)convertView.findViewById(R.id.box_gv);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }
        final String name = list.get(position);
        if(!TextUtils.isEmpty(name)){
            if(vh.box.isChecked()){
                vh.box.setText(name);
            }else {
                vh.box.setText(Html.fromHtml(""+name+"<font color = \"#17cbd1\" > +</font>"));
            }
            vh.box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        vh.box.setText(name);
                    }else {
                        vh.box.setText(Html.fromHtml(""+name+"<font color = \"#17cbd1\" > +</font>"));
                    }
//                    callback.setItemChange(name);
                }
            });
        }else {
            vh.box.setText("");
        }
        return convertView;
    }

    class ViewHolder{
        CheckBox box;
    }
}

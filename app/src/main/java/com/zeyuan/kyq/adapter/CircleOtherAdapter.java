package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.forcallback.AdapterCallback;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CircleFollowCheckBox;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/11.
 */
public class CircleOtherAdapter extends BaseAdapter{

    private Context context;
    private List<String> list;
    private AdapterCallback callback;
    private Map<String,String> data;
    private List<String> follows;

    public CircleOtherAdapter(Context context,List<String> list,AdapterCallback callback){
        this.context = context;
        this.list = list;
        this.follows = UserinfoData.getFocusCircle(context);
        this.callback = callback;
        data = (Map<String,String>) Factory.getData(Const.N_DataCircleValues);
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
        if(convertView == null){
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_list_circle_other,null);
            vh.iv = (ImageView)convertView.findViewById(R.id.iv_circle_item);
            vh.tv = (TextView)convertView.findViewById(R.id.tv_name_circle_item);
            vh.cb = (CircleFollowCheckBox)convertView.findViewById(R.id.isfollow);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }

        final String id = list.get(position);
        vh.iv.setImageResource(UiUtils.getCancerImage(id));
        String name = data.get(id);
        if(TextUtils.isEmpty(name)){
            vh.tv.setText("");
        }else {
            vh.tv.setText(name);
        }

        try {
            if(follows.contains(id)){
                vh.cb.setChecked(true);
            } else {
                vh.cb.setChecked(false);
            }
        }catch (Exception e){

        }

        try {
            final boolean check = vh.cb.isChecked();
            vh.cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.forAdapterCallback(0,0,id,!check,null);
                }
            });
        }catch (Exception e){

        }

        return convertView;
    }

    public void updata(){
        this.follows = UserinfoData.getFocusCircle(context);
        notifyDataSetChanged();
    }

    public void updata(List<String> list){
        this.list = list;
        updata();
    }

    class ViewHolder{
        ImageView iv;
        TextView tv;
        CircleFollowCheckBox cb;
    }

}

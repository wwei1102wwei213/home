package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.CircleClassEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.forcallback.AdapterCallback;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CircleFollowCheckBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/11.
 *
 *
 * @author wwei
 */
public class CircleCancerAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater inflater;
    private Map<String,List<String>> map;
    private Map<String,String> data;
    private Map<String,String> cancerData;
    private List<String> child;
    private List<CircleClassEntity> list;
    private AdapterCallback callback;
    private List<String> follows;

    public CircleCancerAdapter(Context context,AdapterCallback callback){
        this.context = context;
        this.callback = callback;
        this.inflater = LayoutInflater.from(context);
        this.follows = UserinfoData.getFocusCircle(context);
        init();
    }

    private void init(){
        try {
            data = (Map<String,String>) Factory.getData(Const.N_DataCircleValues);
            cancerData = (Map<String,String>) Factory.getData(Const.N_DataCancerValues);
            map = (Map<String,List<String>>) Factory.getData(Const.N_DataCircleClass);
            list = new ArrayList<>();
            CircleClassEntity entity;
            List<String> temp;
            for(String str:map.keySet()){
                temp = map.get(str);
                for(int i=0;i<temp.size();i++){
                    entity = new CircleClassEntity();
                    entity.setCircleID(temp.get(i));
                    if(i==0){
                        entity.setIsFrist(true);
                        entity.setParentID(str);
                    }else {
                        entity.setIsFrist(false);
                        entity.setParentID(str);
                    }
                    list.add(entity);
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,context,"抗癌圈适配器数据获取出错");
        }
    }

    @Override
    public int getCount() {
        if(list==null) list = new ArrayList<>();
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position).getCircleID();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

         ViewHolder vh = null;
        if (convertView == null){
            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_circle_cancer,null);
            vh.title = (TextView)convertView.findViewById(R.id.tv_item_title);
            vh.name = (TextView)convertView.findViewById(R.id.tv_name_circle_item);
            vh.iv = (ImageView)convertView.findViewById(R.id.iv_circle_item);
            vh.cb = (CircleFollowCheckBox)convertView.findViewById(R.id.isfollow);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }
        CircleClassEntity entity = list.get(position);
        if(entity.isFrist()){
            vh.title.setVisibility(View.VISIBLE);
            String text = cancerData.get(entity.getParentID());
            if (!TextUtils.isEmpty(text)){
                vh.title.setText(text);
            }else {
                vh.title.setText("");
            }
            vh.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }else {
            vh.title.setVisibility(View.GONE);
        }
        int res = UiUtils.getCancerImage(entity.getParentID());
        vh.iv.setImageResource(res);
        final String id = entity.getCircleID();
        String name = data.get(id);
        if (!TextUtils.isEmpty(name)){
            vh.name.setText(name);
        }else {
            vh.name.setText("");
        }
        if(follows.contains(id)){
            vh.cb.setChecked(true);
        }else {
            vh.cb.setChecked(false);
        }
        final boolean check = vh.cb.isChecked();
        vh.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.forAdapterCallback(position,0,id,!check,null);
            }
        });
        return convertView;
    }

    public void updata(){
        this.follows = UserinfoData.getFocusCircle(context);
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView title;
        TextView name;
        ImageView iv;
        CircleFollowCheckBox cb;
    }
}

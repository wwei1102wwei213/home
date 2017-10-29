package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.SameEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.widget.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 *
 *
 *
 * @author wwei
 */
public class SymptomCardAdapter extends BaseAdapter{

    private Context context;
    private List<List<SameEntity>> list;
    private SymptomCareCallBack callback;

    public SymptomCardAdapter(Context context,List<List<SameEntity>> list,SymptomCareCallBack callback){
        this.context = context;
        this.callback = callback;
        this.list = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            this.list.add(list.get(i));
        }
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

        try {
            ViewHolder vh = null;
            if (convertView==null){
                //此处convertView获取对象的方式需要注意
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.card_touch_fly_item, parent, false);
                vh = new ViewHolder();
                vh.refresh = convertView.findViewById(R.id.refresh);
                vh.flow = (FlowLayout)convertView.findViewById(R.id.fl_symptom);
                convertView.setTag(vh);
            }
            vh = (ViewHolder)convertView.getTag();
            List<SameEntity> card = list.get(position);
            vh.flow.removeAllViews();
            if (card!=null&&card.size()>0){
                for (final SameEntity entity:card){
                    TextView tv = (TextView)View.inflate(context,R.layout.item_symptom_flowlayout,null);
                    tv.setText(TextUtils.isEmpty(entity.getName())?"":entity.getName());
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callback.OnItemClickCall(entity);
                        }
                    });
                    vh.flow.addView(tv);
                }
            }
            vh.refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.OnRefreshClickCall();
                }
            });
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"getView");
        }
        return convertView;
    }

    public void update(){
        if (list.size()>0){
            list.remove(0);
            notifyDataSetChanged();
        }
        LogCustom.i("ZYS","数据源长度："+list.size());
    }

    public void update(List<List<SameEntity>> list){
        this.list = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            this.list.add(list.get(i));
        }
        notifyDataSetChanged();
        LogCustom.i("ZYS","数据源长度："+list.size());
    }

    class ViewHolder{
        View refresh;
        FlowLayout flow;
    }

    public interface SymptomCareCallBack{
        void OnItemClickCall(SameEntity entity);
        void OnRefreshClickCall();
    }

}

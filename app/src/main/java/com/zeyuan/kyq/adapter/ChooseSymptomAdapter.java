package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.SameEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;

import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/27.
 *
 * @author wwei
 */
public class ChooseSymptomAdapter extends BaseAdapter{

    private Context context;
    private List<SameEntity> list;
    private List<String> check;
    private Map<String, String> performValues;
    private onChooseChangeListener callback;
    private int index;

    public ChooseSymptomAdapter(Context context,List<String> data,List<String> check,onChooseChangeListener callback,int index){
        this.context = context;
        if (check==null) check = new ArrayList<>();
        this.callback = callback;
        this.check = check;
        this.index = index;
        this.performValues = (Map<String, String>) Factory.getData(Const.N_DataPerformValues);
        if (data==null||data.size()==0) {
            list = new ArrayList<>();
        }else {
            list = new ArrayList<>();
            SameEntity entity;
            for (String str:data){
                entity = new SameEntity();
                entity.setId(str);
                entity.setName(performValues.get(str));
                list.add(entity);
            }
            Collections.sort(list, new Comparator<SameEntity>() {
                Collator collator = Collator.getInstance(Locale.CHINA);
                @Override
                public int compare(SameEntity o1, SameEntity o2) {
                    CollationKey key1 = collator.getCollationKey(o1.getName());
                    CollationKey key2 = collator.getCollationKey(o2.getName());
                    return key1.compareTo(key2);
                }
            });
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

        ViewHolder vh;
        if (convertView==null){
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_choose_symptom_lv,null);
            vh.iv = (ImageView)convertView.findViewById(R.id.iv);
            vh.tv = (TextView)convertView.findViewById(R.id.tv);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }
        SameEntity entity = list.get(position);
        vh.tv.setText(TextUtils.isEmpty(entity.getName())?"":entity.getName());
        if (check.contains(entity.getId())){
            vh.iv.setVisibility(View.VISIBLE);
            vh.tv.setSelected(true);
        }else {
            vh.iv.setVisibility(View.INVISIBLE);
            vh.tv.setSelected(false);
        }

        return convertView;
    }

    public void update(List<String> data,int index) {
        this.index = index;
        int num = 0;
        if (data==null||data.size()==0) {
            list = new ArrayList<>();
        }else {
            list = new ArrayList<>();
            SameEntity entity;
            for (String str:data){
                entity = new SameEntity();
                entity.setId(str);
                entity.setName(performValues.get(str));
                list.add(entity);
//                if (check.contains(str)) num++;
            }
            Collections.sort(list, new Comparator<SameEntity>() {
                Collator collator = Collator.getInstance(Locale.CHINA);
                @Override
                public int compare(SameEntity o1, SameEntity o2) {
                    CollationKey key1 = collator.getCollationKey(o1.getName());
                    CollationKey key2 = collator.getCollationKey(o2.getName());
                    return key1.compareTo(key2);
                }
            });
        }
        this.notifyDataSetChanged();
    }

    public void click(int position){
        String id  = list.get(position).getId();
        if (check.contains(id)){
            check.remove(id);
        }else {
            check.add(id);
        }
        notifyDataSetChanged();
        callback.onChooseChanged(index,check);
    }

    class ViewHolder{
        TextView tv;
        ImageView iv;
    }

    public interface onChooseChangeListener{
        void onChooseChanged(int index,List<String> check);
    }

}

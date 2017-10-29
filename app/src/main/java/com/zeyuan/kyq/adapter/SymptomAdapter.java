package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
 * Created by Administrator on 2015/9/29.
 */
public class SymptomAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;

    private Map<String, String> performValues;
    private List<SameEntity> list;

    public SymptomAdapter(Context context, List<String> data) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
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
    public String getItem(int position) {
        return list.get(position).getId();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_symptom_textview, null);
            viewHolder = new ViewHolder();
            viewHolder.simptom = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String string =list.get(position).getName();
        viewHolder.simptom.setText(string);
        return convertView;
    }

    class ViewHolder {
        TextView simptom;
    }

    public void update(List<String> data) {
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
        this.notifyDataSetChanged();
    }
}

package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.PhoneCountryEntity;
import com.zeyuan.kyq.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huyongbiao on 2017/6/26.
 */

public class PhoneCountryAdapter extends ArrayAdapter<PhoneCountryEntity> implements SectionIndexer {
    List<String> list;
    private List<PhoneCountryEntity> phoneCountryEntities;
    private LayoutInflater inflater;
    private SparseIntArray positionOfSection;
    private SparseIntArray sectionOfPosition;

    public PhoneCountryAdapter(@NonNull Context context, @LayoutRes int resource, List<PhoneCountryEntity> phoneCountryEntities) {
        super(context, resource, phoneCountryEntities);
        this.inflater = LayoutInflater.from(context);
        this.phoneCountryEntities = phoneCountryEntities;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_phone_country, parent, false);
            holder.header = (TextView) convertView.findViewById(R.id.header);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_code = (TextView) convertView.findViewById(R.id.tv_code);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PhoneCountryEntity phoneCountryEntity = getItem(position);
        if (phoneCountryEntity == null)
            return convertView;
        String countryName = phoneCountryEntity.getName();
        String header = phoneCountryEntity.getInitialLetter();
        if (position == 0 || header != null && !header.equals(getItem(position - 1).getInitialLetter())) {
            if (TextUtils.isEmpty(header)) {
                holder.header.setVisibility(View.GONE);
            } else {
                holder.header.setVisibility(View.VISIBLE);
                if (header.equals("#")) {
                    holder.header.setText("常用");
                } else {
                    holder.header.setText(header);
                }

            }
        } else {
            holder.header.setVisibility(View.GONE);
        }
        holder.tv_name.setText(countryName);
        holder.tv_code.setText("+" + phoneCountryEntity.getCode() + "");
        return convertView;
    }

    @Override
    public int getPositionForSection(int section) {
        return positionOfSection.get(section);
    }

    @Override
    public int getSectionForPosition(int position) {
        return sectionOfPosition.get(position);
    }

    @Override
    public Object[] getSections() {
        positionOfSection = new SparseIntArray();
        sectionOfPosition = new SparseIntArray();
        int count = getCount();
        list = new ArrayList<>();
        list.add(getContext().getString(R.string.search_header));
        positionOfSection.put(0, 0);
        sectionOfPosition.put(0, 0);
        for (int i = 0; i < count; i++) {

            String letter = getItem(i).getInitialLetter();
            int section = list.size() - 1;
            if (list.get(section) != null && !list.get(section).equals(letter)) {
                list.add(letter);
                section++;
                positionOfSection.put(section, i);
            }
            sectionOfPosition.put(i, section);
        }
        return list.toArray(new String[list.size()]);
    }

    private static class ViewHolder {
        TextView header;//每组用户开头的英文字母
        TextView tv_name;
        TextView tv_code;
    }
}

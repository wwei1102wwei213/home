package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.Shortcut;
import com.zeyuan.kyq.R;

import java.util.List;

/**
 * Created by Administrator on 2017/5/10.
 * 圈子快速入口适配器
 */

public class TasteGvAdapter extends BaseAdapter {
    private Context context;
    private List<Shortcut> shortcuts;
    private int count;

    public TasteGvAdapter(Context context, List<Shortcut> shortcuts) {
        this.context = context;
        this.shortcuts = shortcuts;
        if (shortcuts.size() > 8)
            count = 8;
        else
            count = shortcuts.size();
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return shortcuts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_taste, parent, false);
            vh.iv_tag = (ImageView) convertView.findViewById(R.id.iv_tag);
            vh.tv_taste_name = (TextView) convertView.findViewById(R.id.tv_taste_name);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        try {
            Shortcut shortcut = shortcuts.get(position);
            if (shortcut.getTag() == 0) {
                vh.iv_tag.setVisibility(View.INVISIBLE);
            } else if (shortcut.getTag() == 1) {
                vh.iv_tag.setVisibility(View.VISIBLE);
                vh.iv_tag.setImageResource(R.mipmap.ic_hot);
            } else if (shortcut.getTag() == 2) {
                vh.iv_tag.setVisibility(View.VISIBLE);
                vh.iv_tag.setImageResource(R.mipmap.ic_new);
            }
            vh.tv_taste_name.setText(shortcut.getName());

        } catch (Exception e) {

        }
        return convertView;
    }

    public void showMore(boolean isShowMore) {
        if (isShowMore) {
            if (shortcuts.size() > 8) {
                count = shortcuts.size();
                super.notifyDataSetChanged();
            }
        } else {
            if (shortcuts.size() > 8) {
                count = 8;
                super.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void notifyDataSetChanged() {
        if (shortcuts.size() > 8)
            count = 8;
        else
            count = shortcuts.size();
        super.notifyDataSetChanged();
    }

    class ViewHolder {
        TextView tv_taste_name;
        ImageView iv_tag;
    }
}

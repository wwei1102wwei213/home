package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zeyuan.kyq.R;

/**
 * Created by Administrator on 2015/9/16.
 */
public class TopForumAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    public TopForumAdapter(Context context) {
        this.context = context;
        inflater= LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_top_forum, null);
        return convertView;
    }
}
